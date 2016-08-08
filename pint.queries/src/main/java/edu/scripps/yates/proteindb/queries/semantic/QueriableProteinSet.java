package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
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

/**
 * Wrapper class to handle the links between protein and psms
 *
 * @author Salva
 *
 */
public class QueriableProteinSet implements QueriableProteinInterface {
	private final Set<LinkBetweenProteinAndPSM> links = new HashSet<LinkBetweenProteinAndPSM>();
	/**
	 * Proteins that share the same primary accession
	 */
	private final Set<Protein> proteins = new HashSet<Protein>();
	private final String primaryAcc;
	private ProteinAccession primaryProteinAcc;

	private final static Logger log = Logger.getLogger(QueriableProteinSet.class);
	private static final Map<String, QueriableProteinSet> map = new HashMap<String, QueriableProteinSet>();

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

	private QueriableProteinSet(Protein protein) {
		primaryAcc = protein.getAcc();
		proteins.add(protein);
	}

	private QueriableProteinSet(Collection<Protein> proteins) {
		primaryAcc = proteins.iterator().next().getAcc();
		this.proteins.addAll(proteins);
	}

	/**
	 * Returns the protein, assuring that the returned protein is available in
	 * the current Hibernate session, by calling merge(Protein) if
	 * {@link Protein} is not in the session.
	 *
	 * @return the proteins
	 */
	@Override
	public Set<Protein> getProteins() {

		final Iterator<Protein> proteinIterator = proteins.iterator();
		while (proteinIterator.hasNext()) {
			Protein protein = proteinIterator.next();
			if (!ContextualSessionHandler.getSession().contains(protein)) {
				// Lock lock = new ReentrantLock(true);
				// try {
				// lock.lock();
				protein = (Protein) ContextualSessionHandler.load(protein.getId(), Protein.class);
				// } finally {
				// lock.unlock();
				// }
			}
			if (protein.getPsms().isEmpty()) {
				proteinIterator.remove();
			}
		}
		return proteins;

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

	/**
	 * It removes the psm from the protein that has it
	 *
	 * @param psm
	 */
	public void remove(Psm psm) {
		for (Protein protein : getProteins()) {
			boolean removed = protein.getPsms().remove(psm);
			if (removed) {
				Set<Psm> psms = protein.getPsms();
				Set<String> seqs = new HashSet<String>();
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

		HashSet<Peptide> peptideSet = new HashSet<Peptide>();
		for (Psm psm : getPsms()) {
			Peptide peptide = psm.getPeptide();
			if (!ContextualSessionHandler.getSession().contains(peptide)) {
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
	@Override
	public Set<Psm> getPsms() {

		HashSet<Psm> psmSet = new HashSet<Psm>();
		for (Protein protein : getProteins()) {
			final Set<Psm> psms = protein.getPsms();
			for (Psm psm : psms) {
				if (!ContextualSessionHandler.getSession().contains(psm)) {
					psm = (Psm) ContextualSessionHandler.load(psm.getId(), Psm.class);
				}
				psmSet.add(psm);
			}
		}

		return psmSet;
	}

	@Override
	public Set<ProteinAccession> getProteinAccessions() {
		Set<ProteinAccession> ret = new HashSet<ProteinAccession>();
		for (Protein protein : getProteins()) {
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
		Set<Condition> ret = new HashSet<Condition>();
		for (Protein protein : getProteins()) {
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
	@Override
	public Set<ProteinAmount> getProteinAmounts() {
		Set<ProteinAmount> ret = new HashSet<ProteinAmount>();
		for (Protein protein : getProteins()) {
			final Set<ProteinAmount> proteinAmounts = protein.getProteinAmounts();
			for (ProteinAmount proteinAmount : proteinAmounts) {
				if (!ContextualSessionHandler.getSession().contains(proteinAmount)) {
					proteinAmount = (ProteinAmount) ContextualSessionHandler.getSession().merge(proteinAmount);
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
	@Override
	public Set<Integer> getProteinDBIds() {
		Set<Integer> ret = new HashSet<Integer>();
		for (Protein protein : getProteins()) {
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
	@Override
	public Organism getOrganism() {

		Organism organism = getProteins().iterator().next().getOrganism();
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
		Set<ProteinAnnotation> ret = new HashSet<ProteinAnnotation>();
		for (Protein protein : getProteins()) {
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
	@Override
	public Set<ProteinThreshold> getProteinThresholds() {
		Set<ProteinThreshold> ret = new HashSet<ProteinThreshold>();
		for (Protein protein : getProteins()) {
			final Set<ProteinThreshold> proteinThresholds = protein.getProteinThresholds();
			for (ProteinThreshold proteinThreshold : proteinThresholds) {
				if (!ContextualSessionHandler.getSession().contains(proteinThreshold)) {
					proteinThreshold = (ProteinThreshold) ContextualSessionHandler.getSession().merge(proteinThreshold);
				}
				ret.add(proteinThreshold);
			}
		}
		return ret;
	}

	@Override
	public Set<Gene> getGenes() {
		Set<Gene> ret = new HashSet<Gene>();
		for (Protein protein : getProteins()) {
			ret.addAll(protein.getGenes());
		}
		return ret;
	}

	@Override
	public Set<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(String condition1Name, String condition2Name,
			String ratioName) {
		Set<ProteinRatioValue> ret = new HashSet<ProteinRatioValue>();
		for (Protein protein : getProteins()) {
			final Set<ProteinRatioValue> proteinRatiosBetweenTwoConditions = PersistenceUtils
					.getProteinRatiosBetweenTwoConditions(protein, condition1Name, condition2Name, ratioName);
			ret.addAll(proteinRatiosBetweenTwoConditions);
		}
		return ret;
	}

	@Override
	public Set<ProteinScore> getProteinScores() {
		Set<ProteinScore> ret = new HashSet<ProteinScore>();
		for (Protein protein : getProteins()) {
			ret.addAll(protein.getProteinScores());
		}
		return ret;

	}

	@Override
	public Integer getLength() {
		return getProteins().iterator().next().getLength();
	}

	@Override
	public Double getMw() {

		return getProteins().iterator().next().getMw();
	}

	@Override
	public Double getPi() {
		return getProteins().iterator().next().getPi();
	}

	@Override
	public Set<MsRun> getMsRuns() {
		Set<MsRun> ret = new HashSet<MsRun>();
		for (Protein protein : getProteins()) {
			ret.add(protein.getMsRun());
		}
		return ret;
	}

	@Override
	public Set<ProteinRatioValue> getProteinRatioValues() {
		Set<ProteinRatioValue> ret = new HashSet<ProteinRatioValue>();
		for (Protein protein : getProteins()) {
			ret.addAll(protein.getProteinRatioValues());
		}
		return ret;
	}

}
