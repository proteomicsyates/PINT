package edu.scripps.yates.server.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
	private static final String UNIPROT_RELEASES_DATES_FILE_NAME = "uniprot_releases_dates.txt";
	private static String projectFilesPath;
	private static Map<Integer, List<FileWithFormat>> filesByImportProcessID = new HashMap<Integer, List<FileWithFormat>>();
	private static Map<Integer, File> projectCfgFileByImportProcessID = new HashMap<Integer, File>();
	private static boolean ready = false;
	private static boolean loading;
	private final static DecimalFormat myFormatter = new DecimalFormat("#.#");

	private static File getXmlFolder() {

		String folderName = getProjectFilesPath() + File.separator + XML;
		File folder = new File(folderName);
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
		for (File xmlFile : xmlFolder.listFiles()) {
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
				} catch (Exception e) {
					log.warn("Error reading project cfg file: " + xmlFile.getAbsolutePath());
				}
			}
		}

		ready = true;
		loading = false;
	}

	private static void moveToOLD(File fileToDelete) {
		log.info("Moving file " + fileToDelete.getAbsolutePath() + " to " + getOLDXmlFolder().getAbsolutePath());
		File destineFile = getOLDXmlFile(FilenameUtils.getBaseName(fileToDelete.getAbsolutePath()));
		try {
			FileUtils.moveFile(fileToDelete, destineFile);
			log.info(fileToDelete.getAbsolutePath() + " moved to " + destineFile.getAbsolutePath());
		} catch (IOException e) {
			log.warn(e);
		}

	}

	private static File getOLDXmlFile(String fileName) {
		return new File(getOLDXmlFolder().getAbsolutePath() + File.separator + fileName + ".xml");
	}

	private static File getOLDXmlFolder() {
		String folderName = getXmlFolder() + File.separator + OLD;
		File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	public static File getDataFile(int jobID, String fileName, String fileId, FileFormat format) {
		File folder = getDataFileFolder(jobID, fileId, format);
		File file = new File(folder.getAbsoluteFile() + File.separator + fileName);
		return file;
	}

	private static File getDataFileFolder(int jobID, String fileId, FileFormat format) {

		final File projectDataFileFolder = getProjectDataFileFolder(jobID, true);
		File folder = new File(
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

		String folderName = getProjectFilesPath() + File.separator + DATA + File.separator + jobID;
		File folder = new File(folderName);
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
		File folder = getDownloadFileFolder();
		File file = new File(folder.getAbsoluteFile() + File.separator + fileName);
		return file;
	}

	/**
	 * Gets the file located at: projectFilesPath/REACTOME/
	 *
	 * @param fileName
	 * @return
	 */
	public static File getReactomeFile(String fileName) {
		File folder = getReactomeFileFolder();
		File file = new File(folder.getAbsoluteFile() + File.separator + fileName);
		return file;
	}

	private static File getDownloadFileFolder() {
		String folderName = getProjectFilesPath() + File.separator + DOWNLOAD;
		File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	private static File getReactomeFileFolder() {
		String folderName = getProjectFilesPath() + File.separator + REACTOME;
		File folder = new File(folderName);
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
		String folderName = getProjectFilesPath() + File.separator + UNIPROT_RELEASES;
		File folder = new File(folderName);
		if (!folder.exists()) {
			log.info("Creating " + folder.getAbsolutePath() + " on server");
			folder.mkdirs();
		} else {
			log.info(folder.getAbsolutePath() + " found in server");
		}
		return folder;
	}

	public static File getProjectXmlFile(String projectXmlFileName) {
		File folder = getXmlFolder();
		File file = new File(folder.getAbsoluteFile() + File.separator + projectXmlFileName);
		return file;
	}

	public static String getProjectFilesPath(ServletContext servletContext) {
		Map<String, String> env = System.getenv();
		if (projectFilesPath == null || projectFilesPath.equals(System.getProperty("java.io.tmpdir"))) {
			String projectFilePathProperty = ServletContextProperty.PROJECT_FILES_PATH_SERVER;
			if (env.containsKey(ServerConstants.PINT_DEVELOPER_ENV_VAR)
					&& env.get(ServerConstants.PINT_DEVELOPER_ENV_VAR).equals("true")) {
				log.info("USING DEVELOPMENT MODE");
				projectFilePathProperty = ServletContextProperty.PROJECT_FILES_PATH;
			}
			log.info("Using init parameter: " + projectFilePathProperty);
			try {
				projectFilesPath = ServletContextProperty.getServletContextProperty(servletContext,
						projectFilePathProperty);
				log.info("Using: " + projectFilePathProperty + "=" + projectFilesPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			loadIfNeeded();
		}
		return projectFilesPath;
	}

	public static void indexFilesByImportProcessID(int importProcessIdentifier, Collection<FileWithFormat> files) {
		if (files == null || files.isEmpty())
			return;
		log.info("Storing " + files.size() + " data files in server");
		for (FileWithFormat fileWithType : files) {

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
			List<FileWithFormat> list = new ArrayList<FileWithFormat>();
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
		File actualFile = remoteFile.getRemoteFileSSHReference().getRemoteFile();
		FormatType actualFileType = remoteFile.getFormat();
		FileWithFormat fileWithType = new FileWithFormat(remoteFile.getId(), actualFile, actualFileType,
				remoteFile.getFastaDigestionBean());
		indexFileByImportProcessID(importProcessIdentifier, fileWithType);

	}

	public static void indexRemoteFilesByImportProcessID(int importProcessIdentifier,
			Collection<RemoteFileWithType> remoteFiles) {
		if (remoteFiles == null)
			return;
		for (RemoteFileWithType remoteFileWithType : remoteFiles) {
			indexRemoteFileByImportProcessID(importProcessIdentifier, remoteFileWithType);
		}
	}

	public static List<FileWithFormat> getFilesByImportProcessID(int importProcessIdentifier, FileFormat format) {
		if (!filesByImportProcessID.containsKey(importProcessIdentifier) && format != null) {
			final File projectDataFileFolder = getProjectDataFileFolder(importProcessIdentifier, true);
			if (projectDataFileFolder.exists() && projectDataFileFolder.listFiles() != null) {
				for (File dataFileFolder : projectDataFileFolder.listFiles()) {
					if (dataFileFolder.listFiles().length == 1) {
						final File dataFileFormatFolder = dataFileFolder.listFiles()[0];
						if (dataFileFormatFolder.listFiles().length == 1) {
							final File dataFile = dataFileFormatFolder.listFiles()[0];
							if (dataFile.exists()) {
								// TODO substitute the null by something

								FileWithFormat obj = new FileWithFormat(
										FilenameUtils.getName(dataFile.getAbsolutePath()), dataFile,
										FormatType.fromValue(format.name().toLowerCase()), null);
								if (filesByImportProcessID.containsKey(importProcessIdentifier)) {
									filesByImportProcessID.get(importProcessIdentifier).add(obj);
								} else {
									List<FileWithFormat> list = new ArrayList<FileWithFormat>();
									list.add(obj);
									filesByImportProcessID.put(importProcessIdentifier, list);
								}
							}
						}
					}
				}
			}
		}
		return filesByImportProcessID.get(importProcessIdentifier);
	}

	public static FileWithFormat getFileByImportProcessID(int importProcessIdentifier,
			FileNameWithTypeBean fileNameWithType) {

		List<FileWithFormat> indexedFiles = getFilesByImportProcessID(importProcessIdentifier,
				fileNameWithType.getFileFormat());
		if (indexedFiles != null) {
			for (FileWithFormat indexedFileWithType : indexedFiles) {
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
	 * Gets the path to the file
	 * /projectDefaultPath/FASTAS/fasta_file_name.fasta
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
	 * Removes all the content of the folder of data files for the jobID
	 * provided
	 *
	 * @param jobID
	 */
	public static void removeDataFiles(int jobID) {
		log.info("Removing files from server for import process: " + jobID);
		final File projectDataFileFolder = FileManager.getProjectDataFileFolder(jobID, false);
		// go to the latest folder
		try {
			edu.scripps.yates.utilities.files.FileUtils.deleteFolderRecursive(projectDataFileFolder);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("Error deleting files for import process: " + jobID + "\t" + e.getMessage());
		}

	}

	/**
	 * Gets the file in which the uniprot releases dates will be stored
	 *
	 * @param projectFilesPath2
	 * @return
	 */
	public static File getUniprotReleasesDatesFile() {
		return new File(
				getUniprotReleasesFolder().getAbsolutePath() + File.separator + UNIPROT_RELEASES_DATES_FILE_NAME);

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
}
