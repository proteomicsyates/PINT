package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class ProteinRatioValueAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4685761045421513044L;
	private final Ratio ratio;
	private static final Map<Integer, ProteinRatioValue> map = new HashMap<Integer, ProteinRatioValue>();
	private final Protein protein;
	private final Project hibProject;

	public ProteinRatioValueAdapter(Ratio proteinRatioModel2, Protein protein, Project hibProject) {
		ratio = proteinRatioModel2;
		this.hibProject = hibProject;
		if (protein == null)
			throw new IllegalArgumentException("Protein cannot be null");
		this.protein = protein;
	}

	@Override
	public ProteinRatioValue adapt() {
		if (map.containsKey(ratio.hashCode()))
			return map.get(ratio.hashCode());
		ProteinRatioValue ret = new ProteinRatioValue();
		map.put(ratio.hashCode(), ret);

		final RatioDescriptor proteinRatioDescriptor = new RatioDescriptorAdapter(ratio.getDescription(),
				ratio.getCondition1(), ratio.getCondition2(), ret, hibProject).adapt();

		ret.setRatioDescriptor(proteinRatioDescriptor);

		// score
		final Score score = ratio.getAssociatedConfidenceScore();
		if (score != null) {
			final ConfidenceScoreType scoreType = new ConfidenceScoreTypeAdapter(score).adapt();
			ret.setConfidenceScoreType(scoreType);
			ret.setConfidenceScoreValue(Double.valueOf(score.getValue()));
			ret.setConfidenceScoreName(score.getScoreName());
		}
		// combination type
		final CombinationType combinationType = ratio.getCombinationType();
		if (combinationType != null) {
			ret.setCombinationType(new CombinationTypeAdapter(combinationType, ret).adapt());
		}
		ret.setValue(PersistenceUtils.parseRatioValueRemoveInfinities(ratio.getValue()));

		ret.setProtein(protein);

		return ret;
	}

	/**
	 * @return the proteinRatioModel
	 */
	public Ratio getProteinRatioModel() {
		return ratio;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
