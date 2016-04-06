package edu.scripps.yates.client.history;

public enum TargetHistory {
	HOME("home"), RELOAD("reload"), SUBMIT("submit"), QUERY("query"), BROWSE(
			"browse"), REGISTER("register"), HELP("help"), ABOUT("about"), NEWS(
			"news"), TOOLS("tools"), PROTEIN_DOWNLOAD("proteinDownload"), LOAD_PROJECT(
			"loadProject"), PSEAQUANT("PSEA-Quant");

	private final String targetHistory;

	private TargetHistory(String targetHistory) {
		this.targetHistory = targetHistory;
	}

	/**
	 * @return the targetHistory
	 */
	public String getTargetHistory() {
		return targetHistory;
	}

}
