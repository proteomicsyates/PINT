package edu.scripps.yates.proteindb.queries.semantic.command;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.NumericalCondition;
import edu.scripps.yates.proteindb.queries.Query;
import edu.scripps.yates.proteindb.queries.dataproviders.ProteinProviderFromDB;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.AbstractQuery;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.util.CommandReference;
import edu.scripps.yates.proteindb.queries.semantic.util.MyCommandTokenizer;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.UniprotLineHeader;
import edu.scripps.yates.utilities.strings.StringUtils;

/**
 * Implements a {@link Query} from Annotation command:<br>
 * CAN[Uniprot version, Uniprot header line, Annotation type, Annotation name,
 * Annotation value, Numerical condition]
 *
 * @author Salva
 *
 */
public class QueryFromComplexAnnotationCommand extends AbstractQuery {
	private UniprotLineHeader uniprotLineHeader;
	private final Set<edu.scripps.yates.utilities.proteomicsmodel.AnnotationType> annotationTypes = new HashSet<edu.scripps.yates.utilities.proteomicsmodel.AnnotationType>();
	private String annotationNameString;
	private String annotationValueString;
	private NumericalCondition numericalCondition;
	private String uniprotVersion;
	private static Logger log = Logger.getLogger(QueryFromComplexAnnotationCommand.class);

	public QueryFromComplexAnnotationCommand(CommandReference commandReference) throws MalformedQueryException {

		super(commandReference);
		final String[] split = MyCommandTokenizer.splitCommand(commandReference.getCommandValue());
		if (split.length == 6) {
			uniprotVersion = split[0].trim();
			String ulhString = split[1].trim();
			String annotationTypeString = split[2].trim();
			annotationNameString = split[3].trim();
			annotationValueString = split[4].trim();

			String numericalConditionString = split[5].trim();

			uniprotLineHeader = null;
			if (!"".equals(ulhString)) {
				uniprotLineHeader = UniprotLineHeader.translateStringToUniprotLineHeader(ulhString);
				if (uniprotLineHeader == null)
					throw new MalformedQueryException("Uniprot header line is not recognized: '" + ulhString + "'");
				// just in case that annotationType is empty
				if ("".equals(annotationTypeString))
					annotationTypes.addAll(edu.scripps.yates.utilities.proteomicsmodel.AnnotationType
							.getAnnotationTypesByUniprotLineHeader(uniprotLineHeader));
			}
			edu.scripps.yates.utilities.proteomicsmodel.AnnotationType annotationType = null;
			if (!"".equals(annotationTypeString)) {
				annotationType = edu.scripps.yates.utilities.proteomicsmodel.AnnotationType
						.translateStringToAnnotationType(annotationTypeString);
				if (annotationType == null)
					throw new MalformedQueryException(
							"Annotation type is not recognized: '" + annotationTypeString + "'");
				annotationTypes.add(annotationType);
			}
			if (uniprotLineHeader != null && annotationType != null)
				if (!annotationType.getUniprotLineHeaders().contains(uniprotLineHeader))
					throw new MalformedQueryException("Uniprot header line '" + ulhString + "' and annotation type '"
							+ annotationTypeString + "' are not compatible in a COMPLEX_ANNOTATION command");

			// or uniprotlineheader or annotationtype should be not null
			if (!(uniprotLineHeader != null || annotationType != null))
				throw new MalformedQueryException(
						"Or Uniprot header line or annotation type must be defined in the COMPLEX_ANNOTATION command '"
								+ commandReference + "'");

			if (!"".equals(numericalConditionString)) {
				numericalCondition = new NumericalCondition(numericalConditionString);
			}

			return;
		}

		throw new MalformedQueryException("Command value '" + commandReference + "' is not well formed for '"
				+ commandReference.getCommand().name() + "' command. Use this format: "
				+ commandReference.getCommand().getFormat());
	}

