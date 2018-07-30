package edu.scripps.yates.excel.proteindb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import edu.scripps.yates.cv.CVManager;
import edu.scripps.yates.cv.CommonlyUsedCV;
import edu.scripps.yates.dtaselect.PSMImplFromDTASelect;
import edu.scripps.yates.excel.util.ColumnIndexManager;
import edu.scripps.yates.excel.util.ColumnIndexManager.ColumnName;
import edu.scripps.yates.utilities.grouping.GroupablePeptide;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.ipi.UniprotEntry.UNIPROT_TYPE;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.model.factories.ConditionEx;
import edu.scripps.yates.utilities.model.factories.GeneEx;
import edu.scripps.yates.utilities.model.factories.PSMEx;
import edu.scripps.yates.utilities.model.factories.ProteinAnnotationEx;
import edu.scripps.yates.utilities.model.factories.RatioEx;
import edu.scripps.yates.utilities.model.factories.ScoreEx;
import edu.scripps.yates.utilities.model.factories.ThresholdEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinImpl2 implements Protein {
	private static final Logger log = Logger.getLogger(ProteinImpl2.class);
	private static final String NOT_SUPPORTED = "NOT SUPPORTED";
	public static final ColumnName[] expression_analysis_TMT__126_label = {
			ColumnName.expression_analysis_TMT__126_label_HBE, ColumnName.expression_analysis_TMT__127_label_HBE,
			ColumnName.expression_analysis_TMT__128_label_HBE };
	public static final ColumnName[] expression_analysis_TMT__126_label2 = {
			ColumnName.expression_analysis_TMT__129_label_CFBE, ColumnName.expression_analysis_TMT__130_label_CFBE,
			ColumnName.expression_analysis_TMT__131_label_CFBE };
	public static final ColumnName[] wt_columns = { ColumnName.wt_022810, ColumnName.wt_040910, ColumnName.wt_072408,
			ColumnName.wt_110207, ColumnName.wt_1107, ColumnName.wt_112409, ColumnName.wt_HOAB };
	public static final ColumnName[] mut_columns = { ColumnName.mut_102609, ColumnName.mut_121108,
			ColumnName._0h_092809, ColumnName._0h_110109, ColumnName._0h_091109, ColumnName.mut_OAB, ColumnName.mut_S45,
			ColumnName.mut_S46, ColumnName.mut_S47, ColumnName.mut_S48 };
	public static final ColumnName[] background_columns = { ColumnName.mock_022308, ColumnName.mock_022408,
			ColumnName.mock_061910, ColumnName.silent_021408, ColumnName.silent_072208 };
	public static final ColumnName[] tsa_columns = { ColumnName.TSA_042010, ColumnName.TSA_070708,
			ColumnName.TSA_071908, ColumnName.TSA_Cinhib, ColumnName.TSA_Hinhib };
	public static final ColumnName[] saha_columns = { ColumnName.SAHA1, ColumnName.SAHA2, ColumnName.SAHA3,
			ColumnName.SAHA4 };
	public static final ColumnName[] dmso_column = { ColumnName.DMSO_031010, ColumnName.DMSO_050908,
			ColumnName.DMSO_051508, ColumnName.DMSO_100209 };
	public static final ColumnName[] siHDAC7_columns = { ColumnName.siHDAC7_011909, ColumnName.siHDAC7_030809,
			ColumnName.siHDAC7_031909 };
	public static final ColumnName[] corr4a_columns = { ColumnName.Corr4A_032510, ColumnName.Corr4A_021210,
			ColumnName.Corr4A_032610 };
	public static final ColumnName[] _1h_columns = { ColumnName._1h_092909, ColumnName._1h_111709,
			ColumnName._1h_112909, ColumnName._1h_120909 };
	public static final ColumnName[] _6h_columns = { ColumnName._6h_091109, ColumnName._6h_101209,
			ColumnName._6h_101709, ColumnName._6h_10209 };
	public static final ColumnName[] _24h_columns = { ColumnName._24h_050610, ColumnName._24h_050710 };
	public static final ColumnName[] _24hrev_columns = { ColumnName._24hrev_051010, ColumnName._24hrev_053010 };
	private static String QUANTIFICATION_PVALUE = CVManager.getPreferredName(CommonlyUsedCV.quantificationPValueID);

	/**
	 * List of rows by sheet number
	 */
	private final TIntObjectHashMap<List<Row>> rows;
	private final ColumnIndexManager columnManager;
	private final Set<Amount> amounts = new THashSet<Amount>();
	private final Set<Ratio> proteinRatios = new THashSet<Ratio>();

	private Set<Threshold> thresholds;
	private final Set<PSM> psms = new THashSet<PSM>();
	private final List<Accession> secondaryAccessions = new ArrayList<Accession>();
	private int length;
	private double pi;
	private double mw;
	private Accession primaryAccession;
	private MSRun msRun;
	private ProteinGroup group;
	private ProteinEvidence evidence;
	private final Set<Peptide> peptides = new THashSet<Peptide>();
	private final Set<Condition> conditions = new THashSet<Condition>();
	private final Set<Score> scores = new THashSet<Score>();
	private boolean ratiosParsed = false;
	private final Set<Gene> genes = new THashSet<Gene>();
	private boolean genesParsed = false;
	private static Map<String, String> staticUniprot2IPIMap = new THashMap<String, String>();

	static {

		if (QUANTIFICATION_PVALUE == null)
			QUANTIFICATION_PVALUE = CVManager.getPreferredName(CommonlyUsedCV.quantificationPValueID);
	}

	public ProteinImpl2(ColumnName columnName, TIntObjectHashMap<List<Row>> rows, ColumnIndexManager colManager,
			MSRun msrun) {
		msRun = msrun;
		this.rows = rows;
		if (colManager == null)
			throw new IllegalArgumentException("ColumnIndexManager is null");
		columnManager = colManager;
	}

	private Row getMainRow(int numSheet) {
		return rows.get(numSheet).get(0);
	}

	private Row getRow(int numSheet, int numRow) {
		final List<Row> list = rows.get(numSheet);
		if (list != null && list.size() >= numRow + 1)
			return list.get(numRow);
		return null;
	}

	@Override
	public Set<Gene> getGenes() {
		if (!genesParsed) {
			final Cell cell = getRow(columnManager.getSheetIndex(ColumnName.Official_Gene_symbol), 0)
					.getCell(columnManager.getColumnIndex(ColumnName.Official_Gene_symbol));
			if (cell != null) {

				final Set<String> geneNames = new THashSet<String>();
				final String stringCellValue = cell.getStringCellValue();
				if (stringCellValue.contains(";")) {
					final String[] split = stringCellValue.split(";");
					for (final String string : split) {
						if (!geneNames.contains(string)) {
							genes.add(new GeneEx(string));
							geneNames.add(string);
						}
					}
				} else {
					genes.add(new GeneEx(stringCellValue));
				}

			}
			genesParsed = true;
		}
		return genes;
	}

	private List<String> getProteinNames() {

		final List<Row> rowList = rows.get(columnManager.getSheetIndex(ColumnName.uniprot_annotation_Protein_names));
		final List<String> ret = new ArrayList<String>();
		if (rowList != null) {
			for (int i = 0; i < rowList.size(); i++) {
				final String annotation = getStringValue(getCell(ColumnName.uniprot_annotation_Protein_names, i));
				if (annotation != null) {
					final List<String> proteinDescriptions = parseProteinDescriptions(annotation);
					ret.addAll(proteinDescriptions);
				}
			}
		}
		return ret;

	}

	private List<String> parseProteinDescriptions(String annotation) {
		final List<String> ret = new ArrayList<String>();
		if (annotation.contains("(") && annotation.contains(")")) {
			// before parenthesis
			final String beforeParenthesis = annotation.substring(0, annotation.indexOf("(")).trim();
			if (!"".equals(beforeParenthesis))
				ret.add(beforeParenthesis.trim());
			// between parenthesis
			final int validIndexOfCloseParenthesis = getValidIndexOfCloseParenthesis(annotation);
			final String tmp = annotation.substring(annotation.indexOf("(") + 1, validIndexOfCloseParenthesis);
			ret.add(tmp.trim());
			annotation = annotation.substring(validIndexOfCloseParenthesis + 1);
			final List<String> retTmp = parseProteinDescriptions(annotation);
			ret.addAll(retTmp);
		} else {
			if (!"".equals(annotation))
				ret.add(annotation);
		}

		return ret;
	}

	private int getValidIndexOfCloseParenthesis(String annotation) {

		int numOpenParenthesis = 0;
		for (int i = 0; i < annotation.length(); i++) {
			final char charAt = annotation.charAt(i);
			if (charAt == '(')
				numOpenParenthesis++;
			if (charAt == ')') {
				if (numOpenParenthesis == 1)
					return i;
				else
					numOpenParenthesis--;
			}
		}
		return -1;

	}

	private List<Accession> getUniprotAccessions() {
		final List<Accession> ret = new ArrayList<Accession>();

		// get the IPI Accession and get the mapping from the
		// IPI2Uniprot mapping system
		final Accession ipiACC = getIPIAccession();
		final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance().map2Uniprot(ipiACC.getAccession());
		if (!map2Uniprot.isEmpty()) {
			final List<UniprotEntry> swissProts = getUniprotEntryByType(map2Uniprot, UNIPROT_TYPE.SWISSPROT);
			if (!swissProts.isEmpty()) {
				if (swissProts.size() > 1)
					log.info("There are more than one swisprot map for IPI: " + ipiACC);
				map2Uniprot.clear();
				map2Uniprot.addAll(swissProts);
			}
			for (final UniprotEntry uniprot : map2Uniprot) {
				final AccessionEx accessionEx = new AccessionEx(uniprot.getAcc(), AccessionType.UNIPROT);
				if (uniprot.getName() != null && !"".equals(uniprot.getName())) {
					accessionEx.setDescription(uniprot.getName());
				} else {

					final List<String> proteinNames = getProteinNames();
					if (!proteinNames.isEmpty())
						accessionEx.setDescription(proteinNames.get(0));
					// else
					// log.debug(getProteinNames());
					if (proteinNames.size() > 1) {
						for (int i = 1; i < proteinNames.size(); i++) {
							accessionEx.addAlternativeName(proteinNames.get(i));
						}
					}
				}
				ret.add(accessionEx);
			}
		} else {
			log.debug("no uniprot mapping found for ipi protein: " + ipiACC);
		}
		// NO UNIPROT ACCs ARE CAPTURED

		// also get the uniprots from the columns
		// Row row = getRow(columnManager.getSheetIndex(ColumnName.Uniprot_ID),
		// 0);
		//
		// if (row != null) {
		//
		// // uniprot accession if available
		// int uniprotAccIndex = columnManager
		// .getColumnIndex(ColumnName.Uniprot_ID);
		// Cell cell2 = row.getCell(uniprotAccIndex);
		// if (cell2 != null) {
		// final String uniprotAccValue = getStringValue(cell2);
		// String[] split;
		// if (uniprotAccValue.contains(";")) {
		// split = uniprotAccValue.split(";");
		// } else {
		// split = new String[1];
		// split[0] = uniprotAccValue;
		// }
		//
		// uniprotAcc = split[0].trim();
		//
		// }
		//
		// }
		// if (ret.size() > 1)
		// System.out.println("ASDF");
		return ret;

	}

	private List<UniprotEntry> getUniprotEntryByType(Collection<UniprotEntry> entries, UniprotEntry.UNIPROT_TYPE type) {
		final List<UniprotEntry> ret = new ArrayList<UniprotEntry>();
		if (entries != null) {
			for (final UniprotEntry uniprotEntry : entries) {
				if (uniprotEntry.getType() == type) {
					ret.add(uniprotEntry);
				}
			}
		}
		return ret;
	}

	private Accession getIPIAccession() {

		final Row mainRow = getMainRow(columnManager.getSheetIndex(ColumnName.LOCUS));
		if (mainRow != null) {
			// get the IPI accession
			final int locus = columnManager.getColumnIndex(ColumnName.LOCUS);
			final Cell cell = mainRow.getCell(locus);
			if (cell != null) {
				final AccessionEx accessionEx = new AccessionEx(getStringValue(cell), AccessionType.IPI);
				return accessionEx;
			}
		}
		return null;
	}

	@Override
	public Set<ProteinAnnotation> getAnnotations() {
		final Set<ProteinAnnotation> ret = new THashSet<ProteinAnnotation>();

		// forst parse "uniprot annotation" column, that contains different
		// annotations
		final List<Row> rowList = rows.get(columnManager.getSheetIndex(ColumnName.UNIPROT_ANNOTATION));
		for (int i = 0; i < rowList.size(); i++) {
			final String annotation = getStringValue(getCell(ColumnName.UNIPROT_ANNOTATION, i));
			if (annotation != null) {
				final Map<AnnotationType, List<String>> parsedAnnotations = AnnotationType.parseAnnotations(annotation);
				for (final AnnotationType annotationType : parsedAnnotations.keySet()) {
					final List<String> list = parsedAnnotations.get(annotationType);
					for (final String annotationValue : list) {
						ret.add(new ProteinAnnotationEx(annotationType, annotationType.getKey(), annotationValue));
					}
				}
			}
		}

		// uniprot status
		ret.addAll(getProteinAnnotations(AnnotationType.status, ColumnName.uniprot_annotation__Status));
		// uniprot domains
		ret.addAll(getProteinAnnotations(AnnotationType.domain, ColumnName.uniprot_annotation_Domains));
		// uniprot GO
		ret.addAll(getProteinAnnotations(AnnotationType.GO, ColumnName.uniprot_annotation_Gene_ontology__GO_));
		// uniprot GO IDs
		// ret.addAll(getProteinAnnotations(AnnotationType.GO_IDs,
		// ColumnName.uniprot_annotation_Gene_ontology_IDs));
		// uniprot catalytic activity
		ret.addAll(getProteinAnnotations(AnnotationType.catalytic_activity,
				ColumnName.uniprot_annotation_General_annotation__CATALYTIC_ACTIVITY_));
		// uniprot function
		ret.addAll(getProteinAnnotations(AnnotationType.function,
				ColumnName.uniprot_annotation_General_annotation__FUNCTION_));
		// uniprot disease
		ret.addAll(getProteinAnnotations(AnnotationType.disease,
				ColumnName.uniprot_annotation_General_annotation__DISEASE_));

		// uniprot pathway
		ret.addAll(getProteinAnnotations(AnnotationType.pathway,
				ColumnName.uniprot_annotation_General_annotation__PATHWAY_));
		// uniprot subcellular location
		ret.addAll(getProteinAnnotations(AnnotationType.subcellular_location,
				ColumnName.uniprot_annotation_General_annotation__SUBCELLULAR_LOCATION_));
		// uniprot subunit
		ret.addAll(getProteinAnnotations(AnnotationType.subunit,
				ColumnName.uniprot_annotation_General_annotation__SUBUNIT_));

		return ret;
	}

	private Set<ProteinAnnotationEx> getProteinAnnotations(AnnotationType annotationType, ColumnName columnName) {
		final Set<ProteinAnnotationEx> ret = new THashSet<ProteinAnnotationEx>();
		final List<Row> rowList = rows.get(columnManager.getSheetIndex(columnName));
		for (int i = 0; i < rowList.size(); i++) {
			String annotation = getStringValue(getCell(columnName, i));
			if (annotation != null) {
				if (annotation.toLowerCase().startsWith(annotationType.getKey().toLowerCase() + ":"))
					annotation = annotation.substring(annotation.indexOf(":") + 1).trim();
				final ProteinAnnotationEx proteinAnnotation = new ProteinAnnotationEx(annotationType,
						annotationType.getKey(), annotation);
				ret.add(proteinAnnotation);
			}
		}
		return ret;
	}

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public Set<Ratio> getRatios() {
		if (!ratiosParsed) {

			final Ratio tmtExpressionAnalysisRatio = getTMTExpressionAnalysisRatio();
			if (tmtExpressionAnalysisRatio != null)
				proteinRatios.add(tmtExpressionAnalysisRatio);

			final Ratio mutRatio = getMUTRatio();
			if (mutRatio != null)
				proteinRatios.add(mutRatio);

			final Ratio wtRatio = getWTRatio();
			if (wtRatio != null)
				proteinRatios.add(wtRatio);

			final Ratio generalRatio = getGeneralRatio();
			if (generalRatio != null)
				proteinRatios.add(generalRatio);

			final Ratio sahaRatio = getSAHARatio();
			if (sahaRatio != null)
				proteinRatios.add(sahaRatio);

			ratiosParsed = true;
		}
		return proteinRatios;
	}

	private Ratio getSAHARatio() {

		final Double ratio = getDoubleValue(getCell(ColumnName.SAHA_ratio_BKG, 0));
		if (ratio != null) {
			// just to make sure that conditions are already created:
			getAmounts();

			final Condition wtcondition = ProteinSetAdapter
					.getExperimentalCondition(edu.scripps.yates.excel.proteindb.enums.ConditionName.WT, null, null);
			final Condition mutcondition = ProteinSetAdapter
					.getExperimentalCondition(edu.scripps.yates.excel.proteindb.enums.ConditionName.SAHA, null, null);

			final RatioEx proteinRatio = new RatioEx(2, wtcondition, mutcondition, "Ratio over background for SAHA",
					AggregationLevel.PROTEIN);
			// pvalue of the ratio
			final Double value = getDoubleValue(getCell(ColumnName.SAHA_P, 0));
			if (value != null) {
				final String description = "Probability value (P) for DeltaF508 CFTR interactors upon SAHA treatment to distinguish from background";
				final ScoreEx score = new ScoreEx(value.toString(), ColumnName.SAHA_P.name(), QUANTIFICATION_PVALUE,
						description);
				proteinRatio.setAssociatedConfidenceScore(score);
			}
			return proteinRatio;
		} else {
			return null;
		}
	}

	private Ratio getGeneralRatio() {
		final Double ratio = getDoubleValue(getCell(ColumnName.ALL_ratio_BKG, 0));
		if (ratio != null) {
			// just to make sure that conditions are already created:
			getAmounts();

			final Sample superSample = ProteinSetAdapter.getSample(
					"sample: every sample excepting to DeltaF508, wt and SAHA", false, false,
					ProteinSetAdapter.humanOrganism, ProteinSetAdapter.tissue);
			final Condition clusterExperimentalCondition = ProteinSetAdapter.getExperimentalCondition(
					edu.scripps.yates.excel.proteindb.enums.ConditionName.ALL_CONDITIONS,
					"All proteins from every experiment excepting to DeltaF508, wt and SAHA", superSample);

			final Condition background = ProteinSetAdapter
					.getExperimentalCondition(edu.scripps.yates.excel.proteindb.enums.ConditionName.WT, null, null);

			final RatioEx proteinRatio = new RatioEx(ratio, clusterExperimentalCondition, background,
					"Ratio over background for all other experiments DeltaF508, wt and SAHA", AggregationLevel.PROTEIN);

			// pvalue of the ratio
			final Double pvalue = getDoubleValue(getCell(ColumnName.All_P, 0));
			if (pvalue != null) {
				final String description = "Probability value (P) for  DeltaF508 CFTR interactors in drug-treated experiments or temperature-experiments to distinguish from background";
				final ScoreEx score = new ScoreEx(pvalue.toString(), ColumnName.All_P.name(), QUANTIFICATION_PVALUE,
						description);
				proteinRatio.setAssociatedConfidenceScore(score);
			}
			return proteinRatio;
		} else {
			return null;
		}
	}

	private Ratio getWTRatio() {
		final Double spc = getDoubleValue(getCell(ColumnName.WT_ratio_BKG, 0));
		if (spc != null) {
			// just to make sure that conditions are already created:
			getAmounts();

			final Condition wtcondition = ProteinSetAdapter.getExperimentalCondition(
					edu.scripps.yates.excel.proteindb.enums.ConditionName.BACKGROUND, null, null);
			final Condition mutcondition = ProteinSetAdapter
					.getExperimentalCondition(edu.scripps.yates.excel.proteindb.enums.ConditionName.WT, null, null);
			final RatioEx proteinRatio = new RatioEx(4, wtcondition, mutcondition,
					"Ratio over background for DeltaF508", AggregationLevel.PROTEIN);
			// pvalue of the ratio
			final Double value = getDoubleValue(getCell(ColumnName.WT_P, 0));
			if (value != null) {
				final String description = "p-value for wt CFTR Interators to distinguish from background";
				final ScoreEx score = new ScoreEx(value.toString(), ColumnName.WT_P.name(), QUANTIFICATION_PVALUE,
						description);
				proteinRatio.setAssociatedConfidenceScore(score);
			}
			return proteinRatio;
		} else {
			return null;
		}
	}

	private Ratio getMUTRatio() {
		final Double spc = getDoubleValue(getCell(ColumnName.MUT_ratio_BKG, 0));
		if (spc != null) {
			// just to make sure that conditions are already created:
			getAmounts();

			final Condition wtcondition = ProteinSetAdapter.getExperimentalCondition(
					edu.scripps.yates.excel.proteindb.enums.ConditionName.BACKGROUND, null, null);
			final Condition mutcondition = ProteinSetAdapter
					.getExperimentalCondition(edu.scripps.yates.excel.proteindb.enums.ConditionName.MUT, null, null);
			final RatioEx proteinRatio = new RatioEx(1, wtcondition, mutcondition,
					"Ratio over background for DeltaF508", AggregationLevel.PROTEIN);
			// pvalue of the ratio
			final Double value = getDoubleValue(getCell(ColumnName.MUT_P, 0));
			if (value != null) {
				final String description = "p-value for DeltaF508 CFTR Interators to distinguish from background";
				final ScoreEx score = new ScoreEx(value.toString(), ColumnName.MUT_P.name(), QUANTIFICATION_PVALUE,
						description);
				proteinRatio.setAssociatedConfidenceScore(score);
			}
			return proteinRatio;
		} else {
			return null;
		}
	}

	private Ratio getTMTExpressionAnalysisRatio() {
		final Double ratioValue = getDoubleValue(getCell(ColumnName.expression_analysis_TMT_log2_R_, 0));
		if (ratioValue != null) {

			final Condition wtcondition = ProteinSetAdapter
					.getExperimentalCondition(edu.scripps.yates.excel.proteindb.enums.ConditionName.WT, null, null);
			final Condition mutcondition = ProteinSetAdapter
					.getExperimentalCondition(edu.scripps.yates.excel.proteindb.enums.ConditionName.MUT, null, null);
			final RatioEx proteinRatio = new RatioEx(ratioValue, wtcondition, mutcondition, "fold change (wt/DeltaF)",
					AggregationLevel.PROTEIN);
			// pvalue of the ratio
			final Double value = getDoubleValue(getCell(ColumnName.expression_analysis_TMT_p_value, 0));
			if (value != null) {
				final String description = "p-value, unpaired t-test for differential expression (two-tailed and two-sample t-test on every protein)";
				final ScoreEx score = new ScoreEx(value.toString(), ColumnName.expression_analysis_TMT_p_value.name(),
						QUANTIFICATION_PVALUE, description);
				proteinRatio.setAssociatedConfidenceScore(score);
			}
			return proteinRatio;
		} else {
			return null;
		}
	}

	// private Set<ProteinAmount> getScrambledsiRNAAmounts(Experiment
	// experiment) {
	// Set<ProteinAmount> amounts = new THashSet<ProteinAmount>();
	//
	// // DMSO Condition
	// ExperimentalCondition scrambledsiRNA = getExperimentalCondition(
	// "ScrambledsiRNA", "ScrambledsiRNA treated");
	//
	// // Protein amounts for each 3 replicates:
	// // Set<ProteinAmount> replicatesAmounts = new THashSet<ProteinAmount>();
	// // ColumnName[] columnNames = { ColumnName.DMSO_031010,
	// // ColumnName.DMSO_050908, ColumnName.DMSO_051508 };
	// // replicatesAmounts = getProteinAmounts(columnNames,
	// // hDAC7siRNACondition,
	// // experiment, AmountType.SPC);
	// // if (!replicatesAmounts.isEmpty()) {
	// // amounts.addAll(replicatesAmounts);
	//
	// final Set<ProteinAmount> proteinAmounts = getProteinAmounts(
	// new ColumnName[] { ColumnName.HDAC7si_SUM }, scrambledsiRNA,
	// experiment);
	// if (proteinAmounts != null)
	// amounts.addAll(proteinAmounts);
	// // }
	// return amounts;
	// }

	// private Set<ProteinAmount> getMockIPsAmounts() {
	// Set<ProteinAmount> amounts = new THashSet<ProteinAmount>();
	//
	// // 1h 30C Condition
	// ExperimentalCondition mockIPs_Condition = getExperimentalCondition(
	// "Mock IPs", "Mock IPs");
	//
	// // Protein amounts for each 4 replicates:
	// // Set<ProteinAmount> replicatesAmounts = new THashSet<ProteinAmount>();
	// // ColumnName[] columnNames = { ColumnName.DMSO_031010,
	// // ColumnName.DMSO_050908, ColumnName.DMSO_051508 };
	// // replicatesAmounts = getProteinAmounts(columnNames,
	// // hDAC7siRNACondition,
	// // experiment);
	// // if (!replicatesAmounts.isEmpty()) {
	// // amounts.addAll(replicatesAmounts);
	//
	// final Set<ProteinAmount> proteinAmounts = getProteinAmounts(
	// new ColumnName[] {}, mockIPs_Condition);
	// if (proteinAmounts != null)
	// amounts.addAll(proteinAmounts);
	// // }
	// return amounts;
	// }

	// private Set<ProteinAmount> getNullIPsAmounts() {
	// Set<ProteinAmount> amounts = new THashSet<ProteinAmount>();
	//
	// // 1h 30C Condition
	// ExperimentalCondition nullIPs_Condition = getExperimentalCondition(
	// "Null IPs", "Null IPs");
	//
	// // Protein amounts for each 4 replicates:
	// // Set<ProteinAmount> replicatesAmounts = new THashSet<ProteinAmount>();
	// // ColumnName[] columnNames = { ColumnName.DMSO_031010,
	// // ColumnName.DMSO_050908, ColumnName.DMSO_051508 };
	// // replicatesAmounts = getProteinAmounts(columnNames,
	// // hDAC7siRNACondition,
	// // experiment, AmountType.SPC);
	// // if (!replicatesAmounts.isEmpty()) {
	// // amounts.addAll(replicatesAmounts);
	//
	// final Set<ProteinAmount> proteinAmounts = getProteinAmounts(
	// new ColumnName[] {}, nullIPs_Condition);
	// if (proteinAmounts != null)
	// amounts.addAll(proteinAmounts);
	// // }
	// return amounts;
	// }

	public void addDMSOSumAmount(double dmsoSumAmount) {
		final Sample sample = ProteinSetAdapter.getSample("DMSO treated sample", false, false,
				ProteinSetAdapter.humanOrganism, ProteinSetAdapter.tissue);
		final ConditionEx dmsoCondition = ProteinSetAdapter.getExperimentalCondition(
				edu.scripps.yates.excel.proteindb.enums.ConditionName.DMSO, "DMSO treated", sample);

		final AmountEx proteinAmount = new AmountEx(dmsoSumAmount, AmountType.SPC, CombinationType.SUM, dmsoCondition);

		amounts.add(proteinAmount);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String ipiAcc = ModelUtils.getAccessions(this, AccessionType.IPI).iterator().next().getAccession();
		final StringBuilder sb = new StringBuilder();
		final List<Accession> uniprotAccs = ModelUtils.getAccessions(this, AccessionType.UNIPROT);
		if (!uniprotAccs.isEmpty())
			sb.append("(");
		for (final Accession acc : uniprotAccs) {
			if (!"(".equals(sb.toString()))
				sb.append("|");
			sb.append(acc.getAccession());
		}
		if (!"".equals(sb.toString()))
			sb.append(")");
		return ipiAcc + sb.toString();
	}

	@Override
	public Set<Threshold> getThresholds() {
		if (thresholds != null)
			return thresholds;
		thresholds = new THashSet<Threshold>();
		final Set<Threshold> coreInteractomeThresholds = getCoreInteractomeThresholds();
		if (!coreInteractomeThresholds.isEmpty()) {

			thresholds.addAll(coreInteractomeThresholds);
		}
		return thresholds;
	}

	private Set<Threshold> getCoreInteractomeThresholds() {
		final Set<Threshold> ret = new THashSet<Threshold>();

		// 4A
		Boolean bool = getBooleanValue(getCell(ColumnName.Xscorefilter_4A, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getCorr4AThreshold(bool);
			ret.add(thresholdCombination);
		}

		// HDAC7si
		bool = getBooleanValue(getCell(ColumnName.Xscorefilter_HDAC7si, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThresholdHDACsi(bool);
			ret.add(thresholdCombination);
		}

		// Saha
		bool = getBooleanValue(getCell(ColumnName.Xc_score_Saha, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThresholdSAHA(bool);
			ret.add(thresholdCombination);
		}

		// TSA
		bool = getBooleanValue(getCell(ColumnName.Xscorefilter_TSA, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThresholdTSA(bool);
			ret.add(thresholdCombination);
		}

		// 1h
		bool = getBooleanValue(getCell(ColumnName.Xscorefilter_1h, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThreshold_1H(bool);
			ret.add(thresholdCombination);
		}

		// 24h
		bool = getBooleanValue(getCell(ColumnName.Xscorefilter_24h, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThreshold_24H(bool);
			ret.add(thresholdCombination);
		}

		// 24h_rev
		bool = getBooleanValue(getCell(ColumnName._24h_rev_Xscorefilter, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThreshold_24HREV(bool);
			ret.add(thresholdCombination);
		}

		// 6h
		bool = getBooleanValue(getCell(ColumnName.Xscorefilter_6h, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThreshold_6H(bool);
			ret.add(thresholdCombination);
		}

		// global core-interactome
		bool = getBooleanValue(getCell(ColumnName.Xcscorefilter, 0));
		if (bool != null) {
			final Threshold thresholdCombination = getThreshold_Global(bool);
			ret.add(thresholdCombination);
		}

		return ret;
	}

	private Threshold getThreshold_Global(Boolean pass) {
		final String name = ColumnName.Xcscorefilter.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and (WT_global > 1 or MUT_global > 1))" + "or(MUT_P> .929)"
				+ "or(SAHA_P > .949 and (WT_global > 1 or MUT_global > 1))" + "or(WT_P > .88);1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);
		return threshold;
	}

	private Threshold getThreshold_6H(Boolean pass) {
		final String name = ColumnName.Xscorefilter_6h.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and ( ${6h_SUM} > 0 ))" + "or(MUT_P> .929 and (${6h_SUM} >0))"
				+ "or(SAHA_P > .949 and (${6h_SUM} > 0))" + "or(WT_P > .88 and (${6h_SUM}>0));1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	private Threshold getThreshold_24HREV(Boolean pass) {
		final String name = ColumnName._24h_rev_Xscorefilter.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and ( ${24H-rev_SUM} > 0 ))"
				+ "or(MUT_P> .929 and (${24H-rev_SUM}>0))" + "or(SAHA_P > .949 and (${24H-rev_SUM} > 0))"
				+ "or(WT_P > .88 and (${24H-rev_SUM}>0));1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	private Threshold getThreshold_24H(Boolean pass) {
		final String name = ColumnName.Xscorefilter_24h.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and ( ${24h_SUM} > 0 ))" + "or(MUT_P> .929 and (${24h_SUM} >0))"
				+ "or(SAHA_P > .949 and (${24h_SUM} > 0))" + "or(WT_P > .88 and (${24h_SUM}>0));1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	private Threshold getThreshold_1H(Boolean pass) {
		final String name = ColumnName.Xscorefilter_1h.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and ( ${1H_SUM} > 0 ))" + "or(MUT_P> .929 and (${1H_SUM} >0))"
				+ "or(SAHA_P > .949 and (${1H_SUM} > 0))" + "or(WT_P > .88 and (${1H_SUM}>0));1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	private Threshold getThresholdTSA(Boolean pass) {
		final String name = ColumnName.Xscorefilter_TSA.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and ( ${TSA_SUM} > 0 ))" + "or(MUT_P> .929 and (${TSA_SUM}>0))"
				+ "or(SAHA_P > .949 and (${TSA_SUM} > 0))" + "or(WT_P > .88 and (${TSA_SUM}>0));1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	private Threshold getThresholdSAHA(Boolean pass) {
		final String name = ColumnName.Xc_score_Saha.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and (WT_global > 1 or MUT_global > 1))" + "or(MUT_P> .929)"
				+ "or(SAHA_P > .949 and SAHA_glob >1) " + "or(WT_P > .88);1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	private Threshold getThresholdHDACsi(Boolean pass) {
		final String name = ColumnName.Xscorefilter_HDAC7si.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and ( ${HDAC7si_SUM} > 0 ))"
				+ "or(MUT_P> .929 and (${HDAC7si_SUM}>0))" + "or(SAHA_P > .949 and (${HDAC7si_SUM} > 0))"
				+ "or(WT_P > .88 and (${HDAC7si_SUM}>0));1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	private Threshold getCorr4AThreshold(Boolean pass) {
		final String name = ColumnName.Xscorefilter_4A.name();
		ThresholdEx threshold = null;

		final String description = "If ((All_P > .959 and ( ${4A_SUM} > 0 ))" + "or(MUT_P> .929 and (${4A_SUM}>0))"
				+ "or(SAHA_P > .949 and (${4A_SUM} > 0))" + "or(WT_P > .88 and (${4A_SUM}>0));1)";
		threshold = new ThresholdEx(name, pass);
		threshold.setDescription(description);

		return threshold;
	}

	@Override
	public Boolean passThreshold(String thresholdName) {
		final Set<Threshold> thresholds = getThresholds();

		for (final Threshold threshold2 : thresholds) {
			if (threshold2.getName().equals(thresholdName))
				return threshold2.isPassThreshold();
		}

		return null;
	}

	private Boolean getBooleanValue(Cell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			final String string = cell.getStringCellValue();
			if (string != null) {
				if (string.equals("1") || string.toLowerCase().equals("true"))
					return new Boolean(true);
				if (string.equals("0") || string.toLowerCase().equals("false"))
					return new Boolean(false);
			}
			break;
		case Cell.CELL_TYPE_NUMERIC:

			final double value = cell.getNumericCellValue();
			if (value == 1)
				return new Boolean(true);
			else if (value == 0)
				return new Boolean(false);
		}
		return null;
	}

	private String getStringValue(Cell cell) {
		if (cell == null)
			return null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		default:
			return NOT_SUPPORTED;
		}
	}

	private Double getDoubleValue(Cell cell) {
		if (cell == null)
			return null;
		try {

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				return Double.valueOf(cell.getStringCellValue());
			case Cell.CELL_TYPE_NUMERIC:

				return cell.getNumericCellValue();

			default:
				return null;
			}

		} catch (final NumberFormatException e) {

		}
		return null;
	}

	private Cell getCell(ColumnName columnName, int rowIndex) {
		final Cell cell = getRow(columnManager.getSheetIndex(columnName), rowIndex)
				.getCell(columnManager.getColumnIndex(columnName));
		return cell;
	}

	@Override
	public Set<PSM> getPSMs() {
		return psms;
	}

	public void addPSM(PSM psm, Condition condition) {

		if (!getMSRun().equals(psm.getMSRun()))
			throw new IllegalArgumentException("A psm from a different msRun cannot be linked with this protein");

		if (psm instanceof PSMImplFromDTASelect) {
			final PSMEx psmEx = new PSMEx(psm.getIdentifier(), psm.getSequence(), psm.getFullSequence());
			psmEx.setMSRun(msRun);
			psmEx.setAfterSeq(psm.getAfterSeq());
			psmEx.setBeforeSeq(psm.getBeforeSeq());
			psmEx.setCalculatedMH(psm.getCalcMH());
			psmEx.setExperimentalMH(psm.getExperimentalMH());
			psmEx.setIonProportion(psm.getIonProportion());
			psmEx.setMassErrorPPM(psm.getMassErrorPPM());
			psmEx.setPi(psm.getPI());
			psmEx.setPtms(psm.getPTMs());
			psmEx.setRelation(psm.getRelation());
			psmEx.setScores(psm.getScores());
			psmEx.setSpr(psm.getSPR());
			psmEx.setTotalIntensity(psm.getTotalIntensity());

			// ((PSMImplFromDTASelect) psm).getProteins().add(this);
			if (condition != null) {
				// create a psm amount per each psm
				final AmountEx psmAmount = new AmountEx(1.0, AmountType.SPC, condition);
				psmEx.addAmount(psmAmount);
				psmEx.addCondition(condition);
				conditions.add(condition);
			}
			addPSM(psmEx);
		} else {
			addPSM(psm);

		}
	}

	public Peptide addPeptide(Peptide peptide, Condition condition) {
		if (!peptide.getMSRun().equals(getMSRun()))
			throw new IllegalArgumentException("A peptide from a different msRun cannot be linked with this protein");

		// if (peptide instanceof PeptideImplFromProteinsAndPSMs) {
		// PeptideEx pep = new PeptideEx(peptide.getSequence(), msRun);
		// pep.getProteins().add(this);
		// pep.setScores(peptide.getScores());
		// for (Ratio ratio : peptide.getRatios()) {
		// pep.addRatio(ratio);
		// }
		//
		// if (condition != null) {
		// // create a peptide amount per each psm
		// AmountEx peptideAmount = new AmountEx(peptide.getPSMs().size(),
		// AmountType.SPC, condition);
		// pep.getAmounts().add(peptideAmount);
		// pep.getConditions().add(condition);
		// conditions.add(condition);
		// }
		// peptides.add(pep);
		// return pep;
		// } else {
		peptides.add(peptide);
		return peptide;
		// }
	}

	@Override
	public List<Accession> getSecondaryAccessions() {
		if (secondaryAccessions.isEmpty()) {
			final Accession primaryAccession2 = getPrimaryAccession();
			// UNIPROT ACCs
			final List<Accession> uniprotAccessions = getUniprotAccessions();
			for (final Accession accession : uniprotAccessions) {
				if (accession.equals(primaryAccession2))
					continue;
				secondaryAccessions.add(accession);
			}

			// IPI ACCs
			final AccessionEx ipiAccession = (AccessionEx) getIPIAccession();
			if (!ipiAccession.equals(primaryAccession2)) {
				final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance()
						.map2Uniprot(ipiAccession.getAccession());
				final List<String> proteinNames = new ArrayList<String>();
				if (!map2Uniprot.isEmpty()) {
					for (final UniprotEntry uniprotEntry : map2Uniprot) {
						if (uniprotEntry.getType() == UNIPROT_TYPE.SWISSPROT) {
							proteinNames.add(uniprotEntry.getName());
							break;
						}
					}
					if (proteinNames.isEmpty()) {
						proteinNames.add(map2Uniprot.iterator().next().getName());
					}
				}
				if (proteinNames.isEmpty()) {
					proteinNames.addAll(getProteinNames());
				}
				if (!proteinNames.isEmpty())
					ipiAccession.setDescription(proteinNames.get(0));
				if (proteinNames.size() > 1) {
					for (int i = 1; i < proteinNames.size(); i++) {
						ipiAccession.addAlternativeName(proteinNames.get(i));
					}
				}
				secondaryAccessions.add(ipiAccession);
			}
		}
		return secondaryAccessions;
	}

	@Override
	public int getDBId() {
		return 0;
	}

	@Override
	public Accession getPrimaryAccession() {
		if (primaryAccession == null) {
			// only map to Uniprot if the resulting Uniprot entry has not been
			// used before by another IPI entry

			// get the first from uniprot
			final List<Accession> uniprotAccessions = getUniprotAccessions();
			final Accession ipiAccession = getIPIAccession();
			if (uniprotAccessions != null && !uniprotAccessions.isEmpty()) {
				final Accession uniprotAcc = uniprotAccessions.get(0);
				boolean takeUniprot = false;
				if (!staticUniprot2IPIMap.containsKey(uniprotAcc.getAccession())) {
					takeUniprot = true;
				} else if (staticUniprot2IPIMap.get(uniprotAcc.getAccession()).equals(ipiAccession.getAccession())) {
					takeUniprot = true;
				}
				if (takeUniprot) {
					primaryAccession = uniprotAcc;
					staticUniprot2IPIMap.put(uniprotAcc.getAccession(), ipiAccession.getAccession());
				}
			}
			if (primaryAccession == null) {
				if (!uniprotAccessions.isEmpty())
					System.out.println("There was  uniprot entries");
				// if not uniprot, get the IPI one
				primaryAccession = ipiAccession;
			}
		}
		return primaryAccession;
	}

	@Override
	public String getAccession() {
		return getPrimaryAccession().getAccession();
	}

	@Override
	public int getLength() {
		return length;
	}

	@Override
	public double getPi() {
		return pi;
	}

	@Override
	public double getMW() {
		return mw;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	@Override
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param pi
	 *            the pi to set
	 */
	@Override
	public void setPi(double pi) {
		this.pi = pi;
	}

	/**
	 * @param mw
	 *            the mw to set
	 */
	@Override
	public void setMw(double mw) {
		this.mw = mw;
	}

	@Override
	public void addAmount(Amount proteinAmount) {
		if (!amounts.contains(proteinAmount))
			amounts.add(proteinAmount);
		addCondition(proteinAmount.getCondition());
	}

	@Override
	public String getSequence() {
		return null;
	}

	@Override
	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;

	}

	@Override
	public void setProteinGroup(ProteinGroup proteinGroup) {
		group = proteinGroup;

	}

	@Override
	public Set<Peptide> getPeptides() {
		return peptides;
	}

	@Override
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	@Override
	public ProteinGroup getProteinGroup() {
		return group;
	}

	@Override
	public Organism getOrganism() {
		return ProteinSetAdapter.humanOrganism;
	}

	@Override
	public Set<Score> getScores() {
		return scores;
	}

	@Override
	public Set<Condition> getConditions() {
		return conditions;
	}

	@Override
	public MSRun getMSRun() {
		return msRun;
	}

	@Override
	public List<GroupablePeptide> getGroupablePeptides() {
		final List<GroupablePeptide> ret = new ArrayList<GroupablePeptide>();
		ret.addAll(getPSMs());
		return ret;
	}

	public void setMsRun(MSRun msRun2) {
		msRun = msRun2;

	}

	@Override
	public void addRatio(Ratio ratio) {
		if (!proteinRatios.contains(ratio))
			proteinRatios.add(ratio);
	}

	@Override
	public void addCondition(Condition condition) {
		if (!conditions.contains(condition))
			conditions.add(condition);

	}

	@Override
	public void addScore(Score score) {
		if (!scores.contains(score)) {
			scores.add(score);
		}

	}

	@Override
	public void addPSM(PSM psm) {
		if (!psms.contains(psm))
			psms.add(psm);

	}

	@Override
	public void setOrganism(Organism organism) {
		organism = organism;

	}

	@Override
	public void setMSRun(MSRun msRun) {
		this.msRun = msRun;

	}

	@Override
	public void addPeptide(Peptide peptide) {
		if (!peptides.contains(peptide))
			peptides.add(peptide);

	}

	@Override
	public void addGene(Gene gene) {
		if (!genes.contains(gene))
			genes.add(gene);

	}

}
