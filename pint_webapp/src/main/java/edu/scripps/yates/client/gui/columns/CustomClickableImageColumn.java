package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.client.ProteinRetrievalServiceAsync;
import edu.scripps.yates.client.gui.components.SharingPeptidesPanel;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;

public class CustomClickableImageColumn<T> extends Column<T, ImageResource> implements MyColumn<T> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	private final String sessionID;
	private static final ProteinRetrievalServiceAsync service = ProteinRetrievalServiceAsync.Util.getInstance();

	public CustomClickableImageColumn(final String sessionID, ColumnName columnName, boolean visibleState,
			Header<String> footer) {
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
		setCellStyleNames("clickableImageColumn");

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
				service.getProteinsByPeptide(sessionID, (ContainsPeptides) object,
						new AsyncCallback<ProteinPeptideCluster>() {

							@Override
							public void onSuccess(ProteinPeptideCluster result) {
								showSharingPeptidesTablePanel((ContainsPeptides) object, result);
							}

							@Override
							public void onFailure(Throwable caught) {
								StatusReportersRegister.getInstance().notifyStatusReporters(caught);
							}
						});
			}
		}

		super.onBrowserEvent(context, elem, object, event);
	}

	void showSharingPeptidesTablePanel(ContainsPeptides containsPeptides, ProteinPeptideCluster result) {
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
			StatusReportersRegister.getInstance().notifyStatusReporters("Unsupported object: " + containsPeptides);
		}
	}

	@Override
	public ColumnName getColumnName() {
		return columnName;
	}

	@Override
	public String getExperimentalConditionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimentalCondition2Name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRatioName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVisible() {
		return visibleState;
	}

	@Override
	public Header<String> getFooter() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public String getScoreName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getValue(T object) {
		return MyClientBundle.INSTANCE.arrowUp();
	}

	@Override
	public AmountType getAmountType() {
		// TODO Auto-generated method stub
		return null;
	}

}
