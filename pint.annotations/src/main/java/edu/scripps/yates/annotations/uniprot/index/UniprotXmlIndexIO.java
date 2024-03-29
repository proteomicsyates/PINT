package edu.scripps.yates.annotations.uniprot.index;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.util.IndexException;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.utilities.index.TextFileIndexIO;
import edu.scripps.yates.utilities.index.TextFileIndexMultiThreadSafeIO;
import gnu.trove.set.hash.THashSet;

/**
 * Extension of {@link TextFileIndexIO} using as start token <entry and as end
 * token </entry>.<br>
 * The string taken between start and end tokens will be parsed as an
 * {@link Entry} (unmarshalled) and will be indexed using the Accessions of the
 * {@link Entry}.
 *
 * @author Salva
 *
 */
public class UniprotXmlIndexIO extends TextFileIndexMultiThreadSafeIO {
	private final static Logger log = Logger.getLogger(UniprotXmlIndexIO.class);
	private final static String ENTRY_START_TOKEN = "<entry";
	private final static String ENTRY_FINAL_TOKEN = "</entry>";
	protected static final String PREFIX = "<?xml version='1.0' encoding='UTF-8'?>\n<uniprot xmlns=\"http://uniprot.org/uniprot\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://uniprot.org/uniprot http://www.uniprot.org/support/docs/uniprot.xsd\">";
	protected static final String SUFFIX = "\n<copyright>\nCopyrighted by the UniProt Consortium, see http://www.uniprot.org/terms\nDistributed under the Creative Commons Attribution-NoDerivs License\n</copyright>\n</uniprot>";

	private final JAXBContext jaxbContext;

	/**
	 * Constructor using the file path
	 *
	 * @param path
	 * @throws IOException
	 * @throws JAXBException
	 */
	public UniprotXmlIndexIO(String path) throws IOException, JAXBException {
		super(path, ENTRY_START_TOKEN, ENTRY_FINAL_TOKEN);
		jaxbContext = JAXBContext.newInstance(Uniprot.class);
	}

	/**
	 * Constructor using the {@link File}
	 *
	 * @param path
	 * @throws IOException
	 * @throws JAXBException
	 */
	public UniprotXmlIndexIO(File file) throws IOException, JAXBException {
		super(file, ENTRY_START_TOKEN, ENTRY_FINAL_TOKEN);
		jaxbContext = JAXBContext.newInstance(Uniprot.class);
	}

	/**
	 * Gets the keys to use in the index for each entry. In this case each entry
	 * corresponds to an {@link Entry}, and the keys will be the accessions of
	 * the uniprot {@link Entry}
	 */
	@Override
	protected Set<String> getKeys(String string) {

		final List<Entry> entries = unmarshallMultipleEntriesFromString(string);
		if (entries != null) {
			final Set<String> accs = new HashSet<String>();
			// ret = convertUniprotEntries2Proteins(uniprot);
			for (final Entry entry : entries) {
				accs.addAll(getKeys(entry));
			}

			return accs;
		}

		return null;
	}

	protected Set<String> getKeys(Entry entry) {

		// ret = convertUniprotEntries2Proteins(uniprot);
		final Set<String> accs = new THashSet<String>();
		if (entry.getAccession() != null) {
			for (final String acc : entry.getAccession()) {
				accs.add(acc);
			}
		}
		if (entry.getName() != null) {
			for (final String name : entry.getName()) {
				accs.add(name);
			}
		}
		return accs;

	}

	protected Entry unmarshallSingleEntryFromString(String string) {
		if (string.startsWith("<<"))
			string = string.substring(1);
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
			final Uniprot uniprot = (Uniprot) unmarshaller.unmarshal(new StringReader(PREFIX + string + SUFFIX));
			final Entry entry = uniprot.getEntry().get(0);
			return entry;
		} catch (final JAXBException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new IndexException(
					"Error reading index. Index file may be corrupt. Try to delete it and run the program again.");
		}
	}

	protected List<Entry> unmarshallMultipleEntriesFromString(String string) {
		if (string.startsWith("<<")) {
			string = string.substring(1);
		}
		String s = "";
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
			s = PREFIX + string + SUFFIX;
			final Uniprot uniprot = (Uniprot) unmarshaller.unmarshal(new StringReader(s));
			return uniprot.getEntry();
		} catch (final JAXBException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new IndexException(
					"Error reading index. Index file may be corrupt. Try to delete it and run the program again.\n"
							+ s);
		}
	}

}
