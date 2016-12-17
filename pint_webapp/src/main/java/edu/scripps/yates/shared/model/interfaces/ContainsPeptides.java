package edu.scripps.yates.shared.model.interfaces;

import java.util.List;
import java.util.Set;

import edu.scripps.yates.shared.model.PeptideBean;

public interface ContainsPeptides {
	public List<PeptideBean> getPeptides();

	public Set<Integer> getPeptideDBIds();
}
