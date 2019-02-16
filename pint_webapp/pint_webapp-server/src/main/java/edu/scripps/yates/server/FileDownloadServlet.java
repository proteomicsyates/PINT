package edu.scripps.yates.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServletCommonInit;
import edu.scripps.yates.shared.util.SharedConstants;

/**
 * Servlet implementation class FileDownloadServer
 */
public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// public static final String TEMP_FOLDER_LOCATION = System
	// .getProperty("java.io.tmpdir");
	/**
	 * Overrided in order to run the method implemented on ServletCommonInit
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		ServletCommonInit.init(config.getServletContext());
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FileDownloadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse p_response)
			throws ServletException, IOException {
		final String filename = request.getParameter(SharedConstants.FILE_TO_DOWNLOAD);
		final String fileType = request.getParameter(SharedConstants.FILE_TYPE);

		p_response.setContentType(MediaType.TEXT_PLAIN_VALUE);
		// get file depending on the FILE TYPE
		File file = null;
		if (fileType != null && fileType.equals(SharedConstants.ID_DATA_FILE_TYPE)) {
			file = FileManager.getDownloadFile(filename);
			p_response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		} else if (fileType != null && fileType.equals(SharedConstants.IMPORT_CFG_FILE_TYPE)) {
			file = FileManager.getProjectXmlFile(filename);
			p_response.setContentType(MediaType.TEXT_XML_VALUE);
			p_response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		} else if (fileType != null && fileType.equals(SharedConstants.TEMPLATE)) {
			file = FileManager.getProjectCfgFileTemplate(filename);
			p_response.setContentType(MediaType.TEXT_XML_VALUE);
			p_response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		} else if (fileType != null && fileType.equals(SharedConstants.PSEA_QUANT_DATA_FILE_TYPE)) {
			file = FileManager.getPSEAQuantFile(filename);
			p_response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		} else if (fileType != null && fileType.equals(SharedConstants.REACTOME_ANALYSIS_RESULT_FILE_TYPE)) {
			file = FileManager.getReactomeFile(filename);
			// dont add the header, just keep the plain text file
		}
		if (file == null)
			return;

		final long length = file.length();
		final FileInputStream fis = new FileInputStream(file);

		if (length > 0 && length <= Integer.MAX_VALUE)
			p_response.setContentLength((int) length);
		final ServletOutputStream out = p_response.getOutputStream();
		p_response.setBufferSize(32768);
		final int bufSize = p_response.getBufferSize();
		final byte[] buffer = new byte[bufSize];
		final BufferedInputStream bis = new BufferedInputStream(fis, bufSize);
		int bytes;
		while ((bytes = bis.read(buffer, 0, bufSize)) >= 0)
			out.write(buffer, 0, bytes);
		bis.close();
		fis.close();
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
