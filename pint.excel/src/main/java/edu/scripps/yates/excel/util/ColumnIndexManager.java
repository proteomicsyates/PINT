package edu.scripps.yates.excel.util;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * This class implements the methods needed for knowing which index has each
 * column in an excel table
 *
 * @author Salva
 *
 */
public class ColumnIndexManager {
	public static enum ColumnName {
		Official_Gene_symbol, _4A_SUM, Converted_1_Error_SAHA, Converted_1_Global_average_SAHA, Converted_1_SAHA_ratio_BKG, Corr4A_032510, DMSO_031010, DMSO_050908, DMSO_051508, DMSO_100209, HDAC7si_SUM, SAHA_glob, SAHA_P, SAHA1, SAHA2, SAHA3, SAHA4, TSA_070708, TSA_071908, TSA_1s_down, TSA_Cinhib, TSA_error, TSA_global_average, TSA_Hinhib, TSA_SUM, Xc_score_Saha, Xscorefilter_4A, Xscorefilter_HDAC7si, Xscorefilter_TSA, expression_analysis_TMT_log2_R_, expression_analysis_TMT_p_value, Process, UNIPROT_ANNOTATION, uniprot_annotation_Domains, uniprot_annotation_Features, uniprot_annotation_Gene_names, uniprot_annotation_Gene_ontology__GO_, uniprot_annotation_Gene_ontology_IDs, uniprot_annotation_General_annotation__CATALYTIC_ACTIVITY_, uniprot_annotation_General_annotation__DISEASE_, uniprot_annotation_General_annotation__FUNCTION_, uniprot_annotation_General_annotation__GENERAL_, uniprot_annotation_General_annotation__PATHWAY_, uniprot_annotation_General_annotation__SUBCELLULAR_LOCATION_, uniprot_annotation_General_annotation__SUBUNIT_, LOCUS, Uniprot_ID, uniprot_annotation_Protein_names, _1H_SUM, _24h_rev_Xscorefilter, _24h_SUM, _24H_rev_SUM, _6h_SUM, Converted_1_1h_2s_regulated, Converted_1_1h_error, Converted_1_1h_global_average, Converted_1_24h_error, Converted_1_24h_global_average, Converted_1_6h_error, Converted_1_6h_global_average, Xscorefilter_1h, Xscorefilter_24h, Xscorefilter_6h, _0h_091109, _0h_092809, _0h_110109, Converted_1_down_1s_, Converted_1_down_2s_, Converted_1_Error_log_1_sqrt, Converted_1_Global_average, Converted_1_MUT_ratio_BKG, Converted_1_up_1s_, Converted_1_up_2s_, Converted_1_WT_ratio_BKG, mut_102609, mut_121108, MUT_1s, MUT_2s, MUT_2s_up, MUT_global, mut_OAB, MUT_only, MUT_P, mut_S45, mut_S46, mut_S47, mut_S48, wt_022810, wt_040910, wt_072408, wt_110207, wt_1107, wt_112409, WT_1s, WT_1s_up, WT_2s, WT_2s_up, WT_global, wt_HOAB, WT_only, WT_P, All_P, Converted_1_ALL_ratio_BKG, all_experiments, Controll_Summe, Controll_Summe_MUT_and_WT_P, Function, Genemania_ID, MUT_1s_down_Count, mut_8_occurrance, mut_occurrance, MUT_only_Count, NICHT_Kern, Occurrance, Pathway, _774_toscott_Official_Gene_symbol, Xcscorefilter_Count, WT_only_Count, wt_occurrance, Converted_1_Uniprot, converted_mutant_2__Official_Gene_Symbol, converted_mutant_2__wt_speccount, New_Core_Interactor, SAHA_New_Ratio, TSA_042010, SAHA_New_P, MUT_P_Count, WT_P_Count, SAHA_P_Count, All_P_Count, calculated_overlap, calculated_overlap_Count, Duplicates, Zwiebel_kern, Zwiebel_Count, Zwiebel_Running_Count, MUT_1s_up_Count, Kern_Count, MUT_2s_up_Count, MUT_2s_down_Count, Controll_Summe_Count, Controll_Summe_MUT_and_WT_P_Count, Zwiebel_MUT_only, Zwiebel_MUT_only_Count, Zwiebel_WT_only, Zwiebel_WT_only_Count, WT_1s_Count, MUT_1s_Count, WT_2s_Count, MUT_2s_Count, Quersumme, Quersume_Count, Filed_2, maxP_ols_core, maxP_new_core, _1sigma, MUT_LOGSUM, Field_3, WT_LOGSUM, _1h_LOGSUM, _6h_LOGSUM, _24h_LOGSUM, _24H_rev_LOGSUM, SAHA_LOGSUM, TSA_LOGSUM, _4A_LOGSUM, HDAC7_LOGSUM, DMSO_LOGSUM, Xscorefilter_24h_Count, _24h_rev_Xscorefilter_Count, sequence_coverage, Corr4A_032610, Corr4A_021210, siHDAC7_011909, siHDAC7_030809, siHDAC7_031909, _1h_112909, _1h_120909, _1h_092909, _1h_111709, _6h_091109, _6h_101709, _6h_10209, _6h_101209, _24h_050610, _24h_050710, _24hrev_051010, _24hrev_053010, Nodesize_mutant, AVG_SUM_, AVG_SUM__CFTR, Global_average, Error__log_1_sqrt___, select___2s_, select__2s_, up__2s_, down__2s_, select___1s_, _select___1s_, up__1s_, down__1s_, New_P_All, Global_average_SAHA, Error_SAHA, SAHA_select2s_pos, SAHA_select2s_neg, SAHA_2s_up, SAHA_2s_down, SAHA_select1s_pos, SAHA_select1s_neg, SAHA_1s_up, SAHA_1s_down, TSA_select2s_pos, TSA_2s_up, TSA_2s_down, TSA_2s_regulated, TSA_select1s_pos, TSA_select1s_neg, TSA_1s_up, TSA_1s_regulated, TSA_select2s_neg, DMSO_global_average, DMSO_error, DMSO_sum, DMSO_select2s_pos, DMSO_select2s_neg, DMSO_2s_up, DMSO_2s_down, DMSO_2s_regulated, DMSO_select1s_pos, DMSO_select1s_neg, DMSO_1s_up, DMSO_1s_down, DMSO_1s_regulated, Corr4A_global_average, Corr4A_error, Corr4A_select2s_pos, Corr4A_select2s_neg, Corr4A_2s_up, Corr4A_2s_down, Corr4A_2s_regulated, Corr4A_select1s_pos, Corr4A_select1s_neg, Corr4A_1s_up, Corr4A_1s_down, Corr4A_1s_regulated, HDAC7_GLOB_AVERAGE, HDAC7_error, HDAC7_select2s_pos, HDAC7_select2s_neg, HDAC7_2s_up, HDAC7_2s_down, HDAC7_2s_regulated, HDAC7_select1s_pos, HDAC7_select1s_neg, HDAC7_1s_up, HDAC7_1s_down, HDAC7_1s_regulated, _1h_global_average, _1h_error, _1h_select2s_pos, _1h_select2s_neg, _1h_2s_up, _1h_2s_down, _1h_2s_regulated, _1h_select1s_pos, _1h_select1s_neg, _1h_1s_up, _1h_1S_down, _1h_1s_regulated, _6h_global_average, _6h_error, _6h_select2s_pos, _6h_select2s_neg, _6h_2s_up, _6h_2s_down, _6h_2s_regulated, _6h_select1s_pos, _6h_select1s_neg, _6h_1s_up, _6h_1S_down, _6h_1s_regulated, _24h_global_average, _24h_error, _24h_select2s_pos, _24h_select2s_neg, _24h_2s_up, _24h_2s_down, _24h_2s_regulated, _24h_select1s_pos, _24h_select1s_neg, _24h_1s_up, _24h_1S_down, _24h_1s_regulated, _24hrev_global_average, _24hrev_error, _24hrev_select2s_pos, _24hrev_select2s_neg, _24hrev_2s_up, _24hrev_2s_down, _24hrev_2s_regulated, _24hrev_select1s_pos, _24hrev_select1s_neg, _24hrev_1s_up, _24hrev_1S_down, _24hrev_1s_regulated, differenz, Nodesize_wt, Nodesize_SAHA, Nodesize_TSA, Nodesize_4A, Nodesize_DMSO, Nodesize_HDAC7, Nodesize_1h, Nodesize_6h, Nodesize_24h, Nodesize_24h_rev, WT_ratio_BKG, MUT_ratio_BKG, SAHA_ratio_BKG, ALL_ratio_BKG, _24H_LOGSUM, SCR_LOGSUM, MUT_calculated__LOGSUM_rest, WT_calculated_LOGSUM, SAHA_calculated_LOGSUM, _24h_calculated_LOGSUM, expression_analysis_TMT__126_label_HBE, expression_analysis_TMT__127_label_HBE, expression_analysis_TMT__128_label_HBE, expression_analysis_TMT__129_label_CFBE, expression_analysis_TMT__130_label_CFBE, expression_analysis_TMT__131_label_CFBE, expression_analysis_TMT__Uniprot_Identifier, expression_analysis_TMT__negLOG10_p_, expression_analysis_TMT__regulated_proteins, expression_analysis_TMT__126_MS3, expression_analysis_TMT__127_MS3, expression_analysis_TMT__128_MS3, expression_analysis_TMT__129_MS3, expression_analysis_TMT__130_MS3, expression_analysis_TMT__131_MS3, expression_analysis_TMT__log2_R__MS3, expression_analysis_TMT__negLOG10p_MS3, expression_analysis_TMT__126_label_MS3, expression_analysis_TMT__127_label_MS3, expression_analysis_TMT__128_label_MS3, expression_analysis_TMT__129_label_MS3, expression_analysis_TMT__130_label_MS3, expression_analysis_TMT__131_label_MS3, expression_analysis_TMT__p_value_MS3, expression_analysis_TMT__log2_R__MS3_n, expression_analysis_TMT__neglog10_p__MS3, uniprot_annotation__Entry, uniprot_annotation__Entry_name, uniprot_annotation__Status, uniprot_annotation__General_annotation__CATALYTIC_ACTIVITY_, uniprot_annotation__Locus, condition, replicate_name, Xcscorefilter, mock_061910, mock_022408, mock_022308, silent_072208, silent_021408
	};

