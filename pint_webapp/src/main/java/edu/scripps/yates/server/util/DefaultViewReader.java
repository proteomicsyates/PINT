package edu.scripps.yates.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.shared.columns.ColumnName;
import edu.scripps.yates.shared.columns.ColumnWithVisibility;
import edu.scripps.yates.shared.columns.PSMColumns;
import edu.scripps.yates.shared.columns.PeptideColumns;
import edu.scripps.yates.shared.columns.ProteinColumns;
import edu.scripps.yates.shared.columns.ProteinGroupColumns;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.DefaultView.ORDER;
import edu.scripps.yates.shared.util.DefaultView.TAB;

public class DefaultViewReader {
	private static final Logger log = Logger.getLogger(DefaultView.class);
	private final DefaultView defaultView;
	private static final String NEWLINE = "\n";

	public DefaultViewReader(String projectTag) {
		log.info("**Creating default view for project " + projectTag);
		defaultView = new DefaultView();
		log.info("default view object created empty");
		defaultView.setProjectTag(projectTag);
		log.info("project tag set");
		final File defaultViewConfigurationFile = FileManager.getProjectDefaultViewConfigurationTxt(projectTag);
		log.info("Default view configuration file is: " + defaultViewConfigurationFile.getAbsolutePath());
		if (!defaultViewConfigurationFile.exists()) {
			log.info("Default view  not configured for project: " + projectTag);
			log.info("Creating new default configuration file for the default view: '" + projectTag + "'");
			writeDefaultDefaultViewFile(projectTag);
		} else {
			log.info("Default view detected for project: " + projectTag);
		}
		init(defaultViewConfigurationFile);
		log.info("Defaul view loaded for project: " + projectTag);
	}

