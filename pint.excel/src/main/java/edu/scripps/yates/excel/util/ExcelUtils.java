package edu.scripps.yates.excel.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.ExcelSheet;
import edu.scripps.yates.excel.impl.ExcelFileImpl;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMPosition;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.adapters.PTMAdapter;
import edu.scripps.yates.utilities.proteomicsmodel.adapters.PTMSiteAdapter;
import edu.scripps.yates.utilities.proteomicsmodel.factories.PTMEx;
import edu.scripps.yates.utilities.util.StringPosition;
import gnu.trove.map.hash.TDoubleObjectHashMap;
import gnu.trove.map.hash.THashMap;

public class ExcelUtils {
	private final static Logger log = Logger.getLogger(ExcelUtils.class);
	private final static String BRAKETS_REGEXP = "([^\\[]+)\\[(\\w+?)\\](\\w+?)";
	private final static String PARENTHESIS_REGEXP = ".*?([^\\(\\)]+)\\((\\S+?)\\)([^\\)\\(]+).*?";
	private final static String POINTS_REGEXP = "\\w+?\\.(\\S+?)\\.\\w+?";
	private static final Map<MSRun, Map<String, Peptide>> peptidesByMSRun = new THashMap<MSRun, Map<String, Peptide>>();
	// IF YOU CHANGE THIS, YOU WILL HAVE TO CHANGE IT ALSO IN PINT
	// SharedConstants.java
	public static final String MULTIPLE_ITEM_SEPARATOR = ",";

	// private static boolean somethingExtrangeInSequence(String seq) {
	// return seq.matches(".*\\[.*\\].*") || seq.matches(".*\\.(.*)\\..*")
	// || seq.matches(".*\\).*\\(.*");
	//
	// }
	// private static boolean somethingExtrangeInSequence(String seq) {
	// return seq.matches(BRAKETS_REGEXP) || seq.matches(PARENTHESIS_REGEXP)
	// || seq.matches(POINTS_REGEXP);
	// }

	public static List<PTM> getPTMsFromRawSequence(String seq) {
		final TDoubleObjectHashMap<PTM> map = new TDoubleObjectHashMap<PTM>();
		if (FastaParser.somethingExtrangeInSequence(seq)) {
			final String cleanSequence = FastaParser.cleanSequence(seq);
			final List<StringPosition> massDiffsStrings = FastaParser.getInside(seq);
			if (!massDiffsStrings.isEmpty()) {
				for (final StringPosition stringMassDiffNumber : massDiffsStrings) {
					// get the PTM
					String aa = "";
					if (stringMassDiffNumber.position > 0)
						aa = String.valueOf(
								appendList(FastaParser.getOutside(seq)).charAt(stringMassDiffNumber.position - 1));
					try {
						final double massDiff = Double.valueOf(stringMassDiffNumber.string);
						if (!map.containsKey(massDiff)) {

							final PTM ptm = new PTMAdapter(massDiff, aa, stringMassDiffNumber.position, PTMPosition
									.getPTMPositionFromSequence(cleanSequence, stringMassDiffNumber.position)).adapt();
							map.put(massDiff, ptm);
						} else {
							// add a ptm site
							final PTMEx ptm = (PTMEx) map.get(massDiff);
							ptm.addPtmSite(new PTMSiteAdapter(aa, stringMassDiffNumber.position, PTMPosition
									.getPTMPositionFromSequence(cleanSequence, stringMassDiffNumber.position)).adapt());
						}
					} catch (final NumberFormatException e) {
						log.info(stringMassDiffNumber + " cannot be parsed as a number. " + e.getMessage());
					}
				}
			}
		}
		final List<PTM> ret = new ArrayList<PTM>();
		for (final PTM ptm : map.valueCollection()) {
			ret.add(ptm);
		}
		Collections.sort(ret, new Comparator<PTM>() {

			@Override
			public int compare(PTM ptm1, PTM ptm2) {
				final int pos1 = getMinPTMPosition(ptm1);
				final int pos2 = getMinPTMPosition(ptm2);
				return Integer.compare(pos1, pos2);
			}

			private int getMinPTMPosition(PTM ptm1) {
				int min = Integer.MAX_VALUE;
				for (final PTMSite site : ptm1.getPTMSites()) {
					if (min > site.getPosition())
						min = site.getPosition();
				}
				return min;
			}
		});
		return ret;
	}

	private static String appendList(List<String> list) {
		final StringBuffer sb = new StringBuffer();
		for (final String string : list) {
			sb.append(string);
		}
		return sb.toString();
	}

	// public static void createPeptides(Protein protein) {
	// MSRun msRun = protein.getMSRun();
	// Set<PSM> psms = protein.getPSMs();
	// if (psms != null) {
	// Map<String, Set<PSM>> psmMapBySequence = ModelUtils
	// .getPSMMapBySequence(psms);
	// Map<String, Peptide> peptideSet = null;
	// // Create peptides grouped by the MSRun
	// if (peptidesByMSRun.containsKey(msRun)) {
	// peptideSet = peptidesByMSRun.get(msRun);
	// } else {
	// peptideSet = new THashMap<String, Peptide>();
	// peptidesByMSRun.put(msRun, peptideSet);
	// }
	// for (String sequence : psmMapBySequence.keySet()) {
	// final Set<PSM> psmsWithThatSequence = psmMapBySequence
	// .get(sequence);
	// Peptide peptide = null;
	// if (peptideSet.containsKey(sequence)) {
	// peptide = peptideSet.get(sequence);
	// } else {
	// // create the peptide
	// peptide = new PeptideEx(sequence, msRun);
	// peptideSet.put(sequence, peptide);
	// }
	// // psm-peptide relation
	// for (PSM psmWithThatSequence : psmsWithThatSequence) {
	// peptide.addPSM(psmWithThatSequence);
	// psmWithThatSequence.setPeptide(peptide);
	// }
	// // protein-peptide relation
	// protein.addPeptide(peptide);
	// peptide.addProtein(protein);
	//
	// }
	// }
	//
	// }
	public static List<String> getLines(File excelFile, String separator) throws IOException {
		final List<String> ret = new ArrayList<String>();
		final ExcelFileImpl reader = new ExcelFileImpl(excelFile);
		final ExcelSheet excelSheet = reader.getSheets().get(0);
		final List<String> columnHeaders = excelSheet.getColumnHeaders();
		StringBuilder sb = new StringBuilder();
		for (final String header : columnHeaders) {
			sb.append(header).append(separator);
		}
		ret.add(sb.toString());

		final int maxRow = excelSheet.getColumns().get(0).getLastNonEmptyRow();
		for (int row = 0; row < maxRow; row++) {
			sb = new StringBuilder();
			for (final ExcelColumn column : excelSheet.getColumns()) {
				final List<Object> values = column.getValues();
				final Object object = values.get(row);
				sb.append(object.toString()).append(separator);
			}
			ret.add(sb.toString());
		}
		return ret;
	}
}
