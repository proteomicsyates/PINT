package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.MsRunTypeBean;
import edu.scripps.yates.utilities.dates.DatesUtil;

public class MsRunTypeAdapter implements Adapter<MsRunType> {

	private final MsRunTypeBean msRunTypeBean;

	public MsRunTypeAdapter(MsRunTypeBean msRunTypeBean) {
		this.msRunTypeBean = msRunTypeBean;
	}

	@Override
	public MsRunType adapt() {

		return apdatFromMsRunTypeBean();

	}

	private MsRunType apdatFromMsRunTypeBean() {
		MsRunType ret = new MsRunType();
		if (msRunTypeBean.getDate() != null) {
			ret.setDate(DatesUtil.toXMLGregorianCalendar(msRunTypeBean
					.getDate()));
		}
		ret.setEnzymeNocutResidues(msRunTypeBean.getEnzymeNocutResidues());
		ret.setEnzymeResidues(msRunTypeBean.getEnzymeResidues());
		ret.setFastaFileRef(msRunTypeBean.getFastaFileRef());
		ret.setId(msRunTypeBean.getId());
		ret.setPath(msRunTypeBean.getPath());

		return ret;
	}

}
