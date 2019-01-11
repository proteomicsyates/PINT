package edu.scripps.yates.client.pint.wizard;

import org.moxieapps.gwt.uploader.client.File;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ExcelColumnRefPanel;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalDesignTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;

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

}
