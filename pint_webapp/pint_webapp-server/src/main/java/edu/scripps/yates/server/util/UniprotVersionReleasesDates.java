package edu.scripps.yates.server.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.shared.model.ProjectBean;
import gnu.trove.map.hash.THashMap;

/**
 * This class will be able to register every new Uniprot release with a vertain
 * time point. Then, it will be able to tell if a certain project that was saved
 * in the database at certain date was annotated with which versions of Uniprot
 * since then.
 *
 * @author Salva
 *
 */
public class UniprotVersionReleasesDates {
	private static final Logger log = Logger.getLogger(UniprotVersionReleasesDates.class);
	private static UniprotVersionReleasesDates instance;
	private static String projectFilesPath;
	private final DateFormat df = new SimpleDateFormat("yyyy MM dd", Locale.US);

	private UniprotVersionReleasesDates(String projectFilesPath) {
		UniprotVersionReleasesDates.projectFilesPath = projectFilesPath;

	}

	public static UniprotVersionReleasesDates getInstance(String projectFilesPath) {
		if (instance == null || !UniprotVersionReleasesDates.projectFilesPath.equals(projectFilesPath))
			instance = new UniprotVersionReleasesDates(projectFilesPath);
		return instance;
	}

	public void saveNewVersion(String currentUniprotVersion, Date date) throws IOException {
		// check that that version is actually new
		if (getUniprotReleaseDatesByVersions().containsKey(currentUniprotVersion)) {
			log.info("Uniprot version " + currentUniprotVersion + " was already registered with date: "
					+ df.format(getUniprotReleaseDatesByVersions().get(currentUniprotVersion)));
			return;
		}
		final File uniprotReleasesDatesFile = getUniprotReleasesDatesFile();
		FileOutputStream fos = new FileOutputStream(uniprotReleasesDatesFile, true);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		try {
			bw.write(currentUniprotVersion + "\t" + df.format(date));
			bw.newLine();

		} finally {
			bw.close();
			log.info("Uniprot version " + currentUniprotVersion + " registered with date: "
					+ df.format(getUniprotReleaseDatesByVersions().get(currentUniprotVersion)));
		}
	}

	private File getUniprotReleasesDatesFile() {
		return new File(FileManager.getUniprotReleasesFolder() + File.separator
				+ UniprotProteinLocalRetriever.UNIPROT_RELEASES_DATES_FILE_NAME);
	}

	/**
	 * Gets a map of uniprot versions with its associated release date times
	 *
	 * @return
	 */
	public Map<String, Date> getUniprotReleaseDatesByVersions() {
		Map<String, Date> ret = new THashMap<String, Date>();
		final File uniprotReleasesDatesFile = getUniprotReleasesDatesFile();
		try {
			List<String> lines = Files.readAllLines(Paths.get(uniprotReleasesDatesFile.getAbsolutePath()),
					Charset.forName("UTF-8"));
			for (String line : lines) {
				if (line.contains("\t")) {
					final String[] split = line.split("\t");
					String version = split[0];
					Date releaseDate = df.parse(split[1]);
					ret.put(version, releaseDate);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * Gets all the uniprot versions that were released after the uploaded date
	 * of the project
	 *
	 * @param project
	 * @return
	 */
	public List<String> getAvailableUniprotVersions(ProjectBean project) {
		List<String> ret = new ArrayList<String>();
		final Date uploadedDate = project.getUploadedDate();
		final Map<String, Date> uniprotReleaseDatesByVersions = getUniprotReleaseDatesByVersions();
		for (String uniprotVersion : uniprotReleaseDatesByVersions.keySet()) {
			// if uploadedDate is before the uniprot version release
			if (uploadedDate.compareTo(uniprotReleaseDatesByVersions.get(uniprotVersion)) < 0) {
				log.info("Upload date '" + df.format(uploadedDate) + "' is before the uniprot release date '"
						+ df.format(uniprotReleaseDatesByVersions.get(uniprotVersion) + "'"));
				ret.add(uniprotVersion);
			}
		}
		return ret;
	}
}
