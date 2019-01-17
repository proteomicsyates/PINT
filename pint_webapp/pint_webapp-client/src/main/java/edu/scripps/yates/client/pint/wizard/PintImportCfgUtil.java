package edu.scripps.yates.client.pint.wizard;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.uploader.client.File;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ExcelColumnRefPanel;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class PintImportCfgUtil {

	public static FileTypeBean addFile(PintImportCfgBean pintImportConfiguration, File file) throws PintException {
		if (pintImportConfiguration.getFileSet() == null) {
			pintImportConfiguration.setFileSet(new FileSetTypeBean());
		}
		for (final FileTypeBean file2 : pintImportConfiguration.getFileSet().getFile()) {
			if (file2.getId().equals(file.getId())) {
				throw new PintException(
						"A file with the name '" + file.getId()
								+ "' is already created. Please rename the file before uploading it.",
						PINT_ERROR_TYPE.ITEM_ID_REPEATED);
			}
		}
		final FileTypeBean newFileTypeBean = new FileTypeBean();
		pintImportConfiguration.getFileSet().getFile().add(newFileTypeBean);
		newFileTypeBean.setId(file.getId());
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

	public static void updateSampleID(PintImportCfgBean pintImportCfgBean, String oldID, String newID) {
		final List<SampleTypeBean> samples = getSamples(pintImportCfgBean);
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

	public static void updateConditionID(PintImportCfgBean pintImportConfiguration, String oldID, String newID) {
		final List<ExperimentalConditionTypeBean> conditions = getConditions(pintImportConfiguration);
		for (final ExperimentalConditionTypeBean experimentalConditionTypeBean : conditions) {
			if (experimentalConditionTypeBean.getId().equals(oldID)) {
				experimentalConditionTypeBean.setId(newID);
			}
		}
		// TODO to change something referencing any condition
	}

	public static void updateOrganism(PintImportCfgBean pintImportConfiguration, String oldID, String newID) {
		final List<OrganismTypeBean> organisms = getOrganisms(pintImportConfiguration);
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

	public static void updateTissue(PintImportCfgBean pintImportConfiguration, String oldID, String newID) {
		final List<TissueTypeBean> Tissues = getTissues(pintImportConfiguration);
		for (final TissueTypeBean tissueTypeBean : Tissues) {
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
}
