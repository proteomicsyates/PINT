package edu.scripps.yates.census.analysis.clustering;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.CensusChroParser;
import edu.scripps.yates.census.read.model.Ion;
import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPeptide;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.io.DBIndexSearchParamsImpl;
import edu.scripps.yates.utilities.alignment.nwalign.NWAlign;
import edu.scripps.yates.utilities.alignment.nwalign.NWResult;

public class ProteinClusterUtils {
	private final static Logger log = Logger.getLogger(ProteinClusterUtils.class);

	public static Map<String, Set<NWResult>> alignPeptides(Collection<QuantifiedPeptideInterface> peptideCollection,
			int minAlignmentScore, double minPercentajeOfsmilirarity, int minConsecutiveLength) {
		Set<NWResult> gAS = new HashSet<NWResult>();
		Map<String, Set<NWResult>> gAM = new HashMap<String, Set<NWResult>>();

		List<QuantifiedPeptideInterface> peptideList = new ArrayList<QuantifiedPeptideInterface>();
		peptideList.addAll(peptideCollection);
		log.info("Aligning " + peptideList.size() + " with parameters: " + minAlignmentScore + ","
				+ minConsecutiveLength + "," + minPercentajeOfsmilirarity);
		for (int i = 0; i < peptideList.size(); i++) {
			QuantifiedPeptideInterface pep1 = peptideList.get(i);

			for (int j = i + 1; j < peptideList.size(); j++) {
				QuantifiedPeptideInterface pep2 = peptideList.get(j);
				NWResult pepResults = NWAlign.needlemanWunsch(pep1.getSequence(), pep2.getSequence(), -11, -1);
				if ((pepResults.getFinalAlignmentScore() >= minAlignmentScore
						&& pepResults.getSequenceIdentity() >= minPercentajeOfsmilirarity)
						&& pepResults.getMaxConsecutiveIdenticalAlignment() >= minConsecutiveLength) {
					// pepResults call to function to put result in map
					// pass map, result, call on both pep1
					gAM = putAlignmentResultInMap(gAM, pepResults, gAS, pep1);
					gAM = putAlignmentResultInMap(gAM, pepResults, gAS, pep2);
				}
			}
		}
		log.info("Alignment done.");
		log.info(gAM.size() + " alignments passed the thresholds");
		return gAM;

	}

	public static Map<String, Set<NWResult>> putAlignmentResultInMap(Map<String, Set<NWResult>> gAM, NWResult result,
			Set<NWResult> gAS, QuantifiedPeptideInterface pep) {
		String seq = pep.getSequence();

		// if gAM has the sequence, use that sequence to get the result
		if ((gAM.containsKey(seq))) {
			gAM.get(seq).add(result);
		}
		// if gAM does not have the sequence, make a new set of results, add the
		// results to this set, and then put the sequence and the set in gAM
		else {
			Set<NWResult> set = new HashSet<NWResult>();
			set.add(result);
			gAM.put(seq, set);
		}

		// return to alignPeptides
		return gAM;
	}

	public static ProteinCluster mergeClusters(ProteinCluster clusterReceiver, ProteinCluster clusterDonor) {
		log.info("Merging clusters...");
		for (QuantifiedPeptideInterface peptide : clusterDonor.getPeptideSet()) {
			clusterReceiver.addPeptide(peptide);
		}

		for (QuantifiedProteinInterface protein : clusterDonor.getProteinSet()) {
			clusterReceiver.addProtein(protein);
		}
		return clusterReceiver;
	}

	public static CensusChroParser getCensusChroParser(String fastaName, List<String> filePaths,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, QuantificationLabel numeratorLabel,
			QuantificationLabel denominatorLabel) throws FileNotFoundException {

		int i = 0;
		CensusChroParser parser = new CensusChroParser();
		for (String filePath : filePaths) {
			parser.addFile(new File(filePath), labelsByConditions.get(i), numeratorLabel, denominatorLabel);
			i++;
		}

		parser.addIonExclusion(IonSerieType.B, 1);
		parser.addIonExclusion(IonSerieType.Y, 1);

		File fastaFile = new File(fastaName);

		DBIndexSearchParams defaultDBIndexParams = DBIndexInterface.getDefaultDBIndexParams(fastaFile);
		char[] enzymeArray = { 'K' };
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setEnzymeArr(enzymeArray, 2, false);
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setEnzymeOffset(0);
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setEnzymeNocutResidues("");
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setH2OPlusProtonAdded(true);
		DBIndexInterface dbIndex = new DBIndexInterface(defaultDBIndexParams);
		parser.setDbIndex(dbIndex);
		// gets rid of decoys
		parser.setDecoyPattern("Reverse");

		return parser;
	}

