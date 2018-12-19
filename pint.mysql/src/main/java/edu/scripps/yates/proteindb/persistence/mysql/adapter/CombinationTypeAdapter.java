package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.CombinationType;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import gnu.trove.map.hash.THashMap;

public class CombinationTypeAdapter implements Adapter<CombinationType>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6274363811783352819L;
	private final edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType combinationType;
	private final ProteinAmount proteinAmount;
	private final ProteinRatioValue proteinRatio;
	private final PeptideAmount peptideAmount;
	private final PeptideRatioValue peptideRatio;
	private final PsmAmount psmAmount;
	private final PsmRatioValue psmRatio;
	private final static Map<String, CombinationType> map = new THashMap<String, CombinationType>();

	public CombinationTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType combinationType,
			ProteinAmount proteinAmount) {
		this.combinationType = combinationType;
		this.proteinAmount = proteinAmount;
		proteinRatio = null;
		peptideAmount = null;
		psmAmount = null;
		peptideRatio = null;
		psmRatio = null;
	}

	public CombinationTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType combinationType,
			PeptideAmount peptideAmount) {
		this.combinationType = combinationType;
		this.peptideAmount = peptideAmount;
		proteinAmount = null;
		proteinRatio = null;
		psmAmount = null;
		peptideRatio = null;
		psmRatio = null;
	}

	public CombinationTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType combinationType,
			ProteinRatioValue proteinRatio) {
		this.combinationType = combinationType;
		this.proteinRatio = proteinRatio;
		proteinAmount = null;
		peptideAmount = null;
		psmAmount = null;
		peptideRatio = null;
		psmRatio = null;
	}

	public CombinationTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType combinationType,
			PsmAmount amount) {
		this.combinationType = combinationType;
		proteinRatio = null;
		proteinAmount = null;
		peptideAmount = null;
		psmAmount = amount;
		peptideRatio = null;
		psmRatio = null;
	}

	public CombinationTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType combinationType,
			PeptideRatioValue ratio) {
		this.combinationType = combinationType;
		proteinRatio = null;
		proteinAmount = null;
		peptideAmount = null;
		psmAmount = null;
		peptideRatio = ratio;
		psmRatio = null;
	}

	public CombinationTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType combinationType,
			PsmRatioValue ratio) {
		this.combinationType = combinationType;
		proteinRatio = null;
		proteinAmount = null;
		peptideAmount = null;
		psmAmount = null;
		peptideRatio = null;
		psmRatio = ratio;
	}

	@Override
	public CombinationType adapt() {
		CombinationType ret = new CombinationType(combinationType.getName());
		if (map.containsKey(combinationType.getName()))
			return map.get(combinationType.getName());
		map.put(combinationType.getName(), ret);
		ret.setDescription(combinationType.getDescription());
		if (proteinAmount != null)
			ret.getProteinAmounts().add(proteinAmount);

		if (proteinRatio != null)
			ret.getProteinRatioValues().add(proteinRatio);

		if (peptideAmount != null)
			ret.getPeptideAmounts().add(peptideAmount);

		if (peptideRatio != null)
			ret.getPeptideRatioValues().add(peptideRatio);

		if (psmAmount != null)
			ret.getPsmAmounts().add(psmAmount);

		if (psmRatio != null)
			ret.getPsmRatioValues().add(psmRatio);
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
