package edu.scripps.yates.shared.model.light;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.PeptideRelation;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.interfaces.ContainsAmounts;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.interfaces.ContainsScores;
import edu.scripps.yates.shared.model.interfaces.ContainsSequence;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class PeptideBeanLight implements Comparable<PeptideBeanLight>, Serializable, ContainsRatios, ContainsAmounts,
		ContainsPrimaryAccessions, ContainsLightPSMs, ContainsSequence, ContainsScores {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	/**
	 *
	 */
	private static final long serialVersionUID = -1713998292042049510L;
	private String sequence;
	private String fullSequence;

	private int length;
	private Map<String, ScoreBean> scores = new HashMap<String, ScoreBean>();
	private List<AmountBean> amounts = new ArrayList<AmountBean>();
	private HashMap<String, List<AmountBean>> amountsByExperimentalCondition = new HashMap<String, List<AmountBean>>();
	private HashMap<String, List<RatioBean>> ratiosByExperimentalcondition = new HashMap<String, List<RatioBean>>();
	private Set<ProteinBeanLight> proteins = new HashSet<ProteinBeanLight>();
	private List<PSMBeanLight> psms = new ArrayList<PSMBeanLight>();

	private Set<RatioBean> ratios = new HashSet<RatioBean>();
	private List<AccessionBean> proteinsPrimaryAccessions = new ArrayList<AccessionBean>();
	private String proteinAccessionString;
	private String proteinDescriptionString;
	private Map<String, List<Pair<Integer, Integer>>> startingPositions = new HashMap<String, List<Pair<Integer, Integer>>>();
	private Set<OrganismBean> organisms = new HashSet<OrganismBean>();
	private Set<String> rawSequences = new HashSet<String>();
	private int peptideBeanUniqueIdentifier;
	private int numPSMs;
	private PeptideRelation relation;
	private Map<String, RatioDistribution> ratioDistributions;
	private List<PTMBean> ptms = new ArrayList<PTMBean>();
	private String ptmString;
	private String ptmScoreString;
	private Map<ExperimentalConditionBean, Integer> numPSMsByCondition;
	private String organismString;
	private String extendedStartingPositionsString;
	private String conditionsString;
	private static int uniqueIdentifier = 0;

	public PeptideBeanLight() {
		peptideBeanUniqueIdentifier = uniqueIdentifier++;
	}

	/**
	 * @return the peptideBeanUniqueIdentifier
	 */
	public int getPeptideBeanUniqueIdentifier() {
		return peptideBeanUniqueIdentifier;
	}

	public void setProteins(Set<ProteinBeanLight> proteins) {
		this.proteins = proteins;
	}

	/**
	 * @param fullSequence the fullSequence to set
	 */
	public void setFullSequence(String fullSequence) {
		this.fullSequence = fullSequence;
	}

	public String getFullSequence() {
		return fullSequence;
	}

	@Override
	public int compareTo(PeptideBeanLight peptideBean) {
		return getSequence().compareTo(peptideBean.getSequence());
	}

	public void addPSMToPeptide(PSMBeanLight psm) {
		if (psm == null || psms.contains(psm)) {
			return;
		}
		psms.add(psm);

		rawSequences.add(psm.getFullSequence());
		// peptide
		psm.setPeptideBeanToPSM(this);
		// relation
		relation = psm.getRelation();

		// set Proteins
		final Set<ProteinBeanLight> proteins2 = psm.getProteins();
		for (final ProteinBeanLight proteinBean : proteins2) {
			addProteinToPeptide(proteinBean);
		}

		// organisms
		for (final OrganismBean organismBean : psm.getOrganisms()) {
			addOrganism(organismBean);
		}
		// add all the proteins to the psm
		final Set<ProteinBeanLight> proteins3 = getProteins();
		for (final ProteinBeanLight proteinBean : proteins3) {
			psm.addProteinToPSM(proteinBean);
		}
	}

	@Override
	public List<PSMBeanLight> getPsms() {
		return psms;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * Gets a Map of {@link ScoreBean} by score name
	 *
	 * @return
	 */
	@Override
	public Map<String, ScoreBean> getScores() {
		return scores;
	}

	@Override
	public void setScores(Map<String, ScoreBean> scores) {
		this.scores = scores;
	}

	@Override
	public void addScore(ScoreBean score) {
		if (scores == null)
			scores = new HashMap<String, ScoreBean>();
		scores.put(score.getScoreName(), score);
	}

	public void addOrganism(OrganismBean organism) {
		organisms.add(organism);
	}

	@Override
	public List<AmountBean> getAmounts() {
		return amounts;
	}

	public void setAmounts(List<AmountBean> amounts) {
		this.amounts = amounts;
	}

	/**
	 * @return the proteinAmountsByExperimentalCondition
	 */
	@Override
	public HashMap<String, List<AmountBean>> getAmountsByExperimentalCondition() {
		return amountsByExperimentalCondition;
	}

	/**
	 * @param proteinAmountsByExperimentalCondition the
	 *                                              proteinAmountsByExperimentalCondition
	 *                                              to set
	 */
	public void setAmountsByExperimentalCondition(
			HashMap<String, List<AmountBean>> proteinAmountsByExperimentalCondition) {
		amountsByExperimentalCondition = proteinAmountsByExperimentalCondition;
	}

	public void addAmount(AmountBean psmAmount) {
		amounts.add(psmAmount);

		// index by experiment condition
		addtoMap(amountsByExperimentalCondition, psmAmount);
	}

	private void addtoMap(HashMap<String, List<AmountBean>> map, AmountBean amount) {
		final String key = amount.getExperimentalCondition().getId();
		if (map.containsKey(key)) {
			map.get(key).add(amount);
		} else {
			final List<AmountBean> set = new ArrayList<AmountBean>();
			set.add(amount);
			map.put(key, set);
		}
	}

	public String getProteinAccessionString() {
		if (proteinAccessionString == null) {
			final StringBuilder sb = new StringBuilder();
			for (final AccessionBean acc : getProteinsPrimaryAccessions()) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(acc.getAccession());
			}
			proteinAccessionString = sb.toString();
		}
		return proteinAccessionString;
	}

	public boolean addProteinToPeptide(ProteinBeanLight proteinBeanLight) {

		if (proteinBeanLight == null || proteins.contains(proteinBeanLight)) {
			return false;
		}
		proteins.add(proteinBeanLight);
		if (!proteinsPrimaryAccessions.contains(proteinBeanLight.getPrimaryAccession())) {
			proteinsPrimaryAccessions.add(proteinBeanLight.getPrimaryAccession());
		}
		proteinBeanLight.addPeptideToProtein(this);
		// final List<PSMBean> psms2 = protein.getPsms();
		// for (PSMBean psmBean : psms2) {
		// addPSMToPeptide(psmBean);
		// }
		// final List<PSMBean> psms3 = getPsms();
		// for (PSMBean psmBean : psms3) {
		// protein.addPSMtoProtein(psmBean);
		// }
		return true;
	}

	public Set<ProteinBeanLight> getProteins() {
		return proteins;
		// return Collections.emptySet();
	}

	public String getProteinDescriptionString() {
		if (proteinDescriptionString == null) {
			final StringBuilder sb = new StringBuilder();
			for (final ProteinBeanLight proteinBean : proteins) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(proteinBean.getDescriptionString());
			}
			proteinDescriptionString = sb.toString();
		}
		return proteinDescriptionString;
	}

	@Override
	public ScoreBean getScoreByName(String scoreName) {
		return scores.get(scoreName);
	}

	public void addRatio(RatioBean ratio) {
		ratios.add(ratio);
		addtoMap(ratio);
	}

	private void addtoMap(RatioBean ratioBean) {
		final ExperimentalConditionBean condition1 = ratioBean.getCondition1();
		final ExperimentalConditionBean condition2 = ratioBean.getCondition2();
		if (ratiosByExperimentalcondition.containsKey(condition1.getId())) {
			ratiosByExperimentalcondition.get(condition1.getId()).add(ratioBean);
		} else {
			final List<RatioBean> set = new ArrayList<RatioBean>();
			set.add(ratioBean);
			ratiosByExperimentalcondition.put(condition1.getId(), set);
		}
		if (ratiosByExperimentalcondition.containsKey(condition2.getId())) {
			ratiosByExperimentalcondition.get(condition2.getId()).add(ratioBean);
		} else {
			final List<RatioBean> set = new ArrayList<RatioBean>();
			set.add(ratioBean);
			ratiosByExperimentalcondition.put(condition2.getId(), set);
		}
	}

	/**
	 * @return the ratios
	 */
	@Override
	public Set<RatioBean> getRatios() {
		return ratios;
	}

	/**
	 * @param ratios the ratios to set
	 */
	public void setRatios(Set<RatioBean> ratios) {
		this.ratios = ratios;
	}

	/**
	 * Gets a alphabetically sorted list of accessions
	 *
	 * @return the proteinsPrimaryAccessions
	 */
	public List<AccessionBean> getProteinsPrimaryAccessions() {
		Collections.sort(proteinsPrimaryAccessions, SharedDataUtil.getComparatorByAccession());
		return proteinsPrimaryAccessions;
	}

	/**
	 * @param proteinsPrimaryAccessions the proteinsPrimaryAccessions to set
	 */
	public void setProteinsPrimaryAccessions(List<AccessionBean> proteinsPrimaryAccessions) {
		this.proteinsPrimaryAccessions = proteinsPrimaryAccessions;
	}

	/**
	 * @return the ratiosByExperimentalcondition
	 */
	public HashMap<String, List<RatioBean>> getRatiosByExperimentalcondition() {
		return ratiosByExperimentalcondition;
	}

	/**
	 * @param ratiosByExperimentalcondition the ratiosByExperimentalcondition to set
	 */
	public void setRatiosByExperimentalcondition(HashMap<String, List<RatioBean>> ratiosByExperimentalcondition) {
		this.ratiosByExperimentalcondition = ratiosByExperimentalcondition;
	}

	/**
	 * Gets the ratios available between these two conditions. No matter the order
	 * of the comparison. It assures that not repeated ratios are in the result.
	 *
	 * @param conditionName1
	 * @param conditionName2
	 * @return
	 */
	@Override
	public List<RatioBean> getRatiosByConditions(String conditionName1, String conditionName2, String projectTag,
			String ratioName, boolean skipInfinities) {
		return SharedDataUtil.getRatiosByConditions(getRatios(), conditionName1, conditionName2, projectTag, ratioName,
				skipInfinities);
	}

	/**
	 * Gets a string with the amount types
	 *
	 * @param conditionName
	 * @param projectTag
	 * @return
	 */
	public String getAmountTypeString(String conditionName, String projectTag) {

		final Set<AmountType> amountTypes = new HashSet<AmountType>();
		final List<AmountBean> amounts2 = getAmounts();
		for (final AmountBean amountBean : amounts2) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {

					amountTypes.add(amountBean.getAmountType());

				}
			}
		}

		final StringBuilder sb = new StringBuilder();
		for (final AmountType amountType : amountTypes) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(amountType.getDescription());
		}
		return sb.toString();
	}

	@Override
	public boolean hasCombinationAmounts(String conditionName, String projectTag) {

		final List<AmountBean> amounts2 = getAmounts();
		for (final AmountBean amountBean : amounts2) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
					if (amountBean.isComposed())
						return true;
				}
			}
		}

		return false;
	}

	@Override
	public List<AmountBean> getCombinationAmount(String conditionName, String projectTag) {
		final List<AmountBean> ret = new ArrayList<AmountBean>();

		final List<AmountBean> amounts2 = getAmounts();
		for (final AmountBean amountBean : amounts2) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
					if (amountBean.isComposed())
						ret.add(amountBean);
				}
			}
		}

		return ret;
	}

	@Override
	public List<AmountBean> getNonCombinationAmounts(String conditionName, String projectTag) {
		final List<AmountBean> ret = new ArrayList<AmountBean>();
		final List<AmountBean> amounts2 = getAmounts();
		for (final AmountBean amountBean : amounts2) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
					if (!amountBean.isComposed())
						ret.add(amountBean);
				}
			}
		}
		return ret;
	}

	@Override
	public List<AccessionBean> getPrimaryAccessions() {
		return getProteinsPrimaryAccessions();
	}

	/**
	 * Gets the starting positions of the peptide sequence depending on the protein.
	 * The first AA of the protein is the position 1.
	 *
	 * @return the startingPositions is a {@link HashMap} where the key is the
	 *         protein accession and the value is the positions in which the peptide
	 *         can be in the protein
	 */
	public Map<String, List<Pair<Integer, Integer>>> getStartingPositions() {
		return startingPositions;
	}

	/**
	 * @param startingPositions the startingPositions to set
	 */
	public void setStartingPositions(Map<String, List<Pair<Integer, Integer>>> startingPositions) {
		this.startingPositions = startingPositions;
	}

	public String getStartingPositionsString() {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = getStartingPositions();
		final StringBuilder sb = new StringBuilder();

		if (startingPositions != null) {
			int minPosition = Integer.MAX_VALUE;
			final List<Integer> list = new ArrayList<Integer>();
			for (final List<Pair<Integer, Integer>> positions : startingPositions.values()) {
				for (final Pair<Integer, Integer> startAndEnd : positions) {
					final int position = startAndEnd.getFirstElement();
					if (!list.contains(position))
						list.add(position);
					if (position < minPosition)
						minPosition = position;
				}
			}
			if (list.size() == 1) {
				sb.append(list.get(0));
			} else {
				if (minPosition != Integer.MAX_VALUE)
					sb.append(minPosition + "*");
			}
		}
		return sb.toString();
	}

	public String getExtendedStartingPositionsString() {
		return extendedStartingPositionsString;

	}

	public void setExtendedStartingPositionsString(String extendedStartingPositionsString) {
		this.extendedStartingPositionsString = extendedStartingPositionsString;
	}

	public Set<OrganismBean> getOrganisms() {
		if (organisms == null)
			organisms = new HashSet<OrganismBean>();
		return organisms;
	}

	public String getOrganismsString() {
		return organismString;
	}

	public String getOrganismString() {
		return organismString;
	}

	public void setOrganismString(String organismString) {
		this.organismString = organismString;
	}

	public String getPtmString() {
		return ptmString;
	}

	public String getPtmScoreString() {
		return ptmScoreString;
	}

	public void setProteinAccessionString(String proteinAccessionString) {
		this.proteinAccessionString = proteinAccessionString;
	}

	public void setProteinDescriptionString(String proteinDescriptionString) {
		this.proteinDescriptionString = proteinDescriptionString;
	}

	/**
	 * @param organisms the organisms to set
	 */
	public void setOrganisms(Set<OrganismBean> organisms) {
		this.organisms = organisms;
	}

	/**
	 * Returns true if the {@link PsmBean} has been identified in the provided
	 * projects, which means that at least one {@link AmountBean} or one
	 * {@link RatioBean} points to that project
	 *
	 * @param projectTag
	 * @return
	 */
	public boolean isFromThisProject(String projectTag) {
		if (projectTag != null) {
			if (amounts != null) {
				for (final AmountBean amount : amounts) {
					if (amount.getExperimentalCondition() != null) {
						if (amount.getExperimentalCondition().getProject() != null) {
							if (amount.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
								return true;
							}
						}
					}
				}
			}
			if (ratios != null) {
				for (final RatioBean ratio : ratios) {
					if (ratio.getRatioDescriptorBean() != null) {
						if (projectTag.equals(ratio.getRatioDescriptorBean().getProjectTag())) {
							return true;
						}
					}
				}
			}
		}
		return false;
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

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param psms the psms to set
	 */
	public void setPsms(List<PSMBeanLight> psms) {
		this.psms = psms;
	}

	/**
	 * @param peptideBeanUniqueIdentifier the peptideBeanUniqueIdentifier to set
	 */
	public void setPeptideBeanUniqueIdentifier(int peptideBeanUniqueIdentifier) {
		this.peptideBeanUniqueIdentifier = peptideBeanUniqueIdentifier;
	}

	/**
	 * @return the rawSequences
	 */
	public Set<String> getRawSequences() {
		return rawSequences;
	}

	/**
	 * @param rawSequences the rawSequences to set
	 */
	public void setRawSequences(Set<String> rawSequences) {
		this.rawSequences = rawSequences;
	}

	/**
	 * @return the numPSMs
	 */
	@Override
	public int getNumPSMs() {
		return numPSMs;
	}

	/**
	 * @param numPSMs the numPSMs to set
	 */
	public void setNumPSMs(int numPSMs) {
		this.numPSMs = numPSMs;
	}

	@Override
	public String toString() {
		return sequence + "(" + getPeptideBeanUniqueIdentifier() + "-" + hashCode() + ")";
	}

	/**
	 * @return the relation
	 */
	public PeptideRelation getRelation() {
		return relation;
	}

	/**
	 * @param relation the relation to set
	 */
	public void setRelation(PeptideRelation relation) {
		this.relation = relation;
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
		throw new IllegalArgumentException("Not supported in PeptideBeanLigth. use setter instead");
	}

	@Override
	public RatioDistribution getRatioDistribution(RatioBean ratio) {
		return getRatioDistributions().get(SharedDataUtil.getRatioKey(ratio));

	}

	@Override
	public int getNumPSMsByCondition(String projectTag, String conditionName) {
		if (!getNumPSMsByCondition().isEmpty()) {
			for (final ExperimentalConditionBean conditionBean : getNumPSMsByCondition().keySet()) {
				if (conditionBean.getId().equals(conditionName)) {
					if (conditionBean.getProject().getTag().equals(projectTag)) {
						return getNumPSMsByCondition().get(conditionBean);
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

	public List<PTMBean> getPtms() {
		return ptms;
	}

	/**
	 * @param ptms the ptms to set
	 */
	public void setPtms(List<PTMBean> ptms) {
		this.ptms = ptms;
	}

	public void addPtm(PTMBean ptm) {
		if (ptms == null)
			ptms = new ArrayList<PTMBean>();
		// look the positions in which a ptm is already there, just in case, to
		// avoid duplicates
		final Set<Integer> positions = new HashSet<Integer>();
		for (final PTMBean ptmBean : ptms) {
			for (final PTMSiteBean ptmSite : ptmBean.getPtmSites()) {
				positions.add(ptmSite.getPosition());
			}
		}
		if (!ptms.contains(ptm)) {
			final List<PTMSiteBean> ptmSites = ptm.getPtmSites();
			final Iterator<PTMSiteBean> iterator = ptmSites.iterator();
			while (iterator.hasNext()) {
				final PTMSiteBean ptmSite = iterator.next();
				if (positions.contains(ptmSite.getPosition())) {
					iterator.remove();
				}
			}
			if (!ptm.getPtmSites().isEmpty()) {
				ptms.add(ptm);
			}
		}
	}

	/**
	 * @param ptmString the ptmString to set
	 */
	public void setPtmString(String ptmString) {
		this.ptmString = ptmString;
	}

	/**
	 * @param ptmScoreString the ptmScoreString to set
	 */
	public void setPtmScoreString(String ptmScoreString) {
		this.ptmScoreString = ptmScoreString;
	}

	public String getPTMString() {
		return ptmString;
	}

	public String getPTMScoreString() {
//		if (ptmScoreString == null) {
//
//			ptmScoreString = SharedDataUtil.getPTMScoreString(ptms);
//		}
		return ptmScoreString;
	}

	public String getPTMScoreString(String ptmScoreName) {
//		if (ptmScoreString == null) {
//			ptmScoreString = SharedDataUtil.getPTMScoreString(ptmScoreName, ptms);
//		}
		return ptmScoreString;
	}

	public void setRatioDistributions(Map<String, RatioDistribution> ratioDistributions) {
		this.ratioDistributions = ratioDistributions;
	}

	public void setNumPSMsByCondition(Map<ExperimentalConditionBean, Integer> numPSMsByCondition2) {
		this.numPSMsByCondition = numPSMsByCondition2;
	}
}
