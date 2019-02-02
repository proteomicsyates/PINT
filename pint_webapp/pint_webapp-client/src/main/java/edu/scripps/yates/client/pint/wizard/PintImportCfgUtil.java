package edu.scripps.yates.client.pint.wizard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.moxieapps.gwt.uploader.client.File;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ExcelColumnRefPanel;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.AmountTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PeptideRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PsmRatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.QuantificationInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RatiosTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;

public class PintImportCfgUtil {
	public static final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("MM/dd/yyyy");

	public static FileTypeBean addFile(PintImportCfgBean pintImportConfiguration, File file, String fileIDInServer)
			throws PintException {
		if (pintImportConfiguration.getFileSet() == null) {
			pintImportConfiguration.setFileSet(new FileSetTypeBean());
		}
		for (final FileTypeBean file2 : pintImportConfiguration.getFileSet().getFile()) {
			if (file2.getId().equals(fileIDInServer)) {
				throw new PintException(
						"A file with the name '" + fileIDInServer
								+ "' is already created. Please rename the file before uploading it.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);
			}
		}
		final FileTypeBean newFileTypeBean = new FileTypeBean();
		pintImportConfiguration.getFileSet().getFile().add(newFileTypeBean);
		newFileTypeBean.setId(fileIDInServer);
		newFileTypeBean.setName(file.getName());
		return newFileTypeBean;
	}

	public static String getExcelImportDescription(SheetsTypeBean sheets) {
		final StringBuilder sb = new StringBuilder();
		if (sheets != null) {
			sb.append(sheets.getSheet().size() + " sheets");
			for (final SheetTypeBean sheet : sheets.getSheet()) {
				final String sheetName = ExcelColumnRefPanel.getSheetID(sheet.getId());
				sb.append(", '" + sheetName + "': " + sheet.getColumn().size() + " columns");
				break;// just one
			}
		}

		return sb.toString();
	}

	public static void addSample(PintImportCfgBean pintImportConfiguration, SampleTypeBean sampleObj)
			throws PintException {
		if (pintImportConfiguration.getProject().getExperimentalDesign() == null) {
			final ExperimentalDesignTypeBean experimentalDesignBean = new ExperimentalDesignTypeBean();
			pintImportConfiguration.getProject().setExperimentalDesign(experimentalDesignBean);
		}
		if (pintImportConfiguration.getProject().getExperimentalDesign().getSampleSet() == null) {
			pintImportConfiguration.getProject().getExperimentalDesign().setSampleSet(new SampleSetTypeBean());
		}
		for (final SampleTypeBean sample : pintImportConfiguration.getProject().getExperimentalDesign().getSampleSet()
				.getSample()) {
			if (sample.getId().equals(sampleObj.getId())) {
				throw new PintException(
						"A sample with the name '" + sample.getId()
								+ "' is already created. Please use any other name for the new sample.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);

			}
		}
		pintImportConfiguration.getProject().getExperimentalDesign().getSampleSet().getSample().add(sampleObj);
	}

	public static FileNameWithTypeBean getFileNameWithTypeBeanl(FileTypeBean fileTypeBean) {
		final FileNameWithTypeBean fileNameWithTypeBean = new FileNameWithTypeBean();
		fileNameWithTypeBean.setFileFormat(fileTypeBean.getFormat());
		fileNameWithTypeBean.setFileName(fileTypeBean.getName());
		fileNameWithTypeBean.setId(fileTypeBean.getId());
		return fileNameWithTypeBean;
	}

	public static void addCondition(PintImportCfgBean pintImportConfiguration, ExperimentalConditionTypeBean itemBean)
			throws PintException {
		if (pintImportConfiguration.getProject().getExperimentalConditions() == null) {
			final ExperimentalConditionsTypeBean experimentalConditions = new ExperimentalConditionsTypeBean();
			pintImportConfiguration.getProject().setExperimentalConditions(experimentalConditions);
		}

		for (final ExperimentalConditionTypeBean condition2 : pintImportConfiguration.getProject()
				.getExperimentalConditions().getExperimentalCondition()) {
			if (condition2.getId().equals(itemBean.getId())) {
				throw new PintException(
						"An experimental condition with the name '" + itemBean.getId()
								+ "' is already created. Please use any other name for the new experimental condition.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);

			}
		}
		pintImportConfiguration.getProject().getExperimentalConditions().getExperimentalCondition().add(itemBean);

	}

	public static List<SampleTypeBean> getSamples(PintImportCfgBean pintImportConfiguration) {
		final List<SampleTypeBean> ret = new ArrayList<SampleTypeBean>();
		if (pintImportConfiguration.getProject() != null) {
			if (pintImportConfiguration.getProject().getExperimentalDesign() != null) {
				if (pintImportConfiguration.getProject().getExperimentalDesign().getSampleSet() != null) {
					for (final SampleTypeBean sampleTypeBean : pintImportConfiguration.getProject()
							.getExperimentalDesign().getSampleSet().getSample()) {
						ret.add(sampleTypeBean);
					}
				}
			}
		}
		return ret;
	}

	public static boolean removeSample(PintImportCfgBean pintImportCfgBean, SampleTypeBean item) {
		boolean ret = false;
		if (pintImportCfgBean != null) {
			if (pintImportCfgBean.getProject() != null) {
				if (pintImportCfgBean.getProject().getExperimentalDesign() != null) {
					if (pintImportCfgBean.getProject().getExperimentalDesign().getSampleSet() != null) {
						ret = pintImportCfgBean.getProject().getExperimentalDesign().getSampleSet().getSample()
								.remove(item);
					}
				}
				if (pintImportCfgBean.getProject().getExperimentalConditions() != null) {
					for (final ExperimentalConditionTypeBean condition : pintImportCfgBean.getProject()
							.getExperimentalConditions().getExperimentalCondition()) {
						if (item.getId().equals(condition.getSampleRef())) {
							condition.setSampleRef(null);
						}
					}
				}
			}
		}
		return ret;
	}

	public static boolean removeCondition(PintImportCfgBean pintImportConfiguration,
			ExperimentalConditionTypeBean item) {
		boolean ret = false;
		if (pintImportConfiguration != null) {
			if (pintImportConfiguration.getProject() != null) {
				if (pintImportConfiguration.getProject().getExperimentalConditions() != null) {
					ret = pintImportConfiguration.getProject().getExperimentalConditions().getExperimentalCondition()
							.remove(item);
				}
			}
		}
		return ret;
	}

	public static List<ExperimentalConditionTypeBean> getConditions(PintImportCfgBean pintImportConfiguration) {
		final List<ExperimentalConditionTypeBean> ret = new ArrayList<ExperimentalConditionTypeBean>();
		if (pintImportConfiguration != null) {
			if (pintImportConfiguration.getProject() != null) {
				if (pintImportConfiguration.getProject().getExperimentalConditions() != null) {
					ret.addAll(pintImportConfiguration.getProject().getExperimentalConditions()
							.getExperimentalCondition());
				}
			}
		}
		return ret;
	}

	public static boolean removeOrganism(PintImportCfgBean pintImportCfgBean, OrganismTypeBean item) {
		boolean ret = false;
		if (pintImportCfgBean != null) {
			if (pintImportCfgBean.getProject() != null) {
				if (pintImportCfgBean.getProject().getExperimentalDesign() != null) {
					if (pintImportCfgBean.getProject().getExperimentalDesign().getOrganismSet() != null) {
						ret = pintImportCfgBean.getProject().getExperimentalDesign().getOrganismSet().getOrganism()
								.remove(item);
					}
					if (pintImportCfgBean.getProject().getExperimentalDesign().getSampleSet() != null) {
						for (final SampleTypeBean sample : pintImportCfgBean.getProject().getExperimentalDesign()
								.getSampleSet().getSample()) {
							if (item.getId().equals(sample.getOrganismRef())) {
								sample.setOrganismRef(null);
							}
						}
					}
				}
			}
		}
		return ret;
	}

	public static void addOrganism(PintImportCfgBean pintImportConfiguration, OrganismTypeBean organism2)
			throws PintException {
		if (pintImportConfiguration.getProject().getExperimentalDesign() == null) {
			final ExperimentalDesignTypeBean experimentalDesignBean = new ExperimentalDesignTypeBean();
			pintImportConfiguration.getProject().setExperimentalDesign(experimentalDesignBean);
		}
		if (pintImportConfiguration.getProject().getExperimentalDesign().getOrganismSet() == null) {
			pintImportConfiguration.getProject().getExperimentalDesign().setOrganismSet(new OrganismSetTypeBean());
		}
		for (final OrganismTypeBean organism : pintImportConfiguration.getProject().getExperimentalDesign()
				.getOrganismSet().getOrganism()) {
			if (organism.getId().equals(organism2.getId())) {
				throw new PintException(
						"An organism with the name '" + organism.getId()
								+ "' is already created. Please use any other name for the new organism.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);

			}
		}
		pintImportConfiguration.getProject().getExperimentalDesign().getOrganismSet().getOrganism().add(organism2);
	}

	public static List<OrganismTypeBean> getOrganisms(PintImportCfgBean pintImportConfiguration) {
		final List<OrganismTypeBean> ret = new ArrayList<OrganismTypeBean>();
		if (pintImportConfiguration.getProject() != null) {
			if (pintImportConfiguration.getProject().getExperimentalDesign() != null) {
				if (pintImportConfiguration.getProject().getExperimentalDesign().getOrganismSet() != null) {
					for (final OrganismTypeBean organismTypeBean : pintImportConfiguration.getProject()
							.getExperimentalDesign().getOrganismSet().getOrganism()) {
						ret.add(organismTypeBean);
					}
				}
			}
		}
		return ret;
	}

	public static List<TissueTypeBean> getTissues(PintImportCfgBean pintImportConfiguration) {
		final List<TissueTypeBean> ret = new ArrayList<TissueTypeBean>();
		if (pintImportConfiguration.getProject() != null) {
			if (pintImportConfiguration.getProject().getExperimentalDesign() != null) {
				if (pintImportConfiguration.getProject().getExperimentalDesign().getTissueSet() != null) {
					for (final TissueTypeBean tissueTypeBean : pintImportConfiguration.getProject()
							.getExperimentalDesign().getTissueSet().getTissue()) {
						ret.add(tissueTypeBean);
					}
				}
			}
		}
		return ret;
	}

	public static boolean removeTissue(PintImportCfgBean pintImportCfgBean, TissueTypeBean item) {
		boolean ret = false;
		if (pintImportCfgBean != null) {
			if (pintImportCfgBean.getProject() != null) {
				if (pintImportCfgBean.getProject().getExperimentalDesign() != null) {
					if (pintImportCfgBean.getProject().getExperimentalDesign().getTissueSet() != null) {
						ret = pintImportCfgBean.getProject().getExperimentalDesign().getTissueSet().getTissue()
								.remove(item);
					}
					if (pintImportCfgBean.getProject().getExperimentalDesign().getSampleSet() != null) {
						for (final SampleTypeBean sample : pintImportCfgBean.getProject().getExperimentalDesign()
								.getSampleSet().getSample()) {
							if (item.getId().equals(sample.getTissueRef())) {
								sample.setTissueRef(null);
							}
						}
					}
				}
			}
		}
		return ret;
	}

	public static void addTissue(PintImportCfgBean pintImportConfiguration, TissueTypeBean tissue2)
			throws PintException {
		if (pintImportConfiguration.getProject().getExperimentalDesign() == null) {
			final ExperimentalDesignTypeBean experimentalDesignBean = new ExperimentalDesignTypeBean();
			pintImportConfiguration.getProject().setExperimentalDesign(experimentalDesignBean);
		}
		if (pintImportConfiguration.getProject().getExperimentalDesign().getTissueSet() == null) {
			pintImportConfiguration.getProject().getExperimentalDesign().setTissueSet(new TissueSetTypeBean());
		}
		for (final TissueTypeBean tissue : pintImportConfiguration.getProject().getExperimentalDesign().getTissueSet()
				.getTissue()) {
			if (tissue.getId().equals(tissue2.getId())) {
				throw new PintException(
						"A tissue/cell line with the name '" + tissue.getId()
								+ "' is already created. Please use any other name for the new tissue/cell line.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);

			}
		}
		pintImportConfiguration.getProject().getExperimentalDesign().getTissueSet().getTissue().add(tissue2);
	}

	public static void updateSampleID(PintImportCfgBean pintImportCfgBean, String oldID, String newID)
			throws PintException {
		final List<SampleTypeBean> samples = getSamples(pintImportCfgBean);
		final long idsAsTheNewOne = samples.stream().filter(sample -> sample.getId().equals(newID)).count();
		if (idsAsTheNewOne != 0l) {
			throw new PintException("There is already a sample with the name '" + newID + "'. Try a different name.",
					PINT_ERROR_TYPE.ITEM_ID_REPEATED);
		}
		for (final SampleTypeBean sampleTypeBean : samples) {
			if (sampleTypeBean.getId().equals(oldID)) {
				sampleTypeBean.setId(newID);
			}
		}
		// referenced by condition
		final List<ExperimentalConditionTypeBean> conditions = getConditions(pintImportCfgBean);
		for (final ExperimentalConditionTypeBean experimentalConditionTypeBean : conditions) {
			if (oldID.equals(experimentalConditionTypeBean.getSampleRef())) {
				experimentalConditionTypeBean.setSampleRef(newID);
			}
		}
	}

	public static void updateConditionID(PintImportCfgBean pintImportConfiguration, String oldID, String newID)
			throws PintException {
		final List<ExperimentalConditionTypeBean> conditions = getConditions(pintImportConfiguration);
		final long idsAsTheNewOne = conditions.stream().filter(condition -> condition.getId().equals(newID)).count();
		if (idsAsTheNewOne != 0l) {
			throw new PintException(
					"There is already an condition with the name '" + newID + "'. Try a different name.",
					PINT_ERROR_TYPE.ITEM_ID_REPEATED);
		}
		for (final ExperimentalConditionTypeBean experimentalConditionTypeBean : conditions) {
			if (experimentalConditionTypeBean.getId().equals(oldID)) {
				experimentalConditionTypeBean.setId(newID);
			}
		}
		// TODO to change something referencing any condition
	}

	public static void updateOrganism(PintImportCfgBean pintImportConfiguration, String oldID, String newID)
			throws PintException {
		final List<OrganismTypeBean> organisms = getOrganisms(pintImportConfiguration);
		final long idsAsTheNewOne = organisms.stream().filter(organism -> organism.getId().equals(newID)).count();
		if (idsAsTheNewOne != 0l) {
			throw new PintException("There is already an organism with the name '" + newID + "'. Try a different name.",
					PINT_ERROR_TYPE.ITEM_ID_REPEATED);
		}
		for (final OrganismTypeBean organismTypeBean : organisms) {
			if (organismTypeBean.getId().equals(oldID)) {
				organismTypeBean.setId(newID);
			}
		}
		// references to organisms
		final List<SampleTypeBean> samples = getSamples(pintImportConfiguration);
		for (final SampleTypeBean sampleTypeBean : samples) {
			if (oldID.equals(sampleTypeBean.getOrganismRef())) {
				sampleTypeBean.setOrganismRef(newID);
			}
		}
	}

	public static void updateTissue(PintImportCfgBean pintImportConfiguration, String oldID, String newID)
			throws PintException {
		final List<TissueTypeBean> tissues = getTissues(pintImportConfiguration);
		final long idsAsTheNewOne = tissues.stream().filter(tissue -> tissue.getId().equals(newID)).count();
		if (idsAsTheNewOne != 0l) {
			throw new PintException(
					"There is already an tissue/cell line with the name '" + newID + "'. Try a different name.",
					PINT_ERROR_TYPE.ITEM_ID_REPEATED);
		}
		for (final TissueTypeBean tissueTypeBean : tissues) {
			if (tissueTypeBean.getId().equals(oldID)) {
				tissueTypeBean.setId(newID);
			}
		}
		// references to Tissues
		final List<SampleTypeBean> samples = getSamples(pintImportConfiguration);
		for (final SampleTypeBean sampleTypeBean : samples) {
			if (oldID.equals(sampleTypeBean.getTissueRef())) {
				sampleTypeBean.setTissueRef(newID);
			}
		}
	}

	public static void updateLabel(PintImportCfgBean pintImportConfiguration, String oldID, String newID)
			throws PintException {
		final List<LabelTypeBean> labels = getLabels(pintImportConfiguration);
		final long idsAsTheNewOne = labels.stream().filter(label -> label.getId().equals(newID)).count();
		if (idsAsTheNewOne != 0l) {
			throw new PintException(
					"There is already an isobaric label line with the name '" + newID + "'. Try a different name.",
					PINT_ERROR_TYPE.ITEM_ID_REPEATED);
		}
		for (final LabelTypeBean labelTypeBean : labels) {
			if (labelTypeBean.getId().equals(oldID)) {
				labelTypeBean.setId(newID);
			}
		}
		// references to Labels
		final List<SampleTypeBean> samples = getSamples(pintImportConfiguration);
		for (final SampleTypeBean sampleTypeBean : samples) {
			if (oldID.equals(sampleTypeBean.getLabelRef())) {
				sampleTypeBean.setLabelRef(newID);
			}
		}
	}

	public static void addLabel(PintImportCfgBean pintImportConfiguration, LabelTypeBean label) throws PintException {
		if (pintImportConfiguration.getProject().getExperimentalDesign() == null) {
			final ExperimentalDesignTypeBean experimentalDesignBean = new ExperimentalDesignTypeBean();
			pintImportConfiguration.getProject().setExperimentalDesign(experimentalDesignBean);
		}
		if (pintImportConfiguration.getProject().getExperimentalDesign().getLabelSet() == null) {
			pintImportConfiguration.getProject().getExperimentalDesign().setLabelSet(new LabelSetTypeBean());
		}
		for (final LabelTypeBean label2 : pintImportConfiguration.getProject().getExperimentalDesign().getLabelSet()
				.getLabel()) {
			if (label.getId().equals(label2.getId())) {
				throw new PintException(
						"An isobaric label with the name '" + label.getId()
								+ "' is already created. Please use any other name for the new isobaric label.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);

			}
		}
		pintImportConfiguration.getProject().getExperimentalDesign().getLabelSet().getLabel().add(label);
	}

	public static List<LabelTypeBean> getLabels(PintImportCfgBean pintImportConfiguration) {
		final List<LabelTypeBean> ret = new ArrayList<LabelTypeBean>();
		if (pintImportConfiguration.getProject() != null) {
			if (pintImportConfiguration.getProject().getExperimentalDesign() != null) {
				if (pintImportConfiguration.getProject().getExperimentalDesign().getLabelSet() != null) {
					for (final LabelTypeBean labelTypeBean : pintImportConfiguration.getProject()
							.getExperimentalDesign().getLabelSet().getLabel()) {
						ret.add(labelTypeBean);
					}
				}
			}
		}
		return ret;
	}

	public static boolean removeLabel(PintImportCfgBean pintImportCfgBean, LabelTypeBean item) {
		boolean ret = false;
		if (pintImportCfgBean != null) {
			if (pintImportCfgBean.getProject() != null) {
				if (pintImportCfgBean.getProject().getExperimentalDesign() != null) {
					if (pintImportCfgBean.getProject().getExperimentalDesign().getLabelSet() != null) {
						ret = pintImportCfgBean.getProject().getExperimentalDesign().getLabelSet().getLabel()
								.remove(item);
					}
					// referenced label
					if (pintImportCfgBean.getProject().getExperimentalDesign().getSampleSet() != null) {
						for (final SampleTypeBean sample : pintImportCfgBean.getProject().getExperimentalDesign()
								.getSampleSet().getSample()) {
							if (item.getId().equals(sample.getLabelRef())) {
								sample.setLabelRef(null);
							}
						}
					}
				}
			}
		}
		return ret;
	}

	public static List<MsRunTypeBean> getMSRuns(PintImportCfgBean pintImportConfiguration) {
		final List<MsRunTypeBean> ret = new ArrayList<MsRunTypeBean>();
		if (pintImportConfiguration.getProject() != null) {
			if (pintImportConfiguration.getProject().getMsRuns() != null) {
				for (final MsRunTypeBean msRunTypeBean : pintImportConfiguration.getProject().getMsRuns().getMsRun()) {
					ret.add(msRunTypeBean);
				}
			}
		}
		return ret;
	}

	public static boolean removeMSRun(PintImportCfgBean pintImportCfgBean, MsRunTypeBean item) {
		boolean ret = false;
		if (pintImportCfgBean != null) {
			if (pintImportCfgBean.getProject() != null) {
				if (pintImportCfgBean.getProject().getMsRuns() != null) {
					ret = pintImportCfgBean.getProject().getMsRuns().getMsRun().remove(item);
					// references
					final List<ExperimentalConditionTypeBean> conditions = getConditions(pintImportCfgBean);
					for (final ExperimentalConditionTypeBean conditionTypeBean : conditions) {
						// referenced in identificationInfo

						if (conditionTypeBean.getIdentificationInfo() != null) {
							if (conditionTypeBean.getIdentificationInfo().getExcelIdentInfo() != null) {
								for (final IdentificationExcelTypeBean idExcelType : conditionTypeBean
										.getIdentificationInfo().getExcelIdentInfo()) {
									if (idExcelType.getMsRunRef() != null) {

										final String newMSRunRef = getReplacementMSRunRefToRemove(
												idExcelType.getMsRunRef(), item.getId());
										idExcelType.setMsRunRef(newMSRunRef);
									}
								}
							}
							if (conditionTypeBean.getIdentificationInfo().getRemoteFilesIdentInfo() != null) {
								for (final RemoteInfoTypeBean remoteInfoType : conditionTypeBean.getIdentificationInfo()
										.getRemoteFilesIdentInfo()) {
									if (remoteInfoType.getMsRunRef() != null) {
										final String newMSRunRef = getReplacementMSRunRefToRemove(
												remoteInfoType.getMsRunRef(), item.getId());
										remoteInfoType.setMsRunRef(newMSRunRef);
									}
								}
							}
						}
						// referenced in quantificationInfo
						if (conditionTypeBean.getQuantificationInfo() != null) {
							if (conditionTypeBean.getQuantificationInfo().getExcelQuantInfo() != null) {
								for (final QuantificationExcelTypeBean quantExcelType : conditionTypeBean
										.getQuantificationInfo().getExcelQuantInfo()) {
									if (quantExcelType.getMsRunRef() != null) {
										final String newMSRunRef = getReplacementMSRunRefToRemove(
												quantExcelType.getMsRunRef(), item.getId());
										quantExcelType.setMsRunRef(newMSRunRef);
									}
								}
							}
							if (conditionTypeBean.getQuantificationInfo().getRemoteFilesQuantInfo() != null) {
								if (conditionTypeBean.getQuantificationInfo().getRemoteFilesQuantInfo() != null) {
									for (final RemoteInfoTypeBean remoteInfoType : conditionTypeBean
											.getQuantificationInfo().getRemoteFilesQuantInfo()) {
										if (remoteInfoType.getMsRunRef() != null) {
											final String newMSRunRef = getReplacementMSRunRefToRemove(
													remoteInfoType.getMsRunRef(), item.getId());
											remoteInfoType.setMsRunRef(newMSRunRef);
										}
									}
								}
							}
						}
					}

				}
			}
		}
		return ret;
	}

	/**
	 * It replaces the msRunIDString removing the newMSRunID from it<br>
	 * if it contains the msRun ID, remove it, but takes into account that it may
	 * contain more than one separated by commas
	 * 
	 * @param msRunRefString
	 * @param msRunIDToRemove
	 * @return
	 */
	private static String getReplacementMSRunRefToRemove(String msRunRefString, String msRunIDToRemove) {
		final List<String> msRunRefs = new ArrayList<String>();
		if (msRunRefString.contains(",")) {
			final String[] split = msRunRefString.split(",");
			for (final String msRunRef : split) {
				msRunRefs.add(msRunRef);
			}
		} else {
			msRunRefs.add(msRunRefString);
		}
		final StringBuilder sb = new StringBuilder();
		for (final String msrunRef : msRunRefs) {
			if (!msrunRef.equalsIgnoreCase(msRunIDToRemove)) {
				if (!"".equals(sb.toString())) {
					sb.append(",");
				}
				sb.append(msrunRef);
			}
		}
		if ("".equals(sb.toString())) {
			return null;
		} else {
			return sb.toString();
		}
	}

	public static void addMSRun(PintImportCfgBean pintImportConfiguration, MsRunTypeBean msRun2) throws PintException {
		if (pintImportConfiguration.getProject().getMsRuns() == null) {
			final MsRunsTypeBean msRunsType = new MsRunsTypeBean();
			pintImportConfiguration.getProject().setMsRuns(msRunsType);
		}

		for (final MsRunTypeBean msRun : pintImportConfiguration.getProject().getMsRuns().getMsRun()) {
			if (msRun.getId().equals(msRun2.getId())) {
				throw new PintException(
						"A MS Run with the name '" + msRun.getId()
								+ "' is already created. Please use any other name for the new MS Run.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);

			}
		}
		pintImportConfiguration.getProject().getMsRuns().getMsRun().add(msRun2);
	}

	public static void updateMSRun(PintImportCfgBean pintImportConfiguration, String oldID, String newID)
			throws PintException {
		final List<MsRunTypeBean> msRuns = getMSRuns(pintImportConfiguration);
		final long idsAsTheNewOne = msRuns.stream().filter(msRun -> msRun.getId().equals(newID)).count();
		if (idsAsTheNewOne != 0l) {
			throw new PintException("There is already a MS Run with the name '" + newID + "'. Try a different name.",
					PINT_ERROR_TYPE.ITEM_ID_REPEATED);
		}
		for (final MsRunTypeBean msRunTypeBean : msRuns) {
			if (msRunTypeBean.getId().equals(oldID)) {
				msRunTypeBean.setId(newID);
			}
		}
		// references to msRuns
		// references
		final List<ExperimentalConditionTypeBean> conditions = getConditions(pintImportConfiguration);
		for (final ExperimentalConditionTypeBean conditionTypeBean : conditions) {
			// referenced in identificationInfo

			if (conditionTypeBean.getIdentificationInfo() != null) {
				if (conditionTypeBean.getIdentificationInfo().getExcelIdentInfo() != null) {
					for (final IdentificationExcelTypeBean idExcelType : conditionTypeBean.getIdentificationInfo()
							.getExcelIdentInfo()) {
						if (oldID.equals(idExcelType.getMsRunRef())) {
							final String newMSRunRef = getReplacementMSRunRefToAdd(idExcelType.getMsRunRef(), newID);
							idExcelType.setMsRunRef(newMSRunRef);
						}
					}
				}
				if (conditionTypeBean.getIdentificationInfo().getRemoteFilesIdentInfo() != null) {
					for (final RemoteInfoTypeBean remoteInfoType : conditionTypeBean.getIdentificationInfo()
							.getRemoteFilesIdentInfo()) {
						if (oldID.equals(remoteInfoType.getMsRunRef())) {
							final String newMSRunRef = getReplacementMSRunRefToAdd(remoteInfoType.getMsRunRef(), newID);
							remoteInfoType.setMsRunRef(newMSRunRef);
						}
					}
				}
			}
			// referenced in quantificationInfo
			if (conditionTypeBean.getQuantificationInfo() != null) {
				if (conditionTypeBean.getQuantificationInfo().getExcelQuantInfo() != null) {
					for (final QuantificationExcelTypeBean quantExcelType : conditionTypeBean.getQuantificationInfo()
							.getExcelQuantInfo()) {
						if (oldID.equals(quantExcelType.getMsRunRef())) {
							final String newMSRunRef = getReplacementMSRunRefToAdd(quantExcelType.getMsRunRef(), newID);
							quantExcelType.setMsRunRef(newMSRunRef);
						}
					}
				}
				if (conditionTypeBean.getQuantificationInfo().getRemoteFilesQuantInfo() != null) {
					if (conditionTypeBean.getQuantificationInfo().getRemoteFilesQuantInfo() != null) {
						for (final RemoteInfoTypeBean remoteInfoType : conditionTypeBean.getQuantificationInfo()
								.getRemoteFilesQuantInfo()) {
							if (oldID.equals(remoteInfoType.getMsRunRef())) {
								final String newMSRunRef = getReplacementMSRunRefToAdd(remoteInfoType.getMsRunRef(),
										newID);
								remoteInfoType.setMsRunRef(newMSRunRef);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Adds the newID to the msRunRefString. It this is not empty, it will
	 * concatenate it with a separating comma.
	 * 
	 * @param msRunRefString
	 * @param newID
	 * @return
	 */
	private static String getReplacementMSRunRefToAdd(String msRunRefString, String newID) {
		final List<String> msRunRefs = new ArrayList<String>();
		if (msRunRefString.contains(",")) {
			final String[] split = msRunRefString.split(",");
			for (final String msRunRef : split) {
				msRunRefs.add(msRunRef);
			}
		} else {
			msRunRefs.add(msRunRefString);
		}
		msRunRefs.add(newID);
		final StringBuilder sb = new StringBuilder();
		for (final String msrunRef : msRunRefs) {

			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(msrunRef);

		}
		if ("".equals(sb.toString())) {
			return null;
		} else {
			return sb.toString();
		}
	}

	public static List<FileTypeBean> getFilesByFormat(PintImportCfgBean pintImportCfg, FileFormat fileFormat) {
		final List<FileTypeBean> ret = new ArrayList<FileTypeBean>();
		if (pintImportCfg.getFileSet() != null) {
			for (final FileTypeBean fileTypeBean : pintImportCfg.getFileSet().getFile()) {
				if (fileTypeBean.getFormat() == fileFormat) {
					ret.add(fileTypeBean);
				}
			}
		}
		return ret;
	}

	public static List<FileTypeBean> getNonExcelFiles(PintImportCfgBean pintImportCfg) {
		final List<FileTypeBean> ret = new ArrayList<FileTypeBean>();
		if (pintImportCfg.getFileSet() != null) {
			for (final FileTypeBean fileTypeBean : pintImportCfg.getFileSet().getFile()) {
				if (fileTypeBean.getFormat() != FileFormat.EXCEL) {
					ret.add(fileTypeBean);
				}
			}
		}
		return ret;
	}

	public static SampleTypeBean getSample(PintImportCfgBean pintImportConfiguration, String sampleRef) {
		try {
			return getSamples(pintImportConfiguration).stream().filter(sample -> sample.getId().equals(sampleRef))
					.findFirst().get();
		} catch (final NoSuchElementException e) {
			return null;
		}
	}

	public static OrganismTypeBean getOrganism(PintImportCfgBean pintImportConfiguration, String organismRef) {
		try {
			return getOrganisms(pintImportConfiguration).stream()
					.filter(organism -> organism.getId().equals(organismRef)).findFirst().get();
		} catch (final NoSuchElementException e) {
			return null;
		}
	}

	public static TissueTypeBean getTissue(PintImportCfgBean pintImportConfiguration, String tissueRef) {
		try {
			return getTissues(pintImportConfiguration).stream().filter(tissue -> tissue.getId().equals(tissueRef))
					.findFirst().get();
		} catch (final NoSuchElementException e) {
			return null;
		}
	}

	public static LabelTypeBean getLabel(PintImportCfgBean pintImportConfiguration, String labelRef) {
		try {
			return getLabels(pintImportConfiguration).stream().filter(label -> label.getId().equals(labelRef))
					.findFirst().get();
		} catch (final NoSuchElementException e) {
			return null;
		}
	}

	public static String getTitleMSRun(MsRunTypeBean msRun) {
		final StringBuilder sb = new StringBuilder();
		sb.append("MS run: " + msRun.getId());
		if (msRun.getDate() != null) {
			sb.append("\nCreation date: " + dateFormatter.format(msRun.getDate()));
		}
		if (msRun.getPath() != null) {
			sb.append("\nPath: " + msRun.getPath());
		}
		return sb.toString();
	}

	public static List<FileTypeBean> getFiles(PintImportCfgBean pintImportCfg) {
		final List<FileTypeBean> ret = new ArrayList<FileTypeBean>();
		if (pintImportCfg.getFileSet() != null) {
			for (final FileTypeBean fileTypeBean : pintImportCfg.getFileSet().getFile()) {
				ret.add(fileTypeBean);
			}
		}
		return ret;
	}

	public static String getTitleInputFile(FileTypeBean fileType) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Input file: " + fileType.getId());
		sb.append("\nFormat: " + fileType.getFormat().getName());
		if (fileType.getFormat() == FileFormat.EXCEL) {
			sb.append("\nExcel file containing " + fileType.getSheets().getSheet().size() + " sheets:");
			for (final SheetTypeBean sheet : fileType.getSheets().getSheet()) {
				sb.append("\n\tSheet '" + ExcelColumnRefPanel.getSheetID(sheet.getId()) + "' with "
						+ sheet.getColumn().size() + " columns detected");
			}
		}
		return sb.toString();
	}

	public static String getTitleSample(SampleTypeBean sample) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Sample: " + sample.getId());
		if (sample.getDescription() != null) {
			sb.append("\nDescription: " + sample.getDescription());
		}
		if (sample.getOrganismRef() != null) {
			sb.append("\nOrganism: " + sample.getOrganismRef());
		}
		if (sample.getTissueRef() != null) {
			sb.append("\nTissue/Cell type: " + sample.getTissueRef());
		}
		if (sample.getLabelRef() != null) {
			sb.append("\nLabelled with: " + sample.getLabelRef());
		}
		return sb.toString();
	}

	public static String getTitleOrganism(OrganismTypeBean organism) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Organism: " + organism.getId());
		if (organism.getDescription() != null) {
			sb.append("\nDescription: " + organism.getDescription());
		}
		return sb.toString();
	}

	public static String getTitleTissue(TissueTypeBean tissue) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Tissue/Cell type: " + tissue.getId());
		if (tissue.getDescription() != null) {
			sb.append("\nDescription: " + tissue.getDescription());
		}
		return sb.toString();
	}

	public static String getConditionTitle(ExperimentalConditionTypeBean condition) {
		final StringBuilder sb = new StringBuilder();
		sb.append("Experimental condition: " + condition.getId());
		if (condition.getDescription() != null) {
			sb.append("\nDescription: " + condition.getDescription());
		}
		if (condition.getSampleRef() != null && !"".equals(condition.getSampleRef())) {
			sb.append("\nReferenced sample: " + condition.getSampleRef());
		}
		return sb.toString();
	}

	public static ExperimentalConditionTypeBean getCondition(PintImportCfgBean pintImportConfiguration,
			String conditionID) {
		try {
			return getConditions(pintImportConfiguration).stream()
					.filter(condition -> condition.getId().equals(conditionID)).findFirst().get();
		} catch (final NoSuchElementException e) {
			return null;
		}
	}

	public static void addPSMRatio(PintImportCfgBean pintImportConfiguration, RemoteFilesRatioTypeBean ratios) {
		if (pintImportConfiguration.getProject().getRatios() == null) {
			pintImportConfiguration.getProject().setRatios(new RatiosTypeBean());
		}
		if (pintImportConfiguration.getProject().getRatios().getPsmAmountRatios() == null) {
			pintImportConfiguration.getProject().getRatios().setPsmAmountRatios(new PsmRatiosTypeBean());
		}
		pintImportConfiguration.getProject().getRatios().getPsmAmountRatios().getRemoteFilesRatio().add(ratios);
	}

	public static void addPSMRatioFromExcel(PintImportCfgBean pintImportConfiguration,
			ExcelAmountRatioTypeBean ratios) {
		if (pintImportConfiguration.getProject().getRatios() == null) {
			pintImportConfiguration.getProject().setRatios(new RatiosTypeBean());
		}
		if (pintImportConfiguration.getProject().getRatios().getPsmAmountRatios() == null) {
			pintImportConfiguration.getProject().getRatios().setPsmAmountRatios(new PsmRatiosTypeBean());
		}
		pintImportConfiguration.getProject().getRatios().getPsmAmountRatios().getExcelRatio().add(ratios);
	}

	public static void addPeptideRatio(PintImportCfgBean pintImportConfiguration, RemoteFilesRatioTypeBean ratios) {
		if (pintImportConfiguration.getProject().getRatios() == null) {
			pintImportConfiguration.getProject().setRatios(new RatiosTypeBean());
		}
		if (pintImportConfiguration.getProject().getRatios().getPeptideAmountRatios() == null) {
			pintImportConfiguration.getProject().getRatios().setPeptideAmountRatios(new PeptideRatiosTypeBean());
		}
		pintImportConfiguration.getProject().getRatios().getPeptideAmountRatios().getRemoteFilesRatio().add(ratios);
	}

	public static void addPeptideRatioFromExcel(PintImportCfgBean pintImportConfiguration,
			ExcelAmountRatioTypeBean ratios) {
		if (pintImportConfiguration.getProject().getRatios() == null) {
			pintImportConfiguration.getProject().setRatios(new RatiosTypeBean());
		}
		if (pintImportConfiguration.getProject().getRatios().getPeptideAmountRatios() == null) {
			pintImportConfiguration.getProject().getRatios().setPeptideAmountRatios(new PeptideRatiosTypeBean());
		}
		pintImportConfiguration.getProject().getRatios().getPeptideAmountRatios().getExcelRatio().add(ratios);
	}

	public static void addProteinRatio(PintImportCfgBean pintImportConfiguration, RemoteFilesRatioTypeBean ratios) {
		if (pintImportConfiguration.getProject().getRatios() == null) {
			pintImportConfiguration.getProject().setRatios(new RatiosTypeBean());
		}
		if (pintImportConfiguration.getProject().getRatios().getProteinAmountRatios() == null) {
			pintImportConfiguration.getProject().getRatios().setProteinAmountRatios(new ProteinRatiosTypeBean());
		}
		pintImportConfiguration.getProject().getRatios().getProteinAmountRatios().getRemoteFilesRatio().add(ratios);
	}

	public static void addProteinRatioFromExcel(PintImportCfgBean pintImportConfiguration,
			ExcelAmountRatioTypeBean ratios) {
		if (pintImportConfiguration.getProject().getRatios() == null) {
			pintImportConfiguration.getProject().setRatios(new RatiosTypeBean());
		}
		if (pintImportConfiguration.getProject().getRatios().getProteinAmountRatios() == null) {
			pintImportConfiguration.getProject().getRatios().setProteinAmountRatios(new ProteinRatiosTypeBean());
		}
		pintImportConfiguration.getProject().getRatios().getProteinAmountRatios().getExcelRatio().add(ratios);
	}

	public static void removeRatiosByFileID(PintImportCfgBean pintImportConfiguration, String fileID) {
		if (pintImportConfiguration.getProject().getRatios() != null) {
			if (pintImportConfiguration.getProject().getRatios().getPeptideAmountRatios() != null) {
				final Iterator<ExcelAmountRatioTypeBean> iterator = pintImportConfiguration.getProject().getRatios()
						.getPeptideAmountRatios().getExcelRatio().iterator();
				while (iterator.hasNext()) {
					final ExcelAmountRatioTypeBean excelRatio = iterator.next();
					if (getExcelFileIdFromExcelColumnID(excelRatio.getColumnRef()).equals(fileID)) {
						iterator.remove();
						GWT.log("Protein Ratio for file '" + fileID + "' removed");
					}
				}
				final Iterator<RemoteFilesRatioTypeBean> iterator2 = pintImportConfiguration.getProject().getRatios()
						.getPeptideAmountRatios().getRemoteFilesRatio().iterator();
				while (iterator2.hasNext()) {
					final RemoteFilesRatioTypeBean ratio = iterator2.next();
					if (ratio.getFileRef().equals(fileID)) {
						iterator2.remove();
						GWT.log("Protein Ratio for file '" + fileID + "' removed");
					}
				}
			}
			if (pintImportConfiguration.getProject().getRatios().getProteinAmountRatios() != null) {
				final Iterator<ExcelAmountRatioTypeBean> iterator = pintImportConfiguration.getProject().getRatios()
						.getProteinAmountRatios().getExcelRatio().iterator();
				while (iterator.hasNext()) {
					final ExcelAmountRatioTypeBean excelRatio = iterator.next();
					if (getExcelFileIdFromExcelColumnID(excelRatio.getColumnRef()).equals(fileID)) {
						iterator.remove();
						GWT.log("Peptide Ratio for file '" + fileID + "' removed");
					}
				}
				final Iterator<RemoteFilesRatioTypeBean> iterator2 = pintImportConfiguration.getProject().getRatios()
						.getProteinAmountRatios().getRemoteFilesRatio().iterator();
				while (iterator2.hasNext()) {
					final RemoteFilesRatioTypeBean ratio = iterator2.next();
					if (ratio.getFileRef().equals(fileID)) {
						iterator2.remove();
						GWT.log("Peptide Ratio for file '" + fileID + "' removed");
					}
				}
			}
			if (pintImportConfiguration.getProject().getRatios().getPsmAmountRatios() != null) {
				final Iterator<ExcelAmountRatioTypeBean> iterator = pintImportConfiguration.getProject().getRatios()
						.getPsmAmountRatios().getExcelRatio().iterator();
				while (iterator.hasNext()) {
					final ExcelAmountRatioTypeBean excelRatio = iterator.next();
					if (getExcelFileIdFromExcelColumnID(excelRatio.getColumnRef()).equals(fileID)) {
						iterator.remove();
						GWT.log("PSM Ratio for file '" + fileID + "' removed");
					}
				}
				final Iterator<RemoteFilesRatioTypeBean> iterator2 = pintImportConfiguration.getProject().getRatios()
						.getPsmAmountRatios().getRemoteFilesRatio().iterator();
				while (iterator2.hasNext()) {
					final RemoteFilesRatioTypeBean ratio = iterator2.next();
					if (ratio.getFileRef().equals(fileID)) {
						iterator2.remove();
						GWT.log("PSM Ratio for file '" + fileID + "' removed");
					}
				}
			}
		}
	}

	private static String getExcelFileIdFromExcelColumnID(String columnID) {
		if (columnID.contains(SharedConstants.EXCEL_ID_SEPARATOR)) {
			final String[] split = columnID.split(SharedConstants.EXCEL_ID_SEPARATOR);
			if (split.length > 0) {
				return split[0];
			}
		}
		return null;
	}

	public static void removeFile(PintImportCfgBean pintImportConfiguration, String fileID) {
		if (pintImportConfiguration.getFileSet() != null) {
			final Iterator<FileTypeBean> iterator = pintImportConfiguration.getFileSet().getFile().iterator();
			while (iterator.hasNext()) {
				final FileTypeBean file = iterator.next();
				if (file.getId().equals(fileID)) {
					iterator.remove();
				}
			}
		}
		final ProjectTypeBean project = pintImportConfiguration.getProject();
		if (project != null) {
			removeIdentificationsByFileID(pintImportConfiguration, fileID);
			removeQuantificationsByFileID(pintImportConfiguration, fileID);

			final RatiosTypeBean ratios = project.getRatios();
			if (ratios != null) {
				if (ratios.getPeptideAmountRatios() != null) {
					final Iterator<ExcelAmountRatioTypeBean> iterator = ratios.getPeptideAmountRatios().getExcelRatio()
							.iterator();
					while (iterator.hasNext()) {
						final ExcelAmountRatioTypeBean excelRatio = iterator.next();
						final String fileID2 = getExcelFileIdFromExcelColumnID(excelRatio.getColumnRef());
						if (fileID.equals(fileID2)) {
							iterator.remove();
						}
					}
					final Iterator<RemoteFilesRatioTypeBean> iterator2 = ratios.getPeptideAmountRatios()
							.getRemoteFilesRatio().iterator();
					while (iterator2.hasNext()) {
						final RemoteFilesRatioTypeBean ratio = iterator2.next();
						if (ratio.getFileRef().equals(fileID)) {
							iterator2.remove();
						}
					}
					if (ratios.getPeptideAmountRatios().getExcelRatio().isEmpty()
							&& ratios.getPeptideAmountRatios().getRemoteFilesRatio().isEmpty()) {
						ratios.setPeptideAmountRatios(null);
					}
				}
				if (ratios.getProteinAmountRatios() != null) {
					final Iterator<ExcelAmountRatioTypeBean> iterator = ratios.getProteinAmountRatios().getExcelRatio()
							.iterator();
					while (iterator.hasNext()) {
						final ExcelAmountRatioTypeBean excelRatio = iterator.next();
						final String fileID2 = getExcelFileIdFromExcelColumnID(excelRatio.getColumnRef());
						if (fileID.equals(fileID2)) {
							iterator.remove();
						}
					}
					final Iterator<RemoteFilesRatioTypeBean> iterator2 = ratios.getProteinAmountRatios()
							.getRemoteFilesRatio().iterator();
					while (iterator2.hasNext()) {
						final RemoteFilesRatioTypeBean ratio = iterator2.next();
						if (ratio.getFileRef().equals(fileID)) {
							iterator2.remove();
						}
					}
					if (ratios.getProteinAmountRatios().getExcelRatio().isEmpty()
							&& ratios.getProteinAmountRatios().getRemoteFilesRatio().isEmpty()) {
						ratios.setProteinAmountRatios(null);
					}
				}
				if (ratios.getPsmAmountRatios() != null) {
					final Iterator<ExcelAmountRatioTypeBean> iterator = ratios.getPsmAmountRatios().getExcelRatio()
							.iterator();
					while (iterator.hasNext()) {
						final ExcelAmountRatioTypeBean excelRatio = iterator.next();
						final String fileID2 = getExcelFileIdFromExcelColumnID(excelRatio.getColumnRef());
						if (fileID.equals(fileID2)) {
							iterator.remove();
						}
					}
					final Iterator<RemoteFilesRatioTypeBean> iterator2 = ratios.getPsmAmountRatios()
							.getRemoteFilesRatio().iterator();
					while (iterator2.hasNext()) {
						final RemoteFilesRatioTypeBean ratio = iterator2.next();
						if (ratio.getFileRef().equals(fileID)) {
							iterator2.remove();
						}
					}
					if (ratios.getPsmAmountRatios().getExcelRatio().isEmpty()
							&& ratios.getPsmAmountRatios().getRemoteFilesRatio().isEmpty()) {
						ratios.setPsmAmountRatios(null);
					}
				}

			}
		}
	}

	public static void removeQuantificationsByFileID(PintImportCfgBean pintImportConfiguration, String fileID) {

		for (final ExperimentalConditionTypeBean condition : getConditions(pintImportConfiguration)) {
			if (condition.getQuantificationInfo() != null) {
				removeFileFromExcelQuantification(condition.getQuantificationInfo().getExcelQuantInfo(), fileID);
				removeFileFromQuantification(condition.getQuantificationInfo().getRemoteFilesQuantInfo(), fileID);
				if (condition.getQuantificationInfo().getExcelQuantInfo().isEmpty()
						&& condition.getQuantificationInfo().getRemoteFilesQuantInfo().isEmpty()) {
					condition.setQuantificationInfo(null);
				}
			}
		}

	}

	public static void removeIdentificationsByFileID(PintImportCfgBean pintImportConfiguration, String fileID) {

		for (final ExperimentalConditionTypeBean condition : getConditions(pintImportConfiguration)) {
			if (condition.getIdentificationInfo() != null) {
				removeFileFromExcelIdentification(condition.getIdentificationInfo().getExcelIdentInfo(), fileID);
				removeFileFromIdentification(condition.getIdentificationInfo().getRemoteFilesIdentInfo(), fileID);
				if (condition.getIdentificationInfo().getExcelIdentInfo().isEmpty()
						&& condition.getIdentificationInfo().getRemoteFilesIdentInfo().isEmpty()) {
					condition.setIdentificationInfo(null);
				}
			}
		}

	}

	private static boolean removeFileFromQuantification(List<RemoteInfoTypeBean> remoteFilesQuantInfo, String fileID) {
		final Iterator<RemoteInfoTypeBean> iterator = remoteFilesQuantInfo.iterator();
		boolean ret = false;
		while (iterator.hasNext()) {
			final RemoteInfoTypeBean quant = iterator.next();
			if (quant.getFileRefs().contains(fileID)) {
				quant.getFileRefs().remove(fileID);
				ret = true;
			}
			if (quant.getFileRefs().isEmpty() && quant.getMsRunRef() == null) {
				iterator.remove();
			}
		}
		return ret;
	}

	private static boolean removeFileFromIdentification(List<RemoteInfoTypeBean> remoteFilesIdentInfo, String fileID) {
		final Iterator<RemoteInfoTypeBean> iterator = remoteFilesIdentInfo.iterator();
		boolean ret = false;
		while (iterator.hasNext()) {
			final RemoteInfoTypeBean ident = iterator.next();
			if (ident.getFileRefs().contains(fileID)) {
				ident.getFileRefs().remove(fileID);
				ret = true;
			}
			if (ident.getFileRefs().isEmpty() && ident.getMsRunRef() == null) {
				iterator.remove();
			}
		}
		return ret;
	}

	private static void removeFileFromExcelQuantification(List<QuantificationExcelTypeBean> excelQuantInfo,
			String fileID) {
		final Iterator<QuantificationExcelTypeBean> iterator = excelQuantInfo.iterator();
		while (iterator.hasNext()) {
			final QuantificationExcelTypeBean excelQuant = iterator.next();
			boolean remove = false;
			for (final AmountTypeBean amount : excelQuant.getPeptideAmounts()) {
				if (getExcelFileIdFromExcelColumnID(amount.getColumnRef()).equals(fileID)) {
					remove = true;
					break;
				}
			}
			if (remove) {
				iterator.remove();
				continue;
			}
			for (final AmountTypeBean amount : excelQuant.getProteinAmounts()) {
				if (getExcelFileIdFromExcelColumnID(amount.getColumnRef()).equals(fileID)) {
					remove = true;
					break;
				}
			}
			if (remove) {
				iterator.remove();
				continue;
			}
			for (final AmountTypeBean amount : excelQuant.getPsmAmounts()) {
				if (getExcelFileIdFromExcelColumnID(amount.getColumnRef()).equals(fileID)) {
					remove = true;
					break;
				}
			}
			if (remove) {
				iterator.remove();
				continue;
			}
		}
	}

	private static void removeFileFromExcelIdentification(List<IdentificationExcelTypeBean> excelIdentInfo,
			String fileID) {
		final Iterator<IdentificationExcelTypeBean> iterator = excelIdentInfo.iterator();
		boolean remove = false;
		while (iterator.hasNext()) {
			final IdentificationExcelTypeBean excelIdent = iterator.next();
			for (final ScoreTypeBean score : excelIdent.getPeptideScore()) {
				if (getExcelFileIdFromExcelColumnID(score.getColumnRef()).equals(fileID)) {
					remove = true;
					break;
				}
			}
			if (remove) {
				iterator.remove();
				continue;
			}
			if (excelIdent.getProteinAccession() != null) {
				if (getExcelFileIdFromExcelColumnID(excelIdent.getProteinAccession().getColumnRef()).equals(fileID)) {
					iterator.remove();
					continue;
				}
			}
			if (excelIdent.getProteinAnnotations() != null) {
				for (final ProteinAnnotationTypeBean annotation : excelIdent.getProteinAnnotations()
						.getProteinAnnotation()) {
					if (getExcelFileIdFromExcelColumnID(annotation.getColumnRef()).equals(fileID)) {
						remove = true;
						break;
					}
				}
				if (remove) {
					iterator.remove();
					continue;
				}
			}
			if (excelIdent.getProteinDescription() != null) {
				if (getExcelFileIdFromExcelColumnID(excelIdent.getProteinDescription().getColumnRef()).equals(fileID)) {
					iterator.remove();
					continue;
				}
			}
			for (final ScoreTypeBean score : excelIdent.getProteinScore()) {
				if (getExcelFileIdFromExcelColumnID(score.getColumnRef()).equals(fileID)) {
					remove = true;
					break;
				}
			}
			if (remove) {
				iterator.remove();
				continue;
			}
			if (excelIdent.getProteinThresholds() != null) {
				for (final ProteinThresholdTypeBean threshold : excelIdent.getProteinThresholds()
						.getProteinThreshold()) {
					if (getExcelFileIdFromExcelColumnID(threshold.getColumnRef()).equals(fileID)) {
						remove = true;
						break;
					}
				}
				if (remove) {
					iterator.remove();
					continue;
				}
			}
			for (final ScoreTypeBean score : excelIdent.getPsmScore()) {
				if (getExcelFileIdFromExcelColumnID(score.getColumnRef()).equals(fileID)) {
					remove = true;
					break;
				}
			}
			if (remove) {
				iterator.remove();
				continue;
			}
			for (final ScoreTypeBean score : excelIdent.getPtmScore()) {
				if (getExcelFileIdFromExcelColumnID(score.getColumnRef()).equals(fileID)) {
					remove = true;
					break;
				}
			}
			if (remove) {
				iterator.remove();
				continue;
			}
			if (excelIdent.getSequence() != null) {
				if (getExcelFileIdFromExcelColumnID(excelIdent.getSequence().getColumnRef()).equals(fileID)) {
					iterator.remove();
					continue;
				}
			}
		}
	}

	public static boolean addIdentificationToCondition(PintImportCfgBean pintImportConfiguration, String conditionID,
			RemoteInfoTypeBean identificationTypeBean) {
		final ExperimentalConditionTypeBean condition = PintImportCfgUtil.getCondition(pintImportConfiguration,
				conditionID);
		if (condition.getIdentificationInfo() == null) {
			condition.setIdentificationInfo(new IdentificationInfoTypeBean());
		}
		// first check whether there is already an RemoteInfoTypeBean pointing to the
		// same files
		for (final RemoteInfoTypeBean identificationTypeBean2 : condition.getIdentificationInfo()
				.getRemoteFilesIdentInfo()) {
			if (sortAndConcatenate(identificationTypeBean2.getFileRefs())
					.equals(sortAndConcatenate(identificationTypeBean.getFileRefs()))) {
				// We add any non existing reference to a ms run
				identificationTypeBean2.setMsRunRef(
						mergeMSRunRefs(identificationTypeBean2.getMsRunRef(), identificationTypeBean.getMsRunRef()));
				return false;
			}
		}
		return condition.getIdentificationInfo().getRemoteFilesIdentInfo().add(identificationTypeBean);
	}

	private static String mergeMSRunRefs(String msRunRefs1, String msRunRefs2) {
		if (msRunRefs1 == null) {
			return msRunRefs2;
		}
		if (msRunRefs2 == null) {
			return msRunRefs1;
		}
		final List<String> msRunRefList1 = getMSRunRefs(msRunRefs1);
		final List<String> msRunRefList2 = getMSRunRefs(msRunRefs2);
		final List<String> joinedList = new ArrayList<String>();
		joinedList.addAll(msRunRefList1);
		joinedList.addAll(msRunRefList2);
		return sortAndConcatenate(joinedList);
	}

	private static List<String> getMSRunRefs(String msRunRefs) {
		final List<String> ret = new ArrayList<String>();
		if (msRunRefs.contains(",")) {
			final String[] split = msRunRefs.split(",");
			for (final String string : split) {
				ret.add(string);
			}
		} else {
			ret.add(msRunRefs);
		}
		return ret;
	}

	private static String sortAndConcatenate(List<String> list) {
		if (list == null) {
			return "";
		}
		final StringBuilder sb = new StringBuilder();
		Collections.sort(list);
		for (final String string : list) {
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(string);
		}
		return sb.toString();

	}

	public static boolean addExcelIdentificationToCondition(PintImportCfgBean pintImportConfiguration,
			String conditionID, IdentificationExcelTypeBean identificationExcelTypeBean) {
		final ExperimentalConditionTypeBean condition = PintImportCfgUtil.getCondition(pintImportConfiguration,
				conditionID);
		if (condition.getIdentificationInfo() == null) {
			condition.setIdentificationInfo(new IdentificationInfoTypeBean());
		}
		return condition.getIdentificationInfo().getExcelIdentInfo().add(identificationExcelTypeBean);
	}

	public static boolean addQuantificationToCondition(PintImportCfgBean pintImportConfiguration, String conditionID,
			RemoteInfoTypeBean quantInfoTypeBean) {
		final ExperimentalConditionTypeBean condition = PintImportCfgUtil.getCondition(pintImportConfiguration,
				conditionID);
		if (condition.getQuantificationInfo() == null) {
			condition.setQuantificationInfo(new QuantificationInfoTypeBean());
		}
		// first check whether there is already an RemoteInfoTypeBean pointing to the
		// same files
		for (final RemoteInfoTypeBean quantInfoTypeBean2 : condition.getQuantificationInfo()
				.getRemoteFilesQuantInfo()) {
			if (sortAndConcatenate(quantInfoTypeBean2.getFileRefs())
					.equals(sortAndConcatenate(quantInfoTypeBean.getFileRefs()))) {
				// We add any non existing reference to a ms run
				quantInfoTypeBean2
						.setMsRunRef(mergeMSRunRefs(quantInfoTypeBean2.getMsRunRef(), quantInfoTypeBean.getMsRunRef()));
				return false;
			}
		}
		return condition.getQuantificationInfo().getRemoteFilesQuantInfo().add(quantInfoTypeBean);
	}

	public static boolean addExcelQuantificationToCondition(PintImportCfgBean pintImportConfiguration,
			String conditionID, QuantificationExcelTypeBean quantificationExcelTypeBean) {
		final ExperimentalConditionTypeBean condition = PintImportCfgUtil.getCondition(pintImportConfiguration,
				conditionID);
		if (condition.getQuantificationInfo() == null) {
			condition.setQuantificationInfo(new QuantificationInfoTypeBean());
		}
		return condition.getQuantificationInfo().getExcelQuantInfo().add(quantificationExcelTypeBean);
	}

	public static void removeFileFromCondition(String fileID, ExperimentalConditionTypeBean condition) {
		if (condition.getIdentificationInfo() != null) {
			removeFileFromExcelIdentification(condition.getIdentificationInfo().getExcelIdentInfo(), fileID);
			removeFileFromIdentification(condition.getIdentificationInfo().getRemoteFilesIdentInfo(), fileID);
		}
		if (condition.getQuantificationInfo() != null) {
			removeFileFromExcelQuantification(condition.getQuantificationInfo().getExcelQuantInfo(), fileID);
			removeFileFromQuantification(condition.getQuantificationInfo().getRemoteFilesQuantInfo(), fileID);
		}

	}

}
