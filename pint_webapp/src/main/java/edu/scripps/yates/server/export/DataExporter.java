package edu.scripps.yates.server.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gwt.user.client.Window;

import edu.scripps.yates.client.exceptions.PintException;
import edu.scripps.yates.client.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.server.DataSet;
import edu.scripps.yates.server.DataSetsManager;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServerDataUtils;
import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnProvider;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.columns.ProteinGroupColumns;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinEvidence;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.ScoreBean;
import edu.scripps.yates.shared.util.DataGridRenderValue;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtils;

public class DataExporter {
	private static final Logger log = Logger.getLogger(DataExporter.class);
	private static final String NEW_LINE = System.lineSeparator();
	private static final String TAB = "\t";
	private static final String PROT = "PROT";
	private static final String PSM = "PSM";
	private static final String PG = "PG";
	private static final String EMPTY = "-";

	public static File existsFileProteinsFromProjects(List<String> projectTags, String projectFilesPath)
			throws PintException {

		String projectsStringTag = SharedDataUtils.getProjectTagCollectionKey(projectTags);
		File newFile = FileManager.getDownloadFile(projectFilesPath, "proteins-" + projectsStringTag + ".csv");
		if (newFile.exists() && newFile.length() > 0l)
			return newFile;
		return null;
	}