	public static CensusChroParser getCensusChroParser(DBIndexSearchParams dbIndexParams, List<String> filePaths,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, QuantificationLabel numeratorLabel,
			QuantificationLabel denominatorLabel) throws FileNotFoundException {

		int i = 0;
		CensusChroParser parser = new CensusChroParser();
		for (String filePath : filePaths) {
			parser.addFile(new File(filePath), labelsByConditions.get(i), numeratorLabel, denominatorLabel);
			i++;
		}

		parser.addIonExclusion(IonSerieType.B, 1);
		parser.addIonExclusion(IonSerieType.Y, 1);

		DBIndexInterface dbIndex = new DBIndexInterface(dbIndexParams);
		parser.setDbIndex(dbIndex);
		// gets rid of decoys
		parser.setDecoyPattern("Reverse");

		return parser;
	}

	public static CensusChroParser getCensusChroParser(String fastaName, List<String> filePaths,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, List<QuantificationLabel> numeratorLabel,
			List<QuantificationLabel> denominatorLabel) throws FileNotFoundException {
		// Set parser (6 files) to peptides

		int i = 0;
		CensusChroParser parser = new CensusChroParser();
		for (String filePath : filePaths) {
			parser.addFile(new File(filePath), labelsByConditions.get(i), numeratorLabel.get(i),
					denominatorLabel.get(i));
			i++;
		}

		parser.addIonExclusion(IonSerieType.B, 1);
		parser.addIonExclusion(IonSerieType.Y, 1);

		File fastaFile = new File(fastaName);

		DBIndexSearchParams defaultDBIndexParams = DBIndexInterface.getDefaultDBIndexParams(fastaFile);
		char[] enzymeArray = { 'K' };
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setEnzymeArr(enzymeArray, 2, false);
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setEnzymeOffset(0);
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setEnzymeNocutResidues("");
		((DBIndexSearchParamsImpl) defaultDBIndexParams).setH2OPlusProtonAdded(true);
		DBIndexInterface dbIndex = new DBIndexInterface(defaultDBIndexParams);
		parser.setDbIndex(dbIndex);
		// gets rid of decoys
		parser.setDecoyPattern("Reverse");

		return parser;
	}

	public static double getCountRatio(IsobaricQuantifiedPeptide peptide, QuantCondition cond1, QuantCondition cond2) {
		Set<Ion> ions1 = peptide.getIonsByCondition().get(cond1);
		int numIons1 = 0;
		if (ions1 != null) {
			numIons1 = ions1.size();
		}
		Set<Ion> ions2 = peptide.getIonsByCondition().get(cond2);
		int numIons2 = 0;
		if (ions2 != null) {
			numIons2 = ions2.size();
		}
		if (numIons1 == 0 && numIons2 != 0) {
			return Double.NEGATIVE_INFINITY;
		}
		if (numIons1 != 0 && numIons2 == 0) {
			return Double.POSITIVE_INFINITY;
		}
		if (numIons1 == 0 && numIons2 == 0) {
			return Double.NaN;
		}
		return Math.log(1.0 * numIons1 / numIons2) / Math.log(2);

	}

