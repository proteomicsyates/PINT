package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExcelAmountRatioType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;

public class ExcelAmountRatioTypeBeanAdapter implements Adapter<ExcelAmountRatioTypeBean> {
	private final ExcelAmountRatioType ratio;

	public ExcelAmountRatioTypeBeanAdapter(ExcelAmountRatioType ratio) {
		this.ratio = ratio;
	}

	@Override
	public ExcelAmountRatioTypeBean adapt() {
		ExcelAmountRatioTypeBean ret = new ExcelAmountRatioTypeBean();

		ret.setColumnRef(ratio.getColumnRef());
		if (ratio.getDenominator() != null)
			ret.setDenominator(ratio.getDenominator().getConditionRef());
		ret.setName(ratio.getName());
		if (ratio.getNumerator() != null)
			ret.setNumerator(ratio.getNumerator().getConditionRef());
		if (ratio.getRatioScore() != null)
			ret.setRatioScore(new ScoreTypeBeanAdapter(ratio.getRatioScore()).adapt());

		// one of thr three
		if (ratio.getProteinAccession() != null) {
			ret.setProteinAccession(new ProteinAccessionTypeBeanAdapter(ratio.getProteinAccession()).adapt());
		} else if (ratio.getPeptideSequence() != null) {
			ret.setPeptideSequence(new PeptideSequenceTypeBeanAdapter(ratio.getPeptideSequence()).adapt());
		} else if (ratio.getPsmId() != null) {
			ret.setPsmId(new PsmTypeBeanAdapter(ratio.getPsmId()).adapt());
		}
		ret.setMsRunRef(ratio.getMsRunRef());
		return ret;
	}
}
