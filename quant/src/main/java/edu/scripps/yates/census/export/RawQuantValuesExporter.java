package edu.scripps.yates.census.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.CensusChroParser;
import edu.scripps.yates.census.read.model.Ion;
import edu.scripps.yates.census.read.model.IsoRatio;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPeptide;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedProtein;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedProteinGroup;
import edu.scripps.yates.census.read.model.QuantifiedPeptide;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.dbindex.IndexedProtein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PAnalyzer;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.maths.Maths;

public class RawQuantValuesExporter {
	private static final String TAB = "\t";
	private static final String SEPARATOR = " ## ";

	private static class QuantValue {
		final double ratio;
		final double weight;

		public QuantValue(String ratio, String weigth) {
			this.ratio = Double.valueOf(ratio);
			weight = Double.valueOf(weigth);
		}
	}

	private final File quixotFileResults;

	public RawQuantValuesExporter(File quixotFileResults) {
		this.quixotFileResults = quixotFileResults;
	}

	public void printRawQuantTableValuesAtPSMLevel(BufferedWriter bw, QuantCondition conditionNumerator,
			QuantCondition conditionDenominator, CensusChroParser parser, DBIndexInterface dbIndex) throws IOException {

		printPSMHeader(bw);

		parser.setDbIndex(dbIndex);
		final Map<String, QuantifiedPSMInterface> psmMap = parser.getPSMMap();
		Map<String, QuantValue> finalPeptideQuantValues = readFinalPeptideQuantValuesFromFile();
		for (QuantifiedPSMInterface quantifiedPSM : psmMap.values()) {
			if (quantifiedPSM instanceof IsobaricQuantifiedPSM) {
				// one line per isobaric ratio
				if (quantifiedPSM instanceof IsobaricQuantifiedPSM) {
					IsobaricQuantifiedPSM isoPSM = (IsobaricQuantifiedPSM) quantifiedPSM;
					final Set<IsoRatio> ratios = isoPSM.getNonInfinityIsoRatios();
					if (!ratios.isEmpty()) {
						for (IsoRatio ratio : ratios) {
							printStaticQuantInfo(conditionNumerator, conditionDenominator, isoPSM,
									finalPeptideQuantValues, dbIndex, bw);

							bw.write(ratio.getLog2Ratio(conditionNumerator, conditionDenominator) + TAB);

							bw.write(ratio.getIonSerieType().name() + TAB);
							bw.write(ratio.getNumIon() + TAB);
							bw.write(ratio.getMass(conditionNumerator) + TAB);
							bw.write(ratio.getMass(conditionDenominator) + TAB);
							bw.write(ratio.getIntensity(conditionNumerator) + TAB);
							bw.write(ratio.getIntensity(conditionDenominator) + TAB);
							bw.write("\n");
						}
					} else {
						printStaticQuantInfo(conditionNumerator, conditionDenominator, isoPSM, finalPeptideQuantValues,
								dbIndex, bw);
						bw.write("\n");
					}
				}
			} else {
				throw new IllegalArgumentException(quantifiedPSM.getClass().getName() + " is not supported yet");
			}
		}

	}

	private void printProteinGroupHeader(BufferedWriter bw) throws IOException {
		bw.write("Experiment" + TAB + "DmDv" + TAB + "DvDm" + TAB + "Accessions" + TAB + "Gene names" + TAB
				+ "descriptions" + TAB + "DROME" + TAB + "DROVI" + TAB + "BOTH" + TAB + "Taxonomies" + TAB + "# PSMs"
				+ TAB + "# sequences" + TAB + "# unique peptides" + TAB + "protein evidence" + TAB + "# proteins" + TAB
				+ "max peak" + TAB + "# quant peptides" + TAB + "FINAL RATIO MEAN" + TAB + "FINAL RATIO STDEV" + TAB
				+ "FINAL WEIGHT MEAN" + TAB + "FINAL WEIGHT STDEV" + TAB + "# isobaric ratios" + TAB
				+ "mean isob ratios" + TAB + "std isob ratios" + TAB + "# singleton LIGHT" + TAB + "# singleton HEAVY"
				+ TAB + "singleton count ratio" + TAB + "# ions LIGHT" + TAB + "# ions HEAVY" + TAB + "count ratio");
		bw.write("\n");
	}

	private void printProteinHeader(BufferedWriter bw) throws IOException {
		bw.write("Experiment" + TAB + "DmDv" + TAB + "DvDm" + TAB + "Accession" + TAB + "Gene name" + TAB
				+ "description" + TAB + "DROME" + TAB + "DROVI" + TAB + "Taxonomy" + TAB + "# PSMs" + TAB
				+ "# sequences" + TAB + "# unique peptides" + TAB + "protein evidence" + TAB + "seq. length" + TAB
				+ "max peak" + TAB + "# quant peptides" + TAB + "FINAL RATIO MEAN" + TAB + "FINAL RATIO STDEV" + TAB
				+ "FINAL WEIGHT MEAN" + TAB + "FINAL WEIGHT STDEV" + TAB + "# isobaric ratios" + TAB
				+ "mean isob ratios" + TAB + "std isob ratios" + TAB + "# singleton LIGHT" + TAB + "# singleton HEAVY"
				+ TAB + "singleton count ratio" + TAB + "# ions LIGHT" + TAB + "# ions HEAVY" + TAB + "count ratio");
		bw.write("\n");
	}

