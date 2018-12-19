package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.dbindex.DBIndexImpl;
import edu.scripps.yates.excel.proteindb.importcfg.RemoteFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.parsers.idparser.IdentificationsParser;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.factories.OrganismEx;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinsAdapterByRemoteFiles implements edu.scripps.yates.utilities.pattern.Adapter<Map<String, Protein>> {
	private final static Logger log = Logger.getLogger(ProteinsAdapterByRemoteFiles.class);
	private final Condition condition;
	private final RemoteFileReader remoteFileReader;
	private final RemoteInfoType remoteInfoCfg;
	private final Set<MSRun> msruns = new THashSet<MSRun>();
	private final OrganismSetType organismSet;
	// we assign this first msRun to all psms
	private MSRun msrunForPSMs;

	public ProteinsAdapterByRemoteFiles(RemoteInfoType remoteInfoCfg, RemoteFileReader remoteFileReader,
			Condition expCondition, List<MsRunType> msRunCfgd, OrganismSetType organismSet) {
		this.remoteInfoCfg = remoteInfoCfg;
		condition = expCondition;
		this.remoteFileReader = remoteFileReader;
		for (final MsRunType msRunType : msRunCfgd) {

			final MSRun msRun = new MSRunAdapter(msRunType).adapt();
			msruns.add(msRun);
			if (msrunForPSMs == null) {
				msrunForPSMs = msRun;
			}
		}
		this.organismSet = organismSet;
	}

	@Override
	public Map<String, Protein> adapt() {
		return getProteinsFromRemoteFileReader();
	}

	private Map<String, Protein> getProteinsFromRemoteFileReader() {
		Map<String, Protein> retMap = null;

		final List<FileReferenceType> fileRefs = remoteInfoCfg.getFileRef();
		final Set<String> fileRefSet = new THashSet<String>();
		DBIndexImpl fastaDBIndex = null;
		for (final FileReferenceType fileReference : fileRefs) {
			// try to get it as a FASTA file.
			if (fastaDBIndex == null)
				fastaDBIndex = remoteFileReader.getFastaDBIndex(fileReference.getFileRef());

			final String fileRef = fileReference.getFileRef();
			log.info("Reading identification data from remote files at: " + fileRef);
			fileRefSet.add(fileRef);
		}
		try {

			retMap = createProteinsFromRemoteFiles(fileRefSet, fastaDBIndex);

		} catch (final IOException e) {
			e.printStackTrace();
		}

		return retMap;
	}

	private Map<String, Protein> createProteinsFromRemoteFiles(Set<String> fileRefSet, DBIndexImpl fastaDBIndex)
			throws IOException {
		final Map<String, Protein> retMap = new THashMap<String, Protein>();
		String fileRefString = "";

		for (final String fileReference : fileRefSet) {
			if (!"".equals(fileRefString)) {
				fileRefString += ",";
			}
			fileRefString += fileReference;
		}

		// set dbindex and decoy pattern
		final List<IdentificationsParser> identificationsParsers = remoteFileReader
				.getIdentificationsParsers(fileRefSet);
		if (identificationsParsers != null) {
			for (final IdentificationsParser identificationsParser : identificationsParsers) {

				if (remoteInfoCfg.getDiscardDecoys() != null) {
					identificationsParser.setDecoyPattern(remoteInfoCfg.getDiscardDecoys());
				}

				if (fastaDBIndex != null) {
					identificationsParser.setDbIndex(fastaDBIndex);
				}
				// throw an exception if t 0 parameter is not stated
				final String parameterValue = identificationsParser.getCommandLineParameter().getParameterValue("t");
				if (!ImportCfgFileReader.ignoreDTASelectParameterT
						&& (parameterValue == null || parameterValue.equals("0"))) {
					throw new IllegalArgumentException(
							"For a comprenhensive aggregation of the data, PINT enforces to use \"t 0\" parameter, which means:"
									+ " \"-t 0    Show all spectra for each sequence\""
									+ "\nIn order to ignore it, set ImportCfgFileReader.ignoreDTASelectParameterT = true;");
				}
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
			loadUniprotAnnotations(identificationsParsers, quantParser);
		} else {
			log.info("Ignoring Uniprot annotations");
		}

		if (identificationsParsers != null) {
			for (final IdentificationsParser identificationsParser : identificationsParsers) {

				final Collection<Protein> proteins = identificationsParser.getProteinMap().values();
				for (Protein protein : proteins) {
					// change on Sept 20, 2017
					// because getMSRunID in dtaselectpsms is different than the
					// msrun.getid, and so, they were not added.
					// Protein protein = new
					// ProteinImplFromDTASelect(dtaSelectProtein, msrun, false);
					final String accession = protein.getAccession();

					// look in the static model storage first

					if (StaticProteomicsModelStorage.containsProtein(msruns, condition.getName(), accession)) {
						final Protein proteinFromStaticStorage = StaticProteomicsModelStorage
								.getProtein(condition.getName(), accession, msruns).iterator().next();
						proteinFromStaticStorage.mergeWithProtein(protein);
						protein = proteinFromStaticStorage;
					}

					StaticProteomicsModelStorage.addProtein(protein, msruns, condition.getName());
					if (protein.getOrganism() == null) {
						protein.setOrganism(getOrganismFromConfFile());
					}
					for (final MSRun msRun : msruns) {
						protein.addMSRun(msRun);
					}

					if (condition == null) {
						log.info("condition is null");
					}
					protein.addCondition(condition);
					// set condition to amounts
					if (protein.getAmounts() != null) {
						for (final Amount amount : protein.getAmounts()) {
							amount.setCondition(condition);
						}
					}

					retMap.put(protein.getAccession(), protein);
					final List<PSM> psMs = protein.getPSMs();
					if (psMs != null) {
						for (final PSM psm : psMs) {
							StaticProteomicsModelStorage.addPSM(psm, msrunForPSMs, condition.getName());
							psm.addCondition(condition);
						}
					}
					final Set<Peptide> peptides = protein.getPeptides();
					if (peptides != null) {
						for (final Peptide peptide : peptides) {
							StaticProteomicsModelStorage.addPeptide(peptide, msruns, condition.getName());
							peptide.addCondition(condition);
						}
					}
				}
			}
		} else if (quantParser != null) {
			final Map<String, QuantifiedProteinInterface> proteinMap = quantParser.getProteinMap();
			for (Protein protein : proteinMap.values()) {
				for (final MSRun msrun : msruns) {
					protein.addMSRun(msrun);
				}
				protein.addCondition(condition);
				if (StaticProteomicsModelStorage.containsProtein(msruns, condition.getName(), protein.getAccession())) {
					protein = StaticProteomicsModelStorage
							.getProtein(condition.getName(), protein.getAccession(), msruns).iterator().next();
				}
				StaticProteomicsModelStorage.addProtein(protein, msruns, condition.getName());
				retMap.put(protein.getAccession(), protein);
				final List<PSM> psMs = protein.getPSMs();
				if (psMs != null) {
					for (final PSM psm : psMs) {
						StaticProteomicsModelStorage.addPSM(psm, msrunForPSMs, condition.getName());
						psm.addCondition(condition);
					}
				}
				final Set<Peptide> peptides = protein.getPeptides();
				if (peptides != null) {
					for (final Peptide peptide : peptides) {
						StaticProteomicsModelStorage.addPeptide(peptide, msruns, condition.getName());
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

	private void loadUniprotAnnotations(List<IdentificationsParser> identificationsParsers, QuantParser quantParser)
			throws IOException {
		final Set<String> accessions = getUniprotAccs(identificationsParsers, quantParser);
		log.info("Getting annotations from " + accessions.size() + " proteins");
		final UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		upr.getAnnotatedProteins(accessions);
	}

	private Set<String> getUniprotAccs(List<IdentificationsParser> identificationsParsers, QuantParser quantParser)
			throws IOException {
		final Set<String> accessions = new THashSet<String>();
		if (identificationsParsers != null) {
			for (final IdentificationsParser identificationsParser : identificationsParsers) {

				try {
					final Set<String> locuses = identificationsParser.getProteinMap().keySet();
					for (final String locus : locuses) {

						final Accession acc = FastaParser.getACC(locus);
						final Set<String> uniProtAccs = new THashSet<String>();
						if (acc.getAccessionType() == AccessionType.UNIPROT) {
							uniProtAccs.add(acc.getAccession());
						} else if (acc.getAccessionType() == AccessionType.IPI) {
							final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance()
									.map2Uniprot(acc.getAccession());
							for (final UniprotEntry uniprotEntry : map2Uniprot) {
								uniProtAccs.add(uniprotEntry.getAcc());
							}
						}
						if (!uniProtAccs.isEmpty()) {
							accessions.addAll(uniProtAccs);
						}
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (quantParser != null) {
			final Set<String> locuses = quantParser.getProteinMap().keySet();
			for (final String locus : locuses) {
				final String uniProtACC = FastaParser.getUniProtACC(locus);
				if (uniProtACC != null && FastaParser.isUniProtACC(uniProtACC))
					accessions.add(uniProtACC);
			}
		}
		return accessions;
	}
}
