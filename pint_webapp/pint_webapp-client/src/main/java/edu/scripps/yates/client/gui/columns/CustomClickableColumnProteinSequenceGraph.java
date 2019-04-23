package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.util.ClientSafeHtmlUtils;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.ProteinBean;

public class CustomClickableColumnProteinSequenceGraph extends Column<ProteinBean, Void>
		implements MyColumn<ProteinBean> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	private final Header<?> header;
	private Header<String> footer;

	public CustomClickableColumnProteinSequenceGraph(ColumnName columnName, boolean visibleState, Header<String> footer,
			Header<?> header, int defaultWidth) {
		super(new CustomClickableTextCell<Void>(columnName));
		this.columnName = columnName;
		this.visibleState = visibleState;
		this.header = header;
		this.defaultWidth = defaultWidth;
		if (visibleState) {
			width = getDefaultWidth();
		} else {
			width = 0;
		}
	}

	public CustomClickableColumnProteinSequenceGraph(ColumnName columnName, boolean visible) {
		this(columnName, visible, null, new MyTextHeader(columnName, "Protein Coverage graph"), 80);
	}

	@Override
	public final void onBrowserEvent(Context context, Element elem, final ProteinBean object, NativeEvent event) {
		onBrowserEventImplementation(context, elem, object, event);
		super.onBrowserEvent(context, elem, object, event);

	}

	private void onBrowserEventImplementation(Context context, Element elem, ProteinBean p, NativeEvent event) {
		final String type = event.getType();
		if (type.equals(BrowserEvents.CLICK)) {
			showPopUpDialogWithSequenceGraph(p);
		}
	}

	private void showPopUpDialogWithSequenceGraph(ProteinBean proteinBean) {

		final FlexTable table = new FlexTable();
		table.getElement().getStyle().setMargin(20, Unit.PX);
		table.setWidth("800px");

		final Label label1 = new Label(
				"Set the cursor over a portion of the protein sequence representation to see more information");
		label1.getElement().getStyle().setFontSize(13, Unit.PX);
		label1.getElement().getStyle().setFontWeight(FontWeight.BOLD);
		label1.getElement().getStyle().setMarginBottom(20, Unit.PX);
		table.setWidget(0, 0, label1);
		table.getFlexCellFormatter().setColSpan(0, 0, 2);

		int row = 1;
		final Label proteinLabel = new Label(proteinBean.getPrimaryAccession().getAccession());
		proteinLabel.setTitle("Protein name: " + proteinBean.getDescriptionString() + "\n\n" + "Gene(s): "
				+ ClientSafeHtmlUtils.getGenesTooltip(proteinBean));
		proteinLabel.getElement().getStyle().setFontSize(13, Unit.PX);
		proteinLabel.getElement().getStyle().setMarginRight(10, Unit.PX);

		table.setWidget(row, 0, proteinLabel);
		table.getFlexCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_MIDDLE);
		table.getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		//
		final SafeHtml proteinCoverageGraphic = ClientSafeHtmlUtils.getProteinCoverageGraphic(proteinBean, true);
		final HTMLPanel panel = new HTMLPanel(proteinCoverageGraphic);
//			panel.getElement().getStyle().setMarginTop(20, Unit.PX);
//			panel.getElement().getStyle().setMarginBottom(40, Unit.PX);
		table.setWidget(row, 1, panel);
		table.getFlexCellFormatter().setWidth(row, 1, "100%");

		row++;
		final WindowBox window = new WindowBox(table, "Protein Coverage Graph");
		window.setCloseIconVisible(false);
		window.setMinimizeIconVisible(false);
		window.setAnimationEnabled(true);
		window.setGlassEnabled(true);
		window.resize();
		window.setResizable(true);
		window.center();
		//
		final Label label2 = new Label("Click outside of this dialog to close it");
		label2.getElement().getStyle().setMarginTop(30, Unit.PX);
		table.setWidget(row, 0, label2);
		table.getFlexCellFormatter().setColSpan(row, 0, 2);
	}

	@Override
	public void render(Context context, ProteinBean p, SafeHtmlBuilder sb) {
		sb.append(ClientSafeHtmlUtils.getProteinCoverageGraphic(p));
		// super.render(context, object, sb);
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
		return footer;
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
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public Header<?> getHeader() {
		return header;
	}

	@Override
	public Comparator<ProteinBean> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void getValue(ProteinBean object) {
		return null;
	}

}
