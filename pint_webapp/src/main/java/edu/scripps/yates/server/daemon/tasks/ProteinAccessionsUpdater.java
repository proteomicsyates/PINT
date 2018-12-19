package edu.scripps.yates.server.daemon.tasks;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.hibernate.Criteria;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.Projections;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;
import gnu.trove.set.hash.THashSet;

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
public class ProteinAccessionsUpdater extends PintServerDaemonTask {

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
	public ProteinAccessionsUpdater(ServletContext servletContext) {
		super(servletContext);
	}

	@Override
	public void run() {
		try {
			ContextualSessionHandler.getCurrentSession().beginTransaction();
			log.info("Starting " + getTaskName());

			final Criteria criteria = ContextualSessionHandler.getCurrentSession().createCriteria(Protein.class)
					.setReadOnly(true).setFetchSize(5000).setProjection(Projections.property("acc"));
			ScrollableResults proteins = criteria.scroll(ScrollMode.FORWARD_ONLY);
			log.info("scrollable result of proteins in the DB retrieved");
			final IPI2UniprotACCMap ipi2uniprot = IPI2UniprotACCMap.getInstance();

			final Set<String> accs = new THashSet<String>();
			while (proteins.next()) {
				final String acc = proteins.getString(0);
				final Accession accessionWithType = FastaParser.getACC(acc);
				if (accessionWithType.getAccessionType() == AccessionType.UNIPROT) {
					accs.add(accessionWithType.getAccession());
				} else if (accessionWithType.getAccessionType() == AccessionType.IPI) {
					final List<UniprotEntry> map2Uniprot = ipi2uniprot.map2Uniprot(accessionWithType.getAccession());
					for (final UniprotEntry uniprotEntry : map2Uniprot) {
						accs.add(uniprotEntry.getAcc());
					}
				}
			}
			log.info(accs.size() + " accs in the DB retrieved");

			final UniprotProteinLocalRetriever ulr = new UniprotProteinLocalRetriever(urs.getUniprotReleasesFolder(),
					true, true, true);
			ulr.setRetrieveFastaIsoforms(false);

			final Map<String, Entry> annotatedProteins = ulr.getAnnotatedProteins(null, accs);
			int i = 0;
			proteins = ContextualSessionHandler.retrieveIterator(Protein.class, 5000, false);
			while (proteins.next()) {
				final edu.scripps.yates.proteindb.persistence.mysql.Protein protein = (Protein) proteins.get(0);
				System.out.println(++i + "\t" + protein.getId());
				final String proteinAccession = protein.getAcc();
				final Accession accPair = FastaParser.getACC(proteinAccession);
				final AccessionType accType = accPair.getAccessionType();
				final String accession = accPair.getAccession();
				String ipiAcc = null;
				String uniprotAcc = null;

				if (accType == AccessionType.IPI) {
					log.info("It contains a IPI acc");
					ipiAcc = accession;
				} else if (accType == AccessionType.UNIPROT) {
					uniprotAcc = proteinAccession;
				}

				if (uniprotAcc != null) {

					final Entry annotatedProtein = annotatedProteins.get(uniprotAcc);

					final String primaryAcc = UniprotEntryUtil.getPrimaryAccession(annotatedProtein);

					if (primaryAcc != null) {
						if (!primaryAcc.equals(uniprotAcc)) {
							protein.setAcc(primaryAcc);
							log.info("Changing ACC for protein id:" + protein.getId() + " from " + uniprotAcc + " to "
									+ primaryAcc);
							ContextualSessionHandler.saveOrUpdate(protein);
						}
					}

				} else if (ipiAcc != null) {
					final List<UniprotEntry> map2Uniprot = ipi2uniprot.map2Uniprot(ipiAcc);
					if (!map2Uniprot.isEmpty()) {
						final Set<String> uniprotACCs = new THashSet<String>();
						final Set<Entry> entries = new THashSet<Entry>();
						final UniprotEntry uniprotEntry = map2Uniprot.get(0);
						Entry entry = null;
						if (annotatedProteins.containsKey(uniprotEntry.getAcc())) {
							entry = annotatedProteins.get(uniprotEntry.getAcc());
						} else {
							final Map<String, Entry> uniprots = ulr.getAnnotatedProtein(null, uniprotEntry.getAcc());
							if (!uniprots.isEmpty()) {
								entry = uniprots.get(uniprotEntry.getAcc());
							}
						}
						final String primaryAcc = UniprotEntryUtil.getPrimaryAccession(entry);
						if (!primaryAcc.equals(ipiAcc)) {
							log.info("Changing ACC for protein id:" + protein.getId() + " from " + ipiAcc + " to "
									+ primaryAcc);
							protein.setAcc(primaryAcc);
							ContextualSessionHandler.saveOrUpdate(protein);
						}
					}
				}
			}
			log.info("Committing transaction...");
			ContextualSessionHandler.finishGoodTransaction();

		} catch (final Exception e) {
			e.printStackTrace();
			ContextualSessionHandler.getCurrentSession().getTransaction().rollback();
		} finally {
			ContextualSessionHandler.getCurrentSession().close();
		}
	}

	@Override
	public boolean justRunOnce() {
		return false;
	}
}
