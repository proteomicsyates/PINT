package edu.scripps.yates.client.ui.wizard.form;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.model.PrincipalInvestigatorBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.util.Pair;

public class PrincipalInvestigatorForm extends AbstractFormCollection {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1718824761348138173L;

	private DoSomethingTask<Void> onPINameTyped;

	public PrincipalInvestigatorForm(final PintContext context) {
		super(context);
		final PintImportCfgBean pintImportCfgBean = context.getPintImportConfiguration();
		PrincipalInvestigatorBean pi = pintImportCfgBean.getProject().getPrincipalInvestigator();
		if (pi == null) {
			pi = new PrincipalInvestigatorBean();
			pintImportCfgBean.getProject().setPrincipalInvestigator(pi);
		}
		final TextBoxFormInformation name = new TextBoxFormInformation("Name", "The name of the Principal Investigator",
				true, 20.0);
		add(name);
		name.linkToObject(new UpdateAction() {

			@Override
			public void onChange(GwtEvent event) {
				pintImportCfgBean.getProject().getPrincipalInvestigator().setName(name.getTextBox().getValue());
				if (onPINameTyped != null) {
					onPINameTyped.doSomething();
				}
			}
		});
		// set the textbox with the value that may come from the PintImportCfgBean
		name.getTextBox().setValue(pi.getName(), false);

		// email
		final TextBoxFormInformation email = new TextBoxFormInformation("Email",
				"The email of the Principal Investigator", true, 20.0);
		add(email);
		email.linkToObject(new UpdateAction() {
			@Override
			public void onChange(GwtEvent event) {
				pintImportCfgBean.getProject().getPrincipalInvestigator()
						.setEmail(email.getTextBox().getValue().trim());
			}
		});
		// set the textbox with the value that may come from the PintImportCfgBean
		email.getTextBox().setValue(pi.getEmail(), false);

		// institution
		final TextAreaFormInformation institution = new TextAreaFormInformation("Institution",
				"The Institution of the Principal Investigator. Name and address", true, 30.0);
		add(institution);
		institution.linkToObject(new UpdateAction() {
			@Override
			public void onChange(GwtEvent event) {
				pintImportCfgBean.getProject().getPrincipalInvestigator()
						.setInstitution(institution.getTextBox().getValue().trim());
			}
		});
		// set the textbox with the value that may come from the PintImportCfgBean
		institution.getTextBox().setValue(pi.getInstitution(), true);

		// country
		final TextBoxFormInformation country = new TextBoxFormInformation("Country", "The country of the Institution ",
				true, 20.0);
		add(country);
		country.linkToObject(new UpdateAction() {
			@Override
			public void onChange(GwtEvent event) {
				pintImportCfgBean.getProject().getPrincipalInvestigator()
						.setCountry(country.getTextBox().getValue().trim());
			}
		});
		// set the textbox with the value that may come from the PintImportCfgBean
		country.getTextBox().setValue(pi.getCountry(), false);
	}

	public void setOnPINameTyped(DoSomethingTask<Void> onProjectTagTyped) {
		this.onPINameTyped = onProjectTagTyped;
	}

	@Override
	public List<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>> getTextFormsLinks() {

		return Collections.emptyList();
	}
}
