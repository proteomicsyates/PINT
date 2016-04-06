package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;

public class ExcelAmountRatioTypeAdapter implements Adapter<ExcelAmountRatioType> {
	private final ExcelAmountRatioTypeBean ratio;

	public ExcelAmountRatioTypeAdapter(ExcelAmountRatioTypeBean ratio) {
		this.ratio = ratio;
	}

	@Override
	public ExcelAmountRatioType adapt() {
		ExcelAmountRatioType ret = new ExcelAmountRatioType();

		ret.setColumnRef(ratio.getColumnRef());
		if (ratio.getDenominator() != null)
			ret.setDenominator(new ConditionRefTypeAdapter(ratio.getDenominator()).adapt());
		ret.setName(ratio.getName());
		if (ratio.getNumerator() != null)
			ret.setNumerator(new ConditionRefTypeAdapter(ratio.getNumerator()).adapt());
		if (ratio.getRatioScore() != null)
			ret.setRatioScore(new ScoreTypeAdapter(ratio.getRatioScore()).adapt());

		// one of the three:
		if (ratio.getProteinAccession() != null) {
			ret.setProteinAccession(new ProteinAccessionTypeAdapter(ratio.getProteinAccession()).adapt());
		} else if (ratio.getPeptideSequence() != null) {
			ret.setPeptideSequence(new SequenceTypeAdapter(ratio.getPeptideSequence()).adapt());
		} else if (ratio.getPsmId() != null) {
			ret.setPsmId(new PsmIdTypeAdapter(ratio.getPsmId()).adapt());
		}

		ret.setMsRunRef(ratio.getMsRunRef());

		return ret;
	}
}
