package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotACCQuery;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinAccessionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProteinDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ScoreType;
import edu.scripps.yates.model.util.ProteinExExtension;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.factories.AccessionEx;
import edu.scripps.yates.utilities.proteomicsmodel.factories.ProteinEx;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import gnu.trove.set.hash.THashSet;

public class ProteinsAdapterByExcel implements edu.scripps.yates.utilities.pattern.Adapter<Set<Protein>> {
	private final static Logger log = Logger.getLogger(ProteinsAdapterByExcel.class);
	private final IdentificationExcelType excelCfg;
	private final ExcelFileReader excelFileReader;
	private final Condition expCondition;
	private final Set<MSRun> msRuns = new THashSet<MSRun>();
	private final Sample sample;
	// this is the MSRun that will be used to assign to PSMs
	private MSRun psmsMSRun;

	// in order to take the same protein for different psms
	// private final static Map<String, Map<String, ProteinEx>>
	// proteinMapByMSRunID = new THashMap<String, Map<String, ProteinEx>>();
	// public final static Map<Condition, Map<String, Set<Protein>>>
	// proteinMapByCondition = new THashMap<Condition, Map<String,
	// Set<Protein>>>();
	// public static final Map<String, TIntObjectHashMap< Set<Protein>>>
	// proteinsByMSRunAndRowIndex = new THashMap<String, TIntObjectHashMap<
	// Set<Protein>>>();

	/**
	 *
	 * @param excelTypeCfg
	 * @param excelFileReader
	 * @param expCondition
	 * @param msRuns
	 * @param proteinAmountRatios
	 * @param psmByRowIndex
	 * @param organism
	 */
	public ProteinsAdapterByExcel(IdentificationExcelType excelTypeCfg, ExcelFileReader excelFileReader,
			Condition expCondition, Collection<MsRunType> msRuns, Sample sample, Project project) {
		excelCfg = excelTypeCfg;
		this.excelFileReader = excelFileReader;
		this.expCondition = expCondition;

		for (final MsRunType msRunType : msRuns) {
			final MSRun msRun = new MSRunAdapter(msRunType, project).adapt();
			this.msRuns.add(msRun);
			if (psmsMSRun == null) {
				psmsMSRun = msRun;
			}
		}
		this.sample = sample;
	}

	@Override
	public Set<Protein> adapt() {
		// retrieve accessions from uniprot first in order to retrieve all at
		// once
		loadUniprotAnnotations();

		final Set<Protein> proteinsFromExcelReader = getProteinsFromExcelReader();
		// addProteinsByCondition(expCondition, proteinsFromExcelReader);
		return proteinsFromExcelReader;

	}

