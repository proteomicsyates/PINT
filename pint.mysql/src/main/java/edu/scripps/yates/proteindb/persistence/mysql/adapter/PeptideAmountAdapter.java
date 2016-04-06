package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.Project;

public class PeptideAmountAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount>,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8212630940707841612L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Amount amount;
	private final static Map<Integer, PeptideAmount> map = new HashMap<Integer, PeptideAmount>();
	private final Peptide peptide;
	private final Project hibProject;
	private static final Logger log = Logger
			.getLogger(PeptideAmountAdapter.class);

	public PeptideAmountAdapter(edu.scripps.yates.utilities.proteomicsmodel.Amount amount,
			Peptide peptide, Project hibProject) {
		this.amount = amount;
		this.peptide = peptide;
		this.hibProject = hibProject;
	}

	@Override
	public PeptideAmount adapt() {
		if (map.containsKey(amount.hashCode())) {
			return map.get(amount.hashCode());

		}
		PeptideAmount ret = new PeptideAmount();

		map.put(amount.hashCode(), ret);
		ret.setAmountType(new AmountTypeAdapter(amount.getAmountType()).adapt());
		if (amount.getCombinationType() != null)
			ret.setCombinationType(new CombinationTypeAdapter(amount
					.getCombinationType(), ret).adapt());

		ret.setValue(amount.getValue());

		// ms runs
		// final Set<MSRun> runs = peptideAmount.getRuns();
		// for (MSRun msRun : runs) {
		// MsRun hibMsRun = new MSRunAdapter(msRun).adapt();
		// ret.getMsRuns().add(hibMsRun);
		// }
		ret.setCondition(new ConditionAdapter(amount.getCondition(), hibProject)
				.adapt());
		ret.setPeptide(peptide);
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
