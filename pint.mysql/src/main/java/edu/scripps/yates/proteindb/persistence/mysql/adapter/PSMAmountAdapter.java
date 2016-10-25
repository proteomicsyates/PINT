package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;

public class PSMAmountAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.PsmAmount>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8212630940707841612L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Amount amount;
	private final Psm psm;
	private static final Logger log = Logger.getLogger(PSMAmountAdapter.class);
	private final Project hibProject;

	public PSMAmountAdapter(edu.scripps.yates.utilities.proteomicsmodel.Amount amount, Psm psm, Project hibProject) {
		this.amount = amount;
		this.psm = psm;
		this.hibProject = hibProject;
	}

	@Override
	public PsmAmount adapt() {
		PsmAmount ret = new PsmAmount();

		ret.setAmountType(new AmountTypeAdapter(amount.getAmountType()).adapt());
		if (amount.getCombinationType() != null)
			ret.setCombinationType(new CombinationTypeAdapter(amount.getCombinationType(), ret).adapt());

		ret.setValue(amount.getValue());
		ret.setCondition(new ConditionAdapter(amount.getCondition(), hibProject).adapt());
		ret.setSingleton(amount.isSingleton());
		// ms runs
		// final Set<MSRun> runs = peptideAmount.getRuns();
		// for (MSRun msRun : runs) {
		// MsRun hibMsRun = new MSRunAdapter(msRun).adapt();
		// ret.getMsRuns().add(hibMsRun);
		// }

		ret.setPsm(psm);
		return ret;
	}

}
