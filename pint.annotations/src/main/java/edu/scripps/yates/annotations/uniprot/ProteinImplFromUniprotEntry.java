package edu.scripps.yates.annotations.uniprot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.compomics.util.protein.AASequenceImpl;

import edu.scripps.yates.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.EvidenceType;
import edu.scripps.yates.annotations.uniprot.xml.EvidencedStringType;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.annotations.uniprot.xml.GeneNameType;
import edu.scripps.yates.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.annotations.uniprot.xml.IsoformType;
import edu.scripps.yates.annotations.uniprot.xml.IsoformType.Name;
import edu.scripps.yates.annotations.uniprot.xml.KeywordType;
import edu.scripps.yates.annotations.uniprot.xml.LocationType;
import edu.scripps.yates.annotations.uniprot.xml.OrganismNameType;
import edu.scripps.yates.annotations.uniprot.xml.OrganismType;
import edu.scripps.yates.annotations.uniprot.xml.PropertyType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinExistenceType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType.AlternativeName;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType.RecommendedName;
import edu.scripps.yates.annotations.uniprot.xml.ProteinType.SubmittedName;
import edu.scripps.yates.annotations.uniprot.xml.ReferenceType;
import edu.scripps.yates.annotations.uniprot.xml.SourceDataType.Plasmid;
import edu.scripps.yates.annotations.uniprot.xml.SourceDataType.Strain;
import edu.scripps.yates.annotations.uniprot.xml.SourceDataType.Transposon;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.model.factories.GeneEx;
import edu.scripps.yates.utilities.model.factories.OrganismEx;
import edu.scripps.yates.utilities.model.factories.ProteinAnnotationEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;

public class ProteinImplFromUniprotEntry implements Protein {
	private static Logger log = Logger.getLogger(ProteinImplFromUniprotEntry.class);
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
	private final List<Accession> secondaryAccessions = new ArrayList<Accession>();
	private Accession primaryAccession;
	private ProteinEvidence evidence;
	private ProteinGroup proteinGroup;
	private Organism organism;
	private final Set<Amount> amounts = new HashSet<Amount>();
	private final Set<Condition> conditions = new HashSet<Condition>();
	private final Set<Ratio> ratios = new HashSet<Ratio>();
	private final Set<Score> scores = new HashSet<Score>();
	private final Set<PSM> psms = new HashSet<PSM>();
	private boolean organismParsed;
	private MSRun msRun;
	private int length = 0;
	private boolean lengthParsed;
	private double pi;
	private double mw;
	private boolean mwParsed;
	private final Set<Peptide> peptides = new HashSet<Peptide>();
	private final Set<Gene> genes = new HashSet<Gene>();
	private boolean genesParsed;

	public ProteinImplFromUniprotEntry(Entry proteinEntry) {
		entry = proteinEntry;
		primaryAccession = getPrimaryAccession();

	}

