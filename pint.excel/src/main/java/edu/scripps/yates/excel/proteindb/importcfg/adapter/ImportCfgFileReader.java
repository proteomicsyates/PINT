package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.CensusRatio;
import edu.scripps.yates.census.read.model.Ion;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedProtein;
import edu.scripps.yates.census.read.model.QuantAmount;
import edu.scripps.yates.census.read.model.RatioDescriptor;
import edu.scripps.yates.census.read.model.StaticQuantMaps;
import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.RemoteFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PeptideRatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfgType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAccessionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinRatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PsmRatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PsmType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SequenceType;
import edu.scripps.yates.excel.util.ExcelUtils;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.maths.Maths;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.model.factories.ProjectEx;
import edu.scripps.yates.utilities.model.factories.RatioEx;
import edu.scripps.yates.utilities.model.factories.ScoreEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.HasAmounts;
import edu.scripps.yates.utilities.proteomicsmodel.HasRatios;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import edu.scripps.yates.utilities.util.Pair;

public class ImportCfgFileReader {
	private static final Logger log = Logger.getLogger(ImportCfgFileReader.class);
	private static JAXBContext jaxbContext;
	public static boolean ignoreDTASelectParameterT = false;
	public static boolean ignoreUniprotAnnotations = false;
	private ProjectType projectCfg;
	private ExcelFileReader excelReader;
	private HashMap<String, Condition> conditionsByConditionID;
	private RemoteFileReader remoteFileReader;
	public static final String SINGLETON_SUM_COUNT_RATIO = "Singleton counts ratio";
	public static final String SINGLETON_SUM_INTENSITIES_RATIO = "Singleton sum intensities ratio";
	public static final String SINGLETON_INTENSITY_RATIO = "Singleton intensity ratio";
	private final Map<String, Set<Protein>> proteinMap = new HashMap<String, Set<Protein>>();
	private Pattern discardDecoyRegexp;
	public static boolean ALLOW_PROTEINS_IN_EXCEL_NOT_FOUND_BEFORE = false;

