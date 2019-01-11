package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean;

public class FileSetTypeBeanAdapter implements Adapter<FileSetTypeBean> {
	private final FileSetType fileSet;

	public FileSetTypeBeanAdapter(FileSetType fileSet) {
		this.fileSet = fileSet;
	}

	@Override
	public FileSetTypeBean adapt() {
		FileSetTypeBean ret = new FileSetTypeBean();
		if (fileSet.getFile() != null) {
			for (FileType fileTypeBean : fileSet.getFile()) {
				ret.getFile().add(new FileTypeBeanAdapter(fileTypeBean).adapt());
			}
		}
		return ret;
	}

}
