package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
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
import edu.scripps.yates.shared.model.interfaces.ContainsId;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.interfaces.ContainsScores;
import edu.scripps.yates.shared.model.interfaces.ContainsSequence;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class PeptideBean implements Comparable<PeptideBean>, Serializable, ContainsRatios, ContainsAmounts, ContainsId,
		ContainsConditions, ContainsPrimaryAccessions, ContainsPSMs, ContainsSequence, ContainsScores {
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	/**
	 *
	 */
	private static final long serialVersionUID = -1713998292042049510L;
	private Set<MSRunBean> msRuns = new HashSet<MSRunBean>();
	private String sequence;
	private String fullSequence;

	private int length;
	private Double calculatedMH;
	private Map<String, ScoreBean> scores = new HashMap<String, ScoreBean>();
	private List<AmountBean> amounts = new ArrayList<AmountBean>();
	private HashMap<String, List<AmountBean>> amountsByExperimentalCondition = new HashMap<String, List<AmountBean>>();
	private HashMap<String, List<RatioBean>> ratiosByExperimentalcondition = new HashMap<String, List<RatioBean>>();
	private Set<ProteinBean> proteins = new HashSet<ProteinBean>();
	private List<PSMBean> psms = new ArrayList<PSMBean>();

	private Set<RatioBean> ratios = new HashSet<RatioBean>();
	private List<AccessionBean> proteinsPrimaryAccessions = new ArrayList<AccessionBean>();
	private String proteinAccessionString;
	private String proteinDescriptionString;
	private Map<String, List<Pair<Integer, Integer>>> startingPositions = new HashMap<String, List<Pair<Integer, Integer>>>();
	private Set<OrganismBean> organisms = new HashSet<OrganismBean>();
	private Set<Integer> proteinDBIds = new HashSet<Integer>();
	private Set<ExperimentalConditionBean> conditions = new HashSet<ExperimentalConditionBean>();
	private Set<Integer> dbIds = new HashSet<Integer>();
	private Map<ExperimentalConditionBean, Set<Integer>> psmIdsByCondition = new HashMap<ExperimentalConditionBean, Set<Integer>>();
	private Set<Integer> psmIds = new HashSet<Integer>();
	private Set<String> rawSequences = new HashSet<String>();
	private int peptideBeanUniqueIdentifier;
	private int numPSMs;
	private PeptideBeanLight lightVersion;

	private PeptideRelation relation;
	private Map<String, RatioDistribution> ratioDistributions;
	private List<PTMBean> ptms = new ArrayList<PTMBean>();
	private String ptmString;
	private String ptmScoreString;
	private Map<ExperimentalConditionBean, Integer> numPSMsByCondition;
	private static int uniqueIdentifier = 0;

	public PeptideBean() {
		peptideBeanUniqueIdentifier = uniqueIdentifier++;
	}

	/**
	 * @return the peptideBeanUniqueIdentifier
	 */
	public int getPeptideBeanUniqueIdentifier() {
		return peptideBeanUniqueIdentifier;
	}

	public void setProteins(Set<ProteinBean> proteins) {
		this.proteins = proteins;
	}

	/**
	 * @return the dbId
	 */
	public Set<Integer> getDbIds() {
		return dbIds;
	}

	public void addDbId(int dbId) {
		dbIds.add(dbId);
	}

	/**
	 * @param dbId the dbId to set
	 */
	public void setDbIds(Set<Integer> dbIds) {
		this.dbIds = dbIds;
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
	public int compareTo(PeptideBean peptideBean) {
		return getSequence().compareTo(peptideBean.getSequence());
	}

	public void addMSRun(MSRunBean msRun) {
		msRuns.add(msRun);
	}

	public Set<MSRunBean> getMSRuns() {
		return msRuns;
	}

	public void addPSMToPeptide(PSMBean psm) {
		if (psm == null || (psmIds.contains(psm.getDbID()) && !psms.isEmpty()) || psms.contains(psm)) {
			return;
		}
		psms.add(psm);
		psmIds.add(psm.getDbID());
		rawSequences.add(psm.getFullSequence());
		// peptide
		psm.setPeptideBeanToPSM(this);
		// relation
		relation = psm.getRelation();
		// msRUN
		addMSRun(psm.getMsRun());
		// set M+H
		setCalculatedMH(psm.getCalcMH());
		// map to conditions
		addPSMToMapByconditions(psm);
		// set Proteins
		final Set<ProteinBean> proteins2 = psm.getProteins();
		for (final ProteinBean proteinBean : proteins2) {
			addProteinToPeptide(proteinBean);
		}

		// organisms
		for (final OrganismBean organismBean : psm.getOrganisms()) {
			addOrganism(organismBean);
		}
		// add all the proteins to the psm
		final Set<ProteinBean> proteins3 = getProteins();
		for (final ProteinBean proteinBean : proteins3) {
			psm.addProteinToPSM(proteinBean);
		}
	}

	private void addPSMToMapByconditions(PSMBean psmBean) {
		// add to psm ids by conditions
		final Set<ExperimentalConditionBean> conditions2 = psmBean.getConditions();
		if (conditions2.isEmpty()) {
			return;
		}
		for (final ExperimentalConditionBean experimentalConditionBean : conditions2) {
			if (psmIdsByCondition.containsKey(experimentalConditionBean)) {
				psmIdsByCondition.get(experimentalConditionBean).add(psmBean.getDbID());
			} else {
				final Set<Integer> set = new HashSet<Integer>();
				set.add(psmBean.getDbID());
				psmIdsByCondition.put(experimentalConditionBean, set);
			}
		}
	}

	@Override
	public List<PSMBean> getPsms() {
		return psms;
	}

	public Double getCalcMH() {
		return calculatedMH;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	/**
	 * @return the calculatedMH
	 */
	public Double getCalculatedMH() {
		return calculatedMH;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * @param calculatedMH the calculatedMH to set
	 */
	public void setCalculatedMH(Double calculatedMH) {
		this.calculatedMH = calculatedMH;
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

	public boolean addProteinToPeptide(ProteinBean protein) {

		if (protein == null || proteins.contains(protein)) {
			return false;
		}
		proteins.add(protein);
		proteinDBIds.addAll(protein.getDbIds());
		if (!proteinsPrimaryAccessions.contains(protein.getPrimaryAccession())) {
			proteinsPrimaryAccessions.add(protein.getPrimaryAccession());
		}
		protein.addPeptideToProtein(this);
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

	public Set<ProteinBean> getProteins() {
		return proteins;
		// return Collections.emptySet();
	}

	public String getProteinDescriptionString() {
		if (proteinDescriptionString == null) {
			final StringBuilder sb = new StringBuilder();
			for (final ProteinBean proteinBean : proteins) {
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

	public void addPositionByProtein(String accession, Pair<Integer, Integer> positions) {
		if (startingPositions.containsKey(accession)) {
			startingPositions.get(accession).add(positions);
		} else {
			final List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
			list.add(positions);
			startingPositions.put(accession, list);
		}
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
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = getStartingPositions();
		final List<AccessionBean> primaryAccessions = getPrimaryAccessions();
		final StringBuilder sb = new StringBuilder();
		for (final AccessionBean accessionBean : primaryAccessions) {
			if (startingPositions != null && startingPositions.containsKey(accessionBean.getAccession())) {
				final List<Pair<Integer, Integer>> listTmp = startingPositions.get(accessionBean.getAccession());
				// to avoid duplicates:
				final List<Integer> list = new ArrayList<Integer>();
				for (final Pair<Integer, Integer> startAndEnd : listTmp) {
					final int position = startAndEnd.getFirstElement();
					if (!list.contains(position))
						list.add(position);
				}
				//

				if (list.size() == 1) {
					sb.append("Position in protein " + accessionBean.getAccession() + ": " + list.get(0));
				} else {
					sb.append("Positions in protein " + accessionBean.getAccession() + ": ");
					for (int i = 0; i < list.size(); i++) {
						final Integer position = list.get(i);

						sb.append(position);
						if (i != list.size() - 1)
							sb.append(", ");

					}
				}
			} else {
				sb.append("Position in protein " + accessionBean.getAccession() + " not available");
			}
			sb.append(SharedConstants.SEPARATOR);
		}

		return sb.toString();
	}

	public Set<OrganismBean> getOrganisms() {
		if (organisms == null)
			organisms = new HashSet<OrganismBean>();
		return organisms;
	}

	public String getOrganismsString() {
		final StringBuilder sb = new StringBuilder();
		final Set<OrganismBean> organisms = getOrganisms();
		// sort them for allowing sorting in columns
		final List<OrganismBean> list = new ArrayList<OrganismBean>();
		list.addAll(organisms);
		Collections.sort(list, new Comparator<OrganismBean>() {

			@Override
			public int compare(OrganismBean o1, OrganismBean o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		for (final OrganismBean organismBean : list) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(organismBean.getId());
		}
		return sb.toString();
	}

	/**
	 * @param organisms the organisms to set
	 */
	public void setOrganisms(Set<OrganismBean> organisms) {
		this.organisms = organisms;
	}

	/**
	 * @return the proteinDBIds
	 */
	public Set<Integer> getProteinDBIds() {
		return proteinDBIds;
	}

	/**
	 * @param proteinDBIds the proteinDBIds to set
	 */
	public void setProteinDBIds(Set<Integer> proteinDBIds) {
		this.proteinDBIds = proteinDBIds;
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

	public void addCondition(ExperimentalConditionBean condition) {
		conditions.add(condition);
	}

	@Override
	public Set<ExperimentalConditionBean> getConditions() {
		return conditions;
	}

	public void setConditions(Set<ExperimentalConditionBean> conditions) {
		this.conditions = conditions;
	}

	public String getConditionsString() {
		return SharedDataUtil.getConditionString(this);
	}

	@Override
	public String getId() {
		return getFullSequence();
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
	 * @return the psmIds
	 */
	@Override
	public Set<Integer> getPSMDBIds() {
		return psmIds;
	}

	/**
	 * @param psmIds the psmIds to set
	 */
	public void setPsmIds(Set<Integer> psmIds) {
		this.psmIds = psmIds;
	}

	/**
	 * @return the psmIdsByCondition
	 */
	@Override
	public Map<ExperimentalConditionBean, Set<Integer>> getPSMDBIdsByCondition() {
		return psmIdsByCondition;
	}

	/**
	 * @param psmIdsByCondition the psmIdsByCondition to set
	 */
	public void setPsmIdsByCondition(Map<ExperimentalConditionBean, Set<Integer>> psmIdsByCondition) {
		this.psmIdsByCondition = psmIdsByCondition;
	}

	/**
	 * @return the msRuns
	 */
	public Set<MSRunBean> getMsRuns() {
		return msRuns;
	}

	/**
	 * @param msRuns the msRuns to set
	 */
	public void setMsRuns(Set<MSRunBean> msRuns) {
		this.msRuns = msRuns;
	}

	/**
	 * @param psms the psms to set
	 */
	public void setPsms(List<PSMBean> psms) {
		this.psms = psms;
	}

	/**
	 * @param peptideBeanUniqueIdentifier the peptideBeanUniqueIdentifier to set
	 */
	public void setPeptideBeanUniqueIdentifier(int peptideBeanUniqueIdentifier) {
		this.peptideBeanUniqueIdentifier = peptideBeanUniqueIdentifier;
	}

	private static Set<String> seqs = new HashSet<String>();

	public PeptideBeanLight cloneToLightPeptideBean() {

		if (lightVersion == null) {
			seqs.add(getSequence());
			lightVersion = new PeptideBeanLight();
			// set light to true
			lightVersion.setAmounts(getAmounts());
			lightVersion.setAmountsByExperimentalCondition(getAmountsByExperimentalCondition());
			lightVersion.setConditionsString(getConditionsString());
			lightVersion.setLength(getLength());
			lightVersion.setOrganisms(getOrganisms());
			lightVersion.setPeptideBeanUniqueIdentifier(getPeptideBeanUniqueIdentifier());
			lightVersion.setProteinsPrimaryAccessions(getProteinsPrimaryAccessions());
			lightVersion.setNumPSMs(getNumPSMs());
			lightVersion.setNumPSMsByCondition(getNumPSMsByCondition());
			lightVersion.setRatios(getRatios());
			lightVersion.setRatiosByExperimentalcondition(getRatiosByExperimentalcondition());
			lightVersion.setScores(getScores());
			lightVersion.setSequence(getSequence());
			lightVersion.setStartingPositions(getStartingPositions());
			lightVersion.setRawSequences(getRawSequences());
			lightVersion.setRelation(getRelation());
			lightVersion.setRatioDistributions(getRatioDistributions());
			lightVersion.setPtms(getPtms());
			lightVersion.setPtmScoreString(getPtmScoreString());
			lightVersion.setPtmString(getPtmString());
			lightVersion.setFullSequence(getFullSequence());
			lightVersion.setOrganismString(getOrganismsString());
			lightVersion.setProteinAccessionString(getProteinAccessionString());
			lightVersion.setProteinDescriptionString(getProteinDescriptionString());
			lightVersion.setExtendedStartingPositionsString(getExtendedStartingPositionsString());
			for (final ProteinBean proteinBean : getProteins()) {
				ProteinBeanLight lightProtein = proteinBean.getLightVersion();
				if (lightProtein == null) {
					lightProtein = proteinBean.cloneToLightProteinBean();
				}
				lightVersion.addProteinToPeptide(lightProtein);
			}

		}
		if (needsToQueryProteins()) {
			for (final ProteinBean proteinBean : getProteins()) {
				ProteinBeanLight lightProtein = proteinBean.getLightVersion();
				if (lightProtein == null) {
					lightProtein = proteinBean.cloneToLightProteinBean();
				}
				lightVersion.addProteinToPeptide(lightProtein);
			}
		}
		return lightVersion;
	}

	public PeptideBeanLight getLightVersion() {
		return lightVersion;
	}

	/**
	 * Returns true if some proteinDBIds are not retrieved yet
	 * 
	 * @return
	 */
	public boolean needsToQueryProteins() {

		final Set<ProteinBean> proteins2 = getProteins();
		final Set<Integer> proteinDBIds3 = new HashSet<Integer>();
		for (final ProteinBean proteinBean : proteins2) {
			proteinDBIds3.addAll(proteinBean.getDbIds());
		}
		if (proteinDBIds3.size() != getProteinDBIds().size()) {
			System.out.println("Needs to query proteins from peptideBean " + this.getId());
			return true;
		}
		if (lightVersion != null && getProteins().size() != lightVersion.getProteins().size()) {
			return true;
		}
		return false;
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
	public int getNumPSMs() {
		if (numPSMs == 0) {
			numPSMs = getPSMDBIds().size();
		}
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
		if (relation == null) {
			relation = getProteins().iterator().next().getPeptideRelationsBySequence().get(getSequence());
		}
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
		if (ratioDistribution != null) {
			final Map<String, RatioDistribution> ratioDistributions2 = getRatioDistributions();
			if (ratioDistributions2 != null && !ratioDistributions2.containsKey(ratioDistribution.getRatioKey())) {
				getRatioDistributions().put(ratioDistribution.getRatioKey(), ratioDistribution);
			}
		}
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

	public Map<ExperimentalConditionBean, Integer> getNumPSMsByCondition() {
		if (numPSMsByCondition == null) {
			numPSMsByCondition = new HashMap<ExperimentalConditionBean, Integer>();
			for (final ExperimentalConditionBean conditionBean : psmIdsByCondition.keySet()) {
				numPSMsByCondition.put(conditionBean, psmIdsByCondition.get(conditionBean).size());
			}
		}
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

	public Map<ExperimentalConditionBean, Set<Integer>> getPsmIdsByCondition() {
		return psmIdsByCondition;
	}

	public Set<Integer> getPsmIds() {
		return psmIds;
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

	public void setLightVersion(PeptideBeanLight lightVersion) {
		this.lightVersion = lightVersion;
	}

	public void setRatioDistributions(Map<String, RatioDistribution> ratioDistributions) {
		this.ratioDistributions = ratioDistributions;
	}

	public void setNumPSMsByCondition(Map<ExperimentalConditionBean, Integer> numPSMsByCondition) {
		this.numPSMsByCondition = numPSMsByCondition;
	}

}
