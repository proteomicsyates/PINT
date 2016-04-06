package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServersType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.ServerTypeBean;
import edu.scripps.yates.shared.model.projectCreator.excel.ServersTypeBean;

public class ServersTypeAdapter implements Adapter<ServersType> {
	private final ServersTypeBean serversTypeBean;

	public ServersTypeAdapter(ServersTypeBean serversTypeBean) {
		this.serversTypeBean = serversTypeBean;
	}

	@Override
	public ServersType adapt() {

		return adaptFromServerTypeBean();

	}

	private ServersType adaptFromServerTypeBean() {
		ServersType ret = new ServersType();
		if (serversTypeBean.getServer() != null) {
			for (ServerTypeBean serverTypeBean : serversTypeBean.getServer()) {
				ret.getServer().add(
						new ServerTypeAdapter(serverTypeBean).adapt());
			}
		}
		return ret;
	}

}
