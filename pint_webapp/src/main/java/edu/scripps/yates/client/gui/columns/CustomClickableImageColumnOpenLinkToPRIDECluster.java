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

public class CustomClickableImageColumnOpenLinkToPRIDECluster<T> extends Column<T, ImageResource>
		implements MyColumn<T> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	private final HtmlTemplates template = GWT.create(HtmlTemplates.class);
	private final Header<String> footer;
	private final Header<?> header = new TextHeader("PRIDE");

	public CustomClickableImageColumnOpenLinkToPRIDECluster(ColumnName columnName, boolean visibleState,
			Header<String> footer) {
		super(new CustomImageCell());
		this.columnName = columnName;
		this.visibleState = visibleState;
		defaultWidth = 50;
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
	public void onBrowserEvent(Context context, Element elem, final T object, NativeEvent event) {
		final String type = event.getType();
		System.out.println(type);
		if (type.equals(BrowserEvents.CLICK)) {

			String url = "http://www.ebi.ac.uk/pride/cluster/#/list?q=" + getSearchTerm(object) + "&page=1&size=20";
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
		return new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				if (o1 instanceof ContainsSequence) {
					return ((ContainsSequence) o1).getSequence().compareTo(((ContainsSequence) o2).getSequence());
				} else if (o1 instanceof ProteinBean) {
					return ((ProteinBean) o1).getPrimaryAccession().getAccession()
							.compareTo(((ProteinBean) o2).getPrimaryAccession().getAccession());
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
	public ImageResource getValue(T object) {
		final ImageResource prideClusterLogo = MyClientBundle.INSTANCE.prideClusterLogo();
		return prideClusterLogo;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Column#render(com.google.gwt.cell.
	 * client.Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	@Override
	public void render(Context context, T object, SafeHtmlBuilder sb) {
		sb.append(template.startToolTip(
				"Search " + getSearchTypeTerm(object) + "'" + getSearchTerm(object) + "' in PRIDE CLUSTER (EBI)"));

		super.render(context, object, sb);
		sb.append(template.endToolTip());
	}

	private String getSearchTerm(T object) {
		if (object instanceof ContainsSequence) {
			return ((ContainsSequence) object).getSequence();
		} else if (object instanceof ProteinBean) {
			return ((ProteinBean) object).getPrimaryAccession().getAccession();
		} else {
			return null;
		}
	}

	private String getSearchTypeTerm(T object) {
		if (object instanceof ContainsSequence) {
			return "peptide";
		} else if (object instanceof ProteinBean) {
			return "protein";
		} else {
			return null;
		}
	}

	@Override
	public Header<?> getHeader() {
		return header;
	}
}
