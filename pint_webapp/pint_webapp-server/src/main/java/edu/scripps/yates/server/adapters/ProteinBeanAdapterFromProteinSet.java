package edu.scripps.yates.server.adapters;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ProteinAmountToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ProteinRatioToProteinTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToMSRunIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToProteinThresholdIDTableMapper;
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
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

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
	private boolean includePeptides;
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
			boolean psmCentric, boolean includePeptides) {
		queriableProtein = queriableProteins;
		this.hiddenPTMs = hiddenPTMs;
		this.psmCentric = psmCentric;
		this.includePeptides = includePeptides;
	}

	@Override
	public ProteinBean adapt() {
		final String primaryAccession = queriableProtein.getPrimaryAccession();
		ProteinBean ret = null;
		if (map.containsKey(primaryAccession)) {
			ret = map.get(primaryAccession);

		} else {

			// if (primaryAcc != null && map.containsKey(primaryAcc)) {
			// ret = map.get(primaryAcc);
			// } else {
			ret = new ProteinBean();
			map.put(primaryAccession, ret);
		}
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
		// keep the ones already added in a set, so that we ignore them in the loops of
		// this method
		final TIntSet individualProteinsAlreadyAdded = new TIntHashSet();
		boolean thereAreNewProteins = false;
		for (final Protein protein : queriableProtein.getAllProteins()) {
			if (proteinBean.getDbIds().contains(protein.getId())) {
				individualProteinsAlreadyAdded.add(protein.getId());
			} else {
				thereAreNewProteins = true;
			}
		}
		if (!thereAreNewProteins) {
			return;
		}
		for (final int dbId : queriableProtein.getProteinDBIds().toArray()) {
			ServerCacheProteinBeansByProteinDBId.getInstance().addtoCache(proteinBean, dbId);
		}
		proteinBean.addDbIds(queriableProtein.getProteinDBIds().toArray());
		final ProteinAccession primaryProteinAccession = queriableProtein.getPrimaryProteinAccession();
		proteinBean.setPrimaryAccession(new AccessionBeanAdapter(primaryProteinAccession).adapt());

		final String acc = proteinBean.getPrimaryAccession().getAccession();
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
		final List<Gene> genes = queriableProtein.getGenes();
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
			if (individualProteinsAlreadyAdded.contains(protein.getId())) {
				continue;
			}
			final Set<MSRunBean> msrunsForThatProtein = new THashSet<MSRunBean>();
//			final TIntArrayList msRunIDs = MSRunToProteinTableMapper.getInstance().mapIDs1(protein);
			final TIntSet msRunIDs = ProteinIDToMSRunIDTableMapper.getInstance()
					.getMSRunIDsFromProteinID(protein.getId());
			if (msRunIDs != null) {
				final TIntSet msRunIDsMissing = new TIntHashSet();
				for (final int msRunID : msRunIDs.toArray()) {
					final MSRunBean msRunBean = MSRunBeanAdapter.getBeanByMSRunID(msRunID);
					if (msRunBean == null) {
						msRunIDsMissing.add(msRunID);

					} else {

						proteinBean.addMsrun(msRunBean);
						msrunsForThatProtein.add(msRunBean);
					}
				}
				if (!msRunIDsMissing.isEmpty()) {
					final List<MsRun> msRuns = (List<MsRun>) PreparedCriteria.getBatchLoadByIDs(MsRun.class,
							msRunIDsMissing, true, 250);
					for (final MsRun msRun : msRuns) {
						final MSRunBean msRunBean = new MSRunBeanAdapter(msRun, false).adapt();
						proteinBean.addMsrun(msRunBean);
						msrunsForThatProtein.add(msRunBean);
					}
				}

			}

			final List<ProteinAmount> proteinAmountValues = ProteinAmountToProteinTableMapper.getInstance()
					.mapProtein(protein);
			if (proteinAmountValues != null && !proteinAmountValues.isEmpty()) {
				for (final ProteinAmount proteinAmountValue : proteinAmountValues) {
					AmountBean proteinAmountValueBean = AmountBeanAdapter
							.getBeanByProteinAmountValueID(proteinAmountValue.getId());
					if (proteinAmountValueBean == null) {
						proteinAmountValueBean = new AmountBean();
						proteinAmountValueBean.setExperimentalCondition(
								ConditionBeanAdapter.getBeanByConditionID(proteinAmountValue.getCondition().getId()));
						if (proteinAmountValue.getAmountType() != null) {
							proteinAmountValueBean.setAmountType(edu.scripps.yates.shared.model.AmountType
									.fromValue(proteinAmountValue.getAmountType().getName()));
						}
						proteinAmountValueBean.setValue(proteinAmountValue.getValue());
						proteinAmountValueBean.setManualSPC(proteinAmountValue.getManualSPC());
						proteinAmountValueBean.setComposed(proteinAmountValue.getCombinationType() != null);
						for (final MSRunBean msRunBean : msrunsForThatProtein) {
							proteinAmountValueBean.addMsRun(msRunBean);
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
			if (individualProteinsAlreadyAdded.contains(protein.getId())) {
				continue;
			}
			final List<ProteinRatioValue> proteinRatioValueWrappers = ProteinRatioToProteinTableMapper.getInstance()
					.mapProtein(protein);
			if (proteinRatioValueWrappers != null && !proteinRatioValueWrappers.isEmpty()) {
				for (final ProteinRatioValue proteinRatioValue : proteinRatioValueWrappers) {
					RatioBean proteinRatioValueBean = ProteinRatioBeanAdapter
							.getBeanByProteinRatioValueID(proteinRatioValue.getId());
					if (proteinRatioValueBean == null) {
						proteinRatioValueBean = new RatioBean();

//						final RatioDescriptor ratioDescriptor = ProteinRatioToProteinTableMapper.getInstance()
//								.getRatioDescriptor(proteinRatioValueWrapper.getRatioDescriptorID());
						// TODO make sure that this doesnt make a query:
						final RatioDescriptor ratioDescriptor = proteinRatioValue.getRatioDescriptor();
						proteinRatioValueBean.setDescription(ratioDescriptor.getDescription());

						proteinRatioValueBean.setCondition1(ConditionBeanAdapter.getBeanByConditionID(
								ratioDescriptor.getConditionByExperimentalCondition1Id().getId()));
						proteinRatioValueBean.setCondition2(ConditionBeanAdapter.getBeanByConditionID(
								ratioDescriptor.getConditionByExperimentalCondition2Id().getId()));
						proteinRatioValueBean.setDbID(proteinRatioValue.getId());
						final RatioDescriptorBean ratioDescriptorBean = new RatioDescriptorBean();
						ratioDescriptorBean.setAggregationLevel(SharedAggregationLevel.PROTEIN);
						ratioDescriptorBean.setCondition1Name(proteinRatioValueBean.getCondition1().getId());
						ratioDescriptorBean.setCondition2Name(proteinRatioValueBean.getCondition2().getId());
						ratioDescriptorBean.setRatioName(ratioDescriptor.getDescription());
						ratioDescriptorBean.setProteinScoreName(proteinRatioValue.getConfidenceScoreName());
						ratioDescriptorBean.setRatioDescriptorID(ratioDescriptor.getId());
						proteinRatioValueBean.setRatioDescriptorBean(ratioDescriptorBean);

						if (proteinRatioValue.getConfidenceScoreValue() != null) {
							proteinRatioValueBean.setAssociatedConfidenceScore(
									new ScoreBeanAdapter(proteinRatioValue.getConfidenceScoreValue().toString(),
											proteinRatioValue.getConfidenceScoreName(),
											proteinRatioValue.getConfidenceScoreType()).adapt());
						}
						proteinRatioValueBean.setValue(proteinRatioValue.getValue());
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
		final List<ProteinScore> proteinScoresIterator = queriableProtein
				.getProteinScores(individualProteinsAlreadyAdded);
		for (final ProteinScore proteinScore : proteinScoresIterator) {
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
			if (individualProteinsAlreadyAdded.contains(protein.getId())) {
				continue;
			}
			final TIntSet proteinThresholdIDs = ProteinIDToProteinThresholdIDTableMapper.getInstance()
					.getProteinThresholdIDsFromProteinID(protein.getId());
			if (proteinThresholdIDs != null && !proteinThresholdIDs.isEmpty()) {
				final List<ProteinThreshold> proteinThresholds = (List<ProteinThreshold>) PreparedCriteria
						.getBatchLoadByIDs(ProteinThreshold.class, proteinThresholdIDs, true, 100);
				if (proteinThresholds != null && !proteinThresholds.isEmpty()) {
					for (final ProteinThreshold proteinThreshold : proteinThresholds) {
						ThresholdBean thresholdBean = ThresholdBeanAdapter
								.getBeanByConditionID(proteinThreshold.getId());
						if (thresholdBean == null) {
							thresholdBean = new ThresholdBean();
							thresholdBean.setPass(proteinThreshold.isPassThreshold());
							thresholdBean.setDescription(proteinThreshold.getDescription());
							thresholdBean.setName(proteinThreshold.getName());
						}
						proteinBean.addThreshold(thresholdBean);
					}
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
			if (individualProteinsAlreadyAdded.contains(protein.getId())) {
				continue;
			}
			final TIntArrayList conditionIDs = ConditionToProteinTableMapper.getInstance().mapIDs1(protein);
			if (conditionIDs != null) {
				for (final int conditionID : conditionIDs.toArray()) {
					ExperimentalConditionBean conditionBean = ConditionBeanAdapter.getBeanByConditionID(conditionID);
					if (conditionBean == null) {
						for (final Object obj : protein.getConditions()) {
							final Condition condition = (Condition) obj;
							if (condition.getId() == conditionID) {
								conditionBean = new ConditionBeanAdapter(condition, true, includePeptides).adapt();
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
						final ExperimentalConditionBean conditionBean = new ConditionBeanAdapter(condition, true,
								includePeptides).adapt();

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
				if (link.getQueriablePeptideSet() != null) {
					proteinBean.getDifferentSequences().add(link.getQueriablePeptideSet().getFullSequence());
				}
				final List<LinkBetweenQueriableProteinSetAndPeptideSet> linkToPeptides = link.getQueriableProtein()
						.getLinksToPeptides();
				int numPSMs = 0;
				for (final LinkBetweenQueriableProteinSetAndPeptideSet link2 : linkToPeptides) {
					numPSMs += link2.getQueriablePeptideSet().getNumPSMs();
				}
				proteinBean.setNumPSMs(numPSMs);
				if (!includePeptides) {
					// add peptideDBIds to proteinBean
					if (link.getQueriablePeptideSet() != null) {
						link.getQueriablePeptideSet().getIndividualPeptides().stream()
								.forEach(peptide -> proteinBean.addPeptideDBId(peptide.getId()));
					}
					continue;
				}
				final PeptideBean peptideBean = new PeptideBeanAdapterFromPeptideSet(link.getQueriablePeptideSet(),
						hiddenPTMs, psmCentric).adapt();
				proteinBean.addPeptideToProtein(peptideBean);

				if (psmCentric) {
					final List<Peptide> peptides = link.getQueriablePeptideSet().getIndividualPeptides();
					for (final Peptide peptide : peptides) {
						if (!proteinBean.getPeptideDBIds().contains(peptide.getId())) {
							final Set<Condition> conditions = peptide.getConditions();
							for (final Condition condition : conditions) {
								final ExperimentalConditionBean conditionBean = new ConditionBeanAdapter(condition,
										true, includePeptides).adapt();

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
