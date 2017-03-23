package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.censuschro.ProteinImplFromQuantifiedProtein;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.dtaselect.ProteinImplFromDTASelect;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.dtaselectparser.util.DTASelectProtein;
import edu.scripps.yates.excel.proteindb.importcfg.RemoteFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.model.factories.OrganismEx;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.util.Pair;

public class ProteinsAdapterByRemoteFiles implements edu.scripps.yates.utilities.pattern.Adapter<Map<String, Protein>> {
	private final static Logger log = Logger.getLogger(ProteinsAdapterByRemoteFiles.class);
	private final Condition condition;
	private final RemoteFileReader remoteFileReader;
	private final RemoteInfoType remoteInfoCfg;
	private final MSRun msrun;
	private final OrganismSetType organismSet;

	public ProteinsAdapterByRemoteFiles(RemoteInfoType remoteInfoCfg, RemoteFileReader remoteFileReader,
			Condition expCondition, MsRunType msRunCfg, OrganismSetType organismSet) {
		this.remoteInfoCfg = remoteInfoCfg;
		condition = expCondition;
		this.remoteFileReader = remoteFileReader;
		msrun = new MSRunAdapter(msRunCfg).adapt();
		this.organismSet = organismSet;
	}

	@Override
	public Map<String, Protein> adapt() {
		return getProteinsFromRemoteFileReader();
	}

