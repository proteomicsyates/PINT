package edu.scripps.yates.shared.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.shared.GWT;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AccessionType;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.GeneBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.OmimEntryBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinAnnotationBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.ThresholdBean;
import edu.scripps.yates.shared.model.UniprotFeatureBean;
import edu.scripps.yates.shared.model.interfaces.ContainsConditions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class SharedDataUtil {
	public static final Map<FileNameWithTypeBean, FileTypeBean> excelBeansByExcelFileWithFormatBeansMap = new HashMap<FileNameWithTypeBean, FileTypeBean>();
	public static final String EMPTY_VALUE = "-";

	/**
	 * Gets the scores associated to the ratios
	 *
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param ratios
	 * @param ratioScoreName
	 * @return
	 */
	public static List<ScoreBean> getRatioScoreValues(String condition1Name, String condition2Name,
			List<RatioBean> ratios, String ratioScoreName) {
		final List<ScoreBean> ret = new ArrayList<ScoreBean>();
		for (final RatioBean ratio : ratios) {
			if (ratio.getCondition1().getId().equalsIgnoreCase(condition1Name)
					&& ratio.getCondition2().getId().equalsIgnoreCase(condition2Name)) {
				if (ratio.getAssociatedConfidenceScore() != null
						&& ratio.getAssociatedConfidenceScore().getScoreName().equals(ratioScoreName)) {
					ret.add(ratio.getAssociatedConfidenceScore());
				}

			} else if (ratio.getCondition1().getId().equalsIgnoreCase(condition2Name)
					&& ratio.getCondition2().getId().equalsIgnoreCase(condition1Name)) {
				if (ratio.getAssociatedConfidenceScore() != null
						&& ratio.getAssociatedConfidenceScore().getScoreName().equals(ratioScoreName)) {
					ret.add(ratio.getAssociatedConfidenceScore());
				}
			}
		}
		return ret;
	}

	public static String getConditionString(ContainsConditions containsConditions) {
		final List<String> conditions2 = new ArrayList<String>();
		for (final ExperimentalConditionBean condition : containsConditions.getConditions()) {
			conditions2.add(condition.getId());
		}

		Collections.sort(conditions2);
		final StringBuilder sb = new StringBuilder();
		for (final String string : conditions2) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(string);
		}
		return sb.toString();
	}

	/**
	 * Gets the values of the ratios applying the negative if necessary since all
	 * ratios are log2 ratios
	 *
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param ratios1
	 * @return
	 */
	public static List<Double> getRatioValues(String condition1Name, String condition2Name, List<RatioBean> ratios1) {
		final List<Double> ret = new ArrayList<Double>();
		for (final RatioBean ratio : ratios1) {
			if (ratio.getCondition1().getId().equalsIgnoreCase(condition1Name)
					&& ratio.getCondition2().getId().equalsIgnoreCase(condition2Name)) {
				ret.add(ratio.getValue());
			} else if (ratio.getCondition1().getId().equalsIgnoreCase(condition2Name)
					&& ratio.getCondition2().getId().equalsIgnoreCase(condition1Name)) {
				ret.add(-ratio.getValue());
			}
		}
		return ret;
	}

	/**
	 * If all the ratios in the collection are INF, it will return INF. If all are
	 * -INF, it will return -INF
	 *
	 * @param ratioValues
	 * @return
	 */
	public static Double getEfectiveRatio(Collection<Double> ratioValues) {
		// look if the ratios are infinity or -infinity
		boolean areAllPlusINF = true;
		for (final Double ratioValue : ratioValues) {
			if (Double.compare(Double.POSITIVE_INFINITY, ratioValue) != 0
					&& Double.compare(Double.MAX_VALUE, ratioValue) != 0) {
				areAllPlusINF = false;
				break;
			}
		}
		if (areAllPlusINF) {
			return Double.POSITIVE_INFINITY;
		}
		boolean areAllMinusINF = true;
		for (final Double ratioValue : ratioValues) {
			if (Double.compare(Double.NEGATIVE_INFINITY, ratioValue) != 0
					&& Double.compare(-Double.MAX_VALUE, ratioValue) != 0) {
				areAllMinusINF = false;
				break;
			}
		}
		if (areAllMinusINF) {
			return Double.NEGATIVE_INFINITY;
		}
		return null;
	}

	public static boolean isNumber(String potentialNumber) {
		try {
			Double.valueOf(potentialNumber);
			return true;
		} catch (final NumberFormatException e) {

		}
		return false;
	}

	/**
	 * If element is 1C-ConditionName, then, <br>
	 * the 1 is the project, and the C is the condition and ConditionName is the
	 * condition name.<br>
	 * if element is 1AC-ConditionName, then, the 1 is the project and the AC is the
	 * condition and ConditionName is the condition name.
	 *
	 * @param conditionSelection
	 * @return the ConditionName
	 */
	public static String parseConditionNameFromConditionSelection(String conditionSelection) {
		if (conditionSelection.contains("-")) {
			return conditionSelection.split("-")[1];
		}
		return conditionSelection;
	}

	public static Comparator<AccessionBean> getComparatorByAccession() {
		final Comparator<AccessionBean> ret = new Comparator<AccessionBean>() {

			@Override
			public int compare(AccessionBean o1, AccessionBean o2) {
				if (o1 != null && o2 != null) {
					String acc1 = o1.getAccession();
					if (acc1 == null) {
						acc1 = "";
					}
					String acc2 = o2.getAccession();
					if (acc2 == null) {
						acc2 = "";
					}
					return acc1.compareTo(acc2);
				}
				return 0;
			}
		};
		return ret;
	}

	public static Comparator<ProteinBean> getComparatorByPrymaryAcc() {

		final Comparator<ProteinBean> comparatorByProteinPrimaryAcc = new Comparator<ProteinBean>() {
			@Override
			public int compare(ProteinBean o1, ProteinBean o2) {
				return o1.getPrimaryAccession().getAccession().compareTo(o2.getPrimaryAccession().getAccession());
			}
		};

		return comparatorByProteinPrimaryAcc;
	}

	public static Comparator<ProteinBeanLight> getComparatorByPrymaryAccForLightProteins() {

		final Comparator<ProteinBeanLight> comparatorByProteinPrimaryAcc = new Comparator<ProteinBeanLight>() {
			@Override
			public int compare(ProteinBeanLight o1, ProteinBeanLight o2) {
				return o1.getPrimaryAccession().getAccession().compareTo(o2.getPrimaryAccession().getAccession());
			}
		};

		return comparatorByProteinPrimaryAcc;
	}

	public static Collection<String> getPrimaryAccessions(Collection<ProteinBean> proteinMap, AccessionType accType) {
		final Set<String> ret = new HashSet<String>();
		if (proteinMap != null) {
			for (final ProteinBean proteinBean : proteinMap) {
				final Set<AccessionBean> accessions = proteinBean.getAccessions(accType);
				for (final AccessionBean accessionBean : accessions) {
					if (accessionBean.isPrimaryAccession())
						ret.add(accessionBean.getAccession());
				}
			}
		}
		return ret;
	}

	public static List<RatioBean> getRatiosByConditions(Collection<RatioBean> ratios, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities) {
		// sometimes, for the render of the ratio graphs, the ratioName comes
		// with the name of the column and then the ratio name between
		// parenthesis.
		// so in that case, we want it to parse it
		if (ratioName != null && ratioName.contains(ColumnName.PROTEIN_RATIO_GRAPH.getName() + " (")) {
			ratioName = ratioName.replace(ColumnName.PROTEIN_RATIO_GRAPH.getName() + " (", "");
			if (ratioName.endsWith(")")) {
				ratioName = ratioName.substring(0, ratioName.length() - 1);
			}
		}
		final Set<Integer> ratioIDs = new HashSet<Integer>();
		final List<RatioBean> ret = new ArrayList<RatioBean>();
		if (ratios != null) {
			for (final RatioBean ratioBean : ratios) {

				if (ratioName != null && !ratioName.equals(ratioBean.getDescription()))
					continue;
				if (skipInfinities && Double.isInfinite(ratioBean.getValue()))
					continue;

				if (ratioIDs.contains(ratioBean.getDbID()))
					continue;
				if (ratioBean.getCondition1().getProject().getTag().equalsIgnoreCase(projectTag)
						&& ratioBean.getCondition2().getProject().getTag().equalsIgnoreCase(projectTag)) {
					if (ratioBean.getCondition1().getId().equalsIgnoreCase(condition1Name)
							&& ratioBean.getCondition2().getId().equalsIgnoreCase(condition2Name)) {
						ret.add(ratioBean);
						ratioIDs.add(ratioBean.getDbID());
					} else if (ratioBean.getCondition1().getId().equalsIgnoreCase(condition2Name)
							&& ratioBean.getCondition2().getId().equalsIgnoreCase(condition1Name)) {
						ret.add(ratioBean);
						ratioIDs.add(ratioBean.getDbID());
					}
				}
			}
		}
		return sortRatiosByID(ret);
	}

	private static List<RatioBean> sortRatiosByID(List<RatioBean> unsortedRatios) {
		final List<RatioBean> ret = new ArrayList<RatioBean>();
		ret.addAll(unsortedRatios);

		Collections.sort(ret, new Comparator<RatioBean>() {

			@Override
			public int compare(RatioBean o1, RatioBean o2) {
				return Integer.compare(o1.getDbID(), o2.getDbID());
			}
		});
		return ret;
	}

	/**
	 * Gets a unique string key for the collection of project tags by sorting them
	 * and returning the concatenated string of them
	 *
	 * @param projectTags
	 * @return
	 */
	public static String getProjectTagCollectionKey(Collection<String> projectTags, String separator) {
		final List<String> list = new ArrayList<String>();
		list.addAll(projectTags);
		Collections.sort(list);
		String ret = "";
		for (final String string : list) {
			if (separator != null && ret.equals("")) {
				ret += separator;
			}
			ret += string;
		}
		return ret;
	}

	/**
	 * Gets a unique string key for the collection of project tags by sorting them
	 * and returning the concatenated string of them
	 *
	 * @param projectTags
	 * @return
	 */
	public static String getProjectTagCollectionKey(Collection<String> projectTags) {
		return getProjectTagCollectionKey(projectTags, null);
	}

	/**
	 * Gets a Map of conditions by projects from a collections of ratios
	 *
	 * @param ratios
	 * @return
	 */
	public static Map<String, List<String>> getConditionsByProjectsFromRatios(Collection<RatioBean> ratios) {
		final Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (ratios != null) {
			for (final RatioBean ratioBean : ratios) {
				final ExperimentalConditionBean experimentalCondition1 = ratioBean.getCondition1();
				mergeStringMapsWithNoRepetitionsOnList(ret, getConditionsByProject(experimentalCondition1));
				final ExperimentalConditionBean experimentalCondition2 = ratioBean.getCondition2();
				mergeStringMapsWithNoRepetitionsOnList(ret, getConditionsByProject(experimentalCondition2));

			}
		}
		return ret;
	}

	/**
	 * GEts a Map of conditions by projects from a collection of amounts
	 *
	 * @param amounts
	 * @return
	 */
	public static Map<String, List<String>> getConditionsByProjectsFromAmounts(Collection<AmountBean> amounts) {
		final Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (amounts != null) {
			for (final AmountBean amountBean : amounts) {
				final ExperimentalConditionBean experimentalCondition = amountBean.getExperimentalCondition();
				mergeStringMapsWithNoRepetitionsOnList(ret, getConditionsByProject(experimentalCondition));

			}
		}
		return ret;
	}

	/**
	 * Adds donor data to a receiver map assuring that the List<String> doesn't
	 * contain duplicates.
	 *
	 * @param receiver
	 * @param donor
	 */
	public static void mergeStringMapsWithNoRepetitionsOnList(Map<String, List<String>> receiver,
			Map<String, List<String>> donor) {
		for (final String key : donor.keySet()) {
			final List<String> list = donor.get(key);
			if (receiver.containsKey(key)) {
				for (final String item : list) {
					if (!receiver.get(key).contains(item))
						receiver.get(key).add(item);
				}
			} else {
				final List<String> list2 = new ArrayList<String>();
				list2.addAll(list);
				receiver.put(key, list2);
			}
		}
	}

	/**
	 * Gets a Map of conditions by projects from an
	 * {@link ExperimentalConditionBean}
	 *
	 * @param experimentalCondition
	 * @return
	 */
	public static Map<String, List<String>> getConditionsByProject(ExperimentalConditionBean experimentalCondition) {
		final Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (experimentalCondition != null && experimentalCondition.getProject() != null) {
			if (ret.containsKey(experimentalCondition.getProject().getTag())) {
				if (!ret.get(experimentalCondition.getProject().getTag()).contains(experimentalCondition.getId())) {
					ret.get(experimentalCondition.getProject().getTag()).add(experimentalCondition.getId());
				}
			} else {
				final List<String> list = new ArrayList<String>();
				list.add(experimentalCondition.getId());
				ret.put(experimentalCondition.getProject().getTag(), list);
			}
		}
		return ret;
	}

	/**
	 * Adds data from donor to receiver assuring that the List of
	 * {@link RatioDescriptorBean} has no repeated elements
	 *
	 * @param receiver
	 * @param donor
	 */
	public static void mergeRatioDescriptorMapsWithNoRepetitionsOnList(Map<String, List<RatioDescriptorBean>> receiver,
			Map<String, List<RatioDescriptorBean>> donor) {
		for (final String key : donor.keySet()) {
			final List<RatioDescriptorBean> list = donor.get(key);
			if (receiver.containsKey(key)) {
				for (final RatioDescriptorBean item : list) {
					if (!receiver.get(key).contains(item))
						receiver.get(key).add(item);
				}
			} else {
				final List<RatioDescriptorBean> list2 = new ArrayList<RatioDescriptorBean>();
				list2.addAll(list);
				receiver.put(key, list2);
			}
		}

	}

	/**
	 * Gets a Map of {@link RatioDescriptorBean} by projects from a collection of
	 * {@link RatioBean}
	 *
	 * @param ratios
	 * @return
	 */
	public static Map<String, List<RatioDescriptorBean>> getRatioDescriptorsByProjectsFromRatios(
			Collection<RatioBean> ratios) {
		final Map<String, List<RatioDescriptorBean>> ret = new HashMap<String, List<RatioDescriptorBean>>();

		if (ratios != null) {
			for (final RatioBean ratioBean : ratios) {
				final RatioDescriptorBean ratioDescriptorBean = ratioBean.getRatioDescriptorBean();
				if (ret.containsKey(ratioDescriptorBean.getProjectTag())) {
					final List<RatioDescriptorBean> list = ret.get(ratioDescriptorBean.getProjectTag());
					if (!list.contains(ratioDescriptorBean))
						list.add(ratioDescriptorBean);
				} else {
					final List<RatioDescriptorBean> list = new ArrayList<RatioDescriptorBean>();
					list.add(ratioDescriptorBean);
					ret.put(ratioDescriptorBean.getProjectTag(), list);
				}

			}
		}
		return ret;
	}

	/**
	 * Gets a list of {@link ProteinBean} from the provided Collection of
	 * {@link ProteinBean} that belongs to a certain project which means that at
	 * least one amount or ratio has been measured in that project
	 *
	 * @param proteins
	 * @param projectTag
	 * @return
	 */
	public static List<ProteinBean> getProteinsFromProject(Collection<ProteinBean> proteins, String projectTag) {
		final List<ProteinBean> ret = new ArrayList<ProteinBean>();
		if (proteins != null) {
			for (final ProteinBean proteinBean : proteins) {
				if (proteinBean.isFromThisProject(projectTag)) {
					ret.add(proteinBean);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a list of {@link ProteinGroupBean} from the provided Collection of
	 * {@link ProteinGroupBean} that belongs to a certain project which means that
	 * at least one amount or ratio has been measured in that project
	 *
	 * @param proteinGroups
	 * @param projectTag
	 * @return
	 */
	public static List<ProteinGroupBean> getProteinGroupsFromProject(List<ProteinGroupBean> proteinGroups,
			String projectTag) {
		final List<ProteinGroupBean> ret = new ArrayList<ProteinGroupBean>();
		if (proteinGroups != null) {
			for (final ProteinGroupBean proteinGroup : proteinGroups) {
				if (proteinGroup.isFromThisProject(projectTag)) {
					ret.add(proteinGroup);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a List of {@link PSMBean} from a collection of {@link ProteinBean}. <br>
	 * Note that if the {@link ProteinBean} is not linked to any {@link PSMBean} the
	 * list will be empty
	 *
	 * @param proteinBeans
	 * @return
	 */
	public static List<PSMBean> getPSMBeansFromProteinBeans(Collection<ProteinBean> proteinBeans) {
		final List<PSMBean> ret = new ArrayList<PSMBean>();
		final Set<Integer> psmIDs = new HashSet<Integer>();
		if (proteinBeans != null) {
			for (final ProteinBean proteinBean : proteinBeans) {
				final List<PSMBean> psmBeans = proteinBean.getPsms();
				if (psmBeans != null) {
					for (final PSMBean psmBean : psmBeans) {
						if (!psmIDs.contains(psmBean.getDbID())) {
							psmIDs.add(psmBean.getDbID());
							ret.add(psmBean);
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a List of {@link ProteinBean} from a collection of {@link PSMBean}. <br>
	 * Note that if the {@link PSMBean} is not linked to any {@link ProteinBean} the
	 * list will be empty
	 *
	 * @param psmBeans
	 * @return
	 */
	public static List<ProteinBean> getProteinBeansFromPSMBeans(List<PSMBean> psmBeans) {
		final List<ProteinBean> ret = new ArrayList<ProteinBean>();
		final Set<String> proteinIDs = new HashSet<String>();
		if (psmBeans != null) {
			for (final PSMBean psmBean : psmBeans) {
				final Set<ProteinBean> proteinBeans = psmBean.getProteins();
				if (proteinBeans != null) {
					for (final ProteinBean proteinBean : proteinBeans) {
						if (!proteinIDs.contains(proteinBean.getId())) {
							proteinIDs.add(proteinBean.getId());
							ret.add(proteinBean);
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a List of {@link ProteinBean} from a collection of {@link PeptideBean}.
	 * <br>
	 * Note that if the {@link PeptideBean} is not linked to any {@link ProteinBean}
	 * the list will be empty
	 *
	 * @param peptideBeans
	 * @return
	 */
	public static List<ProteinBean> getProteinBeansFromPeptideBeans(List<PeptideBean> peptideBeans) {
		final List<ProteinBean> ret = new ArrayList<ProteinBean>();
		final Set<String> proteinIDs = new HashSet<String>();
		if (peptideBeans != null) {
			for (final PeptideBean peptideBean : peptideBeans) {
				final Set<ProteinBean> proteinBeans = peptideBean.getProteins();
				if (proteinBeans != null) {
					for (final ProteinBean proteinBean : proteinBeans) {
						if (!proteinIDs.contains(proteinBean.getId())) {
							proteinIDs.add(proteinBean.getId());
							ret.add(proteinBean);
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a non redundant List of {@link PeptideBean} from a collection of
	 * {@link ProteinBean}. <br>
	 * Note that if the {@link ProteinBean} is not linked to any {@link PeptideBean}
	 * the list will be empty
	 *
	 * @param proteinBeans
	 * @return
	 */
	public static List<PeptideBean> getPeptideBeansFromProteinBeans(Collection<ProteinBean> proteinBeans) {
		final List<PeptideBean> ret = new ArrayList<PeptideBean>();
		final Set<String> peptideIDs = new HashSet<String>();
		if (proteinBeans != null) {
			for (final ProteinBean proteinBean : proteinBeans) {
				final List<PeptideBean> peptideBeans = proteinBean.getPeptides();
				if (peptideBeans != null && !peptideBeans.isEmpty()) {
					for (final PeptideBean peptideBean : peptideBeans) {
						final String fullSequence = peptideBean.getFullSequence();
						if (!peptideIDs.contains(peptideBean.getId())) {
							peptideIDs.add(peptideBean.getId());
							ret.add(peptideBean);
						} else {
							GWT.log(fullSequence);
						}
						proteinBean.addDifferentSequence(fullSequence);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Gets the {@link Peptide} objects from the {@link Protein}s and organize them
	 * in a {@link Map} using the sequence as the key
	 *
	 * @param proteins
	 * @return
	 */
	public static Map<String, Set<PeptideBean>> getPeptideMapFromProteins(Collection<ProteinBean> proteins) {
		final Map<String, Set<PeptideBean>> ret = new HashMap<String, Set<PeptideBean>>();

		for (final ProteinBean protein : proteins) {
			final List<PeptideBean> peptides = protein.getPeptides();
			for (final PeptideBean peptide : peptides) {
				final String sequence = peptide.getSequence();
				if (ret.containsKey(sequence)) {
					ret.get(sequence).add(peptide);
				} else {
					final Set<PeptideBean> set = new HashSet<PeptideBean>();
					set.add(peptide);
					ret.put(sequence, set);
				}
			}
		}

		return ret;
	}

	/**
	 * Merge one {@link ProteinBean} in the other. Any PSM, peptide or any other
	 * attribute of the proteinBeanDonor will be added to the proteinBeanReceiver
	 *
	 * @param proteinBeanReceiver
	 * @param proteinBeanDonor
	 */
	public static void mergeProteinBeans(ProteinBean proteinBeanReceiver, ProteinBean proteinBeanDonor) {
		// only merge if they have the same primary accession
		if (!proteinBeanReceiver.getPrimaryAccession().getAccession()
				.equals(proteinBeanDonor.getPrimaryAccession().getAccession())) {
			return;
		}
		for (final AmountBean amount : proteinBeanDonor.getAmounts()) {
			proteinBeanReceiver.addAmount(amount);
		}
		for (final ProteinAnnotationBean annotation : proteinBeanDonor.getAnnotations()) {
			proteinBeanReceiver.getAnnotations().add(annotation);
		}
		final Set<Integer> dbIds = proteinBeanDonor.getDbIds();
		for (final Integer dbId : dbIds) {
			proteinBeanReceiver.addDbId(dbId);

		}

		final Set<String> functions = proteinBeanDonor.getFunctions();
		for (final String function : functions) {
			proteinBeanReceiver.addFunction(function);
		}

		final Collection<List<UniprotFeatureBean>> uniprotFeatures = proteinBeanDonor.getUniprotFeatures().values();
		for (final List<UniprotFeatureBean> uniprotFeatureSet : uniprotFeatures) {
			for (final UniprotFeatureBean uniprotFeature : uniprotFeatureSet) {
				proteinBeanReceiver.addUniprotFeature(uniprotFeature);
			}

		}

		// reset geneString in order to be rebuilt after adding new genes
		proteinBeanReceiver.setGeneString(null);
		final Set<GeneBean> genes = proteinBeanDonor.getGenes();
		for (final GeneBean geneBean : genes) {
			proteinBeanReceiver.addGene(geneBean);
		}

		final Set<MSRunBean> msruns = proteinBeanDonor.getMsruns();
		for (final MSRunBean msRunBean : msruns) {
			proteinBeanReceiver.addMsrun(msRunBean);
		}
		// reset the number of PSMs to be recalculated after adding the new PSMs
		proteinBeanReceiver.setNumPSMs(0);
		final List<PSMBean> psms = proteinBeanDonor.getPsms();
		for (final PSMBean psm : psms) {
			proteinBeanReceiver.addPSMtoProtein(psm);
		}

		final Set<RatioBean> ratios = proteinBeanDonor.getRatios();
		for (final RatioBean ratioBean : ratios) {
			proteinBeanReceiver.addProteinRatio(ratioBean);
		}

		final Set<ThresholdBean> thresholds = proteinBeanDonor.getThresholds();
		for (final ThresholdBean thresholdBean : thresholds) {
			proteinBeanReceiver.addThreshold(thresholdBean);
		}
		final Set<ExperimentalConditionBean> conditions = proteinBeanDonor.getConditions();
		for (final ExperimentalConditionBean experimentalConditionBean : conditions) {
			proteinBeanReceiver.addCondition(experimentalConditionBean);
		}
		final Map<Integer, OmimEntryBean> omimEntries = proteinBeanDonor.getOmimEntries();
		for (final OmimEntryBean omimEntry : omimEntries.values()) {
			proteinBeanReceiver.addOMIMEntry(omimEntry);
		}

		// reset numPeptide to force to get the actual number after adding the
		// donor different sequences
		proteinBeanReceiver.setNumPeptides(0);
		proteinBeanReceiver.addDifferentSequences(proteinBeanDonor.getDifferentSequences());

		for (final PeptideBean peptideBean : proteinBeanDonor.getPeptides()) {
			proteinBeanReceiver.addPeptideToProtein(peptideBean);
		}
	}

	/**
	 * Gets a unique key for the ratio bean, including the ratio name, the condition
	 * names, the project and the aggregation level
	 *
	 * @param ratio
	 * @return
	 */
	public static String getRatioKey(RatioBean ratio) {
		final String key = new StringBuilder().append(ratio.getDescription()).append(ratio.getCondition1().getId())
				.append(ratio.getCondition2().getId()).append(ratio.getCondition1().getProject().getTag())
				.append(ratio.getRatioDescriptorBean().getAggregationLevel().name()).toString();
		return key;
	}

	public static String getAmountHeader(AmountType amountType, String conditionID) {
		return amountType + " (" + conditionID + ")";
	}

	public static String getAmountHeaderTooltip(AmountType amountType, String conditionName, String projectTag) {
		return "Amount type: " + amountType + SharedConstants.SEPARATOR + "Experimental condition: " + conditionName
				+ SharedConstants.SEPARATOR + "Project: " + projectTag;
	}

	public static String getRatioHeader(String ratioName, String condition1ID, String condition2ID) {

		return ratioName + " (" + condition1ID + " / " + condition2ID + ")";
	}

	public static String getRatioScoreHeader(String scoreName, String ratioName, String condition1ID,
			String condition2ID) {

		return scoreName + " (" + ratioName + "#" + condition1ID + " / " + condition2ID + ")";
	}

	public static String getRatioHeaderTooltip(ColumnName columnName, String condition1Name, String condition2Name,
			String ratioName) {

		if (columnName == ColumnName.PSM_RATIO_GRAPH || columnName == ColumnName.PROTEIN_RATIO_GRAPH
				|| columnName == ColumnName.PEPTIDE_RATIO_GRAPH) {
			return "Graphical representation of the ratio between conditions: " + condition1Name + " / "
					+ condition2Name + SharedConstants.SEPARATOR + "Ratio name: " + ratioName;
		}
		return "Ratio between conditions: " + condition1Name + " / " + condition2Name + SharedConstants.SEPARATOR
				+ "Ratio name: " + ratioName;
	}

	public static String getRatioScoreHeaderTooltip(ColumnName columnName, String condition1Name, String condition2Name,
			String ratioName, String scoreName) {

		if (columnName == ColumnName.PSM_RATIO_GRAPH || columnName == ColumnName.PROTEIN_RATIO_GRAPH
				|| columnName == ColumnName.PEPTIDE_RATIO_GRAPH) {
			return "Graphical representation of the ratio between conditions: " + condition1Name + " / "
					+ condition2Name + SharedConstants.SEPARATOR + "Ratio name: " + ratioName;
		}
		return "Ratio score: " + scoreName + SharedConstants.SEPARATOR + "Ratio name: " + ratioName
				+ SharedConstants.SEPARATOR + "Ratio between conditions: " + condition1Name + " / " + condition2Name;
	}

	public static List<PSMBean> getPSMBeansFromProteinBeans(List<ProteinBean> proteinBeans) {
		final List<PSMBean> ret = new ArrayList<PSMBean>();
		final Set<Integer> psmIDs = new HashSet<Integer>();
		if (proteinBeans != null) {
			for (final ProteinBean proteinBean : proteinBeans) {
				final List<PSMBean> psmBeans = proteinBean.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(proteinBean, false);
				for (final PSMBean psmBean : psmBeans) {
					if (!psmIDs.contains(psmBean.getDbID())) {
						psmIDs.add(psmBean.getDbID());
						ret.add(psmBean);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Get a Map of {@link RatioDescriptorBean} by project from a collection of
	 * {@link ProteinBean}
	 *
	 * @param currentProteins
	 * @return
	 */
	public static Map<String, List<RatioDescriptorBean>> getRatioDescriptorsByProjectsFromProteins(
			Collection<ProteinBean> proteins) {
		final Map<String, List<RatioDescriptorBean>> ret = new HashMap<String, List<RatioDescriptorBean>>();
		for (final ProteinBean proteinBean : proteins) {
			final Set<RatioBean> ratios = proteinBean.getRatios();
			if (ratios != null && !ratios.isEmpty()) {
				mergeRatioDescriptorMapsWithNoRepetitionsOnList(ret, getRatioDescriptorsByProjectsFromRatios(ratios));
			}
			final List<PSMBean> psms = proteinBean.getPsms();
			// RemoteServicesTasks.getPSMsFromProtein(proteinBean, false);
			for (final PSMBean psmBean : psms) {
				final Set<RatioBean> ratios2 = psmBean.getRatios();
				if (ratios2 != null) {
					mergeRatioDescriptorMapsWithNoRepetitionsOnList(ret,
							getRatioDescriptorsByProjectsFromRatios(ratios2));
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a alphabetically sorted list of score names associated to psms
	 *
	 * @param protein
	 * @return
	 */
	public static List<String> getPSMScoreNamesFromProteins(Collection<ProteinBean> proteins) {
		final List<String> ret = new ArrayList<String>();
		if (proteins != null) {
			for (final ProteinBean protein : proteins) {
				final List<PSMBean> psms = protein.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(protein, false);
				for (final PSMBean psmBean : psms) {
					final Map<String, ScoreBean> scores = psmBean.getScores();
					if (scores != null) {
						final Set<String> scoreNames = scores.keySet();
						for (final String scoreName : scoreNames) {
							if (!ret.contains(scoreName))
								ret.add(scoreName);
						}
					}
				}
			}
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * Gets a alphabetically sorted list of score names associated to psms
	 *
	 * @param protein
	 * @return
	 */
	public static List<String> getPeptideScoreNamesFromProteins(Collection<ProteinBean> proteins) {
		final List<String> ret = new ArrayList<String>();
		if (proteins != null) {
			for (final ProteinBean protein : proteins) {
				final List<PeptideBean> peptides = protein.getPeptides();
				// RemoteServicesTasks .getPSMsFromProtein(protein, false);
				for (final PeptideBean peptideBean : peptides) {
					final Map<String, ScoreBean> scores = peptideBean.getScores();
					if (scores != null) {
						final Set<String> scoreNames = scores.keySet();
						for (final String scoreName : scoreNames) {
							if (!ret.contains(scoreName))
								ret.add(scoreName);
						}
					}
				}
			}
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * Gets a alphabetically sorted list of score names associated to proteins
	 *
	 * @param protein
	 * @return
	 */
	public static List<String> getProteinScoreNamesFromProteins(Collection<ProteinBean> proteins) {
		final List<String> ret = new ArrayList<String>();
		if (proteins != null) {
			for (final ProteinBean protein : proteins) {

				final Map<String, ScoreBean> scores = protein.getScores();
				if (scores != null) {
					final Set<String> scoreNames = scores.keySet();
					for (final String scoreName : scoreNames) {
						if (!ret.contains(scoreName))
							ret.add(scoreName);
					}
				}

			}
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * Gets a alphabetically sorted list of score names associated to PTMs of psms
	 *
	 * @param protein
	 * @return
	 */
	public static List<String> getPTMScoreNamesFromProteins(Collection<ProteinBean> proteins) {
		final List<String> ret = new ArrayList<String>();
		if (proteins != null) {
			for (final ProteinBean protein : proteins) {
				final List<PSMBean> psms = protein.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(protein, false);
				for (final PSMBean psmBean : psms) {
					final List<PTMBean> ptms = psmBean.getPtms();
					if (ptms != null) {
						for (final PTMBean ptmBean : ptms) {
							final List<PTMSiteBean> ptmSites = ptmBean.getPtmSites();
							if (ptmSites != null) {
								for (final PTMSiteBean ptmSiteBean : ptmSites) {
									final ScoreBean score = ptmSiteBean.getScore();
									if (score != null) {
										if (!ret.contains(score.getScoreName()))
											ret.add(score.getScoreName());
									}
								}
							}
						}
					}
				}
			}
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * Gets a Map of conditions by projects from the amounts and ratios of a
	 * collections of proteins, looking also on amounts and ratios of their PSMs
	 *
	 * @param currentProteins
	 * @return
	 */
	public static Map<String, List<String>> getConditionsByProjects(Collection<ProteinBean> proteins) {
		final Map<String, List<String>> ret = new HashMap<String, List<String>>();

		if (proteins != null) {
			for (final ProteinBean proteinBean : proteins) {
				mergeStringMapsWithNoRepetitionsOnList(ret,
						getConditionsByProjectsFromAmounts(proteinBean.getAmounts()));
				mergeStringMapsWithNoRepetitionsOnList(ret, getConditionsByProjectsFromRatios(proteinBean.getRatios()));
				final List<PSMBean> psmBeans = proteinBean.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(proteinBean, true);
				if (psmBeans != null) {
					for (final PSMBean psmBean : psmBeans) {
						mergeStringMapsWithNoRepetitionsOnList(ret,
								getConditionsByProjectsFromAmounts(psmBean.getAmounts()));
						mergeStringMapsWithNoRepetitionsOnList(ret,
								getConditionsByProjectsFromRatios(psmBean.getRatios()));
					}
				}
			}
		}

		return ret;
	}

	public static int getMinStartingPosition(PSMBean o1) {
		int min = Integer.MAX_VALUE;
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = o1.getStartingPositions();
		for (final List<Pair<Integer, Integer>> positions : startingPositions.values()) {
			for (final Pair<Integer, Integer> startAndEnd : positions) {
				final int position = startAndEnd.getFirstElement();
				if (min > position)
					min = position;
			}
		}

		return min;
	}

	public static Map<String, ProteinBean> getProteinBeansByPrimaryAccession(Set<ProteinBean> proteins) {
		final Map<String, ProteinBean> ret = new HashMap<String, ProteinBean>();
		for (final ProteinBean proteinBean : proteins) {
			ret.put(proteinBean.getPrimaryAccession().getAccession(), proteinBean);
		}
		return ret;
	}

	public static Map<String, ProteinBeanLight> getLightProteinBeansByPrimaryAccession(Set<ProteinBeanLight> proteins) {
		final Map<String, ProteinBeanLight> ret = new HashMap<String, ProteinBeanLight>();
		for (final ProteinBeanLight proteinBean : proteins) {
			ret.put(proteinBean.getPrimaryAccession().getAccession(), proteinBean);
		}
		return ret;
	}

	public static String getUniprotFeatureString(PSMBean p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBean> proteins = p.getProteins();
		final Map<String, ProteinBean> proteinBeanByAccession = getProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		final List<ProteinBean> proteinBeanList = new ArrayList<ProteinBean>();
		for (final AccessionBean acc : primaryAccessions) {
			if (proteinBeanByAccession.containsKey(acc.getAccession())) {
				proteinBeanList.add(proteinBeanByAccession.get(acc.getAccession()));
			}
		}
		return getUniprotFeatureString(startingPositions, proteinBeanList, featureTypes);
	}

	public static String getUniprotFeatureString(PeptideBean p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBean> proteins = p.getProteins();
		final Map<String, ProteinBean> proteinBeanByAccession = getProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		final List<ProteinBean> proteinBeanList = new ArrayList<ProteinBean>();
		for (final AccessionBean acc : primaryAccessions) {
			if (proteinBeanByAccession.containsKey(acc.getAccession())) {
				proteinBeanList.add(proteinBeanByAccession.get(acc.getAccession()));
			}
		}
		return getUniprotFeatureString(startingPositions, proteinBeanList, featureTypes);
	}

	public static String getUniprotFeatureString(ProteinBean p, String... featureTypes) {
		final Set<UniprotFeatureBean> uniprotFeatures = new HashSet<UniprotFeatureBean>();
		for (final String featureType : featureTypes) {
			uniprotFeatures.addAll(p.getUniprotFeaturesByFeatureType(featureType));
		}
		final StringBuilder sb = new StringBuilder();
		for (final UniprotFeatureBean uniprotFeature : uniprotFeatures) {
			if (uniprotFeature.getDescription() != null) {
				sb.append(uniprotFeature.getDescription());
			} else {
				sb.append(uniprotFeature.getFeatureType());
			}
			if (uniprotFeature.getPositionStart() > -1) {
				if (uniprotFeature.getPositionStart() == uniprotFeature.getPositionEnd()) {
					sb.append(" (").append(uniprotFeature.getPositionStart()).append(")");
				} else {
					sb.append(" (").append(uniprotFeature.getPositionStart()).append("-")
							.append(uniprotFeature.getPositionEnd()).append(")");
				}
			}
		}
		return sb.toString();
	}

	public static String getUniprotFeatureString(PSMBeanLight p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBeanLight> proteins = p.getProteins();
		final Map<String, ProteinBeanLight> proteinBeanByAccession = getLightProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		final List<ProteinBeanLight> proteinBeanList = new ArrayList<ProteinBeanLight>();
		for (final AccessionBean acc : primaryAccessions) {
			if (proteinBeanByAccession.containsKey(acc.getAccession())) {
				proteinBeanList.add(proteinBeanByAccession.get(acc.getAccession()));
			}
		}
		return getUniprotFeatureStringFromLightProteins(startingPositions, proteinBeanList, featureTypes);
	}

	public static String getUniprotFeatureString(PeptideBeanLight p, String... featureTypes) {
		final Map<String, List<Pair<Integer, Integer>>> startingPositions = p.getStartingPositions();
		final List<AccessionBean> primaryAccessions = p.getPrimaryAccessions();
		final Set<ProteinBeanLight> proteins = p.getProteins();
		final Map<String, ProteinBeanLight> proteinBeanByAccession = getLightProteinBeansByPrimaryAccession(proteins);
		// get a list of proteins according to the order of the primary
		// accessions
		final List<ProteinBeanLight> proteinBeanList = new ArrayList<ProteinBeanLight>();
		for (final AccessionBean acc : primaryAccessions) {
			if (proteinBeanByAccession.containsKey(acc.getAccession())) {
				proteinBeanList.add(proteinBeanByAccession.get(acc.getAccession()));
			}
		}
		return getUniprotFeatureStringFromLightProteins(startingPositions, proteinBeanList, featureTypes);
	}

	public static String getUniprotFeatureString(ProteinBeanLight p, String... featureTypes) {
		final Set<UniprotFeatureBean> uniprotFeatures = new HashSet<UniprotFeatureBean>();
		for (final String featureType : featureTypes) {
			uniprotFeatures.addAll(p.getUniprotFeaturesByFeatureType(featureType));
		}
		final StringBuilder sb = new StringBuilder();
		for (final UniprotFeatureBean uniprotFeature : uniprotFeatures) {
			if (uniprotFeature.getDescription() != null) {
				sb.append(uniprotFeature.getDescription());
			} else {
				sb.append(uniprotFeature.getFeatureType());
			}
			if (uniprotFeature.getPositionStart() > -1) {
				if (uniprotFeature.getPositionStart() == uniprotFeature.getPositionEnd()) {
					sb.append(" (").append(uniprotFeature.getPositionStart()).append(")");
				} else {
					sb.append(" (").append(uniprotFeature.getPositionStart()).append("-")
							.append(uniprotFeature.getPositionEnd()).append(")");
				}
			}
		}
		return sb.toString();
	}

	private static String getUniprotFeatureString(Map<String, List<Pair<Integer, Integer>>> startingPositionsByProtein,
			List<ProteinBean> proteinBeans, String... featureTypes) {
		final StringBuilder sb = new StringBuilder();
		for (final ProteinBean p : proteinBeans) {
			if (startingPositionsByProtein.containsKey(p.getPrimaryAccession().getAccession())) {
				final Set<UniprotFeatureBean> uniprotFeatures = new HashSet<UniprotFeatureBean>();
				for (final String featureType : featureTypes) {
					uniprotFeatures.addAll(p.getUniprotFeaturesByFeatureType(featureType));
				}

				for (final UniprotFeatureBean uniprotFeature : uniprotFeatures) {
					// only consider the ones with annotated start and end
					// positions
					if (uniprotFeature.getPositionStart() > -1 && uniprotFeature.getPositionEnd() > -1) {
						final List<Pair<Integer, Integer>> startingPositions = startingPositionsByProtein
								.get(p.getPrimaryAccession().getAccession());
						final SequenceOverlapping included = isPeptideIncludedInThatRange(startingPositions,
								uniprotFeature.getPositionStart(), uniprotFeature.getPositionEnd());
						if (included != SequenceOverlapping.NOT_COVERED) {
							sb.append(uniprotFeature.getFeatureType() + " ");
							if (uniprotFeature.getDescription() != null) {
								sb.append(uniprotFeature.getDescription());
							}

							if (uniprotFeature.getPositionStart() == uniprotFeature.getPositionEnd()) {
								sb.append(" (").append(uniprotFeature.getPositionStart()).append(")");
							} else {
								sb.append(" (").append(uniprotFeature.getPositionStart()).append("-")
										.append(uniprotFeature.getPositionEnd()).append(")");
							}
							sb.append(" (" + included.getDescription() + ")");
						}
					}
				}
			} else {
				// there is not starting positions for this peptide in this
				// protein
			}
			if (proteinBeans.size() > 1) {
				sb.append(SharedConstants.NEW_LINE_JAVA);
			}
		}
		return sb.toString();
	}

	private static String getUniprotFeatureStringFromLightProteins(
			Map<String, List<Pair<Integer, Integer>>> startingPositionsByProtein, List<ProteinBeanLight> proteinBeans,
			String... featureTypes) {
		final StringBuilder sb = new StringBuilder();
		for (final ProteinBeanLight p : proteinBeans) {
			if (startingPositionsByProtein.containsKey(p.getPrimaryAccession().getAccession())) {
				final Set<UniprotFeatureBean> uniprotFeatures = new HashSet<UniprotFeatureBean>();
				for (final String featureType : featureTypes) {
					uniprotFeatures.addAll(p.getUniprotFeaturesByFeatureType(featureType));
				}

				for (final UniprotFeatureBean uniprotFeature : uniprotFeatures) {
					// only consider the ones with annotated start and end
					// positions
					if (uniprotFeature.getPositionStart() > -1 && uniprotFeature.getPositionEnd() > -1) {
						final List<Pair<Integer, Integer>> startingPositions = startingPositionsByProtein
								.get(p.getPrimaryAccession().getAccession());
						final SequenceOverlapping included = isPeptideIncludedInThatRange(startingPositions,
								uniprotFeature.getPositionStart(), uniprotFeature.getPositionEnd());
						if (included != SequenceOverlapping.NOT_COVERED) {
							sb.append(uniprotFeature.getFeatureType() + " ");
							if (uniprotFeature.getDescription() != null) {
								sb.append(uniprotFeature.getDescription());
							}

							if (uniprotFeature.getPositionStart() == uniprotFeature.getPositionEnd()) {
								sb.append(" (").append(uniprotFeature.getPositionStart()).append(")");
							} else {
								sb.append(" (").append(uniprotFeature.getPositionStart()).append("-")
										.append(uniprotFeature.getPositionEnd()).append(")");
							}
							sb.append(" (" + included.getDescription() + ")");
						}
					}
				}
			} else {
				// there is not starting positions for this peptide in this
				// protein
			}
			if (proteinBeans.size() > 1) {
				sb.append(SharedConstants.NEW_LINE_JAVA);
			}
		}
		return sb.toString();
	}
	// private boolean isPeptideIncludedInThatRange(List<Integer>
	// startingPositions, int positionStart,
	// int positionEnd) {
	// for (Integer startingPosition : startingPositions) {
	// if (startingPosition >= positionStart && startingPosition <= positionEnd)
	// {
	// return true;
	// }
	// }
	// return false;
	// }

	public static SequenceOverlapping isPeptideIncludedInThatRange(List<Pair<Integer, Integer>> startingEndingPositions,
			int positionStart, int positionEnd) {
		SequenceOverlapping ret = SequenceOverlapping.NOT_COVERED;
		for (final Pair<Integer, Integer> startingEndingPosition : startingEndingPositions) {
			final int startingPosition = startingEndingPosition.getFirstElement();
			final int endingPosition = startingEndingPosition.getSecondElement();
			if (
			// it starts in the range
			startingPosition >= positionStart && startingPosition <= positionEnd ||
			// it ends in the range
					endingPosition >= positionStart && endingPosition <= positionEnd) {
				if (startingPosition >= positionStart && endingPosition <= positionEnd) {
					return SequenceOverlapping.TOTALLY_COVERED;
				}
				ret = SequenceOverlapping.PARTIALLY_COVERED;
			}
		}
		return ret;
	}

	public static String getPTMString(List<PTMBean> ptms) {

		final StringBuilder sb = new StringBuilder();

		if (ptms != null) {
			// sort ptms by the position

			Collections.sort(ptms, new Comparator<PTMBean>() {
				@Override
				public int compare(PTMBean o1, PTMBean o2) {
					int minPosition1 = Integer.MAX_VALUE;
					int minPosition2 = Integer.MAX_VALUE;
					for (final PTMSiteBean ptmSite : o1.getPtmSites()) {
						if (minPosition1 > ptmSite.getPosition())
							minPosition1 = ptmSite.getPosition();
					}
					for (final PTMSiteBean ptmSite : o2.getPtmSites()) {
						if (minPosition2 > ptmSite.getPosition())
							minPosition2 = ptmSite.getPosition();
					}
					return Integer.compare(minPosition1, minPosition2);
				}
			});
			// now that is sorted:
			for (final PTMBean ptmBean : ptms) {
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
					final PTMSiteBean ptmSiteBean = ptmSites.get(i);
					if (i > 0)
						sb.append(",");
					sb.append(ptmSiteBean.getPosition());
				}
				sb.append(")");
			}

		}

		return sb.toString();
	}

	public static String getMSRunsIDString(Set<MSRunBean> msRuns) {
		final StringBuilder sb = new StringBuilder();
		final List<String> msRunIDs = new ArrayList<String>();
		for (final MSRunBean msRunBean : msRuns) {
			msRunIDs.add(msRunBean.getId());
		}
		Collections.sort(msRunIDs);
		for (final String msRunID : msRunIDs) {
			if (!"".equals(sb.toString())) {
				sb.append(",");
			}
			sb.append(msRunID);
		}
		return sb.toString();
	}

	public static String parseEmptyString(String string) {
		if (string == null || "".equals(string))
			return EMPTY_VALUE;
		return string;
	}

	public static List<AmountBean> sortAmountsByRunID(List<AmountBean> composedAmounts) {
		if (composedAmounts == null)
			return null;

		Collections.sort(composedAmounts, new Comparator<AmountBean>() {
			@Override
			public int compare(AmountBean o1, AmountBean o2) {
				final StringBuilder sb1 = new StringBuilder();
				o1.getMsRuns().stream().map(msRun -> msRun.getId()).sorted().forEach(msRunID -> sb1.append(msRunID));
				final StringBuilder sb2 = new StringBuilder();
				o2.getMsRuns().stream().map(msRun -> msRun.getId()).sorted().forEach(msRunID -> sb2.append(msRunID));
				return sb1.toString().compareTo(sb2.toString());
			}
		});
		return composedAmounts;
	}

	public static List<RatioBean> getRatiosByConditions(ContainsRatios containsRatios, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities) {
		return getRatiosByConditions(containsRatios.getRatios(), condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);
	}

	public static List<ScoreBean> getRatioScoresByConditions(ContainsRatios containsRatios, String condition1Name,
			String condition2Name, String projectTag, String ratioName, String ratioScoreName) {
		final List<RatioBean> ratiosByConditions = getRatiosByConditions(containsRatios, condition1Name, condition2Name,
				projectTag, ratioName, false);
		final List<ScoreBean> ratioScores = getRatioScoreValues(condition1Name, condition2Name, ratiosByConditions,
				ratioScoreName);
		return ratioScores;
	}

}
