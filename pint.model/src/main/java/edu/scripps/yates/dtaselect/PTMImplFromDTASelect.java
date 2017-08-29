package edu.scripps.yates.dtaselect;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.dtaselectparser.util.DTASelectModification;
import edu.scripps.yates.utilities.proteomicsmodel.PTM;
import edu.scripps.yates.utilities.proteomicsmodel.PTMSite;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.pridemod.ModReader;
import uk.ac.ebi.pridemod.PrideModController;
import uk.ac.ebi.pridemod.slimmod.model.SlimModCollection;
import uk.ac.ebi.pridemod.slimmod.model.SlimModification;

public class PTMImplFromDTASelect implements PTM {
	private final DTASelectModification dtaSelectModification;
	private final SlimModification slimModification;
	private final uk.ac.ebi.pridemod.model.PTM prideModPTM;
	private ModReader modReader;
	private static SlimModCollection preferredModifications;
	private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PTMImplFromDTASelect.class);
	private static Set<String> errorMessages = new THashSet<String>();

	public PTMImplFromDTASelect(DTASelectModification dtaSelectModification) {
		this.dtaSelectModification = dtaSelectModification;
		if (preferredModifications == null) {
			URL url = getClass().getClassLoader().getResource("modification_mappings_dtaSelect.xml");
			if (url != null) {
				preferredModifications = PrideModController.parseSlimModCollection(url);
			} else {
				throw new IllegalStateException("Could not find preferred modification file");
			}
		}
		if (modReader == null) {
			modReader = ModReader.getInstance();
		}
		double delta = dtaSelectModification.getModificationShift();
		double precision = 0.01;

		// first try with the newest pride mod library:

		final List<uk.ac.ebi.pridemod.model.PTM> ptmListByMonoDeltaMass = modReader.getPTMListByMonoDeltaMass(delta);
		if (ptmListByMonoDeltaMass != null && !ptmListByMonoDeltaMass.isEmpty()) {
			prideModPTM = ptmListByMonoDeltaMass.get(0);
			slimModification = null;
		} else {
			prideModPTM = null;
			// try with the old pride mod library:

			// map by delta
			SlimModCollection filteredMods = preferredModifications.getbyDelta(delta, precision);
			if (!filteredMods.isEmpty()) {
				slimModification = filteredMods.get(0);
			} else {

				final String message = "PTM modification with delta mass=" + delta
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
	public Double getMassShift() {
		return dtaSelectModification.getModificationShift();
	}

	@Override
	public String getName() {
		if (prideModPTM != null) {
			return prideModPTM.getName();
		}
		if (slimModification != null) {
			return slimModification.getShortNamePsiMod();
		}
		return "unknown";
	}

	@Override
	public String getCVId() {
		if (prideModPTM != null) {
			return prideModPTM.getAccession();
		}
		if (slimModification != null) {
			return slimModification.getIdPsiMod();
		}
		return null;
	}

	@Override
	public List<PTMSite> getPTMSites() {
		List<PTMSite> ret = new ArrayList<PTMSite>();
		ret.add(new PTMSiteImplFromDTASelect(dtaSelectModification));
		return ret;
	}

}
