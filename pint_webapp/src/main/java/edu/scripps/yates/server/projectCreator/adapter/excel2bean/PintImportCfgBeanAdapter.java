package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PintImportCfg;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public class PintImportCfgBeanAdapter implements Adapter<PintImportCfgBean> {
	private final PintImportCfg pintImportCfg;

	public PintImportCfgBeanAdapter(PintImportCfg pintImportCfg) {
		this.pintImportCfg = pintImportCfg;
	}

	@Override
	public PintImportCfgBean adapt() {
		PintImportCfgBean ret = new PintImportCfgBean();

		if (pintImportCfg.getFileSet() != null)
			ret.setFileSet(new FileSetTypeBeanAdapter(pintImportCfg.getFileSet()).adapt());
		if (pintImportCfg.getProject() != null) {
			ret.setProject(new ProjectTypeBeanAdapter(pintImportCfg.getProject()).adapt());
		}
		if (pintImportCfg.getServers() != null) {
			ret.setServers(new ServersTypeBeanAdapter(pintImportCfg.getServers()).adapt());
		}
		ret.setVersion(pintImportCfg.getVersion());
		if (pintImportCfg.getImportID() != null) {
			ret.setImportID(pintImportCfg.getImportID());
		}
		return ret;
	}
}
