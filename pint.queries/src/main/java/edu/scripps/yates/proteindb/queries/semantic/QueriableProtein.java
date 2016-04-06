package edu.scripps.yates.proteindb.queries.semantic;

import java.util.HashMap;
import java.util.HashSet;
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

/**
 * Wrapper class to handle the links between protein and psms
 *
 * @author Salva
 *
 */
public class QueriableProtein implements QueriableProteinInterface {
	private final Set<LinkBetweenProteinAndPSM> links = new HashSet<LinkBetweenProteinAndPSM>();
	/**
	 * Proteins that share the same primary accession
	 */
	private Protein protein;
	private final String primaryAcc;
	private ProteinAccession primaryProteinAcc;

	private final static Logger log = Logger.getLogger(QueriableProtein.class);
	private static final Map<Integer, QueriableProtein> map = new HashMap<Integer, QueriableProtein>();

	public static QueriableProtein getInstance(Protein protein, boolean forceToCreateNewObject) {
		final Integer id = protein.getId();
		if (!forceToCreateNewObject && map.containsKey(id)) {
			final QueriableProtein queriableProtein = map.get(id);
			return queriableProtein;
		} else {
			QueriableProtein queriableProtein = new QueriableProtein(protein);
			map.put(id, queriableProtein);
			return queriableProtein;
		}
	}

	private QueriableProtein(Protein protein) {
		primaryAcc = protein.getAcc();
		this.protein = protein;
	}

	/**
	 * Returns the protein, assuring that the returned protein is available in
	 * the current Hibernate session, by calling merge(Protein) if
	 * {@link Protein} is not in the session.
	 *
	 * @return the proteins
	 */
	public Protein getProtein() {

		if (!ContextualSessionHandler.getSession().contains(protein)) {
			// Lock lock = new ReentrantLock(true);
			// try {
			// lock.lock();
			protein = (Protein) ContextualSessionHandler.getSession().merge(protein);
			// } finally {
			// lock.unlock();
			// }
		}

		return protein;

	}

	@Override
	public Set<Protein> getProteins() {
		Set<Protein> ret = new HashSet<Protein>();
		ret.add(getProtein());
		return ret;
	}

	/**
	 * @return the links
	 */
	@Override
	public Set<LinkBetweenProteinAndPSM> getLinks() {
		return links;
	}

	@Override
	public void removeLink(LinkBetweenProteinAndPSM link) {
		boolean removed = links.remove(link);
		if (!removed)
			log.warn("BAD");
	}

	/*
	 * (non-Javadoc)
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
	@Override
	public Set<Peptide> getPeptides() {
		Set<Peptide> ret = new HashSet<Peptide>();

		final Set<Peptide> peptides = getProtein().getPeptides();
		for (Peptide peptide : peptides) {
			if (!ContextualSessionHandler.getSession().contains(peptide)) {
				peptide = (Peptide) ContextualSessionHandler.getSession().merge(peptide);
			}
			ret.add(peptide);
		}

		return ret;
	}

	@Override
	public Set<ProteinAccession> getProteinAccessions() {
		Set<ProteinAccession> ret = new HashSet<ProteinAccession>();

		ret.addAll(getProtein().getProteinAccessions());

		return ret;
	}

	/**
	 * Returns the first {@link ProteinAccession} from the {@link Protein} that
	 * is annotated as primary accession (isIsPrimary()=true). If not found,
	 * return one of the accessions.
	 *
	 * @return
	 */
	@Override
	public String getPrimaryAccession() {
		return primaryAcc;
	}

	@Override
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
	@Override
	public Set<Condition> getConditions() {

		return getProtein().getConditions();

	}

	/**
	 * Gets a {@link Set} of {@link ProteinAmount} from the {@link Protein},
	 * assuring that they are linked to the current session
	 *
	 * @return
	 */
	@Override
	public Set<ProteinAmount> getProteinAmounts() {
		Set<ProteinAmount> ret = new HashSet<ProteinAmount>();

		final Set<ProteinAmount> proteinAmounts = getProtein().getProteinAmounts();
		for (ProteinAmount proteinAmount : proteinAmounts) {
			if (!ContextualSessionHandler.getSession().contains(proteinAmount)) {
				proteinAmount = (ProteinAmount) ContextualSessionHandler.getSession().merge(proteinAmount);
			}
			ret.add(proteinAmount);
		}

		return ret;
	}

	/**
	 * Gets the protein db ID
	 *
	 * @return
	 */
	@Override
	public Set<Integer> getProteinDBIds() {
		Set<Integer> ret = new HashSet<Integer>();
		ret.add(getProtein().getId());
		return ret;
	}

	/**
	 * Gets the organism of the proteins, assuring that it is in the current
	 * session.
	 *
	 * @return
	 */
	@Override
	public Organism getOrganism() {

		Organism organism = getProtein().getOrganism();
		if (!ContextualSessionHandler.getSession().contains(organism)) {
			organism = (Organism) ContextualSessionHandler.getSession().merge(organism);
		}
		return organism;
	}

	/**
	 * Gets a {@link Set} of {@link ProteinAnnotation} of the {@link Protein}
	 *
	 * @return
	 */
	@Override
	public Set<ProteinAnnotation> getProteinAnnotations() {
		return getProtein().getProteinAnnotations();
	}

	/**
	 * Gets the {@link ProteinThreshold}s of the {@link Protein}s assuring that
	 * they are linked to the session
	 *
	 * @return
	 */
	@Override
	public Set<ProteinThreshold> getProteinThresholds() {
		Set<ProteinThreshold> ret = new HashSet<ProteinThreshold>();

		final Set<ProteinThreshold> proteinThresholds = getProtein().getProteinThresholds();
		for (ProteinThreshold proteinThreshold : proteinThresholds) {
			if (!ContextualSessionHandler.getSession().contains(proteinThreshold)) {
				proteinThreshold = (ProteinThreshold) ContextualSessionHandler.getSession().merge(proteinThreshold);
			}
			ret.add(proteinThreshold);
		}

		return ret;
	}

	@Override
	public Set<Gene> getGenes() {
		return getProtein().getGenes();
	}

	@Override
	public Set<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(String condition1Name, String condition2Name,
			String ratioName) {
		Set<ProteinRatioValue> ret = new HashSet<ProteinRatioValue>();

		final Set<ProteinRatioValue> proteinRatiosBetweenTwoConditions = PersistenceUtils
				.getProteinRatiosBetweenTwoConditions(getProtein(), condition1Name, condition2Name, ratioName);
		ret.addAll(proteinRatiosBetweenTwoConditions);

		return ret;
	}

	@Override
	public Set<ProteinScore> getProteinScores() {
		return getProtein().getProteinScores();

	}

	@Override
	public Integer getLength() {
		return getProtein().getLength();
	}

	@Override
	public Double getMw() {
		return getProtein().getMw();
	}

	@Override
	public Double getPi() {
		return getProtein().getPi();
	}

	@Override

	public Set<MsRun> getMsRuns() {
		Set<MsRun> ret = new HashSet<MsRun>();
		ret.add(getProtein().getMsRun());
		return ret;
	}

	@Override

	public Set<ProteinRatioValue> getProteinRatioValues() {
		return getProtein().getProteinRatioValues();
	}

	@Override
	public Set<Psm> getPsms() {
		return getProtein().getPsms();
	}

}
