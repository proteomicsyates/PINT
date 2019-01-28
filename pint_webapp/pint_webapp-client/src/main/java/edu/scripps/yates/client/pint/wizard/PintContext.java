package edu.scripps.yates.client.pint.wizard;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import edu.scripps.yates.client.ui.wizard.WizardContext;
import edu.scripps.yates.shared.model.projectCreator.excel.PintImportCfgBean;

public class PintContext extends WizardContext {
	private PintImportCfgBean pintImportCfg;
	private Boolean isQuantitative;
	private final String sessionID;
	private Boolean useFasta;
	private final Map<String, Boolean> extractNSAFFromFileByFileID = new HashMap<String, Boolean>();

	public PintContext(String sessionID2) {
		this.sessionID = sessionID2;
	}

	@Override
	public boolean isComplete() {
		// TODO Auto-generated method stub
		GWT.log("iscomplete!!!");
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		GWT.log("resetting!!!");
	}

	public PintImportCfgBean getPintImportConfiguration() {
		if (pintImportCfg == null) {
			pintImportCfg = new PintImportCfgBean();

		}
		return pintImportCfg;
	}

	public Boolean isQuantitative() {
		return isQuantitative;
	}

	public void setQuantitative(Boolean isQuantitative) {
		this.isQuantitative = isQuantitative;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setUseFasta(boolean b) {
		this.useFasta = b;
	}

	public Boolean isUseFasta() {
		return useFasta;
	}

	public void setExtractNSAF(String fileId, boolean extractNSAF) {
		extractNSAFFromFileByFileID.put(fileId, extractNSAF);
	}

	public Boolean isExtractNSAF(String fileID) {
		return extractNSAFFromFileByFileID.get(fileID);
	}
}
