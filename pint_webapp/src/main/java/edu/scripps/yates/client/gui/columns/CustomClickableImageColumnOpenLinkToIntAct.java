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

public class CustomClickableImageColumnOpenLinkToIntAct extends AbsctractCustomClickableImageColumn<ProteinBean>
		implements MyColumn<ProteinBean> {

	public CustomClickableImageColumnOpenLinkToIntAct(ColumnName columnName, boolean visibleState,
			Header<String> footer) {
		super(columnName, visibleState, footer, new TextHeader("IntAct"), 40);

	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.google.gwt.user.cellview.client.Column#onBrowserEvent(com.google.gwt.
	 * cell.client.Cell.Context, com.google.gwt.dom.client.Element,
	 * java.lang.Object, com.google.gwt.dom.client.NativeEvent)
	 */
	@Override
	public void onBrowserEventImplementation(Context context, Element elem, final ProteinBean object,
			NativeEvent event) {
		final String type = event.getType();
		System.out.println(type);
		if (type.equals(BrowserEvents.CLICK)) {

			String url = "http://www.ebi.ac.uk/intact/query/" + object.getPrimaryAccession().getAccession();
			Window.open(url, "_blank", "enabled");

		}

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

}
