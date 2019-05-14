package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType;

public class ProteinRatioValueAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4685761045421513044L;
	private final Ratio ratio;
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

		final ProteinRatioValue ret = new ProteinRatioValue();

		final RatioDescriptor proteinRatioDescriptor = new RatioDescriptorAdapter(ratio.getDescription(),
				ratio.getCondition1(), ratio.getCondition2(), ret, hibProject).adapt();

		ret.setRatioDescriptor(proteinRatioDescriptor);

		// score
		final Score score = ratio.getAssociatedConfidenceScore();
		if (score != null) {
			final String scoreType = score.getScoreType();
			ret.setConfidenceScoreType(scoreType);
			try {
				ret.setConfidenceScoreValue(Double.valueOf(score.getValue()));
			} catch (final NumberFormatException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Error parsing protein ratio score: " + score.getScoreName()
						+ " with value: " + score.getValue());
			}
			ret.setConfidenceScoreName(score.getScoreName());
		}
		// combination type
		final CombinationType combinationType = ratio.getCombinationType();
		if (combinationType != null) {
			ret.setCombinationType(combinationType.getName());
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

}
