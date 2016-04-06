package edu.scripps.yates.dbindex.io;

import java.io.UnsupportedEncodingException;
// import java.util.ArrayList;
// import java.util.List;
import java.util.regex.Pattern;

public class Fasta implements Comparable<Fasta> {

	// description line of this Fasta sequence
	protected static String defline;
	// the sequence string of this Fasta
	protected byte[] sequence;
	private String seq;
	private String accession = null;
	private String sequestLikeAccession = null;
	private double mPlusH = 0;
	private static final Pattern pattern = Pattern.compile("(.*)\\d");

	public Fasta(String defline, String sequence) {
		this.defline = defline;
		// System.out.println("defline: " + this.defline);
		sequence = sequence.toUpperCase();
		// seq = sequence;
		try {
			this.sequence = sequence.getBytes("US-ASCII");
		} catch (UnsupportedEncodingException e) {
			System.err.println("Unknow charset");
			System.exit(1);
		}
		// System.out.println("hahaha, defline: " + this.defline);
	}

	@Override
	public String toString() {
		return "Fasta{defline=" + defline + '}';
	}

	public void setMPlusH(double mh) {
		mPlusH = mh;
	}

	public double getMPlusH() {
		return mPlusH;
	}

	// public double setMPlusH() {
	// return mPlusH;
	// }
	// to order the sequence from long to short
	public int compareTo(Fasta f) {
		if (f == null)
			return -1;
		return f.getLength() - getLength();
	}

	public static String getAccessionWithNoVersion(String ac) {
		int index = ac.indexOf(".");
		if (index == -1) {
			return ac;
		} else {
			return ac.substring(0, index);
		}
	}

	public String getAccessionWithNoVersion() {
		int index = accession.indexOf(".");
		if (index == -1) {
			return accession;
		} else {
			return getAccession().substring(0, index);
		}
	}

	public String getSequence() {
		if (seq == null) {
			seq = new String(sequence);
		}
		return seq;
		// return new String(sequence);
	}

	public byte[] getSequenceAsBytes() {
		return sequence;
	}

	public String getOriginalDefline() {

		// return defline.substring(1,defline.length());
		return defline;
	}

	public String getDefline() {

		return defline.substring(1, defline.length());
	}

	public byte byteAt(int index) {
		return sequence[index];
	}

	public int getLength() {
		return sequence.length;
	}

	// get accession without version
	public String getAccession() {
		if (accession == null) {
			// System.out.println("defline: " + defline);
			accession = getAccession(defline.substring(1));

		}

		return accession;
	}

	public String getSequestLikeAccession() {
		if (sequestLikeAccession == null) {
			sequestLikeAccession = getSequestLikeAccession(defline.substring(1));
		}
		return sequestLikeAccession;
	}

	public boolean isReversed() {
		return getAccession().startsWith("Re");
	}

	public static String getSequestLikeAccession(String acc) {
		String[] arr = acc.split("\t");
		String[] arr1 = arr[0].split(" ");
		String newacc = arr1[0];
		if (newacc != null && newacc.length() > 40) {
			newacc = newacc.substring(0, 40);
		}
		return newacc;

	}

	public static String getAccession(String accession) {
		// NCBI, IPI, or others such as UNIT_PROT, SGD, NCI
		// accession = getDefline().substring( getDefline().indexOf('>')+1 );
		// accession = getDefline();

		// There are many corruptted sqt file. Ignore it.
		try {
			if (accession.startsWith("gi") && accession.contains("|")) // NCBI
			{
				String[] arr = accession.split("\\|");

				if (arr.length >= 4
						&& ("gb".equals(arr[2]) || "ref".equals(arr[2])
								|| "emb".equals(arr[2]) || "dbj".equals(arr[2])
								|| "prf".equals(arr[2]) || "sp".equals(arr[2]))
						|| "tpd".equals(arr[2]) || "tpg".equals(arr[2])
						|| "tpe".equals(arr[2]))
					accession = arr[3];
				else {
					arr = accession.split(" ");
					accession = arr[1];
				}

				// Accession # should end with digit. If accession # does not
				// end with digit,
				// grap next string (We assume this next one ends with digit.)
				/*
				 * if( pattern.matcher(arr[3]).matches() ) accession = arr[3];
				 * else accession = arr[4].substring(0, arr[4].indexOf(" "));
				 */

			} else if (accession.startsWith("IPI")) // IPI
			{
				String arr[] = accession.split("\\|");
				String subArr[] = arr[0].split(":");

				if (subArr.length > 1)
					accession = subArr[1];
				else
					accession = subArr[0];
			} else if (accession.startsWith("Re")
					|| accession.startsWith("contam")
					|| accession.startsWith("Contam")) // Reverse database
			{
				int space = accession.indexOf(" ");
				int tab = accession.indexOf("\t");

				if (space < 0)
					space = 40;
				if (tab < 0)
					tab = 40;

				int index = (tab > space) ? space : tab;

				int end;

				if (index <= 0 || index >= 40) // no space
				{
					int length = accession.length();
					end = (length > 40) ? 40 : length;
				} else
					// cut by the first space
					end = index;

				accession = accession.substring(0, end);
			} else // UNIT_PROT, NCI or SGD

			{
				int spaceIndex = accession.indexOf(" ");
				int tabIndex;

				if (spaceIndex > 0) {
					tabIndex = accession.indexOf("\t");

					if (tabIndex > 0 && spaceIndex > tabIndex)
						accession = accession.substring(0, tabIndex);
					else
						accession = accession.substring(0, spaceIndex);
				}
			}
		} catch (Exception e) {
			// System.out.println("No Correct Accession found, but this will be handled by MSP system."
			// + accession + " " + e);

			int i = accession.indexOf(" ");
			if (i < 0)
				return accession;
			else
				return accession.substring(0, i);

		}

		return accession;
	}

	public static void main(String args[]) throws Exception {

		java.io.FileInputStream f = new java.io.FileInputStream(args[0]);

		Fasta fasta = null;

		try {
			for (java.util.Iterator<Fasta> itr = FastaReader.getFastas(f); itr
					.hasNext();) {
				fasta = itr.next();
				fasta.getAccession();
				System.out.println("===>>" + fasta.getAccession() + " "
						+ fasta.getDefline() + "<===");
			}
		} catch (Exception e) {
			System.out.println("===>>" + fasta.getDefline() + "<===");

		}

		// System.out.println( "==>" +
		// Fasta.getAccession("Reverse_IPI:IPI00\t Tax_Id=9606 Hypot") + "<==");
		// System.out.println("==>>" + accession + "<==");
	}
}
