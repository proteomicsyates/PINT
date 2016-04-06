package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;

public class ServerTypeBeanAdapter implements Adapter<ServerTypeBean> {
	private final ServerType serverType;

	public ServerTypeBeanAdapter(ServerType serverType) {
		this.serverType = serverType;
	}

	@Override
	public ServerTypeBean adapt() {

		return adaptFromServerTypeBean();

	}

	private ServerTypeBean adaptFromServerTypeBean() {

		ServerTypeBean ret = new ServerTypeBean();
		ret.setHostName(serverType.getHostName());
		ret.setEncryptedPassword(serverType.getPassword());
		ret.setUserName(serverType.getUserName());
		ret.setId(serverType.getId());
		return ret;
	}

}
