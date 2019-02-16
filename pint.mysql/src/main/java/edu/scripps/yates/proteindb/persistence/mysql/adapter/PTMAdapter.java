package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.List;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import gnu.trove.map.hash.TIntObjectHashMap;

public class PTMAdapter implements Adapter<Ptm>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 5095797962976071733L;
	private final static TIntObjectHashMap<Ptm> map = new TIntObjectHashMap<Ptm>();

	private final PTM ptm;
	private final Psm psm;
	private final Peptide peptide;

	public PTMAdapter(PTM ptm, Psm psm) {
		this.psm = psm;
		this.ptm = ptm;
		peptide = null;
	}

	public PTMAdapter(PTM ptm, Peptide peptide) {
		psm = null;
		this.ptm = ptm;
		this.peptide = peptide;

	}

	@Override
	public Ptm adapt() {
		Ptm ret = null;
		if (map.contains(ptm.hashCode())) {
			ret = map.get(ptm.hashCode());
			if (psm != null) {
				ret.setPsm(psm);
			}
			if (peptide != null) {
				ret.setPeptide(peptide);
			}
		} else {
			if (psm != null) {
				ret = new Ptm(psm, ptm.getMassShift(), ptm.getName());
//				ret.setPeptide(psm.getPeptide());
			} else if (peptide != null) {
				ret = new Ptm(peptide, ptm.getMassShift(), ptm.getName());
//				if (!peptide.getPsms().isEmpty()) {
//					final Psm psmFromPeptide = (Psm) peptide.getPsms().iterator().next();
//					ret.setPsm(psmFromPeptide);
//				}
			}
			map.put(ptm.hashCode(), ret);
		}
		ret.setCvId(ptm.getCVId());
		final List<PTMSite> ptmSites = ptm.getPTMSites();
		if (ptmSites != null) {
			for (final PTMSite ptmSite : ptmSites) {
				ret.getPtmSites().add(new PTMSiteAdapter(ptmSite, ret).adapt());
			}

		}

		return ret;
	}

}
