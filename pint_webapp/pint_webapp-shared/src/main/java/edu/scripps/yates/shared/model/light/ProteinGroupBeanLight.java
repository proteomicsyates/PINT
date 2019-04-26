package edu.scripps.yates.shared.model.light;

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

import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.GeneBean;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.interfaces.ContainsAmounts;
import edu.scripps.yates.shared.model.interfaces.ContainsGenes;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPeptides;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class ProteinGroupBeanLight extends ArrayList<ProteinBeanLight> implements Serializable, ContainsRatios,
		ContainsAmounts, ContainsGenes, ContainsPrimaryAccessions, ContainsLightPeptides, ContainsLightPSMs {

	/**
	 *
	 */
	private static final long serialVersionUID = -2919705603019167262L;

	private int numPeptides;

	private int numPSMs;

	private List<PeptideBeanLight> peptides = new ArrayList<PeptideBeanLight>();

	// used only for the SPCs:
	private List<AmountBean> amounts = new ArrayList<AmountBean>();

	private Map<String, RatioDistribution> ratioDistributions;

	private Map<ExperimentalConditionBean, Integer> numPSMsByCondition;

	private String conditionsString;

	private List<AccessionBean> differentPrimaryAccessions;

	private String groupMemberEvidences;

	private String groupMemberExistences;

	private String organismsString;

	private String alternativeNamesString;

	private String secondaryAccessionsString;

	private List<AccessionBean> primaryAccessions;

	private String primaryAccessionsString;

	private String descriptionsString;

	private HashMap<String, List<AmountBean>> amountsByExperimentalCondition;

	public ProteinGroupBeanLight() {

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
		for (final ProteinBeanLight protein : this) {
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
		for (final ProteinBeanLight protein : this) {
			sb.append(protein.getProteinDBString());
		}
		return sb.toString();
	}

	@Override
	public int getNumPSMs() {
		if (numPSMs == 0) {
			for (final PeptideBeanLight peptide : getPeptides()) {
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
		return primaryAccessions;
	}

	public void setPrimaryAccessions(List<AccessionBean> primaryAccessions) {
		this.primaryAccessions = primaryAccessions;
	}

	public String getPrimaryAccessionsString() {
		return primaryAccessionsString;
	}

	public void setPrimaryAccessionsString(String primaryAccessionsString) {
		this.primaryAccessionsString = primaryAccessionsString;
	}

	public String getDescriptionsString() {
		return descriptionsString;
	}

	public void setDescriptionsString(String descriptionsString) {
		this.descriptionsString = descriptionsString;
	}

	@Override
	public List<AmountBean> getAmounts() {
		return amounts;
	}

	@Override
	public boolean hasCombinationAmounts(String conditionName, String projectTag) {
		for (final ProteinBeanLight protein : this) {
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
		return amountsByExperimentalCondition;
	}

	public void setAmountsByExperimentalCondition(HashMap<String, List<AmountBean>> amountsByExperimentalCondition) {
		this.amountsByExperimentalCondition = amountsByExperimentalCondition;
	}

	@Override
	public List<RatioBean> getRatiosByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, boolean skipInfinities) {
		final List<RatioBean> ret = new ArrayList<RatioBean>();
		for (final ProteinBeanLight protein : this) {
			ret.addAll(protein.getRatios());
		}
		return SharedDataUtil.getRatiosByConditions(ret, condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);

	}

	public List<AccessionBean> getSecondaryAccessions() {
		final List<AccessionBean> ret = new ArrayList<AccessionBean>();
		for (final ProteinBeanLight protein : this) {
			ret.addAll(protein.getSecondaryAccessions());
		}
		return ret;
	}

	public List<AccessionBean> getDifferentPrimaryAccessions() {
		return differentPrimaryAccessions;
	}

	public void setDifferentPrimaryAccessions(List<AccessionBean> differentPrimaryAccessions) {
		this.differentPrimaryAccessions = differentPrimaryAccessions;
	}

	public String getGroupMemberEvidences() {
		return groupMemberEvidences;
	}

	public void setGroupMemberEvidences(String groupMemberEvidences) {
		this.groupMemberEvidences = groupMemberEvidences;
	}

	public String getGroupMemberExistences() {
		return groupMemberExistences;
	}

	public void setGroupMemberExistences(String groupMemberExistences) {
		this.groupMemberExistences = groupMemberExistences;
	}

	public String getOrganismsString() {
		return organismsString;

	}

	public void setOrganismsString(String organismsString) {
		this.organismsString = organismsString;
	}

	public String getAlternativeNamesString() {
		return alternativeNamesString;
	}

	public void setAlternativeNamesString(String alternativeNamesString) {
		this.alternativeNamesString = alternativeNamesString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	@Override
	public boolean add(ProteinBeanLight e) {
		if (!contains(e)) {
			// add peptides
			final List<PeptideBeanLight> peptides2 = e.getPeptides();
			if (peptides2 != null) {
				for (final PeptideBeanLight peptideBean : peptides2) {
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
	public boolean addAll(Collection<? extends ProteinBeanLight> c) {
		if (c != null) {
			for (final ProteinBeanLight proteinBean : c) {
				this.add(proteinBean);
			}
			return true;
		}
		return false;
	}

	public String getSecondaryAccessionsString() {
		return secondaryAccessionsString;
	}

	public void setSecondaryAccessionsString(String secondaryAccessionsString) {
		this.secondaryAccessionsString = secondaryAccessionsString;
	}

	/**
	 * Returns true if any of the proteins belonging to that protein group belongs
	 * to the provided project
	 *
	 * @param projectTag
	 * @return
	 */
	public boolean isFromThisProject(String projectTag) {
		for (final ProteinBeanLight proteinBean : this) {
			if (proteinBean.isFromThisProject(projectTag))
				return true;
		}
		return false;
	}

	@Override
	public List<PSMBeanLight> getPsms() {
		throw new IllegalArgumentException("Not used in ProteinGroupBeanLight");
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

	public String getConditionsString() {
		return conditionsString;
	}

	public void setConditionsString(String conditionsString) {
		this.conditionsString = conditionsString;
	}

	@Override
	public List<PeptideBeanLight> getPeptides() {
		return peptides;
	}

	public void addPeptide(PeptideBeanLight peptideBean) {
		if (peptides.contains(peptideBean)) {
			return;
		}
		peptides.add(peptideBean);
	}

	public void setPeptides(List<PeptideBeanLight> peptides2) {
		peptides = peptides2;

	}

	/**
	 * @param amounts the amounts to set
	 */
	public void setAmounts(List<AmountBean> amounts) {
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

	public Iterator<ProteinBeanLight> getIterator(Comparator<ProteinBeanLight> comparator) {
		final List<ProteinBeanLight> list = new ArrayList<ProteinBeanLight>();
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
		for (final ProteinBeanLight protein : this) {
			ret.addAll(protein.getRatios());
		}
		return ret;
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
		return numPSMsByCondition;
	}

	public void setNumPSMsByCondition(Map<ExperimentalConditionBean, Integer> numPSMsByCondition) {
		this.numPSMsByCondition = numPSMsByCondition;
	}

	public void setRatioDistributions(Map<String, RatioDistribution> ratioDistributions2) {
		this.ratioDistributions = ratioDistributions2;
	}

}
