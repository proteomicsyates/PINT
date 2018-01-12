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

import edu.scripps.yates.annotations.uniprot.ProteinImplFromUniprotEntry;
import edu.scripps.yates.shared.model.interfaces.ContainsAmounts;
import edu.scripps.yates.shared.model.interfaces.ContainsConditions;
import edu.scripps.yates.shared.model.interfaces.ContainsGenes;
import edu.scripps.yates.shared.model.interfaces.ContainsId;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.model.interfaces.ContainsPrimaryAccessions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.interfaces.ContainsScores;
import edu.scripps.yates.shared.util.NumberFormat;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class ProteinBean
		implements Comparable<ProteinBean>, Serializable, ContainsRatios, ContainsAmounts, ContainsId, ContainsGenes,
		ContainsPrimaryAccessions, ContainsPSMs, ContainsPeptides, ContainsConditions, ContainsScores, Cloneable {
	/**
	 *
	 */
	private static final long serialVersionUID = -1435542806814270031L;
	private Set<Integer> dbIds = new HashSet<Integer>();
	private List<PSMBean> psms = new ArrayList<PSMBean>();
	private List<PeptideBean> peptides = new ArrayList<PeptideBean>();

	private Set<AccessionBean> secondaryAccessions = new HashSet<AccessionBean>();

	private AccessionBean primaryAccession;
	private double mw = -1;
	private Set<AmountBean> amounts = new HashSet<AmountBean>();
	private HashMap<String, Set<AmountBean>> amountsByExperimentalCondition = new HashMap<String, Set<AmountBean>>();
	private HashMap<String, Set<RatioBean>> ratiosByExperimentalcondition = new HashMap<String, Set<RatioBean>>();
	private HashMap<MSRunBean, Set<AmountBean>> amountsByMSRunID = new HashMap<MSRunBean, Set<AmountBean>>();
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
	private Set<MSRunBean> msruns = new HashSet<MSRunBean>();
	private Set<Integer> psmIds = new HashSet<Integer>();
	private Set<String> differentSequences = new HashSet<String>();
	private OrganismBean organism;
	private Set<String> functions = new HashSet<String>();
	private Map<ExperimentalConditionBean, Set<Integer>> psmIdsByCondition = new HashMap<ExperimentalConditionBean, Set<Integer>>();
	private Map<MSRunBean, Set<Integer>> psmIdsbyMSRun = new HashMap<MSRunBean, Set<Integer>>();
	private Set<ExperimentalConditionBean> conditions = new HashSet<ExperimentalConditionBean>();
	private Map<Integer, OmimEntryBean> omimEntries = new HashMap<Integer, OmimEntryBean>();
	private int numPSMs;
	private int numPeptides;
	private ProteinBean lightVersion;
	private UniprotProteinExistence uniprotProteinExistence;
	private char[] coverageArrayString;
	private String ensemblID;
	private Map<String, RatioDistribution> ratioDistributions;
	private Map<String, List<UniprotFeatureBean>> uniprotFeatures = new HashMap<String, List<UniprotFeatureBean>>();
	private List<ReactomePathwayRef> reactomePathways = new ArrayList<ReactomePathwayRef>();
	private Set<Integer> peptideDBIds = new HashSet<Integer>();
	private Map<String, ScoreBean> scores = new HashMap<String, ScoreBean>();
	private Map<ExperimentalConditionBean, Integer> numPSMsByCondition;

	public ProteinBean() {
		proteinBeanUniqueIdentifier = hashCode();
	}

	/**
	 * @return the msruns
	 */
	public Set<MSRunBean> getMsruns() {
		return msruns;
	}

	/**
	 * @param msruns
	 *            the msruns to set
	 */
	public void setMsruns(Set<MSRunBean> msruns) {
		this.msruns = msruns;
	}

	/**
	 * @return the psmIds
	 */
	public Set<Integer> getPsmIds() {
		return psmIds;
	}

	/**
	 * @return the psmIdsByCondition
	 */
	public Map<ExperimentalConditionBean, Set<Integer>> getPsmIdsByCondition() {
		return psmIdsByCondition;
	}

	/**
	 * @return the psmIdsbyMSRun
	 */
	public Map<MSRunBean, Set<Integer>> getPsmIdsbyMSRun() {
		return psmIdsbyMSRun;
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
	 * @param geneString
	 *            the geneString to set
	 */
	public void setGeneString(String geneString) {
		this.geneString = geneString;
	}

	/**
	 * @return the functions
	 */
	public Set<String> getFunctions() {
		return functions;
	}

	/**
	 * @param functions
	 *            the functions to set
	 */
	public void setFunctions(Set<String> functions) {
		this.functions = functions;
	}

	/**
	 * @return the genes
	 */
	public Set<GeneBean> getGenes() {
		return genes;
	}

	/**
	 * @param coverage
	 *            the coverage to set
	 */
	public void setCoverage(Double coverage) {
		this.coverage = coverage;
	}

	/**
	 * @param alternativeNamesString
	 *            the alternativeNamesString to set
	 */
	public void setAlternativeNamesString(String alternativeNamesString) {
		this.alternativeNamesString = alternativeNamesString;
	}

	/**
	 * @param descriptionString
	 *            the descriptionString to set
	 */
	public void setDescriptionString(String descriptionString) {
		this.descriptionString = descriptionString;
	}

	/**
	 * @param proteinBeanUniqueIdentifier
	 *            the proteinBeanUniqueIdentifier to set
	 */
	public void setProteinBeanUniqueIdentifier(int proteinBeanUniqueIdentifier) {
		this.proteinBeanUniqueIdentifier = proteinBeanUniqueIdentifier;
	}

	/**
	 * @return the amountsByMSRunID
	 */
	public HashMap<MSRunBean, Set<AmountBean>> getAmountsByMSRunID() {
		return amountsByMSRunID;
	}

	/**
	 * @param amountsByMSRunID
	 *            the amountsByMSRunID to set
	 */
	public void setAmountsByMSRunID(HashMap<MSRunBean, Set<AmountBean>> amountsByMSRunID) {
		this.amountsByMSRunID = amountsByMSRunID;
	}

	public String getProteinDBString() {

		List<Integer> sorted = new ArrayList<Integer>();
		sorted.addAll(dbIds);
		Collections.sort(sorted);
		String ret = "";
		for (Integer integer : sorted) {
			ret += integer + "-";
		}
		return ret;
	}

	/**
	 * @return the psms
	 */
	@Override
	public List<PSMBean> getPsms() {
		return psms;
	}

	/**
	 * @return the psms
	 */
	@Override
	public List<PeptideBean> getPeptides() {
		return peptides;
	}

	/**
	 * @param psms
	 *            the psms to set
	 */
	public void setPsms(List<PSMBean> psms) {
		this.psms = psms;
	}

	/**
	 * @return the accessions
	 */
	public Set<AccessionBean> getSecondaryAccessions() {
		return secondaryAccessions;
	}

	/**
	 * @param accessions
	 *            the accessions to set
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
		Set<AccessionBean> ret = new HashSet<AccessionBean>();
		final AccessionBean primaryAccession2 = getPrimaryAccession();
		if (primaryAccession2 != null && primaryAccession2.getAccessionType() != null
				&& primaryAccession2.getAccessionType().equals(accType))
			ret.add(primaryAccession2);
		for (AccessionBean acc : secondaryAccessions) {
			if (acc.getAccessionType().equals(accType))
				ret.add(acc);
		}
		return ret;
	}

	public void addSecondaryAccession(AccessionBean acc) {
		secondaryAccessions.add(acc);

	}

	public void addPSMtoProtein(PSMBean psmBean) {
		if (psmBean == null || psms.contains(psmBean) || psmIds.contains(psmBean.getDbID())) {
			return;
		}
		psms.add(psmBean);
		psmBean.addProteinToPSM(this);
		addPSMToMapByconditions(psmBean);
		addPSMToMapByRuns(psmBean);
		psmIds.add(psmBean.getDbID());
		addPeptideToProtein(psmBean.getPeptideBean());

	}

	private void addPSMToMapByconditions(PSMBean psmBean) {
		// add to psm ids by conditions
		final Set<ExperimentalConditionBean> conditions2 = psmBean.getConditions();
		if (conditions2.isEmpty()) {
			return;
		}
		for (ExperimentalConditionBean experimentalConditionBean : conditions2) {
			if (psmIdsByCondition.containsKey(experimentalConditionBean)) {
				psmIdsByCondition.get(experimentalConditionBean).add(psmBean.getDbID());
			} else {
				Set<Integer> set = new HashSet<Integer>();
				set.add(psmBean.getDbID());
				psmIdsByCondition.put(experimentalConditionBean, set);
			}
		}
	}

	private void addPSMToMapByRuns(PSMBean psmBean) {
		// add to psm ids by conditions
		final MSRunBean msrun = psmBean.getMsRun();

		if (psmIdsbyMSRun.containsKey(msrun)) {
			psmIdsbyMSRun.get(msrun).add(psmBean.getDbID());
		} else {
			Set<Integer> set = new HashSet<Integer>();
			set.add(psmBean.getDbID());
			psmIdsbyMSRun.put(msrun, set);
		}

	}

	public void addPeptideToProtein(PeptideBean peptideBean) {
		if (peptideBean == null || peptides.contains(peptideBean) || peptideDBIds.containsAll(peptideBean.getDbIds()))
			return;
		peptideDBIds.addAll(peptideBean.getDbIds());
		peptides.add(peptideBean);
		peptideBean.addProteinToPeptide(this);
		final List<PSMBean> psms2 = peptideBean.getPsms();
		for (PSMBean psmBean : psms2) {
			addPSMtoProtein(psmBean);
		}
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

	public void addDbIds(Collection<Integer> dbIds) {
		this.dbIds.addAll(dbIds);
	}

	public void addDbIds(int[] dbIds) {
		for (int dbId : dbIds) {
			this.dbIds.add(dbId);
		}

	}

	/**
	 * @param dbId
	 *            the dbId to set
	 */
	public void setDbIds(Set<Integer> dbIds) {
		this.dbIds = dbIds;
	}

	@Override
	public int compareTo(ProteinBean proteinBean) {
		return getPrimaryAccession().getAccession().compareTo(proteinBean.getPrimaryAccession().getAccession());

	}

	public String getDescriptionString() {
		if (descriptionString == null) {
			// to avoid redundancies, store first in a set
			List<String> list = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			final Set<AccessionBean> uniprotACCs = getAccessions(edu.scripps.yates.shared.model.AccessionType.UNIPROT);
			if (uniprotACCs != null) {
				for (AccessionBean acc : uniprotACCs) {
					if (!list.contains(acc.getDescription()))
						list.add(acc.getDescription());
				}
			}
			final Set<AccessionBean> ipiACCs = getAccessions(edu.scripps.yates.shared.model.AccessionType.IPI);
			if (ipiACCs != null && !ipiACCs.isEmpty()) {

				for (AccessionBean acc : ipiACCs) {
					if (!list.contains(acc.getDescription()))
						list.add(acc.getDescription());
				}
			}
			for (String description : list) {
				if (!"".equals(sb.toString()))
					sb.append(",");
				sb.append(description);
			}
			descriptionString = sb.toString();
		}
		return descriptionString;
	}

	public AccessionBean getPrimaryAccession() {
		return primaryAccession;
	}

	public void setMw(double mw) {
		this.mw = mw;

	}

	public void setAmounts(Set<AmountBean> proteinAmounts) {
		amounts = proteinAmounts;
		for (AmountBean proteinAmountBean : proteinAmounts) {
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
		for (RatioBean ratioBean : proteinRatios) {
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
			Set<RatioBean> set = new HashSet<RatioBean>();
			set.add(ratioBean);
			ratiosByExperimentalcondition.put(condition1.getId(), set);
		}
		if (ratiosByExperimentalcondition.containsKey(condition2.getId())) {
			ratiosByExperimentalcondition.get(condition2.getId()).add(ratioBean);
		} else {
			Set<RatioBean> set = new HashSet<RatioBean>();
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
	public Set<AmountBean> getAmounts() {
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

	public void addAmount(AmountBean proteinAmount) {
		amounts.add(proteinAmount);

		addAmountToMap(proteinAmount);
	}

	private void addAmountToMap(AmountBean proteinAmount) {
		// index by experiment condition
		if (amountsByExperimentalCondition.containsKey(proteinAmount.getExperimentalCondition().getId())) {
			amountsByExperimentalCondition.get(proteinAmount.getExperimentalCondition().getId()).add(proteinAmount);
		} else {
			Set<AmountBean> set = new HashSet<AmountBean>();
			set.add(proteinAmount);
			amountsByExperimentalCondition.put(proteinAmount.getExperimentalCondition().getId(), set);
		}

		// index by msRun
		if (amountsByMSRunID.containsKey(proteinAmount.getMsRun())) {
			amountsByMSRunID.get(proteinAmount.getMsRun()).add(proteinAmount);
		} else {
			Set<AmountBean> set = new HashSet<AmountBean>();
			set.add(proteinAmount);
			amountsByMSRunID.put(proteinAmount.getMsRun(), set);
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
		List<GeneBean> list = new ArrayList<GeneBean>();
		for (GeneBean gene : genes) {
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

	public GeneBean getRepresentativeGene() {

		// look for a primary one
		for (GeneBean gene : genes) {
			if (GeneBean.PRIMARY.equals(gene.getGeneType())) {
				return gene;
			}
		}
		if (!genes.isEmpty()) {
			// sort genes by name alphabetically
			List<GeneBean> list = new ArrayList<GeneBean>();
			list.addAll(genes);
			Collections.sort(list, new Comparator<GeneBean>() {

				@Override
				public int compare(GeneBean o1, GeneBean o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
			// return the first one in the list
			return list.get(0);
		}
		return null;
	}

	public void addGene(GeneBean gene) {
		genes.add(gene);
	}

	public String getGenesString(String type, boolean onlyPrimary) {

		StringBuilder sb = new StringBuilder();
		Set<String> set = new HashSet<String>();
		final List<GeneBean> genes = getGenes(onlyPrimary);
		for (GeneBean geneBean : genes) {
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

	public String getAmountString(String conditionName, String projectTag) {
		StringBuilder sb = new StringBuilder();
		// get by experimental condition name
		Set<AmountBean> proteinAmountSet = amountsByExperimentalCondition.get(conditionName);
		List<AmountBean> proteinAmounts = sortAmountsByRunID(proteinAmountSet);
		if (proteinAmounts != null) {
			// if some amounts are resulting from the combination
			// (sum/average...)
			// over other amounts, report only them
			Set<AmountBean> composedAmounts = AmountBean.getComposedAmounts(proteinAmounts);
			if (!composedAmounts.isEmpty())
				proteinAmounts = sortAmountsByRunID(composedAmounts);
			for (AmountBean proteinAmountBean : proteinAmounts) {
				if (proteinAmountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {

					// if the resulting string is a number, try to format it:
					final Double doubleValue = proteinAmountBean.getValue();
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
				}
			}
		}

		return sb.toString();
	}

	private List<AmountBean> sortAmountsByRunID(Set<AmountBean> composedAmounts) {
		if (composedAmounts == null)
			return null;
		List<AmountBean> ret = new ArrayList<AmountBean>();

		ret.addAll(composedAmounts);

		Collections.sort(ret, new Comparator<AmountBean>() {
			@Override
			public int compare(AmountBean o1, AmountBean o2) {
				return o1.getMsRun().getRunID().compareTo(o2.getMsRun().getRunID());
			}
		});
		return ret;
	}

	/**
	 * @param primaryAccession
	 *            the primaryAccession to set
	 */
	public void setPrimaryAccession(AccessionBean primaryAccession) {
		boolean overrideAcc = true;
		if (primaryAccession != null && primaryAccession.getDescription() != null) {
			if (primaryAccession.getDescription().startsWith("This entry was marked as obsolete")) {
				overrideAcc = false;
			}
		}
		if (this.primaryAccession == null || (overrideAcc && primaryAccession != null)) {
			this.primaryAccession = primaryAccession;
			// check whether there are one secondary accession equal to the
			// primary, and in that case, remove it
			if (getSecondaryAccessions() != null) {
				final Iterator<AccessionBean> secondaryAccessionsIterator = secondaryAccessions.iterator();
				while (secondaryAccessionsIterator.hasNext()) {
					final AccessionBean secondaryAccession = secondaryAccessionsIterator.next();
					if (secondaryAccession.getAccession().equals(primaryAccession.getAccession())) {
						secondaryAccessionsIterator.remove();
					}
				}
			}
		}
	}

	/**
	 * @param genes
	 *            the genes to set
	 */
	public void setGenes(Set<GeneBean> genes) {
		this.genes = genes;
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

	public String getAlternativeNamesString() {
		if (alternativeNamesString == null) {
			final List<String> alternativeNames = getPrimaryAccession().getAlternativeNames();
			StringBuilder sb = new StringBuilder();
			for (String altName : alternativeNames) {
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
	 * @param thresholds
	 *            the thresholds to set
	 */
	public void setThresholds(Set<ThresholdBean> thresholds) {
		this.thresholds = thresholds;
	}

	public void addThreshold(ThresholdBean threshold) {
		thresholds.add(threshold);
	}

	public void addCondition(ExperimentalConditionBean condition) {
		conditions.add(condition);
	}

	/**
	 * @return the numPSMs
	 */
	public int getNumPSMs() {
		if (numPSMs == 0) {
			final Set<Integer> psmIds2 = getPSMDBIds();
			if (psmIds2 != null && !psmIds2.isEmpty())
				numPSMs = psmIds2.size();
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

	/**
	 * @return the numPeptides
	 */
	public int getNumPeptides() {
		if (numPeptides == 0) {
			final Set<String> differentSequences2 = getDifferentSequences();
			if (differentSequences2 != null && !differentSequences2.isEmpty()) {
				numPeptides = differentSequences2.size();
			}
		}
		return numPeptides;
	}

	/**
	 * @param numPeptides
	 *            the numPeptides to set
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
	 * @param ratiosByExperimentalcondition
	 *            the ratiosByExperimentalcondition to set
	 */
	public void setRatiosByExperimentalcondition(HashMap<String, Set<RatioBean>> ratiosByExperimentalcondition) {
		this.ratiosByExperimentalcondition = ratiosByExperimentalcondition;
	}

	@Override
	public List<RatioBean> getRatiosByConditions(String conditionName1, String conditionName2, String projectTag,
			String ratioName, boolean skipInfinities) {
		return SharedDataUtils.getRatiosByConditions(getRatios(), conditionName1, conditionName2, projectTag, ratioName,
				skipInfinities);
	}

	@Override
	public String getRatioStringByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, boolean skipInfinities, boolean formatNumber) {
		StringBuilder sb = new StringBuilder();

		final List<RatioBean> ratiosByConditions = getRatiosByConditions(condition1Name, condition2Name, projectTag,
				ratioName, skipInfinities);
		final List<Double> ratioValues = SharedDataUtils.getRatioValues(condition1Name, condition2Name,
				ratiosByConditions);
		for (Double value : ratioValues) {

			if (value.toString().endsWith(".0")) {
				if (!"".equals(sb.toString()))
					sb.append(SharedConstants.SEPARATOR);
				sb.append(String.valueOf(value.intValue()));
			} else {
				try {
					String format = null;
					if (formatNumber) {
						format = SharedDataUtils.formatNumber(value, 2, true);
					} else {
						format = String.valueOf(value);
					}
					if (!"".equals(sb.toString()))
						sb.append(SharedConstants.SEPARATOR);
					sb.append(format);
				} catch (NumberFormatException e2) {

				}
			}
		}

		return sb.toString();
	}

	@Override
	public String getRatioScoreStringByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName, boolean skipInfinities, boolean formatNumber) {
		StringBuilder sb = new StringBuilder();

		final List<ScoreBean> ratioScores = getRatioScoresByConditions(condition1Name, condition2Name, projectTag,
				ratioName, ratioScoreName);
		for (ScoreBean ratioScore : ratioScores) {
			try {
				Double value = Double.valueOf(ratioScore.getValue());
				if (value.toString().endsWith(".0")) {
					if (!"".equals(sb.toString()))
						sb.append(SharedConstants.SEPARATOR);
					sb.append(String.valueOf(value.intValue()));
				} else {
					try {
						String format = null;
						if (formatNumber) {
							format = SharedDataUtils.formatNumber(value, 3, true);
						} else {
							format = String.valueOf(value);
						}
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

	/**
	 * Gets a String with the amount types
	 *
	 * @param conditionName
	 * @param projectTag
	 * @return
	 */
	public String getAmountTypeString(String conditionName, String projectTag) {
		Set<AmountBean> amounts = getAmountsByExperimentalCondition().get(conditionName);
		List<AmountType> amountTypes = new ArrayList<AmountType>();
		if (amounts != null) {
			Set<AmountBean> composedAmounts = AmountBean.getComposedAmounts(amounts);
			if (!composedAmounts.isEmpty())
				amounts = composedAmounts;
			for (AmountBean amount : amounts) {
				if (!amountTypes.contains(amount.getAmountType())) {
					amountTypes.add(amount.getAmountType());
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
		List<AmountBean> ret = new ArrayList<AmountBean>();
		final Set<AmountBean> amounts2 = getAmounts();
		for (AmountBean amountBean : amounts2) {
			if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
				if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectTag)) {
					if (!amountBean.isComposed() && !ret.contains(amountBean))
						ret.add(amountBean);
				}
			}
		}
		return ret;
	}

	public Set<String> getDifferentSequences() {
		return differentSequences;
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
	 * @param differentSequences
	 *            the differentSequences to set
	 */
	public void setDifferentSequences(Set<String> differentSequences) {
		this.differentSequences = differentSequences;
	}

	/**
	 * @return the evidence
	 */
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	/**
	 * @param evidence
	 *            the evidence to set
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
		StringBuilder sb = new StringBuilder();
		for (String function : functions) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(function.replace("\n", ""));
		}
		return sb.toString();
	}

	public void addFunction(String function) {
		if (functions == null)
			functions = new HashSet<String>();
		functions.add(function);
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
			List<UniprotFeatureBean> list = new ArrayList<UniprotFeatureBean>();
			list.add(uniprotFeature);
			uniprotFeatures.put(uniprotFeature.getFeatureType(), list);
		}
	}

	public String getSecondaryAccessionsString() {
		List<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		final Set<AccessionBean> secondaryAccessions2 = getSecondaryAccessions();
		if (secondaryAccessions2 != null) {
			for (AccessionBean accessionBean : secondaryAccessions2) {
				final String accession = accessionBean.getAccession();
				if (!list.contains(accession) && !getPrimaryAccession().getAccession().equals(accession))
					list.add(accession);
			}
		}
		Collections.sort(list);
		for (String acc : list) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(acc);
		}
		return sb.toString();
	}

	@Override
	public String getId() {
		return getPrimaryAccession().getAccession();
	}

	@Override
	public List<AccessionBean> getPrimaryAccessions() {
		List<AccessionBean> list = new ArrayList<AccessionBean>();
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
	 * Returns true if the {@link ProteinBean} has been identified in the
	 * provided projects, which means that at least one {@link AmountBean} or
	 * one {@link RatioBean} points to that project
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
			if (psms != null) {
				for (PSMBean psmBean : psms) {
					if (psmBean.isFromThisProject(projectTag)) {
						return true;
					}
				}
			}
		}
		return false;
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
	 * Creates a light version of a {@link ProteinBean}, without any list of
	 * {@link PSMBean} or PSMIds
	 *
	 * @return
	 */
	public ProteinBean cloneToLightProteinBean() {
		if (lightVersion == null) {
			lightVersion = new ProteinBean();

			// ret.psms.addAll(getPsms());

			lightVersion.alternativeNamesString = getAlternativeNamesString();
			lightVersion.amounts.addAll(getAmounts());
			lightVersion.amountsByExperimentalCondition.putAll(getAmountsByExperimentalCondition());
			lightVersion.amountsByMSRunID.putAll(getAmountsByMSRunID());
			lightVersion.annotations.addAll(getAnnotations());
			lightVersion.coverage = getCoverage();
			lightVersion.dbIds.addAll(dbIds);
			lightVersion.descriptionString = getDescriptionString();
			lightVersion.differentSequences.addAll(getDifferentSequences());
			lightVersion.setNumPeptides(getDifferentSequences().size());

			lightVersion.evidence = getEvidence();
			lightVersion.functions.addAll(getFunctions());
			lightVersion.genes.addAll(getGenes());
			lightVersion.geneString = getGeneString();
			lightVersion.length = getLength();
			lightVersion.msruns.addAll(getMsruns());
			lightVersion.mw = getMw();
			lightVersion.organism = getOrganism();
			lightVersion.pi = getPi();
			lightVersion.primaryAccession = getPrimaryAccession();
			lightVersion.proteinBeanUniqueIdentifier = getProteinBeanUniqueIdentifier();
			// ret.psmIds.addAll(psmIds);
			lightVersion.setNumPSMs(getPSMDBIds().size());

			lightVersion.getNumPSMsByCondition().putAll(getNumPSMsByCondition());
			// ret.psmIdsByCondition.putAll(getPsmIdsByCondition());
			// ret.psmIdsbyMSRun.putAll(getPsmIdsbyMSRun());
			lightVersion.ratios.addAll(getRatios());
			lightVersion.scores.putAll(getScores());
			lightVersion.ratiosByExperimentalcondition.putAll(getRatiosByExperimentalcondition());
			lightVersion.secondaryAccessions.addAll(getSecondaryAccessions());
			lightVersion.thresholds.addAll(getThresholds());
			lightVersion.conditions.addAll(conditions);
			lightVersion.omimEntries.putAll(getOmimEntries());
			lightVersion.uniprotFeatures.putAll(getUniprotFeatures());
			lightVersion.setUniprotProteinExistence(getUniprotProteinExistence());
			// allow peptides in proteins.
			// IMPORTANT: do it after cloning all the other features of the
			// protein
			for (PeptideBean peptideBean : getPeptides()) {
				final PeptideBean lightPeptide = peptideBean.cloneToLightPeptideBean();
				lightVersion.addPeptideToProtein(lightPeptide);
			}
			lightVersion.getPSMDBIds().addAll(getPSMDBIds());
			lightVersion.getPeptideDBIds().addAll(getPeptideDBIds());
			lightVersion.coverageArrayString = coverageArrayString;
			lightVersion.ratioDistributions = getRatioDistributions();
			lightVersion.reactomePathways.addAll(getReactomePathways());
		}
		return lightVersion;
	}

	@Override
	public List<ScoreBean> getRatioScoresByConditions(String condition1Name, String condition2Name, String projectTag,
			String ratioName, String ratioScoreName) {
		final List<RatioBean> ratiosByConditions = getRatiosByConditions(condition1Name, condition2Name, projectTag,
				ratioName, false);
		final List<ScoreBean> ratioScores = SharedDataUtils.getRatioScoreValues(condition1Name, condition2Name,
				ratiosByConditions, ratioScoreName);
		return ratioScores;
	}

	/**
	 * @return the conditions
	 */
	@Override
	public Set<ExperimentalConditionBean> getConditions() {
		return conditions;
	}

	/**
	 * @param conditions
	 *            the conditions to set
	 */
	public void setConditions(Set<ExperimentalConditionBean> conditions) {
		this.conditions = conditions;
	}

	public String getConditionsString() {
		return SharedDataUtils.getConditionString(this);
	}

	public void addOMIMEntries(Collection<OmimEntryBean> omimEntries) {
		for (OmimEntryBean omimEntry : omimEntries) {
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
	 * @param omimEntries
	 *            the omimEntries to set
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
		StringBuilder sb = new StringBuilder();

		List<Integer> sortedIDs = getOmimSortedIDs();
		for (Integer omimID : sortedIDs) {
			sb.append(omimID);
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public List<Integer> getOmimSortedIDs() {
		List<Integer> sortedIds = new ArrayList<Integer>();

		if (!omimEntries.isEmpty()) {
			final Set<Integer> keySet = omimEntries.keySet();
			for (Integer i : keySet) {
				sortedIds.add(i);
			}

			Collections.sort(sortedIds);

		}
		return sortedIds;
	}

	/**
	 * @param peptides
	 *            the peptides to set
	 */
	public void setPeptides(List<PeptideBean> peptides) {
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
	 * @param uniprotProteinExistence
	 *            the uniprotProteinExistence to set
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
	 * @param coverageArrayString
	 *            the coverageArrayString to set
	 */
	public void setCoverageArrayString(char[] coverageArrayString) {
		this.coverageArrayString = coverageArrayString;
	}

	public void addEnsemblID(String ensemblAnnotation) {
		// this could come like: protein sequence ID=ENSP00000301659,gene
		// ID=ENSG00000167914
		if (ensemblAnnotation.contains("gene ID")) {
			String[] split = new String[1];
			if (ensemblAnnotation.contains(ProteinImplFromUniprotEntry.ANNOTATION_SEPARATOR)) {
				split = ensemblAnnotation.split(ProteinImplFromUniprotEntry.ANNOTATION_SEPARATOR);
			} else {
				split[0] = ensemblAnnotation;
			}
			for (String string : split) {
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

	public void addMsrun(MSRunBean msRun) {
		msruns.add(msRun);

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
	 * @param uniprotFeatures
	 *            the uniprotFeatures to set
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
	 * @param reactomePathways
	 *            the reactomePathways to set
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

	@Override
	public Set<Integer> getPeptideDBIds() {
		return peptideDBIds;
	}

	/**
	 * @param peptideDBIds
	 *            the peptideDBIds to set
	 */
	public void setPeptideDBIds(Set<Integer> peptideDBIds) {
		this.peptideDBIds = peptideDBIds;
	}

	public ProteinBean getLightVersion() {
		return lightVersion;
	}

	public void setLightVersion(ProteinBean lightVersion) {
		this.lightVersion = lightVersion;
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
		if (!numPSMsByCondition.isEmpty()) {
			for (ExperimentalConditionBean conditionBean : numPSMsByCondition.keySet()) {
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
			Map<ExperimentalConditionBean, Set<Integer>> psmdbIdsByCondition = getPSMDBIdsByCondition();
			for (ExperimentalConditionBean conditionBean : psmdbIdsByCondition.keySet()) {
				numPSMsByCondition.put(conditionBean, psmdbIdsByCondition.get(conditionBean).size());
			}
		}
		return numPSMsByCondition;
	}
}
