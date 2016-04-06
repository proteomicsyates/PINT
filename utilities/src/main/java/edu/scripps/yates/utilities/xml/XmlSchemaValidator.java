package edu.scripps.yates.utilities.xml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlSchemaValidator {
	/**
	 * This static object is used to create the Schema object used for
	 * validation.
	 */
	private static final SchemaFactory SCHEMA_FACTORY = SchemaFactory
			.newInstance("http://www.w3.org/2001/XMLSchema");

	public static Schema getSchema(URI aSchemaUri) throws SAXException,
			MalformedURLException {
		return SCHEMA_FACTORY.newSchema(aSchemaUri.toURL());
	}

	public static Schema getSchema(URL aSchemaUrl) throws SAXException {
		return SCHEMA_FACTORY.newSchema(aSchemaUrl);
	}

	public static Schema getSchema(File schemaFile) throws SAXException {
		return SCHEMA_FACTORY.newSchema(schemaFile);
	}

	public static Schema getSchema(Source schemaSource) throws SAXException {
		return SCHEMA_FACTORY.newSchema(schemaSource);
	}

	public static XMLSchemaValidatorErrorHandler validate(Reader reader,
			Schema schema) throws SAXException {

		final XMLSchemaValidatorErrorHandler errorHandler = new XMLSchemaValidatorErrorHandler();
		Validator validator = schema.newValidator();
		validator.setErrorHandler(errorHandler);
		try {
			XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance()
					.createXMLStreamReader(reader);
			validator.validate(new StAXSource(xmlStreamReader));
		} catch (IOException ioe) {
			errorHandler.fatalError(ioe);
		} catch (SAXParseException spe) {
			errorHandler.fatalError(spe);
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
		}
		return errorHandler;
	}

}
