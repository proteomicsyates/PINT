package edu.scripps.yates.proteindb.queries.semantic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Organism;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideScore;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAmount;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.AmountTypeAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.tablemapper.ConditionToPeptideTableMapper;
import edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class QueriablePeptideSet {
	private final List<Peptide> peptides = new ArrayList<Peptide>();
	private final Set<LinkBetweenQueriableProteinSetAndPeptideSet> links = new THashSet<LinkBetweenQueriableProteinSetAndPeptideSet>();
	private List<Peptide> individualPeptides;
	private Set<Condition> conditions;
	private Set<PeptideScore> peptideScores;
	private Set<MsRun> msRuns;
	private Set<PeptideRatioValue> peptideRatios;
	private Set<PeptideAmount> peptideAmounts;
	private Set<Ptm> ptms;
	private static final Map<String, QueriablePeptideSet> map = new THashMap<String, QueriablePeptideSet>();
	private final static Logger log = Logger.getLogger(QueriablePeptideSet.class);

	public static QueriablePeptideSet getInstance(Collection<Peptide> peptides, boolean forceToCreateNewObject) {
		final String id = getPeptidesID(peptides);
		if (!forceToCreateNewObject && map.containsKey(id)) {
			final QueriablePeptideSet queriablePeptide = map.get(id);
			return queriablePeptide;

		} else {
			final QueriablePeptideSet queriablePeptide = new QueriablePeptideSet(peptides);
			map.put(id, queriablePeptide);
			return queriablePeptide;
		}
	}

	private static String getPeptidesID(Collection<Peptide> peptides) {
		final List<Integer> individualIds = new ArrayList<Integer>();
		for (final Peptide peptide : peptides) {
			individualIds.add(peptide.getId());
		}
		Collections.sort(individualIds);
		final StringBuilder sb = new StringBuilder();
		for (final Integer integer : individualIds) {
			sb.append(integer);
		}
		return sb.toString();
	}

	private QueriablePeptideSet(Collection<Peptide> peptides) {
		this.peptides.addAll(peptides);
		// quality check
		final Set<String> fullSequence = peptides.stream().map(p -> p.getFullSequence()).collect(Collectors.toSet());
		if (fullSequence.size() != 1) {
			log.info("wrong");
		}
	}

	/**
	 * @return the peptide
	 */
	public List<Peptide> getIndividualPeptides() {
		if (individualPeptides == null) {
			individualPeptides = new ArrayList<Peptide>();
			for (final Peptide peptide : peptides) {
				individualPeptides.add(peptide);
			}
		}
		return individualPeptides;
	}

	/**
	 * @return the links
	 */
	public Set<LinkBetweenQueriableProteinSetAndPeptideSet> getLinksToProteins() {
		return links;
	}

	public void removeProteinSetLink(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		final boolean removed = links.remove(link);
		if (!removed)
			log.warn("BAD");
	}

	public Set<Organism> getOrganisms() {
		final Set<String> organismNames = new THashSet<String>();
		final Set<Organism> ret = new THashSet<Organism>();
		final Set<LinkBetweenQueriableProteinSetAndPeptideSet> links = getLinksToProteins();
		for (final LinkBetweenQueriableProteinSetAndPeptideSet link : links) {
			final Organism organism = link.getQueriableProtein().getOrganism();
			if (!organismNames.contains(organism.getName())) {
				organismNames.add(organism.getName());
				ret.add(organism);
			}
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getFullSequence();
	}

	private List<Peptide> getPeptides() {
		return peptides;
	}

	public Set<Condition> getConditions() {
		if (conditions == null) {
			conditions = new THashSet<Condition>();
			for (final Peptide peptide : getIndividualPeptides()) {
				final List<Condition> conditions2 = ConditionToPeptideTableMapper.getInstance().getConditions(peptide);
				conditions.addAll(conditions2);
			}
		}
		return conditions;
	}

	public String getFullSequence() {
		return getPeptides().get(0).getFullSequence();
	}

	public void clearLinks() {
		links.clear();

	}

	public void addLink(LinkBetweenQueriableProteinSetAndPeptideSet link) {
		links.add(link);
	}

	public Set<PeptideScore> getPeptideScores() {
		if (peptideScores == null) {
			peptideScores = new THashSet<PeptideScore>();
			for (final Peptide peptide : getIndividualPeptides()) {
				peptideScores.addAll(peptide.getPeptideScores());
			}
		}
		return peptideScores;
	}

	public Set<MsRun> getMsRuns() {
		if (msRuns == null) {
			msRuns = new THashSet<MsRun>();
			for (final Peptide peptide : getIndividualPeptides()) {
				msRuns.addAll(peptide.getMsRuns());
			}
		}
		return msRuns;
	}

	public Set<PeptideRatioValue> getPeptideRatioValues() {
		if (peptideRatios == null) {
			peptideRatios = new THashSet<PeptideRatioValue>();
			for (final Peptide peptide : getIndividualPeptides()) {
				peptideRatios.addAll(peptide.getPeptideRatioValues());
			}
		}
		return peptideRatios;
	}

	public Set<Psm> getPsms() {
		final Set<Psm> ret = new THashSet<Psm>();
		for (final Peptide peptide : getIndividualPeptides()) {
			ret.addAll(peptide.getPsms());
		}
		return ret;
	}

	/**
	 * Gets a {@link Set} of {@link ProteinAmount} from the {@link Protein},
	 * assuring that they are linked to the current session
	 * 
	 * @param amountType
	 *
	 * @return
	 */

	public Set<PeptideAmount> getPeptideAmounts(AmountType amountType) {
		if (peptideAmounts == null) {
			peptideAmounts = new THashSet<PeptideAmount>();

			for (final Peptide peptide : getIndividualPeptides()) {
				boolean hasSPC = false;
				if (AmountType.SPC != amountType) {
					final Set<PeptideAmount> peptideAmounts2 = peptide.getPeptideAmounts();
					for (PeptideAmount peptideAmount : peptideAmounts2) {
						if (peptideAmount.getAmountType().getName().equalsIgnoreCase(
								edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType.SPC.name())) {
							hasSPC = true;
						}
						if (!ContextualSessionHandler.getCurrentSession().contains(peptideAmount)) {
							peptideAmount = (PeptideAmount) ContextualSessionHandler.getCurrentSession()
									.merge(peptideAmount);
						}
						peptideAmounts.add(peptideAmount);
					}
				}
				if (!hasSPC) {
					final Set<Condition> conditions2 = peptide.getConditions();
					for (final Condition condition : conditions2) {
						final PeptideAmount spc = new PeptideAmount(peptide,
								new AmountTypeAdapter(edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType.SPC)
										.adapt(),
								condition, peptide.getNumPsms());
						peptideAmounts.add(spc);
					}
				}
			}
		}
		return peptideAmounts;
	}

	public Set<Ptm> getPtms() {
		if (ptms == null) {
			ptms = new THashSet<Ptm>();
			for (final Peptide peptide : getIndividualPeptides()) {
				final Set<Ptm> ptms2 = peptide.getPtms();
				for (Ptm ptm : ptms2) {
					if (!ContextualSessionHandler.getCurrentSession().contains(ptm)) {
						ptm = (Ptm) ContextualSessionHandler.getCurrentSession().merge(ptm);
					}
					ptms.add(ptm);
				}
			}
		}
		return ptms;
	}

	public String getSequence() {
		return getIndividualPeptides().get(0).getSequence();
	}

}
