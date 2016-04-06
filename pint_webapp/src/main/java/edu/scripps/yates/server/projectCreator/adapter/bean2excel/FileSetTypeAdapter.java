package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.FileSetTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.FileTypeBean;

public class FileSetTypeAdapter implements Adapter<FileSetType> {
	private final FileSetTypeBean fileSet;

	public FileSetTypeAdapter(FileSetTypeBean fileSet) {
		this.fileSet = fileSet;
	}

	@Override
	public FileSetType adapt() {
		FileSetType ret = new FileSetType();
		if (fileSet.getFile() != null) {
			for (FileTypeBean fileTypeBean : fileSet.getFile()) {
				ret.getFile().add(new FileTypeAdapter(fileTypeBean).adapt());
			}
		}
		return ret;
	}

}
