package edu.scripps.yates.server;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.FileWithFormat;
import edu.scripps.yates.shared.model.FileFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.utilities.files.FileUtils;

public class NewFileUploadServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7731689155440452972L;
	private final long FILE_SIZE_LIMIT = -1;// 20 * 1024 * 1024 * 1024; // 20 GB
	private final Logger logger = Logger.getLogger("UploadServlet");
	private static int fileIDs = 1;
	public static final FileFormat defaultFileFormat = FileFormat.MZIDENTML;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			int jobID = 0;
			if (req.getParameterMap().containsKey(SharedConstants.JOB_ID_PARAM)) {
				jobID = Integer.valueOf(req.getParameter(SharedConstants.JOB_ID_PARAM));
			}
			final DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
			final ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
			fileUpload.setSizeMax(FILE_SIZE_LIMIT);

			final List<FileItem> items = fileUpload.parseRequest(req);

			for (final FileItem item : items) {
				if (item.isFormField()) {
					logger.info("Received form field:");
					logger.info("Name: " + item.getFieldName());
					logger.info("Value: " + item.getString());
				} else {
					logger.info("Received file:");
					logger.info("Name: " + item.getName());
					logger.info("Size: " + FileUtils.getDescriptiveSizeFromBytes(item.getSize()));
				}

				if (!item.isFormField()) {
					if (FILE_SIZE_LIMIT != -1 && item.getSize() > FILE_SIZE_LIMIT) {
						resp.sendError(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE, "File size exceeds limit");

						return;
					}

					// Typically here you would process the file in some way:
					// InputStream in = item.getInputStream();
					// ...
					final String fileID = "file_" + fileIDs++;
					final FileFormat format = defaultFileFormat;
					final File file = FileManager.getDataFile(jobID, item.getName(), fileID, format);
					final FastaDigestionBean fastaDigestionBean = null;
					final FileWithFormat fileWithFormat = new FileWithFormat(fileID, file,
							FormatType.fromValue(format.name().toLowerCase()), fastaDigestionBean);
					FileManager.indexFileByImportProcessID(jobID, fileWithFormat);
					logger.info("Saving data file to: " + file.getAbsolutePath());
					item.write(file);
					if (!item.isInMemory()) {
						item.delete();
					}
				}
			}
		} catch (final Exception e) {
			logger.error("Throwing servlet exception for unhandled exception", e);
			throw new ServletException(e);
		}
	}

}