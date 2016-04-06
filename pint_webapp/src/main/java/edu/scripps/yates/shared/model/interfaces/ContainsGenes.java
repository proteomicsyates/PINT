package edu.scripps.yates.shared.model.interfaces;

import java.util.List;

import edu.scripps.yates.shared.model.GeneBean;

public interface ContainsGenes {
	public List<GeneBean> getGenes(boolean onlyPrimary);

}
