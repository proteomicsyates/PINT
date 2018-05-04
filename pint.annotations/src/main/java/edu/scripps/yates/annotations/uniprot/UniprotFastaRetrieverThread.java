package edu.scripps.yates.annotations.uniprot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.pi.ParIterator;
import edu.scripps.yates.utilities.pi.reductions.Reducible;
import gnu.trove.map.hash.THashMap;

public class UniprotFastaRetrieverThread extends Thread {
	private static Logger log = Logger.getLogger(UniprotFastaRetrieverThread.class);
	private final Reducible<Map<String, Entry>> reducibleMap;
	private final ParIterator<String> iterator;

	public UniprotFastaRetrieverThread(ParIterator<String> iterator, Reducible<Map<String, Entry>> reducibleMap) {
		this.iterator = iterator;
		this.reducibleMap = reducibleMap;
	}

	@Override
	public void run() {
		final Map<String, Entry> ret = new THashMap<String, Entry>();
		reducibleMap.set(ret);

		while (iterator.hasNext()) {
			final String accession = iterator.next();
			try {
				if (UniprotProteinRemoteRetriever.entriesWithNoFASTA.contains(accession)) {
					continue;
				}

				final Entry fastaEntry = UniprotFastaRetriever.getFastaEntry(accession);
				if (fastaEntry != null) {
					ret.put(accession, fastaEntry);
				} else {
					log.info("Adding " + accession + " to the list of proteins with no FASTA sequence available.");
					UniprotProteinRemoteRetriever.entriesWithNoFASTA.add(accession);
				}

			} catch (final MalformedURLException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			} catch (final IOException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			} catch (final InterruptedException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			} catch (final URISyntaxException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
				iterator.register(e);
			}
		}

	}
}
