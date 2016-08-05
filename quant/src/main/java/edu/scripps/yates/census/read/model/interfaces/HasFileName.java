package edu.scripps.yates.census.read.model.interfaces;

import java.util.Set;

public interface HasFileName {
	public void addFileName(String fileName);

	public Set<String> getFileNames();

}
