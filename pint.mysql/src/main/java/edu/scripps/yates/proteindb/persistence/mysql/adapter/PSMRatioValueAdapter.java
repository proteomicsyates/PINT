package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.model.enums.CombinationType;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PSMRatioValueAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3143862235227791218L;
	/**
	 *
	 */
	private final Ratio ratio;
	private final Project hibProject;
	private final Psm psm;
	private final static Logger log = Logger.getLogger(PSMRatioValueAdapter.class);

	public PSMRatioValueAdapter(Ratio ratio, Psm psm, Project hibProject) {
		this.ratio = ratio;
		this.hibProject = hibProject;
		if (psm == null)
			throw new IllegalArgumentException("psm cannot be null");
		this.psm = psm;
	}

	@Override
	public PsmRatioValue adapt() {

		PsmRatioValue ret = new PsmRatioValue();

		final RatioDescriptor peptideRatioDescriptor = new RatioDescriptorAdapter(ratio.getDescription(),
				ratio.getCondition1(), ratio.getCondition2(), ret, hibProject).adapt();

		ret.setRatioDescriptor(peptideRatioDescriptor);

		// score
		final Score score = ratio.getAssociatedConfidenceScore();
		if (score != null) {
			try {
				final Double scoreValue = Double.valueOf(score.getValue());
				if (!Double.isNaN(scoreValue)) {
					final ConfidenceScoreType scoreType = new ConfidenceScoreTypeAdapter(score).adapt();
					ret.setConfidenceScoreType(scoreType);
					ret.setConfidenceScoreValue(scoreValue);
					ret.setConfidenceScoreName(score.getScoreName());
				} else {
					log.debug("Nan score. Ignoring it");
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
				throw new IllegalArgumentException(
						"Error parsing PSM ratio score: " + score.getScoreName() + " with value: " + score.getValue());
			}
		}
		// combination type
		final CombinationType combinationType = ratio.getCombinationType();
		if (combinationType != null) {
			ret.setCombinationType(new CombinationTypeAdapter(combinationType, ret).adapt());
		}
		ret.setValue(PersistenceUtils.parseRatioValueRemoveInfinities(ratio.getValue()));

		ret.setPsm(psm);

		return ret;
	}

}
