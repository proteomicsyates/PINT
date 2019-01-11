package edu.scripps.yates.client.ui.wizard.view.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

import edu.scripps.yates.client.ui.wizard.Wizard.Display;
import edu.scripps.yates.client.ui.wizard.view.HasWizardButtonMethods;
import edu.scripps.yates.client.ui.wizard.view.HasWizardButtons;

/**
 * The default implementation of {@link Display#getButtonBar()}.
 * 
 * @author Brandon Tilley
 *
 */
public class WizardNavigationPanel extends Composite implements HasWizardButtons {

	private final HorizontalPanel container;

	private final WizardButton prev;
	private final WizardButton next;
	private final WizardButton cancel;
	private final WizardButton finish;
	private final String buttonWidth = "125px";
	private final String buttonHeight = "40px";

	public WizardNavigationPanel() {
		this("< Prev", "Next >");
	}

	public WizardNavigationPanel(String prevText, String nextText) {
		this(prevText, nextText, "Cancel", "Finish");
	}

	public WizardNavigationPanel(String prevText, String nextText, String cancelText, String finishText) {
		container = new HorizontalPanel();
		initWidget(container);

		container.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		container.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		container.setSpacing(0);
		container.setWidth("100%");

		final HTML leftSpacer = new HTML("&nbsp;");
		leftSpacer.setWidth("100%");
		cancel = new WizardButton(cancelText);
		finish = new WizardButton(finishText);
		final HTML spacer = new HTML("&nbsp;");
		prev = new WizardButton(prevText);
		next = new WizardButton(nextText);
		final HTML rightSpacer = new HTML("&nbsp;");
		rightSpacer.setWidth("100%");
		container.add(leftSpacer);
		container.add(cancel);
		container.add(finish);
		container.add(spacer);
		container.add(prev);
		container.add(next);
		container.add(rightSpacer);
		container.setCellWidth(leftSpacer, "50%");
		container.setCellWidth(rightSpacer, "50%");
		cancel.setWidth(buttonWidth);
		cancel.setHeight(buttonHeight);
		finish.setWidth(buttonWidth);
		finish.setHeight(buttonHeight);
		container.setCellWidth(spacer, "20px");
		prev.setWidth(buttonWidth);
		prev.setHeight(buttonHeight);
		next.setWidth(buttonWidth);
		next.setHeight(buttonHeight);
	}

	@Override
	public HasWizardButtonMethods getCancelButton() {
		return cancel;
	}

	@Override
	public HasWizardButtonMethods getFinishButton() {
		return finish;
	}

	@Override
	public HasWizardButtonMethods getNextButton() {
		return next;
	}

	@Override
	public HasWizardButtonMethods getPreviousButton() {
		return prev;
	}

}