	public boolean evaluate(QueriableProteinSet protein) {
		// annotateProtein(protein, uniprotVersion);

		final Set<ProteinAnnotation> proteinAnnotations = protein.getProteinAnnotations();
		return evaluate(proteinAnnotations);
	}

	@Override
	public boolean evaluate(LinkBetweenQueriableProteinSetAndPSM link) {

		// annotate the proteins according to the uniprot version provided
		final QueriableProteinSet protein = link.getQueriableProtein();
		return evaluate(protein);

	}

	// static void annotateProtein(QueriableProteinInterface protein, String
	// uniprotVersion) {
	// Map<String, Set<QueriableProteinInterface>> map = new HashMap<String,
	// Set<QueriableProteinInterface>>();
	// final String primaryAcc = protein.getPrimaryAccession().getAccession();
	// if (map.containsKey(primaryAcc)) {
	// map.get(primaryAcc).add(protein);
	// } else {
	// Set<QueriableProteinInterface> set = new
	// HashSet<QueriableProteinInterface>();
	// set.add(protein);
	// map.put(primaryAcc, set);
	// }
	// annotateProteins(map, uniprotVersion);
	// }

	// static void annotateProteins(Map<String, Set<QueriableProteinInterface>>
	// proteinList, String uniprotVersion) {
	//
	// ProteinAnnotator.getInstance(uniprotVersion).annotateProteins2(proteinList);
	//
	// }

	@Override
	public AggregationLevel getAggregationLevel() {
		return AggregationLevel.PROTEIN;
	}

	/**
	 * @return the uniprotVersion
	 */
	public String getUniprotVersion() {
		return uniprotVersion;
	}

	@Override
	public ProteinProviderFromDB initProtenProvider() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean requiresFurtherEvaluation() {
		return true;
	}

	public boolean evaluate(Set<ProteinAnnotation> proteinAnnotations) {
		if (proteinAnnotations != null) {
			int numMatchedAnnotations = 0;
			// to be included in the result, at least one annotation
			// will have to be present
			for (edu.scripps.yates.utilities.proteomicsmodel.AnnotationType annotationType : annotationTypes) {
				List<ProteinAnnotation> proteinAnnotations2 = PersistenceUtils.getProteinAnnotations(proteinAnnotations,
						annotationType);
				for (ProteinAnnotation proteinAnnotation : proteinAnnotations2) {

					// NAME==null
					if ("".equals(annotationNameString)) {

						// NAME==null VALUE!=null
						if (!"".equals(annotationValueString)) {

							if (StringUtils.compareStrings(proteinAnnotation.getName(), annotationValueString, true,
									true, false)
									|| StringUtils.compareStrings(proteinAnnotation.getValue(), annotationValueString,
											true, true, false)) {
								numMatchedAnnotations++;
								// no restriction on the number
								// of times that that annotation
								// must be present
								if (numericalCondition == null) {
									return true;
								}
							}

						} else {
							// NAME==null VALUE===null
							numMatchedAnnotations++;
							if (numericalCondition == null) {
								return true;
							}
						}

					} else {
						// NAME!=null
						if (StringUtils.compareStrings(proteinAnnotation.getName(), annotationNameString, true, true,
								false)) {

							// NAME!=null VALUE!=null
							if (!"".equals(annotationValueString)) {
								if (StringUtils.compareStrings(proteinAnnotation.getValue(), annotationValueString,
										true, true, false)) {
									// no restriction on the number
									// of times that that annotation
									// must be present
									numMatchedAnnotations++;
									if (numericalCondition == null) {
										return true;
									}
								}
							} else {
								// NAME!=null VALUE==null
								numMatchedAnnotations++;
								if (numericalCondition == null) {

									return true;
								}
							}
						}
					}

				}
				if (numericalCondition != null && numericalCondition.matches(numMatchedAnnotations)) {
					return true;
				}

			}
		}
		return false;
	}

}
