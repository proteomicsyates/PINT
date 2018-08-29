package edu.scripps.yates.shared.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;

public class DefaultView implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 86463838854014781L;
	private static final String QUERY_NAME_SEPARATOR = "###";
	private String projectTag;
	private List<ColumnWithVisibility> proteinDefaultView = new ArrayList<ColumnWithVisibility>();
	private List<ColumnWithVisibility> proteinGroupDefaultView = new ArrayList<ColumnWithVisibility>();
	private List<ColumnWithVisibility> psmDefaultView = new ArrayList<ColumnWithVisibility>();
	private List<ColumnWithVisibility> peptideDefaultView = new ArrayList<ColumnWithVisibility>();
	private String projectDescription;
	private String projectInstructions;
	private String projectViewComments;
	private TAB defaultTab = TAB.PROTEIN;
	private ColumnName proteinsSortedBy = ColumnName.SPECTRUM_COUNT;
	private ColumnName proteinGroupsSortedBy = ColumnName.SPECTRUM_COUNT;
	private ColumnName psmsSortedBy = null;
	private ColumnName peptidesSortedBy = null;
	private String proteinSortingScore;
	private String proteinGroupSortingScore;
	private String psmSortingScore;
	private String peptideSortingScore;
	private ORDER proteinOrder;
	private ORDER proteinGroupOrder;
	private ORDER psmOrder;
	private ORDER peptideOrder;
	private List<ProjectNamedQuery> projectNamedQueries = new ArrayList<ProjectNamedQuery>();
	private int proteinPageSize = 50;
	private int proteinGroupPageSize = 50;
	private int peptidePageSize = 50;
	private int psmPageSize = 50;
	private Set<String> hiddenPTMs = new HashSet<String>();

	public DefaultView() {
	}

	public enum ORDER {
		ASCENDING, DESCENDING
	};

	public enum TAB {
		PSM, PEPTIDE, PROTEIN, PROTEIN_GROUP
	};

	/**
	 * @return the projectTag
	 */
	public String getProjectTag() {
		return projectTag;
	}

	/**
	 * @param projectTag
	 *            the projectTag to set
	 */
	public void setProjectTag(String projectTag) {
		this.projectTag = projectTag;
	}

	/**
	 * @return the proteinDefaultView
	 */
	public List<ColumnWithVisibility> getProteinDefaultView() {
		return proteinDefaultView;
	}

	public boolean getProteinDefaultView(ColumnName columnName) {
		for (final ColumnWithVisibility columnWithVisibility : proteinDefaultView) {
			if (columnWithVisibility.getColumn() == columnName)
				return columnWithVisibility.isVisible();
		}
		return false;
	}

	/**
	 * @param proteinDefaultView
	 *            the proteinDefaultView to set
	 */
	public void setProteinDefaultView(List<ColumnWithVisibility> proteinDefaultView) {
		this.proteinDefaultView = proteinDefaultView;
	}

	/**
	 * @return the proteinGroupDefaultView
	 */
	public List<ColumnWithVisibility> getProteinGroupDefaultView() {
		return proteinGroupDefaultView;
	}

	public boolean getProteinGroupDefaultView(ColumnName columnName) {
		for (final ColumnWithVisibility columnWithVisibility : proteinGroupDefaultView) {
			if (columnWithVisibility.getColumn() == columnName)
				return columnWithVisibility.isVisible();
		}
		return false;
	}

	/**
	 * @param proteinGroupDefaultView
	 *            the proteinGroupDefaultView to set
	 */
	public void setProteinGroupDefaultView(List<ColumnWithVisibility> proteinGroupDefaultView) {
		this.proteinGroupDefaultView = proteinGroupDefaultView;
	}

	/**
	 * @return the psmDefaultView
	 */
	public List<ColumnWithVisibility> getPsmDefaultView() {
		return psmDefaultView;
	}

	public boolean getPSMDefaultView(ColumnName columnName) {
		for (final ColumnWithVisibility columnWithVisibility : psmDefaultView) {
			if (columnWithVisibility.getColumn() == columnName)
				return columnWithVisibility.isVisible();
		}
		return false;
	}

	/**
	 * @param psmDefaultView
	 *            the psmDefaultView to set
	 */
	public void setPsmDefaultView(List<ColumnWithVisibility> psmDefaultView) {
		this.psmDefaultView = psmDefaultView;
	}

	/**
	 * @return the defaultTab
	 */
	public TAB getDefaultTab() {
		return defaultTab;
	}

	/**
	 * @param defaultTab
	 *            the defaultTab to set
	 */
	public void setDefaultTab(TAB defaultTab) {
		this.defaultTab = defaultTab;
	}

	/**
	 * @return the proteinsSortedBy
	 */
	public ColumnName getProteinsSortedBy() {
		return proteinsSortedBy;
	}

	/**
	 * @param proteinsSortedBy
	 *            the proteinsSortedBy to set
	 */
	public void setProteinsSortedBy(ColumnName proteinsSortedBy) {
		this.proteinsSortedBy = proteinsSortedBy;
	}

	/**
	 * @return the psmsSortedBy
	 */
	public ColumnName getPsmsSortedBy() {
		return psmsSortedBy;
	}

	/**
	 * @param psmsSortedBy
	 *            the psmsSortedBy to set
	 */
	public void setPsmsSortedBy(ColumnName psmsSortedBy) {
		this.psmsSortedBy = psmsSortedBy;
	}

	/**
	 * @return the proteinSortingScore
	 */
	public String getProteinSortingScore() {
		return proteinSortingScore;
	}

	/**
	 * @param proteinSortingScore
	 *            the proteinSortingScore to set
	 */
	public void setProteinSortingScore(String proteinSortingScore) {
		this.proteinSortingScore = proteinSortingScore;
	}

	/**
	 * @return the psmSortingScore
	 */
	public String getPsmSortingScore() {
		return psmSortingScore;
	}

	/**
	 * @param psmSortingScore
	 *            the psmSortingScore to set
	 */
	public void setPsmSortingScore(String psmSortingScore) {
		this.psmSortingScore = psmSortingScore;
	}

	/**
	 * @return the proteinOrder
	 */
	public ORDER getProteinOrder() {
		return proteinOrder;
	}

	/**
	 * @param proteinOrder
	 *            the proteinOrder to set
	 */
	public void setProteinOrder(ORDER proteinOrder) {
		this.proteinOrder = proteinOrder;
	}

	/**
	 * @return the proteinGroupOrder
	 */
	public ORDER getProteinGroupOrder() {
		return proteinGroupOrder;
	}

	/**
	 * @param proteinGroupOrder
	 *            the proteinGroupOrder to set
	 */
	public void setProteinGroupOrder(ORDER proteinGroupOrder) {
		this.proteinGroupOrder = proteinGroupOrder;
	}

	/**
	 * @return the psmOrder
	 */
	public ORDER getPsmOrder() {
		return psmOrder;
	}

	/**
	 * @param psmOrder
	 *            the psmOrder to set
	 */
	public void setPsmOrder(ORDER psmOrder) {
		this.psmOrder = psmOrder;
	}

	/**
	 * @return the proteinGroupsSortedBy
	 */
	public ColumnName getProteinGroupsSortedBy() {
		return proteinGroupsSortedBy;
	}

	/**
	 * @param proteinGroupsSortedBy
	 *            the proteinGroupsSortedBy to set
	 */
	public void setProteinGroupsSortedBy(ColumnName proteinGroupsSortedBy) {
		this.proteinGroupsSortedBy = proteinGroupsSortedBy;
	}

	/**
	 * @return the proteinGroupSortingScore
	 */
	public String getProteinGroupSortingScore() {
		return proteinGroupSortingScore;
	}

	/**
	 * @param proteinGroupSortingScore
	 *            the proteinGroupSortingScore to set
	 */
	public void setProteinGroupSortingScore(String proteinGroupSortingScore) {
		this.proteinGroupSortingScore = proteinGroupSortingScore;
	}

	/**
	 * @return the projectDescription
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription
	 *            the projectDescription to set
	 */
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	/**
	 * @return the projectInstructions
	 */
	public String getProjectInstructions() {
		return projectInstructions;
	}

	/**
	 * @param projectInstructions
	 *            the projectInstructions to set
	 */
	public void setProjectInstructions(String projectInstructions) {
		this.projectInstructions = projectInstructions;
	}

	/**
	 * @return the projectViewComments
	 */
	public String getProjectViewComments() {
		return projectViewComments;
	}

	/**
	 * @param projectViewComments
	 *            the projectViewComments to set
	 */
	public void setProjectViewComments(String projectViewComments) {
		this.projectViewComments = projectViewComments;
	}

	/**
	 * @return the proteinPageSize
	 */
	public int getProteinPageSize() {
		return proteinPageSize;
	}

	/**
	 * @param proteinPageSize
	 *            the proteinPageSize to set
	 */
	public void setProteinPageSize(int proteinPageSize) {
		this.proteinPageSize = proteinPageSize;
	}

	/**
	 * @return the proteinGroupPageSize
	 */
	public int getProteinGroupPageSize() {
		return proteinGroupPageSize;
	}

	/**
	 * @param proteinGroupPageSize
	 *            the proteinGroupPageSize to set
	 */
	public void setProteinGroupPageSize(int proteinGroupPageSize) {
		this.proteinGroupPageSize = proteinGroupPageSize;
	}

	/**
	 * @return the psmPageSize
	 */
	public int getPsmPageSize() {
		return psmPageSize;
	}

	/**
	 * @param psmPageSize
	 *            the psmPageSize to set
	 */
	public void setPsmPageSize(int psmPageSize) {
		this.psmPageSize = psmPageSize;
	}

	/**
	 * @return the hiddenPTMs
	 */
	public Set<String> getHiddenPTMs() {
		return hiddenPTMs;
	}

	/**
	 * @param hiddenPTMs
	 *            the hiddenPTMs to set
	 */
	public void setHiddenPTMs(Set<String> hiddenPTMs) {
		this.hiddenPTMs = hiddenPTMs;
	}

	public void addHiddenPTMs(String hiddenPTM) {
		if (hiddenPTMs == null)
			hiddenPTMs = new HashSet<String>();
		hiddenPTMs.add(hiddenPTM);
	}

	/**
	 * @return the peptideDefaultView
	 */
	public List<ColumnWithVisibility> getPeptideDefaultView() {
		return peptideDefaultView;
	}

	/**
	 * @param peptideDefaultView
	 *            the peptideDefaultView to set
	 */
	public void setPeptideDefaultView(List<ColumnWithVisibility> peptideDefaultView) {
		this.peptideDefaultView = peptideDefaultView;
	}

	/**
	 * @return the peptidesSortedBy
	 */
	public ColumnName getPeptidesSortedBy() {
		return peptidesSortedBy;
	}

	/**
	 * @param peptidesSortedBy
	 *            the peptidesSortedBy to set
	 */
	public void setPeptidesSortedBy(ColumnName peptidesSortedBy) {
		this.peptidesSortedBy = peptidesSortedBy;
	}

	/**
	 * @return the peptideSortingScore
	 */
	public String getPeptideSortingScore() {
		return peptideSortingScore;
	}

	/**
	 * @param peptideSortingScore
	 *            the peptideSortingScore to set
	 */
	public void setPeptideSortingScore(String peptideSortingScore) {
		this.peptideSortingScore = peptideSortingScore;
	}

	/**
	 * @return the peptideOrder
	 */
	public ORDER getPeptideOrder() {
		return peptideOrder;
	}

	/**
	 * @param peptideOrder
	 *            the peptideOrder to set
	 */
	public void setPeptideOrder(ORDER peptideOrder) {
		this.peptideOrder = peptideOrder;
	}

	/**
	 * @return the peptidePageSize
	 */
	public int getPeptidePageSize() {
		return peptidePageSize;
	}

	/**
	 * @param peptidePageSize
	 *            the peptidePageSize to set
	 */
	public void setPeptidePageSize(int peptidePageSize) {
		this.peptidePageSize = peptidePageSize;
	}

	/**
	 * @return the projectNamedQueries
	 */
	public List<ProjectNamedQuery> getProjectNamedQueries() {
		if (projectNamedQueries == null) {
			projectNamedQueries = new ArrayList<ProjectNamedQuery>();
		}
		return projectNamedQueries;
	}

	/**
	 * @param projectNamedQueries
	 *            the projectNamedQueries to set
	 */
	public void setProjectNamedQueries(List<ProjectNamedQuery> projectNamedQueries) {
		this.projectNamedQueries = projectNamedQueries;
	}

	/**
	 * Parse the parameter string. If found two parameters, use the first as a
	 * name and the second as the query of a {@link ProjectNamedQuery} that is
	 * added to the list of {@link ProjectNamedQuery}.<br>
	 * If the parameter is not composed by two parameters, then the name will be
	 * choosen by default a number
	 *
	 * @param value
	 */
	public void addProjectNamedQueries(String value, int index) {
		if (value == null) {
			return;
		}
		final ProjectNamedQuery projectNameQuery = new ProjectNamedQuery();

		if (value.contains(QUERY_NAME_SEPARATOR)) {
			final String[] split = value.split(QUERY_NAME_SEPARATOR);
			projectNameQuery.setName(split[0].trim());
			projectNameQuery.setQuery(split[1].trim());
		} else {
			// if ("".equals(value.trim())) {
			// // projectNameQuery.setName("Load the whole project");
			// // projectNameQuery.setQuery("COND[," + projectTag + "]");
			// } else {
			projectNameQuery.setName("Query_" + projectNamedQueries.size() + 1);
			projectNameQuery.setQuery(value.trim());
			// }
		}
		projectNameQuery.setIndex(index);
		projectNamedQueries.add(projectNameQuery);

	}

}
