package edu.scripps.yates.client.gui.columns.footers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.gui.columns.MyDataGrid;
import edu.scripps.yates.client.util.ClientDataUtil;
import edu.scripps.yates.client.util.ClientNumberFormat;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.model.light.PeptideBeanLight;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedMaths;

public class PeptideFooterManager extends FooterManager<PeptideBeanLight> {
	public PeptideFooterManager(MyDataGrid<PeptideBeanLight> datagrid) {
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

			case PROTEIN_LENGTH:
				footers.put(columnName, getLengthFooter());
				break;

			case SPECTRUM_COUNT:
				footers.put(columnName, getSpectrumCountFooter());
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
				final List<PeptideBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "-";
				} else {
					final List<Double> validAmounts = new ArrayList<Double>();
					for (final PeptideBeanLight item : visibleItems) {
						boolean validAmount = false;
						final List<AmountBean> amounts = item.getAmountsByExperimentalCondition().get(conditionName);
						if (amounts == null || amounts.isEmpty())
							continue;
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
							for (final AmountBean proteinAmountBean : amounts) {
								if (proteinAmountBean.getExperimentalCondition().getProject().getTag()
										.equals(projectName))
									subSum += proteinAmountBean.getValue();
							}
							amountValue = subSum / amounts.size();
							validAmount = true;
						}
						if (validAmount) {
							validAmounts.add(amountValue);
						}
					}
					if (validAmounts.isEmpty())
						return "-";
					final Double[] amountArray = validAmounts.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.##").format(SharedMaths.mean(amountArray));
					if (validAmounts.size() > 1) {
						ret += " (" + NumberFormat.getFormat("#.##").format(SharedMaths.stddev(amountArray)) + ")";
					}
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
				final List<PeptideBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					final List<Double> validRatios = new ArrayList<Double>();
					for (final PeptideBeanLight item : visibleItems) {
						final List<RatioBean> ratios = item.getRatiosByConditions(condition1Name, condition2Name,
								projectTag, ratioName, true);
						if (ratios == null || ratios.isEmpty())
							continue;
						Double ratioValue = 0.0;
						boolean validRatio = false;
						// try to convert to double the amountString
						try {
							ratioValue = Double.valueOf(ClientDataUtil.getRatioStringByConditions(item, condition1Name,
									condition2Name, projectTag, ratioName, true, false));
							validRatio = true;
						} catch (final NumberFormatException e) {
							double subSum = 0.0;
							for (final RatioBean ratio : ratios) {
								subSum += ratio.getValue();
							}
							ratioValue = subSum / ratios.size();
							validRatio = true;
						}
						if (validRatio) {
							validRatios.add(ratioValue);
						}
					}
					if (validRatios.isEmpty())
						return "-";
					final Double[] ratiosArray = validRatios.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.##").format(SharedMaths.mean(ratiosArray));
					if (validRatios.size() > 1) {
						ret += " (" + NumberFormat.getFormat("#.##").format(SharedMaths.stddev(ratiosArray)) + ")";
					}
					return ret;
				}
			}
		};
		return header;
	}

	@Override
	public Header<String> getScoreFooterByScore(String scoreName) {
		return getEmptyFooter();
	}

	private Header<String> getSpectrumCountFooter() {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PeptideBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					int sum = 0;
					int total = 0;
					for (final PeptideBeanLight item : visibleItems) {
						final int specCount = item.getNumPSMs();
						if (specCount > 0) {
							sum += specCount;
							total++;
						}
					}
					return NumberFormat.getFormat("#.#").format(Double.valueOf(sum) / total);
				}
			}
		};
		return header;
	}

	private Header<String> getLengthFooter() {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PeptideBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					int sumLength = 0;
					int total = 0;
					for (final PeptideBeanLight item : visibleItems) {
						final int length = item.getLength();
						if (length > 0) {
							sumLength += length;
							total++;
						}
					}
					if (total > 0)
						return NumberFormat.getFormat("#.#").format(Double.valueOf(sumLength) / total);
					return "-";
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

	@Override
	public Header<String> getRatioScoreFooterByConditions(final String condition1Name, final String condition2Name,
			final String projectTag, final String ratioName, final String ratioScoreName) {
		final Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				final List<PeptideBeanLight> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					final List<Double> validRatioScores = new ArrayList<Double>();
					for (final PeptideBeanLight item : visibleItems) {
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
