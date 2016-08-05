package edu.scripps.yates.utilities.fasta;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.taxonomy.UniprotOrganism;
import edu.scripps.yates.utilities.taxonomy.UniprotSpeciesCodeMap;
import edu.scripps.yates.utilities.util.Pair;
import edu.scripps.yates.utilities.util.StringPosition;

public class FastaParser {
	private static Set<String> notRecognizedFastas = new HashSet<String>();

	public static enum UNIPROT_FASTA_KEYWORD {
		OS, GN, PE, SV
	};

	// taken from http://www.uniprot.org/help/accession_numbers
	public static final Pattern UNIPROT_ACC = Pattern
			.compile("[\\W^]([OPQ][0-9][A-Z0-9]{3}[0-9](\\-[0-9]{1,2})?|[A-NR-Z][0-9]([A-Z][A-Z0-9]{2}[0-9]){1,2})");

	public static final Pattern UNIPROT_ACC_TYPE1 = Pattern
			.compile("([OPQ][0-9][A-Z0-9][A-Z0-9][A-Z0-9][0-9](\\-[0-9]{1,2})?)");
	public static final Pattern UNIPROT_ACC_TYPE2 = Pattern.compile("([A-NR-Z][0-9][A-Z][A-Z0-9][A-Z0-9][0-9])");
	public static final Pattern UNIPROT_ACC_TYPE3 = Pattern
			.compile("([A-NR-Z][0-9][A-Z][A-Z0-9][A-Z0-9][0-9][A-Z][A-Z0-9][A-Z0-9][0-9])");

	public static final Pattern ONLY_UNIPROT_ACC = Pattern
			.compile("([OPQ][0-9][A-Z0-9]{3}[0-9](\\-[0-9]{1,2})?|[A-NR-Z][0-9]([A-Z][A-Z0-9]{2}[0-9]){1,2})");
	public static final Pattern NCBI_ACC = Pattern.compile(".*(gi\\|[^|]+)\\|.*");

	private static Pattern IPI_ACC = Pattern.compile(".*(IPI[0-9]{8}\\.*[0-9]*).*");

	public static final Pattern OS_FASTA_HEADER = Pattern.compile(".*OS=(.*)(GN)?PE=.*");
	public static final Pattern BRACKETS_FASTA_HEADER = Pattern.compile(".*\\[(.*)\\].*");
	public static final Pattern GENE_FASTA_HEADER = Pattern.compile(".*GN=(.*)PE=");
	public static final Pattern GENE_SYMBOL_FASTA_HEADER = Pattern.compile(".*Gene_Symbol=(\\S+).*");
	public static final Pattern PE_FASTA_HEADER = Pattern.compile(".*PE=(\\d+).*");
	public static final Pattern SV_FASTA_HEADER = Pattern.compile(".*SV=(\\d+).*");
	public static final Pattern TAXID_FASTA_HEADER = Pattern.compile(".*Tax_Id=(\\d+).*");
	/**
	 * regular expressions to capture data that will be removed since these
	 * annotations can be located in the middle or at the beginning of the fasta
	 * header
	 */
	public static final String[] FASTA_ANNOTATIONS = { "Tax_Id=\\d+", "Gene_Symbol=\\S+" };
	public static Pattern[] FASTA_ANNOTATION_PATTERNS;
	public static final String CONTAMINANT_PREFIX = "contaminant";

