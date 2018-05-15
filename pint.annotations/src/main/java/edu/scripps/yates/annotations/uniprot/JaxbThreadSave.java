package edu.scripps.yates.annotations.uniprot;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbThreadSave {
	private final ThreadLocal<Unmarshaller> unmarshaller;
	private final ThreadLocal<Marshaller> marshaller;

	public JaxbThreadSave(JAXBContext jaxb) {
		unmarshaller = ThreadLocal.withInitial(() -> safe(jaxb::createUnmarshaller));
		marshaller = ThreadLocal.withInitial(() -> safe(jaxb::createMarshaller));
	}

	public JaxbThreadSave(Class... classes) {
		this(safe(() -> JAXBContext.newInstance(classes)));
	}

	private static <T> T safe(Callable<T> fn) {
		try {
			return fn.call();
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object unmarshal(InputStream is) throws JAXBException {
		return unmarshaller.get().unmarshal(is);
	}

	public Object unmarshal(File f) throws JAXBException {
		return unmarshaller.get().unmarshal(f);
	}

	public void marshal(Object jaxbElement, OutputStream os) throws JAXBException {
		marshaller.get().marshal(jaxbElement, os);
	}

	public void marshal(Object jaxbElement, File output) throws JAXBException {
		marshaller.get().marshal(jaxbElement, output);
	}
}
