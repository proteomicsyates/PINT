package edu.scripps.yates.annotations.uniprot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.compomics.util.protein.AASequenceImpl;

import edu.scripps.yates.utilities.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.EvidenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.EvidencedStringType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.GeneNameType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.IsoformType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.IsoformType.Name;
import edu.scripps.yates.utilities.annotations.uniprot.xml.KeywordType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.LocationType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.OrganismNameType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.OrganismType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.PropertyType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinExistenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinType.AlternativeName;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinType.RecommendedName;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinType.SubmittedName;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ReferenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.SourceDataType.Plasmid;
import edu.scripps.yates.utilities.annotations.uniprot.xml.SourceDataType.Strain;
import edu.scripps.yates.utilities.annotations.uniprot.xml.SourceDataType.Transposon;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.AbstractProtein;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.factories.AccessionEx;
import edu.scripps.yates.utilities.proteomicsmodel.factories.GeneEx;
import edu.scripps.yates.utilities.proteomicsmodel.factories.OrganismEx;
import edu.scripps.yates.utilities.proteomicsmodel.factories.ProteinAnnotationEx;

public class ProteinImplFromUniprotEntry extends AbstractProtein {
	private static Logger log = Logger.getLogger(ProteinImplFromUniprotEntry.class);
	// IF YOU CHANGE THIS ANN_SEPARATOR, CHANGE IT ALSO IN ProteinBean in PINT
	// shared code
	public static final String ANNOTATION_SEPARATOR = "###";
	private final static String GO = "GO";
	public static final String ID = "id";
	public static final String STATUS = "status";
	public static final String REF = "ref";
	public static final String BEGIN = "begin";
	public static final String END = "end";
	public static final String POSITION = "position";
	public static final String ORIGINAL = "original";
	private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	private final Entry entry;
	private boolean organismParsed;
	private boolean lengthParsed;
	private boolean mwParsed;
	private boolean genesParsed;
	private boolean annotationsParsed;

	public ProteinImplFromUniprotEntry(Entry proteinEntry) {
		entry = proteinEntry;
		parsePrimaryAndSecondaryAccessionsFromEntry(proteinEntry);
		setKey(getAccession());
	}

	private boolean isIsoform(String accession) {

		final String version = FastaParser.getIsoformVersion(accession);
		if (version != null) {
			return true;
		}

		return false;
	}

	@Override
	public Set<Gene> getGenes() {
		if (!genesParsed) {
			final List<GeneType> genesUniprot = entry.getGene();
			if (genesUniprot != null) {
				for (final GeneType gene : genesUniprot) {
					for (final GeneNameType geneName : gene.getName()) {
						final GeneEx geneEx = new GeneEx(geneName.getValue());
						geneEx.setGeneType(geneName.getType());
						addGene(geneEx);
					}
				}
			}
			genesParsed = true;
		}
		return super.getGenes();
	}

