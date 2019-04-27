package edu.scripps.yates.shared.model.light;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AccessionType;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.GeneBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.OmimEntryBean;
import edu.scripps.yates.shared.model.OrganismBean;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.PeptideRelation;
import edu.scripps.yates.shared.model.ProteinAnnotationBean;
import edu.scripps.yates.shared.model.ProteinEvidence;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDistribution;
import edu.scripps.yates.shared.model.ReactomePathwayRef;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.ThresholdBean;
import edu.scripps.yates.shared.model.UniprotFeatureBean;
import edu.scripps.yates.shared.model.UniprotProteinExistence;
import edu.scripps.yates.shared.model.interfaces.ContainsAmounts;
import edu.scripps.yates.shared.model.interfaces.ContainsGenes;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPeptides;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.interfaces.ContainsScores;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;

public class ProteinBeanLight implements Comparable<ProteinBeanLight>, Serializable, ContainsRatios, ContainsAmounts,
		ContainsGenes, ContainsPrimaryAccessions, ContainsLightPSMs, ContainsLightPeptides, ContainsScores {
	/**
	 *
	 */
	private static final long serialVersionUID = -1435542806814270031L;
	private List<PSMBeanLight> psms = new ArrayList<PSMBeanLight>();
	private List<PeptideBeanLight> peptides = new ArrayList<PeptideBeanLight>();

	private Set<AccessionBean> secondaryAccessions = new HashSet<AccessionBean>();

	private AccessionBean primaryAccession;
	private double mw = -1;
	private List<AmountBean> amounts = new ArrayList<AmountBean>();
	private HashMap<String, List<AmountBean>> amountsByExperimentalCondition = new HashMap<String, List<AmountBean>>();
	private HashMap<String, Set<RatioBean>> ratiosByExperimentalcondition = new HashMap<String, Set<RatioBean>>();
	private HashMap<MSRunBean, List<AmountBean>> amountsByMSRunID = new HashMap<MSRunBean, List<AmountBean>>();
	private ProteinEvidence evidence;

	private int proteinBeanUniqueIdentifier;
	private int length;
	private double pi = -1;
	private Double coverage;
	private Set<RatioBean> ratios = new HashSet<RatioBean>();
	private Set<GeneBean> genes = new HashSet<GeneBean>();
	private Set<ProteinAnnotationBean> annotations = new HashSet<ProteinAnnotationBean>();
	private Set<ThresholdBean> thresholds = new HashSet<ThresholdBean>();

	private String alternativeNamesString;
	private String geneString;
	private String descriptionString;
	private OrganismBean organism;

	private Map<Integer, OmimEntryBean> omimEntries = new HashMap<Integer, OmimEntryBean>();
	private int numPSMs;
	private int numPeptides;
	private UniprotProteinExistence uniprotProteinExistence;
	private char[] coverageArrayString;
	private String ensemblID;
	private Map<String, RatioDistribution> ratioDistributions;
	private Map<String, List<UniprotFeatureBean>> uniprotFeatures = new HashMap<String, List<UniprotFeatureBean>>();
	private List<ReactomePathwayRef> reactomePathways = new ArrayList<ReactomePathwayRef>();
	private Map<String, ScoreBean> scores = new HashMap<String, ScoreBean>();
	private Map<ExperimentalConditionBean, Integer> numPSMsByCondition;
	private Map<String, PeptideRelation> peptideRelationsBySequence = new HashMap<String, PeptideRelation>();
	private boolean annotated;
	private String proteinDBString;
	private String conditionsString;
	private String functionString;
	private static int uniqueIdentifiers = 0;
	public static final String ANNOTATION_SEPARATOR = "###";

	public ProteinBeanLight() {
		proteinBeanUniqueIdentifier = uniqueIdentifiers++;
	}

	/**
	 * @return the proteinBeanUniqueIdentifier
	 */
	public int getProteinBeanUniqueIdentifier() {
		return proteinBeanUniqueIdentifier;
	}

	/**
	 * @return the geneString
	 */
	public String getGeneString() {
		return geneString;
	}

	/**
	 * @param geneString the geneString to set
	 */
	public void setGeneString(String geneString) {
		this.geneString = geneString;
	}

	/**
	 * @return the genes
	 */
	private Set<GeneBean> getGenes() {
		return genes;
	}

	/**
	 * @param coverage the coverage to set
	 */
	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}

	/**
	 * @param alternativeNamesString the alternativeNamesString to set
	 */
	public void setAlternativeNamesString(String alternativeNamesString) {
		this.alternativeNamesString = alternativeNamesString;
	}

	/**
	 * @param descriptionString the descriptionString to set
	 */
	public void setDescriptionString(String descriptionString) {
		this.descriptionString = descriptionString;
	}

	/**
	 * @param proteinBeanUniqueIdentifier the proteinBeanUniqueIdentifier to set
	 */
	public void setProteinBeanUniqueIdentifier(int proteinBeanUniqueIdentifier) {
		this.proteinBeanUniqueIdentifier = proteinBeanUniqueIdentifier;
	}

	/**
	 * @return the amountsByMSRunID
	 */
	public HashMap<MSRunBean, List<AmountBean>> getAmountsByMSRunID() {
		return amountsByMSRunID;
	}

	/**
	 * @param amountsByMSRunID the amountsByMSRunID to set
	 */
	public void setAmountsByMSRunID(HashMap<MSRunBean, List<AmountBean>> amountsByMSRunID) {
		this.amountsByMSRunID = amountsByMSRunID;
	}

	public String getProteinDBString() {

		return proteinDBString;
	}

	public void setProteinDBString(String proteinDBString) {
		this.proteinDBString = proteinDBString;
	}

	/**
	 * @return the psms
	 */
	@Override
	public List<PSMBeanLight> getPsms() {
		return psms;
	}

	/**
	 * @return the psms
	 */
	@Override
	public List<PeptideBeanLight> getPeptides() {
		return peptides;
	}

	/**
	 * @param psms the psms to set
	 */
	public void setPsms(List<PSMBeanLight> psms) {
		this.psms = psms;
	}

	/**
	 * @return the accessions
	 */
	public Set<AccessionBean> getSecondaryAccessions() {
		return secondaryAccessions;
	}

	/**
	 * @param accessions the accessions to set
	 */
	public void setSecondaryAccessions(Set<AccessionBean> accessions) {
		secondaryAccessions = accessions;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Set<AccessionBean> getAccessions(AccessionType accType) {
		final Set<AccessionBean> ret = new HashSet<AccessionBean>();
		final AccessionBean primaryAccession2 = getPrimaryAccession();
		if (primaryAccession2 != null && primaryAccession2.getAccessionType() != null
				&& primaryAccession2.getAccessionType().equals(accType))
			ret.add(primaryAccession2);
		for (final AccessionBean acc : secondaryAccessions) {
			if (acc.getAccessionType().equals(accType))
				ret.add(acc);
		}
		return ret;
	}

	public void addSecondaryAccession(AccessionBean acc) {
		secondaryAccessions.add(acc);

	}

	public void addPSMtoProtein(PSMBeanLight psmBeanLight) {
		if (psmBeanLight == null || psms.contains(psmBeanLight)) {
			return;
		}
		psms.add(psmBeanLight);
		psmBeanLight.addProteinToPSM(this);

		addPeptideToProtein(psmBeanLight.getPeptideBean());

	}

	public boolean addPeptideToProtein(PeptideBeanLight lightPeptide) {
		if (lightPeptide == null || peptides.contains(lightPeptide))
			return false;

		final boolean ret = peptides.add(lightPeptide);
		lightPeptide.addProteinToPeptide(this);
		final List<PSMBeanLight> psms2 = lightPeptide.getPsms();
		for (final PSMBeanLight psmBean : psms2) {
			addPSMtoProtein(psmBean);
		}
		return ret;
	}

	@Override
	public int compareTo(ProteinBeanLight proteinBean) {
		return getPrimaryAccession().getAccession().compareTo(proteinBean.getPrimaryAccession().getAccession());

	}

	public String getDescriptionString() {

		return descriptionString;
	}

	public AccessionBean getPrimaryAccession() {
		return primaryAccession;
	}

	public void setMw(double mw) {
		this.mw = mw;

	}

	public void setAmounts(List<AmountBean> proteinAmounts) {
		amounts = proteinAmounts;
		for (final AmountBean proteinAmountBean : proteinAmounts) {
			addAmountToMap(proteinAmountBean);
		}
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPi(double pi) {
		this.pi = pi;
	}

	public void setRatios(Set<RatioBean> proteinRatios) {
		ratios = proteinRatios;
		for (final RatioBean ratioBean : proteinRatios) {
			addtoMap(ratioBean);
		}

	}

	private void addtoMap(RatioBean ratioBean) {
		if (ratioBean == null) {
			return;
		}
		final ExperimentalConditionBean condition1 = ratioBean.getCondition1();
		final ExperimentalConditionBean condition2 = ratioBean.getCondition2();
		if (ratiosByExperimentalcondition.containsKey(condition1.getId())) {
			ratiosByExperimentalcondition.get(condition1.getId()).add(ratioBean);
		} else {
			final Set<RatioBean> set = new HashSet<RatioBean>();
			set.add(ratioBean);
			ratiosByExperimentalcondition.put(condition1.getId(), set);
		}
		if (ratiosByExperimentalcondition.containsKey(condition2.getId())) {
			ratiosByExperimentalcondition.get(condition2.getId()).add(ratioBean);
		} else {
			final Set<RatioBean> set = new HashSet<RatioBean>();
			set.add(ratioBean);
			ratiosByExperimentalcondition.put(condition2.getId(), set);
		}
	}

	/**
	 * @return the mw
	 */
	public double getMw() {
		if (mw > 0)
			return mw;
		return 0.0;
	}

	/**
	 * @return the proteinAmounts
	 */
	@Override
	public List<AmountBean> getAmounts() {
		return amounts;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the pi
	 */
	public double getPi() {
		return pi;
	}

	/**
	 * @return the proteinRatios
	 */
	@Override
	public Set<RatioBean> getRatios() {
		return ratios;
	}

	private void addAmountToMap(AmountBean proteinAmount) {
		// index by experiment condition
		if (amountsByExperimentalCondition.containsKey(proteinAmount.getExperimentalCondition().getId())) {
			amountsByExperimentalCondition.get(proteinAmount.getExperimentalCondition().getId()).add(proteinAmount);
		} else {
			final List<AmountBean> set = new ArrayList<AmountBean>();
			set.add(proteinAmount);
			amountsByExperimentalCondition.put(proteinAmount.getExperimentalCondition().getId(), set);
		}

		// index by msRun
		for (final MSRunBean msrunBean : proteinAmount.getMsRuns()) {

			if (amountsByMSRunID.containsKey(msrunBean)) {
				amountsByMSRunID.get(msrunBean).add(proteinAmount);
			} else {
				final List<AmountBean> set = new ArrayList<AmountBean>();
				set.add(proteinAmount);
				amountsByMSRunID.put(msrunBean, set);
			}
		}
	}

	public void addProteinRatio(RatioBean proteinRatioBean) {
		if (proteinRatioBean == null) {
			return;
		}
		ratios.add(proteinRatioBean);
		addtoMap(proteinRatioBean);
	}

	/**
	 * Gets genes from protein. If onlyPrimary is true, only one, the primary is
	 * returned. If there is not primary, then, an empty list will be returned
	 *
	 * @param onlyPrimary
	 * @return
	 */
	@Override
	public List<GeneBean> getGenes(boolean onlyPrimary) {
		final List<GeneBean> list = new ArrayList<GeneBean>();
		for (final GeneBean gene : getGenes()) {
			if (!onlyPrimary || GeneBean.PRIMARY.equals(gene.getGeneType())) {
				list.add(gene);
				if (onlyPrimary) {
					// if only primary is called, only one will be returned, so
					// return here
					return list;
				}
			}
		}
		return list;
	}

	public void addGene(GeneBean gene) {
		getGenes().add(gene);
	}

	private String getGenesString(String type, boolean onlyPrimary) {

		final StringBuilder sb = new StringBuilder();
		final Set<String> set = new HashSet<String>();
		final List<GeneBean> genes = getGenes(onlyPrimary);
		for (final GeneBean geneBean : genes) {
			if (type == null || (geneBean.getGeneType() != null && geneBean.getGeneType().equalsIgnoreCase(type))) {
				if (!set.contains(geneBean.getGeneID())) {
					set.add(geneBean.getGeneID());
					if (!"".equals(sb.toString()))
						sb.append("\n");
					sb.append(geneBean.getGeneID());
				}
			}
		}

		return sb.toString();

	}

	public String getGenesString(boolean onlyPrimary) {
		if (geneString == null) {
			geneString = getGenesString(null, onlyPrimary);
		}
		return geneString;
	}

	/**
	 * @param primaryAccession the primaryAccession to set
	 */
	public void setPrimaryAccession(AccessionBean primaryAccession) {

		this.primaryAccession = primaryAccession;

	}

	/**
	 * @param genes the genes to set
	 */
	public void setGenes(Set<GeneBean> genes) {
		this.genes = genes;
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

	public String getAlternativeNamesString() {
		if (alternativeNamesString == null) {
			final List<String> alternativeNames = getPrimaryAccession().getAlternativeNames();
			final StringBuilder sb = new StringBuilder();
			for (final String altName : alternativeNames) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(altName);
			}
			alternativeNamesString = sb.toString();
		}
		return alternativeNamesString;
	}

	/**
	 * @return the annotations
	 */
	public Set<ProteinAnnotationBean> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Set<ProteinAnnotationBean> annotations) {
		this.annotations = annotations;
	}

	public void addAnnotation(ProteinAnnotationBean annotation) {
		annotations.add(annotation);
	}

	/**
	 * @return the thresholds
	 */
	public Set<ThresholdBean> getThresholds() {
		return thresholds;
	}

	/**
	 * @param thresholds the thresholds to set
	 */
	public void setThresholds(Set<ThresholdBean> thresholds) {
		this.thresholds = thresholds;
	}

	public void addThreshold(ThresholdBean threshold) {
		thresholds.add(threshold);
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

	/**
	 * @return the numPeptides
	 */
	public int getNumPeptides() {
		return numPeptides;
	}

	/**
	 * @param numPeptides the numPeptides to set
	 */
	public void setNumPeptides(int numPeptides) {
		this.numPeptides = numPeptides;
	}

	/**
	 * @return the ratiosByExperimentalcondition
	 */
	public HashMap<String, Set<RatioBean>> getRatiosByExperimentalcondition() {
		return ratiosByExperimentalcondition;
	}

	/**
	 * @param ratiosByExperimentalcondition the ratiosByExperimentalcondition to set
	 */
	public void setRatiosByExperimentalcondition(HashMap<String, Set<RatioBean>> ratiosByExperimentalcondition) {
		this.ratiosByExperimentalcondition = ratiosByExperimentalcondition;
	}

	@Override
	public List<RatioBean> getRatiosByConditions(String conditionName1, String conditionName2, String projectTag,
			String ratioName, boolean skipInfinities) {
		return SharedDataUtil.getRatiosByConditions(getRatios(), conditionName1, conditionName2, projectTag, ratioName,
				skipInfinities);
	}

	/**
	 * Gets a String with the amount types
	 *
	 * @param conditionName
	 * @param projectTag
	 * @return
	 */
	public String getAmountTypeString(String conditionName, String projectTag) {
		List<AmountBean> amounts = getAmountsByExperimentalCondition().get(conditionName);
		final List<AmountType> amountTypes = new ArrayList<AmountType>();
		if (amounts != null) {
			final List<AmountBean> composedAmounts = AmountBean.getComposedAmounts(amounts);
			if (!composedAmounts.isEmpty())
				amounts = composedAmounts;
			for (final AmountBean amount : amounts) {
				if (!amountTypes.contains(amount.getAmountType())) {
					amountTypes.add(amount.getAmountType());
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
		final List<AmountBean> amounts2 = getAmounts();
		for (final AmountBean amountBean : amounts2) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
					if (!amountBean.isComposed() && !ret.contains(amountBean))
						ret.add(amountBean);
				}
			}
		}
		return ret;
	}

	/**
	 * @return the evidence
	 */
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	/**
	 * @param evidence the evidence to set
	 */
	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;
	}

	public OrganismBean getOrganism() {
		return organism;
	}

	public void setOrganism(OrganismBean organism) {
		this.organism = organism;
	}

	public String getFunctionString() {
		return functionString;
	}

	public void setFunctionString(String functionString) {
		this.functionString = functionString;
	}

	public void addUniprotFeature(UniprotFeatureBean uniprotFeature) {
		if (uniprotFeatures == null)
			uniprotFeatures = new HashMap<String, List<UniprotFeatureBean>>();
		if (uniprotFeatures.containsKey(uniprotFeature.getFeatureType())) {
			final List<UniprotFeatureBean> list = uniprotFeatures.get(uniprotFeature.getFeatureType());
			if (!list.contains(uniprotFeature)) {
				list.add(uniprotFeature);
				Collections.sort(list);
			}
		} else {
			final List<UniprotFeatureBean> list = new ArrayList<UniprotFeatureBean>();
			list.add(uniprotFeature);
			uniprotFeatures.put(uniprotFeature.getFeatureType(), list);
		}
	}

	public String getSecondaryAccessionsString() {
		final List<String> list = new ArrayList<String>();
		final StringBuilder sb = new StringBuilder();
		final Set<AccessionBean> secondaryAccessions2 = getSecondaryAccessions();
		if (secondaryAccessions2 != null) {
			for (final AccessionBean accessionBean : secondaryAccessions2) {
				final String accession = accessionBean.getAccession();
				if (!list.contains(accession) && !getPrimaryAccession().getAccession().equals(accession))
					list.add(accession);
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
	public List<AccessionBean> getPrimaryAccessions() {
		final List<AccessionBean> list = new ArrayList<AccessionBean>();
		list.add(getPrimaryAccession());
		return list;
	}

	/**
	 * @return the coverage
	 */
	public Double getCoverage() {
		return coverage;
	}

	/**
	 * Returns true if the {@link ProteinBeanLight} has been identified in the
	 * provided projects, which means that at least one {@link AmountBean} or one
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
			if (psms != null) {
				for (final PSMBeanLight psmBean : psms) {
					if (psmBean.isFromThisProject(projectTag)) {
						return true;
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

	public void addOMIMEntries(Collection<OmimEntryBean> omimEntries) {
		for (final OmimEntryBean omimEntry : omimEntries) {
			addOMIMEntry(omimEntry);
		}
	}

	public void addOMIMEntry(OmimEntryBean omimEntry) {
		omimEntries.put(omimEntry.getId(), omimEntry);
	}

	/**
	 * @return the omimEntries
	 */
	public Map<Integer, OmimEntryBean> getOmimEntries() {
		return omimEntries;
	}

	/**
	 * @param omimEntries the omimEntries to set
	 */
	public void setOmimEntries(Map<Integer, OmimEntryBean> omimEntries) {
		this.omimEntries = omimEntries;
	}

	/**
	 * Return a list of OMIM Ids separated by comma
	 *
	 * @return
	 */
	public String getOmimIDString() {
		final StringBuilder sb = new StringBuilder();

		final List<Integer> sortedIDs = getOmimSortedIDs();
		for (final Integer omimID : sortedIDs) {
			sb.append(omimID);
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public List<Integer> getOmimSortedIDs() {
		final List<Integer> sortedIds = new ArrayList<Integer>();

		if (!omimEntries.isEmpty()) {
			final Set<Integer> keySet = omimEntries.keySet();
			for (final Integer i : keySet) {
				sortedIds.add(i);
			}

			Collections.sort(sortedIds);

		}
		return sortedIds;
	}

	/**
	 * @param peptides the peptides to set
	 */
	public void setPeptides(List<PeptideBeanLight> peptides) {
		this.peptides = peptides;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return primaryAccession.getAccession();
	}

	/**
	 * @return the uniprotProteinExistence
	 */
	public UniprotProteinExistence getUniprotProteinExistence() {
		return uniprotProteinExistence;
	}

	/**
	 * @param uniprotProteinExistence the uniprotProteinExistence to set
	 */
	public void setUniprotProteinExistence(UniprotProteinExistence uniprotProteinExistence) {
		this.uniprotProteinExistence = uniprotProteinExistence;
	}

	/**
	 * Returns a char[] array with 0 and 1, representing the AA of a protein
	 * sequence, and how they are covered (1) or not (0)
	 *
	 * @return
	 */
	public char[] getCoverageArrayString() {
		return coverageArrayString;
	}

	/**
	 * @param coverageArrayString the coverageArrayString to set
	 */
	public void setCoverageArrayString(char[] coverageArrayString) {
		this.coverageArrayString = coverageArrayString;
	}

	public void addEnsemblID(String ensemblAnnotation) {
		// this could come like: protein sequence ID=ENSP00000301659,gene
		// ID=ENSG00000167914
		if (ensemblAnnotation.contains("gene ID")) {
			String[] split = new String[1];
			if (ensemblAnnotation.contains(ANNOTATION_SEPARATOR)) {
				split = ensemblAnnotation.split(ANNOTATION_SEPARATOR);
			} else {
				split[0] = ensemblAnnotation;
			}
			for (final String string : split) {
				if (string.startsWith("gene ID") && string.contains(":")) {
					ensemblID = string.split(":")[1];
				}
			}
		}

	}

	/**
	 * @return the ensemblID
	 */
	public String getEnsemblID() {
		return ensemblID;
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

	/**
	 * @return the uniprotFeatures
	 */
	public Map<String, List<UniprotFeatureBean>> getUniprotFeatures() {
		if (uniprotFeatures == null) {
			uniprotFeatures = new HashMap<String, List<UniprotFeatureBean>>();
		}
		return uniprotFeatures;
	}

	public List<UniprotFeatureBean> getUniprotFeaturesByFeatureType(String featureType) {
		if (uniprotFeatures.containsKey(featureType)) {
			return uniprotFeatures.get(featureType);
		}
		return Collections.emptyList();
	}

	/**
	 * @param uniprotFeatures the uniprotFeatures to set
	 */
	public void setUniprotFeatures(HashMap<String, List<UniprotFeatureBean>> uniprotFeatures) {
		this.uniprotFeatures = uniprotFeatures;
	}

	/**
	 * @return the reactomePathways
	 */
	public List<ReactomePathwayRef> getReactomePathways() {
		return reactomePathways;
	}

	/**
	 * @param reactomePathways the reactomePathways to set
	 */
	public void setReactomePathways(List<ReactomePathwayRef> reactomePathways) {
		this.reactomePathways = reactomePathways;
	}

	public void addReactomePathwayRef(ReactomePathwayRef reactomePathwayRef) {
		if (reactomePathways == null) {
			reactomePathways = new ArrayList<ReactomePathwayRef>();
		}
		reactomePathways.add(reactomePathwayRef);

	}

	public void setEnsemblID(String ensemblID) {
		this.ensemblID = ensemblID;
	}

	public void setRatioDistributions(Map<String, RatioDistribution> ratioDistributions) {
		this.ratioDistributions = ratioDistributions;
	}

	public void setUniprotFeatures(Map<String, List<UniprotFeatureBean>> uniprotFeatures) {
		this.uniprotFeatures = uniprotFeatures;
	}

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

	@Override
	public ScoreBean getScoreByName(String scoreName) {
		return scores.get(scoreName);
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

	public void addPeptideRelation(String peptideSequence, PeptideRelation peptideRelation) {
		peptideRelationsBySequence.put(peptideSequence, peptideRelation);
	}

	public Map<String, PeptideRelation> getPeptideRelationsBySequence() {
		return peptideRelationsBySequence;
	}

	public void setPeptideRelationsBySequence(Map<String, PeptideRelation> peptideRelationsBySequence) {
		this.peptideRelationsBySequence = peptideRelationsBySequence;
	}

	public void setAnnotated(boolean b) {
		this.annotated = b;
	}

	public boolean isAnnotated() {
		return annotated;
	}

	public void setNumPSMsByCondition(Map<ExperimentalConditionBean, Integer> numPSMsByCondition2) {
		this.numPSMsByCondition = numPSMsByCondition2;
	}
}
