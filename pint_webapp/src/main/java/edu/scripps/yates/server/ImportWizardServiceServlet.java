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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.scripps.yates.client.ImportWizardService;
import edu.scripps.yates.client.exceptions.PintException;
import edu.scripps.yates.client.exceptions.PintException.PINT_ERROR_TYPE;
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
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ProjectType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RatiosType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.server.configuration.ConfigurationPropertiesIO;
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
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.projectCreator.ExcelDataReference;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.AccessionTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ColumnTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;
import edu.scripps.yates.shared.util.CryptoUtil;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import edu.scripps.yates.utilities.taxonomy.UniprotOrganism;
import edu.scripps.yates.utilities.taxonomy.UniprotSpeciesCodeMap;
import edu.scripps.yates.utilities.xml.XMLSchemaValidatorErrorHandler;
import edu.scripps.yates.utilities.xml.XmlSchemaValidator;
import psidev.psi.tools.validator.ValidatorMessage;

public class ImportWizardServiceServlet extends RemoteServiceServlet implements ImportWizardService {
	private static int importProcessNumber = 0;
	private final ObjectFactory factory = new ObjectFactory();
	private ControlVocabularyManager cvManager;
	private List<String> scoreTypes;
	private List<String> ptmNames;
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
		ServletCommonInit.init(getServletContext());
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
		int importProcessIdentifier = generateNewImportProcessID();
		// check if the project name is not used before
		// checkProjectAvailability(projectTag);
		// check if all data files are present
		List<FileWithFormat> files = checkDataFileAvailabilities(importProcessIdentifier, dataFileNames);
		// check if all remote files are accessible
		checkRemoteFilesAvailabilities(sessionID, remoteFilesWithTypes);
		// get the files to the local system
		List<RemoteFileWithType> remoteFiles = getRemoteFilesWithType(importProcessIdentifier, remoteFilesWithTypes);

		// index data files by process ID
		FileManager.indexFilesByImportProcessID(importProcessIdentifier, files);
		// index remote files by process ID
		FileManager.indexRemoteFilesByImportProcessID(importProcessIdentifier, remoteFiles);
		// create projectCfgFile
		File cfgFile = createImportCfgFile(importProcessIdentifier, projectTag, files, remoteFiles);
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
		List<RemoteFileWithTypeBean> remoteFiles = new ArrayList<RemoteFileWithTypeBean>();
		List<FileNameWithTypeBean> localFiles = new ArrayList<FileNameWithTypeBean>();
		for (FileNameWithTypeBean dataFile : dataFiles) {
			if (dataFile instanceof RemoteFileWithTypeBean) {
				remoteFiles.add((RemoteFileWithTypeBean) dataFile);
			} else {
				localFiles.add(dataFile);
			}
		}

		// check if all data files are present
		checkRemoteFilesAvailabilities(sessionID, remoteFiles);
		List<FileWithFormat> availableLocalFiles = checkDataFileAvailabilities(jobID, localFiles);

		// index data files by process ID
		if (!availableLocalFiles.isEmpty())
			FileManager.indexFilesByImportProcessID(jobID, availableLocalFiles);

