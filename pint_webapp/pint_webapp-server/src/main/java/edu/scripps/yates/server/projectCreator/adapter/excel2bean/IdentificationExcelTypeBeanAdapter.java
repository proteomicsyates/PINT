package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAnnotationType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinThresholdType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PtmScoreType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.IdentificationExcelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinAnnotationsTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProteinThresholdsTypeBean;

public class IdentificationExcelTypeBeanAdapter implements Adapter<IdentificationExcelTypeBean> {
	private final IdentificationExcelType excelFileInfo;

	public IdentificationExcelTypeBeanAdapter(IdentificationExcelType excelFileInfo) {
		this.excelFileInfo = excelFileInfo;
	}

	@Override
	public IdentificationExcelTypeBean adapt() {
		final IdentificationExcelTypeBean ret = new IdentificationExcelTypeBean();
		if (excelFileInfo.getProteinAccession() != null) {
			ret.setProteinAccession(new ProteinAccessionTypeBeanAdapter(excelFileInfo.getProteinAccession()).adapt());
		}
		if (excelFileInfo.getProteinDescription() != null) {
			ret.setProteinDescription(
					new ProteinDescriptionTypeBeanAdapter(excelFileInfo.getProteinDescription()).adapt());
		}
		if (excelFileInfo.getProteinAnnotations() != null) {
			ret.setProteinAnnotations(new ProteinAnnotationsTypeBean());
			for (final ProteinAnnotationType proteinAnnotationType : excelFileInfo.getProteinAnnotations()
					.getProteinAnnotation()) {
				ret.getProteinAnnotations().getProteinAnnotation()
						.add(new ProteinAnnotationTypeBeanAdapter(proteinAnnotationType).adapt());
			}
		}
		if (excelFileInfo.getProteinScore() != null) {
			for (final ScoreType scoreType : excelFileInfo.getProteinScore()) {
				ret.getProteinScore().add(new ScoreTypeBeanAdapter(scoreType).adapt());
			}
		}
		if (excelFileInfo.getSequence() != null) {
			ret.setSequence(new SequenceTypeBeanAdapter(excelFileInfo.getSequence()).adapt());
		}
		if (excelFileInfo.getPsmId() != null) {
			ret.setPsmId(new PsmTypeBeanAdapter(excelFileInfo.getPsmId()).adapt());
		}
		if (excelFileInfo.getPsmScore() != null) {
			for (final ScoreType psmScoreType : excelFileInfo.getPsmScore()) {
				ret.getPsmScore().add(new ScoreTypeBeanAdapter(psmScoreType).adapt());
			}
		}
		if (excelFileInfo.getPtmScore() != null) {
			for (final PtmScoreType ptmScoreType : excelFileInfo.getPtmScore()) {
				ret.getPtmScore().add(new PtmScoreTypeBeanAdapter(ptmScoreType).adapt());
			}
		}
		if (excelFileInfo.getSequence() != null) {
			ret.setSequence(new SequenceTypeBeanAdapter(excelFileInfo.getSequence()).adapt());
		}
		if (excelFileInfo.getPeptideScore() != null) {
			for (final ScoreType peptideScoreType : excelFileInfo.getPeptideScore()) {
				ret.getPeptideScore().add(new ScoreTypeBeanAdapter(peptideScoreType).adapt());
			}
		}
		if (excelFileInfo.getMsRunRef() != null) {
			ret.setMsRunRef(excelFileInfo.getMsRunRef());
		}
		if (excelFileInfo.getDiscardDecoys() != null) {
			ret.setDiscardDecoys(excelFileInfo.getDiscardDecoys());
		}

		if (excelFileInfo.getProteinThresholds() != null
				&& excelFileInfo.getProteinThresholds().getProteinThreshold() != null
				&& !excelFileInfo.getProteinThresholds().getProteinThreshold().isEmpty()) {
			ret.setProteinThresholds(new ProteinThresholdsTypeBean());
			for (final ProteinThresholdType proteinThreshold : excelFileInfo.getProteinThresholds()
					.getProteinThreshold()) {
				ret.getProteinThresholds().getProteinThreshold()
						.add(new ProteinThresholdTypeBeanAdapter(proteinThreshold).adapt());
			}
		}
		return ret;
	}
}
