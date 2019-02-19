package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.list.array.TIntArrayList;

public class MSRunToPeptideTableMapper extends AbstractTableMapper<MsRun, Peptide> {

	private static MSRunToPeptideTableMapper instance;

	private MSRunToPeptideTableMapper() {

	}

	public static MSRunToPeptideTableMapper getInstance() {
		if (instance == null) {
			instance = new MSRunToPeptideTableMapper();
		}
		return instance;
	}

	@Override
	public TIntArrayList queryForIDs(MsRun msRun) {
		final Set<Integer> proteinIDs = PreparedCriteria.getPeptideIDsFromMsRun(msRun);
		return transformToTIntArray(proteinIDs);
	}

	@Override
	protected int getIDFromT(MsRun t) {
		return t.getId();
	}

	@Override
	protected int getIDFromY(Peptide y) {
		return y.getId();
	}

}
