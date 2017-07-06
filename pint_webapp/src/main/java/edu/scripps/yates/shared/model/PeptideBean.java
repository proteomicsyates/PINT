package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.interfaces.ContainsAmounts;
import edu.scripps.yates.shared.model.interfaces.ContainsConditions;
import edu.scripps.yates.shared.model.interfaces.ContainsId;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.interfaces.ContainsSequence;
import edu.scripps.yates.shared.util.NumberFormat;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class PeptideBean implements Comparable<PeptideBean>, Serializable, ContainsRatios, ContainsAmounts, ContainsId,
		ContainsConditions, ContainsPrimaryAccessions, ContainsPSMs, ContainsSequence {
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
	private int length;
	private Double calculatedMH;
	private Map<String, ScoreBean> scores = new HashMap<String, ScoreBean>();
	private Set<AmountBean> amounts = new HashSet<AmountBean>();
	private HashMap<String, Set<AmountBean>> amountsByExperimentalCondition = new HashMap<String, Set<AmountBean>>();
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
	private Map<MSRunBean, Set<Integer>> psmIdsbyMSRun = new HashMap<MSRunBean, Set<Integer>>();
	private Map<ExperimentalConditionBean, Set<Integer>> psmIdsByCondition = new HashMap<ExperimentalConditionBean, Set<Integer>>();
	private Set<Integer> psmIds = new HashSet<Integer>();
	private Set<String> rawSequences = new HashSet<String>();
	private int peptideBeanUniqueIdentifier;
	private int numPSMs;
	private PeptideBean lightVersion;
	private PeptideRelation relation;
	private Map<String, RatioDistribution> ratioDistributions;

	public PeptideBean() {

		peptideBeanUniqueIdentifier = hashCode();
	}

	/**
	 * @return the peptideBeanUniqueIdentifier
	 */
	public int getPeptideBeanUniqueIdentifier() {
		return peptideBeanUniqueIdentifier;
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
	 * @param dbId
	 *            the dbId to set
	 */
	public void setDbIds(Set<Integer> dbIds) {
		this.dbIds = dbIds;
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
		if (psm == null || psmIds.contains(psm.getDbID()) || psms.contains(psm)) {
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

		// set Proteins
		final Set<ProteinBean> proteins2 = psm.getProteins();
		for (ProteinBean proteinBean : proteins2) {
			addProteinToPeptide(proteinBean);
		}

		// organisms
		for (OrganismBean organismBean : psm.getOrganisms()) {
			addOrganism(organismBean);
		}
		// add all the proteins to the psm
		final Set<ProteinBean> proteins3 = getProteins();
		for (ProteinBean proteinBean : proteins3) {
			psm.addProteinToPSM(proteinBean);
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
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * @param calculatedMH
	 *            the calculatedMH to set
	 */
	public void setCalculatedMH(Double calculatedMH) {
		this.calculatedMH = calculatedMH;
	}

	/**
	 * Gets a Map of {@link ScoreBean} by score name
	 *
	 * @return
	 */
	public Map<String, ScoreBean> getScores() {
		return scores;
	}

	public void setScores(Map<String, ScoreBean> scores) {
		this.scores = scores;
	}

	public void addScore(ScoreBean score) {
		if (scores == null)
			scores = new HashMap<String, ScoreBean>();
		if (score.getScoreName() == null)
			System.out.println("Asdf");
		scores.put(score.getScoreName(), score);
	}

	public void addOrganism(OrganismBean organism) {
		organisms.add(organism);
	}

	@Override
	public Set<AmountBean> getAmounts() {
		return amounts;
	}

	public void setAmounts(Set<AmountBean> amounts) {
		this.amounts = amounts;
	}

	public String getAmountString(String conditionName, String projectTag) {
		StringBuilder sb = new StringBuilder();
		// get by experimental condition name
		Set<AmountBean> amounts = amountsByExperimentalCondition.get(conditionName);

		if (amounts != null) {
			// if some amounts are resulting from the combination
			// (sum/average...)
			// over other amounts, report only them
			Set<AmountBean> composedAmounts = AmountBean.getComposedAmounts(amounts);
			if (!composedAmounts.isEmpty())
				amounts = composedAmounts;
			for (AmountBean amountBean : amounts) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {

					// if the resulting string is a number, try to format it:
					final Double doubleValue = amountBean.getValue();
					if (doubleValue.toString().endsWith(".0")) {
						if (!"".equals(sb.toString()))
							sb.append("\n");
						sb.append(String.valueOf(doubleValue.intValue()));
					} else {
						try {
							final String format = NumberFormat.getFormat("#.##").format(doubleValue);
							if (!"".equals(sb.toString()))
								sb.append("\n");
							sb.append(format);
						} catch (NumberFormatException e2) {

						}
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * @return the proteinAmountsByExperimentalCondition
	 */
	@Override
	public HashMap<String, Set<AmountBean>> getAmountsByExperimentalCondition() {
		return amountsByExperimentalCondition;
	}

	/**
	 * @param proteinAmountsByExperimentalCondition
	 *            the proteinAmountsByExperimentalCondition to set
	 */
	public void setAmountsByExperimentalCondition(
			HashMap<String, Set<AmountBean>> proteinAmountsByExperimentalCondition) {
		amountsByExperimentalCondition = proteinAmountsByExperimentalCondition;
	}

	public void addAmount(AmountBean psmAmount) {
		amounts.add(psmAmount);

		// index by experiment condition
		addtoMap(amountsByExperimentalCondition, psmAmount);
	}

	private void addtoMap(HashMap<String, Set<AmountBean>> map, AmountBean amount) {
		String key = amount.getExperimentalCondition().getId();
		if (map.containsKey(key)) {
			map.get(key).add(amount);
		} else {
			Set<AmountBean> set = new HashSet<AmountBean>();
			set.add(amount);
			map.put(key, set);
		}
	}

	public String getProteinAccessionString() {
		if (proteinAccessionString == null) {
			StringBuilder sb = new StringBuilder();
			for (AccessionBean acc : getProteinsPrimaryAccessions()) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(acc.getAccession());
			}
			proteinAccessionString = sb.toString();
		}
		return proteinAccessionString;
	}

	public void addProteinToPeptide(ProteinBean protein) {

		if (protein == null || proteins.contains(protein) || proteinDBIds.containsAll(protein.getDbIds())) {
			return;
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

	}

	public void setProteins(Set<ProteinBean> proteins) {
		this.proteins = proteins;
	}

	public Set<ProteinBean> getProteins() {
		return proteins;
		// return Collections.emptySet();
	}

	public String getProteinDescriptionString() {
		if (proteinDescriptionString == null) {
			StringBuilder sb = new StringBuilder();
			for (ProteinBean proteinBean : proteins) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(proteinBean.getDescriptionString());
			}
			proteinDescriptionString = sb.toString();
		}
		return proteinDescriptionString;
	}

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
			List<RatioBean> set = new ArrayList<RatioBean>();
			set.add(ratioBean);
			ratiosByExperimentalcondition.put(condition1.getId(), set);
		}
		if (ratiosByExperimentalcondition.containsKey(condition2.getId())) {
			ratiosByExperimentalcondition.get(condition2.getId()).add(ratioBean);
		} else {
			List<RatioBean> set = new ArrayList<RatioBean>();
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
	 * @param ratios
	 *            the ratios to set
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
		Collections.sort(proteinsPrimaryAccessions, SharedDataUtils.getComparatorByAccession());
		return proteinsPrimaryAccessions;
	}

	/**
	 * @param proteinsPrimaryAccessions
	 *            the proteinsPrimaryAccessions to set
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
	 * @param ratiosByExperimentalcondition
	 *            the ratiosByExperimentalcondition to set
	 */
	public void setRatiosByExperimentalcondition(HashMap<String, List<RatioBean>> ratiosByExperimentalcondition) {
		this.ratiosByExperimentalcondition = ratiosByExperimentalcondition;
	}

	/**
	 * Gets the ratios available between these two conditions. No matter the
	 * order of the comparison. It assures that not repeated ratios are in the
	 * result.
	 *
	 * @param conditionName1
	 * @param conditionName2
	 * @return
	 */
	@Override
	public List<RatioBean> getRatiosByConditions(String conditionName1, String conditionName2, String projectTag,
			String ratioName, boolean skipInfinities) {
		return SharedDataUtils.getRatiosByConditions(getRatios(), conditionName1, conditionName2, projectTag, ratioName,
				skipInfinities);
	}

	@Override
	public String getRatioStringByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, boolean skipInfinities) {
		StringBuilder sb = new StringBuilder();

		final List<RatioBean> ratiosByConditions = getRatiosByConditions(condition1Name, condition2Name, projectTag,
				ratioName, skipInfinities);
		final List<Double> ratioValues = SharedDataUtils.getRatioValues(condition1Name, condition2Name,
				ratiosByConditions);
		for (Double value : ratioValues) {

			try {
				final String format = NumberFormat.getFormat("#.##").format(value);
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(format);
			} catch (NumberFormatException e2) {

			}

		}

		return sb.toString();
	}

	/**
	 * Gets a string with the amount types
	 *
	 * @param conditionName
	 * @param projectTag
	 * @return
	 */
	public String getAmountTypeString(String conditionName, String projectTag) {

		Set<AmountType> amountTypes = new HashSet<AmountType>();
		final Set<AmountBean> amounts2 = getAmounts();
		for (AmountBean amountBean : amounts2) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {

					amountTypes.add(amountBean.getAmountType());

				}
			}
		}

		StringBuilder sb = new StringBuilder();
		for (AmountType amountType : amountTypes) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(amountType.getDescription());
		}
		return sb.toString();
	}

	@Override
	public boolean hasCombinationAmounts(String conditionName, String projectTag) {

		final Set<AmountBean> amounts2 = getAmounts();
		for (AmountBean amountBean : amounts2) {
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
		List<AmountBean> ret = new ArrayList<AmountBean>();

		final Set<AmountBean> amounts2 = getAmounts();
		for (AmountBean amountBean : amounts2) {
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
		List<AmountBean> ret = new ArrayList<AmountBean>();
		final Set<AmountBean> amounts2 = getAmounts();
		for (AmountBean amountBean : amounts2) {
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
			List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
			list.add(positions);
			startingPositions.put(accession, list);
		}
	}

	/**
	 * Gets the starting positions of the peptide sequence depending on the
	 * protein. The first AA of the protein is the position 1.
	 *
	 * @return the startingPositions is a {@link HashMap} where the key is the
	 *         protein accession and the value is the positions in which the
	 *         peptide can be in the protein
	 */
	public Map<String, List<Pair<Integer, Integer>>> getStartingPositions() {
		return startingPositions;
	}

	/**
	 * @param startingPositions
	 *            the startingPositions to set
	 */
	public void setStartingPositions(Map<String, List<Pair<Integer, Integer>>> startingPositions) {
		this.startingPositions = startingPositions;
	}

	public String getStartingPositionsString() {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = getStartingPositions();
		StringBuilder sb = new StringBuilder();

		if (startingPositions != null) {
			int minPosition = Integer.MAX_VALUE;
			List<Integer> list = new ArrayList<Integer>();
			for (List<Pair<Integer, Integer>> positions : startingPositions.values()) {
				for (Pair<Integer, Integer> startAndEnd : positions) {
					int position = startAndEnd.getFirstElement();
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
		StringBuilder sb = new StringBuilder();
		for (AccessionBean accessionBean : primaryAccessions) {
			if (startingPositions != null && startingPositions.containsKey(accessionBean.getAccession())) {
				final List<Pair<Integer, Integer>> listTmp = startingPositions.get(accessionBean.getAccession());
				// to avoid duplicates:
				List<Integer> list = new ArrayList<Integer>();
				for (Pair<Integer, Integer> startAndEnd : listTmp) {
					int position = startAndEnd.getFirstElement();
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
		StringBuilder sb = new StringBuilder();
		final Set<OrganismBean> organisms = getOrganisms();
		// sort them for allowing sorting in columns
		List<OrganismBean> list = new ArrayList<OrganismBean>();
		list.addAll(organisms);
		Collections.sort(list, new Comparator<OrganismBean>() {

			@Override
			public int compare(OrganismBean o1, OrganismBean o2) {
				return o1.getId().compareTo(o2.getId());
			}
		});
		for (OrganismBean organismBean : list) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(organismBean.getId());
		}
		return sb.toString();
	}

	/**
	 * @param organisms
	 *            the organisms to set
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
	 * @param proteinDBIds
	 *            the proteinDBIds to set
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
				for (AmountBean amount : amounts) {
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
				for (RatioBean ratio : ratios) {
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
			String ratioName) {
		final List<RatioBean> ratiosByConditions = getRatiosByConditions(condition1Name, condition2Name, projectTag,
				ratioName, false);
		final List<ScoreBean> ratioScores = SharedDataUtils.getRatioScoreValues(condition1Name, condition2Name,
				ratiosByConditions);
		return ratioScores;
	}

	@Override
	public String getRatioScoreStringByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, boolean skipInfinities) {
		StringBuilder sb = new StringBuilder();

		final List<ScoreBean> ratioScores = getRatioScoresByConditions(condition1Name, condition2Name, projectTag,
				ratioName);
		for (ScoreBean ratioScore : ratioScores) {
			try {
				Double doubleValue = Double.valueOf(ratioScore.getValue());
				if (doubleValue.toString().endsWith(".0")) {
					if (!"".equals(sb.toString()))
						sb.append(SharedConstants.SEPARATOR);
					sb.append(String.valueOf(doubleValue.intValue()));
				} else {
					try {
						final String format = NumberFormat.getFormat("#.##").format(doubleValue);
						if (!"".equals(sb.toString()))
							sb.append(SharedConstants.SEPARATOR);
						sb.append(format);
					} catch (NumberFormatException e2) {

					}
				}
			} catch (NumberFormatException e) {
				// add the string as it is
			}

		}

		return sb.toString();
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
		return SharedDataUtils.getConditionString(this);
	}

	@Override
	public String getId() {
		return getSequence();
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
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
	 * @param psmIds
	 *            the psmIds to set
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
	 * @return the psmIdsbyMSRun
	 */
	@Override
	public Map<MSRunBean, Set<Integer>> getPSMDBIdsbyMSRun() {
		return psmIdsbyMSRun;
	}

	/**
	 * @param psmIdsByCondition
	 *            the psmIdsByCondition to set
	 */
	public void setPsmIdsByCondition(Map<ExperimentalConditionBean, Set<Integer>> psmIdsByCondition) {
		this.psmIdsByCondition = psmIdsByCondition;
	}

	/**
	 * @param psmIdsbyMSRun
	 *            the psmIdsbyMSRun to set
	 */
	public void setPsmIdsbyMSRun(Map<MSRunBean, Set<Integer>> psmIdsbyMSRun) {
		this.psmIdsbyMSRun = psmIdsbyMSRun;
	}

	/**
	 * @return the msRuns
	 */
	public Set<MSRunBean> getMsRuns() {
		return msRuns;
	}

	/**
	 * @param msRuns
	 *            the msRuns to set
	 */
	public void setMsRuns(Set<MSRunBean> msRuns) {
		this.msRuns = msRuns;
	}

	/**
	 * @param psms
	 *            the psms to set
	 */
	public void setPsms(List<PSMBean> psms) {
		this.psms = psms;
	}

	/**
	 * @param peptideBeanUniqueIdentifier
	 *            the peptideBeanUniqueIdentifier to set
	 */
	public void setPeptideBeanUniqueIdentifier(int peptideBeanUniqueIdentifier) {
		this.peptideBeanUniqueIdentifier = peptideBeanUniqueIdentifier;
	}

	private static Set<String> seqs = new HashSet<String>();

	public PeptideBean cloneToLightPeptideBean() {

		if (lightVersion == null) {

			seqs.add(getSequence());
			lightVersion = new PeptideBean();
			lightVersion.setAmounts(getAmounts());
			lightVersion.setAmountsByExperimentalCondition(getAmountsByExperimentalCondition());
			lightVersion.setCalculatedMH(getCalculatedMH());
			lightVersion.setConditions(getConditions());
			lightVersion.setDbIds(getDbIds());
			lightVersion.setLength(getLength());
			lightVersion.setMsRuns(getMSRuns());
			lightVersion.setOrganisms(getOrganisms());
			lightVersion.setPeptideBeanUniqueIdentifier(getPeptideBeanUniqueIdentifier());
			// ret.setProteinDBIds(getProteinDBIds());
			// ret.setProteins(getProteins());
			lightVersion.setProteinsPrimaryAccessions(getProteinsPrimaryAccessions());
			// ret.setPsmIds(getPsmIds());
			// ret.setPsmIdsByCondition(getPsmIdsByCondition());
			// ret.setPsmIdsbyMSRun(getPsmIdsbyMSRun());
			// ret.setPsms(getPsms());

			lightVersion.setNumPSMs(getNumPSMs());

			lightVersion.setRatios(getRatios());
			lightVersion.setRatiosByExperimentalcondition(getRatiosByExperimentalcondition());
			lightVersion.setScores(getScores());
			lightVersion.setSequence(getSequence());
			lightVersion.setStartingPositions(getStartingPositions());
			lightVersion.setRawSequences(getRawSequences());
			lightVersion.setRelation(getRelation());
			lightVersion.ratioDistributions = getRatioDistributions();
		}
		return lightVersion;
	}

	/**
	 * @return the rawSequences
	 */
	public Set<String> getRawSequences() {
		return rawSequences;
	}

	/**
	 * @param rawSequences
	 *            the rawSequences to set
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
	 * @param numPSMs
	 *            the numPSMs to set
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
	 * @param relation
	 *            the relation to set
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
				ratioDistributions.put(ratioDistribution.getRatioKey(), ratioDistribution);
			}
		}
	}

	@Override
	public RatioDistribution getRatioDistribution(RatioBean ratio) {
		return ratioDistributions.get(SharedDataUtils.getRatioKey(ratio));

	}
}
