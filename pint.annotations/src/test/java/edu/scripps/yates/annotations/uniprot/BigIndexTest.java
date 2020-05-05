package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.util.Map;

import org.junit.Test;

import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;

public class BigIndexTest {
	@Test
	public void bigIndexTest() {
		File uniprotReleasesFolder = new File("Z:\\share\\Salva\\data\\uniprotKB\\bigIndex");
		UniprotProteinLocalRetriever uplr = new UniprotProteinLocalRetriever(uniprotReleasesFolder, true);
		String acc = "P30971";
		Map<String, Entry> annotatedProtein = uplr.getAnnotatedProtein(null, acc);
		String taxonomy = UniprotEntryUtil.getTaxonomyName(annotatedProtein.get(acc));
		System.out.println(taxonomy);

	}
}
