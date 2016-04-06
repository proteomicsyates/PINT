package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServersType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean;

public class ServersTypeBeanAdapter implements Adapter<ServersTypeBean> {
	private final ServersType serversTypeBean;

	public ServersTypeBeanAdapter(ServersType serversTypeBean) {
		this.serversTypeBean = serversTypeBean;
	}

	@Override
	public ServersTypeBean adapt() {

		return adaptFromServerTypeBean();

	}

	private ServersTypeBean adaptFromServerTypeBean() {
		ServersTypeBean ret = new ServersTypeBean();
		if (serversTypeBean.getServer() != null) {
			for (ServerType serverType : serversTypeBean.getServer()) {
				ret.getServer().add(new ServerTypeBeanAdapter(serverType).adapt());
			}
		}
		return ret;
	}

}
