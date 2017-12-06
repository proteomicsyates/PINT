package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.cache.ClientCacheGeneralObjects;
import edu.scripps.yates.client.cache.GeneralObject;
import edu.scripps.yates.client.gui.components.projectCreatorWizard.manager.RepresentsObject;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ScoreTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class ScorePanel extends ContainsExcelColumnRefPanelAndTable<Pair<String, ScoreTypeBean>, ScoreTypeBean>
		implements RepresentsObject<ScoreTypeBean> {
	private final ListBox comboBoxScoreType;
	private TextBox scoreNameTextBox;
	private final TextArea textAreaDescription;
	private CustomColumn<Pair<String, ScoreTypeBean>> column;
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private final String tableHeader;
	private final String sessionID;

	public ScorePanel(String sessionID, FileTypeBean excelFileBean, String tableHeader, ScoreTypeBean scoreTypeBean) {
		super(excelFileBean, scoreTypeBean);
		this.sessionID = sessionID;
		this.tableHeader = tableHeader;
		FlexTable flexTable = new FlexTable();
		flexTable.setCellPadding(5);
		mainPanel.add(flexTable);

		Label lblSelectExcelColumn = new Label("Select Excel column:");
		flexTable.setWidget(0, 0, lblSelectExcelColumn);
		flexTable.setWidget(1, 0, getExcelColumnRefPanel());

		Label lblProteinAccession = new Label(tableHeader);
		flexTable.setWidget(2, 0, lblProteinAccession);

		Label lblGroupSeparator = new Label("Score name:");
		flexTable.setWidget(3, 0, lblGroupSeparator);

		if (scoreNameTextBox == null) {
			scoreNameTextBox = new TextBox();
			scoreNameTextBox.setText("My score");
		}
		scoreNameTextBox.setAlignment(TextAlignment.LEFT);
		flexTable.setWidget(3, 1, scoreNameTextBox);

		Label lblType = new Label("Score type:");
		flexTable.setWidget(4, 0, lblType);

		comboBoxScoreType = new ListBox();
		flexTable.setWidget(4, 1, comboBoxScoreType);
		flexTable.getFlexCellFormatter().setColSpan(2, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.getCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		flexTable.getCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_LEFT);

		Label lblScoreDescription = new Label("Score description:");
		flexTable.setWidget(5, 0, lblScoreDescription);

		textAreaDescription = new TextArea();
		textAreaDescription.setVisibleLines(5);
		flexTable.setWidget(5, 1, textAreaDescription);
		textAreaDescription.setWidth("100%");
		flexTable.getCellFormatter().setVerticalAlignment(5, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(5, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		addHandlers();
		loadScoreTypes();

		if (representedObject != null) {
			updateGUIFromObjectData();
		} else {
			updateRepresentedObject();
		}
		if ("".equals(scoreNameTextBox.getText())) {
			scoreNameTextBox.setText("My score");
		}
	}

	/**
	 * Calls to getScoreTypes using the webservice
	 */
	private void loadScoreTypes() {
		// look into general objects cache first
		if (ClientCacheGeneralObjects.getInstance().contains(GeneralObject.SCORE_TYPES)) {
			loadScoreTypesInCombo(ClientCacheGeneralObjects.getInstance().getFromCache(GeneralObject.SCORE_TYPES));

			return;
		}

	}

	private void loadScoreTypesInCombo(List<String> scoreTypes) {
		if (scoreTypes != null) {
			if (comboBoxScoreType.getItemCount() == 0) {
				comboBoxScoreType.addItem("", "");
			}
			for (String scoreType : scoreTypes) {
				comboBoxScoreType.addItem(scoreType, scoreType);
			}
		}
	}

	private void addHandlers() {
		// if column is selected, write the column header as the name of the
		// score
		getExcelColumnRefPanel().addExcelColumnsChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final String columnName = getExcelColumnRefPanel().getColumnName();
				scoreNameTextBox.setText(columnName);
			}
		});
		// if name is typed in the score name, change the column header name
		scoreNameTextBox.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				getTable().redrawHeaders();
			}
		});
	}

	@Override
	public List<CustomColumn<Pair<String, ScoreTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, ScoreTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, ScoreTypeBean>>>();

		column = new CustomColumn<Pair<String, ScoreTypeBean>>(tableHeader) {
			@Override
			public String getValue(Pair<String, ScoreTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final ScoreTypeBean scoreType = pair.getSecondElement();
				if (scoreType != null) {
					// TODO
					return rawValue;
				}
				return null;
			}
		};
		ret.add(column);
		return ret;
	}

	@Override
	public List<Header<String>> getInitHeaders() {
		List<Header<String>> ret = new ArrayList<Header<String>>();
		if (scoreNameTextBox == null) {
			scoreNameTextBox = new TextBox();
			scoreNameTextBox.setText("My score");
		}

		ret.add(new MyHeader(scoreNameTextBox));

		return ret;
	}

	@Override
	public void updateRepresentedObject() {
		representedObject.setColumnRef(getExcelColumnRefPanel().getColumnRef());
		if (!"".equals(textAreaDescription.getText()))
			representedObject.setDescription(textAreaDescription.getText());
		else
			representedObject.setDescription(null);

		if (!"".equals(scoreNameTextBox.getText()))
			representedObject.setScoreName(scoreNameTextBox.getText());
		else
			representedObject.setScoreName(null);
		if (comboBoxScoreType.getSelectedIndex() > -1)
			representedObject.setScoreType(comboBoxScoreType.getValue(comboBoxScoreType.getSelectedIndex()));
		else
			representedObject.setScoreType(null);

	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (representedObject.getColumnRef() == null)
			return false;
		if (representedObject.getScoreName() == null)
			return false;
		if (representedObject.getScoreType() == null)
			return false;
		return true;
	}

	@Override
	public ScoreTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(ScoreTypeBean dataObject) {

		setRepresentedObject(dataObject);
		updateGUIFromObjectData();

	}

	@Override
	public void updateGUIFromObjectData() {

		if (representedObject != null) {
			super.getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
			scoreNameTextBox.setValue(representedObject.getScoreName());

			ProjectCreatorWizardUtil.selectInCombo(comboBoxScoreType, representedObject.getScoreType());

			textAreaDescription.setValue(representedObject.getDescription());
		} else {
			scoreNameTextBox.setValue("");
			textAreaDescription.setValue("");
			getExcelColumnRefPanel().selectExcelColumn(null);
		}
	}
}
