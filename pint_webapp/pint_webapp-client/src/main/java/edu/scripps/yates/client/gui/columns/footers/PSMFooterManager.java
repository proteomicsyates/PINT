package edu.scripps.yates.client.gui.columns.footers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.util.ClientDataUtil;
import edu.scripps.yates.client.util.ClientNumberFormat;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBeanLight;
import edu.scripps.yates.shared.model.PTMBean;
import edu.scripps.yates.shared.model.PTMSiteBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedMaths;

public class PSMFooterManager extends FooterManager<PSMBeanLight> {

	public PSMFooterManager(MyDataGrid<PSMBeanLight> datagrid) {
		super(datagrid);
		final ColumnName[] columnNames = ColumnName.values();
		for (final ColumnName columnName : columnNames) {
			switch (columnName) {
			case COVERAGE:
				footers.put(columnName, getEmptyFooter());
				break;
			case ACC:
				footers.put(columnName, getEmptyFooter());
				break;
			case PTM_SCORE:
				footers.put(columnName, getPTM_ScoreFooter());
				break;
			case NUM_PTMS:
				footers.put(columnName, getNumPTMsFooter());
				break;
			case NUM_PTM_SITES:
				footers.put(columnName, getNumPTMSitesFooter());
				break;
			case PEPTIDE_PI:
				footers.put(columnName, getPeptidePIFooter());
				break;
			default:
				footers.put(columnName, getEmptyFooter());
			}
		}
	}

	@Override
	public Header<String> getFooter(ColumnName columnName) {
		return footers.get(columnName);
	}

	@Override
	public Header<String> getAmountFooterByCondition(final String conditionName, final AmountType amountType,
			final String projectName) {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "-";
				} else {
					double sum = 0;
					final List<Double> validAmounts = new ArrayList<Double>();
					for (final PSMBeanLight item : visibleItems) {
						final Set<AmountBean> amounts = new HashSet<AmountBean>();
						final List<AmountBean> amounts2 = item.getAmounts();
						for (final AmountBean amountBean : amounts2) {
							if (amountBean.getExperimentalCondition().getId().equals(conditionName)) {
								if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectName)) {
									amounts.add(amountBean);
								}
							}
						}

