package edu.scripps.yates.client.ui.wizard.pages;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.client.ui.wizard.WizardPage.PageID;

public class PageIDController {
	private static final Map<Class<? extends AbstractWizardPage>, PageID> pageIDMap = new HashMap<Class<? extends AbstractWizardPage>, PageID>();

	public static PageID getPageIDByPageClass(Class<? extends AbstractWizardPage> class1) {
		if (!pageIDMap.containsKey(class1)) {
			final PageID pageID = new PageID();
			pageIDMap.put(class1, pageID);
		}
		return pageIDMap.get(class1);
	}

}
