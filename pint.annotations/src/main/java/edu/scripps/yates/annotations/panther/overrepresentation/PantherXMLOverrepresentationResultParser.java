package edu.scripps.yates.annotations.panther.overrepresentation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.panther.overrepresentation.xml.Overrepresentation;
import edu.scripps.yates.annotations.panther.overrepresentation.xml.Overrepresentation.Group;
import edu.scripps.yates.annotations.panther.overrepresentation.xml.Overrepresentation.Group.Result;

public class PantherXMLOverrepresentationResultParser {
	private final static Logger log = Logger.getLogger(PantherXMLOverrepresentationResultParser.class);
	private final File xmlResultsFile;
	private Overrepresentation result;
	private static JAXBContext jaxbContext;

	public static JAXBContext getJAXBContext() {
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(Overrepresentation.class);
			} catch (final JAXBException e) {
				e.printStackTrace();

			}
		}
		return jaxbContext;
	}

	public PantherXMLOverrepresentationResultParser(File xmlResultsFile) {
		this.xmlResultsFile = xmlResultsFile;
	}

	public List<Result> getEnrichedAnnotations(double fdrThreshold) throws JAXBException {
		if (result == null) {
			result = parseFile();
			if (result == null) {
				throw new IllegalArgumentException("No results in file " + this.xmlResultsFile.getAbsolutePath());
			}
		}
		final List<Result> ret = new ArrayList<Result>();
		for (final Group group : result.getGroup()) {
			ret.addAll(group.getResult().stream().filter(r -> r.getInputList().getFdr() <= fdrThreshold)
					.collect(Collectors.toList()));
		}
		return ret;
	}

	public Overrepresentation getOverrepresentation() throws JAXBException {
		if (result == null) {
			result = parseFile();
		}

		return result;
	}

	private Overrepresentation parseFile() throws JAXBException {

		log.info("Processing file " + this.xmlResultsFile.getAbsolutePath());

		final Unmarshaller unmarshaller = getJAXBContext().createUnmarshaller();
		final Overrepresentation result = (Overrepresentation) unmarshaller.unmarshal(this.xmlResultsFile);

		log.info("Response parsed succesfully");
		if (result.getGroup() != null) {
			log.info(result.getGroup().size() + " groups read from " + result.getAnnotationType());
		}
		return result;

	}
}