	private static Logger log = Logger.getLogger(FastaParser.class);
	private static final Pattern UNIPROT_SP_ACC_TMP = Pattern.compile(".*s?\\|(\\S+)\\|\\S*\\s.*");
	private static final Pattern UNIPROT_TR_ACC_TMP = Pattern.compile(".*tr\\|(\\S+)\\|\\S*\\s.*");
	private static final Pattern UNIPROT_SP_ACC_TMP2 = Pattern.compile(".*sp\\|\\S+\\|\\S*\\s(.*)");
	private static final Pattern UNIPROT_TR_ACC_TMP2 = Pattern.compile(".*tr\\|\\S+\\|\\S*\\s(.*)");
	private static final Pattern dashAndNumber = Pattern.compile("^(-[0-9]).*");
	private static final Pattern startingByWord = Pattern.compile("^\\w+");
	private static final Pattern endingByWord = Pattern.compile("\\w+$");
	private static final Pattern untilSpace = Pattern.compile("^(\\S+)");
	static {
		FASTA_ANNOTATION_PATTERNS = new Pattern[FASTA_ANNOTATIONS.length];
		int index = 0;
		for (String fasta_annotation : FASTA_ANNOTATIONS) {
			FASTA_ANNOTATION_PATTERNS[index] = Pattern.compile(fasta_annotation);
			index++;
		}
	}

	/**
	 * This function parses the uniprot accession, getting the resulting string
	 * after applying this regular expression:
	 * ".*([OPQ][0-9][A-Z0-9]{3}[0-9]|[A-NR-Z][0-9]([A-Z][A-Z0-9]{2}[0-9]){1,2}).*"
	 * If the regular expresion doens't fit, then the original id is returned.
	 *
	 * @param id
	 * @return
	 */
	public static String getUniProtACC(String id) {
		if (id != null && !"".equals(id)) {
			if (id.length() > 600) {
				return null;
			}
			final Matcher matcherSP = UNIPROT_SP_ACC_TMP.matcher(id);
			final Matcher matcherTR = UNIPROT_TR_ACC_TMP.matcher(id);
			if (matcherSP.find()) {
				final String group = matcherSP.group(1);
				if (!"".equals(group))
					id = group.trim();
			} else if (matcherTR.find()) {
				final String group = matcherTR.group(1);
				if (!"".equals(group))
					id = group.trim();
			}
			Matcher matcher5 = null;
			String trim = null;
			final Matcher matcher2 = UNIPROT_ACC_TYPE1.matcher(id);
			if (matcher2.find()) {
				trim = matcher2.group(1).trim();
			}
			final Matcher matcher3 = UNIPROT_ACC_TYPE2.matcher(id);
			if (matcher3.find()) {
				trim = matcher3.group(1).trim();
			}
			final Matcher matcher4 = UNIPROT_ACC_TYPE3.matcher(id);
			if (matcher4.find()) {
				trim = matcher4.group(1).trim();
			}
			if (trim != null) {

				// get the string after the trim in the id
				String tmp = id.substring(id.indexOf(trim) + trim.length());
				if (tmp.length() > 0) {
					final Matcher matcher6 = dashAndNumber.matcher(tmp);
					if (matcher6.find()) {
						return trim + matcher6.group(1);
					}
					// check that there is no more words before the id

					if (startingByWord.matcher(tmp).find()) {
						return null;
					}

				}
				tmp = id.substring(0, id.indexOf(trim));
				if (tmp.length() > 0) {

					if (endingByWord.matcher(tmp).find()) {
						return null;
					}
				}
				return trim;
			}
		}
		return null;
	}

	public static boolean isUniProtACC(String id) {
		if (id != null && !"".equals(id)) {
			final Matcher matcher = ONLY_UNIPROT_ACC.matcher(id);
			if (matcher.find()) {
				return true;
			}
		}
		return false;
	}

	public static String getNCBIACC(String id) {
		if (id != null && !"".equals(id)) {
			if (id.length() > 600) {
				return null;
			}
			final Matcher matcher = NCBI_ACC.matcher(id);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		}
		return null;
	}

	public static String getIPIACC(String id) {
		if (id != null && !"".equals(id)) {
			if (id.length() > 600) {
				return null;
			}
			final Matcher matcher = IPI_ACC.matcher(id);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		}
		return null;
	}

