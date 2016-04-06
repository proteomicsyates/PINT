package edu.scripps.yates.utilities.proteomicsmodel;

import java.net.URL;
import java.util.Date;
import java.util.Set;

public interface Project {

	public String getDescription();

	public String getName();

	public String getTag();

	public Date getReleaseDate();

	public Date getUploadedDate();

	public URL getPubmedLink();

	public Set<Condition> getConditions();

	public Set<MSRun> getMSRuns();

	public boolean isPrivate();

	public boolean isBig();
}
