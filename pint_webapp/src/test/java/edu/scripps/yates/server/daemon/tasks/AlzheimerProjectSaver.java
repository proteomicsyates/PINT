package edu.scripps.yates.server.daemon.tasks;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.io.FilenameUtils;

import edu.scripps.yates.excel.proteindb.importcfg.adapter.ImportCfgFileReader;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.server.util.FileManager;

public class AlzheimerProjectSaver extends PintServerDaemonTask {

	public AlzheimerProjectSaver(ServletContext servletContext) {
		super(servletContext);
		ImportCfgFileReader.ignoreDTASelectParameterT = true;
	}

	@Override
	public void run() {
		try {
			log.info("Starting " + getTaskName());
			int numConditionsSaved = 0;
			File folder = new File("/home/salvador/PInt/xml/old");
			for (File xmlFile : folder.listFiles()) {
				if (xmlFile.isFile()
						&& FilenameUtils.getBaseName(xmlFile.getAbsolutePath()).startsWith("alzheimerProject_NEW_")) {
					log.info("Saving file " + FilenameUtils.getBaseName(xmlFile.getAbsolutePath()));
					try {
						ContextualSessionHandler.beginGoodTransaction();
						ImportCfgFileReader importReader = new ImportCfgFileReader();
						final edu.scripps.yates.utilities.proteomicsmodel.Project projectFromCfgFile = importReader
								.getProjectFromCfgFile(xmlFile, FileManager.getFastaIndexFolder());
						log.info(projectFromCfgFile.getName() + " file readed");
						final MySQLSaver mySQLSaver = new MySQLSaver();
						mySQLSaver.saveProject(projectFromCfgFile);
						log.info("Finishing transaction");
						ContextualSessionHandler.finishGoodTransaction();
						ContextualSessionHandler.closeSession();
						log.info("transaction finished");
						log.info(++numConditionsSaved + " conditions saved so far");
					} catch (Exception e) {
						e.printStackTrace();
						ContextualSessionHandler.getSession().getTransaction().rollback();
					}
				}
			}
		} finally {
			ContextualSessionHandler.getSession().close();
		}
	}

	@Override
	public boolean justRunOnce() {
		return true;
	}

}
