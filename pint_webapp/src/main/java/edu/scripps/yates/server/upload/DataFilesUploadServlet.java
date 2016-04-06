package edu.scripps.yates.server.upload;

import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.util.SharedConstants;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

public class DataFilesUploadServlet extends UploadAction {
	private static final Logger log = Logger
			.getLogger(DataFilesUploadServlet.class);
	private static final long serialVersionUID = 1L;

	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	/**
	 * Maintain a list with received files and their content types.
	 */
	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	/**
	 * Override executeAction to save the received files in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		log.info(sessionFiles.size() + " files received");
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					for (String paramName : request.getParameterMap().keySet()) {
						log.info(paramName + "="
								+ request.getParameter(paramName));
					}
					int jobID = 0;
					if (request.getParameterMap().containsKey(
							SharedConstants.JOB_ID_PARAM)) {
						jobID = Integer.valueOf(request
								.getParameter(SharedConstants.JOB_ID_PARAM));
					}
					String fileID = "0";
					if (request.getParameterMap().containsKey(
							SharedConstants.FILE_ID_PARAM)) {
						fileID = request
								.getParameter(SharedConstants.FILE_ID_PARAM);
					}
					FileFormat format = null;
					if (request.getParameterMap().containsKey(
							SharedConstants.FILE_FORMAT)) {
						String formatString = request
								.getParameter(SharedConstants.FILE_FORMAT);
						format = FileFormat
								.getFileFormatFromString(formatString);
					}
					String projectFilesPath = FileManager
							.getProjectFilesPath(getServletContext());
					File file = FileManager.getDataFile(jobID,
							projectFilesPath, item.getName(), fileID, format);
					log.info("Saving data file to: " + file.getAbsolutePath());
					item.write(file);

					// / Save a list with the received files
					receivedFiles.put(item.getFieldName(), file);
					receivedContentTypes.put(item.getFieldName(),
							item.getContentType());
					log.info("File saved at: " + file.getAbsolutePath());
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send a customized message to the client.
		String plural = receivedFiles.size() > 1 ? "s" : "";
		response = receivedFiles.size() + " file" + plural + " received";
		// / Send your customized message to the client.
		return response;
	}

	/**
	 * Get the content of an uploaded file.
	 */
	@Override
	public void getUploadedFile(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
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
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
		File file = receivedFiles.get(fieldName);
		receivedFiles.remove(fieldName);
		receivedContentTypes.remove(fieldName);
		if (file != null) {
			file.delete();
		}
	}
}
