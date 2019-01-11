package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinGroupBean;

public class SharedProteinGroupComparator extends AbstractProteinGroupBeanComparator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 903294888259694398L;

	public SharedProteinGroupComparator() {
		super();
	}

	public SharedProteinGroupComparator(ColumnName columnName) {
		super(columnName);
	}

	public SharedProteinGroupComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public SharedProteinGroupComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		super(columnName, experimentalConditionName, amountType, projectTag);
	}

	public SharedProteinGroupComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName);
	}

	public SharedProteinGroupComparator(ColumnName columnName, String condition1Name, String condition2Name,
			String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, condition1Name, condition2Name, projectTag, ratioName, ratioScoreName);
	}

	@Override
	public int compare(ProteinGroupBean o1, ProteinGroupBean o2) {
		if (columnName == ColumnName.PROTEIN_RATIO || columnName == ColumnName.PROTEIN_RATIO_GRAPH) {
			return compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false);
		}
		switch (columnName) {
		case ACC:
			return o1.getPrimaryAccessionsString().compareTo(o2.getPrimaryAccessionsString());
		case DESCRIPTION:
			String description1 = o1.getDescriptionsString();
			String description2 = o2.getDescriptionsString();
			if (description1 == null)
				description1 = "";
			if (description2 == null)
				description2 = "";

			return description1.compareToIgnoreCase(description2);
		case SPECTRUM_COUNT:
			return Integer.compare(o1.getNumPSMs(), o2.getNumPSMs());
		case SPC_PER_CONDITION:
			return Integer.compare(o1.getNumPSMsByCondition(projectTag, conditionName),
					o2.getNumPSMsByCondition(projectTag, conditionName));
		case COVERAGE:
			return compareNumbers(o1.get(0).getCoverage(), o2.get(0).getCoverage());
		case PROTEIN_SEQUENCE_COVERAGE_IMG:
			return compareNumbers(o1.get(0).getCoverage(), o2.get(0).getCoverage());

		case GENE:
			final String genesString = o1.getGenesString(true);
			final String genesString2 = o2.getGenesString(true);
			return compareStrings(genesString, genesString2, true);
		case SEQUENCE_COUNT:
			return Integer.compare(o1.getNumPeptides(), o2.getNumPeptides());
		case PROTEIN_GROUP_TYPE:
			return compareStrings(o1.getGroupMemberEvidences(), o2.getGroupMemberEvidences(), true);
		case UNIPROT_PROTEIN_EXISTENCE:
			return o1.getGroupMemberExistences().compareTo(o2.getGroupMemberExistences());
		case NUM_PROTEIN_GROUP_MEMBERS:
			return Integer.compare(o1.getDifferentPrimaryAccessions().size(),
					o2.getDifferentPrimaryAccessions().size());
		case SECONDARY_ACCS:
			return compareStrings(o1.getSecondaryAccessionsString(), o2.getSecondaryAccessionsString(), true);
		case CONDITION:
			return compareStrings(o1.getConditionsString(), o2.getConditionsString(), true);
		default:
			break;
		}
		return 0;
	}

}
