package edu.scripps.yates.client.gui.columns.footers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;

import edu.scripps.yates.client.util.ClientMaths;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AmountBean;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.GeneBean;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;

public class ProteinGroupFooterManager extends FooterManager<ProteinGroupBean> {
	public ProteinGroupFooterManager(DataGrid<ProteinGroupBean> datagrid) {
		super(datagrid);
		final ColumnName[] columnNames = ColumnName.values();
		for (ColumnName columnName : columnNames) {
			switch (columnName) {

			case ACC:
				footers.put(columnName, getEmptyFooter());
				break;
			case GENE:
				footers.put(columnName, getGeneFooter());
				break;
			case SEQUENCE_COUNT:
				footers.put(columnName, getSeqCountFooter());
				break;
			case SPECTRUM_COUNT:
				footers.put(columnName, getSpectrumCountFooter());
				break;
			case NUM_PROTEIN_GROUP_MEMBERS:
				footers.put(columnName, getNumProteinGroupMembersFooter());
				break;
			case SECONDARY_ACCS:
				footers.put(columnName, getNumSecondaryAccsFooter());
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
		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "-";
				} else {
					double sum = 0;
					List<Double> validAmounts = new ArrayList<Double>();
					for (ProteinGroupBean item : visibleItems) {
						boolean validAmount = false;
						Set<AmountBean> amounts = item.getAmountsByExperimentalCondition().get(conditionName);
						if (amounts == null || amounts.isEmpty())
							continue;
						Double amountValue = 0.0;
						// try to convert to double the amountString
						try {
							DataGridRenderValue data = DataGridRenderValue.getAmountDataGridRenderValue(item,
									conditionName, amountType, projectName);
							if (data.getActualNonRoundedValue() != null) {
								amountValue = data.getActualNonRoundedValue();
							} else {
								amountValue = Double.valueOf(data.getValue());
							}
							validAmount = true;
						} catch (NumberFormatException e) {
							double subSum = 0.0;
							for (AmountBean proteinAmountBean : amounts) {
								if (proteinAmountBean.getExperimentalCondition().getProject().getTag()
										.equals(projectName))
									subSum += proteinAmountBean.getValue();
							}
							amountValue = subSum / amounts.size();
							validAmount = true;
						}
						if (validAmount) {
							validAmounts.add(amountValue);
							sum += amountValue;
						}
					}
					if (validAmounts.isEmpty())
						return "-";
					final Double[] amountArray = validAmounts.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.##").format(ClientMaths.mean(amountArray));
					if (validAmounts.size() > 1) {
						ret += " (" + NumberFormat.getFormat("#.##").format(ClientMaths.stddev(amountArray)) + ")";
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
		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					double sum = 0;
					List<Double> validRatios = new ArrayList<Double>();
					for (ProteinGroupBean item : visibleItems) {
						final List<RatioBean> ratios = item.getRatiosByConditions(condition1Name, condition2Name,
								projectTag, ratioName, true);
						if (ratios == null || ratios.isEmpty())
							continue;
						Double ratioValue = 0.0;
						boolean validRatio = false;
						// try to convert to double the amountString
						try {
							ratioValue = Double.valueOf(item.getRatioStringByConditions(condition1Name, condition2Name,
									projectTag, ratioName, true));
							validRatio = true;
						} catch (NumberFormatException e) {
							double subSum = 0.0;
							for (RatioBean ratio : ratios) {
								subSum += ratio.getValue();
							}
							ratioValue = subSum / ratios.size();
							validRatio = true;
						}
						if (validRatio) {
							validRatios.add(ratioValue);
							sum += ratioValue;
						}
					}
					if (validRatios.isEmpty())
						return "-";
					final Double[] ratiosArray = validRatios.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.##").format(ClientMaths.mean(ratiosArray));
					if (validRatios.size() > 1) {
						ret += " (" + NumberFormat.getFormat("#.##").format(ClientMaths.stddev(ratiosArray)) + ")";
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

	private Header<String> getNumProteinGroupMembersFooter() {
		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					List<Integer> nums = new ArrayList<Integer>();
					for (ProteinGroupBean item : visibleItems) {
						final int num = item.size();
						if (num > 0) {
							nums.add(num);
						}
					}
					return NumberFormat.getFormat("#.#").format(ClientMaths.mean(nums.toArray(new Integer[0])));
				}
			}
		};
		return header;
	}

	private Header<String> getSpectrumCountFooter() {
		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					int sum = 0;
					int total = 0;
					for (ProteinGroupBean item : visibleItems) {
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

	private Header<String> getSeqCountFooter() {
		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					int sum = 0;
					int total = 0;
					for (ProteinGroupBean item : visibleItems) {
						final int seqCount = item.getNumPeptides();
						if (seqCount > 0) {
							sum += seqCount;
							total++;
						}
					}
					return NumberFormat.getFormat("#.#").format(Double.valueOf(sum) / total);
				}
			}
		};
		return header;
	}

	private Header<String> getGeneFooter() {
		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					Set<String> geneNames = new HashSet<String>();
					for (ProteinGroupBean item : visibleItems) {
						final List<GeneBean> genes = item.getGenes(false);
						for (GeneBean geneBean : genes) {
							if (geneBean != null) {
								geneNames.add(geneBean.getGeneID());
							} else {
								geneNames.add("-");
							}
						}
					}
					if (geneNames.isEmpty())
						return "-";
					return String.valueOf(geneNames.size());
				}
			}
		};
		return header;
	}

	private Header<String> getEmptyFooter() {
		Header<String> emptyHeader = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				return "-";
			}
		};
		return emptyHeader;
	}

	private Header<String> getNumSecondaryAccsFooter() {
		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					Set<String> set = new HashSet<String>();
					for (ProteinGroupBean item : visibleItems) {
						final List<AccessionBean> secondaryAccessions = item.getSecondaryAccessions();
						if (secondaryAccessions != null) {
							for (AccessionBean accessionBean : secondaryAccessions) {
								set.add(accessionBean.getAccession());
							}
						}

					}
					return String.valueOf(set.size());
				}
			}
		};
		return header;
	}

	@Override
	public Header<String> getRatioScoreFooterByConditions(final String condition1Name, final String condition2Name,
			final String projectTag, final String ratioName) {

		Header<String> header = new Header<String>(new TextCell()) {
			@Override
			public String getValue() {
				List<ProteinGroupBean> visibleItems = dataGrid.getVisibleItems();
				if (visibleItems.size() == 0) {
					return "";
				} else {
					double sum = 0;
					List<Double> validRatios = new ArrayList<Double>();
					for (ProteinGroupBean item : visibleItems) {
						final List<RatioBean> ratios = item.getRatiosByConditions(condition1Name, condition2Name,
								projectTag, ratioName, true);
						if (ratios == null || ratios.isEmpty())
							continue;
						Double ratioValue = 0.0;
						boolean validRatio = false;
						// try to convert to double the amountString
						try {
							ratioValue = Double.valueOf(item.getRatioScoreStringByConditions(condition1Name,
									condition2Name, projectTag, ratioName, true));
							validRatio = true;
						} catch (NumberFormatException e) {
							double subSum = 0.0;
							for (RatioBean ratio : ratios) {
								subSum += ratio.getValue();
							}
							ratioValue = subSum / ratios.size();
							validRatio = true;
						}
						if (validRatio) {
							validRatios.add(ratioValue);
							sum += ratioValue;
						}
					}
					if (validRatios.isEmpty())
						return "-";
					final Double[] ratiosArray = validRatios.toArray(new Double[0]);
					String ret = NumberFormat.getFormat("#.##").format(ClientMaths.mean(ratiosArray));
					if (validRatios.size() > 1) {
						ret += " (" + NumberFormat.getFormat("#.##").format(ClientMaths.stddev(ratiosArray)) + ")";
					}
					return ret;
				}
			}
		};
		return header;

	}
}
