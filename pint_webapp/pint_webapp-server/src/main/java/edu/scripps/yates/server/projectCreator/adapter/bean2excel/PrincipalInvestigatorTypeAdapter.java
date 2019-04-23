package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.PrincipalInvestigatorType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.PrincipalInvestigatorBean;

public class PrincipalInvestigatorTypeAdapter implements Adapter<PrincipalInvestigatorType> {
	private final PrincipalInvestigatorBean principalInvestigator;

	public PrincipalInvestigatorTypeAdapter(PrincipalInvestigatorBean principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	@Override
	public PrincipalInvestigatorType adapt() {
		final PrincipalInvestigatorType ret = new PrincipalInvestigatorType();
		ret.setCountry(principalInvestigator.getCountry());
		ret.setEmail(principalInvestigator.getEmail());
		ret.setInstitution(principalInvestigator.getInstitution());
		ret.setName(principalInvestigator.getName());
		return ret;
	}

}
