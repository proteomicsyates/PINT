package edu.scripps.yates.model.util;

import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.proteored.miapeapi.interfaces.Adapter;

import edu.scripps.yates.utilities.model.factories.PTMEx;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.pride.utilities.pridemod.ModReader;
import uk.ac.ebi.pridemod.PrideModController;
import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;
import uk.ac.ebi.pridemod.slimmod.model.SlimModification;

public class PTMAdapter implements Adapter<PTM> {
	private final double massShift;
	private final int position;
	private final SlimModification slimModification;
	private final String aa;
	private final Score score;
	private static final Set<String> errorMessages = new THashSet<String>();
	private static SlimModCollection preferredModifications;
	private final static Logger log = Logger.getLogger(PTMAdapter.class);
	private final uk.ac.ebi.pride.utilities.pridemod.model.PTM prideModPTM;
	private static final String MOD0 = "MOD:00000";

	public PTMAdapter(double massShift, String aa, int position) {
		this(massShift, aa, position, null);
	}

	public PTMAdapter(double massShift, String aa, int position, Score score) {
		this.massShift = massShift;
		this.position = position;
		this.aa = aa;
		this.score = score;
		final List<uk.ac.ebi.pride.utilities.pridemod.model.PTM> ptmListByMonoDeltaMass = ModReader.getInstance()
				.getPTMListByMonoDeltaMass(massShift);
		if (ptmListByMonoDeltaMass != null && !ptmListByMonoDeltaMass.isEmpty()) {
			prideModPTM = ptmListByMonoDeltaMass.get(0);
			slimModification = null;
		} else {
			prideModPTM = null;

			if (PTMAdapter.preferredModifications == null) {
				final URL url = getClass().getClassLoader().getResource("modification_mappings_dtaSelect.xml");
				if (url != null) {
					preferredModifications = PrideModController.parseSlimModCollection(url);
				} else {
					throw new IllegalStateException("Could not find preferred modification file");
				}
			}

			final double precision = 0.01;
			// map by delta
			final SlimModCollection filteredMods = preferredModifications.getbyDelta(massShift, precision);
			if (filteredMods != null && !filteredMods.isEmpty()) {
				slimModification = filteredMods.get(0);
			} else {

				final String message = "PTM modification with delta mass=" + massShift
						+ " is not recognized in the system. Please, contact system administrator in order to add it as a supported PTM in the system.";
				if (!errorMessages.contains(message)) {
					log.warn(message);
					errorMessages.add(message);
				}
				slimModification = null;
			}
		}
	}

	@Override
	public PTM adapt() {
		String name = "unknown";
		if (prideModPTM != null) {
			name = prideModPTM.getName();
		} else if (slimModification != null) {
			name = slimModification.getShortNamePsiMod();
		}
		final PTMEx ptm = new PTMEx(name, massShift);
		if (prideModPTM != null) {
			ptm.setCvId(prideModPTM.getAccession());
		} else if (slimModification != null) {
			final String idPsiMod = slimModification.getIdPsiMod();
			if (!idPsiMod.equals(MOD0)) {
				ptm.setCvId(idPsiMod);
			} // otherwise, keep it as null
		}
		ptm.addPtmSite(new PTMSiteAdapter(aa, position, score).adapt());
		return ptm;
	}

}
