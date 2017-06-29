package edu.scripps.yates.server.daemon.tasks;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.hibernate.ScrollableResults;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.utilities.model.enums.AccessionType;

/**
 * {@link PintServerDaemonTask} that checks all proteins in the DB and checks
 * whether they have more than one primary accession or not. In case of having
 * it:<br>
 * <ul>
 * <li>in case of being IPI accessions, they are translated to the uniprot
 * one.</li>
 * <li>in case of being Uniprot Accessions, only the true primary accession is
 * set as primary according to the lattest version of Uniprot</li>
 * </ul>
 *
 * @author Salva
 *
 */
public class ProteinAccessionsUpdaterScroll extends PintServerDaemonTask {

	/**
	 * {@link PintServerDaemonTask} that checks all proteins in the DB and
	 * checks whether they have more than one primary accession or not. In case
	 * of having it:<br>
	 * <ul>
	 * <li>in case of being IPI accessions, they are translated to the uniprot
	 * one.</li>
	 * <li>in case of being Uniprot Accessions, only the true primary accession
	 * is set as primary according to the lattest version of Uniprot</li>
	 * </ul>
	 * 
	 * @param servletContext
	 */
	public ProteinAccessionsUpdaterScroll(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		try {
			UniprotProteinLocalRetriever ulr = new UniprotProteinLocalRetriever(urs.getUniprotReleasesFolder(), true);
			ContextualSessionHandler.getSession().beginTransaction();
			log.info("Starting " + getTaskName());

			ScrollableResults proteins = ContextualSessionHandler
					.retrieveReadOnlyIterator(edu.scripps.yates.proteindb.persistence.mysql.Protein.class);
			log.info("Scroll adquired for proteins in the DB");
			int totalAccCounter = 0;
			int chunkSize = 200;
			int accCounter = 0;
			Set<Protein> proteinSet = new HashSet<Protein>();
			while (proteins.next()) {
				totalAccCounter++;
				accCounter++;
				if (totalAccCounter % 100 == 0) {
					log.info(totalAccCounter + " proteins analyzed in " + this.getClass().getSimpleName());
				}
				edu.scripps.yates.proteindb.persistence.mysql.Protein p = (Protein) proteins.get()[0];
				proteinSet.add(p);
				if (accCounter < chunkSize) {
					continue;
				} else {
					try {
						ContextualSessionHandler.flush();
						Set<String> accs = new HashSet<String>();
						for (Protein p2 : proteinSet) {
							Set<ProteinAccession> paccs = p2.getProteinAccessions();
							for (ProteinAccession proteinAccession : paccs) {
								if (proteinAccession.getAccessionType().equals(AccessionType.UNIPROT.name())) {
									accs.add(proteinAccession.getAccession());
								}
							}
						}

						ulr.getAnnotatedProteins(null, accs);
						for (Protein protein : proteinSet) {
							final Set<ProteinAccession> proteinAccessions = protein.getProteinAccessions();
							Set<ProteinAccession> primaryAccs = new HashSet<ProteinAccession>();
							Set<ProteinAccession> primaryUniprotAccs = new HashSet<ProteinAccession>();
							ProteinAccession ipiAcc = null;
							Set<String> uniprotACCs = new HashSet<String>();
							for (ProteinAccession proteinAccession : proteinAccessions) {
								if (proteinAccession.isIsPrimary()) {
									primaryAccs.add(proteinAccession);
									if (proteinAccession.getAccessionType().equals(AccessionType.IPI.name())) {
										ipiAcc = proteinAccession;
									} else if (proteinAccession.getAccessionType()
											.equals(AccessionType.UNIPROT.name())) {
										primaryUniprotAccs.add(proteinAccession);
									}
								}

								if (proteinAccession.getAccessionType().equals(AccessionType.UNIPROT.name())) {
									uniprotACCs.add(proteinAccession.getAccession());
								}
							}
							if (primaryAccs.isEmpty()) {
								log.info("Protein with ID: " + protein.getId() + " has no primary IDs");
								if (!proteinAccessions.isEmpty()) {
									ProteinAccession acc = proteinAccessions.iterator().next();
									log.info("Setting primary ID to " + acc.getAccession());
									acc.setIsPrimary(true);
									ContextualSessionHandler.saveOrUpdate(acc);
									log.info(acc.getAccession() + " set to primary ID");
								}
							}
							if (primaryAccs.size() > 1) {
								log.info(protein.getId() + " contains more than one primary acc in the DB");
								if (ipiAcc != null) {
									log.info("It contains also a IPI acc");
									final Map<String, Entry> annotatedProteins = ulr.getAnnotatedProteins(null,
											uniprotACCs);
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
										log.info(
												"Contains both swissprot and trembl as primary. Lets keep the swissprot");
										hasTrembl.setIsPrimary(false);
										log.info("Setting " + hasTrembl.getAccession() + " as not primary");
										ContextualSessionHandler.saveOrUpdate(hasTrembl);
									}
									if ((hasSwissProt || hasTrembl != null) && ipiAcc != null && ipiAcc.isIsPrimary()) {
										log.info("both Uniprot and IPI as primary. Lets keep the swissprot");
										ipiAcc.setIsPrimary(false);
										log.info("Setting " + ipiAcc.getAccession() + " as not primary");
										ContextualSessionHandler.saveOrUpdate(ipiAcc);
									}
								} else {
									if (primaryUniprotAccs.size() > 1) {
										log.info("there is more than one uniprot acc as primary.");
										final Map<String, Entry> annotatedProteins = ulr.getAnnotatedProteins(null,
												uniprotACCs);
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
															+ " as not primary being the primary: "
															+ goodAcc.getAccession());
													ContextualSessionHandler.saveOrUpdate(proteinAccession2);
												}
											} else {
												String tmp = "";
												for (ProteinAccession proteinAccession : primaryUniprotAccs) {
													tmp += proteinAccession.getAccession() + ",";
												}
												log.info(
														"Protein with more than one valid uniprot primary acc: " + tmp);

											}
										} else {
											log.info("Protein with obsolete accession but not valid found");
											for (ProteinAccession proteinAccession : primaryUniprotAccs) {
												log.info(proteinAccession.getAccession());
											}

										}

									}
								}
								// remove protein from session to allow it to be
								// removed
								// by
								// GC
								ContextualSessionHandler.getSession().evict(protein);
								// remove protein from cache for the same reason
								ContextualSessionHandler.getSessionFactory(null, null, null).getCache()
										.evictEntity(Protein.class, protein.getId());
							}
						}
					} finally {
						accCounter = 0;
						proteinSet.clear();
					}
				}
			}

			log.info("Committing transaction...");
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
