package edu.scripps.yates.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPeptideIDTableMapper;
import edu.scripps.yates.server.adapters.PeptideBeanAdapterFromPeptideSet;
import edu.scripps.yates.server.adapters.ProteinBeanAdapterFromProteinSet;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.RatioAnalyzer;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class DataSet {
	private final List<ProteinGroupBean> proteinGroups = new ArrayList<ProteinGroupBean>();
	private final List<ProteinBean> proteins = new ArrayList<ProteinBean>();
	private final Set<String> proteinScores = new THashSet<String>();
	private final List<PeptideBean> peptides = new ArrayList<PeptideBean>();
	private final Set<String> peptideScores = new THashSet<String>();
	private final List<PSMBean> psms = new ArrayList<PSMBean>();
	private final Set<String> psmScores = new THashSet<String>();
	private final Set<String> ptmScores = new THashSet<String>();
	private final Set<String> scoreTypes = new THashSet<String>();

	private final TIntObjectHashMap<ProteinBean> proteinsByProteinBeanUniqueIdentifier = new TIntObjectHashMap<ProteinBean>();
	private final Map<String, ProteinBean> proteinsByAccession = new THashMap<String, ProteinBean>();
	private final TIntObjectHashMap<PSMBean> psmsByPSMDBId = new TIntObjectHashMap<PSMBean>();
	private final TIntObjectHashMap<PeptideBean> peptidesByPeptideBeanUniqueIdentifier = new TIntObjectHashMap<PeptideBean>();
	private final Map<String, PeptideBean> peptidesBySequence = new THashMap<String, PeptideBean>();

	private final Map<String, Set<PSMBean>> psmsBySequence = new THashMap<String, Set<PSMBean>>();
	private final Map<String, Set<PSMBean>> psmsByRawSequence = new THashMap<String, Set<PSMBean>>();
	private final RatioAnalyzer ratioAnalyzer = new RatioAnalyzer();

	private boolean separateNonConclusiveProteins = false;
	private boolean ready;
	private final String sessionId;
	private String name;
	private Thread activeDatasetThread;
	private final static Logger log = Logger.getLogger(DataSet.class);
	private Date latestAccess;
	private final boolean psmCentric;
	private final Set<String> hiddenPTMs;
	private boolean readyForGettingPeptides;

	public DataSet(String sessionID, String name, boolean psmCentric, Set<String> hiddenPTMs) {
		sessionId = sessionID;
		this.psmCentric = psmCentric;
		this.hiddenPTMs = hiddenPTMs;
		setName(name);
		activeDatasetThread = Thread.currentThread();
		setLatestAccess(new Date());
	}

	/**
	 * @return the proteins
	 */
	public List<ProteinBean> getProteins() {
		if (!ready)
			return Collections.emptyList();
		if (proteins.isEmpty()) {
			final List<ProteinBean> proteinBeansFromPSMBeans = SharedDataUtil.getProteinBeansFromPSMBeans(psms);
			for (final ProteinBean proteinBean : proteinBeansFromPSMBeans) {
				addProtein(proteinBean);
			}
		}
		return proteins;
	}

	/**
	 * @return the proteins
	 */
	public List<PeptideBean> getPeptides() {
		if (!ready)
			return Collections.emptyList();
		if (peptides.isEmpty() && readyForGettingPeptides) {
			for (final ProteinBean proteinBean : proteins) {
				final List<PeptideBean> peptidesFromProtein = getPeptidesFromProtein(proteinBean);
				for (final PeptideBean peptideBean : peptidesFromProtein) {
					addPeptide(peptideBean);
				}
			}
//			final List<PeptideBean> peptideBeansFromProteinBeans = SharedDataUtil
//					.getPeptideBeansFromProteinBeans(proteins);
//			for (final PeptideBean peptideBean : peptideBeansFromProteinBeans) {
//				addPeptide(peptideBean);
//			}
		}
		return peptides;
	}

	/**
	 * @return the psms
	 */
	public List<PSMBean> getPsms() {
		if (!ready)
			return Collections.emptyList();
		if (psms.isEmpty() && psmCentric) {
			final List<PSMBean> psmBeansFromProteinBeans = SharedDataUtil.getPSMBeansFromProteinBeans(proteins);
			log.info("Getting " + psmBeansFromProteinBeans.size() + " PSMs from " + proteins.size() + " proteins");
			for (final PSMBean psmBean : psmBeansFromProteinBeans) {
				addPsm(psmBean);
			}
		}
		return psms;
	}

	public List<ProteinBean> getProteins(int start, int end) {
		if (getProteins().isEmpty()) {
			return proteins;
		}
		if (end > proteins.size()) {
			end = proteins.size();
		}
		if (start > end) {
			start = end - 1;
		}
		return proteins.subList(start, end);
	}

	public List<PeptideBean> getPeptides(int start, int end) {
		if (getPeptides().isEmpty())
			return peptides;
		if (end > peptides.size())
			end = peptides.size();
		if (start > end) {
			start = end - 1;
		}
		return peptides.subList(start, end);
	}

	public List<ProteinGroupBean> getProteinGroups(Boolean separateNonConclusiveProteins, int start, int end) {
		if (getProteinGroups(separateNonConclusiveProteins).isEmpty())
			return proteinGroups;
		if (end > proteinGroups.size())
			end = proteinGroups.size();
		return proteinGroups.subList(start, end);
	}

	public List<PSMBean> getPsms(int start, int end) {
		if (getPsms().isEmpty())
			return psms;
		if (end > psms.size())
			end = psms.size();
		if (start > end) {
			start = end - 1;
		}
		return psms.subList(start, end);
	}

	public boolean isEmpty() {

		return getProteins().isEmpty();

	}

	public void clearDataSet() {
		proteins.clear();
		psms.clear();
		proteinGroups.clear();
		proteinsByProteinBeanUniqueIdentifier.clear();
		proteinsByAccession.clear();
		psmsByRawSequence.clear();
		psmsBySequence.clear();
		psmsByPSMDBId.clear();
		peptides.clear();
		peptidesByPeptideBeanUniqueIdentifier.clear();
		peptidesBySequence.clear();
		ready = false;
	}

	/**
	 * If there is one protein with the same accession in the dataset, the protein
	 * will be merged. PSMs are automatically added to the dataset
	 *
	 * @param proteinBean
	 */
	public void addProtein(ProteinBean proteinBean) {
		if (proteinsByProteinBeanUniqueIdentifier.containsKey(proteinBean.getProteinBeanUniqueIdentifier())) {
			return;
		}
		if (proteinsByAccession.containsKey(proteinBean.getPrimaryAccession().getAccession())) {
			SharedDataUtil.mergeProteinBeans(proteinsByAccession.get(proteinBean.getPrimaryAccession().getAccession()),
					proteinBean);
			return;
		}

		proteins.add(proteinBean);
		proteinsByProteinBeanUniqueIdentifier.put(proteinBean.getProteinBeanUniqueIdentifier(), proteinBean);
		if (proteinBean.getPsms() != null) {
			for (final PSMBean psmBean : proteinBean.getPsms()) {
				addPsm(psmBean);
			}
		}
		// add ratios to ratioAnalyzer
		ratioAnalyzer.addRatios(proteinBean.getRatios());
		// add protein scores
		addScores(proteinScores, proteinBean.getScores());
	}

	public void addPsm(PSMBean psmBean) {
		if (psmsByPSMDBId.containsKey(psmBean.getDbID()))
			return;
		psms.add(psmBean);

		if (psmsBySequence.containsKey(psmBean.getSequence())) {
			psmsBySequence.get(psmBean.getSequence()).add(psmBean);
		} else {
			final Set<PSMBean> set = new THashSet<PSMBean>();
			set.add(psmBean);
			psmsBySequence.put(psmBean.getSequence(), set);
		}
		if (psmsByRawSequence.containsKey(psmBean.getFullSequence())) {
			psmsByRawSequence.get(psmBean.getFullSequence()).add(psmBean);
		} else {
			final Set<PSMBean> set = new THashSet<PSMBean>();
			set.add(psmBean);
			psmsByRawSequence.put(psmBean.getFullSequence(), set);
		}
		psmsByPSMDBId.put(psmBean.getDbID(), psmBean);
		// add ratios to ratioAnalyzer
		ratioAnalyzer.addRatios(psmBean.getRatios());
		// add psm scores
		addScores(psmScores, psmBean.getScores());
		// add ptm scores
		final List<PTMBean> ptms = psmBean.getPtms();
		if (ptms != null) {
			for (final PTMBean ptm : ptms) {
				final List<PTMSiteBean> ptmSites = ptm.getPtmSites();
				if (ptmSites != null) {
					for (final PTMSiteBean ptmSiteBean : ptmSites) {
						final ScoreBean score = ptmSiteBean.getScore();
						if (score != null) {
							addScore(ptmScores, score);
						}
					}
				}
			}
		}
	}

	public int getNumDifferentSequences(boolean distinguisModifiedPeptides) {
		if (distinguisModifiedPeptides) {
			return peptidesBySequence.size();
		} else {
			return psmsBySequence.size();
		}
	}

	public ProteinBeanSubList getLightProteinBeanSubList(int start, int end) {
		log.info("Getting protein list from " + start + " to " + end + " dataset '" + getName() + "' in session ID: "
				+ sessionId);
		final List<ProteinBean> proteins2 = getProteins(start, end);
		final List<ProteinBean> lightProteins = new ArrayList<ProteinBean>();
		for (final ProteinBean heavyProtein : proteins2) {
			final ProteinBean lightProteinBean = heavyProtein.cloneToLightProteinBean();
			getPeptidesFromProtein(lightProteinBean);
			lightProteins.add(lightProteinBean);
			lightProteinBean.getDbIds().clear();
			lightProteinBean.getPeptideDBIds().clear();
			lightProteinBean.getPeptideDBIdsByCondition().clear();
		}
		return new ProteinBeanSubList(lightProteins, getProteins().size());
//		return ProteinBeanSubList.getLightProteinBeanSubList(lightProteins, getProteins().size());
	}

	public PeptideBeanSubList getLightPeptideBeanSubList(int start, int end) {
		log.info("Getting peptide list from " + start + " to " + end + " dataset '" + getName() + "' in session ID: "
				+ sessionId);
		final List<PeptideBean> peptides2 = getPeptides(start, end);
		return PeptideBeanSubList.getLightPeptideBeanSubList(peptides2, getPeptides().size());
	}

	public List<ProteinGroupBean> getProteinGroups(Boolean separateNonConclusiveProteins) {
		if (!ready)
			return Collections.emptyList();
		if (proteinGroups.isEmpty() || (separateNonConclusiveProteins != null
				&& Boolean.compare(separateNonConclusiveProteins, this.separateNonConclusiveProteins) != 0)) {
			if (separateNonConclusiveProteins != null) {
				this.separateNonConclusiveProteins = separateNonConclusiveProteins;
			}
			proteinGroups.clear();
			proteinGroups.addAll(
					RemoteServicesTasks.groupProteins(getProteins(), this.separateNonConclusiveProteins, psmCentric));
		}
		return proteinGroups;
	}

	public PsmBeanSubList getLightPsmBeanSubList(int start, int end) {
		log.info("Getting psm list from " + start + " to " + end + " dataset '" + getName() + "' in session ID: "
				+ sessionId);
		final List<PSMBean> psms2 = getPsms(start, end);
		return PsmBeanSubList.getLightPsmsBeanSubList(psms2, getPsms().size());
	}

	public ProteinGroupBeanSubList getLightProteinGroupBeanSubList(Boolean separateNonConclusiveProteins, int start,
			int end) {
		log.info("Getting sorted protein group list from " + start + " to " + end + " dataset '" + getName()
				+ "' in session ID: " + sessionId);
		final List<ProteinGroupBean> proteinGroups2 = getProteinGroups(separateNonConclusiveProteins, start, end);
		return ProteinGroupBeanSubList.getLightProteinGroupBeanSubList(proteinGroups2,
				getProteinGroups(separateNonConclusiveProteins).size());
	}

	public void sortProteins(Comparator<ProteinBean> comparator) {
		final List<ProteinBean> proteins2 = getProteins();
		if (proteins2.isEmpty()) {
			return;
		}
		log.info("Sorting " + proteins2.size() + " Proteins");
		Collections.sort(proteins2, comparator);
	}

	public void sortProteinGroups(Comparator<ProteinGroupBean> comparator, Boolean separateNonConclusiveProteins) {

		final List<ProteinGroupBean> proteinGroups2 = getProteinGroups(separateNonConclusiveProteins);
		if (proteinGroups2.isEmpty()) {
			return;
		}
		log.info("Sorting " + proteinGroups2.size() + " Protein Groups");
		Collections.sort(proteinGroups2, comparator);
		log.info("Protein Groups sorted");
	}

	public void sortPsms(Comparator<PSMBean> comparator) {
		final List<PSMBean> psms2 = getPsms();
		if (psms2.isEmpty()) {
			return;
		}
		log.info("Sorting " + psms2.size() + " PSMs");
		Collections.sort(psms2, comparator);
	}

	public void sortPeptides(Comparator<PeptideBean> comparator) {
		final List<PeptideBean> peptides2 = getPeptides();
		if (peptides2.isEmpty()) {
			return;
		}
		log.info("Sorting " + peptides2.size() + " Peptides");
		Collections.sort(peptides2, comparator);
	}

	/**
	 * @return the ready
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * @param ready the ready to set
	 */
	public void setReady(boolean ready) {
		log.info("Dataset '" + name + "' ready: " + ready);
		this.ready = ready;

		// assign ratioDistributions
		assignRatioDistributionsToPSMs();
		assignRatioDistributionsToPeptides();
		assignRatioDistributionsToProteins();
	}

	private void assignRatioDistributions(Collection<ContainsRatios> containsRatios) {
		for (final ContainsRatios ratioContainer : containsRatios) {
			final Set<RatioBean> ratios = ratioContainer.getRatios();
			for (final RatioBean ratioBean : ratios) {
				final RatioDistribution ratioDistribution = ratioAnalyzer.getRatioDistribution(ratioBean);
				if (ratioDistribution != null) {
					ratioContainer.addRatioDistribution(ratioDistribution);
				}
			}
		}
	}

	private void assignRatioDistributionsToPSMs() {
		final List<PSMBean> psms2 = getPsms();
		final List<ContainsRatios> list = new ArrayList<ContainsRatios>();
		list.addAll(psms2);
		assignRatioDistributions(list);
	}

	private void assignRatioDistributionsToProteins() {
		final List<ProteinBean> proteins2 = getProteins();
		final List<ContainsRatios> list = new ArrayList<ContainsRatios>();
		list.addAll(proteins2);
		assignRatioDistributions(list);
	}

	private void assignRatioDistributionsToPeptides() {
		final List<PeptideBean> peptides2 = getPeptides();
		final List<ContainsRatios> list = new ArrayList<ContainsRatios>();
		list.addAll(peptides2);
		assignRatioDistributions(list);
	}

	public List<PSMBean> getPsmsFromPsmProvider(ContainsPSMs psmProvider) {
		log.info("Getting PSMs from psmProvider");
		if (psmProvider instanceof ProteinBean) {
			log.info("psmProvider is a Protein");
			return getPsmsFromProtein((ProteinBean) psmProvider);
		} else if (psmProvider instanceof ProteinGroupBean) {
			log.info("psmProvider is a ProteinGroup");
			return getPsmsFromProteinGroup((ProteinGroupBean) psmProvider);
		} else if (psmProvider instanceof PeptideBean) {
			log.info("psmProvider is a peptide");
			return getPsmsFromPeptide((PeptideBean) psmProvider);
		}
		return Collections.emptyList();
	}

	public List<PeptideBean> getPeptidesFromPeptideProvider(ContainsPeptides peptideProvider) {

		log.info("Getting peptides from peptideProvider");
		if (peptideProvider instanceof ProteinBean) {
			log.info("peptideProvider is a Protein");
			return getPeptidesFromProtein((ProteinBean) peptideProvider);
		} else if (peptideProvider instanceof ProteinGroupBean) {
			log.info("peptideProvider is a ProteinGroup");
			return getPeptidesFromProteinGroup((ProteinGroupBean) peptideProvider);
		}
		return Collections.emptyList();

	}

	public List<PSMBean> getPsmsFromProteinGroup(ProteinGroupBean proteinGroup) {
		if (proteinGroup != null) {
			log.info("Getting PSMs from protein Group " + proteinGroup.getPrimaryAccessionsString());
			final List<PSMBean> ret = new ArrayList<PSMBean>();
			for (final ProteinBean proteinBean : proteinGroup) {
				final List<PSMBean> psms2 = getPsmsFromPsmProvider(proteinBean);
				for (final PSMBean psmBean : psms2) {
					if (!ret.contains(psmBean)) {
						ret.add(psmBean);
					}
				}
			}
			log.info("ProteinGroup " + proteinGroup.getPrimaryAccessionsString() + " contains " + ret.size() + " PSMs");
			return ret;
		}
		log.info("ProteinGroup is null...returning empty PSM list");
		return Collections.emptyList();
	}

	private List<PeptideBean> getPeptidesFromProteinGroup(ProteinGroupBean proteinGroup) {
		if (proteinGroup != null) {
			log.info("Getting Peptides from protein Group " + proteinGroup.getPrimaryAccessionsString());
			final List<PeptideBean> ret = new ArrayList<PeptideBean>();
			for (final ProteinBean proteinBean : proteinGroup) {
				final List<PeptideBean> peptides2 = getPeptidesFromProtein(proteinBean);
				for (final PeptideBean peptideBean : peptides2) {
					if (!ret.contains(peptideBean)) {
						ret.add(peptideBean);
					}
				}
			}
			log.info("ProteinGroup " + proteinGroup.getPrimaryAccessionsString() + " contains " + ret.size()
					+ " Peptides");
			return ret;
		}
		log.info("ProteinGroup is null...returning empty PSM list");
		return Collections.emptyList();
	}

	public List<PSMBean> getPsmsFromProtein(ProteinBean protein) {
		if (protein != null) {
			log.info("Getting PSMs from protein " + protein.getPrimaryAccession().getAccession());
			// search for the proteinbean with the same unique identifier
			final int uniqueId = protein.getProteinBeanUniqueIdentifier();
			if (proteinsByProteinBeanUniqueIdentifier.containsKey(uniqueId)) {
				final List<PSMBean> psms2 = proteinsByProteinBeanUniqueIdentifier.get(uniqueId).getPsms();
				if (psms2 != null && !psms2.isEmpty()) {
					log.warn("Protein " + protein.getPrimaryAccession().getAccession() + " contains " + psms2.size()
							+ " PSMs");
					return psms2;
				} else {
					log.warn("Protein with unique identifier: " + protein.getProteinBeanUniqueIdentifier()
							+ " has no psms...returning empty PSM list");
					return Collections.emptyList();
				}
			} else {
				log.warn("Protein not found with unique identifier: " + protein.getProteinBeanUniqueIdentifier()
						+ " ...returning empty PSM list");
				return Collections.emptyList();
			}
		} else {
			log.warn("Protein is null...returning empty PSM list");
			return Collections.emptyList();
		}

	}

	public List<PSMBean> getPsmsFromPeptide(PeptideBean peptide) {
		if (peptide != null) {
			log.info("Getting PSMs from peptide " + peptide.getSequence());
			// search for the proteinbean with the same unique identifier
			final int uniqueId = peptide.getPeptideBeanUniqueIdentifier();
			if (peptidesByPeptideBeanUniqueIdentifier.containsKey(uniqueId)) {
				final List<PSMBean> psms2 = peptidesByPeptideBeanUniqueIdentifier.get(uniqueId).getPsms();
				if (psms2 != null && !psms2.isEmpty()) {
					log.info("Peptide " + peptide.getSequence() + " contains " + psms2.size() + " PSMs");
					return psms2;
				}
			} else {
				log.info("Peptide with unique id " + uniqueId + " is not found");
			}
		}
		log.info("Peptide is null...returning empty PSM list");
		return Collections.emptyList();
	}

	private List<PeptideBean> getPeptidesFromProtein(ProteinBean lightProtein) {

		if (lightProtein != null) {

			log.debug("Getting Peptides from protein " + lightProtein.getPrimaryAccession().getAccession());
			// search for the proteinbean with the same unique identifier
			final int uniqueId = lightProtein.getProteinBeanUniqueIdentifier();
			if (proteinsByProteinBeanUniqueIdentifier.containsKey(uniqueId)) {

				final ProteinBean proteinBean = proteinsByProteinBeanUniqueIdentifier.get(uniqueId);
				if (!proteinBean.needsToQueryPeptides()) {
					final List<PeptideBean> peptides2 = proteinBean.getPeptides();
					log.debug("Protein " + lightProtein.getPrimaryAccession().getAccession() + " contains "
							+ peptides2.size() + " peptides");
					return peptides2;
				} else {
					// they are not retrieved yet from the database
					log.debug("Peptide from proteins " + lightProtein.getPrimaryAccession().getAccession()
							+ " were not retrived from DB yet....now querying them...");
					final Set<Integer> peptideDBIds = proteinBean.getPeptideDBIds();
					final TIntSet tintset = new TIntHashSet(peptideDBIds);
					final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(tintset, true, 2500);
					final Map<String, List<Peptide>> peptideMap = new THashMap<String, List<Peptide>>();
					PersistenceUtils.addToPeptideMapByFullSequence(peptideMap, peptides);
					// now create the peptideBean from the peptideMap
					final List<PeptideBean> ret = new ArrayList<PeptideBean>();
					for (final List<Peptide> peptideSet : peptideMap.values()) {
						final PeptideBean peptideBean = new PeptideBeanAdapterFromPeptideSet(peptideSet, hiddenPTMs,
								psmCentric).adapt();

						ret.add(peptideBean);
						peptideBean.addProteinToPeptide(lightProtein);
						proteinBean.addPeptideToProtein(peptideBean);
						//
						final Map<String, Collection<Protein>> proteinsFromPeptides = PersistenceUtils
								.getProteinsFromPeptidesUsingProteinToPeptideMappingTable(peptideSet, false);
						for (final Collection<Protein> proteinSet : proteinsFromPeptides.values()) {

							final ProteinBean newProteinBean = new ProteinBeanAdapterFromProteinSet(proteinSet,
									hiddenPTMs, psmCentric).adapt();

							peptideBean.addProteinToPeptide(newProteinBean);
							newProteinBean.addPeptideToProtein(peptideBean);
							if (peptideBean.getRelation() == null) {
								peptideBean.setRelation(
										newProteinBean.getPeptideRelationsBySequence().get(peptideBean.getSequence()));
							}
						}

					}
					exploreConnections(proteinBean);

					// call to annotate protein, because it will ask for the protein sequence in
					// uniprot and will draw the proteinSequenceGraph and will assign the starting
					// position of each peptide in the protein
					annotateProtein(proteinBean);
					return ret;

				}
			} else {
				log.warn("Protein not found in proteins by unique protein bean identifier");
				return Collections.emptyList();
			}
		}
		log.info("Protein is null...returning empty Peptide list");
		return Collections.emptyList();

	}

	/**
	 * it will ask for the protein sequence in uniprot and will draw the
	 * proteinSequenceGraph and will assign the starting position of each peptide in
	 * the protein
	 * 
	 * @param proteinBean
	 */
	private void annotateProtein(ProteinBean proteinBean) {
		if (proteinBean.isAnnotated()) {
			return;
		}
		final THashSet<ProteinBean> proteinBeans = new THashSet<ProteinBean>();
		proteinBeans.add(proteinBean);
		RemoteServicesTasks.annotateProteinBeansWithUniprot(proteinBeans, null, hiddenPTMs, true, true);

	}

	private void exploreConnections(ProteinBean proteinBean) {
		final TIntSet proteinDBIDsTotal = new TIntHashSet();
		final TIntSet peptideDBIDsTotal = new TIntHashSet();
		proteinDBIDsTotal.addAll(proteinBean.getDbIds());
		exploreConnectionFromPeptides(proteinBean, proteinDBIDsTotal, peptideDBIDsTotal);

	}

	private void exploreConnectionFromPeptides(ProteinBean proteinBean, TIntSet proteinDBIDsTotal,
			TIntSet peptideDBIDsTotal) {
		final Set<Integer> peptideDBIds = proteinBean.getPeptideDBIds();
		final TIntSet toQuery = new TIntHashSet();
		for (final Integer pepID : peptideDBIds) {
			if (peptideDBIDsTotal.contains(pepID)) {
				continue;
			}
			toQuery.add(pepID);
		}
		if (toQuery.isEmpty()) {
			return;
		}
		// I get the peptides from the DB that are not yet retrieved
		final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(toQuery, true, 2500);
		peptideDBIDsTotal.addAll(toQuery);
		// I pull them by full sequences to create the peptideMap per each set
		final Map<String, List<Peptide>> peptideMap = new THashMap<String, List<Peptide>>();
		PersistenceUtils.addToPeptideMapByFullSequence(peptideMap, peptides);
		final TIntSet proteinIDsToFollow = new TIntHashSet();
		final Map<Integer, PeptideBean> peptideBeanByPeptideIDs = new THashMap<Integer, PeptideBean>();
		for (final String fullSequence : peptideMap.keySet()) {
			final List<Peptide> peptideSet = peptideMap.get(fullSequence);
			// I create the peptideBean or retrieve the peptide Bean that was already
			// associated with that full sequence
			final PeptideBean newPeptideBean = new PeptideBeanAdapterFromPeptideSet(peptideSet, hiddenPTMs, psmCentric)
					.adapt();
			// add the peptideBean to the proteinBean
			proteinBean.addPeptideToProtein(newPeptideBean);
//			peptidesByPeptideBeanUniqueIdentifier.put(newPeptideBean.getPeptideBeanUniqueIdentifier(), newPeptideBean);
			// if the peptideBean was not present when grouping was done, retrieve the
			// peptideRelation from its protein by sequence and set it to the peptide
			if (newPeptideBean.getRelation() == null) {
				newPeptideBean
						.setRelation(proteinBean.getPeptideRelationsBySequence().get(newPeptideBean.getSequence()));
			}
			// associate the created peptideBean with the identifiers of the peptides we
			// used to create it
			for (final Peptide peptide : peptideSet) {
				peptideBeanByPeptideIDs.put(peptide.getId(), newPeptideBean);
				// check if some of the proteins of these peptides have not been processed yet
				// and in that case, store them in a set
				final TIntSet proteinIDs = ProteinIDToPeptideIDTableMapper.getInstance()
						.getProteinIDsFromPeptideID(peptide.getId());
				for (final int proteinID : proteinIDs.toArray()) {
					if (!proteinDBIDsTotal.contains(proteinID)) {
						proteinIDsToFollow.add(proteinID);
					}
				}
			}
		}

		// if the set of proteins to followUp is not empty
		if (!proteinIDsToFollow.isEmpty()) {
			final List<Protein> proteinsToFollow = (List<Protein>) PreparedCriteria.getBatchLoadByIDs(Protein.class,
					proteinIDsToFollow, true, 200);
			// group them by primary accession map
			final Map<String, Collection<Protein>> proteinMap = new THashMap<String, Collection<Protein>>();
			PersistenceUtils.addToMapByPrimaryAcc(proteinMap, proteinsToFollow);
			final Set<ProteinBean> newProteinBeansToExplore = new THashSet<ProteinBean>();
			for (final String acc : proteinMap.keySet()) {
				final Collection<Protein> proteinSet = proteinMap.get(acc);
				// create a new ProteinBean per each proteinSet with the same primary accession
				final ProteinBean newProteinBean = new ProteinBeanAdapterFromProteinSet(proteinSet, hiddenPTMs,
						psmCentric).adapt();
				final Set<Integer> peptideDBIds2 = newProteinBean.getPeptideDBIds();
				// clone it, otherwise will be a concurrent exception because when adding
				// peptide to protein, the id will be added to the same array we are iterating
				final TIntSet peptideDBIDs2 = new TIntHashSet();
				peptideDBIDs2.addAll(peptideDBIds2);
				for (final int peptideDBId : peptideDBIDs2.toArray()) {
					newProteinBean.addPeptideToProtein(peptideBeanByPeptideIDs.get(peptideDBId));
				}
				proteinsByProteinBeanUniqueIdentifier.put(newProteinBean.getProteinBeanUniqueIdentifier(),
						newProteinBean);
				for (final Protein protein : proteinSet) {
					proteinDBIDsTotal.add(protein.getId());
				}
				newProteinBeansToExplore.add(newProteinBean);
			}
			for (final ProteinBean newProteinBeanToExplore : newProteinBeansToExplore) {
				exploreConnectionFromPeptides(newProteinBeanToExplore, proteinDBIDsTotal, peptideDBIDsTotal);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "/nDATASET:\nThread holder ID:\t" + activeDatasetThread.getId() + "\nReady:\t" + ready + "\nName: "
				+ name + "\nSessionID: " + sessionId + "\nProtein groups:\t" + getProteinGroups(false).size() + "\n"
				+ "Proteins:\t" + getProteins().size() + "\n" + "PSMs:\t" + getPsms().size() + "\n" + "Peptides:\t"
				+ getNumDifferentSequences(false) + "\n" + "Peptides(ptm sensible):\t" + getNumDifferentSequences(true)
				+ "\n";
	}

	public void addPeptide(PeptideBean peptideBean) {
		if (peptidesByPeptideBeanUniqueIdentifier.containsKey(peptideBean.getPeptideBeanUniqueIdentifier()))
			return;
		//
		if (peptidesBySequence.containsKey(peptideBean.getFullSequence())) {
			mergePeptideBeans(peptidesBySequence.get(peptideBean.getFullSequence()), peptideBean);
			return;
		}
		peptidesBySequence.put(peptideBean.getFullSequence(), peptideBean);
		peptides.add(peptideBean);
		peptidesByPeptideBeanUniqueIdentifier.put(peptideBean.getPeptideBeanUniqueIdentifier(), peptideBean);
		// add ratios to ratioAnalyzer
		ratioAnalyzer.addRatios(peptideBean.getRatios());
		// add peptide scores
		addScores(peptideScores, peptideBean.getScores());
		// add ptm scores
		final List<PTMBean> ptms = peptideBean.getPtms();
		if (ptms != null) {
			for (final PTMBean ptm : ptms) {
				final List<PTMSiteBean> ptmSites = ptm.getPtmSites();
				if (ptmSites != null) {
					for (final PTMSiteBean ptmSiteBean : ptmSites) {
						final ScoreBean score = ptmSiteBean.getScore();
						if (score != null) {
							addScore(ptmScores, score);
						}
					}
				}
			}
		}
	}

	private void addScores(Set<String> scoreNamesSet, Map<String, ScoreBean> scores) {
		if (scores != null) {
			addScores(scoreNamesSet, scores.values());
		}
	}

	private void addScores(Set<String> scoreNamesSet, Collection<ScoreBean> scores) {
		for (final ScoreBean score : scores) {
			addScore(scoreNamesSet, score);

		}
	}

	private void addScore(Set<String> scoreNamesSet, ScoreBean score) {
		if (score != null) {
			if (score.getScoreName() != null) {
				scoreNamesSet.add(score.getScoreName());
			}
			if (score.getScoreType() != null) {
				scoreTypes.add(score.getScoreType());
			}
		}
	}

	private void mergePeptideBeans(PeptideBean peptideBean, PeptideBean peptideBean2) {
		log.info("Merging peptides...TODO!!");
		// TODO
	}

	public TIntObjectHashMap<ProteinBean> getProteinBeansByUniqueIdentifier() {
		return proteinsByProteinBeanUniqueIdentifier;
	}

	public TIntObjectHashMap<PeptideBean> getPeptideBeansByUniqueIdentifier() {
		return peptidesByPeptideBeanUniqueIdentifier;
	}

	public TIntObjectHashMap<PSMBean> getPSMBeansByUniqueIdentifier() {
		return psmsByPSMDBId;
	}

	/**
	 * Check whether in the dataset there is more than one {@link ProteinBean} with
	 * the same primary accession, and in that case, merge the two
	 * {@link ProteinBean}
	 */
	public void fixPrimaryAccessionDuplicates() {
		setReady(false);
		final List<ProteinBean> proteinList = getProteins();
		final int initialProteinNumber = proteinList.size();

		final Map<String, ProteinBean> map = new THashMap<String, ProteinBean>();
		int foundDuplicate = 0;
		final Iterator<ProteinBean> iterator = proteinList.iterator();
		while (iterator.hasNext()) {
			final ProteinBean proteinBean = iterator.next();
			if (map.containsKey(proteinBean.getPrimaryAccession().getAccession())) {
				foundDuplicate++;
				// merge the two protein beans
				SharedDataUtil.mergeProteinBeans(map.get(proteinBean.getPrimaryAccession().getAccession()),
						proteinBean);
				// remove proteinBean from list
				iterator.remove();
				// remove from map by unique identifier
				proteinsByProteinBeanUniqueIdentifier.remove(proteinBean.getProteinBeanUniqueIdentifier());
				// override in proteinsByAccession inserting the one in the map
				proteinsByAccession.put(proteinBean.getPrimaryAccession().getAccession(),
						map.get(proteinBean.getPrimaryAccession().getAccession()));
			} else {
				map.put(proteinBean.getPrimaryAccession().getAccession(), proteinBean);
			}
		}
		if (foundDuplicate > 0) {
			log.info(foundDuplicate + " duplicates found. Rebuilding peptide and PSM lists");
			log.info(getProteins().size() + " proteins now. Before: " + initialProteinNumber);
			peptides.clear();
			psms.clear();
		}
		setReady(true);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public Thread getActiveDatasetThread() {
		return activeDatasetThread;
	}

	public Date getLatestAccess() {
		return latestAccess;
	}

	public void setLatestAccess(Date latestAccess) {
		this.latestAccess = latestAccess;
	}

	public void setActiveDatasetThread(Thread activeDatasetThread) {
		log.info("Changing thread holder for dataset in session ID '" + sessionId + "' to thread "
				+ activeDatasetThread.getId());

		this.activeDatasetThread = activeDatasetThread;
	}

	public List<String> getProteinScores() {
		getProteins();
		return proteinScores.stream().collect(Collectors.toList()).stream().sorted().collect(Collectors.toList());
	}

	public List<String> getPeptideScores() {
		getPeptides();
		return peptideScores.stream().collect(Collectors.toList()).stream().sorted().collect(Collectors.toList());
	}

	public List<String> getPsmScores() {
		if (psmCentric) {
			getPsms();
		} else {
			getPeptides();
		}
		return psmScores.stream().collect(Collectors.toList()).stream().sorted().collect(Collectors.toList());
	}

	public List<String> getPtmScores() {
		if (psmCentric) {
			getPsms();
		} else {
			getPeptides();
		}
		return ptmScores.stream().collect(Collectors.toList()).stream().sorted().collect(Collectors.toList());
	}

	public List<String> getScoreTypes() {
		getProteins();
		getPeptides();
		if (psmCentric) {
			getPsms();
		}
		return scoreTypes.stream().collect(Collectors.toList()).stream().sorted().collect(Collectors.toList());
	}

	public static DataSet emptyDataSet() {
		return new DataSet(null, null, false, null);
	}

	public void setReadyForGettingPeptides(boolean b) {
		this.readyForGettingPeptides = b;
	}

	protected boolean isReadyForGettingPeptides() {
		return readyForGettingPeptides;
	}
}
