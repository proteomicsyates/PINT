package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.components.SharingPeptidesPanel;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.tasks.PendingTasksManager;
import edu.scripps.yates.client.tasks.TaskType;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;

public class CustomClickableImageColumnShowPeptideTable<T> extends Column<T, ImageResource> implements MyColumn<T> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	private final String sessionID;
	private static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();
	private final HtmlTemplates template = GWT.create(HtmlTemplates.class);
	private final Header<String> footer;

	public CustomClickableImageColumnShowPeptideTable(final String sessionID, ColumnName columnName,
			boolean visibleState, Header<String> footer) {
		super(new CustomImageCell());
		this.columnName = columnName;
		this.visibleState = visibleState;
		this.sessionID = sessionID;
		defaultWidth = 30;
		if (visibleState) {
			width = getDefaultWidth();
		} else {
			width = 0;
		}
		this.footer = footer;
		setCellStyleNames("clickableImageColumn");
		super.setSortable(false);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Column#onBrowserEvent(com.google.gwt.
	 * cell.client.Cell.Context, com.google.gwt.dom.client.Element,
	 * java.lang.Object, com.google.gwt.dom.client.NativeEvent)
	 */
	@Override
	public void onBrowserEvent(Context context, Element elem, final T object, NativeEvent event) {
		final String type = event.getType();
		System.out.println(type);
		if (type.equals(BrowserEvents.CLICK)) {
			if (object instanceof ContainsPeptides) {
				String keyTMP = object.toString();
				if (object instanceof ProteinBean) {
					keyTMP = ((ProteinBean) object).getPrimaryAccession().getAccession();
				} else if (object instanceof ProteinGroupBean) {
					keyTMP = ((ProteinGroupBean) object).getPrimaryAccessionsString();
				}
				final String key = keyTMP;
				PendingTasksManager.addPendingTask(TaskType.PROTEINS_BY_PEPTIDE, key);
				service.getProteinsByPeptide(sessionID, (ContainsPeptides) object,
						new AsyncCallback<ProteinPeptideCluster>() {

							@Override
							public void onSuccess(ProteinPeptideCluster result) {
								try {
									showSharingPeptidesTablePanel(object, result);
								} finally {
									PendingTasksManager.removeTask(TaskType.PROTEINS_BY_PEPTIDE, key);
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								try {
									StatusReportersRegister.getInstance().notifyStatusReporters(caught);
								} finally {
									PendingTasksManager.removeTask(TaskType.PROTEINS_BY_PEPTIDE, key);
								}
							}
						});
			}
		}

		super.onBrowserEvent(context, elem, object, event);
	}

	void showSharingPeptidesTablePanel(final T containsPeptides, final ProteinPeptideCluster result) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				SharingPeptidesPanel table = new SharingPeptidesPanel(result);
				WindowBox window = null;
				if (containsPeptides instanceof ProteinGroupBean) {
					window = new WindowBox(table, "Peptides explaining protein group '"
							+ ((ProteinGroupBean) containsPeptides).getPrimaryAccessionsString() + "'");
				} else if (containsPeptides instanceof ProteinBean) {
					window = new WindowBox(table, "Peptides explaining protein '"
							+ ((ProteinBean) containsPeptides).getPrimaryAccession().getAccession() + "'");
				}
				if (window != null) {
					window.setAnimationEnabled(true);
					window.setGlassEnabled(true);
					window.resize();
					window.center();
				} else {
					StatusReportersRegister.getInstance()
							.notifyStatusReporters("Unsupported object: " + containsPeptides);
				}
			}
		});

	}

	@Override
	public ColumnName getColumnName() {
		return columnName;
	}

	@Override
	public boolean isVisible() {
		return visibleState;
	}

	@Override
	public Header<String> getFooter() {
		return this.footer;
	}

	@Override
	public Unit getDefaultWidthUnit() {
		return Unit.PX;
	}

	@Override
	public double getDefaultWidth() {
		return defaultWidth;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public Comparator<T> getComparator() {
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public ImageResource getValue(T object) {
		return MyClientBundle.INSTANCE.arrowUp();
	}

	@Override
	public void render(Context context, T object, SafeHtmlBuilder sb) {
		String className = "protein";
		if (object instanceof ProteinGroupBean) {
			className = "protein group";
		}
		sb.append(template.startToolTip("Show peptides explaining " + className));

		super.render(context, object, sb);
		sb.append(template.endToolTip());
	}
}