	public ImportCfgFileReader() {
		try {
			jaxbContext = JAXBContext.newInstance("edu.scripps.yates.excel.proteindb.importcfg.jaxb");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private PintImportCfg readCfgFile(File xmlFile) throws IOException, JAXBException {

		if (xmlFile.exists() && jaxbContext != null) {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Object ret = unmarshaller.unmarshal(xmlFile);
			return (PintImportCfg) ret;
		}

		throw new FileNotFoundException("File " + xmlFile.getAbsolutePath() + " not found");
	}

	private PintImportCfg readCfgFile(InputStream is) throws IOException, JAXBException {

		if (is != null && jaxbContext != null) {
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			Object ret = unmarshaller.unmarshal(is);
			return (PintImportCfg) ret;
		}

		throw new FileNotFoundException("Input stream is null ");
	}

	/**
	 *
	 * @param is
	 * @param fastaIndexFolder
	 *            the folder in which the fasta files will be indexed in
	 *            necessary. If null, the fasta index will be created in a TEMP
	 *            folder of the system
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Project getProjectFromCfgFile(InputStream is, File fastaIndexFolder) throws IllegalArgumentException {

		try {
			final PintImportCfgType cfg = readCfgFile(is);
			return getProjectFromCfgFile(cfg, fastaIndexFolder);

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 *
	 * @param xmlFile
	 * @param fastaIndexFolder
	 *            the folder in which the fasta files will be indexed in
	 *            necessary. If null, the fasta index will be created in a TEMP
	 *            folder of the system
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Project getProjectFromCfgFile(File xmlFile, File fastaIndexFolder) throws IllegalArgumentException {

		try {
			final PintImportCfgType cfg = readCfgFile(xmlFile);
			return getProjectFromCfgFile(cfg, fastaIndexFolder);

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 *
	 * @param cfg
	 * @param fastaIndexFolder
	 *            the folder in which the fasta files will be indexed in
	 *            necessary. If null, the fasta index will be created in a TEMP
	 *            folder of the system
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	// this method is synchonized in order to keep other threads waiting and
	// therefore, avoid potential errors due to the use and the clearing of the
	// static information
	private synchronized Project getProjectFromCfgFile(PintImportCfgType cfg, File fastaIndexFolder)
			throws IOException, URISyntaxException {
		log.info("Clearing static Quant information");
		StaticQuantMaps.clearInfo();

		projectCfg = cfg.getProject();
		ProjectEx project = new ProjectEx(projectCfg.getName(), projectCfg.getDescription());
		if (projectCfg.getReleaseDate() != null) {
			project.setReleaseDate(projectCfg.getReleaseDate().toGregorianCalendar().getTime());
		}
		project.setTag(projectCfg.getTag());
		project.setUploadedDate(new Date());
		project.setPrivate(true);

		excelReader = new ExcelFileReader(cfg.getFileSet(), cfg.getServers());

		// TODO CHANGE THIS!!
		Map<String, Map<QuantCondition, QuantificationLabel>> labelsByConditionsByFile = getLabelsByConditions(cfg);
		Map<String, QuantificationLabel> numeratorLabelsByFile = getNumeratorLabelsByFile(cfg,
				labelsByConditionsByFile);
		Map<String, QuantificationLabel> denominatorLabelsByFile = getDenominatorLabelsByFile(cfg,
				labelsByConditionsByFile);
		Map<String, List<RatioDescriptor>> ratioDescriptorsByFile = getRatioDescriptorsByFile(cfg,
				labelsByConditionsByFile);
		remoteFileReader = new RemoteFileReader(cfg.getFileSet(), cfg.getServers(), fastaIndexFolder,
				labelsByConditionsByFile, ratioDescriptorsByFile);

		if (remoteFileReader.getRemoteFiles().isEmpty())
			remoteFileReader = null;

		// msRuns
		final MsRunsType msRuns = projectCfg.getMsRuns();
		if (msRuns != null && msRuns.getMsRun() != null) {
			for (MsRunType msrunType : msRuns.getMsRun()) {
				MSRun msrun = new MSRunAdapter(msrunType, project).adapt();
				project.getMsRuns().add(msrun);
			}
		}
		// experimental Conditions
		final RatiosType ratiosCfg = projectCfg.getRatios();
		conditionsByConditionID = new HashMap<String, Condition>();
		for (ExperimentalConditionType expConditionCfg : projectCfg.getExperimentalConditions()
				.getExperimentalCondition()) {
			final ConditionAdapter conditionAdapter = new ConditionAdapter(expConditionCfg, msRuns,
					projectCfg.getExperimentalDesign(), project, excelReader, remoteFileReader);
			final Condition condition = conditionAdapter.adapt();
			conditionsByConditionID.put(condition.getName(), condition);
			project.getConditions().add(condition);

			// index proteins by ACC
			for (Protein protein : condition.getProteins()) {
				addToMapWithPrimaryAndSecondaryAccs(protein, proteinMap);
			}
		}

		// get the ratios, iterate over them getting the corresponding
		// proteins for each rowIndex and then creating an object for all
		// the proteins in that row if they correspond with the conditions
		// of the ratio
		// protein ratios
		if (ratiosCfg != null) {
			createRatios(ratiosCfg, project);
		}
		// loop over all items that contains conditions and if they contains
		// Quantconditions, change it by the Conditions in the
		// conditionsByConditionID map
		updateConditions(project);
		// clear static information
		clearStaticInformation();
		return project;
	}

	/**
	 * loop over all items that contains conditions and if they contains
	 * {@link QuantCondition}, change it by the {@link Condition} in the
	 * conditionsByConditionID map
	 *
	 * @param conditionsByConditionID2
	 * @param project
	 */
	private void updateConditions(Project project) {

		final Set<Condition> conditions = project.getConditions();
		for (Condition condition : conditions) {
			// proteins
			final Set<Protein> proteins = condition.getProteins();
			for (Protein protein : proteins) {
				updateConditionsInHasAmounts(protein);
				updateConditionsInHasRatios(protein);
			}
			// peptides
			final Set<Peptide> peptides = condition.getPeptides();
			for (Peptide peptide : peptides) {
				updateConditionsInHasAmounts(peptide);
				updateConditionsInHasRatios(peptide);
			}
			// psms
			final Set<PSM> psMs = condition.getPSMs();
			for (PSM psm : psMs) {
				updateConditionsInHasAmounts(psm);
				updateConditionsInHasRatios(psm);
			}
		}
	}

	private void updateConditionsInHasAmounts(HasAmounts hasAmounts) {
		if (hasAmounts.getAmounts() != null) {
			for (Amount amount : hasAmounts.getAmounts()) {
				if (amount.getCondition() instanceof QuantCondition) {
					if (amount instanceof AmountEx) {
						((AmountEx) amount).setCondition(conditionsByConditionID.get(amount.getCondition().getName()));
					} else if (amount instanceof QuantAmount) {
						((QuantAmount) amount)
								.setCondition(conditionsByConditionID.get(amount.getCondition().getName()));
					}
				}
			}
		}
	}

	private void updateConditionsInHasRatios(HasRatios hasAmounts) {
		if (hasAmounts.getRatios() != null) {
			for (Ratio ratio : hasAmounts.getRatios()) {
				if (ratio.getCondition1() instanceof QuantCondition) {
					if (ratio instanceof RatioEx) {
						((RatioEx) ratio).setCondition1(conditionsByConditionID.get(ratio.getCondition1().getName()));
					} else if (ratio instanceof CensusRatio) {
						((CensusRatio) ratio)
								.setCondition1(conditionsByConditionID.get(ratio.getCondition1().getName()));
					}
				}
				if (ratio.getCondition2() instanceof QuantCondition) {
					if (ratio instanceof RatioEx) {
						((RatioEx) ratio).setCondition2(conditionsByConditionID.get(ratio.getCondition2().getName()));
					} else if (ratio instanceof CensusRatio) {
						((CensusRatio) ratio)
								.setCondition2(conditionsByConditionID.get(ratio.getCondition2().getName()));
					}
				}
			}
		}
	}

	private Map<String, List<RatioDescriptor>> getRatioDescriptorsByFile(PintImportCfgType cfg,
			Map<String, Map<QuantCondition, QuantificationLabel>> labelsByConditionsByFile) {
		Map<String, List<RatioDescriptor>> ret = new HashMap<String, List<RatioDescriptor>>();

		if (cfg != null && cfg.getProject() != null && cfg.getProject().getRatios() != null) {
			if (cfg.getProject().getRatios().getPsmAmountRatios() != null
					&& cfg.getProject().getRatios().getPsmAmountRatios().getRemoteFilesRatio() != null) {
				for (RemoteFilesRatioType remoteFileRatio : cfg.getProject().getRatios().getPsmAmountRatios()
						.getRemoteFilesRatio()) {

					final String fileRef = remoteFileRatio.getFileRef();
					RatioDescriptor ratioDescriptor = getRatioDescriptor(remoteFileRatio,
							labelsByConditionsByFile.get(fileRef));
					if (ret.containsKey(fileRef)) {
						ret.get(fileRef).add(ratioDescriptor);
					} else {
						List<RatioDescriptor> list = new ArrayList<RatioDescriptor>();
						list.add(ratioDescriptor);
						ret.put(fileRef, list);
					}
				}
			}
			if (cfg.getProject().getRatios().getProteinAmountRatios() != null
					&& cfg.getProject().getRatios().getProteinAmountRatios().getRemoteFilesRatio() != null) {
				for (RemoteFilesRatioType remoteFileRatio : cfg.getProject().getRatios().getProteinAmountRatios()
						.getRemoteFilesRatio()) {
					final String fileRef = remoteFileRatio.getFileRef();
					RatioDescriptor ratioDescriptor = getRatioDescriptor(remoteFileRatio,
							labelsByConditionsByFile.get(fileRef));
					if (ret.containsKey(fileRef)) {
						ret.get(fileRef).add(ratioDescriptor);
					} else {
						List<RatioDescriptor> list = new ArrayList<RatioDescriptor>();
						list.add(ratioDescriptor);
						ret.put(fileRef, list);
					}
				}
			}
		}

		return ret;
	}

	private RatioDescriptor getRatioDescriptor(RemoteFilesRatioType remoteFileRatio,
			Map<QuantCondition, QuantificationLabel> labelsByConditions) {
		QuantCondition condition1 = null;
		QuantificationLabel label1 = null;
		QuantCondition condition2 = null;
		QuantificationLabel label2 = null;
		for (QuantCondition condition : labelsByConditions.keySet()) {
			final QuantificationLabel label = labelsByConditions.get(condition);
			if (remoteFileRatio.getNumerator().getConditionRef().equals(condition.getName())) {
				label1 = label;
				condition1 = condition;
			}
			if (remoteFileRatio.getDenominator().getConditionRef().equals(condition.getName())) {
				label2 = label;
				condition2 = condition;
			}
		}

		RatioDescriptor ret = new RatioDescriptor(label1, label2, condition1, condition2);
		return ret;
	}

	private Map<String, QuantificationLabel> getNumeratorLabelsByFile(PintImportCfgType cfg,
			Map<String, Map<QuantCondition, QuantificationLabel>> labelsByConditionsByFile) {
		Map<String, QuantificationLabel> ret = new HashMap<String, QuantificationLabel>();
		if (cfg != null && cfg.getProject() != null && cfg.getProject().getRatios() != null) {
			if (cfg.getProject().getRatios().getPsmAmountRatios() != null
					&& cfg.getProject().getRatios().getPsmAmountRatios().getRemoteFilesRatio() != null) {
				for (RemoteFilesRatioType remoteFileRatio : cfg.getProject().getRatios().getPsmAmountRatios()
						.getRemoteFilesRatio()) {
					final String fileRef = remoteFileRatio.getFileRef();
					final Map<QuantCondition, QuantificationLabel> labelsByConditions = labelsByConditionsByFile
							.get(fileRef);
					final String numeratorConditionID = remoteFileRatio.getNumerator().getConditionRef();
					for (QuantCondition quantCondition : labelsByConditions.keySet()) {
						if (quantCondition.getName().equals(numeratorConditionID)) {
							ret.put(remoteFileRatio.getFileRef(), labelsByConditions.get(quantCondition));
						}
					}
				}
			}
			if (cfg.getProject().getRatios().getProteinAmountRatios() != null
					&& cfg.getProject().getRatios().getProteinAmountRatios().getRemoteFilesRatio() != null) {
				for (RemoteFilesRatioType remoteFileRatio : cfg.getProject().getRatios().getProteinAmountRatios()
						.getRemoteFilesRatio()) {
					final String fileRef = remoteFileRatio.getFileRef();
					final Map<QuantCondition, QuantificationLabel> labelsByConditions = labelsByConditionsByFile
							.get(fileRef);
					final String numeratorConditionID = remoteFileRatio.getNumerator().getConditionRef();
					for (QuantCondition quantCondition : labelsByConditions.keySet()) {
						if (quantCondition.getName().equals(numeratorConditionID)) {
							ret.put(remoteFileRatio.getFileRef(), labelsByConditions.get(quantCondition));
						}
					}
				}
			}
		}

		// insert LIGHT by default in all the files not detected
		if (cfg != null && cfg.getFileSet() != null && cfg.getFileSet().getFile() != null) {
			for (FileType fileType : cfg.getFileSet().getFile()) {
				if (!ret.containsKey(fileType.getId())) {
					ret.put(fileType.getId(), QuantificationLabel.LIGHT);
				}
			}
		}
		return ret;
	}

	private Map<String, QuantificationLabel> getDenominatorLabelsByFile(PintImportCfgType cfg,
			Map<String, Map<QuantCondition, QuantificationLabel>> labelsByConditionsByFile) {
		Map<String, QuantificationLabel> ret = new HashMap<String, QuantificationLabel>();
		if (cfg != null && cfg.getProject() != null && cfg.getProject().getRatios() != null) {
			if (cfg.getProject().getRatios().getPsmAmountRatios() != null
					&& cfg.getProject().getRatios().getPsmAmountRatios().getRemoteFilesRatio() != null) {
				for (RemoteFilesRatioType remoteFileRatio : cfg.getProject().getRatios().getPsmAmountRatios()
						.getRemoteFilesRatio()) {
					final String fileRef = remoteFileRatio.getFileRef();
					final Map<QuantCondition, QuantificationLabel> labelsByConditions = labelsByConditionsByFile
							.get(fileRef);
					final String denominatorConditionID = remoteFileRatio.getDenominator().getConditionRef();
					for (QuantCondition quantCondition : labelsByConditions.keySet()) {
						if (quantCondition.getName().equals(denominatorConditionID)) {
							ret.put(remoteFileRatio.getFileRef(), labelsByConditions.get(quantCondition));
						}
					}
				}
			}
			if (cfg.getProject().getRatios().getProteinAmountRatios() != null
					&& cfg.getProject().getRatios().getProteinAmountRatios().getRemoteFilesRatio() != null) {
				for (RemoteFilesRatioType remoteFileRatio : cfg.getProject().getRatios().getProteinAmountRatios()
						.getRemoteFilesRatio()) {
					final String fileRef = remoteFileRatio.getFileRef();
					final Map<QuantCondition, QuantificationLabel> labelsByConditions = labelsByConditionsByFile
							.get(fileRef);
					final String denominatorConditionID = remoteFileRatio.getDenominator().getConditionRef();
					for (QuantCondition quantCondition : labelsByConditions.keySet()) {
						if (quantCondition.getName().equals(denominatorConditionID)) {
							ret.put(remoteFileRatio.getFileRef(), labelsByConditions.get(quantCondition));
						}
					}
				}
			}
		}

		// insert LIGHT by default in all the files not detected
		if (cfg != null && cfg.getFileSet() != null && cfg.getFileSet().getFile() != null) {
			for (FileType fileType : cfg.getFileSet().getFile()) {
				if (!ret.containsKey(fileType.getId())) {
					ret.put(fileType.getId(), QuantificationLabel.LIGHT);
				}
			}
		}
		return ret;
	}

	private Pattern getDecoyProteinAccRegexp() {
		try {
			if (discardDecoyRegexp == null) {
				if (projectCfg != null) {
					if (projectCfg.getExperimentalConditions() != null) {
						for (ExperimentalConditionType condition : projectCfg.getExperimentalConditions()
								.getExperimentalCondition()) {
							if (condition.getIdentificationInfo() != null) {
								if (condition.getIdentificationInfo().getExcelIdentInfo() != null) {
									for (IdentificationExcelType idExcel : condition.getIdentificationInfo()
											.getExcelIdentInfo()) {
										if (idExcel.getDiscardDecoys() != null
												&& !"".equals(idExcel.getDiscardDecoys())) {
											discardDecoyRegexp = Pattern.compile(idExcel.getDiscardDecoys());
										}
									}
								}
								if (condition.getIdentificationInfo().getRemoteFilesIdentInfo() != null) {
									for (RemoteInfoType remoteId : condition.getIdentificationInfo()
											.getRemoteFilesIdentInfo()) {
										if (remoteId.getDiscardDecoys() != null
												&& !"".equals(remoteId.getDiscardDecoys())) {
											discardDecoyRegexp = Pattern.compile(remoteId.getDiscardDecoys());
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (PatternSyntaxException e) {
			log.warn(e);
		}
		return discardDecoyRegexp;
	}

	/**
	 * Get a list of Maps of conditions to labels. There is a map for each file
	 * in the file set list
	 *
	 * @param cfg
	 * @return
	 */
	private Map<String, Map<QuantCondition, QuantificationLabel>> getLabelsByConditions(PintImportCfgType cfg) {
		Map<String, Map<QuantCondition, QuantificationLabel>> ret = new HashMap<String, Map<QuantCondition, QuantificationLabel>>();

		// get all census-chro
		// List<FileType> censusChroFiles =
		// getQuantitativeFiles(cfg.getFileSet());
		for (FileType fileType : cfg.getFileSet().getFile()) {
			if (fileType.getFormat() == FormatType.CENSUS_CHRO_XML
					|| fileType.getFormat() == FormatType.CENSUS_OUT_TXT) {
				// add the map in any case
				Map<QuantCondition, QuantificationLabel> map = new HashMap<QuantCondition, QuantificationLabel>();
				ret.put(fileType.getId(), map);
				String fileId = fileType.getId();
				for (ExperimentalConditionType conditionType : cfg.getProject().getExperimentalConditions()
						.getExperimentalCondition()) {
					final QuantCondition condition = new QuantCondition(conditionType.getId(), projectCfg.getTag());
					if (conditionType.getIdentificationInfo() != null
							&& conditionType.getIdentificationInfo().getRemoteFilesIdentInfo() != null) {
						for (RemoteInfoType remoteInfoType : conditionType.getIdentificationInfo()
								.getRemoteFilesIdentInfo()) {
							for (FileReferenceType fileRef : remoteInfoType.getFileRef()) {
								if (fileId.equals(fileRef.getFileRef())) {
									final String sampleRef = conditionType.getSampleRef();
									SampleType sampleType = getSampleType(sampleRef, cfg);
									if (sampleType != null) {
										final String labelID = sampleType.getLabelRef();
										map.put(condition, QuantificationLabel.getByName(labelID));
									}
								}
							}
						}
					}
					if (conditionType.getQuantificationInfo() != null
							&& conditionType.getQuantificationInfo().getRemoteFilesQuantInfo() != null) {
						for (RemoteInfoType remoteInfoType : conditionType.getQuantificationInfo()
								.getRemoteFilesQuantInfo()) {
							for (FileReferenceType fileRef : remoteInfoType.getFileRef()) {
								if (fileId.equals(fileRef.getFileRef())) {
									final String sampleRef = conditionType.getSampleRef();
									SampleType sampleType = getSampleType(sampleRef, cfg);
									if (sampleType != null) {
										final String labelID = sampleType.getLabelRef();
										map.put(condition, QuantificationLabel.getByName(labelID));
									}
								}
							}
						}
					}
				}
				// check in the ratios
				if (cfg.getProject().getRatios() != null) {
					List<RemoteFilesRatioType> ratioList = new ArrayList<RemoteFilesRatioType>();
					if (cfg.getProject().getRatios().getProteinAmountRatios() != null) {
						if (cfg.getProject().getRatios().getProteinAmountRatios().getRemoteFilesRatio() != null) {
							ratioList.addAll(
									cfg.getProject().getRatios().getProteinAmountRatios().getRemoteFilesRatio());
						}
					}
					if (cfg.getProject().getRatios().getPsmAmountRatios() != null) {
						if (cfg.getProject().getRatios().getPsmAmountRatios().getRemoteFilesRatio() != null) {
							ratioList.addAll(cfg.getProject().getRatios().getPsmAmountRatios().getRemoteFilesRatio());
						}
					}
					for (RemoteFilesRatioType fileRatio : ratioList) {
						if (fileId.equals(fileRatio.getFileRef())) {
							final String numeratorConditionId = fileRatio.getNumerator().getConditionRef();
							for (ExperimentalConditionType conditionType : cfg.getProject().getExperimentalConditions()
									.getExperimentalCondition()) {
								if (conditionType.getId().equals(numeratorConditionId)) {
									final String sampleRef = conditionType.getSampleRef();
									if (sampleRef != null) {
										SampleType sampleType = getSampleType(sampleRef, cfg);
										if (sampleType != null) {
											final String labelID = sampleType.getLabelRef();
											map.put(new QuantCondition(conditionType.getId(), projectCfg.getTag()),
													QuantificationLabel.getByName(labelID));
										}
									}
								}
							}
							final String denominatorConditionId = fileRatio.getDenominator().getConditionRef();
							for (ExperimentalConditionType conditionType : cfg.getProject().getExperimentalConditions()
									.getExperimentalCondition()) {
								if (conditionType.getId().equals(denominatorConditionId)) {
									final String sampleRef = conditionType.getSampleRef();
									if (sampleRef != null) {
										SampleType sampleType = getSampleType(sampleRef, cfg);
										if (sampleType != null) {
											final String labelID = sampleType.getLabelRef();
											map.put(new QuantCondition(conditionType.getId(), projectCfg.getTag()),
													QuantificationLabel.getByName(labelID));
										}
									}
								}
							}
						}
					}
				}

			} else {
				ret.put(fileType.getId(), null);
			}
		}
		return ret;
	}

	private SampleType getSampleType(String sampleRef, PintImportCfgType cfg) {
		if (cfg.getProject().getExperimentalDesign() != null
				&& cfg.getProject().getExperimentalDesign().getSampleSet() != null
				&& cfg.getProject().getExperimentalDesign().getSampleSet().getSample() != null) {
			for (SampleType sample : cfg.getProject().getExperimentalDesign().getSampleSet().getSample()) {
				if (sample.getId().equals(sampleRef)) {
					return sample;
				}
			}
		}
		return null;
	}

	private List<FileType> getQuantitativeFiles(FileSetType fileSet) {
		List<FileType> ret = new ArrayList<FileType>();
		ret.addAll(getCensusChroFiles(fileSet));
		ret.addAll(getCensusOutFiles(fileSet));
		return ret;
	}

	private List<FileType> getCensusChroFiles(FileSetType fileSet) {
		List<FileType> ret = new ArrayList<FileType>();
		for (FileType fileType : fileSet.getFile()) {
			if (fileType.getFormat() == FormatType.CENSUS_CHRO_XML) {
				ret.add(fileType);
			}
		}
		return ret;
	}

	private List<FileType> getCensusOutFiles(FileSetType fileSet) {
		List<FileType> ret = new ArrayList<FileType>();
		for (FileType fileType : fileSet.getFile()) {
			if (fileType.getFormat() == FormatType.CENSUS_OUT_TXT) {
				ret.add(fileType);
			}
		}
		return ret;
	}

	private void addToMapWithPrimaryAndSecondaryAccs(Protein protein, Map<String, Set<Protein>> proteinMap2) {
		final String accession = protein.getPrimaryAccession().getAccession();
		addToMap(proteinMap2, accession, protein);

		// index also by secondary accessions
		final List<Accession> secondaryAccessions = protein.getSecondaryAccessions();
		if (secondaryAccessions != null) {
			for (Accession secAcc : secondaryAccessions) {
				addToMap(proteinMap2, secAcc.getAccession(), protein);
			}
		}

	}

	private void addToMap(Map<String, Set<Protein>> proteinMap2, String accession, Protein protein) {
		if (proteinMap2.containsKey(accession)) {
			proteinMap2.get(accession).add(protein);
		} else {
			Set<Protein> set = new HashSet<Protein>();
			set.add(protein);
			proteinMap2.put(accession, set);
		}

	}

	private void createRatios(RatiosType ratiosCfg, Project project) {
		if (ratiosCfg != null) {
			if (ratiosCfg.getProteinAmountRatios() != null) {
				createProteinRatios(ratiosCfg.getProteinAmountRatios(), project);
			}
			if (ratiosCfg.getPeptideAmountRatios() != null) {
				createPeptideRatios(ratiosCfg.getPeptideAmountRatios(), project);
			}
			if (ratiosCfg.getPsmAmountRatios() != null) {
				createPSMRatios(ratiosCfg.getPsmAmountRatios(), project);
			}
		}
	}

	private void createPSMRatios(PsmRatiosType psmAmountRatios, Project project) {
		if (psmAmountRatios != null) {
			if (psmAmountRatios.getExcelRatio() != null && !psmAmountRatios.getExcelRatio().isEmpty()) {
				createPSMRatiosFromExcel(psmAmountRatios.getExcelRatio(), project);
			}
			if (psmAmountRatios.getRemoteFilesRatio() != null && !psmAmountRatios.getRemoteFilesRatio().isEmpty()) {
				createPSMRatiosFromRemoteFile(psmAmountRatios.getRemoteFilesRatio());
			}
		}
	}

	private void createPeptideRatios(PeptideRatiosType peptideAmountRatios, Project project) {
		if (peptideAmountRatios != null) {
			if (peptideAmountRatios.getExcelRatio() != null && !peptideAmountRatios.getExcelRatio().isEmpty()) {
				createPeptideRatiosFromExcel(peptideAmountRatios.getExcelRatio(), project);
			}
			// TODO see if we have the peptide level ratios somewhere. If so, be
			// carefull and look into the implementations of the
			// QuantifiedPeptideInterface because some ratios and amounts are
			// actually a collection of the ones from the PSMs

			// if (peptideAmountRatios.getRemoteFilesRatio() != null
			// && !peptideAmountRatios.getRemoteFilesRatio().isEmpty()) {
			// createPeptideRatiosFromRemoteFile(peptideAmountRatios.getRemoteFilesRatio());
			// }
		}
	}

	private void createPSMRatiosFromRemoteFile(List<RemoteFilesRatioType> remoteFileRatiosCfg) {
		if (remoteFileRatiosCfg != null) {
			for (RemoteFilesRatioType remoteFilesRatioType : remoteFileRatiosCfg) {

				String msRunRef = remoteFilesRatioType.getMsRunRef();

				final QuantParser censusQuantParser = remoteFileReader
						.getQuantParser(remoteFilesRatioType.getFileRef());
				if (censusQuantParser != null) {
					if (remoteFilesRatioType.getDiscardDecoys() != null)
						censusQuantParser.setDecoyPattern(remoteFilesRatioType.getDiscardDecoys());

					final Map<String, QuantifiedPSMInterface> quantPSMsMap = censusQuantParser.getPSMMap();

					if (quantPSMsMap != null) {
						Set<PSM> runPSMs = ConditionAdapter.getPSMsByRunID(projectCfg.getTag(), msRunRef);
						// aqui se usaa el msrun pero no en excel. Usarlo en
						// excel, aunque sea opcionalmente?
						if (runPSMs != null) {
							for (PSM runPSM : runPSMs) {
								final QuantifiedPSMInterface quantifiedPSM = quantPSMsMap
										.get(runPSM.getPSMIdentifier());
								if (quantifiedPSM == null) {
									// log.warn(runPSM.getPSMIdentifier()
									// + " doesn't have quantitative
									// information. Skipping it...");
									continue;
								}

								// /////////////////////////////////////////////
								// ratios (from ratios of
								// peaks = pairs)
								final Set<QuantRatio> ratios = quantifiedPSM.getRatios();
								if (ratios != null) {
									Map<Pair<String, String>, List<QuantRatio>> mapQuantRatios = new HashMap<Pair<String, String>, List<QuantRatio>>();
									for (QuantRatio quantRatio : ratios) {
										final QuantificationLabel label1 = quantRatio.getLabel1();
										final QuantificationLabel label2 = quantRatio.getLabel2();

										String sample1 = getSamplesByLabel(label1,
												remoteFilesRatioType.getNumerator().getConditionRef(),
												remoteFilesRatioType.getDenominator().getConditionRef());
										String sample2 = getSamplesByLabel(label2,
												remoteFilesRatioType.getNumerator().getConditionRef(),
												remoteFilesRatioType.getDenominator().getConditionRef());
										if (sample1 == null || sample2 == null) {
											continue;
										}
										Pair<String, String> samplePair = new Pair<String, String>(sample1, sample2);
										if (mapQuantRatios.containsKey(samplePair)) {
											// take LOG 2 ratio
											mapQuantRatios.get(samplePair).add(quantRatio);
										} else {
											List<QuantRatio> list = new ArrayList<QuantRatio>();
											list.add(quantRatio);
											mapQuantRatios.put(samplePair, list);
										}
									}
									for (Pair<String, String> pair : mapQuantRatios.keySet()) {
										final String sample1 = pair.getFirstelement();
										Condition condition1 = getConditionBySampleAndRatio(sample1,
												remoteFilesRatioType);
										final String sample2 = pair.getSecondElement();
										Condition condition2 = getConditionBySampleAndRatio(sample2,
												remoteFilesRatioType);

										final List<QuantRatio> ratioList = mapQuantRatios.get(pair);
										// treat MAX or MIN value ratios
										// separetely
										final List<QuantRatio> safeRatios = new ArrayList<QuantRatio>();
										final List<QuantRatio> maxOrMinValueRatioValues = new ArrayList<QuantRatio>();
										for (QuantRatio ratioValue : ratioList) {
											// discard a ratio that is not
											// between the two conditions
											if (!ratioValue.getCondition1().getName().equals(condition1.getName())
													&& !ratioValue.getCondition2().getName()
															.equals(condition2.getName())) {
												if (!ratioValue.getCondition1().getName().equals(condition2.getName())
														&& !ratioValue.getCondition2().getName()
																.equals(condition1.getName())) {
													continue;
												}
											}

											final Double log2Ratio = ratioValue.getLog2Ratio(condition1.getName(),
													condition2.getName());
											if (Maths.isMaxOrMinValue(log2Ratio)) {
												maxOrMinValueRatioValues.add(ratioValue);
											} else {
												safeRatios.add(ratioValue);
											}
										}

										if (!safeRatios.isEmpty()) {
											// get the ratios by its names, in
											// order to average them if there is
											// more than one of the same name
											Map<String, Set<QuantRatio>> ratiosByNames = getRatiosByDescription(
													safeRatios);
											for (Set<QuantRatio> safeRatiosSameName : ratiosByNames.values()) {

												if (safeRatiosSameName.size() > 1) {
													// make the average of the
													// values
													List<Double> safeRatioValues = new ArrayList<Double>();
													for (QuantRatio safeRatio : safeRatiosSameName) {
														final Double log2Ratio = safeRatio.getLog2Ratio(
																condition1.getName(), condition2.getName());
														if (log2Ratio != null) {
															safeRatioValues.add(log2Ratio);
														}
													}
													RatioEx ratio = new RatioEx(
															Maths.mean(safeRatioValues.toArray(new Double[0])),
															condition1, condition2, CombinationType.AVERAGE,
															safeRatiosSameName.iterator().next().getDescription(),
															AggregationLevel.PSM);

													ScoreEx score = new ScoreEx(
															String.valueOf(Maths
																	.stddev(safeRatioValues.toArray(new Double[0]))),
															"Standard deviation of ratios",
															"Standard deviation of ratios at spectrum level",
															"Standard deviation of the ratios computed in this spectrum");
													if (!Double.isNaN(Double.valueOf(score.getValue()))) {
														ratio.setAssociatedConfidenceScore(score);
													}
													runPSM.addRatio(ratio);
												} else {
													final QuantRatio safeRatio = safeRatiosSameName.iterator().next();
													final Double log2RatioValue = safeRatio
															.getLog2Ratio(condition1.getName(), condition2.getName());
													if (log2RatioValue != null) {
														// as cv term
														// MS:1001132:
														String ratioDescription = safeRatio.getDescription();
														// if (safeRatio
														// instanceof IsoRatio)
														// {
														// ratioDescription =
														// ISOBARIC_RATIO;
														// }
														RatioEx ratio = new RatioEx(log2RatioValue, condition1,
																condition2, ratioDescription, AggregationLevel.PSM);
														final Score ratioScore = safeRatio
																.getAssociatedConfidenceScore();
														if (ratioScore != null && !Double
																.isNaN(Double.valueOf(ratioScore.getValue()))) {
															ScoreEx score = new ScoreEx(ratioScore.getValue(),
																	ratioScore.getScoreName(),
																	ratioScore.getScoreType(),
																	ratioScore.getScoreDescription());
															ratio.setAssociatedConfidenceScore(score);
														}
														runPSM.addRatio(ratio);

													}
												}
											}
										}
										// store the infinite ratios as
										// singleton intensity ratios
										if (!maxOrMinValueRatioValues.isEmpty()) {
											for (QuantRatio infiniteRatio : maxOrMinValueRatioValues) {
												final Double nonLogRatio = infiniteRatio
														.getNonLogRatio(condition1.getName(), condition2.getName());
												if (nonLogRatio != null) {
													RatioEx ratio = new RatioEx(nonLogRatio, condition1, condition2,
															SINGLETON_INTENSITY_RATIO, AggregationLevel.PSM);
													runPSM.addRatio(ratio);
												}
											}
										}

									}
								}
								// end of ratios
								// ///////////////////////////////////////////

								// ///////////////////////////////////////////
								// singleton ratio sum intensities based and
								// singleton ratio sum counts based:
								// sum of singleton intensities of condition 1 /
								// sum ofsingleton intensities of condition 2
								// and
								// number of singleton peaks of condition 1 /
								// number of singleton peaks of condition 2
								//
								if (quantifiedPSM instanceof IsobaricQuantifiedPSM) {
									IsobaricQuantifiedPSM isoPSM = (IsobaricQuantifiedPSM) quantifiedPSM;

									final Map<QuantificationLabel, Set<Ion>> singletonIonsByLabelMap = isoPSM
											.getSingletonIonsByLabel();
									List<QuantificationLabel> quantLabelList = new ArrayList<QuantificationLabel>();
									quantLabelList.addAll(singletonIonsByLabelMap.keySet());

									for (int i = 0; i < quantLabelList.size(); i++) {
										final QuantificationLabel quantificationLabel1 = quantLabelList.get(i);
										String sample1 = getSamplesByLabel(quantificationLabel1,
												remoteFilesRatioType.getNumerator().getConditionRef(),
												remoteFilesRatioType.getDenominator().getConditionRef());
										if (sample1 == null) {
											continue;
										}
										final Condition condition1 = getConditionBySampleAndRatio(sample1,
												remoteFilesRatioType);
										Set<Ion> ions1 = singletonIonsByLabelMap.get(quantificationLabel1);
										for (int j = i + 1; j < quantLabelList.size(); j++) {
											final QuantificationLabel quantificationLabel2 = quantLabelList.get(j);
											String sample2 = getSamplesByLabel(quantificationLabel2,
													remoteFilesRatioType.getNumerator().getConditionRef(),
													remoteFilesRatioType.getDenominator().getConditionRef());
											if (sample2 == null) {
												continue;
											}
											final Condition condition2 = getConditionBySampleAndRatio(sample2,
													remoteFilesRatioType);
											Set<Ion> ions2 = singletonIonsByLabelMap.get(quantificationLabel2);
											if (!ions2.isEmpty()) {
												// singleton ratio count based
												double singletonRatioCountValue = Double.valueOf(ions1.size())
														/ Double.valueOf(ions2.size());
												// make the log2
												double log2Ratio = Math.log(singletonRatioCountValue) / Math.log(2);
												RatioEx singletonRatioCount = new RatioEx(log2Ratio, condition1,
														condition2, SINGLETON_SUM_COUNT_RATIO, AggregationLevel.PSM);
												runPSM.addRatio(singletonRatioCount);

												// singleton ratio intensity
												// based
												double singletonRatioIntensityValue = Double
														.valueOf(getIntensitySum(ions1))
														/ Double.valueOf(getIntensitySum(ions2));
												// make the log2
												log2Ratio = Math.log(singletonRatioIntensityValue) / Math.log(2);
												RatioEx singletonRatioIntensity = new RatioEx(log2Ratio, condition1,
														condition2, SINGLETON_SUM_INTENSITIES_RATIO,
														AggregationLevel.PSM);
												runPSM.addRatio(singletonRatioIntensity);
											} else {
												// ions2 is empty, so
												// denominator is
												// 0
												if (!ions1.isEmpty()) {
													// n/0 -> positive infinity,
													// log(inf) = inf
													RatioEx singletonRatioCount = new RatioEx(Double.POSITIVE_INFINITY,
															condition1, condition2, SINGLETON_SUM_COUNT_RATIO,
															AggregationLevel.PSM);
													runPSM.addRatio(singletonRatioCount);
													RatioEx singletonRatioIntensity = new RatioEx(
															Double.POSITIVE_INFINITY, condition1, condition2,
															SINGLETON_SUM_INTENSITIES_RATIO, AggregationLevel.PSM);
													runPSM.addRatio(singletonRatioIntensity);
												}

											}
										}
									}
								}

								// end of singleton ratio intensity based
								// ////////////////////////////////////////////

							}
						}
					}
				}
			}
		}
	}

	private Map<String, Set<QuantRatio>> getRatiosByDescription(List<QuantRatio> safeRatios) {
		Map<String, Set<QuantRatio>> ret = new HashMap<String, Set<QuantRatio>>();
		for (QuantRatio ratio : safeRatios) {
			if (!ret.containsKey(ratio.getDescription())) {
				Set<QuantRatio> set = new HashSet<QuantRatio>();
				set.add(ratio);
				ret.put(ratio.getDescription(), set);
			} else {
				ret.get(ratio.getDescription()).add(ratio);
			}
		}
		return ret;
	}

	private void createProteinRatiosFromRemoteFile(List<RemoteFilesRatioType> remoteFileRatiosCfg) {
		if (remoteFileRatiosCfg != null) {
			for (RemoteFilesRatioType remoteFilesRatioType : remoteFileRatiosCfg) {

				String msRunRef = remoteFilesRatioType.getMsRunRef();

				final QuantParser censusQuantParser = remoteFileReader
						.getQuantParser(remoteFilesRatioType.getFileRef());
				if (censusQuantParser != null) {
					if (remoteFilesRatioType.getDiscardDecoys() != null) {
						censusQuantParser.setDecoyPattern(remoteFilesRatioType.getDiscardDecoys());
					}
					final Map<String, QuantifiedProteinInterface> quantProteinMap = censusQuantParser.getProteinMap();

					if (quantProteinMap != null) {
						Set<Protein> runProteins = ConditionAdapter.getProteinsByRunID(projectCfg.getTag(), msRunRef);
						// aqui se usaa el msrun pero no en excel. Usarlo en
						// excel, aunque sea opcionalmente?
						if (runProteins != null) {
							// proteins already in run that are quantified:
							for (Protein runProtein : runProteins) {
								QuantifiedProteinInterface quantifiedProtein = quantProteinMap
										.get(runProtein.getAccession());
								if (quantifiedProtein == null) {
									final List<Accession> secondaryAccessions = runProtein.getSecondaryAccessions();
									for (Accession accession : secondaryAccessions) {
										if (quantProteinMap.containsKey(accession.getAccession())) {
											quantifiedProtein = quantProteinMap.get(accession.getAccession());
										}
									}
									if (quantifiedProtein == null) {
										log.warn(runProtein.getAccession()
												+ " doesn't have quantitative information. Skipping it...");
										continue;
									}
								}

								// /////////////////////////////////////////////
								// ratios
								final Set<QuantRatio> ratios = quantifiedProtein.getRatios();
								if (ratios != null) {
									Map<Pair<String, String>, List<QuantRatio>> mapQuantRatios = new HashMap<Pair<String, String>, List<QuantRatio>>();
									for (QuantRatio quantRatio : ratios) {
										final QuantificationLabel label1 = quantRatio.getLabel1();
										final QuantificationLabel label2 = quantRatio.getLabel2();

										String sample1 = getSamplesByLabel(label1,
												remoteFilesRatioType.getNumerator().getConditionRef(),
												remoteFilesRatioType.getDenominator().getConditionRef());
										if (sample1 == null) {
											continue;
										}
										String sample2 = getSamplesByLabel(label2,
												remoteFilesRatioType.getNumerator().getConditionRef(),
												remoteFilesRatioType.getDenominator().getConditionRef());
										if (sample2 == null) {
											continue;
										}
										// log.info("Sample1=" + sample1 +
										// "\tSample2=" + sample2);
										Pair<String, String> samplePair = new Pair<String, String>(sample1, sample2);
										if (mapQuantRatios.containsKey(samplePair)) {
											// take LOG 2 ratio
											mapQuantRatios.get(samplePair).add(quantRatio);
										} else {
											List<QuantRatio> list = new ArrayList<QuantRatio>();
											list.add(quantRatio);
											mapQuantRatios.put(samplePair, list);
										}
									}
									for (Pair<String, String> pair : mapQuantRatios.keySet()) {
										final String sample1 = pair.getFirstelement();
										Condition condition1 = getConditionBySampleAndRatio(sample1,
												remoteFilesRatioType);
										final String sample2 = pair.getSecondElement();
										Condition condition2 = getConditionBySampleAndRatio(sample2,
												remoteFilesRatioType);

										final List<QuantRatio> ratioList = mapQuantRatios.get(pair);
										// treat MAX or MIN value ratios
										// separetely
										final List<QuantRatio> safeRatios = new ArrayList<QuantRatio>();
										final List<QuantRatio> maxOrMinValueRatioValues = new ArrayList<QuantRatio>();
										for (QuantRatio ratioValue : ratioList) {
											final Double log2Ratio = ratioValue.getLog2Ratio(condition1.getName(),
													condition2.getName());
											if (Maths.isMaxOrMinValue(log2Ratio)) {
												maxOrMinValueRatioValues.add(ratioValue);
											} else {
												safeRatios.add(ratioValue);
											}
										}

										if (!safeRatios.isEmpty()) {
											// get the ratios by its names, in
											// order to average them if there is
											// more than one of the same name
											Map<String, Set<QuantRatio>> ratiosByNames = getRatiosByDescription(
													safeRatios);
											for (Set<QuantRatio> safeRatiosSameName : ratiosByNames.values()) {

												if (safeRatiosSameName.size() > 1) {
													// make the average of the
													// values
													List<Double> safeRatioValues = new ArrayList<Double>();
													for (QuantRatio safeRatio : safeRatiosSameName) {
														final Double log2Ratio = safeRatio.getLog2Ratio(
																condition1.getName(), condition2.getName());
														if (log2Ratio != null) {
															safeRatioValues.add(log2Ratio);
														}
													}
													RatioEx ratio = new RatioEx(
															Maths.mean(safeRatioValues.toArray(new Double[0])),
															condition1, condition2, CombinationType.AVERAGE,
															safeRatiosSameName.iterator().next().getDescription(),
															AggregationLevel.PROTEIN);
													ScoreEx score = new ScoreEx(
															String.valueOf(Maths
																	.stddev(safeRatioValues.toArray(new Double[0]))),
															"Standard deviation of ratios",
															"Standard deviation of ratios at protein level",
															"Standard deviation of the ratios computed in this protein");
													if (!Double.isNaN(Double.valueOf(score.getValue()))) {
														ratio.setAssociatedConfidenceScore(score);
													}
													runProtein.addRatio(ratio);
												} else {
													final QuantRatio safeRatio = safeRatiosSameName.iterator().next();

													final Double log2Ratio = safeRatio
															.getLog2Ratio(condition1.getName(), condition2.getName());
													if (log2Ratio != null) {
														// as cv term
														// MS:1001132:
														String ratioDescription = safeRatio.getDescription();
														// if (safeRatio
														// instanceof IsoRatio)
														// {
														// ratioDescription =
														// ISOBARIC_RATIO;
														// }
														RatioEx ratio = new RatioEx(log2Ratio, condition1, condition2,
																ratioDescription, AggregationLevel.PROTEIN);
														final Score ratioScore = safeRatio
																.getAssociatedConfidenceScore();
														if (ratioScore != null && !"NA".equals(ratioScore.getValue())
																&& !Double
																		.isNaN(Double.valueOf(ratioScore.getValue()))) {
															ScoreEx score = new ScoreEx(ratioScore.getValue(),
																	ratioScore.getScoreName(),
																	ratioScore.getScoreType(),
																	ratioScore.getScoreDescription());
															ratio.setAssociatedConfidenceScore(score);
														}
														runProtein.addRatio(ratio);

													}
												}
											}
										}
										// store the infinite ratios as
										// singleton intensity ratios
										if (!maxOrMinValueRatioValues.isEmpty()) {
											for (QuantRatio infiniteRatio : maxOrMinValueRatioValues) {
												final Double nonLogRatio = infiniteRatio
														.getNonLogRatio(condition1.getName(), condition2.getName());
												if (nonLogRatio != null) {
													RatioEx ratio = new RatioEx(nonLogRatio, condition1, condition2,
															SINGLETON_INTENSITY_RATIO, AggregationLevel.PROTEIN);
													runProtein.addRatio(ratio);
												}
											}
										}

									}
								}
								// end of ratios
								// ///////////////////////////////////////////

								// ///////////////////////////////////////////
								// singleton ratio sum intensities based and
								// singleton ratio sum counts based:
								// sum of singleton intensities of condition 1 /
								// sum ofsingleton intensities of condition 2
								// and
								// number of singleton peaks of condition 1 /
								// number of singleton peaks of condition 2
								//
								if (quantifiedProtein instanceof IsobaricQuantifiedProtein) {
									IsobaricQuantifiedProtein isoProtein = (IsobaricQuantifiedProtein) quantifiedProtein;

									final Map<QuantificationLabel, Set<Ion>> singletonIonsByLabelMap = isoProtein
											.getSingletonIonsByLabel();
									List<QuantificationLabel> quantLabelList = new ArrayList<QuantificationLabel>();
									quantLabelList.addAll(singletonIonsByLabelMap.keySet());

									for (int i = 0; i < quantLabelList.size(); i++) {
										final QuantificationLabel quantificationLabel1 = quantLabelList.get(i);
										String sample1 = getSamplesByLabel(quantificationLabel1,
												remoteFilesRatioType.getNumerator().getConditionRef(),
												remoteFilesRatioType.getDenominator().getConditionRef());
										if (sample1 == null) {
											continue;
										}
										final Condition condition1 = getConditionBySampleAndRatio(sample1,
												remoteFilesRatioType);
										Set<Ion> ions1 = singletonIonsByLabelMap.get(quantificationLabel1);
										for (int j = i + 1; j < quantLabelList.size(); j++) {
											final QuantificationLabel quantificationLabel2 = quantLabelList.get(j);
											String sample2 = getSamplesByLabel(quantificationLabel2,
													remoteFilesRatioType.getNumerator().getConditionRef(),
													remoteFilesRatioType.getDenominator().getConditionRef());
											if (sample2 == null) {
												continue;
											}
											final Condition condition2 = getConditionBySampleAndRatio(sample2,
													remoteFilesRatioType);
											Set<Ion> ions2 = singletonIonsByLabelMap.get(quantificationLabel2);
											if (!ions2.isEmpty()) {
												// singleton ratio count based
												double singletonRatioCountValue = Double.valueOf(ions1.size())
														/ Double.valueOf(ions2.size());
												// make the log2
												double log2Ratio = Math.log(singletonRatioCountValue) / Math.log(2);
												RatioEx singletonRatioCount = new RatioEx(log2Ratio, condition1,
														condition2, SINGLETON_SUM_COUNT_RATIO,
														AggregationLevel.PROTEIN);
												runProtein.addRatio(singletonRatioCount);

												// singleton ratio intensity
												// based
												double singletonRatioIntensityValue = Double
														.valueOf(getIntensitySum(ions1))
														/ Double.valueOf(getIntensitySum(ions2));
												// make the log2
												log2Ratio = Math.log(singletonRatioIntensityValue) / Math.log(2);
												RatioEx singletonRatioIntensity = new RatioEx(log2Ratio, condition1,
														condition2, SINGLETON_SUM_INTENSITIES_RATIO,
														AggregationLevel.PROTEIN);
												runProtein.addRatio(singletonRatioIntensity);
											} else {
												// ions2 is empty, so
												// denominator is
												// 0
												if (!ions1.isEmpty()) {
													// n/0 -> positive infinity,
													// log(inf) = inf
													RatioEx singletonRatioCount = new RatioEx(Double.POSITIVE_INFINITY,
															condition1, condition2, SINGLETON_SUM_COUNT_RATIO,
															AggregationLevel.PROTEIN);
													runProtein.addRatio(singletonRatioCount);
													RatioEx singletonRatioIntensity = new RatioEx(
															Double.POSITIVE_INFINITY, condition1, condition2,
															SINGLETON_SUM_INTENSITIES_RATIO, AggregationLevel.PROTEIN);
													runProtein.addRatio(singletonRatioIntensity);
												}

											}
										}
									}
								}

								// end of singleton ratio intensity based
								// ////////////////////////////////////////////

							}
						}
					}
				}
			}
		}
	}

	private Condition getConditionBySampleAndRatio(String sample1, RemoteFilesRatioType remoteFilesRatioType) {
		// log.info("getConditionBySample: " + sample1);

		Condition ret = null;
		final String numeratorConditionRef = remoteFilesRatioType.getNumerator().getConditionRef();
		// log.info("Numerator condition ref: " + numeratorConditionRef);
		if (conditionsByConditionID.get(numeratorConditionRef).getSample().getName().equals(sample1)) {
			// log.info("match with sample in numerator");
			ret = conditionsByConditionID.get(numeratorConditionRef);
		} else {
			final String denominatorConditionRef = remoteFilesRatioType.getDenominator().getConditionRef();
			// log.info("Denominator condition ref: " +
			// denominatorConditionRef);
			if (conditionsByConditionID.get(denominatorConditionRef).getSample().getName().equals(sample1)) {
				// log.info("match with sample in denominator");
				ret = conditionsByConditionID.get(denominatorConditionRef);
			} else {
				throw new IllegalArgumentException("incoherent experimental setup");
			}
		}
		// log.info("condition: " + ret.getName());
		return ret;
	}

	private long getIntensitySum(Set<Ion> ions) {
		long intensitySum = 0;
		if (ions != null) {
			for (Ion ion : ions) {
				intensitySum += ion.getIntensity();
			}
		}
		return intensitySum;
	}

	/**
	 * Looks for the samples which are pointing to the label referenced.<br>
	 * To simplify, we assume that each label is used only in one sample, so we
	 * return the first in the list
	 *
	 * @param label
	 * @param condition2ID
	 * @param condition1ID
	 * @return
	 */
	private String getSamplesByLabel(QuantificationLabel label, String condition1ID, String condition2ID) {
		List<String> ret = new ArrayList<String>();
		for (ExperimentalConditionType expConditionCfg : projectCfg.getExperimentalConditions()
				.getExperimentalCondition()) {
			if (!expConditionCfg.getId().equals(condition1ID) && !expConditionCfg.getId().equals(condition2ID))
				continue;
			if (expConditionCfg.getSampleRef() != null) {
				final SampleType sampleCfg = getSampleCfg(expConditionCfg.getSampleRef());
				if (sampleCfg.getLabelRef() != null) {
					final QuantificationLabel labelByName = QuantificationLabel.getByName(sampleCfg.getLabelRef());
					if (labelByName != null && labelByName.equals(label))
						ret.add(expConditionCfg.getSampleRef());
				}
			}
		}
		if (ret.isEmpty()) {
			return null;
		}
		if (ret.size() > 1)
			throw new IllegalArgumentException("It is not possible that more than one sample has the same label "
					+ label + " in between samples from conditions: " + condition1ID + " and " + condition2ID);
		return ret.get(0);
	}

	private void createProteinRatios(ProteinRatiosType proteinAmountRatios, Project project) {
		if (proteinAmountRatios != null && proteinAmountRatios.getExcelRatio() != null
				&& !proteinAmountRatios.getExcelRatio().isEmpty()) {
			createProteinRatiosFromExcel(proteinAmountRatios.getExcelRatio(), project);
		}
		if (proteinAmountRatios.getRemoteFilesRatio() != null && !proteinAmountRatios.getRemoteFilesRatio().isEmpty()) {
			createProteinRatiosFromRemoteFile(proteinAmountRatios.getRemoteFilesRatio());
		}

	}

	private void createProteinRatiosFromExcel(List<ExcelAmountRatioType> excelRatiosCfg, Project project) {
		if (excelRatiosCfg != null) {

			for (ExcelAmountRatioType excelRatioCfg : excelRatiosCfg) {
				List<String> msRunRefs = new ArrayList<String>();
				String msRunRefString = excelRatioCfg.getMsRunRef();
				if (msRunRefString.contains(ExcelUtils.MULTIPLE_ITEM_SEPARATOR)) {
					msRunRefs.addAll(Arrays.asList(msRunRefString.split(ExcelUtils.MULTIPLE_ITEM_SEPARATOR)));
				} else {
					msRunRefs.add(msRunRefString);
				}
				final String condition1Ref = excelRatioCfg.getNumerator().getConditionRef();
				final String condition2Ref = excelRatioCfg.getDenominator().getConditionRef();
				final ExcelColumn ratioColumn = excelReader.getExcelColumnFromReference(excelRatioCfg.getColumnRef());

				final ProteinAccessionType proteinAccessionCfg = excelRatioCfg.getProteinAccession();

				if (proteinAccessionCfg == null) {
					log.info(
							"Protein accession is null for ratio. Trying to assign the ratios to proteins in the same row...");
					int rowIndex = -1;

					for (Object ratioValueObj : ratioColumn.getValues()) {
						rowIndex++;
						if (ratioValueObj != null) {
							Double ratioValue = null;
							try {
								ratioValue = Double.valueOf(ratioValueObj.toString());
								if (Double.compare(ratioValue, Double.NaN) == 0) {
									continue;
								}
							} catch (NumberFormatException e) {
								continue;
							}
							if (ratioValue == null) {
								continue;
							}
							Sample sample1 = getSampleByCondition(condition1Ref);
							Sample sample2 = getSampleByCondition(condition2Ref);
							RatioEx ratio = new AmountRatioAdapter(excelRatioCfg.getName(), ratioValue, condition1Ref,
									condition2Ref, sample1, sample2, project, AggregationLevel.PROTEIN).adapt();
							if (excelRatioCfg.getRatioScore() != null) {
								Score score = new ScoreAdapter(excelRatioCfg.getRatioScore(), excelReader, rowIndex)
										.adapt();
								if (score != null) {
									ratio.setAssociatedConfidenceScore(score);
								}
							}

							// get proteins in the same row and the same msRun
							for (String msRunRef : msRunRefs) {
								Set<Protein> proteins1 = StaticProteomicsModelStorage.getProtein(msRunRef,
										condition1Ref, rowIndex, null);
								if (proteins1.isEmpty()) {
									throw new IllegalArgumentException("Protein in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								Set<Protein> proteins2 = StaticProteomicsModelStorage.getProtein(msRunRef,
										condition2Ref, rowIndex, null);
								if (proteins2.isEmpty()) {
									throw new IllegalArgumentException("Protein in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								Set<Protein> rowProteins = ModelUtils.getProteinIntersection(proteins1, proteins2);
								if (rowProteins.isEmpty()) {
									throw new IllegalArgumentException("Protein in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								for (Protein protein : rowProteins) {
									protein.addRatio(ratio);
								}
							}
						}
					}
				} else {
					log.info(
							"Protein accession for ratio is defined. Using protein accession to get proteins and then assign them ratios");
					final ExcelColumn proteinAccColumn = excelReader
							.getExcelColumnFromReference(proteinAccessionCfg.getColumnRef());
					int rowIndex = -1;

					for (Object ratioValueObj : ratioColumn.getValues()) {
						rowIndex++;
						if (ratioValueObj != null) {
							Double ratioValue = null;
							try {
								ratioValue = Double.valueOf(ratioValueObj.toString());
								if (Double.compare(ratioValue, Double.NaN) == 0) {
									continue;
								}
							} catch (NumberFormatException e) {
								continue;
							}

							if (ratioValue == null) {
								continue;
							}
							Sample sample1 = getSampleByCondition(condition1Ref);
							Sample sample2 = getSampleByCondition(condition2Ref);
							RatioEx ratio = new AmountRatioAdapter(excelRatioCfg.getName(), ratioValue, condition1Ref,
									condition2Ref, sample1, sample2, project, AggregationLevel.PROTEIN).adapt();
							if (excelRatioCfg.getRatioScore() != null) {
								Score score = new ScoreAdapter(excelRatioCfg.getRatioScore(), excelReader, rowIndex)
										.adapt();
								if (score != null)
									ratio.setAssociatedConfidenceScore(score);

							}

							final String rawProteinAccession = proteinAccColumn.getValues().get(rowIndex).toString();
							List<String> proteinAccsToParse = ProteinsAdapterByExcel.getTokens(
									proteinAccessionCfg.isGroups(), proteinAccessionCfg.getGroupSeparator(),
									rawProteinAccession);
							for (String proteinAcc : proteinAccsToParse) {
								final Pair<String, String> proteinAccession = FastaParser.getACC(proteinAcc);
								final List<String> uniprotAccs = new ArrayList<String>();
								if (proteinAccession.getSecondElement()
										.equals(edu.scripps.yates.utilities.model.enums.AccessionType.IPI.name())) {
									final List<UniprotEntry> entries = IPI2UniprotACCMap.getInstance()
											.map2Uniprot(proteinAcc);
									if (!entries.isEmpty()) {
										for (UniprotEntry uniprotEntry : entries) {
											uniprotAccs.add(uniprotEntry.getAcc());
										}
									} else {
										uniprotAccs.add(proteinAcc);
									}

								} else {
									uniprotAccs.addAll(proteinAccsToParse);
								}
								for (String uniprotAcc : uniprotAccs) {
									Pattern regexp = getDecoyProteinAccRegexp();
									if (regexp != null) {
										final Matcher matcher = regexp.matcher(uniprotAcc);
										if (matcher.find()) {
											continue;
										}
									}
									Set<Protein> proteins1 = StaticProteomicsModelStorage.getProtein(msRunRefs,
											condition1Ref, uniprotAcc);
									if (proteins1.isEmpty()) {
										throw new IllegalArgumentException(
												"Protein  " + uniprotAcc + " in row " + rowIndex
														+ " from the Excel file is not found to assign a ratio to it between condition "
														+ condition1Ref + " and " + condition2Ref + " in msRuns "
														+ getStringFromCollection(msRunRefs));
									}
									Set<Protein> proteins2 = StaticProteomicsModelStorage.getProtein(msRunRefs,
											condition2Ref, uniprotAcc);
									if (proteins2.isEmpty()) {
										throw new IllegalArgumentException(
												"Protein " + uniprotAcc + " in row  " + rowIndex
														+ " from the Excel file is not found to assign a ratio to it between condition "
														+ condition1Ref + " and " + condition2Ref + " in msRuns "
														+ getStringFromCollection(msRunRefs));
									}

									Set<Protein> rowProteins = ModelUtils.getProteinIntersection(proteins1, proteins2);
									if (rowProteins.isEmpty()) {
										throw new IllegalArgumentException("Protein " + proteinAcc
												+ " from the Excel file is not found to assign a ratio to it between condition "
												+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRefs);
									}
									for (Protein protein : rowProteins) {
										protein.addRatio(ratio);
									}
								}
							}

						}
					}
				}

			}
		}

	}

	// private Set<Protein>
	// getProteinsWithThatAccAndDetectedInOneCondition(String proteinAcc,
	// Project project,
	// String conditionRef) {
	// Set<Protein> ret = new HashSet<Protein>();
	//
	// Condition condition = getCondition(conditionRef, project);
	// if (condition != null) {
	// final Set<Protein> proteins = proteinMap.get(proteinAcc);
	// if (proteins != null) {
	// for (Protein protein : proteins) {
	// if (protein.getConditions().contains(condition)) {
	// ret.add(protein);
	// }
	// }
	// }
	// }
	// return ret;
	// }

	private Condition getCondition(String conditionRef, Project project) {
		if (project != null) {
			final Set<Condition> conditions = project.getConditions();
			if (conditions != null) {
				for (Condition condition : conditions) {
					if (condition.getName().equals(conditionRef)) {
						return condition;
					}
				}
			}
		}
		return null;
	}

	private Set<Protein> getProteinsWithThatAccAndDetectedInBothConditions(String proteinAcc, Project project,
			String condition1Ref, String condition2Ref) {
		Set<Protein> ret = new HashSet<Protein>();

		Condition condition1 = getCondition(condition1Ref, project);
		Condition condition2 = getCondition(condition2Ref, project);
		final Set<Protein> proteins = proteinMap.get(proteinAcc);
		if (proteins != null) {
			for (Protein protein : proteins) {
				if (protein.getConditions().contains(condition1) && protein.getConditions().contains(condition2)) {
					ret.add(protein);
				}
			}
		}
		return ret;
	}

	private void createPSMRatiosFromExcel(List<ExcelAmountRatioType> excelRatiosCfg, Project project) {
		if (excelRatiosCfg != null) {

			for (ExcelAmountRatioType excelRatioCfg : excelRatiosCfg) {
				List<String> msRunRefs = new ArrayList<String>();
				String msRunRefString = excelRatioCfg.getMsRunRef();
				if (msRunRefString.contains(",")) {
					msRunRefs.addAll(Arrays.asList(msRunRefString.split(",")));
				} else {
					msRunRefs.add(msRunRefString);
				}
				final String condition1Ref = excelRatioCfg.getNumerator().getConditionRef();
				final String condition2Ref = excelRatioCfg.getDenominator().getConditionRef();
				final ExcelColumn ratioColumn = excelReader.getExcelColumnFromReference(excelRatioCfg.getColumnRef());

				final PsmType psmIDCfg = excelRatioCfg.getPsmId();

				if (psmIDCfg == null) {
					log.info(
							"PSM identifier is null for ratio. Trying to assign the ratios to PSMs in the same row...");
					int rowIndex = -1;

					for (Object ratioValueObj : ratioColumn.getValues()) {
						rowIndex++;
						if (ratioValueObj != null) {
							Double ratioValue = null;
							try {
								ratioValue = Double.valueOf(ratioValueObj.toString());
								if (Double.compare(ratioValue, Double.NaN) == 0) {
									continue;
								}
							} catch (NumberFormatException e) {
								continue;
							}
							if (ratioValue == null) {
								continue;
							}
							Sample sample1 = getSampleByCondition(condition1Ref);
							Sample sample2 = getSampleByCondition(condition2Ref);
							RatioEx ratio = new AmountRatioAdapter(excelRatioCfg.getName(), ratioValue, condition1Ref,
									condition2Ref, sample1, sample2, project, AggregationLevel.PSM).adapt();
							if (excelRatioCfg.getRatioScore() != null) {
								Score score = new ScoreAdapter(excelRatioCfg.getRatioScore(), excelReader, rowIndex)
										.adapt();
								if (score != null) {
									ratio.setAssociatedConfidenceScore(score);
								}
							}

							// get psms in the same row and the same msRun
							for (String msRunRef : msRunRefs) {
								Set<PSM> psms1 = StaticProteomicsModelStorage.getPSM(msRunRef, condition1Ref, rowIndex,
										null);
								if (psms1.isEmpty()) {
									throw new IllegalArgumentException("PSM in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								Set<PSM> psms2 = StaticProteomicsModelStorage.getPSM(msRunRef, condition2Ref, rowIndex,
										null);
								if (psms2.isEmpty()) {
									throw new IllegalArgumentException("PSM in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								Set<PSM> rowPSMs = ModelUtils.getPSMIntersection(psms1, psms2);
								if (rowPSMs.isEmpty()) {
									throw new IllegalArgumentException("PSM in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								for (PSM psm : rowPSMs) {
									psm.addRatio(ratio);
								}
							}
						}
					}
				} else {
					int rowIndex = -1;

					final ExcelColumn psmIDColumn = excelReader.getExcelColumnFromReference(psmIDCfg.getColumnRef());
					for (Object ratioValueObj : ratioColumn.getValues()) {
						rowIndex++;
						String psmID = null;
						if (psmIDColumn != null && psmIDColumn.getValues().size() > rowIndex) {
							psmID = (String) psmIDColumn.getValues().get(rowIndex);
						}
						if (ratioValueObj != null) {
							double ratioValue = Double.valueOf(ratioValueObj.toString());
							Sample sample1 = getSampleByCondition(condition1Ref);
							Sample sample2 = getSampleByCondition(condition2Ref);
							RatioEx ratio = new AmountRatioAdapter(excelRatioCfg.getName(), ratioValue, condition1Ref,
									condition2Ref, sample1, sample2, project, AggregationLevel.PSM).adapt();
							if (excelRatioCfg.getRatioScore() != null) {
								Score score = new ScoreAdapter(excelRatioCfg.getRatioScore(), excelReader, rowIndex)
										.adapt();
								if (score != null)
									ratio.setAssociatedConfidenceScore(score);

							}

							for (String msRunRef : msRunRefs) {
								final Set<PSM> rowPSMCondition1 = StaticProteomicsModelStorage.getPSM(msRunRef,
										condition1Ref, psmID);
								if (rowPSMCondition1.isEmpty()) {
									throw new IllegalArgumentException("PSM id " + psmID
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								final Set<PSM> rowPSMCondition2 = StaticProteomicsModelStorage.getPSM(msRunRef,
										condition2Ref, psmID);
								if (rowPSMCondition2.isEmpty()) {
									throw new IllegalArgumentException("PSM id " + psmID
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								final Set<PSM> rowPSM = ModelUtils.getPSMIntersection(rowPSMCondition1,
										rowPSMCondition2);
								if (rowPSM.isEmpty()) {
									throw new IllegalArgumentException("PSM id " + psmID
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								for (PSM psm : rowPSM) {
									psm.addRatio(ratio);
								}
							}

						}

					}

				}
			}
		}
	}

	private void createPeptideRatiosFromExcel(List<ExcelAmountRatioType> excelRatiosCfg, Project project) {
		if (excelRatiosCfg != null) {

			for (ExcelAmountRatioType excelRatioCfg : excelRatiosCfg) {
				List<String> msRunRefs = new ArrayList<String>();
				String msRunRefString = excelRatioCfg.getMsRunRef();
				if (msRunRefString.contains(",")) {
					msRunRefs.addAll(Arrays.asList(msRunRefString.split(",")));
				} else {
					msRunRefs.add(msRunRefString);
				}
				final String condition1Ref = excelRatioCfg.getNumerator().getConditionRef();
				final String condition2Ref = excelRatioCfg.getDenominator().getConditionRef();
				final ExcelColumn ratioColumn = excelReader.getExcelColumnFromReference(excelRatioCfg.getColumnRef());
				final SequenceType peptideSequenceCfg = excelRatioCfg.getPeptideSequence();

				if (peptideSequenceCfg == null) {
					log.info(
							"Peptide sequence is null for ratio. Trying to assign the ratios to peptides in the same row...");
					int rowIndex = -1;

					for (Object ratioValueObj : ratioColumn.getValues()) {
						rowIndex++;
						if (ratioValueObj != null) {
							Double ratioValue = null;
							try {
								ratioValue = Double.valueOf(ratioValueObj.toString());
								if (Double.compare(ratioValue, Double.NaN) == 0) {
									continue;
								}
							} catch (NumberFormatException e) {
								continue;
							}
							if (ratioValue == null) {
								continue;
							}
							Sample sample1 = getSampleByCondition(condition1Ref);
							Sample sample2 = getSampleByCondition(condition2Ref);
							RatioEx ratio = new AmountRatioAdapter(excelRatioCfg.getName(), ratioValue, condition1Ref,
									condition2Ref, sample1, sample2, project, AggregationLevel.PEPTIDE).adapt();
							if (excelRatioCfg.getRatioScore() != null) {
								Score score = new ScoreAdapter(excelRatioCfg.getRatioScore(), excelReader, rowIndex)
										.adapt();
								if (score != null) {
									ratio.setAssociatedConfidenceScore(score);
								}
							}

							// get peptides in the same row and the same msRun
							for (String msRunRef : msRunRefs) {

								final Set<Peptide> rowPeptideCondition1 = StaticProteomicsModelStorage
										.getPeptide(msRunRef, condition1Ref, rowIndex, null);
								if (rowPeptideCondition1.isEmpty()) {
									throw new IllegalArgumentException("Peptide in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								final Set<Peptide> rowPeptideCondition2 = StaticProteomicsModelStorage
										.getPeptide(msRunRef, condition2Ref, rowIndex, null);
								if (rowPeptideCondition2.isEmpty()) {
									throw new IllegalArgumentException("Peptide in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								Set<Peptide> rowPeptides = ModelUtils.getPeptideIntersection(rowPeptideCondition1,
										rowPeptideCondition2);
								if (rowPeptides.isEmpty()) {
									throw new IllegalArgumentException("Peptide in row " + rowIndex
											+ " from the Excel file is not found to assign a ratio to it between condition "
											+ condition1Ref + " and " + condition2Ref + " in msRun " + msRunRef);
								}
								for (Peptide peptide : rowPeptides) {
									peptide.addRatio(ratio);
								}
							}
						}
					}
				} else {
					log.info(
							"Peptide sequence for ratio is defined. Using peptide sequence to get peptides and then assign them ratios");

					final ExcelColumn peptideSequenceColumn = excelReader
							.getExcelColumnFromReference(peptideSequenceCfg.getColumnRef());
					int rowIndex = -1;
					for (Object ratioValueObj : ratioColumn.getValues()) {
						rowIndex++;
						if (ratioValueObj != null) {
							String peptideSequence = null;
							if (peptideSequenceColumn != null && peptideSequenceColumn.getValues().size() > rowIndex) {
								peptideSequence = (String) peptideSequenceColumn.getValues().get(rowIndex);
							}
							double ratioValue = Double.valueOf(ratioValueObj.toString());
							Sample sample1 = getSampleByCondition(condition1Ref);
							Sample sample2 = getSampleByCondition(condition2Ref);
							RatioEx ratio = new AmountRatioAdapter(excelRatioCfg.getName(), ratioValue, condition1Ref,
									condition2Ref, sample1, sample2, project, AggregationLevel.PEPTIDE).adapt();
							if (excelRatioCfg.getRatioScore() != null) {
								Score score = new ScoreAdapter(excelRatioCfg.getRatioScore(), excelReader, rowIndex)
										.adapt();
								if (score != null)
									ratio.setAssociatedConfidenceScore(score);

							}

							// get the Peptides in those msRuns
							final Set<Peptide> rowPeptideCondition1 = StaticProteomicsModelStorage.getPeptide(msRunRefs,
									condition1Ref, peptideSequence);
							if (rowPeptideCondition1.isEmpty()) {
								throw new IllegalArgumentException("Peptide " + peptideSequence
										+ " from the Excel file is not found to assign a ratio to it between condition "
										+ condition1Ref + " and " + condition2Ref + " in msRuns "
										+ getStringFromCollection(msRunRefs));
							}
							final Set<Peptide> rowPeptideCondition2 = StaticProteomicsModelStorage.getPeptide(msRunRefs,
									condition2Ref, peptideSequence);
							if (rowPeptideCondition2.isEmpty()) {
								throw new IllegalArgumentException("Peptide " + peptideSequence
										+ " from the Excel file is not found to assign a ratio to it between condition "
										+ condition1Ref + " and " + condition2Ref + " in msRuns "
										+ getStringFromCollection(msRunRefs));
							}
							Set<Peptide> rowPeptides = ModelUtils.getPeptideIntersection(rowPeptideCondition1,
									rowPeptideCondition2);
							if (rowPeptides.isEmpty()) {
								throw new IllegalArgumentException("Peptide " + peptideSequence
										+ " from the Excel file is not found to assign a ratio to it between condition "
										+ condition1Ref + " and " + condition2Ref + " in msRuns "
										+ getStringFromCollection(msRunRefs));
							}
							// Set<HasConditions> hasconditions = new
							// HashSet<HasConditions>();
							// hasconditions.addAll(rowPeptide);
							// hasconditions =
							// areDetectedOnBothConditions(hasconditions,
							// condition1Ref, condition2Ref);
							// add the same ratio to all of the
							// PSMs
							for (Peptide rowPeptide : rowPeptides) {
								// Peptide peptide = (Peptide) hasCondition;
								rowPeptide.addRatio(ratio);
							}

						}

					}
				}
			}
		}
	}

	// /**
	// * Search for the condition in the conditions of the proteins and return
	// * true if both are found
	// *
	// * @param hasConditions
	// * @param condition1Ref
	// * @param condition2Ref
	// * @return
	// */
	// private Set<HasConditions> areDetectedOnBothConditions(Set<HasConditions>
	// hasConditions, String condition1Ref,
	// String condition2Ref) {
	// Set<HasConditions> ret = new HashSet<HasConditions>();
	//
	// for (HasConditions hasCondition : hasConditions) {
	// boolean condition1Found = false;
	// boolean condition2Found = false;
	// final Set<Condition> conditions = hasCondition.getConditions();
	// for (Condition condition : conditions) {
	// if (condition.getName().equals(condition1Ref))
	// condition1Found = true;
	// if (condition.getName().equals(condition2Ref))
	// condition2Found = true;
	// }
	// if (condition1Found && condition2Found)
	// ret.add(hasCondition);
	// }
	//
	// return ret;
	// }
	private String getStringFromCollection(Collection<String> collection) {
		if (collection == null)
			return "";
		List<String> list = new ArrayList<String>();
		list.addAll(collection);
		Collections.sort(list);
		StringBuilder sb = new StringBuilder();
		for (String string : list) {
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(string);
		}
		return sb.toString();
	}

	/**
	 * Gets the {@link Sample} that is referring the condition with id equals to
	 * the parameter
	 *
	 * @param conditionId
	 * @return
	 */
	private Sample getSampleByCondition(String conditionId) {
		for (ExperimentalConditionType expConditionCfg : projectCfg.getExperimentalConditions()
				.getExperimentalCondition()) {
			if (expConditionCfg.getId().equals(conditionId)) {
				return new SampleAdapter(getSampleCfg(expConditionCfg.getSampleRef()),
						projectCfg.getExperimentalDesign().getOrganismSet(),
						projectCfg.getExperimentalDesign().getTissueSet(),
						projectCfg.getExperimentalDesign().getLabelSet()).adapt();
			}
		}
		throw new IllegalArgumentException("Sample from referenced condition '" + conditionId + "' is not found");
	}

	/**
	 * Gets the {@link SampleType} from the {@link ExperimentalDesignType} with
	 * Id equals to the parameter
	 *
	 * @param sampleRef
	 * @return
	 */
	private SampleType getSampleCfg(String sampleRef) {
		for (SampleType sampleCfg : projectCfg.getExperimentalDesign().getSampleSet().getSample()) {
			if (sampleCfg.getId().equals(sampleRef))
				return sampleCfg;
		}
		throw new IllegalArgumentException("Sample referenced as '" + sampleRef + "' is not found");

	}

	private void clearStaticInformation() {
		log.info("Clearing static information");
		ConditionAdapter.clearStaticInformation();
		LabelAdapter.clearStaticInformation();
		MSRunAdapter.clearStaticInformation();
		OrganismAdapter.clearStaticInformation();
		SampleAdapter.clearStaticInformation();
		TissueAdapter.clearStaticInformation();

		// ProteinsAdapterByExcel.clearStaticInformation();
		// PSMAdapterByExcel.clearStaticInformation();
		// PeptideAdapterByExcel.clearStaticInformation();
		// ProteinsAdapterByExcel.clearStaticInformationByRow();
		// PSMAdapterByExcel.clearStaticInformationByRow();
		// PeptideAdapterByExcel.clearStaticInformationByRow();

		StaticProteomicsModelStorage.clearData();
	}

}
