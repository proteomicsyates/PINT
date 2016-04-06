package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import java.util.List;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class RemoteInfoTypeBeanAdapter implements Adapter<RemoteInfoTypeBean> {
	private final RemoteInfoType identificationRemoteFile;

	public RemoteInfoTypeBeanAdapter(RemoteInfoType identificationRemoteFile) {
		this.identificationRemoteFile = identificationRemoteFile;
	}

	@Override
	public RemoteInfoTypeBean adapt() {
		RemoteInfoTypeBean ret = new RemoteInfoTypeBean();

		ret.setDiscardDecoys(identificationRemoteFile.getDiscardDecoys());
		ret.setMsRunRef(identificationRemoteFile.getMsRunRef());
		final List<FileReferenceType> localFiles = identificationRemoteFile
				.getFileRef();
		if (localFiles != null) {
			for (FileReferenceType fileNameWithType : localFiles) {
				ret.getFileRefs().add(fileNameWithType.getFileRef());
			}
		}

		return ret;
	}

}