	private void init(File defaultViewConfigurationFile) {
		log.info(
				"initializing default view from configuration file: " + defaultViewConfigurationFile.getAbsolutePath());
		// set by default if some default view is not present in the defaultView
		// file
		defaultView.setPeptideOrder(ORDER.DESCENDING);
		defaultView.setPeptidesSortedBy(ColumnName.PEPTIDE_SEQUENCE);

		try {
			final BufferedReader br = new BufferedReader(new FileReader(defaultViewConfigurationFile));

			try {
				int numDefaultQuery = 0;
				boolean proteinGroupColumns = false;
				boolean proteinColumns = false;
				boolean peptideColumns = false;
				boolean psmColumns = false;
				boolean projectDescriptionBoolean = false;
				boolean projectInstructionBoolean = false;
				boolean projectViewCommentBoolean = false;
				final StringBuilder projectDescription = new StringBuilder();
				final StringBuilder projectInstructions = new StringBuilder();
				final StringBuilder projectViewComments = new StringBuilder();
				String line = null;
				while ((line = br.readLine()) != null) {
					line = line.trim();
					if ("".equals(line))
						continue;
					String property = line;
					String value = null;

					if (line.startsWith(ServerConstants.DEFAULT_VIEW_METADATA)) {
						line = line.substring(line.indexOf(ServerConstants.DEFAULT_VIEW_METADATA)
								+ ServerConstants.DEFAULT_VIEW_METADATA.length()).trim();
					} else if (line.startsWith(ServerConstants.DEFAULT_VIEW_COLUMNS)) {
						line = line.substring(line.indexOf(ServerConstants.DEFAULT_VIEW_COLUMNS)
								+ ServerConstants.DEFAULT_VIEW_COLUMNS.length()).trim();
					} else {
						continue;
					}

					if (line.contains("=")) {
						final String[] split = line.split("=");
						property = split[0].trim();
						value = "";
						for (int j = 1; j < split.length; j++) {
							value += split[j].trim();
							if (j < split.length - 1) {
								value += "=";
							}
						}
					} else {

						property = line;
					}
					if (ServerConstants.PROTEIN_GROUP_COLUMNS.equals(property)) {
						proteinGroupColumns = true;
						continue;
					} else if (ServerConstants.PROTEIN_COLUMNS.equals(property)) {
						proteinColumns = true;
						continue;
					} else if (ServerConstants.PEPTIDE_COLUMNS.equals(property)) {
						peptideColumns = true;
						continue;
					} else if (ServerConstants.PSM_COLUMNS.equals(property)) {
						psmColumns = true;
						continue;
					} else if (ServerConstants.END_COLUMNS.equals(property)) {
						proteinGroupColumns = false;
						proteinColumns = false;
						psmColumns = false;
						peptideColumns = false;
						continue;
					} else if (ServerConstants.PROJECT_DESCRIPTION.equals(property)) {
						projectDescriptionBoolean = true;
						continue;
					} else if (ServerConstants.END_PROJECT_DESCRIPTION.equals(property)) {
						projectDescriptionBoolean = false;
						defaultView.setProjectDescription(projectDescription.toString());
						continue;
					} else if (ServerConstants.PROJECT_INSTRUCTIONS.equals(property)) {
						projectInstructionBoolean = true;
						continue;
					} else if (ServerConstants.END_PROJECT_INSTRUCTIONS.equals(property)) {
						projectInstructionBoolean = false;
						defaultView.setProjectInstructions(projectInstructions.toString());
						continue;
					} else if (ServerConstants.PROJECT_VIEW_COMMENTS.equals(property)) {
						projectViewCommentBoolean = true;
						continue;
					} else if (ServerConstants.END_PROJECT_VIEW_COMMENTS.equals(property)) {
						projectViewCommentBoolean = false;
						defaultView.setProjectViewComments(projectViewComments.toString());
						continue;
					} else if (projectDescriptionBoolean) {
						if (!"".equals(projectDescription.toString()))
							projectDescription.append(NEWLINE);
						if (value != null)
							property += "=" + value;
						projectDescription.append(property);
					} else if (projectInstructionBoolean) {
						if (!"".equals(projectInstructions.toString()))
							projectInstructions.append(NEWLINE);
						if (value != null)
							property += "=" + value;
						projectInstructions.append(property);
					} else if (projectViewCommentBoolean) {
						if (!"".equals(projectViewComments.toString()))
							projectViewComments.append(NEWLINE);
						if (value != null)
							property += "=" + value;
						projectViewComments.append(line);
					} else {
						if (property == null)
							continue;
					}

					if (ServerConstants.DEFAULT_QUERY_STRING.equals(property)) {
						if (!"".equals(value)) {
							defaultView.addProjectNamedQueries(value, numDefaultQuery++);
						}
						continue;
					}
					if (ServerConstants.DEFAULT_TAB.equals(property)) {
						defaultView.setDefaultTab(TAB.valueOf(value));
						continue;
					}
					if (ServerConstants.PROTEIN_GROUPS_SORTED_BY.equals(property)) {
						defaultView.setProteinGroupsSortedBy(ColumnName.valueOf(value));
						continue;
					}
					if (ServerConstants.PROTEINS_SORTED_BY.equals(property)) {
						defaultView.setProteinsSortedBy(ColumnName.valueOf(value));
						continue;
					}
					if (ServerConstants.PEPTIDES_SORTED_BY.equals(property)) {
						defaultView.setPeptidesSortedBy(ColumnName.valueOf(value));
						continue;
					}
					if (ServerConstants.PSMS_SORTED_BY.equals(property)) {
						defaultView.setPsmsSortedBy(ColumnName.valueOf(value));
						continue;
					}
					if (ServerConstants.PROTEIN_GROUP_ORDER.equals(property)) {
						defaultView.setProteinGroupOrder(ORDER.valueOf(value));
						continue;
					}
					if (ServerConstants.PROTEIN_ORDER.equals(property)) {
						defaultView.setProteinOrder(ORDER.valueOf(value));
						continue;
					}
					if (ServerConstants.PEPTIDE_ORDER.equals(property)) {
						defaultView.setPeptideOrder(ORDER.valueOf(value));
						continue;
					}
					if (ServerConstants.PSM_ORDER.equals(property)) {
						defaultView.setPsmOrder(ORDER.valueOf(value));
						continue;
					}
					if (ServerConstants.PROTEIN_GROUP_SORTING_SCORE.equals(property)) {
						defaultView.setProteinGroupSortingScore(value);
						continue;
					}
					if (ServerConstants.PROTEIN_SORTING_SCORE.equals(property)) {
						defaultView.setProteinSortingScore(value);
						continue;
					}
					if (ServerConstants.PEPTIDE_SORTING_SCORE.equals(property)) {
						defaultView.setPeptideSortingScore(value);
						continue;
					}
					if (ServerConstants.PSM_SORTING_SCORE.equals(property)) {
						defaultView.setPsmSortingScore(value);
						continue;
					}

					if (ServerConstants.PROTEIN_PAGE_SIZE.equals(property)) {
						if (value != null) {
							try {
								defaultView.setProteinPageSize(Integer.valueOf(value));
							} catch (final NumberFormatException e) {

							}
						}
						continue;
					}
					if (ServerConstants.PROTEIN_GROUP_PAGE_SIZE.equals(property)) {
						if (value != null) {
							try {
								defaultView.setProteinGroupPageSize(Integer.valueOf(value));
							} catch (final NumberFormatException e) {

							}
						}
						continue;
					}
					if (ServerConstants.PEPTIDE_PAGE_SIZE.equals(property)) {
						if (value != null) {
							try {
								defaultView.setPeptidePageSize(Integer.valueOf(value));
							} catch (final NumberFormatException e) {

							}
						}
						continue;
					}
					if (ServerConstants.PSM_PAGE_SIZE.equals(property)) {
						if (value != null) {
							try {
								defaultView.setPsmPageSize(Integer.valueOf(value));
							} catch (final NumberFormatException e) {

							}
						}
						continue;
					}
					if (ServerConstants.HIDDEN_PTMS.equals(property)) {
						if (value != null) {
							try {
								if (value.contains(",")) {
									final String[] split = value.split(",");
									for (final String ptm : split) {
										defaultView.addHiddenPTMs(ptm.trim());
									}
								} else {
									defaultView.addHiddenPTMs(value);
								}
							} catch (final NumberFormatException e) {

							}
						}
						continue;
					}
					// columns
					if (proteinGroupColumns) {
						final ColumnName columnName = ColumnName.getByPropertyName(property);
						if (columnName != null) {
							final Boolean view = Boolean.valueOf(value);
							if (view != null) {
								defaultView.getProteinGroupDefaultView()
										.add(new ColumnWithVisibility(columnName, view));
							}
						}
					} else if (proteinColumns) {
						final ColumnName columnName = ColumnName.getByPropertyName(property);
						if (columnName != null) {
							final Boolean view = Boolean.valueOf(value);
							if (view != null) {
								defaultView.getProteinDefaultView().add(new ColumnWithVisibility(columnName, view));
							}
						}
					} else if (peptideColumns) {
						final ColumnName columnName = ColumnName.getByPropertyName(property);
						if (columnName != null) {
							final Boolean view = Boolean.valueOf(value);
							if (view != null) {
								defaultView.getPeptideDefaultView().add(new ColumnWithVisibility(columnName, view));
							}
						}
					} else if (psmColumns) {
						final ColumnName columnName = ColumnName.getByPropertyName(property);
						if (columnName != null) {
							final Boolean view = Boolean.valueOf(value);
							if (view != null) {
								defaultView.getPsmDefaultView().add(new ColumnWithVisibility(columnName, view));
							}
						}
					}
				}
			} catch (final IOException e) {

				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void writeDefaultDefaultViewFile(String projectTag) {
		final File file = FileManager.getProjectDefaultViewConfigurationTxt(projectTag);
		if (!file.exists()) {
			file.getParentFile().mkdirs();
		}
		Writer writer = null;

		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			writer.write("This file is setting the default view for the project: '" + projectTag + "'" + NEWLINE);
			writer.write("Please, contact to salvador at scripps.edu for more information about this format" + NEWLINE
					+ NEWLINE);
			writer.write("A line starting by '" + ServerConstants.DEFAULT_VIEW_METADATA
					+ "' means that it is metadata line" + NEWLINE);
			writer.write("A line starting by '" + ServerConstants.DEFAULT_VIEW_COLUMNS
					+ "' means that it is column line, with the information of showing or not that column by default"
					+ NEWLINE);
			writer.write("Any other line not starting by '" + ServerConstants.DEFAULT_VIEW_COLUMNS + "' or '"
					+ ServerConstants.DEFAULT_VIEW_COLUMNS + "' will be ignored " + NEWLINE);

			writer.write("Possible values for ordering: " + ORDER.ASCENDING + ", " + ORDER.DESCENDING + NEWLINE);
			writer.write("In '" + ServerConstants.PROTEIN_GROUP_SORTING_SCORE + "', '"
					+ ServerConstants.PROTEIN_SORTING_SCORE + "' and '" + ServerConstants.PSM_SORTING_SCORE
					+ "' lines, the score must be stated with the same name it appears in the header of the table in the PInt data view"
					+ NEWLINE + NEWLINE);
			// project description
			writer.write(
					"A description of the project that will be shown before loading the data (several lines are allowed)"
							+ NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROJECT_DESCRIPTION + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.END_PROJECT_DESCRIPTION
					+ NEWLINE + NEWLINE);
			// project Instructions
			writer.write(
					"This text will be also shown before loading the data, as a detailed description of the data that is in the project (any prefiltering criteria, whether it is a subset of the whole data or not, quick description of the experimental conditions that are shown, etc...)  (several lines are allowed)"
							+ NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROJECT_INSTRUCTIONS + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.END_PROJECT_INSTRUCTIONS
					+ NEWLINE + NEWLINE);
			// project view comments
			writer.write(
					"A description of the project that will be shown before loading the data  (several lines are allowed)"
							+ NEWLINE);
			writer.write(
					ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROJECT_VIEW_COMMENTS + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.END_PROJECT_VIEW_COMMENTS
					+ NEWLINE + NEWLINE);
			// default query string
			writer.write(
					"The default query using the syntax described in the PInt help for query commands to show in the project."
							+ NEWLINE);
			writer.write("If empty, all data will be loaded." + NEWLINE);
			writer.write("If stated, it should be explained in the '" + ServerConstants.PROJECT_INSTRUCTIONS + "' line"
					+ NEWLINE);
			writer.write(
					"For giving a description to the query, separate it with '###' as:  'MT      default query string=Core interactome ### THR[Xcscorefilter, true]'"
							+ NEWLINE);
			writer.write("Multiple " + ServerConstants.DEFAULT_VIEW_METADATA + " lines are allowed." + NEWLINE);

			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.DEFAULT_QUERY_STRING + "="
					+ NEWLINE + NEWLINE);
			// default tab
			writer.write("The default tab to show when loading the project (possible values: "
					+ Arrays.toString(TAB.values()) + ")" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.DEFAULT_TAB + "=" + TAB.PROTEIN
					+ NEWLINE + NEWLINE);
			// hidden PTMs
			writer.write("A list of PTM names that will be masked (hidden) in the view." + NEWLINE);
			writer.write(
					"This is a comma separated values list of PTMs. Example: acetylated residue,phosphorylated residue,monomethylated residue"
							+ NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.HIDDEN_PTMS + "=" + "" + NEWLINE
					+ NEWLINE);

			// protein groups
			writer.write("Default view for protein groups" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_GROUPS_SORTED_BY + "="
					+ ColumnName.SPECTRUM_COUNT.name() + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_GROUP_ORDER + "="
					+ ORDER.DESCENDING + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_GROUP_SORTING_SCORE
					+ "=" + "" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_GROUP_PAGE_SIZE + "="
					+ 50 + NEWLINE);
			writer.write(
					ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_GROUP_COLUMNS + NEWLINE);
			List<ColumnWithVisibility> columns = ProteinGroupColumns.getInstance().getColumns();
			for (final ColumnWithVisibility columnWithVisibility : columns) {
				writer.write(ServerConstants.DEFAULT_VIEW_COLUMNS + "\t" + columnWithVisibility.getColumn().name() + "="
						+ columnWithVisibility.isVisible() + NEWLINE);
			}
			writer.write(
					ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.END_COLUMNS + NEWLINE + NEWLINE);

			// proteins
			writer.write("Default view for proteins" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEINS_SORTED_BY + "="
					+ ColumnName.SPECTRUM_COUNT.name() + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_ORDER + "="
					+ ORDER.DESCENDING + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_SORTING_SCORE + "=" + ""
					+ NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_PAGE_SIZE + "=" + 50
					+ NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PROTEIN_COLUMNS + NEWLINE);
			columns = ProteinColumns.getInstance().getColumns();
			for (final ColumnWithVisibility columnWithVisibility : columns) {
				writer.write(ServerConstants.DEFAULT_VIEW_COLUMNS + "\t" + columnWithVisibility.getColumn().name() + "="
						+ columnWithVisibility.isVisible() + NEWLINE);
			}
			writer.write(
					ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.END_COLUMNS + NEWLINE + NEWLINE);

			// peptides
			writer.write("Default view for peptides" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PEPTIDES_SORTED_BY + "="
					+ ColumnName.SPECTRUM_COUNT.name() + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PEPTIDE_ORDER + "="
					+ ORDER.DESCENDING + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PEPTIDE_SORTING_SCORE + "=" + ""
					+ NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PEPTIDE_PAGE_SIZE + "=" + 50
					+ NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PEPTIDE_COLUMNS + NEWLINE);
			columns = PeptideColumns.getInstance().getColumns();
			for (final ColumnWithVisibility columnWithVisibility : columns) {
				writer.write(ServerConstants.DEFAULT_VIEW_COLUMNS + "\t" + columnWithVisibility.getColumn().name() + "="
						+ columnWithVisibility.isVisible() + NEWLINE);
			}
			writer.write(
					ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.END_COLUMNS + NEWLINE + NEWLINE);

			// psms
			writer.write("Default view for PSMs" + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PSMS_SORTED_BY + "="
					+ ColumnName.PEPTIDE_SEQUENCE.name() + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PSM_ORDER + "="
					+ ORDER.DESCENDING + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PSM_SORTING_SCORE + "=" + ""
					+ NEWLINE);
			writer.write("Size of the pagination for the PSM tables" + NEWLINE);
			writer.write(
					ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PSM_PAGE_SIZE + "=" + 50 + NEWLINE);
			writer.write(ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.PSM_COLUMNS + NEWLINE);
			columns = PSMColumns.getInstance().getColumns();
			for (final ColumnWithVisibility columnWithVisibility : columns) {
				writer.write(ServerConstants.DEFAULT_VIEW_COLUMNS + "\t" + columnWithVisibility.getColumn().name() + "="
						+ columnWithVisibility.isVisible() + NEWLINE);
			}
			writer.write(
					ServerConstants.DEFAULT_VIEW_METADATA + "\t" + ServerConstants.END_COLUMNS + NEWLINE + NEWLINE);

		} catch (final IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	public DefaultView getDefaultView() {
		return defaultView;
	}
}
