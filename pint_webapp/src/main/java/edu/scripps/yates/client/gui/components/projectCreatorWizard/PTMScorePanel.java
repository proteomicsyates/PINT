package edu.scripps.yates.client.gui.components.projectCreatorWizard;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

import edu.scripps.yates.client.ImportWizardServiceAsync;
import edu.scripps.yates.client.util.StatusReportersRegister;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PtmScoreTypeBean;
import edu.scripps.yates.shared.util.Pair;

public class PTMScorePanel
		extends ContainsExcelColumnRefPanelAndTable<Pair<String, PtmScoreTypeBean>, PtmScoreTypeBean> {

	private final String tableHeader;
	private TextBox scoreNameTextBox;
	private final ListBox comboBoxProteinScoreType;
	private final TextArea textAreaDescription;
	private final ImportWizardServiceAsync service = ImportWizardServiceAsync.Util.getInstance();
	private final SuggestBox suggestBoxModificationName;
	private final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private final String sessionID;

	public PTMScorePanel(String sessionID, FileTypeBean excelFileBean, String tableHeader,
			PtmScoreTypeBean ptmScoreTypeBean) {
		super(excelFileBean, ptmScoreTypeBean);
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
		reloadTableIfChange(scoreNameTextBox);
		scoreNameTextBox.setAlignment(TextAlignment.RIGHT);
		flexTable.setWidget(3, 1, scoreNameTextBox);

		Label lblType = new Label("Score type:");
		flexTable.setWidget(4, 0, lblType);

		comboBoxProteinScoreType = new ListBox();
		reloadTableIfChange(comboBoxProteinScoreType);
		flexTable.setWidget(4, 1, comboBoxProteinScoreType);
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
		reloadTableIfChange(textAreaDescription);
		textAreaDescription.setVisibleLines(5);
		flexTable.setWidget(5, 1, textAreaDescription);
		textAreaDescription.setWidth("100%");
		flexTable.getCellFormatter().setVerticalAlignment(5, 1, HasVerticalAlignment.ALIGN_MIDDLE);
		flexTable.getCellFormatter().setVerticalAlignment(5, 0, HasVerticalAlignment.ALIGN_TOP);
		flexTable.getCellFormatter().setHorizontalAlignment(5, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		Label lblModificationName = new Label("Modification name:");
		flexTable.setWidget(6, 0, lblModificationName);
		flexTable.getCellFormatter().setHorizontalAlignment(6, 0, HasHorizontalAlignment.ALIGN_RIGHT);

		suggestBoxModificationName = new SuggestBox(oracle);
		reloadTableIfChange(suggestBoxModificationName);
		flexTable.setWidget(6, 1, suggestBoxModificationName);
		flexTable.getCellFormatter().setHorizontalAlignment(6, 1, HasHorizontalAlignment.ALIGN_LEFT);

		addHandlers();
		getScoreTypes();
		getModificationNamesSuggestions();

		if (ptmScoreTypeBean != null) {
			updateGUIFromObjectData();
		} else {
			updateRepresentedObject();
		}
		if ("".equals(scoreNameTextBox.getText())) {
			scoreNameTextBox.setText("My score");
		}

	}

	private void getModificationNamesSuggestions() {
		service.getPTMNames(sessionID, new AsyncCallback<List<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
			}

			@Override
			public void onSuccess(List<String> result) {
				if (result != null)
					oracle.addAll(result);

			}
		});
	}

	/**
	 * Calls to getScoreTypes using the webservice
	 */
	private void getScoreTypes() {
		service.getScoreTypes(sessionID, new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> scoreTypes) {
				if (scoreTypes != null) {
					for (String scoreType : scoreTypes) {
						comboBoxProteinScoreType.addItem(scoreType, scoreType);
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				StatusReportersRegister.getInstance().notifyStatusReporters(caught);
			}
		});

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
	public List<CustomColumn<Pair<String, PtmScoreTypeBean>>> getInitColumns() {
		List<CustomColumn<Pair<String, PtmScoreTypeBean>>> ret = new ArrayList<CustomColumn<Pair<String, PtmScoreTypeBean>>>();

		CustomColumn<Pair<String, PtmScoreTypeBean>> ptmScoreColumn = new CustomColumn<Pair<String, PtmScoreTypeBean>>(
				tableHeader) {
			@Override
			public String getValue(Pair<String, PtmScoreTypeBean> pair) {
				final String rawValue = pair.getFirstElement();
				final PtmScoreTypeBean ptmScoreType = pair.getSecondElement();

				if (ptmScoreType != null) {
					// TODO
					return rawValue;
				}
				return null;
			}
		};
		ret.add(ptmScoreColumn);
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
		representedObject.setDescription(textAreaDescription.getValue());

		representedObject.setModificationName(suggestBoxModificationName.getValue());

		representedObject.setScoreName(scoreNameTextBox.getValue());
		if (comboBoxProteinScoreType.getSelectedIndex() > -1)
			representedObject
					.setScoreType(comboBoxProteinScoreType.getValue(comboBoxProteinScoreType.getSelectedIndex()));
		else
			representedObject.setScoreType(null);
	}

	@Override
	public boolean isValid() {
		updateRepresentedObject();
		if (representedObject.getColumnRef() == null)
			return false;
		if (representedObject.getDescription() == null || "".equals(representedObject.getDescription()))
			return false;
		if (representedObject.getModificationName() == null || "".equals(representedObject.getModificationName()))
			return false;
		if (representedObject.getScoreName() == null || "".equals(representedObject.getScoreName()))
			return false;
		if (representedObject.getScoreType() == null || "".equals(representedObject.getScoreType()))
			return false;
		return true;
	}

	@Override
	public PtmScoreTypeBean getObject() {
		return representedObject;
	}

	@Override
	public void updateGUIFromObjectData(PtmScoreTypeBean dataObject) {
		setRepresentedObject(dataObject);
		updateGUIFromObjectData();

	}

	@Override
	public void updateGUIFromObjectData() {
		if (representedObject != null) {
			scoreNameTextBox.setValue(representedObject.getScoreName());
			ProjectCreatorWizardUtil.selectInCombo(comboBoxProteinScoreType, representedObject.getScoreType());
			suggestBoxModificationName.setValue(representedObject.getModificationName());
			textAreaDescription.setValue(representedObject.getDescription());
			getExcelColumnRefPanel().selectExcelColumn(representedObject.getColumnRef());
		} else {
			scoreNameTextBox.setValue("");
			suggestBoxModificationName.setValue("");
			textAreaDescription.setValue("");
			ProjectCreatorWizardUtil.selectInCombo(comboBoxProteinScoreType, null);
			getExcelColumnRefPanel().selectExcelColumn(null);
		}

	}
}
