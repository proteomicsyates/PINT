package edu.scripps.yates.model.util;

import org.proteored.miapeapi.interfaces.Adapter;

import edu.scripps.yates.utilities.model.factories.PTMSiteEx;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PTMSiteAdapter implements Adapter<PTMSite> {
	private final String aa;
	private final int position;
	private final Score score;

	public PTMSiteAdapter(String aa, int position, Score score) {
		this.aa = aa;
		this.position = position;
		this.score = score;
	}

	public PTMSiteAdapter(String aa, int position) {
		this(aa, position, null);
	}

	@Override
	public PTMSite adapt() {
		PTMSiteEx ptmSite = new PTMSiteEx(aa, position);
		if (score != null)
			ptmSite.setScore(score);
		return ptmSite;
	}

}
