package edu.scripps.yates.excel.proteindb.agent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import edu.scripps.yates.agent.DataProvider;
import edu.scripps.yates.dtaselect.ProteinDTASelectParser;
import edu.scripps.yates.excel.ExcelReader;
import edu.scripps.yates.excel.proteindb.ProteinImpl2;
import edu.scripps.yates.excel.proteindb.ProteinSetAdapter;
import edu.scripps.yates.excel.util.ColumnIndexManager;
import edu.scripps.yates.excel.util.ColumnIndexManager.ColumnName;
import edu.scripps.yates.excel.util.NewRunsMapReaderFromTxt;
import edu.scripps.yates.persistence.memory.DataManagerMemory;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.enums.AmountType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.model.factories.ProteinEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinProviderFromExcel implements DataProvider<Protein> {
	// public final static String defaultFilePath =
	// "C:\\Users\\Salva\\Desktop\\Dropbox\\Scripps\\Sandra\\new
	// files\\main_data_part1_2_test.xls";
	public final static String defaultFilePath = "C:\\Users\\Salva\\Desktop\\Dropbox\\Scripps\\Sandra\\new files\\main_data_part1_2.xls";

	public final static String defaultMasterProteinListFilePath = "C:\\Users\\Salva\\Desktop\\Dropbox\\Scripps\\Sandra\\new files\\2291_IDs.xls";
	private static Logger log = Logger.getLogger(ProteinProviderFromExcel.class);
	/**
	 * Map for storing the object instances of dta select psms, in order to not
	 * create different instances of PSMImpl from the same DTASelectPSM instance
	 * object
	 */
	private static TIntObjectHashMap<PSM> dtaSelectPSMsMap = new TIntObjectHashMap<PSM>();
	private static TIntObjectHashMap<Peptide> dtaSelectPeptideMap = new TIntObjectHashMap<Peptide>();

	// private final HashMap<String, Protein> proteinSetByPrimaryAcc = new
	// HashMap<String, Protein>();
	// private final HashMap<String, Protein> proteinListByUniprotAcc = new
	// HashMap<String, Protein>();
	// private final HashMap<String, Protein> proteinListByIPIAcc = new
	// HashMap<String, Protein>();
	// private final HashMap<String, Protein> proteinList = new THashMap<String,
	// Protein>();

	public static final DataManagerMemory datamanager = new DataManagerMemory();
	private final int[] sheetsToReadIndexes = { 0, 1 };
	private final Set<MSRun> msRuns = new THashSet<MSRun>();
	private int maxNumProteinsToLoad = Integer.MAX_VALUE;
	private Set<Protein> proteinSet = new THashSet<Protein>();
	private Map<String, Set<Protein>> proteinMap;

	private Map<String, Set<Protein>> extendedMap;

	private final String actualFilePath;
	private static String masterProteinListFilePath;
	public static final Map<MSRun, Condition> conditionsPerRun = new THashMap<MSRun, Condition>();

	public ProteinProviderFromExcel(File file, File masterFile) {
		this(false, file, masterFile);

	}

	public ProteinProviderFromExcel() {

		this(false);

	}

	public ProteinProviderFromExcel(boolean loadPSMsFromIP2) {
		actualFilePath = defaultFilePath;
		masterProteinListFilePath = defaultMasterProteinListFilePath;
		init(loadPSMsFromIP2);
	}

	public ProteinProviderFromExcel(boolean loadPSMsFromIP2, File file, File masterFile) {
		if (file == null) {
			actualFilePath = defaultFilePath;
		} else {
			actualFilePath = file.getAbsolutePath();
		}
		if (masterFile != null) {
			masterProteinListFilePath = masterFile.getAbsolutePath();
		} else {
			masterProteinListFilePath = defaultMasterProteinListFilePath;
		}
		init(loadPSMsFromIP2);
	}

	public ProteinProviderFromExcel(boolean loadPSMsFromIP2, int maxNumProteinsToLoad) {
		actualFilePath = defaultFilePath;
		masterProteinListFilePath = defaultMasterProteinListFilePath;
		this.maxNumProteinsToLoad = maxNumProteinsToLoad;
		init(loadPSMsFromIP2);
	}

	public ProteinProviderFromExcel(int maxNumProteinsToLoad) {
		actualFilePath = defaultFilePath;
		masterProteinListFilePath = defaultMasterProteinListFilePath;
		this.maxNumProteinsToLoad = maxNumProteinsToLoad;
		init(false);

	}

	private void init(boolean loadPSMsFromIP2) {

		// load proteins
		loadProteinsFromExcel();

		if (loadPSMsFromIP2)
			loadPSMs();

	}

	/**
	 * @return the datamanager
	 */
	public DataManagerMemory getDataManager() {
		return datamanager;
	}

	/**
	 * Get the protein list (key=accession, list<String>=list of gene symbols)
	 * that will serve as a reference list, which means that other protein not
	 * included here, will be discarded.
	 *
	 * @return
	 */
	public Map<String, List<String>> getMasterProteinList() {
		Map<String, List<String>> ret = new THashMap<String, List<String>>();
		try {
			ExcelReader reader = new ExcelReader(ProteinProviderFromExcel.masterProteinListFilePath, 0, 0);

			int rowNumber = 1;
			Map<String, String> columnValuePairs = reader.getColumnValuePairs(0, rowNumber);
			while (true) {
				final String geneSymbols = columnValuePairs.get("Official Gene symbol");
				final String locus = columnValuePairs.get("locus");
				if (locus == null)
					break;
				List<String> geneSymbolsList = new ArrayList<String>();
				if (geneSymbols.contains(";")) {
					final String[] split = geneSymbols.split(";");
					for (String string : split) {
						geneSymbolsList.add(string);
					}
				} else {
					geneSymbolsList.add(geneSymbols);
				}

				if (!ret.containsKey(locus)) {
					ret.put(locus, geneSymbolsList);
				} else {
					throw new IllegalArgumentException("Repeated locus: " + locus);
				}
				columnValuePairs = reader.getColumnValuePairs(0, ++rowNumber);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ret;
	}

	private void loadPSMs() {
		try {
			RemoteSSHFileReference shamuServer = new RemoteSSHFileReference();
			// per each run, get the dta select from Shamu server

			for (MSRun msRun : msRuns) {
				log.info(msRun.getRunId());
				String path = msRun.getPath();
				if (path == null || "".equals(path)
				// || !path.contains("ip2_data"))
				)
					continue;
				// path = path.substring(path.indexOf("ip2_data"));

				if (RemoteSSHFileReference.failedConnectionAttemps <= RemoteSSHFileReference.MAX_CONNECTIONS_ATTEMPS) {
					final File dtaSelectFile = shamuServer.getRemoteFile(path);
					if (dtaSelectFile != null) {
						ProteinDTASelectParser parser;
						parser = new ProteinDTASelectParser(dtaSelectFile.toURL());

						dtaSelectFile.delete();
						final Map<String, Set<Protein>> proteinsFromDTASelect = parser.getProteins();
						log.debug(proteinsFromDTASelect.size() + " proteins parsed");
						for (String proteinACC : proteinsFromDTASelect.keySet()) {
							final Set<Protein> proteinSet = proteinsFromDTASelect.get(proteinACC);
							for (Protein dtaSelectProtein : proteinSet) {

								dtaSelectProtein.setMSRun(msRun);

								String proteinAccNoPoint = proteinACC;
								// remove the point IPI123123.1 to IPI123123
								final int indexOfPoint = proteinAccNoPoint.indexOf(".");
								if (indexOfPoint > -1) {
									proteinAccNoPoint = proteinAccNoPoint.substring(0, indexOfPoint);
								}
								// in case of processing mock runs or silent
								// runs,
								// get the protein information and
								// quantification
								// information from the dtaselect file.
								boolean isABackgroundProtein = false;
								if (msRun.getRunId().contains("mock") || msRun.getRunId().contains("silent")) {
									isABackgroundProtein = true;
								}
								if (getExtendedMap().containsKey(proteinAccNoPoint)) {
									Collection<Protein> proteins = getExtendedMap().get(proteinAccNoPoint);
									for (Protein protein : proteins) {
										if (!protein.getMSRun().getRunId().equals(msRun.getRunId()))
											continue;
										if (protein.getPrimaryAccession().getAccession().equals("IPI00218570")
												|| protein.getPrimaryAccession().getAccession().equals("P15259")) {
											System.out.println(protein.getAccession());
										}
										// ProteinImpl2 proteinImpl =
										// (ProteinImpl2) protein;
										// protein.setMSRun(msRun);
										// add protein features

										if (dtaSelectProtein.getLength() > 0)
											protein.setLength(dtaSelectProtein.getLength());
										protein.setMw(dtaSelectProtein.getMW());
										protein.setPi(dtaSelectProtein.getPi());

										final Set<Peptide> dtaSelectPeptides = dtaSelectProtein.getPeptides();
										for (Peptide dtaSelectPeptide : dtaSelectPeptides) {
											dtaSelectPeptide.setMSRun(msRun);
											if (!ProteinProviderFromExcel.dtaSelectPeptideMap
													.containsKey(dtaSelectPeptide.hashCode())) {

												ProteinProviderFromExcel.dtaSelectPeptideMap
														.put(dtaSelectPeptide.hashCode(), dtaSelectPeptide);
												// protein.addPeptide(dtaSelectPeptide);
											}
											protein.addPeptide(dtaSelectPeptide);
											for (PSM dtaSelectPSM : dtaSelectPeptide.getPSMs()) {
												dtaSelectPSM.setPeptide(dtaSelectPeptide);
												dtaSelectPSM.setMSRun(msRun);
												if (!ProteinProviderFromExcel.dtaSelectPSMsMap
														.containsKey(dtaSelectPSM.hashCode())) {

													ProteinProviderFromExcel.dtaSelectPSMsMap
															.put(dtaSelectPSM.hashCode(), dtaSelectPSM);

													// dtaSelectPeptide
													// .addPSM(dtaSelectPSM);
													//
													// protein.addPSM(dtaSelectPSM);
												}
												dtaSelectPeptide.addPSM(dtaSelectPSM);

												protein.addPSM(dtaSelectPSM);
											}
										}
										// see if there is not protein name in
										// the
										// primary accession
										final Accession primaryAccession = protein.getPrimaryAccession();
										final String description = primaryAccession.getDescription();
										if (description == null || "".equals(description)) {
											if (primaryAccession instanceof AccessionEx) {
												AccessionEx accEx = (AccessionEx) primaryAccession;
												accEx.setDescription(
														dtaSelectProtein.getPrimaryAccession().getDescription());
											}
										}
										if (isABackgroundProtein) {
											// get the amount from the spectral
											// count value in dta select in case
											// of
											// BACKGROUND
											if (dtaSelectProtein.getPSMs() != null) {
												AmountEx proteinAmount = new AmountEx(
														Double.valueOf(dtaSelectProtein.getPSMs().size()),
														AmountType.SPC, getBackgroundCondition());
												protein.addAmount(proteinAmount);
											}
										}

									}
								} else if (isABackgroundProtein) {
									Protein backgroundProtein = new ProteinEx(AccessionType.IPI, proteinAccNoPoint);

									if (dtaSelectProtein.getLength() > 0)
										backgroundProtein.setLength(dtaSelectProtein.getLength());
									backgroundProtein.setMw(dtaSelectProtein.getMW());
									backgroundProtein.setPi(dtaSelectProtein.getPi());
									// get the amount
									if (dtaSelectProtein.getPSMs() != null) {
										Amount proteinAmount = new AmountEx(
												Double.valueOf(dtaSelectProtein.getPSMs().size()), AmountType.SPC,
												getBackgroundCondition());
										backgroundProtein.addAmount(proteinAmount);
									}
									// proteinListByIPIAcc.put(proteinAccNoPoint,
									// backgroundProtein);
									// proteinList.put(proteinAccNoPoint,
									// backgroundProtein);
									// proteinSetByPrimaryAcc.put(proteinAccNoPoint,
									// backgroundProtein);
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Set<MSRun> getMSRuns() {
		Set<MSRun> set = new THashSet<MSRun>();

		List<ColumnName> list = new ArrayList<ColumnName>();
		list.addAll(getColumnNames(ProteinImpl2.expression_analysis_TMT__126_label));
		list.addAll(getColumnNames(ProteinImpl2.expression_analysis_TMT__126_label2));
		set.addAll(getMSRunsFromList(list, null));
		list.clear();

		list.addAll(getColumnNames(ProteinImpl2.wt_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.WT));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2.mut_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.MUT));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2.background_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.BACKGROUND));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2.tsa_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.TSA));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2.saha_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.SAHA));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2.dmso_column));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.DMSO));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2.siHDAC7_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.SCRAMBLED_siRNA));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2.corr4a_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName.CORR4A));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2._1h_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName._1H_30C));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2._6h_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName._6H_30C));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2._24h_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName._24H_30C));
		list.clear();
		list.addAll(getColumnNames(ProteinImpl2._24hrev_columns));
		set.addAll(getMSRunsFromList(list, edu.scripps.yates.excel.proteindb.enums.ConditionName._24h_30C_14h_37C));

		return set;
	}

	/**
	 *
	 * @param list
	 * @param condition
	 *            in order to create the map between conditions and runs
	 * @return
	 */
	private Collection<MSRun> getMSRunsFromList(List<ColumnName> list,
			edu.scripps.yates.excel.proteindb.enums.ConditionName condition) {
		Set<MSRun> set = new THashSet<MSRun>();

		for (ColumnName columnName : list) {
			final MSRun msRun = datamanager.getMSRun(columnName.name(),
					NewRunsMapReaderFromTxt.getPathByReplicateName(columnName.name()));
			if (msRun != null) {
				Sample sample = ProteinSetAdapter.getSample(condition.name(), false, false,
						ProteinSetAdapter.humanOrganism, ProteinSetAdapter.tissue);
				if (condition != null)
					conditionsPerRun.put(msRun, ProteinSetAdapter.getExperimentalCondition(condition, null, sample));
				set.add(msRun);
			}
		}
		return set;
	}

	private Collection<ColumnName> getColumnNames(ColumnName[] columnNames) {
		List<ColumnName> list = new ArrayList<ColumnName>();
		for (ColumnName columnName : columnNames) {
			list.add(columnName);
		}
		return list;
	}

	private edu.scripps.yates.utilities.proteomicsmodel.Condition getBackgroundCondition() {
		return ProteinSetAdapter.getExperimentalCondition(
				edu.scripps.yates.excel.proteindb.enums.ConditionName.BACKGROUND, "Background", null);
	}

	private Map<String, Double> readDMSOSumValues(Sheet sheet) {
		Map<String, Double> ret = new THashMap<String, Double>();
		final int lastRowNum = sheet.getLastRowNum();
		for (int rownum = 1; rownum <= lastRowNum; rownum++) {
			Row row = sheet.getRow(rownum);
			if (row != null && row.getRowNum() > 0) {
				String ipi = row.getCell(0).getStringCellValue();
				Double dmso_sum;
				try {
					dmso_sum = row.getCell(1).getNumericCellValue();
				} catch (Exception e) {
					dmso_sum = Double.valueOf(row.getCell(1).getStringCellValue());
				}
				if (dmso_sum != null)
					ret.put(ipi, dmso_sum);
			}
		}
		return ret;
	}

	@Override
	public Map<String, Set<Protein>> getProteinMap() {
		if (proteinMap == null) {
			proteinMap = new THashMap<String, Set<Protein>>();
			final Set<Protein> proteinSet2 = getProteinSet();
			ModelUtils.addToMap(proteinMap, proteinSet2);
		}
		return proteinMap;
	}

	/**
	 * Gets the map of the proteins by all the accessions, not only by the
	 * primary one
	 *
	 * @return
	 */
	private Map<String, Set<Protein>> getExtendedMap() {
		if (extendedMap == null) {
			extendedMap = new THashMap<String, Set<Protein>>();
			final Collection<Set<Protein>> proteinSets = getProteinMap().values();
			for (Set<Protein> proteinSet : proteinSets) {
				for (Protein protein : proteinSet) {
					// primary
					final String accession = protein.getPrimaryAccession().getAccession();
					if (extendedMap.containsKey(accession)) {
						extendedMap.get(accession).add(protein);
					} else {
						Set<Protein> set = new THashSet<Protein>();
						set.add(protein);
						extendedMap.put(accession, set);
					}
					final List<Accession> secondaryAccessions = protein.getSecondaryAccessions();
					for (Accession secondaryAccession : secondaryAccessions) {
						final String accession2 = secondaryAccession.getAccession();
						if (extendedMap.containsKey(accession2)) {
							extendedMap.get(accession2).add(protein);
						} else {
							Set<Protein> set = new THashSet<Protein>();
							set.add(protein);
							extendedMap.put(accession2, set);
						}
					}
				}
			}
		}
		return extendedMap;
	}

	private Set<Protein> getProteinSet() {
		if (proteinSet == null) {
			proteinSet = new THashSet<Protein>();
			loadProteinsFromExcel();
			// for (Protein protein : proteinSetByPrimaryAcc.values()) {
			// proteinSet.add(protein);
			// }
		}
		return proteinSet;
	}

	private void loadProteinsFromExcel() {
		if (proteinSet.isEmpty()) {
			ExcelReader reader;
			try {

				reader = new ExcelReader(actualFilePath, 0, 1);
				msRuns.addAll(getMSRuns());
				Workbook workbook = reader.getWorkbook();
				// ProteinImpl previousProtein = null;
				ProteinSetAdapter currentProteinSetAdapter = null;
				String currentIpiAccString = null;
				final Map<String, List<String>> masterProteinList = getMasterProteinList();
				Sheet firstSheet = workbook.getSheetAt(0);
				for (int rownum = 1; rownum <= firstSheet.getLastRowNum(); rownum++) {
					Row row = firstSheet.getRow(rownum);
					log.info("Row " + rownum + "/" + firstSheet.getLastRowNum());
					if (row != null && row.getRowNum() > 0) {
						final String ipiAccString = getCell(row, ColumnName.LOCUS, reader.getColumnIndexManager())
								.getStringCellValue();
						if (ipiAccString == null || "".equals(ipiAccString)) {
							currentProteinSetAdapter.addRow(row, 0);
						} else {

							// check if the current IPI should be stored
							if (masterProteinList.containsKey(currentIpiAccString)) {
								proteinSet.addAll(currentProteinSetAdapter.adapt());
							}
							currentIpiAccString = ipiAccString;
							currentProteinSetAdapter = new ProteinSetAdapter(row, 0, reader.getColumnIndexManager());
							// add the rows of the protein in subsequent sheets,
							// assuming that the information of the protein is
							// located in the same row number
							for (int i = 1; i < sheetsToReadIndexes.length; i++) {
								final Sheet sheetAt = workbook.getSheetAt(sheetsToReadIndexes[i]);
								final Row row2 = sheetAt.getRow(rownum);
								if (row2 != null) {
									currentProteinSetAdapter.addRow(row2, i);
								}
							}

						}
					} else {
						// add the rows of the protein in subsequent sheets,
						// assuming that the information of the protein is
						// located in the same row number
						for (int i = 1; i < sheetsToReadIndexes.length; i++) {
							final Sheet sheetAt = workbook.getSheetAt(sheetsToReadIndexes[i]);
							final Row row2 = sheetAt.getRow(rownum);
							if (row2 != null) {
								currentProteinSetAdapter.addRow(row2, i);
							}
						}
					}
					// if the maximum number of proteins is reached
					if (proteinSet.size() == maxNumProteinsToLoad)
						break;
				}
				// Add the previous Protein adapter just if is
				// present in the master list
				if (masterProteinList.containsKey(currentIpiAccString)) {
					proteinSet.addAll(currentProteinSetAdapter.adapt());
				}

				// read the third sheet, where the DMSO_SUM is located
				// and add it to the proteins from the replicates of DMSO
				Map<String, Double> dmso_sumValues = readDMSOSumValues(workbook.getSheetAt(2));
				for (Protein protein : proteinSet) {
					final MSRun msRun = protein.getMSRun();
					if (msRun != null) {
						for (ColumnName columnName : ProteinImpl2.dmso_column) {
							final String pathByReplicateName = NewRunsMapReaderFromTxt
									.getPathByReplicateName(columnName.name());
							final String path = msRun.getPath();
							if (pathByReplicateName.contains(path)) {
								final String ipiAccession = ModelUtils.getAccessions(protein, AccessionType.IPI).get(0)
										.getAccession();
								if (ipiAccession != null) {
									if (dmso_sumValues.containsKey(ipiAccession))
										((ProteinImpl2) protein).addDMSOSumAmount(dmso_sumValues.get(ipiAccession));
								}
							}
						}
					}
					// get the msRuns from IP2
					// final Set<MSRun> msRuns2 = protein.getMSRuns();
					// if (msRuns2 != null && !msRuns2.isEmpty()) {
					// msRuns.addAll(msRuns2);
					// }
				}
				log.info(proteinSet.size() + " proteins readed");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	private Cell getCell(Row row, ColumnName columnName, ColumnIndexManager columnIndexManager) {
		Cell cell = row.getCell(columnIndexManager.getColumnIndex(columnName));
		return cell;
	}

	public List<Protein> get(String accession) {
		List<Protein> list = new ArrayList<Protein>();
		final Map<String, Set<Protein>> map = getProteinMap();
		for (Set<Protein> proteinSet : map.values()) {
			for (Protein protein : proteinSet) {
				final Accession primaryAccession = protein.getPrimaryAccession();
				final List<Accession> secondaryAccessions = protein.getSecondaryAccessions();
				if (primaryAccession.getAccession().equals(accession)) {
					list.add(protein);
					continue;
				} else {
					for (Accession accession2 : secondaryAccessions) {
						if (accession2.getAccession().equals(accession)) {
							list.add(protein);
							break;
						}
					}
				}
			}
		}
		return list;
	}

}