	/**
	 * Gets a pair where the first element is the parsed protein id and the
	 * second is the accession type
	 *
	 * @param id
	 * @return
	 */
	public static Pair<String, String> getACC(String id) {
		// if starts by >, remove it
		if (id.startsWith(">"))
			id = id.substring(1);
		// if starts by Reverse, take the word as the id
		String reverse = "Reverse";
		if (id.length() >= reverse.length() && id.substring(0, reverse.length()).equalsIgnoreCase(reverse)) {

			final Matcher matcher = untilSpace.matcher(id);
			if (matcher.find()) {
				return new Pair<String, String>(matcher.group(0).trim(), "UNKNOWN");
			} else {
				return new Pair<String, String>(id.trim(), "UNKNOWN");
			}
		}

		final String uniProtACC = getUniProtACC(id);
		if (uniProtACC != null) {
			return new Pair<String, String>(uniProtACC, "UNIPROT");
		}
		final String ncbiacc = getNCBIACC(id);
		if (ncbiacc != null) {
			return new Pair<String, String>(ncbiacc, "NCBI");
		}

		final String ipiacc = getIPIACC(id);
		if (ipiacc != null) {
			return new Pair<String, String>(ipiacc, "IPI");
		}

		return new Pair<String, String>(id.trim(), "UNKNOWN");
	}

	/**
	 * Gets the OS=taxonomy name from the fasta header. Note that it is
	 * necessary to be before the GN= annotation. It also takes the taxonomy if
	 * it is between brackets if it is not found before.<br>
	 * if it is not found yet, it uses the fullaccession provided to get the
	 * Uniprot code if available and use the {@link UniprotSpeciesCodeMap} class
	 * to get the species name.
	 *
	 * @param fastaheader
	 * @return
	 */
	public static String getOrganismNameFromFastaHeader(String fastaheader, String fullaccession) {
		if (notRecognizedFastas.contains(fastaheader) && !notRecognizedFastas.contains(fullaccession)) {
			return null;
		}
		if (fastaheader.length() > 600 && fullaccession.length() > 600) {
			return null;
		}
		// new way 4 Feb 2015
		final String[] split = fastaheader.split(" ");
		for (int i = 0; i < split.length; i++) {
			final String string = split[i];
			if (startsWithUniprotKeyword(string)) {
				if (string.startsWith(UNIPROT_FASTA_KEYWORD.OS.name())) {
					String tmp = getStringAfterEqual(string);
					String ret = "";
					// if (tmp != null) {
					// ret = tmp;
					// }
					if (tmp == null) {
						continue;
					}
					ret = tmp;
					for (int j = i + 1; j < split.length; j++) {
						// get everything until another keyword
						if (!startsWithUniprotKeyword(split[j])) {
							ret += " " + split[j];
						} else {
							break;
						}
					}
					return ret.trim();
				}
			}
		}

		if (fastaheader != null && !"".equals(fastaheader)) {
			final Matcher matcher = OS_FASTA_HEADER.matcher(fastaheader);
			if (matcher.find()) {
				final String osString = matcher.group(1).trim();
				final String taxonomyFromString = getTaxonomyFromString(osString);
				if (taxonomyFromString != null)
					return taxonomyFromString.trim();
			}

			final Matcher matcher3 = TAXID_FASTA_HEADER.matcher(fastaheader);
			if (matcher3.find()) {
				final String taxID = matcher3.group(1).trim();
				final String taxonomyFromString = getTaxonomyFromString(taxID);
				if (taxonomyFromString != null)
					return taxonomyFromString.trim();
			}
			final Matcher matcher2 = BRACKETS_FASTA_HEADER.matcher(fastaheader);
			if (matcher2.find()) {
				final String insideBrackets = matcher2.group(1).trim();
				final String taxonomyFromString = getTaxonomyFromString(insideBrackets);
				if (taxonomyFromString != null)
					return taxonomyFromString.trim();
			}
			if (fullaccession != null) {
				final Matcher matcher4 = BRACKETS_FASTA_HEADER.matcher(fullaccession);
				if (matcher4.find()) {
					final String insideBrackets = matcher4.group(1).trim();
					final String taxonomyFromString = getTaxonomyFromString(insideBrackets);
					if (taxonomyFromString != null)
						return taxonomyFromString.trim();
				}
				// try to get from the code of the uniprot like _DROVI

				String code = getUniprotTaxonomyCode(fullaccession);
				if (code != null) {
					final UniprotOrganism organism = UniprotSpeciesCodeMap.getInstance().get(code);
					if (organism != null) {
						return organism.getScientificName().trim();
					}
				}

				code = getUniprotTaxonomyCode(fastaheader);
				if (code != null) {
					final UniprotOrganism organism = UniprotSpeciesCodeMap.getInstance().get(code);
					if (organism != null) {
						return organism.getScientificName().trim();
					}
				}
			}
		}
		if (!notRecognizedFastas.contains(fastaheader)) {
			notRecognizedFastas.add(fastaheader);
		}
		if (!notRecognizedFastas.contains(fullaccession)) {
			notRecognizedFastas.add(fullaccession);
		}

		return null;
	}

