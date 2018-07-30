package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.census.read.CensusChroParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.censuschro.PSMImplFromQuantifiedPSM;
import edu.scripps.yates.dtaselect.PSMImplFromDTASelect;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.dtaselectparser.util.DTASelectPSM;
import edu.scripps.yates.excel.proteindb.importcfg.RemoteFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class PSMsAdapterByRemoteFiles implements edu.scripps.yates.utilities.pattern.Adapter<Map<String, PSM>> {
	private final static Logger log = Logger.getLogger(PSMsAdapterByRemoteFiles.class);
	private final Condition expCondition;
	private final RemoteFileReader remoteFileReader;
	private final RemoteInfoType remoteInfoCfg;
	private final MSRun msrun;

	public PSMsAdapterByRemoteFiles(RemoteInfoType remoteInfoCfg, RemoteFileReader remoteFileReader,
			Condition expCondition, MSRun msrun) {
		this.remoteInfoCfg = remoteInfoCfg;
		this.expCondition = expCondition;
		this.remoteFileReader = remoteFileReader;
		this.msrun = msrun;
	}

	@Override
	public Map<String, PSM> adapt() {
		return getPSMsFromRemoteFileReader();
	}

	private Map<String, PSM> getPSMsFromRemoteFileReader() {
		Map<String, PSM> retMap = new THashMap<String, PSM>();
		// if there is identification data
		if (remoteInfoCfg != null) {

			final List<FileReferenceType> fileRefs = remoteInfoCfg.getFileRef();
			Set<String> fileRefSet = new THashSet<String>();
			String fileRefString = "";
			for (FileReferenceType fileReference : fileRefs) {
				final String fileRef = fileReference.getFileRef();
				log.info("Reading identification data from remote files at: " + fileRef);
				fileRefString += fileRef + ",";
				fileRefSet.add(fileRef);
			}
			try {
				final DTASelectParser dtaSelectFilterParser = remoteFileReader.getDTASelectFilterParser(fileRefSet);
				final CensusChroParser censusChroParser = remoteFileReader.getCensusChroParser(fileRefSet);
				if (dtaSelectFilterParser != null) {
					final Map<String, DTASelectPSM> dtaSelectPSMs = dtaSelectFilterParser.getDTASelectPSMsByPSMID();
					for (DTASelectPSM dtaSelectPSM : dtaSelectPSMs.values()) {
						final PSMImplFromDTASelect psm = new PSMImplFromDTASelect(dtaSelectPSM, msrun);
						psm.addCondition(expCondition);
						StaticProteomicsModelStorage.addPSM(psm, msrun, expCondition.getName());
						retMap.put(psm.getIdentifier(), psm);
					}
				} else if (censusChroParser != null) {
					if (remoteInfoCfg.getDiscardDecoys() != null)
						censusChroParser.setDecoyPattern(remoteInfoCfg.getDiscardDecoys());
					final Map<String, QuantifiedPSMInterface> quantifiedPSMs = censusChroParser.getPSMMap();
					for (QuantifiedPSMInterface quantifiedPSM : quantifiedPSMs.values()) {
						final PSMImplFromQuantifiedPSM psm = new PSMImplFromQuantifiedPSM(quantifiedPSM, msrun);
						psm.addCondition(expCondition);
						StaticProteomicsModelStorage.addPSM(psm, msrun, expCondition.getName());
						retMap.put(psm.getIdentifier(), psm);
					}
				} else {
					throw new IllegalArgumentException("Remote file '" + fileRefString
							+ "' is not reachable. Review the connection settings to server");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return retMap;
	}

}
