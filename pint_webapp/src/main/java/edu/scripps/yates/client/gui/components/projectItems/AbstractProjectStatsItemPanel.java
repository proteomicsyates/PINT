package edu.scripps.yates.client.gui.components.projectItems;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.model.projectStats.ProjectStats;

/**
 * Class that represents the stats of a single project (num proteins, peptides,
 * psms, etc) and that is presented in a panel.<br>
 * It is an extension of {@link AbstractItemPanel}
 *
 * @author Salva
 *
 * @param <T>
 *            parent class from which the items are depending
 */
public abstract class AbstractProjectStatsItemPanel<T> extends AbstractItemPanel<T, ProjectStats<T>> {
	protected final Map<T, ProjectStats<T>> projectStatsMap = new HashMap<T, ProjectStats<T>>();

	protected final MyClientBundle clientBundle = MyClientBundle.INSTANCE;
	protected final FlexTable rightPanel;
	protected final int rowConditions = 0;
	protected final int rowSamples = 1;
	protected final int rowMSRuns = 2;
	protected final int rowProteins = 3;
	protected final int rowGenes = 4;
	protected final int rowPeptides = 5;
	protected final int rowPSMs = 6;

	protected final NumberFormat format = NumberFormat.getFormat("###,###");
	private final Set<ProjectStats<T>> selectedItems = new HashSet<ProjectStats<T>>();

	protected AbstractProjectStatsItemPanel(T parent, boolean keepLeftPanel) {
		this("Project stats", parent, keepLeftPanel);
	}

	protected AbstractProjectStatsItemPanel(T parent) {
		this(parent, false);
	}

	protected AbstractProjectStatsItemPanel(String title, T parent) {
		this(title, parent, false);
	}

