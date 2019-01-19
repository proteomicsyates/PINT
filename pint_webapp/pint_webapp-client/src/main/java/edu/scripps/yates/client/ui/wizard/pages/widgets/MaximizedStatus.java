package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.HashMap;
import java.util.Map;

/**
 * This class keeps the status of maximized or minimez of each itemBean in order
 * to keep that information everytime a page is left so that if we come back we
 * create the {@link AbstractItemWidget} with that same status
 * 
 * @author salvador
 *
 */
public class MaximizedStatus {
	private final Map<String, Boolean> maximizedStatusByItemBeanID = new HashMap<String, Boolean>();

	public void addMaximizedStatus(String itemBeanID, boolean maximized) {
		this.maximizedStatusByItemBeanID.put(itemBeanID, maximized);
	}

	public Boolean getMaximizedStatus(String itemBeanID) {
		return maximizedStatusByItemBeanID.get(itemBeanID);
	}
}
