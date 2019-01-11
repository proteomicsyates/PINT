package edu.scripps.yates.client.gui.reactome;

public enum ReactomeSupportedSpecies {
	Homo_sapiens("Homo sapiens"), Arabidopsis_thaliana("Arabidopsis thaliana"), //
	Bos_taurus("Bos taurus"), Caenorhabditis_elegans("Caenorhabditis elegans"), //
	Canis_familiaris("Canis familiaris"), Danio_rerio("Danio rerio"), //
	Dictyostelium_discoideum("Dictyostelium discoideum"), Drosophila_melanogaster("Drosophila_melanogaster"), //
	Gallus_gallus("Gallus gallus"), Mus_musculus("Mus musculus"), //
	Mycobacterium_tuberculosis("Mycobacterium tuberculosis"), Oryza_sativa("Oryza sativa"), //
	Plasmodium_falciparum("Plasmodium falciparum"), Rattus_norvegicus("Rattus norvegicus"), //
	Saccharomyces_cerevisiae("Saccharomyces cerevisiae"), Schizosaccharomyces_pombe("Schizosaccharomyces pombe"), //
	Sus_scrofa("Sus scrofa"), Taeniopygia_guttata("Taeniopygia guttata"), Xenopus_tropicalis("Xenopus tropicalis");
	private final String scientificName;

	ReactomeSupportedSpecies(String scientificName) {
		this.scientificName = scientificName;
	}

	/**
	 * @return the scientificName
	 */
	public String getScientificName() {
		return scientificName;
	}

	public static ReactomeSupportedSpecies getByScientificName(String scientifiName) {
		for (ReactomeSupportedSpecies reactomeSupportedSpecies : values()) {
			if (reactomeSupportedSpecies.getScientificName().equalsIgnoreCase(scientifiName)) {
				return reactomeSupportedSpecies;
			}
		}
		return null;
	}

	public String toJSonString() {
		return name() + ".json";
	}
}
