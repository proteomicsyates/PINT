package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

/**
 * Wrapper class to handle the links between protein and psms
 *
 * @author Salva
 *
 */
public class QueriableProteinSet {
	private final Set<LinkBetweenQueriableProteinSetAndPSM> links = new THashSet<LinkBetweenQueriableProteinSetAndPSM>();
	/**
	 * Proteins that share the same primary accession
	 */
	private final List<Protein> proteins = new ArrayList<Protein>();
	private final String primaryAcc;
	private ProteinAccession primaryProteinAcc;
	private Set<Protein> individualProteins;

	private final static Logger log = Logger.getLogger(QueriableProteinSet.class);
	private static final Map<String, QueriableProteinSet> map = new THashMap<String, QueriableProteinSet>();

	public static QueriableProteinSet getInstance(Collection<Protein> proteins, boolean forceToCreateNewObject) {
		final String id = getProteinsID(proteins);

		if (!forceToCreateNewObject && map.containsKey(id)) {
			final QueriableProteinSet queriableProtein = map.get(id);
			return queriableProtein;
		} else {
			QueriableProteinSet queriableProtein = new QueriableProteinSet(proteins);
			map.put(id, queriableProtein);
			return queriableProtein;
		}
	}

	private static String getProteinsID(Collection<Protein> proteins) {
		List<Integer> individualIds = new ArrayList<Integer>();
		for (Protein protein : proteins) {
			individualIds.add(protein.getId());
		}
		Collections.sort(individualIds);
		StringBuilder sb = new StringBuilder();
		for (Integer integer : individualIds) {
			sb.append(integer);
		}
		return sb.toString();
	}

	private QueriableProteinSet(Collection<Protein> proteins) {
		primaryAcc = proteins.iterator().next().getAcc();
		this.proteins.addAll(proteins);
	}

	/**
	 * Return all the individual proteins<br>
	 * <b>Note that maybe some of them have no PSMs and are not valid</b>
	 *
	 * @return
	 */
	public List<Protein> getAllProteins() {
		return proteins;
	}

	/**
	 * Returns the proteins, assuring that the returned protein is available in
	 * the current Hibernate session, by calling merge(Protein) if
	 * {@link Protein} is not in the session.
	 *
	 * @return the proteins
	 */

	public Set<Protein> getIndividualProteins() {
		if (individualProteins == null) {
			individualProteins = new THashSet<Protein>();
			final List<Protein> allProteins = getAllProteins();
			for (Protein protein : allProteins) {
				if (!protein.getPsms().isEmpty()) {
					individualProteins.add(protein);
				}
			}
			// if (!proteins2.isEmpty()) {
			// for (Protein protein : proteins2) {
			// if
			// (!ContextualSessionHandler.getSession().contains(protein))
			// {
			// protein = (Protein)
			// ContextualSessionHandler.load(protein.getId(),
			// Protein.class);
			// }
			// individualProteins.add(protein);
			// }
			// individualProteins.addAll(proteins2);
			// }

		}
		return individualProteins;
		// final Iterator<Protein> proteinIterator = proteins.iterator();
		// while (proteinIterator.hasNext()) {
		// Protein protein = proteinIterator.next();
		// if (!ContextualSessionHandler.getSession().contains(protein)) {
		// // Lock lock = new ReentrantLock(true);
		// // try {
		// // lock.lock();
		// protein = (Protein) ContextualSessionHandler.load(protein.getId(),
		// Protein.class);
		// // } finally {
		// // lock.unlock();
		// // }
		// }
		// if (protein.getPsms().isEmpty()) {
		// proteinIterator.remove();
		// }
		// }
		// return proteins;

	}

	/**
	 * @return the links
	 */

	public Set<LinkBetweenQueriableProteinSetAndPSM> getLinks() {
		return links;
	}

	public void removeLink(LinkBetweenQueriableProteinSetAndPSM link) {
		boolean removed = links.remove(link);
		// force to recreate the individual proteins
		individualProteins = null;
		if (!removed)
			log.warn("BAD");
	}

