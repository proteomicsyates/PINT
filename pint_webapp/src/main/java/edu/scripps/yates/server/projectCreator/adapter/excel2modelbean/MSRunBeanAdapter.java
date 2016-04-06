package edu.scripps.yates.server.projectCreator.adapter.excel2modelbean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.MSRunBean;

public class MSRunBeanAdapter implements Adapter<MSRunBean> {

	private final MsRunType msRunType;

	public MSRunBeanAdapter(MsRunType msRunType) {
		this.msRunType = msRunType;
	}

	@Override
	public MSRunBean adapt() {
		MSRunBean ret = new MSRunBean();
		ret.setRunID(msRunType.getId());
		ret.setPath(msRunType.getPath());
		ret.setFastaFileRef(msRunType.getFastaFileRef());
		ret.setEnzymeResidues(msRunType.getEnzymeResidues());
		ret.setEnzymeNoCutResidues(msRunType.getEnzymeNocutResidues());
		if (msRunType.getDate() != null)
			ret.setDate(msRunType.getDate().toGregorianCalendar().getTime());
		return ret;
	}

}
