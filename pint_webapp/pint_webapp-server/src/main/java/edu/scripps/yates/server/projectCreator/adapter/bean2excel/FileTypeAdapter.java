package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import java.io.File;
import java.net.URI;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SheetsType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.server.util.FileWithFormat;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class FileTypeAdapter implements Adapter<FileType> {
	private final FileWithFormat fileWithType;
	private final FileTypeBean fileTypeBean;

	public FileTypeAdapter(FileWithFormat fileWithType) {
		this.fileWithType = fileWithType;
		fileTypeBean = null;
	}

	public FileTypeAdapter(FileTypeBean fileTypeBean) {
		this.fileTypeBean = fileTypeBean;
		fileWithType = null;
	}

	@Override
	public FileType adapt() {
		if (fileWithType != null) {
			return adaptFromFileWithType();
		} else {
			return adaptFromFileTypeBean();
		}

	}

	private FileType adaptFromFileTypeBean() {
		ObjectFactory factory = new ObjectFactory();
		FileType fileType = factory.createFileType();
		if (fileTypeBean.getFormat() != null)
			fileType.setFormat(new FormatTypeAdapter(fileTypeBean.getFormat())
					.adapt());
		fileType.setId(fileTypeBean.getId());
		fileType.setName(fileTypeBean.getName());
		fileType.setUrl(fileTypeBean.getUrl());
		if (fileTypeBean.getSheets() != null) {
			final SheetsType sheets = new SheetsTypeAdapter(
					fileTypeBean.getSheets()).adapt();
			fileType.setSheets(sheets);
		}
		if (fileTypeBean.getFastaDigestion() != null)
			fileType.setFastaDigestion(new FastaDigestionTypeAdapter(
					fileTypeBean.getFastaDigestion()).adapt());

		fileType.setRelativePath(fileTypeBean.getRelativePath());
		fileType.setServerRef(fileTypeBean.getServerRef());
		return fileType;
	}

	private FileType adaptFromFileWithType() {
		ObjectFactory factory = new ObjectFactory();
		FileType fileType = factory.createFileType();
		fileType.setFormat(fileWithType.getFormat());
		fileType.setId(fileWithType.getId());
		fileType.setName(fileWithType.getFileName());
		final File file = fileWithType.getFile();
		final URI uri = file.toURI();
		// fileType.setUrl(UriUtils.encode(uri
		// .toString()));
		fileType.setUrl(uri.toString());
		if (fileWithType.getFormat() == FormatType.EXCEL) {
			final SheetsType sheets = new SheetsTypeAdapter(fileWithType)
					.adapt();
			fileType.setSheets(sheets);
		}
		if (fileWithType.getFastaDigestionBean() != null) {
			fileType.setFastaDigestion(new FastaDigestionTypeAdapter(
					fileWithType.getFastaDigestionBean()).adapt());
		}
		return fileType;
	}

}
