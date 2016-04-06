package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;

public class PTMImpl implements PTM {
	private final Ptm hibPTM;
	private List<PTMSite> ptmSites;

	public PTMImpl(Ptm hibPTM) {
		this.hibPTM = hibPTM;
	}

	@Override
	public Double getMassShift() {
		return hibPTM.getMassShift();
	}

	@Override
	public String getName() {
		return hibPTM.getName();
	}

	@Override
	public String getCVId() {
		return hibPTM.getCvId();
	}

	@Override
	public List<PTMSite> getPTMSites() {
		if (ptmSites == null) {
			ptmSites = new ArrayList<PTMSite>();
			final Set<PtmSite> hibPTMSites = hibPTM.getPtmSites();
			for (PtmSite hibPtmSite : hibPTMSites) {
				if (PTMSiteImpl.ptmSiteMap.containsKey(hibPtmSite.getId())) {
					ptmSites.add(PTMSiteImpl.ptmSiteMap.get(hibPtmSite.getId()));
				} else {
					final PTMSiteImpl ptmSiteImpl = new PTMSiteImpl(hibPtmSite);
					ptmSites.add(ptmSiteImpl);
				}
			}
		}
		return ptmSites;
	}
}
