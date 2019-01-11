package edu.scripps.yates.server.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.PsmAmount;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmScore;
import edu.scripps.yates.proteindb.persistence.mysql.Ptm;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.Adapter;
import edu.scripps.yates.proteindb.queries.semantic.LinkBetweenQueriableProteinSetAndPSM;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePsm;
import edu.scripps.yates.server.cache.ServerCachePSMBeansByPSMDBId;
import edu.scripps.yates.shared.model.MSRunBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.util.SharedConstants;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TObjectIntHashMap;

public class PSMBeanAdapter implements Adapter<PSMBean> {
	private static ThreadLocal<TObjectIntHashMap<String>> staticPsmIdentifierByRunID = new ThreadLocal<TObjectIntHashMap<String>>();
	private static int staticPsmIdentifier = 0;
	private final QueriablePsm queriablePsm;
	private final Collection<String> hiddenPTMs;

	public static void clearStaticMap() {
		if (staticPsmIdentifierByRunID.get() != null) {
			staticPsmIdentifierByRunID.get().clear();
		}
	}

	public PSMBeanAdapter(QueriablePsm queriablePsm, Collection<String> hiddenPTMs) {
		this.queriablePsm = queriablePsm;
		this.hiddenPTMs = hiddenPTMs;
		initializeMap();
	}

	private void initializeMap() {
		if (staticPsmIdentifierByRunID.get() == null) {
			staticPsmIdentifierByRunID.set(new TObjectIntHashMap<String>());
		}
	}

	@Override
	public PSMBean adapt() {
		final Psm psm = queriablePsm.getPsm();
		if (ServerCachePSMBeansByPSMDBId.getInstance().contains(psm.getId())) {
			final PSMBean psmBean = ServerCachePSMBeansByPSMDBId.getInstance().getFromCache(psm.getId());
			// even if the psmBean was already created, add the corresponding
			// proteinbeans

			addProteinBeans(psmBean, queriablePsm);
			return psmBean;
		}
		final PSMBean ret = new PSMBean();
		ServerCachePSMBeansByPSMDBId.getInstance().addtoCache(ret, psm.getId());
		ret.setDbID(psm.getId());
		ret.setChargeState(psm.getChargeState());
		ret.setCalculatedMH(psm.getCalMh());
		ret.setExperimentalMH(psm.getMh());
		final Set<Ptm> ptms = psm.getPtms();
		ret.setFullSequence(getFullSequence(psm.getFullSequence(), psm.getSequence(), ptms, hiddenPTMs));
		ret.setIonProportion(psm.getIonProportion());
		ret.setMassErrorPPM(psm.getPpmError());
		final MSRunBean msRunBean = new MSRunBeanAdapter(psm.getMsRun(), false).adapt();
		ret.setMsRun(msRunBean);
		ret.setPi(psm.getPi());
		if (psm.getPsmId() != null) {
			ret.setPsmID(psm.getPsmId());
		} else {
			// if run is not null, use it for the identifier
			if (msRunBean != null)
				ret.setPsmID(getNextIdentifier(msRunBean.getRunID()));
			else
				ret.setPsmID(getNextIdentifier(null));
		}
		ret.setSequence(psm.getSequence());
		ret.setSpr(psm.getSpr());
		ret.setTotalIntensity(psm.getTotalIntensity());
		if (psm.getPsmAmounts() != null) {
			for (final Object obj : psm.getPsmAmounts()) {
				final PsmAmount amount = (PsmAmount) obj;
				ret.addAmount(new AmountBeanAdapter(amount, psm.getMsRun()).adapt());
			}
		}
		if (psm.getPsmRatioValues() != null) {
			for (final Object obj : psm.getPsmRatioValues()) {
				ret.addRatio(new PSMRatioBeanAdapter((PsmRatioValue) obj).adapt());
			}
		}
		if (queriablePsm.getLinks() != null) {
			addProteinBeans(ret, queriablePsm);

		}
		if (psm.getPsmScores() != null) {
			if (SharedConstants.ADAPT_PSM_SCORES) {
				for (final Object obj : psm.getPsmScores()) {
					final PsmScore score = (PsmScore) obj;
					ret.addScore(new ScoreBeanAdapter(String.valueOf(score.getValue()), score.getName(),
							score.getConfidenceScoreType()).adapt());
				}
			}
		}

		if (ptms != null) {
			for (final Object obj : ptms) {
				final Ptm ptm = (Ptm) obj;
				// skip PTMs in the list of hidden PTMs
				if (hiddenPTMs != null && hiddenPTMs.contains(ptm.getName()))
					continue;
				ret.addPtm(new PTMBeanAdapter(ptm).adapt());
			}
		}
		// conditions
		if (psm.getConditions() != null) {
			for (final Object obj : psm.getConditions()) {
				final Condition condition = (Condition) obj;
				ret.addCondition(new ConditionBeanAdapter(condition, true).adapt());
			}
		}
		return ret;
	}

