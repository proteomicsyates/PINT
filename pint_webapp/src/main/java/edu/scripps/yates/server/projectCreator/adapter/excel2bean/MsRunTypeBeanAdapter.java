package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.utilities.dates.DatesUtil;

public class MsRunTypeBeanAdapter implements Adapter<MsRunTypeBean> {

	private final MsRunType msRunType;

	public MsRunTypeBeanAdapter(MsRunType msRunType) {
		this.msRunType = msRunType;
	}

	@Override
	public MsRunTypeBean adapt() {

		return adaptFromMsRunType();

	}

	private MsRunTypeBean adaptFromMsRunType() {
		MsRunTypeBean ret = new MsRunTypeBean();
		if (msRunType.getDate() != null) {

			ret.setDate(DatesUtil.toDate(msRunType.getDate()));

		}
		ret.setEnzymeNocutResidues(msRunType.getEnzymeNocutResidues());
		ret.setEnzymeResidues(msRunType.getEnzymeResidues());
		ret.setFastaFileRef(msRunType.getFastaFileRef());
		ret.setId(msRunType.getId());
		ret.setPath(msRunType.getPath());

		return ret;
	}

}
