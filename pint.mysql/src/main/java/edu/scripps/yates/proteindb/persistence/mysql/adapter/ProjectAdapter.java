package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ProjectAdapter implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.Project>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 8704484208696801294L;
	private static final Logger log = Logger.getLogger(ProjectAdapter.class);
	private final edu.scripps.yates.utilities.proteomicsmodel.Project project;
	private final static TIntObjectHashMap<edu.scripps.yates.proteindb.persistence.mysql.Project> map = new TIntObjectHashMap<Project>();

	public ProjectAdapter(edu.scripps.yates.utilities.proteomicsmodel.Project project2) {
		project = project2;
	}

	@Override
	public synchronized Project adapt() {
		clearStaticInformation();
		final Project ret = new Project();
		if (map.containsKey(project.hashCode()))
			return map.get(project.hashCode());
		map.put(project.hashCode(), ret);
		ret.setDescription(project.getDescription());
		ret.setName(project.getName());
		ret.setPrivate_(project.isPrivate());
		ret.setBig(project.isBig());
		try {
			ret.setPubmedLink(project.getPubmedLink().toURI().toString());
		} catch (final Exception e) {
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
		final Set<ConditionAdapter> adapters = new THashSet<ConditionAdapter>();
		for (final Condition condition : conditions) {

			adapters.add(new ConditionAdapter(condition, ret));
		}
		for (final ConditionAdapter conditionAdapter : adapters) {
			ret.getConditions().add(conditionAdapter.adapt());
		}
		// msRuns
		final Set<MSRun> msRuns = project.getMSRuns();
		if (msRuns != null) {
			for (final MSRun msRun : msRuns) {
				ret.getMsRuns().add(new MSRunAdapter(msRun, ret).adapt());
			}
		}

		// add the peptides and psms to the conditions
		for (final Object obj : ret.getConditions()) {
			final edu.scripps.yates.proteindb.persistence.mysql.Condition condition = (edu.scripps.yates.proteindb.persistence.mysql.Condition) obj;
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> proteins = condition.getProteins();
			log.info("Assigning peptides and psms to condition " + condition.getName());

			for (final edu.scripps.yates.proteindb.persistence.mysql.Protein protein : proteins) {
				condition.getPeptides().addAll(protein.getPeptides());
				condition.getPsms().addAll(protein.getPsms());
			}

		}

		if (project.getPrincipalInvestigator() != null) {
			final StringBuilder sb = new StringBuilder();
			sb.append(project.getPrincipalInvestigator().getName()).append(ImportCfgUtil.PI_SEPARATOR)
					.append(project.getPrincipalInvestigator().getEmail()).append(ImportCfgUtil.PI_SEPARATOR)
					.append(project.getPrincipalInvestigator().getInstitution()).append(ImportCfgUtil.PI_SEPARATOR)
					.append(project.getPrincipalInvestigator().getCountry());
			ret.setPi(sb.toString());
		}

		if (project.getInstruments() != null) {
			final StringBuilder sb = new StringBuilder();
			for (final String instrument : project.getInstruments()) {
				if (!"".equals(sb.toString())) {
					sb.append(ImportCfgUtil.PI_SEPARATOR);
				}
				sb.append(instrument);
			}
			ret.setInstruments(sb.toString());
		}

		clearStaticInformation();

		return ret;
	}

	public static void clearStaticInformation() {
		log.info("Clearing static information");

		AnnotationTypeAdapter.clearStaticInformation();

		ConditionAdapter.clearStaticInformation();

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
		TissueAdapter.clearStaticInformation();
		StaticProteomicsModelStorage.clearData();
	}

}