	protected AbstractProjectStatsItemPanel(String title, T parent, boolean keepLeftPanel) {
		super(title, parent, keepLeftPanel, false);
		// add description panel in the right
		rightPanel = new FlexTable();
		Label label1 = new Label("Number of experimental conditions:");
		label1.setStyleName("ProjectItemIndividualItemTitle");
		rightPanel.setWidget(rowConditions, 0, label1);
		rightPanel.getFlexCellFormatter().setAlignment(rowConditions, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		Label numConditionsLabel = new Label();
		rightPanel.setWidget(rowConditions, 1, numConditionsLabel);
		rightPanel.getFlexCellFormatter().setAlignment(rowConditions, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		Label label2 = new Label("Number of samples:");
		label2.setStyleName("ProjectItemIndividualItemTitle");
		rightPanel.setWidget(rowSamples, 0, label2);
		rightPanel.getFlexCellFormatter().setAlignment(rowSamples, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		Label numSamplesLabel = new Label();
		rightPanel.setWidget(rowSamples, 1, numSamplesLabel);
		rightPanel.getFlexCellFormatter().setAlignment(rowSamples, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);
		Label label3 = new Label("Number of MS runs:");
		label3.setStyleName("ProjectItemIndividualItemTitle");
		rightPanel.setWidget(rowMSRuns, 0, label3);
		rightPanel.getFlexCellFormatter().setAlignment(rowMSRuns, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		Label numMSRunsLabel = new Label();
		rightPanel.setWidget(rowMSRuns, 1, numMSRunsLabel);
		rightPanel.getFlexCellFormatter().setAlignment(rowMSRuns, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		Label label4 = new Label("Number of proteins:");
		label4.setStyleName("ProjectItemIndividualItemTitle");
		rightPanel.setWidget(rowProteins, 0, label4);
		rightPanel.getFlexCellFormatter().setAlignment(rowProteins, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		Label numProteinsLabel = new Label();
		rightPanel.setWidget(rowProteins, 1, numProteinsLabel);
		rightPanel.getFlexCellFormatter().setAlignment(rowProteins, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		Label label7 = new Label("Number of genes:");
		label7.setStyleName("ProjectItemIndividualItemTitle");
		rightPanel.setWidget(rowGenes, 0, label7);
		rightPanel.getFlexCellFormatter().setAlignment(rowGenes, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		Label numGenesLabel = new Label();
		rightPanel.setWidget(rowGenes, 1, numGenesLabel);
		rightPanel.getFlexCellFormatter().setAlignment(rowGenes, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		Label label5 = new Label("Number of peptides:");
		label5.setStyleName("ProjectItemIndividualItemTitle");
		rightPanel.setWidget(rowPeptides, 0, label5);
		rightPanel.getFlexCellFormatter().setAlignment(rowPeptides, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		Label numPeptidesLabel = new Label();
		rightPanel.setWidget(rowPeptides, 1, numPeptidesLabel);
		rightPanel.getFlexCellFormatter().setAlignment(rowPeptides, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		Label label6 = new Label("Number PSMs:");
		label6.setStyleName("ProjectItemIndividualItemTitle");
		rightPanel.setWidget(rowPSMs, 0, label6);
		rightPanel.getFlexCellFormatter().setAlignment(rowPSMs, 0, HasHorizontalAlignment.ALIGN_RIGHT,
				HasVerticalAlignment.ALIGN_TOP);
		Label numPSMsLabel = new Label();
		rightPanel.setWidget(rowPSMs, 1, numPSMsLabel);
		rightPanel.getFlexCellFormatter().setAlignment(rowPSMs, 1, HasHorizontalAlignment.ALIGN_LEFT,
				HasVerticalAlignment.ALIGN_TOP);

		addRightPanel(rightPanel);

		// to force the update
		currentParent = null;
		// update
		updateParent(parent);
	}

	@Override
	public void updateParent(T parent) {
		try {
			clearItemList();
			if (parent != null) {
				if (parent.equals(currentParent)) {
					// return;
				}
				currentParent = parent;
				final ProjectStats<T> projectStats = getProjectStats(parent);
				addItemToList(projectStats.toString(), projectStats);
				selectFirstItem();
			} else {
				selectItem(null);
			}
		} finally {
			afterUpdateParent(parent);
		}
	}

	@Override
	public void selectItem(ProjectStats<T> projectStats) {
		if (projectStats != null) {
			loadProjectStats(projectStats);
		} else {
			loadProjectStats(null);
		}

	}

	/**
	 * Method to implement from the extended class. It will be called after
	 * calling to updateParent(T)
	 *
	 * @param parent
	 */
	public abstract void afterUpdateParent(T parent);

	private void loadProjectStats(ProjectStats<T> projectStats) {
		if (projectStats == null) {
			return;
		}

		if (projectStats.getNumConditions() != null) {
			Label label = new Label(format.format(projectStats.getNumConditions()));
			label.setStyleName("no-wrap");
			rightPanel.setWidget(rowConditions, 1, label);
		} else {
			if (!selectedItems.contains(selectedItem)) {
				requestNumConditions(projectStats.getT());
			}
		}
		if (projectStats.getNumSamples() != null) {
			Label label = new Label(format.format(projectStats.getNumSamples()));
			label.setStyleName("no-wrap");
			rightPanel.setWidget(rowSamples, 1, label);
		} else {
			if (!selectedItems.contains(selectedItem)) {
				requestNumSamples(projectStats.getT());
			}
		}
		if (projectStats.getNumMSRuns() != null) {
			Label label = new Label(format.format(projectStats.getNumMSRuns()));
			label.setStyleName("no-wrap");
			rightPanel.setWidget(rowMSRuns, 1, label);
		} else {
			if (!selectedItems.contains(selectedItem)) {
				requestNumMSRuns(projectStats.getT());
			}
		}
		if (projectStats.getNumProteins() != null) {
			Label label = new Label(format.format(projectStats.getNumProteins()));
			label.setStyleName("no-wrap");
			rightPanel.setWidget(rowProteins, 1, label);
		} else {
			if (!selectedItems.contains(selectedItem)) {
				requestNumProteins(projectStats.getT());
			}
		}
		if (projectStats.getNumGenes() != null) {
			Label label = new Label(format.format(projectStats.getNumGenes()));
			label.setStyleName("no-wrap");
			rightPanel.setWidget(rowGenes, 1, label);
		} else {
			if (!selectedItems.contains(selectedItem)) {
				requestNumGenes(projectStats.getT());
			}
		}
		if (projectStats.getNumPeptides() != null) {
			Label label = new Label(format.format(projectStats.getNumPeptides()));
			label.setStyleName("no-wrap");
			rightPanel.setWidget(rowPeptides, 1, label);
		} else {
			if (!selectedItems.contains(selectedItem)) {
				requestNumPeptides(projectStats.getT());
			}
		}
		if (projectStats.getNumPSMs() != null) {
			Label label = new Label(String.valueOf(format.format(projectStats.getNumPSMs())));
			label.setStyleName("no-wrap");
			rightPanel.setWidget(rowPSMs, 1, label);
		} else {
			if (!selectedItems.contains(selectedItem)) {
				requestNumPSMs(projectStats.getT());
			}
		}
		// store the selected item in the set of selectedItems. Doing this, if
		// an item was previously selected, it will not request again the values
		// to the server
		selectedItems.add(selectedItem);
	}

	private ProjectStats<T> getProjectStats(T t) {
		if (!projectStatsMap.containsKey(t)) {
			ProjectStats<T> stats = createProjectStats(t);
			projectStatsMap.put(t, stats);
		}
		return projectStatsMap.get(t);
	}

	public abstract void requestNumGenes(final T t);

	public abstract void requestNumPSMs(final T t);

	public abstract void requestNumPeptides(final T t);

	public abstract void requestNumProteins(final T t);

	public abstract void requestNumMSRuns(final T t);

	public abstract void requestNumConditions(final T t);

	public abstract void requestNumSamples(final T t);

	/**
	 * Creates a new object of the type {@link ProjectStats}
	 *
	 * @param t
	 * @return
	 */
	public abstract ProjectStats<T> createProjectStats(T t);
}
