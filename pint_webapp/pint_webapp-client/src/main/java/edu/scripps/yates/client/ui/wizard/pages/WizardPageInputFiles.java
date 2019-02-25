package edu.scripps.yates.client.ui.wizard.pages;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import edu.scripps.yates.client.ui.wizard.pages.panels.NewSelectInputFilesPanel;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.FileFormat;

public class WizardPageInputFiles extends AbstractWizardPage {

	private FlexTable panel;
	private NewSelectInputFilesPanel selectInputFiles;
	private int rowForPanel;

	public WizardPageInputFiles() {
		super("Input files");
	}

	@Override
	protected Widget createPage() {
		int row = 0;
		final SimplePanel ret = new SimplePanel();
		panel = new FlexTable();
		ret.setWidget(panel);
		panel.setStyleName(WizardStyles.wizardRegularPage);
		//
		row++;
		final String text1 = "Now, you need to upload the input files you want to use.";
		final Label label1 = new Label(text1);
		label1.setStyleName(WizardStyles.WizardWelcomeLabel2);
		panel.setWidget(row, 0, label1);
		//
		row++;
		String text2 = "Supported input files are:  <ul>";
		for (final FileFormat format : FileFormat.values()) {
			if (format == FileFormat.UNKNOWN) {
				continue;
			}
			text2 += "<li>" + format.getName() + "</li>";
		}
		text2 += "</ul>";

		final InlineHTML label2 = new InlineHTML(text2);
		label2.setStyleName(WizardStyles.WizardExplanationLabel);
		panel.setWidget(row, 0, label2);
		//
		row++;
		String text3 = "Note: In case of using Excel files you have to take into account the following aspects:<ul>";
		text3 += "<li>Check the table before uploading to detect and remove non numeric values (such as <b>'NA', '#N/A', '#DIV/0!'</b>) in columns that are suppose to have only numeric values</li>";
		text3 += "<li>If the Excel file contains <b>more than one sheet,</b> each one will be considered as a different file.</li>";
		text3 += "<li>The <b>first row</b> in the table will be considered as the <b>header</b> and it will be used to show the user which columns are used to get the information.</li>";
		text3 += "<li>In order to incorporate data at <b>PSM-level</b> or <b>Peptide-level</b> you must have, in addition, <b>a column with the Protein accession</b> in the same table, so that each Peptide or PSM in each row will be assigned to the protein (or proteins) in the accession column.</li>";
		text3 += "<li>In order to incorporate data at <b>PSM-level</b>, you must have <b>a column with unique PSM identifiers</b>. If the PSM identifier is found in other input files (Excel or not), the information will be merged considering just one PSM.";
		text3 += "<li>Data from the same <b>Protein/Peptide/PSM</b> can be provided by <b>different files</b> (even different formats) as long as:"
				+ "<ul>"
				+ "<li>they have the same identifiers (<b>protein accession/peptide sequence with PTMs/PSM identifier</b> respectivelly) and,</li>"
				+ "<li>they are assigned to the same experiment/replicate at the last step of this wizard.</li>"
				+ "</ul>" + "</li>";
		text3 += "<li>If you have data from different <b>replicates or experiments</b> for the same items (Proteins or Peptides) in a single table, you will have to <b>split them in different files or sheets</b> before upload them, because they will have to be processed separatelly.";
		text3 += "</ul>";
		final InlineHTML label3 = new InlineHTML(text3);
		label3.setStyleName(WizardStyles.WizardInfoMessage);
		panel.setWidget(row, 0, label3);

		rowForPanel = row + 1;
		return ret;
	}

	@Override
	public void beforeShow() {

		// select input files
		selectInputFiles = new NewSelectInputFilesPanel(getWizard());
		panel.setWidget(rowForPanel, 0, selectInputFiles);

		super.beforeShow();
	}

	@Override
	public PageID getPageID() {
		return PageIDController.getPageIDByPageClass(this.getClass());
	}

	@Override
	protected void registerPageTitle(String title) {
		PageTitleController.addPageTitle(this.getPageID(), title);
	}
}
