package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.Set;

import edu.scripps.yates.utilities.proteomicsmodel.Label;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Tissue;
import gnu.trove.map.hash.TIntObjectHashMap;

public class SampleImpl implements Sample {
	protected final static TIntObjectHashMap<Sample> samplesMap = new TIntObjectHashMap<Sample>();

	private final edu.scripps.yates.proteindb.persistence.mysql.Sample hibSample;
	private Tissue tissue;
	private Organism organism;

	public SampleImpl(edu.scripps.yates.proteindb.persistence.mysql.Sample sample) {
		hibSample = sample;
		samplesMap.put(sample.getId(), this);
	}

	@Override
	public String getDescription() {
		return hibSample.getDescription();
	}

	@Override
	public Boolean isWildType() {
		return hibSample.getWildtype();
	}

	@Override
	public String getName() {
		return hibSample.getName();
	}

	@Override
	public Label getLabel() {
		return new LabelImpl(hibSample.getLabel());
	}

	@Override
	public Tissue getTissue() {
		if (tissue == null) {
			if (hibSample.getTissue() != null) {
				if (TissueImpl.tissuesMap.containsKey(hibSample.getTissue().getTissueId())) {
					tissue = TissueImpl.tissuesMap.get(hibSample.getTissue().getTissueId());
				} else {
					tissue = new TissueImpl(hibSample.getTissue());
				}
			}
		}
		return tissue;
	}

	@Override
	public Organism getOrganism() {
		if (organism == null) {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Organism> hibOrganisms = hibSample.getOrganisms();
			if (hibOrganisms != null) {
				for (edu.scripps.yates.proteindb.persistence.mysql.Organism hibOrganism : hibOrganisms) {
					if (OrganismImpl.organismMap.containsKey(hibOrganism.getTaxonomyId())) {
						organism = OrganismImpl.organismMap.get(hibOrganism.getTaxonomyId());
					} else {
						organism = new OrganismImpl(hibOrganism);
					}
				}
			}
		}
		return organism;
	}
}
