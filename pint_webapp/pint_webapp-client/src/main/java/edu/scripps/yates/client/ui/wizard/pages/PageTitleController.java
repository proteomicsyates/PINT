package edu.scripps.yates.client.ui.wizard.pages;

import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.client.ui.wizard.WizardPage.PageID;

public class PageTitleController {
	private static final Map<PageID, String> pageTitleMap = new HashMap<PageID, String>();

	public static String getPageTitleByPageID(PageID pageID) {
		return pageTitleMap.get(pageID);
	}

	public static void addPageTitle(PageID pageID, String title) {
		pageTitleMap.put(pageID, title);
	}

}
