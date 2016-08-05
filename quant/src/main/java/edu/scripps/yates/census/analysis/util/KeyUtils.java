package edu.scripps.yates.census.analysis.util;

import java.util.Collections;
import java.util.Comparator;

import edu.scripps.yates.census.quant.xml.ProteinType;
import edu.scripps.yates.census.quant.xml.ProteinType.Peptide;
import edu.scripps.yates.census.read.model.IsoRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.dbindex.IndexedProtein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.ProteinGroup;

public class KeyUtils {
	public static String getSequenceChargeKey(QuantifiedPSMInterface psm, boolean chargeStateSensible) {
		final String seq = FastaParser.cleanSequence(psm.getFullSequence());
		if (chargeStateSensible) {
			return seq + "-" + psm.getCharge();
		} else {
			return seq;
		}
	}

	// public static String getProteinKey(DTASelectProtein protein) {
	// final String locus = protein.getLocus();
	// return FastaParser.getACC(locus).getFirstelement();
	// }

	// public static String getProteinKey(String locus) {
	// return FastaParser.getACC(locus).getFirstelement();
	// }
	public static String getProteinKey(ProteinType protein) {
		return FastaParser.getACC(protein.getLocus()).getFirstelement();
	}

	public static String getProteinKey(IndexedProtein indexedProtein) {
		final String fastaDefLine = indexedProtein.getFastaDefLine();
		return FastaParser.getACC(fastaDefLine).getFirstelement();
	}

	/**
	 *
	 * @param psm
	 * @param chargeSensible
	 *            if true, then, the charge will be considered for
	 *            differentiating peptides with different charge states. If
	 *            false, peptides with different charge states will have the
	 *            same key
	 * @return
	 */
	public static String getSpectrumKey(QuantifiedPSMInterface psm, boolean chargeSensible) {

		StringBuilder sb = new StringBuilder();
		if (psm.getRawFileName() != null)
			sb.append(psm.getRawFileName());
		if (!"".equals(sb.toString())) {
			sb.append("-");
		}
		if (psm.getScan() != null)
			sb.append(psm.getScan());
		if (chargeSensible) {
			sb.append("." + psm.getCharge());
		}
		return sb.toString();
	}

	/**
	 *
	 * @param psm
	 * @param chargeSensible
	 *            if true, then, the charge will be considered for
	 *            differentiating peptides with different charge states. If
	 *            false, peptides with different charge states will have the
	 *            same key
	 * @return
	 */
	public static String getSpectrumKey(Peptide peptide, boolean chargeSensible) {

		final String string = peptide.getFile() + "-" + peptide.getScan();
		if (chargeSensible) {
			return string + "." + peptide.getCharge();
		} else {
			return string;
		}
	}

	// /**
	// * returns ionSerieTypeName+numIon+spectrumKey
	// *
	// * @param ratio
	// * @param peptide
	// * @param chargeSensible
	// * if true, then, the charge will be considered for
	// * differentiating peptides with different charge states. If
	// * false, peptides with different charge states will have the
	// * same key
	// * @return
	// */
	// public static String getIonKey(IsoRatio ratio, QuantifiedPSMInterface
	// quantPSM, boolean chargeSensible) {
	// String ionSerieTypeName = "";
	// if (ratio.getIonSerieType() != null) {
	// ionSerieTypeName = ratio.getIonSerieType().name();
	// }
	// String numIon = "";
	// if (ratio.getNumIon() > 0)
	// numIon = String.valueOf(ratio.getNumIon());
	// String ret = ionSerieTypeName + numIon;
	// if (!"".equals(ret)) {
	// ret += "-";
	// }
	// ret += getSpectrumKey(quantPSM, chargeSensible);
	// return ret;
	//
	// }

	/**
	 * returns ionSerieTypeName+numIon+spectrumKey
	 *
	 * @param ratio
	 * @param peptide
	 * @param chargeSensible
	 *            if true, then, the charge will be considered for
	 *            differentiating peptides with different charge states. If
	 *            false, peptides with different charge states will have the
	 *            same key
	 * @return
	 */
	public static String getIonKey(IsoRatio ratio, Peptide peptide, boolean chargeSensible) {

		return ratio.getIonSerieType().name() + ratio.getNumIon() + "-" + getSpectrumKey(peptide, chargeSensible);

	}

	public static String getGroupKey(ProteinGroup group) {
		Collections.sort(group, new Comparator<GroupableProtein>() {

			@Override
			public int compare(GroupableProtein o1, GroupableProtein o2) {
				return o1.getAccession().compareTo(o2.getAccession());
			}
		});
		String key = "";
		for (GroupableProtein protein : group) {
			if (!"".equals(key))
				key += ",";
			key += protein.getAccession();
		}
		return key;
	}
}
