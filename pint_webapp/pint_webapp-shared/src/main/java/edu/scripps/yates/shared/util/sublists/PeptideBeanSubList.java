package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.PeptideBean;

public class PeptideBeanSubList extends DataSubList<PeptideBean>implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5446270565312833772L;

	/**
	 *
	 */

	public PeptideBeanSubList() {

	}

	private PeptideBeanSubList(List<PeptideBean> peptides, int totalNumber) {
		super(peptides, totalNumber);
	}

	public static PeptideBeanSubList getLightPeptideBeanSubList(List<PeptideBean> peptides, int totalNumberOfItems) {

		List<PeptideBean> clonedPeptides = new ArrayList<PeptideBean>();
		for (PeptideBean peptideBean : peptides) {
			PeptideBean clonedPeptide = peptideBean.cloneToLightPeptideBean();
			clonedPeptides.add(clonedPeptide);

		}
		return new PeptideBeanSubList(clonedPeptides, totalNumberOfItems);
	}

}
