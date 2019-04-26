package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.Window;

import edu.scripps.yates.client.gui.templates.MyClientBundle;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.light.ProteinBeanLight;

public class CustomClickableImageColumnOpenLinkToComplexPortal
		extends AbsctractCustomClickableImageColumn<ProteinBeanLight> implements MyColumn<ProteinBeanLight> {

	public CustomClickableImageColumnOpenLinkToComplexPortal(ColumnName columnName, boolean visibleState,
			Header<String> footer) {
		super(columnName, visibleState, footer, new MyTextHeader(columnName, "Complex"), 40);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.cellview.client.Column#onBrowserEvent(com.google.gwt.
	 * cell.client.Cell.Context, com.google.gwt.dom.client.Element,
	 * java.lang.Object, com.google.gwt.dom.client.NativeEvent)
	 */
	@Override
	public void onBrowserEventImplementation(Context context, Element elem, final ProteinBeanLight object,
			NativeEvent event) {
		final String type = event.getType();
		System.out.println(type);
		if (type.equals(BrowserEvents.CLICK)) {

			final String url = "http://www.ebi.ac.uk/intact/complex/?q=" + object.getPrimaryAccession().getAccession();
			Window.open(url, "_blank", "enabled");

		}

	}

	@Override
	public Comparator<ProteinBeanLight> getComparator() {
		return new Comparator<ProteinBeanLight>() {

			@Override
			public int compare(ProteinBeanLight o1, ProteinBeanLight o2) {

				return o1.getPrimaryAccession().getAccession().compareTo(o2.getPrimaryAccession().getAccession());

			}

		};
	}

	@Override
	public ImageResource getValue(ProteinBeanLight object) {
		final ImageResource image = MyClientBundle.INSTANCE.complexPortalLogo();
		return image;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.cellview.client.Column#render(com.google.gwt.cell.
	 * client.Cell.Context, java.lang.Object,
	 * com.google.gwt.safehtml.shared.SafeHtmlBuilder)
	 */
	// @Override
	// public void render(Context context, ProteinBeanLight object, SafeHtmlBuilder
	// sb) {
	// sb.append(template.startToolTip(
	// "Search protein '" + object.getPrimaryAccession().getAccession() + "' in
	// Complex Portal (EBI)"));
	//
	// super.render(context, object, sb);
	// sb.append(template.endToolTip());
	// }

	@Override
	public String getTitle(ProteinBeanLight object) {
		return "Search protein '" + object.getPrimaryAccession().getAccession() + "' in Complex Portal (EBI)";
	}

}
