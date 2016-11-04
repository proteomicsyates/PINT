package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.ArrayList;
import java.util.HashSet;
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
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.model.factories.PSMEx;
import edu.scripps.yates.utilities.model.factories.PeptideEx;
import edu.scripps.yates.utilities.model.factories.ProteinEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.Threshold;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.util.Pair;

public class ProteinsAdapterByExcel implements edu.scripps.yates.utilities.pattern.Adapter<Set<Protein>> {
	private final static Logger log = Logger.getLogger(ProteinsAdapterByExcel.class);
	private final IdentificationExcelType excelCfg;
	private final ExcelFileReader excelFileReader;
	private final Condition expCondition;
	private final MsRunType msRun;
	private final Sample sample;

	// in order to take the same protein for different psms
	// private final static Map<String, Map<String, ProteinEx>>
	// proteinMapByMSRunID = new HashMap<String, Map<String, ProteinEx>>();
	// public final static Map<Condition, Map<String, Set<Protein>>>
	// proteinMapByCondition = new HashMap<Condition, Map<String,
	// Set<Protein>>>();
	// public static final Map<String, Map<Integer, Set<Protein>>>
	// proteinsByMSRunAndRowIndex = new HashMap<String, Map<Integer,
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
			Condition expCondition, MsRunType msRun, Sample sample) {
		excelCfg = excelTypeCfg;
		this.excelFileReader = excelFileReader;
		this.expCondition = expCondition;
		this.msRun = msRun;
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
		Set<String> accessions = getUniprotAccs();
		log.info("Getting annotations from " + accessions.size() + " proteins");
		// use null in order to get the latest version, the current one.
		String uniprotVersion = null;
		UniprotProteinRetriever upr = new UniprotProteinRetriever(uniprotVersion,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
		upr.getAnnotatedProteins(accessions);
	}

	private Set<String> getUniprotAccs() {
		Set<String> accessions = new HashSet<String>();
		final ExcelColumn proteinAccColumn = excelFileReader
				.getExcelColumnFromReference(excelCfg.getProteinAccession().getColumnRef());
		ProteinAccessionType proteinAccessionCfg = excelCfg.getProteinAccession();

		final ProteinDescriptionType proteinDescriptionCfg = excelCfg.getProteinDescription();
		ExcelColumn proteinDescColumn = null;
		if (proteinDescriptionCfg != null) {
			proteinDescColumn = excelFileReader.getExcelColumnFromReference(proteinDescriptionCfg.getColumnRef());
		}
		final List<Object> proteinAccessionValues = proteinAccColumn.getValues();
		int rowIndex = 0;
		for (Object object : proteinAccessionValues) {
			String rawProteinDescription = null;
			if (proteinDescColumn != null && rowIndex < proteinDescColumn.getValues().size())
				rawProteinDescription = proteinDescColumn.getValues().get(rowIndex).toString().trim();
			final String rawProteinAccession = object.toString();
			List<String> proteinAccsToParse = getTokens(proteinAccessionCfg.isGroups(),
					proteinAccessionCfg.getGroupSeparator(), rawProteinAccession);
			List<String> proteinDescsToParse = null;
			if (proteinDescriptionCfg != null) {
				proteinDescsToParse = getTokens(proteinDescriptionCfg.isGroups(),
						proteinDescriptionCfg.getGroupSeparator(), rawProteinDescription);
			}
			for (int i = 0; i < proteinAccsToParse.size(); i++) {
				String proteinAccToParse = proteinAccsToParse.get(i).trim();
				String proteinDescToParse = null;
				if (proteinDescsToParse != null && i < proteinDescsToParse.size())
					proteinDescToParse = proteinDescsToParse.get(i).trim();
				try {
					String proteinAcc = applyRegexp(proteinAccToParse, proteinAccessionCfg.getRegexp());
					if (proteinAcc == null) {
						log.info("Skipping protein accession: '" + proteinAccToParse + "'");
						continue;
					}
					String noParsedProteinDescription = null;
					if (proteinDescriptionCfg != null)
						noParsedProteinDescription = applyRegexp(proteinDescToParse, proteinDescriptionCfg.getRegexp());
					final Pair<String, String> proteinAccessionType = FastaParser.getACC(proteinAcc);
					if (!proteinAccessionType.getSecondElement().equals(AccessionType.IPI.name())) {

						accessions.add(proteinAcc);
					} else {
						// if it is an IPI:

						// try to convert to uniprot
						final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance().map2Uniprot(proteinAcc);
						boolean somethingFound = false;
						if (!map2Uniprot.isEmpty()) {
							for (UniprotEntry uniprotEntry : map2Uniprot) {
								accessions.add(uniprotEntry.getAcc());
							}
							somethingFound = true;
						}
						if (!somethingFound) {

							AccessionEx accession = new AccessionEx(proteinAcc,
									AccessionType.fromValue(proteinAccessionType.getSecondElement()));
							// has to be non parsed protein description for
							// letting map2Uniprot use the information in
							// the header to get the map if possible
							accession.setDescription(noParsedProteinDescription);
							final List<Protein> proteinsMapped = UniprotACCQuery.map2Uniprot(accession, false,
									UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
									UniprotProteinRetrievalSettings.getInstance().isUseIndex());
							if (proteinsMapped != null) {
								for (Protein protein : proteinsMapped) {
									accessions.add(protein.getAccession());
								}
							}
						}

					}
				} catch (IllegalArgumentException e) {

					continue;
				}
			}
			rowIndex++;
		}
		return accessions;

	}

	private Set<Protein> getProteinsFromExcelReader() {
		Set<Protein> ret = new HashSet<Protein>();

		final ExcelColumn proteinAccessionValuesColumn = excelFileReader
				.getExcelColumnFromReference(excelCfg.getProteinAccession().getColumnRef());

		final List<Object> proteinAccessionValues = proteinAccessionValuesColumn.getValues();
		int rowIndex = 0;
		for (Object object : proteinAccessionValues) {
			boolean grabProteinIfNoProteinAmount = false;
			if (excelCfg.getSequence() != null) {
				grabProteinIfNoProteinAmount = true;
			}
			final Set<Protein> proteinSet = getProteins(rowIndex, grabProteinIfNoProteinAmount);
			// add to map by row index
			for (Protein protein : proteinSet) {
				// addProteinByMSRunIDAndRowIndex(msRun.getId(), rowIndex,
				// protein);
				StaticProteomicsModelStorage.addProtein(protein, msRun.getId(), expCondition.getName());
			}

			if (!proteinSet.isEmpty()) {
				if (excelCfg.getSequence() != null) {
					// get the psms
					final PSM psm = new PSMAdapterByExcel(rowIndex, excelCfg, excelFileReader, msRun, expCondition)
							.adapt();
					if (psm != null) {
						// add the psm to the proteins
						// add the proteins to the psm
						for (Protein protein : proteinSet) {
							((ProteinEx) protein).addPSM(psm);
							((PSMEx) psm).addProtein(protein);
						}
					}

					// get the peptides
					final Peptide peptide = new PeptideAdapterByExcel(rowIndex, excelCfg, excelFileReader, msRun,
							expCondition).adapt();
					if (peptide != null) {
						// peptide - psm
						if (psm != null) {
							((PSMEx) psm).setPeptide(peptide);
							((PeptideEx) peptide).addPSM(psm);
						}
						// add the peptide to the proteins
						// add the proteins to the peptide
						for (Protein protein : proteinSet) {
							((ProteinEx) protein).addPeptide(peptide);
							((PeptideEx) peptide).addProtein(protein);
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

	// private List<Protein> getAmountsFromExcelReaderOLD() {
	// List<Protein> ret = new ArrayList<Protein>();
	// final ProteinAmountsType proteinAmountsCfg = excelExpConditionCfg
	// .getProteinAmounts();
	//
	// for (edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountType
	// proteinAmountCfg : proteinAmountsCfg
	// .getProteinAmount()) {
	//
	// AmountType amountType = AmountType.valueOf(proteinAmountCfg
	// .getType().name());
	//
	// final ExcelColumn proteinAmountValuesColumn = excelFileReader
	// .getExcelColumnFromReference(proteinAmountCfg
	// .getColumnRef());
	//
	// final List<Object> proteinAmountValues = proteinAmountValuesColumn
	// .getValues();
	// int rowIndex = 0;
	// for (Object object : proteinAmountValues) {
	// Double value = (Double) object;
	// HashMap<String, Protein> proteins = getProteins(rowIndex);
	// for (Protein protein : proteins.values()) {
	// AmountEx proteinAmount = new AmountEx(value, amountType,
	// expCondition);
	// // if (protein instanceof ProteinEx) {
	// ((ProteinEx) protein).addAmount(proteinAmount);
	// // }
	// ret.add(proteinAmount);
	// }
	// rowIndex++;
	// }
	//
	// }
	// return ret;
	// }

	private Set<Protein> getProteins(int rowIndex, boolean grabProteinIfNoProteinAmount) {
		log.info("Getting proteins from row " + rowIndex + " on condition " + expCondition.getName() + " and run "
				+ msRun.getId());
		Set<Protein> ret = new HashSet<Protein>();

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
		List<String> proteinAccsToParse = getTokens(proteinAccessionCfg.isGroups(),
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
		// proteinMap = new HashMap<String, ProteinEx>();
		// proteinMapByMSRunID.put(msRun.getId(), proteinMap);
		// }
		for (int i = 0; i < proteinAccsToParse.size(); i++) {
			String proteinAccToParse = proteinAccsToParse.get(i).trim();
			String proteinDescriptionToParse = "";
			if (proteinDescriptionsToParse != null && i < proteinDescriptionsToParse.size())
				proteinDescriptionToParse = proteinDescriptionsToParse.get(i).trim();
			String proteinAcc = null;
			Pair<String, String> proteinAccession = null;
			try {
				if (excelCfg.getDiscardDecoys() != null && !"".equals(excelCfg.getDiscardDecoys())) {
					String decoyAcc = applyRegexp(proteinAccToParse, excelCfg.getDiscardDecoys());
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
			} catch (IllegalArgumentException e) {
				log.warn(e.getMessage());
				log.warn("skipping string '" + proteinAccToParse + "' as accession");
				continue;
			}
			String proteinDescription = null;
			if (proteinDescriptionCfg != null)
				proteinDescription = applyRegexp(proteinDescriptionToParse, proteinDescriptionCfg.getRegexp());
			proteinDescription = FastaParser.getDescription(proteinDescription);
			final AccessionEx accession = new AccessionEx(proteinAcc,
					AccessionType.fromValue(proteinAccession.getSecondElement()));
			accession.setDescription(proteinDescription);

			Protein protein = new ProteinExExtension(accession, sample.getOrganism());
			// IMPORTANT: use accession.getAccession instead of proteinAcc just
			// in case that in AccessionEx the accession changes
			if (StaticProteomicsModelStorage.containsProtein(msRun.getId(), expCondition.getName(),
					protein.getAccession())) {
				// protein = proteinMap.get(accession.getAccession());
				protein = StaticProteomicsModelStorage
						.getProtein(msRun.getId(), expCondition.getName(), protein.getAccession()).iterator().next();
			} else {

				// change on 11/14/2014... be careful
				// not add to the proteinMap until get the Uniprot accession
				// proteinMap.put(accession.getAccession(), protein);
				//

				((AccessionEx) protein.getPrimaryAccession()).setDescription(proteinDescription);
				// if the primary accession is not uniprot, check it remotely
				// using the description of the protein if possible
				addUniprotInformation((ProteinEx) protein);
				if (StaticProteomicsModelStorage.containsProtein(msRun.getId(), expCondition.getName(),
						protein.getAccession())) {
					Protein proteinOLD = StaticProteomicsModelStorage.getProtein(msRun.getId(), expCondition.getName(),
							protein.getPrimaryAccession().getAccession()).iterator().next();
					// add to map with also the secondary accession
					if (protein.getSecondaryAccessions() != null) {
						for (Accession secondaryAcc : protein.getSecondaryAccessions()) {
							if (proteinOLD instanceof ProteinEx) {
								((ProteinEx) proteinOLD).addSecondaryAccession(secondaryAcc);
							}
							// add again in order to be indexed by the new
							// accessions:
							StaticProteomicsModelStorage.addProtein(proteinOLD, msRun.getId(), expCondition.getName());
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
			StaticProteomicsModelStorage.addProtein(protein, msRun.getId(), expCondition.getName(), rowIndex);
			// msrun
			protein.setMSRun(new MSRunAdapter(msRun).adapt());
			// condition
			protein.addCondition(expCondition);

			boolean addToSet = true;
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
				for (ScoreType scoreCfg : excelCfg.getProteinScore()) {
					final Score score = new ScoreAdapter(scoreCfg, excelFileReader, rowIndex).adapt();
					if (score != null)
						protein.addScore(score);
				}

			}
			// protein ratios
			// if (proteinRatiosCfg != null) {
			// final List<AmountRatioType> ratioListCfg = proteinRatiosCfg
			// .getAmountRatio();
			// for (AmountRatioType ratioCfg : ratioListCfg) {
			// if (ratioCfg.getNominator().getConditionRef()
			// .equals(conditionID)
			// || ratioCfg.getDenominator().getConditionRef()
			// .equals(conditionID)) {
			// final ExcelColumn ratioExcelColumn = excelFileReader
			// .getExcelColumnFromReference(ratioCfg
			// .getColumnRef());
			// if (rowIndex < ratioExcelColumn.getValues().size()) {
			// final Object ratioValueObj = ratioExcelColumn
			// .getValues().get(rowIndex);
			// if (ratioValueObj != null) {
			// try {
			// double ratioValue = Double
			// .valueOf(ratioValueObj.toString());
			// protein.addRatio(new AmountRatioAdapter(
			// ratioCfg.getName(), ratioValue,
			// ratioCfg.getNominator()
			// .getConditionRef(),
			// ratioCfg.getDenominator()
			// .getConditionRef(), sample,
			// project).adapt());
			// } catch (NumberFormatException e) {
			// // do nothing
			// }
			// }
			// }
			// }
			// }
			// }

		}
		// protein annotations
		if (excelCfg.getProteinAnnotations() != null) {
			Set<ProteinAnnotation> proteinAnnotations = new ProteinAnnotationsAdapterByExcel(rowIndex, excelCfg,
					excelFileReader).adapt();
			for (ProteinAnnotation proteinAnnotation : proteinAnnotations) {
				for (Protein protein : ret) {
					ProteinEx proteinEx = (ProteinEx) protein;
					proteinEx.addProteinAnnotation(proteinAnnotation);
				}
			}
		}
		// protein thresholds
		if (excelCfg.getProteinThresholds() != null) {
			Set<Threshold> proteinThresholds = new ProteinThresholdAdapterByExcel(rowIndex, excelCfg, excelFileReader)
					.adapt();
			for (Threshold threshold : proteinThresholds) {
				for (Protein protein : ret) {
					ProteinEx proteinEx = (ProteinEx) protein;
					proteinEx.addProteinThreshold(threshold);
				}
			}
		}
		return ret;
	}

	/**
	 * Add uniprot information to the protein. The Uniprot information will be
	 * the accessions, the descriptions, the length, the PI. Note that the
	 * primary accession of the protein can be modified since can be mapped from
	 * IPI to Uniprot, and in that case the IPI accession will become a
	 * secondary accession and the Uniprot one will become the primary.
	 *
	 * @param protein
	 */
	protected static void addUniprotInformation(ProteinEx protein) {
		if (!protein.getPrimaryAccession().getAccessionType().equals(AccessionType.UNIPROT)) {
			final List<Protein> map2Uniprot = UniprotACCQuery.map2Uniprot(protein.getPrimaryAccession(), true,
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex());
			if (!map2Uniprot.isEmpty()) {
				Protein firstProtein = map2Uniprot.iterator().next();
				final Accession primaryAccession = firstProtein.getPrimaryAccession();
				if (map2Uniprot.size() > 1) {
					log.info(map2Uniprot.size() + " proteins retrieved using the geneSymbol. Taking the fisrt one: "
							+ primaryAccession.getAccession());
					log.info("");
				}
				Accession previousPrimaryAcc = protein.getPrimaryAccession();
				protein.setPrimaryAccession(primaryAccession);
				if (primaryAccession.getDescription() == null || "".equals(primaryAccession.getDescription()))
					log.info("description null");
				protein.addSecondaryAccession(previousPrimaryAcc);
			}
		} else {
			UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex());
			final Map<String, Protein> annotatedProteins = upr
					.getAnnotatedProtein(protein.getPrimaryAccession().getAccession());
			if (annotatedProteins != null) {
				for (String proteinAcc : annotatedProteins.keySet()) {
					// accessions/descriptions
					final Protein annotatedProtein = annotatedProteins.get(proteinAcc);
					if (protein.getPrimaryAccession().getAccession().equals(proteinAcc)) {
						// update the description
						((AccessionEx) protein.getPrimaryAccession())
								.setDescription(annotatedProtein.getPrimaryAccession().getDescription());
						// update the alternative names
						((AccessionEx) protein.getPrimaryAccession())
								.setAlternativeNames(annotatedProtein.getPrimaryAccession().getAlternativeNames());
					} else {
						// add as secondary accessions
						AccessionEx secondaryAcc = new AccessionEx(proteinAcc, AccessionType.UNIPROT);
						secondaryAcc.setDescription(annotatedProtein.getPrimaryAccession().getDescription());
						secondaryAcc.setAlternativeNames(annotatedProtein.getPrimaryAccession().getAlternativeNames());
						protein.addSecondaryAccession(secondaryAcc);
					}

					// genes
					final Set<Gene> genes = annotatedProtein.getGenes();
					if (genes != null && !genes.isEmpty()) {
						for (Gene gene : genes) {
							protein.addGene(gene);
						}

					}

					// lentgh
					if (annotatedProtein.getLength() > 0)
						protein.setLength(annotatedProtein.getLength());

					// mol w
					if (annotatedProtein.getMW() > 0)
						protein.setMw(annotatedProtein.getMW());

					// pi
					if (annotatedProtein.getPi() > 0)
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
		} catch (PatternSyntaxException e) {
			throw new IllegalArgumentException("Regular expression '" + regexp + "' not valid for string: '"
					+ stringtoParse + "', " + e.getMessage());
		}
	}

	static List<String> getTokens(boolean isGroups, String groupSeparator, String rawString) {
		List<String> list = new ArrayList<String>();
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
	// final Map<Integer, Set<Protein>> map =
	// proteinsByMSRunAndRowIndex.get(msRunRef);
	// addProteinByRowIndex(map, rowIndex, protein);
	// } else {
	// Map<Integer, Set<Protein>> map = new HashMap<Integer, Set<Protein>>();
	// addProteinByRowIndex(map, rowIndex, protein);
	// proteinsByMSRunAndRowIndex.put(msRunRef, map);
	// }
	// }
	//
	// private static void addProteinByRowIndex(Map<Integer, Set<Protein>> map,
	// int rowIndex, Protein protein) {
	// if (map.containsKey(rowIndex)) {
	// map.get(rowIndex).add(protein);
	// } else {
	// Set<Protein> set = new HashSet<Protein>();
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
	// Map<String, Set<Protein>> map = new HashMap<String, Set<Protein>>();
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
	// Set<Protein> ret = new HashSet<Protein>();
	// if (proteinsByMSRunAndRowIndex.containsKey(msRunRef)) {
	// final Map<Integer, Set<Protein>> proteinsByRowIndex =
	// proteinsByMSRunAndRowIndex.get(msRunRef);
	// if (proteinsByRowIndex.containsKey(rowIndex)) {
	// return proteinsByRowIndex.get(rowIndex);
	// }
	// }
	// return ret;
	// }
}
