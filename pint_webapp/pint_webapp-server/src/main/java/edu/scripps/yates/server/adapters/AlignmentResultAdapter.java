package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.util.AlignmentResult;
import edu.scripps.yates.utilities.alignment.nwalign.NWResult;

public class AlignmentResultAdapter implements Adapter<AlignmentResult> {
	private final NWResult nwResult;
	private final PeptideBeanLight peptideBean1;
	private final PeptideBeanLight peptideBean2;

	public AlignmentResultAdapter(NWResult result, PeptideBeanLight peptideBean1, PeptideBeanLight peptideBean2) {
		nwResult = result;
		this.peptideBean1 = peptideBean1;
		this.peptideBean2 = peptideBean2;
	}

	@Override
	public AlignmentResult adapt() {
		final AlignmentResult ret = new AlignmentResult();
		ret.setAlignmentLength(nwResult.getAlignmentLength());
		ret.setAlignmentString(nwResult.getAlignmentString());
		ret.setFinalAlignmentScore(nwResult.getFinalAlignmentScore());
		ret.setIdenticalLength(nwResult.getIdenticalLength());
		ret.setSeq1(peptideBean1);
		ret.setSeq2(peptideBean2);
		return ret;
	}

}
