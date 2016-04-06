package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;

public class SampleAdapter implements
		Adapter<edu.scripps.yates.proteindb.persistence.mysql.Sample>,
		Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6490277510126371539L;
	private final edu.scripps.yates.utilities.proteomicsmodel.Sample sample;
	private final static Map<Integer, edu.scripps.yates.proteindb.persistence.mysql.Sample> map = new HashMap<Integer, Sample>();

	public SampleAdapter(edu.scripps.yates.utilities.proteomicsmodel.Sample sample2) {
		sample = sample2;
	}

	@Override
	public Sample adapt() {

		if (map.containsKey(sample.hashCode()))
			return map.get(sample.hashCode());
		Sample ret = new Sample(sample.getName());
		map.put(sample.hashCode(), ret);
		ret.setDescription(sample.getDescription());
		ret.setWildtype(sample.isWildType());
		// label
		if (sample.getLabel() != null)
			ret.setLabel(new LabelAdapter(sample.getLabel()).adapt());
		// tissue
		if (sample.getTissue() != null)
			ret.setTissue(new TissueAdapter(sample.getTissue()).adapt());

		// organism
		final Organism organism = sample.getOrganism();
		if (organism != null) {
			final edu.scripps.yates.proteindb.persistence.mysql.Organism hibOrganism = new OrganismAdapter(
					organism).adapt();
			ret.getOrganisms().add(hibOrganism);
		}
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
