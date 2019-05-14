package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.enums.CombinationType;

public class PeptideRatioValueAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -4685761045421513044L;
	private final Ratio ratio;
	private final Project hibProject;
	private final Peptide peptide;

	public PeptideRatioValueAdapter(Ratio ratio, Peptide peptide, Project hibProject) {
		this.ratio = ratio;
		this.hibProject = hibProject;
		if (peptide == null)
			throw new IllegalArgumentException("peptide cannot be null");
		this.peptide = peptide;
	}

	@Override
	public PeptideRatioValue adapt() {
		final PeptideRatioValue ret = new PeptideRatioValue();

		final RatioDescriptor peptideRatioDescriptor = new RatioDescriptorAdapter(ratio.getDescription(),
				ratio.getCondition1(), ratio.getCondition2(), ret, hibProject).adapt();

		ret.setRatioDescriptor(peptideRatioDescriptor);

		// score
		final Score score = ratio.getAssociatedConfidenceScore();
		if (score != null) {
			final String scoreType = score.getScoreType();
			ret.setConfidenceScoreType(scoreType);
			try {
				ret.setConfidenceScoreValue(Double.valueOf(score.getValue()));
			} catch (final NumberFormatException e) {
				e.printStackTrace();
				throw new IllegalArgumentException("Error parsing peptide ratio score: " + score.getScoreName()
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

		ret.setPeptide(peptide);

		return ret;
	}

	/**
	 * @return the peptideRatioModel
	 */
	public Ratio getPeptideRatioModel() {
		return ratio;
	}

}
