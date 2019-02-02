package edu.scripps.yates.client.ui.wizard.pages.widgets;

import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.ExcelAmountRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ExperimentalConditionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteFilesRatioTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

/**
 * This panel is going to have a left part with the numerator and denominator
 * labels to drop off a condition in the right, we will have the dragable
 * conditions
 * 
 * @author salvador
 *
 */
public class ConditionSelectorForFileWithRatiosWidget extends AbstractConditionSelectorForFileWidget {

	private ExperimentalConditionTypeBean condition1;
	private ExperimentalConditionTypeBean condition2;

	public ConditionSelectorForFileWithRatiosWidget(PintContext context, FileTypeBean file) {
		super(context, file);

		final List<ExperimentalConditionTypeBean> conditions = PintImportCfgUtil
				.getConditions(context.getPintImportConfiguration());

		final FlexTable leftTable = new FlexTable();
		leftTable.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		setWidget(0, 0, leftTable);
		getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);

		final Label label1 = new Label("Ratio definition:");
		label1.setStyleName(WizardStyles.WizardExplanationLabel);
		leftTable.setWidget(0, 0, label1);
		leftTable.getFlexCellFormatter().setColSpan(0, 0, 3);

		final Label l = new Label("Condition");
		l.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(1, 1, l);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);
		final Label l2 = new Label("Sample");
		l2.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(1, 2, l2);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(1, 2, HasHorizontalAlignment.ALIGN_CENTER);

		//
		final Label label2 = new Label("Ratio numerator:");
		label2.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(2, 0, label2);
		final Label labelSample = new Label("-");
		labelSample.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(2, 2, labelSample);
		labelSample.getElement().getStyle().setMarginLeft(10, Unit.PX);
		final AbstractItemDropLabel<ConditionSelectorForFileWithRatiosWidget> dropLabelNumerator = new AbstractItemDropLabel<ConditionSelectorForFileWithRatiosWidget>(
				"drop numerator Condition", DroppableFormat.SAMPLE, this, true) {

			@Override
			protected void updateItemWithData(String conditionID, DroppableFormat format,
					ConditionSelectorForFileWithRatiosWidget form) {
				condition1 = PintImportCfgUtil.getCondition(context.getPintImportConfiguration(), conditionID);
				// set sample in column 3
				if (condition1.getSampleRef() != null) {
					labelSample.setText(condition1.getSampleRef());
					labelSample.setStyleName(WizardStyles.WizardDraggableLabelFixed);
				} else {
					labelSample.setText("-");
					labelSample.setStyleName(WizardStyles.WizardExplanationLabel);
				}
				checkIfBothConditionsAreSelected();
			}
		};
		dropLabelNumerator.getElement().getStyle().setMarginLeft(10, Unit.PX);
		leftTable.setWidget(2, 1, dropLabelNumerator);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
		//
		final Label label3 = new Label("Ratio denominator:");
		label3.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(3, 0, label3);
		final Label labelSample2 = new Label("-");
		labelSample2.setStyleName(WizardStyles.WizardInfoMessage);
		leftTable.setWidget(3, 2, labelSample2);
		labelSample2.getElement().getStyle().setMarginLeft(10, Unit.PX);
		final AbstractItemDropLabel<ConditionSelectorForFileWithRatiosWidget> dropLabelDenominator = new AbstractItemDropLabel<ConditionSelectorForFileWithRatiosWidget>(
				"drop denominator Condition", DroppableFormat.SAMPLE, this, true) {

			@Override
			protected void updateItemWithData(String conditionID, DroppableFormat format,
					ConditionSelectorForFileWithRatiosWidget form) {
				condition2 = PintImportCfgUtil.getCondition(context.getPintImportConfiguration(), conditionID);
				// set sample in column 3
				if (condition2.getSampleRef() != null) {
					labelSample2.setText(condition2.getSampleRef());
					labelSample2.setStyleName(WizardStyles.WizardDraggableLabelFixed);
				} else {
					labelSample2.setText("-");
					labelSample2.setStyleName(WizardStyles.WizardExplanationLabel);
				}
				checkIfBothConditionsAreSelected();
			}
		};
		dropLabelDenominator.getElement().getStyle().setMarginLeft(10, Unit.PX);
		leftTable.setWidget(3, 1, dropLabelDenominator);
		leftTable.getFlexCellFormatter().setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_CENTER);

		// RIGHT PANEL with conditions to drag
		// right with the dragable conditions
		final FlexTable rightTable = new FlexTable();
		rightTable.setStyleName(WizardStyles.WizardQuestionPanelLessRoundCorners);
		setWidget(0, 1, rightTable);
		getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
		//
		final Label label4 = new Label("Experimental Conditions created:");
		label4.setStyleName(WizardStyles.WizardExplanationLabel);
		rightTable.setWidget(0, 0, label4);
		rightTable.getFlexCellFormatter().setColSpan(0, 0, 2);
		//
		final Label label5 = new Label("Drag and drop to define the ratio:");
		label5.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(1, 0, label5);
		rightTable.getFlexCellFormatter().setColSpan(1, 0, 2);
		label5.getElement().getStyle().setMarginTop(10, Unit.PX);
		//
		final Label label61 = new Label("Condition");
		label61.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(2, 0, label61);

		final Label label6 = new Label("Sample");
		label6.setStyleName(WizardStyles.WizardInfoMessage);
		rightTable.setWidget(2, 1, label6);
		int i = 3;
		for (final ExperimentalConditionTypeBean condition : conditions) {

			final ItemDraggableLabel conditionDraggableLabel = new ItemDraggableLabel(condition.getId(),
					DroppableFormat.SAMPLE, condition.getId());
			rightTable.setWidget(i, 0, conditionDraggableLabel);
			final Label label7 = new Label("-");
			label7.setStyleName(WizardStyles.WizardInfoMessage);
			if (condition.getSampleRef() != null) {
				label7.setText(condition.getSampleRef());
				label7.setStyleName(WizardStyles.WizardDraggableLabelFixed);
			}
			rightTable.setWidget(i, 1, label7);
			if (i == 3) {
				// just the first one
				conditionDraggableLabel.getElement().getStyle().setMarginTop(10, Unit.PX);
				label7.getElement().getStyle().setMarginTop(10, Unit.PX);
			}
			i++;
		}
	}

	/**
	 * Run the tasks registered to be run when both conditions are selected or if
	 * both conditions are NOT selected, and updates the {@link PintImportCfgBean}
	 * with the new ratio, as well as removes any other ratio
	 */
	protected void checkIfBothConditionsAreSelected() {
		// first remove any ratio referring to this file id
		PintImportCfgUtil.removeRatiosByFileID(context.getPintImportConfiguration(), file.getId());
		if (condition1 != null && condition2 != null) {
			// do the tasks registered to be run when both conditions are selected
			getOnConditionAddedTask().doSomething(condition1);
			getOnConditionAddedTask().doSomething(condition2);

			// add identifications with this file to both conditions
			PintImportCfgUtil.removeIdentificationsByFileID(context.getPintImportConfiguration(), file.getId());
			addIdentifications();

			// depending on the file format, we will be able to create different levels of
			// ratios
			switch (file.getFormat()) {
			case CENSUS_CHRO_XML:
			case CENSUS_OUT_TXT:
				PintImportCfgUtil.removeRatiosByFileID(context.getPintImportConfiguration(), file.getId());
				addProteinRatio();
				addPSMRatio();
				PintImportCfgUtil.removeQuantificationsByFileID(context.getPintImportConfiguration(), file.getId());
				addAmounts();
				break;
			case EXCEL:
				PintImportCfgUtil.removeRatiosByFileID(context.getPintImportConfiguration(), file.getId());

			case MZIDENTML:
			case DTA_SELECT_FILTER_TXT:
			case FASTA:
				break;
			default:
				break;
			}

		} else {
			// do the tasks registered to be run when NOT both conditions are selected
			getOnConditionRemovedTask().doSomething(condition1);
			getOnConditionRemovedTask().doSomething(condition2);
		}
	}

	private void addAmounts() {
		final RemoteInfoTypeBean quantInfoType = createRemoteInfoTypeBean();
		PintImportCfgUtil.addQuantificationToCondition(context.getPintImportConfiguration(), condition1.getId(),
				quantInfoType);
		final RemoteInfoTypeBean quantInfoType2 = createRemoteInfoTypeBean();
		PintImportCfgUtil.addQuantificationToCondition(context.getPintImportConfiguration(), condition2.getId(),
				quantInfoType2);

	}

	private void addIdentifications() {
		final RemoteInfoTypeBean identificationTypeBean = createRemoteInfoTypeBean();
		PintImportCfgUtil.addIdentificationToCondition(context.getPintImportConfiguration(), condition1.getId(),
				identificationTypeBean);
		final RemoteInfoTypeBean identificationTypeBean2 = createRemoteInfoTypeBean();
		PintImportCfgUtil.addIdentificationToCondition(context.getPintImportConfiguration(), condition2.getId(),
				identificationTypeBean2);
	}

	private RemoteInfoTypeBean createRemoteInfoTypeBean() {
		final RemoteInfoTypeBean ret = new RemoteInfoTypeBean();
		ret.getFileRefs().add(file.getId());
		return ret;
	}

	private RemoteFilesRatioTypeBean createRemoteFileRatioTypeBean() {
		final RemoteFilesRatioTypeBean ratios = new RemoteFilesRatioTypeBean();
		ratios.setNumerator(condition1.getId());
		ratios.setDenominator(condition2.getId());
		ratios.setFileRef(file.getId());
		return ratios;
	}

	private void addExcelPSMRatio() {
		PintImportCfgUtil.removeRatiosByFileID(context.getPintImportConfiguration(), file.getId());
		PintImportCfgUtil.addPSMRatioFromExcel(context.getPintImportConfiguration(), createExcelFileRatioTypeBean());
	}

	private void addExcelPeptideRatio() {
		PintImportCfgUtil.removeRatiosByFileID(context.getPintImportConfiguration(), file.getId());
		PintImportCfgUtil.addPeptideRatioFromExcel(context.getPintImportConfiguration(),
				createExcelFileRatioTypeBean());
	}

	private void addExcelProteinRatio() {
		PintImportCfgUtil.removeRatiosByFileID(context.getPintImportConfiguration(), file.getId());
		PintImportCfgUtil.addProteinRatioFromExcel(context.getPintImportConfiguration(),
				createExcelFileRatioTypeBean());
	}

	private ExcelAmountRatioTypeBean createExcelFileRatioTypeBean() {
		// TODO Auto-generated method stub
		return null;
	}

	private void addPSMRatio() {

		PintImportCfgUtil.addPSMRatio(context.getPintImportConfiguration(), createRemoteFileRatioTypeBean());
	}

	private void addPeptideRatio() {
		PintImportCfgUtil.addPeptideRatio(context.getPintImportConfiguration(), createRemoteFileRatioTypeBean());
	}

	private void addProteinRatio() {
		PintImportCfgUtil.addProteinRatio(context.getPintImportConfiguration(), createRemoteFileRatioTypeBean());
	}

	public ExperimentalConditionTypeBean getCondition1() {
		return condition1;
	}

	public ExperimentalConditionTypeBean getCondition2() {
		return condition2;
	}

}
