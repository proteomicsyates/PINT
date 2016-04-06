package edu.scripps.yates.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import edu.scripps.yates.server.util.ServletCommonInit;

public class FileUploadServlet extends HttpServlet {
	private static final int maxFileSize = 10 * (1024 * 1024); // 10 megs max

	/**
	 * Overrided in order to run the method implemented on ServletCommonInit
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		ServletCommonInit.init(getServletContext());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletFileUpload upload = new ServletFileUpload();

		try {
			FileItemIterator iter = upload.getItemIterator(request);

			while (iter.hasNext()) {
				FileItemStream item = iter.next();

				String name = item.getFieldName();
				InputStream stream = item.openStream();

				// Process the input stream
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				int len;
				byte[] buffer = new byte[8192];
				while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, len);
				}

				if (out.size() > maxFileSize) {
					throw new RuntimeException("File is > than " + maxFileSize);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