	/**
	 * Gets the string just after an "="
	 *
	 * @param string
	 * @return
	 */
	private static String getStringAfterEqual(String string) {
		if (string.contains("=")) {
			return string.substring(string.indexOf("=") + 1).trim();
		}
		return null;
	}

	/**
	 * True if the string starts with any UNIPROT KEYWORD
	 *
	 * @param string
	 * @return
	 */
	private static boolean startsWithUniprotKeyword(String string) {
		for (UNIPROT_FASTA_KEYWORD keyw : UNIPROT_FASTA_KEYWORD.values()) {
			if (string.startsWith(keyw.name())) {
				return true;
			}
		}
		return false;
	}

	private static String getTaxonomyFromString(String taxonomyString) {
		final UniprotOrganism uniprotOrganism = UniprotSpeciesCodeMap.getInstance().get(taxonomyString);
		if (uniprotOrganism != null) {
			return uniprotOrganism.getScientificName();
		}
		return null;
	}

	/**
	 * If the uniprot accession is like
	 * >Reverse_gi|75022131|sp|Q9GRW7.1|NONA_DROVI, it gets "_DROVI"
	 *
	 * @param fastaheader
	 * @return
	 */
	private static String getUniprotTaxonomyCode(String string) {
		if (string.lastIndexOf("_") > -1) {
			String trim = string.substring(string.lastIndexOf("_") + 1).trim();
			if (trim.contains(" "))
				trim = trim.substring(0, trim.indexOf(" "));
			return trim;
		}
		return null;
	}

	/**
	 * Gets the GN=gene name from the fasta header. Note that it is necessary to
	 * be before the annotation PE
	 *
	 * @param fastaheader
	 * @return
	 */
	public static String getGeneFromFastaHeader(String fastaheader) {
		if (fastaheader != null && !"".equals(fastaheader)) {
			final Matcher matcher = GENE_FASTA_HEADER.matcher(fastaheader);
			if (matcher.find()) {
				return getFirstWordIfMany(matcher.group(1).trim());
			}
			final Matcher matcher2 = GENE_SYMBOL_FASTA_HEADER.matcher(fastaheader);
			if (matcher2.find()) {
				return getFirstWordIfMany(matcher2.group(1).trim());
			}
		}
		return null;
	}

	/**
	 * If the string contains separators like "," or ";", take just the first
	 * word
	 *
	 * @param trim
	 * @return
	 */
	private static String getFirstWordIfMany(String string) {
		if (string.contains(","))
			return string.split(",")[0];
		if (string.contains(";"))
			return string.split(";")[0];
		return string;
	}

	/**
	 * Gets the protein existence (PE=xx) annotation from the fasta header
	 *
	 * @param fastaheader
	 * @return
	 */
	public static String getProteinExistenceFromFastaHeader(String fastaheader) {
		if (fastaheader != null && !"".equals(fastaheader)) {
			final Matcher matcher = PE_FASTA_HEADER.matcher(fastaheader);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		}
		return null;

	}

	/**
	 * Gets the sequence version (SV=xx) annotation from the fasta header
	 *
	 * @param fastaheader
	 * @return
	 */
	public static String getSequenceVersionFromFastaHeader(String fastaheader) {
		if (fastaheader != null && !"".equals(fastaheader)) {
			final Matcher matcher = SV_FASTA_HEADER.matcher(fastaheader);
			if (matcher.find()) {
				return matcher.group(1).trim();
			}
		}
		return null;

	}

