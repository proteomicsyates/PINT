package edu.scripps.yates.annotations.uniprot.index;

import java.io.StringReader;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.EvidenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.GeneLocationType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.GeneType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.KeywordType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.OrganismType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinExistenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ProteinType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.ReferenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.SequenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Uniprot;

public class EntryWrapper extends Entry {
	private final static Logger log = Logger.getLogger(EntryWrapper.class);
	private final String entryString;
	private static JAXBContext jaxbContext;
	private Entry entry;
	static {
		try {
			jaxbContext = JAXBContext.newInstance(Uniprot.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public EntryWrapper(String entryString) {
		this.entryString = entryString;

	}

	private Entry getEntry() {
		if (entry == null) {
			entry = unmarshallFromString(entryString);
		}
		return entry;
	}

	private Entry unmarshallFromString(String string) {
		if (string.startsWith("<<"))
			string = string.substring(1);
		Unmarshaller unmarshaller;
		try {
			unmarshaller = jaxbContext.createUnmarshaller();
			Uniprot uniprot = (Uniprot) unmarshaller
					.unmarshal(new StringReader(UniprotXmlIndexIO.PREFIX + string + UniprotXmlIndexIO.SUFFIX));
			Entry entry = uniprot.getEntry().get(0);
			return entry;
		} catch (JAXBException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getAccession()
	 */
	@Override
	public List<String> getAccession() {
		return getEntry().getAccession();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getName()
	 */
	@Override
	public List<String> getName() {

		return getEntry().getName();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getProtein()
	 */
	@Override
	public ProteinType getProtein() {

		return getEntry().getProtein();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#setProtein(edu.scripps.
	 * yates.annotations.uniprot.xml.ProteinType)
	 */
	@Override
	public void setProtein(ProteinType value) {

		getEntry().setProtein(value);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getGene()
	 */
	@Override
	public List<GeneType> getGene() {

		return getEntry().getGene();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getOrganism()
	 */
	@Override
	public OrganismType getOrganism() {

		return getEntry().getOrganism();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#setOrganism(edu.scripps.
	 * yates.annotations.uniprot.xml.OrganismType)
	 */
	@Override
	public void setOrganism(OrganismType value) {

		getEntry().setOrganism(value);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getOrganismHost()
	 */
	@Override
	public List<OrganismType> getOrganismHost() {

		return getEntry().getOrganismHost();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getGeneLocation()
	 */
	@Override
	public List<GeneLocationType> getGeneLocation() {

		return getEntry().getGeneLocation();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getReference()
	 */
	@Override
	public List<ReferenceType> getReference() {

		return getEntry().getReference();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getComment()
	 */
	@Override
	public List<CommentType> getComment() {

		return getEntry().getComment();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getDbReference()
	 */
	@Override
	public List<DbReferenceType> getDbReference() {

		return getEntry().getDbReference();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#getProteinExistence()
	 */
	@Override
	public ProteinExistenceType getProteinExistence() {

		return getEntry().getProteinExistence();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#setProteinExistence(edu.
	 * scripps.yates.annotations.uniprot.xml.ProteinExistenceType)
	 */
	@Override
	public void setProteinExistence(ProteinExistenceType value) {

		getEntry().setProteinExistence(value);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getKeyword()
	 */
	@Override
	public List<KeywordType> getKeyword() {

		return getEntry().getKeyword();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getFeature()
	 */
	@Override
	public List<FeatureType> getFeature() {

		return getEntry().getFeature();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getEvidence()
	 */
	@Override
	public List<EvidenceType> getEvidence() {

		return getEntry().getEvidence();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getSequence()
	 */
	@Override
	public SequenceType getSequence() {

		return getEntry().getSequence();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#setSequence(edu.scripps.
	 * yates.annotations.uniprot.xml.SequenceType)
	 */
	@Override
	public void setSequence(SequenceType value) {

		getEntry().setSequence(value);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getDataset()
	 */
	@Override
	public String getDataset() {

		return getEntry().getDataset();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#setDataset(java.lang.
	 * String)
	 */
	@Override
	public void setDataset(String value) {

		getEntry().setDataset(value);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getCreated()
	 */
	@Override
	public XMLGregorianCalendar getCreated() {

		return getEntry().getCreated();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#setCreated(javax.xml.
	 * datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setCreated(XMLGregorianCalendar value) {

		getEntry().setCreated(value);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getModified()
	 */
	@Override
	public XMLGregorianCalendar getModified() {

		return getEntry().getModified();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * edu.scripps.yates.annotations.uniprot.xml.Entry#setModified(javax.xml.
	 * datatype.XMLGregorianCalendar)
	 */
	@Override
	public void setModified(XMLGregorianCalendar value) {

		getEntry().setModified(value);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#getVersion()
	 */
	@Override
	public int getVersion() {

		return getEntry().getVersion();
	}

	/*
	 * (non-Javadoc)
	 * @see edu.scripps.yates.annotations.uniprot.xml.Entry#setVersion(int)
	 */
	@Override
	public void setVersion(int value) {

		getEntry().setVersion(value);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return getEntry().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		return getEntry().equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return getEntry().toString();
	}

}
