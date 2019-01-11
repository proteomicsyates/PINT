package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import java.util.List;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.RemoteInfoTypeBean;

public class RemoteInfoTypeAdapter implements Adapter<RemoteInfoType> {
	private final RemoteInfoTypeBean remoteInfoTypeBean;

	public RemoteInfoTypeAdapter(RemoteInfoTypeBean remoteInfoTypeBean) {
		this.remoteInfoTypeBean = remoteInfoTypeBean;
	}

	@Override
	public RemoteInfoType adapt() {
		RemoteInfoType ret = new RemoteInfoType();

		ret.setDiscardDecoys(remoteInfoTypeBean.getDiscardDecoys());
		ret.setMsRunRef(remoteInfoTypeBean.getMsRunRef());
		final List<String> localFiles = remoteInfoTypeBean.getFileRefs();
		if (localFiles != null) {
			for (String localFileRef : localFiles) {
				final FileReferenceType fileRef = new FileReferenceType();
				fileRef.setFileRef(localFileRef);
				ret.getFileRef().add(fileRef);
			}
		}

		return ret;
	}

}