	private void loadUniprotAnnotations() {
		final Set<String> accessions = getUniprotAccs();
		log.info("Getting annotations from " + accessions.size() + " proteins");
		// use null in order to get the latest version, the current one.
		final String uniprotVersion = null;
		final UniprotProteinRetriever upr = new UniprotProteinRetriever(uniprotVersion,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		upr.getAnnotatedProteins(accessions);
	}

	private Set<String> getUniprotAccs() {
		final Set<String> accessions = new THashSet<String>();
		final ExcelColumn proteinAccColumn = excelFileReader
				.getExcelColumnFromReference(excelCfg.getProteinAccession().getColumnRef());
		final ProteinAccessionType proteinAccessionCfg = excelCfg.getProteinAccession();

		final ProteinDescriptionType proteinDescriptionCfg = excelCfg.getProteinDescription();
		ExcelColumn proteinDescColumn = null;
		if (proteinDescriptionCfg != null) {
			proteinDescColumn = excelFileReader.getExcelColumnFromReference(proteinDescriptionCfg.getColumnRef());
		}
		final List<Object> proteinAccessionValues = proteinAccColumn.getValues();
		int rowIndex = 0;
		for (final Object object : proteinAccessionValues) {
			String rawProteinDescription = null;
			if (proteinDescColumn != null && rowIndex < proteinDescColumn.getValues().size())
				rawProteinDescription = proteinDescColumn.getValues().get(rowIndex).toString().trim();
			final String rawProteinAccession = object.toString();
			final List<String> proteinAccsToParse = getTokens(proteinAccessionCfg.isGroups(),
					proteinAccessionCfg.getGroupSeparator(), rawProteinAccession);
			List<String> proteinDescsToParse = null;
			if (proteinDescriptionCfg != null) {
				proteinDescsToParse = getTokens(proteinDescriptionCfg.isGroups(),
						proteinDescriptionCfg.getGroupSeparator(), rawProteinDescription);
			}
			for (int i = 0; i < proteinAccsToParse.size(); i++) {
				final String proteinAccToParse = proteinAccsToParse.get(i).trim();
				String proteinDescToParse = null;
				if (proteinDescsToParse != null && i < proteinDescsToParse.size())
					proteinDescToParse = proteinDescsToParse.get(i).trim();
				try {
					final String proteinAcc = applyRegexp(proteinAccToParse, proteinAccessionCfg.getRegexp());
					if (proteinAcc == null) {
						log.info("Skipping protein accession: '" + proteinAccToParse + "'");
						continue;
					}
					String noParsedProteinDescription = null;
					if (proteinDescriptionCfg != null)
						noParsedProteinDescription = applyRegexp(proteinDescToParse, proteinDescriptionCfg.getRegexp());
					final Accession proteinAccessionType = FastaParser.getACC(proteinAcc);
					if (proteinAccessionType.getAccessionType() != AccessionType.IPI) {

						accessions.add(proteinAcc);
					} else {
						// if it is an IPI:

						// try to convert to uniprot
						final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance().map2Uniprot(proteinAcc);
						boolean somethingFound = false;
						if (!map2Uniprot.isEmpty()) {
							for (final UniprotEntry uniprotEntry : map2Uniprot) {
								accessions.add(uniprotEntry.getAcc());
							}
							somethingFound = true;
						}
						if (!somethingFound) {

							final AccessionEx accession = new AccessionEx(proteinAcc,
									proteinAccessionType.getAccessionType());
							// has to be non parsed protein description for
							// letting map2Uniprot use the information in
							// the header to get the map if possible
							accession.setDescription(noParsedProteinDescription);
							final List<Protein> proteinsMapped = UniprotACCQuery.map2Uniprot(accession, false);
							if (proteinsMapped != null) {
								for (final Protein protein : proteinsMapped) {
									accessions.add(protein.getAccession());
								}
							}
						}

					}
				} catch (final IllegalArgumentException e) {

					continue;
				}
			}
			rowIndex++;
		}
		return accessions;

	}

	private Set<Protein> getProteinsFromExcelReader() {
		final Set<Protein> ret = new THashSet<Protein>();

		final ExcelColumn proteinAccessionValuesColumn = excelFileReader
				.getExcelColumnFromReference(excelCfg.getProteinAccession().getColumnRef());

		final List<Object> proteinAccessionValues = proteinAccessionValuesColumn.getValues();
		int rowIndex = 0;
		for (final Object object : proteinAccessionValues) {
			boolean grabProteinIfNoProteinAmount = false;
			if (excelCfg.getSequence() != null) {
				grabProteinIfNoProteinAmount = true;
			}
			final Set<Protein> proteinSet = getProteins(rowIndex, grabProteinIfNoProteinAmount);
			// add to map by row index
			for (final Protein protein : proteinSet) {
				// addProteinByMSRunIDAndRowIndex(msRun.getId(), rowIndex,
				// protein);
				StaticProteomicsModelStorage.addProtein(protein, msRuns, expCondition.getName());
			}

			if (!proteinSet.isEmpty()) {
				if (excelCfg.getSequence() != null) {
					//
					PSM psm = null;
					final Peptide peptide = new PeptideAdapterByExcel(rowIndex, excelCfg, excelFileReader, msRuns,
							expCondition).adapt();
					// if there is no psmid, hope that the peptide already have a psm, otherwise,
					// we do not create a PSM until the end of the process
					if (excelCfg.getPsmId() != null) {
						// get the psms
						psm = new PSMAdapterByExcel(rowIndex, excelCfg, excelFileReader, psmsMSRun, expCondition)
								.adapt();
					}
					if (psm != null) {
						// add the psm to the proteins
						// add the proteins to the psm
						for (final Protein protein : proteinSet) {
							protein.addPSM(psm, true);
						}
					}

					// get the peptides

					if (peptide != null) {
						// peptide - psm
						if (psm != null) {
							psm.setPeptide(peptide, true);
						}
						// add the peptide to the proteins
						// add the proteins to the peptide
						for (final Protein protein : proteinSet) {
							protein.addPeptide(peptide, true);
						}
					}
				}
				ret.addAll(proteinSet);

			}

			rowIndex++;
		}
		log.info(ret.size() + " proteins readed from " + rowIndex + 1 + " rows in excel");
		return ret;
	}

	private Set<Protein> getProteins(int rowIndex, boolean grabProteinIfNoProteinAmount) {
		log.debug("Getting proteins from row " + rowIndex + " on condition " + expCondition.getName() + " and "
				+ msRuns.size() + " MS runs.");
		final Set<Protein> ret = new THashSet<Protein>();

		final ProteinAccessionType proteinAccessionCfg = excelCfg.getProteinAccession();
		final ProteinDescriptionType proteinDescriptionCfg = excelCfg.getProteinDescription();
		final ExcelColumn proteinAccColumn = excelFileReader
				.getExcelColumnFromReference(proteinAccessionCfg.getColumnRef());
		ExcelColumn proteinDescriptionColumn = null;
		if (proteinDescriptionCfg != null)
			proteinDescriptionColumn = excelFileReader
					.getExcelColumnFromReference(proteinDescriptionCfg.getColumnRef());
		final String rawProteinAccession = proteinAccColumn.getValues().get(rowIndex).toString();
		String rawProteinDescription = null;
		if (proteinDescriptionColumn != null) {
			rawProteinDescription = proteinDescriptionColumn.getValues().get(rowIndex).toString();
		}
		final List<String> proteinAccsToParse = getTokens(proteinAccessionCfg.isGroups(),
				proteinAccessionCfg.getGroupSeparator(), rawProteinAccession);
		List<String> proteinDescriptionsToParse = null;
		if (proteinDescriptionCfg != null) {
			proteinDescriptionsToParse = getTokens(proteinDescriptionCfg.isGroups(),
					proteinDescriptionCfg.getGroupSeparator(), rawProteinDescription);
		}

		// Map<String, ProteinEx> proteinMap = null;
		// if (proteinMapByMSRunID.containsKey(msRun.getId())) {
		// proteinMap = proteinMapByMSRunID.get(msRun.getId());
		// } else {
		// proteinMap = new THashMap<String, ProteinEx>();
		// proteinMapByMSRunID.put(msRun.getId(), proteinMap);
		// }
		for (int i = 0; i < proteinAccsToParse.size(); i++) {
			final String proteinAccToParse = proteinAccsToParse.get(i).trim();
			String proteinDescriptionToParse = "";
			if (proteinDescriptionsToParse != null && i < proteinDescriptionsToParse.size())
				proteinDescriptionToParse = proteinDescriptionsToParse.get(i).trim();
			String proteinAcc = null;
			Accession proteinAccession = null;
			try {
				if (excelCfg.getDiscardDecoys() != null && !"".equals(excelCfg.getDiscardDecoys())) {
					final String decoyAcc = applyRegexp(proteinAccToParse, excelCfg.getDiscardDecoys());
					if (decoyAcc != null) {
						log.info("Discarding DECOY accession: " + proteinAccToParse);
						continue;
					}
				}
				proteinAcc = applyRegexp(proteinAccToParse, proteinAccessionCfg.getRegexp());
				if (proteinAcc == null)
					throw new IllegalArgumentException("Regular expression '" + proteinAccessionCfg.getRegexp()
							+ "' not valid for string: '" + proteinAccToParse + "'");
				proteinAccession = FastaParser.getACC(proteinAcc);
			} catch (final IllegalArgumentException e) {
				log.warn(e.getMessage());
				log.warn("skipping string '" + proteinAccToParse + "' as accession");
				continue;
			}
			String proteinDescription = null;
			if (proteinDescriptionCfg != null)
				proteinDescription = applyRegexp(proteinDescriptionToParse, proteinDescriptionCfg.getRegexp());
			proteinDescription = FastaParser.getDescription(proteinDescription);
			final AccessionEx accession = new AccessionEx(proteinAcc, proteinAccession.getAccessionType());
			accession.setDescription(proteinDescription);

			Protein protein = new ProteinExExtension(accession, sample.getOrganism());
			// IMPORTANT: use accession.getAccession instead of proteinAcc just
			// in case that in AccessionEx the accession changes
			if (StaticProteomicsModelStorage.containsProtein(msRuns, expCondition.getName(), protein.getAccession())) {
				// protein = proteinMap.get(accession.getAccession());
				protein = StaticProteomicsModelStorage
						.getProtein(expCondition.getName(), protein.getAccession(), msRuns).iterator().next();
			} else {

				// change on 11/14/2014... be careful
				// not add to the proteinMap until get the Uniprot accession
				// proteinMap.put(accession.getAccession(), protein);
				//

				((AccessionEx) protein.getPrimaryAccession()).setDescription(proteinDescription);
				// if the primary accession is not uniprot, check it remotely
				// using the description of the protein if possible
				addUniprotInformation((ProteinEx) protein);
				if (StaticProteomicsModelStorage.containsProtein(msRuns, expCondition.getName(),
						protein.getAccession())) {
					final Protein proteinOLD = StaticProteomicsModelStorage
							.getProtein(expCondition.getName(), protein.getPrimaryAccession().getAccession(), msRuns)
							.iterator().next();
					// add to map with also the secondary accession
					if (protein.getSecondaryAccessions() != null) {
						for (final Accession secondaryAcc : protein.getSecondaryAccessions()) {
							if (proteinOLD instanceof ProteinEx) {
								((ProteinEx) proteinOLD).addSecondaryAccession(secondaryAcc);
							}
							// add again in order to be indexed by the new
							// accessions:
							StaticProteomicsModelStorage.addProtein(proteinOLD, msRuns, expCondition.getName());
							// proteinMap.put(secondaryAcc.getAccession(),
							// proteinOLD);
						}
					}
					protein = proteinOLD;
				} else {
					// change on 11/14/2014... be careful
					// proteinMap.put(accession.getAccession(), protein);
					// proteinMap.put(protein.getPrimaryAccession().getAccession(),
					// protein);

					// add to map with also the secondary accession
					// if (protein.getSecondaryAccessions() != null) {
					// for (Accession secondaryAcc :
					// protein.getSecondaryAccessions()) {
					// proteinMap.put(secondaryAcc.getAccession(), protein);
					// }
					// }
					//

				}
			}
			StaticProteomicsModelStorage.addProtein(protein, msRuns, expCondition.getName(), rowIndex);
			// msrun
			for (final MSRun msRun : msRuns) {
				protein.addMSRun(msRun);
			}
			// condition
			protein.addCondition(expCondition);

			final boolean addToSet = true;
			// protein amounts
			// DISABLED SINCE AMOUNTS ARE CREATED AT CONDITIONADAPTER
			// if (excelCfg.getProteinAmounts() != null) {
			// final List<Amount> proteinAmounts = new
			// ProteinAmountsAdapterByExcel(
			// rowIndex, excelCfg.getProteinAmounts(),
			// excelFileReader, expCondition).adapt();
			// if (proteinAmounts.isEmpty())
			// addToSet = false;
			// for (Amount amount : proteinAmounts) {
			// protein.addAmount(amount);
			// }
			// }

			// add to the protein set
			if (addToSet || grabProteinIfNoProteinAmount) {
				ret.add(protein);
			} else {
				log.debug("Skipping this protein " + protein.getPrimaryAccession().getAccession() + " in condition "
						+ expCondition.getName() + " because has no protein amounts");
				continue;
			}

			// protein scores
			if (excelCfg.getProteinScore() != null) {
				for (final ScoreType scoreCfg : excelCfg.getProteinScore()) {
					final Score score = new ScoreAdapter(scoreCfg, excelFileReader, rowIndex).adapt();
					if (score != null)
						protein.addScore(score);
				}

			}

		}
		// protein annotations
		if (excelCfg.getProteinAnnotations() != null) {
			final Set<ProteinAnnotation> proteinAnnotations = new ProteinAnnotationsAdapterByExcel(rowIndex, excelCfg,
					excelFileReader).adapt();
			for (final ProteinAnnotation proteinAnnotation : proteinAnnotations) {
				for (final Protein protein : ret) {
					final ProteinEx proteinEx = (ProteinEx) protein;
					proteinEx.addProteinAnnotation(proteinAnnotation);
				}
			}
		}
		// protein thresholds
		if (excelCfg.getProteinThresholds() != null) {
			final Set<Threshold> proteinThresholds = new ProteinThresholdAdapterByExcel(rowIndex, excelCfg,
					excelFileReader).adapt();
			for (final Threshold threshold : proteinThresholds) {
				for (final Protein protein : ret) {
					protein.addThreshold(threshold);
				}
			}
		}
		return ret;
	}

	/**
	 * Add uniprot information to the protein. The Uniprot information will be the
	 * accessions, the descriptions, the length, the PI. Note that the primary
	 * accession of the protein can be modified since can be mapped from IPI to
	 * Uniprot, and in that case the IPI accession will become a secondary accession
	 * and the Uniprot one will become the primary.
	 *
	 * @param protein
	 */
	protected static void addUniprotInformation(ProteinEx protein) {
		if (!protein.getPrimaryAccession().getAccessionType().equals(AccessionType.UNIPROT)) {
			final List<Protein> map2Uniprot = UniprotACCQuery.map2Uniprot(protein.getPrimaryAccession(), true);
			if (!map2Uniprot.isEmpty()) {
				final Protein firstProtein = map2Uniprot.iterator().next();
				final Accession primaryAccession = firstProtein.getPrimaryAccession();
				if (map2Uniprot.size() > 1) {
					log.info(map2Uniprot.size() + " proteins retrieved using the geneSymbol. Taking the fisrt one: "
							+ primaryAccession.getAccession());
					log.info("");
				}
				final Accession previousPrimaryAcc = protein.getPrimaryAccession();
				protein.setPrimaryAccession(primaryAccession);
				if (primaryAccession.getDescription() == null || "".equals(primaryAccession.getDescription()))
					log.info("description null");
				protein.addSecondaryAccession(previousPrimaryAcc);
			}
		} else {
			final UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex());
			final Map<String, Protein> annotatedProteins = upr
					.getAnnotatedProtein(protein.getPrimaryAccession().getAccession());
			if (annotatedProteins != null) {
				for (final String proteinAcc : annotatedProteins.keySet()) {
					// accessions/descriptions
					final Protein annotatedProtein = annotatedProteins.get(proteinAcc);
					if (annotatedProtein == null) {
						continue;
					}
					if (protein.getPrimaryAccession().getAccession().equals(proteinAcc)) {
						// update the description
						((AccessionEx) protein.getPrimaryAccession())
								.setDescription(annotatedProtein.getPrimaryAccession().getDescription());
						// update the alternative names
						((AccessionEx) protein.getPrimaryAccession())
								.setAlternativeNames(annotatedProtein.getPrimaryAccession().getAlternativeNames());
					} else {
						// add as secondary accessions
						final AccessionEx secondaryAcc = new AccessionEx(proteinAcc, AccessionType.UNIPROT);
						secondaryAcc.setDescription(annotatedProtein.getPrimaryAccession().getDescription());
						secondaryAcc.setAlternativeNames(annotatedProtein.getPrimaryAccession().getAlternativeNames());
						protein.addSecondaryAccession(secondaryAcc);
					}

					// genes
					final Set<Gene> genes = annotatedProtein.getGenes();
					if (genes != null && !genes.isEmpty()) {
						for (final Gene gene : genes) {
							protein.addGene(gene);
						}

					}

					// lentgh
					if (annotatedProtein.getLength() != null && annotatedProtein.getLength() > 0)
						protein.setLength(annotatedProtein.getLength());

					// mol w
					if (annotatedProtein.getMw() != null && annotatedProtein.getMw() > 0)
						protein.setMw(annotatedProtein.getMw());

					// pi
					if (annotatedProtein.getPi() != null && annotatedProtein.getPi() > 0)
						protein.setPi(annotatedProtein.getPi());
				}
			}

		}

	}

