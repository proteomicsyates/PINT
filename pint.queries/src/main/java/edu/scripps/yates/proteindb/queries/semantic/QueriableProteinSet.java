package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
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
import edu.scripps.yates.proteindb.persistence.mysql.Tissue;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProteinAccessionAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.PeptideIDToPSMIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToConditionIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToMSRunIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToPeptideIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToProteinScoreIDTableMapper;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.idtablemapper.ProteinIDToProteinThresholdIDTableMapper;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.factories.GeneEx;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.TIntCollection;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

/**
 * Wrapper class to handle the links between protein and psms
 *
 * @author Salva
 *
 */
public class QueriableProteinSet {
	private final List<LinkBetweenQueriableProteinSetAndPSM> linksToPSMs = new ArrayList<LinkBetweenQueriableProteinSetAndPSM>();
	private final List<LinkBetweenQueriableProteinSetAndPeptideSet> linksToPeptides = new ArrayList<LinkBetweenQueriableProteinSetAndPeptideSet>();

	/**
	 * Proteins that share the same primary accession
	 */
	private final List<Protein> proteins = new ArrayList<Protein>();
	private final String primaryAcc;
	private ProteinAccession primaryProteinAcc;
	private Set<Protein> individualProteins;
	private String uniprotKBVersion;
	private Set<String> proteinAccs;
	private List<ProteinScore> proteinScores;
	private List<MsRun> msRuns;
	private List<ProteinRatioValue> proteinRatios;
	private List<ProteinThreshold> proteinThresholds;
	private List<ProteinAmount> proteinAmounts;
	private List<ProteinAnnotation> proteinAnnotations;
	private List<edu.scripps.yates.utilities.proteomicsmodel.Gene> genes;
	private List<Condition> conditions;
	private Organism organism;
	private List<Tissue> tissues;
	private List<Psm> psmList;
	private TIntList psmIDs;
	private List<Peptide> peptideList;
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
		final TIntArrayList individualIds = new TIntArrayList(proteins.size());
		for (final Protein protein : proteins) {
			individualIds.add(protein.getId());
		}
		individualIds.sort();
		final StringBuilder sb = new StringBuilder();
		for (final int id : individualIds.toArray()) {
			sb.append(id);
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
	 * Returns the proteins, assuring that the returned protein is available in the
	 * current Hibernate session, by calling merge(Protein) if {@link Protein} is
	 * not in the session.
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
	public List<LinkBetweenQueriableProteinSetAndPSM> getLinksToPSMs() {
		return linksToPSMs;
	}

	/**
	 * @return the links
	 */
	public List<LinkBetweenQueriableProteinSetAndPeptideSet> getLinksToPeptides() {
		return linksToPeptides;
	}

	public void removeLink(LinkBetweenQueriableProteinSetAndPSM link) {
		final boolean removed = linksToPSMs.remove(link);
		// force to recreate the individual proteins
		individualProteins = null;
		// force to recreate psms
		psmList = null;
		if (!removed)
			log.warn("BAD");
	}

	public void removeLink(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		final boolean removed = linksToPeptides.remove(link);
		// force to recreate the individual proteins
		individualProteins = null;
		// force to recreate psms
		psmList = null;
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
	 * Returns the peptides, assuring that the returned peptides are available in
	 * the current Hibernate session, by calling merge(Peptide) if {@link Peptide}
	 * is not in the session.
	 *
	 * @return the peptides
	 */

	public List<Peptide> getPeptides() {
		if (peptideList == null || peptideList.isEmpty()) {
			final TIntArrayList proteinIds = new TIntArrayList();
			for (final Protein protein : getIndividualProteins()) {
				proteinIds.add(protein.getId());
			}
			final TIntSet peptideIDs = ProteinIDToPeptideIDTableMapper.getInstance()
					.getPeptideIDsFromProteinIDs(proteinIds);
			peptideList = (List<Peptide>) PreparedCriteria.getBatchLoadByIDs(Peptide.class, peptideIDs, true, 250);
		}
		return peptideList;
	}

	/**
	 * Returns the peptides, assuring that the returned peptides are available in
	 * the current Hibernate session, by calling merge(Peptide) if {@link Peptide}
	 * is not in the session.
	 *
	 * @return the peptides
	 */

	public List<Psm> getPsms() {
		if (psmList == null || psmList.isEmpty()) {
			if (getLinksToPSMs() != null) {
				psmList = new ArrayList<Psm>();
				getLinksToPSMs().stream().forEach(link -> psmList.add(link.getQueriablePsm().getPsm()));
			} else if (getLinksToPeptides() != null) {
				psmList = new ArrayList<Psm>();
				for (final LinkBetweenQueriableProteinSetAndPeptideSet link : getLinksToPeptides()) {
					final TIntList peptideIDs = new TIntArrayList();
					link.getQueriablePeptideSet().getIndividualPeptides().stream()
							.forEach(peptide -> peptideIDs.add(peptide.getId()));
					final TIntSet psmIDs = PeptideIDToPSMIDTableMapper.getInstance()
							.getPSMIDsFromPeptideIDs(peptideIDs);
					final List<Psm> psmListTMP = (List<Psm>) PreparedCriteria.getBatchLoadByIDs(Psm.class, psmIDs, true,
							250);
					for (final Psm psm : psmListTMP) {
						if (!psmList.contains(psm)) {
							psmList.add(psm);
						}
					}

				}

			}
		}
		return psmList;
	}

	/**
	 * Gets the IDs of the PSMs by iterating over the links to peptides or PSMs
	 * 
	 * @return
	 */
	public TIntList getPSMIDs() {
		if (psmIDs == null || psmIDs.isEmpty()) {
			if (getLinksToPSMs() != null && !getLinksToPSMs().isEmpty()) {
				psmIDs = new TIntArrayList();
				getLinksToPSMs().stream().forEach(link -> psmIDs.add(link.getQueriablePsm().getPsm().getId()));
			} else if (getLinksToPeptides() != null && !getLinksToPeptides().isEmpty()) {
				psmIDs = new TIntArrayList();
				for (final LinkBetweenQueriableProteinSetAndPeptideSet link : getLinksToPeptides()) {
					final TIntList peptideIDs = new TIntArrayList();
					link.getQueriablePeptideSet().getIndividualPeptides().stream()
							.forEach(peptide -> peptideIDs.add(peptide.getId()));
					final TIntSet psmIDs2 = PeptideIDToPSMIDTableMapper.getInstance()
							.getPSMIDsFromPeptideIDs(peptideIDs);
					for (final int psmID : psmIDs2.toArray()) {
						if (!psmIDs.contains(psmID)) {
							psmIDs.add(psmID);
						}
					}

				}

			}
		}
		return psmIDs;
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
	 * Returns the first {@link ProteinAccession} from the {@link Protein} that is
	 * annotated as primary accession (isIsPrimary()=true). If not found, return one
	 * of the accessions.
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
	public List<Condition> getConditions() {
		if (conditions == null) {
			final TIntArrayList proteinIds = new TIntArrayList();
			for (final Protein protein : getIndividualProteins()) {
				proteinIds.add(protein.getId());
			}
			final TIntSet conditionIDs = ProteinIDToConditionIDTableMapper.getInstance()
					.getConditionIDsFromProteinIDs(proteinIds);
			conditions = (List<Condition>) PreparedCriteria.getBatchLoadByIDs(Condition.class, conditionIDs, true, 250);
		}
		return conditions;
	}

	public List<Tissue> getTissues() {
		if (tissues == null) {
			tissues = new ArrayList<Tissue>();
			for (final Condition condition : getConditions()) {
				final Tissue tissue = condition.getSample().getTissue();
				if (!tissues.contains(tissue)) {
					tissues.add(tissue);
				}
			}
		}
		return tissues;
	}

	/**
	 * Gets a {@link Set} of {@link ProteinAmount} from the {@link Protein},
	 * assuring that they are linked to the current session
	 *
	 * @return
	 */

	public List<ProteinAmount> getProteinAmounts() {
		if (proteinAmounts == null) {
			proteinAmounts = new ArrayList<ProteinAmount>();

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
			if (uniprotEntry != null) {
				final String taxonomyName = UniprotEntryUtil.getTaxonomyName(uniprotEntry);
				final String taxonomyNCBIID = UniprotEntryUtil.getTaxonomyNCBIID(uniprotEntry);

				organism = new Organism(taxonomyNCBIID);
				organism.setName(taxonomyName);
			}

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

	public List<ProteinAnnotation> getProteinAnnotations() {
		if (proteinAnnotations == null) {
			proteinAnnotations = new ArrayList<ProteinAnnotation>();
			proteinAnnotations.addAll(
					ProteinAnnotator.getInstance(uniprotKBVersion).getProteinAnnotationByProteinAcc(primaryAcc));
			for (final Protein protein : getIndividualProteins()) {
				proteinAnnotations.addAll(protein.getProteinAnnotations());
			}
		}
		return proteinAnnotations;
	}

	/**
	 * Gets the {@link ProteinThreshold}s of the {@link Protein}s assuring that they
	 * are linked to the session
	 *
	 * @return
	 */

	public List<ProteinThreshold> getProteinThresholds() {
		if (proteinThresholds == null) {

			final TIntArrayList proteinIds = new TIntArrayList();
			for (final Protein protein : getIndividualProteins()) {
				proteinIds.add(protein.getId());
			}
			final TIntSet proteinThresholdIDs = ProteinIDToProteinThresholdIDTableMapper.getInstance()
					.getProteinThresholdIDsFromProteinIDs(proteinIds);
			proteinThresholds = (List<ProteinThreshold>) PreparedCriteria.getBatchLoadByIDs(ProteinThreshold.class,
					proteinThresholdIDs, true, 250);
		}
		return proteinThresholds;
	}

	public List<edu.scripps.yates.utilities.proteomicsmodel.Gene> getGenes() {
		if (genes == null) {
			genes = new ArrayList<edu.scripps.yates.utilities.proteomicsmodel.Gene>();
			final List<Pair<String, String>> geneNames = UniprotEntryUtil.getGeneName(getUniprotEntry(), false, false);
			for (int i = 0; i < geneNames.size(); i++) {
				final Pair<String, String> geneName = geneNames.get(i);
				final GeneEx newGene = new GeneEx(geneName.getFirstelement());
				newGene.setGeneType(geneName.getSecondElement());
				genes.add(newGene);
			}
		}
		return genes;
	}

	public List<ProteinRatioValue> getProteinRatiosBetweenTwoConditions(String condition1Name, String condition2Name,
			String ratioName) {
		final List<ProteinRatioValue> ret = new ArrayList<ProteinRatioValue>();
		for (final Protein protein : getIndividualProteins()) {
			final Set<ProteinRatioValue> proteinRatiosBetweenTwoConditions = PersistenceUtils
					.getProteinRatiosBetweenTwoConditions(protein, condition1Name, condition2Name, ratioName);
			ret.addAll(proteinRatiosBetweenTwoConditions);
		}
		return ret;
	}

	public List<ProteinScore> getProteinScores() {
		if (proteinScores == null) {

			final TIntSet proteinIDs = new TIntHashSet();
			for (final Protein protein : getIndividualProteins()) {
				proteinIDs.add(protein.getId());

			}
			final TIntSet proteinScoreIDs = ProteinIDToProteinScoreIDTableMapper.getInstance()
					.getProteinScoreIDsFromProteinIDs(proteinIDs);
			proteinScores = (List<ProteinScore>) PreparedCriteria.getBatchLoadByIDs(ProteinScore.class, proteinScoreIDs,
					true, 250);
		}
		return proteinScores;

	}

	public List<ProteinScore> getProteinScores(TIntCollection proteinIDsToIgnore) {

		final TIntArrayList proteinIds = new TIntArrayList();
		for (final Protein protein : getIndividualProteins()) {
			if (proteinIDsToIgnore.contains(protein.getId())) {
				continue;
			}
			proteinIds.add(protein.getId());
		}
		final TIntSet proteinScoreIDs = ProteinIDToProteinScoreIDTableMapper.getInstance()
				.getProteinScoreIDsFromProteinIDs(proteinIds);
		final List<ProteinScore> proteinScores = (List<ProteinScore>) PreparedCriteria
				.getBatchLoadByIDs(ProteinScore.class, proteinScoreIDs, true, 250);

		return proteinScores;

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

	public List<MsRun> getMsRuns() {
		if (msRuns == null) {
			msRuns = new ArrayList<MsRun>();
			final TIntSet msRunIDs = new TIntHashSet();
			for (final Protein protein : getIndividualProteins()) {
				final TIntSet msRunIDs2 = ProteinIDToMSRunIDTableMapper.getInstance()
						.getMSRunIDsFromProteinID(protein.getId());
				msRunIDs.addAll(msRunIDs2);
			}
			final List<MsRun> msRuns2 = (List<MsRun>) PreparedCriteria.getBatchLoadByIDs(MsRun.class, msRunIDs, true,
					50);
			for (final MsRun msRun : msRuns2) {
				msRuns.add(msRun);
			}
		}
		return msRuns;
	}

	public List<ProteinRatioValue> getProteinRatioValues() {
		if (proteinRatios == null) {
			proteinRatios = new ArrayList<ProteinRatioValue>();
			for (final Protein protein : getIndividualProteins()) {
				proteinRatios.addAll(protein.getProteinRatioValues());
			}
		}
		return proteinRatios;
	}

	public void addLinkToPSM(LinkBetweenQueriableProteinSetAndPSM link) {
		if (!linksToPSMs.contains(link)) {
			linksToPSMs.add(link);
			// force to recreate the individual proteins
			individualProteins = null;
		}
	}

	public void addLinkToPeptide(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		if (!linksToPeptides.contains(link)) {
			linksToPeptides.add(link);
			// force to recreate the individual proteins
			individualProteins = null;
		}
	}

	public void clearLinks() {
		linksToPSMs.clear();
		linksToPeptides.clear();
		// force to recreate the individual proteins
		individualProteins = null;
	}

}
