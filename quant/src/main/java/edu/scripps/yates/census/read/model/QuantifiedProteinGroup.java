package edu.scripps.yates.census.read.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.model.interfaces.QuantRatio;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.util.QuantUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.model.enums.AggregationLevel;

public class QuantifiedProteinGroup extends AbstractContainsQuantifiedPSMs {
	private static final String SEPARATOR = " ## ";
	private final Set<IsobaricQuantifiedProtein> proteins = new HashSet<IsobaricQuantifiedProtein>();

	public QuantifiedProteinGroup(ProteinGroup proteinGroup) {
		for (GroupableProtein groupableProtein : proteinGroup) {
			if (groupableProtein instanceof IsobaricQuantifiedProtein) {
				proteins.add((IsobaricQuantifiedProtein) groupableProtein);
			}
		}

	}

	public int size() {
		return getProteins().size();
	}

	@Override
	public Set<QuantifiedPSMInterface> getQuantifiedPSMs() {
		Set<QuantifiedPSMInterface> ret = new HashSet<QuantifiedPSMInterface>();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			ret.addAll(quantifiedProtein.getQuantifiedPSMs());
		}

		return ret;
	}

	public String getAccessionString() {
		StringBuilder sb = new StringBuilder();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			if (!"".equals(sb.toString()))
				sb.append(SEPARATOR);
			sb.append(quantifiedProtein.getAccession());
		}
		return sb.toString();
	}

	public String getDescriptionString() {
		StringBuilder sb = new StringBuilder();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			if (!"".equals(sb.toString()))
				sb.append(SEPARATOR);
			sb.append(quantifiedProtein.getDescription());
		}
		return sb.toString();
	}

	public List<String> getTaxonomies() {
		List<String> ret = new ArrayList<String>();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			ret.addAll(quantifiedProtein.getTaxonomies());
		}
		return ret;
	}

	/**
	 * NOTE THAT THIS RETURNED LIST IS NOT VALID FOR ADDING NEW PROTEINS TO THE
	 * GROUP
	 *
	 * @return the proteins
	 */
	public List<IsobaricQuantifiedProtein> getProteins() {
		List<IsobaricQuantifiedProtein> ret = new ArrayList<IsobaricQuantifiedProtein>();
		ret.addAll(proteins);
		Collections.sort(ret, new Comparator<IsobaricQuantifiedProtein>() {

			@Override
			public int compare(IsobaricQuantifiedProtein o1, IsobaricQuantifiedProtein o2) {
				final String accession1 = o1.getAccession();
				final String accession2 = o2.getAccession();
				return accession1.compareTo(accession2);
			}
		});
		return ret;
	}

	public String getGeneNameString() {
		StringBuilder sb = new StringBuilder();
		for (IsobaricQuantifiedProtein quantifiedProtein : getProteins()) {
			if (!"".equals(sb.toString()))
				sb.append(SEPARATOR);
			String geneFromFastaHeader = FastaParser.getGeneFromFastaHeader(quantifiedProtein.getAccession());
			if (geneFromFastaHeader == null) {
				geneFromFastaHeader = FastaParser.getGeneFromFastaHeader(quantifiedProtein.getDescription());
			}
			sb.append(geneFromFastaHeader);
		}
		return sb.toString();
	}

	public Set<String> getFileNames() {
		Set<String> ret = new HashSet<String>();
		for (IsobaricQuantifiedProtein quantprotein : proteins) {
			ret.addAll(quantprotein.getFileNames());
		}
		return ret;
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
	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator) {
		return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios()), AggregationLevel.PROTEINGROUP);
	}

	@Override
	public QuantRatio getConsensusRatio(QuantCondition quantConditionNumerator,
			QuantCondition quantConditionDenominator, String replicateName) {
		return QuantUtil.getAverageRatio(QuantUtil.getNonInfinityRatios(getRatios(replicateName)),
				AggregationLevel.PROTEIN);
	}
}