	static String applyRegexp(String stringtoParse, String regexp) {
		if (stringtoParse == null || "".equals(stringtoParse) || regexp == null || "".equals(regexp))
			return "";
		if (regexp.equals(".*")) {
			regexp = "(.*)";
		}
		if (!regexp.contains(".")) {
			regexp = "(" + regexp + ").*";
		}
		try {
			final Pattern compile = Pattern.compile(regexp);
			final Matcher matcher = compile.matcher(stringtoParse);
			if (matcher.find() && matcher.groupCount() > 0) {
				return matcher.group(1);
			} else {
				return null;
			}
		} catch (final PatternSyntaxException e) {
			throw new IllegalArgumentException("Regular expression '" + regexp + "' not valid for string: '"
					+ stringtoParse + "', " + e.getMessage());
		}
	}

	static List<String> getTokens(boolean isGroups, String groupSeparator, String rawString) {
		final List<String> list = new ArrayList<String>();
		if (isGroups) {
			final StringTokenizer stringTokenizer = new StringTokenizer(rawString, groupSeparator, false);
			while (stringTokenizer.hasMoreElements()) {
				final String nextToken = stringTokenizer.nextToken();
				if (!"".equals(nextToken))
					list.add(nextToken);
			}
		} else {
			list.add(rawString);
		}
		return list;
	}