	private void printPeptideHeader(BufferedWriter bw) throws IOException {
		bw.write("Experiment" + TAB + "DmDv" + TAB + "DvDm" + TAB + "DROME" + TAB + "DROVI" + TAB + "BOTH" + TAB
				+ "Taxonomies" + TAB + "Accessions" + TAB + "Protein names" + TAB + "Gene names" + TAB + "# proteins"
				+ TAB + "unique" + TAB + "# PSMs" + TAB + "seq. length" + TAB + "sequence" + TAB + "Theor M+H" + TAB
				+ "max peak" + TAB + "FINAL RATIO" + TAB + "FINAL WEIGHT" + TAB + "# isobaric ratios" + TAB
				+ "mean isob ratios" + TAB + "std isob ratios" + TAB + "# singleton LIGHT" + TAB + "# singleton HEAVY"
				+ TAB + "singleton count ratio" + TAB + "# ions LIGHT" + TAB + "# ions HEAVY" + TAB + "count ratio");
		bw.write("\n");
	}

	private void printPSMHeader(BufferedWriter bw) throws IOException {
		bw.write("Experiment" + TAB + "DmDv" + TAB + "DvDm" + TAB + "Scan#" + TAB + "psmID" + TAB + "DROME" + TAB
				+ "DROVI" + TAB + "BOTH" + TAB + "Taxonomies" + TAB + "Accessions" + TAB + "Protein names" + TAB
				+ "Gene names" + TAB + "# proteins" + TAB + "unique" + TAB + "seq. length" + TAB + "sequence" + TAB
				+ "charge" + TAB + "M+H" + TAB + "Theor M+H" + TAB + "max peak" + TAB + "FINAL RATIO" + TAB
				+ "FINAL WEIGHT" + TAB + "# isobaric ratios" + TAB + "mean isob ratios" + TAB + "std isob ratios" + TAB
				+ "# singleton LIGHT" + TAB + "# singleton HEAVY" + TAB + "singleton count ratio" + TAB + "# ions LIGHT"
				+ TAB + "# ions HEAVY" + TAB + "count ratio" + TAB + "Isobaric ratio" + TAB + "Serie Isob ratio" + TAB
				+ "Ion Isob Ratio" + TAB + "Mass(LIGHT)" + TAB + "Mass(HEAVY)" + TAB + "Iso_ratio_Intensity1" + TAB
				+ "Iso_ratio_Intensity2");
		bw.write("\n");
	}

	public void printRawQuantTableValuesAtProteinLevel(BufferedWriter bw, QuantCondition conditionNumerator,
			QuantCondition conditionDenominator, CensusChroParser parser, DBIndexInterface dbIndex) throws IOException {

		printProteinHeader(bw);
		final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();
		PAnalyzer panalyzer = new PAnalyzer(false);
		Set<GroupableProtein> proteins = new HashSet<GroupableProtein>();
		proteins.addAll(proteinMap.values());
		panalyzer.run(proteins);
		Map<String, QuantValue> finalPeptideQuantValues = readFinalPeptideQuantValuesFromFile();
		for (String proteinAcc : proteinMap.keySet()) {
			QuantifiedProteinInterface quantifiedProtein = proteinMap.get(proteinAcc);
			if (quantifiedProtein instanceof IsobaricQuantifiedProtein) {
				printStaticQuantInfo(conditionNumerator, conditionDenominator,
						(IsobaricQuantifiedProtein) quantifiedProtein, finalPeptideQuantValues, dbIndex, bw);
			} else {
				throw new IllegalArgumentException(quantifiedProtein.getClass().getName() + " is not supported yet");
			}
			bw.write("\n");
		}
	}

	public void printRawQuantTableValuesAtProteinGroupLevel(BufferedWriter bw, QuantCondition conditionNumerator,
			QuantCondition conditionDenominator, CensusChroParser parser, DBIndexInterface dbIndex) throws IOException {

		printProteinGroupHeader(bw);
		final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();
		PAnalyzer panalyzer = new PAnalyzer(false);
		final Collection<QuantifiedProteinInterface> quantProteins = proteinMap.values();
		Set<GroupableProtein> groupableProteins = new HashSet<GroupableProtein>();
		for (GroupableProtein quantProtein : quantProteins) {
			groupableProteins.add(quantProtein);
		}
		final List<ProteinGroup> proteinGroups = panalyzer.run(groupableProteins);

		Map<String, QuantValue> finalPeptideQuantValues = readFinalPeptideQuantValuesFromFile();
		for (ProteinGroup proteinGroup : proteinGroups) {
			printStaticQuantInfo(conditionNumerator, conditionDenominator,
					new IsobaricQuantifiedProteinGroup(proteinGroup), finalPeptideQuantValues, dbIndex, bw);
			bw.write("\n");
		}
	}

