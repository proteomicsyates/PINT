package edu.scripps.yates.server.projectCreator.adapter.excel2bean;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FastaDisgestionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;

public class FastaDigestionBeanAdapter implements Adapter<FastaDigestionBean> {
	private final FastaDisgestionType fastaDigestion;

	public FastaDigestionBeanAdapter(FastaDisgestionType fastaDigestion) {
		this.fastaDigestion = fastaDigestion;
	}

	@Override
	public FastaDigestionBean adapt() {
		FastaDigestionBean ret = new FastaDigestionBean();
		ret.setCleavageAAs(fastaDigestion.getCleavageAAs());
		ret.setEnzymeNoCutResidues(fastaDigestion.getEnzymeNoCutResidues());
		ret.setEnzymeOffset(fastaDigestion.getEnzymeOffset());
		ret.setIsH2OPlusProtonAdded(fastaDigestion.isIsH2OPlusProtonAdded());
		ret.setMisscleavages(fastaDigestion.getMisscleavages());
		return ret;
	}

}
