package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAnnotationsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinThresholdsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;

public class IdentificationExcelTypeAdapter implements Adapter<IdentificationExcelType> {
	private final IdentificationExcelTypeBean excelFileInfo;

	public IdentificationExcelTypeAdapter(IdentificationExcelTypeBean excelFileInfo) {
		this.excelFileInfo = excelFileInfo;
	}

	@Override
	public IdentificationExcelType adapt() {
		IdentificationExcelType ret = new IdentificationExcelType();

		ret.setMsRunRef(excelFileInfo.getMsRunRef());
		if (excelFileInfo.getProteinAccession() != null) {
			ret.setProteinAccession(new ProteinAccessionTypeAdapter(excelFileInfo.getProteinAccession()).adapt());
		}
		if (excelFileInfo.getProteinDescription() != null) {
			ret.setProteinDescription(new ProteinDescriptionTypeAdapter(excelFileInfo.getProteinDescription()).adapt());
		}
		if (excelFileInfo.getProteinAnnotations() != null) {
			ret.setProteinAnnotations(new ProteinAnnotationsType());
			for (ProteinAnnotationTypeBean proteinAnnotationTypeBean : excelFileInfo.getProteinAnnotations()
					.getProteinAnnotation()) {
				ret.getProteinAnnotations().getProteinAnnotation()
						.add(new ProteinAnnotationTypeAdapter(proteinAnnotationTypeBean).adapt());
			}
		}
		if (excelFileInfo.getProteinThresholds() != null) {
			ret.setProteinThresholds(new ProteinThresholdsType());
			for (ProteinThresholdTypeBean proteinThresholdTypeBean : excelFileInfo.getProteinThresholds()
					.getProteinThreshold()) {
				ret.getProteinThresholds().getProteinThreshold()
						.add(new ProteinThresholdTypeAdapter(proteinThresholdTypeBean).adapt());
			}
		}
		if (excelFileInfo.getProteinScore() != null) {
			for (ScoreTypeBean scoreTypeBean : excelFileInfo.getProteinScore()) {
				ret.getProteinScore().add(new ScoreTypeAdapter(scoreTypeBean).adapt());
			}
		}

		if (excelFileInfo.getPsmScore() != null) {
			for (ScoreTypeBean psmScoreTypeBean : excelFileInfo.getPsmScore()) {
				ret.getPsmScore().add(new ScoreTypeAdapter(psmScoreTypeBean).adapt());
			}
		}
		if (excelFileInfo.getPtmScore() != null) {
			for (PtmScoreTypeBean ptmScoreTypeBean : excelFileInfo.getPtmScore()) {
				ret.getPtmScore().add(new PtmScoreTypeAdapter(ptmScoreTypeBean).adapt());
			}
		}

		if (excelFileInfo.getSequence() != null) {
			ret.setSequence(new SequenceTypeAdapter(excelFileInfo.getSequence()).adapt());
		}
		if (excelFileInfo.getPeptideScore() != null) {
			for (ScoreTypeBean scoreTypebean : excelFileInfo.getPeptideScore()) {
				ret.getPeptideScore().add(new ScoreTypeAdapter(scoreTypebean).adapt());
			}
		}
		if (excelFileInfo.getDiscardDecoys() != null) {
			ret.setDiscardDecoys(excelFileInfo.getDiscardDecoys());
		}
		return ret;
	}
}
