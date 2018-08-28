package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.collect.Iterables;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.util.UniprotEntryUtil;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinScore;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinThreshold;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProteinAccessionAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.util.Pair;
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
	private final Set<LinkBetweenQueriableProteinSetAndPSM> linksToPSMs = new THashSet<LinkBetweenQueriableProteinSetAndPSM>();
	private final Set<LinkBetweenQueriableProteinSetAndPeptideSet> linksToPeptides = new THashSet<LinkBetweenQueriableProteinSetAndPeptideSet>();

	/**
	 * Proteins that share the same primary accession
	 */
	private final List<Protein> proteins = new ArrayList<Protein>();
	private final String primaryAcc;
	private ProteinAccession primaryProteinAcc;
	private Set<Protein> individualProteins;
	private String uniprotKBVersion;
	private Set<String> proteinAccs;
	private THashSet<ProteinScore> proteinScores;
	private THashSet<MsRun> msRuns;
	private THashSet<ProteinRatioValue> proteinRatios;
	private THashSet<ProteinThreshold> proteinThresholds;
	private THashSet<ProteinAmount> proteinAmounts;
	private THashSet<ProteinAnnotation> proteinAnnotations;
	private THashSet<Gene> genes;
	private THashSet<Condition> conditions;
	private Organism organism;
	private static UniprotProteinLocalRetriever uplr;

	private final static Logger log = Logger.getLogger(QueriableProteinSet.class);
	private static final Map<String, QueriableProteinSet> map = new THashMap<String, QueriableProteinSet>();

	public static QueriableProteinSet getInstance(Collection<Protein> proteins, boolean forceToCreateNewObject) {
		final String id = getProteinsID(proteins);

		if (!forceToCreateNewObject && map.containsKey(id)) {
			final QueriableProteinSet queriableProtein = map.get(id);
			return queriableProtein;
		} else {
			final QueriableProteinSet queriableProtein = new QueriableProteinSet(proteins);
			map.put(id, queriableProtein);
			return queriableProtein;
		}
	}

	private static String getProteinsID(Collection<Protein> proteins) {
		final List<Integer> individualIds = new ArrayList<Integer>();
		for (final Protein protein : proteins) {
			individualIds.add(protein.getId());
		}
		Collections.sort(individualIds);
		final StringBuilder sb = new StringBuilder();
		for (final Integer integer : individualIds) {
			sb.append(integer);
		}
		return sb.toString();
	}

	private QueriableProteinSet(Collection<Protein> proteins) {
		primaryAcc = proteins.iterator().next().getAcc();
		this.proteins.addAll(proteins);
	}

	private UniprotProteinLocalRetriever getUplr() {
		if (uplr == null) {
			uplr = new UniprotProteinLocalRetriever(
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex(), true, true);
		}
		return uplr;
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
			for (final Protein protein : allProteins) {
				// disable check of peptides because this triggers a query
				// if (!protein.getPeptides().isEmpty()) {
				individualProteins.add(protein);
				// }
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
	public Set<LinkBetweenQueriableProteinSetAndPSM> getLinksToPSMs() {
		return linksToPSMs;
	}

	/**
	 * @return the links
	 */
	public Set<LinkBetweenQueriableProteinSetAndPeptideSet> getLinksToPeptides() {
		return linksToPeptides;
	}

	public void removeLink(LinkBetweenQueriableProteinSetAndPSM link) {
		final boolean removed = linksToPSMs.remove(link);
		// force to recreate the individual proteins
		individualProteins = null;
		if (!removed)
			log.warn("BAD");
	}

	public void removeLink(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		final boolean removed = linksToPeptides.remove(link);
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
		for (final Protein protein : getIndividualProteins()) {
			final boolean removed = protein.getPsms().remove(psm);
			if (removed) {
				final Set<Psm> psms = protein.getPsms();
				final Set<String> seqs = new THashSet<String>();
				for (final Psm psm2 : psms) {
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

		final Set<Peptide> peptideSet = new THashSet<Peptide>();
		for (final Protein protein : getIndividualProteins()) {
			final Set<Peptide> peptides = protein.getPeptides();
			for (Peptide peptide : peptides) {
				if (!ContextualSessionHandler.getCurrentSession().contains(peptide)) {
					peptide = (Peptide) ContextualSessionHandler.load(peptide.getId(), Peptide.class);
				}
				peptideSet.add(peptide);
			}
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

		final Set<Psm> psmSet = new THashSet<Psm>();
		for (final Protein protein : getIndividualProteins()) {
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

	public Set<String> getProteinAccessions() {
		if (proteinAccs == null) {
			proteinAccs = new THashSet<String>();
			for (final Protein protein : getIndividualProteins()) {
				proteinAccs.add(protein.getAcc());
			}
		}
		return proteinAccs;

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
			String description = null;
			String alternativeNamesString = "";
			if (FastaParser.isUniProtACC(primaryAcc)) {
				accessionType = AccessionType.UNIPROT.name();
				description = UniprotEntryUtil.getProteinDescription(getUniprotEntry());
				final List<String> alternativeNames = UniprotEntryUtil.getAlternativeNames(getUniprotEntry());
				for (final String alternativeName : alternativeNames) {
					if (!"".equals(alternativeNamesString)) {
						alternativeNamesString += ProteinAccessionAdapter.SEPARATOR;
					}
					alternativeNamesString += alternativeName;
				}
			} else if (FastaParser.getIPIACC(primaryAcc) != null) {
				accessionType = AccessionType.IPI.name();
			} else if (FastaParser.getNCBIACC(primaryAcc) != null) {
				accessionType = AccessionType.NCBI.name();
			}
			primaryProteinAcc = new ProteinAccession(primaryAcc, accessionType, true);
			if (!"".equals(alternativeNamesString)) {
				primaryProteinAcc.setAlternativeNames(alternativeNamesString);
			}
			primaryProteinAcc.setDescription(description);
		}
		return primaryProteinAcc;
	}

	/**
	 * Gets a {@link Set} of {@link Condition} from the {@link Protein}
	 *
	 * @return
	 */

	public Set<Condition> getConditions() {
		if (conditions == null) {
			conditions = new THashSet<Condition>();
			for (final Protein protein : getIndividualProteins()) {
				conditions.addAll(protein.getConditions());
			}
		}
		return conditions;
	}

	/**
	 * Gets a {@link Set} of {@link ProteinAmount} from the {@link Protein},
	 * assuring that they are linked to the current session
	 *
	 * @return
	 */

	public Set<ProteinAmount> getProteinAmounts() {
		if (proteinAmounts == null) {
			proteinAmounts = new THashSet<ProteinAmount>();
			for (final Protein protein : getIndividualProteins()) {
				final Set<ProteinAmount> proteinAmounts = protein.getProteinAmounts();
				for (ProteinAmount proteinAmount : proteinAmounts) {
					if (!ContextualSessionHandler.getCurrentSession().contains(proteinAmount)) {
						proteinAmount = (ProteinAmount) ContextualSessionHandler.getCurrentSession()
								.merge(proteinAmount);
					}
					proteinAmounts.add(proteinAmount);
				}
			}
		}
		return proteinAmounts;
	}

	/**
	 * Gets the protein db ID
	 *
	 * @return
	 */

	public TIntHashSet getProteinDBIds() {
		final TIntHashSet ret = new TIntHashSet();
		for (final Protein protein : getIndividualProteins()) {
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
		if (organism == null) {
			final Entry uniprotEntry = getUniprotEntry();
			final String taxonomyName = UniprotEntryUtil.getTaxonomyName(uniprotEntry);
			final String taxonomyNCBIID = UniprotEntryUtil.getTaxonomyNCBIID(uniprotEntry);
			organism = new Organism(taxonomyNCBIID);
			organism.setName(taxonomyName);

			// if
			// (!ContextualSessionHandler.getCurrentSession().contains(organism))
			// {
			// organism = (Organism)
			// ContextualSessionHandler.getCurrentSession().merge(organism);
			// }
		}
		return organism;

	}

	private Entry getUniprotEntry() {
		final Map<String, Entry> annotatedProtein = getUplr().getAnnotatedProtein(uniprotKBVersion, primaryAcc);
		if (annotatedProtein.containsKey(primaryAcc)) {
			return annotatedProtein.get(primaryAcc);
		}
		return null;
	}

	/**
	 * Gets a {@link Set} of {@link ProteinAnnotation} of the {@link Protein}
	 *
	 * @return
	 */

	public Set<ProteinAnnotation> getProteinAnnotations() {
		if (proteinAnnotations == null) {
			proteinAnnotations = new THashSet<ProteinAnnotation>();
			proteinAnnotations.addAll(
					ProteinAnnotator.getInstance(uniprotKBVersion).getProteinAnnotationByProteinAcc(primaryAcc));
			for (final Protein protein : getIndividualProteins()) {
				proteinAnnotations.addAll(protein.getProteinAnnotations());
			}
		}
		return proteinAnnotations;
	}

	/**
	 * Gets the {@link ProteinThreshold}s of the {@link Protein}s assuring that
	 * they are linked to the session
	 *
	 * @return
	 */

	public Set<ProteinThreshold> getProteinThresholds() {
		if (proteinThresholds == null) {
			proteinThresholds = new THashSet<ProteinThreshold>();
			for (final Protein protein : getIndividualProteins()) {
				final Set<ProteinThreshold> proteinThresholds = protein.getProteinThresholds();
				for (ProteinThreshold proteinThreshold : proteinThresholds) {
					if (!ContextualSessionHandler.getCurrentSession().contains(proteinThreshold)) {
						proteinThreshold = (ProteinThreshold) ContextualSessionHandler.getCurrentSession()
								.merge(proteinThreshold);
					}
					proteinThresholds.add(proteinThreshold);
				}
			}
		}
		return proteinThresholds;
	}

	public Set<Gene> getGenes() {
		if (genes == null) {
			genes = new THashSet<Gene>();
			final List<Pair<String, String>> geneNames = UniprotEntryUtil.getGeneName(getUniprotEntry(), false, false);
			for (int i = 0; i < geneNames.size(); i++) {
				final Pair<String, String> geneName = geneNames.get(i);
				final Gene newGene = new Gene(geneName.getFirstelement());
				newGene.setGeneType(geneName.getSecondElement());
				genes.add(newGene);
			}
		}
		return genes;
	}

	public Set<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(String condition1Name, String condition2Name,
			String ratioName) {
		final Set<ProteinRatioValue> ret = new THashSet<ProteinRatioValue>();
		for (final Protein protein : getIndividualProteins()) {
			final Set<ProteinRatioValue> proteinRatiosBetweenTwoConditions = PersistenceUtils
					.getProteinRatiosBetweenTwoConditions(protein, condition1Name, condition2Name, ratioName);
			ret.addAll(proteinRatiosBetweenTwoConditions);
		}
		return ret;
	}

	public Set<ProteinScore> getProteinScores() {
		if (proteinScores == null) {
			proteinScores = new THashSet<ProteinScore>();
			for (final Protein protein : getIndividualProteins()) {
				proteinScores.addAll(protein.getProteinScores());
			}
		}
		return proteinScores;

	}

	public Iterator<ProteinScore> getProteinScoresIterator() {
		Iterable<ProteinScore> ret = null;

		for (final Protein protein : getIndividualProteins()) {
			final Set<ProteinScore> proteinScores = protein.getProteinScores();
			if (ret == null) {
				ret = proteinScores;
			} else {
				ret = Iterables.concat(ret, proteinScores);
			}
		}

		return ret.iterator();
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
		if (msRuns == null) {
			msRuns = new THashSet<MsRun>();
			for (final Protein protein : getIndividualProteins()) {
				msRuns.add(protein.getMsRun());
			}
		}
		return msRuns;
	}

	public Set<ProteinRatioValue> getProteinRatioValues() {
		if (proteinRatios == null) {
			proteinRatios = new THashSet<ProteinRatioValue>();
			for (final Protein protein : getIndividualProteins()) {
				proteinRatios.addAll(protein.getProteinRatioValues());
			}
		}
		return proteinRatios;
	}

	public Iterator<ProteinRatioValue> getProteinRatioValuesIterator() {
		Iterable<ProteinRatioValue> ret = null;

		for (final Protein protein : getIndividualProteins()) {
			final Set<ProteinRatioValue> proteinRatioValues = protein.getProteinRatioValues();
			if (ret == null) {
				ret = proteinRatioValues;
			} else {
				ret = Iterables.concat(ret, proteinRatioValues);
			}
		}

		return ret.iterator();
	}

	public void addLinkToPSM(LinkBetweenQueriableProteinSetAndPSM link) {
		linksToPSMs.add(link);
		// force to recreate the individual proteins
		individualProteins = null;
	}

	public void addLinkToPeptide(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		linksToPeptides.add(link);
		// force to recreate the individual proteins
		individualProteins = null;
	}

	public void clearLinks() {
		linksToPSMs.clear();
		linksToPeptides.clear();
		// force to recreate the individual proteins
		individualProteins = null;
	}

}
