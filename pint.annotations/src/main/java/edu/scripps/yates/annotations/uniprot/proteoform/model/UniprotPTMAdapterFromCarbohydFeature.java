package edu.scripps.yates.annotations.uniprot.proteoform.model;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotPTMCVReader;
import edu.scripps.yates.annotations.uniprot.UniprotPTMCVTerm;
import edu.scripps.yates.utilities.pattern.Adapter;
import uk.ac.ebi.kraken.interfaces.uniprot.features.CarbohydFeature;

public class UniprotPTMAdapterFromCarbohydFeature implements Adapter<UniprotPTM> {
	private final static Logger log = Logger.getLogger(UniprotPTMAdapterFromCarbohydFeature.class);
	private final CarbohydFeature feature;

	public UniprotPTMAdapterFromCarbohydFeature(CarbohydFeature feature) {
		this.feature = feature;
	}

	@Override
	public UniprotPTM adapt() {
		int positionInProtein = Integer.valueOf(ProteoformUtil.getLocationString(feature));
		String modificationName = "";
		if (feature.getCarbohydLinkType() != null) {
			modificationName = feature.getCarbohydLinkType().getValue();
		}
		if (feature.getLinkedSugar() != null) {
			if (!"".equals(modificationName)) {
				modificationName += " ";
			}
			modificationName += feature.getLinkedSugar().getValue();
		}
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
