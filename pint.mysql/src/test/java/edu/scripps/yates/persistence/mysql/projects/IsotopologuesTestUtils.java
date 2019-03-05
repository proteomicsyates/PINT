package edu.scripps.yates.persistence.mysql.projects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Ratio;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.TIntHashSet;
import junit.framework.Assert;

public class IsotopologuesTestUtils {
	public static void lookForRepeatedPSMsBelongingToDifferentConditions(Project project) {
		TIntHashSet psmHashCodes = new TIntHashSet();
		int repeatedPSMs = 0;
		Map<String, List<Condition>> map = new THashMap<String, List<Condition>>();
		for (Condition condition : project.getConditions()) {
			final Set<Peptide> peptides = condition.getPeptides();
			Assert.assertTrue(!peptides.isEmpty());
			final Set<PSM> psMs = condition.getPSMs();
			System.out.println(psMs.size() + " in condition " + condition.getName());
			for (PSM psm : psMs) {
				Assert.assertTrue(psm.getConditions().size() == 2);

				if (psmHashCodes.contains(psm.hashCode())) {
					repeatedPSMs++;
				} else {
					psmHashCodes.add(psm.hashCode());
				}
				if (!map.containsKey(psm.getIdentifier())) {
					List<Condition> conditions = new ArrayList<Condition>();
					conditions.add(condition);
					map.put(psm.getIdentifier(), conditions);
				} else {
					map.get(psm.getIdentifier()).add(condition);
				}
			}
		}
		System.out.println(repeatedPSMs + " repeated PSMs (same hashcode references in different conditions)");
		int psmPresentInOneCondition = 0;
		int psmPresentInTwoConditions = 0;
		for (String psmID : map.keySet()) {
			final List<Condition> list = map.get(psmID);
			if (list.size() == 1) {
				psmPresentInOneCondition++;
			} else if (list.size() == 2) {
				psmPresentInTwoConditions++;
			} else {
				Assert.fail();
			}
		}
		System.out.println(psmPresentInOneCondition + " psmPresentInOneCondition");
		System.out.println(psmPresentInTwoConditions + " psmPresentInTwoConditions");

		Assert.assertTrue(psmPresentInOneCondition == 0);
		Assert.assertTrue(psmPresentInTwoConditions > 0);
	}

	public static void lookForsingletonPSMS(Project project) {
		int numSingletons = 0;

		for (Condition condition : project.getConditions()) {
			int numPSMs = 0;
			final Set<Peptide> peptides = condition.getPeptides();
			Assert.assertTrue(!peptides.isEmpty());
			final Set<PSM> psMs = condition.getPSMs();
			System.out.println(psMs.size() + " in condition " + condition.getName());
			for (PSM psm : psMs) {
				numPSMs++;
				final Set<Amount> amounts = psm.getAmounts();
				if (amounts != null) {
					for (Amount amount : amounts) {
						if (amount.isSingleton())
							numSingletons++;
					}
				}
			}
			System.out.println(numSingletons + " singleton measurements in " + numPSMs + " PSMS at condition "
					+ condition.getName());
			Assert.assertTrue(numSingletons > 0);
		}
	}

	public static void lookForPSMsWithRatios(Project project) {
		int numRatios = 0;

		for (Condition condition : project.getConditions()) {
			int numPSMs = 0;
			final Set<Peptide> peptides = condition.getPeptides();
			Assert.assertTrue(!peptides.isEmpty());
			final Set<PSM> psMs = condition.getPSMs();
			System.out.println(psMs.size() + " in condition " + condition.getName());
			for (PSM psm : psMs) {
				numPSMs++;
				final Set<Ratio> ratios = psm.getRatios();
				if (ratios != null) {
					numRatios += ratios.size();
				}
			}
			System.out.println(
					numRatios + " ratios measurements in " + numPSMs + " PSMS at condition " + condition.getName());
			Assert.assertTrue(numRatios > 0);
		}
	}
}
