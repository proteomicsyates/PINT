package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ObjectFactory;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;

public class ServerTypeAdapter implements Adapter<ServerType> {
	private final ServerTypeBean serverTypeBean;

	public ServerTypeAdapter(ServerTypeBean serverTypeBean) {
		this.serverTypeBean = serverTypeBean;
	}

	@Override
	public ServerType adapt() {

		return adaptFromServerTypeBean();

	}

	private ServerType adaptFromServerTypeBean() {

		ObjectFactory factory = new ObjectFactory();
		ServerType ret = factory.createServerType();
		ret.setHostName(serverTypeBean.getHostName());
		ret.setPassword(serverTypeBean.getEncryptedPassword());
		ret.setUserName(serverTypeBean.getUserName());
		ret.setId(serverTypeBean.getId());
		return ret;
	}

}
