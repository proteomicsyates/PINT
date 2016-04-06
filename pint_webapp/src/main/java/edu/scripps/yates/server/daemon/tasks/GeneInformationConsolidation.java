package edu.scripps.yates.server.daemon.tasks;

import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.server.tasks.RemoteServicesTasks;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;

public class GeneInformationConsolidation extends PintServerDaemonTask {

	public GeneInformationConsolidation(ServletContext servletContext) {
		super(servletContext);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		log.info("Starting " + getTaskName());

		try {
			ContextualSessionHandler.getSession().beginTransaction();
			final Set<ProjectBean> projectBeans = RemoteServicesTasks.getProjectBeans();
			for (ProjectBean projectBean : projectBeans) {
				final Map<String, Set<Protein>> proteins = PreparedQueries
						.getProteinsByProjectCondition(projectBean.getTag(), null);
				for (Set<Protein> proteinSet : proteins.values()) {
					for (Protein proteinFromDB : proteinSet) {
						final ProteinAccession primaryAccession = PersistenceUtils.getPrimaryAccession(proteinFromDB);
						if (primaryAccession.getAccessionType().equalsIgnoreCase(AccessionType.UNIPROT.name())) {
							UniprotProteinRetriever upr = new UniprotProteinRetriever(null,
									urs.getUniprotReleasesFolder(), urs.isUseIndex());
							final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = upr
									.getAnnotatedProtein(primaryAccession.getAccession());
							if (annotatedProteins != null) {
								final edu.scripps.yates.utilities.proteomicsmodel.Protein annotatedProtein = annotatedProteins
										.get(primaryAccession.getAccession());
								if (annotatedProtein == null)
									continue;
								final Set<Gene> genesFromUniprot = annotatedProtein.getGenes();
								final Set<edu.scripps.yates.proteindb.persistence.mysql.Gene> genesFromDB = proteinFromDB
										.getGenes();
								for (Gene geneFromUniprot : genesFromUniprot) {
									boolean found = false;
									for (edu.scripps.yates.proteindb.persistence.mysql.Gene geneFromDB : genesFromDB) {
										if (geneFromDB.getGeneId().equals(geneFromUniprot.getGeneID())) {
											found = true;
											// check for the gene type
											if (geneFromUniprot.getGeneType() != null
													&& !"".equals(geneFromUniprot.getGeneType()) && !geneFromUniprot
															.getGeneType().equals(geneFromDB.getGeneType())) {
												log.info("Updating gene type in DB. Setting geneType="
														+ geneFromUniprot.getGeneType() + " to gene "
														+ geneFromDB.getGeneId() + " DBid=" + geneFromDB.getId());
												geneFromDB.setGeneType(geneFromUniprot.getGeneType());

												ContextualSessionHandler.saveOrUpdate(geneFromDB);

											} else {
												// log.info("Gene in DB is
												// correct");
											}
											break;
										}
									}
									if (!found) {
										log.info("Gene " + geneFromUniprot.getGeneID() + " not found in DB for protein "
												+ primaryAccession.getAccession() + " ProteinDBID="
												+ proteinFromDB.getId());
										edu.scripps.yates.proteindb.persistence.mysql.Gene hibGene = new edu.scripps.yates.proteindb.persistence.mysql.Gene();
										hibGene.setGeneId(geneFromUniprot.getGeneID());
										hibGene.setGeneType(geneFromUniprot.getGeneType());
										hibGene.getProteins().add(proteinFromDB);

										ContextualSessionHandler.saveOrUpdate(hibGene);

									}
								}
							}
						}
					}
				}
			}
			log.info("Commiting transaction");
			ContextualSessionHandler.finishGoodTransaction();
			log.info("Transaction committed");
		} catch (Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.rollbackTransaction();
		} finally {
			ContextualSessionHandler.getSession().close();
		}
	}

	@Override
	public boolean justRunOnce() {
		return true;
	}

}
