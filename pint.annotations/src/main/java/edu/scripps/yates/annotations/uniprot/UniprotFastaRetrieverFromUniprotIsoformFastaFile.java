package edu.scripps.yates.annotations.uniprot;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import gnu.trove.map.hash.THashMap;

public class UniprotFastaRetrieverFromUniprotIsoformFastaFile {
	private static final String ISOFORMS_SUBFOLDER_NAME = "isoforms";
	private static Logger log = Logger.getLogger(UniprotFastaRetrieverFromUniprotIsoformFastaFile.class);
	private final File isoformsFolder;
	private Map<String, Entry> fastas;

	public UniprotFastaRetrieverFromUniprotIsoformFastaFile(File uniprotFolder, String uniprotVersion) {
		this.isoformsFolder = new File(uniprotFolder.getAbsolutePath() + File.separator + uniprotVersion
				+ File.separator + ISOFORMS_SUBFOLDER_NAME);
		if (!isoformsFolder.exists()) {
			isoformsFolder.mkdirs();
		}

	}

	public Map<String, Entry> run() {
		if (fastas == null) {
			BufferedOutputStream fos = null;
			try {

				final File isoformsZippedFile = getIsoformsZippedFile();
				final URL url = new URL(
						"https://ftp.expasy.org/databases/uniprot/current_release/knowledgebase/complete/uniprot_sprot_varsplic.fasta.gz");
				if (!isoformsZippedFile.exists() || isoformsZippedFile.length() == 0l) {
					final long t1 = System.currentTimeMillis();
					log.info("Downloading isoform sequences from uniprot at: " + url.toString());
					final InputStream is = url.openStream();
					fos = new BufferedOutputStream(new FileOutputStream(isoformsZippedFile));
					IOUtils.copy(is, fos);
					fos.close();

					final long t2 = System.currentTimeMillis();
					log.debug("File downloaded in " + DatesUtil.getDescriptiveTimeFromMillisecs(t2 - t1));
				}
				final long t2 = System.currentTimeMillis();
				fastas = parseFASTAFile(isoformsZippedFile);

				final long t3 = System.currentTimeMillis();
				log.debug("File processed in " + DatesUtil.getDescriptiveTimeFromMillisecs(t3 - t2));

			} catch (final UnsupportedOperationException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final IOException e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} catch (final Exception e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (final IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return fastas;
	}

	private final Map<String, Entry> parseFASTAFile(final File gzFile) throws IOException {
		final Map<String, Entry> ret = new THashMap<String, Entry>();
		log.info("Parsing " + gzFile.getAbsolutePath());
		final InputStream fileStream = new FileInputStream(gzFile.getAbsolutePath());
		final InputStream gzipStream = new GZIPInputStream(fileStream);
		final Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
		BufferedReader buffered = null;

		try {
			buffered = new BufferedReader(decoder);

			StringBuilder sequence = new StringBuilder();
			String fastaHeader = null;
			String accession = null;
			String line = null;
			while ((line = buffered.readLine()) != null) {

				int numHeaders = 0;

				if (line.startsWith(">")) {
					numHeaders++;
					// check if there was a sequence before and it was the
					// correct one (takeSequence=true)
					if (!"".equals(sequence.toString())) {
						final Entry entry = new UniprotEntryAdapterFromFASTA(accession, fastaHeader,
								sequence.toString()).adapt();
						if (ret.containsKey(accession)) {
							log.info("Really" + line);
						}
						ret.put(accession, entry);
					}
					fastaHeader = line;
					final Accession newAccession = FastaParser.getACC(fastaHeader);
					sequence = new StringBuilder();
					accession = newAccession.getAccession();
				} else {
					sequence.append(line);
				}

			}
			final Entry entry = new UniprotEntryAdapterFromFASTA(accession, fastaHeader, sequence.toString()).adapt();
			ret.put(accession, entry);
			log.info("Parsing " + gzFile.getAbsolutePath() + " yielded " + ret.size() + " sequences");
			return ret;
		} finally {
			if (buffered != null) {
				buffered.close();
			}
		}
	}

	private File getIsoformsZippedFile() {
		return new File(this.isoformsFolder.getAbsolutePath() + File.separator + "uniprot_sprot_varsplic.fasta.gz");
	}

}
