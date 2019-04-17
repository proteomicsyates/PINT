package edu.scripps.yates.server.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ConditionIDToProteinAmountIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ConditionIDToRatioDescriptorIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.PeptideIDToMSRunIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.PeptideIDToPTMIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToConditionIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToMSRunIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPSMIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPeptideIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToProteinScoreIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToProteinThresholdIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.RatioDescriptorIDToPSMRatioValueIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.RatioDescriptorIDToPeptideRatioValueIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.RatioDescriptorIDToProteinRatioValueIDTableMapper;
import edu.scripps.yates.server.configuration.PintConfigurationPropertiesIO;
import edu.scripps.yates.shared.configuration.PintConfigurationProperties;
import edu.scripps.yates.shared.util.SharedConstants;

/**
 * This class contains a common method to run from all servlets in the init()
 * method.
 *
 * @author Salva
 *
 */
public class ServletCommonInit {
	private final static Logger log = Logger.getLogger(ServletCommonInit.class);

	public static void init(ServletContext context) {
		FileManager.getProjectFilesPath(context);
		boolean sessionOpen = false;
		try {
			// init the DB connection
			final File pintPropertiesFile = FileManager.getPINTPropertiesFile(context);
			final PintConfigurationProperties properties = PintConfigurationPropertiesIO
					.readProperties(pintPropertiesFile);

			ContextualSessionHandler.getSessionFactory(properties.getDb_username(), properties.getDb_password(),
					properties.getDb_url());

			ContextualSessionHandler.beginGoodTransaction();
			sessionOpen = true;
			ContextualSessionHandler.finishGoodTransaction();
		} catch (final Exception e) {
			e.printStackTrace();
			log.error(e);
			log.error("Some error happened trying to initiate the database connection: " + e.getMessage());
		} finally {
			if (sessionOpen) {
				ContextualSessionHandler.closeSession();
			}
		}
		// configure the Uniprot annotations retrieval for using the local
		// folder and whether to index or not the annotations file, all
		// configured in the servlet context (web.xml file)

		// use the index
		final Boolean useIndex = true;
		// uniprot releases folder
		final File uniprotReleasesFolder = FileManager.getUniprotReleasesFolder();
		// configure the UniprotProteinRetrievalSettings
		UniprotProteinRetrievalSettings.getInstance(uniprotReleasesFolder, useIndex);

		if (SharedConstants.loadTableIDMappingsOnCommonServlet) {
			loadTableIDMappings();
		}

	}

	private static void loadTableIDMappings() {
		final List<Thread> threads = new ArrayList<Thread>();
		final Runnable runnable = () -> {
			// load protein-peptide mapping
			ProteinIDToPeptideIDTableMapper.getInstance();
		};
		final Thread t = new Thread(runnable);
		threads.add(t);

		final Runnable runnable2 = () -> {
			// load protein-psm mapping
			ProteinIDToPSMIDTableMapper.getInstance();
		};
		final Thread t2 = new Thread(runnable2);
		threads.add(t2);

		final Runnable runnable3 = () -> {
			// load protein-msrun mapping
			ProteinIDToMSRunIDTableMapper.getInstance();
		};
		final Thread t3 = new Thread(runnable3);
		threads.add(t3);

		final Runnable runnable4 = () -> {
			// load peptide-msrun mapping
			PeptideIDToMSRunIDTableMapper.getInstance();
		};
		final Thread t4 = new Thread(runnable4);
		threads.add(t4);

		final Runnable runnable5 = () -> {
			// load condition-ratiodescriptor mapping
			ConditionIDToRatioDescriptorIDTableMapper.getInstance();
		};
		final Thread t5 = new Thread(runnable5);
		threads.add(t5);
		final Runnable runnable6 = () -> {
			// load ratio descriptor-protein ratio values mapping
			RatioDescriptorIDToProteinRatioValueIDTableMapper.getInstance();
		};
		final Thread t6 = new Thread(runnable6);
		threads.add(t6);
		final Runnable runnable7 = () -> {
			// load ratio descriptor-psm ratio values mapping
			RatioDescriptorIDToPSMRatioValueIDTableMapper.getInstance();
		};
		final Thread t7 = new Thread(runnable7);
		threads.add(t7);
		final Runnable runnable8 = () -> {
			// load ratio descriptor-Peptide ratio values mapping
			RatioDescriptorIDToPeptideRatioValueIDTableMapper.getInstance();
		};
		final Thread t8 = new Thread(runnable8);
		threads.add(t8);
		final Runnable runnable9 = () -> {
			// load condition-protein amount mapping
			ConditionIDToProteinAmountIDTableMapper.getInstance();
		};
		final Thread t9 = new Thread(runnable9);
		threads.add(t9);
		final Runnable runnable10 = () -> {
			// load protein-protein threshold mapping
			ProteinIDToProteinThresholdIDTableMapper.getInstance();
		};
		final Thread t10 = new Thread(runnable10);
		threads.add(t10);
		final Runnable runnable11 = () -> {
			// load protein-protein score mapping
			ProteinIDToProteinScoreIDTableMapper.getInstance();
		};
		final Thread t11 = new Thread(runnable11);
		threads.add(t11);
		final Runnable runnable12 = () -> {
			// load peptide-ptm mapping
			PeptideIDToPTMIDTableMapper.getInstance();
		};
		final Thread t12 = new Thread(runnable12);
		threads.add(t12);
		final Runnable runnable13 = () -> {
			// load protein-condition mapping
			ProteinIDToConditionIDTableMapper.getInstance();
		};
		final Thread t13 = new Thread(runnable13);
		threads.add(t13);
		for (final Thread thread : threads) {
			thread.start();
		}

		log.info("All ID map tables are loading in " + threads.size() + " threads");
		final Runnable runnableWaiter = () -> {
			for (final Thread thread : threads) {
				try {
					thread.join();
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			}
			log.info("All ID map tables are loaded");
		};
		final Thread threadWaiter = new Thread(runnableWaiter);
		threadWaiter.start();
	}

}