	private boolean isIsoform() {
		if (primaryAccession.getAccessionType() == AccessionType.UNIPROT) {
			final String accession = primaryAccession.getAccession();
			String version = FastaParser.getIsoformVersion(accession);
			if (version != null && !"1".equals(version)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<Gene> getGenes() {
		if (!genesParsed) {
			final List<GeneType> genesUniprot = entry.getGene();
			if (genesUniprot != null) {
				for (GeneType gene : genesUniprot) {
					for (GeneNameType geneName : gene.getName()) {
						final GeneEx geneEx = new GeneEx(geneName.getValue());
						geneEx.setGeneType(geneName.getType());
						genes.add(geneEx);
					}
				}
			}
			genesParsed = true;
		}
		return genes;
	}

	private List<String> getDescriptions() {
		final ProteinType protein = entry.getProtein();
		List<String> ret = new ArrayList<String>();
		if (entry.getProtein() != null) {
			final RecommendedName recommendedName = protein.getRecommendedName();
			if (recommendedName != null) {
				StringBuilder sb = new StringBuilder();
				if (recommendedName.getFullName() != null && recommendedName.getFullName().getValue() != null) {
					sb.append(recommendedName.getFullName().getValue().trim());
				}
				final List<EvidencedStringType> shortNames = recommendedName.getShortName();
				if (shortNames != null && shortNames.isEmpty()) {
					for (int i = 0; i < shortNames.size(); i++) {
						EvidencedStringType shortName = shortNames.get(i);
						sb.append(" (" + shortName.getValue().trim() + ")");
					}
				}
				final List<EvidencedStringType> ecNumbers = recommendedName.getEcNumber();
				if (ecNumbers != null && ecNumbers.isEmpty()) {
					for (EvidencedStringType ecNumber : ecNumbers) {
						sb.append(" (" + ecNumber.getValue().trim() + ")");
					}
				}
				ret.add(sb.toString());
			}
			final List<AlternativeName> alternativeNames = protein.getAlternativeName();
			if (alternativeNames != null && !alternativeNames.isEmpty()) {
				for (AlternativeName alternativeName2 : alternativeNames) {
					StringBuilder sb = new StringBuilder();
					sb.append(alternativeName2.getFullName().getValue().trim());
					final List<EvidencedStringType> shortNames = alternativeName2.getShortName();
					if (shortNames != null && shortNames.isEmpty()) {
						for (int i = 0; i < shortNames.size(); i++) {
							EvidencedStringType shortName = shortNames.get(i);
							sb.append(" (" + shortName.getValue().trim() + ")");
						}
					}
					final List<EvidencedStringType> ecNumbers = alternativeName2.getEcNumber();
					if (ecNumbers != null && ecNumbers.isEmpty()) {
						for (EvidencedStringType ecNumber : ecNumbers) {
							sb.append(" (" + ecNumber.getValue().trim() + ")");
						}
					}
					ret.add(sb.toString());
				}
			}

			final List<SubmittedName> submittedNames = protein.getSubmittedName();
			if (submittedNames != null && !submittedNames.isEmpty()) {
				for (SubmittedName submittedName2 : submittedNames) {
					StringBuilder sb = new StringBuilder();
					sb.append(submittedName2.getFullName().getValue().trim());
					final List<EvidencedStringType> ecNumbers = submittedName2.getEcNumber();
					if (ecNumbers != null && ecNumbers.isEmpty()) {
						for (EvidencedStringType ecNumber : ecNumbers) {
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
		Set<ProteinAnnotation> ret = new HashSet<ProteinAnnotation>();
		// comments (CC):
		final List<CommentType> comments = entry.getComment();
		if (comments != null) {
			for (CommentType comment : comments) {
				final AnnotationType annotationType = AnnotationType.translateStringToAnnotationType(comment.getType());
				if ("tissue specificity".equals(annotationType.getKey()) && comment.getText() != null) {
					// in this case, the text can contains different phrases,
					// each of them can start by "isoform 4 is expressed..."
					// in that case, only include the ones for the appropiate
					// isoform
					for (EvidencedStringType text : comment.getText()) {
						if (text.getValue() != null) {
							List<String> phrases = new ArrayList<String>();
							if (text.getValue().contains(".")) {
								final String[] split = text.getValue().split("\\.");
								final List<String> asList = Arrays.asList(split);
								phrases.addAll(asList);
							} else {
								phrases.add(text.getValue());
							}
							final Pattern isoformPattern = Pattern.compile("isoform (\\d+)");
							StringBuilder sb = new StringBuilder();
							for (String phrase : phrases) {
								final Matcher matcher = isoformPattern.matcher(phrase.toLowerCase());
								if (matcher.find()) {
									// if found, only get if when the isoform
									// number is correct
									if (isIsoform()) {
										if (FastaParser.getIsoformVersion(getAccession()).equals(matcher.group(1))) {
											if (!"".equals(sb.toString())) {
												sb.append(" ");
											}
											sb.append(phrase.trim()).append(".");
										}
									}
								} else {
									// not found, only get it if we are in the
									// canonical entry
									if (!isIsoform()) {
										if (!"".equals(sb.toString())) {
											sb.append(" ");
										}
										sb.append(phrase.trim()).append(".");
									}
								}
							}
							ProteinAnnotationEx proteinAnnotationEx = new ProteinAnnotationEx(annotationType,
									comment.getType(), sb.toString());
							ret.add(proteinAnnotationEx);
						}
					}
				} else {
					final Set<ProteinAnnotation> annotations = new ProteinAnnotationsFromCommentAdapter(comment)
							.adapt();
					ret.addAll(annotations);
				}
			}
		}

		// features (FT):
		final List<FeatureType> features = entry.getFeature();
		if (features != null) {
			for (FeatureType feature : features) {
				final AnnotationType annotationType = AnnotationType.translateStringToAnnotationType(feature.getType());
				if (skipFeature(feature)) {
					continue;
				}

				// the the description as the annotation name if available.
				// Otherwise, take the type
				String annotName = feature.getDescription();
				if (annotName == null) {
					annotName = feature.getType();
				}
				ProteinAnnotation annotation = new ProteinAnnotationEx(annotationType, annotName,
						getValueFromFeature(feature));
				ret.add(annotation);
			}
		}

		// keywords (KW)
		final List<KeywordType> keywords = entry.getKeyword();
		if (keywords != null) {
			for (KeywordType keyword : keywords) {
				ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.uniprotKeyword,
						keyword.getValue(), getValueFromKeyword(keyword));
				ret.add(annotation);
			}
		}

		// protein existence (PE)
		final ProteinExistenceType proteinExistence = entry.getProteinExistence();
		if (proteinExistence != null) {
			ProteinAnnotation annotation = new ProteinAnnotationEx(
					AnnotationType.translateStringToAnnotationType(proteinExistence.getType()),
					proteinExistence.getType());
			ret.add(annotation);
		}

		// DB reference (DR)
		final List<DbReferenceType> dbReferences = entry.getDbReference();
		if (dbReferences != null) {
			for (DbReferenceType dbReference : dbReferences) {

				// <dbReference type="GO" id="GO:0000166">
				// <property type="term" value="F:nucleotide binding"/>
				// <property type="evidence" value="IEA:InterPro"/>
				// </dbReference>
				// <dbReference type="PhosphoSite" id="P16884"/>
				// <dbReference type="UCSC" id="RGD:3159">
				// <property type="organism name" value="rat"/>
				// </dbReference>

				StringBuilder sb = new StringBuilder();
				final List<PropertyType> properties = dbReference.getProperty();
				if (properties != null) {
					for (PropertyType property : properties) {
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
					ProteinAnnotation annotation = new ProteinAnnotationEx(annotationType, dbReference.getId(),
							sb.toString());
					ret.add(annotation);
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
			for (ReferenceType reference : references) {
				if (reference.getSource() != null) {
					final List<Object> strainOrPlasmidOrTransposon = reference.getSource()
							.getStrainOrPlasmidOrTransposon();
					if (strainOrPlasmidOrTransposon != null) {
						for (Object obj : strainOrPlasmidOrTransposon) {
							if (obj instanceof Strain) {
								Strain strain = (Strain) obj;
								StringBuilder sb = new StringBuilder();
								getValueFromEvidences(sb, strain.getEvidence());
								ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.strain,
										strain.getValue(), sb.toString());
								ret.add(annotation);
							} else if (obj instanceof Plasmid) {
								Plasmid plasmid = (Plasmid) obj;
								StringBuilder sb = new StringBuilder();
								getValueFromEvidences(sb, plasmid.getEvidence());
								ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.plasmid,
										plasmid.getValue(), sb.toString());
								ret.add(annotation);
							} else if (obj instanceof Transposon) {
								Transposon transposon = (Transposon) obj;
								StringBuilder sb = new StringBuilder();
								getValueFromEvidences(sb, transposon.getEvidence());
								ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.transposon,
										transposon.getValue(), sb.toString());
								ret.add(annotation);
							} else if (obj instanceof edu.scripps.yates.annotations.uniprot.xml.SourceDataType.Tissue) {
								edu.scripps.yates.annotations.uniprot.xml.SourceDataType.Tissue tissue = (edu.scripps.yates.annotations.uniprot.xml.SourceDataType.Tissue) obj;
								StringBuilder sb = new StringBuilder();
								getValueFromEvidences(sb, tissue.getEvidence());
								ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.tissue,
										tissue.getValue(), sb.toString());
								ret.add(annotation);
							}
						}
					}
				}
			}
		}

		// reviewed or unreviewed
		final String dataset = entry.getDataset();
		if ("Swiss-Prot".equalsIgnoreCase(dataset)) {
			ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.status, "reviewed");
			ret.add(annotation);
		} else if ("TrEMBL".equalsIgnoreCase(dataset)) {
			ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.status, "unreviewed");
			ret.add(annotation);
		}

		// entry creation date
		if (entry.getCreated() != null) {
			Calendar calendar = entry.getCreated().toGregorianCalendar();
			formatter.setTimeZone(calendar.getTimeZone());
			String dateString = formatter.format(calendar.getTime());
			ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.entry_creation_date, dateString);
			ret.add(annotation);
		}

		// entry modified
		if (entry.getModified() != null) {
			Calendar calendar = entry.getModified().toGregorianCalendar();
			formatter.setTimeZone(calendar.getTimeZone());
			String dateString = formatter.format(calendar.getTime());
			ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.entry_modified, dateString);
			ret.add(annotation);
		}

		// entry version
		if (entry.getVersion() > 0) {
			ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.entry_version,
					String.valueOf(entry.getVersion()));
			ret.add(annotation);
		}

		// sequence modified
		if (entry.getSequence() != null && entry.getSequence().getModified() != null) {
			Calendar calendar = entry.getSequence().getModified().toGregorianCalendar();
			formatter.setTimeZone(calendar.getTimeZone());
			String dateString = formatter.format(calendar.getTime());
			ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.sequence_modified, dateString);
			ret.add(annotation);
		}

		// sequence version
		if (entry.getSequence() != null && entry.getSequence().getVersion() > -1) {
			ProteinAnnotation annotation = new ProteinAnnotationEx(AnnotationType.sequence_modified,
					String.valueOf(entry.getSequence().getVersion()));
			ret.add(annotation);
		}

		return ret;
	}

