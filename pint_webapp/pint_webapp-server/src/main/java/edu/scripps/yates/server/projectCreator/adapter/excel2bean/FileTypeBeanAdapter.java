package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.SheetsTypeBean;

public class FileTypeBeanAdapter implements Adapter<FileTypeBean> {
	private final FileType fileType;

	public FileTypeBeanAdapter(FileType fileType) {
		this.fileType = fileType;
	}

	@Override
	public FileTypeBean adapt() {

		return adaptFromFileTypeBean();

	}

	private FileTypeBean adaptFromFileTypeBean() {
		final FileTypeBean ret = new FileTypeBean();
		if (fileType.getFormat() != null)
			ret.setFormat(new FormatTypeBeanAdapter(fileType.getFormat()).adapt());
		ret.setId(fileType.getId());
		ret.setName(fileType.getName());
		ret.setUrl(fileType.getUrl());

		if (fileType.getSheets() != null) {
			final SheetsTypeBean sheets = new SheetsTypeBeanAdapter(fileType.getSheets()).adapt();
			ret.setSheets(sheets);
		}
		ret.setRelativePath(fileType.getRelativePath());
		ret.setServerRef(fileType.getServerRef());
		if (fileType.getFastaDigestion() != null) {
			ret.setFastaDigestion(new FastaDigestionBeanAdapter(fileType.getFastaDigestion()).adapt());
		}
		return ret;
	}
}
