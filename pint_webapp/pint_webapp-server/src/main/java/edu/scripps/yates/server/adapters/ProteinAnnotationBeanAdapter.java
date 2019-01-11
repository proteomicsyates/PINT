package edu.scripps.yates.server.adapters;

import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.ProteinAnnotationBean;
import gnu.trove.map.hash.TIntObjectHashMap;

public class ProteinAnnotationBeanAdapter implements Adapter<ProteinAnnotationBean> {
	private final ProteinAnnotation proteinAnnotation;
	private final edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation modelProteinAnnotation;
	private final static ThreadLocal<TIntObjectHashMap<ProteinAnnotationBean>> map = new ThreadLocal<TIntObjectHashMap<ProteinAnnotationBean>>();

	public ProteinAnnotationBeanAdapter(ProteinAnnotation proteinAnnotation) {
		this.proteinAnnotation = proteinAnnotation;
		modelProteinAnnotation = null;
		initializeMap();
	}

	public ProteinAnnotationBeanAdapter(
			edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation) {
		modelProteinAnnotation = proteinAnnotation;
		this.proteinAnnotation = null;
		initializeMap();
	}

	private void initializeMap() {
		if (map.get() == null) {
			map.set(new TIntObjectHashMap<ProteinAnnotationBean>());
		}
	}

	@Override
	public ProteinAnnotationBean adapt() {
		if (proteinAnnotation != null) {
			if (proteinAnnotation.getId() != null && map.get().containsKey(proteinAnnotation.getId())) {
				return map.get().get(proteinAnnotation.getId());
			}
			ProteinAnnotationBean ret = new ProteinAnnotationBean();
			if (proteinAnnotation.getId() != null) {
				map.get().put(proteinAnnotation.getId(), ret);
			}
			ret.setName(proteinAnnotation.getName());
			ret.setSource(proteinAnnotation.getSource());
			ret.setType(proteinAnnotation.getAnnotationType().getName());
			ret.setValue(proteinAnnotation.getValue());
			return ret;
		} else {

			ProteinAnnotationBean ret = new ProteinAnnotationBean();
			ret.setName(modelProteinAnnotation.getName());
			ret.setSource(modelProteinAnnotation.getSource());
			ret.setType(modelProteinAnnotation.getAnnotationType().getKey());
			ret.setValue(modelProteinAnnotation.getValue());
			return ret;
		}
	}

	public static void clearStaticMap() {
		if (map.get() != null) {
			map.get().clear();
		}
	}
}
