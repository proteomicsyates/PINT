package edu.scripps.yates.server.daemon.tasks;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.utilities.model.enums.AccessionType;

public class ProteinAccessionsUpdater extends PintServerDaemonTask {

	private final String projectFilesPath;

	public ProteinAccessionsUpdater(ServletContext servletContext) {
		super(servletContext);
		projectFilesPath = FileManager.getProjectFilesPath(servletContext);
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.getSession().beginTransaction();
			log.info("Starting " + getTaskName());

			List<edu.scripps.yates.proteindb.persistence.mysql.Protein> proteins = ContextualSessionHandler
					.retrieveList(edu.scripps.yates.proteindb.persistence.mysql.Protein.class);

			log.debug(proteins.size() + " proteins in the DB");
			UniprotProteinLocalRetriever ulr = new UniprotProteinLocalRetriever(urs.getUniprotReleasesFolder(), true);

			for (edu.scripps.yates.proteindb.persistence.mysql.Protein protein : proteins) {
				final Set<ProteinAccession> proteinAccessions = protein.getProteinAccessions();
				Set<ProteinAccession> primaryAccs = new HashSet<ProteinAccession>();
				Set<ProteinAccession> primaryUniprotAccs = new HashSet<ProteinAccession>();
				ProteinAccession ipiAcc = null;
				Set<String> accs = new HashSet<String>();
				for (ProteinAccession proteinAccession : proteinAccessions) {
					if (proteinAccession.isIsPrimary()) {
						primaryAccs.add(proteinAccession);
						if (proteinAccession.getAccessionType().equals(AccessionType.IPI.name())) {
							ipiAcc = proteinAccession;
						} else if (proteinAccession.getAccessionType().equals(AccessionType.UNIPROT.name())) {
							primaryUniprotAccs.add(proteinAccession);
						}
					}

					if (proteinAccession.getAccessionType().equals(AccessionType.UNIPROT.name())) {
						accs.add(proteinAccession.getAccession());
					}
				}
				if (primaryAccs.size() > 1) {
					log.debug(protein.getId() + " contains more than one primary acc in the DB");
					if (ipiAcc != null) {
						log.debug("It contains also a IPI acc");
						final Map<String, Entry> annotatedProteins = ulr.getAnnotatedProteins(null, accs);
						boolean hasSwissProt = false;
						ProteinAccession hasTrembl = null;
						for (ProteinAccession primaryAcc : primaryAccs) {
							if (annotatedProteins.containsKey(primaryAcc.getAccession())) {
								final Entry entry = annotatedProteins.get(primaryAcc.getAccession());
								if ("Swiss-Prot".equalsIgnoreCase(entry.getDataset())) {
									hasSwissProt = true;
								} else if ("TrEMBL".equalsIgnoreCase(entry.getDataset())) {
									hasTrembl = primaryAcc;
								}
							}
						}
						if (hasSwissProt && hasTrembl != null) {
							log.debug("Contains both swissprot and trembl as primary. Lets keep the swissprot");
							hasTrembl.setIsPrimary(false);
							log.debug("Setting " + hasTrembl.getAccession() + " as not primary");
							ContextualSessionHandler.saveOrUpdate(hasTrembl);
						}
						if ((hasSwissProt || hasTrembl != null) && ipiAcc != null && ipiAcc.isIsPrimary()) {
							log.debug("both Uniprot and IPI as primary. Lets keep the swissprot");
							ipiAcc.setIsPrimary(false);
							log.debug("Setting " + ipiAcc.getAccession() + " as not primary");
							ContextualSessionHandler.saveOrUpdate(ipiAcc);
						}
					} else {
						if (primaryUniprotAccs.size() > 1) {
							log.debug("there is more than one uniprot acc as primary.");
							final Map<String, Entry> annotatedProteins = ulr.getAnnotatedProteins(null, accs);
							ProteinAccession goodAcc = null;
							Set<ProteinAccession> accsToDowngrade = new HashSet<ProteinAccession>();
							for (ProteinAccession primaryAcc : primaryUniprotAccs) {
								if (annotatedProteins.containsKey(primaryAcc.getAccession())) {
									final Entry entry = annotatedProteins.get(primaryAcc.getAccession());
									if (entry.getName().get(0).contains("obsolete")) {
										accsToDowngrade.add(primaryAcc);
									} else {
										goodAcc = primaryAcc;
									}
								}
							}
							if (goodAcc != null) {
								if (!accsToDowngrade.isEmpty()) {
									for (ProteinAccession proteinAccession2 : accsToDowngrade) {
										proteinAccession2.setIsPrimary(false);
										log.info("Setting obsolete acc " + proteinAccession2.getAccession()
												+ " as not primary being the primary: " + goodAcc.getAccession());
										ContextualSessionHandler.saveOrUpdate(proteinAccession2);
									}
								} else {
									String tmp = "";
									for (ProteinAccession proteinAccession : primaryUniprotAccs) {
										tmp += proteinAccession.getAccession() + ",";
									}
									log.debug("Protein with more than one valid uniprot primary acc: " + tmp);

								}
							} else {
								log.debug("Protein with obsolete accession but not valid found");
								for (ProteinAccession proteinAccession : primaryUniprotAccs) {
									log.debug(proteinAccession.getAccession());
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
			ContextualSessionHandler.getSession().close();
		}
	}

	@Override
	public boolean justRunOnce() {
		return false;
	}
}
