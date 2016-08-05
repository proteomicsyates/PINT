package edu.scripps.yates.census.read.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPeptideInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.dbindex.IndexedProtein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePSM;
import edu.scripps.yates.utilities.grouping.ProteinEvidence;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.util.Pair;

public class QuantifiedProteinFromDBIndexEntry extends AbstractContainsQuantifiedPSMs
		implements QuantifiedProteinInterface {
	private static final Logger log = Logger.getLogger(QuantifiedProteinFromDBIndexEntry.class);

	private final Set<QuantifiedPSMInterface> quantifiedPSMs = new HashSet<QuantifiedPSMInterface>();
	private boolean distinguishModifiedPeptides;
	private IndexedProtein indexedProtein;
	private ProteinEvidence evidence;
	private String primaryAccession;
	private ProteinGroup proteinGroup;
	private String accessionType;
	private final Set<QuantifiedPeptideInterface> quantifiedPeptides = new HashSet<QuantifiedPeptideInterface>();
	private final Set<Amount> amounts = new HashSet<Amount>();

	private String description;

	private String taxonomy;

	private final Set<String> fileNames = new HashSet<String>();

	private boolean discarded;

	public QuantifiedProteinFromDBIndexEntry(IndexedProtein indexedProtein) throws IOException {
		this.indexedProtein = indexedProtein;
		final Pair<String, String> accPair = FastaParser.getACC(indexedProtein.getFastaDefLine());
		primaryAccession = accPair.getFirstelement();
		accessionType = accPair.getSecondElement();
	}

	@Override
	public String getKey() {
		return KeyUtils.getProteinKey(indexedProtein);
	}

	@Override
	public String getDescription() {
		if (description == null) {
			if (indexedProtein != null) {
				description = FastaParser.getDescription(indexedProtein.getFastaDefLine());
			}
		}
		return description;
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
		return primaryAccession;
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
	public String getTaxonomy() {
		if (taxonomy == null) {
			String fastaHeader = null;

			if (indexedProtein != null)
				fastaHeader = indexedProtein.getFastaDefLine();
			final String accession = getAccession();

			final String organismNameFromFastaHeader = FastaParser.getOrganismNameFromFastaHeader(fastaHeader,
					accession);
			taxonomy = organismNameFromFastaHeader;
		}
		return taxonomy;
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
	 * @return the primaryAccession
	 */
	public String getPrimaryAccession() {
		return primaryAccession;
	}

	/**
	 * @param primaryAccession
	 *            the primaryAccession to set
	 */
	public void setPrimaryAccession(String primaryAccession) {
		this.primaryAccession = primaryAccession;
	}

	/**
	 * @param accessionType
	 *            the accessionType to set
	 */
	public void setAccessionType(String accessionType) {
		this.accessionType = accessionType;
	}

	/**
	 * @param indexedProtein
	 *            the indexedProtein to set
	 */
	public void setIndexedProtein(IndexedProtein indexedProtein) {
		this.indexedProtein = indexedProtein;
	}

	@Override
	public Integer getLength() {
		return null;
	}

	@Override
	public void setAccession(String primaryAccession) {
		this.primaryAccession = primaryAccession;

	}

	@Override
	public void setDescription(String description) {
		this.description = description;

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
	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
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
	public void addRatio(QuantRatio ratio) {
		ratios.add(ratio);

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
