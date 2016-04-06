package edu.scripps.yates.census.read.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPeptide;
import edu.scripps.yates.census.read.model.QuantifiedPeptide;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.utilities.fasta.FastaParser;

public class QuantUtil {
	public static void addToPeptideMap(QuantifiedPSMInterface quantifiedPSM, Map<String, QuantifiedPeptide> map,
			boolean distinguishModifiedPeptides) {
		final String sequenceKey = getSequenceKey(quantifiedPSM, distinguishModifiedPeptides);
		QuantifiedPeptide quantifiedPeptide = null;
		if (map.containsKey(sequenceKey)) {
			quantifiedPeptide = map.get(sequenceKey);
			quantifiedPeptide.addPSM(quantifiedPSM);
		} else {
			quantifiedPeptide = new QuantifiedPeptide(quantifiedPSM, distinguishModifiedPeptides);
			map.put(sequenceKey, quantifiedPeptide);
		}

		quantifiedPSM.setQuantifiedPeptide(quantifiedPeptide);
	}

	public static void addToIsobaricPeptideMap(IsobaricQuantifiedPSM quantifiedPSM,
			Map<String, IsobaricQuantifiedPeptide> map, boolean distinguishModifiedPeptides) {
		final String sequenceKey = getSequenceKey(quantifiedPSM, distinguishModifiedPeptides);
		IsobaricQuantifiedPeptide quantifiedPeptide = null;
		if (map.containsKey(sequenceKey)) {
			quantifiedPeptide = map.get(sequenceKey);
			quantifiedPeptide.addPSM(quantifiedPSM);
		} else {
			quantifiedPeptide = new IsobaricQuantifiedPeptide(quantifiedPSM, distinguishModifiedPeptides);
			map.put(sequenceKey, quantifiedPeptide);
		}

		quantifiedPSM.setQuantifiedPeptide(quantifiedPeptide);
	}

	/**
	 * Create a Map of {@link IsobaricQuantifiedPeptide} from a collection of
	 * {@link QuantifiedPSMInterface}s. The resulting peptides will be added
	 * also automatically to all the {@link QuantifiedProteinInterface}s of the
	 * {@link QuantifiedPSMInterface}s, removing fist the
	 * {@link IsobaricQuantifiedPeptide}s linked to any
	 * {@link QuantifiedPSMInterface} and {@link QuantifiedProteinInterface}
	 *
	 * @param quantifiedPSMs
	 * @param distringuishModifiedPeptides
	 * @return
	 */
	public static Map<String, IsobaricQuantifiedPeptide> getIsobaricQuantifiedPeptides(
			Collection<IsobaricQuantifiedPSM> quantifiedPSMs, boolean distringuishModifiedPeptides) {
		Map<String, IsobaricQuantifiedPeptide> peptideMap = new HashMap<String, IsobaricQuantifiedPeptide>();

		// remove any peptide in the psms and proteins before create them
		for (IsobaricQuantifiedPSM quantifiedPSM : quantifiedPSMs) {
			quantifiedPSM.setQuantifiedPeptide(null);
			for (QuantifiedProteinInterface protein : quantifiedPSM.getQuantifiedProteins()) {
				protein.getQuantifiedPeptides().clear();
			}
		}

		for (IsobaricQuantifiedPSM quantifiedPSM : quantifiedPSMs) {
			QuantUtil.addToIsobaricPeptideMap(quantifiedPSM, peptideMap, distringuishModifiedPeptides);
			final String sequenceKey = getSequenceKey(quantifiedPSM, distringuishModifiedPeptides);
			final IsobaricQuantifiedPeptide createdPeptide = peptideMap.get(sequenceKey);

			// add it to the proteins of the psm
			final Set<QuantifiedProteinInterface> quantifiedProteins = quantifiedPSM.getQuantifiedProteins();
			for (QuantifiedProteinInterface protein : quantifiedProteins) {
				protein.addPeptide(createdPeptide);
			}
		}

		return peptideMap;
	}

	public static String getSequenceKey(QuantifiedPSMInterface quantPSM, boolean distinguishModifiedSequence) {
		if (distinguishModifiedSequence) {
			return quantPSM.getSequence();
		} else {
			return FastaParser.cleanSequence(quantPSM.getSequence());
		}
	}

	/**
	 * Create a Map of {@link IsobaricQuantifiedPeptide} getting the peptides
	 * from the {@link QuantifiedPSMInterface}
	 *
	 * @param quantifiedPSMs
	 * @param distringuishModifiedPeptides
	 * @return
	 */
	public static Map<String, IsobaricQuantifiedPeptide> getIsobaricQuantifiedPeptides(
			Collection<IsobaricQuantifiedPSM> quantifiedPSMs) {
		Map<String, IsobaricQuantifiedPeptide> peptideMap = new HashMap<String, IsobaricQuantifiedPeptide>();

		for (IsobaricQuantifiedPSM quantifiedPSM : quantifiedPSMs) {
			final IsobaricQuantifiedPeptide quantifiedPeptide = quantifiedPSM.getQuantifiedPeptide();
			if (!peptideMap.containsKey(quantifiedPeptide.getKey())) {
				peptideMap.put(quantifiedPeptide.getKey(), quantifiedPeptide);
			}
		}

		return peptideMap;
	}

}
