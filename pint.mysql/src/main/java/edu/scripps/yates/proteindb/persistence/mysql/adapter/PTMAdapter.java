package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.List;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;

public class PTMAdapter implements Adapter<Ptm>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5095797962976071733L;
	private final PTM ptm;
	private final Psm psm;

	public PTMAdapter(PTM ptm, Psm psm) {
		this.psm = psm;
		this.ptm = ptm;
	}

	@Override
	public Ptm adapt() {
		Ptm ret = null;

		ret = new Ptm(psm, ptm.getMassShift(), ptm.getName());

		ret.setCvId(ptm.getCVId());
		final List<PTMSite> ptmSites = ptm.getPTMSites();
		if (ptmSites != null) {
			for (PTMSite ptmSite : ptmSites) {
				ret.getPtmSites().add(new PTMSiteAdapter(ptmSite, ret).adapt());
			}

		}

		return ret;
	}

}
