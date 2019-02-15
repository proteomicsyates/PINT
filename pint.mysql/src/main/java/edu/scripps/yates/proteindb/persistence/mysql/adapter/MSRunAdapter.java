package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import gnu.trove.map.hash.THashMap;

public class MSRunAdapter implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.MsRun>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4629687015292318476L;
	private static Logger log = Logger.getLogger(MSRunAdapter.class);
	private final edu.scripps.yates.utilities.proteomicsmodel.MSRun msRun;
	private final Project project;
	private final static Map<String, MsRun> map = new THashMap<String, MsRun>();

	public MSRunAdapter(edu.scripps.yates.utilities.proteomicsmodel.MSRun msRun2, Project project) {
		if (msRun2 == null || msRun2.getRunId() == null)
			throw new IllegalArgumentException("MSRun is null!");
		msRun = msRun2;
		this.project = project;
	}

	@Override
	public MsRun adapt() {

		if (map.containsKey(msRun.getRunId())) {
			return map.get(msRun.getRunId());
		}
		final MsRun ret = new MsRun();
		map.put(msRun.getRunId(), ret);
		ret.setDate(msRun.getDate());
		ret.setPath(msRun.getPath());
		if (ret.getPath() == null) {
			ret.setPath("N/A");
		}
		ret.setRunId(msRun.getRunId());
		ret.setProject(project);
		return ret;
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
