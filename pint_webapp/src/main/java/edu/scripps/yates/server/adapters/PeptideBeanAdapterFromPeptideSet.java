package edu.scripps.yates.server.adapters;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.server.cache.ServerCachePSMBeansByPSMDBId;
import edu.scripps.yates.server.cache.ServerCacheProteinBeansByProteinDBId;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;

/**
 * Adapter for creating a single {@link ProteinBean} from a {@link Collection}
 * of {@link Protein}
 *
 * @author Salva
 *
 */
public class PeptideBeanAdapterFromPeptideSet implements Adapter<PeptideBean> {
	private final static Logger log = Logger.getLogger(PeptideBeanAdapterFromPeptideSet.class);
	private final Set<Peptide> peptideSet;

	public PeptideBeanAdapterFromPeptideSet(Set<Peptide> peptideSet) {
		this.peptideSet = peptideSet;
	}

	@Override
	public PeptideBean adapt() {
		PeptideBean ret = null;
		// if (primaryAcc != null && map.containsKey(primaryAcc)) {
		// ret = map.get(primaryAcc);
		// } else {
		ret = new PeptideBean();

		// if (primaryAcc != null)
		// map.put(primaryAcc, ret);
		// }
		for (Peptide peptide : peptideSet) {
			addPeptideInformation(ret, peptide);
		}
		return ret;
	}