	private final HashMap<String, Integer> columnNameColumnIndexHash = new HashMap<String, Integer>();
	private final HashMap<String, Integer> columnNameSheetNumberHash = new HashMap<String, Integer>();

	private static final Logger log = Logger.getLogger(ColumnIndexManager.class);

	public ColumnIndexManager(HashMap<Integer, List<String>> columnNames) {

		final Set<Integer> sheetNumbers = columnNames.keySet();
		for (Integer sheetNumber : sheetNumbers) {
			int index = 0;
			log.debug("Sheet " + sheetNumber);
			for (String columnName : columnNames.get(sheetNumber)) {

				log.debug("Column " + columnName);
				ColumnName columnNameObj = getColumnName(columnName);
				if (columnNameObj != null) {
					if (columnNameColumnIndexHash.containsKey(columnNameObj.name())) {
						log.warn("Column " + columnNameObj.name() + " is repeated!! It will be ignored");
					} else {
						columnNameColumnIndexHash.put(columnNameObj.name(), index);
						columnNameSheetNumberHash.put(columnNameObj.name(), sheetNumber);
					}
				} else {
					// log.warn("Column not recognized: " + columnName);
				}
				index++;

			}
		}

	}

	public ColumnName getColumnName(String columnName) {
		columnName = columnName.replace("ausgangsfile_copy::", "");
		columnName = columnName.trim();
		switch (columnName) {
		case "Official Gene symbol":
			return ColumnName.Official_Gene_symbol;
		case "Uniprot ID":
			return ColumnName.Uniprot_ID;
		case "locus":
			return ColumnName.LOCUS;
		case "MUT_P":
			return ColumnName.MUT_P;
		case "WT_P":
			return ColumnName.WT_P;
		case "Converted 1::Global_average":
			return ColumnName.Converted_1_Global_average;
		case "Converted 1::Error _log_1_sqrt___":
			return ColumnName.Converted_1_Error_log_1_sqrt;
		case "Converted 1::down _1s_":
			return ColumnName.Converted_1_down_1s_;
		case "Converted 1::down _2s_":
			return ColumnName.Converted_1_down_2s_;
		case "Converted 1::up _1s_":
			return ColumnName.Converted_1_up_1s_;
		case "Converted 1::up _2s_":
			return ColumnName.Converted_1_up_2s_;
		case "uniprot_annotation::Domains":
			return ColumnName.uniprot_annotation_Domains;
		case "uniprot_annotation::Protein names":
			return ColumnName.uniprot_annotation_Protein_names;
		case "_0h_091109":
			return ColumnName._0h_091109;
		case "_0h_092809":
			return ColumnName._0h_092809;
		case "_0h_110109":
			return ColumnName._0h_110109;
		case "1H_SUM":
			return ColumnName._1H_SUM;
		case "24h_rev_Xscorefilter":
			return ColumnName._24h_rev_Xscorefilter;
		case "24h_SUM":
			return ColumnName._24h_SUM;
		case "24H-rev_SUM":
			return ColumnName._24H_rev_SUM;
		case "4A_SUM":
			return ColumnName._4A_SUM;
		case "6h_SUM":
			return ColumnName._6h_SUM;

		case "All_P":
			return ColumnName.All_P;
		case "Controll_Summe":
			return ColumnName.Controll_Summe;
		case "Controll_Summe_MUT_and_WT_P":
			return ColumnName.Controll_Summe_MUT_and_WT_P;
		case "Function":
			return ColumnName.Function;
		case "Genemania_ID":
			return ColumnName.Genemania_ID;
		case "HDAC7si_SUM":
			return ColumnName.HDAC7si_SUM;
		case "mut_102609":
			return ColumnName.mut_102609;
		case "mut_121108":
			return ColumnName.mut_121108;
		case "MUT_1s_down Count":
			return ColumnName.MUT_1s_down_Count;
		case "MUT_2s_up":
			return ColumnName.MUT_2s_up;
		case "mut_8_occurance":
			return ColumnName.mut_8_occurrance;
		case "MUT_global":
			return ColumnName.MUT_global;

		case "mut_occurance":
			return ColumnName.mut_occurrance;
		case "MUT_only":
			return ColumnName.MUT_only;
		case "MUT_only Count":
			return ColumnName.MUT_only_Count;
		case "NICHT_Kern":
			return ColumnName.NICHT_Kern;
		case "Occurance":
			return ColumnName.Occurrance;
		case "Pathway":
			return ColumnName.Pathway;
		case "Process":
			return ColumnName.Process;
		case "SAHA_glob":
			return ColumnName.SAHA_glob;
		case "SAHA_P":
			return ColumnName.SAHA_P;
		case "TSA_SUM":
			return ColumnName.TSA_SUM;
		case "Uniprot annotation":
			return ColumnName.UNIPROT_ANNOTATION;
		case "WT_1s_up":
			return ColumnName.WT_1s_up;
		case "WT_2s_up":
			return ColumnName.WT_2s_up;
		case "WT_global":
			return ColumnName.WT_global;
		case "wt_occurance":
			return ColumnName.wt_occurrance;
		case "WT_only":
			return ColumnName.WT_only;
		case "WT_only Count":
			return ColumnName.WT_only_Count;
		case "Xc_score_Saha":
			return ColumnName.Xc_score_Saha;
		case "Xcscorefilter":
			return ColumnName.Xcscorefilter;
		case "Xcscorefilter Count":
			return ColumnName.Xcscorefilter_Count;
		case "Xscorefilter_1h":
			return ColumnName.Xscorefilter_1h;
		case "Xscorefilter_24h":
			return ColumnName.Xscorefilter_24h;
		case "Xscorefilter_4A":
			return ColumnName.Xscorefilter_4A;
		case "Xscorefilter_6h":
			return ColumnName.Xscorefilter_6h;
		case "Xscorefilter_HDAC7si":
			return ColumnName.Xscorefilter_HDAC7si;
		case "Xscorefilter_TSA":
			return ColumnName.Xscorefilter_TSA;
		case "_774_toscott::Official Gene symbol":
			return ColumnName._774_toscott_Official_Gene_symbol;
		case "Converted 1::1h_2s_regulated":
			return ColumnName.Converted_1_1h_2s_regulated;
		case "Converted 1::1h_error":
			return ColumnName.Converted_1_1h_error;
		case "Converted 1::1h_global average":
			return ColumnName.Converted_1_1h_global_average;
		case "Converted 1::24h_error":
			return ColumnName.Converted_1_24h_error;
		case "Converted 1::24h_global average":
			return ColumnName.Converted_1_24h_global_average;
		case "Converted 1::6h_error":
			return ColumnName.Converted_1_6h_error;
		case "Converted 1::6h_global average":
			return ColumnName.Converted_1_6h_global_average;
		case "Converted 1::ALL_ratio_BKG":
			return ColumnName.Converted_1_ALL_ratio_BKG;
		case "Converted 1::Error_SAHA":
			return ColumnName.Converted_1_Error_SAHA;
		case "Converted 1::Global_average_SAHA":
			return ColumnName.Converted_1_Global_average_SAHA;
		case "Converted 1::MUT_ratio_BKG":
			return ColumnName.Converted_1_MUT_ratio_BKG;
		case "Converted 1::SAHA_ratio_BKG":
			return ColumnName.Converted_1_SAHA_ratio_BKG;
		case "Converted 1::Uniprot":
			return ColumnName.Converted_1_Uniprot;
		case "Converted 1::WT_ratio_BKG":
			return ColumnName.Converted_1_WT_ratio_BKG;
		case "converted_mutant 2::Official Gene Symbol":
			return ColumnName.converted_mutant_2__Official_Gene_Symbol;
		case "converted_mutant 2::wt_speccount":
			return ColumnName.converted_mutant_2__wt_speccount;
		case "expression_analysis_TMT::log2(R)":
			return ColumnName.expression_analysis_TMT_log2_R_;
		case "expression_analysis_TMT::p-value":
			return ColumnName.expression_analysis_TMT_p_value;
		case "uniprot_annotation::Features":
			return ColumnName.uniprot_annotation_Features;
		case "uniprot_annotation::Gene names":
			return ColumnName.uniprot_annotation_Gene_names;
		case "uniprot_annotation::Gene ontology _GO_":
			return ColumnName.uniprot_annotation_Gene_ontology__GO_;
		case "uniprot_annotation::Gene ontology IDs":
			return ColumnName.uniprot_annotation_Gene_ontology_IDs;
		case "uniprot_annotation::General annotation_CATALYTIC_ACTIVITY_":
			return ColumnName.uniprot_annotation_General_annotation__CATALYTIC_ACTIVITY_;
		case "uniprot_annotation::General annotation _DISEASE_":
			return ColumnName.uniprot_annotation_General_annotation__DISEASE_;
		case "uniprot_annotation::General annotation _FUNCTION_":
			return ColumnName.uniprot_annotation_General_annotation__FUNCTION_;
		case "uniprot_annotation::General annotation _GENERAL_":
			return ColumnName.uniprot_annotation_General_annotation__GENERAL_;
		case "uniprot_annotation::General annotation _PATHWAY_":
			return ColumnName.uniprot_annotation_General_annotation__PATHWAY_;
		case "uniprot_annotation::General annotation _SUBCELLULAR_LOCATION_":
			return ColumnName.uniprot_annotation_General_annotation__SUBCELLULAR_LOCATION_;
		case "uniprot_annotation::General annotation _SUBUNIT_":
			return ColumnName.uniprot_annotation_General_annotation__SUBUNIT_;
		case "Corr4A_032510":
			return ColumnName.Corr4A_032510;
		case "DMSO_031010":
			return ColumnName.DMSO_031010;
		case "DMSO_050908":
			return ColumnName.DMSO_050908;
		case "DMSO_051508":
			return ColumnName.DMSO_051508;
		case "DMSO_100209":
			return ColumnName.DMSO_100209;
		case "mut_OAB":
			return ColumnName.mut_OAB;
		case "mut_S45":
			return ColumnName.mut_S45;
		case "mut_S46":
			return ColumnName.mut_S46;
		case "mut_S47":
			return ColumnName.mut_S47;
		case "mut_S48":
			return ColumnName.mut_S48;
		case "New_Core_Interactor":
			return ColumnName.New_Core_Interactor;
		case "SAHA_New_P":
			return ColumnName.SAHA_New_P;
		case "SAHA_New_Ratio":
			return ColumnName.SAHA_New_Ratio;
		case "SAHA1":
			return ColumnName.SAHA1;
		case "SAHA2":
			return ColumnName.SAHA2;
		case "SAHA3":
			return ColumnName.SAHA3;
		case "SAHA4":
			return ColumnName.SAHA4;
		case "TSA_042010":
			return ColumnName.TSA_042010;
		case "TSA_070708":
			return ColumnName.TSA_070708;
		case "TSA_071908":
			return ColumnName.TSA_071908;
		case "TSA_1s_down":
			return ColumnName.TSA_1s_down;
		case "TSA_Cinhib":
			return ColumnName.TSA_Cinhib;
		case "TSA_error":
			return ColumnName.TSA_error;
		case "TSA_global_average":
			return ColumnName.TSA_global_average;
		case "TSA_Hinhib":
			return ColumnName.TSA_Hinhib;
		case "wt_022810":
			return ColumnName.wt_022810;
		case "wt_040910":
			return ColumnName.wt_040910;
		case "wt_072408":
			return ColumnName.wt_072408;
		case "wt_110207":
			return ColumnName.wt_110207;
		case "wt_1107":
			return ColumnName.wt_1107;
		case "wt_112409":
			return ColumnName.wt_112409;
		case "wt_HOAB":
			return ColumnName.wt_HOAB;
		case "WT_P Count":
			return ColumnName.WT_P_Count;
		case "MUT_P Count":
			return ColumnName.MUT_P_Count;
		case "SAHA_P Count":
			return ColumnName.SAHA_P_Count;
		case "All_P Count":
			return ColumnName.All_P_Count;
		case "calculated overlap":
			return ColumnName.calculated_overlap;
		case "calculated overlap Count":
			return ColumnName.calculated_overlap_Count;
		case "Duplicates":
			return ColumnName.Duplicates;
		case "Zwiebel_kern":
			return ColumnName.Zwiebel_kern;
		case "Zwiebel Count":
			return ColumnName.Zwiebel_Count;
		case "Zwiebel Running Count":
			return ColumnName.Zwiebel_Running_Count;
		case "MUT_1s_up Count":
			return ColumnName.MUT_1s_up_Count;
		case "Kern Count":
			return ColumnName.Kern_Count;
		case "MUT_2s_up Count":
			return ColumnName.MUT_2s_up_Count;
		case "MUT_2s_down Count":
			return ColumnName.MUT_2s_down_Count;
		case "Controll_Summe Count":
			return ColumnName.Controll_Summe_Count;
		case "Controll_Summe_MUT_and_WT_P Count":
			return ColumnName.Controll_Summe_MUT_and_WT_P_Count;
		case "Xcscore_filter Count":
			return ColumnName.Xcscorefilter_Count;
		case "Zwiebel_MUT_only":
			return ColumnName.Zwiebel_MUT_only;
		case "Zwiebel_MUT_only Count":
			return ColumnName.Zwiebel_MUT_only_Count;
		case "Zwiebel_WT_only":
			return ColumnName.Zwiebel_WT_only;
		case "Zwiebel_WT_only Count":
			return ColumnName.Zwiebel_WT_only_Count;
		case "WT_1s":
			return ColumnName.WT_1s;
		case "WT_1s Count":
			return ColumnName.WT_1s_Count;
		case "MUT_1s":
			return ColumnName.MUT_1s;
		case "MUT_1s Count":
			return ColumnName.MUT_1s_Count;
		case "WT_2s":
			return ColumnName.WT_2s;
		case "WT_2s Count":
			return ColumnName.WT_2s_Count;
		case "MUT_2s":
			return ColumnName.MUT_2s;
		case "MUT_2s Count":
			return ColumnName.MUT_2s_Count;
		case "Quersumme":
			return ColumnName.Quersumme;
		case "Quersumme Count":
			return ColumnName.Quersume_Count;
		case "Field 2":
			return ColumnName.Filed_2;
		case "maxP_old_core":
			return ColumnName.maxP_ols_core;
		case "maxP_new-core":
			return ColumnName.maxP_new_core;
		case "1sigma":
			return ColumnName._1sigma;
		case "MUT_LOGSUM":
			return ColumnName.MUT_LOGSUM;
		case "Field 3":
			return ColumnName.Field_3;
		case "WT_LOGSUM":
			return ColumnName.WT_LOGSUM;
		case "1h_LOGSUM":
			return ColumnName._1h_LOGSUM;
		case "6h_LOGSUM":
			return ColumnName._6h_LOGSUM;
		case "24h_LOGSUM":
			return ColumnName._24h_LOGSUM;
		case "24hrev_LOGSUM":
			return ColumnName._24H_rev_LOGSUM;
		case "SAHA_LOGSUM":
			return ColumnName.SAHA_LOGSUM;
		case "TSA_LOGSUM":
			return ColumnName.TSA_LOGSUM;
		case "4A_LOGSUM":
			return ColumnName._4A_LOGSUM;
		case "HDAC7_LOGSUM":
			return ColumnName.HDAC7_LOGSUM;
		case "DMSO_LOGSUM":
			return ColumnName.DMSO_LOGSUM;
		case "Xscorefilter_24h Count":
			return ColumnName.Xscorefilter_24h_Count;
		case "24h_rev_Xscorefilter Count":
			return ColumnName._24h_rev_Xscorefilter_Count;
		case "sequence_coverage":
			return ColumnName.sequence_coverage;
		case "Corr4A_032610":
			return ColumnName.Corr4A_032610;
		case "Corr4A_021210":
			return ColumnName.Corr4A_021210;

		case "siHDAC7_011909":
			return ColumnName.siHDAC7_011909;

		case "siHDAC7_030809":
			return ColumnName.siHDAC7_030809;

		case "siHDAC7_031909":
			return ColumnName.siHDAC7_031909;

		case "1h_112909":
			return ColumnName._1h_112909;

		case "1h_120909":
			return ColumnName._1h_120909;

		case "1h_092909":
			return ColumnName._1h_092909;

		case "1h_111709":
			return ColumnName._1h_111709;

		case "6h_091109":
			return ColumnName._6h_091109;

		case "6h_101709":
			return ColumnName._6h_101709;

		case "6h_10209":
			return ColumnName._6h_10209;

		case "6h_101209":
			return ColumnName._6h_101209;

		case "24h_050610":
			return ColumnName._24h_050610;

		case "24h_050710":
			return ColumnName._24h_050710;

		case "24hrev_051010":
			return ColumnName._24hrev_051010;

		case "24hrev_053010":
			return ColumnName._24hrev_053010;

		case "Nodesize_mutant":
			return ColumnName.Nodesize_mutant;

		case "AVG_SUM_":
			return ColumnName.AVG_SUM_;

		case "AVG_SUM__CFTR":
			return ColumnName.AVG_SUM__CFTR;

		case "Global_average":
			return ColumnName.Global_average;

		case "Error _log_1_sqrt___":
			return ColumnName.Error__log_1_sqrt___;

		case "select_ _2s_":
			return ColumnName.select___2s_;

		case "select__2s_":
			return ColumnName.select__2s_;

		case "up _2s_":
			return ColumnName.up__2s_;

		case "down _2s_":
			return ColumnName.down__2s_;

		case "select_ _1s_":
			return ColumnName.select___1s_;

		case "_select_ _1s_":
			return ColumnName._select___1s_;

		case "up _1s_":
			return ColumnName.up__1s_;

		case "down _1s_":
			return ColumnName.down__1s_;

		case "New_P_All":
			return ColumnName.New_P_All;

		case "Global_average_SAHA":
			return ColumnName.Global_average_SAHA;

		case "Error_SAHA":
			return ColumnName.Error_SAHA;

		case "SAHA_select2s_pos":
			return ColumnName.SAHA_select2s_pos;

		case "SAHA_select2s_neg":
			return ColumnName.SAHA_select2s_neg;

		case "SAHA_2s_up":
			return ColumnName.SAHA_2s_up;

		case "SAHA_2s_down":
			return ColumnName.SAHA_2s_down;

		case "SAHA_select1s_pos":
			return ColumnName.SAHA_select1s_pos;

		case "SAHA_select1s_neg":
			return ColumnName.SAHA_select1s_neg;

		case "SAHA_1s_up":
			return ColumnName.SAHA_1s_up;

		case "SAHA_1s_down":
			return ColumnName.SAHA_1s_down;

		case "TSA_select2s_pos":
			return ColumnName.TSA_select2s_pos;

		case "TSA_2s_up":
			return ColumnName.TSA_2s_up;

		case "TSA_2s_down":
			return ColumnName.TSA_2s_down;

		case "TSA_2s_regulated":
			return ColumnName.TSA_2s_regulated;

		case "TSA_select1s_pos":
			return ColumnName.TSA_select1s_pos;

		case "TSA_select1s_neg":
			return ColumnName.TSA_select1s_neg;

		case "TSA_1s_up":
			return ColumnName.TSA_1s_up;

		case "TSA_1s_regulated":
			return ColumnName.TSA_1s_regulated;

		case "TSA_select2s_neg":
			return ColumnName.TSA_select2s_neg;

		case "DMSO_global_average":
			return ColumnName.DMSO_global_average;

		case "DMSO_error":
			return ColumnName.DMSO_error;

		case "DMSO_select2s_pos":
			return ColumnName.DMSO_select2s_pos;

		case "DMSO_select2s_neg":
			return ColumnName.DMSO_select2s_neg;

		case "DMSO_2s_up":
			return ColumnName.DMSO_2s_up;

		case "DMSO_2s_down":
			return ColumnName.DMSO_2s_down;

		case "DMSO_2s_regulated":
			return ColumnName.DMSO_2s_regulated;

		case "DMSO_select1s_pos":
			return ColumnName.DMSO_select1s_pos;

		case "DMSO_select1s_neg":
			return ColumnName.DMSO_select1s_neg;

		case "DMSO_1s_up":
			return ColumnName.DMSO_1s_up;

		case "DMSO_1s_down":
			return ColumnName.DMSO_1s_down;

		case "DMSO_1s_regulated":
			return ColumnName.DMSO_1s_regulated;

		case "Corr4A_global_average":
			return ColumnName.Corr4A_global_average;

		case "Corr4A_error":
			return ColumnName.Corr4A_error;

		case "Corr4A_select2s_pos":
			return ColumnName.Corr4A_select2s_pos;

		case "Corr4A_select2s_neg":
			return ColumnName.Corr4A_select2s_neg;

		case "Corr4A_2s_up":
			return ColumnName.Corr4A_2s_up;

		case "Corr4A_2s_down":
			return ColumnName.Corr4A_2s_down;

		case "Corr4A_2s_regulated":
			return ColumnName.Corr4A_2s_regulated;

		case "Corr4A_select1s_pos":
			return ColumnName.Corr4A_select1s_pos;

		case "Corr4A_select1s_neg":
			return ColumnName.Corr4A_select1s_neg;

		case "Corr4A_1s_up":
			return ColumnName.Corr4A_1s_up;

		case "Corr4A_1s_down":
			return ColumnName.Corr4A_1s_down;

		case "Corr4A_1s_regulated":
			return ColumnName.Corr4A_1s_regulated;

		case "HDAC7_GLOB_AVERAGE":
			return ColumnName.HDAC7_GLOB_AVERAGE;

		case "HDAC7_error":
			return ColumnName.HDAC7_error;

		case "HDAC7_select2s_pos":
			return ColumnName.HDAC7_select2s_pos;

		case "HDAC7_select2s_neg":
			return ColumnName.HDAC7_select2s_neg;

		case "HDAC7_2s_up":
			return ColumnName.HDAC7_2s_up;

		case "HDAC7_2s_down":
			return ColumnName.HDAC7_2s_down;

		case "HDAC7_2s_regulated":
			return ColumnName.HDAC7_2s_regulated;

		case "HDAC7_select1s_pos":
			return ColumnName.HDAC7_select1s_pos;

		case "HDAC7_select1s_neg":
			return ColumnName.HDAC7_select1s_neg;

		case "HDAC7_1s_up":
			return ColumnName.HDAC7_1s_up;

		case "HDAC7_1s_down":
			return ColumnName.HDAC7_1s_down;

		case "HDAC7_1s_regulated":
			return ColumnName.HDAC7_1s_regulated;

		case "1h_global average":
			return ColumnName._1h_global_average;

		case "1h_error":
			return ColumnName._1h_error;

		case "1h_select2s_pos":
			return ColumnName._1h_select2s_pos;

		case "1h_select2s_neg":
			return ColumnName._1h_select2s_neg;

		case "1h_2s_up":
			return ColumnName._1h_2s_up;

		case "1h_2s_down":
			return ColumnName._1h_2s_down;

		case "1h_2s_regulated":
			return ColumnName._1h_2s_regulated;

		case "1h_select1s_pos":
			return ColumnName._1h_select1s_pos;

		case "1h_select1s_neg":
			return ColumnName._1h_select1s_neg;

		case "1h_1s_up":
			return ColumnName._1h_1s_up;

		case "1h_1S_down":
			return ColumnName._1h_1S_down;

		case "1h_1s_regulated":
			return ColumnName._1h_1s_regulated;

		case "6h_global average":
			return ColumnName._6h_global_average;

		case "6h_error":
			return ColumnName._6h_error;

		case "6h_select2s_pos":
			return ColumnName._6h_select2s_pos;

		case "6h_select2s_neg":
			return ColumnName._6h_select2s_neg;

		case "6h_2s_up":
			return ColumnName._6h_2s_up;

		case "6h_2s_down":
			return ColumnName._6h_2s_down;

		case "6h_2s_regulated":
			return ColumnName._6h_2s_regulated;

		case "6h_select1s_pos":
			return ColumnName._6h_select1s_pos;

		case "6h_select1s_neg":
			return ColumnName._6h_select1s_neg;

		case "6h_1s_up":
			return ColumnName._6h_1s_up;

		case "6h_1S_down":
			return ColumnName._6h_1S_down;

		case "6h_1s_regulated":
			return ColumnName._6h_1s_regulated;

		case "24h_global average":
			return ColumnName._24h_global_average;

		case "24h_error":
			return ColumnName._24h_error;

		case "24h_select2s_pos":
			return ColumnName._24h_select2s_pos;

		case "24h_select2s_neg":
			return ColumnName._24h_select2s_neg;

		case "24h_2s_up":
			return ColumnName._24h_2s_up;

		case "24h_2s_down":
			return ColumnName._24h_2s_down;

		case "24h_2s_regulated":
			return ColumnName._24h_2s_regulated;

		case "24h_select1s_pos":
			return ColumnName._24h_select1s_pos;

		case "24h_select1s_neg":
			return ColumnName._24h_select1s_neg;

		case "24h_1s_up":
			return ColumnName._24h_1s_up;

		case "24h_1S_down":
			return ColumnName._24h_1S_down;

		case "24h_1s_regulated":
			return ColumnName._24h_1s_regulated;

		case "24hrev_global average":
			return ColumnName._24hrev_global_average;

		case "24hrev_error":
			return ColumnName._24hrev_error;

		case "24hrev_select2s_pos":
			return ColumnName._24hrev_select2s_pos;

		case "24hrev_select2s_neg":
			return ColumnName._24hrev_select2s_neg;

		case "24hrev_2s_up":
			return ColumnName._24hrev_2s_up;

		case "24hrev_2s_down":
			return ColumnName._24hrev_2s_down;

		case "24hrev_2s_regulated":
			return ColumnName._24hrev_2s_regulated;

		case "24hrev_select1s_pos":
			return ColumnName._24hrev_select1s_pos;

		case "24hrev_select1s_neg":
			return ColumnName._24hrev_select1s_neg;

		case "24hrev_1s_up":
			return ColumnName._24hrev_1s_up;

		case "24hrev_1S_down":
			return ColumnName._24hrev_1S_down;

		case "24hrev_1s_regulated":
			return ColumnName._24hrev_1s_regulated;

		case "differenz":
			return ColumnName.differenz;

		case "Nodesize_wt":
			return ColumnName.Nodesize_wt;

		case "Nodesize_SAHA":
			return ColumnName.Nodesize_SAHA;

		case "Nodesize_TSA":
			return ColumnName.Nodesize_TSA;

		case "Nodesize_4A":
			return ColumnName.Nodesize_4A;

		case "Nodesize_DMSO":
			return ColumnName.Nodesize_DMSO;

		case "Nodesize_HDAC7":
			return ColumnName.Nodesize_HDAC7;

		case "Nodesize_1h":
			return ColumnName.Nodesize_1h;

		case "Nodesize_6h":
			return ColumnName.Nodesize_6h;

		case "Nodesize_24h":
			return ColumnName.Nodesize_24h;

		case "Nodesize_24h_rev":
			return ColumnName.Nodesize_24h_rev;

		case "WT_ratio_BKG":
			return ColumnName.WT_ratio_BKG;

		case "MUT_ratio_BKG":
			return ColumnName.MUT_ratio_BKG;

		case "SAHA_ratio_BKG":
			return ColumnName.SAHA_ratio_BKG;

		case "ALL_ratio_BKG":
			return ColumnName.ALL_ratio_BKG;

		case "24H_LOGSUM":
			return ColumnName._24H_LOGSUM;

		case "SCR_LOGSUM":
			return ColumnName.SCR_LOGSUM;

		case "MUT_calculated _LOGSUM_rest":
			return ColumnName.MUT_calculated__LOGSUM_rest;

		case "WT_calculated_LOGSUM":
			return ColumnName.WT_calculated_LOGSUM;

		case "SAHA_calculated_LOGSUM":
			return ColumnName.SAHA_calculated_LOGSUM;

		case "24h_calculated_LOGSUM":
			return ColumnName._24h_calculated_LOGSUM;

		case "expression_analysis_TMT::126_label_HBE":
			return ColumnName.expression_analysis_TMT__126_label_HBE;

		case "expression_analysis_TMT::127_label_HBE":
			return ColumnName.expression_analysis_TMT__127_label_HBE;

		case "expression_analysis_TMT::128_label_HBE":
			return ColumnName.expression_analysis_TMT__128_label_HBE;

		case "expression_analysis_TMT::129_label_CFBE":
			return ColumnName.expression_analysis_TMT__129_label_CFBE;

		case "expression_analysis_TMT::130_label_CFBE":
			return ColumnName.expression_analysis_TMT__130_label_CFBE;

		case "expression_analysis_TMT::131_label_CFBE":
			return ColumnName.expression_analysis_TMT__131_label_CFBE;

		case "expression_analysis_TMT::Uniprot_Identifier":
			return ColumnName.expression_analysis_TMT__Uniprot_Identifier;

		case "expression_analysis_TMT::negLOG10(p)":
			return ColumnName.expression_analysis_TMT__negLOG10_p_;

		case "expression_analysis_TMT::regulated_proteins":
			return ColumnName.expression_analysis_TMT__regulated_proteins;

		case "expression_analysis_TMT::126_MS3":
			return ColumnName.expression_analysis_TMT__126_MS3;

		case "expression_analysis_TMT::127_MS3":
			return ColumnName.expression_analysis_TMT__127_MS3;

		case "expression_analysis_TMT::128_MS3":
			return ColumnName.expression_analysis_TMT__128_MS3;

		case "expression_analysis_TMT::129_MS3":
			return ColumnName.expression_analysis_TMT__129_MS3;

		case "expression_analysis_TMT::130_MS3":
			return ColumnName.expression_analysis_TMT__130_MS3;

		case "expression_analysis_TMT::131_MS3":
			return ColumnName.expression_analysis_TMT__131_MS3;

		case "expression_analysis_TMT::p-value_MS3":
			return ColumnName.expression_analysis_TMT__p_value_MS3;

		case "expression_analysis_TMT::log2(R)_MS3":
			return ColumnName.expression_analysis_TMT__log2_R__MS3;

		case "expression_analysis_TMT::negLOG10p_MS3":
			return ColumnName.expression_analysis_TMT__negLOG10p_MS3;

		case "expression_analysis_TMT::126_label_MS3":
			return ColumnName.expression_analysis_TMT__126_label_MS3;

		case "expression_analysis_TMT::127_label_MS3":
			return ColumnName.expression_analysis_TMT__127_label_MS3;

		case "expression_analysis_TMT::128_label_MS3":
			return ColumnName.expression_analysis_TMT__128_label_MS3;

		case "expression_analysis_TMT::129_label_MS3":
			return ColumnName.expression_analysis_TMT__129_label_MS3;

		case "expression_analysis_TMT::130_label_MS3":
			return ColumnName.expression_analysis_TMT__130_label_MS3;

		case "expression_analysis_TMT::131_label_MS3":
			return ColumnName.expression_analysis_TMT__131_label_MS3;

		case "expression_analysis_TMT::p_value_MS3":
			return ColumnName.expression_analysis_TMT__p_value_MS3;

		case "expression_analysis_TMT::log2(R)_MS3_n":
			return ColumnName.expression_analysis_TMT__log2_R__MS3_n;

		case "expression_analysis_TMT::neglog10(p)_MS3":
			return ColumnName.expression_analysis_TMT__neglog10_p__MS3;

		case "uniprot_annotation::Entry":
			return ColumnName.uniprot_annotation__Entry;

		case "uniprot_annotation::Entry name":
			return ColumnName.uniprot_annotation__Entry_name;

		case "uniprot_annotation::Status":
			return ColumnName.uniprot_annotation__Status;

		case "uniprot_annotation::General annotation _CATALYTIC_ACTIVITY_":
			return ColumnName.uniprot_annotation__General_annotation__CATALYTIC_ACTIVITY_;

		case "uniprot_annotation::Locus":
			return ColumnName.uniprot_annotation__Locus;
		case "condition":
			return ColumnName.condition;
		case "Replicate name":
			return ColumnName.replicate_name;
		case "DMSO_SUM":
			return ColumnName.DMSO_sum;
		default:
			System.out.println(columnName);
			return null;
		}
	}

	public int getColumnIndex(ColumnName columnName) {
		return getColumnIndex(columnName.name());
	}

	public int getColumnIndex(String columnName) {
		// log.debug("Getting column index for " + columnName);
		Integer integer = columnNameColumnIndexHash.get(columnName);
		if (integer != null)
			return integer;
		try {
			throw new IllegalArgumentException("Column name is not recognized: '" + columnName + "'");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			return 0;
		}
	}

	public int getSheetIndex(ColumnName columnName) {
		return getSheetIndex(columnName.name());
	}

	public int getSheetIndex(String columnName) {
		// log.debug("Getting column index for " + columnName.name());
		Integer integer = columnNameSheetNumberHash.get(columnName);
		if (integer != null)
			return integer;
		try {
			throw new IllegalArgumentException("Column name is not recognized: '" + columnName + "'");
		} catch (Exception e) {

			return 0;
		}
	}

}
