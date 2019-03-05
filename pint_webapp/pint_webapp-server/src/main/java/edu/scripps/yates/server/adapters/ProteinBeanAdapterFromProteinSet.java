package edu.scripps.yates.server.adapters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.Threshold;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.MSRunToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ProteinAmountToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ProteinRatioToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ThresholdToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.AmountValueWrapper;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.ProteinThresholdWrapper;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.RatioValueWrapper;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.server.cache.ServerCacheProteinBeansByProteinDBId;
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
import edu.scripps.yates.shared.model.ThresholdBean;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;

/**
 * Adapter for creating a single {@link ProteinBean} from a {@link Collection}
 * of {@link Protein}
 *
 * @author Salva
 *
 */
public class ProteinBeanAdapterFromProteinSet implements Adapter<ProteinBean> {
	private final static Logger log = Logger.getLogger(ProteinBeanAdapterFromProteinSet.class);
	// private final static Map<String, ProteinBean> map = new THashMap<String,
	// ProteinBean>();
	private final QueriableProteinSet queriableProtein;
	// private final String primaryAcc;
	private final Set<String> hiddenPTMs;
	private final boolean psmCentric;
	public static final java.util.Map<String, ProteinBean> map = new THashMap<String, ProteinBean>();

	public ProteinBeanAdapterFromProteinSet(Collection<Protein> proteins, Set<String> hiddenPTMs, boolean psmCentric) {
		this.psmCentric = psmCentric;
		// primaryAcc = primaryAcc;
		this.hiddenPTMs = hiddenPTMs;

		// for (Protein protein : proteins) {
		queriableProtein = QueriableProteinSet.getInstance(proteins, false);
		// }
		// removed. 19 April 2015. ProteinPSMLinks have to be created all at
		// once with all proteins in the dataset. Otherwise, the links will
		// be cleared each time createProteinPSMLink is called.
		// // create links
		// List<ProteinPSMLink> links = QueriesUtil
		// .createProteinPSMLinks(proteins);
		// for (ProteinPSMLink proteinPSMLink : links) {
		// queriableProteins.add(proteinPSMLink.getQueriableProtein());
		// }

	}

	public static void clearStaticMap() {
		map.clear();
	}

	public ProteinBeanAdapterFromProteinSet(QueriableProteinSet queriableProteins, Set<String> hiddenPTMs,
			boolean psmCentric) {
		queriableProtein = queriableProteins;
		this.hiddenPTMs = hiddenPTMs;
		this.psmCentric = psmCentric;
	}

	@Override
	public ProteinBean adapt() {
		final String primaryAccession = queriableProtein.getPrimaryAccession();
		if (map.containsKey(primaryAccession)) {
			return map.get(primaryAccession);
		}
		ProteinBean ret = null;
		// if (primaryAcc != null && map.containsKey(primaryAcc)) {
		// ret = map.get(primaryAcc);
		// } else {
		ret = new ProteinBean();
		map.put(primaryAccession, ret);
		// if (primaryAcc != null)
		// map.put(primaryAcc, ret);
		// }

		if (Thread.currentThread().isInterrupted()) {
			throw new PintRuntimeException(PINT_ERROR_TYPE.THREAD_INTERRUPTED);
		}

		addProteinInformationToProteinBean(ret, queriableProtein, hiddenPTMs);

		return ret;
	}

