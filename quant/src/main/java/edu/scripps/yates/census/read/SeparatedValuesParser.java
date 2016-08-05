package edu.scripps.yates.census.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.read.model.CensusRatio;
import edu.scripps.yates.census.read.model.QuantStaticMaps;
import edu.scripps.yates.census.read.model.QuantifiedPSMFromCensusOut;
import edu.scripps.yates.census.read.model.QuantifiedPeptide;
import edu.scripps.yates.census.read.model.QuantifiedProteinFromCensusOut;
import edu.scripps.yates.census.read.model.QuantifiedProteinFromDBIndexEntry;
import edu.scripps.yates.census.read.model.RatioScore;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.IndexedProtein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public class SeparatedValuesParser extends AbstractQuantParser {
	private final static Logger log = Logger.getLogger(SeparatedValuesParser.class);
	private final String separator;
	private static int scanNumberTMP = 0;
	private final static int PSM_ID_COL = 0;
	private final static int SEQ_COL = 1;
	private final static int RATIO_COL = 2;
	private final static int RATIO_WEIGHT_COL = 3;
	private final static int PROTEIN_ACC_COL = 4;
	private static final String RATIO_WEIGHT = "Ratio initial weigh";

	public SeparatedValuesParser(String separator) {
		super();
		this.separator = separator;
	}

	public SeparatedValuesParser(List<RemoteSSHFileReference> remoteSSHServers, String separator,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(remoteSSHServers, labelsByConditions, labelNumerator, labelDenominator);
		this.separator = separator;
	}

	public SeparatedValuesParser(String separator, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			Collection<RemoteSSHFileReference> remoteSSHServers, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(labelsByConditions, remoteSSHServers, labelNumerator, labelDenominator);
		this.separator = separator;
	}

	public SeparatedValuesParser(RemoteSSHFileReference remoteSSHServer, String separator,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(remoteSSHServer, labelsByConditions, labelNumerator, labelDenominator);
		this.separator = separator;
	}

	public SeparatedValuesParser(File xmlFile, String separator,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFile, labelsByConditions, labelNumerator, labelDenominator);
		this.separator = separator;
	}

	public SeparatedValuesParser(File[] xmlFiles, String separator,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
		this.separator = separator;
	}

	public SeparatedValuesParser(File[] xmlFiles, String separator,
			Map<QuantCondition, QuantificationLabel>[] labelsByConditions, QuantificationLabel[] labelNumerator,
			QuantificationLabel[] labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
		this.separator = separator;
	}

	public SeparatedValuesParser(Collection<File> xmlFiles, String separator,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
		this.separator = separator;
	}

	public SeparatedValuesParser(RemoteSSHFileReference remoteServer, String separator, QuantificationLabel label1,
			QuantCondition cond1, QuantificationLabel label2, QuantCondition cond2) {
		super(remoteServer, label1, cond1, label2, cond2);
		this.separator = separator;
	}

	public SeparatedValuesParser(File inputFile, String separator, QuantificationLabel label1, QuantCondition cond1,
			QuantificationLabel label2, QuantCondition cond2) throws FileNotFoundException {
		super(inputFile, label1, cond1, label2, cond2);
		this.separator = separator;
	}

	@Override
	protected void process() {
		processed = false;
		log.info("Processing file...");

		try {
			int numDecoy = 0;
			boolean someValidFile = false;
			for (RemoteSSHFileReference remoteFileRetriever : remoteFileRetrievers) {
				final Map<QuantCondition, QuantificationLabel> labelsByConditions = labelsByConditionsByFile
						.get(remoteFileRetriever);
				final Map<QuantificationLabel, QuantCondition> conditionsByLabels = new HashMap<QuantificationLabel, QuantCondition>();
				for (QuantCondition cond : labelsByConditions.keySet()) {
					conditionsByLabels.put(labelsByConditions.get(cond), cond);
				}
				QuantificationLabel labelNumerator = numeratorLabelByFile.get(remoteFileRetriever);
				QuantificationLabel labelDenominator = denominatorLabelByFile.get(remoteFileRetriever);

				String experimentKey = FilenameUtils.getBaseName(remoteFileRetriever.getOutputFile().getAbsolutePath());
				log.info(experimentKey);
				// get all the Quantified PSMs first
				// Set<QuantifiedPSMInterface> psms = new
				// HashSet<QuantifiedPSMInterface>();
				final File remoteFile = remoteFileRetriever.getRemoteFile();
				if (remoteFile == null || !remoteFile.exists())
					continue;
				log.info("Reading " + remoteFile.getAbsolutePath());
				someValidFile = true;
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(remoteFile));

					String line;

					int numLine = 0;
					while ((line = br.readLine()) != null) {
						numLine++;
						if (line.contains(separator)) {
							String psmID = null;
							String seq = null;
							Double ratio = null;
							Double ratioWeigth = null;
							String proteinAcc = null;
							final String[] split = line.split(separator);
							if (split.length > PSM_ID_COL + 1) {
								psmID = split[PSM_ID_COL];
							}
							if (split.length > SEQ_COL + 1) {
								seq = split[SEQ_COL];
							}
							if (split.length > RATIO_COL + 1) {
								try {
									ratio = Double.valueOf(split[RATIO_COL]);
								} catch (NumberFormatException e) {
									e.printStackTrace();
									throw new IllegalArgumentException("Error in line:" + numLine + ", col:" + RATIO_COL
											+ 1 + "\t" + e.getMessage());
								}
							}
							if (split.length > RATIO_WEIGHT_COL + 1) {
								try {
									ratioWeigth = Double.valueOf(split[RATIO_WEIGHT_COL]);
								} catch (NumberFormatException e) {
									e.printStackTrace();
									throw new IllegalArgumentException("Error in line:" + numLine + ", col:"
											+ RATIO_WEIGHT_COL + 1 + "\t" + e.getMessage());
								}
							}
							if (split.length > PROTEIN_ACC_COL + 1) {
								proteinAcc = split[PROTEIN_ACC_COL];
							}
							processPSMLine(psmID, seq, ratio, ratioWeigth, proteinAcc, conditionsByLabels,
									labelsByConditions, labelNumerator, labelDenominator, experimentKey,
									remoteFileRetriever);
						}

					}

					br.close();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						br.close();
					}
				}

				log.info("(" + experimentKey + ") " + localPsmMap.size() + " PSMs from this parser. "
						+ QuantStaticMaps.psmMap.size() + " PSMs in the system");
				log.info("(" + experimentKey + ") " + localProteinMap.size() + " Proteins from this parser. "
						+ QuantStaticMaps.proteinMap.size() + " Proteins in the system");
				log.info("(" + experimentKey + ") " + localPeptideMap.size() + " Peptides from this parser. "
						+ QuantStaticMaps.peptideMap.size() + " Peptides in the system");
				if (decoyPattern != null) {
					log.info(numDecoy + " decoy Proteins were discarded  in " + experimentKey);
				}

			}
			if (!someValidFile)
				throw new IllegalArgumentException("some error occurred while reading the files");

			processed = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// if (processed) {
			// // to create the peptides at the end
			// peptideMap.clear();
			// peptideMap.putAll(
			// QuantifiedPeptide.getQuantifiedPeptides(getPSMMap().values(),
			// distinguishModifiedPeptides));
			// }
		}
	}

	private void processPSMLine(String psmId, String sequence, Double ratioValue, Double ratioWeigth, String proteinACC,
			Map<QuantificationLabel, QuantCondition> conditionsByLabels,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator, String experimentKey, RemoteSSHFileReference remoteFileRetriever)
			throws IOException {

		// new psm

		// dont look into the QuantifiedPSM.map because each
		// line is always a new PSM
		String inputFileName = FilenameUtils.getName(remoteFileRetriever.getOutputFile().getAbsolutePath());
		String rawFileName = FastaParser.getFileNameFromPSMIdentifier(psmId);
		if (rawFileName == null) {
			rawFileName = inputFileName;
		}

		// scan number
		int scanNumber = 0;
		try {
			scanNumber = Integer.valueOf(FastaParser.getScanFromPSMIdentifier(psmId));
		} catch (Exception e) {
			// get next available number
			scanNumber = ++scanNumberTMP;
		}

		// charge state
		int chargeState = 0;
		try {
			chargeState = Integer.valueOf(FastaParser.getChargeStateFromPSMIdentifier(psmId));
		} catch (Exception e) {
		}
		QuantifiedPSMInterface quantifiedPSM = new QuantifiedPSMFromCensusOut(sequence, labelsByConditions,
				peptideToSpectraMap, scanNumber, chargeState, chargeStateSensible, rawFileName, false);

		quantifiedPSM.addFileName(inputFileName);
		final String psmKey = KeyUtils.getSpectrumKey(quantifiedPSM, chargeStateSensible);
		// in case of TMT, the psm may have been created before
		if (QuantStaticMaps.psmMap.containsKey(psmKey)) {
			quantifiedPSM = QuantStaticMaps.psmMap.getItem(psmKey);
		}
		QuantStaticMaps.psmMap.addItem(quantifiedPSM);

		// psms.add(quantifiedPSM);
		// add to map
		if (!localPsmMap.containsKey(quantifiedPSM.getKey())) {
			localPsmMap.put(quantifiedPSM.getKey(), quantifiedPSM);
		}
		// PSM regular ratio
		// add PSM ratios from census out
		if (ratioValue != null) {
			try {
				CensusRatio ratio = new CensusRatio(ratioValue, false, conditionsByLabels, labelNumerator,
						labelDenominator, AggregationLevel.PSM, "RATIO");
				// set singleton
				if (ratioValue == 0 || Double.compare(Double.POSITIVE_INFINITY, ratioValue) == 0) {
					if (quantifiedPSM instanceof QuantifiedPSMFromCensusOut) {
						((QuantifiedPSMFromCensusOut) quantifiedPSM).setSingleton(true);
					}
				}
				if (ratioWeigth != null) {
					RatioScore ratioScore = new RatioScore(String.valueOf(ratioWeigth), RATIO_WEIGHT,
							"PSM-level quantification confidence metric", RATIO_WEIGHT);
					ratio.setRatioScore(ratioScore);
				}

				// add ratio to PSM
				quantifiedPSM.addRatio(ratio);

			} catch (NumberFormatException e) {
				// skip this
			}
		}

		// create the peptide
		QuantifiedPeptideInterface quantifiedPeptide = null;
		final String peptideKey = KeyUtils.getSequenceChargeKey(quantifiedPSM, chargeStateSensible);
		if (QuantStaticMaps.peptideMap.containsKey(peptideKey)) {
			quantifiedPeptide = QuantStaticMaps.peptideMap.getItem(peptideKey);
		} else {
			quantifiedPeptide = new QuantifiedPeptide(quantifiedPSM, distinguishModifiedPeptides);
		}
		QuantStaticMaps.peptideMap.addItem(quantifiedPeptide);
		quantifiedPeptide.addFileName(inputFileName);
		quantifiedPSM.setQuantifiedPeptide(quantifiedPeptide);
		// add peptide to map
		if (!localPeptideMap.containsKey(peptideKey)) {
			localPeptideMap.put(peptideKey, quantifiedPeptide);
		}

		if (dbIndex != null) {
			String cleanSeq = quantifiedPSM.getSequence();
			final Set<IndexedProtein> indexedProteins = dbIndex.getProteins(cleanSeq);
			if (indexedProteins.isEmpty()) {
				throw new IllegalArgumentException("The peptide " + cleanSeq
						+ " is not found in Fasta DB.\nReview the default indexing parameters such as the number of allowed misscleavages.");
				// log.warn("The peptide " + cleanSeq +
				// " is not found in Fasta DB.");
				// continue;
			}
			// create a new Quantified Protein for each
			// indexedProtein
			for (IndexedProtein indexedProtein : indexedProteins) {
				String proteinKey = KeyUtils.getProteinKey(indexedProtein);
				QuantifiedProteinInterface quantifiedProtein = null;
				if (QuantStaticMaps.proteinMap.containsKey(proteinKey)) {
					quantifiedProtein = QuantStaticMaps.proteinMap.getItem(proteinKey);
				} else {
					quantifiedProtein = new QuantifiedProteinFromDBIndexEntry(indexedProtein);
				}
				// add psm to the proteins
				quantifiedProtein.addPSM(quantifiedPSM);
				// add protein to the psm
				quantifiedPSM.addQuantifiedProtein(quantifiedProtein);
				// add peptide to the protein
				quantifiedProtein.addPeptide(quantifiedPeptide);
				// add to the map (if it was already there
				// is not a problem, it will be only once)
				addToMap(proteinKey, proteinToPeptidesMap,
						KeyUtils.getSequenceChargeKey(quantifiedPSM, chargeStateSensible));
				// add protein to protein map
				localProteinMap.put(proteinKey, quantifiedProtein);
				// add to protein-experiment map
				addToMap(experimentKey, experimentToProteinsMap, proteinKey);

				quantifiedProtein.addFileName(inputFileName);
			}
		}
		if (proteinACC != null) {
			String proteinKey = proteinACC;
			QuantifiedProteinInterface quantifiedProtein = null;
			if (QuantStaticMaps.proteinMap.containsKey(proteinKey)) {
				quantifiedProtein = QuantStaticMaps.proteinMap.getItem(proteinKey);
			} else {
				quantifiedProtein = new QuantifiedProteinFromCensusOut(proteinKey);
			}
			// add psm to the proteins
			quantifiedProtein.addPSM(quantifiedPSM);
			// add protein to the psm
			quantifiedPSM.addQuantifiedProtein(quantifiedProtein);
			// add peptide to the protein
			quantifiedProtein.addPeptide(quantifiedPeptide);
			// add to the map (if it was already there
			// is not a problem, it will be only once)
			addToMap(proteinKey, proteinToPeptidesMap,
					KeyUtils.getSequenceChargeKey(quantifiedPSM, chargeStateSensible));
			// add protein to protein map
			localProteinMap.put(proteinKey, quantifiedProtein);
			// add to protein-experiment map
			addToMap(experimentKey, experimentToProteinsMap, proteinKey);

			quantifiedProtein.addFileName(inputFileName);
		}
	}
}