		log.info("Files added successfully");
	}

	@Override
	public void addDataFile(String sessionID, int jobID, FileNameWithTypeBean dataFile) throws PintException {
		// get import data object
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);

		List<FileNameWithTypeBean> list = new ArrayList<FileNameWithTypeBean>();
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
		List<RemoteFileWithType> remoteFiles = getRemoteFilesWithType(jobID, remoteFilesWithTypes);
		// index files
		FileManager.indexRemoteFilesByImportProcessID(jobID, remoteFiles);

	}

	@Override
	public void addRemoteFile(String sessionID, int jobID, RemoteFileWithTypeBean remoteFilesWithType)
			throws PintException {
		List<RemoteFileWithTypeBean> remotefileWithTypeBeanList = new ArrayList<RemoteFileWithTypeBean>();
		remotefileWithTypeBeanList.add(remoteFilesWithType);
		addRemoteFiles(sessionID, jobID, remotefileWithTypeBeanList);
	}

	@Override
	public int startNewImportProcess(String sessionID, String uploadedProjectCfgFileName) throws PintException {
		// create new identifier for the process

		File cfgFile = FileManager.getProjectXmlFile(uploadedProjectCfgFileName);

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
		ProjectBean projectbean = new ProjectBeanAdapter(pintImportCfg.getProject()).adapt();
		return projectbean;

	}

	@Override
	public PintImportCfgBean getPintImportCfgTypeBean(String sessionID, int jobID) throws PintException {
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);
		PintImportCfgBean ret = new PintImportCfgBeanAdapter(pintImportCfg).adapt();
		return ret;

	}

	@Override
	public List<String> getOrganismList(String sessionID) {
		log.info("Getting organism list");
		if (organisms.isEmpty()) {
			List<UniprotOrganism> uniprotOrganims = UniprotSpeciesCodeMap.getInstance().getOrganisms();
			for (UniprotOrganism uniprotOrganism : uniprotOrganims) {
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
				for (ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
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
		boolean accessOK = RemoteSSHFileReference.loginCheck(server.getHostName(), server.getUserName(), password);
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
				RemoteSSHFileReference remoteFile = new RemoteSSHFileReference(server.getHostName(),
						server.getUserName(), password, remoteFileWithTypeBean.getFileName(), null);
				remoteFile.setRemotePath(remoteFileWithTypeBean.getRemotePath());
				errorMessage = remoteFile.isAvailable();
			} catch (IllegalArgumentException e) {
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

		ProjectType project = factory.createProjectType();
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
	private int generateNewImportProcessID() {
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
	 * @throws PintException
	 *             if the file is not in the server
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
	 * Checks if the files are in the server in the appropriate location and if
	 * they are all there, return the {@link List} of {@link File}
	 *
	 * @param jobID
	 * @param dataFileNames
	 * @return the list of files in the server that are available
	 * @throws PintException
	 *             if some of the files are not in the server
	 */
	private List<FileWithFormat> checkDataFileAvailabilities(int jobID, List<FileNameWithTypeBean> dataFileNames)
			throws PintException {

		List<String> filesNotFound = new ArrayList<String>();
		List<FileWithFormat> ret = new ArrayList<FileWithFormat>();
		if (dataFileNames == null || dataFileNames.isEmpty())
			return ret;
		log.info("Checking file availabilities of " + dataFileNames.size() + " files");
		for (FileNameWithTypeBean dataFileName : dataFileNames) {
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
			StringBuilder sb = new StringBuilder();
			for (String fileNotFound : filesNotFound) {
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

		for (RemoteFileWithTypeBean remoteFileWithTypeBean : remoteFilesWithTypesBeans) {
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
		List<RemoteFileWithType> ret = new ArrayList<RemoteFileWithType>();
		if (remoteFilesWithTypesBeans == null)
			return ret;
		for (RemoteFileWithTypeBean remoteFileWithTypeBean : remoteFilesWithTypesBeans) {
			File outputFile = FileManager.getDataFile(jobID, remoteFileWithTypeBean.getFileName(),
					remoteFileWithTypeBean.getId(), remoteFileWithTypeBean.getFileFormat());
			final ServerTypeBean serverBean = remoteFileWithTypeBean.getServer();
			RemoteSSHFileReference remoteFileReference = new RemoteSSHFileReference(serverBean.getHostName(),
					serverBean.getUserName(), serverBean.getPassword(), remoteFileWithTypeBean.getFileName(),
					outputFile);
			remoteFileReference.setRemotePath(remoteFileWithTypeBean.getRemotePath());
			FormatType fileType = null;
			if (remoteFileWithTypeBean.getFileFormat() != null)
				fileType = FormatType.fromValue(remoteFileWithTypeBean.getFileFormat().name().toLowerCase());
			ServerType server = new ServerTypeAdapter(serverBean).adapt();
			RemoteFileWithType remoteFile = new RemoteFileWithType(remoteFileWithTypeBean.getId(), remoteFileReference,
					fileType, server, remoteFileWithTypeBean.getFastaDigestionBean());
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
		for (ProjectBean projectBean : projectBeans) {
			if (projectBean.getTag().equalsIgnoreCase(projectTag))
				throw new PintException("Project '" + projectTag + "' already exists in the server",
						PINT_ERROR_TYPE.PROJECT_ALREADY_IN_SERVER);
		}
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
			ExcelFileImpl excelFile = new ExcelFileImpl(file.getFile());
			final ExcelSheet excelSheet = excelFile.getSheetMap().get(excelDataReference.getSheetName());
			if (excelSheet != null) {
				final ExcelColumn excelColumn = excelSheet.getColumnMap().get(excelDataReference.getColumnKey());
				if (excelColumn != null) {
					final List<Object> randomValues = excelColumn.getRandomValues(numValues);
					if (randomValues != null) {
						List<String> ret = new ArrayList<String>();
						for (Object randomValue : randomValues) {
							if (randomValue == null)
								ret.add("");
							else
								ret.add(randomValue.toString());
						}
						return ret;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.FILE_READING_ERROR);
		}
		throw new PintException("No information has been readed", PINT_ERROR_TYPE.FILE_READING_ERROR);

	}

	@Override
	public List<String> getAvailableACCTypes(String sessionID) {
		List<String> ret = new ArrayList<String>();

		final AccessionTypeBean[] values = AccessionTypeBean.values();
		for (AccessionTypeBean accessionType : values) {
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
		List<FileFormat> ret = new ArrayList<FileFormat>();
		for (FormatType format : values) {
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
		List<DataSourceBean> ret = new ArrayList<DataSourceBean>();
		final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportCfgFromJobID(jobID);
		if (pintImportCfg.getFileSet() != null) {
			for (FileType fileType : pintImportCfg.getFileSet().getFile()) {
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

		} catch (IOException e) {
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
		} catch (Exception e) {
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
				for (FileWithFormat fileWithFormat : filesByImportProcessID) {
					log.info("Adding attached files information from: " + fileWithFormat.getFile().getAbsoluteFile());
					// look if the Id is already in the fileset, and in that
					// case, update the fileType
					FileType found = null;
					for (FileType fileType : pintImportCfg.getFileSet().getFile()) {
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

		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	private void decryptPasswords(PintImportCfg pintImportCfg) {
		if (pintImportCfg != null) {
			if (pintImportCfg.getServers() != null) {
				for (ServerType server : pintImportCfg.getServers().getServer()) {
					server.setPassword(CryptoUtil.decrypt(server.getPassword()));
				}
			}
		}

	}

	public void validateImportConfigurationFile(File savedFileCfg) throws PintException {

		try {
			StringBuilder sb = new StringBuilder();
			final PintImportCfg pintImportCfg = ImportCfgFileParserUtil.getPintImportFromFile(savedFileCfg);
			// check experimental conditions
			if (pintImportCfg.getProject() != null && pintImportCfg.getProject().getExperimentalConditions() != null) {
				for (ExperimentalConditionType conditionType : pintImportCfg.getProject().getExperimentalConditions()
						.getExperimentalCondition()) {
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
						sb.append(
								"Identification data or protein/peptide amounts are needed in experimental condition '"
										+ conditionType.getId() + "'\n");
					}
				}
			}
			// check ratios
			if (pintImportCfg.getProject() != null && pintImportCfg.getProject().getExperimentalConditions() != null) {
				final RatiosType ratios = pintImportCfg.getProject().getRatios();
				if (ratios != null) {
					if (ratios.getPsmAmountRatios() != null) {
						for (RemoteFilesRatioType remoteFilesRatioType : ratios.getPsmAmountRatios()
								.getRemoteFilesRatio()) {
							validateRemoteFilesRatio(sb, remoteFilesRatioType,
									pintImportCfg.getProject().getExperimentalConditions());
							// validate If Ligh and Heavy Labels are there

						}
					}
				}
			}

			URL schemaLocation = this.getClass().getClassLoader().getResource(ServerConstants.PROJECT_XML_CFG);
			Reader reader = new FileReader(savedFileCfg);
			final XMLSchemaValidatorErrorHandler errorHandler = XmlSchemaValidator.validate(reader,
					XmlSchemaValidator.getSchema(schemaLocation));
			if (!errorHandler.getErrorsAsValidatorMessages().isEmpty()) {

				for (ValidatorMessage validatorMessage : errorHandler.getErrorsAsValidatorMessages()) {
					if (!"".equals(sb.toString()))
						sb.append("\n");
					sb.append(translateValidatorMessageIntoUserMessage(validatorMessage.getMessage()));
				}

			}
			if (!"".equals(sb.toString()))
				throw new PintException(sb.toString(), PINT_ERROR_TYPE.IMPORT_XML_SCHEMA_ERROR);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.FILE_NOT_FOUND_IN_SERVER);
		} catch (SAXException e) {
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
		// if not recognized
		return message;
	}

	/**
	 * This function will validate a {@link RemoteFilesRatioType} checking that
	 * the fileID is referred in both conditions and also referring msRun
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
		// TODO
	}

	@Override
	public String submitProject(int jobID, PintImportCfgBean pintImportCfgBean) throws PintException {
		try {
			// clear cache dbIndexes
			ImportCfgUtil.clearDBIndexes();

			log.info("Submitting project with jobID: " + jobID + " and project tag: "
					+ pintImportCfgBean.getProject().getTag());

			// save import xml file
			boolean valid = saveImportConfiguration(jobID, pintImportCfgBean);
			final File projectCfgFileByImportProcessID = FileManager.getProjectCfgFileByImportProcessID(jobID);
			if (!valid) {
				// this will throw a PintException with the error messages
				validateImportConfigurationFile(projectCfgFileByImportProcessID);
			}
			// save project into the database
			ImportCfgFileReader importReader = new ImportCfgFileReader();
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
		} catch (Exception e) {
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

		List<FileNameWithTypeBean> dataFileNames = new ArrayList<FileNameWithTypeBean>();
		dataFileNames.add(file);
		checkDataFileAvailabilities(jobId, dataFileNames);

		final File excelFile = FileManager.getDataFile(jobId, file.getFileName(), file.getId(), file.getFileFormat());
		ExcelFileImpl reader;
		try {
			reader = new ExcelFileImpl(excelFile);
			FileTypeBean ret = new FileTypeBean();
			ret.setId(file.getId());
			final List<ExcelSheet> excelSheets = reader.getSheets();
			if (!excelSheets.isEmpty()) {
				ret.setSheets(new SheetsTypeBean());
			}
			for (ExcelSheet excelSheet : excelSheets) {
				SheetTypeBean sheetBean = new SheetTypeBean();
				sheetBean.setId(file.getId() + SharedConstants.EXCEL_ID_SEPARATOR + excelSheet.getName());
				final List<ExcelColumn> excelColumns = excelSheet.getColumns();
				if (excelColumns != null) {
					for (ExcelColumn excelColumn : excelColumns) {
						ColumnTypeBean columnBean = new ColumnTypeBean();
						columnBean.setHeader(excelColumn.getHeader());
						columnBean.setId(sheetBean.getId() + SharedConstants.EXCEL_ID_SEPARATOR + excelColumn.getKey());
						sheetBean.getColumn().add(columnBean);
					}
				}
				ret.getSheets().getSheet().add(sheetBean);
			}
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.FILE_READING_ERROR);
		}
	}

	@Override
	public List<String> getScoreTypes(String sessionID) {
		if (scoreTypes == null) {
			scoreTypes = new ArrayList<String>();
			Set<String> set = new HashSet<String>();
			// TODO take the possible values from here?
			final List<ControlVocabularyTerm> possibleValues = Score.getInstance(cvManager).getPossibleValues();
			for (ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
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
			Set<String> set = new HashSet<String>();
			// TODO take the possible values from here?
			final List<ControlVocabularyTerm> possibleValues = PeptideModificationName.getInstance(cvManager)
					.getPossibleValues();
			for (ControlVocabularyTerm controlVocabularyTerm : possibleValues) {
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
		final FileWithFormat fileByImportProcessID = FileManager.getFileByImportProcessID(jobID, fileNameWithType);
		if (fileByImportProcessID != null) {
			FileManager.removeFromIndexFileByImportProcessID(jobID, fileByImportProcessID);
			final File oldDataFile = fileByImportProcessID.getFile();
			oldDataFile.delete();

			// remove oldDataFile parent folder
			File parentFile = oldDataFile.getParentFile();

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

		try {
			if (newDataFile.exists()) {
				newDataFile.delete();
			}
			// move the file
			FileUtils.moveFile(oldDataFile, newDataFile);
			// remove oldDataFile parent folder if empty recursively
			File parentFile = oldDataFile.getParentFile();

			edu.scripps.yates.utilities.files.FileUtils.removeFolderIfEmtpy(parentFile, true);

			// remove old index
			if (fileOLD.getFileFormat() != null) { // only if has a format has
				// been indexed before
				FormatType format = FormatType.fromValue(fileOLD.getFileFormat().name().toLowerCase());
				final FileWithFormat oldFileWithFormat = new FileWithFormat(fileOLD.getId(), oldDataFile, format,
						fileOLD.getFastaDigestionBean());
				FileManager.removeFromIndexFileByImportProcessID(jobID, oldFileWithFormat);
				// index the new file
				final FileWithFormat newFileWithFormat = new FileWithFormat(fileNew.getId(), newDataFile, format,
						fileNew.getFastaDigestionBean());
				FileManager.indexFileByImportProcessID(jobID, newFileWithFormat);
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw new PintException(e, PINT_ERROR_TYPE.MOVING_FILE_ERROR);
		}

	}

	@Override
	public void checkUserLogin(String userName, String encryptedPassword) throws PintException {
		final String adminPassword = ConfigurationPropertiesIO.readProperties().getAdminPassword();
		if (adminPassword.equals(encryptedPassword)) {
			return;
		} else {
			throw new PintException("Password is not correct", PINT_ERROR_TYPE.LOGIN_FAILED);
		}

	}
}
