package edu.scripps.yates.annotations.uniprot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.EvidencedStringType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinType.RecommendedName;
import edu.scripps.yates.utilities.pattern.Adapter;

public class UniprotEntryAdapterFromRDF implements Adapter<Entry> {
	private final String accession;
	private final boolean obsolete;
	private final boolean reviewed;
	private final String modified;
	private final int version;

	public UniprotEntryAdapterFromRDF(String accession, boolean obsolete, boolean reviewed, String modified,
			int version) {
		this.accession = accession;
		this.obsolete = obsolete;
		this.reviewed = reviewed;
		this.modified = modified;
		this.version = version;

	}

	@Override
	public Entry adapt() {
		Entry ret = new Entry();
		ret.getAccession().add(accession);
		if (modified != null) {
			GregorianCalendar c = new GregorianCalendar();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				Date date = format.parse(modified);
				c.set(date.getYear(), date.getMonth(), date.getDate());

				ret.setModified(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));

			} catch (ParseException e1) {
				e1.printStackTrace();
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}

		}
		ret.setVersion(version);
		String ENTRY_LEGEND = null;
		if (reviewed) {
			ENTRY_LEGEND = "This entry was modified from UniprotKB";
			if (modified != null) {
				ENTRY_LEGEND += " at '" + modified + "'";
			}
		} else if (obsolete) {
			ENTRY_LEGEND = "This entry was marked as obsolete from UniprotKB";
			if (modified != null) {
				ENTRY_LEGEND += " at '" + modified + "'";
			}
		}
		ret.getName().add(ENTRY_LEGEND);
		final ProteinType protein = new ProteinType();
		final EvidencedStringType evidence = new EvidencedStringType();
		evidence.setValue(ENTRY_LEGEND);
		final RecommendedName recommendedName = new RecommendedName();
		recommendedName.setFullName(evidence);
		protein.setRecommendedName(recommendedName);
		ret.setProtein(protein);
		return ret;
	}
}
