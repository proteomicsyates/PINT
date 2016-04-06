package edu.scripps.yates.utilities.proteomicsmodel;

import java.util.Date;

public interface MSRun {
	public int getDBId();

	public String getRunId();

	public String getPath();

	public Date getDate();

	public Project getProject();

}
