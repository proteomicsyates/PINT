package edu.scripps.yates.server.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.server.projectCreator.ImportCfgFileParserUtil;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.map.hash.TIntObjectHashMap;

public class FileManager {
	private static final Logger log = Logger.getLogger(FileManager.class);
	private static final String DATA = "data";
	private static final String XML = "xml";
	private static final String OLD = "old";
	private static final String DOWNLOAD = "download";
	private static final String REACTOME = "reactome";

	private static final String UNIPROT_RELEASES = "uniprot";
	private static final String PSEAQUANT = "psea_quant";
	private static final String FASTAS = "fastas";
	private static final String DEFAULT_VIEWS = "default-views";
	private static String projectFilesPath;
	private static TIntObjectHashMap<List<FileWithFormat>> filesByImportProcessID = new TIntObjectHashMap<List<FileWithFormat>>();
	private static TIntObjectHashMap<File> projectCfgFileByImportProcessID = new TIntObjectHashMap<File>();
	private static TIntObjectHashMap<File> projectCfgFileByImportProcessKey = new TIntObjectHashMap<File>();

	private static boolean ready = false;
	private static boolean loading;
	private static final String PROJECT_STATS_FILE_NAME = "stats.properties";
	private static final TIntObjectHashMap<FileSummaries> fileSummariesByImportID = new TIntObjectHashMap<FileSummaries>();
	private static final String TEMPLATES = "templates";