	private void addPeptideInformation(PeptideBean ret, Peptide peptide) {

		ret.addDbId(peptide.getId());

		if (peptide.getSequence() != null) {
			ret.setLength(peptide.getSequence().length());
			ret.setSequence(peptide.getSequence());
		}

		final Set<Protein> proteins = peptide.getProteins();
		if (proteins != null) {
			boolean oneProteinIsGood = false;
			for (Protein protein : proteins) {
				final ProteinBean proteinBean = ServerCacheProteinBeansByProteinDBId.getInstance()
						.getFromCache(protein.getId());
				if (proteinBean != null) {
					oneProteinIsGood = true;
					ret.addProteinToPeptide(proteinBean);
				}

			}
			if (!oneProteinIsGood) {
				log.warn("Peptide id: " + peptide.getId()
						+ " has not valid associated protein. It should have been filtered out before");
			}

		}

		// log.debug("Adapting msRun for peptide DbID: " + peptide.getId());
		ret.addMSRun(new MSRunBeanAdapter(peptide.getMsRun()).adapt());
		// log.debug("Adapting amounts for peptide DbID: " + peptide.getId());

		if (peptide.getPeptideAmounts() != null) {
			for (Object obj : peptide.getPeptideAmounts()) {
				PeptideAmount peptideAmount = (PeptideAmount) obj;
				final AmountBean amount = new AmountBeanAdapter(peptideAmount, peptide.getMsRun()).adapt();
				ret.addAmount(amount);
			}
		}
		// log.debug("Adapting ratios for peptide DbID: " + peptide.getId());
		if (peptide.getPeptideRatioValues() != null) {
			for (Object obj : peptide.getPeptideRatioValues()) {
				PeptideRatioValue peptideRatioValue = (PeptideRatioValue) obj;
				ret.addRatio(new PeptideRatioBeanAdapter(peptideRatioValue).adapt());
			}
		}

		Map<Condition, Set<Psm>> psmsByCondition = new HashMap<Condition, Set<Psm>>();
		// log.debug("Adapting psms for peptide DbID: " + peptide.getId());

		for (Object obj : peptide.getPsms()) {
			final Psm psm = (Psm) obj;
			final PSMBean psmBeanByPSMId = ServerCachePSMBeansByPSMDBId.getInstance().getFromCache(psm.getId());
			if (psmBeanByPSMId != null) {
				ret.addPSMToPeptide(psmBeanByPSMId);
				psmBeanByPSMId.setPeptideBeanToPSM(ret);
			} else {
				// if it is not in the cache, is because the PSMBean has not
				// created, because it was filtered out by a query
				final String message = "PSM with DB id: " + psm.getId() + " [" + psm.getPsmId() + ", "
						+ psm.getSequence()
						+ "] is not found in the ServerCachePSMBeansByPSMDBId. You need to create the PSMBean objects before create the peptide bean objects";
				continue;
				// throw new IllegalArgumentException(message);
			}
			if (!ret.getPSMDBIds().contains(psm.getId())) {
				ret.getPSMDBIds().add(psm.getId());

				final Set<Condition> conditions = psm.getConditions();
				for (Condition condition : conditions) {
					final ExperimentalConditionBean conditionBean = new ConditionBeanAdapter(condition).adapt();

					if (psmsByCondition.containsKey(condition)) {
						psmsByCondition.get(condition).add(psm);
					} else {
						Set<Psm> set = new HashSet<Psm>();
						set.add(psm);
						psmsByCondition.put(condition, set);
					}

					// psms by condition ID
					if (ret.getPSMDBIdsByCondition().containsKey(conditionBean)) {
						ret.getPSMDBIdsByCondition().get(conditionBean).add(psm.getId());
					} else {
						Set<Integer> set = new HashSet<Integer>();
						set.add(psm.getId());
						ret.getPSMDBIdsByCondition().put(conditionBean, set);
					}
				}
				// psm by run id
				final MSRunBean msRunBean = new MSRunBeanAdapter(psm.getMsRun()).adapt();
				if (ret.getPSMDBIdsbyMSRun().containsKey(msRunBean)) {
					ret.getPSMDBIdsbyMSRun().get(msRunBean).add(psm.getId());
				} else {
					Set<Integer> set = new HashSet<Integer>();
					set.add(psm.getId());
					ret.getPSMDBIdsbyMSRun().put(msRunBean, set);
				}
			}
		}

		log.debug("PSMs: " + ret.getPSMDBIds().size());
		log.debug("PSMs by conditions: " + ret.getPSMDBIdsByCondition().size());
		log.debug("PSMs by msruns: " + ret.getPSMDBIdsbyMSRun().size());

		boolean hasManualSPCs = false;
		for (AmountBean amountBean : ret.getAmounts()) {
			// not do this in SPC created from excel
			// files, which may be recalculated somehow
			if (amountBean.getManualSPC() != null && amountBean.getManualSPC()) {
				log.debug("peptide having manual SPC. Not recalculating it");
				hasManualSPCs = true;
				break;
			}
		}
		if (!hasManualSPCs) {
			log.debug("peptide doesnt have manual SPC. Checking if it is necessary to add or change SPC");
			for (ExperimentalConditionBean condition : ret.getPSMDBIdsByCondition().keySet()) {
				Set<Integer> psmsByCond = ret.getPSMDBIdsByCondition().get(condition);
				for (MSRunBean msRunBean : ret.getPSMDBIdsbyMSRun().keySet()) {
					final Set<Integer> psmsByRun = ret.getPSMDBIdsbyMSRun().get(msRunBean);
					// num in common
					int numCommon = 0;
					for (Integer integer : psmsByRun) {
						if (psmsByCond.contains(integer))
							numCommon++;
					}
					log.debug("SPC expected for MSRun:" + msRunBean.getRunID() + " and condition: " + condition.getId()
							+ " =" + numCommon);
					boolean amountfound = false;
					if (ret.getAmounts() != null && !ret.getAmounts().isEmpty()) {
						final Iterator<AmountBean> iterator = ret.getAmounts().iterator();
						while (iterator.hasNext()) {
							AmountBean amountBean = iterator.next();
							// not do this in SPC created from excel
							// files, which may be recalculated somehow
							if (amountBean.getManualSPC() != null && amountBean.getManualSPC()) {
								log.debug("has manual SPC. Skipping recalculation.");
								continue;
							}
							// for SPCs:
							if (amountBean.getAmountType() == edu.scripps.yates.shared.model.AmountType.SPC) {
								if (msRunBean.equals(amountBean.getMsRun())
										&& condition.equals(amountBean.getExperimentalCondition())) {
									amountfound = true;
									log.debug("Amount found");
									// recalculate it if necessary
									if (numCommon > 0) {
										if (Double.compare(numCommon, amountBean.getValue()) != 0) {
											log.debug("Adjusting SPC for peptide " + ret.getSequence() + " from "
													+ amountBean.getValue() + " to " + numCommon);
											amountBean.setValue(numCommon);
										}
									} else {
										log.debug("Removing SPC for peptide " + ret.getSequence() + " from "
												+ amountBean.getValue() + " to " + numCommon);
										iterator.remove();
									}

								}
							}
						}
					}
					if (!amountfound && numCommon > 0) {
						log.debug("Adding SPC for peptide " + ret.getSequence() + " value= " + numCommon);
						AmountBean amount = new AmountBean();
						amount.setAmountType(edu.scripps.yates.shared.model.AmountType.SPC);
						amount.setExperimentalCondition(condition);
						amount.setMsRun(msRunBean);
						amount.setValue(numCommon);
						ret.addAmount(amount);
					}

				}

			}
		}

		// log.debug("Adapting roganisms for peptide DbID: " + peptide.getId());

		// conditions
		if (peptide.getConditions() != null) {
			for (Object obj : peptide.getConditions()) {
				Condition condition = (Condition) obj;
				ret.addCondition(new ConditionBeanAdapter(condition).adapt());
			}
		}

	}
}
