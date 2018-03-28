package edu.scripps.yates.annotations.uniprot.variant.model;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotPTMCVReader;
import edu.scripps.yates.annotations.uniprot.UniprotPTMCVTerm;
import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.HasFeatureDescription;

public class UniprotPTMAdapterFromFeature implements Adapter<UniprotPTM> {
	private final static Logger log = Logger.getLogger(UniprotPTMAdapterFromFeature.class);
	private final HasFeatureDescription feature;

	public UniprotPTMAdapterFromFeature(HasFeatureDescription feature) {
		this.feature = feature;
	}

	@Override
	public UniprotPTM adapt() {
		int positionInProtein = Integer.valueOf(VariantsUtil.getLocationString(feature));
		String modificationName = VariantsUtil.getDescription(feature);
		// sometimes it has an additional description after the name and a
		// semicolon as
		// "Phosphotyrosine; by FYN"
		if (modificationName.contains(";")) {
			modificationName = modificationName.split(";")[0];
		}
		UniprotPTMCVTerm ptmCVTerm = UniprotPTMCVReader.getInstance().getPtmsByID(modificationName);
		if (ptmCVTerm != null) {
			UniprotPTM ret = new UniprotPTM(positionInProtein, ptmCVTerm);
			return ret;
		}
		log.warn("PTM not recognized from PTMList.txt from uniprot: " + modificationName);
		return null;
	}

}
