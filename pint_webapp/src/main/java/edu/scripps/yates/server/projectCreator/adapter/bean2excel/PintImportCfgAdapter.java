package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public class PintImportCfgAdapter implements Adapter<PintImportCfg> {
	private final PintImportCfgBean pintImportCfgBean;

	public PintImportCfgAdapter(PintImportCfgBean pintImportCfgBean) {
		this.pintImportCfgBean = pintImportCfgBean;
	}

	@Override
	public PintImportCfg adapt() {
		PintImportCfg ret = new PintImportCfg();

		if (pintImportCfgBean.getFileSet() != null)
			ret.setFileSet(new FileSetTypeAdapter(pintImportCfgBean
					.getFileSet()).adapt());
		if (pintImportCfgBean.getProject() != null) {
			ret.setProject(new ProjectTypeAdapter(pintImportCfgBean
					.getProject()).adapt());
		}
		if (pintImportCfgBean.getServers() != null
				&& !pintImportCfgBean.getServers().getServer().isEmpty()) {
			ret.setServers(new ServersTypeAdapter(pintImportCfgBean
					.getServers()).adapt());
		}
		ret.setVersion(pintImportCfgBean.getVersion());
		ret.setImportID(pintImportCfgBean.getImportID());
		return ret;
	}
}
