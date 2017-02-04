package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;

public class ProjectAdapter implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.Project>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8704484208696801294L;
	private static final Logger log = Logger.getLogger(ProjectAdapter.class);
	private final edu.scripps.yates.utilities.proteomicsmodel.Project project;
	private final static Map<Integer, edu.scripps.yates.proteindb.persistence.mysql.Project> map = new HashMap<Integer, Project>();

	public ProjectAdapter(edu.scripps.yates.utilities.proteomicsmodel.Project project2) {
		project = project2;
	}

	@Override
	public synchronized Project adapt() {
		clearStaticInformation();
		Project ret = new Project();
		if (map.containsKey(project.hashCode()))
			return map.get(project.hashCode());
		map.put(project.hashCode(), ret);
		ret.setDescription(project.getDescription());
		ret.setName(project.getName());
		ret.setPrivate_(project.isPrivate());
		ret.setBig(project.isBig());
		try {
			ret.setPubmedLink(project.getPubmedLink().toURI().toString());
		} catch (Exception e) {
		}
		ret.setReleaseDate(project.getReleaseDate());
		final Date uploadedDate = project.getUploadedDate();
		if (uploadedDate != null) {
			ret.setUploadedDate(uploadedDate);
		} else {
			ret.setUploadedDate(new Date());
		}
		final String tag = project.getTag();
		ret.setTag(tag);

		final Set<Condition> conditions = project.getConditions();

		// create all the adapters and then adapt them
		Set<ConditionAdapter> adapters = new HashSet<ConditionAdapter>();
		for (Condition condition : conditions) {

			adapters.add(new ConditionAdapter(condition, ret));
		}
		for (ConditionAdapter conditionAdapter : adapters) {
			ret.getConditions().add(conditionAdapter.adapt());
		}
		// msRuns
		final Set<MSRun> msRuns = project.getMSRuns();
		if (msRuns != null) {
			for (MSRun msRun : msRuns) {
				ret.getMsRuns().add(new MSRunAdapter(msRun, ret).adapt());
			}
		}

		// add the peptides and psms to the conditions
		for (Object obj : ret.getConditions()) {
			edu.scripps.yates.proteindb.persistence.mysql.Condition condition = (edu.scripps.yates.proteindb.persistence.mysql.Condition) obj;
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> proteins = condition.getProteins();
			log.info("Assigning peptides and psms to condition " + condition.getName());

			for (edu.scripps.yates.proteindb.persistence.mysql.Protein protein : proteins) {
				condition.getPeptides().addAll(protein.getPeptides());
				condition.getPsms().addAll(protein.getPsms());
			}

		}

		clearStaticInformation();

		return ret;
	}

	public static void clearStaticInformation() {
		log.info("Clearing static information");
		AmountTypeAdapter.clearStaticInformation();
		AnnotationTypeAdapter.clearStaticInformation();
		CombinationTypeAdapter.clearStaticInformation();
		ConditionAdapter.clearStaticInformation();
		ConfidenceScoreTypeAdapter.clearStaticInformation();
		GeneAdapter.clearStaticInformation();
		LabelAdapter.clearStaticInformation();
		MSRunAdapter.clearStaticInformation();
		OrganismAdapter.clearStaticInformation();
		PeptideAdapter.clearStaticInformation();
		// PeptideAmountAdapter.clearStaticInformation();
		// PeptideRatioValueAdapter.clearStaticInformation();
		// PeptideScoreAdapter.clearStaticInformation();
		ProteinAccessionAdapter.clearStaticInformation();
		ProteinAdapter.clearStaticInformation();
		// ProteinAmountAdapter.clearStaticInformation();
		// ProteinAnnotationAdapter.clearStaticInformation();
		// ProteinRatioValueAdapter.clearStaticInformation();
		// ProteinScoreAdapter.clearStaticInformation();
		// ProteinThresholdAdapter.clearStaticInformation();
		PSMAdapter.clearStaticInformation();
		// PSMAmountAdapter.clearStaticInformation();
		// PSMRatioValueAdapter.clearStaticInformation();
		// PSMScoreAdapter.clearStaticInformation();
		// PTMAdapter.clearStaticInformation();
		// PTMSiteAdapter.clearStaticInformation();
		RatioDescriptorAdapter.clearStaticInformation();
		SampleAdapter.clearStaticInformation();
		ThresholdAdapter.clearStaticInformation();
		TissueAdapter.clearStaticInformation();
		StaticProteomicsModelStorage.clearData();
	}

}