	private List<String> getDescriptions() {
		final ProteinType protein = entry.getProtein();
		final List<String> ret = new ArrayList<String>();
		if (entry.getProtein() != null) {
			final RecommendedName recommendedName = protein.getRecommendedName();
			if (recommendedName != null) {
				final StringBuilder sb = new StringBuilder();
				if (recommendedName.getFullName() != null && recommendedName.getFullName().getValue() != null) {
					sb.append(recommendedName.getFullName().getValue().trim());
				}
				final List<EvidencedStringType> shortNames = recommendedName.getShortName();
				if (shortNames != null && shortNames.isEmpty()) {
					for (int i = 0; i < shortNames.size(); i++) {
						final EvidencedStringType shortName = shortNames.get(i);
						sb.append(" (" + shortName.getValue().trim() + ")");
					}
				}
				final List<EvidencedStringType> ecNumbers = recommendedName.getEcNumber();
				if (ecNumbers != null && ecNumbers.isEmpty()) {
					for (final EvidencedStringType ecNumber : ecNumbers) {
						sb.append(" (" + ecNumber.getValue().trim() + ")");
					}
				}
				ret.add(sb.toString());
			}
			final List<AlternativeName> alternativeNames = protein.getAlternativeName();
			if (alternativeNames != null && !alternativeNames.isEmpty()) {
				for (final AlternativeName alternativeName2 : alternativeNames) {
					final StringBuilder sb = new StringBuilder();
					sb.append(alternativeName2.getFullName().getValue().trim());
					final List<EvidencedStringType> shortNames = alternativeName2.getShortName();
					if (shortNames != null && shortNames.isEmpty()) {
						for (int i = 0; i < shortNames.size(); i++) {
							final EvidencedStringType shortName = shortNames.get(i);
							sb.append(" (" + shortName.getValue().trim() + ")");
						}
					}
					final List<EvidencedStringType> ecNumbers = alternativeName2.getEcNumber();
					if (ecNumbers != null && ecNumbers.isEmpty()) {
						for (final EvidencedStringType ecNumber : ecNumbers) {
							sb.append(" (" + ecNumber.getValue().trim() + ")");
						}
					}
					ret.add(sb.toString());
				}
			}

			final List<SubmittedName> submittedNames = protein.getSubmittedName();
			if (submittedNames != null && !submittedNames.isEmpty()) {
				for (final SubmittedName submittedName2 : submittedNames) {
					final StringBuilder sb = new StringBuilder();
					sb.append(submittedName2.getFullName().getValue().trim());
					final List<EvidencedStringType> ecNumbers = submittedName2.getEcNumber();
					if (ecNumbers != null && ecNumbers.isEmpty()) {
						for (final EvidencedStringType ecNumber : ecNumbers) {
							sb.append(" (" + ecNumber.getValue().trim() + ")");
						}
					}
					ret.add(sb.toString());
				}

			}
		}
		return ret;

	}

