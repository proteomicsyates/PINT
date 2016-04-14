package edu.scripps.yates.utilities.dates;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

public class DatesUtil {
	private static final Logger log = Logger.getLogger(DatesUtil.class);
	private static final DecimalFormat df = new DecimalFormat("#.#");

	/*
	 * Converts java.util.Date to javax.xml.datatype.XMLGregorianCalendar
	 */
	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(date);
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
		} catch (DatatypeConfigurationException ex) {
			log.warn(ex);
		}
		return xmlCalendar;
	}

	/*
	 * Converts XMLGregorianCalendar to java.util.Date in Java
	 */
	public static Date toDate(XMLGregorianCalendar calendar) {
		if (calendar == null) {
			return null;
		}
		return calendar.toGregorianCalendar().getTime();
	}

	public static String getDescriptiveTimeFromMillisecs(double timeInMillisecs) {
		if (timeInMillisecs < 1000) {
			return df.format(timeInMillisecs) + " ms";
		}
		double timeInSeg = timeInMillisecs / 1000;
		if (timeInSeg < 60) {
			return df.format(timeInSeg) + " sg";
		}
		double timeInMin = timeInSeg / 60;
		if (timeInMin < 60) {
			return df.format(timeInMin) + " min";
		}
		double timeInHours = timeInMin / 60;
		return df.format(timeInHours) + " hours";
	}

}