						if (amounts == null || amounts.isEmpty())
							continue;
						boolean validAmount = false;
						Double amountValue = 0.0;
						// try to convert to double the amountString
						try {
							final DataGridRenderValue data = DataGridRenderValue.getAmountDataGridRenderValue(item,
									conditionName, amountType, projectName, new ClientNumberFormat("#.##"));
							if (data.getActualNonRoundedValue() != null) {
								amountValue = data.getActualNonRoundedValue();
							} else {
								amountValue = Double.valueOf(data.getValue());
							}
							validAmount = true;
						} catch (final NumberFormatException e) {
							double subSum = 0.0;
							int numAmounts = 0;
							for (final AmountBean amountBean : amounts) {
								if (amountBean.getExperimentalCondition().getProject().getTag().equals(projectName)) {
									subSum += amountBean.getValue();
									numAmounts++;
									validAmount = true;
								}
							}
							if (validAmount)
								amountValue = subSum / numAmounts;
						}
						if (validAmount) {
							validAmounts.add(amountValue);
							sum += amountValue;
						}
					}
					if (validAmounts.isEmpty())
						return "-";
					final Double[] amountArray = validAmounts.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.###").format(SharedMaths.mean(amountArray));
					if (validAmounts.size() > 1)
						ret += " (" + NumberFormat.getFormat("#.##").format(SharedMaths.stddev(amountArray)) + ")";
					return ret;
				}
			}
		};
		return header;
	}

	@Override
	public Header<String> getRatioFooterByConditions(final String condition1Name, final String condition2Name,
			final String projectTag, final String ratioName) {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					double sum = 0;
					final List<Double> validRatios = new ArrayList<Double>();
					for (final PSMBeanLight item : visibleItems) {
						Double ratioValue = 0.0;
						boolean validRatioValue = false;
						final List<RatioBean> ratiosByConditions = item.getRatiosByConditions(condition1Name,
								condition2Name, projectTag, ratioName, true);
						if (ratiosByConditions == null || ratiosByConditions.isEmpty())
							continue;
						// try to convert to double the amountString
						try {
							ratioValue = Double.valueOf(ClientDataUtil.getRatioStringByConditions(item, condition1Name,
									condition2Name, projectTag, ratioName, true, false));
							validRatioValue = true;
						} catch (final NumberFormatException e) {

							final List<RatioBean> ratios = ratiosByConditions;
							if (ratios != null) {
								double subSum = 0.0;
								for (final RatioBean ratio : ratios) {
									subSum += ratio.getValue();
								}
								ratioValue = subSum / ratios.size();
								validRatioValue = true;
							}

						}
						if (validRatioValue) {
							sum += ratioValue;
							validRatios.add(ratioValue);
						}
					}

					if (validRatios.isEmpty())
						return "-";
					final Double[] ratioArray = validRatios.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.###").format(SharedMaths.mean(ratioArray));
					if (validRatios.size() > 1)
						ret += " (" + NumberFormat.getFormat("#.##").format(SharedMaths.stddev(ratioArray)) + ")";
					return ret;
					// Double.valueOf(sum) / validRatios.size());
				}
			}
		};
		return header;
	}

	@Override
	public Header<String> getScoreFooterByScore(final String scoreName) {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					final List<Double> validScores = new ArrayList<Double>();

					for (final PSMBeanLight item : visibleItems) {
						Double scoreValue = 0.0;
						// try to convert to double the amountString
						try {
							final ScoreBean score = item.getScoreByName(scoreName);
							if (score != null) {
								scoreValue = Double.valueOf(score.getValue());
								validScores.add(scoreValue);
							}
						} catch (final NumberFormatException e) {
							return "-";

						}
					}
					final double mean = SharedMaths.mean(validScores.toArray(new Double[0]));
					final double std = SharedMaths.stddev(validScores.toArray(new Double[0]));
					if (Double.compare(std, Double.NaN) == 0)
						return NumberFormat.getFormat("#.##").format(mean);
					return NumberFormat.getFormat("#.##").format(mean) + " ("
							+ NumberFormat.getFormat("#.##").format(std) + ")";
				}
			}
		};
		return header;
	}

	private Header<String> getEmptyFooter() {
		final Header<String> emptyHeader = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				return "-";
			}
		};
		return emptyHeader;
	}

	private Header<String> getPTM_ScoreFooter() {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					double sum = 0;
					int numScores = 0;
					for (final PSMBeanLight item : visibleItems) {
						final List<PTMBean> ptms = item.getPtms();
						if (ptms != null) {
							for (final PTMBean ptmBean : ptms) {
								final List<PTMSiteBean> ptmSites = ptmBean.getPtmSites();
								if (ptmSites != null) {
									for (final PTMSiteBean ptmSiteBean : ptmSites) {
										final ScoreBean score = ptmSiteBean.getScore();
										if (score != null) {
											final String scoreString = score.getValue();
											try {
												final Double scoreValue = Double.valueOf(scoreString);
												sum += scoreValue;
												numScores++;
											} catch (final NumberFormatException e) {
												// do nothing
											}
										}
									}
								}
							}
						}
					}
					if (numScores > 0)
						return NumberFormat.getFormat("#.##").format(sum / numScores);
					return "-";
				}
			}
		};
		return header;
	}

	private Header<String> getNumPTMsFooter() {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "-";
				} else {
					final List<Integer> validNumPTMs = new ArrayList<Integer>();
					for (final PSMBeanLight item : visibleItems) {
						final List<PTMBean> ptms = item.getPtms();
						if (ptms != null) {
							final int numPTMs = ptms.size();
							validNumPTMs.add(numPTMs);
						}
					}
					if (validNumPTMs.isEmpty())
						return "-";
					return String.valueOf(SharedMaths.sum(validNumPTMs.toArray(new Integer[0])));
				}
			}
		};
		return header;
	}

	private Header<String> getNumPTMSitesFooter() {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "-";
				} else {
					final List<Integer> list = new ArrayList<Integer>();
					boolean validValue = false;
					for (final PSMBeanLight item : visibleItems) {
						final List<PTMBean> ptms = item.getPtms();
						int numPTMs = 0;
						if (ptms != null) {
							for (final PTMBean ptmBean : ptms) {
								if (ptmBean.getPtmSites() != null && !ptmBean.getPtmSites().isEmpty()) {
									validValue = true;
									final int numPTMSites = ptmBean.getPtmSites().size();
									numPTMs += numPTMSites;
								}
							}
						}
						list.add(numPTMs);
					}
					if (!validValue)
						return "-";
					return String.valueOf(SharedMaths.sum(list.toArray(new Integer[0])));
				}
			}
		};
		return header;
	}

	private Header<String> getPeptidePIFooter() {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					double sum = 0;
					boolean validValue = false;
					for (final PSMBeanLight item : visibleItems) {
						final Double pi = item.getPi();
						if (pi != null) {
							sum += pi;
							validValue = true;
						}
					}
					if (!validValue)
						return "-";
					return NumberFormat.getFormat("#.#").format(sum / visibleItems.size());
				}
			}
		};
		return header;
	}

	@Override
	public Header<String> getRatioScoreFooterByConditions(final String condition1Name, final String condition2Name,
			final String projectTag, final String ratioName, final String ratioScoreName) {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PSMBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					final List<Double> validRatioScores = new ArrayList<Double>();
					for (final PSMBeanLight item : visibleItems) {
						final List<RatioBean> ratios = item.getRatiosByConditions(condition1Name, condition2Name,
								projectTag, ratioName, true);
						if (ratios == null || ratios.isEmpty())
							continue;
						Double ratioScoreValue = 0.0;
						boolean validRatio = false;
						// try to convert to double the amountString
						try {
							ratioScoreValue = Double
									.valueOf(ClientDataUtil.getRatioScoreStringByConditions(item, condition1Name,
											condition2Name, projectTag, ratioName, ratioScoreName, true, false));
							validRatio = true;
						} catch (final NumberFormatException e) {
							double subSum = 0.0;
							for (final RatioBean ratio : ratios) {
								subSum += ratio.getValue();
							}
							ratioScoreValue = subSum / ratios.size();
							validRatio = true;
						}
						if (validRatio) {
							validRatioScores.add(ratioScoreValue);
						}
					}
					if (validRatioScores.isEmpty())
						return "-";
					final Double[] ratiosArray = validRatioScores.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.##").format(SharedMaths.mean(ratiosArray));
					if (validRatioScores.size() > 1) {
						ret += " (" + NumberFormat.getFormat("#.##").format(SharedMaths.stddev(ratiosArray)) + ")";
					}
					return ret;
				}
			}
		};
		return header;
	}
}
