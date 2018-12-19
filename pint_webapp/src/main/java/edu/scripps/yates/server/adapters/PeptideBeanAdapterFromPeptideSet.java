package edu.scripps.yates.server.adapters;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.MSRunToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.PeptideAmountToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.PeptideRatioToPeptideTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.AmountValueWrapper;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.RatioValueWrapper;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePeptideSet;
import edu.scripps.yates.server.cache.ServerCachePSMBeansByPSMDBId;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.exceptions.PintRuntimeException;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

/**
 * Adapter for creating a single {@link ProteinBean} from a {@link Collection}
 * of {@link Protein}
 *
 * @author Salva
 *
 */
public class PeptideBeanAdapterFromPeptideSet implements Adapter<PeptideBean> {
	private final static Logger log = Logger.getLogger(PeptideBeanAdapterFromPeptideSet.class);
	private final QueriablePeptideSet queriablePeptideSet;
	private final boolean psmCentric;
	private final Collection<String> hiddenPTMs;
	public static final java.util.Map<String, PeptideBean> map = new THashMap<String, PeptideBean>();

	public PeptideBeanAdapterFromPeptideSet(QueriablePeptideSet queriablePeptideSet, Set<String> hiddenPTMs,
			boolean psmCentric) {
		this.queriablePeptideSet = queriablePeptideSet;
		this.psmCentric = psmCentric;
		this.hiddenPTMs = hiddenPTMs;
	}

	public static void clearStaticMap() {
		map.clear();
	}

	@Override
	public PeptideBean adapt() {
		if (map.containsKey(queriablePeptideSet.getFullSequence())) {
			return map.get(queriablePeptideSet.getFullSequence());
		}
		PeptideBean ret = null;
		// if (primaryAcc != null && map.containsKey(primaryAcc)) {
		// ret = map.get(primaryAcc);
		// } else {
		ret = new PeptideBean();
		map.put(queriablePeptideSet.getFullSequence(), ret);
		ret.setNumPSMs(0);
		// if (primaryAcc != null)
		// map.put(primaryAcc, ret);
		// }
		for (final Peptide peptide : queriablePeptideSet.getIndividualPeptides()) {
			addPeptideInformation(ret, peptide);
		}

		return ret;
	}

