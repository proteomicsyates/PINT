package edu.scripps.yates.census.read.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.model.interfaces.Ratio;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;

public class QuantifiedPeptide extends AbstractContainsQuantifiedPSMs implements QuantifiedPeptideInterface {
	protected final String sequenceKey;
	protected final boolean distinguishModifiedSequences;
	protected final Set<QuantifiedPSMInterface> psms = new HashSet<QuantifiedPSMInterface>();
	private final Set<Amount> amounts = new HashSet<Amount>();

	/**
	 * Creates a {@link QuantifiedPeptide} object, adding the
	 * {@link QuantifiedPSMInterface} to its list of
	 * {@link QuantifiedPSMInterface}s
	 *
	 * @param quantPSM
	 * @param distinguishModifiedSequences
	 */
	public QuantifiedPeptide(QuantifiedPSMInterface quantPSM, boolean distinguishModifiedSequences) {
		sequenceKey = QuantUtil.getSequenceKey(quantPSM, distinguishModifiedSequences);
		StaticMaps.peptideMap.addItem(this);
		this.distinguishModifiedSequences = distinguishModifiedSequences;
		addPSM(quantPSM);

	}

	@Override
	public String getKey() {
		return sequenceKey;
	}

	/**
	 * It assures that has the same sequence, taking into account the
	 * distinguishModifiedSequences of the instance
	 */
	public boolean addPSM(QuantifiedPSMInterface quantPSM) {
		if (sequenceKey.equals(QuantUtil.getSequenceKey(quantPSM, distinguishModifiedSequences))) {
			return psms.add(quantPSM);
		}
		return false;
	}

	/**
	 * Create a Map of {@link QuantifiedPeptide} from a collection of
	 * {@link QuantifiedPSMInterface}s. The resulting peptides will be added
	 * also automatically to all the {@link QuantifiedProteinInterface}s of the
	 * {@link QuantifiedPSMInterface}s, removing fist the
	 * {@link QuantifiedPeptide}s linked to any {@link QuantifiedPSMInterface}
	 * and {@link QuantifiedProteinInterface}
	 *
	 * @param quantifiedPSMs
	 * @param distringuishModifiedPeptides
	 * @return
	 */
	public static Map<String, QuantifiedPeptide> getQuantifiedPeptides(
			Collection<QuantifiedPSMInterface> quantifiedPSMs, boolean distringuishModifiedPeptides) {
		Map<String, QuantifiedPeptide> peptideMap = new HashMap<String, QuantifiedPeptide>();

		// remove any peptide in the psms and proteins before create them
		for (QuantifiedPSMInterface quantifiedPSM : quantifiedPSMs) {
			quantifiedPSM.setQuantifiedPeptide(null);
			for (QuantifiedProteinInterface protein : quantifiedPSM.getQuantifiedProteins()) {
				protein.getQuantifiedPeptides().clear();
			}
		}

		for (QuantifiedPSMInterface quantifiedPSM : quantifiedPSMs) {
			QuantUtil.addToPeptideMap(quantifiedPSM, peptideMap, distringuishModifiedPeptides);
			final String sequenceKey = QuantUtil.getSequenceKey(quantifiedPSM, distringuishModifiedPeptides);
			final QuantifiedPeptide createdPeptide = peptideMap.get(sequenceKey);

			// add it to the proteins of the psm
			final Set<QuantifiedProteinInterface> quantifiedProteins = quantifiedPSM.getQuantifiedProteins();
			for (QuantifiedProteinInterface protein : quantifiedProteins) {
				protein.addPeptide(createdPeptide);
			}
		}

		return peptideMap;
	}

	/**
	 * Create a Map of {@link QuantifiedPeptide} getting the peptides from the
	 * {@link QuantifiedPSMInterface}
	 *
	 * @param quantifiedPSMs
	 * @param distringuishModifiedPeptides
	 * @return
	 */
	public static Map<String, QuantifiedPeptideInterface> getQuantifiedPeptides(
			Collection<QuantifiedPSMInterface> quantifiedPSMs) {
		Map<String, QuantifiedPeptideInterface> peptideMap = new HashMap<String, QuantifiedPeptideInterface>();
		for (QuantifiedPSMInterface quantifiedPSM : quantifiedPSMs) {
			final QuantifiedPeptideInterface quantifiedPeptide = quantifiedPSM.getQuantifiedPeptide();
			if (!peptideMap.containsKey(quantifiedPeptide.getKey())) {
				peptideMap.put(quantifiedPeptide.getKey(), quantifiedPeptide);
			}
		}
		return peptideMap;
	}

	@Override
	public Set<QuantifiedPSMInterface> getQuantifiedPSMs() {
		return psms;
	}

	@Override
	public String getSequence() {
		return sequenceKey;
	}

	/**
	 *
	 * @return NOte that the returned set is created everytime this method is
	 *         called, because proteins are taken from the psms of the peptide
	 */
	@Override
	public Set<QuantifiedProteinInterface> getQuantifiedProteins() {
		Set<QuantifiedProteinInterface> set = new HashSet<QuantifiedProteinInterface>();
		for (QuantifiedPSMInterface psm : psms) {
			for (QuantifiedProteinInterface quantProtein : psm.getQuantifiedProteins()) {
				set.add(quantProtein);
			}
		}
		return set;
	}

	@Override
	public Float getCalcMHplus() {
		if (!psms.isEmpty())
			return psms.iterator().next().getCalcMHplus();
		return null;
	}

	/**
	 * @return the fileNames
	 */
	@Override
	public Set<String> getRawFileNames() {
		Set<String> ret = new HashSet<String>();
		for (QuantifiedPSMInterface quantPSM : psms) {
			ret.add(quantPSM.getFileName());
		}
		return ret;
	}

	@Override
	public String toString() {
		return getSequence();
	}

	@Override
	public String getFullSequence() {
		return getSequence();
	}

	@Override
	public Set<String> getTaxonomies() {
		Set<String> ret = new HashSet<String>();
		for (QuantifiedPSMInterface quantifiedPSM : psms) {
			ret.addAll(quantifiedPSM.getTaxonomies());
		}
		return ret;
	}

	@Override
	public Float getMHplus() {
		if (!psms.isEmpty()) {
			return psms.iterator().next().getMHplus();
		}
		return null;
	}

	@Override
	public Set<Ratio> getRatios() {
		Set<Ratio> ret = new HashSet<Ratio>();

		final Set<QuantifiedPSMInterface> quantifiedPSMs = getQuantifiedPSMs();
		for (QuantifiedPSMInterface quantifiedPSM : quantifiedPSMs) {
			ret.addAll(quantifiedPSM.getRatios());
		}

		return ret;
	}

	@Override
	public Set<Amount> getAmounts() {
		if (amounts.isEmpty()) {
			for (QuantifiedPSMInterface psm : getQuantifiedPSMs()) {
				amounts.addAll(psm.getAmounts());
			}
		}
		return amounts;
	}

	@Override
	public void addAmount(Amount amount) {
		amounts.add(amount);

	}

}
