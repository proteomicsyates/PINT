package edu.scripps.yates.client.ui.wizard.form;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.GwtEvent;

import edu.scripps.yates.client.gui.incrementalCommands.DoSomethingTask;
import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ProjectTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class ProjectForm extends AbstractFormCollection {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1718824761348138173L;
	private final TextBoxFormInformation projectTag;
	private final TextBoxFormInformation projectName;
	private DoSomethingTask<Void> onProjectTagTyped;

	public ProjectForm(final PintContext context) {
		super(context);
		final PintImportCfgBean pintImportCfgBean = context.getPintImportConfiguration();
		ProjectTypeBean project = pintImportCfgBean.getProject();
		if (project == null) {
			project = new ProjectTypeBean();
			pintImportCfgBean.setProject(project);
		}
		projectTag = new TextBoxFormInformation("Dataset tag",
				"The short tag of the dataset. Maximum lenght of 7 characters", true, 7.0);
		add(projectTag);
		projectTag.linkToObject(new UpdateAction() {

			@Override
			public void onChange(GwtEvent event) {
				pintImportCfgBean.getProject().setTag(projectTag.getTextBox().getText());
				if (onProjectTagTyped != null) {
					onProjectTagTyped.doSomething();
				}
			}
		});
		// set the textbox with the value that may come from the PintImportCfgBean
		projectTag.getTextBox().setValue(project.getTag(), false);

		// project name
		projectName = new TextBoxFormInformation("Project name", "The name of the project", false, 20.0);
		add(projectName);
		projectName.linkToObject(new UpdateAction() {
			@Override
			public void onChange(GwtEvent event) {
				pintImportCfgBean.getProject().setName(projectName.getTextBox().getValue().trim());
			}
		});
		// set the textbox with the value that may come from the PintImportCfgBean
		projectName.getTextBox().setValue(project.getName(), false);

		final TextAreaFormInformation description = new TextAreaFormInformation("Dataset description",
				"A paragraph with the description of the project. It may be similar to the abstract of its publication",
				true, 30.0);
		add(description);
		description.linkToObject(new UpdateAction() {
			@Override
			public void onChange(GwtEvent event) {
				pintImportCfgBean.getProject().setDescription(description.getTextBox().getValue().trim());
			}
		});
		// set the textbox with the value that may come from the PintImportCfgBean
		description.getTextBox().setValue(project.getDescription(), false);
	}

	@Override
	public List<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>> getTextFormsLinks() {
		final List<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>> ret = new ArrayList<Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation>>();
		final Pair<AbstractTextBasedFormInformation, AbstractTextBasedFormInformation> pair = super.getLink(projectTag,
				projectName);
		ret.add(pair);
		return ret;
	}

	public void setOnProjectTagTyped(DoSomethingTask<Void> onProjectTagTyped) {
		this.onProjectTagTyped = onProjectTagTyped;
	}
}