	public static Set<ProteinCluster> getProteinClusters(Map<String, QuantifiedPeptideInterface> peptideMap,
			int minAlignmentScore, double minPercentajeOfsmilirarity, int minConsecutiveLength) {
		log.info("Creating clusters by similarity");

		// align all peptides
		log.info("Aligning " + peptideMap.size() + " peptides");
		Map<String, Set<NWResult>> gAM = ProteinClusterUtils.alignPeptides(peptideMap.values(), minAlignmentScore,
				minPercentajeOfsmilirarity, minConsecutiveLength);
		log.info(gAM.size() + " peptides aligned");
		// cluster set
		Set<ProteinCluster> clusterSet = new HashSet<ProteinCluster>();
		// map clusters by peptide sequence
		Map<QuantifiedPeptideInterface, ProteinCluster> clustersByPeptides = new HashMap<QuantifiedPeptideInterface, ProteinCluster>();

		// iterate over peptides
		int numPeptides = 0;
		for (QuantifiedPeptideInterface peptide1 : peptideMap.values()) {
			log.info(numPeptides++ + "/" + peptideMap.values().size() + " peptides. " + clusterSet.size()
					+ " clusters.");
			String sequence1 = peptide1.getSequence();

			ProteinCluster proteinCluster = null;
			// if there was already a cluster associated with that peptide, take
			// it.
			if (clustersByPeptides.containsKey(peptide1)) {
				proteinCluster = clustersByPeptides.get(peptide1);
			} else {
				// otherwise, create a new proteinCluster
				proteinCluster = new ProteinCluster();
				clusterSet.add(proteinCluster);

			}
			// add the peptide anyway.
			proteinCluster.addPeptide(peptide1);
			clustersByPeptides.put(peptide1, proteinCluster);

			// get proteins of the peptide
			Set<QuantifiedProteinInterface> proteinSet = peptide1.getQuantifiedProteins();

			for (QuantifiedProteinInterface protein : proteinSet) {

				// put protein in cluster
				proteinCluster.addProtein(protein);

				// peptide 2 <- protein
				Set<QuantifiedPeptideInterface> peptides = protein.getQuantifiedPeptides();

				for (QuantifiedPeptideInterface peptide2 : peptides) {

					// check the if peptide2 has already a cluster and merge it
					// if different
					if (clustersByPeptides.containsKey(peptide2)) {
						ProteinCluster proteinCluster2 = clustersByPeptides.get(peptide2);
						if (proteinCluster2 != proteinCluster) {
							clusterSet.remove(proteinCluster2);
							ProteinClusterUtils.mergeClusters(proteinCluster, proteinCluster2);
							for (QuantifiedPeptideInterface quantifiedPeptide : proteinCluster.getPeptideSet()) {
								clustersByPeptides.put(quantifiedPeptide, proteinCluster);
							}
						}
					}

					// add new peptides to cluster
					proteinCluster.addPeptide(peptide2);

					// Map <- peptide, cluster
					clustersByPeptides.put(peptide2, proteinCluster);
				}
			}

			// check if the peptide has been aligned with other peptide
			// in that case,
			if (gAM.containsKey(sequence1)) {
				Set<NWResult> goodAlignments = gAM.get(sequence1);
				for (NWResult goodAlignment : goodAlignments) {

					String sequence2 = goodAlignment.getSeq1();
					if (sequence2.equals(sequence1)) {
						sequence2 = goodAlignment.getSeq2();
					}
					// check the if peptide2 has already a cluster and merge it
					// if different
					QuantifiedPeptideInterface peptide2 = peptideMap.get(sequence2);
					if (clustersByPeptides.containsKey(peptide2)) {
						ProteinCluster proteinCluster2 = clustersByPeptides.get(peptide2);
						if (proteinCluster2 != proteinCluster) {
							clusterSet.remove(proteinCluster2);
							ProteinClusterUtils.mergeClusters(proteinCluster, proteinCluster2);
							for (QuantifiedPeptideInterface quantifiedPeptide : proteinCluster.getPeptideSet()) {
								clustersByPeptides.put(quantifiedPeptide, proteinCluster);
							}
						}
					}
					proteinCluster.addPeptide(peptide2);
					clustersByPeptides.put(peptide2, proteinCluster);

				}
			}

		}
		log.info(clusterSet.size() + " protein clusters created");

		Set<String> set = new HashSet<String>();
		List<ProteinCluster> list = new ArrayList<ProteinCluster>();
		list.addAll(clusterSet);
		Collections.sort(list, new Comparator<ProteinCluster>() {

			@Override
			public int compare(ProteinCluster o1, ProteinCluster o2) {
				return o1.getProteinClusterKey().compareTo(o2.getProteinClusterKey());
			}
		});
		for (ProteinCluster proteinCluster2 : list) {
			if (set.contains(proteinCluster2.getProteinClusterKey())) {
				throw new IllegalArgumentException("Two clusters cannot have the same proteins!");
			}
			set.add(proteinCluster2.getProteinClusterKey());
		}
		return clusterSet;

	}

}
