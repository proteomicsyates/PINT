package edu.scripps.yates.shared.columns.comparator;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.shared.util.UniprotFeatures;

public class SharedProteinBeanComparator extends AbstractProteinBeanComparator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3483454596575168784L;

	public SharedProteinBeanComparator() {
		super();
	};

	public SharedProteinBeanComparator(ColumnName columnName) {
		super(columnName);
	}

	public SharedProteinBeanComparator(ColumnName columnName, String scoreName) {
		super(columnName, scoreName);
	}

	public SharedProteinBeanComparator(ColumnName columnName, String experimentalConditionName, AmountType amountType,
			String projectTag) {
		super(columnName, experimentalConditionName, amountType, projectTag);
	}

	public SharedProteinBeanComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName);
	}

	public SharedProteinBeanComparator(ColumnName columnName, String experimentalCondition1Name,
			String experimentalCondition2Name, String projectTag, String ratioName, String ratioScoreName) {
		super(columnName, experimentalCondition1Name, experimentalCondition2Name, projectTag, ratioName,
				ratioScoreName);
	}

	@Override
	public int compare(ProteinBean o1, ProteinBean o2) {
		if (columnName == ColumnName.PROTEIN_SCORE && scoreName != null) {
			final ScoreBean score1 = o1.getScoreByName(scoreName);
			final ScoreBean score2 = o2.getScoreByName(scoreName);
			String value1 = null;
			String value2 = null;
			if (score1 != null)
				value1 = score1.getValue();
			if (score2 != null)
				value2 = score2.getValue();

			return compareNumberStrings(value1, value2, true);

		} else if (columnName == ColumnName.PROTEIN_RATIO || columnName == ColumnName.PROTEIN_RATIO_GRAPH) {
			return compareRatios(o1, o2, conditionName, condition2Name, projectTag, ratioName, false);
		} else if (columnName == ColumnName.PROTEIN_RATIO_SCORE) {
			return compareRatioScores(o1, o2, conditionName, condition2Name, projectTag, ratioName, scoreName, false);
		}
		switch (columnName) {
		case ACC:
			return compareStrings(o1.getPrimaryAccession().getAccession(), o2.getPrimaryAccession().getAccession(),
					false);
		case DESCRIPTION:
			String description1 = o1.getPrimaryAccession().getDescription();
			String description2 = o2.getPrimaryAccession().getDescription();
			return compareStrings(description1, description2, true);

		case SPECTRUM_COUNT:
			return Integer.compare(o1.getNumPSMs(), o2.getNumPSMs());
		case SPC_PER_CONDITION:
			return Integer.compare(o1.getNumPSMsByCondition(projectTag, conditionName),
					o2.getNumPSMsByCondition(projectTag, conditionName));
		case COVERAGE:
			return compareNumbers(o1.getCoverage(), o2.getCoverage());
		case PROTEIN_SEQUENCE_COVERAGE_IMG:
			return compareNumbers(o1.getCoverage(), o2.getCoverage());
		case MOL_W:
			return Double.compare(o1.getMw(), o2.getMw());
		case PROTEIN_PI:
			return Double.compare(o1.getPi(), o2.getPi());
		case GENE:
			// take the primary only. If not available, take the rest
			String genesString = o1.getGenesString(true);
			if ("".equals(genesString))
				genesString = o1.getGenesString(false);
			String genesString2 = o2.getGenesString(true);
			if ("".equals(genesString2))
				genesString2 = o2.getGenesString(false);

			return compareStrings(genesString, genesString2, true);
		case SEQUENCE_COUNT:
			return Integer.compare(o1.getNumPeptides(), o2.getNumPeptides());
		case PROTEIN_LENGTH:
			return Integer.compare(o1.getLength(), o2.getLength());
		case ALTERNATIVE_NAMES:
			return compareStrings(o1.getAlternativeNamesString(), o2.getAlternativeNamesString(), true);
		case PROTEIN_FUNCTION:
			return compareStrings(o1.getFunctionString(), o2.getFunctionString(), true);
		case SECONDARY_ACCS:
			return compareStrings(o1.getSecondaryAccessionsString(), o2.getSecondaryAccessionsString(), true);
		case CONDITION:
			return compareStrings(o1.getConditionsString(), o2.getConditionsString(), false);
		case OMIM:
			return compareStrings(o1.getOmimIDString(), o2.getOmimIDString(), true);
		case PROTEIN_GROUP_TYPE:
			return o1.getEvidence().name().compareTo(o2.getEvidence().name());
		case UNIPROT_PROTEIN_EXISTENCE:
			String name1 = "";
			if (o1.getUniprotProteinExistence() != null) {
				name1 = o1.getUniprotProteinExistence().getName();
			}
			String name2 = "";
			if (o2.getUniprotProteinExistence() != null) {
				name2 = o2.getUniprotProteinExistence().getName();
			}
			return compareStrings(name1, name2, true);
		case PROTEIN_ACTIVE_SITE:
		case PROTEIN_DOMAIN_FAMILIES:
		case PROTEIN_NATURAL_VARIATIONS:
		case PROTEIN_SECONDARY_STRUCTURE:
		case PROTEIN_EXPERIMENTAL_INFO:
		case PROTEIN_MOLECULAR_PROCESSING:
		case PROTEIN_PTM:
			final String[] uniprotFeatures = UniprotFeatures.getUniprotFeaturesByColumnName(columnName);
			if (uniprotFeatures != null && uniprotFeatures.length > 0) {
				return SharedDataUtil.getUniprotFeatureString(o1, uniprotFeatures)
						.compareTo(SharedDataUtil.getUniprotFeatureString(o2, uniprotFeatures));
			} else {
				return 0;
			}

		default:
			break;
		}
		return 0;
	}
}