	public void printRawQuantTableValuesAtPeptideLevel(BufferedWriter bw, QuantCondition conditionNumerator,
			QuantCondition conditionDenominator, CensusChroParser parser, DBIndexInterface dbIndex) throws IOException {

		printPeptideHeader(bw);
		final Map<String, QuantifiedPeptideInterface> peptideMap = parser.getPeptideMap();
		Map<String, QuantValue> finalPeptideQuantValues = readFinalPeptideQuantValuesFromFile();
		for (String sequence : peptideMap.keySet()) {
			QuantifiedPeptideInterface quantifiedPeptide = peptideMap.get(sequence);
			if (quantifiedPeptide instanceof IsobaricQuantifiedPeptide) {
				printStaticQuantInfo(conditionNumerator, conditionDenominator,
						(IsobaricQuantifiedPeptide) quantifiedPeptide, finalPeptideQuantValues, dbIndex, bw);
			} else {
				throw new IllegalArgumentException(quantifiedPeptide.getClass().getName() + " is not supported yet");
			}
			bw.write("\n");

		}

	}

	private void printStaticQuantInfo(QuantCondition conditionNumerator, QuantCondition conditionDenominator,
			IsobaricQuantifiedPSM quantifiedPSM, Map<String, QuantValue> finalPeptideQuantValues,
			DBIndexInterface dbIndex, BufferedWriter bw) throws IOException {
		// experiment name
		boolean dmdv = false;
		boolean dvdm = false;

		final String fileName = quantifiedPSM.getFileName();
		if (fileName.contains("DmDv"))
			dmdv = true;
		if (fileName.contains("DvDm"))
			dvdm = true;
		bw.write(fileName + TAB);

		// dmDv
		bw.write(dmdv ? "1" : "0");
		bw.write(TAB);
		// dmDv
		bw.write(dvdm ? "1" : "0");
		bw.write(TAB);

		// scan #
		bw.write(quantifiedPSM.getScan() + TAB);
		// psmID
		bw.write(quantifiedPSM.getPSMIdentifier() + TAB);

		// Taxonomy
		String peptide = FastaParser.cleanSequence(quantifiedPSM.getSequence());
		final Set<IndexedProtein> proteinSet = dbIndex.getProteins(peptide);
		List<IndexedProtein> proteins = new ArrayList<IndexedProtein>();
		Set<String> accessions = new HashSet<String>();
		for (IndexedProtein indexedProtein : proteinSet) {
			if (accessions.contains(indexedProtein.getAccession()))
				continue;
			accessions.add(indexedProtein.getAccession());
			proteins.add(indexedProtein);
		}

		Collections.sort(proteins, new Comparator<IndexedProtein>() {

			@Override
			public int compare(IndexedProtein o1, IndexedProtein o2) {
				final String accession1 = FastaParser.getACC(o1.getAccession()).getFirstelement();
				final String accession2 = FastaParser.getACC(o2.getAccession()).getFirstelement();
				return accession1.compareTo(accession2);
			}
		});
		boolean drome = false;
		boolean drovi = false;
		String DROME = "DROME";
		String DROVI = "DROVI";
		for (IndexedProtein indexedProtein : proteins) {
			final String fastaDefLine = indexedProtein.getFastaDefLine();
			if (fastaDefLine.contains(DROME)) {
				drome = true;
			} else if (fastaDefLine.contains(DROVI)) {
				drovi = true;
			} else {
				throw new IllegalArgumentException("only drome or drovi are supported");
			}
		}
		// DROME
		bw.write(drome ? "1" : "0");
		bw.write(TAB);
		// DROVI
		bw.write(drovi ? "1" : "0");
		bw.write(TAB);
		// BOTH
		bw.write(drome && drovi ? "1" : "0");
		bw.write(TAB);
		// taxonomy names
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getOrganismNameFromFastaHeader(indexedProtein.getFastaDefLine(),
					indexedProtein.getAccession()) + SEPARATOR);
		}
		bw.write(TAB);
		// accessions
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getACC(indexedProtein.getAccession()).getFirstelement() + SEPARATOR);
		}
		bw.write(TAB);
		// protein descriptions
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getDescription(indexedProtein.getFastaDefLine()) + SEPARATOR);
		}
		bw.write(TAB);
		// gene name
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getGeneFromFastaHeader(indexedProtein.getFastaDefLine()) + SEPARATOR);
		}
		bw.write(TAB);
		// num proteins
		bw.write(proteins.size() + TAB);
		// unique
		if (proteins.size() == 1) {
			bw.write("1" + TAB);
		} else {
			bw.write("0" + TAB);
		}

		// length
		bw.write(quantifiedPSM.getSequence().length() + TAB);
		// sequence
		bw.write(quantifiedPSM.getSequence() + TAB);
		if (quantifiedPSM instanceof QuantifiedPSMInterface) {
			// charge
			bw.write(((QuantifiedPSMInterface) quantifiedPSM).getCharge() + TAB);
			// M+H
			bw.write(((QuantifiedPSMInterface) quantifiedPSM).getMHplus() + TAB);
		}
		// Theor M+H
		bw.write(quantifiedPSM.getCalcMHplus() + TAB);
		// max peak
		bw.write(quantifiedPSM.getMaxPeak() + TAB);

		if (finalPeptideQuantValues.containsKey(quantifiedPSM.getSequence())) {
			// final quant value
			bw.write(finalPeptideQuantValues.get(quantifiedPSM.getSequence()).ratio + TAB);
			// final quant weight
			bw.write(finalPeptideQuantValues.get(quantifiedPSM.getSequence()).weight + TAB);
		} else {
			// final quant value
			bw.write("-" + TAB);
			// final quant weight
			bw.write("-" + TAB);
		}
		// # isob ratios
		bw.write(quantifiedPSM.getNonInfinityIsoRatios().size() + TAB);

		// mean isob ratios
		bw.write(quantifiedPSM.getMeanRatios(conditionNumerator, conditionDenominator) + TAB);

		// STDEV isob ratios
		bw.write(quantifiedPSM.getSTDRatios(conditionNumerator, conditionDenominator) + TAB);

		final Map<QuantCondition, Set<Ion>> singletonIons = quantifiedPSM.getSingletonIonsByCondition();
		// # singleton Light
		int singletonNumerator = 0;
		if (singletonIons.containsKey(conditionNumerator)) {
			singletonNumerator = singletonIons.get(conditionNumerator).size();
		}
		// # light
		int numNumeratorIons = 0;
		if (quantifiedPSM.getIonsByCondition().containsKey(conditionNumerator)) {
			numNumeratorIons = quantifiedPSM.getIonsByCondition().get(conditionNumerator).size();
		}
		// # singleton Heavy
		int singletonDenominator = 0;
		if (singletonIons.containsKey(conditionDenominator)) {
			singletonDenominator = singletonIons.get(conditionDenominator).size();
		}
		// # heavy
		int numDenominatorIons = 0;
		if (quantifiedPSM.getIonsByCondition().containsKey(conditionDenominator)) {
			numDenominatorIons = quantifiedPSM.getIonsByCondition().get(conditionDenominator).size();
		}

		bw.write(singletonNumerator + TAB);
		bw.write(singletonDenominator + TAB);
		// singleton count ratio
		bw.write(getCountRatioString(singletonNumerator, singletonDenominator) + TAB);
		bw.write(numNumeratorIons + TAB);
		bw.write(numDenominatorIons + TAB);
		// ion count ratio
		bw.write(getCountRatioString(numNumeratorIons, numDenominatorIons) + TAB);

	}

	private void printStaticQuantInfo(QuantCondition conditionNumerator, QuantCondition conditionDenominator,
			IsobaricQuantifiedPeptide quantifiedPeptide, Map<String, QuantValue> finalPeptideQuantValues,
			DBIndexInterface dbIndex, BufferedWriter bw) throws IOException {
		// experiment name
		boolean dmdv = false;
		boolean dvdm = false;

		final Set<String> fileNames = quantifiedPeptide.getRawFileNames();
		List<String> fileNamesSorted = getSorted(fileNames);
		for (String fileName : fileNamesSorted) {
			if (fileName.contains("DmDv"))
				dmdv = true;
			if (fileName.contains("DvDm"))
				dvdm = true;
			bw.write(fileName + ",");
		}
		bw.write(TAB);

		// dmDv
		bw.write(dmdv ? "1" : "0");
		bw.write(TAB);
		// dmDv
		bw.write(dvdm ? "1" : "0");
		bw.write(TAB);

		// Taxonomy
		String peptide = FastaParser.cleanSequence(quantifiedPeptide.getSequence());
		final Set<IndexedProtein> proteinSet = dbIndex.getProteins(peptide);
		List<IndexedProtein> proteins = new ArrayList<IndexedProtein>();
		Set<String> accessions = new HashSet<String>();
		for (IndexedProtein indexedProtein : proteinSet) {
			if (accessions.contains(indexedProtein.getAccession()))
				continue;
			accessions.add(indexedProtein.getAccession());
			proteins.add(indexedProtein);
		}

		Collections.sort(proteins, new Comparator<IndexedProtein>() {

			@Override
			public int compare(IndexedProtein o1, IndexedProtein o2) {
				final String accession1 = FastaParser.getACC(o1.getAccession()).getFirstelement();
				final String accession2 = FastaParser.getACC(o2.getAccession()).getFirstelement();
				return accession1.compareTo(accession2);
			}
		});
		boolean drome = false;
		boolean drovi = false;
		String DROME = "DROME";
		String DROVI = "DROVI";
		for (IndexedProtein indexedProtein : proteins) {
			final String fastaDefLine = indexedProtein.getFastaDefLine();
			if (fastaDefLine.contains(DROME)) {
				drome = true;
			} else if (fastaDefLine.contains(DROVI)) {
				drovi = true;
			} else {
				throw new IllegalArgumentException("only drome or drovi are supported");

			}
		}
		// DROME
		bw.write(drome ? "1" : "0");
		bw.write(TAB);
		// DROVI
		bw.write(drovi ? "1" : "0");
		bw.write(TAB);
		// BOTH
		bw.write(drome && drovi ? "1" : "0");
		bw.write(TAB);
		// taxonomy names
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getOrganismNameFromFastaHeader(indexedProtein.getFastaDefLine(),
					indexedProtein.getAccession()) + SEPARATOR);
		}
		bw.write(TAB);
		// accessions
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getACC(indexedProtein.getAccession()).getFirstelement() + SEPARATOR);
		}
		bw.write(TAB);
		// protein descriptions
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getDescription(indexedProtein.getFastaDefLine()) + SEPARATOR);
		}
		bw.write(TAB);
		// gene name
		for (IndexedProtein indexedProtein : proteins) {
			bw.write(FastaParser.getGeneFromFastaHeader(indexedProtein.getFastaDefLine()) + SEPARATOR);
		}
		bw.write(TAB);
		// num proteins
		bw.write(proteins.size() + TAB);
		// unique
		if (proteins.size() == 1) {
			bw.write("1" + TAB);
		} else {
			bw.write("0" + TAB);
		}
		// num psms
		if (quantifiedPeptide instanceof IsobaricQuantifiedPeptide) {
			bw.write(quantifiedPeptide.getQuantifiedPSMs().size() + TAB);
		}
		// length
		bw.write(quantifiedPeptide.getSequence().length() + TAB);
		// sequence
		bw.write(quantifiedPeptide.getSequence() + TAB);
		if (quantifiedPeptide instanceof QuantifiedPSMInterface) {
			// charge
			bw.write(((QuantifiedPSMInterface) quantifiedPeptide).getCharge() + TAB);
			// M+H
			bw.write(((QuantifiedPSMInterface) quantifiedPeptide).getMHplus() + TAB);
		}
		// Theor M+H
		bw.write(quantifiedPeptide.getCalcMHplus() + TAB);
		// max peak
		bw.write(quantifiedPeptide.getMaxPeak() + TAB);

		if (finalPeptideQuantValues.containsKey(quantifiedPeptide.getSequence())) {
			// final quant value
			bw.write(finalPeptideQuantValues.get(quantifiedPeptide.getSequence()).ratio + TAB);
			// final quant weight
			bw.write(finalPeptideQuantValues.get(quantifiedPeptide.getSequence()).weight + TAB);
		} else {
			// final quant value
			bw.write("-" + TAB);
			// final quant weight
			bw.write("-" + TAB);
		}
		// # isob ratios
		bw.write(quantifiedPeptide.getNonInfinityIsoRatios().size() + TAB);

		// mean isob ratios
		bw.write(quantifiedPeptide.getMeanRatios(conditionNumerator, conditionDenominator) + TAB);

		// STDEV isob ratios
		bw.write(quantifiedPeptide.getSTDRatios(conditionNumerator, conditionDenominator) + TAB);

		final Map<QuantCondition, Set<Ion>> singletonIons = quantifiedPeptide.getSingletonIonsByCondition();
		// # singleton Light
		int singletonNumerator = 0;
		if (singletonIons.containsKey(conditionNumerator)) {
			singletonNumerator = singletonIons.get(conditionNumerator).size();
		}
		// # light
		int numNumeratorIons = 0;
		if (quantifiedPeptide.getIonsByCondition().containsKey(conditionNumerator)) {
			numNumeratorIons = quantifiedPeptide.getIonsByCondition().get(conditionNumerator).size();
		}
		// # singleton Heavy
		int singletonDenominator = 0;
		if (singletonIons.containsKey(conditionDenominator)) {
			singletonDenominator = singletonIons.get(conditionDenominator).size();
		}
		// # heavy
		int numDenominatorIons = 0;
		if (quantifiedPeptide.getIonsByCondition().containsKey(conditionDenominator)) {
			numDenominatorIons = quantifiedPeptide.getIonsByCondition().get(conditionDenominator).size();
		}

		bw.write(singletonNumerator + TAB);
		bw.write(singletonDenominator + TAB);
		// singleton count ratio
		bw.write(getCountRatioString(singletonNumerator, singletonDenominator) + TAB);
		bw.write(numNumeratorIons + TAB);
		bw.write(numDenominatorIons + TAB);
		// ion count ratio
		bw.write(getCountRatioString(numNumeratorIons, numDenominatorIons) + TAB);

	}

	private List<String> getSorted(Set<String> fileNames) {
		List<String> list = new ArrayList<String>();
		list.addAll(fileNames);
		Collections.sort(list);
		return list;
	}

	private void printStaticQuantInfo(QuantCondition conditionNumerator, QuantCondition conditionDenominator,
			IsobaricQuantifiedProtein quantifiedProtein, Map<String, QuantValue> finalPeptideQuantValues,
			DBIndexInterface dbIndex, BufferedWriter bw) throws IOException {
		// experiment name
		boolean dmdv = false;
		boolean dvdm = false;
		final Set<String> fileNames = quantifiedProtein.getFileNames();
		List<String> fileNamesSorted = getSorted(fileNames);
		for (String fileName : fileNamesSorted) {
			bw.write(fileName + ",");
			if (fileName.contains("DmDv"))
				dmdv = true;
			if (fileName.contains("DvDm"))
				dvdm = true;
		}
		bw.write(TAB);
		// dmDv
		bw.write(dmdv ? "1" : "0");
		bw.write(TAB);
		// dmDv
		bw.write(dvdm ? "1" : "0");
		bw.write(TAB);

		// accession
		bw.write(quantifiedProtein.getAccession() + TAB);
		// gene name
		String geneFromFastaHeader = FastaParser.getGeneFromFastaHeader(quantifiedProtein.getAccession());
		if (geneFromFastaHeader == null) {
			geneFromFastaHeader = FastaParser.getGeneFromFastaHeader(quantifiedProtein.getDescription());
		}
		bw.write(geneFromFastaHeader + TAB);
		// description
		bw.write(quantifiedProtein.getDescription() + TAB);
		// Taxonomy
		final String taxonomy = quantifiedProtein.getTaxonomy();

		boolean drome = false;
		boolean drovi = false;
		String DROME = "DROME";
		String DROVI = "DROVI";

		if (taxonomy.contains(DROME) || taxonomy.equalsIgnoreCase("Drosophila melanogaster")) {
			drome = true;
		} else if (taxonomy.contains(DROVI) || taxonomy.equalsIgnoreCase("Drosophila virilis")) {
			drovi = true;
		} else {
			throw new IllegalArgumentException("only drome or drovi are supported");

		}

		// DROME
		bw.write(drome ? "1" : "0");
		bw.write(TAB);
		// DROVI
		bw.write(drovi ? "1" : "0");
		bw.write(TAB);
		// // BOTH
		// bw.write(drome && drovi ? "1" : "0");
		// bw.write(TAB);
		// // num proteins
		// bw.write(quantifiedProtein.getGroupableProteins().size() + TAB);

		// taxonomy name
		bw.write(quantifiedProtein.getTaxonomy() + TAB);

		// num psms
		bw.write(quantifiedProtein.getQuantifiedPSMs().size() + TAB);
		// num sequences
		Map<String, QuantifiedPeptideInterface> peptides = IsobaricQuantifiedPeptide
				.getQuantifiedPeptides(quantifiedProtein.getQuantifiedPSMs());
		bw.write(peptides.size() + TAB);
		// num unique peptides
		Set<QuantifiedPSMInterface> psms = new HashSet<QuantifiedPSMInterface>();
		for (QuantifiedPSMInterface psm : quantifiedProtein.getQuantifiedPSMs()) {
			if (psm.getQuantifiedProteins().size() == 1) {
				psms.add(psm);
			}
		}
		bw.write(IsobaricQuantifiedPeptide.getQuantifiedPeptides(psms, false).size() + TAB);
		// protein group member type
		bw.write(quantifiedProtein.getEvidence() + TAB);
		// seq length
		bw.write(quantifiedProtein.getLength() + TAB);
		// max peak
		bw.write(quantifiedProtein.getMaxPeak() + TAB);

		List<Double> finalQuantValues = new ArrayList<Double>();
		List<Double> finalQuantWeigth = new ArrayList<Double>();
		final Map<String, QuantifiedPeptide> quantifiedPeptides = IsobaricQuantifiedPeptide
				.getQuantifiedPeptides(quantifiedProtein.getQuantifiedPSMs(), true);
		int numQuantifiedPeptides = 0;
		for (QuantifiedPeptide quantPeptide : quantifiedPeptides.values()) {
			if (finalPeptideQuantValues.containsKey(quantPeptide.getSequence())) {
				numQuantifiedPeptides++;
				final QuantValue quantValue = finalPeptideQuantValues.get(quantPeptide.getSequence());
				finalQuantValues.add(quantValue.ratio);
				finalQuantWeigth.add(quantValue.weight);
			} else {
				System.out.println(quantPeptide.getSequence() + " not found");
			}
		}
		// num quant peptides
		bw.write(numQuantifiedPeptides + TAB);
		// final quant value mean
		bw.write(Maths.mean(finalQuantValues.toArray(new Double[0])) + TAB);
		// final quant value stdev
		bw.write(Maths.stddev(finalQuantValues.toArray(new Double[0])) + TAB);
		// final quant weight mean
		bw.write(Maths.mean(finalQuantWeigth.toArray(new Double[0])) + TAB);
		// final weight value stdev
		bw.write(Maths.stddev(finalQuantWeigth.toArray(new Double[0])) + TAB);

		// # isob ratios
		bw.write(quantifiedProtein.getNonInfinityIsoRatios().size() + TAB);

		// mean isob ratios
		bw.write(quantifiedProtein.getMeanRatios(conditionNumerator, conditionDenominator) + TAB);

		// STDEV isob ratios
		bw.write(quantifiedProtein.getSTDRatios(conditionNumerator, conditionDenominator) + TAB);

		final Map<QuantCondition, Set<Ion>> singletonIons = quantifiedProtein.getSingletonIonsByCondition();
		// # singleton Light
		int singletonNumerator = 0;
		if (singletonIons.containsKey(conditionNumerator)) {
			singletonNumerator = singletonIons.get(conditionNumerator).size();
		}
		// # light
		int numNumeratorIons = 0;
		if (quantifiedProtein.getIonsByCondition().containsKey(conditionNumerator)) {
			numNumeratorIons = quantifiedProtein.getIonsByCondition().get(conditionNumerator).size();
		}
		// # singleton Heavy
		int singletonDenominator = 0;
		if (singletonIons.containsKey(conditionDenominator)) {
			singletonDenominator = singletonIons.get(conditionDenominator).size();
		}
		// # heavy
		int numDenominatorIons = 0;
		if (quantifiedProtein.getIonsByCondition().containsKey(conditionDenominator)) {
			numDenominatorIons = quantifiedProtein.getIonsByCondition().get(conditionDenominator).size();
		}

		bw.write(singletonNumerator + TAB);
		bw.write(singletonDenominator + TAB);
		// singleton count ratio
		bw.write(getCountRatioString(singletonNumerator, singletonDenominator) + TAB);
		bw.write(numNumeratorIons + TAB);
		bw.write(numDenominatorIons + TAB);
		// ion count ratio
		bw.write(getCountRatioString(numNumeratorIons, numDenominatorIons) + TAB);
	}

	private void printStaticQuantInfo(QuantCondition conditionNumerator, QuantCondition conditionDenominator,
			IsobaricQuantifiedProteinGroup quantifiedProteinGroup, Map<String, QuantValue> finalPeptideQuantValues,
			DBIndexInterface dbIndex, BufferedWriter bw) throws IOException {
		// experiment name
		boolean dmdv = false;
		boolean dvdm = false;
		final Set<String> fileNames = quantifiedProteinGroup.getFileNames();
		List<String> fileNamesSorted = getSorted(fileNames);
		for (String fileName : fileNamesSorted) {
			bw.write(fileName + ",");
			if (fileName.contains("DmDv"))
				dmdv = true;
			if (fileName.contains("DvDm"))
				dvdm = true;
		}
		bw.write(TAB);
		// dmDv
		bw.write(dmdv ? "1" : "0");
		bw.write(TAB);
		// dmDv
		bw.write(dvdm ? "1" : "0");
		bw.write(TAB);

		// accession
		bw.write(quantifiedProteinGroup.getAccessionString() + TAB);
		// gene name
		bw.write(quantifiedProteinGroup.getGeneNameString() + TAB);

		// description
		bw.write(quantifiedProteinGroup.getDescriptionString() + TAB);

		// Taxonomy
		final List<String> taxonomies = quantifiedProteinGroup.getTaxonomies();

		boolean drome = false;
		boolean drovi = false;
		String DROME = "DROME";
		String DROVI = "DROVI";
		for (String taxonomy : taxonomies) {
			if (taxonomy.contains(DROME) || taxonomy.equalsIgnoreCase("Drosophila melanogaster")) {
				drome = true;
			} else if (taxonomy.contains(DROVI) || taxonomy.equalsIgnoreCase("Drosophila virilis")) {
				drovi = true;
			} else {
				throw new IllegalArgumentException("only drome or drovi are supported");

			}
		}
		// DROME
		bw.write(drome ? "1" : "0");
		bw.write(TAB);
		// DROVI
		bw.write(drovi ? "1" : "0");
		bw.write(TAB);
		// // BOTH
		bw.write(drome && drovi ? "1" : "0");
		bw.write(TAB);
		// taxonomy names
		for (String taxonomy : taxonomies) {
			bw.write(taxonomy + SEPARATOR);
		}
		bw.write(TAB);
		// num psms
		bw.write(quantifiedProteinGroup.getQuantifiedPSMs().size() + TAB);
		// num sequences
		Map<String, QuantifiedPeptideInterface> peptides = IsobaricQuantifiedPeptide
				.getQuantifiedPeptides(quantifiedProteinGroup.getQuantifiedPSMs());
		bw.write(peptides.size() + TAB);
		// num unique peptides
		int numUniquePeptides = 0;
		for (String sequence : peptides.keySet()) {
			if (dbIndex.getProteins(FastaParser.cleanSequence(sequence)).size() == 1) {
				numUniquePeptides++;
			}
		}
		bw.write(numUniquePeptides + TAB);
		// protein group member type
		for (QuantifiedProteinInterface quantifiedProtein : quantifiedProteinGroup.getProteins()) {
			bw.write(quantifiedProtein.getEvidence() + SEPARATOR);
		}
		bw.write(TAB);
		// num proteins
		bw.write(quantifiedProteinGroup.size() + TAB);
		// max peak
		bw.write(quantifiedProteinGroup.getMaxPeak() + TAB);

		List<Double> finalQuantValues = new ArrayList<Double>();
		List<Double> finalQuantWeigth = new ArrayList<Double>();
		final Map<String, QuantifiedPeptideInterface> quantifiedPeptides = IsobaricQuantifiedPeptide
				.getQuantifiedPeptides(quantifiedProteinGroup.getQuantifiedPSMs());
		int numQuantifiedPeptides = 0;
		for (QuantifiedPeptideInterface quantPeptide : quantifiedPeptides.values()) {
			if (finalPeptideQuantValues.containsKey(quantPeptide.getSequence())) {
				numQuantifiedPeptides++;
				final QuantValue quantValue = finalPeptideQuantValues.get(quantPeptide.getSequence());
				finalQuantValues.add(quantValue.ratio);
				finalQuantWeigth.add(quantValue.weight);
			} else {
				System.out.println(quantPeptide.getSequence() + " not found");
			}
		}
		// num quant peptides
		bw.write(numQuantifiedPeptides + TAB);
		// final quant value mean
		bw.write(Maths.mean(finalQuantValues.toArray(new Double[0])) + TAB);
		// final quant value stdev
		bw.write(Maths.stddev(finalQuantValues.toArray(new Double[0])) + TAB);
		// final quant weight mean
		bw.write(Maths.mean(finalQuantWeigth.toArray(new Double[0])) + TAB);
		// final weight value stdev
		bw.write(Maths.stddev(finalQuantWeigth.toArray(new Double[0])) + TAB);

		// # isob ratios
		bw.write(quantifiedProteinGroup.getNonInfinityIsoRatios().size() + TAB);

		// mean isob ratios
		bw.write(quantifiedProteinGroup.getMeanRatios(conditionNumerator, conditionDenominator) + TAB);

		// STDEV isob ratios
		bw.write(quantifiedProteinGroup.getSTDRatios(conditionNumerator, conditionDenominator) + TAB);

		final Map<QuantCondition, Set<Ion>> singletonIons = quantifiedProteinGroup.getSingletonIonsByCondition();
		// # singleton Light
		int singletonNumerator = 0;
		if (singletonIons.containsKey(conditionNumerator)) {
			singletonNumerator = singletonIons.get(conditionNumerator).size();
		}
		// # light
		int numNumeratorIons = 0;
		if (quantifiedProteinGroup.getIonsByCondition().containsKey(conditionNumerator)) {
			numNumeratorIons = quantifiedProteinGroup.getIonsByCondition().get(conditionNumerator).size();
		}
		// # singleton Heavy
		int singletonDenominator = 0;
		if (singletonIons.containsKey(conditionDenominator)) {
			singletonDenominator = singletonIons.get(conditionDenominator).size();
		}
		// # heavy
		int numDenominatorIons = 0;
		if (quantifiedProteinGroup.getIonsByCondition().containsKey(conditionDenominator)) {
			numDenominatorIons = quantifiedProteinGroup.getIonsByCondition().get(conditionDenominator).size();
		}

		bw.write(singletonNumerator + TAB);
		bw.write(singletonDenominator + TAB);
		// singleton count ratio
		bw.write(getCountRatioString(singletonNumerator, singletonDenominator) + TAB);
		bw.write(numNumeratorIons + TAB);
		bw.write(numDenominatorIons + TAB);
		// ion count ratio
		bw.write(getCountRatioString(numNumeratorIons, numDenominatorIons) + TAB);

	}

	private String getCountRatioString(int singletonLight, int singletonHeavy) {
		if (singletonLight == 0 && singletonHeavy == 0)
			return "-";
		if (singletonLight != 0 && singletonHeavy != 0)
			return String
					.valueOf(Math.log(Double.valueOf(singletonLight) / Double.valueOf(singletonHeavy)) / Math.log(2));
		if (singletonLight == 0 && singletonHeavy != 0)
			return "NEGATIVE_INF";
		if (singletonHeavy == 0)
			return "POSITIVE_INF";

		return "-";
	}

	private Map<String, QuantValue> readFinalPeptideQuantValuesFromFile() throws IOException {
		Map<String, QuantValue> finalPeptideQuantValues = new HashMap<String, QuantValue>();
		if (quixotFileResults != null && quixotFileResults.exists()) {
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(quixotFileResults));
				String line = null;
				int numLine = 0;
				while ((line = br.readLine()) != null) {
					numLine++;
					if (numLine == 1)
						continue;
					final String[] split = line.split("\t");
					finalPeptideQuantValues.put(FastaParser.cleanSequence(split[2]),
							new QuantValue(split[0], split[1]));

				}
			} finally {
				if (br != null) {
					br.close();
				}
			}
		}
		return finalPeptideQuantValues;
	}
}
