package edu.scripps.yates.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class ServerDataUtils {
	public static List<PSMBean> getPSMBeansFromProteinBeans(
			List<ProteinBean> proteinBeans) {
		List<PSMBean> ret = new ArrayList<PSMBean>();
		Set<Integer> psmIDs = new HashSet<Integer>();
		if (proteinBeans != null) {
			for (ProteinBean proteinBean : proteinBeans) {
				final List<PSMBean> psmBeans = proteinBean.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(proteinBean, false);
				for (PSMBean psmBean : psmBeans) {
					if (!psmIDs.contains(psmBean.getDbID())) {
						psmIDs.add(psmBean.getDbID());
						ret.add(psmBean);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Get a Map of {@link RatioDescriptorBean} by project from a collection of
	 * {@link ProteinBean}
	 * 
	 * @param currentProteins
	 * @return
	 */
	public static Map<String, List<RatioDescriptorBean>> getRatioDescriptorsByProjectsFromProteins(
			Collection<ProteinBean> proteins) {
		Map<String, List<RatioDescriptorBean>> ret = new HashMap<String, List<RatioDescriptorBean>>();
		for (ProteinBean proteinBean : proteins) {
			final Set<RatioBean> ratios = proteinBean.getRatios();
			if (ratios != null && !ratios.isEmpty()) {
				SharedDataUtils
						.mergeRatioDescriptorMapsWithNoRepetitionsOnList(
								ret,
								SharedDataUtils
										.getRatioDescriptorsByProjectsFromRatios(ratios));
			}
			final List<PSMBean> psms = proteinBean.getPsms();
			// RemoteServicesTasks.getPSMsFromProtein(proteinBean, false);
			for (PSMBean psmBean : psms) {
				final Set<RatioBean> ratios2 = psmBean.getRatios();
				if (ratios2 != null) {
					SharedDataUtils
							.mergeRatioDescriptorMapsWithNoRepetitionsOnList(
									ret,
									SharedDataUtils
											.getRatioDescriptorsByProjectsFromRatios(ratios2));
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a alphabetically sorted list of score names associated to psms
	 * 
	 * @param protein
	 * @return
	 */
	public static List<String> getPSMScoreNamesFromProteins(
			Collection<ProteinBean> proteins) {
		List<String> ret = new ArrayList<String>();
		if (proteins != null) {
			for (ProteinBean protein : proteins) {
				final List<PSMBean> psms = protein.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(protein, false);
				for (PSMBean psmBean : psms) {
					final Map<String, ScoreBean> scores = psmBean.getScores();
					if (scores != null) {
						final Set<String> scoreNames = scores.keySet();
						for (String scoreName : scoreNames) {
							if (!ret.contains(scoreName))
								ret.add(scoreName);
						}
					}
				}
			}
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * Gets a alphabetically sorted list of score names associated to PTMs of
	 * psms
	 * 
	 * @param protein
	 * @return
	 */
	public static List<String> getPTMScoreNamesFromProteins(
			Collection<ProteinBean> proteins) {
		List<String> ret = new ArrayList<String>();
		if (proteins != null) {
			for (ProteinBean protein : proteins) {
				final List<PSMBean> psms = protein.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(protein, false);
				for (PSMBean psmBean : psms) {
					final List<PTMBean> ptms = psmBean.getPtms();
					if (ptms != null) {
						for (PTMBean ptmBean : ptms) {
							final List<PTMSiteBean> ptmSites = ptmBean
									.getPtmSites();
							if (ptmSites != null) {
								for (PTMSiteBean ptmSiteBean : ptmSites) {
									final ScoreBean score = ptmSiteBean
											.getScore();
									if (score != null) {
										if (!ret.contains(score.getScoreName()))
											ret.add(score.getScoreName());
									}
								}
							}
						}
					}
				}
			}
		}
		Collections.sort(ret);
		return ret;
	}

	/**
	 * Gets a Map of conditions by projects from the amounts and ratios of a
	 * collections of proteins, looking also on amounts and ratios of their PSMs
	 * 
	 * @param currentProteins
	 * @return
	 */
	public static Map<String, List<String>> getConditionsByProjects(
			Collection<ProteinBean> proteins) {
		Map<String, List<String>> ret = new HashMap<String, List<String>>();

		if (proteins != null) {
			for (ProteinBean proteinBean : proteins) {
				SharedDataUtils.mergeStringMapsWithNoRepetitionsOnList(ret,
						SharedDataUtils
								.getConditionsByProjectsFromAmounts(proteinBean
										.getAmounts()));
				SharedDataUtils.mergeStringMapsWithNoRepetitionsOnList(ret,
						SharedDataUtils
								.getConditionsByProjectsFromRatios(proteinBean
										.getRatios()));
				final List<PSMBean> psmBeans = proteinBean.getPsms();
				// RemoteServicesTasks .getPSMsFromProtein(proteinBean, true);
				if (psmBeans != null) {
					for (PSMBean psmBean : psmBeans) {
						SharedDataUtils
								.mergeStringMapsWithNoRepetitionsOnList(
										ret,
										SharedDataUtils
												.getConditionsByProjectsFromAmounts(psmBean
														.getAmounts()));
						SharedDataUtils
								.mergeStringMapsWithNoRepetitionsOnList(
										ret,
										SharedDataUtils
												.getConditionsByProjectsFromRatios(psmBean
														.getRatios()));
					}
				}
			}
		}

		return ret;
	}
}