	/**
	 * Adds the protein information from {@link QueriableProteinSet} to a
	 * {@link ProteinBean}
	 *
	 * @param proteinBean
	 * @param queriableProtein
	 * @param hiddenPTMs
	 * @throws InterruptedException
	 */
	private void addProteinInformationToProteinBean(ProteinBean proteinBean, QueriableProteinSet queriableProtein,
			Set<String> hiddenPTMs) {
		for (final Integer dbId : queriableProtein.getProteinDBIds()._set) {
			ServerCacheProteinBeansByProteinDBId.getInstance().addtoCache(proteinBean, dbId);
		}
		proteinBean.addDbIds(queriableProtein.getProteinDBIds()._set);
		final ProteinAccession primaryProteinAccession = queriableProtein.getPrimaryProteinAccession();
		proteinBean.setPrimaryAccession(new AccessionBeanAdapter(primaryProteinAccession).adapt());

		// for (final ProteinAccession proteinAccession :
		// queriableProtein.getProteinAccessions()) {
		// if
		// (!proteinAccession.getAccession().equals(primaryProteinAccession.getAccession()))
		// {
		// proteinBean.addSecondaryAccession(new
		// AccessionBeanAdapter(proteinAccession).adapt());
		// }
		// }
		if (proteinBean.getPrimaryAccession() == null) {
			log.error("This protein has not primary accession ");
		}
		// log.debug("Adapting genes for protein DbID: " + protein.getId()
		// + " with linkToPSMs=" + linkToPSMs);
		final Set<Gene> genes = queriableProtein.getGenes();
		if (genes != null && !genes.isEmpty()) {
			for (final Gene gene : genes) {
				proteinBean.addGene(new GeneBeanAdapter(gene).adapt());
			}
		} else {

		}
		if (queriableProtein.getLength() != null) {
			proteinBean.setLength(queriableProtein.getLength());
		}
		if (queriableProtein.getMw() != null) {
			proteinBean.setMw(queriableProtein.getMw());
		}
		if (queriableProtein.getPi() != null)
			proteinBean.setPi(queriableProtein.getPi());

		// log.debug("Adapting msRun for protein DbID: " + protein.getId());
		// first look into the protein to msrun mapper
		for (final Protein protein : queriableProtein.getAllProteins()) {
			final TIntArrayList msRunIDs = MSRunToProteinTableMapper.getInstance().mapIDs1(protein);
			if (msRunIDs != null) {
				for (final int msRunID : msRunIDs.toArray()) {
					MSRunBean msRunBean = MSRunBeanAdapter.getBeanByMSRunID(msRunID);
					if (msRunBean == null) {
						final Set msRuns = protein.getMsRuns();
						for (final Object object : msRuns) {
							final MsRun msRun = (MsRun) object;
							if (msRun.getId() == msRunID) {
								msRunBean = new MSRunBeanAdapter(msRun, false).adapt();
								proteinBean.addMsrun(msRunBean);
							}
						}

					}
				}
			}
		}

		// log.debug("Adapting amounts for protein DbID: " +
		// protein.getId());
		for (final Protein protein : queriableProtein.getIndividualProteins()) {
			final List<AmountValueWrapper> proteinAmountValueWrappers = ProteinAmountToProteinTableMapper.getInstance()
					.mapProtein(protein);
			if (proteinAmountValueWrappers != null && !proteinAmountValueWrappers.isEmpty()) {
				for (final AmountValueWrapper proteinAmountValueWrapper : proteinAmountValueWrappers) {
					AmountBean proteinAmountValueBean = AmountBeanAdapter
							.getBeanByProteinAmountValueID(proteinAmountValueWrapper.getId());
					if (proteinAmountValueBean == null) {
						proteinAmountValueBean = new AmountBean();
						proteinAmountValueBean.setExperimentalCondition(
								ConditionBeanAdapter.getBeanByConditionID(proteinAmountValueWrapper.getConditionID()));
						if (proteinAmountValueWrapper.getAmountType() != null) {
							proteinAmountValueBean.setAmountType(edu.scripps.yates.shared.model.AmountType
									.fromValue(proteinAmountValueWrapper.getAmountType().getName()));
						}
						proteinAmountValueBean.setValue(proteinAmountValueWrapper.getValue());
						proteinAmountValueBean.setManualSPC(proteinAmountValueWrapper.getManualSPC());
						proteinAmountValueBean.setComposed(proteinAmountValueWrapper.getCombinationType() != null);
						for (final Object object : protein.getMsRuns()) {
							final MsRun msRun = (MsRun) object;
							proteinAmountValueBean.addMsRun(new MSRunBeanAdapter(msRun, false).adapt());
						}
					}
					proteinBean.addAmount(proteinAmountValueBean);
				}
			}
			// if (protein.getProteinAmounts() != null) {
			// for (final Object obj : protein.getProteinAmounts()) {
			// final ProteinAmount proteinAmount = (ProteinAmount) obj;
			// final AmountBean amount = new AmountBeanAdapter(proteinAmount,
			// protein.getMsRun()).adapt();
			//
			// proteinBean.addAmount(amount);
			// }
			// }
		}
		// log.debug("Adapting ratios for protein DbID: " +
		// protein.getId());
		// if (queriableProtein.getProteinRatioValues() != null) {
		// for (final Object obj : queriableProtein.getProteinRatioValues()) {
		// final ProteinRatioValue proteinRatioValue = (ProteinRatioValue) obj;
		// proteinBean.addProteinRatio(new
		// ProteinRatioBeanAdapter(proteinRatioValue).adapt());
		// }
		// }
		for (final Protein protein : queriableProtein.getAllProteins()) {
			final List<RatioValueWrapper> proteinRatioValueWrappers = ProteinRatioToProteinTableMapper.getInstance()
					.mapProtein(protein);
			if (proteinRatioValueWrappers != null && !proteinRatioValueWrappers.isEmpty()) {
				for (final RatioValueWrapper proteinRatioValueWrapper : proteinRatioValueWrappers) {
					RatioBean proteinRatioValueBean = ProteinRatioBeanAdapter
							.getBeanByProteinRatioValueID(proteinRatioValueWrapper.getId());
					if (proteinRatioValueBean == null) {
						proteinRatioValueBean = new RatioBean();
						final RatioDescriptor ratioDescriptor = ProteinRatioToProteinTableMapper.getInstance()
								.getRatioDescriptor(proteinRatioValueWrapper.getRatioDescriptorID());
						proteinRatioValueBean.setDescription(ratioDescriptor.getDescription());
						proteinRatioValueBean.setCondition1(ConditionBeanAdapter.getBeanByConditionID(
								ratioDescriptor.getConditionByExperimentalCondition1Id().getId()));
						proteinRatioValueBean.setCondition2(ConditionBeanAdapter.getBeanByConditionID(
								ratioDescriptor.getConditionByExperimentalCondition2Id().getId()));
						proteinRatioValueBean.setDbID(proteinRatioValueWrapper.getId());
						final RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorBean();
						ratioDescriptorBean.setAggregationLevel(SharedAggregationLevel.PROTEIN);
						ratioDescriptorBean.setCondition1Name(proteinRatioValueBean.getCondition1().getId());
						ratioDescriptorBean.setCondition2Name(proteinRatioValueBean.getCondition2().getId());
						ratioDescriptorBean.setRatioName(ratioDescriptor.getDescription());
						ratioDescriptorBean.setProteinScoreName(proteinRatioValueWrapper.getConfidenceScoreName());
						proteinRatioValueBean.setRatioDescriptorBean(ratioDescriptorBean);

						if (proteinRatioValueWrapper.getConfidenceScoreValue() != null) {
							proteinRatioValueBean.setAssociatedConfidenceScore(
									new ScoreBeanAdapter(proteinRatioValueWrapper.getConfidenceScoreValue().toString(),
											proteinRatioValueWrapper.getConfidenceScoreName(),
											proteinRatioValueWrapper.getConfidenceScoreType()).adapt());
						}
						proteinRatioValueBean.setValue(proteinRatioValueWrapper.getValue());
					}
					proteinBean.addProteinRatio(proteinRatioValueBean);
				}
			}
		}
		// final Iterator<ProteinRatioValue> proteinRatioValuesIterator =
		// queriableProtein.getProteinRatioValuesIterator();
		// while (proteinRatioValuesIterator.hasNext()) {
		// final ProteinRatioValue proteinRatioValue =
		// proteinRatioValuesIterator.next();
		// proteinBean.addProteinRatio(new
		// ProteinRatioBeanAdapter(proteinRatioValue).adapt());
		// }

		// if (queriableProtein.getProteinScores() != null) {
		// for (final Object obj : queriableProtein.getProteinScores()) {
		// final ProteinScore proteinScore = (ProteinScore) obj;
		// proteinBean.addScore(new
		// ScoreBeanAdapter(String.valueOf(proteinScore.getValue()),
		// proteinScore.getName(),
		// proteinScore.getConfidenceScoreType()).adapt());
		// }
		// }
		final Iterator<ProteinScore> proteinScoresIterator = queriableProtein.getProteinScoresIterator();
		while (proteinScoresIterator.hasNext()) {
			final ProteinScore proteinScore = proteinScoresIterator.next();
			proteinBean.addScore(new ScoreBeanAdapter(String.valueOf(proteinScore.getValue()), proteinScore.getName(),
					proteinScore.getConfidenceScoreType()).adapt());
		}
		// log.debug("Adapting annorations for protein DbID: " +
		// protein.getId());
		// disabled on 14th Ago 2018 to improve performance.
		// annotations will come only from Uniprot
		// if (queriableProtein.getProteinAnnotations() != null) {
		// for (final Object obj : queriableProtein.getProteinAnnotations()) {
		// final ProteinAnnotation proteinAnnotation = (ProteinAnnotation) obj;
		// proteinBean.addAnnotation(new
		// ProteinAnnotationBeanAdapter(proteinAnnotation).adapt());
		// }
		// }

		// thresholds
		// first check in the mapper of thresholds to proteins
		for (final Protein protein : queriableProtein.getAllProteins()) {
			final List<ProteinThresholdWrapper> thresholdWrappers = ThresholdToProteinTableMapper.getInstance()
					.mapProtein(protein);
			if (thresholdWrappers != null && !thresholdWrappers.isEmpty()) {
				for (final ProteinThresholdWrapper proteinThresholdWrapper : thresholdWrappers) {
					ThresholdBean thresholdBean = ThresholdBeanAdapter
							.getBeanByConditionID(proteinThresholdWrapper.getId());
					if (thresholdBean == null) {
						thresholdBean = new ThresholdBean();
						final Threshold threshold = ThresholdToProteinTableMapper.getInstance()
								.getThreshold(proteinThresholdWrapper);
						thresholdBean.setPass(proteinThresholdWrapper.isPass());
						thresholdBean.setDescription(threshold.getDescription());
						thresholdBean.setName(threshold.getName());
					}
					proteinBean.addThreshold(thresholdBean);
				}
			}
		}
		// log.debug("Adapting organisms for protein DbID: " +
		// protein.getId());
		if (queriableProtein.getOrganism() != null) {
			proteinBean.setOrganism(new OrganismBeanAdapter(queriableProtein.getOrganism()).adapt());
		}

		// conditions
		// first check in the mapper of conditions to proteins
		for (final Protein protein : queriableProtein.getAllProteins()) {
			final TIntArrayList conditionIDs = ConditionToProteinTableMapper.getInstance().mapIDs1(protein);
			if (conditionIDs != null) {
				for (final int conditionID : conditionIDs.toArray()) {
					ExperimentalConditionBean conditionBean = ConditionBeanAdapter.getBeanByConditionID(conditionID);
					if (conditionBean == null) {
						for (final Object obj : protein.getConditions()) {
							final Condition condition = (Condition) obj;
							if (condition.getId() == conditionID) {
								conditionBean = new ConditionBeanAdapter(condition, true).adapt();
								break;
							}
						}
					}
					if (conditionBean != null) {
						proteinBean.addCondition(conditionBean);
					}
				}
			}
		}

		//
		// end of iteration over the Proteins o the QueriableProtein
		//

		// log.debug("Adapting psms for protein DbID: " + protein.getId());
		if (queriableProtein.getLinksToPSMs() != null) {
			for (final LinkBetweenQueriableProteinSetAndPSM link : queriableProtein.getLinksToPSMs()) {
				if (Thread.interrupted()) {
					throw new PintRuntimeException("Thread interrumpted while adapting proteinBean from proteinSet",
							PINT_ERROR_TYPE.THREAD_INTERRUPTED);
				}
				final Psm psm = link.getQueriablePsm().getPsm();

				if (!proteinBean.getPSMDBIds().contains(psm.getId())) {

					final String sequence = psm.getFullSequence();
					proteinBean.addDifferentSequence(sequence);

					final PSMBean psmBean = new PSMBeanAdapter(link.getQueriablePsm(), hiddenPTMs).adapt();
					proteinBean.addPSMtoProtein(psmBean);

					final Set<Condition> conditions = psm.getConditions();
					for (final Condition condition : conditions) {
						final ExperimentalConditionBean conditionBean = new ConditionBeanAdapter(condition, true)
								.adapt();

						// psms by condition ID
						if (proteinBean.getPSMDBIdsByCondition().containsKey(conditionBean)) {
							proteinBean.getPSMDBIdsByCondition().get(conditionBean).add(psm.getId());
						} else {
							final Set<Integer> set = new HashSet<Integer>();
							set.add(psm.getId());
							proteinBean.getPSMDBIdsByCondition().put(conditionBean, set);
						}
					}
				}
			}
			log.debug("PSMs: " + proteinBean.getPSMDBIds().size());
			log.debug("PSMs by conditions: " + proteinBean.getPSMDBIdsByCondition().size());
		}
		// TODO
		// I think this has to be disabled because the peptidebeans are created
		// after creating the protein beans, from the Dataset.getPeptides()
		if (queriableProtein.getLinksToPeptides() != null) {
			for (final LinkBetweenQueriableProteinSetAndPeptideSet link : queriableProtein.getLinksToPeptides()) {
				if (Thread.interrupted()) {
					throw new PintRuntimeException("Thread interrupted while adapting proteinBean from proteinSet",
							PINT_ERROR_TYPE.THREAD_INTERRUPTED);
				}
				final PeptideBean peptideBean = new PeptideBeanAdapterFromPeptideSet(link.getQueriablePeptide(),
						hiddenPTMs, psmCentric).adapt();
				proteinBean.addPeptideToProtein(peptideBean);
				proteinBean.getDifferentSequences().add(link.getQueriablePeptide().getFullSequence());
				if (psmCentric) {
					final List<Peptide> peptides = link.getQueriablePeptide().getIndividualPeptides();
					for (final Peptide peptide : peptides) {
						if (!proteinBean.getPeptideDBIds().contains(peptide.getId())) {
							final Set<Condition> conditions = peptide.getConditions();
							for (final Condition condition : conditions) {
								final ExperimentalConditionBean conditionBean = new ConditionBeanAdapter(condition,
										true).adapt();

								// psms by condition ID
								if (proteinBean.getPSMDBIdsByCondition().containsKey(conditionBean)) {
									proteinBean.getPSMDBIdsByCondition().get(conditionBean).add(peptide.getId());
								} else {
									final Set<Integer> set = new HashSet<Integer>();
									set.add(peptide.getId());
									proteinBean.getPSMDBIdsByCondition().put(conditionBean, set);
								}
							}
						}
					}
				}
			}
			log.debug("Peptides: " + proteinBean.getPeptideDBIds().size());
			log.debug("Peptides by conditions: " + proteinBean.getPeptideDBIdsByCondition().size());
		}

	}

	// log.debug("Adapting is finished for protein DbID: " +
	// protein.getId());
}