	/**
	 * This method parse the description string removing annotations from the
	 * header like:<br>
	 * "Tax_id=1234" or "OS=Drosophila virilis" or "GN=Dvir\GJ22546"...<br>
	 * It also take out the sp|ASDFF|ASDF if exists.
	 *
	 * @param description
	 * @return
	 */
	public static String getDescription(String description) {
		if (description == null)
			return null;
		// log.debug("Trying to get description from '" + description + "'");
		final String ncbiacc = getNCBIACC(description);
		if (ncbiacc != null && !description.equals(ncbiacc)) {
			if (description.indexOf(" ") >= 0)
				description = description.substring(description.indexOf(" ")).trim();
		}
		// take out the sp|ASDF|ASDF or tr|ASDF|ASDF

		final Matcher matcherSP = UNIPROT_SP_ACC_TMP2.matcher(description);
		final Matcher matcherTR = UNIPROT_TR_ACC_TMP2.matcher(description);
		if (matcherSP.find()) {
			final String group = matcherSP.group(1);
			if (!"".equals(group))
				description = group.trim();
		} else if (matcherTR.find()) {
			final String group = matcherTR.group(1);
			if (!"".equals(group))
				description = group.trim();
		}

		for (Pattern regex : FASTA_ANNOTATION_PATTERNS) {
			Matcher regexMatcher = regex.matcher(description);
			description = regexMatcher.replaceFirst("").trim();
		}

		// remove all data from the first annotation
		int firstAnnotationIndex = Integer.MAX_VALUE;

		// Annotations that are always after the description
		String[] annotations = { "OS=", "GN=", "PE=", "SV=" };

		for (String annotation : annotations) {
			final int os = description.indexOf(annotation);
			if (os != -1 && os < firstAnnotationIndex) {
				firstAnnotationIndex = os;
			}
		}

		if (firstAnnotationIndex != Integer.MAX_VALUE && firstAnnotationIndex > -1) {
			final String ret = description.substring(0, firstAnnotationIndex).trim();
			if (!"".equals(ret))
				return ret;
		}

		// if there is something between brackets and it matches with a organism
		// name, remove it
		try {
			// log.info(description);

			final Matcher matcher2 = BRACKETS_FASTA_HEADER.matcher(description);
			if (matcher2.find()) {

				final String match = matcher2.group(1);
				final String taxonomy = getTaxonomyFromString(match);
				if (taxonomy != null) {
					final String quote = Pattern.quote("[" + match + "]");
					description = description.replaceFirst(quote, "").trim();
				}
			}
		} catch (PatternSyntaxException e) {
			e.printStackTrace();
		}
		return description.trim();
	}

	/**
	 * This function allow to get the peptide sequence as <br>
	 * <ul>
	 * <li>K.VDLSFSPSQSLPASHAHLR.V -> VDLSFSPSQSLPASHAHLR</li>
	 * <li>R.LLLQQVSLPELPGEYSMK.V + Oxidation (M) -> LLLQQVSLPELPGEYSMK</li>
	 * <li>(-)TVAAPSVFIFPPSDEQLK(S) -> TVAAPSVFIFPPSDEQLK</li>
	 * <li>K.EKS[167.00]KESAIASTEVK.L -> EKSKESAIASTEVK</li>
	 * </ul>
	 * getting just the sequence without modifications and between the pre and
	 * post AA if available
	 *
	 * @param seq
	 * @return
	 */
	public static String cleanSequence(String seq) {
		if (seq == null)
			return null;
		String seqTmp = seq.trim();
		try {
			if (somethingExtrangeInSequence(seqTmp)) {

				// parenthesis or brackets
				List<String> outside = getOutside(seqTmp);
				if (!outside.isEmpty()) {
					String tmp = appendList(outside);
					return removeBeforeAfterAAs(tmp);
				}

			}
			return seq.toUpperCase();
		} catch (Exception e) {
			return seqTmp;
		}
	}

