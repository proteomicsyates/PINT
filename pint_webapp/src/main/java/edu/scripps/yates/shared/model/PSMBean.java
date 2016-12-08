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
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.interfaces.ContainsSequence;
import edu.scripps.yates.shared.util.NumberFormat;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class PSMBean implements Serializable, ContainsRatios, ContainsAmounts, ContainsId, ContainsConditions,
		ContainsPrimaryAccessions, ContainsSequence {
	/**
	 *
	 */
	private static final long serialVersionUID = -1713998292042049510L;
	private String psmID;
	private Integer dbID;
	private MSRunBean msRun;
	private String fullSequence;
	private String sequence;
	private List<PTMBean> ptms = new ArrayList<PTMBean>();
	private Double experimentalMH;
	private Double calculatedMH;
	private Double massErrorPPM;
	private Double totalIntensity;
	private Integer spr;
	private Double ionProportion;
	private Double pi;
	private Map<String, ScoreBean> scores = new HashMap<String, ScoreBean>();
	private Set<AmountBean> amounts = new HashSet<AmountBean>();
	private HashMap<String, Set<AmountBean>> amountsByExperimentalCondition = new HashMap<String, Set<AmountBean>>();
	private HashMap<String, List<RatioBean>> ratiosByExperimentalcondition = new HashMap<String, List<RatioBean>>();
	private Set<ProteinBean> proteins = new HashSet<ProteinBean>();
	private Set<RatioBean> ratios = new HashSet<RatioBean>();
	private List<AccessionBean> proteinsPrimaryAccessions = new ArrayList<AccessionBean>();
	private String ptmString;
	private String ptmScoreString;
	private String proteinAccessionString;
	private String proteinDescriptionString;
	private String chargeState;
	private Map<String, List<Pair<Integer, Integer>>> startingPositions = new HashMap<String, List<Pair<Integer, Integer>>>();
	private Set<OrganismBean> organisms = new HashSet<OrganismBean>();
	private Set<Integer> proteinDBIds = new HashSet<Integer>();
	private Set<ExperimentalConditionBean> conditions = new HashSet<ExperimentalConditionBean>();
	private int numProteins;
	private PeptideRelation relation;
	private PSMBean lightVersion;
	private PeptideBean peptideBean;
	private Map<String, RatioDistribution> ratioDistributions = new HashMap<String, RatioDistribution>();

	public PSMBean() {

	}

	public PSMBean(String psmID, MSRunBean msRun, String sequence, String fullSequence) {
		this.psmID = psmID;
		this.msRun = msRun;
		this.sequence = sequence;
		this.fullSequence = fullSequence;
	}

	public String getPSMIdentifier() {
		return psmID;
	}

	public Double getExperimentalMH() {
		return experimentalMH;
	}

	public Double getCalcMH() {
		return calculatedMH;
	}

	public Double getMassErrorPPM() {
		return massErrorPPM;
	}

	public Double getTotalIntensity() {
		return totalIntensity;
	}

	public Integer getSPR() {
		return spr;
	}

	public Double getIonProportion() {
		return ionProportion;
	}

	public String getFullSequence() {
		return fullSequence;
	}

	@Override
	public String getSequence() {
		return sequence;
	}

	/**
	 * @param ptms
	 *            the ptms to set
	 */
	public void setPtms(List<PTMBean> ptms) {
		this.ptms = ptms;
	}

	public void addPtm(PTMBean ptm) {
		if (ptms == null)
			ptms = new ArrayList<PTMBean>();
		ptms.add(ptm);
	}

	/**
	 * @return the psmID
	 */
	public String getPsmID() {
		return psmID;
	}

	/**
	 * @param psmID
	 *            the psmID to set
	 */
	public void setPsmID(String psmID) {
		this.psmID = psmID;
	}

	/**
	 * @return the msRun
	 */
	public MSRunBean getMsRun() {
		return msRun;
	}

	/**
	 * @param msRun
	 *            the msRun to set
	 */
	public void setMsRun(MSRunBean msRun) {
		this.msRun = msRun;
	}

	/**
	 * @return the ptms
	 */
	public List<PTMBean> getPtms() {
		return ptms;
	}

	/**
	 * @return the calculatedMH
	 */
	public Double getCalculatedMH() {
		return calculatedMH;
	}

	/**
	 * @return the spr
	 */
	public Integer getSpr() {
		return spr;
	}

	/**
	 * @return the pi
	 */
	public Double getPi() {
		return pi;
	}

	/**
	 * @param fullSequence
	 *            the fullSequence to set
	 */
	public void setFullSequence(String fullSequence) {
		this.fullSequence = fullSequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	/**
	 * @param experimentalMH
	 *            the experimentalMH to set
	 */
	public void setExperimentalMH(Double experimentalMH) {
		this.experimentalMH = experimentalMH;
	}

	/**
	 * @param calculatedMH
	 *            the calculatedMH to set
	 */
	public void setCalculatedMH(Double calculatedMH) {
		this.calculatedMH = calculatedMH;
	}

	/**
	 * @param massErrorPPM
	 *            the massErrorPPM to set
	 */
	public void setMassErrorPPM(Double massErrorPPM) {
		this.massErrorPPM = massErrorPPM;
	}

	/**
	 * @param totalIntensity
	 *            the totalIntensity to set
	 */
	public void setTotalIntensity(Double totalIntensity) {
		this.totalIntensity = totalIntensity;
	}

	/**
	 * @param spr
	 *            the spr to set
	 */
	public void setSpr(Integer spr) {
		this.spr = spr;
	}

	/**
	 * @param ionProportion
	 *            the ionProportion to set
	 */
	public void setIonProportion(Double ionProportion) {
		this.ionProportion = ionProportion;
	}

	/**
	 * @param pi
	 *            the pi to set
	 */
	public void setPi(Double pi) {
		this.pi = pi;
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

	public String getPTMString() {
		if (ptmString == null) {
			StringBuilder sb = new StringBuilder();

			if (ptms != null) {
				// sort ptms by the position

				Collections.sort(ptms, new Comparator<PTMBean>() {
					@Override
					public int compare(PTMBean o1, PTMBean o2) {
						int minPosition1 = Integer.MAX_VALUE;
						int minPosition2 = Integer.MAX_VALUE;
						for (PTMSiteBean ptmSite : o1.getPtmSites()) {
							if (minPosition1 > ptmSite.getPosition())
								minPosition1 = ptmSite.getPosition();
						}
						for (PTMSiteBean ptmSite : o2.getPtmSites()) {
							if (minPosition2 > ptmSite.getPosition())
								minPosition2 = ptmSite.getPosition();
						}
						return Integer.compare(minPosition1, minPosition2);
					}
				});
				// now that is sorted:
				for (PTMBean ptmBean : ptms) {
					if (!"".equals(sb.toString()))
						sb.append(SharedConstants.SEPARATOR);
					sb.append(ptmBean.getName() + " (");

					final List<PTMSiteBean> ptmSites = ptmBean.getPtmSites();
					// sort by position
					Collections.sort(ptmSites, new Comparator<PTMSiteBean>() {
						@Override
						public int compare(PTMSiteBean o1, PTMSiteBean o2) {
							return Integer.compare(o1.getPosition(), o2.getPosition());
						}
					});
					for (int i = 0; i < ptmSites.size(); i++) {
						PTMSiteBean ptmSiteBean = ptmSites.get(i);
						if (i > 0)
							sb.append(",");
						sb.append(ptmSiteBean.getPosition());
					}
					sb.append(")");
				}

			}

			ptmString = sb.toString();
		}
		return ptmString;
	}

	public String getPTMScoreString() {
		if (ptmScoreString == null) {
			Set<String> ret = new HashSet<String>();
			StringBuilder sb = new StringBuilder();
			if (ptms != null) {
				for (PTMBean ptm : ptms) {
					final String scoreString = ptm.getScoreString();
					if (!ret.contains(scoreString)) {
						if (!"".equals(sb.toString()))
							sb.append(SharedConstants.SEPARATOR);
						sb.append(scoreString);
						ret.add(scoreString);
					}

				}
			}
			ptmScoreString = sb.toString();
		}
		return ptmScoreString;
	}

	public String getPTMScoreString(String ptmScoreName) {
		if (ptmScoreString == null) {
			Set<String> ret = new HashSet<String>();
			StringBuilder sb = new StringBuilder();
			if (ptms != null) {
				for (PTMBean ptm : ptms) {
					final String scoreString = ptm.getScoreString(ptmScoreName);
					if (!ret.contains(scoreString)) {
						if (!"".equals(sb.toString()))
							sb.append(SharedConstants.SEPARATOR);
						sb.append(scoreString);
						ret.add(scoreString);
					}

				}
			}
			ptmScoreString = sb.toString();
		}
		return ptmScoreString;
	}

	public String getProteinAccessionString() {
		if (proteinAccessionString == null) {
			StringBuilder sb = new StringBuilder();
			// sort first by alphabetic order
			List<String> sortedList = new ArrayList<String>();
			for (AccessionBean acc : proteinsPrimaryAccessions) {
				sortedList.add(acc.getAccession());
			}
			Collections.sort(sortedList);
			for (String acc : sortedList) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(acc);
			}
			proteinAccessionString = sb.toString();
		}
		return proteinAccessionString;
	}

	public void addProteinToPSM(ProteinBean protein) {
		if (protein == null || proteins.contains(protein)) {
			return;
		}
		if (!proteinsPrimaryAccessions.contains(protein.getPrimaryAccession())) {
			proteinsPrimaryAccessions.add(protein.getPrimaryAccession());
		}
		proteins.add(protein);
		protein.addPSMtoProtein(this);
		protein.addPeptideToProtein(getPeptideBean());
	}

	public void setProteins(Set<ProteinBean> proteins) {
		this.proteins = proteins;
	}

	public Set<ProteinBean> getProteins() {
		return proteins;
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
	 * @return the proteinsPrimaryAccessions
	 */
	public List<AccessionBean> getProteinsPrimaryAccessions() {
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

	/**
	 * @return the dbID
	 */
	public Integer getDbID() {
		return dbID;
	}

	/**
	 * @param dbID
	 *            the dbID to set
	 */
	public void setDbID(Integer dbID) {
		this.dbID = dbID;
	}

	public String getChargeState() {
		return chargeState;
	}

	public void setChargeState(String chargeState) {
		this.chargeState = chargeState;
	}

	@Override
	public String getId() {
		return getPsmID();
	}

	@Override
	public List<AccessionBean> getPrimaryAccessions() {
		return getProteinsPrimaryAccessions();
	}

	public void addPositionByProtein(String accession, Pair<Integer, Integer> position) {
		if (startingPositions.containsKey(accession)) {
			startingPositions.get(accession).add(position);
		} else {
			List<Pair<Integer, Integer>> list = new ArrayList<Pair<Integer, Integer>>();
			list.add(position);
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

	public PSMBean cloneToLightPsmBean() {
		if (lightVersion == null) {
			lightVersion = new PSMBean();

			lightVersion.setAmounts(getAmounts());
			lightVersion.setAmountsByExperimentalCondition(getAmountsByExperimentalCondition());
			lightVersion.setCalculatedMH(getCalculatedMH());
			lightVersion.setChargeState(getChargeState());
			lightVersion.setConditions(getConditions());
			lightVersion.setDbID(getDbID());
			lightVersion.setExperimentalMH(getExperimentalMH());
			lightVersion.setFullSequence(getFullSequence());
			lightVersion.setIonProportion(getIonProportion());
			lightVersion.setMassErrorPPM(getMassErrorPPM());
			lightVersion.setMsRun(getMsRun());
			lightVersion.setOrganisms(getOrganisms());
			lightVersion.setPi(getPi());
			// ret.setProteinDBIds(getProteinDBIds());

			final Set<ProteinBean> proteins2 = getProteins();
			for (ProteinBean proteinBean : proteins2) {
				lightVersion.addProteinToPSM(proteinBean.cloneToLightProteinBean());
			}

			lightVersion.setNumProteins(proteins2.size());
			lightVersion.setProteinsPrimaryAccessions(getProteinsPrimaryAccessions());
			lightVersion.setPsmID(getPsmID());
			lightVersion.setPtms(getPtms());
			lightVersion.setPtmScoreString(getPTMScoreString());
			lightVersion.setPtmString(getPTMString());
			lightVersion.setRatios(getRatios());
			lightVersion.setRatiosByExperimentalcondition(getRatiosByExperimentalcondition());
			lightVersion.setScores(getScores());
			lightVersion.setSequence(getSequence());
			lightVersion.setSpr(getSpr());
			lightVersion.setStartingPositions(getStartingPositions());
			lightVersion.setTotalIntensity(getTotalIntensity());
			lightVersion.setRelation(getRelation());
			lightVersion.ratioDistributions = getRatioDistributions();
		}
		return lightVersion;
	}

	public void setNumProteins(int numProteins) {
		this.numProteins = numProteins;

	}

	/**
	 * @return the ptmString
	 */
	public String getPtmString() {
		return ptmString;
	}

	/**
	 * @param ptmString
	 *            the ptmString to set
	 */
	public void setPtmString(String ptmString) {
		this.ptmString = ptmString;
	}

	/**
	 * @return the ptmScoreString
	 */
	public String getPtmScoreString() {
		return ptmScoreString;
	}

	/**
	 * @param ptmScoreString
	 *            the ptmScoreString to set
	 */
	public void setPtmScoreString(String ptmScoreString) {
		this.ptmScoreString = ptmScoreString;
	}

	/**
	 * @return the numProteins
	 */
	public int getNumProteins() {
		return numProteins;
	}

	/**
	 * @param proteinAccessionString
	 *            the proteinAccessionString to set
	 */
	public void setProteinAccessionString(String proteinAccessionString) {
		this.proteinAccessionString = proteinAccessionString;
	}

	/**
	 * @param proteinDescriptionString
	 *            the proteinDescriptionString to set
	 */
	public void setProteinDescriptionString(String proteinDescriptionString) {
		this.proteinDescriptionString = proteinDescriptionString;
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
	public void setRelation(PeptideRelation evidence) {
		relation = evidence;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ProteinBean protein : getProteins()) {
			sb.append(protein.getPrimaryAccession().getAccession() + ",");
		}
		String score = "";
		final ScoreBean scoreByName = getScoreByName("SEQUEST:xcorr");
		if (scoreByName != null) {
			score = scoreByName.getScoreName() + ":" + scoreByName.getValue();
		}
		return "[psmID=" + psmID + ", seq=" + fullSequence + ", prot=" + sb.toString() + ", " + score + "]";
	}

	public PeptideBean getPeptideBean() {
		return peptideBean;
	}

	/**
	 * @param peptideBean
	 *            the peptideBean to set
	 */
	public void setPeptideBeanToPSM(PeptideBean peptideBean) {
		if (peptideBean == null) {
			return;
		}
		if (this.peptideBean == null) {
			this.peptideBean = peptideBean;
			// add the proteins of the peptide to the psm
			final Set<ProteinBean> proteins2 = peptideBean.getProteins();
			for (ProteinBean proteinBean : proteins2) {
				addProteinToPSM(proteinBean);
			}
		}
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
		return ratioDistributions.get(SharedDataUtils.getRatioKey(ratio));

	}

}