	private Map<String, Protein> getProteinsFromRemoteFileReader() {
		Map<String, Protein> retMap = null;

		final List<FileReferenceType> fileRefs = remoteInfoCfg.getFileRef();
		Set<String> fileRefSet = new HashSet<String>();
		DBIndexInterface fastaDBIndex = null;
		for (FileReferenceType fileReference : fileRefs) {
			// try to get it as a FASTA file.
			if (fastaDBIndex == null)
				fastaDBIndex = remoteFileReader.getFastaDBIndex(fileReference.getFileRef());

			final String fileRef = fileReference.getFileRef();
			log.info("Reading identification data from remote files at: " + fileRef);
			fileRefSet.add(fileRef);
		}
		try {

			retMap = createProteinsFromRemoteFiles(fileRefSet, fastaDBIndex);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return retMap;
	}

	private Map<String, Protein> createProteinsFromRemoteFiles(Set<String> fileRefSet, DBIndexInterface fastaDBIndex)
			throws IOException {
		Map<String, Protein> retMap = new HashMap<String, Protein>();
		String fileRefString = "";

		for (String fileReference : fileRefSet) {
			if (!"".equals(fileRefString)) {
				fileRefString += ",";
			}
			fileRefString += fileReference;
		}

		// DTA Select PArser
		final DTASelectParser dtaSelectFilterParser = remoteFileReader.getDTASelectFilterParser(fileRefSet);
		if (dtaSelectFilterParser != null) {
			if (remoteInfoCfg.getDiscardDecoys() != null) {
				dtaSelectFilterParser.setDecoyPattern(remoteInfoCfg.getDiscardDecoys());
			}

			if (fastaDBIndex != null) {
				dtaSelectFilterParser.setDbIndex(fastaDBIndex);
			}
			// throw an exception if t 0 parameter is not stated
			final String parameterValue = dtaSelectFilterParser.getCommandLineParameter().getParameterValue("t");
			if (!ImportCfgFileReader.ignoreDTASelectParameterT
					&& (parameterValue == null || parameterValue.equals("0"))) {
				throw new IllegalArgumentException(
						"For a comprenhensive aggregation of the data, PINT enforces to use \"t 0\" parameter, which means:"
								+ " \"-t 0    Show all spectra for each sequence\""
								+ "\nIn order to ignore it, set ImportCfgFileReader.ignoreDTASelectParameterT = true;");
			}
		}
		// Census CHRO parser
		QuantParser quantParser = remoteFileReader.getCensusChroParser(fileRefSet);
		// if null, get Census OUT parser
		if (quantParser == null) {
			quantParser = remoteFileReader.getCensusOutParser(fileRefSet);
		}
		if (quantParser != null) {
			if (remoteInfoCfg.getDiscardDecoys() != null)
				quantParser.setDecoyPattern(remoteInfoCfg.getDiscardDecoys());
			if (fastaDBIndex != null)
				quantParser.setDbIndex(fastaDBIndex);

		}
		// retrieve accessions from uniprot first in order to retrieve all at
		// once
		if (!ImportCfgFileReader.ignoreUniprotAnnotations) {
			loadUniprotAnnotations(dtaSelectFilterParser, quantParser);
		} else {
			log.info("Ignoring Uniprot annotations");
		}

		if (dtaSelectFilterParser != null) {
			final Collection<DTASelectProtein> dtaSelectProteins = dtaSelectFilterParser.getDTASelectProteins()
					.values();
			for (DTASelectProtein dtaSelectProtein : dtaSelectProteins) {
				Protein protein = new ProteinImplFromDTASelect(dtaSelectProtein, msrun, false);
				if (protein.getOrganism() == null) {
					((ProteinImplFromDTASelect) protein).setOrganism(getOrganismFromConfFile());
				}
				// look in the static model storage first
				if (StaticProteomicsModelStorage.containsProtein(msrun.getRunId(), condition.getName(),
						protein.getAccession())) {
					protein = StaticProteomicsModelStorage
							.getProtein(msrun.getRunId(), condition.getName(), protein.getAccession()).iterator()
							.next();
				}

				StaticProteomicsModelStorage.addProtein(protein, msrun.getRunId(), condition.getName());
				protein.setMSRun(msrun);
				protein.addCondition(condition);
				retMap.put(protein.getAccession(), protein);
				final Set<PSM> psMs = protein.getPSMs();
				if (psMs != null) {
					for (PSM psm : psMs) {
						StaticProteomicsModelStorage.addPSM(psm, msrun.getRunId(), condition.getName());
						psm.addCondition(condition);
					}
				}
				final Set<Peptide> peptides = protein.getPeptides();
				if (peptides != null) {
					for (Peptide peptide : peptides) {
						StaticProteomicsModelStorage.addPeptide(peptide, msrun.getRunId(), condition.getName());
						peptide.addCondition(condition);
					}
				}
			}
		} else if (quantParser != null) {
			final Map<String, QuantifiedProteinInterface> proteinMap = quantParser.getProteinMap();
			for (QuantifiedProteinInterface quantProtein : proteinMap.values()) {
				Protein protein = new ProteinImplFromQuantifiedProtein(quantProtein, msrun, condition);
				if (StaticProteomicsModelStorage.containsProtein(msrun.getRunId(), condition.getName(),
						protein.getAccession())) {
					protein = StaticProteomicsModelStorage
							.getProtein(msrun.getRunId(), condition.getName(), protein.getAccession()).iterator()
							.next();
				}
				StaticProteomicsModelStorage.addProtein(protein, msrun, condition.getName());
				retMap.put(protein.getAccession(), protein);
				final Set<PSM> psMs = protein.getPSMs();
				if (psMs != null) {
					for (PSM psm : psMs) {
						StaticProteomicsModelStorage.addPSM(psm, msrun, condition.getName());
						psm.addCondition(condition);
					}
				}
				final Set<Peptide> peptides = protein.getPeptides();
				if (peptides != null) {
					for (Peptide peptide : peptides) {
						StaticProteomicsModelStorage.addPeptide(peptide, msrun, condition.getName());
						peptide.addCondition(condition);
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Remote file '" + fileRefString
					+ "' is not reachable.\nTry it again or review the connection settings to server");
		}
		return retMap;
	}

	private Organism getOrganismFromConfFile() {
		if (organismSet != null) {
			final IdDescriptionType organismType = organismSet.getOrganism().get(0);
			return new OrganismAdapter(organismType).adapt();
		}

		final OrganismEx organismEx = new OrganismEx("0000");
		organismEx.setName("Unknown");
		return organismEx;
	}

	private void loadUniprotAnnotations(DTASelectParser dtaSelectFilterParser, QuantParser quantParser) {
		Set<String> accessions = getUniprotAccs(dtaSelectFilterParser, quantParser);
		log.info("Getting annotations from " + accessions.size() + " proteins");
		UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		upr.getAnnotatedProteins(accessions);
	}

	private Set<String> getUniprotAccs(DTASelectParser dtaSelectFilterParser, QuantParser quantParser) {
		Set<String> accessions = new HashSet<String>();
		if (dtaSelectFilterParser != null) {
			try {
				final Set<String> locuses = dtaSelectFilterParser.getDTASelectProteins().keySet();
				for (String locus : locuses) {

					final Pair<String, String> acc = FastaParser.getACC(locus);
					Set<String> uniProtAccs = new HashSet<String>();
					if (acc.getSecondElement().equals("UNIPROT")) {
						uniProtAccs.add(acc.getFirstelement());
					} else if (acc.getSecondElement().equals("IPI")) {
						final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance()
								.map2Uniprot(acc.getFirstelement());
						for (UniprotEntry uniprotEntry : map2Uniprot) {
							uniProtAccs.add(uniprotEntry.getAcc());
						}
					}
					if (!uniProtAccs.isEmpty()) {
						accessions.addAll(uniProtAccs);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (quantParser != null) {
			final Set<String> locuses = quantParser.getProteinMap().keySet();
			for (String locus : locuses) {
				final String uniProtACC = FastaParser.getUniProtACC(locus);
				if (uniProtACC != null && FastaParser.isUniProtACC(uniProtACC))
					accessions.add(uniProtACC);
			}
		}
		return accessions;
	}
}
