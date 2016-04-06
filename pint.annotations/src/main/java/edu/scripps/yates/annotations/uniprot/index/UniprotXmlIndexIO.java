package edu.scripps.yates.annotations.uniprot.index;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.Uniprot;
import edu.scripps.yates.utilities.index.TextFileIndexIO;

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
public class UniprotXmlIndexIO extends TextFileIndexIO {
	private final static Logger log = Logger.getLogger(UniprotXmlIndexIO.class);
	private final static String ENTRY_START_TOKEN = "<entry";
	private final static String ENTRY_FINAL_TOKEN = "</entry>";
	private static final String PREFIX = "<?xml version='1.0' encoding='UTF-8'?>\n<uniprot xmlns=\"http://uniprot.org/uniprot\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://uniprot.org/uniprot http://www.uniprot.org/support/docs/uniprot.xsd\">";
	private static final String SUFFIX = "\n<copyright>\nCopyrighted by the UniProt Consortium, see http://www.uniprot.org/terms\nDistributed under the Creative Commons Attribution-NoDerivs License\n</copyright>\n</uniprot>";

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

		Entry entry = unmarshallFromString(string);
		if (entry != null) {
			// ret = convertUniprotEntries2Proteins(uniprot);
			Set<String> accs = new HashSet<String>();
			if (entry.getAccession() != null) {
				for (String acc : entry.getAccession()) {
					accs.add(acc);
				}
			}
			return accs;
		}

		return null;
	}

	protected Entry unmarshallFromString(String string) {
		if (string.startsWith("<<"))
			string = string.substring(1);
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
			Uniprot uniprot = (Uniprot) unmarshaller.unmarshal(new StringReader(PREFIX + string + SUFFIX));
			Entry entry = uniprot.getEntry().get(0);
			return entry;
		} catch (JAXBException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
}
