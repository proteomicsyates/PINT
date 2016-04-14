package edu.scripps.yates.utilities.genebank;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

/**
 * Utility class for using gbk (GeneBank) files
 *
 * @author Salva
 *
 */
public class GenebankToFasta {
	/**
	 *
	 * It creates a FASTA file from a GeneBank file, getting the translated AA
	 * sequence from the "translation" elements in the file.
	 *
	 * @param gbkFile
	 * @return
	 */
	public static File convertoToFasta(File gbkFile) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			File fastaFile = new File(gbkFile.getParent() + File.separator
					+ FilenameUtils.getBaseName(gbkFile.getAbsolutePath()) + ".fasta");
			br = new BufferedReader(new FileReader(gbkFile));
			bw = new BufferedWriter(new FileWriter(fastaFile));
			String line = null;
			boolean isTranslation = false;
			StringBuilder sequence = new StringBuilder();
			StringBuilder header = new StringBuilder();
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("/translation=\"")) {
					if (!"".equals(sequence.toString())) {
						bw.write(">" + header.toString());
						bw.newLine();
						bw.write(sequence.toString());
						bw.newLine();
						sequence = new StringBuilder();
						header = new StringBuilder();
					}
					isTranslation = true;
					line = line.substring(line.indexOf("\"") + 1);
				} else if (line.startsWith("/gene=\"")) {
					if (!"".equals(header.toString())) {
						header.append(" ");
					}
					String tmp = line.substring(1);
					if (!header.toString().contains(tmp)) {
						header.append(line.substring(1));
					}
				} else if (line.startsWith("/locus_tag=\"")) {
					if (!"".equals(header.toString())) {
						header.append(" ");
					}
					String tmp = line.substring(1);
					if (!header.toString().contains(tmp)) {
						header.append(line.substring(1));
					}
				} else if (line.startsWith("/description=\"")) {
					if (!"".equals(header.toString())) {
						header.append(" ");
					}
					String tmp = line.substring(1);
					if (!header.toString().contains(tmp)) {
						header.append(line.substring(1));
					}
				} else if (line.startsWith("/protein_id=\"")) {
					if (!"".equals(header.toString())) {
						header.append(" ");
					}
					String tmp = line.substring(1);
					if (!header.toString().contains(tmp)) {
						header.append(line.substring(1));
					}
				}

				if (isTranslation) {
					if (line.endsWith("*\"")) {
						final String substring = line.substring(0, line.indexOf("*"));
						sequence.append(substring);
						isTranslation = false;
					} else {
						sequence.append(line);
					}
				}
			}
			return fastaFile;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
}
