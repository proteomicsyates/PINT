package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;

public class ProteinAmountAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount>,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8212630940707841612L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Amount proteinAmount;
	private final Project project;
	private final Protein protein;
	private final static Map<Integer, ProteinAmount> map = new HashMap<Integer, ProteinAmount>();
	private static final Logger log = Logger
			.getLogger(ProteinAmountAdapter.class);

	public ProteinAmountAdapter(edu.scripps.yates.utilities.proteomicsmodel.Amount proteinAmount,
			Project project, Protein protein) {
		this.proteinAmount = proteinAmount;
		this.project = project;
		this.protein = protein;
	}

	@Override
	public ProteinAmount adapt() {
		if (map.containsKey(proteinAmount.hashCode())) {
			return map.get(proteinAmount.hashCode());

		}
		ProteinAmount ret = new ProteinAmount();

		map.put(proteinAmount.hashCode(), ret);
		ret.setAmountType(new AmountTypeAdapter(proteinAmount.getAmountType())
				.adapt());
		if (proteinAmount.getCombinationType() != null)
			ret.setCombinationType(new CombinationTypeAdapter(proteinAmount
					.getCombinationType(), ret).adapt());

		ret.setValue(proteinAmount.getValue());
		ret.setCondition(new ConditionAdapter(proteinAmount.getCondition(),
				project).adapt());
		ret.setManualSPC(proteinAmount.isManualSpc());
		ret.setProtein(protein);
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
