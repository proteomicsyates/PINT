package edu.scripps.yates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import edu.scripps.yates.shared.model.FileSummary;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgTypeBean;

public interface ImportWizardServiceAsync {

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void startNewImportProcess(java.lang.String sessionID, java.lang.String projectTag,
			java.util.List<edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean> dataFileNames,
			java.util.List<edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean> remoteFiles,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getOrganismList(java.lang.String sessionID, AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getTaxId(java.lang.String sessionID, java.lang.String organismName, AsyncCallback<java.lang.String> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getTissueList(java.lang.String sessionID, AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void checkRemoteFileInfoAccessibility(java.lang.String sessionID,
			edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean remoteFile,
			AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void checkDataFileAvailability(java.lang.String sessionID, int jobID,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fileNameWithTypeBean,
			AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getExcelFileBean(java.lang.String sessionID, int jobId,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean file,
			AsyncCallback<edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getRandomValues(java.lang.String sessionID, int jobId,
			edu.scripps.yates.shared.model.projectCreator.ExcelDataReference excelDataReference, int numValues,
			AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getAvailableACCTypes(java.lang.String sessionID, AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void addDataFiles(java.lang.String sessionID, int jobID,
			java.util.List<edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean> files,
			AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void addDataFile(java.lang.String sessionID, int jobID,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean dataFile, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void startNewImportProcess(java.lang.String sessionID, java.lang.String uploadedProjectCfgFileName,
			AsyncCallback<java.lang.Integer> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void addRemoteFiles(java.lang.String sessionID, int jobID,
			java.util.List<edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean> remoteFilesWithTypes,
			AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void addRemoteFile(java.lang.String sessionID, int jobID,
			edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean remoteFilesWithType,
			AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getFileFormats(java.lang.String sessionID,
			AsyncCallback<java.util.List<edu.scripps.yates.shared.model.FileFormat>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getProjectBean(java.lang.String sessionID, int jobID,
			AsyncCallback<edu.scripps.yates.shared.model.ProjectBean> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getPintImportCfgTypeBean(java.lang.String sessionID, int jobID,
			AsyncCallback<edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getDataSourceBeans(java.lang.String sessionID, int jobID,
			AsyncCallback<java.util.List<edu.scripps.yates.shared.model.DataSourceBean>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void checkServerAccessibility(java.lang.String sessionID,
			edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean server, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getRandomProteinAccessions(java.lang.String sessionID, int importJobID,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean dataFileBean,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fastaFileBean, int numTestCases,
			AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void saveImportConfiguration(int jobID,
			edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean pintImportCfgBean,
			AsyncCallback<java.lang.Boolean> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void submitProject(int jobID,
			edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean pintImportCfgBean,
			AsyncCallback<java.lang.String> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getScoreTypes(java.lang.String sessionID, AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void getPTMNames(java.lang.String sessionID, AsyncCallback<java.util.List<java.lang.String>> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void removeDataFile(java.lang.String sessionID, int jobID,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean dataFile, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void moveDataFile(java.lang.String sessionID, int jobID,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fileOLD,
			edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean fileNew, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void checkUserLogin(java.lang.String userName, java.lang.String encryptedPassword, AsyncCallback<Void> callback);

	/**
	 * GWT-RPC service asynchronous (client-side) interface
	 * 
	 * @see edu.scripps.yates.ImportWizardService
	 */
	void generateNewImportProcessID(AsyncCallback<java.lang.Integer> callback);

	void updateFileFormat(String sessionID, int importID, FileTypeBean fileTypeBean,
			AsyncCallback<FileTypeBean> callback);

	void validatePintImportCfg(PintImportCfgTypeBean pintImportCfg, AsyncCallback<String> callback);

	/**
	 * Utility class to get the RPC Async interface from client-side code
	 */
	public static final class Util {
		private static ImportWizardServiceAsync instance;

		public static final ImportWizardServiceAsync getInstance() {
			if (instance == null) {
				instance = (ImportWizardServiceAsync) GWT.create(ImportWizardService.class);
			}
			return instance;
		}

		private Util() {
			// Utility class should not be instantiated
		}
	}

	void getFileSummary(int importID, String sessionID, FileTypeBean file, AsyncCallback<FileSummary> asyncCallback);

	void getUploadedFileID(int importID, String uploadedFileSignature, AsyncCallback<String> asyncCallback);

	void getPintImportCfgTypeBeanByProcessKey(int hashCode, AsyncCallback<PintImportCfgBean> asyncCallback);

	void getTemplateFiles(AsyncCallback<java.util.Map<String, String>> asyncCallback);
}
