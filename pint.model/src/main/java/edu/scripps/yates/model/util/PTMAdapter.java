package edu.scripps.yates.model.util;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.interfaces.Adapter;

import uk.ac.ebi.pridemod.PrideModController;
import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;
import uk.ac.ebi.pridemod.slimmod.model.SlimModification;
import edu.scripps.yates.utilities.model.factories.PTMEx;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.Score;

public class PTMAdapter implements Adapter<PTM> {
	private final double massShift;
	private final int position;
	private final SlimModification slimModification;
	private final String aa;
	private final Score score;
	private static final Set<String> errorMessages = new HashSet<String>();
	private static SlimModCollection preferredModifications;
	private final static Logger log = Logger.getLogger(PTMAdapter.class);

	public PTMAdapter(double massShift, String aa, int position, Score score) {
		this.massShift = massShift;
		this.position = position;
		this.aa = aa;
		this.score = score;
		if (PTMAdapter.preferredModifications == null) {
			URL url = getClass().getClassLoader().getResource(
					"modification_mappings.xml");
			if (url != null) {
				preferredModifications = PrideModController
						.parseSlimModCollection(url);
			} else {
				throw new IllegalStateException(
						"Could not find preferred modification file");
			}
		}

		double precision = 0.01;
		// map by delta
		SlimModCollection filteredMods = preferredModifications.getbyDelta(
				massShift, precision);
		if (filteredMods != null && !filteredMods.isEmpty()) {
			slimModification = filteredMods.get(0);
		} else {

			final String message = "PTM modification with delta mass="
					+ massShift
					+ " is not recognized in the system. Please, contact system administrator in order to add it as a supported PTM in the system.";
			if (!errorMessages.contains(message)) {
				log.warn(message);
				errorMessages.add(message);
			}
			slimModification = null;
		}
	}

	public PTMAdapter(double massShift, String aa, int position) {
		this(massShift, aa, position, null);
	}

	@Override
	public PTM adapt() {
		String name = "unknown";
		if (slimModification != null) {
			name = slimModification.getShortNamePsiMod();
		}
		PTMEx ptm = new PTMEx(name, massShift);
		if (slimModification != null) {
			ptm.setCvId(slimModification.getIdPsiMod());
		}
		ptm.addPtmSite(new PTMSiteAdapter(aa, position, score).adapt());
		return ptm;
	}

}
