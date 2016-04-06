package edu.scripps.yates.excel.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.model.util.PTMAdapter;
import edu.scripps.yates.model.util.PTMSiteAdapter;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.factories.PTMEx;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.util.StringPosition;

public class ExcelUtils {
	private final static Logger log = Logger.getLogger(ExcelUtils.class);
	private final static String BRAKETS_REGEXP = "([^\\[]+)\\[(\\w+?)\\](\\w+?)";
	private final static String PARENTHESIS_REGEXP = ".*?([^\\(\\)]+)\\((\\S+?)\\)([^\\)\\(]+).*?";
	private final static String POINTS_REGEXP = "\\w+?\\.(\\S+?)\\.\\w+?";
	private static final HashMap<MSRun, Map<String, Peptide>> peptidesByMSRun = new HashMap<MSRun, Map<String, Peptide>>();
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
		HashMap<Double, PTM> map = new HashMap<Double, PTM>();
		if (FastaParser.somethingExtrangeInSequence(seq)) {
			List<StringPosition> massDiffsStrings = FastaParser.getInside(seq);
			if (!massDiffsStrings.isEmpty()) {
				for (StringPosition stringMassDiffNumber : massDiffsStrings) {
					// get the PTM
					String aa = "";
					if (stringMassDiffNumber.position > -1)
						aa = String
								.valueOf(appendList(FastaParser.getOutside(seq)).charAt(stringMassDiffNumber.position));
					try {
						double massDiff = Double.valueOf(stringMassDiffNumber.string);
						if (!map.containsKey(massDiff)) {

							PTM ptm = new PTMAdapter(massDiff, aa, stringMassDiffNumber.position + 1).adapt();
							map.put(massDiff, ptm);
						} else {
							// add a ptm site
							final PTMEx ptm = (PTMEx) map.get(massDiff);
							ptm.addPtmSite(new PTMSiteAdapter(aa, stringMassDiffNumber.position + 1).adapt());
						}
					} catch (NumberFormatException e) {
						log.info(stringMassDiffNumber + " cannot be parsed as a number. " + e.getMessage());
					}
				}
			}
		}
		List<PTM> ret = new ArrayList<PTM>();
		for (PTM ptm : map.values()) {
			ret.add(ptm);
		}
		Collections.sort(ret, new Comparator<PTM>() {

			@Override
			public int compare(PTM ptm1, PTM ptm2) {
				int pos1 = getMinPTMPosition(ptm1);
				int pos2 = getMinPTMPosition(ptm2);
				return Integer.compare(pos1, pos2);
			}

			private int getMinPTMPosition(PTM ptm1) {
				int min = Integer.MAX_VALUE;
				for (PTMSite site : ptm1.getPTMSites()) {
					if (min > site.getPosition())
						min = site.getPosition();
				}
				return min;
			}
		});
		return ret;
	}

	private static String appendList(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
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
	// peptideSet = new HashMap<String, Peptide>();
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
}
