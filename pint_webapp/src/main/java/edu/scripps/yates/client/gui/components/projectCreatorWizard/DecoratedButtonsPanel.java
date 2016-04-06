package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.gui.templates.MyClientBundle;

public class DecoratedButtonsPanel extends Composite {

	private final FlowPanel mainPanel;
	private WindowBox windowBox;
	private final Map<Widget, PushButton> buttonsByWidgetsMap = new HashMap<Widget, PushButton>();

	public DecoratedButtonsPanel() {
		this(null);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public DecoratedButtonsPanel(String title) {
		mainPanel = new FlowPanel();
		mainPanel.setSize("100%", "100%");
		mainPanel.setStyleName("DecoratedButtonsPanel");

		initWidget(mainPanel);
		// if (title != null) {
		Label label = new Label(title);
		label.setStyleName("DecoradButtonsPanel-Title");
		mainPanel.add(label);
		// }
	}

	public void addButton(final String title, final Widget widget) {
		PushButton pshbtnIdent = new PushButton();
		pshbtnIdent.setSize("128px", "128px");
		pshbtnIdent.setTitle(title);
		pshbtnIdent.setStyleName("DecorateButtonsPanel_pushButton");
		pshbtnIdent.getUpFace().setImage(getImage(MyClientBundle.INSTANCE.experimentIconBlank()));
		pshbtnIdent.getUpHoveringFace().setImage(getImage(MyClientBundle.INSTANCE.experimentIconMarked()));
		mainPanel.add(pshbtnIdent);

		pshbtnIdent.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showDialog(widget, title);

			}
		});
		buttonsByWidgetsMap.put(widget, pshbtnIdent);
	}

	public void removeButton(Widget widget) {
		if (buttonsByWidgetsMap.containsKey(widget)) {
			buttonsByWidgetsMap.get(widget).removeFromParent();
		}
	}

	public void setUpFaceImage(Image image, Widget widget) {
		if (buttonsByWidgetsMap.containsKey(widget)) {
			buttonsByWidgetsMap.get(widget).getUpFace().setImage(image);
		}
	}

	public void setUpHoveringFaceImage(Image image, Widget widget) {
		if (buttonsByWidgetsMap.containsKey(widget)) {
			buttonsByWidgetsMap.get(widget).getUpHoveringFace().setImage(image);
		}
	}

	private Image getImage(ImageResource imageResource) {
		final Image image = new Image(imageResource);
		image.setSize("128px", "128px");
		return image;
	}

	protected void showDialog(Widget widget, String title) {
		if (windowBox == null) {
			windowBox = new WindowBox(true, true, true, true, true);
			windowBox.setAnimationEnabled(true);
			windowBox.setGlassEnabled(true);
		}
		windowBox.setWidget(widget);
		windowBox.setText(title);
		// // add loadingDialogShowerTask to close event
		// windowBox.addCloseEventDoSomethingTask(getLoadingDialogShowerTask());

		widget.addHandler(new ResizeHandler() {
			@Override
			public void onResize(final ResizeEvent event) {
				windowBox.resize();
			}

		}, ResizeEvent.getType());

		windowBox.center();
	}

}
