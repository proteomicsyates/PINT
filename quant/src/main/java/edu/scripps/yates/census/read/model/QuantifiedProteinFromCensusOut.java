package edu.scripps.yates.census.read.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.util.Pair;

public class QuantifiedProteinFromCensusOut extends AbstractContainsQuantifiedPSMs
		implements QuantifiedProteinInterface {
	private static final Logger log = Logger.getLogger(QuantifiedProteinFromCensusOut.class);

	private final Set<QuantifiedPSMInterface> quantifiedPSMs = new HashSet<QuantifiedPSMInterface>();
	private boolean distinguishModifiedPeptides;
	private ProteinEvidence evidence;
	private String accession;
	private ProteinGroup proteinGroup;
	private String accessionType;
	private final Set<QuantifiedPeptideInterface> quantifiedPeptides = new HashSet<QuantifiedPeptideInterface>();
	private final Set<Amount> amounts = new HashSet<Amount>();

	private String description;

	private String taxonomy;

	private final Set<String> fileNames = new HashSet<String>();

	private boolean discarded;

	public QuantifiedProteinFromCensusOut(String proteinACC) {
		final Pair<String, String> accPair = FastaParser.getACC(proteinACC);
		accession = accPair.getFirstelement();
		accessionType = accPair.getSecondElement();
	}

	@Override
	public String getKey() {
		return accession;
	}

	/**
	 * @return the quantifiedPSMs
	 */
	@Override
	public Set<QuantifiedPSMInterface> getQuantifiedPSMs() {
		return quantifiedPSMs;
	}

	/**
	 * @return the quantifiedPeptides
	 */
	@Override
	public Set<QuantifiedPeptideInterface> getQuantifiedPeptides() {
		return quantifiedPeptides;
	}

	@Override
	public void addPSM(QuantifiedPSMInterface quantifiedPSM) {
		quantifiedPSMs.add(quantifiedPSM);
		quantifiedPSM.addQuantifiedProtein(this);

	}

	@Override
	public int getDBId() {
		return hashCode();
	}

	@Override
	public String getAccession() {
		return accession;
	}

	@Override
	public String getAccessionType() {
		return accessionType;
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
	public List<GroupablePSM> getGroupablePSMs() {
		List<GroupablePSM> list = new ArrayList<GroupablePSM>();
		list.addAll(getQuantifiedPSMs());
		return list;
	}

	@Override
	public ProteinGroup getProteinGroup() {
		return proteinGroup;
	}

	@Override
	public Set<String> getTaxonomies() {
		if (taxonomy == null) {
			String fastaHeader = getDescription();
			final String accession = getAccession();
			taxonomy = FastaParser.getOrganismNameFromFastaHeader(fastaHeader, accession);
		}
		Set<String> set = new HashSet<String>();
		set.add(taxonomy);
		return set;

	}

	@Override
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}

	@Override
	public ProteinEvidence getEvidence() {
		return evidence;
	}

	/**
	 * @return the distinguishModifiedPeptides
	 */
	public boolean isDistinguishModifiedPeptides() {
		return distinguishModifiedPeptides;
	}

	/**
	 * @param distinguishModifiedPeptides
	 *            the distinguishModifiedPeptides to set
	 */
	public void setDistinguishModifiedPeptides(boolean distinguishModifiedPeptides) {
		this.distinguishModifiedPeptides = distinguishModifiedPeptides;
	}

	/**
	 * @return the rawfileNames
	 */

	@Override
	public Set<String> getRawFileNames() {
		Set<String> ret = new HashSet<String>();
		final Set<QuantifiedPSMInterface> quantifiedPSMs2 = getQuantifiedPSMs();
		for (QuantifiedPSMInterface quantifiedPSMInterface : quantifiedPSMs2) {
			ret.add(quantifiedPSMInterface.getRawFileName());
		}
		return ret;
	}

	@Override
	public void addPeptide(QuantifiedPeptideInterface peptide) {
		if (quantifiedPeptides.contains(peptide))
			return;
		quantifiedPeptides.add(peptide);
	}

	/**
	 * @param accession
	 *            the primaryAccession to set
	 */
	@Override
	public void setAccession(String accession) {
		this.accession = accession;
	}

	/**
	 * @param accessionType
	 *            the accessionType to set
	 */
	public void setAccessionType(String accessionType) {
		this.accessionType = accessionType;
	}

	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Integer getLength() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getAccession() + ": ");
		List<QuantifiedPeptideInterface> list = new ArrayList<QuantifiedPeptideInterface>();
		list.addAll(getQuantifiedPeptides());
		Collections.sort(list, new Comparator<QuantifiedPeptideInterface>() {

			@Override
			public int compare(QuantifiedPeptideInterface o1, QuantifiedPeptideInterface o2) {
				return o1.getSequence().compareTo(o2.getSequence());
			}
		});
		StringBuilder sb2 = new StringBuilder();
		for (QuantifiedPeptideInterface quantifiedPeptide : list) {
			if (!"".equals(sb2.toString()))
				sb2.append(",");
			sb2.append(quantifiedPeptide.getSequence());
		}
		sb.append(sb2);
		return sb.toString();
	}

	@Override
	public void addRatio(QuantRatio ratio) {
		ratios.add(ratio);

	}

	@Override
	public Set<Amount> getAmounts() {
		return amounts;
	}

	@Override
	public void addAmount(Amount amount) {
		amounts.add(amount);
	}

	@Override
	public Set<QuantRatio> getNonInfinityRatios() {
		return QuantUtil.getNonInfinityRatios(getRatios());
	}

	@Override
	public void addFileName(String fileName) {
		fileNames.add(fileName);

	}

	@Override
	public Set<String> getFileNames() {
		return fileNames;
	}

	@Override
	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator) {
		return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios()), AggregationLevel.PROTEIN);
	}

	@Override
	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator, String replicateName) {
		return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios(replicateName)),
				AggregationLevel.PROTEIN);
	}

	@Override
	public boolean isDiscarded() {

		return discarded;
	}

	@Override
	public void setDiscarded(boolean discarded) {
		this.discarded = discarded;
		final Set<QuantifiedPSMInterface> quantifiedPSMs = getQuantifiedPSMs();
		for (QuantifiedPSMInterface quantifiedPSMInterface : quantifiedPSMs) {
			quantifiedPSMInterface.setDiscarded(discarded);
		}
	}
}
