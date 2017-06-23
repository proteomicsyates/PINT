package edu.scripps.yates.server.daemon.tasks;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRemoteRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.access.MySQLSaver;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProteinAccessionAdapter;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.UniprotVersionReleasesDates;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;

/**
 * {@link PintServerDaemonTask} that checks all the accessions of the proteins
 * in the DB and goes to Uniprot to check whether is still the primary accession
 * of that protein. If not, it updates that accession as not primary and saves
 * the new primary one in the DB linked to that protein
 *
 * @author Salva
 *
 */
public class ProteinUniprotAnnotationUpdater extends PintServerDaemonTask {

	private final String projectFilesPath;

	/**
	 * {@link PintServerDaemonTask} that checks all the accessions of the
	 * proteins in the DB and goes to Uniprot to check whether is still the
	 * primary accession of that protein. If not, it updates that accession as
	 * not primary and saves the new primary one in the DB linked to that
	 * protein
	 *
	 * @param servletContext
	 */
	public ProteinUniprotAnnotationUpdater(ServletContext servletContext) {
		super(servletContext);
		log.info("Creating PINT ServerDaemonTask: " + this.getClass().getCanonicalName());
		projectFilesPath = FileManager.getProjectFilesPath(servletContext);
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.beginGoodTransaction();
			log.info("Starting " + getTaskName());
			// get the uniprot accessions
			final List<ProteinAccession> proteinAccessions = PreparedQueries
					.getProteinAccession(AccessionType.UNIPROT.name());
			final Set<String> accessions = new HashSet<String>();

			for (ProteinAccession proteinAccession : proteinAccessions) {
				if (proteinAccession.getAccessionType().equals(AccessionType.UNIPROT.name()))
					accessions.add(proteinAccession.getAccession());
			}

			log.info(accessions.size() + " proteins to be annotated with the latest version of Uniprot");

			// if index is used
			if (urs.isUseIndex()) {
				// check if the current version is new, and if so, save the
				// version
				// with the today date
				final String currentUniprotVersion = UniprotProteinRemoteRetriever.getCurrentUniprotRemoteVersion();
				File uniprotIndexFile = new File(
						urs.getUniprotReleasesFolder().getAbsolutePath() + File.separator + currentUniprotVersion);
				if (!uniprotIndexFile.exists()) {
					try {
						log.info("New version of UNIPROT is detected today: " + currentUniprotVersion);
						UniprotVersionReleasesDates.getInstance(projectFilesPath).saveNewVersion(currentUniprotVersion,
								new Date());
					} catch (IOException e) {
						e.printStackTrace();
						log.error(e);
					}
				}

				// syncronizeUniprotVersionsAndProjectsInDB();
				// fixGeneNames();
			}
			UniprotProteinRetriever uplr = new UniprotProteinRetriever(null, urs.getUniprotReleasesFolder(),
					urs.isUseIndex());

			final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = uplr
					.getAnnotatedProteins(accessions);
			log.info(annotatedProteins.size() + " annotated proteins for current Uniprot version");

			MySQLSaver saver = new MySQLSaver();
			for (ProteinAccession proteinAcc : proteinAccessions) {
				if (proteinAcc.getAccession().contains("-")) {
					// log.info("Setting protein accession '" +
					// proteinAcc.getAccession() + "' to not primary");
					// proteinAcc.setIsPrimary(false);
					// TODO change description to isoform X of protein Y
				}
				if (annotatedProteins.containsKey(proteinAcc.getAccession())) {
					final Protein protein = annotatedProteins.get(proteinAcc.getAccession());

					if (proteinAcc.isIsPrimary() && protein != null) {

						// check if that is still primary in Uniprot
						final Accession primaryAccession = protein.getPrimaryAccession();
						if (!proteinAcc.getAccession().equals(primaryAccession.getAccession())) {
							proteinAcc.setIsPrimary(false);
							log.info("Setting protein accession '" + proteinAcc.getAccession() + "' to not primary");
							ContextualSessionHandler.saveOrUpdate(proteinAcc);
							// check if the protein linked to that
							// proteinAccession contains the actual primary
							// accession
							final Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> proteins = proteinAcc
									.getProteins();
							for (edu.scripps.yates.proteindb.persistence.mysql.Protein protein2 : proteins) {
								boolean found = false;
								final Set<ProteinAccession> proteinAccessions2 = protein2.getProteinAccessions();
								for (ProteinAccession proteinAccession2 : proteinAccessions2) {
									if (proteinAccession2.getAccession().equals(primaryAccession.getAccession())) {
										found = true;
									}
								}
								if (!found) {
									log.info("Setting primary accession '" + primaryAccession.getAccession()
											+ "' to protein id: " + protein2.getId());
									// insert new primary accession to that
									// protein
									ProteinAccession newProteinAcc = new ProteinAccessionAdapter(primaryAccession, true)
											.adapt();
									newProteinAcc.getProteins().add(protein2);
									saver.saveProteinAccession(newProteinAcc, protein2);
								}
							}

						}
					}
				}
			}
			ContextualSessionHandler.finishGoodTransaction();

		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.getSession().getTransaction().rollback();
		} finally {
			ContextualSessionHandler.closeSession();
		}
	}

	@Override
	public boolean justRunOnce() {
		return false;
	}
}
