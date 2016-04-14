package edu.scripps.yates.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;

import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.RatioAnalyzer;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.SharedDataUtils;
import edu.scripps.yates.shared.util.sublists.PeptideBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinBeanSubList;
import edu.scripps.yates.shared.util.sublists.ProteinGroupBeanSubList;
import edu.scripps.yates.shared.util.sublists.PsmBeanSubList;

public class DataSet {
	private final List<ProteinGroupBean> proteinGroups = new ArrayList<ProteinGroupBean>();
	private final List<ProteinBean> proteins = new ArrayList<ProteinBean>();
	private final List<PeptideBean> peptides = new ArrayList<PeptideBean>();
	private final List<PSMBean> psms = new ArrayList<PSMBean>();

	private final HashMap<Integer, ProteinBean> proteinsByProteinbeanUniqueIdentifier = new HashMap<Integer, ProteinBean>();
	private final HashMap<String, ProteinBean> proteinsByAccession = new HashMap<String, ProteinBean>();
	private final HashMap<Integer, PSMBean> psmsByPSMDBId = new HashMap<Integer, PSMBean>();
	private final HashMap<Integer, PeptideBean> peptidesByPeptideBeanUniqueIdentifier = new HashMap<Integer, PeptideBean>();
	private final HashMap<String, PeptideBean> peptidesBySequence = new HashMap<String, PeptideBean>();

	private final Map<String, Set<PSMBean>> psmsBySequence = new HashMap<String, Set<PSMBean>>();
	private final Map<String, Set<PSMBean>> psmsByRawSequence = new HashMap<String, Set<PSMBean>>();
	private final RatioAnalyzer ratioAnalyzer = new RatioAnalyzer();

	private boolean separateNonConclusiveProteins = false;
	private boolean ready;
	private final String sessionId;
	private String name;
	private final Criteria criteria;
	private final static Logger log = Logger.getLogger(DataSet.class);

	public DataSet(String sessionID, String name) {
		this(sessionID, name, null);
	}

	public DataSet(String sessionID, String name, Criteria criteria) {
		sessionId = sessionID;
		setName(name);
		this.criteria = criteria;
	}

	/**
	 * @return the proteins
	 */
	public List<ProteinBean> getProteins() {
		if (!ready)
			return Collections.EMPTY_LIST;
		if (proteins.isEmpty()) {
			proteins.addAll(SharedDataUtils.getProteinBeansFromPSMBeans(psms));
		}
		return proteins;
	}

	/**
	 * @return the proteins
	 */
	public List<PeptideBean> getPeptides() {
		if (!ready)
			return Collections.EMPTY_LIST;
		if (peptides.isEmpty()) {

			final List<PeptideBean> peptideBeansFromProteinBeans = SharedDataUtils
					.getPeptideBeansFromProteinBeans(proteins);
			for (PeptideBean peptideBean : peptideBeansFromProteinBeans) {
				addPeptide(peptideBean);
			}
		}
		return peptides;
	}

	/**
	 * @return the psms
	 */
	public List<PSMBean> getPsms() {
		if (!ready)
			return Collections.EMPTY_LIST;
		if (psms.isEmpty()) {
			final List<PSMBean> psmBeansFromProteinBeans = SharedDataUtils.getPSMBeansFromProteinBeans(proteins);
			log.info("Getting " + psmBeansFromProteinBeans.size() + " PSMs from " + proteins.size() + " proteins");
			for (PSMBean psmBean : psmBeansFromProteinBeans) {
				addPsm(psmBean);
			}
		}
		return psms;
	}

	public List<ProteinBean> getProteins(int start, int end) {
		if (getProteins().isEmpty())
			return proteins;
		if (end > proteins.size())
			end = proteins.size();
		return proteins.subList(start, end);
	}

	public List<PeptideBean> getPeptides(int start, int end) {
		if (getPeptides().isEmpty())
			return peptides;
		if (end > peptides.size())
			end = peptides.size();
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
		return psms.subList(start, end);
	}

	public boolean isEmpty() {

		return getProteins().isEmpty();

	}

