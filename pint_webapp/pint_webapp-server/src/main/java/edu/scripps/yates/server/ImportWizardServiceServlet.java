/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package edu.scripps.yates.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.LocalOboControlVocabularyManager;
import org.proteored.miapeapi.cv.LocalOboTestControlVocabularyManager;
import org.proteored.miapeapi.cv.TissuesTypes;
import org.proteored.miapeapi.cv.msi.PeptideModificationName;
import org.proteored.miapeapi.cv.msi.Score;
import org.xml.sax.SAXException;

import com.compomics.dbtoolkit.io.DBLoaderLoader;
import com.compomics.dbtoolkit.io.interfaces.DBLoader;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.scripps.yates.ImportWizardService;
import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.CensusChroParser;
import edu.scripps.yates.census.read.CensusOutParser;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.ExcelSheet;
import edu.scripps.yates.excel.impl.ExcelFileImpl;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ConditionRefType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.mzidentmlparser.MzIdentMLIdentificationsParser;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.server.configuration.PintConfigurationPropertiesIO;
import edu.scripps.yates.server.lock.LockerByTag;
import edu.scripps.yates.server.projectCreator.ImportCfgFileParserUtil;
import edu.scripps.yates.server.projectCreator.adapter.FileSetAdapter;
import edu.scripps.yates.server.projectCreator.adapter.RemoteSSHFileReferenceAdapter;
import edu.scripps.yates.server.projectCreator.adapter.bean2excel.FileTypeAdapter;
import edu.scripps.yates.server.projectCreator.adapter.bean2excel.PintImportCfgAdapter;
import edu.scripps.yates.server.projectCreator.adapter.bean2excel.ServerTypeAdapter;
import edu.scripps.yates.server.projectCreator.adapter.excel2bean.PintImportCfgBeanAdapter;
import edu.scripps.yates.server.projectCreator.adapter.excel2modelbean.DataSourceBeanAdapter;
import edu.scripps.yates.server.projectCreator.adapter.excel2modelbean.ProjectBeanAdapter;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.FileWithFormat;
import edu.scripps.yates.server.util.RemoteFileWithType;
import edu.scripps.yates.server.util.ServerConstants;
import edu.scripps.yates.server.util.ServletCommonInit;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.FileSummary;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.projectCreator.ExcelDataReference;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.AccessionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;
import edu.scripps.yates.shared.util.CryptoUtil;
import edu.scripps.yates.shared.util.Pair;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import edu.scripps.yates.utilities.taxonomy.UniprotOrganism;
import edu.scripps.yates.utilities.taxonomy.UniprotSpeciesCodeMap;
import edu.scripps.yates.utilities.xml.XMLSchemaValidatorErrorHandler;
import edu.scripps.yates.utilities.xml.XmlSchemaValidator;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import psidev.psi.tools.validator.ValidatorMessage;

public class ImportWizardServiceServlet extends RemoteServiceServlet implements ImportWizardService {
	private static int importProcessNumber = 0;
	private final ObjectFactory factory = new ObjectFactory();
	private ControlVocabularyManager cvManager;
	private List<String> scoreTypes;
	private List<String> ptmNames;
	private final Map<File, ExcelFileImpl> excelFileReaders = new THashMap<File, ExcelFileImpl>();

