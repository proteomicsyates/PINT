package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.ProjectBean;

public class MSRunBeanAdapter implements Adapter<MSRunBean> {

	private final MsRunType msRunType;
	private final ProjectBean projectBean;

	public MSRunBeanAdapter(MsRunType msRunType, ProjectBean projectBean) {
		this.msRunType = msRunType;
		this.projectBean = projectBean;
	}

	@Override
	public MSRunBean adapt() {
		MSRunBean ret = new MSRunBean();
		ret.setRunID(msRunType.getId());
		ret.setPath(msRunType.getPath());
		ret.setFastaFileRef(msRunType.getFastaFileRef());
		ret.setEnzymeResidues(msRunType.getEnzymeResidues());
		ret.setEnzymeNoCutResidues(msRunType.getEnzymeNocutResidues());
		if (msRunType.getDate() != null) {
			ret.setDate(msRunType.getDate().toGregorianCalendar().getTime());
		}
		ret.setProject(projectBean);
		return ret;
	}

}
