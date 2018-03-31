package edu.scripps.yates.annotations.uniprot.proteoform.xml;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotPTMCVReader;
import edu.scripps.yates.annotations.uniprot.UniprotPTMCVTerm;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotPTM;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.pattern.Adapter;

public class UniprotPTMAdapterFromFeature implements Adapter<UniprotPTM> {
	private final static Logger log = Logger.getLogger(UniprotPTMAdapterFromFeature.class);
	private final FeatureType feature;

	public UniprotPTMAdapterFromFeature(FeatureType feature) {
		this.feature = feature;
	}

	@Override
	public UniprotPTM adapt() {
		try {
			int positionInProtein = Integer.valueOf(ProteoformUtil.getLocationString(feature));

			String modificationName = ProteoformUtil.getDescription(feature);
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
		} catch (NumberFormatException e) {
			// do nothing
		}
		return null;
	}

}
