package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;

public class ProteinAmountAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8212630940707841612L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Amount proteinAmount;
	private final Project project;
	private final Protein protein;
	private static final Logger log = Logger.getLogger(ProteinAmountAdapter.class);

	public ProteinAmountAdapter(edu.scripps.yates.utilities.proteomicsmodel.Amount proteinAmount, Project project,
			Protein protein) {
		this.proteinAmount = proteinAmount;
		this.project = project;
		this.protein = protein;
	}

	@Override
	public ProteinAmount adapt() {

		final ProteinAmount ret = new ProteinAmount();

		ret.setAmountType(proteinAmount.getAmountType().name());
		if (proteinAmount.getCombinationType() != null)
			ret.setCombinationType(proteinAmount.getCombinationType().getName());

		ret.setValue(proteinAmount.getValue());
		ret.setCondition(new ConditionAdapter(proteinAmount.getCondition(), project).adapt());
		ret.setManualSPC(proteinAmount.isManualSpc());
		ret.setProtein(protein);
		return ret;
	}

}
