package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.util.Map;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.utilities.model.factories.MSRunEx;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import gnu.trove.map.hash.THashMap;

public class MSRunAdapter implements edu.scripps.yates.utilities.pattern.Adapter<MSRun> {
	private static Map<String, MSRun> map = new THashMap<String, MSRun>();
	private final MsRunType msRunCfg;
	private final Project project;

	public MSRunAdapter(MsRunType msRunCfg, Project project) {
		this.msRunCfg = msRunCfg;
		this.project = project;
	}

	public MSRunAdapter(MsRunType msRunCfg) {
		this.msRunCfg = msRunCfg;
		project = null;
	}

	@Override
	public MSRun adapt() {
		if (map.containsKey(msRunCfg.getId())) {
			final MSRun msRun = map.get(msRunCfg.getId());
			if (project != null && msRun instanceof MSRunEx) {
				((MSRunEx) msRun).setProject(project);
			}
			if (msRun.getProject() == null)
				System.out.println("sdf");
			return msRun;
		}
		MSRunEx msRun = new MSRunEx(msRunCfg.getId(), msRunCfg.getPath());
		if (msRunCfg.getDate() != null)
			msRun.setDate(msRunCfg.getDate().toGregorianCalendar().getTime());

		// if (msRunCfg.getFastaFileRef() != null) {
		// msRun.setEnzymeNoCutResidues(msRunCfg.getEnzymeNocutResidues());
		// msRun.setEnzymeResidues(msRunCfg.getEnzymeResidues());
		// msRun.setFastaFile(remoteFileReader.getFastaFile(msRunCfg
		// .getFastaFileRef()));
		// }
		msRun.setProject(project);
		map.put(msRunCfg.getId(), msRun);
		return msRun;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
