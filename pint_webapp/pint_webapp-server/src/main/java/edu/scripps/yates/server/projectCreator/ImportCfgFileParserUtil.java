package edu.scripps.yates.server.projectCreator;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;

public class ImportCfgFileParserUtil {
	private static JAXBContext jaxbContext;
	private static Logger log = Logger.getLogger(ImportCfgFileParserUtil.class);

	static {
		try {
			jaxbContext = JAXBContext.newInstance("edu.scripps.yates.excel.proteindb.importcfg.jaxb");
		} catch (final JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Unmarshall a {@link PintImportCfg} object from a {@link File}
	 *
	 * @param fileCfg
	 * @return
	 * @throws PintException
	 */
	public static PintImportCfg getPintImportFromFile(File fileCfg) throws PintException {
		try {
			final Unmarshaller marshaller = jaxbContext.createUnmarshaller();
			final PintImportCfg unmarshal = (PintImportCfg) marshaller.unmarshal(fileCfg);
			return unmarshal;
		} catch (final JAXBException e) {
			e.printStackTrace();
			String message = "Format incorrect for dataset import configuration file.";
			if (e.getMessage() != null) {
				message += " " + e.getMessage();
			}
			throw new PintException(message, PINT_ERROR_TYPE.PARSING_ERROR);
		}
	}

	/**
	 * Unmarshall a {@link PintImportCfg} object from a jobID
	 *
	 * @param jobID
	 * @return
	 * @throws PintException if there is not {@link PintImportCfg} associated to
	 *                       that jodID
	 */
	public static PintImportCfg getPintImportCfgFromJobID(int jobID) throws PintException {
		log.info("getting import process id '" + jobID + "'");
		final File projectCfgFileByImportProcessID = FileManager.getProjectCfgFileByImportProcessID(jobID);
		if (projectCfgFileByImportProcessID != null) {
			final PintImportCfg pintImportFromFile = getPintImportFromFile(projectCfgFileByImportProcessID);
			if (pintImportFromFile != null) {
				log.info("Import process id '" + jobID + "' valid");
				return pintImportFromFile;
			}
		}
		final String message = "Import process ID: '" + jobID
				+ "' doesn't exist or there was a problem retrieving the import process";
		log.warn(message);
		throw new PintException(message, PINT_ERROR_TYPE.IMPORT_PROCESS_ID_NOT_FOUND);
	}

	/**
	 * Marshall the {@link PintImportCfg} object to a file ans save it in the
	 * appropiate location
	 *
	 * @param pintImportCfg
	 * @param projectFilesPath
	 * @return
	 */
	public static File saveFileCfg(PintImportCfg pintImportCfg) {
		try {
			final Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			final File xmlFile = FileManager.getProjectXmlFile(pintImportCfg.getProject().getTag() + ".xml");
			marshaller.marshal(pintImportCfg, xmlFile);
			return xmlFile;
		} catch (final JAXBException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}

	}
}
