package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProteinAccessionAdapter;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import gnu.trove.map.hash.THashMap;

public class AccessionImpl implements Accession {
	private final ProteinAccession proteinAccession;
	protected final static Map<String, Accession> accessionMap = new THashMap<String, Accession>();

	public AccessionImpl(ProteinAccession proteinAccession) {
		this.proteinAccession = proteinAccession;
		accessionMap.put(proteinAccession.getAccession(), this);
	}

	@Override
	public AccessionType getAccessionType() {
		return AccessionType.fromValue(proteinAccession.getAccessionType());
	}

	@Override
	public String getAccession() {
		return proteinAccession.getAccession();
	}

	@Override
	public String getDescription() {
		return proteinAccession.getDescription();
	}

	@Override
	public boolean equals(Accession accession) {
		if (getAccession().equalsIgnoreCase(accession.getAccession()))
			if (getAccessionType().equals(accession.getAccessionType()))
				return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Accession)
			return this.equals((Accession) obj);
		return super.equals(obj);
	}

	/*
	 * In order to get called the equals method when containsKey is called in a
	 * HashMap where the keys are AccessionEx (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAccession();
	}

	@Override
	public List<String> getAlternativeNames() {
		List<String> ret = new ArrayList<String>();
		String alternativeNames = proteinAccession.getAlternativeNames();
		if (alternativeNames != null)
			if (alternativeNames.contains(ProteinAccessionAdapter.SEPARATOR)) {
				StringTokenizer tokenizer = new StringTokenizer(alternativeNames, ProteinAccessionAdapter.SEPARATOR);

				while (tokenizer.hasMoreElements()) {
					String string = tokenizer.nextToken();
					ret.add(string);
				}
			} else {
				ret.add(alternativeNames);
			}

		return ret;
	}

}