	private static File getXmlFolder() {

		final String folderName = getProjectFilesPath() + File.separator + XML;
		final File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	private static synchronized void loadProjectCfgFilesByImportProcessID() {
		loading = true;
		final File xmlFolder = getXmlFolder();
		for (final File xmlFile : xmlFolder.listFiles()) {
			if (xmlFile.isFile()) {
				try {
					final PintImportCfg pintImportFromFile = ImportCfgFileParserUtil.getPintImportFromFile(xmlFile);
					final Integer importID = pintImportFromFile.getImportID();
					if (importID != null) {
						if (projectCfgFileByImportProcessID.containsKey(importID)) {
							final File xmlFile2 = projectCfgFileByImportProcessID.get(importID);
							// both files have the same import process
							// to solve this bug, we should keep the newer file
							File fileToDelete = null;
							if (xmlFile2.lastModified() < xmlFile.lastModified()) {
								indexProjectCfgFileByImportProcessID(importID, xmlFile);
								fileToDelete = xmlFile2;
							} else {
								fileToDelete = xmlFile;
							}
							moveToOLD(fileToDelete);

						} else {
							indexProjectCfgFileByImportProcessID(importID, xmlFile);
						}
					} else {
						moveToOLD(xmlFile);
					}
				} catch (final Exception e) {
					log.warn("Error reading project cfg file: " + xmlFile.getAbsolutePath());
				}
			}
		}

		ready = true;
		loading = false;
	}

	private static void moveToOLD(File fileToDelete) {
		log.info("Moving file " + fileToDelete.getAbsolutePath() + " to " + getOLDXmlFolder().getAbsolutePath());
		final File destineFile = getOLDXmlFile(FilenameUtils.getBaseName(fileToDelete.getAbsolutePath()));
		try {
			FileUtils.moveFile(fileToDelete, destineFile);
			log.info(fileToDelete.getAbsolutePath() + " moved to " + destineFile.getAbsolutePath());
		} catch (final IOException e) {
			log.warn(e);
		}

	}

	private static File getOLDXmlFile(String fileName) {
		return new File(getOLDXmlFolder().getAbsolutePath() + File.separator + fileName + ".xml");
	}

	private static File getOLDXmlFolder() {
		final String folderName = getXmlFolder() + File.separator + OLD;
		final File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	public static File getDataFile(int jobID, String fileName, String fileId, FileFormat format) {
		final File folder = getDataFileFolder(jobID, fileId, format);
		final File file = new File(folder.getAbsoluteFile() + File.separator + fileName);
		return file;
	}

	private static File getDataFileFolder(int jobID, String fileId, FileFormat format) {

		final File projectDataFileFolder = getProjectDataFileFolder(jobID, true);
		final File folder = new File(
				projectDataFileFolder.getAbsolutePath() + File.separator + fileId + File.separator + format.name());
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	public static File getProjectDataFileFolder(int jobID, boolean createIfNotExist) {

		final String folderName = getProjectFilesPath() + File.separator + DATA + File.separator + jobID;
		final File folder = new File(folderName);
		if (!folder.exists()) {
			if (createIfNotExist) {
				log.info("Creating " + folder.getAbsolutePath() + " on server");
				folder.mkdirs();
			}
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	/**
	 * Gets the file located at: projectFilesPath/DOWNLOAD/
	 *
	 * @param fileName
	 * @return
	 */
	public static File getDownloadFile(String fileName) {
		final File folder = getDownloadFileFolder();
		final File file = new File(folder.getAbsoluteFile() + File.separator + fileName);
		return file;
	}

	/**
	 * Gets the file located at: projectFilesPath/REACTOME/
	 *
	 * @param fileName
	 * @return
	 */
	public static File getReactomeFile(String fileName) {
		final File folder = getReactomeFileFolder();
		final File file = new File(folder.getAbsoluteFile() + File.separator + fileName);
		return file;
	}

	private static File getDownloadFileFolder() {
		final String folderName = getProjectFilesPath() + File.separator + DOWNLOAD;
		final File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	private static File getReactomeFileFolder() {
		final String folderName = getProjectFilesPath() + File.separator + REACTOME;
		final File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	private static String getProjectFilesPath() {
		if (projectFilesPath == null) {
			projectFilesPath = System.getProperty("java.io.tmpdir");
		}
		return projectFilesPath;
	}

	public static File getUniprotReleasesFolder() {
		final String folderName = getProjectFilesPath() + File.separator + UNIPROT_RELEASES;
		final File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	public static File getProjectXmlFile(String projectXmlFileName) {
		final File folder = getXmlFolder();
		final File file = new File(folder.getAbsoluteFile() + File.separator + projectXmlFileName);
		return file;
	}

	public static String getProjectFilesPath(ServletContext servletContext) {
		if (projectFilesPath == null || projectFilesPath.equals(System.getProperty("java.io.tmpdir"))) {
			if (ServerUtil.isTestServer()) {
				projectFilesPath = "D:\\PINT";
//				projectFilesPath = "Z:\\share\\Salva\\data\\PInt";
			} else {
				projectFilesPath = servletContext.getInitParameter(SharedConstants.PINT_HOME_PATH);

			}
			projectFilesPath = projectFilesPath.replace("\\", "/");
			projectFilesPath = projectFilesPath.replace("//", "/");
			loadIfNeeded();
		}
		return projectFilesPath;
	}

	public static void indexFilesByImportProcessID(int importProcessIdentifier, Collection<FileWithFormat> files) {
		if (files == null || files.isEmpty())
			return;
		log.info("Storing " + files.size() + " data files in server");
		for (final FileWithFormat fileWithType : files) {

			indexFileByImportProcessID(importProcessIdentifier, fileWithType);
		}
	}

	public static void indexFileByImportProcessID(int importProcessIdentifier, FileWithFormat file) {
		if (file == null)
			return;
		log.info("storing " + file.getFileName() + " in import job id '" + importProcessIdentifier + "'");
		if (filesByImportProcessID.containsKey(importProcessIdentifier)) {
			final List<FileWithFormat> alreadyStoredFiles = filesByImportProcessID.get(importProcessIdentifier);
			// remove the file with the same id if exist previously
			final Iterator<FileWithFormat> iterator = alreadyStoredFiles.iterator();
			while (iterator.hasNext()) {
				final FileWithFormat next = iterator.next();
				if (next.getId().equals(file.getId())) {
					iterator.remove();
				}
			}
			alreadyStoredFiles.add(file);
		} else {
			final List<FileWithFormat> list = new ArrayList<FileWithFormat>();
			list.add(file);
			filesByImportProcessID.put(importProcessIdentifier, list);
		}
		log.info(file.getFileName() + " stored in import job id '" + importProcessIdentifier + "'");
	}

	public static void removeFromIndexFileByImportProcessID(int importProcessIdentifier, FileWithFormat file) {

		if (filesByImportProcessID.containsKey(importProcessIdentifier)) {
			final Iterator<FileWithFormat> iterator = filesByImportProcessID.get(importProcessIdentifier).iterator();
			while (iterator.hasNext()) {
				final FileWithFormat fileWithFormat = iterator.next();
				if (file.equals(fileWithFormat))
					iterator.remove();
			}
		}
	}

	public static void indexRemoteFileByImportProcessID(int importProcessIdentifier, RemoteFileWithType remoteFile) {
		if (remoteFile == null)
			return;
		final File actualFile = remoteFile.getRemoteFileSSHReference().getRemoteFile();
		final FormatType actualFileType = remoteFile.getFormat();
		final FileWithFormat fileWithType = new FileWithFormat(remoteFile.getId(), actualFile, actualFileType,
				remoteFile.getFastaDigestionBean());
		indexFileByImportProcessID(importProcessIdentifier, fileWithType);

	}

	public static void indexRemoteFilesByImportProcessID(int importProcessIdentifier,
			Collection<RemoteFileWithType> remoteFiles) {
		if (remoteFiles == null)
			return;
		for (final RemoteFileWithType remoteFileWithType : remoteFiles) {
			indexRemoteFileByImportProcessID(importProcessIdentifier, remoteFileWithType);
		}
	}

	public static List<FileWithFormat> getFilesByImportProcessID(int importProcessIdentifier) {
		return getFilesByImportProcessID(importProcessIdentifier, null);
	}

	public static List<FileWithFormat> getFilesByImportProcessID(int importProcessIdentifier, FileFormat format) {
		filesByImportProcessID.remove(importProcessIdentifier);
		final File projectDataFileFolder = getProjectDataFileFolder(importProcessIdentifier, true);
		if (projectDataFileFolder.exists() && projectDataFileFolder.listFiles() != null) {
			for (final File dataFileFolder : projectDataFileFolder.listFiles()) {
				final String fileID = FilenameUtils.getBaseName(dataFileFolder.getAbsolutePath());
//				if (dataFileFolder.listFiles().length == 1) {
				for (final File dataFileFormatFolder : dataFileFolder.listFiles()) {

					final FormatType formatFromFolder = FormatType
							.fromValue(FilenameUtils.getBaseName(dataFileFormatFolder.getAbsolutePath()));
					if (dataFileFormatFolder.listFiles().length > 0) {
						for (final File dataFile : dataFileFormatFolder.listFiles()) {

							if (dataFile.exists()) {
								// TODO substitute the null by something
								FileWithFormat obj = null;
								if (format != null) {
									obj = new FileWithFormat(fileID, dataFile,
											FormatType.fromValue(format.name().toLowerCase()), null);
								} else {
									obj = new FileWithFormat(fileID, dataFile, formatFromFolder, null);
								}
								if (filesByImportProcessID.containsKey(importProcessIdentifier)) {
									filesByImportProcessID.get(importProcessIdentifier).add(obj);
								} else {
									final List<FileWithFormat> list = new ArrayList<FileWithFormat>();
									list.add(obj);
									filesByImportProcessID.put(importProcessIdentifier, list);
								}
							}
						}
					}
				}
//				}
			}
		}

		return filesByImportProcessID.get(importProcessIdentifier);
	}

	public static FileWithFormat getFileByImportProcessID(int importProcessIdentifier,
			FileNameWithTypeBean fileNameWithType) {

		final List<FileWithFormat> indexedFiles = getFilesByImportProcessID(importProcessIdentifier,
				fileNameWithType.getFileFormat());
		if (indexedFiles != null) {
			for (final FileWithFormat indexedFileWithType : indexedFiles) {
				if (indexedFileWithType.getFileName().equals(fileNameWithType.getFileName())) {
					FormatType formatType = null;
					if (fileNameWithType.getFileFormat() != null)
						formatType = FormatType.fromValue(fileNameWithType.getFileFormat().name().toLowerCase());
					if (indexedFileWithType.getFormat() != null && indexedFileWithType.getFormat().equals(formatType)) {
						return indexedFileWithType;
					}
				}
			}
		}
		return null;
	}

	public static void indexProjectCfgFileByImportProcessID(int importProcessIdentifier, File cfgFile) {
		loadIfNeeded();
		log.info("Project cfg file '" + cfgFile.getAbsolutePath() + "' stored with import ID = "
				+ importProcessIdentifier);
		projectCfgFileByImportProcessID.put(importProcessIdentifier, cfgFile);
	}

	public static void indexProjectCfgFileByImportCfgKey(int importProcessKey, File cfgFile) {

		projectCfgFileByImportProcessKey.put(importProcessKey, cfgFile);
	}

	public static File getProjectCfgFileByImportProcessKey(int importProcessKey) {
		return projectCfgFileByImportProcessKey.get(importProcessKey);
	}

	private static void loadIfNeeded() {
		if (!ready && !loading) {
			loadProjectCfgFilesByImportProcessID();
		}
	}

	public static File getProjectCfgFileByImportProcessID(int importProcessIdentifier) {
		loadIfNeeded();
		return projectCfgFileByImportProcessID.get(importProcessIdentifier);
	}

	public static void removeProjectCfgFileByImportProcessID(int importProcessIdentifier) {
		loadIfNeeded();
		projectCfgFileByImportProcessID.remove(importProcessIdentifier);
	}

	public static String getFileSizeString(File newTempFile) {
		if (newTempFile.exists()) {

			return edu.scripps.yates.utilities.files.FileUtils.getDescriptiveSizeFromBytes(newTempFile.length());

		}
		return "empty file";
	}

	public static File getProjectDefaultViewConfigurationTxt(String projectTag) {
		log.info("Getting project default view configuration txt file for " + projectTag + " in "
				+ getProjectFilesPath());
		final File file = new File(
				getProjectFilesPath() + File.separator + DEFAULT_VIEWS + File.separator + projectTag + ".txt");
		return file;
	}

	/**
	 * Gets the path to the file /projectDefaultPath/FASTAS/fasta_file_name.fasta
	 *
	 * @param fastaFileName
	 * @param projectFileDefaultPath
	 * @return
	 */
	public static File getFastaFile(String fastaFileName) {
		return new File(getProjectFilesPath() + File.separator + FASTAS + File.separator + fastaFileName);
	}

	/**
	 * Gets the path to the file /projectDefaultPath/FASTAS
	 *
	 * @param projectFileDefaultPath
	 * @return
	 */
	public static File getFastaIndexFolder() {
		return new File(getProjectFilesPath() + File.separator + FASTAS);
	}

	/**
	 * Removes all the content of the folder of data files for the jobID provided
	 *
	 * @param jobID
	 */
	public static void removeDataFiles(int jobID) {
		log.info("Removing files from server for import process: " + jobID);
		final File projectDataFileFolder = FileManager.getProjectDataFileFolder(jobID, false);
		// go to the latest folder
		try {
			edu.scripps.yates.utilities.files.FileUtils.deleteFolderRecursive(projectDataFileFolder);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			log.error("Error deleting files for import process: " + jobID + "\t" + e.getMessage());
		}

	}

	public static File getPSEAQuantFolder() {
		final File folder = new File(getProjectFilesPath() + File.separator + PSEAQUANT);
		if (!folder.exists()) {
			log.info("creating " + folder.getAbsolutePath() + " in server");
			folder.mkdirs();
		}
		return folder;
	}

	public static File getPSEAQuantFile(String filename) {
		return new File(getPSEAQuantFolder() + File.separator + filename);
	}

	public static File getProjectStatsFile() {
		return new File(getProjectFilesPath() + File.separator + PROJECT_STATS_FILE_NAME);
	}

	public static File getPINTPropertiesFile(ServletContext context) {
		File file = null;
		if (!isTest()) {
			file = new File(getProjectFilesPath(context) + File.separator + ServerConstants.PINT_PROPERTIES_FILE_NAME);
		} else {
			file = new File(
					getProjectFilesPath(context) + File.separator + ServerConstants.PINT_TEST_PROPERTIES_FILE_NAME);
		}
		log.debug("Using properties file: " + file.getAbsolutePath());

		return file;
	}

	public static File getLatestDeletedProjectsFile(ServletContext context) {
		File file = null;
		if (!isTest()) {
			file = new File(
					getProjectFilesPath(context) + File.separator + ServerConstants.PINT_LATEST_DELETED_PROJECTS_FILE);
		} else {
			file = new File(
					getProjectFilesPath(context) + File.separator + ServerConstants.PINT_LATEST_DELETED_PROJECTS_FILE);
		}
		log.debug("Getting latest deleted projects file: " + file.getAbsolutePath());

		return file;
	}

	private static boolean isTest() {
		final Map<String, String> env = System.getenv();
		if (env.get("SERVER_TEST") != null && env.get("SERVER_TEST").equals("true")) {
			return true;
		}
		return false;
	}

	public static FileSummaries getFileSummariesByImportID(int importID) {
		if (!fileSummariesByImportID.containsKey(importID)) {
			final FileSummaries fileSummaries = new FileSummaries();
			fileSummariesByImportID.put(importID, fileSummaries);
		}
		return fileSummariesByImportID.get(importID);
	}

	public static void removeFileSummaries(int importID) {
		fileSummariesByImportID.remove(importID);
	}

	public static File getProjectCfgFileFolder() {
		return getXmlFolder();
	}

	public static List<File> getProjectCfgFileTemplates(ServletContext context) {
		final List<File> ret = new ArrayList<File>();
		final File folder = new File(getProjectFilesPath(context) + File.separator + TEMPLATES);
		if (folder.exists()) {
			final File[] listFiles = folder.listFiles();
			for (final File file : listFiles) {
				ret.add(file);
			}
		}
		return ret;
	}

	public static File getProjectCfgFileTemplate(String filename) {
		final File file = new File(getProjectFilesPath() + File.separator + TEMPLATES + File.separator + filename);
		if (file.exists()) {
			return file;
		}
		return null;
	}
}
