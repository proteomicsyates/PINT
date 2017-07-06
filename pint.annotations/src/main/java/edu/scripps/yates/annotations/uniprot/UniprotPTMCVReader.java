package edu.scripps.yates.annotations.uniprot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import edu.scripps.yates.annotations.util.PropertiesUtil;
import gnu.trove.map.hash.THashMap;

public class UniprotPTMCVReader {
	private static UniprotPTMCVReader instance;
	private final Map<String, UniprotPTMCVTerm> ptmsByIDMap = new THashMap<String, UniprotPTMCVTerm>();
	private final Logger log = Logger.getLogger(UniprotPTMCVReader.class);

	public static UniprotPTMCVReader getInstance() {
		if (instance == null) {
			instance = new UniprotPTMCVReader();
		}
		return instance;
	}

	/**
	 * @return the ptmsByID
	 */
	public Map<String, UniprotPTMCVTerm> getPtmsByIDMap() {
		return ptmsByIDMap;
	}

	public UniprotPTMCVTerm getPtmsByID(String id) {
		return ptmsByIDMap.get(id);
	}

	private UniprotPTMCVReader() {

		String line = null;
		InputStream fis = null;
		BufferedReader br = null;
		try {
			final String ptmFileName = PropertiesUtil.getInstance(PropertiesUtil.UNIPROT_PROPERTIES_FILE)
					.getPropertyValue(PropertiesUtil.UNIPROT_PTM_CV_LIST_FILE_PROP);
			log.info("Reading PTM file: " + ptmFileName);
			fis = new ClassPathResource(ptmFileName).getInputStream();
			InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
			UniprotPTMCVTerm term = null;
			while ((line = br.readLine()) != null) {
				final UniprotCVTermCode startingCode = getStartingCode(line);
				if (startingCode != null) {
					if (UniprotCVTermCode.ID == startingCode) {
						term = new UniprotPTMCVTerm();
					}
					final String[] split = line.split(UniprotCVTermCode.CODE_VALUE_SPLIT_VALUE);
					term.addValue(startingCode, split[1]);
				} else if (line.startsWith(UniprotCVTermCode.ENTRY_END)) {
					ptmsByIDMap.put(term.getSingleValue(UniprotCVTermCode.ID), term);
				}
			}
			log.info(ptmsByIDMap.size() + " PTM CV terms readed from Uniprot PTM mappring file " + ptmFileName);
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	private UniprotCVTermCode getStartingCode(String line) {
		for (UniprotCVTermCode code : UniprotCVTermCode.values()) {
			if (line.startsWith(code.name())) {
				return code;
			}
		}
		return null;
	}
}
