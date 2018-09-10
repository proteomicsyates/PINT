package edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper;

import java.util.List;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import gnu.trove.list.array.TIntArrayList;

public class MSRunToProteinTableMapper extends AbstractTableMapper<MsRun, Protein> {

	private static MSRunToProteinTableMapper instance;

	private MSRunToProteinTableMapper() {

	}

	public static MSRunToProteinTableMapper getInstance() {
		if (instance == null) {
			instance = new MSRunToProteinTableMapper();
		}
		return instance;
	}

	@Override
	public TIntArrayList queryForIDs(MsRun msRun) {
		final List<Integer> proteinIDs = PreparedCriteria.getProteinIDsFromMsRun(msRun);
		return transformToTIntArray(proteinIDs);
	}

	@Override
	protected int getIDFromT(MsRun t) {
		return t.getId();
	}

	@Override
	protected int getIDFromY(Protein y) {
		return y.getId();
	}

}
