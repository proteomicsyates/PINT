package edu.scripps.yates.annotations.uniprot.proteoform.xml;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotPTMCVReader;
import edu.scripps.yates.annotations.uniprot.UniprotPTMCVTerm;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformUtil;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotPTM;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.pattern.Adapter;

public class UniprotPTMAdapterFromCrosslinkFeature implements Adapter<List<UniprotPTM>> {
	private final static Logger log = Logger.getLogger(UniprotPTMAdapterFromCrosslinkFeature.class);
	private final FeatureType feature;

	public UniprotPTMAdapterFromCrosslinkFeature(FeatureType feature) {
		this.feature = feature;
	}

	@Override
	public List<UniprotPTM> adapt() {
		List<UniprotPTM> ret = new ArrayList<UniprotPTM>();
		String locationString = ProteoformUtil.getLocationString(feature);
		String modificationName = ProteoformUtil.getDescription(feature, null);
		// sometimes it has an additional description after the name and a
		// semicolon as
		// "Phosphotyrosine; by FYN"
		if (modificationName.contains(";")) {
			modificationName = modificationName.split(";")[0];
		}

		UniprotPTMCVTerm ptmCVTerm = UniprotPTMCVReader.getInstance().getPtmsByIDStart(modificationName);
		if (ptmCVTerm != null) {
			try {

				int positionInProtein = Integer.valueOf(locationString);

				UniprotPTM ptm = new UniprotPTM(positionInProtein, ptmCVTerm);
				ret.add(ptm);

			} catch (NumberFormatException e) {
				// it is like [974-977]
				if (locationString.contains("-")) {
					locationString = locationString.replace("[", "").replace("]", "");
					String[] split = locationString.split("-");
					try {
						int pos1 = Integer.valueOf(split[0]);
						UniprotPTM ptm = new UniprotPTM(pos1, ptmCVTerm);
						ret.add(ptm);
						int pos2 = Integer.valueOf(split[1]);
						UniprotPTM ptm2 = new UniprotPTM(pos2, ptmCVTerm);
						ret.add(ptm2);
					} catch (NumberFormatException e2) {
						// do nothing
					}
				}
			}
		} else {
			log.warn("PTM not recognized from PTMList.txt from uniprot: " + modificationName);
		}
		return ret;
	}

}
