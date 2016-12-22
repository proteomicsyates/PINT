package edu.scripps.yates.client.interfaces;

import java.util.List;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;

public interface ContainsDefaultView {
	public List<ColumnWithVisibility> getItemDefaultView(DefaultView defaultview);

	public ORDER getItemOrder(DefaultView defaultView);

	public String getSortingScore(DefaultView defaultView);

	public ColumnName getSortedBy(DefaultView defaultView);

}
