package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;

public class ProteinAnnotationAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5261943360232585400L;
	private final edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation;
	// disabled because some times, an annotation is built for a protein set, so
	// ideally has to be an object for each protein in the set. But it is not
	// done. So, disabling the static map, we will force to create a new
	// annotation object for each protein
	// private final static TIntObjectHashMap< ProteinAnnotation> map = new
	// TIntObjectHashMap< ProteinAnnotation>();
	private final Protein protein;

	public ProteinAnnotationAdapter(edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation2, Protein protein) {
		proteinAnnotation = proteinAnnotation2;
		this.protein = protein;
	}

	@Override
	public ProteinAnnotation adapt() {
		ProteinAnnotation ret = new ProteinAnnotation();
		// if (map.containsKey(proteinAnnotation.hashCode()))
		// return map.get(proteinAnnotation.hashCode());
		// map.put(proteinAnnotation.hashCode(), ret);
		ret.setValue(proteinAnnotation.getValue());
		ret.setName(proteinAnnotation.getName());
		ret.setAnnotationType(new AnnotationTypeAdapter(proteinAnnotation.getAnnotationType(), ret).adapt());
		ret.setSource(proteinAnnotation.getSource());
		ret.setProtein(protein);
		return ret;
	}

	// protected static void clearStaticInformation() {
	// map.clear();
	// }
}