	private void addPeptideInformation(PeptideBean ret, Peptide peptide) {

		ret.addDbId(peptide.getId());

		if (peptide.getNumPsms() != null) {
			ret.setNumPSMs(ret.getNumPSMs() + peptide.getNumPsms());
		}
		if (peptide.getSequence() != null) {
			ret.setLength(peptide.getSequence().length());
			ret.setSequence(peptide.getSequence());

		}

		if (!peptide.getFullSequence().equals(peptide.getSequence())) {
			final Set<Ptm> ptms = peptide.getPtms();
			ret.setFullSequence(
					PSMBeanAdapter.getFullSequence(peptide.getFullSequence(), peptide.getSequence(), ptms, hiddenPTMs));
			if (ptms != null) {
				for (final Object obj : ptms) {
					final Ptm ptm = (Ptm) obj;
					// skip PTMs in the list of hidden PTMs
					if (hiddenPTMs != null && hiddenPTMs.contains(ptm.getName()))
						continue;
					ret.addPtm(new PTMBeanAdapter(ptm).adapt());
				}
			}
		} else {
			ret.setFullSequence(peptide.getSequence());
		}

		// final Set<Protein> proteins = peptide.getProteins();
		// if (proteins != null) {
		// boolean oneProteinIsGood = false;
		// for (final Protein protein : proteins) {
		// final ProteinBean proteinBean =
		// ServerCacheProteinBeansByProteinDBId.getInstance()
		// .getFromCache(protein.getId());
		// if (proteinBean != null) {
		// if (ret.getSequence() != null) {
		// proteinBean.addDifferentSequence(ret.getSequence());
		// }
		// oneProteinIsGood = true;
		// final boolean added = ret.addProteinToPeptide(proteinBean);
		// if (psmCentric && added) {
		// // add num psms per condition and per msrun
		// for (final Object conditionObj : peptide.getConditions()) {
		// final Condition condition = (Condition) conditionObj;
		// final ExperimentalConditionBean conditionBean = new
		// ConditionBeanAdapter(condition).adapt();
		// if (!proteinBean.getPSMDBIdsByCondition().containsKey(conditionBean))
		// {
		// final Set<Integer> set = new THashSet<Integer>();
		// proteinBean.getPSMDBIdsByCondition().put(conditionBean, set);
		// }
		// for (final Object obj : peptide.getPsms()) {
		// final Psm psm = (Psm) obj;
		// proteinBean.getPSMDBIdsByCondition().get(conditionBean).add(psm.getId());
		// }
		// }
		//
		// }
		// }
		//
		// }
		// if (!oneProteinIsGood) {
		// log.warn("Peptide id: " + peptide.getId()
		// + " has not valid associated protein. It should have been filtered
		// out before");
		// }
		//
		// }

		// log.debug("Adapting amounts for peptide DbID: " + peptide.getId());

		final List<AmountValueWrapper> peptideAmountValueWrappers = PeptideAmountToPeptideTableMapper.getInstance()
				.mapPeptide(peptide);
		if (peptideAmountValueWrappers != null && !peptideAmountValueWrappers.isEmpty()) {
			for (final AmountValueWrapper peptideAmountValueWrapper : peptideAmountValueWrappers) {
				AmountBean peptideAmountValueBean = AmountBeanAdapter
						.getBeanByPeptideAmountValueID(peptideAmountValueWrapper.getId());
				if (peptideAmountValueBean == null) {
					peptideAmountValueBean = new AmountBean();
					peptideAmountValueBean.setExperimentalCondition(
							ConditionBeanAdapter.getBeanByConditionID(peptideAmountValueWrapper.getConditionID()));
					if (peptideAmountValueWrapper.getAmountType() != null) {
						peptideAmountValueBean.setAmountType(edu.scripps.yates.shared.model.AmountType
								.fromValue(peptideAmountValueWrapper.getAmountType().getName()));
					}
					peptideAmountValueBean.setValue(peptideAmountValueWrapper.getValue());
					peptideAmountValueBean.setManualSPC(peptideAmountValueWrapper.getManualSPC());
					peptideAmountValueBean.setComposed(peptideAmountValueWrapper.getCombinationType() != null);
					for (final Object obj : peptide.getMsRuns()) {
						final MsRun msRun = (MsRun) obj;
						peptideAmountValueBean.addMsRun(new MSRunBeanAdapter(msRun, false).adapt());
					}

				}
				ret.addAmount(peptideAmountValueBean);
			}
		}
		// if (peptide.getPeptideAmounts() != null) {
		// for (final Object obj : peptide.getPeptideAmounts()) {
		// final PeptideAmount peptideAmount = (PeptideAmount) obj;
		// final AmountBean amount = new AmountBeanAdapter(peptideAmount,
		// peptide.getMsRun()).adapt();
		// ret.addAmount(amount);
		// }
		// }
		// log.debug("Adapting ratios for peptide DbID: " + peptide.getId());

		final List<RatioValueWrapper> peptideRatioValueWrappers = PeptideRatioToPeptideTableMapper.getInstance()
				.mapPeptide(peptide);
		if (peptideRatioValueWrappers != null && !peptideRatioValueWrappers.isEmpty()) {
			for (final RatioValueWrapper peptideRatioValueWrapper : peptideRatioValueWrappers) {
				RatioBean peptideRatioValueBean = PeptideRatioBeanAdapter
						.getBeanByPeptideRatioValueID(peptideRatioValueWrapper.getId());
				if (peptideRatioValueBean == null) {
					peptideRatioValueBean = new RatioBean();
					final RatioDescriptor ratioDescriptor = PeptideRatioToPeptideTableMapper.getInstance()
							.getRatioDescriptor(peptideRatioValueWrapper.getRatioDescriptorID());
					peptideRatioValueBean.setDescription(ratioDescriptor.getDescription());
					peptideRatioValueBean.setCondition1(ConditionBeanAdapter
							.getBeanByConditionID(ratioDescriptor.getConditionByExperimentalCondition1Id().getId()));
					peptideRatioValueBean.setCondition2(ConditionBeanAdapter
							.getBeanByConditionID(ratioDescriptor.getConditionByExperimentalCondition2Id().getId()));
					peptideRatioValueBean.setDbID(peptideRatioValueWrapper.getId());
					final RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorBean();
					ratioDescriptorBean.setAggregationLevel(SharedAggregationLevel.PEPTIDE);
					ratioDescriptorBean.setCondition1Name(peptideRatioValueBean.getCondition1().getId());
					ratioDescriptorBean.setCondition2Name(peptideRatioValueBean.getCondition2().getId());
					ratioDescriptorBean.setRatioName(ratioDescriptor.getDescription());
					ratioDescriptorBean.setProteinScoreName(peptideRatioValueWrapper.getConfidenceScoreName());
					peptideRatioValueBean.setRatioDescriptorBean(ratioDescriptorBean);

					if (peptideRatioValueWrapper.getConfidenceScoreValue() != null) {
						peptideRatioValueBean.setAssociatedConfidenceScore(
								new ScoreBeanAdapter(peptideRatioValueWrapper.getConfidenceScoreValue().toString(),
										peptideRatioValueWrapper.getConfidenceScoreName(),
										peptideRatioValueWrapper.getConfidenceScoreType()).adapt());
					}
					peptideRatioValueBean.setValue(peptideRatioValueWrapper.getValue());
				}
				ret.addRatio(peptideRatioValueBean);
			}
		}
		// if (peptide.getPeptideRatioValues() != null) {
		// for (final Object obj : peptide.getPeptideRatioValues()) {
		// final PeptideRatioValue peptideRatioValue = (PeptideRatioValue) obj;
		// ret.addRatio(new PeptideRatioBeanAdapter(peptideRatioValue).adapt());
		// }
		// }

		// log.debug("Adapting psms for peptide DbID: " + peptide.getId());
		if (psmCentric) {
			for (final Object obj : peptide.getPsms()) {
				if (Thread.interrupted()) {
					throw new PintRuntimeException("Thread interrumpted while adapting peptideBean from peptideSet",
							PINT_ERROR_TYPE.THREAD_INTERRUPTED);
				}
				final Psm psm = (Psm) obj;

				if (!ret.getPSMDBIds().contains(psm.getId())) {
					ret.getPSMDBIds().add(psm.getId());

				}

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

			}
		}

		// log.debug("Adapting organisms for peptide DbID: " + peptide.getId());

		// conditions
		// first check in the mapper of conditions to peptides

		final TIntArrayList conditionIDs = ConditionToPeptideTableMapper.getInstance().mapIDs1(peptide);
		if (conditionIDs != null) {
			for (final int conditionID : conditionIDs.toArray()) {
				ExperimentalConditionBean conditionBean = ConditionBeanAdapter.getBeanByConditionID(conditionID);
				if (conditionBean == null) {
					if (peptide.getConditions() != null) {
						for (final Object obj : peptide.getConditions()) {
							final Condition condition = (Condition) obj;
							if (condition.getId() == conditionID) {
								conditionBean = new ConditionBeanAdapter(condition, true).adapt();
								break;
							}
						}
					}
				}
				if (conditionBean != null) {
					ret.addCondition(conditionBean);
					if (!ret.getNumPSMsByCondition().containsKey(conditionBean)) {
						ret.getNumPSMsByCondition().put(conditionBean, 0);
					}
					if (peptide.getNumPsms() != null) {
						ret.getNumPSMsByCondition().put(conditionBean,
								ret.getNumPSMsByCondition().get(conditionBean) + peptide.getNumPsms());
					}
					if (psmCentric) {
						if (ret.getPSMDBIdsByCondition().containsKey(conditionBean)) {
							final Set<Integer> set = new THashSet<Integer>();
							ret.getPSMDBIdsByCondition().put(conditionBean, set);
						}
						ret.getPSMDBIdsByCondition().get(conditionBean).addAll(ret.getPSMDBIds());
					}
				}
			}
		}

		// log.debug("Adapting msRun for peptide DbID: " + peptide.getId());
		// first look into the peptide to msrun mapper
		final TIntArrayList msRunIDs = MSRunToPeptideTableMapper.getInstance().mapIDs1(peptide);
		if (msRunIDs != null) {
			for (final int msRunID : msRunIDs.toArray()) {
				MSRunBean msRunBean = MSRunBeanAdapter.getBeanByMSRunID(msRunID);
				if (msRunBean == null) {
					final Set msRuns = peptide.getMsRuns();
					for (final Object object : msRuns) {
						final MsRun msRun = (MsRun) object;
						if (msRun.getId() == msRunID) {
							msRunBean = new MSRunBeanAdapter(msRun, false).adapt();
							ret.addMSRun(msRunBean);
						}
					}

				}

			}
		}

		log.debug("PSMs: " + ret.getNumPSMs());
		log.debug("PSMs by conditions: " + ret.getNumPSMsByCondition());

	}
}