	/**
	 * Decides if the feature is valid for this protein or not because sometimes
	 * the entry is coming from the canonical Uniprot protein and this is
	 * actually an isoform. So we want to skip all annotations regarding the
	 * isoforms.
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
						String string = split[index];
						try {
							int isoformNumber = Integer.valueOf(String.valueOf(string.trim().charAt(0)));
							// isoformVersion can be null iof it is the
							// canonical protein
							if (String.valueOf(isoformNumber).equals(isoformVersion)) {
								return false;
							}
						} catch (NumberFormatException e) {

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
		StringBuilder sb = new StringBuilder();
		sb.append("id:" + keyword.getId());
		getValueFromEvidences(sb, keyword.getEvidence());

		return sb.toString();
	}

	private String getValueFromFeature(FeatureType feature) {
		StringBuilder sb = new StringBuilder();
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
			for (Integer code : evidences) {
				if (code != null) {
					appendIfNotEmpty(sb, ANNOTATION_SEPARATOR);
					EvidenceType evidenceType = getEvidenceType(code, entry.getEvidence());
					if (evidenceType != null) {
						sb.append(" Evidence from:" + getStringFromEvidenceType(evidenceType));
					}
				}
			}
		}
	}

	private String getStringFromEvidenceType(EvidenceType evidenceType) {
		StringBuilder sb = new StringBuilder();

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
			for (EvidenceType evidenceType : evidenceList) {
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

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public Set<Condition> getConditions() {
		return conditions;
	}

	@Override
	public Set<Ratio> getRatios() {
		return ratios;
	}

	@Override
	public Set<Threshold> getThresholds() {
		return null;
	}

	@Override
	public Boolean passThreshold(String thresholdName) {
		return null;
	}

	@Override
	public Set<PSM> getPSMs() {
		return psms;
	}

	@Override
	public int getDBId() {
		return -1;
	}

	@Override
	public Accession getPrimaryAccession() {
		if (primaryAccession == null) {
			final List<String> entryAccessions = entry.getAccession();
			if (!entryAccessions.isEmpty()) {
				int numAcc = 0;
				for (String accession : entryAccessions) {
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
						if (isIsoform()) {
							final String isoformVersion = FastaParser.getIsoformVersion(accession);
							IsoformType isoformType = getIsoformType(primaryAccession.getAccession());
							if (isoformType != null && isoformType.getName() != null) {
								for (Name name : isoformType.getName()) {
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
									for (EvidencedStringType evidencedString : isoformType.getText()) {
										if (evidencedString.getValue() != null) {
											((AccessionEx) primaryAccession)
													.addAlternativeName(evidencedString.getValue());
										}
									}
								}
							}
						}
					} else {
						AccessionEx secondaryAccession = new AccessionEx(accession, AccessionType.UNIPROT);
						final List<String> descriptions = getDescriptions();
						if (!descriptions.isEmpty())
							secondaryAccession.setDescription(descriptions.get(0));
						if (descriptions.size() > 1) {
							for (int j = 1; j < descriptions.size(); j++) {
								secondaryAccession.addAlternativeName(descriptions.get(j));
							}
						}
						secondaryAccessions.add(secondaryAccession);
					}
					numAcc++;
				}

			}
		}
		return primaryAccession;
	}

	/**
	 * Look for a particular {@link IsoformType} into the
	 * entry/comment/isoform/id XPath
	 *
	 * @param isoformID
	 * @return
	 */
	private IsoformType getIsoformType(String isoformID) {
		if (isoformID != null) {
			if (entry.getComment() != null) {
				for (CommentType commentType : entry.getComment()) {
					if (commentType.getIsoform() != null) {
						for (IsoformType isoformType : commentType.getIsoform()) {
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
	public String getAccession() {
		return getPrimaryAccession().getAccession();
	}

	@Override
	public int getLength() {
		if (getSequence() != null && !"".equals(getSequence())) {
			return getSequence().length();
		}

		if (!lengthParsed) {
			if (entry.getSequence() != null)
				length = entry.getSequence().getLength();

			lengthParsed = true;
		}
		return length;
	}

	@Override
	public double getPi() {
		return pi;
	}

	@Override
	public double getMW() {
		if (!mwParsed) {
			final String sequence = getSequence();
			if (sequence != null && !"".equals(sequence)) {

				final double mass = new AASequenceImpl(sequence).getMass();
				if (mass > 0.0) {
					return mass;
				}

			}

			if (entry.getSequence() != null) {
				mw = entry.getSequence().getMass();
			}
			mwParsed = true;
		}
		return mw;
	}

	@Override
	public String getSequence() {
		if (entry.getSequence() != null)
			return entry.getSequence().getValue().trim().replace(" ", "").replace("\n", "");
		return null;
	}

	@Override
	public void setEvidence(ProteinEvidence evidence) {
		this.evidence = evidence;
	}

	@Override
	public void setProteinGroup(ProteinGroup proteinGroup) {
		this.proteinGroup = proteinGroup;

	}

	@Override
	public Set<Score> getScores() {
		return scores;
	}

	@Override
	public List<Accession> getSecondaryAccessions() {
		return secondaryAccessions;
	}

	@Override
	public Set<Peptide> getPeptides() {
		return peptides;
	}

	@Override
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	@Override
	public ProteinGroup getProteinGroup() {
		return proteinGroup;
	}

	@Override
	public Organism getOrganism() {
		if (!organismParsed) {
			final OrganismType uniprotOrganism = entry.getOrganism();
			if (uniprotOrganism != null) {
				String organismName = null;
				final List<OrganismNameType> names = uniprotOrganism.getName();
				for (OrganismNameType organismNameType : names) {
					if (organismNameType.getType().equalsIgnoreCase("scientific"))
						organismName = organismNameType.getValue();
				}
				String ncbiTaxID = null;
				if (uniprotOrganism.getDbReference() != null) {
					for (DbReferenceType dbreference : uniprotOrganism.getDbReference()) {
						if (dbreference.getType().equalsIgnoreCase("NCBI Taxonomy"))
							ncbiTaxID = dbreference.getId();
					}
				}
				if (ncbiTaxID != null) {
					organism = new OrganismEx(ncbiTaxID);
					if (organismName != null)
						((OrganismEx) organism).setName(organismName);
				} else {
					organism = new OrganismEx(organismName);
				}
			}
			organismParsed = true;
		}
		return organism;
	}

	@Override
	public MSRun getMSRun() {
		return msRun;
	}

	@Override
	public List<GroupablePSM> getGroupablePSMs() {
		List<GroupablePSM> list = new ArrayList<GroupablePSM>();
		list.addAll(getPSMs());
		return list;
	}

	@Override
	public void addScore(Score score) {
		if (!scores.contains(score))
			scores.add(score);

	}

	@Override
	public void addRatio(Ratio ratio) {
		if (!ratios.contains(ratio))
			ratios.add(ratio);

	}

	@Override
	public void addAmount(Amount amount) {
		if (!amounts.contains(amount))
			amounts.add(amount);

	}

	@Override
	public void addCondition(Condition condition) {
		if (!conditions.contains(condition))
			conditions.add(condition);

	}

	@Override
	public void addPSM(PSM psm) {
		if (!psms.contains(psm))
			psms.add(psm);

	}

	@Override
	public void setOrganism(Organism organism) {
		this.organism = organism;

	}

	@Override
	public void setMSRun(MSRun msRun) {
		this.msRun = msRun;

	}

	@Override
	public void setMw(double mw) {
		this.mw = mw;

	}

	@Override
	public void setPi(double pi) {
		this.pi = pi;

	}

	@Override
	public void setLength(int length) {
		this.length = length;

	}

	@Override
	public void addPeptide(Peptide peptide) {
		if (!peptides.contains(peptide)) {
			peptides.add(peptide);
		}

	}

	@Override
	public void addGene(Gene gene) {
		if (!genes.contains(gene))
			genes.add(gene);

	}

}