	@Override
	public Set<ProteinAnnotation> getAnnotations() {
		if (getKey().equals("P16884")) {
			log.info(" asdf");
		}
		if (!annotationsParsed) {
			// comments (CC):
			final List<CommentType> comments = entry.getComment();
			if (comments != null) {
				for (final CommentType comment : comments) {
					final AnnotationType annotationType = AnnotationType
							.translateStringToAnnotationType(comment.getType());
					if (annotationType != null && "tissue specificity".equals(annotationType.getKey())
							&& comment.getText() != null) {
						// in this case, the text can contains different
						// phrases,
						// each of them can start by "isoform 4 is expressed..."
						// in that case, only include the ones for the
						// appropiate
						// isoform
						for (final EvidencedStringType text : comment.getText()) {
							if (text.getValue() != null) {
								final List<String> phrases = new ArrayList<String>();
								if (text.getValue().contains(".")) {
									final String[] split = text.getValue().split("\\.");
									final List<String> asList = Arrays.asList(split);
									phrases.addAll(asList);
								} else {
									phrases.add(text.getValue());
								}
								final Pattern isoformPattern = Pattern.compile("isoform (\\d+)");
								final StringBuilder sb = new StringBuilder();
								for (final String phrase : phrases) {
									final Matcher matcher = isoformPattern.matcher(phrase.toLowerCase());
									if (matcher.find()) {
										// if found, only get if when the
										// isoform
										// number is correct
										if (isIsoform(getAccession())) {
											if (FastaParser.getIsoformVersion(getAccession())
													.equals(matcher.group(1))) {
												if (!"".equals(sb.toString())) {
													sb.append(" ");
												}
												sb.append(phrase.trim()).append(".");
											}
										}
									} else {
										// not found, only get it if we are in
										// the canonical entry
										if (!isIsoform(getAccession())) {
											if (!"".equals(sb.toString())) {
												sb.append(" ");
											}
											sb.append(phrase.trim()).append(".");
										}
									}
								}
								final ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(annotationType,
										comment.getType(), sb.toString());
								addProteinAnnotation(proteinAnnotationEx);
							}
						}
					} else {
						final Set<ProteinAnnotation> annotations = new ProteinAnnotationsFromCommentAdapter(comment)
								.adapt();
						for (final ProteinAnnotation proteinAnnotation : annotations) {
							addProteinAnnotation(proteinAnnotation);
						}

					}
				}
			}

			// features (FT):
			final List<FeatureType> features = entry.getFeature();
			if (features != null) {
				for (final FeatureType feature : features) {
					final AnnotationType annotationType = AnnotationType
							.translateStringToAnnotationType(feature.getType());
					if (annotationType == null || skipFeature(feature)) {
						continue;
					}

					// the the description as the annotation name if available.
					// Otherwise, take the type
					String annotName = feature.getDescription();
					if (annotName == null || "".equals(annotName)) {
						annotName = feature.getType();
						if (annotName == null || "".equals(annotName)) {
							continue;
						}
					}
					final ProteinAnnotation annotation = new ProteinAnnotationEx(annotationType, annotName,
							getValueFromFeature(feature));
					addProteinAnnotation(annotation);
				}
			}

			// keywords (KW)
			final List<KeywordType> keywords = entry.getKeyword();
			if (keywords != null) {
				for (final KeywordType keyword : keywords) {
					final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.uniprotKeyword,
							keyword.getValue(), getValueFromKeyword(keyword));
					addProteinAnnotation(annotation);
				}
			}

			// protein existence (PE)
			final ProteinExistenceType proteinExistence = entry.getProteinExistence();
			if (proteinExistence != null) {
				final AnnotationType annotationType = AnnotationType
						.translateStringToAnnotationType(proteinExistence.getType());
				if (annotationType != null) {
					final ProteinAnnotation annotation = new ProteinAnnotationEx(annotationType,
							proteinExistence.getType());
					addProteinAnnotation(annotation);
				}
			}

			// DB reference (DR)
			final List<DbReferenceType> dbReferences = entry.getDbReference();
			if (dbReferences != null) {
				for (final DbReferenceType dbReference : dbReferences) {

					// <dbReference type="GO" id="GO:0000166">
					// <property type="term" value="F:nucleotide binding"/>
					// <property type="evidence" value="IEA:InterPro"/>
					// </dbReference>
					// <dbReference type="PhosphoSite" id="P16884"/>
					// <dbReference type="UCSC" id="RGD:3159">
					// <property type="organism name" value="rat"/>
					// </dbReference>

					final StringBuilder sb = new StringBuilder();
					final List<PropertyType> properties = dbReference.getProperty();
					if (properties != null) {
						for (final PropertyType property : properties) {
							appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
							sb.append(property.getType() + ":" + property.getValue());
						}
					}
					if (dbReference.getEvidence() != null) {
						getValueFromEvidences(sb, dbReference.getEvidence());
					}
					final AnnotationType annotationType = AnnotationType
							.translateStringToAnnotationType(dbReference.getType());
					if (annotationType != null) {
						final ProteinAnnotation annotation = new ProteinAnnotationEx(annotationType,
								dbReference.getId(), sb.toString());
						addProteinAnnotation(annotation);
					}

				}
			}

			// Tissue (RC)
			// <reference key="5">
			// <citation type="submission" date="2007-07" db="UniProtKB">
			// <authorList>
			// <person name="Lubec G."/>
			// <person name="Pradeep J.J.P."/>
			// <person name="Afjehi-Sadat L."/>
			// <person name="Kang S.U."/>
			// </authorList>
			// </citation>
			// <scope>
			// PROTEIN SEQUENCE OF 39-52; 165-178; 229-256; 272-290; 349-368;
			// 389-399 AND 401-424
			// </scope>
			// <scope>IDENTIFICATION BY MASS SPECTROMETRY</scope>
			// <source>
			// <strain>Sprague-Dawley</strain>
			// <tissue>Brain</tissue>
			// <tissue>Spinal cord</tissue>
			// </source>
			// </reference>
			final List<ReferenceType> references = entry.getReference();
			if (references != null) {
				for (final ReferenceType reference : references) {
					if (reference.getSource() != null) {
						final List<Object> strainOrPlasmidOrTransposon = reference.getSource()
								.getStrainOrPlasmidOrTransposon();
						if (strainOrPlasmidOrTransposon != null) {
							for (final Object obj : strainOrPlasmidOrTransposon) {
								if (obj instanceof Strain) {
									final Strain strain = (Strain) obj;
									final StringBuilder sb = new StringBuilder();
									getValueFromEvidences(sb, strain.getEvidence());
									final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.strain,
											strain.getValue(), sb.toString());
									addProteinAnnotation(annotation);
								} else if (obj instanceof Plasmid) {
									final Plasmid plasmid = (Plasmid) obj;
									final StringBuilder sb = new StringBuilder();
									getValueFromEvidences(sb, plasmid.getEvidence());
									final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.plasmid,
											plasmid.getValue(), sb.toString());
									addProteinAnnotation(annotation);
								} else if (obj instanceof Transposon) {
									final Transposon transposon = (Transposon) obj;
									final StringBuilder sb = new StringBuilder();
									getValueFromEvidences(sb, transposon.getEvidence());
									final ProteinAnnotation annotation = new ProteinAnnotationEx(
											AnnotationType.transposon, transposon.getValue(), sb.toString());
									addProteinAnnotation(annotation);
								} else if (obj instanceof edu.scripps.yates.utilities.annotations.uniprot.xml.SourceDataType.Tissue) {
									final edu.scripps.yates.utilities.annotations.uniprot.xml.SourceDataType.Tissue tissue = (edu.scripps.yates.utilities.annotations.uniprot.xml.SourceDataType.Tissue) obj;
									final StringBuilder sb = new StringBuilder();
									getValueFromEvidences(sb, tissue.getEvidence());
									final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.tissue,
											tissue.getValue(), sb.toString());
									addProteinAnnotation(annotation);
								}
							}
						}
					}
				}
			}

			// reviewed or unreviewed
			final String dataset = entry.getDataset();
			if ("Swiss-Prot".equalsIgnoreCase(dataset)) {
				final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.status, "reviewed");
				addProteinAnnotation(annotation);
			} else if ("TrEMBL".equalsIgnoreCase(dataset)) {
				final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.status, "unreviewed");
				addProteinAnnotation(annotation);
			}

			// entry creation date
			if (entry.getCreated() != null) {
				final Calendar calendar = entry.getCreated().toGregorianCalendar();
				formatter.setTimeZone(calendar.getTimeZone());
				final String dateString = formatter.format(calendar.getTime());
				final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.entry_creation_date,
						dateString);
				addProteinAnnotation(annotation);
			}

			// entry modified
			if (entry.getModified() != null) {
				final Calendar calendar = entry.getModified().toGregorianCalendar();
				formatter.setTimeZone(calendar.getTimeZone());
				final String dateString = formatter.format(calendar.getTime());
				final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.entry_modified, dateString);
				addProteinAnnotation(annotation);
			}

			// entry version
			if (entry.getVersion() > 0) {
				final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.entry_version,
						String.valueOf(entry.getVersion()));
				addProteinAnnotation(annotation);
			}

			// sequence modified
			if (entry.getSequence() != null && entry.getSequence().getModified() != null) {
				final Calendar calendar = entry.getSequence().getModified().toGregorianCalendar();
				formatter.setTimeZone(calendar.getTimeZone());
				final String dateString = formatter.format(calendar.getTime());
				final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.sequence_modified,
						dateString);
				addProteinAnnotation(annotation);
			}

			// sequence version
			if (entry.getSequence() != null && entry.getSequence().getVersion() > -1) {
				final ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.sequence_modified,
						String.valueOf(entry.getSequence().getVersion()));
				addProteinAnnotation(annotation);
			}
			annotationsParsed = true;
		}
		return super.getAnnotations();
	}

	/**
	 * Decides if the feature is valid for this protein or not because sometimes the
	 * entry is coming from the canonical Uniprot protein and this is actually an
	 * isoform. So we want to skip all annotations regarding the isoforms.
	 *
	 * @param feature
	 * @return
	 */
	private boolean skipFeature(FeatureType feature) {
		// if it is an isoform, look for "splice variant" features, and
		// look to see a description like: "In isoform 7, isoform 8 and
		// isoform 9." Then, if is is not the correct isoform, skip the
		// annotation.
		final String description = feature.getDescription();
		if ("splice variant".equalsIgnoreCase(feature.getType())) {
			final String isoformVersion = FastaParser.getIsoformVersion(getPrimaryAccession().getAccession());
			if (description != null) {
				if (description.contains("isoform")) {
					final String[] split = description.split("isoform");

					for (int index = 1; index < split.length; index++) {
						final String string = split[index];
						try {
							final int isoformNumber = Integer.valueOf(String.valueOf(string.trim().charAt(0)));
							// isoformVersion can be null iof it is the
							// canonical protein
							if (String.valueOf(isoformNumber).equals(isoformVersion)) {
								return false;
							}
						} catch (final NumberFormatException e) {

						}
					}
					// skip it because it was not found
					return true;
				}
			}
		}
		return false;
	}

	private String getValueFromKeyword(KeywordType keyword) {
		final StringBuilder sb = new StringBuilder();
		sb.append("id:" + keyword.getId());
		getValueFromEvidences(sb, keyword.getEvidence());

		return sb.toString();
	}

	private String getValueFromFeature(FeatureType feature) {
		final StringBuilder sb = new StringBuilder();
		if (feature.getId() != null)
			sb.append(ID + ":" + feature.getDescription());

		if (feature.getStatus() != null) {
			appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
			sb.append(STATUS + ":" + feature.getStatus());
		}

		if (feature.getEvidence() != null) {
			getValueFromEvidences(sb, feature.getEvidence());
		}

		if (feature.getRef() != null) {
			appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
			sb.append(REF + ":" + feature.getRef());
		}

		final LocationType location = feature.getLocation();
		if (location != null) {
			if (location.getBegin() != null) {
				appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
				sb.append(BEGIN + ":" + location.getBegin().getPosition());
			}
			if (location.getEnd() != null) {
				appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
				sb.append(END + ":" + location.getEnd().getPosition());
			}
			if (location.getPosition() != null) {
				appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
				sb.append(POSITION + ":" + location.getPosition().getPosition());
			}
		}

		final String original = feature.getOriginal();
		if (original != null) {
			if (!"".equals(sb.toString()))
				sb.append(ANNOTATION_SEPARATOR);
			sb.append(ORIGINAL + ":" + original);
		}

		return sb.toString().trim();
	}

	private void getValueFromEvidences(StringBuilder sb, List<Integer> evidences) {
		if (evidences != null) {
			for (final Integer code : evidences) {
				if (code != null) {
					appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
					final EvidenceType evidenceType = getEvidenceType(code, entry.getEvidence());
					if (evidenceType != null) {
						sb.append(" Evidence from:" + getStringFromEvidenceType(evidenceType));
					}
				}
			}
		}
	}

	private String getStringFromEvidenceType(EvidenceType evidenceType) {
		final StringBuilder sb = new StringBuilder();

		if (evidenceType != null) {
			if (evidenceType.getType() != null && !"".equals(evidenceType.getType())) {
				sb.append(evidenceType.getType());
			}
			if (evidenceType.getSource() != null && evidenceType.getSource().getDbReference() != null
					&& evidenceType.getSource().getDbReference() != null) {
				if (!"".equals(sb.toString())) {
					sb.append("|");
				}
				if (evidenceType.getSource().getDbReference().getType() != null
						&& !"".equals(evidenceType.getSource().getDbReference().getType())) {
					sb.append(evidenceType.getSource().getDbReference().getType());
				}
				if (evidenceType.getSource().getDbReference().getId() != null
						&& !"".equals(evidenceType.getSource().getDbReference().getId())) {
					if (!"".equals(sb.toString())) {
						sb.append(":");
					}
					sb.append(evidenceType.getSource().getDbReference().getId());
				}
			}
		}

		return sb.toString();
	}

	private EvidenceType getEvidenceType(Integer code, List<EvidenceType> evidenceList) {
		if (evidenceList != null) {
			for (final EvidenceType evidenceType : evidenceList) {
				if (evidenceType.getKey().intValue() == code) {
					return evidenceType;
				}
			}
		}
		return null;
	}

	private void appendIfNotEmpty(StringBuilder sb, String string) {
		if (!"".equals(sb.toString()))
			sb.append(string);
	}

	private void parsePrimaryAndSecondaryAccessionsFromEntry(Entry entry) {
		Accession primaryAccession = null;
		final List<String> entryAccessions = entry.getAccession();
		if (!entryAccessions.isEmpty()) {
			int numAcc = 0;
			for (final String accession : entryAccessions) {
				if (numAcc == 0) {
					primaryAccession = new AccessionEx(accession, AccessionType.UNIPROT);
					final List<String> descriptions = getDescriptions();
					if (!descriptions.isEmpty())
						((AccessionEx) primaryAccession).setDescription(descriptions.get(0));
					if (descriptions.size() > 1) {
						for (int j = 1; j < descriptions.size(); j++) {
							((AccessionEx) primaryAccession).addAlternativeName(descriptions.get(j));
						}
					}
					// if it is an isoform, go to <isoform> in the Entry and
					// look for alternative names
					if (isIsoform(accession)) {
						final String isoformVersion = FastaParser.getIsoformVersion(accession);
						final IsoformType isoformType = getIsoformType(primaryAccession.getAccession());
						if (isoformType != null && isoformType.getName() != null) {
							for (final Name name : isoformType.getName()) {
								// skip when:
								// <id>P04637-8</id>
								// <name>8</name>
								if (!name.getValue().equals(isoformVersion)) {
									((AccessionEx) primaryAccession).addAlternativeName(name.getValue());
								}
							}
							// add the <text> as an alternativeName:
							// <text>Produced by alternative promoter usage
							// and alternative splicing.</text>
							if (isoformType.getText() != null) {
								for (final EvidencedStringType evidencedString : isoformType.getText()) {
									if (evidencedString.getValue() != null) {
										((AccessionEx) primaryAccession).addAlternativeName(evidencedString.getValue());
									}
								}
							}
						}
					}
				} else {
					final AccessionEx secondaryAccession = new AccessionEx(accession, AccessionType.UNIPROT);
					final List<String> descriptions = getDescriptions();
					if (!descriptions.isEmpty())
						secondaryAccession.setDescription(descriptions.get(0));
					if (descriptions.size() > 1) {
						for (int j = 1; j < descriptions.size(); j++) {
							secondaryAccession.addAlternativeName(descriptions.get(j));
						}
					}
					addSecondaryAccession(secondaryAccession);
				}
				numAcc++;
			}

		}
		setPrimaryAccession(primaryAccession);
	}

	/**
	 * Look for a particular {@link IsoformType} into the entry/comment/isoform/id
	 * XPath
	 *
	 * @param isoformID
	 * @return
	 */
	private IsoformType getIsoformType(String isoformID) {
		if (isoformID != null) {
			if (entry.getComment() != null) {
				for (final CommentType commentType : entry.getComment()) {
					if (commentType.getIsoform() != null) {
						for (final IsoformType isoformType : commentType.getIsoform()) {
							if (isoformType.getId() != null) {
								if (isoformType.getId().contains(isoformID)) {
									return isoformType;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Integer getLength() {
		if (getSequence() != null && !"".equals(getSequence())) {
			return getSequence().length();
		}

		if (!lengthParsed) {
			if (entry.getSequence() != null)
				super.setLength(entry.getSequence().getLength());

			lengthParsed = true;
		}
		return super.getLength();
	}

	@Override
	public Float getMw() {
		if (!mwParsed) {

			if (entry.getSequence() != null) {
				setMw(Integer.valueOf(entry.getSequence().getMass()).floatValue());
			} else {
				final String sequence = getSequence();
				if (sequence != null && !"".equals(sequence)) {
					try {
						final double mass = new AASequenceImpl(sequence).getMass();
						if (mass > 0.0) {
							setMw(Double.valueOf(mass).floatValue());
							return super.getMw();
						}
					} catch (final Exception e) {
						e.printStackTrace();
					}
				}
			}
			mwParsed = true;
		}
		return super.getMw();
	}

	@Override
	public String getSequence() {
		if (entry.getSequence() != null)
			return entry.getSequence().getValue().trim().replace(" ", "").replace("\n", "");
		return null;
	}

	@Override
	public Organism getOrganism() {
		if (!organismParsed) {
			Organism organism = null;
			final OrganismType uniprotOrganism = entry.getOrganism();
			if (uniprotOrganism != null) {
				String organismName = null;
				final List<OrganismNameType> names = uniprotOrganism.getName();
				for (final OrganismNameType organismNameType : names) {
					if (organismNameType.getType().equalsIgnoreCase("scientific"))
						organismName = organismNameType.getValue();
				}
				String ncbiTaxID = null;
				if (uniprotOrganism.getDbReference() != null) {
					for (final DbReferenceType dbreference : uniprotOrganism.getDbReference()) {
						if (dbreference.getType().equalsIgnoreCase("NCBI Taxonomy"))
							ncbiTaxID = dbreference.getId();
					}
				}
				if (ncbiTaxID != null) {
					organism = new OrganismEx(ncbiTaxID);
					if (organismName != null) {
						organism.setName(organismName);
					}
				} else {
					organism = new OrganismEx(organismName);
				}
			}
			setOrganism(organism);
			organismParsed = true;
		}
		return super.getOrganism();
	}

}
