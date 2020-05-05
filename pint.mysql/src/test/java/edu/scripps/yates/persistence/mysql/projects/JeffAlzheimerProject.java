package edu.scripps.yates.persistence.mysql.projects;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.CensusOutParser;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.ExcelReader;
import edu.scripps.yates.excel.ExcelSheet;
import edu.scripps.yates.excel.impl.ExcelFileImpl;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ColumnType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ConditionRefType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAccessionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinRatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PsmRatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServersType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.TissueSetType;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class JeffAlzheimerProject {
	private final Logger log = Logger.getLogger(JeffAlzheimerProject.class);

	private final static File censusFilesRootFolder = new File(
			"C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Alzheimer dataset Jeff\\Census files");
	private final static File finalFDRFile = new File(
			"C:\\Users\\Salva\\Desktop\\Dropbox\\Alzheimers proteomics\\FDR_data\\Summary_WithIDs.xlsx");
	private final static String path = "C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Alzheimer dataset Jeff";
	private JAXBContext jaxbContext;
	private IdDescriptionType mouse;
	private final Map<String, List<String>> replicateNumbersByCondition = new THashMap<String, List<String>>();

	private List<MsRunType> msRuns;

	private Map<String, MsRunType> msRunMap;

	private ArrayList<FileType> censusFiles;

	private List<FileType> ratio2QuantCompareFiles;

	@Before
	public void before() {
		Logger.getRootLogger().setLevel(Level.INFO);
		try {
			jaxbContext = JAXBContext.newInstance("edu.scripps.yates.excel.proteindb.importcfg.jaxb");
			readMsRunsFromFile();
		} catch (final JAXBException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void createProjectFile() {
		final PintImportCfg root = new PintImportCfg();
		try {
			final Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));

			final ProjectType project = new ProjectType();
			project.setName("Alzheimer project");
			project.setTag("Alzheimer");
			project.setMsRuns(getMsRuns());
			project.setExperimentalDesign(getExperimentalDesign());
			project.setExperimentalConditions(getExperimentalConditions());

			project.setRatios(getRatios1());
			addRatio2(project.getRatios(), getRatios2());
			// project.setRatios(getRatios2());
			project.setDescription("Alzheimer project");
			final ServersType servers = new ServersType();
			servers.getServer().add(getJainaServer());
			servers.getServer().add(getSealionServer());
			root.setServers(servers);
			root.setFileSet(getFileSet());
			root.setProject(project);

			final List<ExperimentalConditionType> experimentalConditions = new ArrayList<ExperimentalConditionType>();
			experimentalConditions.addAll(project.getExperimentalConditions().getExperimentalCondition());
			final List<RemoteFilesRatioType> proteinRatios = new ArrayList<RemoteFilesRatioType>();
			proteinRatios.addAll(project.getRatios().getProteinAmountRatios().getRemoteFilesRatio());
			final List<ExcelAmountRatioType> proteinRatios2 = new ArrayList<ExcelAmountRatioType>();
			proteinRatios2.addAll(project.getRatios().getProteinAmountRatios().getExcelRatio());
			final List<RemoteFilesRatioType> psmRatios = new ArrayList<RemoteFilesRatioType>();
			psmRatios.addAll(project.getRatios().getPsmAmountRatios().getRemoteFilesRatio());

			for (final Model model : Model.values()) {
				for (final TimePoint timePoint : TimePoint.values()) {
					for (final BrainRegion brainRegion : BrainRegion.values()) {
						final Set<ExperimentalConditionType> trio = new THashSet<ExperimentalConditionType>();
						for (final MouseType mouse : MouseType.values()) {
							String conditionID = getConditionID(model, mouse, timePoint, brainRegion);
							if (conditionID.startsWith("pd")) {
								conditionID = conditionID.replace("pd", "pdapp");
							}
							for (final ExperimentalConditionType experimentalConditionType : experimentalConditions) {
								final String id = experimentalConditionType.getId();
								if (conditionID.equals(id)) {
									trio.add(experimentalConditionType);
									break;
								}
							}
						}

						if (trio.size() == 3) {
							project.getExperimentalConditions().getExperimentalCondition().clear();
							project.getRatios().getProteinAmountRatios().getRemoteFilesRatio().clear();
							project.getRatios().getProteinAmountRatios().getExcelRatio().clear();
							project.getRatios().getPsmAmountRatios().getRemoteFilesRatio().clear();
							for (final ExperimentalConditionType condition : trio) {
								project.getExperimentalConditions().getExperimentalCondition().add(condition);
								for (final RemoteFilesRatioType proteinRatio : proteinRatios) {
									if (proteinRatio.getNumerator().getConditionRef().equals(condition.getId())
											|| proteinRatio.getDenominator().getConditionRef()
													.equals(condition.getId())) {
										if (!project.getRatios().getProteinAmountRatios().getRemoteFilesRatio()
												.contains(proteinRatio)) {
											project.getRatios().getProteinAmountRatios().getRemoteFilesRatio()
													.add(proteinRatio);
										}
									}
								}
								for (final ExcelAmountRatioType proteinRatio2 : proteinRatios2) {
									if (proteinRatio2.getNumerator().getConditionRef().equals(condition.getId())
											|| proteinRatio2.getDenominator().getConditionRef()
													.equals(condition.getId())) {
										if (!project.getRatios().getProteinAmountRatios().getExcelRatio()
												.contains(proteinRatio2)) {
											project.getRatios().getProteinAmountRatios().getExcelRatio()
													.add(proteinRatio2);
										}
									}
								}
								for (final RemoteFilesRatioType psmRatio : psmRatios) {
									if (psmRatio.getNumerator().getConditionRef().equals(condition.getId())
											|| psmRatio.getDenominator().getConditionRef().equals(condition.getId())) {
										if (!project.getRatios().getPsmAmountRatios().getRemoteFilesRatio()
												.contains(psmRatio)) {
											project.getRatios().getPsmAmountRatios().getRemoteFilesRatio()
													.add(psmRatio);
										}
									}
								}
							}

							trio.clear();
							final String conditionName = getConditionID(model, null, timePoint, brainRegion);
							// conditionName = conditionName.substring(0,
							// conditionName.length() - 4);
							final File outputFile = new File(
									path + File.separator + "alzheimerProject_NEW_" + conditionName + ".xml");
							marshaller.marshal(root, outputFile);
						}

					}
				}
			}
		} catch (final JAXBException e) {
			e.printStackTrace();
			fail();
		} catch (final Exception e2) {
			e2.printStackTrace();
		}
	}

	private void addRatio2(RatiosType ratios, RatiosType ratios2) {

		if (ratios2.getProteinAmountRatios() != null) {
			ratios.getProteinAmountRatios().getExcelRatio().addAll(ratios2.getProteinAmountRatios().getExcelRatio());
		}
	}

	private RatiosType getRatios1() {
		final RatiosType ret = new RatiosType();
		final PsmRatiosType psmRatios = new PsmRatiosType();
		ret.setPsmAmountRatios(psmRatios);
		final ProteinRatiosType proteinRatios = new ProteinRatiosType();
		ret.setProteinAmountRatios(proteinRatios);
		for (final MouseType mouse : MouseType.values()) {
			if (mouse == MouseType.control) {
				continue;
			}
			for (final Model model : Model.values()) {
				for (final TimePoint timePoint : TimePoint.values()) {
					for (final BrainRegion brainRegion : BrainRegion.values()) {
						final List<String> replicateList = replicateNumbersByCondition
								.get(getConditionID(model, mouse, timePoint, brainRegion));
						for (final String replicate : replicateList) {

							final RemoteFilesRatioType ratioType = new RemoteFilesRatioType();
							ratioType.setMsRunRef(getMsRun(model, mouse, timePoint, brainRegion, replicate).getId());
							ratioType.setDiscardDecoys(getDiscardDecoy());
							final ExperimentalConditionType experimentalCondition1 = getExperimentalCondition(model,
									mouse, timePoint, brainRegion);
							ratioType.setNumerator(getConditionRefType(experimentalCondition1));
							final ExperimentalConditionType controlCondition = getExperimentalCondition(model,
									MouseType.control, timePoint, brainRegion);
							ratioType.setDenominator(getConditionRefType(controlCondition));
							ratioType.setName("Ratio 1");
							final FileType censusFile = getCensusFile(model, mouse, timePoint, brainRegion, replicate);

							ratioType.setFileRef(censusFile.getId());

							psmRatios.getRemoteFilesRatio().add(ratioType);
							// proteinRatios.getRemoteFilesRatio().add(ratioType);
						}
					}
				}
			}
		}
		return ret;
	}

	private RatiosType getRatios2() {
		final RatiosType ret = new RatiosType();

		final ProteinRatiosType proteinRatios = new ProteinRatiosType();
		ret.setProteinAmountRatios(proteinRatios);

		for (final Model model : Model.values()) {
			for (final TimePoint timePoint : TimePoint.values()) {
				for (final BrainRegion brainRegion : BrainRegion.values()) {
					final List<String> replicateListWT = replicateNumbersByCondition
							.get(getConditionID(model, MouseType.wt, timePoint, brainRegion));
					final List<String> replicateListAD = replicateNumbersByCondition
							.get(getConditionID(model, MouseType.ad, timePoint, brainRegion));
					final ExcelAmountRatioType ratioType = new ExcelAmountRatioType();
					ratioType.setName("Ratio 2");
					ratioType.setColumnRef(getRatio2ColumnRef("ratio", model, timePoint, brainRegion));
					ratioType.setProteinAccession(getRatio2ProteinAccessionType(model, timePoint, brainRegion));
					ratioType.setRatioScore(getRatio2ScoreType(model, timePoint, brainRegion));
					final ExperimentalConditionType wtCondition = getExperimentalCondition(model, MouseType.wt,
							timePoint, brainRegion);
					ratioType.setNumerator(getConditionRefType(wtCondition));
					final ExperimentalConditionType adCondition = getExperimentalCondition(model, MouseType.ad,
							timePoint, brainRegion);
					ratioType.setDenominator(getConditionRefType(adCondition));

					proteinRatios.getExcelRatio().add(ratioType);
					final StringBuilder sb = new StringBuilder();
					for (final String replicate : replicateListWT) {
						if (!"".equals(sb.toString())) {
							sb.append(",");
						}
						sb.append(getMsRun(model, MouseType.wt, timePoint, brainRegion, replicate).getId());
					}
					for (final String replicate : replicateListAD) {
						if (!"".equals(sb.toString())) {
							sb.append(",");
						}
						sb.append(getMsRun(model, MouseType.ad, timePoint, brainRegion, replicate).getId());
					}
					ratioType.setMsRunRef(sb.toString());
				}
			}
		}
		return ret;

	}

	private ScoreType getRatio2ScoreType(Model model, TimePoint timePoint, BrainRegion brainRegion) {
		final ScoreType ret = new ScoreType();
		ret.setScoreName("FDR");
		ret.setScoreType("False discovery rate");
		ret.setColumnRef(getRatio2ColumnRef("FDR", model, timePoint, brainRegion));
		return ret;
	}

	private ProteinAccessionType getRatio2ProteinAccessionType(Model model, TimePoint timePoint,
			BrainRegion brainRegion) {
		final ProteinAccessionType ret = new ProteinAccessionType();
		ret.setGroups(false);
		ret.setRegexp(".*");
		ret.setColumnRef(getRatio2ColumnRef("locus", model, timePoint, brainRegion));
		return ret;
	}

	private String getRatio2ColumnRef(String headerString, Model model, TimePoint timePoint, BrainRegion brainRegion) {
		final FileType ratio2FileType = getRatio2FileType(model, timePoint, brainRegion);
		final List<ColumnType> column = ratio2FileType.getSheets().getSheet().get(0).getColumn();
		for (final ColumnType columnType : column) {
			if (columnType.getHeader().equals(headerString)) {
				return columnType.getId();
			}
		}
		throw new IllegalArgumentException("ratio2 column not found");
	}

	private String getConditionID(Model model, MouseType mouse2, TimePoint timePoint, BrainRegion brainRegion) {
		final StringBuilder sb = new StringBuilder();
		sb.append(model.name);

		if (mouse2 != null) {
			sb.append("_").append(mouse2);
		}
		sb.append("_").append(timePoint).append("_").append(brainRegion);
		// sb.append(model.name).append("_").append(timePoint).append("_").append(brainRegion);

		return sb.toString().toLowerCase();
	}

	private FileType getCensusFile(Model model, MouseType mouse, TimePoint timePoint, BrainRegion brainRegion,
			String replicate) {
		final String lowerCase = getFileId(model, mouse, timePoint, brainRegion, replicate).toLowerCase() + "_Quant";
		for (final FileType fileType : getCensusFiles()) {
			final String fileId = fileType.getId();

			if (fileId.toLowerCase().equals(lowerCase.toLowerCase())) {
				return fileType;
			}
		}
		throw new IllegalArgumentException("Census file not found");
	}

	private String getFileId(Model model, MouseType mouse, TimePoint timePoint, BrainRegion brainRegion,
			String replicate) {
		final StringBuilder sb = new StringBuilder();
		sb.append("File_" + getConditionID(model, mouse, timePoint, brainRegion)).append("_").append(replicate);
		return sb.toString();
	}

	private List<FileType> getCensusFiles() {
		if (censusFiles == null) {
			censusFiles = new ArrayList<FileType>();

			final List<File> files = getFiles(censusFilesRootFolder, "txt");
			for (final File file : files) {
				final FileType fileType = new FileType();
				fileType.setFormat(FormatType.CENSUS_OUT_TXT);
				fileType.setServerRef(getSealionServer().getId());
				fileType.setName(FilenameUtils.getName(file.getAbsolutePath()));
				Model model = null;
				final Model[] values = Model.values();
				for (final Model model2 : values) {
					if (file.getAbsolutePath().contains(model2.name())) {
						model = model2;
					}
				}
				final String id = "File_" + model.name + "_"
						+ FilenameUtils.getBaseName(file.getAbsolutePath()).trim().toLowerCase() + "_Quant";
				fileType.setId(id);
				fileType.setRelativePath("/home/salvador/PInt/data/Alzheimer dataset/Census files/");

				censusFiles.add(fileType);
			}
		}
		return censusFiles;
	}

	@Test
	public void renameFiles() {
		final List<File> files = getFiles(censusFilesRootFolder, "txt");
		files.addAll(getFiles(censusFilesRootFolder, "xlsx"));
		for (final File file : files) {
			String name = FilenameUtils.getName(file.getAbsolutePath());
			for (int i = 1; i < 4; i++) {
				name = name.replace(" ", "");
				final String newFileName = file.getParentFile().getAbsolutePath() + File.separator + name;
				if (!newFileName.equals(file.getAbsolutePath())) {
					// file.renameTo(new File(newFileName));
				}
			}
		}

	}

	private ConditionRefType getConditionRefType(ExperimentalConditionType experimentalCondition) {
		final ConditionRefType ret = new ConditionRefType();
		ret.setConditionRef(experimentalCondition.getId());
		return ret;
	}

	private ExperimentalDesignType getExperimentalDesign() {
		final ExperimentalDesignType ret = new ExperimentalDesignType();
		ret.setSampleSet(getSampleSet());
		ret.setOrganismSet(getOrganismSet());
		ret.setTissueSet(getTissueSet());
		ret.setLabelSet(getLabelSet());
		return ret;
	}

	private LabelSetType getLabelSet() {
		final LabelSetType ret = new LabelSetType();
		ret.getLabel().add(getLabel(N15Label.N14));
		ret.getLabel().add(getLabel(N15Label.N15));
		return ret;
	}

	private TissueSetType getTissueSet() {
		final TissueSetType ret = new TissueSetType();
		ret.getTissue().add(getTissue(BrainRegion.cer));
		ret.getTissue().add(getTissue(BrainRegion.ctx));
		ret.getTissue().add(getTissue(BrainRegion.hip));

		return ret;
	}

	private OrganismSetType getOrganismSet() {
		final OrganismSetType ret = new OrganismSetType();
		ret.getOrganism().add(getMouse());
		return ret;
	}

	private enum Model {
		N5("N5"), PDAPP("PD");

		String name;

		Model(String name) {
			this.name = name;
		}

		static Model getByName(String name) {
			final Model[] values = Model.values();
			for (final Model model : values) {
				if (model.name.equals(name)) {
					return model;
				}
			}
			return null;
		}
	};

	private enum MouseType {
		ad, wt, control
	};

	private enum TimePoint {
		pre, pos
	};

	private enum BrainRegion {
		ctx, hip, cer
	};

	private enum N15Label {
		N14, N15
	};

	private SampleSetType getSampleSet() {
		final SampleSetType ret = new SampleSetType();
		for (final Model model : Model.values()) {
			for (final MouseType mouse : MouseType.values()) {
				for (final TimePoint timePoint : TimePoint.values()) {
					for (final BrainRegion brainRegion : BrainRegion.values()) {

						ret.getSample()
								.add(getSample(model, mouse, timePoint, brainRegion, getLabelByMouseType(mouse)));

					}
				}
			}
		}
		return ret;
	}

	private N15Label getLabelByMouseType(MouseType mouse) {
		if (mouse == MouseType.control) {
			return N15Label.N15;
		} else {
			return N15Label.N14;
		}
	}

	private ExperimentalConditionsType getExperimentalConditions() {
		final ExperimentalConditionsType ret = new ExperimentalConditionsType();
		for (final Model model : Model.values()) {
			for (final MouseType mouse : MouseType.values()) {
				for (final TimePoint timePoint : TimePoint.values()) {
					for (final BrainRegion brainRegion : BrainRegion.values()) {
						ret.getExperimentalCondition()
								.add(getExperimentalCondition(model, mouse, timePoint, brainRegion));

					}
				}
			}
		}
		return ret;
	}

	private ExperimentalConditionType getExperimentalCondition(Model model, MouseType mouseType, TimePoint timepoint,
			BrainRegion brainregion) {
		final ExperimentalConditionType ret = new ExperimentalConditionType();
		final N15Label label = getLabelByMouseType(mouseType);
		ret.setId(model.name().toLowerCase() + "_" + mouseType.name() + "_" + timepoint.name() + "_"
				+ brainregion.name());
		ret.setSampleRef(getSample(model, mouseType, timepoint, brainregion, label).getId());
		ret.setDescription("Model: " + model.name() + ", Mouse type: " + mouseType.name() + ", time-point: "
				+ timepoint.name() + ", brain region: " + brainregion.name() + ", labelled: " + label.name());
		final IdentificationInfoType identInfoType = new IdentificationInfoType();
		ret.setIdentificationInfo(identInfoType);
		// QuantificationInfoType quantInfoType = new QuantificationInfoType();
		// ret.setQuantificationInfo(quantInfoType);

		final String key = getConditionID(model, mouseType, timepoint, brainregion);

		if (replicateNumbersByCondition.containsKey(key)) {
			final List<String> list = replicateNumbersByCondition.get(key);
			for (final String replicateNumber : list) {
				identInfoType.getRemoteFilesIdentInfo()
						.add(getRemoteInfo(model, mouseType, timepoint, brainregion, replicateNumber));
			}
		} else {
			System.out.println(key);
		}

		// if (replicateNumbersByCondition.containsKey(key)) {
		// final List<String> list = replicateNumbersByCondition.get(key);
		// for (String replicateNumber : list) {
		// quantInfoType.getRemoteFilesQuantInfo()
		// .add(getQuantRemoteInfo(model, mouseType, timepoint, brainregion,
		// replicateNumber));
		// }
		// } else {
		// System.out.println(key);
		// }

		return ret;
	}

	private SampleType getSample(Model model, MouseType mouseType, TimePoint timepoint, BrainRegion brainregion,
			N15Label label) {
		final SampleType ret = new SampleType();
		ret.setId("Sample_" + model.name() + "_" + mouseType.name() + "_" + timepoint.name() + "_" + brainregion.name()
				+ "_" + label.name());
		ret.setDescription(ret.getId());
		ret.setOrganismRef(getMouse().getId());
		ret.setTissueRef(getTissue(brainregion).getId());
		ret.setWt(mouseType == MouseType.wt);
		ret.setOrganismRef(getMouse().getId());
		ret.setTissueRef(getTissue(brainregion).getId());
		ret.setLabelRef(getLabel(label).getId());
		return ret;
	}

	private LabelType getLabel(N15Label label) {
		final LabelType ret = new LabelType();
		ret.setId(label.name());
		return ret;
	}

	private IdDescriptionType getTissue(BrainRegion brainregion) {
		final IdDescriptionType ret = new IdDescriptionType();
		ret.setId(brainregion.name());
		return ret;
	}

	private IdDescriptionType getMouse() {
		if (mouse == null) {
			mouse = new IdDescriptionType();
			mouse.setId("10090");
			mouse.setDescription("Mus musculus");
		}
		return mouse;
	}

	private RemoteInfoType getRemoteInfo(Model model, MouseType mouseType, TimePoint timepoint, BrainRegion brainregion,
			String replicateNumber) {
		final RemoteInfoType ret = new RemoteInfoType();
		ret.setDiscardDecoys(getDiscardDecoy());
		ret.setMsRunRef(getMsRun(model, mouseType, timepoint, brainregion, replicateNumber).getId());
		ret.getFileRef().add(getFileReferenceType(model, mouseType, timepoint, brainregion, replicateNumber));
		return ret;
	}

	private RemoteInfoType getQuantRemoteInfo(Model model, MouseType mouseType, TimePoint timepoint,
			BrainRegion brainregion, String replicateNumber) {
		final RemoteInfoType ret = new RemoteInfoType();
		ret.setDiscardDecoys(getDiscardDecoy());
		ret.setMsRunRef(getMsRun(model, mouseType, timepoint, brainregion, replicateNumber).getId());
		ret.getFileRef().add(getQuantFileReferenceType(model, mouseType, timepoint, brainregion, replicateNumber));
		return ret;
	}

	private FileReferenceType getQuantFileReferenceType(Model model, MouseType mouseType, TimePoint timepoint,
			BrainRegion brainregion, String replicateNumber) {
		final FileReferenceType ret = new FileReferenceType();

		final List<FileType> fileFromFile = getCensusFiles();
		final MsRunType msRun = getMsRun(model, mouseType, timepoint, brainregion, replicateNumber);
		for (final FileType fileType : fileFromFile) {
			if (fileType.getId().equals("File_" + msRun.getId() + "_Quant")) {
				ret.setFileRef(fileType.getId());
			}
		}
		if (ret.getFileRef() == null) {
			throw new IllegalArgumentException("File ref not found");
		}
		return ret;
	}

	private FileReferenceType getFileReferenceType(Model model, MouseType mouseType, TimePoint timepoint,
			BrainRegion brainregion, String replicateNumber) {
		final FileReferenceType ret = new FileReferenceType();

		final List<FileType> fileFromFile = getDTASelectFileTypes();
		final MsRunType msRun = getMsRun(model, mouseType, timepoint, brainregion, replicateNumber);
		for (final FileType fileType : fileFromFile) {
			if (fileType.getId().equals("File_" + msRun.getId())) {
				ret.setFileRef(fileType.getId());
			}
		}
		if (ret.getFileRef() == null) {
			throw new IllegalArgumentException("File ref not found");
		}
		return ret;
	}

	private MsRunType getMsRun(Model model, MouseType mouseType, TimePoint timepoint, BrainRegion brainregion,
			String replicateNumber) {
		final MsRunsType msRuns = getMsRuns();
		for (final MsRunType msRunType : msRuns.getMsRun()) {
			if (getModelFromMsRun(msRunType) == model) {
				if (mouseType == MouseType.control || getMouseTypeFromMsRun(msRunType) == mouseType) {
					if (getTimePointFromMsRun(msRunType) == timepoint) {
						if (getBrainRegionFromMsRun(msRunType) == brainregion) {
							final String replicateNameFromMsRun = getReplicateNameFromMsRun(msRunType);
							if (replicateNameFromMsRun.equals(replicateNumber)) {
								return msRunType;
							}
						}
					}
				}
			}
		}
		throw new IllegalArgumentException("MSRun not  found ");
	}

	private String getReplicateNameFromMsRun(MsRunType msRunType) {
		try {
			final String[] split = msRunType.getId().split("_");

			final StringBuilder replicateName2 = new StringBuilder();
			for (int i = 4; i < split.length; i++) {
				if (!"".equals(replicateName2.toString())) {
					replicateName2.append("_");
				}
				replicateName2.append(split[i]);
			}

			return replicateName2.toString().toLowerCase();
			// final String substring =
			// msRunType.getId().substring(msRunType.getId().indexOf("_") + 1);
			// final String substring2 = substring.substring(0,
			// substring.indexOf("_"));
			// final String substring3 =
			// substring2.substring(substring2.length() - 4, substring2.length()
			// - 3);
			//
			// return substring3.toLowerCase();
		} catch (final NumberFormatException e) {
			final String substring = msRunType.getId().substring(0, msRunType.getId().lastIndexOf("_"));
			final String substring2 = substring.substring(substring.length() - 1, substring.length());
			return substring2.toLowerCase();
		}
	}

	private BrainRegion getBrainRegionFromMsRun(MsRunType msRunType) {
		final String id = msRunType.getId();

		final String substring = id.substring(10, 13);
		return BrainRegion.valueOf(substring.toLowerCase());

	}

	private TimePoint getTimePointFromMsRun(MsRunType msRunType) {
		final String id = msRunType.getId();
		final String substring = id.substring(6, 9);
		return TimePoint.valueOf(substring.toLowerCase());
	}

	private MouseType getMouseTypeFromMsRun(MsRunType msRunType) {
		final String id = msRunType.getId();
		final String substring = id.substring(3, 5);
		return MouseType.valueOf(substring.toLowerCase());
	}

	private Model getModelFromMsRun(MsRunType msRunType) {
		final String id = msRunType.getId();
		final String substring = id.substring(0, 2);
		return Model.getByName(substring);
	}

	private String getDiscardDecoy() {
		return "Reverse.*";
	}

	private FileSetType getFileSet() {
		final FileSetType ret = new FileSetType();
		ret.getFile().addAll(getDTASelectFileTypes());
		ret.getFile().addAll(getCensusFiles());
		ret.getFile().addAll(getRatio2QuantCompareFiles());
		return ret;
	}

	private List<FileType> getRatio2QuantCompareFiles() {
		if (ratio2QuantCompareFiles == null) {
			ratio2QuantCompareFiles = new ArrayList<FileType>();
			final File path = new File(
					"C:\\Users\\Salva\\Desktop\\Dropbox\\Alzheimers proteomics\\FDR_data\\final tables");
			for (final File excelFile : path.listFiles()) {
				final String name = FilenameUtils.getBaseName(excelFile.getAbsolutePath());
				final FileType file = new FileType();
				file.setFormat(FormatType.EXCEL);
				file.setId("Excel_" + name);
				file.setServerRef(getSealionServer().getId());
				file.setName(FilenameUtils.getName(excelFile.getAbsolutePath()));
				file.setRelativePath("/home/salvador/PInt/data/Alzheimer dataset/final tables/");
				// try {
				// file.setUrl(new File("/home/salvador/PInt/data/Alzheimer
				// dataset/final tables/"
				// +
				// FilenameUtils.getName(excelFile.getAbsolutePath())).toURI().toURL().toString());
				// } catch (MalformedURLException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				file.setSheets(adaptFromFileWithType(excelFile, file.getId()));
				ratio2QuantCompareFiles.add(file);
			}
		}
		return ratio2QuantCompareFiles;
	}

	private FileType getRatio2FileType(Model model, TimePoint timepoint, BrainRegion brainregion) {
		final String fileName = model.name + "_" + timepoint.name() + "_" + brainregion.name() + ".xlsx";
		final List<FileType> ratio2QuantCompareFiles2 = getRatio2QuantCompareFiles();
		for (final FileType fileType : ratio2QuantCompareFiles2) {
			if (fileType.getName().equalsIgnoreCase(fileName)) {
				return fileType;
			}
		}
		throw new IllegalArgumentException("Ratio 2 file is not found");
	}

	private SheetsType adaptFromFileWithType(File file, String id) {
		final ObjectFactory factory = new ObjectFactory();
		final SheetsType ret = factory.createSheetsType();

		try {
			final ExcelFileImpl excelFile = new ExcelFileImpl(file);
			final List<ExcelSheet> sheets = excelFile.getSheets();
			for (final ExcelSheet sheet : sheets) {
				final SheetType sheetCfg = factory.createSheetType();
				sheetCfg.setId(id + "##" + sheet.getName());
				final List<String> columnKeys = sheet.getColumnKeys();
				for (final String columnKey : columnKeys) {
					final ColumnType column = factory.createColumnType();
					final ExcelColumn excelColumn = sheet.getColumn(columnKey);
					column.setHeader(excelColumn.getHeader());
					column.setId(sheetCfg.getId() + "##" + excelColumn.getKey());
					column.setNumber(excelColumn.isNumerical());
					sheetCfg.getColumn().add(column);
				}
				ret.getSheet().add(sheetCfg);
			}

		} catch (final IOException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}

		return ret;

	}

	private List<FileType> getDTASelectFileTypes() {
		final List<FileType> list = new ArrayList<FileType>();
		final List<MsRunType> msRuns = getMsRuns().getMsRun();
		for (final MsRunType msRunType : msRuns) {
			final FileType file = new FileType();
			file.setFormat(FormatType.DTA_SELECT_FILTER_TXT);
			file.setId("File_" + msRunType.getId());
			file.setRelativePath(msRunType.getPath());
			file.setServerRef(getJainaServer().getId());
			file.setName("DTASelect-filter.txt");
			list.add(file);
		}
		return list;
	}

	private ServerType getJainaServer() {
		final ServerType server = new ServerType();
		server.setHostName("jaina.scripps.edu");
		server.setId("jaina");
		server.setPassword("Natjeija21");
		server.setUserName("salvador");
		return server;
	}

	private ServerType getSealionServer() {
		final ServerType server = new ServerType();
		server.setHostName("sealion.scripps.edu");
		server.setId("sealion");
		server.setPassword("Natjeija21");
		server.setUserName("salvador");
		return server;
	}

	private MsRunsType getMsRuns() {
		final MsRunsType ret = new MsRunsType();
		if (msRuns == null) {
			msRuns = readMsRunsFromFile();
		}
		for (final MsRunType msRunType : msRuns) {
			ret.getMsRun().add(msRunType);

		}
		return ret;
	}

	private List<MsRunType> readMsRunsFromFile() {
		if (msRuns == null) {
			msRuns = new ArrayList<MsRunType>();
			msRunMap = new THashMap<String, MsRunType>();
			try {
				final Path path = Paths.get(JeffAlzheimerProject.path + File.separator + "AD_search_paths.txt");
				final List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
				String replicateName = null;
				String modelName = null;
				String pathName = null;
				Date date = null;
				boolean stringLine = false;
				for (final String line : lines) {
					if ("".equals(line)) {
						stringLine = false;
						final MsRunType msRunType = new MsRunType();
						msRunType.setPath(pathName);
						msRunType.setId(modelName + "_" + replicateName);
						msRunType.setDate(DatesUtil.toXMLGregorianCalendar(date));
						msRuns.add(msRunType);
						msRunMap.put(msRunType.getId(), msRunType);
						addReplicateName(msRunType.getId());
					} else if (line.startsWith("/")) {
						stringLine = false;
						pathName = line.trim();
						if (pathName.contains("_201")) {
							final int beginIndex = pathName.indexOf("_201") + 1;
							final String substring = pathName.substring(beginIndex);
							final String year = substring.substring(0, 4);
							final String month = substring.substring(5, 7);
							final String day = substring.substring(8, 10);
							final String hour = substring.substring(11, 13);
							final String minutes = substring.substring(14, 16);
							final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy/hh:mm");
							final String source = day + "/" + month + "/" + year + "/" + hour + ":" + minutes;
							date = formatter.parse(source);

						}
					} else {
						if (stringLine) {
							modelName = replicateName.toUpperCase();
						}
						stringLine = true;
						replicateName = line.trim();
						if (replicateName.contains("(")) {
							replicateName = replicateName.substring(0, replicateName.indexOf("(") - 1).trim();
						}
						replicateName = replicateName.replace(" ", "").trim();
						if (replicateName.toLowerCase().equals("wt_pre_cer_2_sample27")) {
							System.out.println(replicateName);
						} else if (replicateName.toLowerCase().equals("ad_pos_cer_1_sample34_t2")) {
							System.out.println(replicateName);
						}
						replicateName = replicateName.toLowerCase();
					}
				}
				final MsRunType msRunType = new MsRunType();
				msRunType.setPath(pathName);
				msRunType.setId(modelName + "_" + replicateName);
				msRunType.setDate(DatesUtil.toXMLGregorianCalendar(date));
				msRuns.add(msRunType);
				msRunMap.put(replicateName.toLowerCase(), msRunType);
				addReplicateName(msRunType.getId());
			} catch (final IOException e) {
				fail();
			} catch (final ParseException e) {
				fail();
			}
		}
		return msRuns;
	}

	private void addReplicateName(String replicateName) {
		final String[] split = replicateName.split("_");
		final StringBuilder key = new StringBuilder();
		key.append(split[0]).append("_").append(split[1]).append("_").append(split[2]).append("_").append(split[3]);
		final StringBuilder replicateName2 = new StringBuilder();
		for (int i = 4; i < split.length; i++) {
			if (!"".equals(replicateName2.toString())) {
				replicateName2.append("_");
			}
			replicateName2.append(split[i]);
		}
		String lowerCaseKey = key.toString().toLowerCase();
		lowerCaseKey = lowerCaseKey.replace("1", "").replace("2", "").replace("3", "").replace("post", "pos");
		if (lowerCaseKey.endsWith("_")) {
			lowerCaseKey = lowerCaseKey.substring(0, lowerCaseKey.length() - 1);
		}
		final List<String> lowerCaseKeys = new ArrayList<String>();
		lowerCaseKeys.add(lowerCaseKey);
		if (lowerCaseKey.contains("ad")) {
			lowerCaseKeys.add(lowerCaseKey.replace("ad", "control"));
		} else if (lowerCaseKey.contains("wt")) {
			lowerCaseKeys.add(lowerCaseKey.replace("wt", "control"));
		}
		for (final String lowerCaseKey2 : lowerCaseKeys) {

			final String lowerCase = replicateName2.toString().toLowerCase();
			if (replicateNumbersByCondition.containsKey(lowerCaseKey2)) {
				if (!replicateNumbersByCondition.get(lowerCaseKey2).contains(lowerCase)) {
					replicateNumbersByCondition.get(lowerCaseKey2).add(lowerCase);
				}
			} else {
				final List<String> list = new ArrayList<String>();
				list.add(lowerCase);
				replicateNumbersByCondition.put(lowerCaseKey2, list);
			}
		}
	}

	@Test
	public void convertFilestoTxt() {
		try {
			final File rootFolder = new File("C:\\Users\\Salva\\Desktop\\Dropbox\\Alzheimers proteomics\\Census files");
			final List<File> files = getFiles(rootFolder, "xlsx");
			for (final File file : files) {

				if (FilenameUtils.getExtension(file.getAbsolutePath()).equals("xlsx")) {
					final ExcelReader reader = new ExcelReader(file.getAbsolutePath(), 0, 0);
					final File file2 = reader.saveAsTXT("\t");
					System.out.println("Created: " + file2.getAbsolutePath());
				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void getPSMLevelRatiosFromFiles() {
		try {
			final List<File> files = getFiles(censusFilesRootFolder, "txt");
			for (final File file : files) {
				if (file.getAbsolutePath().contains("AD_postCER1_Sample34")) {
					if (FilenameUtils.getExtension(file.getAbsolutePath()).equals("txt")) {
						final QuantificationLabel label1 = QuantificationLabel.LIGHT;
						final QuantCondition cond1 = new QuantCondition("cond1");
						final QuantificationLabel label2 = QuantificationLabel.HEAVY;
						final QuantCondition cond2 = new QuantCondition("cond2");
						final CensusOutParser parser = new CensusOutParser(file, label1, cond1, label2, cond2);

						final Collection<QuantifiedProteinInterface> quantProteins = parser.getProteinMap().values();
						for (final QuantifiedProteinInterface quantifiedProteinInterface : quantProteins) {
							if (quantifiedProteinInterface.getRawFileNames().contains("N5_34_S7")) {
								final Set<QuantRatio> ratios = quantifiedProteinInterface.getQuantRatios();
								for (final QuantRatio ratio : ratios) {
									final Score ratioScore = ratio.getAssociatedConfidenceScore();
									System.out.println(quantifiedProteinInterface.getAccession() + "\t"
											+ ratio.getNonLogRatio(cond1, cond2));
									if (ratioScore != null) {
										if ("".equals(ratioScore.getValue())) {
											System.out.println("asdf");
										}
										System.out.println("With score: " + ratioScore.getScoreName() + "="
												+ ratioScore.getValue());
									}
								}
								for (final QuantifiedPSMInterface psm : quantifiedProteinInterface
										.getQuantifiedPSMs()) {
									if (psm.getRawFileNames().contains("N5_34_S7")) {

										final Set<QuantRatio> ratios2 = psm.getQuantRatios();
										for (final QuantRatio ratio : ratios2) {
											System.out.println(psm.getSequence() + "\t"
													+ ratio.getLog2Ratio(cond1, cond2) + "\t" + ratio.getDescription());
											final Score ratioScore = ratio.getAssociatedConfidenceScore();
											if (ratioScore != null) {
												System.out.println(ratioScore.getScoreName() + ":"
														+ ratioScore.getValue() + "\t" + ratioScore.getScoreType()
														+ "\t" + ratioScore.getScoreDescription());

											}
										}

									}
								}
							}
						}
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private List<File> getFiles(File file, String extension) {
		final List<File> ret = new ArrayList<File>();
		if (file.isFile()) {
			if (FilenameUtils.getExtension(file.getAbsolutePath()).equals(extension)) {
				ret.add(file);
			}
		} else {
			final File[] listFiles = file.listFiles();
			for (final File file2 : listFiles) {
				ret.addAll(getFiles(file2, extension));
			}
		}
		return ret;
	}

	@Test
	public void readCensusFiles() {
		try {
			final Map<QuantCondition, QuantificationLabel> labelsByConditions = new THashMap<QuantCondition, QuantificationLabel>();
			final QuantCondition cond1 = new QuantCondition("condition1");
			final QuantCondition cond2 = new QuantCondition("condition2");
			labelsByConditions.put(cond1, QuantificationLabel.LIGHT);
			labelsByConditions.put(cond2, QuantificationLabel.HEAVY);
			final List<FileType> censusFiles2 = getCensusFiles();
			for (final FileType fileType : censusFiles2) {
				final File tmpFile = File.createTempFile("test", "txt");
				final RemoteSSHFileReference ref = new RemoteSSHFileReference(getSealionServer().getHostName(),
						getSealionServer().getUserName(), getSealionServer().getPassword(), fileType.getName(),
						tmpFile);
				ref.setRemotePath(fileType.getRelativePath());

				CensusOutParser parser;

				parser = new CensusOutParser(ref, labelsByConditions, QuantificationLabel.N14, QuantificationLabel.N15);
				final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();

			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			fail();
		} catch (final IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void parseProjectTest() {
		try {
			final String uniprotReleasesFolder = "C:\\Users\\Salva\\Desktop\\pint\\uniprot";
			UniprotProteinRetrievalSettings.getInstance(new File(uniprotReleasesFolder), true);
			ImportCfgFileReader.ignoreDTASelectParameterT = true;
			final File file = new File(
					"C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Alzheimer dataset Jeff\\alzheimerProject_NEW_n5_pre_cer.xml");
			// File file2 = new File(
			// "C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Alzheimer
			// dataset Jeff\\alzheimerProject_NEW_N5_ad_pos_ctx_TINNY.xml");
			final List<File> listofFiles = new ArrayList<File>();
			listofFiles.add(file);
			// listofFiles.add(file2);

			for (final File file3 : listofFiles) {

				final ImportCfgFileReader r = new ImportCfgFileReader(true, true);
				final Project projectFromCfgFile = r.getProjectFromCfgFile(file3, null);

				final Set<Protein> proteins = projectFromCfgFile.getConditions().iterator().next().getProteins();
				for (final Protein protein : proteins) {
					boolean found = false;
					final Set<Accession> secondaryAccessions = protein.getSecondaryAccessions();
					for (final Accession accession : secondaryAccessions) {
						if (accession.getAccession().equals("IPI00230766.4")) {
							System.out.println(protein.getAccession());
							found = true;

						}
					}
					if (found) {
						final Set<edu.scripps.yates.utilities.proteomicsmodel.Ratio> ratios = protein.getRatios();
						Assert.assertNotNull(ratios);
						Assert.assertFalse(ratios.isEmpty());
						for (final edu.scripps.yates.utilities.proteomicsmodel.Ratio ratio : ratios) {
							System.out.println(
									protein.getAccession() + " " + ratio.getDescription() + " " + ratio.getValue() + " "
											+ ratio.getAggregationLevel() + " " + protein.getConditions().size() + " "
											+ protein.getMSRuns().iterator().next().getRunId());
						}
						final Set<Amount> amounts = protein.getAmounts();
						for (final Amount amount : amounts) {
							System.out.println(amount.getCondition().getName() + "\t" + amount.getAmountType().name()
									+ "\t" + amount.getValue());
						}
						final List<PSM> psMs = protein.getPSMs();
						for (final PSM psm : psMs) {
							System.out.println(psm.getIdentifier() + "\t" + psm.getFullSequence());
							final Set<Amount> amounts2 = psm.getAmounts();
							for (final Amount amount : amounts2) {
								System.out.println(amount.getCondition().getName() + "\t"
										+ amount.getAmountType().name() + "\t" + amount.getValue());
							}
							final Set<Ratio> ratios2 = psm.getRatios();
							for (final Ratio ratio : ratios2) {
								System.out.println(psm.getFullSequence() + "\t" + ratio.getDescription() + "\t"
										+ ratio.getValue());
							}
						}
					}
				}
				ContextualSessionHandler.beginGoodTransaction();
				final MySQLSaver saver = new MySQLSaver();

				saver.saveProject(projectFromCfgFile);
				ContextualSessionHandler.finishGoodTransaction();

			}
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void saveProjectTest() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			final String uniprotReleasesFolder = "C:\\Users\\Salva\\Desktop\\pint\\uniprot";
			UniprotProteinRetrievalSettings.getInstance(new File(uniprotReleasesFolder), true);
			ImportCfgFileReader.ignoreDTASelectParameterT = true;
			final File file = new File(
					"C:\\Users\\Salva\\Desktop\\data\\PINT projects\\Alzheimer dataset Jeff\\Alzheimer_ad_pre_ctx - Tinny.xml");
			final ImportCfgFileReader r = new ImportCfgFileReader(true, true);
			final Project projectFromCfgFile = r.getProjectFromCfgFile(file, null);
			final MySQLSaver saver = new MySQLSaver();

			saver.saveProject(projectFromCfgFile);
			ContextualSessionHandler.finishGoodTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void getFinalFDRsAndPValuesFromTable() {
		ExcelReader excel;
		try {
			excel = new ExcelReader(finalFDRFile, 0, 1);

			final TIntObjectHashMap<Map<String, Map<String, Double>>> superMap = new TIntObjectHashMap<Map<String, Map<String, Double>>>();
			for (int sheetNumber = 0; sheetNumber <= 1; sheetNumber++) {
				final Map<String, Integer> locusColumnByConditionName = new THashMap<String, Integer>();
				final Map<String, Integer> FDRColumnByConditionName = new THashMap<String, Integer>();
				populateColumnsByConditions(excel, sheetNumber, locusColumnByConditionName, FDRColumnByConditionName);

				final Map<String, Map<String, Double>> map = new THashMap<String, Map<String, Double>>();
				superMap.put(sheetNumber, map);
				for (final String conditionName : locusColumnByConditionName.keySet()) {
					final Map<String, Double> proteinFDRMap = getProteinFDRMap(excel, sheetNumber, conditionName,
							locusColumnByConditionName.get(conditionName), FDRColumnByConditionName.get(conditionName));
					map.put(conditionName, proteinFDRMap);
				}
			}

			printMap(excel, superMap);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void printMap(ExcelReader excel, TIntObjectHashMap<Map<String, Map<String, Double>>> superMap)
			throws IOException {
		for (int i = 0; i <= 1; i++) {
			System.out.println("Experiment: " + excel.getWorkbook().getSheetName(i));
			final Map<String, Map<String, Double>> map = superMap.get(i);
			final List<String> conditionNames = new ArrayList<String>();
			conditionNames.addAll(map.keySet());
			Collections.sort(conditionNames);
			for (final String conditionName : conditionNames) {
				final Map<String, Double> map2 = map.get(conditionName);
				for (final String locus : map2.keySet()) {
					System.out.println(conditionName + "\t" + locus + "\t" + map2.get(locus));
				}
			}
		}

	}

	private Map<String, Double> getProteinFDRMap(ExcelReader excel, int sheetNumber, String conditionName,
			int locusColumn, int FDRColumn) {
		final Map<String, Double> ret = new THashMap<String, Double>();
		int row = 2;
		while (true) {
			final String fdrValue = excel.getNumberValue(sheetNumber, row, FDRColumn);
			if (fdrValue != null) {
				final String locus = excel.getStringValue(sheetNumber, row, locusColumn);
				ret.put(locus, Double.valueOf(fdrValue));
				row++;
			} else {
				break;
			}

		}
		return ret;
	}

	private void populateColumnsByConditions(ExcelReader excel, int sheetNumber,
			Map<String, Integer> locusColumnByConditionName, Map<String, Integer> FDRColumnByConditionName)
			throws IOException {
		final List<String> columNames = excel.getColumnNames().get(sheetNumber);
		final List<String> conditionNames = new ArrayList<String>();
		int index = 0;
		System.out.println();
		final Map<String, Integer> columnByCondition = new THashMap<String, Integer>();
		for (final String columnName : columNames) {
			if (columnName != null) {
				final String[] split = columnName.split("_");
				final String model = split[0];
				final String[] wtOrADs = { "WT", "AD" };
				final String timepoint = split[1];
				final String brain = split[2];

				final String conditionName = model + "_" + timepoint + "_" + brain;
				columnByCondition.put(conditionName, index);
				conditionNames.add(columnName);

			}
			index++;
		}
		// second row

		String cellValue;
		int conditionCounter = 0;
		for (int col = 0; col < 1000; col++) {
			cellValue = excel.getStringValue(sheetNumber, 1, col);
			if (cellValue != null) {
				if ("locus".equals(cellValue)) {
					locusColumnByConditionName.put(conditionNames.get(conditionCounter), col);
					conditionCounter++;
					// locusColumnByConditionName.put(conditionNames.get(conditionCounter),
					// col);
					// conditionCounter++;
				}
				if (cellValue.contains("BH")) {
					FDRColumnByConditionName.put(conditionNames.get(conditionCounter - 1), col);
					// FDRColumnByConditionName.put(conditionNames.get(conditionCounter
					// - 2), col);
				}
			}
		}
		System.out.println("Locus columns:");
		for (final String conditionName : conditionNames) {
			System.out.println(conditionName + "\t" + locusColumnByConditionName.get(conditionName));
		}

		System.out.println("FDR columns:");
		for (final String conditionName : conditionNames) {
			System.out.println(conditionName + "\t" + FDRColumnByConditionName.get(conditionName));
		}

	}

	// @Test
	// public void addQuantCompareRatioToFDRData() {
	// final File quantCompareFolder = new File(
	// "C:\\Users\\Salva\\Desktop\\Dropbox\\Alzheimers
	// proteomics\\FDR_data\\final tables");
	// final File fdrFolder = new
	// File("C:\\Users\\Salva\\Desktop\\Dropbox\\Alzheimers
	// proteomics\\Lasttables");
	// for (File fdrFile : fdrFolder.listFiles()) {
	// FileWriter fo = null;
	// try {
	// // file output
	// File output = new File("C:\\Users\\Salva\\Desktop\\Dropbox\\Alzheimers
	// proteomics\\Lasttables\\"
	// + FilenameUtils.getBaseName(fdrFile.getAbsolutePath()) +
	// "_QuantCompare.txt");
	// fo = new FileWriter(output);
	// ExcelReader excel = new ExcelReader(fdrFile, 0, 1);
	// final List<String> header = excel.getColumnNames().get(1);
	// int ratioIndex = header.indexOf("log(2) ratio change 1/2");
	// int locusIndex = 0;
	// int numRow = 1;
	// String locus = excel.getStringValue(1, numRow, locusIndex);
	// String ratio = excel.getNumberValue(1, numRow, ratioIndex);
	// while (locus != null) {
	// fo.write(locus + "\t" + ratio + "\n");
	// numRow++;
	// locus = excel.getStringValue(1, numRow, locusIndex);
	// ratio = excel.getNumberValue(1, numRow, ratioIndex);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// fail();
	// } finally {
	// if (fo != null) {
	// try {
	// fo.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// }
	// }
}
