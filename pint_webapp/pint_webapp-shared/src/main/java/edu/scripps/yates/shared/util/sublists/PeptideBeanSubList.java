package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;

public class PeptideBeanSubList extends DataSubList<PeptideBeanLight> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5446270565312833772L;

	/**
	 *
	 */

	public PeptideBeanSubList() {

	}

	private PeptideBeanSubList(List<PeptideBeanLight> peptides, int totalNumber) {
		super(peptides, totalNumber);
	}

	public static PeptideBeanSubList getLightPeptideBeanSubList(List<PeptideBean> peptides, int totalNumberOfItems) {

		final List<PeptideBeanLight> lightPeptideBeans = new ArrayList<PeptideBeanLight>();
		for (final PeptideBean peptideBean : peptides) {
			final PeptideBeanLight clonedPeptide = peptideBean.cloneToLightPeptideBean();
			clonedPeptide.getProteins().clear();
			lightPeptideBeans.add(clonedPeptide);
		}
		return new PeptideBeanSubList(lightPeptideBeans, totalNumberOfItems);
	}

}
