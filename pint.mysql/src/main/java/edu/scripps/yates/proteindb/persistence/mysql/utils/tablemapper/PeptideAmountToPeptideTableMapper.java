package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.wrappers.AmountValueWrapper;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

public class PeptideAmountToPeptideTableMapper {
	private final static Logger log = Logger.getLogger(PeptideAmountToPeptideTableMapper.class);
	private final TIntObjectHashMap<List<AmountValueWrapper>> peptide_to_peptideAmountValue = new TIntObjectHashMap<List<AmountValueWrapper>>();
	private static PeptideAmountToPeptideTableMapper instance;
	private final TIntHashSet conditionIDs = new TIntHashSet();

	private PeptideAmountToPeptideTableMapper() {

	}

	public static PeptideAmountToPeptideTableMapper getInstance() {
		if (instance == null) {
			instance = new PeptideAmountToPeptideTableMapper();
		}
		return instance;
	}

	public void addCondition(Condition condition) {
		if (!conditionIDs.contains(condition.getId())) {

			conditionIDs.add(condition.getId());
			// get the proteinvaluewrappers that point to the
			// condition
			final List<AmountValueWrapper> peptideAmountValues = PreparedCriteria
					.getPeptideAmountValuesWrappersFromCondition(condition);
			for (final AmountValueWrapper peptideAmountValue : peptideAmountValues) {
				final int proteinID = peptideAmountValue.getItemID();
				if (!peptide_to_peptideAmountValue.containsKey(proteinID)) {
					final List<AmountValueWrapper> list = new ArrayList<AmountValueWrapper>();
					peptide_to_peptideAmountValue.put(proteinID, list);
				}
				peptide_to_peptideAmountValue.get(proteinID).add(peptideAmountValue);
			}
		}
	}

	public List<AmountValueWrapper> mapPeptide(Peptide peptide) {
		return peptide_to_peptideAmountValue.get(peptide.getId());
	}

}
