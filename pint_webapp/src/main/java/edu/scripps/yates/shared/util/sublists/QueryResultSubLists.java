package edu.scripps.yates.shared.util.sublists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ScoreBean;

public class QueryResultSubLists implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3500609597581892746L;
	private PsmBeanSubList psmSubList;
	private PeptideBeanSubList peptideSubList;
	private ProteinBeanSubList proteinSubList;
	private ProteinGroupBeanSubList proteinGroupSubList;
	private int numDifferentSequences;
	private int numDifferentSequencesDistinguishingModifieds;

	public QueryResultSubLists() {

	}

	/**
	 * @return the psmSubList
	 */
	public PsmBeanSubList getPsmSubList() {
		return psmSubList;
	}

	/**
	 * @param psmSubList
	 *            the psmSubList to set
	 */
	public void setPsmSubList(PsmBeanSubList psmSubList) {
		this.psmSubList = psmSubList;
	}

	/**
	 * @return the proteinSubList
	 */
	public ProteinBeanSubList getProteinSubList() {
		return proteinSubList;
	}

	/**
	 * @param proteinSubList
	 *            the proteinSubList to set
	 */
	public void setProteinSubList(ProteinBeanSubList proteinSubList) {
		this.proteinSubList = proteinSubList;
	}

	/**
	 * @return the proteinGroupSubList
	 */
	public ProteinGroupBeanSubList getProteinGroupSubList() {
		return proteinGroupSubList;
	}

	/**
	 * @param proteinGroupSubList
	 *            the proteinGroupSubList to set
	 */
	public void setProteinGroupSubList(ProteinGroupBeanSubList proteinGroupSubList) {
		this.proteinGroupSubList = proteinGroupSubList;
	}

	public boolean isEmpty() {
		if (proteinSubList != null)
			return proteinSubList.isEmpty();
		return true;

	}

	/**
	 * @return the numDifferentSequences
	 */
	public int getNumDifferentSequences() {
		return numDifferentSequences;
	}

	/**
	 * @param numDifferentSequences
	 *            the numDifferentSequences to set
	 */
	public void setNumDifferentSequences(int numDifferentSequences) {
		this.numDifferentSequences = numDifferentSequences;
	}

	/**
	 * @return the numDifferentSequencesDistinguishingModifieds
	 */
	public int getNumDifferentSequencesDistinguishingModifieds() {
		return numDifferentSequencesDistinguishingModifieds;
	}

	/**
	 * @param numDifferentSequencesDistinguishingModifieds
	 *            the numDifferentSequencesDistinguishingModifieds to set
	 */
	public void setNumDifferentSequencesDistinguishingModifieds(int numDifferentSequencesDistinguishingModifieds) {
		this.numDifferentSequencesDistinguishingModifieds = numDifferentSequencesDistinguishingModifieds;
	}

	/**
	 * @return the peptideSubList
	 */
	public PeptideBeanSubList getPeptideSubList() {
		return peptideSubList;
	}

	/**
	 * @param peptideSubList
	 *            the peptideSubList to set
	 */
	public void setPeptideSubList(PeptideBeanSubList peptideSubList) {
		this.peptideSubList = peptideSubList;
	}

	public List<String> getPSMScoreNames() {
		Set<String> ret = new HashSet<String>();
		final List<PSMBean> dataList = getPsmSubList().getDataList();
		for (PSMBean psmBean : dataList) {
			ret.addAll(psmBean.getScores().keySet());
		}
		List<String> list = new ArrayList<String>();
		list.addAll(ret);
		Collections.sort(list);
		return list;
	}

	public List<String> getProteinScoreNames() {
		Set<String> ret = new HashSet<String>();
		final List<ProteinBean> dataList = getProteinSubList().getDataList();
		for (ProteinBean proteinBean : dataList) {
			ret.addAll(proteinBean.getScores().keySet());
		}
		List<String> list = new ArrayList<String>();
		list.addAll(ret);
		Collections.sort(list);
		return list;
	}

	public List<String> getPSMScoreTypes() {
		Set<String> ret = new HashSet<String>();
		final List<PSMBean> dataList = getPsmSubList().getDataList();
		for (PSMBean psmBean : dataList) {
			final Collection<ScoreBean> scores = psmBean.getScores().values();
			if (scores != null) {
				for (ScoreBean scoreBean : scores) {
					if (scoreBean.getScoreType() != null) {
						ret.add(scoreBean.getScoreType());
					}
				}
			}
		}
		List<String> list = new ArrayList<String>();
		list.addAll(ret);
		Collections.sort(list);
		return list;
	}

	public List<String> getProteinScoreTypes() {
		Set<String> ret = new HashSet<String>();
		final List<ProteinBean> dataList = getProteinSubList().getDataList();
		for (ProteinBean proteinBean : dataList) {
			final Collection<ScoreBean> scores = proteinBean.getScores().values();
			if (scores != null) {
				for (ScoreBean scoreBean : scores) {
					if (scoreBean.getScoreType() != null) {
						ret.add(scoreBean.getScoreType());
					}
				}
			}
		}
		List<String> list = new ArrayList<String>();
		list.addAll(ret);
		Collections.sort(list);
		return list;
	}

	public List<String> getPTMScoreNames() {
		Set<String> ret = new HashSet<String>();
		final List<PSMBean> dataList = getPsmSubList().getDataList();
		for (PSMBean psmBean : dataList) {
			final List<PTMBean> ptms = psmBean.getPtms();
			if (ptms != null) {
				for (PTMBean ptmBean : ptms) {
					final List<PTMSiteBean> ptmSites = ptmBean.getPtmSites();
					if (ptmSites != null) {
						for (PTMSiteBean ptmSiteBean : ptmSites) {
							if (ptmSiteBean.getScore() != null && ptmSiteBean.getScore().getScoreName() != null) {
								ret.add(ptmSiteBean.getScore().getScoreName());
							}
						}
					}
				}
			}
		}
		List<String> list = new ArrayList<String>();
		list.addAll(ret);
		Collections.sort(list);
		return list;
	}
}
