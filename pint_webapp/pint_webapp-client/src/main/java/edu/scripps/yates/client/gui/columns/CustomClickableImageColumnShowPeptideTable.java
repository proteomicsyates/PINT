package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.components.SharingPeptidesPanel;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.statusreporter.StatusReportersRegister;
import edu.scripps.yates.client.tasks.PendingTasksManager;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.model.interfaces.ContainsLightPeptides;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;
import edu.scripps.yates.shared.model.light.ProteinGroupBeanLight;
import edu.scripps.yates.shared.tasks.ShowPeptidesSharedByProteinsTask;
import edu.scripps.yates.shared.tasks.Task;

public class CustomClickableImageColumnShowPeptideTable<T extends ContainsLightPeptides>
		extends AbsctractCustomClickableImageColumn<T> implements MyColumn<T> {
	private static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();
	private final String sessionID;

	public CustomClickableImageColumnShowPeptideTable(final String sessionID, ColumnName columnName,
			boolean visibleState, Header<String> footer) {
		super(columnName, visibleState, footer, new MyTextHeader(columnName, "-"), 30);
		this.sessionID = sessionID;
	}

	@Override
	public void onBrowserEventImplementation(Context context, Element elem, final T object, NativeEvent event) {
		final String type = event.getType();
		if (type.equals(BrowserEvents.CLICK)) {
			String proteinAcc = object.toString();
			if (object instanceof ProteinBeanLight) {
				proteinAcc = ((ProteinBeanLight) object).getPrimaryAccession().getAccession();
			} else if (object instanceof ProteinGroupBeanLight) {
				proteinAcc = ((ProteinGroupBeanLight) object).getPrimaryAccessionsString();
			}

			final Task task = PendingTasksManager.addPendingTask(new ShowPeptidesSharedByProteinsTask(proteinAcc));
			service.getProteinsByPeptide(sessionID, object, new AsyncCallback<ProteinPeptideCluster>() {

				@Override
				public void onSuccess(ProteinPeptideCluster result) {
					try {
						showSharingPeptidesTablePanel(object, result);
					} finally {
						PendingTasksManager.removeTask(task);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					try {
						StatusReportersRegister.getInstance().notifyStatusReporters(caught);
					} finally {
						PendingTasksManager.removeTask(task);
					}
				}
			});

		}

	}

	protected void showSharingPeptidesTablePanel(final ContainsLightPeptides containsPeptides,
			final ProteinPeptideCluster result) {
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				final SharingPeptidesPanel table = new SharingPeptidesPanel(result);
				WindowBox window = null;
				if (containsPeptides instanceof ProteinGroupBeanLight) {
					window = new WindowBox(table, "Peptides explaining protein group");
				} else if (containsPeptides instanceof ProteinBeanLight) {
					window = new WindowBox(table, "Peptides explaining protein");
				}
				if (window != null) {
					window.setAnimationEnabled(true);
					window.setGlassEnabled(true);
					window.resize();
					window.setResizable(true);
					window.center();
				} else {
					StatusReportersRegister.getInstance()
							.notifyStatusReporters("Unsupported object: " + containsPeptides);
				}
			}
		});

	}

	@Override
	public Comparator<T> getComparator() {
		return null;
	}

	@Override
	public ImageResource getValue(ContainsLightPeptides object) {
		return MyClientBundle.INSTANCE.arrowUp();
	}

	@Override
	public void render(Context context, T object, SafeHtmlBuilder sb) {

		sb.append(template.startToolTip(getToolTip(object)));

		super.render(context, object, sb);
		sb.append(template.endToolTip());
	}

	private String getToolTip(T object) {
		String className = "protein";
		if (object instanceof ProteinGroupBeanLight) {
			className = "protein group";
		}
		return "Show peptides explaining " + className;
	}

	@Override
	public String getTitle(T object) {
		return getToolTip(object);
	}

}