	public static List<String> getOutside(String seq) {
		int numOpen = 0;
		List<String> ret = new ArrayList<String>();
		StringBuffer outside = new StringBuffer();
		for (int i = 0; i < seq.length(); i++) {
			final char charAt = seq.charAt(i);
			if (charAt == '[' || charAt == '(') {
				if (!"".equals(outside.toString())) {
					ret.add(outside.toString());
				}
				numOpen++;
			} else if (charAt == ')' || charAt == ']') {
				numOpen--;
				outside = new StringBuffer();
			} else {
				if (numOpen == 0) {
					outside.append(charAt);
				}
			}
		}
		if (!"".equals(outside.toString())) {
			ret.add(outside.toString());
		}
		return ret;
	}

	/**
	 * Gets a list of {@link StringPosition} objects with the information inside
	 * of parenthesis or braquets. The information is the text and the position
	 * in the text, not counting the text inside the parentheis or braquets by
	 * itself.<br>
	 * The position is based on 0, that is, starting from 0 in the first
	 * character.
	 *
	 * @param seq
	 * @return
	 */
	public static List<StringPosition> getInside(String seq) {
		int numOpen = 0;
		List<StringPosition> ret = new ArrayList<StringPosition>();
		StringBuffer inside = new StringBuffer();
		int lastNormal = 0;
		int lenthInsides = 0;
		for (int i = 0; i < seq.length(); i++) {
			final char charAt = seq.charAt(i);
			if (charAt == '[' || charAt == '(') {
				lastNormal = i - 1 - lenthInsides;
				inside = new StringBuffer();
				numOpen++;
				lenthInsides++;
			} else if (charAt == ']' || charAt == ')') {
				numOpen--;
				if (!"".equals(inside.toString())) {
					ret.add(new StringPosition(inside.toString(), lastNormal));
				}
				lenthInsides++;
			} else {
				if (numOpen > 0) {
					inside.append(charAt);
					lenthInsides++;
				}
			}
		}
		return ret;
	}

	public static boolean somethingExtrangeInSequence(String seq) {
		for (int i = 0; i < seq.length(); i++) {
			final char charAt = seq.charAt(i);
			if (charAt == '[' || charAt == ']')
				return true;
			if (charAt == '(' || charAt == ')')
				return true;
			if (charAt == '.')
				return true;
		}
		return false;
	}

	private static String appendList(List<String> list) {
		StringBuffer sb = new StringBuffer();
		for (String string : list) {
			sb.append(string);
		}
		return sb.toString();
	}

	/**
	 * // R.LLLQQVSLPELPGEYSMK.V --> LLLQQVSLPELPGEYSMK
	 *
	 * @param seq
	 * @return
	 */
	private static String removeBeforeAfterAAs(String seq) {
		final String point = ".";
		if (seq.contains(point)) {
			final int firstPoint = seq.indexOf(point);
			final int lastPoint = seq.lastIndexOf(point);

			if (firstPoint != lastPoint) {
				return seq.substring(firstPoint + 1, lastPoint);
			}
		}
		return seq;
	}

	/**
	 * // R.LLLQQVSLPELPGEYSMK.V --> R
	 *
	 * @param seq
	 * @return
	 */
	public static String getBeforeSeq(String seq) {
		final String point = ".";
		if (seq.contains(point)) {
			final int firstPoint = seq.indexOf(point);
			final int lastPoint = seq.lastIndexOf(point);

			if (firstPoint != lastPoint) {
				final String substring = seq.substring(0, firstPoint);
				return substring;
			}
		}
		return null;
	}