	/**
	 * It removes the psm from the protein that has it
	 *
	 * @param psm
	 */
	public void remove(Psm psm) {
		for (Protein protein : getIndividualProteins()) {
			boolean removed = protein.getPsms().remove(psm);
			if (removed) {
				Set<Psm> psms = protein.getPsms();
				Set<String> seqs = new THashSet<String>();
				for (Psm psm2 : psms) {
					seqs.add(psm2.getSequence());
				}
				final Iterator<Peptide> peptideIterator = protein.getPeptides().iterator();
				while (peptideIterator.hasNext()) {
					final Peptide peptide = peptideIterator.next();
					if (!seqs.contains(peptide.getSequence())) {
						peptideIterator.remove();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		return getPrimaryAccession();

	}

	/**
	 * Returns the peptides, assuring that the returned peptides are available
	 * in the current Hibernate session, by calling merge(Peptide) if
	 * {@link Peptide} is not in the session.
	 *
	 * @return the peptides
	 */

	public Set<Peptide> getPeptides() {

		Set<Peptide> peptideSet = new THashSet<Peptide>();
		for (Psm psm : getPsms()) {
			Peptide peptide = psm.getPeptide();
			if (!ContextualSessionHandler.getCurrentSession().contains(peptide)) {
				peptide = (Peptide) ContextualSessionHandler.load(peptide.getId(), Peptide.class);
			}
			peptideSet.add(peptide);

		}

		return peptideSet;
	}

	/**
	 * Returns the peptides, assuring that the returned peptides are available
	 * in the current Hibernate session, by calling merge(Peptide) if
	 * {@link Peptide} is not in the session.
	 *
	 * @return the peptides
	 */

	public Set<Psm> getPsms() {

		Set<Psm> psmSet = new THashSet<Psm>();
		for (Protein protein : getIndividualProteins()) {
			final Set<Psm> psms = protein.getPsms();
			for (Psm psm : psms) {
				if (!ContextualSessionHandler.getCurrentSession().contains(psm)) {
					psm = (Psm) ContextualSessionHandler.load(psm.getId(), Psm.class);
				}
				psmSet.add(psm);
			}
		}

		return psmSet;
	}

	public Set<ProteinAccession> getProteinAccessions() {
		Set<ProteinAccession> ret = new THashSet<ProteinAccession>();
		for (Protein protein : getIndividualProteins()) {
			ret.addAll(protein.getProteinAccessions());
		}
		return ret;
	}

	/**
	 * Returns the first {@link ProteinAccession} from the {@link Protein} that
	 * is annotated as primary accession (isIsPrimary()=true). If not found,
	 * return one of the accessions.
	 *
	 * @return
	 */

	public String getPrimaryAccession() {
		return primaryAcc;
	}

	public ProteinAccession getPrimaryProteinAccession() {
		if (primaryProteinAcc == null) {
			String accessionType = null;
			if (FastaParser.isUniProtACC(primaryAcc)) {
				accessionType = AccessionType.UNIPROT.name();
			} else if (FastaParser.getIPIACC(primaryAcc) != null) {
				accessionType = AccessionType.IPI.name();
			} else if (FastaParser.getNCBIACC(primaryAcc) != null) {
				accessionType = AccessionType.NCBI.name();
			}
			primaryProteinAcc = new ProteinAccession(primaryAcc, accessionType, true);
		}
		return primaryProteinAcc;
	}

	/**
	 * Gets a {@link Set} of {@link Condition} from the {@link Protein}
	 *
	 * @return
	 */

	public Set<Condition> getConditions() {
		Set<Condition> ret = new HashSet<Condition>();
		for (Protein protein : getIndividualProteins()) {
			ret.addAll(protein.getConditions());
		}
		return ret;
	}

	/**
	 * Gets a {@link Set} of {@link ProteinAmount} from the {@link Protein},
	 * assuring that they are linked to the current session
	 *
	 * @return
	 */

	public Set<ProteinAmount> getProteinAmounts() {
		Set<ProteinAmount> ret = new THashSet<ProteinAmount>();
		for (Protein protein : getIndividualProteins()) {
			final Set<ProteinAmount> proteinAmounts = protein.getProteinAmounts();
			for (ProteinAmount proteinAmount : proteinAmounts) {
				if (!ContextualSessionHandler.getCurrentSession().contains(proteinAmount)) {
					proteinAmount = (ProteinAmount) ContextualSessionHandler.getCurrentSession().merge(proteinAmount);
				}
				ret.add(proteinAmount);
			}
		}
		return ret;
	}

	/**
	 * Gets the protein db ID
	 *
	 * @return
	 */

	public TIntHashSet getProteinDBIds() {
		TIntHashSet ret = new TIntHashSet();
		for (Protein protein : getIndividualProteins()) {
			ret.add(protein.getId());
		}
		return ret;
	}

	/**
	 * Gets the organism of the proteins, assuring that it is in the current
	 * session.
	 *
	 * @return
	 */

	public Organism getOrganism() {

		Organism organism = getIndividualProteins().iterator().next().getOrganism();
		if (!ContextualSessionHandler.getCurrentSession().contains(organism)) {
			organism = (Organism) ContextualSessionHandler.getCurrentSession().merge(organism);
		}
		return organism;
	}

	/**
	 * Gets a {@link Set} of {@link ProteinAnnotation} of the {@link Protein}
	 *
	 * @return
	 */

	public Set<ProteinAnnotation> getProteinAnnotations() {
		Set<ProteinAnnotation> ret = new THashSet<ProteinAnnotation>();
		for (Protein protein : getIndividualProteins()) {
			ret.addAll(protein.getProteinAnnotations());
		}
		return ret;
	}

	/**
	 * Gets the {@link ProteinThreshold}s of the {@link Protein}s assuring that
	 * they are linked to the session
	 *
	 * @return
	 */

	public Set<ProteinThreshold> getProteinThresholds() {
		Set<ProteinThreshold> ret = new THashSet<ProteinThreshold>();
		for (Protein protein : getIndividualProteins()) {
			final Set<ProteinThreshold> proteinThresholds = protein.getProteinThresholds();
			for (ProteinThreshold proteinThreshold : proteinThresholds) {
				if (!ContextualSessionHandler.getCurrentSession().contains(proteinThreshold)) {
					proteinThreshold = (ProteinThreshold) ContextualSessionHandler.getCurrentSession()
							.merge(proteinThreshold);
				}
				ret.add(proteinThreshold);
			}
		}
		return ret;
	}

	public Set<Gene> getGenes() {
		Set<Gene> ret = new THashSet<Gene>();
		for (Protein protein : getIndividualProteins()) {
			ret.addAll(protein.getGenes());
		}
		return ret;
	}

	public Set<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(String condition1Name, String condition2Name,
			String ratioName) {
		Set<ProteinRatioValue> ret = new THashSet<ProteinRatioValue>();
		for (Protein protein : getIndividualProteins()) {
			final Set<ProteinRatioValue> proteinRatiosBetweenTwoConditions = PersistenceUtils
					.getProteinRatiosBetweenTwoConditions(protein, condition1Name, condition2Name, ratioName);
			ret.addAll(proteinRatiosBetweenTwoConditions);
		}
		return ret;
	}

	public Set<ProteinScore> getProteinScores() {
		Set<ProteinScore> ret = new THashSet<ProteinScore>();
		for (Protein protein : getIndividualProteins()) {
			ret.addAll(protein.getProteinScores());
		}
		return ret;

	}

	public Integer getLength() {
		return getIndividualProteins().iterator().next().getLength();
	}

	public Double getMw() {

		return getIndividualProteins().iterator().next().getMw();
	}

	public Double getPi() {
		return getIndividualProteins().iterator().next().getPi();
	}

	public Set<MsRun> getMsRuns() {
		Set<MsRun> ret = new THashSet<MsRun>();
		for (Protein protein : getIndividualProteins()) {
			ret.add(protein.getMsRun());
		}
		return ret;
	}

	public Set<ProteinRatioValue> getProteinRatioValues() {
		Set<ProteinRatioValue> ret = new THashSet<ProteinRatioValue>();
		for (Protein protein : getIndividualProteins()) {
			ret.addAll(protein.getProteinRatioValues());
		}
		return ret;
	}

	public void addLink(LinkBetweenQueriableProteinSetAndPSM link) {
		links.add(link);
		// force to recreate the individual proteins
		individualProteins = null;
	}

	public void clearLinks() {
		links.clear();
		// force to recreate the individual proteins
		individualProteins = null;
	}

}
