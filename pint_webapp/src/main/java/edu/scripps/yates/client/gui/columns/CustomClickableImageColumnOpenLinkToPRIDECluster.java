package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Window;

import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.interfaces.ContainsSequence;

public class CustomClickableImageColumnOpenLinkToPRIDECluster<T> extends AbsctractCustomClickableImageColumn<T>
		implements MyColumn<T> {

	public CustomClickableImageColumnOpenLinkToPRIDECluster(ColumnName columnName, boolean visibleState,
			Header<String> footer) {
		super(columnName, visibleState, footer, new TextHeader("PRIDE"), 50);
	}

	@Override
	public void onBrowserEventImplementation(Context context, Element elem, final T object, NativeEvent event) {
		final String type = event.getType();
		System.out.println(type);
		if (type.equals(BrowserEvents.CLICK)) {

			String url = "http://www.ebi.ac.uk/pride/cluster/#/list?q=" + getSearchTerm(object) + "&page=1&size=20";
			Window.open(url, "_blank", "enabled");

		}

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

}
