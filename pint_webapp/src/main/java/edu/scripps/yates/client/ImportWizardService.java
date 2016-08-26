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
package edu.scripps.yates.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import edu.scripps.yates.client.exceptions.PintException;
import edu.scripps.yates.shared.model.DataSourceBean;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.projectCreator.ExcelDataReference;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;

@RemoteServiceRelativePath("importWizard")
public interface ImportWizardService extends RemoteService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ImportWizardService instance;

		public static ImportWizardService getInstance() {
			if (instance == null) {
				instance = GWT.create(ImportWizardService.class);
			}
			return instance;
		}
	}

	/**
	 *
	 * @param projectTag
	 * @param dataFileNames
	 * @return the import process identifier
	 * @throws PintException
	 *             when the projectName is already in the database
	 */
	public int startNewImportProcess(String sessionID, String projectTag, List<FileNameWithTypeBean> dataFileNames,
			List<RemoteFileWithTypeBean> remoteFiles) throws PintException;

	/**
	 * Gets a list of organisms from uniprot
	 *
	 * @return
	 */
	public List<String> getOrganismList(String sessionID) throws PintException;

	/**
	 * Gets a NCBI tax ID if available looking by name
	 *
	 * @param selectedOrganism
	 * @param async
	 */
	public String getTaxId(String sessionID, String organismName) throws PintException;

	/**
	 * Gets a list of tissues
	 *
	 * @return
	 */
	public List<String> getTissueList(String sessionID) throws PintException;

	/**
	 * Checks if the remote file can be accessed or not
	 *
	 * @param remoteFile
	 * @throws PintException
	 */
	public void checkRemoteFileInfoAccessibility(String sessionID, RemoteFileWithTypeBean remoteFile)
			throws PintException;

	/**
	 * Checks if the file has been uploaded before to the server or not,
	 * associated with the provided jobID
	 *
	 * @param jobID
	 * @param fileNameWithTypeBean
	 * @throws PintException
	 */
	public void checkDataFileAvailability(String sessionID, int jobID, FileNameWithTypeBean fileNameWithTypeBean)
			throws PintException;

	/**
	 * Gets the {@link FileTypeBean} object from a given
	 * {@link FileNameWithTypeBean}
	 *
	 * @param jobId
	 * @param fileName
	 * @param sheetName
	 * @return
	 */
	public FileTypeBean getExcelFileBean(String sessionID, int jobId, FileNameWithTypeBean file) throws PintException;

	/**
	 * Gets a number of numValues randomly picked from the rows of an specified
	 * column, from an specified sheet of an specified Excel file.
	 *
	 * @param jobId
	 * @param excelDataReference
	 * @param numValues
	 *
	 * @return
	 */
	public List<String> getRandomValues(String sessionID, int jobId, ExcelDataReference excelDataReference,
			int numValues) throws PintException;

	/**
	 * Gets the list of accession types supported
	 *
	 * @return
	 */
	public List<String> getAvailableACCTypes(String sessionID) throws PintException;

	/**
	 * Add data files to the import process id stated in the parameter jobID
	 *
	 * @param jobID
	 * @param files
	 * @throws PintException
	 */
	public void addDataFiles(String sessionID, int jobID, List<FileNameWithTypeBean> files) throws PintException;

	/**
	 * Add a data file to the import process id stated in the parameter jobID
	 *
	 * @param jobID
	 * @param dataFile
	 * @throws PintException
	 */
	public void addDataFile(String sessionID, int jobID, FileNameWithTypeBean dataFile) throws PintException;

	/**
	 * Starts a new import data process using a an already created and uploaded
	 * config XML file
	 *
	 * @param projectName
	 * @param cfgFile
	 * @return
	 * @throws PintException
	 */
	public int startNewImportProcess(String sessionID, String uploadedProjectCfgFileName) throws PintException;

	/**
	 * Add remote files to the import process id stated in the parameter jobID
	 *
	 * @param jobID
	 * @param remoteFilesWithTypes
	 * @throws PintException
	 */
	public void addRemoteFiles(String sessionID, int jobID, List<RemoteFileWithTypeBean> remoteFilesWithTypes)
			throws PintException;

	/**
	 * Add a remote file to the import process id stated in the parameter jobID
	 *
	 * @param jobID
	 * @param remoteFilesWithType
	 * @throws PintException
	 */
	public void addRemoteFile(String sessionID, int jobID, RemoteFileWithTypeBean remoteFilesWithType)
			throws PintException;

	/**
	 * Gets the list of file formats available for data sources
	 *
	 * @return
	 * @throws PintException
	 */
	public List<FileFormat> getFileFormats(String sessionID) throws PintException;

	/**
	 * Gets the current {@link ProjectBean} associated to the job stated by the
	 * parameter jobID
	 *
	 * @param jobID
	 * @return
	 * @throws PintException
	 */
	public ProjectBean getProjectBean(String sessionID, int jobID) throws PintException;

	/**
	 * Gets the current {@link PintImportCfgBean} associated to the job stated
	 * by the parameter jobID
	 *
	 * @param jobID
	 * @return
	 * @throws PintException
	 */
	public PintImportCfgBean getPintImportCfgTypeBean(String sessionID, int jobID) throws PintException;

	/**
	 * Gets the current list of {@link DataSourceBean} associated with the job
	 * stated by the parameter jobID
	 *
	 * @param importJobID
	 * @return
	 * @throws PintException
	 */
	public List<DataSourceBean> getDataSourceBeans(String sessionID, int jobID) throws PintException;

	/**
	 * Check server login credentials. Throws a {@link PintException} if the
	 * credentials are wrong.
	 *
	 * @param server
	 * @throws PintException
	 */
	public void checkServerAccessibility(String sessionID, ServerTypeBean server) throws PintException;

	/**
	 * Gets a random number of protein accession sfrom the
	 * {@link FileNameWithTypeBean} associated to the provided import job id<br>
	 * If fastaFileBean is not null, it will be used as well.
	 *
	 * @param importJobID
	 * @param dataFileBean
	 *            * @param fastaFileBean
	 * @param numTestCases
	 * @param async
	 */
	public List<String> getRandomProteinAccessions(String sessionID, int importJobID, FileNameWithTypeBean dataFileBean,
			FileNameWithTypeBean fastaFileBean, int numTestCases) throws PintException;

	/**
	 * Save the import configuration in the server
	 *
	 * @param jobID
	 * @param pintImportCfgBean
	 * @return true if the saved project XML file is ok against its schema
	 * @throws PintException
	 */
	public boolean saveImportConfiguration(int jobID, PintImportCfgBean pintImportCfgBean) throws PintException;

	/**
	 * Submits the data to the server to create the project
	 *
	 * @param jobID
	 * @param pintImportCfgBean
	 * @return the encoded project tag
	 * @throws PintException
	 */
	public String submitProject(int jobID, PintImportCfgBean pintImportCfgBean) throws PintException;

	/**
	 * Get the available list of score types
	 *
	 * @return
	 */
	public List<String> getScoreTypes(String sessionID) throws PintException;

	/**
	 * Gets the available list of PTM names
	 *
	 * @return
	 */
	public List<String> getPTMNames(String sessionID) throws PintException;

	/**
	 * Remove from server the file associated with the import job ID provided
	 *
	 * @param jobID
	 * @param dataFile
	 */
	public void removeDataFile(String sessionID, int jobID, FileNameWithTypeBean dataFile) throws PintException;

	/**
	 * Move data file using a new ID for the new file
	 *
	 * @param jobID
	 * @param fileOLD
	 * @param fileNew
	 * @throws PintException
	 */
	public void moveDataFile(String sessionID, int jobID, FileNameWithTypeBean fileOLD, FileNameWithTypeBean fileNew)
			throws PintException;

	/**
	 * Login user
	 *
	 * @param userName
	 * @param encryptedPassword
	 * @throws PintException
	 */
	public void checkUserLogin(String userName, String encryptedPassword) throws PintException;

}
