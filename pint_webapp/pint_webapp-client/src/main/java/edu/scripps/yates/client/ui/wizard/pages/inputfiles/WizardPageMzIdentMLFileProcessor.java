package edu.scripps.yates.client.ui.wizard.pages.inputfiles;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class WizardPageMzIdentMLFileProcessor extends WizardPageDTASelectFileProcessor {

	public WizardPageMzIdentMLFileProcessor(PintContext context, int fileNumber, FileTypeBean file) {
		super(context, fileNumber, file);
	}

	@Override
	public String getText2() {
		return "This is an standard format file containing PSMs, peptides and proteins.";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WizardPageMzIdentMLFileProcessor) {
			final WizardPageMzIdentMLFileProcessor page2 = (WizardPageMzIdentMLFileProcessor) obj;
			if (page2.getPageID().equals(getPageID())) {
				return true;
			}
			return false;
		}
		return super.equals(obj);
	}

}
