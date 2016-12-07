package edu.scripps.yates.client.gui.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.TextButtonCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.components.SharingPeptidesPanel;
import edu.scripps.yates.client.gui.components.WindowBox;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.ProteinPeptideCluster;
import edu.scripps.yates.shared.model.interfaces.ContainsPSMs;

public class CustomTextButtonColumn<T> extends Column<T, String> implements MyIdColumn<T> {
	private final ColumnName columnName;
	private boolean visibleState;
	private final double defaultWidth;
	private double width;
	private final Header<?> header;

	public CustomTextButtonColumn(final String sessionID, ColumnName columnName, boolean visibleState, Header<?> header,
			Header<String> footer) {
		super(new TextButtonCell());
		this.header = header;
		this.columnName = columnName;
		this.visibleState = visibleState;
		defaultWidth = 40;
		if (visibleState) {
			width = getDefaultWidth();
		} else {
			width = 0;
		}
		setCellStyleNames("textButtonColumn");
	}

	void showSharingPeptidesTablePanel(ContainsPSMs containsPSMs, ProteinPeptideCluster result) {
		SharingPeptidesPanel table = new SharingPeptidesPanel(result);
		WindowBox window = null;
		if (containsPSMs instanceof ProteinGroupBean) {
			window = new WindowBox(table, "Peptides explaining protein group '"
					+ ((ProteinGroupBean) containsPSMs).getPrimaryAccessionsString() + "'");
		} else if (containsPSMs instanceof ProteinBean) {
			window = new WindowBox(table, "Peptides explaining protein '"
					+ ((ProteinBean) containsPSMs).getPrimaryAccession().getAccession() + "'");
		}
		if (window != null) {
			window.setAnimationEnabled(true);
			window.setGlassEnabled(true);
			window.resize();
			window.center();
		} else {
			StatusReportersRegister.getInstance().notifyStatusReporters("Unsupported object: " + containsPSMs);
		}
	}

	@Override
	public ColumnName getColumnName() {
		return columnName;
	}

	@Override
	public String getExperimentalConditionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExperimentalCondition2Name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRatioName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProjectTag() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVisible() {
		return visibleState;
	}

	@Override
	public Header<String> getFooter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Unit getDefaultWidthUnit() {
		return Unit.PX;
	}

	@Override
	public double getDefaultWidth() {
		return defaultWidth;
	}

	@Override
	public double getWidth() {
		return width;
	}

	@Override
	public void setWidth(double width) {
		this.width = width;
	}

	@Override
	public Comparator<T> getComparator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVisible(boolean visible) {
		visibleState = visible;

	}

	@Override
	public String getScoreName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue(T object) {
		return "+";
	}

	@Override
	public AmountType getAmountType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Header<?> getHeader() {
		return header;
	}

}
