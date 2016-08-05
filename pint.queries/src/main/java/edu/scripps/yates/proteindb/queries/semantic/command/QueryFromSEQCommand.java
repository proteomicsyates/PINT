package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.dataproviders.psm.PsmProviderFromSEQ;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenProteinAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinInterface;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.strings.StringUtils;
import edu.scripps.yates.utilities.util.Pair;

/**
 * Implements a {@link Query} from SEQ command:<br>
 * SEQ [Aggregation_level, regular_expression]
 *
 * @author Salva
 *
 */
public class QueryFromSEQCommand extends AbstractQuery {

	private Pattern pattern;
	private String regularExpression;
	private PsmProviderFromSEQ psmProviderFromSEQ;
	private AggregationLevel aggregationLevel;
	private final UniprotProteinRetriever uplr;
	private static Logger log = Logger.getLogger(QueryFromSEQCommand.class);

	public QueryFromSEQCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		uplr = new UniprotProteinRetriever(null,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());

		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 2) {
			String aggregationLevelString = split[0].trim();
			if (!"".equals(aggregationLevelString)) {

				aggregationLevel = AggregationLevel.getAggregationLevelByString(aggregationLevelString);
				if (aggregationLevel == null) {
					throw new MalformedQueryException("Aggregation level not recognized as '" + aggregationLevelString
							+ "'. It should be one of these values: " + AggregationLevel.getValuesString() + "'");
				}
				if (aggregationLevel == AggregationLevel.PROTEINGROUP) {
					throw new MalformedQueryException("Aggregation level " + aggregationLevel + " not supported");
				}

			} else {
				throw new MalformedQueryException("Aggregation level MUST not be empty on "
						+ commandReference.getCommand().getAbbreviation() + " command");
			}
			regularExpression = split[1].trim().toUpperCase();
			try {
				pattern = translateUserRegularExpressionToJavaPattern(regularExpression);

				if ("".equals(regularExpression)) {
					throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for "
							+ commandReference.getCommand().name()
							+ " command. At least one argument for a regular expression should be provided");

				}

			} catch (PatternSyntaxException e) {
				log.error(e);
				throw new MalformedQueryException("Regular expression '" + regularExpression
						+ "' is not a valid regular expression: " + e.getMessage());
			}
			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	/**
	 * Change:<br>
	 * X* by \\w*<br>
	 * X+ by \\w+<br>
	 * X by \\w<br>
	 *
	 *
	 * @param regularExpression
	 * @return
	 */
	static Pattern translateUserRegularExpressionToJavaPattern(String regularExpression) {
		String patternString = "";
		String tmp = regularExpression;
		// remove spaces and "-"
		tmp = tmp.replace(" ", "").replace("-", "");

		// replace X by /w
		tmp = tmp.replace("X*", "\\w*");
		tmp = tmp.replace("X+", "\\w+");
		tmp = tmp.replace("X", "\\w");

		if (tmp.contains("|")) {
			final List<Integer> pipePositions = StringUtils.allPositionsOf(tmp, "|");

			for (int i = 1; i <= tmp.length(); i++) {
				if (pipePositions.contains(i)) {
					// "|"
					if (i > 1) {
						// remove previous char
						String previousChar = patternString.substring(patternString.length() - 1);
						patternString = patternString.substring(0, patternString.length() - 1);
						// add [
						patternString += "[" + previousChar + "|";

					}
				} else {
					// not "|", so add it
					patternString += tmp.charAt(i - 1);
					// if the previous was a pipe, add ]
					if (pipePositions.contains(i - 1)) {
						patternString += "]";
					}
				}
			}
		} else {
			patternString = tmp;
		}
		log.info(regularExpression + " converted to " + patternString);
		return Pattern.compile(patternString);
	}

	@Override
	public boolean evaluate(LinkBetweenProteinAndPSM link) {
		if (aggregationLevel == AggregationLevel.PSM || aggregationLevel == AggregationLevel.PEPTIDE) {
			return evaluate(link.getQueriablePsm());
		} else if (aggregationLevel == AggregationLevel.PROTEIN) {
			return evaluate(link.getQueriableProtein());
		}
		throw new IllegalArgumentException("aggregation level " + aggregationLevel + " is not supported");
	}

	private boolean evaluate(QueriablePsm psm) {
		final String seq = psm.getSequence();

		return evaluateSequence(seq);

	}

	private boolean evaluate(QueriableProteinInterface protein) {
		final String seq = getProteinSequence(protein);

		return evaluateSequence(seq);

	}

	private String getProteinSequence(QueriableProteinInterface protein) {
		final String acc = protein.getPrimaryAccession();
		Set<String> uniprotACCs = new HashSet<String>();
		final Pair<String, String> accPair = FastaParser.getACC(acc);
		if (accPair != null && accPair.getSecondElement() == AccessionType.UNIPROT.name()) {
			uniprotACCs.add(accPair.getFirstelement());
		} else if (accPair != null && accPair.getSecondElement() == AccessionType.IPI.name()) {
			final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance()
					.map2Uniprot(accPair.getFirstelement());
			if (map2Uniprot != null) {
				for (UniprotEntry uniprotEntry : map2Uniprot) {
					uniprotACCs.add(uniprotEntry.getAcc());
				}
			}
		}
		if (!uniprotACCs.isEmpty()) {
			final Map<String, Protein> annotatedProtein = uplr.getAnnotatedProteins(uniprotACCs);
			final String uniprotACC = uniprotACCs.iterator().next();
			if (annotatedProtein != null && annotatedProtein.containsKey(uniprotACC)) {
				final Protein protein2 = annotatedProtein.get(uniprotACC);
				if (protein2 != null) {
					String sequence = protein2.getSequence();
					if (sequence != null) {
						sequence.replace("\n", "");
					}
					return sequence;
				}
			}
		}
		return null;
	}

	private boolean evaluateSequence(String seq) {
		if (seq == null) {
			return false;
		}
		final Matcher matcher = pattern.matcher(seq);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return aggregationLevel;
	}

	@Override
	public ProteinProviderFromDB initProtenProvider() {
		// only for PSM or PEPTIDE aggregation levels
		if (aggregationLevel == AggregationLevel.PEPTIDE || aggregationLevel == AggregationLevel.PSM) {
			try {
				psmProviderFromSEQ = new PsmProviderFromSEQ(pattern.toString());
			} catch (IllegalArgumentException e) {

			}
		}
		return psmProviderFromSEQ;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		if (psmProviderFromSEQ == null) {
			return true;
		} else {
			if (containsAmbiguities()) {
				return true;
			}
			return true;
		}
	}

	/**
	 * returns true if the regular expression contains 'X' or OR clausses, i.e.
	 * [P|Y]
	 *
	 * @return
	 */
	private boolean containsAmbiguities() {
		if (regularExpression.contains("X")) {
			return true;
		}
		if (regularExpression.contains("|")) {
			return true;
		}
		if (regularExpression.contains("[")) {
			return true;
		}
		if (regularExpression.contains("]")) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) {
		String text = "rXXy|p";
		String[] toFound = { "art", "arp", "ragyy" };
		final Pattern pattern = translateUserRegularExpressionToJavaPattern(text);
		System.out.println(pattern);
		for (String string : toFound) {
			final Matcher matcher = pattern.matcher(string);
			System.out.println(string + ":\t" + matcher.find());

		}
	}
}
