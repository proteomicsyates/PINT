package edu.scripps.yates.client.ui.wizard.pages.panels.summary;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;

import edu.scripps.yates.client.pint.wizard.PintContext;
import edu.scripps.yates.client.pint.wizard.PintImportCfgUtil;
import edu.scripps.yates.client.ui.wizard.styles.WizardStyles;
import edu.scripps.yates.shared.model.projectCreator.excel.LabelTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.OrganismTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SampleTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.TissueTypeBean;

public class SampleSummaryTable extends AbstractSummaryTable {

	public SampleSummaryTable(PintContext context, SampleTypeBean sample, int number) {
		super(number);
		int row = 0;
		// condition number
		final Label sampleNumber = new Label(number + ".");
		sampleNumber.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 0, sampleNumber);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
		// condition name
		final Label sampleLabel = new Label("Sample name:");
		sampleLabel.setStyleName(WizardStyles.WizardInfoMessage);
		sampleLabel.setTitle(sample.getDescription() != null ? sample.getDescription() : sample.getId());
		setWidget(row, 1, sampleLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label sampleNameLabel = new Label(sample.getId());
		sampleNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickableRed);
		sampleNameLabel.setTitle(PintImportCfgUtil.getTitleSample(sample));
		setWidget(row, 2, sampleNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 2, HasHorizontalAlignment.ALIGN_LEFT);
		row++;
		// organism
		final Label organismLabel = new Label("Organism:");
		organismLabel.setTitle("Organism from which sample '" + sample.getId() + "' is derived from");
		organismLabel.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, organismLabel);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label organismNameLabel = new Label(sample.getOrganismRef());
		final OrganismTypeBean organism = PintImportCfgUtil.getOrganism(context.getPintImportConfiguration(),
				sample.getOrganismRef());
		organismNameLabel.setTitle(PintImportCfgUtil.getTitleOrganism(organism));
		organismNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 1, organismNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		row++;
		// tissue
		final Label tissueLabel = new Label("Tissue / Cell line:");
		tissueLabel.setTitle("Tissue or cell line from which sample '" + sample.getId() + "' is derived from");
		tissueLabel.setStyleName(WizardStyles.WizardInfoMessage);
		setWidget(row, 0, tissueLabel);
		getFlexCellFormatter().setColSpan(row, 0, 2);
		getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		final Label tissueNameLabel = new Label(sample.getTissueRef());
		final TissueTypeBean tissue = PintImportCfgUtil.getTissue(context.getPintImportConfiguration(),
				sample.getTissueRef());
		tissueNameLabel.setTitle(PintImportCfgUtil.getTitleTissue(tissue));
		tissueNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
		setWidget(row, 1, tissueNameLabel);
		getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		if (sample.getLabelRef() != null && !"".equals(sample.getLabelRef())) {
			row++;
			// tissue
			final Label labelLabel = new Label("Isobaric label:");
			labelLabel.setTitle("Isobaric label that was used with sample '" + sample.getId() + "'");
			labelLabel.setStyleName(WizardStyles.WizardInfoMessage);
			setWidget(row, 0, labelLabel);
			getFlexCellFormatter().setColSpan(row, 0, 2);
			getFlexCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT);
			final Label labelNameLabel = new Label(sample.getLabelRef());
			final LabelTypeBean label = PintImportCfgUtil.getLabel(context.getPintImportConfiguration(),
					sample.getLabelRef());
			labelNameLabel
					.setTitle(label.getMassDiff() != null ? "Mass difference: " + label.getMassDiff() : label.getId());
			labelNameLabel.setStyleName(WizardStyles.WizardItemWidgetNameLabelNonClickable);
			setWidget(row, 1, labelNameLabel);
			getFlexCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
		}
	}

}
