package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final static Map<Integer, Ptm> map = new HashMap<Integer, Ptm>();

	public PTMAdapter(PTM ptm, Psm psm) {
		this.psm = psm;
		this.ptm = ptm;
	}

	@Override
	public Ptm adapt() {
		Ptm ret = null;
		if (map.containsKey(ptm.hashCode()))
			ret = map.get(ptm.hashCode());
		else {
			ret = new Ptm(psm, ptm.getMassShift(), ptm.getName());
			map.put(ptm.hashCode(), ret);
		}
		ret.setCvId(ptm.getCVId());
		final List<PTMSite> ptmSites = ptm.getPTMSites();
		if (ptmSites != null) {
			for (PTMSite ptmSite : ptmSites) {
				ret.getPtmSites().add(new PTMSiteAdapter(ptmSite, ret).adapt());
			}

		}

		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
