package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;

import edu.scripps.yates.client.gui.templates.HtmlTemplates;
import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.interfaces.ContainsSequence;

public class CustomClickableImageColumnOpenLinkToIntAct extends Column<ProteinBean, ImageResource>
		implements MyColumn<ProteinBean> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	private final HtmlTemplates template = GWT.create(HtmlTemplates.class);
	private final Header<String> footer;
	private final Header<String> header = new TextHeader("IntAct");

	public CustomClickableImageColumnOpenLinkToIntAct(ColumnName columnName, boolean visibleState,
			Header<String> footer) {
		super(new CustomImageCell());
		this.columnName = columnName;
		this.visibleState = visibleState;
		defaultWidth = 40;
		if (visibleState) {
			width = getDefaultWidth();
		} else {
			width = 0;
		}
		this.footer = footer;
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
	public void onBrowserEvent(Context context, Element elem, final ProteinBean object, NativeEvent event) {
		final String type = event.getType();
		System.out.println(type);
		if (type.equals(BrowserEvents.CLICK)) {

			String url = "http://www.ebi.ac.uk/intact/query/" + object.getPrimaryAccession().getAccession();
			Window.open(url, "_blank", "enabled");

		}

		super.onBrowserEvent(context, elem, object, event);
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
	public Comparator<ProteinBean> getComparator() {
		return new Comparator<ProteinBean>() {

			@Override
			public int compare(ProteinBean o1, ProteinBean o2) {
				if (o1 instanceof ContainsSequence) {
					return ((ContainsSequence) o1).getSequence().compareTo(((ContainsSequence) o2).getSequence());
				} else if (o1 instanceof ProteinBean) {
					return o1.getPrimaryAccession().getAccession().compareTo(o2.getPrimaryAccession().getAccession());
				} else {
					return 0;
				}
			}

		};
	}

	@Override
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public ImageResource getValue(ProteinBean object) {
		final ImageResource image = MyClientBundle.INSTANCE.intActLogo();
		return image;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Column#render(com.google.gwt.cell.
	 * client.Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, ProteinBean object, SafeHtmlBuilder sb) {
		sb.append(template.startToolTip("Search protein '" + object.getPrimaryAccession().getAccession()
				+ "' in IntAct Molecular Interaction Database (EBI)"));

		super.render(context, object, sb);
		sb.append(template.endToolTip());
	}

	@Override
	public Header<?> getHeader() {
		return header;
	}

}
