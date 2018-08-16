package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.proteindb.persistence.mysql.Tissue;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.SampleBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class SampleBeanAdapter implements Adapter<SampleBean> {
	private final Sample sample;
	private final ProjectBean project;
	private final static ThreadLocal<TIntObjectHashMap<SampleBean>> map = new ThreadLocal<TIntObjectHashMap<SampleBean>>();

	public SampleBeanAdapter(Sample sample, ProjectBean project) {
		this.sample = sample;
		this.project = project;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<SampleBean>());
		}
	}

	@Override
	public SampleBean adapt() {
		if (map.get().containsKey(sample.getId())) {
			return map.get().get(sample.getId());
		}
		final SampleBean ret = new SampleBean();
		map.get().put(sample.getId(), ret);
		ret.setDescription(sample.getDescription());
		if (sample.getLabel() != null) {
			ret.setLabel(new LabelBeanAdapter(sample.getLabel()).adapt());
		}
		ret.setName(sample.getName());
		if (sample.getOrganisms() != null && !sample.getOrganisms().isEmpty()) {
			for (final Object obj : sample.getOrganisms()) {
				final Organism organism = (Organism) obj;
				ret.setOrganism(new OrganismBeanAdapter(organism).adapt());
				// just adapt the first. The DB model is different from the data
				// model, allowing more than one organism for each sample
				break;
			}
		}

		final Tissue tissue = sample.getTissue();
		if (tissue != null) {
			ret.setTissue(new TissueBeanAdapter(tissue).adapt());
		}
		if (sample.getWildtype() != null) {
			ret.setWildType(sample.getWildtype());
		}
		ret.setProject(project);

		return ret;
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