	private void addProteinBeans(PSMBean psmBean, QueriablePsm queriablePsm2) {
		// List<AccessionBean> primaryAccessions = new
		// ArrayList<AccessionBean>();

		for (final LinkBetweenQueriableProteinSetAndPSM link : queriablePsm2.getLinks()) {
			final QueriableProteinSet queriableProtein = link.getQueriableProtein();
			final TIntIterator iterator = queriableProtein.getProteinDBIds().iterator();
			while (iterator.hasNext()) {
				psmBean.getProteinDBIds().add(iterator.next());
			}

			// final AccessionBean accession = new
			// AccessionBeanAdapter(queriableProtein.getPrimaryProteinAccession())
			// .adapt();
			// if (!primaryAccessions.contains(accession)) {
			// primaryAccessions.add(accession);
			// }

			if (queriableProtein.getOrganism() != null && queriableProtein.getOrganism().getName() != null) {
				psmBean.addOrganism(new OrganismBeanAdapter(queriableProtein.getOrganism()).adapt());
			}
			// if (addProteins) {
			// final ProteinBean proteinBean = new ProteinBeanAdapter(
			// protein).adapt();
			// primaryAccessions.add(proteinBean.getPrimaryAccession());
			// ret.addProtein(proteinBean);
			// }
		}

	}

	/**
	 * Gets the full sequence of a PSM but avoiding the parenthesis of the
	 * modification stated in the hiddenPTMs
	 *
	 * @param psm
	 * @param hiddenPTMs2
	 * @return
	 */
	protected static String getFullSequence(String fullSequence, String cleanSequence, Set<Ptm> ptms,
			Collection<String> hiddenPTMs) {
		try {
			if (fullSequence == null) {
				return cleanSequence;
			}
			if (hiddenPTMs == null || hiddenPTMs.isEmpty() || ptms == null ||
			// this means that it doesnt have ptms
			// ptms.isEmpty() is an expensive query
					(!fullSequence.contains("(") && !fullSequence.contains("["))) {
				return fullSequence;
			}
			final List<Integer> ptmsPositionsToHidde = new ArrayList<Integer>();
			for (final Ptm ptm : ptms) {
				if (hiddenPTMs != null && hiddenPTMs.contains(ptm.getName())) {
					final Set<PtmSite> ptmSites = ptm.getPtmSites();
					if (ptmSites != null) {
						for (final PtmSite ptmSite : ptmSites) {
							ptmsPositionsToHidde.add(ptmSite.getPosition());
						}
					}
				}
			}
			int positionOnCleanSequence = 0;
			int positionOnAnnotatedSequence = 0;
			final StringBuilder sb = new StringBuilder();
			boolean skipthisPTM = false;
			boolean insideParenthesis = false;
			boolean firstPoint = false;
			while (positionOnAnnotatedSequence < fullSequence.length()) {
				if (ptmsPositionsToHidde.contains(positionOnCleanSequence))
					skipthisPTM = true;

				final char annotatedSequenceAA = fullSequence.charAt(positionOnAnnotatedSequence);
				if (annotatedSequenceAA == '.' && firstPoint == false && !insideParenthesis) {
					firstPoint = true;
					positionOnCleanSequence = -1;
				}

				if (annotatedSequenceAA == '(')
					insideParenthesis = true;

				if (!skipthisPTM || !insideParenthesis)
					sb.append(annotatedSequenceAA);

				if (!insideParenthesis || !skipthisPTM) {
					if (positionOnCleanSequence < cleanSequence.length())
						positionOnCleanSequence++;
				}

				if (annotatedSequenceAA == ')') {
					insideParenthesis = false;
					skipthisPTM = false;
				}

				positionOnAnnotatedSequence++;
			}

			return sb.toString();
		} catch (final Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return null;
	}

	private static String getNextIdentifier(String runID) {
		if (runID != null && !"".equals(runID)) {
			Integer integer = 1;
			if (staticPsmIdentifierByRunID.get().containsKey(runID)) {
				integer = staticPsmIdentifierByRunID.get().get(runID) + 1;
			}
			staticPsmIdentifierByRunID.get().put(runID, integer);
			return runID + "_" + staticPsmIdentifierByRunID.get().get(runID);
		}
		return String.valueOf(++staticPsmIdentifier);
	}

}
