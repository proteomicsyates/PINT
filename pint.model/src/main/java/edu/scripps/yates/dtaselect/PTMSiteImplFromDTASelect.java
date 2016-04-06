package edu.scripps.yates.dtaselect;

import edu.scripps.yates.dtaselectparser.util.DTASelectModification;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PTMSiteImplFromDTASelect implements PTMSite {
	private final DTASelectModification dtaSelectModification;

	public PTMSiteImplFromDTASelect(DTASelectModification dtaSelectModification) {
		this.dtaSelectModification = dtaSelectModification;
	}

	@Override
	public String getAA() {
		return String.valueOf(dtaSelectModification.getAa());
	}

	@Override
	public int getPosition() {
		return dtaSelectModification.getModPosition();
	}

	@Override
	public Score getScore() {
		return null;
	}

}
