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

import com.google.gwt.user.client.ui.ListBox;

import edu.scripps.yates.client.gui.components.projectCreatorWizard.ExcelColumnRefPanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ProjectCreatorWizardUtil;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.ObjectPanels.DataSourceDisclosurePanel;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.ProjectCreatorRegister;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsDataObject;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AccessionType;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ExperimentalConditionBean;
import edu.scripps.yates.shared.model.GeneBean;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.OmimEntryBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.ProteinAnnotationBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.model.ThresholdBean;
import edu.scripps.yates.shared.model.interfaces.ContainsConditions;
import edu.scripps.yates.shared.model.interfaces.ContainsRatios;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class SharedDataUtils {
	public static final Map<FileNameWithTypeBean, FileTypeBean> excelBeansByExcelFileWithFormatBeansMap = new HashMap<FileNameWithTypeBean, FileTypeBean>();

	/**
	 * Gets the scores associated to the ratios
	 *
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param ratios1
	 * @return
	 */
	public static List<ScoreBean> getRatioScoreValues(String condition1Name, String condition2Name,
			List<RatioBean> ratios1) {
		List<ScoreBean> ret = new ArrayList<ScoreBean>();
		for (RatioBean ratio : ratios1) {
			if (ratio.getCondition1().getId().equalsIgnoreCase(condition1Name)
					&& ratio.getCondition2().getId().equalsIgnoreCase(condition2Name)) {
				if (ratio.getAssociatedConfidenceScore() != null) {
					ret.add(ratio.getAssociatedConfidenceScore());
				}

			} else if (ratio.getCondition1().getId().equalsIgnoreCase(condition2Name)
					&& ratio.getCondition2().getId().equalsIgnoreCase(condition1Name)) {
				if (ratio.getAssociatedConfidenceScore() != null) {
					ret.add(ratio.getAssociatedConfidenceScore());
				}
			}
		}
		return ret;
	}

	public static String getConditionString(ContainsConditions containsConditions) {
		final List<String> conditions2 = new ArrayList<String>();
		for (ExperimentalConditionBean condition : containsConditions.getConditions()) {
			conditions2.add(condition.getId());
		}

		Collections.sort(conditions2);
		StringBuilder sb = new StringBuilder();
		for (String string : conditions2) {
			if (!"".equals(sb.toString()))
				sb.append(SharedConstants.SEPARATOR);
			sb.append(string);
		}
		return sb.toString();
	}

	/**
	 * Gets the values of the ratios applying the negative if necessary since
	 * all ratios are log2 ratios
	 *
	 *
	 * @param condition1Name
	 * @param condition2Name
	 * @param ratios1
	 * @return
	 */
	public static List<Double> getRatioValues(String condition1Name, String condition2Name, List<RatioBean> ratios1) {
		List<Double> ret = new ArrayList<Double>();
		for (RatioBean ratio : ratios1) {
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

	public static int compareRatios(ContainsRatios o1, ContainsRatios o2, String condition1Name, String condition2Name,
			String projectTag, String ratioName, boolean skipInfinities) {
		final List<RatioBean> ratios1 = o1.getRatiosByConditions(condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);
		final List<RatioBean> ratios2 = o2.getRatiosByConditions(condition1Name, condition2Name, projectTag, ratioName,
				skipInfinities);
		if (!ratios1.isEmpty() && !ratios2.isEmpty()) {
			final List<Double> ratioValues1 = SharedDataUtils.getRatioValues(condition1Name, condition2Name, ratios1);
			final List<Double> ratioValues2 = SharedDataUtils.getRatioValues(condition1Name, condition2Name, ratios2);
			if (ratioValues1.size() == 1 && ratioValues2.size() == 1) {
				return Double.compare(ratioValues1.get(0), ratioValues2.get(0));
			} else {
				// TODO sort in a concrete way when having several ratios??
				final Double efectiveRatio1 = getEfectiveRatio(ratioValues1);
				final Double efectiveRatio2 = getEfectiveRatio(ratioValues2);
				if (efectiveRatio1 != null && efectiveRatio2 != null) {
					return Double.compare(efectiveRatio1, efectiveRatio2);
				}
			}
		} else if (ratios1.isEmpty() && !ratios2.isEmpty()) {
			return -1;
		} else if (!ratios1.isEmpty() && ratios2.isEmpty()) {
			return 1;
		}
		return 0;

	}

	public static int compareRatioScores(ContainsRatios o1, ContainsRatios o2, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities) {
		try {
			final List<RatioBean> ratios1 = o1.getRatiosByConditions(condition1Name, condition2Name, projectTag,
					ratioName, skipInfinities);
			final List<RatioBean> ratios2 = o2.getRatiosByConditions(condition1Name, condition2Name, projectTag,
					ratioName, skipInfinities);
			if (!ratios1.isEmpty() && !ratios2.isEmpty()) {
				final List<ScoreBean> ratioValues1 = SharedDataUtils.getRatioScoreValues(condition1Name, condition2Name,
						ratios1);
				final List<ScoreBean> ratioValues2 = SharedDataUtils.getRatioScoreValues(condition1Name, condition2Name,
						ratios2);

				final String value1 = ratioValues1.get(0).getValue();
				final String value2 = ratioValues2.get(0).getValue();
				try {
					return Double.compare(Double.valueOf(value1), Double.valueOf(value2));
				} catch (NumberFormatException e) {
					return value1.compareTo(value2);
				}

			} else if (ratios1.isEmpty() && !ratios2.isEmpty()) {
				return -1;
			} else if (!ratios1.isEmpty() && ratios2.isEmpty()) {
				return 1;
			}

		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * If all the ratios in the collection are INF, it will return INF. If all
	 * are -INF, it will return -INF
	 *
	 * @param ratioValues
	 * @return
	 */
	public static Double getEfectiveRatio(Collection<Double> ratioValues) {
		// look if the ratios are infinity or -infinity
		boolean areAllPlusINF = true;
		for (Double ratioValue : ratioValues) {
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
		for (Double ratioValue : ratioValues) {
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

	/**
	 * If element is 1C-ConditionName, then, <br>
	 * the 1 is the project, <br>
	 * and the C is the condition
	 *
	 * @param element
	 * @return the C
	 */
	public static String getConditionSymbolFromConditionElement(String element) {
		if (element.contains("-")) {
			String prefix = element.substring(0, element.indexOf("-"));
			if (prefix.length() == 1)
				return prefix;
			return prefix.substring(prefix.length() - 1);
		}
		return "";
	}

	/**
	 * If element is 1C-ConditionName, then, <br>
	 * the 1 is the project, <br>
	 * and the C is the condition
	 *
	 * @param element
	 * @return the ConditionName
	 */
	public static String getConditionNameFromConditionSelection(String element) {
		if (element.contains("-")) {
			return element.substring(element.indexOf("-") + 1);
		}
		return element;
	}

	/**
	 * If element is 1C-ConditionName, then, <br>
	 * the 1 is the project, <br>
	 * and the C is the condition
	 *
	 * @param element
	 * @return the 1
	 */
	public static String getProjectSymbolFromConditionSelection(String element) {
		if (element.contains("-")) {
			String prefix = element.substring(0, element.indexOf("-"));
			if (prefix.length() == 1)
				return "";
			return prefix.substring(0, prefix.length() - 1);
		}
		return "";
	}

	/**
	 * The project symbol will be searched in the listbox assuming that projects
	 * will be presented as "projectSymbol-projectName".
	 *
	 * @param element
	 * @return the projectSymbol
	 */
	public static String getProjectSymbolFromListBox(String projectName, ListBox listBox) {
		if (listBox.getItemCount() <= 1)
			return "";
		for (int i = 0; i < listBox.getItemCount(); i++) {
			String element = listBox.getItemText(i);
			if (element.contains("-")) {
				String name = element.substring(element.indexOf("-") + 1);
				if (name.equals(projectName)) {
					String symbol = element.substring(0, element.indexOf("-"));
					return symbol;
				}
			} else {
				return element;
			}
		}
		throw new IllegalArgumentException("No project name found!");
	}

	/**
	 * If the projectSymbol is empty, then the listbox MUST have just one
	 * element, and it will be returned.<br>
	 * If the projectSymbol is not empty, then, the project name will be
	 * searched in the listbox assuming that projects will be presented as
	 * "projectSymbol-projectName".
	 *
	 * @param projectSymbol
	 * @param listBox
	 * @return the projectName
	 */
	public static String getProjectNameFromListBox(String projectSymbol, ListBox listBox) {
		if (listBox.getItemCount() == 0)
			return "";
		if (projectSymbol == null || "".equals(projectSymbol))
			return listBox.getItemText(0);
		for (int i = 0; i < listBox.getItemCount(); i++) {
			String element = listBox.getItemText(i);
			if (element.contains("-")) {
				String symbol = element.substring(0, element.indexOf("-"));
				if (symbol.equals(projectSymbol))
					return element.substring(element.indexOf("-") + 1);
			} else {
				return element;
			}
		}
		throw new IllegalArgumentException("No project name found!");
	}

	/**
	 * This function builds a new name for the element of conditions.<br>
	 * The name of the condition will be 1A-ConditionName if there are more than
	 * one project in the project {@link ListBox} or just A-ConditionName if
	 * there is only one project in the list box.<br>
	 * It will check which is the next symbol for the condition to be added.
	 *
	 * @param conditionName
	 * @param projectName
	 * @param conditionsListBox
	 * @param projectsListBox
	 * @return
	 */
	public static String getNewElementNameForCondition(String conditionName, String projectName,
			ListBox conditionsListBox, ListBox projectsListBox) {
		String conditionSymbol = "";
		String projectSymbol = "";
		boolean moreThanOneProject = projectsListBox.getItemCount() > 1;
		if (moreThanOneProject) {
			// more than one project
			projectSymbol = getProjectSymbolFromListBox(projectName, projectsListBox);
		}
		conditionSymbol = getNextAvailableConditionSymbol(conditionsListBox, projectSymbol);

		return projectSymbol + conditionSymbol + "-" + conditionName;
	}

	/**
	 * This function builds a new name for the element of project.<br>
	 * The name of the project will be 1-projectName.<br>
	 * It will check which is the next symbol for the project to be added.
	 *
	 * @param projectName
	 * @param projectsListBox
	 * @return
	 */
	public static String getNewElementNameForProject(String projectName, ListBox projectsListBox) {
		String projectSymbol = getNextAvailableProjectSymbol(projectsListBox);
		if (projectSymbol != null && !"".equals(projectSymbol))
			return projectSymbol + "-" + projectName;
		return projectName;
	}

	private static String getNextAvailableConditionSymbol(ListBox conditionsListBox, String projectSymbol) {
		if (conditionsListBox.getItemCount() == 0) {
			return String.valueOf((char) 65);
		} else {
			String lastProjectSymbol = getProjectSymbolFromConditionSelection(
					conditionsListBox.getItemText(conditionsListBox.getItemCount() - 1));
			if (!lastProjectSymbol.equals(projectSymbol)) {
				return String.valueOf((char) 65);
			}
			String lastConditionSymbol = getConditionSymbolFromConditionElement(
					conditionsListBox.getItemText(conditionsListBox.getItemCount() - 1));
			return String.valueOf((char) (lastConditionSymbol.charAt(0) + 1));
		}
	}

	private static String getNextAvailableProjectSymbol(ListBox projectListBox) {
		if (projectListBox.getItemCount() == 0) {
			return "1";
		} else {
			final String itemText = projectListBox.getItemText(projectListBox.getItemCount() - 1);
			int lastProjectSymbol = Integer.valueOf(getProjectSymbolFromProjectSelection(itemText));
			return String.valueOf(lastProjectSymbol + 1);
		}
	}

	/**
	 * If element is 1-projectName, then, <br>
	 * the 1 is the symbol. if there is not 1-. return empty string
	 *
	 * @param element
	 * @return the projectSymbol
	 */
	private static String getProjectSymbolFromProjectSelection(String element) {

		if (element.contains("-")) {
			String prefix = element.substring(0, element.indexOf("-"));
			return prefix;
		}
		return "";

	}

	public static Comparator<AccessionBean> getComparatorByAccession() {
		Comparator<AccessionBean> ret = new Comparator<AccessionBean>() {

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

		Comparator<ProteinBean> comparatorByProteinPrimaryAcc = new Comparator<ProteinBean>() {
			@Override
			public int compare(ProteinBean o1, ProteinBean o2) {
				return o1.getPrimaryAccession().getAccession().compareTo(o2.getPrimaryAccession().getAccession());
			}
		};

		return comparatorByProteinPrimaryAcc;
	}

	public static Set<String> getItemValuesFromListBox(ListBox listBox) {
		Set<String> selectedItems = new HashSet<String>();
		for (int i = 0; i < listBox.getItemCount(); i++) {
			selectedItems.add(listBox.getValue(i));
		}
		return selectedItems;
	}

	public static Collection<String> getPrimaryAccessions(Collection<ProteinBean> proteinMap, AccessionType accType) {
		Set<String> ret = new HashSet<String>();
		if (proteinMap != null) {
			for (ProteinBean proteinBean : proteinMap) {
				final Set<AccessionBean> accessions = proteinBean.getAccessions(accType);
				for (AccessionBean accessionBean : accessions) {
					if (accessionBean.isPrimaryAccession())
						ret.add(accessionBean.getAccession());
				}
			}
		}
		return ret;
	}

	public static List<RatioBean> getRatiosByConditions(Collection<RatioBean> ratios, String condition1Name,
			String condition2Name, String projectTag, String ratioName, boolean skipInfinities) {
		Set<Integer> ratioIDs = new HashSet<Integer>();
		List<RatioBean> ret = new ArrayList<RatioBean>();
		if (ratios != null) {
			for (RatioBean ratioBean : ratios) {

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
		List<RatioBean> ret = new ArrayList<RatioBean>();
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
	 * Gets a unique string key for the collection of project tags by sorting
	 * them and returning the concatenated string of them
	 *
	 * @param projectTags
	 * @return
	 */
	public static String getProjectTagCollectionKey(Collection<String> projectTags) {
		List<String> list = new ArrayList<String>();
		list.addAll(projectTags);
		Collections.sort(list);
		String ret = "";
		for (String string : list) {
			ret += string;
		}
		return ret;
	}

	/**
	 * Gets a Map of conditions by projects from a collections of ratios
	 *
	 * @param ratios
	 * @return
	 */
	public static Map<String, List<String>> getConditionsByProjectsFromRatios(Collection<RatioBean> ratios) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (ratios != null) {
			for (RatioBean ratioBean : ratios) {
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
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (amounts != null) {
			for (AmountBean amountBean : amounts) {
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
		for (String key : donor.keySet()) {
			final List<String> list = donor.get(key);
			if (receiver.containsKey(key)) {
				for (String item : list) {
					if (!receiver.get(key).contains(item))
						receiver.get(key).add(item);
				}
			} else {
				List<String> list2 = new ArrayList<String>();
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
		Map<String, List<String>> ret = new HashMap<String, List<String>>();
		if (experimentalCondition != null && experimentalCondition.getProject() != null) {
			if (ret.containsKey(experimentalCondition.getProject().getTag())) {
				if (!ret.get(experimentalCondition.getProject().getTag()).contains(experimentalCondition.getId())) {
					ret.get(experimentalCondition.getProject().getTag()).add(experimentalCondition.getId());
				}
			} else {
				List<String> list = new ArrayList<String>();
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
		for (String key : donor.keySet()) {
			final List<RatioDescriptorBean> list = donor.get(key);
			if (receiver.containsKey(key)) {
				for (RatioDescriptorBean item : list) {
					if (!receiver.get(key).contains(item))
						receiver.get(key).add(item);
				}
			} else {
				List<RatioDescriptorBean> list2 = new ArrayList<RatioDescriptorBean>();
				list2.addAll(list);
				receiver.put(key, list2);
			}
		}

	}

	/**
	 * Gets a Map of {@link RatioDescriptorBean} by projects from a collection
	 * of {@link RatioBean}
	 *
	 * @param ratios
	 * @return
	 */
	public static Map<String, List<RatioDescriptorBean>> getRatioDescriptorsByProjectsFromRatios(
			Collection<RatioBean> ratios) {
		Map<String, List<RatioDescriptorBean>> ret = new HashMap<String, List<RatioDescriptorBean>>();

		if (ratios != null) {
			for (RatioBean ratioBean : ratios) {
				final RatioDescriptorBean ratioDescriptorBean = ratioBean.getRatioDescriptorBean();
				if (ret.containsKey(ratioDescriptorBean.getProjectTag())) {
					final List<RatioDescriptorBean> list = ret.get(ratioDescriptorBean.getProjectTag());
					if (!list.contains(ratioDescriptorBean))
						list.add(ratioDescriptorBean);
				} else {
					List<RatioDescriptorBean> list = new ArrayList<RatioDescriptorBean>();
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
		List<ProteinBean> ret = new ArrayList<ProteinBean>();
		if (proteins != null) {
			for (ProteinBean proteinBean : proteins) {
				if (proteinBean.isFromThisProject(projectTag)) {
					ret.add(proteinBean);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a list of {@link ProteinGroupBean} from the provided Collection of
	 * {@link ProteinGroupBean} that belongs to a certain project which means
	 * that at least one amount or ratio has been measured in that project
	 *
	 * @param proteinGroups
	 * @param projectTag
	 * @return
	 */
	public static List<ProteinGroupBean> getProteinGroupsFromProject(List<ProteinGroupBean> proteinGroups,
			String projectTag) {
		List<ProteinGroupBean> ret = new ArrayList<ProteinGroupBean>();
		if (proteinGroups != null) {
			for (ProteinGroupBean proteinGroup : proteinGroups) {
				if (proteinGroup.isFromThisProject(projectTag)) {
					ret.add(proteinGroup);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a List of {@link PSMBean} from a collection of {@link ProteinBean}.
	 * <br>
	 * Note that if the {@link ProteinBean} is not linked to any {@link PSMBean}
	 * the list will be empty
	 *
	 * @param proteinBeans
	 * @return
	 */
	public static List<PSMBean> getPSMBeansFromProteinBeans(Collection<ProteinBean> proteinBeans) {
		List<PSMBean> ret = new ArrayList<PSMBean>();
		Set<Integer> psmIDs = new HashSet<Integer>();
		if (proteinBeans != null) {
			for (ProteinBean proteinBean : proteinBeans) {
				final List<PSMBean> psmBeans = proteinBean.getPsms();
				if (psmBeans != null) {
					for (PSMBean psmBean : psmBeans) {
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
	 * Gets the internal ID of the selected file in the {@link ListBox}. Then,
	 * take that internal ID and search for it in the
	 * {@link ProjectCreatorRegister}. If found, and the found object is a
	 * {@link DataSourceDisclosurePanel}, then, returns the
	 * {@link FileNameWithTypeBean} represented
	 *
	 * @param fileCombo
	 * @param columnRef
	 * @return
	 */
	public static FileNameWithTypeBean getFileNameWithTypeFromFileComboSelection(ListBox fileCombo, String columnRef) {
		final String excelFileID = ExcelColumnRefPanel.getExcelFileID(columnRef);
		String internalID = ProjectCreatorWizardUtil.getValueInCombo(fileCombo, excelFileID);
		try {
			final RepresentsDataObject projectObjectRepresenter = ProjectCreatorRegister
					.getProjectObjectRepresenter(Integer.valueOf(internalID));
			if (projectObjectRepresenter instanceof DataSourceDisclosurePanel) {
				DataSourceDisclosurePanel disclosurePanel = (DataSourceDisclosurePanel) projectObjectRepresenter;
				final FileNameWithTypeBean fileNameWithTypeBean = disclosurePanel.getFileNameWithTypeBean();
				return fileNameWithTypeBean;
			}
		} catch (NumberFormatException e) {

		}
		return null;
	}

	/**
	 * Gets a List of {@link ProteinBean} from a collection of {@link PSMBean}.
	 * <br>
	 * Note that if the {@link PSMBean} is not linked to any {@link ProteinBean}
	 * the list will be empty
	 *
	 * @param psmBeans
	 * @return
	 */
	public static List<ProteinBean> getProteinBeansFromPSMBeans(List<PSMBean> psmBeans) {
		List<ProteinBean> ret = new ArrayList<ProteinBean>();
		Set<String> proteinIDs = new HashSet<String>();
		if (psmBeans != null) {
			for (PSMBean psmBean : psmBeans) {
				final Set<ProteinBean> proteinBeans = psmBean.getProteins();
				if (proteinBeans != null) {
					for (ProteinBean proteinBean : proteinBeans) {
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
	 * Note that if the {@link ProteinBean} is not linked to any
	 * {@link PeptideBean} the list will be empty
	 *
	 * @param proteinBeans
	 * @return
	 */
	public static List<PeptideBean> getPeptideBeansFromProteinBeans(Collection<ProteinBean> proteinBeans) {
		List<PeptideBean> ret = new ArrayList<PeptideBean>();
		Set<String> peptideIDs = new HashSet<String>();
		if (proteinBeans != null) {
			for (ProteinBean proteinBean : proteinBeans) {
				final List<PeptideBean> peptideBeans = proteinBean.getPeptides();
				if (peptideBeans != null) {
					for (PeptideBean peptideBean : peptideBeans) {
						if (!peptideIDs.contains(peptideBean.getId())) {
							peptideIDs.add(peptideBean.getId());
							ret.add(peptideBean);
						}
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Gets the {@link Peptide} objects from the {@link Protein}s and organize
	 * them in a {@link Map} using the sequence as the key
	 *
	 * @param proteins
	 * @return
	 */
	public static Map<String, Set<PeptideBean>> getPeptideMapFromProteins(Collection<ProteinBean> proteins) {
		Map<String, Set<PeptideBean>> ret = new HashMap<String, Set<PeptideBean>>();

		for (ProteinBean protein : proteins) {
			final List<PeptideBean> peptides = protein.getPeptides();
			for (PeptideBean peptide : peptides) {
				final String sequence = peptide.getSequence();
				if (ret.containsKey(sequence)) {
					ret.get(sequence).add(peptide);
				} else {
					Set<PeptideBean> set = new HashSet<PeptideBean>();
					set.add(peptide);
					ret.put(sequence, set);
				}
			}
		}

		return ret;
	}

	/**
	 * Merge one {@link ProteinBean} in the other. Any PSM, peptide or any other
	 * attribute of the proteinBeanDonor will be added to the
	 * proteinBeanReceiver
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
		for (AmountBean amount : proteinBeanDonor.getAmounts()) {
			proteinBeanReceiver.addAmount(amount);
		}
		for (ProteinAnnotationBean annotation : proteinBeanDonor.getAnnotations()) {
			proteinBeanReceiver.getAnnotations().add(annotation);
		}
		final Set<Integer> dbIds = proteinBeanDonor.getDbIds();
		for (Integer dbId : dbIds) {
			proteinBeanReceiver.addDbId(dbId);

		}

		final Set<String> functions = proteinBeanDonor.getFunctions();
		for (String function : functions) {
			proteinBeanReceiver.addFunction(function);
		}

		// reset geneString in order to be rebuilt after adding new genes
		proteinBeanReceiver.setGeneString(null);
		final Set<GeneBean> genes = proteinBeanDonor.getGenes();
		for (GeneBean geneBean : genes) {
			proteinBeanReceiver.addGene(geneBean);
		}

		final Set<MSRunBean> msruns = proteinBeanDonor.getMsruns();
		for (MSRunBean msRunBean : msruns) {
			proteinBeanReceiver.addMsrun(msRunBean);
		}
		// reset the number of PSMs to be recalculated after adding the new PSMs
		proteinBeanReceiver.setNumPSMs(0);
		final List<PSMBean> psms = proteinBeanDonor.getPsms();
		for (PSMBean psm : psms) {
			proteinBeanReceiver.addPSMtoProtein(psm);
		}

		final Set<RatioBean> ratios = proteinBeanDonor.getRatios();
		for (RatioBean ratioBean : ratios) {
			proteinBeanReceiver.addProteinRatio(ratioBean);
		}

		final Set<ThresholdBean> thresholds = proteinBeanDonor.getThresholds();
		for (ThresholdBean thresholdBean : thresholds) {
			proteinBeanReceiver.addThreshold(thresholdBean);
		}
		final Set<ExperimentalConditionBean> conditions = proteinBeanDonor.getConditions();
		for (ExperimentalConditionBean experimentalConditionBean : conditions) {
			proteinBeanReceiver.addCondition(experimentalConditionBean);
		}
		final Map<Integer, OmimEntryBean> omimEntries = proteinBeanDonor.getOmimEntries();
		for (OmimEntryBean omimEntry : omimEntries.values()) {
			proteinBeanReceiver.addOMIMEntry(omimEntry);
		}

		// reset numPeptide to force to get the actual number after adding the
		// donor different sequences
		proteinBeanReceiver.setNumPeptides(0);
		proteinBeanReceiver.getDifferentSequences().addAll(proteinBeanDonor.getDifferentSequences());

		for (PeptideBean peptideBean : proteinBeanDonor.getPeptides()) {
			proteinBeanReceiver.addPeptideToProtein(peptideBean);
		}
	}

	public static String getRatioKey(RatioBean ratio) {
		return new StringBuilder().append(ratio.getDescription()).append(ratio.getCondition1().getId())
				.append(ratio.getCondition2().getId()).append(ratio.getCondition1().getProject().getId()).toString();
	}

	public static String getAmountHeader(AmountType amountType, String conditionID) {
		return amountType + " (" + conditionID + ")";
	}

	public static String getAmountHeaderTooltip(AmountType amountType, String conditionName, String projectTag) {
		return "Amount type: " + amountType + SharedConstants.SEPARATOR + "Experimental condition: " + conditionName
				+ SharedConstants.SEPARATOR + "Project: " + projectTag;
	}

	public static String getRatioHeader(String ratioName, String condition1ID, String condition2ID) {
		return ratioName + "(" + condition1ID + " / " + condition2ID + ")";
	}

	public static String getRatioHeaderTooltip(String condition1Name, String condition2Name, String ratioName) {
		return "Ratio between conditions: " + condition1Name + " / " + condition2Name + SharedConstants.SEPARATOR
				+ "Ratio name: " + ratioName;
	}
}