	public static File exportProteinsFromProjects(List<String> projectTags, String projectFilesPath, String omimAPIKey)
			throws PintException {

		String projectsStringTag = SharedDataUtils.getProjectTagCollectionKey(projectTags);
		File newFile = FileManager.getDownloadFile(projectFilesPath, "proteins-" + projectsStringTag + ".csv");
		if (newFile.exists() && newFile.length() > 0)
			return newFile;
		// File newFile = new File(FileDownloadServlet.TEMP_FOLDER_LOCATION
		// + File.separator + "proteins" + new Random().nextLong()
		// + ".csv");
		final Set<String> hiddenPTMs = RemoteServicesTasks.getHiddenPTMs(projectTags, projectFilesPath);
		log.info("Exporting proteins at file created at: " + newFile.getAbsolutePath());
		FileWriter fw = null;
		try {
			fw = new FileWriter(newFile);
			Map<String, List<String>> conditionsByProject = getConditionsByProjects(projectTags);
			final Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects = getRatioDescriptorsByProjects(
					projectTags);
			List<String> psmScoreNames = RemoteServicesTasks.getPSMScoreNamesFromProjects(projectTags);
			final List<String> ptmScoreNames = RemoteServicesTasks.getPTMScoreNamesFromProjects(projectTags);
			// HEADERS
			writeHeader(fw, projectTags, conditionsByProject, ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames,
					ProteinColumns.getInstance(), PSMColumns.getInstance());

			for (String projectTag : projectTags) {
				log.info("Exporting proteins from project: " + projectTag);
				final Map<String, Set<Protein>> proteinMap = RemoteServicesTasks.getProteinsFromProject(null,
						projectTag, null, hiddenPTMs, omimAPIKey);

				final Set<ProteinBean> proteinBeans = RemoteServicesTasks.createProteinBeans(null, proteinMap,
						hiddenPTMs);

				int i = 0;
				log.info("Now iterating proteins to write the file");
				if (proteinBeans.isEmpty()) {
					throw new PintException("No proteins to export", PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
				}
				for (ProteinBean proteinBean : proteinBeans) {
					// log.info(++i + "/" + total + " protein");
					writeProteinRow(fw, proteinBean, projectTag, projectTags, conditionsByProject,
							ratioDescriptorsByProjects);
					final Set<Integer> psmIds = proteinBean.getPSMDBIds();
					if (psmIds != null && !psmIds.isEmpty()) {
						List<PSMBean> psms = proteinBean.getPsms();
						// RemoteServicesTasks.getPSMsFromProtein(sessionID,
						// proteinBean, false);

						for (PSMBean psmBean : psms) {
							writePSMRow(fw, psmBean, projectTag, projectTags, conditionsByProject,
									ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames);
						}
					}
				}
				log.info("End iteration");
			}
			return newFile;
		} catch (IOException e) {
			e.printStackTrace();
			if (newFile != null && newFile.exists()) {
				newFile.delete();
			}
			throw new PintException(e, PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);

		} finally {
			log.info("closing file writer");
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static File existsFileProteinGroupsFromProjects(List<String> projectTags,
			boolean separateNonConclusiveProteins, String projectFilesPath) throws PintException {

		String projectsStringTag = SharedDataUtils.getProjectTagCollectionKey(projectTags);
		File newFile = FileManager.getDownloadFile(projectFilesPath, "proteinGroups-" + projectsStringTag + ".csv");
		if (newFile.exists() && newFile.length() > 0)
			return newFile;
		return null;
	}

	public static File exportProteinGroupsFromProjects(List<String> projectTags, boolean separateNonConclusiveProteins,
			String projectFilesPath, String omimAPIKey) throws PintException {

		String projectsStringTag = SharedDataUtils.getProjectTagCollectionKey(projectTags);
		File newFile = FileManager.getDownloadFile(projectFilesPath, "proteinGroups-" + projectsStringTag + ".csv");
		if (newFile.exists() && newFile.length() > 0)
			return newFile;
		Set<String> hiddenPTMs = RemoteServicesTasks.getHiddenPTMs(projectTags, projectFilesPath);
		// File newFile = new File(FileDownloadServlet.TEMP_FOLDER_LOCATION
		// + File.separator + "proteinGroups" + new Random().nextLong()
		// + ".csv");
		log.info("Exporting protein groups at file created at: " + newFile.getAbsolutePath());
		FileWriter fw = null;
		try {
			fw = new FileWriter(newFile);
			Map<String, List<String>> conditionsByProject = getConditionsByProjects(projectTags);
			final Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects = getRatioDescriptorsByProjects(
					projectTags);
			List<String> psmScoreNames = RemoteServicesTasks.getPSMScoreNamesFromProjects(projectTags);
			final List<String> ptmScoreNames = RemoteServicesTasks.getPTMScoreNamesFromProjects(projectTags);
			// HEADERS
			writeHeader(fw, projectTags, conditionsByProject, ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames,
					ProteinGroupColumns.getInstance(), PSMColumns.getInstance());

			for (String projectTag : projectTags) {

				Map<String, Set<Protein>> proteinMap = RemoteServicesTasks.getProteinsFromProject(null, projectTag,
						null, hiddenPTMs, omimAPIKey);
				Set<ProteinBean> proteins = RemoteServicesTasks.createProteinBeans(null, proteinMap, hiddenPTMs);
				if (proteins.isEmpty()) {
					throw new PintException("No proteins to export", PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
				}
				final List<ProteinGroupBean> proteinGroups = RemoteServicesTasks.groupProteins(proteins,
						separateNonConclusiveProteins);
				for (ProteinGroupBean proteinGroup : proteinGroups) {
					writeProteinGroupRow(fw, proteinGroup, projectTag, projectTags, conditionsByProject,
							ratioDescriptorsByProjects);
					final List<PSMBean> psms = proteinGroup.getPsms();
					if (psms != null && !psms.isEmpty()) {
						for (PSMBean psmBean : psms) {
							writePSMRow(fw, psmBean, projectTag, projectTags, conditionsByProject,
									ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames);
						}
					}
				}
			}
			return newFile;
		} catch (IOException e) {
			e.printStackTrace();
			if (newFile != null && newFile.exists()) {
				newFile.delete();
			}
			throw new PintException(e, PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void writePSMRow(FileWriter fw, PSMBean psmBean, String actualprojectTag, List<String> projectTags,
			Map<String, List<String>> conditionsByProject,
			Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects, List<String> psmScoreNames,
			List<String> ptmScoreNames) throws IOException, PintException {
		// line identifier: PSM
		fw.write(PSM);
		fw.write(TAB);
		// project
		fw.write(actualprojectTag);
		fw.write(TAB);
		if (psmBean.getPsmID().equals("Hsilam-fmrKO1P17-ctx-p2-09.10750.10750.2")) {
			System.out.println("asf");
		}
		final List<ColumnWithVisibility> psmColumns = PSMColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : psmColumns) {
			if (columnWithOrder.getColumn() == ColumnName.PSM_AMOUNT) {
				// iterate over projects
				for (String projectTag : projectTags) {
					List<String> conditions = conditionsByProject.get(projectTag);
					// iterate over conditions
					for (String conditionName : conditions) {
						final Set<AmountType> amountTypesByCondition = RemoteServicesTasks
								.getPSMAmountTypesByCondition(projectTag, conditionName);
						if (amountTypesByCondition == null || amountTypesByCondition.isEmpty()) {
							fw.write(EMPTY + TAB);
							continue;
						}
						// sort the amounts in a consistent way
						List<AmountType> amountTypeList = new ArrayList<AmountType>();
						amountTypeList.addAll(amountTypesByCondition);
						Collections.sort(amountTypeList, new Comparator<AmountType>() {

							@Override
							public int compare(AmountType o1, AmountType o2) {
								return o1.name().compareTo(o2.name());
							}
						});
						StringBuilder sb = new StringBuilder();
						for (AmountType amountType : amountTypeList) {
							if (actualprojectTag.equals(projectTag)) {
								final String amoutValue = DataGridRenderValue
										.getAmountDataGridRenderValue(psmBean, conditionName, amountType, projectTag)
										.getValue();
								final String parsedValue = parseValue(amoutValue);
								if (!"".equals(sb.toString())) {
									sb.append(",");
								}
								sb.append(parsedValue);
								// fw.write(psmBean.getAmountString(condition,
								// actualprojectTag));

							} else {
								sb.append(EMPTY);
							}
						}
						fw.write(sb.toString() + TAB);
					}
				}
			} else if (columnWithOrder.getColumn() == ColumnName.PSM_RATIO) {
				// iterate over projects
				for (String projectTag : projectTags) {
					List<RatioDescriptorBean> ratioDescriptors = ratioDescriptorsByProjects.get(projectTag);
					if (ratioDescriptors != null) {
						// iterate over ratioDescriptors
						for (RatioDescriptorBean ratioDescriptor : ratioDescriptors) {
							if (actualprojectTag.equals(projectTag)) {
								final String ratioStringByConditions = psmBean.getRatioStringByConditions(
										ratioDescriptor.getCondition1Name(), ratioDescriptor.getCondition2Name(),
										ratioDescriptor.getProjectTag(), ratioDescriptor.getRatioName(), false);
								String parsedValue = parseValue(ratioStringByConditions);
								if ("".equals(parsedValue)) {
									parsedValue = EMPTY;
								}
								fw.write(parsedValue);
								fw.write(TAB);
							} else {
								fw.write(EMPTY);
								fw.write(TAB);
							}
						}
					}
				}
			} else if (columnWithOrder.getColumn() == ColumnName.PSM_SCORE) {
				// iterate over scoreNames
				for (String psmScoreName : psmScoreNames) {
					final ScoreBean scoreByName = psmBean.getScoreByName(psmScoreName);
					if (scoreByName != null) {
						fw.write(scoreByName.getParsedValue() + TAB);
					} else {
						fw.write(EMPTY);
						fw.write(TAB);
					}
				}
			} else if (columnWithOrder.getColumn() == ColumnName.PTM_SCORE) {
				// iterate over scoreNames
				if (ptmScoreNames.isEmpty()) {
					fw.write(EMPTY);
					fw.write(TAB);
				} else {
					for (String ptmScoreName : ptmScoreNames) {
						final String ptmScoreString = psmBean.getPTMScoreString(ptmScoreName);
						if (ptmScoreString != null && !"".equals(ptmScoreString)) {
							fw.write(ptmScoreString + TAB);
						} else {
							fw.write(EMPTY);
							fw.write(TAB);
						}
					}
				}
			} else {
				final String value = PSMColumns.getInstance().getValue(columnWithOrder.getColumn(), psmBean, null, null,
						actualprojectTag, null, null, null, false);
				String parseValue = parseValue(value);
				if ("".equals(parseValue)) {
					parseValue = EMPTY;
				}
				fw.write(parseValue);
				fw.write(TAB);
			}
		}
		fw.write(NEW_LINE);

	}

	private static void writeProteinRow(FileWriter fw, ProteinBean proteinBean, String actualprojectTag,
			List<String> projectTags, Map<String, List<String>> conditionsByProject,
			Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects) throws IOException, PintException {
		// log.info("writing row for "
		// + proteinBean.getPrimaryAccession().getAccession());
		// line identifier: PROT
		fw.write(PROT + TAB);
		// project
		fw.write(actualprojectTag + TAB);

		// protein header
		final List<ColumnWithVisibility> proteinColumns = ProteinColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : proteinColumns) {
			if (columnWithOrder.getColumn() == ColumnName.PROTEIN_AMOUNT) {

				// iterate over projects
				for (String projectTag : projectTags) {
					List<String> conditions = conditionsByProject.get(projectTag);
					// iterate over conditions
					for (String conditionName : conditions) {
						final Set<AmountType> amountTypesByCondition = RemoteServicesTasks
								.getProteinAmountTypesByCondition(projectTag, conditionName);
						// sort the amounts in a consistent way
						List<AmountType> amountTypeList = new ArrayList<AmountType>();
						amountTypeList.addAll(amountTypesByCondition);
						Collections.sort(amountTypeList, new Comparator<AmountType>() {

							@Override
							public int compare(AmountType o1, AmountType o2) {
								return o1.name().compareTo(o2.name());
							}
						});
						for (AmountType amountType : amountTypeList) {
							if (projectTag.equals(actualprojectTag)) {
								final String value = DataGridRenderValue.getAmountDataGridRenderValue(proteinBean,
										conditionName, amountType, projectTag).getValue();
								final String parseValue = parseValue(value);
								fw.write(parseValue);
								// fw.write(parseValue(proteinBean.getAmountString(
								// condition, projectTag)));
								fw.write(TAB);
							} else {
								fw.write(EMPTY);
								fw.write(TAB);
							}
						}
					}
				}
			} else if (columnWithOrder.getColumn() == ColumnName.PROTEIN_RATIO) {
				// iterate over projects
				for (String projectTag : projectTags) {
					List<RatioDescriptorBean> ratioDescriptors = ratioDescriptorsByProjects.get(projectTag);
					if (ratioDescriptors != null) {
						// iterate over ratioDescriptors
						for (RatioDescriptorBean ratioDescriptor : ratioDescriptors) {
							if (projectTag.equals(actualprojectTag)) {
								fw.write(parseValue(proteinBean.getRatioStringByConditions(
										ratioDescriptor.getCondition1Name(), ratioDescriptor.getCondition2Name(),
										ratioDescriptor.getProjectTag(), ratioDescriptor.getRatioName(), false)));
								fw.write(TAB);
							} else {
								fw.write(EMPTY);
								fw.write(TAB);
							}
						}
					}
				}
			} else {
				final String value = ProteinColumns.getInstance().getValue(columnWithOrder.getColumn(), proteinBean,
						null, null, actualprojectTag, null, null, null, false);
				String parseValue = parseValue(value);

				fw.write(parseValue);
				fw.write(TAB);

			}
		}
		fw.write(NEW_LINE);
	}

	private static void writeProteinGroupRow(FileWriter fw, ProteinGroupBean proteinGroup, String actualprojectTag,
			List<String> projectTags, Map<String, List<String>> conditionsByProject,
			Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects) throws IOException, PintException {
		// line identifier: PROT
		fw.write(PG + TAB);
		// project
		fw.write(actualprojectTag + TAB);

		// protein header
		final List<ColumnWithVisibility> proteinColumns = ProteinGroupColumns.getInstance().getColumns();
		for (ColumnWithVisibility columnWithOrder : proteinColumns) {
			if (columnWithOrder.getColumn() == ColumnName.PROTEIN_AMOUNT) {
				// iterate over projects
				for (String projectTag : projectTags) {
					List<String> conditions = conditionsByProject.get(projectTag);
					// iterate over conditions
					for (String conditionName : conditions) {
						final Set<AmountType> amountTypesByCondition = RemoteServicesTasks
								.getProteinAmountTypesByCondition(projectTag, conditionName);
						// sort the amounts in a consistent way
						List<AmountType> amountTypeList = new ArrayList<AmountType>();
						amountTypeList.addAll(amountTypesByCondition);
						Collections.sort(amountTypeList, new Comparator<AmountType>() {

							@Override
							public int compare(AmountType o1, AmountType o2) {
								return o1.name().compareTo(o2.name());
							}
						});
						for (AmountType amountType : amountTypeList) {
							if (projectTag.equals(actualprojectTag)) {
								fw.write(parseValue(DataGridRenderValue.getAmountDataGridRenderValue(proteinGroup,
										conditionName, amountType, projectTag).getValue()));
								// fw.write(parseValue(proteinGroup.getAmountString(
								// condition, projectTag)));
								fw.write(TAB);
							} else {
								fw.write(EMPTY);
								fw.write(TAB);
							}
						}
					}
				}
			} else if (columnWithOrder.getColumn() == ColumnName.PROTEIN_RATIO) {
				// iterate over projects
				for (String projectTag : projectTags) {
					List<RatioDescriptorBean> ratioDescriptors = ratioDescriptorsByProjects.get(projectTag);
					if (ratioDescriptors != null) {
						// iterate over ratioDescriptors
						for (RatioDescriptorBean ratioDescriptor : ratioDescriptors) {
							if (projectTag.equals(actualprojectTag)) {
								fw.write(parseValue(proteinGroup.getRatioStringByConditions(
										ratioDescriptor.getCondition1Name(), ratioDescriptor.getCondition2Name(),
										ratioDescriptor.getProjectTag(), ratioDescriptor.getRatioName(), false)));
								fw.write(TAB);
							} else {
								fw.write(EMPTY);
								fw.write(TAB);
							}
						}
					}
				}
			} else {
				final String value = ProteinGroupColumns.getInstance().getValue(columnWithOrder.getColumn(),
						proteinGroup, null, null, actualprojectTag, null, null, null, false);
				String parseValue = parseValue(value);
				if ("".equals(parseValue)) {
					parseValue = EMPTY;
				}
				fw.write(parseValue);

				fw.write(TAB);
			}
		}
		fw.write(NEW_LINE);

	}

	private static void writeHeader(FileWriter fw, List<String> projectTags,
			Map<String, List<String>> conditionsByProject,
			Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects, List<String> psmScoreNames,
			List<String> ptmScoreNames, ColumnProvider proteinColumnProvider, ColumnProvider psmColumnProvider)
			throws IOException, PintException {
		int numProjects = projectTags.size();
		// line identifier: PEP, PROT, PG
		fw.write("ROW TYPE" + TAB);
		// project
		fw.write(ColumnName.PROJECT.getName() + TAB);

		// protein header
		final List<ColumnWithVisibility> proteinColumns = proteinColumnProvider.getColumns();
		for (ColumnWithVisibility columnWithOrder : proteinColumns) {

			if (columnWithOrder == null)
				log.info("asdf");
			log.info(columnWithOrder.getColumn());
			if (columnWithOrder.getColumn() == ColumnName.PROTEIN_AMOUNT) {

				// iterate over projects
				for (String projectTag : projectTags) {
					String projectTagString = numProjects > 1 ? projectTag + ":" : "";
					List<String> conditions = conditionsByProject.get(projectTag);
					// iterate over conditions
					for (String conditionName : conditions) {
						final Set<AmountType> amountTypesByCondition = RemoteServicesTasks
								.getProteinAmountTypesByCondition(projectTag, conditionName);
						// sort the amounts in a consistent way
						List<AmountType> amountTypeList = new ArrayList<AmountType>();
						amountTypeList.addAll(amountTypesByCondition);
						Collections.sort(amountTypeList, new Comparator<AmountType>() {

							@Override
							public int compare(AmountType o1, AmountType o2) {
								return o1.name().compareTo(o2.name());
							}
						});
						for (AmountType amountType : amountTypeList) {
							fw.write(SharedDataUtils.getAmountHeader(amountType, conditionName));
							// fw.write(parseValue(proteinBean.getAmountString(
							// condition, projectTag)));
							fw.write(TAB);
						}
					}
				}

			} else if (columnWithOrder.getColumn() == ColumnName.PROTEIN_RATIO) {
				// iterate over projects

				for (String projectTag : projectTags) {
					String projectTagString = numProjects > 1 ? projectTag + ":" : "";
					List<RatioDescriptorBean> ratioDescriptors = ratioDescriptorsByProjects.get(projectTag);
					if (ratioDescriptors != null) {
						// iterate over ratioDescriptors
						for (RatioDescriptorBean ratioDescriptor : ratioDescriptors) {
							fw.write(SharedDataUtils.getRatioHeader(ratioDescriptor.getRatioName(),
									ratioDescriptor.getCondition1Name(), ratioDescriptor.getCondition2Name()));
							fw.write(TAB);
						}
					}
				}
			} else {
				fw.write(columnWithOrder.getColumn().getName());
				fw.write(TAB);
			}
		}
		fw.write(NEW_LINE);

		// line identifier: PEP, PROT, PG
		fw.write("ROW TYPE" + TAB);
		// project
		fw.write(ColumnName.PROJECT.getName() + TAB);
		// PSM header
		final List<ColumnWithVisibility> psmColumns = psmColumnProvider.getColumns();
		for (ColumnWithVisibility columnWithOrder : psmColumns) {
			if (columnWithOrder.getColumn() == ColumnName.PSM_AMOUNT) {
				// iterate over projects
				for (String projectTag : projectTags) {
					String projectTagString = numProjects > 1 ? projectTag + ":" : "";
					List<String> conditions = conditionsByProject.get(projectTag);
					// iterate over conditions
					for (String conditionName : conditions) {
						final Set<AmountType> amountTypesByCondition = RemoteServicesTasks
								.getPSMAmountTypesByCondition(projectTag, conditionName);
						// sort the amounts in a consistent way
						List<AmountType> amountTypeList = new ArrayList<AmountType>();
						amountTypeList.addAll(amountTypesByCondition);
						Collections.sort(amountTypeList, new Comparator<AmountType>() {

							@Override
							public int compare(AmountType o1, AmountType o2) {
								return o1.name().compareTo(o2.name());
							}
						});
						for (AmountType amountType : amountTypeList) {
							fw.write(SharedDataUtils.getAmountHeader(amountType, conditionName));
							// fw.write(parseValue(proteinBean.getAmountString(
							// condition, projectTag)));
							fw.write(TAB);
						}
					}
				}

			} else if (columnWithOrder.getColumn() == ColumnName.PSM_RATIO) {
				// iterate over projects
				for (String projectTag : projectTags) {
					String projectTagString = numProjects > 1 ? projectTag + ":" : "";
					List<RatioDescriptorBean> ratioDescriptors = ratioDescriptorsByProjects.get(projectTag);
					// iterate over ratioDescriptors
					if (ratioDescriptors != null) {
						for (RatioDescriptorBean ratioDescriptor : ratioDescriptors) {
							fw.write(projectTagString + ratioDescriptor.getRatioName() + " ("
									+ ratioDescriptor.getCondition1Name() + "/" + ratioDescriptor.getCondition2Name()
									+ ")");
							fw.write(TAB);
						}
					}
				}
			} else if (columnWithOrder.getColumn() == ColumnName.PSM_SCORE) {
				// iterate over scoreNames
				for (String psmScoreName : psmScoreNames) {
					fw.write(psmScoreName + TAB);
				}
			} else if (columnWithOrder.getColumn() == ColumnName.PTM_SCORE) {
				// iterate over scoreNames
				if (ptmScoreNames.isEmpty()) {
					fw.write(columnWithOrder.getColumn().getName() + TAB);
				} else {
					for (String ptmScoreName : ptmScoreNames) {
						fw.write(ptmScoreName + TAB);
					}
				}
			} else {
				fw.write(columnWithOrder.getColumn().getName());
				fw.write(TAB);
			}
		}
		fw.write(NEW_LINE);
	}

	private static Map<String, List<String>> getConditionsByProjects(List<String> projectTags) throws PintException {
		Map<String, List<String>> conditionsByProject = new HashMap<String, List<String>>();
		for (String projectTag : projectTags) {
			final Set<Condition> experimentalConditions = RemoteServicesTasks
					.getExperimentalConditionsFromProject(projectTag);
			List<String> conditions = new ArrayList<String>();
			for (Condition condition : experimentalConditions) {
				conditions.add(condition.getName());
			}
			Collections.sort(conditions);
			conditionsByProject.put(projectTag, conditions);
		}
		return conditionsByProject;
	}

	private static Map<String, List<RatioDescriptorBean>> getRatioDescriptorsByProjects(List<String> projectTags) {
		Map<String, List<RatioDescriptorBean>> ratiosByProject = new HashMap<String, List<RatioDescriptorBean>>();

		Set<String> projectTagSet = new HashSet<String>();
		projectTagSet.addAll(projectTags);
		final List<RatioDescriptorBean> experimentalConditions = RemoteServicesTasks
				.getRatioDescriptorsFromProjects(projectTagSet);
		for (String projectTag : projectTags) {
			for (RatioDescriptorBean ratioDescriptorBean : experimentalConditions) {
				final String projectTagOfTheRatio = ratioDescriptorBean.getProjectTag();
				if (projectTag.equals(projectTagOfTheRatio)) {
					if (ratiosByProject.containsKey(projectTag)) {
						ratiosByProject.get(projectTag).add(ratioDescriptorBean);
					} else {
						List<RatioDescriptorBean> list = new ArrayList<RatioDescriptorBean>();
						list.add(ratioDescriptorBean);
						ratiosByProject.put(projectTag, list);
					}
				}
			}
		}

		return ratiosByProject;
	}

	public static File exportProteins(Collection<ProteinBean> proteins, String projectFilesPath, String queryText)
			throws PintException {

		File newFile = FileManager.getDownloadFile(projectFilesPath, "proteins" + new Random().nextLong() + ".csv");
		log.info("File created at : " + newFile.getAbsolutePath());
		FileWriter fw = null;

		try {
			if (proteins.isEmpty()) {
				throw new PintException("No proteins to export", PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
			}
			fw = new FileWriter(newFile);
			if (queryText != null) {
				fw.write(queryText + "\n");
			}
			Map<String, List<String>> conditionsByProject = ServerDataUtils.getConditionsByProjects(proteins);
			final Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects = ServerDataUtils
					.getRatioDescriptorsByProjectsFromProteins(proteins);
			List<String> psmScoreNames = ServerDataUtils.getPSMScoreNamesFromProteins(proteins);
			final List<String> ptmScoreNames = ServerDataUtils.getPTMScoreNamesFromProteins(proteins);
			// HEADERS
			List<String> projectTags = new ArrayList<String>();
			projectTags.addAll(conditionsByProject.keySet());
			Collections.sort(projectTags);
			writeHeader(fw, projectTags, conditionsByProject, ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames,
					ProteinColumns.getInstance(), PSMColumns.getInstance());

			for (String projectTag : projectTags) {
				log.info("Exporting proteins from project: " + projectTag);

				final List<ProteinBean> proteinsFromProject = SharedDataUtils.getProteinsFromProject(proteins,
						projectTag);
				int i = 0;
				int total = proteinsFromProject.size();
				log.info("Now iterating through " + total + " proteins to write the file");

				for (ProteinBean proteinBean : proteinsFromProject) {
					// log.info(++i + "/" + total + " protein");
					writeProteinRow(fw, proteinBean, projectTag, projectTags, conditionsByProject,
							ratioDescriptorsByProjects);
					final Set<Integer> psmIds = proteinBean.getPSMDBIds();
					if (psmIds != null && !psmIds.isEmpty()) {
						List<PSMBean> psms = proteinBean.getPsms();
						// RemoteServicesTasks .getPSMsFromProtein(proteinBean,
						// false);
						for (PSMBean psmBean : psms) {
							writePSMRow(fw, psmBean, projectTag, projectTags, conditionsByProject,
									ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames);
						}
					}
				}
				log.info("End iteration");
			}
			return newFile;
		} catch (IOException e) {
			e.printStackTrace();
			if (newFile != null && newFile.exists()) {
				newFile.delete();
			}
			throw new PintException(e, PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
		} finally {
			log.info("closing file writer");
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static File exportProteinGroups(Collection<ProteinBean> proteins, boolean separateNonConclusiveProteins,
			String projectFilesPath, String queryText) throws PintException {

		File newFile = FileManager.getDownloadFile(projectFilesPath,
				"proteinGroups" + new Random().nextLong() + ".csv");
		log.info("File created at: " + newFile.getAbsolutePath());
		FileWriter fw = null;

		try {
			if (proteins.isEmpty()) {
				throw new PintException("No proteins to export", PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
			}
			fw = new FileWriter(newFile);
			if (queryText != null) {
				fw.write(queryText + "\n");
			}
			Map<String, List<String>> conditionsByProject = ServerDataUtils.getConditionsByProjects(proteins);
			final Map<String, List<RatioDescriptorBean>> ratioDescriptorsByProjects = ServerDataUtils
					.getRatioDescriptorsByProjectsFromProteins(proteins);
			List<String> psmScoreNames = ServerDataUtils.getPSMScoreNamesFromProteins(proteins);
			final List<String> ptmScoreNames = ServerDataUtils.getPTMScoreNamesFromProteins(proteins);
			// HEADERS
			List<String> projectTags = new ArrayList<String>();
			projectTags.addAll(conditionsByProject.keySet());
			Collections.sort(projectTags);
			// HEADERS
			writeHeader(fw, projectTags, conditionsByProject, ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames,
					ProteinGroupColumns.getInstance(), PSMColumns.getInstance());

			Set<String> hiddenPTMs = RemoteServicesTasks.getHiddenPTMs(projectTags, projectFilesPath);
			final List<ProteinGroupBean> proteinGroups = RemoteServicesTasks.groupProteins(proteins,
					separateNonConclusiveProteins);
			for (String projectTag : projectTags) {

				log.info("Exporting proteins from project: " + projectTag);

				final List<ProteinGroupBean> proteinGroupsInProject = SharedDataUtils
						.getProteinGroupsFromProject(proteinGroups, projectTag);
				int i = 0;
				int total = proteins.size();
				log.info(
						"Now iterating thorugh " + proteinGroupsInProject.size() + " protein groups to write the file");

				for (ProteinGroupBean proteinGroup : proteinGroupsInProject) {
					// log.info(++i + "/" + total + " protein");
					writeProteinGroupRow(fw, proteinGroup, projectTag, projectTags, conditionsByProject,
							ratioDescriptorsByProjects);
					final List<PSMBean> psms = proteinGroup.getPsms();
					if (psms != null && !psms.isEmpty()) {
						for (PSMBean psmBean : psms) {
							writePSMRow(fw, psmBean, projectTag, projectTags, conditionsByProject,
									ratioDescriptorsByProjects, psmScoreNames, ptmScoreNames);
						}
					}
				}
				log.info("End iteration");
			}
			return newFile;
		} catch (IOException e) {
			e.printStackTrace();
			if (newFile != null && newFile.exists()) {
				newFile.delete();
			}
			throw new PintException(e, PINT_ERROR_TYPE.DATA_EXPORTER_ERROR);
		} finally {
			log.info("closing file writer");
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * parse string value, substituting null by '' and '\n' by ','
	 *
	 * @param string
	 * @return
	 */
	private static String parseValue(String string) {
		if (string == null)
			return "";
		if (string.contains("\n"))
			string = string.replace("\n", " ");

		final String infinity = Character.toString(SharedConstants.INFINITY_CHAR_CODE);
		if (string.equals(infinity))
			return String.valueOf(Double.POSITIVE_INFINITY);
		if (string.equals("-" + infinity))
			return String.valueOf(Double.NEGATIVE_INFINITY);
		if ("".equals(string)) {
			string = EMPTY;
		}
		return string;
	}

	public static String exportProteinsForReactome(String sessionID, String projectFilesPath) throws PintException {
		final String fileName = "Reactome_" + sessionID + "_" + System.currentTimeMillis() + ".csv";
		File outFile = FileManager.getDownloadFile(projectFilesPath, fileName);
		BufferedWriter bw = null;
		try {
			FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			final DataSet dataSet = DataSetsManager.getDataSet(sessionID, null);
			final List<ProteinGroupBean> proteinGroups = dataSet.getProteinGroups(false);
			bw.write("PROTEINS\n");
			for (ProteinGroupBean proteinGroupBean : proteinGroups) {
				for (ProteinBean proteinBean : proteinGroupBean) {
					if (proteinBean.getEvidence() != ProteinEvidence.NONCONCLUSIVE) {
						bw.write(proteinBean.getPrimaryAccession().getAccession());
						bw.write("\n");
						break;
					}
				}
			}
			final String href = Window.Location.getProtocol() + "//" + Window.Location.getHost() + "/pint/download?"
					+ SharedConstants.FILE_TO_DOWNLOAD + "=" + fileName + "&" + SharedConstants.FILE_TYPE + "="
					+ SharedConstants.ID_DATA_FILE_TYPE;
			return href;
		} catch (IOException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.REACTOME_INPUT_FILE_GENERATION_ERROR);
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