	public void clearDataSet() {
		proteins.clear();
		psms.clear();
		proteinGroups.clear();
		proteinsByProteinbeanUniqueIdentifier.clear();
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
	 * If there is one protein with the same accession in the dataset, the
	 * protein will be merged. PSMs are automatically added to the dataset
	 *
	 * @param proteinBean
	 */
	public void addProtein(ProteinBean proteinBean) {
		if (proteinsByProteinbeanUniqueIdentifier.containsKey(proteinBean.getProteinBeanUniqueIdentifier())) {
			return;
		}
		if (proteinsByAccession.containsKey(proteinBean.getPrimaryAccession().getAccession())) {
			SharedDataUtils.mergeProteinBeans(proteinsByAccession.get(proteinBean.getPrimaryAccession().getAccession()),
					proteinBean);
			return;
		}

		proteins.add(proteinBean);
		proteinsByProteinbeanUniqueIdentifier.put(proteinBean.getProteinBeanUniqueIdentifier(), proteinBean);
		if (proteinBean.getPsms() != null) {
			for (PSMBean psmBean : proteinBean.getPsms()) {
				addPsm(psmBean);
			}
		}
		// add ratios to ratioAnalyzer
		ratioAnalyzer.addRatios(proteinBean.getRatios());
	}

	public void addPsm(PSMBean psmBean) {
		if (psmsByPSMDBId.containsKey(psmBean.getDbID()))
			return;
		psms.add(psmBean);

		if (psmsBySequence.containsKey(psmBean.getSequence())) {
			psmsBySequence.get(psmBean.getSequence()).add(psmBean);
		} else {
			Set<PSMBean> set = new HashSet<PSMBean>();
			set.add(psmBean);
			psmsBySequence.put(psmBean.getSequence(), set);
		}
		if (psmsByRawSequence.containsKey(psmBean.getFullSequence())) {
			psmsByRawSequence.get(psmBean.getFullSequence()).add(psmBean);
		} else {
			Set<PSMBean> set = new HashSet<PSMBean>();
			set.add(psmBean);
			psmsByRawSequence.put(psmBean.getFullSequence(), set);
		}
		psmsByPSMDBId.put(psmBean.getDbID(), psmBean);
		// add ratios to ratioAnalyzer
		ratioAnalyzer.addRatios(psmBean.getRatios());
	}

	public int getNumDifferentSequences(boolean distinguisModifiedPeptides) {
		if (distinguisModifiedPeptides) {
			return psmsByRawSequence.size();
		} else {
			return psmsBySequence.size();
		}
	}

	public ProteinBeanSubList getLightProteinBeanSubList(int start, int end) {
		log.info("Getting protein list from " + start + " to " + end + " dataset '" + getName() + "' in session ID: "
				+ sessionId);
		final List<ProteinBean> proteins2 = getProteins(start, end);
		return ProteinBeanSubList.getLightProteinBeanSubList(proteins2, getProteins().size());
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
			proteinGroups.addAll(RemoteServicesTasks.groupProteins(getProteins(), this.separateNonConclusiveProteins));
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
	 * @param ready
	 *            the ready to set
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
		for (ContainsRatios ratioContainer : containsRatios) {
			final Set<RatioBean> ratios = ratioContainer.getRatios();
			for (RatioBean ratioBean : ratios) {
				final RatioDistribution ratioDistribution = ratioAnalyzer.getRatioDistribution(ratioBean);
				ratioContainer.addRatioDistribution(ratioDistribution);
			}
		}
	}

	private void assignRatioDistributionsToPSMs() {
		final List<PSMBean> psms2 = getPsms();
		List<ContainsRatios> list = new ArrayList<ContainsRatios>();
		list.addAll(psms2);
		assignRatioDistributions(list);
	}

	private void assignRatioDistributionsToProteins() {
		final List<ProteinBean> proteins2 = getProteins();
		List<ContainsRatios> list = new ArrayList<ContainsRatios>();
		list.addAll(proteins2);
		assignRatioDistributions(list);
	}

	private void assignRatioDistributionsToPeptides() {
		final List<PeptideBean> peptides2 = getPeptides();
		List<ContainsRatios> list = new ArrayList<ContainsRatios>();
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
		return Collections.EMPTY_LIST;
	}

	public List<PeptideBean> getPeptidesFromPeptideProvider(ContainsPeptides peptideProvider) {
		log.info("Getting PSMs from psmProvider");
		if (peptideProvider instanceof ProteinBean) {
			log.info("peptideProvider is a Protein");
			return getPeptidesFromProtein((ProteinBean) peptideProvider);
		} else if (peptideProvider instanceof ProteinGroupBean) {
			log.info("peptideProvider is a ProteinGroup");
			return getPeptidesFromProteinGroup((ProteinGroupBean) peptideProvider);
		}
		return Collections.EMPTY_LIST;
	}

	public List<PSMBean> getPsmsFromProteinGroup(ProteinGroupBean proteinGroup) {
		if (proteinGroup != null) {
			log.info("Getting PSMs from protein Group " + proteinGroup.getPrimaryAccessionsString());
			List<PSMBean> ret = new ArrayList<PSMBean>();
			for (ProteinBean proteinBean : proteinGroup) {
				final List<PSMBean> psms2 = getPsmsFromPsmProvider(proteinBean);
				for (PSMBean psmBean : psms2) {
					if (!ret.contains(psmBean)) {
						ret.add(psmBean);
					}
				}
			}
			log.info("ProteinGroup " + proteinGroup.getPrimaryAccessionsString() + " contains " + ret.size() + " PSMs");
			return ret;
		}
		log.info("ProteinGroup is null...returning empty PSM list");
		return Collections.EMPTY_LIST;
	}

	public List<PeptideBean> getPeptidesFromProteinGroup(ProteinGroupBean proteinGroup) {
		if (proteinGroup != null) {
			log.info("Getting Peptides from protein Group " + proteinGroup.getPrimaryAccessionsString());
			List<PeptideBean> ret = new ArrayList<PeptideBean>();
			for (ProteinBean proteinBean : proteinGroup) {
				final List<PeptideBean> peptides2 = getPeptidesFromPeptideProvider(proteinBean);
				for (PeptideBean peptideBean : peptides2) {
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
		return Collections.EMPTY_LIST;
	}

	public List<PSMBean> getPsmsFromProtein(ProteinBean protein) {
		if (protein != null) {
			log.info("Getting PSMs from protein " + protein.getPrimaryAccession().getAccession());
			// search for the proteinbean with the same unique identifier
			final int uniqueId = protein.getProteinBeanUniqueIdentifier();
			if (proteinsByProteinbeanUniqueIdentifier.containsKey(uniqueId)) {
				final List<PSMBean> psms2 = proteinsByProteinbeanUniqueIdentifier.get(uniqueId).getPsms();
				if (psms2 != null && !psms2.isEmpty()) {
					log.info("Protein " + protein.getPrimaryAccession().getAccession() + " contains " + psms2.size()
							+ " PSMs");
					return psms2;
				}
			}
		}
		log.info("Protein is null...returning empty PSM list");
		return Collections.EMPTY_LIST;
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
		return Collections.EMPTY_LIST;
	}

	public List<PeptideBean> getPeptidesFromProtein(ProteinBean protein) {
		if (protein != null) {
			log.info("Getting Peptides from protein " + protein.getPrimaryAccession().getAccession());
			// search for the proteinbean with the same unique identifier
			final int uniqueId = protein.getProteinBeanUniqueIdentifier();
			if (proteinsByProteinbeanUniqueIdentifier.containsKey(uniqueId)) {
				final List<PeptideBean> peptides2 = proteinsByProteinbeanUniqueIdentifier.get(uniqueId).getPeptides();
				if (peptides2 != null && !peptides2.isEmpty()) {
					log.info("Protein " + protein.getPrimaryAccession().getAccession() + " contains " + peptides2.size()
							+ " peptides");
					return peptides2;
				}
			}
		}
		log.info("Protein is null...returning empty Peptide list");
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "/nDATASET:\nReady:\t" + ready + "\nName: " + name + "\nSessionID: " + sessionId + "\nProtein groups:\t"
				+ getProteinGroups(false).size() + "\n" + "Proteins:\t" + getProteins().size() + "\n" + "PSMs:\t"
				+ getPsms().size() + "\n" + "Peptides:\t" + getNumDifferentSequences(false) + "\n"
				+ "Peptides(ptm sensible):\t" + getNumDifferentSequences(true) + "\n";
	}

	public void addPeptide(PeptideBean peptideBean) {
		if (peptidesByPeptideBeanUniqueIdentifier.containsKey(peptideBean.getPeptideBeanUniqueIdentifier()))
			return;
		//
		if (peptidesBySequence.containsKey(peptideBean.getSequence())) {
			mergePeptideBeans(peptidesBySequence.get(peptideBean.getSequence()), peptideBean);
			return;
		}
		peptidesBySequence.put(peptideBean.getSequence(), peptideBean);
		peptides.add(peptideBean);
		peptidesByPeptideBeanUniqueIdentifier.put(peptideBean.getPeptideBeanUniqueIdentifier(), peptideBean);
		// add ratios to ratioAnalyzer
		ratioAnalyzer.addRatios(peptideBean.getRatios());
	}

	private void mergePeptideBeans(PeptideBean peptideBean, PeptideBean peptideBean2) {
		log.info("Merging peptides...TODO!!");
		// TODO
	}

	public HashMap<Integer, ProteinBean> getProteinBeansByUniqueIdentifier() {
		return proteinsByProteinbeanUniqueIdentifier;
	}

	public HashMap<Integer, PeptideBean> getPeptideBeansByUniqueIdentifier() {
		return peptidesByPeptideBeanUniqueIdentifier;
	}

	public HashMap<Integer, PSMBean> getPSMBeansByUniqueIdentifier() {
		return psmsByPSMDBId;
	}

	/**
	 * Check whether in the dataset there is more than one {@link ProteinBean}
	 * with the same primary accession, and in that case, merge the two
	 * {@link ProteinBean}
	 */
	public void fixPrimaryAccessionDuplicates() {
		setReady(false);
		final List<ProteinBean> proteinList = getProteins();
		int initialProteinNumber = proteinList.size();

		Map<String, ProteinBean> map = new HashMap<String, ProteinBean>();
		int foundDuplicate = 0;
		final Iterator<ProteinBean> iterator = proteinList.iterator();
		while (iterator.hasNext()) {
			ProteinBean proteinBean = iterator.next();
			if (map.containsKey(proteinBean.getPrimaryAccession().getAccession())) {
				foundDuplicate++;
				// merge the two protein beans
				SharedDataUtils.mergeProteinBeans(map.get(proteinBean.getPrimaryAccession().getAccession()),
						proteinBean);
				// remove proteinBean from list
				iterator.remove();
				// remove from map by unique identifier
				proteinsByProteinbeanUniqueIdentifier.remove(proteinBean.getProteinBeanUniqueIdentifier());
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