	// private static void addProteinByMSRunIDAndRowIndex(String msRunRef, int
	// rowIndex, Protein protein) {
	// if (proteinsByMSRunAndRowIndex.containsKey(msRunRef)) {
	// final TIntObjectHashMap< Set<Protein>> map =
	// proteinsByMSRunAndRowIndex.get(msRunRef);
	// addProteinByRowIndex(map, rowIndex, protein);
	// } else {
	// TIntObjectHashMap< Set<Protein>> map = new TIntObjectHashMap<
	// Set<Protein>>();
	// addProteinByRowIndex(map, rowIndex, protein);
	// proteinsByMSRunAndRowIndex.put(msRunRef, map);
	// }
	// }
	//
	// private static void addProteinByRowIndex(TIntObjectHashMap< Set<Protein>>
	// map,
	// int rowIndex, Protein protein) {
	// if (map.containsKey(rowIndex)) {
	// map.get(rowIndex).add(protein);
	// } else {
	// Set<Protein> set = new THashSet<Protein>();
	// set.add(protein);
	// map.put(rowIndex, set);
	// }
	// }
	//
	// private void addProteinsByCondition(Condition condition,
	// Collection<Protein> proteins) {
	// if (proteinMapByCondition.containsKey(condition)) {
	// ModelUtils.addToMap(proteinMapByCondition.get(condition), proteins);
	// } else {
	// Map<String, Set<Protein>> map = new THashMap<String, Set<Protein>>();
	// ModelUtils.addToMap(map, proteins);
	// proteinMapByCondition.put(condition, map);
	// }
	// }

	// protected static void clearStaticInformation() {
	// proteinMapByMSRunID.clear();
	// proteinMapByCondition.clear();
	// }
	//
	// protected static void clearStaticInformationByRow() {
	// proteinsByMSRunAndRowIndex.clear();
	//
	// }
	//
	// protected static Set<Protein> getProteinsByMSRunAndRowIndex(String
	// msRunRef, int rowIndex) {
	// Set<Protein> ret = new THashSet<Protein>();
	// if (proteinsByMSRunAndRowIndex.containsKey(msRunRef)) {
	// final TIntObjectHashMap< Set<Protein>> proteinsByRowIndex =
	// proteinsByMSRunAndRowIndex.get(msRunRef);
	// if (proteinsByRowIndex.containsKey(rowIndex)) {
	// return proteinsByRowIndex.get(rowIndex);
	// }
	// }
	// return ret;
	// }
}
