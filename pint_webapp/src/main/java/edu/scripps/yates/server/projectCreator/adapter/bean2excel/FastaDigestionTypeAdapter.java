package edu.scripps.yates.server.projectCreator.adapter.bean2excel;

import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FastaDisgestionType;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.projectCreator.excel.FastaDigestionBean;

public class FastaDigestionTypeAdapter implements Adapter<FastaDisgestionType> {
	private final FastaDigestionBean fastaDigestion;

	public FastaDigestionTypeAdapter(FastaDigestionBean fastaDigestion) {
		this.fastaDigestion = fastaDigestion;
	}

	@Override
	public FastaDisgestionType adapt() {
		FastaDisgestionType ret = new FastaDisgestionType();
		ret.setCleavageAAs(fastaDigestion.getCleavageAAs());
		ret.setEnzymeNoCutResidues(fastaDigestion.getEnzymeNoCutResidues());
		ret.setEnzymeOffset(fastaDigestion.getEnzymeOffset());
		ret.setIsH2OPlusProtonAdded(fastaDigestion.isIsH2OPlusProtonAdded());
		ret.setMisscleavages(fastaDigestion.getMisscleavages());
		return ret;
	}

}