	/**
	 * // R.LLLQQVSLPELPGEYSMK.V --> V
	 *
	 * @param seq
	 * @return
	 */
	public static String getAfterSeq(String seq) {
		final String point = ".";
		if (seq.contains(point)) {
			final int firstPoint = seq.indexOf(point);
			final int lastPoint = seq.lastIndexOf(point);

			if (firstPoint != lastPoint) {
				final String substring = seq.substring(lastPoint + 1, seq.length());
				return substring;
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(FastaParser.getACC("sp|P54399-2|PDI_DROME"));
		System.out
				.println(FastaParser.getACC("asdfaksjdfhasjkdf_ASDFasdf_ASDfsp|Pasdf22532|SPR2D_sdfsdfHUMAN asdfasdf"));

		System.out.println(FastaParser.getNCBIACC(">gi|194150008|gb|EDW65699.1| GJ18760 [Drosophila virilis"));
		System.out.println(FastaParser.getDescription(">gi|194150008|gb|EDW65699.1| GJ18760 [Drosophila virilis]"));

		System.out.println(FastaParser.getACC(">gi|194150008|gb|EDW65699.1| GJ18760 [Drosophila virilis]"));
		System.out.println(FastaParser.getDescription(
				">sp|P22532|SPR2D_HUMAN Small proline-rich protein 2D OS=Homo sapiens GN=SPRR2D PE=2 SV=2"));
		System.out.println(FastaParser
				.getACC(">sp|P22532|SPR2D_HUMAN Small proline-rich protein 2D OS=Homo sapiens GN=SPRR2D PE=2 SV=2"));
		System.out.println(FastaParser.getACC("IPI:IPI00930860.1|REFSEQ:NP_001155007"));

		System.out.println(FastaParser.getACC("IPI:IPI00930860|REFSEQ:NP_001155007"));

		System.out.println(FastaParser
				.getOrganismNameFromFastaHeader("CG7415 (Fragment) OS=Drosophila simulans PE=2 SV=1 ", "CG7415"));

		System.out.println(FastaParser
				.getOrganismNameFromFastaHeader("CG7415 (Fragment) OS=Drosophila simulans PE=2 SV=1 ", "CG7415"));

		System.out.println(FastaParser.getOrganismNameFromFastaHeader(
				">tr|A3SA23|A3SA23_9RHOB TonB dependent, hydroxamate-type ferrisiderophore, outer membrane receptor OS=Sulfitobacter sp. EE-36 GN=EE36_08023 PE=3 SV=1",
				"asdf"));

		System.out.println(FastaParser
				.getACC("A6NKZ8\tA6NKZ8;  RecName: Full=Putative tubulin beta chain-like protein ENSP00000290377;"));
	}

	public static boolean isContaminant(String accession) {
		if (accession.startsWith(CONTAMINANT_PREFIX)) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the scan number from a string like 'rawfile.scan.scan.charge'
	 *
	 * @param psmId
	 * @return
	 */
	public static String getScanFromPSMIdentifier(String psmId) {
		if (psmId == null) {
			return null;
		}
		final int lastIndexOf = psmId.lastIndexOf(".");
		String scan = psmId.substring(0, lastIndexOf);
		final int lastIndexOf2 = scan.lastIndexOf(".");
		scan = scan.substring(lastIndexOf2 + 1);
		return scan;
	}

	/**
	 * Gets the file name from a string like 'rawfile.scan.scan.charge'
	 *
	 * @param psmId
	 * @return
	 */
	public static String getFileNameFromPSMIdentifier(String psmId) {
		if (psmId == null) {
			return null;
		}
		if (psmId.contains(".")) {
			final int indexOf = psmId.indexOf(".");
			String fileName = psmId.substring(0, indexOf);
			return fileName;
		} else {
			if (psmId.contains("-")) {
				int index = psmId.lastIndexOf("-");
				return psmId.substring(0, index);
			}
		}
		return null;
	}

	public static Integer getChargeStateFromPSMIdentifier(String psmId) {
		if (psmId == null) {
			return null;
		}
		if (psmId.contains(".")) {
			final int indexOf = psmId.lastIndexOf(".");
			String chargeStateString = psmId.substring(indexOf + 1);
			try {
				return Integer.valueOf(chargeStateString);
			} catch (NumberFormatException e) {

			}
		}
		return null;
	}
}
