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
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.scripps.yates.client.ProjectSaverService;
import edu.scripps.yates.client.exceptions.PintException;
import edu.scripps.yates.client.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.server.daemon.PintServerDaemon;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServerConstants;
import edu.scripps.yates.server.util.ServletCommonInit;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.xml.XMLSchemaValidatorErrorHandler;
import edu.scripps.yates.utilities.xml.XmlSchemaValidator;
import psidev.psi.tools.validator.ValidatorMessage;

public class ProjectSaverServiceImpl extends RemoteServiceServlet implements ProjectSaverService {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ProjectSaverServiceImpl.class);

	/**
	 * Overrided in order to run the method implemented on ServletCommonInit
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		ServletCommonInit.init(getServletContext());
	}

	@Override
	public void saveProject(String sessionID, List<String> projectXmlFileNames) throws PintException {

		ImportCfgFileReader.ignoreDTASelectParameterT = true;

		log.info("Trying to save " + projectXmlFileNames.size() + " projects");

		try {

			for (String projectXmlFileName : projectXmlFileNames) {
				log.info("Saving " + projectXmlFileName + " project");
				File xmlFile = FileManager.getProjectXmlFile(projectXmlFileName);

				// it is important to have a new ImportCfgFileReader per
				// project. Otherwise, the information in local variables can be
				// mixed between projects
				ImportCfgFileReader importReader = new ImportCfgFileReader();
				final Project projectFromCfgFile = importReader.getProjectFromCfgFile(xmlFile,
						FileManager.getFastaIndexFolder());
				log.info(projectFromCfgFile.getName() + " file readed");
				final MySQLSaver mySQLSaver = new MySQLSaver();
				mySQLSaver.saveProject(projectFromCfgFile);
				ContextualSessionHandler.finishGoodTransaction();
				log.info("Project saved in db");
			}
			log.info("Everything is OK!");
		} catch (Exception e) {
			log.error(e);
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	@Override
	public List<String> validateProjectXMLCfgFile(String sessionID, String projectFileName) {
		File xmlFile = FileManager.getProjectXmlFile(projectFileName);
		try {
			Reader reader = new FileReader(xmlFile);
			URI schemaURI = Thread.currentThread().getContextClassLoader().getResource(ServerConstants.PROJECT_XML_CFG)
					.toURI();
			XMLSchemaValidatorErrorHandler erroresMessages = XmlSchemaValidator.validate(reader,
					XmlSchemaValidator.getSchema(schemaURI));
			final List<ValidatorMessage> errorsAsValidatorMessages = erroresMessages.getErrorsAsValidatorMessages();
			List<String> ret = new ArrayList<String>();
			for (ValidatorMessage validatorMessage : errorsAsValidatorMessages) {
				ret.add(validatorMessage.getMessage());
			}
			return ret;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String deleteProject(String projectTag) throws PintException {

		// instead of actually delete the project, it will be set for deletion
		// by the DeleteHiddenProjects daemon task
		//
		// final MySQLDeleter mySQLDeleter = new MySQLDeleter();
		// mySQLDeleter.deleteProject(projectTag);
		// log.info("Project " + projectTag + " is deleted");
		// ContextualSessionHandler.finishGoodTransaction();
		// log.info("Everything is OK!");
		List<edu.scripps.yates.proteindb.persistence.mysql.Project> projectList = ContextualSessionHandler
				.retrieveList(edu.scripps.yates.proteindb.persistence.mysql.Project.class);
		for (edu.scripps.yates.proteindb.persistence.mysql.Project project : projectList) {
			if (project.getTag().equals(projectTag)) {
				try {
					project.setHidden(true);
					String newProjectTag = projectTag + "_HIDN";
					if (newProjectTag.length() > 15) {
						newProjectTag = newProjectTag.substring(newProjectTag.length() - 15, newProjectTag.length());
					}
					project.setTag(newProjectTag);
					ContextualSessionHandler.save(project);
					ContextualSessionHandler.finishGoodTransaction();
					return "Project '" + projectTag
							+ "' has been marked for deletion. It will be deleted during the next "
							+ DatesUtil.getDescriptiveTimeFromMillisecs(PintServerDaemon.DELAY_IN_MINUTES * 60 * 1000)
							+ ".";
				} catch (Exception e) {
					ContextualSessionHandler.rollbackTransaction();
					log.error(e);
					throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
				}
			}
		}
		return "Project '" + projectTag + "' has not found in the DB.";

	}

	// @Override
	// public void saveSannyProject(String sessionID) {
	//
	// File excelFile = FileManager.getDataFile(1,
	// FileManager.getProjectFilesPath(getServletContext()),
	// "main_data_part1_2.xls", "1", FileFormat.EXCEL);
	// File masterIdFile = FileManager.getDataFile(1,
	// FileManager.getProjectFilesPath(getServletContext()),
	// "2291_IDs.xls", "1", FileFormat.EXCEL);
	// final DataProvider proteinProvider = new ProteinProviderFromExcel(true,
	// excelFile, masterIdFile);
	// Project project = ProteinSetAdapter.generalProject;
	//
	// SessionPerKeyHandler.getSessionPerKey(sessionID).beginGoodTransaction();
	// new MySQLSaver().saveProject(project);
	//
	// SessionPerKeyHandler.getSessionPerKey(sessionID)
	// .finishGoodTransaction();
	// System.out.println("Everything is OK!");
	//
	// }

}
