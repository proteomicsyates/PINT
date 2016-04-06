package edu.scripps.yates.server.projectCreator.adapter;

import java.util.List;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.server.projectCreator.adapter.bean2excel.FileTypeAdapter;
import edu.scripps.yates.server.util.FileWithFormat;
import edu.scripps.yates.server.util.RemoteFileWithType;

public class FileSetAdapter implements Adapter<FileSetType> {
	private final List<FileWithFormat> files;
	private final List<RemoteFileWithType> remoteFiles;

	public FileSetAdapter(List<FileWithFormat> files,
			List<RemoteFileWithType> remoteFiles) {
		this.files = files;
		this.remoteFiles = remoteFiles;
	}

	@Override
	public FileSetType adapt() {
		ObjectFactory factory = new ObjectFactory();
		final FileSetType ret = factory.createFileSetType();
		if (files != null) {
			for (FileWithFormat fileWithType : files) {
				FileType fileType = new FileTypeAdapter(fileWithType).adapt();
				ret.getFile().add(fileType);
			}
		}
		if (remoteFiles != null) {
			for (RemoteFileWithType remoteFile : remoteFiles) {
				FileType fileType = new FileTypeAdapterByRemoteFile(remoteFile)
						.adapt();
				ret.getFile().add(fileType);
			}
		}
		return ret;
	}

}
