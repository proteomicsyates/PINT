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
import gnu.trove.set.hash.THashSet;

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
		Psm psm = queriablePsm.getPsm();
		if (ServerCachePSMBeansByPSMDBId.getInstance().contains(psm.getId())) {
			final PSMBean psmBean = ServerCachePSMBeansByPSMDBId.getInstance().getFromCache(psm.getId());
			// even if the psmBean was already created, add the corresponding
			// proteinbeans

			addProteinBeans(psmBean, queriablePsm);
			return psmBean;
		}
		PSMBean ret = new PSMBean();
		ServerCachePSMBeansByPSMDBId.getInstance().addtoCache(ret, psm.getId());
		ret.setDbID(psm.getId());
		ret.setChargeState(psm.getChargeState());
		ret.setCalculatedMH(psm.getCalMh());
		ret.setExperimentalMH(psm.getMh());
		ret.setFullSequence(getFullSequence(psm, hiddenPTMs));
		ret.setIonProportion(psm.getIonProportion());
		ret.setMassErrorPPM(psm.getPpmError());
		final MSRunBean msRunBean = new MSRunBeanAdapter(psm.getMsRun()).adapt();
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
			for (Object obj : psm.getPsmAmounts()) {
				PsmAmount amount = (PsmAmount) obj;
				ret.addAmount(new AmountBeanAdapter(amount, psm.getMsRun()).adapt());
			}
		}
		if (psm.getPsmRatioValues() != null) {
			for (Object obj : psm.getPsmRatioValues()) {
				ret.addRatio(new PSMRatioBeanAdapter((PsmRatioValue) obj).adapt());
			}
		}
		if (queriablePsm.getLinks() != null) {
			addProteinBeans(ret, queriablePsm);

		}
		if (psm.getPsmScores() != null) {
			if (SharedConstants.ADAPT_PSM_SCORES) {
				for (Object obj : psm.getPsmScores()) {
					PsmScore score = (PsmScore) obj;
					ret.addScore(new ScoreBeanAdapter(String.valueOf(score.getValue()), score.getName(),
							score.getConfidenceScoreType()).adapt());
				}
			}
		}
		if (psm.getPtms() != null) {
			for (Object obj : psm.getPtms()) {
				Ptm ptm = (Ptm) obj;
				// skip PTMs in the list of hidden PTMs
				if (hiddenPTMs != null && hiddenPTMs.contains(ptm.getName()))
					continue;
				ret.addPtm(new PTMBeanAdapter(ptm).adapt());
			}
		}
		// conditions
		if (psm.getConditions() != null) {
			for (Object obj : psm.getConditions()) {
				Condition condition = (Condition) obj;
				ret.addCondition(new ConditionBeanAdapter(condition).adapt());
			}
		}
		return ret;
	}

	private void addProteinBeans(PSMBean psmBean, QueriablePsm queriablePsm2) {
		// List<AccessionBean> primaryAccessions = new
		// ArrayList<AccessionBean>();

		for (LinkBetweenQueriableProteinSetAndPSM link : queriablePsm2.getLinks()) {
			QueriableProteinSet queriableProtein = link.getQueriableProtein();
			TIntIterator iterator = queriableProtein.getProteinDBIds().iterator();
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
	public static String getFullSequence(Psm psm, Collection<String> hiddenPTMs) {
		try {
			final String fullSequence = psm.getFullSequence();
			final Set<Ptm> ptms = psm.getPtms();
			if (ptms == null || ptms.isEmpty() || hiddenPTMs == null || hiddenPTMs.isEmpty())
				return fullSequence;

			List<Integer> ptmsPositionsToHidde = new ArrayList<Integer>();
			for (Ptm ptm : ptms) {
				if (hiddenPTMs != null && hiddenPTMs.contains(ptm.getName())) {
					final Set<PtmSite> ptmSites = ptm.getPtmSites();
					if (ptmSites != null) {
						for (PtmSite ptmSite : ptmSites) {
							ptmsPositionsToHidde.add(ptmSite.getPosition());
						}
					}
				}
			}
			int positionOnCleanSequence = 0;
			int positionOnAnnotatedSequence = 0;
			final String cleanSequence = psm.getSequence();
			StringBuilder sb = new StringBuilder();
			boolean skipthisPTM = false;
			boolean insideParenthesis = false;
			boolean firstPoint = false;
			while (positionOnAnnotatedSequence < fullSequence.length()) {
				if (ptmsPositionsToHidde.contains(positionOnCleanSequence))
					skipthisPTM = true;

				char annotatedSequenceAA = fullSequence.charAt(positionOnAnnotatedSequence);
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
		} catch (Exception e) {
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

	public static void main(String[] args) {
		final String miPTMName = "MI PTM";
		Set<String> hiddenPTMs2 = new THashSet<String>();
		hiddenPTMs2.add(miPTMName);
		Psm psm2 = new Psm(null, null, "TSNGDDSLFFSNFSLLGTPVLK", "K.TSNGDDSLFFSNFSLLGTPVLK(14.123).D");
		Set<Ptm> ptms = new THashSet<Ptm>();
		Set<PtmSite> ptmSites = new THashSet<PtmSite>();
		PtmSite ptmSite = new PtmSite(null, "K", 22);
		ptmSites.add(ptmSite);
		Ptm ptm = new Ptm(psm2, 123.123, miPTMName, "asdf", ptmSites);
		ptms.add(ptm);
		psm2.setPtms(ptms);
		final String fullSequence = PSMBeanAdapter.getFullSequence(psm2, hiddenPTMs2);
		System.out.println(fullSequence);
	}
}