	private final static List<String> tissues = new ArrayList<String>();
	private final static List<String> organisms = new ArrayList<String>();
	private static int numMaxInSuggestionLists = Integer.MAX_VALUE;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ImportWizardServiceServlet.class);

	public ImportWizardServiceServlet() {

	}

	/**
	 * Overrided in order to run the method implemented on ServletCommonInit
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		ServletCommonInit.init(config.getServletContext());
		final String developerMode = System.getenv().get(SharedConstants.PINT_DEVELOPER_ENV_VAR);
		if (developerMode != null) {
			numMaxInSuggestionLists = 10;
			log.info("Developer mode activated. NumMaxSuggestion set to " + numMaxInSuggestionLists);
			log.info("Using OBO files for tests");
			cvManager = new LocalOboTestControlVocabularyManager();

		} else {
			log.info("Development mode not found");
			log.info("Using full OBO files");
			cvManager = new LocalOboControlVocabularyManager();
		}
	}

	@Override
	public int startNewImportProcess(String sessionID, String projectTag, List<FileNameWithTypeBean> dataFileNames,
			List<RemoteFileWithTypeBean> remoteFilesWithTypes) throws PintException {
		// create new identifier for the process
		final int importProcessIdentifier = generateNewImportProcessID();
		// check if the project name is not used before
		// checkProjectAvailability(projectTag);
		// check if all data files are present
		final List<FileWithFormat> files = checkDataFileAvailabilities(importProcessIdentifier, dataFileNames);
		// check if all remote files are accessible
		checkRemoteFilesAvailabilities(sessionID, remoteFilesWithTypes);
		// get the files to the local system
		final List<RemoteFileWithType> remoteFiles = getRemoteFilesWithType(importProcessIdentifier,
				remoteFilesWithTypes);

		// index data files by process ID
		FileManager.indexFilesByImportProcessID(importProcessIdentifier, files);
		// index remote files by process ID
		FileManager.indexRemoteFilesByImportProcessID(importProcessIdentifier, remoteFiles);
		// create projectCfgFile
		final File cfgFile = createImportCfgFile(importProcessIdentifier, projectTag, files, remoteFiles);
		// index projectCfgFile by process ID
		FileManager.indexProjectCfgFileByImportProcessID(importProcessIdentifier, cfgFile);
		return importProcessIdentifier;
	}

	@Override
	public void addDataFiles(String sessionID, int jobID, List<FileNameWithTypeBean> dataFiles) throws PintException {
		log.info("Adding data files to the server");
		// get import data object
		ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);

		// divide the files into the RemoteFiles and the local ones:
		final List<RemoteFileWithTypeBean> remoteFiles = new ArrayList<RemoteFileWithTypeBean>();
		final List<FileNameWithTypeBean> localFiles = new ArrayList<FileNameWithTypeBean>();
		for (final FileNameWithTypeBean dataFile : dataFiles) {
			if (dataFile instanceof RemoteFileWithTypeBean) {
				remoteFiles.add((RemoteFileWithTypeBean) dataFile);
			} else {
				localFiles.add(dataFile);
			}
		}

		// check if all data files are present
		checkRemoteFilesAvailabilities(sessionID, remoteFiles);
		final List<FileWithFormat> availableLocalFiles = checkDataFileAvailabilities(jobID, localFiles);

		// index data files by process ID
		if (!availableLocalFiles.isEmpty())
			FileManager.indexFilesByImportProcessID(jobID, availableLocalFiles);

		log.info("Files added successfully");
	}

	@Override
	public void addDataFile(String sessionID, int jobID, FileNameWithTypeBean dataFile) throws PintException {
		// get import data object
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);

		final List<FileNameWithTypeBean> list = new ArrayList<FileNameWithTypeBean>();
		list.add(dataFile);
		addDataFiles(sessionID, jobID, list);
	}

	@Override
	public void addRemoteFiles(String sessionID, int jobID, List<RemoteFileWithTypeBean> remoteFilesWithTypes)
			throws PintException {
		// get import data object
		ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);

		// check if all remote files are accessible
		checkRemoteFilesAvailabilities(sessionID, remoteFilesWithTypes);
		// get the files locally
		final List<RemoteFileWithType> remoteFiles = getRemoteFilesWithType(jobID, remoteFilesWithTypes);
		// index files
		FileManager.indexRemoteFilesByImportProcessID(jobID, remoteFiles);

	}

	@Override
	public void addRemoteFile(String sessionID, int jobID, RemoteFileWithTypeBean remoteFilesWithType)
			throws PintException {
		final List<RemoteFileWithTypeBean> remotefileWithTypeBeanList = new ArrayList<RemoteFileWithTypeBean>();
		remotefileWithTypeBeanList.add(remoteFilesWithType);
		addRemoteFiles(sessionID, jobID, remotefileWithTypeBeanList);
	}

	@Override
	public int startNewImportProcess(String sessionID, String uploadedProjectCfgFileName) throws PintException {
		// create new identifier for the process

		final File cfgFile = FileManager.getProjectXmlFile(uploadedProjectCfgFileName);

		// read file and get the project object
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportFromFile(cfgFile);
		Integer importProcessIdentifier = pintImportCfg.getImportID();
		if (importProcessIdentifier == null) {
			log.info("Received file with no importID. The system will assign a new importID.");
			importProcessIdentifier = generateNewImportProcessID();
			log.info("The new importID is: " + importProcessIdentifier);
		} else {
			log.info("Received an XML file for the import process id: " + importProcessIdentifier);
		}
		// index projectCfgFile by process ID
		FileManager.indexProjectCfgFileByImportProcessID(importProcessIdentifier, cfgFile);
		return importProcessIdentifier;
	}

	@Override
	public ProjectBean getProjectBean(String sessionID, int jobID) throws PintException {
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);
		final ProjectBean projectbean = new ProjectBeanAdapter(pintImportCfg.getProject()).adapt();
		return projectbean;

	}

	@Override
	public PintImportCfgBean getPintImportCfgTypeBean(String sessionID, int jobID) throws PintException {
		try {
			final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);
			final PintImportCfgBean ret = new PintImportCfgBeanAdapter(pintImportCfg).adapt();
			return ret;
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.IMPORT_XML_SCHEMA_ERROR);
		}
	}

	@Override
	public List<String> getOrganismList(String sessionID) {
		log.info("Getting organism list");
		if (organisms.isEmpty()) {
			final List<UniprotOrganism> uniprotOrganims = UniprotSpeciesCodeMap.getInstance().getOrganisms();
			for (final UniprotOrganism uniprotOrganism : uniprotOrganims) {
				// TODO remove
				if (organisms.size() == numMaxInSuggestionLists)
					break;
				organisms.add(uniprotOrganism.getScientificName());
			}
			// sort by name
			Collections.sort(organisms);
		}
		log.info("Returning organisms list with " + organisms.size() + " elements");
		return organisms;
	}

	@Override
	public List<String> getTissueList(String sessionID) {
		log.info("Getting tissue list");
		synchronized (sessionID) {
			if (tissues.isEmpty()) {
				final List<ControlVocabularyTerm> possibleValues = TissuesTypes.getInstance(cvManager)
						.getPossibleValues();
				for (final ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
					// TODO remove
					if (tissues.size() == numMaxInSuggestionLists)
						break;
					tissues.add(controlVocabularyTerm.getPreferredName());
				}
				// sort by name
				Collections.sort(tissues);
			}
		}
		log.info("Returning tissue list with " + tissues.size() + " elements");
		return tissues;
	}

	@Override
	public void checkServerAccessibility(String sessionID, ServerTypeBean server) throws PintException {
		final String password = server.getPassword();
		final boolean accessOK = RemoteSSHFileReference.loginCheck(server.getHostName(), server.getUserName(),
				password);
		if (!accessOK)
			throw new PintException("Error connecting to server '" + server.getHostName() + "'",
					PINT_ERROR_TYPE.REMOTE_SERVER_NOT_REACHABLE);
	}

	@Override
	public void checkRemoteFileInfoAccessibility(String sessionID, RemoteFileWithTypeBean remoteFileWithTypeBean)
			throws PintException {
		String errorMessage = null;

		final ServerTypeBean server = remoteFileWithTypeBean.getServer();

		if (server == null || server.getHostName() == null || server.getUserName() == null
				|| server.getPassword() == null) {
			errorMessage = "Server information is missing";
		} else {
			try {
				final String password = server.getPassword();
				final RemoteSSHFileReference remoteFile = new RemoteSSHFileReference(server.getHostName(),
						server.getUserName(), password, remoteFileWithTypeBean.getFileName(), null);
				remoteFile.setRemotePath(remoteFileWithTypeBean.getRemotePath());
				errorMessage = remoteFile.isAvailable();
			} catch (final IllegalArgumentException e) {
				errorMessage = e.getMessage();
			}
		}

		if (errorMessage != null)
			throw new PintException(errorMessage, PINT_ERROR_TYPE.REMOTE_FILE_NOT_REACHABLE);

	}

	private File createImportCfgFile(int importProcessIdentifier, String projectTag, List<FileWithFormat> files,
			List<RemoteFileWithType> remoteFilesWithTypes) {
		final PintImportCfg pintImportCfg = factory.createPintImportCfg();
		pintImportCfg.setImportID(importProcessIdentifier);

		final ProjectType project = factory.createProjectType();
		project.setTag(projectTag);
		pintImportCfg.setProject(project);
		pintImportCfg.setFileSet(new FileSetAdapter(files, remoteFilesWithTypes).adapt());
		return ImportCfgFileParserUtil.saveFileCfg(pintImportCfg);

	}

	/**
	 * Create a new fileCfg updating the tag attribute of the project
	 *
	 * @param projectTag
	 * @param fileCfg
	 * @return
	 * @throws PintException
	 */
	private File updateImportCfgFile(String projectTag, File fileCfg) throws PintException {
		// get the import object
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportFromFile(fileCfg);
		// set name and id
		pintImportCfg.getProject().setTag(projectTag);
		// save locally with the new name
		return ImportCfgFileParserUtil.saveFileCfg(pintImportCfg);

	}

	/**
	 * Generate a new ID for the importProcess
	 *
	 * @return
	 */
	@Override
	public int generateNewImportProcessID() {
		importProcessNumber++;
		while (FileManager.getProjectDataFileFolder(importProcessNumber, false).exists()) {
			importProcessNumber++;
		}
		return importProcessNumber;
	}

	/**
	 * Checks if the file are in the server in the appropriate location
	 *
	 * @param jobID
	 * @param dataFileName
	 * @throws PintException if the file is not in the server
	 */
	@Override
	public void checkDataFileAvailability(String sessionID, int jobID, FileNameWithTypeBean dataFileName)
			throws PintException {

		if (dataFileName == null)
			throw new PintException("No file to check", PINT_ERROR_TYPE.REMOTE_FILE_NOT_REACHABLE);
		log.info("Checking file availability of '" + dataFileName.getFileName() + "'");
		if (dataFileName.getFileName() == null || "".equals(dataFileName.getFileName())) {
			final String message = "File name is empty or not valid";
			log.warn(message);
			throw new PintException(message, PINT_ERROR_TYPE.MISSING_INFORMATION);
		}
		if (dataFileName.getFileFormat() == null) {
			final String message = "File format is empty or not valid";
			log.warn(message);
			throw new PintException(message, PINT_ERROR_TYPE.MISSING_INFORMATION);
		}

		final File dataFile = FileManager.getDataFile(jobID, dataFileName.getFileName(), dataFileName.getId(),
				dataFileName.getFileFormat());
		if (!dataFile.exists()) {
			final String message = "File '" + dataFileName.getFileName() + "' not found in the server";
			log.warn(message);
			throw new PintException(message, PINT_ERROR_TYPE.REMOTE_FILE_NOT_REACHABLE);
		}
		log.info(dataFileName.getFileName() + " with format " + dataFileName.getFileFormat().getName() + " in jobID "
				+ jobID + " in sessionID " + sessionID + " is present and valid");
	}

	/**
	 * Checks if the files are in the server in the appropriate location and if they
	 * are all there, return the {@link List} of {@link File}
	 *
	 * @param jobID
	 * @param dataFileNames
	 * @return the list of files in the server that are available
	 * @throws PintException if some of the files are not in the server
	 */
	private List<FileWithFormat> checkDataFileAvailabilities(int jobID, List<FileNameWithTypeBean> dataFileNames)
			throws PintException {

		final List<String> filesNotFound = new ArrayList<String>();
		final List<FileWithFormat> ret = new ArrayList<FileWithFormat>();
		if (dataFileNames == null || dataFileNames.isEmpty())
			return ret;
		log.info("Checking file availabilities of " + dataFileNames.size() + " files");
		for (final FileNameWithTypeBean dataFileName : dataFileNames) {
			final File dataFile = FileManager.getDataFile(jobID, dataFileName.getFileName(), dataFileName.getId(),
					dataFileName.getFileFormat());
			if (!dataFile.exists()) {
				filesNotFound.add(dataFileName.getFileName());
			} else {
				if (dataFileName.getFileFormat() == null) {
					final String message = "Format is not specified for file '" + dataFileName.getId() + "'";
					log.warn(message);
					throw new PintException(message, PINT_ERROR_TYPE.FILE_READING_ERROR);
				}
				final FormatType formatType = FormatType.fromValue(dataFileName.getFileFormat().name().toLowerCase());
				if (formatType == null) {
					final String message = "Format '" + dataFileName.getFileFormat().name()
							+ "' is not valid for file '" + dataFileName.getId() + "'";
					log.warn(message);
					throw new PintException(message, PINT_ERROR_TYPE.FILE_READING_ERROR);
				}
				ret.add(new FileWithFormat(dataFileName.getId(), dataFile, formatType,
						dataFileName.getFastaDigestionBean()));
			}
		}

		if (!filesNotFound.isEmpty()) {
			final StringBuilder sb = new StringBuilder();
			for (final String fileNotFound : filesNotFound) {
				if (!"".equals(sb.toString()))
					sb.append(",");
				sb.append(fileNotFound);
			}
			final String message = "The following files were not found in the server: " + sb.toString();
			log.warn(message);
			throw new PintException(message, PINT_ERROR_TYPE.FILE_NOT_FOUND_IN_SERVER);
		}
		log.info(ret.size() + " files are valid");
		return ret;
	}

	/**
	 * Checks if all remote files are accessible
	 *
	 * @param remoteFilesWithTypesBeans
	 * @throws PintException
	 */
	private void checkRemoteFilesAvailabilities(String sessionID,
			List<RemoteFileWithTypeBean> remoteFilesWithTypesBeans) throws PintException {

		for (final RemoteFileWithTypeBean remoteFileWithTypeBean : remoteFilesWithTypesBeans) {
			checkRemoteFileInfoAccessibility(sessionID, remoteFileWithTypeBean);
		}
	}

	/**
	 * Checks if all remote files are accessible and return a {@link List} of
	 * {@link RemoteFileWithType}
	 *
	 * @param remoteFilesWithTypesBeans
	 * @return
	 * @throws PintException
	 */
	private List<RemoteFileWithType> getRemoteFilesWithType(int jobID,
			List<RemoteFileWithTypeBean> remoteFilesWithTypesBeans) throws PintException {
		final List<RemoteFileWithType> ret = new ArrayList<RemoteFileWithType>();
		if (remoteFilesWithTypesBeans == null)
			return ret;
		for (final RemoteFileWithTypeBean remoteFileWithTypeBean : remoteFilesWithTypesBeans) {
			final File outputFile = FileManager.getDataFile(jobID, remoteFileWithTypeBean.getFileName(),
					remoteFileWithTypeBean.getId(), remoteFileWithTypeBean.getFileFormat());
			final ServerTypeBean serverBean = remoteFileWithTypeBean.getServer();
			final RemoteSSHFileReference remoteFileReference = new RemoteSSHFileReference(serverBean.getHostName(),
					serverBean.getUserName(), serverBean.getPassword(), remoteFileWithTypeBean.getFileName(),
					outputFile);
			remoteFileReference.setRemotePath(remoteFileWithTypeBean.getRemotePath());
			FormatType fileType = null;
			if (remoteFileWithTypeBean.getFileFormat() != null)
				fileType = FormatType.fromValue(remoteFileWithTypeBean.getFileFormat().name().toLowerCase());
			final ServerType server = new ServerTypeAdapter(serverBean).adapt();
			final RemoteFileWithType remoteFile = new RemoteFileWithType(remoteFileWithTypeBean.getId(),
					remoteFileReference, fileType, server, remoteFileWithTypeBean.getFastaDigestionBean());
			ret.add(remoteFile);

		}
		return ret;
	}

	/**
	 * If the project exists in the DB, throws and exception
	 *
	 * @param projectTag
	 * @throws PintException
	 */
	private void checkProjectAvailability(String projectTag) throws PintException {
		final Set<ProjectBean> projectBeans = RemoteServicesTasks.getProjectBeans();
		for (final ProjectBean projectBean : projectBeans) {
			if (projectBean.getTag().equalsIgnoreCase(projectTag))
				throw new PintException("Project '" + projectTag + "' already exists in the server",
						PINT_ERROR_TYPE.PROJECT_ALREADY_IN_SERVER);
		}
	}

	public ExcelFileImpl getExcelReader(File file) throws IOException {
		ExcelFileImpl excelFile = null;
		if (excelFileReaders.containsKey(file)) {
			excelFile = excelFileReaders.get(file);
		} else {
			excelFile = new ExcelFileImpl(file);
			excelFileReaders.put(file, excelFile);
		}
		return excelFile;
	}

	@Override
	public List<String> getRandomValues(String sessionID, int jobId, ExcelDataReference excelDataReference,
			int numValues) throws PintException {
		final FileWithFormat file = FileManager.getFileByImportProcessID(jobId,
				excelDataReference.getExcelFileNameWithTypeBean());
		if (file == null)
			throw new PintException(
					"File '" + excelDataReference.getExcelFileNameWithTypeBean().getId() + "' not found in server",
					PINT_ERROR_TYPE.FILE_NOT_FOUND_IN_SERVER);
		try {
			final ExcelFileImpl excelFile = getExcelReader(file.getFile());

			final ExcelSheet excelSheet = excelFile.getSheetMap().get(excelDataReference.getSheetName());
			if (excelSheet != null) {
				final ExcelColumn excelColumn = excelSheet.getColumnMap().get(excelDataReference.getColumnKey());
				if (excelColumn != null) {
					final List<Object> randomValues = excelColumn.getRandomValues(numValues);
					if (randomValues != null) {
						final List<String> ret = new ArrayList<String>();
						for (final Object randomValue : randomValues) {
							if (randomValue == null)
								ret.add("");
							else
								ret.add(randomValue.toString());
						}
						return ret;
					}
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.FILE_READING_ERROR);
		}
		throw new PintException("No information has been readed", PINT_ERROR_TYPE.FILE_READING_ERROR);

	}

	@Override
	public List<String> getAvailableACCTypes(String sessionID) {
		final List<String> ret = new ArrayList<String>();

		final AccessionTypeBean[] values = AccessionTypeBean.values();
		for (final AccessionTypeBean accessionType : values) {
			ret.add(accessionType.name());
		}
		return ret;
	}

	@Override
	public String getTaxId(String sessionID, String organismName) {
		final UniprotOrganism uniprotOrganism = UniprotSpeciesCodeMap.getInstance().get(organismName);
		if (uniprotOrganism != null) {
			return String.valueOf(uniprotOrganism.getTaxonCode());
		}
		return null;
	}

	@Override
	public List<FileFormat> getFileFormats(String sessionID) throws PintException {
		final FormatType[] values = FormatType.values();
		final List<FileFormat> ret = new ArrayList<FileFormat>();
		for (final FormatType format : values) {
			final FileFormat fileFormatFromString = FileFormat.getFileFormatFromString(format.name());
			if (fileFormatFromString != null)
				ret.add(fileFormatFromString);
		}
		// sort
		Collections.sort(ret);
		return ret;
	}

	@Override
	public List<DataSourceBean> getDataSourceBeans(String sessionID, int jobID) throws PintException {
		final List<DataSourceBean> ret = new ArrayList<DataSourceBean>();
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);
		if (pintImportCfg.getFileSet() != null) {
			for (final FileType fileType : pintImportCfg.getFileSet().getFile()) {
				ret.add(new DataSourceBeanAdapter(fileType, pintImportCfg.getServers()).adapt());
			}
		}
		return ret;
	}

	private List<String> getRandomProteinAccessions(int importJobID, RemoteFileWithTypeBean remoteFileWithType,
			FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues) throws PintException {
		try {
			final RemoteSSHFileReference remoteSSHFileReference = new RemoteSSHFileReferenceAdapter(remoteFileWithType)
					.adapt();
			switch (remoteFileWithType.getFileFormat()) {
			case CENSUS_OUT_TXT:
				return RemoteServicesTasks.getRandomProteinAccessionsFromCensusOut(importJobID, remoteSSHFileReference,
						fileNameWithTypeFasta, numRandomValues);
			case CENSUS_CHRO_XML:
				return RemoteServicesTasks.getRandomProteinAccessionsFromCensusChro(importJobID, remoteSSHFileReference,
						fileNameWithTypeFasta, numRandomValues);
			case DTA_SELECT_FILTER_TXT:
				return RemoteServicesTasks.getRandomProteinAccessionsFromDTASelectFile(importJobID,
						remoteSSHFileReference, fileNameWithTypeFasta, numRandomValues);
			default:
				throw new PintException(remoteFileWithType.getFileFormat() + " is not supported for this call",
						PINT_ERROR_TYPE.FILE_NOT_SUPPORTED_FOR_THIS_CALL);
			}

		} catch (final IOException e) {
			throw new PintException(e, PINT_ERROR_TYPE.FILE_READING_ERROR);
		}
	}

	@Override
	public List<String> getRandomProteinAccessions(String sessionID, int importJobID,
			FileNameWithTypeBean fileNameWithType, FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues)
			throws PintException {
		try {
			if (fileNameWithType instanceof RemoteFileWithTypeBean) {
				return getRandomProteinAccessions(importJobID, (RemoteFileWithTypeBean) fileNameWithType,
						fileNameWithTypeFasta, numRandomValues);
			} else {
				final FileWithFormat localFile = FileManager.getFileByImportProcessID(importJobID, fileNameWithType);
				if (localFile != null) {
					switch (localFile.getFormat()) {
					case CENSUS_OUT_TXT:
						return RemoteServicesTasks.getRandomProteinAccessionsFromCensusOut(importJobID,
								localFile.getFile(), fileNameWithTypeFasta, numRandomValues);
					case CENSUS_CHRO_XML:
						return RemoteServicesTasks.getRandomProteinAccessionsFromCensusChro(importJobID,
								localFile.getFile(), fileNameWithTypeFasta, numRandomValues);
					case DTA_SELECT_FILTER_TXT:
						return RemoteServicesTasks.getRandomProteinAccessionsFromDTASelectFile(importJobID,
								localFile.getFile(), fileNameWithTypeFasta, numRandomValues);
					default:
						throw new PintException(localFile.getFormat() + " is not supported for this call",
								PINT_ERROR_TYPE.FILE_NOT_SUPPORTED_FOR_THIS_CALL);
					}
				}
			}
		} catch (final Exception e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.FILE_READING_ERROR);
		}
		return null;
	}

	@Override
	public boolean saveImportConfiguration(int jobID, PintImportCfgBean pintImportCfgBean) throws PintException {
		try {
			log.info("Saving import project XML file with jobID: " + jobID + " and project tag: "
					+ pintImportCfgBean.getProject().getTag() + " having "
					+ pintImportCfgBean.getProject().getExperimentalConditions().getExperimentalCondition().size()
					+ " conditions");

			final PintImportCfg pintImportCfg = new PintImportCfgAdapter(pintImportCfgBean).adapt();

			// add the fileSet with the uploaded files
			if (pintImportCfg.getFileSet() == null)
				pintImportCfg.setFileSet(new FileSetType());
			final List<FileWithFormat> filesByImportProcessID = FileManager.getFilesByImportProcessID(jobID, null);
			if (filesByImportProcessID != null) {
				log.info("Adding attached files information from " + filesByImportProcessID.size() + " files");
				for (final FileWithFormat fileWithFormat : filesByImportProcessID) {
					log.info("Adding attached files information from: " + fileWithFormat.getFile().getAbsoluteFile());
					// look if the Id is already in the fileset, and in that
					// case, update the fileType
					FileType found = null;
					for (final FileType fileType : pintImportCfg.getFileSet().getFile()) {
						if (fileType.getId().equals(fileWithFormat.getId()))
							found = fileType;
					}
					if (found == null) {
						if (fileWithFormat.getFormat() != null) {
							pintImportCfg.getFileSet().getFile().add(new FileTypeAdapter(fileWithFormat).adapt());
						}
					} else {
						// update the object
						found.setFormat(fileWithFormat.getFormat());
						found.setName(fileWithFormat.getFileName());
						found.setUrl(fileWithFormat.getFile().toURI().toString());
					}
				}
			}
			// decrypt the passwords if present
			decryptPasswords(pintImportCfg);

			log.info("Saving xml file...");
			// save the xml file
			final File savedFileCfg = ImportCfgFileParserUtil.saveFileCfg(pintImportCfg);
			log.info("Import project XML file saved at: " + savedFileCfg.getAbsolutePath());

			// index projectCfgFile by process ID
			FileManager.indexProjectCfgFileByImportProcessID(jobID, savedFileCfg);

			// check project tag
			checkProjectAvailability(pintImportCfg.getProject().getTag());
			// check Against Schema
			validateImportConfigurationFile(savedFileCfg);
			return true;

		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	private void decryptPasswords(PintImportCfg pintImportCfg) {
		if (pintImportCfg != null) {
			if (pintImportCfg.getServers() != null) {
				for (final ServerType server : pintImportCfg.getServers().getServer()) {
					server.setPassword(CryptoUtil.decrypt(server.getPassword()));
				}
			}
		}

	}

	public void validateImportConfigurationFile(File savedFileCfg) throws PintException {

		try {
			final StringBuilder errorString = new StringBuilder();
			final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportFromFile(savedFileCfg);
			// check experimental conditions
			if (pintImportCfg.getProject() != null && pintImportCfg.getProject().getExperimentalConditions() != null) {
				for (final ExperimentalConditionType conditionType : pintImportCfg.getProject()
						.getExperimentalConditions().getExperimentalCondition()) {
					boolean valid = true;
					if (conditionType.getIdentificationInfo() == null
							&& conditionType.getQuantificationInfo() == null) {
						valid = false;
					} else {
						if ((conditionType.getIdentificationInfo() == null
								|| (conditionType.getIdentificationInfo().getExcelIdentInfo().isEmpty()
										&& conditionType.getIdentificationInfo().getRemoteFilesIdentInfo().isEmpty()))
								&& (conditionType.getQuantificationInfo() == null
										|| (conditionType.getQuantificationInfo().getExcelQuantInfo().isEmpty()
												&& conditionType.getQuantificationInfo().getRemoteFilesQuantInfo()
														.isEmpty()))) {
							valid = false;
						}
					}
					if (!valid) {
						errorString.append(
								"Identification data or protein/peptide amounts are needed in experimental condition '"
										+ conditionType.getId() + "'\n");
					}
				}
			}
			// check ratios
			boolean someRatio = false;
			final Set<String> conditionsInRatios = new THashSet<String>();
			final List<RemoteFilesRatioType> remoteFilesRatioTypes = new ArrayList<RemoteFilesRatioType>();
			if (pintImportCfg.getProject() != null && pintImportCfg.getProject().getExperimentalConditions() != null) {
				final RatiosType ratios = pintImportCfg.getProject().getRatios();
				if (ratios != null) {

					if (ratios.getPsmAmountRatios() != null
							&& ratios.getPsmAmountRatios().getRemoteFilesRatio() != null) {
						remoteFilesRatioTypes.addAll(ratios.getPsmAmountRatios().getRemoteFilesRatio());
					}
					if (ratios.getPeptideAmountRatios() != null
							&& ratios.getPeptideAmountRatios().getRemoteFilesRatio() != null) {
						remoteFilesRatioTypes.addAll(ratios.getPeptideAmountRatios().getRemoteFilesRatio());
					}
					if (ratios.getProteinAmountRatios() != null
							&& ratios.getProteinAmountRatios().getRemoteFilesRatio() != null) {
						remoteFilesRatioTypes.addAll(ratios.getProteinAmountRatios().getRemoteFilesRatio());
					}
					if (!remoteFilesRatioTypes.isEmpty()) {
						someRatio = true;
					}

					for (final RemoteFilesRatioType remoteFilesRatioType : remoteFilesRatioTypes) {
						validateRemoteFilesRatio(errorString, remoteFilesRatioType,
								pintImportCfg.getProject().getExperimentalConditions());
						final String numerator = remoteFilesRatioType.getNumerator().getConditionRef();
						conditionsInRatios.add(numerator);
						final String denominator = remoteFilesRatioType.getDenominator().getConditionRef();
						conditionsInRatios.add(denominator);
						if (numerator.equals(denominator)) {
							errorString.append(
									"Error in the definition of a Ratio. A ratio has to be linked to 2 different conditions.\n");
						}
					}
				}
			}
			// if there is some quantitative ratio, we need at least two labels
			if (someRatio) {
				if (pintImportCfg.getProject().getExperimentalDesign() != null
						&& pintImportCfg.getProject().getExperimentalDesign() != null) {
					int numLabels = 0;
					if (pintImportCfg.getProject().getExperimentalDesign().getLabelSet() != null) {
						for (final LabelType label : pintImportCfg.getProject().getExperimentalDesign().getLabelSet()
								.getLabel()) {
							numLabels++;
							final QuantificationLabel quantLabel = QuantificationLabel.getByName(label.getId());
							if (quantLabel == null) {
								errorString.append("Label '" + label.getId()
										+ "' not recognized. Try to select one of the following values:\n'"
										+ QuantificationLabel.getValuesString() + "'\n");
								errorString
										.append("If you need to add a new label, contact to salvador at scripps.edu\n");
							}
						}
					}
					if (numLabels < 2) {
						errorString.append(
								"The definition of at least 2 labels is necessary when having some quantitative ratio in your project\n");
					}
				}
			}

			if (!conditionsInRatios.isEmpty()) {
				if (pintImportCfg.getProject().getExperimentalConditions() != null) {
					if (pintImportCfg.getProject().getExperimentalConditions().getExperimentalCondition() != null) {
						final List<Pair<String, String>> conditionPairsInRatios = new ArrayList<Pair<String, String>>();
						final Set<String> samplesInRatios = new THashSet<String>();
						for (final ExperimentalConditionType condition : pintImportCfg.getProject()
								.getExperimentalConditions().getExperimentalCondition()) {
							if (conditionsInRatios.contains(condition.getId())) {
								if (condition.getSampleRef() != null) {
									samplesInRatios.add(condition.getSampleRef());
								} else {
									errorString.append("Experimental condition '" + condition.getId()
											+ "' has to be linked to a sample\n");
								}
							}
							for (final RemoteFilesRatioType remoteFileRatio : remoteFilesRatioTypes) {
								// numerator
								if (remoteFileRatio.getNumerator().getConditionRef().equals(condition.getId())) {
									boolean found = false;
									for (final Pair<String, String> conditionPair : conditionPairsInRatios) {
										// if first element is numerator
										if (conditionPair.getFirstElement().equals(condition.getId())) {
											// and second element is denominator
											if (conditionPair.getSecondElement()
													.equals(remoteFileRatio.getDenominator().getConditionRef())) {
												found = true;
											}
										}
									}
									if (!found) {
										final Pair<String, String> newPair = new Pair<String, String>(
												remoteFileRatio.getNumerator().getConditionRef(),
												remoteFileRatio.getDenominator().getConditionRef());
										conditionPairsInRatios.add(newPair);
									}

								}
								// denominator
								if (remoteFileRatio.getDenominator().getConditionRef().equals(condition.getId())) {
									boolean found = false;
									for (final Pair<String, String> conditionPair : conditionPairsInRatios) {
										// if second element is denominator
										if (conditionPair.getSecondElement().equals(condition.getId())) {
											// and first element is numerator
											if (conditionPair.getFirstElement()
													.equals(remoteFileRatio.getNumerator().getConditionRef())) {
												found = true;
											}
										}
									}
									if (!found) {
										final Pair<String, String> newPair = new Pair<String, String>(
												remoteFileRatio.getNumerator().getConditionRef(),
												remoteFileRatio.getDenominator().getConditionRef());
										conditionPairsInRatios.add(newPair);
									}
								}
							}
						}
						// translate conditionPairsInRatios to
						// samplesPairsInRatios
						for (final ExperimentalConditionType condition : pintImportCfg.getProject()
								.getExperimentalConditions().getExperimentalCondition()) {
							for (final Pair<String, String> conditionPair : conditionPairsInRatios) {
								if (conditionPair.getFirstElement().equals(condition.getId())) {
									if (condition.getSampleRef() != null) {
										conditionPair.setFirstElement(condition.getSampleRef());
									}
								}
								if (conditionPair.getSecondElement().equals(condition.getId())) {
									if (condition.getSampleRef() != null) {
										conditionPair.setSecondElement(condition.getSampleRef());
									}
								}
							}
						}
						final List<Pair<String, String>> samplePairsInRatios = conditionPairsInRatios;

						if (pintImportCfg.getProject().getExperimentalDesign() != null
								&& pintImportCfg.getProject().getExperimentalDesign().getSampleSet() != null) {
							for (final SampleType sample : pintImportCfg.getProject().getExperimentalDesign()
									.getSampleSet().getSample()) {
								if (samplesInRatios.contains(sample.getId())) {
									if (sample.getLabelRef() == null) {
										errorString.append("Sample '" + sample.getId()
												+ "' has to be linked to a Label, because it has been referentiated by an experimental condition that is defined in a quantitative ratio.\n");
									}
								}
								for (final Pair<String, String> pair : samplePairsInRatios) {
									if (pair.getFirstElement().equals(sample.getId())) {
										pair.setFirstElement(sample.getLabelRef());
									}
									if (pair.getSecondElement().equals(sample.getId())) {
										pair.setSecondElement(sample.getLabelRef());
									}
									if (pair.getFirstElement().equals(pair.getSecondElement())) {
										errorString
												.append("There is a Ratio between 2 conditions having the same label '"
														+ pair.getFirstElement() + "' coming from sample '"
														+ sample.getId()
														+ "' (see Condition->Sample->Label relationships)\n");
									}
								}
							}
						}

					}
				}
			}

			final URL schemaLocation = this.getClass().getClassLoader().getResource(ServerConstants.PROJECT_XML_CFG);
			final Reader reader = new FileReader(savedFileCfg);
			final XMLSchemaValidatorErrorHandler errorHandler = XmlSchemaValidator.validate(reader,
					XmlSchemaValidator.getSchema(schemaLocation));
			if (!errorHandler.getErrorsAsValidatorMessages().isEmpty()) {

				for (final ValidatorMessage validatorMessage : errorHandler.getErrorsAsValidatorMessages()) {
					if (!"".equals(errorString.toString()))
						errorString.append("\n");
					errorString.append(translateValidatorMessageIntoUserMessage(validatorMessage.getMessage()));
				}

			}
			if (!"".equals(errorString.toString()))
				throw new PintException(errorString.toString(), PINT_ERROR_TYPE.IMPORT_XML_SCHEMA_ERROR);
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.FILE_NOT_FOUND_IN_SERVER);
		} catch (final SAXException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.IMPORT_XML_SCHEMA_ERROR);
		}
	}

	private String translateValidatorMessageIntoUserMessage(String message) {
		if (message.contains("Attribute 'sampleRef' must appear on element 'experimental_condition'")) {
			return "EXPERIMENTAL CONDITION MUST have a reference to a SAMPLE";
		}
		if (message.contains("Attribute 'organismRef' must appear on element 'sample'")) {
			return "SAMPLE MUST have a reference to an ORGANISM";
		}
		if (message.contains("Attribute 'msRunRef' must appear on element 'remoteFiles_ident_info'.")) {
			return "All identification datasets MUST reference a MS RUN";
		}
		if (message.contains("Attribute 'msRunRef' must appear on element 'remoteFiles_quant_info'")) {
			return "All quantification datasets MUST reference a MS RUN";
		}
		if (message.contains("Attribute 'msRunRef' must appear on element 'remoteFiles_ratio'.")) {
			return "All ratios MUST reference a MS RUN";
		}
		if (message.contains("The content of element 'sampleSet' is not complete. One of '{sample}' is expected.")) {
			return "At least one SAMPLE MUST be defined";
		}
		if (message
				.contains("The content of element 'organismSet' is not complete. One of '{organism}' is expected.")) {
			return "At least one ORGANISM MUST be defined";
		}
		if (message.contains("The content of element 'tissueSet' is not complete. One of '{tissue}' is expected.")) {
			return "At least one SAMPLE ORIGIN MUST be defined";
		}
		if (message.contains(
				"The content of element 'experimental_conditions' is not complete. One of '{experimental_condition}' is expected.")) {
			return "At least one EXPERIMENTAL CONDITION MUST be defined";
		}
		if (message.contains("The content of element 'fileSet' is not complete. One of '{file}' is expected.")) {
			return "At least one INPUT FILE MUST be defined";
		}
		if (message.contains("The content of element 'msRuns' is not complete. One of '{msRun}' is expected.")) {
			return "At least one MS RUN MUST be defined";
		}
		if (message.contains("Attribute 'score_type' must appear on element 'ratio_score'.")) {
			return "Ratio score MUST have a ratio type description";
		}
		if (message.contains(
				"Invalid content was found starting with element 'ratio_score'. One of '{protein_accession, peptide_sequence, psm_id, numerator}' is expected")) {
			return "You have to define the experimental conditions used in the ratio";
		}
		// if not recognized
		return message;
	}

	/**
	 * This function will validate a {@link RemoteFilesRatioType} checking that the
	 * fileID is referred in both conditions and also referring msRun
	 *
	 * @param errorString
	 *
	 * @param remoteFilesRatioType
	 * @param experimentalConditionsType
	 */
	private void validateRemoteFilesRatio(StringBuilder errorString, RemoteFilesRatioType remoteFilesRatioType,
			ExperimentalConditionsType experimentalConditionsType) {

		final String msRunID = remoteFilesRatioType.getMsRunRef();

		final String fileID = remoteFilesRatioType.getFileRef();
		final ConditionRefType condition1ID = remoteFilesRatioType.getNumerator();
		final ConditionRefType condition2ID = remoteFilesRatioType.getDenominator();
		log.info("Validating ratio from remote files: fileID:" + fileID + " runID:" + msRunID + " condition1:"
				+ condition1ID + " condition2:" + condition2ID);
		if (msRunID == null) {
			if (!"".equals(errorString.toString())) {
				errorString.append("\n");
			}
			errorString.append("Reference to MSRun is missing in ratio " + remoteFilesRatioType.getName());
		}
	}

	@Override
	public String submitProject(int jobID, PintImportCfgBean pintImportCfgBean) throws PintException {
		try {
			// clear cache dbIndexes
			ImportCfgUtil.clearDBIndexes();

			log.info("Submitting project with jobID: " + jobID + " and project tag: "
					+ pintImportCfgBean.getProject().getTag());

			// save import xml file
			final boolean valid = saveImportConfiguration(jobID, pintImportCfgBean);
			final File projectCfgFileByImportProcessID = FileManager.getProjectCfgFileByImportProcessID(jobID);
			if (!valid) {
				// this will throw a PintException with the error messages
				validateImportConfigurationFile(projectCfgFileByImportProcessID);
			}
			// save project into the database
			final ImportCfgFileReader importReader = new ImportCfgFileReader();
			// TODO
			ImportCfgFileReader.ignoreDTASelectParameterT = true;
			// TODO

			log.info("Reading project from XML file");
			final Project projectFromCfgFile = importReader.getProjectFromCfgFile(projectCfgFileByImportProcessID,
					FileManager.getFastaIndexFolder());
			log.info("Project object created from XML file");
			log.info("Beginning saving transaction");
			// ThreadSessionHandler.beginGoodTransaction();
			new MySQLSaver().saveProject(projectFromCfgFile);
			// ThreadSessionHandler.finishGoodTransaction();
			log.info("Project sucessfully saved in database");
			// log.info("Removing data files from server");
			// FileManager.removeDataFiles(jobID, projectFilesPath);
			// log.info("Data files removed");
			ContextualSessionHandler.finishGoodTransaction();

			final String encryptedProjectTag = CryptoUtil.encrypt(pintImportCfgBean.getProject().getTag());
			return encryptedProjectTag;
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public FileTypeBean getExcelFileBean(String sessionID, int jobId, FileNameWithTypeBean file) throws PintException {
		if (file == null)
			throw new PintException("File is null", PINT_ERROR_TYPE.FILE_READING_ERROR);
		if (!file.getFileFormat().equals(FileFormat.EXCEL))
			throw new PintException("The file " + file.getFileName() + " is not an Excel file",
					PINT_ERROR_TYPE.FILE_NOT_SUPPORTED_FOR_THIS_CALL);

		final List<FileNameWithTypeBean> dataFileNames = new ArrayList<FileNameWithTypeBean>();
		dataFileNames.add(file);
		checkDataFileAvailabilities(jobId, dataFileNames);

		final File excelFile = FileManager.getDataFile(jobId, file.getFileName(), file.getId(), file.getFileFormat());
		ExcelFileImpl reader;
		try {
			reader = getExcelReader(excelFile);
			final FileTypeBean ret = new FileTypeBean();
			ret.setId(file.getId());
			final List<ExcelSheet> excelSheets = reader.getSheets();
			if (!excelSheets.isEmpty()) {
				ret.setSheets(new SheetsTypeBean());
			}
			for (final ExcelSheet excelSheet : excelSheets) {
				final SheetTypeBean sheetBean = new SheetTypeBean();
				sheetBean.setId(file.getId() + SharedConstants.EXCEL_ID_SEPARATOR + excelSheet.getName());
				final List<ExcelColumn> excelColumns = excelSheet.getColumns();
				if (excelColumns != null) {
					for (final ExcelColumn excelColumn : excelColumns) {
						final ColumnTypeBean columnBean = new ColumnTypeBean();
						columnBean.setHeader(excelColumn.getHeader());
						columnBean.setId(sheetBean.getId() + SharedConstants.EXCEL_ID_SEPARATOR + excelColumn.getKey());
						sheetBean.getColumn().add(columnBean);
					}
				}
				ret.getSheets().getSheet().add(sheetBean);
			}
			return ret;
		} catch (final Exception e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.FILE_READING_ERROR);
		}
	}

	@Override
	public List<String> getScoreTypes(String sessionID) {
		if (scoreTypes == null) {
			scoreTypes = new ArrayList<String>();
			final Set<String> set = new THashSet<String>();
			// TODO take the possible values from here?
			final List<ControlVocabularyTerm> possibleValues = Score.getInstance(cvManager).getPossibleValues();
			for (final ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
				set.add(controlVocabularyTerm.getPreferredName());
			}

			scoreTypes.addAll(set);
			Collections.sort(scoreTypes);
		}
		return scoreTypes;
	}

	@Override
	public List<String> getPTMNames(String sessionID) {
		if (ptmNames == null) {
			ptmNames = new ArrayList<String>();
			final Set<String> set = new THashSet<String>();
			// TODO take the possible values from here?
			final List<ControlVocabularyTerm> possibleValues = PeptideModificationName.getInstance(cvManager)
					.getPossibleValues();
			for (final ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
				set.add(controlVocabularyTerm.getPreferredName());
			}

			ptmNames.addAll(set);
			Collections.sort(ptmNames);
		}
		return ptmNames;
	}

	@Override
	public void removeDataFile(String sessionID, int jobID, FileNameWithTypeBean fileNameWithType) {
		if (fileNameWithType == null)
			return;
		if (fileNameWithType.getFileFormat() == null) {
			fileNameWithType.setFileFormat(NewFileUploadServlet.defaultFileFormat);
		}
		final FileWithFormat fileByImportProcessID = FileManager.getFileByImportProcessID(jobID, fileNameWithType);
		if (fileByImportProcessID != null) {
			FileManager.removeFromIndexFileByImportProcessID(jobID, fileByImportProcessID);
			final File oldDataFile = fileByImportProcessID.getFile();
			oldDataFile.delete();

			// remove oldDataFile parent folder
			final File parentFile = oldDataFile.getParentFile();

			edu.scripps.yates.utilities.files.FileUtils.removeFolderIfEmtpy(parentFile, true);

		}
	}

	@Override
	public void moveDataFile(String sessionID, int jobID, FileNameWithTypeBean fileOLD, FileNameWithTypeBean fileNew)
			throws PintException {

		final File oldDataFile = FileManager.getDataFile(jobID, fileOLD.getFileName(), fileOLD.getId(),
				fileOLD.getFileFormat());
		final File newDataFile = FileManager.getDataFile(jobID, fileNew.getFileName(), fileNew.getId(),
				fileNew.getFileFormat());
		final Method method = new Object() {
		}.getClass().getEnclosingMethod();
		try {

			LockerByTag.lock(sessionID, method);
			// move the file
			if (!oldDataFile.exists()) {
				return;
			}
			if (newDataFile.exists()) {
				newDataFile.delete();
			}

			FileUtils.moveFile(oldDataFile, newDataFile);
			// remove oldDataFile parent folder if empty recursively
			final File parentFile = oldDataFile.getParentFile();

			edu.scripps.yates.utilities.files.FileUtils.removeFolderIfEmtpy(parentFile, true);

			// remove old index
			if (fileOLD.getFileFormat() != null) { // only if has a format has
				// been indexed before
				final FormatType format = FormatType.fromValue(fileOLD.getFileFormat().name().toLowerCase());
				final FileWithFormat oldFileWithFormat = new FileWithFormat(fileOLD.getId(), oldDataFile, format,
						fileOLD.getFastaDigestionBean());
				FileManager.removeFromIndexFileByImportProcessID(jobID, oldFileWithFormat);
				// index the new file
				final FileWithFormat newFileWithFormat = new FileWithFormat(fileNew.getId(), newDataFile, format,
						fileNew.getFastaDigestionBean());
				FileManager.indexFileByImportProcessID(jobID, newFileWithFormat);
			}

		} catch (final IOException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.MOVING_FILE_ERROR);
		} finally {
			LockerByTag.unlock(sessionID, method);
		}

	}

	@Override
	public void checkUserLogin(String userName, String encryptedPassword) throws PintException {
		final String adminPassword = PintConfigurationPropertiesIO
				.readProperties(FileManager.getPINTPropertiesFile(getServletContext())).getAdminPassword();
		if (adminPassword == null || "".equals(adminPassword)) {
			return;
		} else if (adminPassword.equals(encryptedPassword)) {
			return;
		} else {
			throw new PintException("Password is not correct", PINT_ERROR_TYPE.LOGIN_FAILED);
		}

	}

	@Override
	public String validatePintImportCfg(PintImportCfgTypeBean pintImportCfgBean) throws PintException {
		final PintImportCfg pintImportCfg = new PintImportCfgAdapter(pintImportCfgBean).adapt();
		final File file = ImportCfgFileParserUtil.saveFileCfg(pintImportCfg);
		return file.getAbsolutePath();
	}

	@Override
	public FileTypeBean updateFileFormat(String sessionID, int importID, FileTypeBean fileTypeBean)
			throws PintException {
		final List<FileWithFormat> files = FileManager.getFilesByImportProcessID(importID);
		if (files == null) {
			throw new PintException("Error updating file format", PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
		for (final FileWithFormat fileWithFormat : files) {
			final String fileName = fileTypeBean.getName();
			if (fileWithFormat.getFileName().equals(fileName)) {
				final FileFormat newFormat = fileTypeBean.getFormat();
				if (!fileWithFormat.getFormat().name().equals(newFormat.getName())) {
					// change its folder
					final File newFile = FileManager.getDataFile(importID, fileWithFormat.getFileName(),
							fileWithFormat.getId(), newFormat);
					if (newFile.exists()) {
						final String message = "There is already a file named '" + fileWithFormat.getFileName()
								+ "' with format " + newFormat.getName();
						log.error(message);
						throw new PintException(message, PINT_ERROR_TYPE.INTERNAL_ERROR);
					} else {
						try {
							FileUtils.moveFile(fileWithFormat.getFile(), newFile);
							// remove oldDataFile parent folder if empty recursively
							edu.scripps.yates.utilities.files.FileUtils
									.removeFolderIfEmtpy(fileWithFormat.getFile().getParentFile(), true);

							// remove old index
							// been indexed before
							final FormatType oldFormat = fileWithFormat.getFormat();
							final FileWithFormat oldFileWithFormat = new FileWithFormat(fileWithFormat.getId(),
									fileWithFormat.getFile(), oldFormat, fileWithFormat.getFastaDigestionBean());
							FileManager.removeFromIndexFileByImportProcessID(importID, oldFileWithFormat);
							// index the new file with new format
							final FileWithFormat newFileWithFormat = new FileWithFormat(fileWithFormat.getId(), newFile,
									FormatType.fromValue(newFormat.name().toLowerCase()),
									fileWithFormat.getFastaDigestionBean());
							FileManager.indexFileByImportProcessID(importID, newFileWithFormat);

							// if the newFormat is Excel, populate the object with the sheets information
							if (newFormat == FileFormat.EXCEL) {
								final FileNameWithTypeBean fileNameWithTypeBean = new FileNameWithTypeBean();
								fileNameWithTypeBean.setFileFormat(newFormat);
								fileNameWithTypeBean.setFileName(fileName);
								fileNameWithTypeBean.setId(fileWithFormat.getId());
								final FileTypeBean ret = getExcelFileBean(sessionID, importID, fileNameWithTypeBean);
								return ret;
							} else {
								fileTypeBean.setSheets(null);
								return fileTypeBean;
							}
						} catch (final IOException e) {
							e.printStackTrace();
							throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
						}
					}
				}
			}
		}
		throw new PintException("File not found", PINT_ERROR_TYPE.FILE_NOT_FOUND_IN_SERVER);
	}

	@Override
	public FileSummary getFileSummary(int importID, String sessionID, FileTypeBean fileTypeBean) throws PintException {
		try {
			final File file = FileManager.getDataFile(importID, fileTypeBean.getName(), fileTypeBean.getId(),
					fileTypeBean.getFormat());
			if (fileTypeBean.getFormat() == FileFormat.DTA_SELECT_FILTER_TXT) {
				final DTASelectParser parser = new DTASelectParser(file);
				final FileSummary ret = new FileSummary();
				ret.setFileTypeBean(fileTypeBean);
				ret.setFileSizeString(
						edu.scripps.yates.utilities.files.FileUtils.getDescriptiveSizeFromBytes(file.length()));
				ret.setNumProteins(parser.getProteinMap().size());
				ret.setNumPeptides(parser.getPSMsByFullSequence().size());
				ret.setNumPSMs(parser.getPSMsByPSMID().size());
				return ret;
			} else if (fileTypeBean.getFormat() == FileFormat.CENSUS_OUT_TXT) {
				final QuantCondition cond1 = new QuantCondition("cond1");
				final QuantCondition cond2 = new QuantCondition("cond2");
				final CensusOutParser parser = new CensusOutParser(file, QuantificationLabel.LIGHT, cond1,
						QuantificationLabel.HEAVY, cond2);
				final FileSummary ret = new FileSummary();
				ret.setFileTypeBean(fileTypeBean);
				ret.setFileSizeString(
						edu.scripps.yates.utilities.files.FileUtils.getDescriptiveSizeFromBytes(file.length()));
				ret.setNumProteins(parser.getProteinMap().size());
				ret.setNumPeptides(parser.getPeptideMap().size());
				ret.setNumPSMs(parser.getPSMMap().size());
				return ret;
			} else if (fileTypeBean.getFormat() == FileFormat.CENSUS_CHRO_XML) {
				final QuantCondition cond1 = new QuantCondition("cond1");
				final QuantCondition cond2 = new QuantCondition("cond2");
				final CensusChroParser parser = new CensusChroParser(file, QuantificationLabel.LIGHT, cond1,
						QuantificationLabel.HEAVY, cond2);
				final FileSummary ret = new FileSummary();
				ret.setFileTypeBean(fileTypeBean);
				ret.setFileSizeString(
						edu.scripps.yates.utilities.files.FileUtils.getDescriptiveSizeFromBytes(file.length()));
				ret.setNumProteins(parser.getProteinMap().size());
				ret.setNumPeptides(parser.getPeptideMap().size());
				ret.setNumPSMs(parser.getPSMMap().size());
				return ret;
			} else if (fileTypeBean.getFormat() == FileFormat.FASTA) {
				final DBLoader loader = DBLoaderLoader.loadDB(file);
				final long numProteins = loader.countNumberOfEntries();
				final FileSummary ret = new FileSummary();
				ret.setFileTypeBean(fileTypeBean);
				ret.setFileSizeString(
						edu.scripps.yates.utilities.files.FileUtils.getDescriptiveSizeFromBytes(file.length()));
				ret.setNumProteins(new Long(numProteins).intValue());
//				ret.setNumPeptides(parser.getPeptideMap().size());
//				ret.setNumPSMs(parser.getPSMMap().size());
				return ret;
			} else if (fileTypeBean.getFormat() == FileFormat.MZIDENTML) {
				final MzIdentMLIdentificationsParser parser = new MzIdentMLIdentificationsParser(file);
				final FileSummary ret = new FileSummary();
				ret.setFileTypeBean(fileTypeBean);
				ret.setFileSizeString(
						edu.scripps.yates.utilities.files.FileUtils.getDescriptiveSizeFromBytes(file.length()));
				ret.setNumProteins(parser.getProteinMap().size());
				ret.setNumPeptides(parser.getPSMsByFullSequence().size());
				ret.setNumPSMs(parser.getPSMsByPSMID().size());
				return ret;
			}
			throw new IllegalArgumentException("Format not supported yet!");
		} catch (final Exception e) {
			e.printStackTrace();

			throw new PintException(e, PINT_ERROR_TYPE.FILE_READING_ERROR);
		}
	}

	@Override
	public String getUploadedFileID(int importID, String uploadedFileSignature) throws PintException {
		final List<FileWithFormat> files = FileManager.getFilesByImportProcessID(importID);
		for (final FileWithFormat fileWithFormat : files) {
			final String signature = fileWithFormat.getFileName() + fileWithFormat.getFile().length();
			if (signature.equals(uploadedFileSignature)) {
				return fileWithFormat.getId();
			}
		}
		throw new PintException("File with signature '" + uploadedFileSignature + "' not found",
				PINT_ERROR_TYPE.FILE_NOT_FOUND_IN_SERVER);
	}

}
