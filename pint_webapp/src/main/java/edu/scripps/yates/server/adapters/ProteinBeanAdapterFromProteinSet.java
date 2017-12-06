package edu.scripps.yates.server.adapters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.server.cache.ServerCacheProteinBeansByProteinDBId;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.exceptions.PintRuntimeException;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.ProteinBean;
import gnu.trove.set.hash.THashSet;

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
	private final Set<QueriableProteinSet> queriableProteins;
	// private final String primaryAcc;
	private final Collection<String> hiddenPTMs;

	public ProteinBeanAdapterFromProteinSet(Collection<Protein> proteins, Collection<String> hiddenPTMs) {

		// primaryAcc = primaryAcc;
		this.hiddenPTMs = hiddenPTMs;
		queriableProteins = new THashSet<QueriableProteinSet>();

		if (proteins != null) {
			// for (Protein protein : proteins) {
			queriableProteins.add(QueriableProteinSet.getInstance(proteins, false));
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
	}

	public ProteinBeanAdapterFromProteinSet(Set<QueriableProteinSet> queriableProteins, Collection<String> hiddenPTMs) {
		this.queriableProteins = queriableProteins;
		this.hiddenPTMs = hiddenPTMs;
	}

	@Override
	public ProteinBean adapt() {
		ProteinBean ret = null;
		// if (primaryAcc != null && map.containsKey(primaryAcc)) {
		// ret = map.get(primaryAcc);
		// } else {
		ret = new ProteinBean();

		// if (primaryAcc != null)
		// map.put(primaryAcc, ret);
		// }
		for (QueriableProteinSet queriableProtein : queriableProteins) {
			if (Thread.currentThread().isInterrupted()) {
				throw new PintRuntimeException(PINT_ERROR_TYPE.THREAD_INTERRUPTED);
			}

			addProteinInformationToProteinBean(ret, queriableProtein, hiddenPTMs);

		}
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
			Collection<String> hiddenPTMs) {
		for (Integer dbId : queriableProtein.getProteinDBIds()._set) {
			ServerCacheProteinBeansByProteinDBId.getInstance().addtoCache(proteinBean, dbId);
		}
		proteinBean.addDbIds(queriableProtein.getProteinDBIds()._set);
		ProteinAccession primaryProteinAccession = queriableProtein.getPrimaryProteinAccession();
		proteinBean.setPrimaryAccession(new AccessionBeanAdapter(primaryProteinAccession).adapt());
		for (ProteinAccession proteinAccession : queriableProtein.getProteinAccessions()) {
			if (!proteinAccession.getAccession().equals(primaryProteinAccession.getAccession())) {
				proteinBean.addSecondaryAccession(new AccessionBeanAdapter(proteinAccession).adapt());
			}
		}
		if (proteinBean.getPrimaryAccession() == null) {
			log.error("This protein has not primary accession ");
		}
		// log.debug("Adapting genes for protein DbID: " + protein.getId()
		// + " with linkToPSMs=" + linkToPSMs);
		if (queriableProtein.getGenes() != null) {
			for (Object obj : queriableProtein.getGenes()) {
				Gene gene = (Gene) obj;
				proteinBean.addGene(new GeneBeanAdapter(gene).adapt());
			}
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
		for (MsRun msRun : queriableProtein.getMsRuns()) {
			proteinBean.addMsrun(new MSRunBeanAdapter(msRun).adapt());
		}
		// log.debug("Adapting amounts for protein DbID: " +
		// protein.getId());
		for (Protein protein : queriableProtein.getIndividualProteins()) {
			if (protein.getProteinAmounts() != null) {
				for (Object obj : protein.getProteinAmounts()) {
					ProteinAmount proteinAmount = (ProteinAmount) obj;
					final AmountBean amount = new AmountBeanAdapter(proteinAmount, protein.getMsRun()).adapt();

					proteinBean.addAmount(amount);
				}
			}
		}
		// log.debug("Adapting ratios for protein DbID: " +
		// protein.getId());
		if (queriableProtein.getProteinRatioValues() != null) {
			for (Object obj : queriableProtein.getProteinRatioValues()) {
				ProteinRatioValue proteinRatioValue = (ProteinRatioValue) obj;
				proteinBean.addProteinRatio(new ProteinRatioBeanAdapter(proteinRatioValue).adapt());
			}
		}

		if (queriableProtein.getProteinScores() != null) {
			for (Object obj : queriableProtein.getProteinScores()) {
				ProteinScore proteinScore = (ProteinScore) obj;
				proteinBean.addScore(new ScoreBeanAdapter(String.valueOf(proteinScore.getValue()),
						proteinScore.getName(), proteinScore.getConfidenceScoreType()).adapt());
			}
		}
		// log.debug("Adapting annorations for protein DbID: " +
		// protein.getId());
		if (queriableProtein.getProteinAnnotations() != null) {
			for (Object obj : queriableProtein.getProteinAnnotations()) {
				ProteinAnnotation proteinAnnotation = (ProteinAnnotation) obj;
				proteinBean.addAnnotation(new ProteinAnnotationBeanAdapter(proteinAnnotation).adapt());
			}
		}

		// thresholds
		if (queriableProtein.getProteinThresholds() != null) {
			for (Object obj : queriableProtein.getProteinThresholds()) {
				ProteinThreshold appliedThreshold = (ProteinThreshold) obj;
				proteinBean.addThreshold(new ThresholdBeanAdapter(appliedThreshold).adapt());
			}
		}
		// log.debug("Adapting roganisms for protein DbID: " +
		// protein.getId());
		if (queriableProtein.getOrganism() != null) {
			proteinBean.setOrganism(new OrganismBeanAdapter(queriableProtein.getOrganism()).adapt());
		}

		// conditions
		if (queriableProtein.getConditions() != null) {
			for (Object obj : queriableProtein.getConditions()) {
				Condition condition = (Condition) obj;
				proteinBean.addCondition(new ConditionBeanAdapter(condition).adapt());
			}
		}

		//
		// end of iteration over the Proteins o the QueriableProtein
		//

		// log.debug("Adapting psms for protein DbID: " + protein.getId());
		if (queriableProtein.getLinks() != null) {
			for (LinkBetweenQueriableProteinSetAndPSM link : queriableProtein.getLinks()) {
				if (Thread.interrupted()) {
					throw new PintRuntimeException("Thread interrumpted while adapting proteinBean from proteinSet",
							PINT_ERROR_TYPE.THREAD_INTERRUPTED);
				}
				final Psm psm = link.getQueriablePsm().getPsm();

				if (!proteinBean.getPSMDBIds().contains(psm.getId())) {

					String sequence = psm.getSequence();
					proteinBean.getDifferentSequences().add(sequence);

					final PSMBean psmBean = new PSMBeanAdapter(link.getQueriablePsm(), hiddenPTMs).adapt();
					proteinBean.addPSMtoProtein(psmBean);

					final Set<Condition> conditions = psm.getConditions();
					for (Condition condition : conditions) {
						final ExperimentalConditionBean conditionBean = new ConditionBeanAdapter(condition).adapt();

						// psms by condition ID
						if (proteinBean.getPSMDBIdsByCondition().containsKey(conditionBean)) {
							proteinBean.getPSMDBIdsByCondition().get(conditionBean).add(psm.getId());
						} else {
							Set<Integer> set = new HashSet<Integer>();
							set.add(psm.getId());
							proteinBean.getPSMDBIdsByCondition().put(conditionBean, set);
						}
					}
					// psm by run id
					final MSRunBean msRunBean = new MSRunBeanAdapter(psm.getMsRun()).adapt();
					if (proteinBean.getPSMDBIdsbyMSRun().containsKey(msRunBean)) {
						proteinBean.getPSMDBIdsbyMSRun().get(msRunBean).add(psm.getId());
					} else {
						Set<Integer> set = new HashSet<Integer>();
						set.add(psm.getId());
						proteinBean.getPSMDBIdsbyMSRun().put(msRunBean, set);
					}
				}
			}
		}
		log.debug("PSMs: " + proteinBean.getPSMDBIds().size());
		log.debug("PSMs by conditions: " + proteinBean.getPSMDBIdsByCondition().size());
		log.debug("PSMs by msruns: " + proteinBean.getPSMDBIdsbyMSRun().size());

		boolean hasManualSPCs = false;
		for (AmountBean amountBean : proteinBean.getAmounts()) {
			// not do this in SPC created from excel
			// files, which may be recalculated somehow
			if (amountBean.getManualSPC() != null && amountBean.getManualSPC()) {
				log.debug("Protein having manual SPC. Not recalculating it");
				hasManualSPCs = true;
				break;
			}
		}
		if (!hasManualSPCs) {
			log.debug("Protein doesnt have manual SPC. Checking if it is necessary to add or change SPC");
			for (ExperimentalConditionBean condition : proteinBean.getPSMDBIdsByCondition().keySet()) {
				Set<Integer> psmsByCond = proteinBean.getPSMDBIdsByCondition().get(condition);
				for (MSRunBean msRunBean : proteinBean.getPSMDBIdsbyMSRun().keySet()) {
					final Set<Integer> psmsByRun = proteinBean.getPSMDBIdsbyMSRun().get(msRunBean);
					// num in common
					int numCommon = 0;
					for (Integer integer : psmsByRun) {
						if (psmsByCond.contains(integer))
							numCommon++;
					}
					log.debug("SPC expected for MSRun:" + msRunBean.getRunID() + " and condition: " + condition.getId()
							+ " =" + numCommon);
					boolean amountfound = false;
					if (proteinBean.getAmounts() != null && !proteinBean.getAmounts().isEmpty()) {
						final Iterator<AmountBean> iterator = proteinBean.getAmounts().iterator();
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
											log.debug("Adjusting SPC for protein "
													+ proteinBean.getPrimaryAccession().getAccession() + " from "
													+ amountBean.getValue() + " to " + numCommon);
											amountBean.setValue(numCommon);
										}
									} else {
										log.debug("Removing SPC for protein "
												+ proteinBean.getPrimaryAccession().getAccession() + " from "
												+ amountBean.getValue() + " to " + numCommon);
										iterator.remove();
									}

								}
							}
						}
					}
					if (!amountfound && numCommon > 0) {
						log.debug("Adding SPC for protein " + proteinBean.getPrimaryAccession().getAccession()
								+ " value= " + numCommon);
						AmountBean amount = new AmountBean();
						amount.setAmountType(edu.scripps.yates.shared.model.AmountType.SPC);
						amount.setExperimentalCondition(condition);
						amount.setMsRun(msRunBean);
						amount.setValue(numCommon);
						proteinBean.addAmount(amount);
					}

				}

			}
		}

		// log.debug("Adapting is finished for protein DbID: " +
		// protein.getId());
	}

}
