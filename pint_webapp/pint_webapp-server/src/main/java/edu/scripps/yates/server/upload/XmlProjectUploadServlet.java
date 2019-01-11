package edu.scripps.yates.server.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServerConstants;
import edu.scripps.yates.shared.util.CryptoUtil;
import edu.scripps.yates.utilities.xml.XMLSchemaValidatorErrorHandler;
import edu.scripps.yates.utilities.xml.XmlSchemaValidator;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;
import psidev.psi.tools.validator.ValidatorMessage;

public class XmlProjectUploadServlet extends UploadAction {
	private static final Logger log = Logger.getLogger(XmlProjectUploadServlet.class);
	private static final long serialVersionUID = 1L;
	private static JAXBContext jaxbContext;
	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();

	static {
		try {
			jaxbContext = JAXBContext.newInstance(PintImportCfg.class);
		} catch (JAXBException e) {
		}

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		FileManager.getProjectFilesPath(config.getServletContext());

	}

	/**
	 * Maintain a list with received files and their content types.
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		List<String> errors = new ArrayList<String>();
		log.info(sessionFiles.size() + " files received");
		if (sessionFiles.size() > 1)
			throw new UploadActionException("This uploader doens't support more than one file at the same time");
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					// needed before the following line
					File file = FileManager.getProjectXmlFile(item.getName());

					log.info("Saving XML project file to: " + file.getAbsolutePath());

					item.write(file);

					// unmarshall to encode the password
					try {
						log.info("Looking for provided passwords");
						final PintImportCfg importCfg = (PintImportCfg) jaxbContext.createUnmarshaller()
								.unmarshal(new FileInputStream(file));
						boolean passFound = false;
						if (importCfg != null && importCfg.getServers() != null
								&& importCfg.getServers().getServer() != null) {
							for (ServerType server : importCfg.getServers().getServer()) {
								final String password = server.getPassword();
								if (password != null && !"".equals(password)) {
									final String encryptPassword = CryptoUtil.encrypt(password);
									log.info(password + " set to " + encryptPassword);
									server.setPassword(encryptPassword);
									passFound = true;
								}
							}
						}
						if (passFound) {
							final Marshaller marshaller = jaxbContext.createMarshaller();
							// marshall to the file with the encoded passwords
							marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
							try {
								marshaller.setProperty("com.sun.xml.bind.indentString", "\t");
							} catch (PropertyException e) {
								marshaller.setProperty("com.sun.xml.internal.bind.indentString", "\t");
							}
							marshaller.marshal(importCfg, file);
							log.info("Updating file again");
						}
					} catch (JAXBException e) {
						e.printStackTrace();
					}

					// / Save a list with the received files
					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(), item.getContentType());
					URI schemaURI = Thread.currentThread().getContextClassLoader()
							.getResource(ServerConstants.PROJECT_XML_CFG).toURI();
					final XMLSchemaValidatorErrorHandler validate = XmlSchemaValidator.validate(new FileReader(file),
							XmlSchemaValidator.getSchema(schemaURI));
					if (!validate.getErrorsAsValidatorMessages().isEmpty()) {
						for (ValidatorMessage errorMessage : validate.getErrorsAsValidatorMessages()) {
							errors.add(errorMessage.getMessage());
						}
					}
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// look for the errors in the validation
		if (!errors.isEmpty())
			return getValidationErrorString(errors);

		// / Send a customized message to the client.
		response = sessionFiles.iterator().next().getName();
		// / Send your customized message to the client.
		return response;
	}

	private String getValidationErrorString(List<String> errors) {
		StringBuilder sb = new StringBuilder();
		for (String error : errors) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append(error);
		}
		return sb.toString();
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String fieldName = request.getParameter(UConsts.PARAM_SHOW);
		File f = receivedFiles.get(fieldName);
		if (f != null) {
			response.setContentType(receivedContentTypes.get(fieldName));
			FileInputStream is = new FileInputStream(f);
			copyFromInputStreamToOutputStream(is, response.getOutputStream());
		} else {
			renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
		}
	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName) throws UploadActionException {
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}
}
