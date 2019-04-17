package edu.scripps.yates.shared.model;

import java.io.Serializable;
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

import edu.scripps.yates.shared.model.interfaces.ContainsAmounts;
import edu.scripps.yates.shared.model.interfaces.ContainsConditions;
import edu.scripps.yates.shared.model.interfaces.ContainsGenes;
import edu.scripps.yates.shared.model.interfaces.ContainsId;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class ProteinGroupBean extends ArrayList<ProteinBean> implements Serializable, ContainsRatios, ContainsAmounts,
		ContainsId, ContainsGenes, ContainsPrimaryAccessions, ContainsPSMs, ContainsPeptides, ContainsConditions {

	/**
	 *
	 */
	private static final long serialVersionUID = -2919705603019167262L;

	private Set<Integer> psmDBIds = new HashSet<Integer>();

	private int numPeptides;

	private int numPSMs;

	private List<PeptideBean> peptides = new ArrayList<PeptideBean>();
	private final Set<Integer> peptideDBIds = new HashSet<Integer>();
	private ProteinGroupBean lightVersion;

	// used only for the SPCs:
	private Set<AmountBean> amounts = new HashSet<AmountBean>();

	private Map<String, RatioDistribution> ratioDistributions;

	private Map<ExperimentalConditionBean, Integer> numPSMsByCondition;

	public ProteinGroupBean() {

	}

	/**
	 * Note that some elements in the list can be null if onlyPrimary is true.
	 *
	 * @param onlyPrimary
	 * @return
	 */
	@Override
	public List<GeneBean> getGenes(boolean onlyPrimary) {
		final List<GeneBean> ret = new ArrayList<GeneBean>();
		for (final ProteinBean protein : this) {
			final List<GeneBean> genes = protein.getGenes(onlyPrimary);
			if (onlyPrimary && genes.isEmpty())
				ret.add(null);
			else
				ret.addAll(genes);
		}
		return ret;
	}

	public String getProteinDBString() {
		final StringBuilder sb = new StringBuilder();
		for (final ProteinBean protein : this) {
			sb.append(protein.getProteinDBString());
		}
		return sb.toString();
	}

	public int getNumPSMs() {
		if (numPSMs == 0) {
			for (final PeptideBean peptide : getPeptides()) {
				numPSMs += peptide.getNumPSMs();
			}
		}
		return numPSMs;
	}

	/**
	 * @param numPSMs the numPSMs to set
	 */
	public void setNumPSMs(int numPSMs) {
		this.numPSMs = numPSMs;
	}

	public String getGenesString(boolean onlyPrimaryGenes) {
		final StringBuilder sb = new StringBuilder();
		final Set<String> set = new HashSet<String>();
		for (final GeneBean gene : getGenes(onlyPrimaryGenes)) {
			if (gene == null || set.contains(gene.getGeneID()))
				continue;

			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);

			set.add(gene.getGeneID());
			sb.append(gene.getGeneID());
		}
		if ("".equals(sb.toString()) && onlyPrimaryGenes)
			return getGenesString(false);
		return sb.toString();
	}

	public int getNumPeptides() {
		if (numPeptides == 0) {
			final Set<String> seq = new HashSet<String>();
			for (final ProteinBean protein : this) {
				seq.addAll(protein.getDifferentSequences());
			}
			numPeptides = seq.size();
		}
		return numPeptides;
	}

	/**
	 * @param numPeptides the numPeptides to set
	 */
	public void setNumPeptides(int numPeptides) {
		this.numPeptides = numPeptides;
	}

	@Override
	public List<AccessionBean> getPrimaryAccessions() {
		final List<AccessionBean> ret = new ArrayList<AccessionBean>();
		for (final ProteinBean protein : this) {
			ret.add(protein.getPrimaryAccession());
		}
		// sort alphabetically
		Collections.sort(ret, SharedDataUtil.getComparatorByAccession());
		return ret;
	}

	public String getPrimaryAccessionsString() {
		final StringBuilder sb = new StringBuilder();
		final Set<String> accs = new HashSet<String>();
		final List<AccessionBean> primaryAccessions = getPrimaryAccessions();
		for (final AccessionBean acc : primaryAccessions) {
			if (accs.contains(acc.getAccession()))
				continue;
			accs.add(acc.getAccession());
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(acc.getAccession());
		}
		return sb.toString();
	}

	public String getDescriptionsString() {
		final StringBuilder sb = new StringBuilder();
		final Set<String> accs = new HashSet<String>();
		for (final AccessionBean acc : getPrimaryAccessions()) {
			if (accs.contains(acc.getAccession()))
				continue;
			accs.add(acc.getAccession());
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			if (acc.getDescription() != null && !"".equals(acc.getDescription()))
				sb.append(acc.getDescription());
			else
				sb.append("-");
		}
		return sb.toString();
	}

	@Override
	public List<AmountBean> getAmounts() {
		final List<AmountBean> ret = new ArrayList<AmountBean>();
		// be carefull with the SPC amounts comming from proteins, because we
		// dont want to count several times the spectral counts.
		for (final ProteinBean protein : this) {
			final List<AmountBean> amounts = protein.getAmounts();
			for (final AmountBean amountBean : amounts) {
				// ignore the SPC amounts at protein group level because here
				// has no sense. Recalculate them later
				if (!amountBean.getAmountType().equals(AmountType.SPC.name())) {
					ret.add(amountBean);
				}
			}
		}

		ret.addAll(amounts);

		return ret;
	}

	@Override
	public boolean hasCombinationAmounts(String conditionName, String projectTag) {
		for (final ProteinBean protein : this) {
			if (protein.hasCombinationAmounts(conditionName, projectTag))
				return true;
		}
		return false;
	}

	@Override
	public List<AmountBean> getCombinationAmount(String conditionName, String projectTag) {
		final List<AmountBean> ret = new ArrayList<AmountBean>();
		for (final AmountBean amountBean : getAmounts()) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
					if (amountBean.isComposed() && !ret.contains(amountBean)) {
						ret.add(amountBean);
					}
				}
			}
		}
		return ret;
	}

	@Override
	public List<AmountBean> getNonCombinationAmounts(String conditionName, String projectTag) {
		final List<AmountBean> ret = new ArrayList<AmountBean>();
		for (final AmountBean amountBean : getAmounts()) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
					if (!amountBean.isComposed() && !ret.contains(amountBean)) {
						ret.add(amountBean);
					}
				}
			}
		}
		return ret;
	}

	@Override
	public HashMap<String, List<AmountBean>> getAmountsByExperimentalCondition() {
		final HashMap<String, List<AmountBean>> ret = new HashMap<String, List<AmountBean>>();
		for (final ProteinBean protein : this) {
			final HashMap<String, List<AmountBean>> amountsByExperimentalCondition = protein
					.getAmountsByExperimentalCondition();
			for (final String conditionName : amountsByExperimentalCondition.keySet()) {
				if (ret.containsKey(conditionName)) {
					ret.get(conditionName).addAll(amountsByExperimentalCondition.get(conditionName));
				} else {
					final List<AmountBean> set = new ArrayList<AmountBean>();
					set.addAll(amountsByExperimentalCondition.get(conditionName));
					ret.put(conditionName, set);
				}
			}
		}
		return ret;
	}

	@Override
	public List<RatioBean> getRatiosByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, boolean skipInfinities) {
		final List<RatioBean> ret = new ArrayList<RatioBean>();
		for (final ProteinBean protein : this) {
			ret.addAll(protein.getRatios());
		}
		return SharedDataUtil.getRatiosByConditions(ret, condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);

	}

	public List<AccessionBean> getSecondaryAccessions() {
		final List<AccessionBean> ret = new ArrayList<AccessionBean>();
		for (final ProteinBean protein : this) {
			ret.addAll(protein.getSecondaryAccessions());
		}
		return ret;
	}

	public Set<Integer> getDbIds() {
		final Set<Integer> ret = new HashSet<Integer>();
		for (final ProteinBean protein : this) {
			ret.addAll(protein.getDbIds());
		}
		return ret;
	}

	public List<AccessionBean> getDifferentPrimaryAccessions() {
		final List<AccessionBean> primaryAccessions = getPrimaryAccessions();
		final Set<String> accs = new HashSet<String>();
		final List<AccessionBean> ret = new ArrayList<AccessionBean>();

		for (final AccessionBean accessionBean : primaryAccessions) {
			if (accs.contains(accessionBean.getAccession()))
				continue;
			accs.add(accessionBean.getAccession());
			ret.add(accessionBean);
		}
		return ret;
	}

	public String getGroupMemberEvidences() {
		StringBuilder sb = new StringBuilder();
		final List<AccessionBean> primaryAccessions = getPrimaryAccessions();
		final Map<AccessionBean, ProteinBean> map = new HashMap<AccessionBean, ProteinBean>();
		for (final ProteinBean protein : this) {
			map.put(protein.getPrimaryAccession(), protein);
		}
		final Set<String> evidences = new HashSet<String>();
		final Set<String> accs = new HashSet<String>();
		for (final AccessionBean accessionBean : primaryAccessions) {
			if (accs.contains(accessionBean.getAccession()))
				continue;
			accs.add(accessionBean.getAccession());
			final ProteinBean proteinBean = map.get(accessionBean);
			if (!"".equals(sb.toString())) {
				sb.append(SharedConstants.SEPARATOR);
			}
			if (proteinBean.getEvidence() != null) {

				sb.append(proteinBean.getEvidence().name());

				evidences.add(proteinBean.getEvidence().name());
			}
		}
		if (evidences.size() == 1)
			sb = new StringBuilder(evidences.iterator().next());
		return sb.toString();
	}

	public String getGroupMemberExistences() {
		StringBuilder sb = new StringBuilder();
		final List<AccessionBean> primaryAccessions = getPrimaryAccessions();
		final Map<AccessionBean, ProteinBean> map = new HashMap<AccessionBean, ProteinBean>();
		for (final ProteinBean protein : this) {
			map.put(protein.getPrimaryAccession(), protein);
		}
		final Set<UniprotProteinExistence> existences = new HashSet<UniprotProteinExistence>();
		final Set<String> accs = new HashSet<String>();
		for (final AccessionBean accessionBean : primaryAccessions) {
			if (accs.contains(accessionBean.getAccession()))
				continue;
			accs.add(accessionBean.getAccession());
			final ProteinBean proteinBean = map.get(accessionBean);

			if (proteinBean.getUniprotProteinExistence() != null) {
				sb.append(proteinBean.getUniprotProteinExistence().getName());
				existences.add(proteinBean.getUniprotProteinExistence());
			}
		}
		if (existences.size() == 1) {
			sb = new StringBuilder();
			final UniprotProteinExistence next = existences.iterator().next();

			sb.append(next.getName());

		}
		return sb.toString();
	}

	public String getOrganismsString() {
		StringBuilder sb = new StringBuilder();
		final List<AccessionBean> primaryAccessions = getPrimaryAccessions();
		final Map<AccessionBean, ProteinBean> map = new HashMap<AccessionBean, ProteinBean>();
		for (final ProteinBean protein : this) {
			map.put(protein.getPrimaryAccession(), protein);
		}
		final Set<String> accs = new HashSet<String>();
		final Set<String> organisms = new HashSet<String>();
		for (final AccessionBean accessionBean : primaryAccessions) {
			if (accs.contains(accessionBean.getAccession()))
				continue;
			accs.add(accessionBean.getAccession());
			final ProteinBean proteinBean = map.get(accessionBean);
			if (!"".equals(sb.toString())) {
				sb.append(SharedConstants.SEPARATOR);
			}
			final OrganismBean organism = proteinBean.getOrganism();
			if (organism != null && organism.getId() != null) {
				sb.append(organism.getId());
				organisms.add(organism.getId());
			}
		}
		// if there is only one, just print once
		if (organisms.size() == 1)
			sb = new StringBuilder(organisms.iterator().next());

		return sb.toString();
	}

	/**
	 * @return the psmIds
	 */
	@Override
	public Set<Integer> getPSMDBIds() {
		if (psmDBIds.isEmpty()) {
			for (final ProteinBean proteinBean : this) {
				psmDBIds.addAll(proteinBean.getPSMDBIds());
			}
		}
		return psmDBIds;
	}

	/**
	 * @param psmIds the psmIds to set
	 */
	public void setPsmIds(Set<Integer> psmIds) {
		psmDBIds = psmIds;
	}

	public String getAlternativeNamesString() {
		final StringBuilder sb = new StringBuilder();
		final Set<String> set = new HashSet<String>();
		for (final ProteinBean proteinBean : this) {
			final String alternativeNames = proteinBean.getAlternativeNamesString();
			if (!set.contains(alternativeNames)) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(alternativeNames);
				set.add(alternativeNames);
			}
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(ProteinBean e) {
		if (!contains(e)) {
			// add peptides
			final List<PeptideBean> peptides2 = e.getPeptides();
			if (peptides2 != null) {
				for (final PeptideBean peptideBean : peptides2) {
					addPeptide(peptideBean);
				}
			}
			return super.add(e);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends ProteinBean> c) {
		if (c != null) {
			for (final ProteinBean proteinBean : c) {
				this.add(proteinBean);
			}
			return true;
		}
		return false;
	}

	public String getSecondaryAccessionsString() {
		final List<String> list = new ArrayList<String>();
		final StringBuilder sb = new StringBuilder();
		for (final ProteinBean proteinBean : this) {
			final Set<AccessionBean> secondaryAccessions2 = proteinBean.getSecondaryAccessions();
			if (secondaryAccessions2 != null) {
				for (final AccessionBean accessionBean : secondaryAccessions2) {
					final String accession = accessionBean.getAccession();
					if (!list.contains(accession)) {
						list.add(accession);
					}
				}
			}

		}
		Collections.sort(list);
		for (final String acc : list) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(acc);
		}
		return sb.toString();
	}

	@Override
	public String getId() {
		final Set<Integer> dbIds = getDbIds();
		final List<Integer> list = new ArrayList<Integer>();
		list.addAll(dbIds);
		Collections.sort(list);
		final StringBuilder sb = new StringBuilder();
		for (final Integer integer : list) {
			sb.append(integer);
		}
		return sb.toString();
	}

	/**
	 * Returns true if any of the proteins belonging to that protein group belongs
	 * to the provided project
	 *
	 * @param projectTag
	 * @return
	 */
	public boolean isFromThisProject(String projectTag) {
		for (final ProteinBean proteinBean : this) {
			if (proteinBean.isFromThisProject(projectTag))
				return true;
		}
		return false;
	}

	@Override
	public Map<ExperimentalConditionBean, Set<Integer>> getPSMDBIdsByCondition() {
		final Map<ExperimentalConditionBean, Set<Integer>> ret = new HashMap<ExperimentalConditionBean, Set<Integer>>();
		for (final ProteinBean proteinBean : this) {
			final Map<ExperimentalConditionBean, Set<Integer>> psmIdsByConditionID = proteinBean
					.getPSMDBIdsByCondition();
			for (final ExperimentalConditionBean condition : psmIdsByConditionID.keySet()) {
				if (ret.containsKey(condition)) {
					ret.get(condition).addAll(psmIdsByConditionID.get(condition));
				} else {
					final Set<Integer> set = new HashSet<Integer>();
					set.addAll(psmIdsByConditionID.get(condition));
					ret.put(condition, set);
				}
			}
		}
		return ret;
	}

	@Override
	public List<PSMBean> getPsms() {
		final List<PSMBean> ret = new ArrayList<PSMBean>();
		for (final ProteinBean proteinBean : this) {
			final List<PSMBean> psms = proteinBean.getPsms();
			for (final PSMBean psmBean : psms) {
				if (!ret.contains(psmBean))
					ret.add(psmBean);
			}
		}
		return ret;
	}

	/**
	 * Creates a light version of {@link ProteinGroupBean} without any list of
	 * {@link PSMBean} or psmIds
	 *
	 * @return
	 */
	public ProteinGroupBean cloneToLightProteinGroupBean() {
		if (lightVersion == null) {
			lightVersion = new ProteinGroupBean();
			final Set<String> sequences = new HashSet<String>();
			final Set<Integer> psmIds = new HashSet<Integer>();
			for (final ProteinBean proteinBean : this) {
				lightVersion.add(proteinBean.cloneToLightProteinBean());
				sequences.addAll(proteinBean.getDifferentSequences());
				psmIds.addAll(proteinBean.getDbIds());
			}
			for (final PeptideBean peptide : getPeptides()) {
				lightVersion.addPeptide(peptide.cloneToLightPeptideBean());
			}
			// ret.psmDBIds.addAll(getPSMDBIds());
			// lightVersion.setNumPeptides(sequences.size());
			lightVersion.setNumPSMs(getPSMDBIds().size());
			lightVersion.getNumPSMsByCondition().putAll(getNumPSMsByCondition());
			// create the amounts for each condition:
			final Map<ExperimentalConditionBean, Set<Integer>> psmdbIdsByCondition = new HashMap<ExperimentalConditionBean, Set<Integer>>();
			for (final ProteinBean proteinBean : this) {
				final Map<ExperimentalConditionBean, Set<Integer>> psmdbIdsByCondition2 = proteinBean
						.getPSMDBIdsByCondition();
				for (final ExperimentalConditionBean condition : psmdbIdsByCondition2.keySet()) {
					if (psmdbIdsByCondition.containsKey(condition)) {
						psmdbIdsByCondition.get(condition).addAll(psmdbIdsByCondition2.get(condition));
					} else {
						final Set<Integer> set = new HashSet<Integer>();
						set.addAll(psmdbIdsByCondition2.get(condition));
						psmdbIdsByCondition.put(condition, set);
					}
				}
			}
			for (final ExperimentalConditionBean condition : psmdbIdsByCondition.keySet()) {
				final double spc = Double.valueOf(psmdbIdsByCondition.get(condition).size());
				final AmountBean amount = new AmountBean();
				amount.setAmountType(AmountType.SPC);
				amount.setComposed(false);
				amount.setExperimentalCondition(condition);
				amount.setValue(spc);
				lightVersion.amounts.add(amount);
			}
			lightVersion.ratioDistributions = getRatioDistributions();
			// set the light version of the light version to itself
			lightVersion.lightVersion = lightVersion;
		}
		return lightVersion;
	}

	@Override
	public List<ScoreBean> getRatioScoresByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName) {
		final List<RatioBean> ratiosByConditions = getRatiosByConditions(condition1Name, condition2Name, projectTag,
				ratioName, false);
		final List<ScoreBean> ratioScores = SharedDataUtil.getRatioScoreValues(condition1Name, condition2Name,
				ratiosByConditions, ratioScoreName);
		return ratioScores;
	}

	@Override
	public Set<ExperimentalConditionBean> getConditions() {
		final Set<ExperimentalConditionBean> conditions = new HashSet<ExperimentalConditionBean>();
		for (final ProteinBean proteinBean : this) {
			conditions.addAll(proteinBean.getConditions());
		}
		return conditions;
	}

	public String getConditionsString() {
		return SharedDataUtil.getConditionString(this);
	}

	@Override
	public List<PeptideBean> getPeptides() {
		return peptides;
	}

	public void addPeptide(PeptideBean peptideBean) {
		if (peptides.contains(peptideBean)) {
			return;
		}
		peptides.add(peptideBean);
	}

	public void setPeptides(List<PeptideBean> peptides2) {
		peptides = peptides2;

	}

	/**
	 * @param amounts the amounts to set
	 */
	public void setAmounts(Set<AmountBean> amounts) {
		this.amounts = amounts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPrimaryAccessionsString();
	}

	public Iterator<ProteinBean> getIterator(Comparator<ProteinBean> comparator) {
		final List<ProteinBean> list = new ArrayList<ProteinBean>();
		list.addAll(this);
		Collections.sort(list, comparator);
		return list.iterator();
	}

	@Override
	public Map<String, RatioDistribution> getRatioDistributions() {
		if (ratioDistributions == null) {
			ratioDistributions = new HashMap<String, RatioDistribution>();
		}
		return ratioDistributions;
	}

	@Override
	public void addRatioDistribution(RatioDistribution ratioDistribution) {
		final Map<String, RatioDistribution> ratioDistributions2 = getRatioDistributions();
		if (!ratioDistributions2.containsKey(ratioDistribution.getRatioKey())) {
			ratioDistributions.put(ratioDistribution.getRatioKey(), ratioDistribution);
		}
	}

	@Override
	public RatioDistribution getRatioDistribution(RatioBean ratio) {
		return ratioDistributions.get(SharedDataUtil.getRatioKey(ratio));

	}

	@Override
	public Set<RatioBean> getRatios() {
		final Set<RatioBean> ret = new HashSet<RatioBean>();
		for (final ProteinBean protein : this) {
			ret.addAll(protein.getRatios());
		}
		return ret;
	}

	@Override
	public Set<Integer> getPeptideDBIds() {
		return peptideDBIds;
	}

	@Override
	public int getNumPSMsByCondition(String projectTag, String conditionName) {
		final Map<ExperimentalConditionBean, Integer> numPSMsByCondition = getNumPSMsByCondition();
		if (!numPSMsByCondition.isEmpty()) {
			for (final ExperimentalConditionBean conditionBean : numPSMsByCondition.keySet()) {
				if (conditionBean.getId().equals(conditionName)) {
					if (conditionBean.getProject().getTag().equals(projectTag)) {
						return numPSMsByCondition.get(conditionBean);
					}
				}
			}
		}
		return 0;
	}

	@Override
	public Map<ExperimentalConditionBean, Integer> getNumPSMsByCondition() {
		if (numPSMsByCondition == null) {
			numPSMsByCondition = new HashMap<ExperimentalConditionBean, Integer>();
			final Map<ExperimentalConditionBean, Set<Integer>> psmdbIdsByCondition = getPSMDBIdsByCondition();
			for (final ExperimentalConditionBean conditionBean : psmdbIdsByCondition.keySet()) {
				numPSMsByCondition.put(conditionBean, psmdbIdsByCondition.get(conditionBean).size());
			}
		}
		return numPSMsByCondition;
	}

}
