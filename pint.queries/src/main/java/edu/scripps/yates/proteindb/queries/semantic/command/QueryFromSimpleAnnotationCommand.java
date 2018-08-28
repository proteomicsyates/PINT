package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.AnnotationType;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.queries.NumericalCondition;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.DataProviderFromDB;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.strings.StringUtils;
import gnu.trove.map.hash.THashMap;

/**
 * Implements a {@link Query} from Annotation command:<br>
 * AN [Uniprot version, Annotation string, Numerical condition]
 *
 * @author Salva
 *
 */
public class QueryFromSimpleAnnotationCommand extends AbstractQuery {

	private String annotationString;
	private NumericalCondition numericalCondition;
	private String uniprotVersion;
	private static Logger log = Logger.getLogger(QueryFromSimpleAnnotationCommand.class);

	public QueryFromSimpleAnnotationCommand(CommandReference commandReference) throws MalformedQueryException {
		super(commandReference);
		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 3) {
			uniprotVersion = split[0].trim();
			annotationString = split[1].trim();
			final String numericalConditionString = split[2].trim();

			if (annotationString == null || "".equals(annotationString)) {
				throw new MalformedQueryException("Annotation string cannot be null" + annotationString + "'");
			}

			if (!"".equals(numericalConditionString)) {
				numericalCondition = new NumericalCondition(numericalConditionString);
			}

			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	public boolean evaluate(Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> proteinAnnotations) {
		if (proteinAnnotations != null) {
			final boolean found = false;
			final Map<edu.scripps.yates.utilities.proteomicsmodel.AnnotationType, Integer> map = new THashMap<edu.scripps.yates.utilities.proteomicsmodel.AnnotationType, Integer>();
			// to be included in the result, at least one annotation
			// will have to be present
			for (final edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation : proteinAnnotations) {

				// NAME!=null
				if (StringUtils.compareStrings(proteinAnnotation.getName(), annotationString, true, true, false)
						|| StringUtils.compareStrings(proteinAnnotation.getValue(), annotationString, true, true,
								false)) {
					if (numericalCondition == null) {
						log.debug(proteinAnnotation.getAnnotationType() + ":" + proteinAnnotation.getName() + " -- "
								+ proteinAnnotation.getValue());
						return true;
					} else {
						if (map.containsKey(proteinAnnotation.getAnnotationType())) {
							final Integer num = map.get(proteinAnnotation.getAnnotationType());
							map.put(proteinAnnotation.getAnnotationType(), num + 1);
						} else {
							map.put(proteinAnnotation.getAnnotationType(), 1);
						}
					}
				}
			}
			if (!found && numericalCondition != null) {
				final Set<edu.scripps.yates.utilities.proteomicsmodel.AnnotationType> keySet = map.keySet();
				for (final edu.scripps.yates.utilities.proteomicsmodel.AnnotationType annotationType : keySet) {
					if (numericalCondition.matches(map.get(annotationType))) {

						return true;
					}
				}

			}
		}

		return false;
	}

	public boolean evaluateHibernateAnnotations(Set<ProteinAnnotation> proteinAnnotations) {
		if (proteinAnnotations != null) {
			final boolean found = false;
			final Map<AnnotationType, Integer> map = new THashMap<AnnotationType, Integer>();
			// to be included in the result, at least one annotation
			// will have to be present
			for (final ProteinAnnotation proteinAnnotation : proteinAnnotations) {

				// NAME!=null
				if (StringUtils.compareStrings(proteinAnnotation.getName(), annotationString, true, true, false)
						|| StringUtils.compareStrings(proteinAnnotation.getValue(), annotationString, true, true,
								false)) {
					if (numericalCondition == null) {
						log.debug(proteinAnnotation.getAnnotationType() + ":" + proteinAnnotation.getName() + " -- "
								+ proteinAnnotation.getValue());
						return true;
					} else {
						if (map.containsKey(proteinAnnotation.getAnnotationType())) {
							final Integer num = map.get(proteinAnnotation.getAnnotationType());
							map.put(proteinAnnotation.getAnnotationType(), num + 1);
						} else {
							map.put(proteinAnnotation.getAnnotationType(), 1);
						}
					}
				}
			}
			if (!found && numericalCondition != null) {
				final Set<AnnotationType> keySet = map.keySet();
				for (final AnnotationType annotationType : keySet) {
					if (numericalCondition.matches(map.get(annotationType))) {

						return true;
					}
				}

			}
		}

		return false;
	}

	public boolean evaluate(QueriableProteinSet protein) {
		final Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> proteinAnnotations = protein
				.getProteinAnnotations();
		return evaluate(proteinAnnotations);
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {

		// annotate the proteins according to the uniprot version provided
		final QueriableProteinSet protein = link.getQueriableProtein();
		// QueryFromComplexAnnotationCommand.annotateProtein(protein,
		// uniprotVersion);
		return evaluate(protein);

	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPeptideSet link) {

		// annotate the proteins according to the uniprot version provided
		final QueriableProteinSet protein = link.getQueriableProtein();
		// QueryFromComplexAnnotationCommand.annotateProtein(protein,
		// uniprotVersion);
		return evaluate(protein);

	}

	@Override
	public AggregationLevel getAggregationLevel() {
		return AggregationLevel.PROTEIN;
	}

	public String getUniprotVersion() {
		return uniprotVersion;
	}

	@Override
	public DataProviderFromDB initProtenProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		return true;
	}
}
