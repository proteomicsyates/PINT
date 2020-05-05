package edu.scripps.yates.persistence.mysql.projects;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.excel.proteindb.importcfg.RemoteFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class Fmr_silam {
	@Before
	public void setAnnotationsIndex() {
		UniprotProteinRetrievalSettings.getInstance(new File("C:\\Users\\Salva\\Desktop\\tmp\\PInt\\uniprot"), true);
		ImportCfgFileReader.ignoreDTASelectParameterT = true;
		// ImportCfgFileReader.ignoreUniprotAnnotations = true;
	}

	@Test
	public void fmrSilam_Project_save() {
		ImportCfgFileReader.ALLOW_PROTEINS_IN_EXCEL_NOT_FOUND_BEFORE = true;
		final String cfgFilePath = "C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Silam\\fmr_silam - forTest.xml";
		final ImportCfgFileReader importReader = new ImportCfgFileReader(true, true);
		final Project project = importReader.getProjectFromCfgFile(new File(cfgFilePath), null);
		ContextualSessionHandler.beginGoodTransaction();
		new MySQLSaver().saveProject(project);

		ContextualSessionHandler.finishGoodTransaction();
		System.out.println("Everything is OK!");
	}

	@Test
	public void fmrSilam_Project() {
		ImportCfgFileReader.ignoreUniprotAnnotations = true;
		final String cfgFilePath = "C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Silam\\fmr_silam - forTest.xml";
		final ImportCfgFileReader importReader = new ImportCfgFileReader(true, true);
		final Project project = importReader.getProjectFromCfgFile(new File(cfgFilePath), null);
		final Set<Condition> conditions = project.getConditions();
		final Set<Protein> ret = new THashSet<Protein>();
		for (final Condition condition : conditions) {
			final Set<Protein> proteins = condition.getProteins();
			for (final Protein protein : proteins) {
				final Set<Gene> genes = protein.getGenes();
				for (final Gene gene : genes) {
					if (gene.getGeneID().equals("Cyb5b")) {
						if (ret.contains(protein)) {
							System.out.println("Ya esta");
						} else {
							ret.add(protein);
						}
						break;
					}
				}
				if (protein.getPrimaryAccession().getAccession().startsWith("Rev")
						|| protein.getPrimaryAccession().getAccession().startsWith("con")) {
					System.out.println(
							protein.getPrimaryAccession().getAccession() + "\t" + protein.getOrganism().getName());
				}
			}
		}

		System.out.println(ret.size() + " proteins");
		for (final Protein protein : ret) {
			final StringBuilder sb = new StringBuilder();
			final Set<Condition> conditions2 = protein.getConditions();
			for (final Condition condition : conditions2) {
				sb.append(condition.getName() + " ");
			}

			System.out.println(protein.getPrimaryAccession().getAccession() + " detected in conditions: "
					+ sb.toString() + " and run: " + protein.getMSRuns().iterator().next().getRunId());
			final Set<Ratio> ratios = protein.getRatios();
			for (final Ratio ratio : ratios) {
				System.out.println(ratio.getCondition1().getName() + " vs " + ratio.getCondition2().getName() + "="
						+ ratio.getValue());
			}
		}
	}

	@Test
	public void IPI2Uniprot_fmrSilam_Project() throws JAXBException, IOException {
		ImportCfgFileReader.ignoreUniprotAnnotations = true;
		final String cfgFilePath = "C:\\Users\\Salva\\Desktop\\tmp\\PInt\\xml\\fmr_silam.xml";

		final JAXBContext jaxbContext = JAXBContext.newInstance("edu.scripps.yates.excel.proteindb.importcfg.jaxb");
		final File xmlFile = new File(cfgFilePath);
		if (xmlFile.exists() && jaxbContext != null) {
			final Map<String, Set<String>> map = new THashMap<String, Set<String>>();
			final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			final PintImportCfg pintImportCfg = (PintImportCfg) unmarshaller.unmarshal(xmlFile);
			final FileSetType fileSet = pintImportCfg.getFileSet();

			// TODO ADD ELEMENTS TO THIS!!!
			final Map<String, Map<QuantCondition, QuantificationLabel>> labelsByConditions = null;

			final RemoteFileReader reader = new RemoteFileReader(fileSet, pintImportCfg.getServers(), null,
					labelsByConditions, null);
			for (final FileType fileType : fileSet.getFile()) {
				if (fileType.getFormat() == FormatType.DTA_SELECT_FILTER_TXT) {
					final DTASelectParser dtaSelectFilterParser = reader.getDTASelectFilterParser(fileType.getId());
					if (dtaSelectFilterParser == null || dtaSelectFilterParser.getProteinMap() == null) {
						System.out.println(dtaSelectFilterParser);
					}
					for (final Protein protein : dtaSelectFilterParser.getProteinMap().values()) {
						final String locus = protein.getAccession();
						final String IPI = FastaParser.getIPIACC(locus);
						final String uniprotAcc = FastaParser.getUniProtACC(locus);
						if (uniprotAcc != null && IPI != null) {
							if (map.containsKey(uniprotAcc)) {
								map.get(uniprotAcc).add(IPI);
							} else {
								final Set<String> set = new THashSet<String>();
								set.add(IPI);
								map.put(uniprotAcc, set);
							}
						}
					}
				}
			}
			for (final String uniprotAcc : map.keySet()) {
				final Set<String> set = map.get(uniprotAcc);
				for (final String ipi : set) {
					System.out.println(uniprotAcc + "\t" + ipi);
				}
			}
		}
	}
}
