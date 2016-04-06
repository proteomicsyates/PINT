package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.Set;

public interface HasPsms {
	public Set<PSM> getPSMs();

	public void addPSM(PSM psm);
}
