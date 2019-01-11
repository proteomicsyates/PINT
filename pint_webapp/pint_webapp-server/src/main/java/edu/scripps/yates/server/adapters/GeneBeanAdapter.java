package edu.scripps.yates.server.adapters;

import java.util.List;

import edu.scripps.yates.genes.GeneInformation;
import edu.scripps.yates.genes.GeneReader;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.shared.model.GeneBean;

public class GeneBeanAdapter implements Adapter<GeneBean> {
	private final Gene hibGene;
	private final edu.scripps.yates.utilities.proteomicsmodel.Gene modelGene;

	public GeneBeanAdapter(Gene gene) {
		hibGene = gene;
		modelGene = null;
	}

	public GeneBeanAdapter(edu.scripps.yates.utilities.proteomicsmodel.Gene gene) {
		hibGene = null;
		modelGene = gene;
	}

	@Override
	public GeneBean adapt() {
		if (hibGene != null) {
			return getGeneBeanFromHibGene();
		} else {
			return getGeneBeanFromModelGene();
		}
	}

	private GeneBean getGeneBeanFromHibGene() {
		GeneBean ret = new GeneBean();
		ret.setGeneID(hibGene.getGeneId());
		ret.setGeneType(hibGene.getGeneType());
		List<GeneInformation> genes = GeneReader.getInstance().getGenesBySymbol(hibGene.getGeneId(), false);
		// take the first one, since only one should be reported:
		if (!genes.isEmpty()) {
			GeneInformation gene = genes.get(0);
			final int hgncId = gene.getHgncId();
			if (hgncId > 0)
				ret.setHgncNumber(hgncId);
			if (gene.getApprovedName() != null)
				ret.setName(gene.getApprovedName());
		}
		return ret;
	}

	private GeneBean getGeneBeanFromModelGene() {
		GeneBean ret = new GeneBean();
		ret.setGeneID(modelGene.getGeneID());
		ret.setGeneType(modelGene.getGeneType());
		List<GeneInformation> genes = GeneReader.getInstance().getGenesBySymbol(modelGene.getGeneID(), false);
		// take the first one, since only one should be reported:
		if (!genes.isEmpty()) {
			GeneInformation gene = genes.get(0);
			final int hgncId = gene.getHgncId();
			if (hgncId > 0)
				ret.setHgncNumber(hgncId);
			if (gene.getApprovedName() != null)
				ret.setName(gene.getApprovedName());
		}
		return ret;
	}
}
