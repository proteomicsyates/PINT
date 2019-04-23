package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PrincipalInvestigatorType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.PrincipalInvestigatorBean;

public class PrincipalInvestigatorBeanAdapter implements Adapter<PrincipalInvestigatorBean> {

	private final PrincipalInvestigatorType principalInvestigator;

	public PrincipalInvestigatorBeanAdapter(PrincipalInvestigatorType principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	@Override
	public PrincipalInvestigatorBean adapt() {
		final PrincipalInvestigatorBean ret = new PrincipalInvestigatorBean();
		ret.setCountry(principalInvestigator.getCountry());
		ret.setEmail(principalInvestigator.getEmail());
		ret.setInstitution(principalInvestigator.getInstitution());
		ret.setName(principalInvestigator.getName());
		return ret;
	}

}
