package edu.scripps.yates.utilities.proteomicsmodel.staticstorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.MSRun;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.staticstorage.ItemStorage;

/**
 * This class is intended to storage {@link Protein}s {@link Peptide}s and
 * {@link PSM}s that are being created during import process, in a way that can
 * be retrieved by {@link Condition}, {@link MSRun} or rowIndex
 *
 * @author Salva
 *
 */
public class ProteomicsModelStaticStorage {
	private static final Logger log = Logger.getLogger(ProteomicsModelStaticStorage.class);
	private static final ItemStorage<Protein> proteinStorage = new ItemStorage<Protein>();
	private static final ItemStorage<Peptide> peptideStorage = new ItemStorage<Peptide>();
	private static final ItemStorage<PSM> psmStorage = new ItemStorage<PSM>();

	public static void clearData() {
		proteinStorage.clearData();
		peptideStorage.clearData();
		psmStorage.clearData();
	}

	public static void addProtein(Protein protein, String msRunID, String conditionID) {
		addProtein(protein, msRunID, conditionID, -1);
	}

	public static void addProtein(Protein protein, MSRun msRun, String conditionID) {
		addProtein(protein, msRun.getRunId(), conditionID);
	}

	public static void addProtein(Protein protein, String msRunID, String conditionID, int excelRowIndex) {
		proteinStorage.add(protein, msRunID, conditionID, excelRowIndex, protein.getAccession());
		if (protein.getSecondaryAccessions() != null) {
			for (Accession secondaryAccession : protein.getSecondaryAccessions()) {
				proteinStorage.add(protein, msRunID, conditionID, excelRowIndex, secondaryAccession.getAccession());
			}
		}
	}

	public static void addPeptide(Peptide peptide, String msRunID, String conditionID) {
		addPeptide(peptide, msRunID, conditionID, -1);
	}

	public static void addPeptide(Peptide peptide, MSRun msRun, String conditionID) {
		addPeptide(peptide, msRun.getRunId(), conditionID);
	}

	public static void addPeptide(Peptide peptide, String msRunID, String conditionID, int excelRowIndex) {
		peptideStorage.add(peptide, msRunID, conditionID, excelRowIndex, peptide.getSequence());
	}

	public static void addPSM(PSM psm, String runID, String conditionID) {
		addPSM(psm, runID, conditionID, -1);
	}

	public static void addPSM(PSM psm, MSRun msRun, String conditionID) {
		addPSM(psm, msRun.getRunId(), conditionID);
	}

	public static void addPSM(PSM psm, String runID, String conditionID, int excelRowIndex) {
		psmStorage.add(psm, runID, conditionID, excelRowIndex, psm.getPSMIdentifier());
	}

	public static boolean containsProtein(String msRunID, String conditionID, String accession) {
		return containsProtein(msRunID, conditionID, -1, accession);
	}

	public static boolean containsProtein(String msRunID, String conditionID, int excelRowIndex, String accession) {
		return proteinStorage.contains(msRunID, conditionID, excelRowIndex, accession);
	}

	public static boolean containsPSM(String msRun, String conditionID, String psmID) {
		return containsPSM(msRun, conditionID, -1, psmID);
	}

	public static boolean containsPSM(MSRun msRun, String conditionID, String psmID) {
		return containsPSM(msRun.getRunId(), conditionID, psmID);
	}

	public static boolean containsPSM(String msRun, String conditionID, int excelRowIndex, String psmID) {
		String psmId2 = psmID;
		if (psmId2 == null) {
			psmId2 = (excelRowIndex + 1) + "-" + msRun;
		}
		return psmStorage.contains(msRun, conditionID, excelRowIndex, psmId2);
	}

	public static boolean containsPeptide(String msRunID, String conditionID, String sequence) {
		return containsPeptide(msRunID, conditionID, -1, sequence);
	}

	public static boolean containsPeptide(MSRun msRun, String conditionID, String sequence) {
		return containsPeptide(msRun.getRunId(), conditionID, sequence);
	}

	public static boolean containsPeptide(String msRunID, String conditionID, int excelRowIndex, String sequence) {
		return peptideStorage.contains(msRunID, conditionID, excelRowIndex, FastaParser.cleanSequence(sequence));
	}

	public static boolean containsPeptide(MSRun msRun, String conditionID, int excelRowIndex, String sequence) {
		return containsPeptide(msRun.getRunId(), conditionID, excelRowIndex, sequence);
	}

	public static Set<Protein> getProtein(String msRunID, String conditionID, String accession) {
		return getProtein(msRunID, conditionID, -1, accession);
	}

	public static Set<Protein> getProtein(Collection<String> msRunIDs, String conditionID, String accession) {
		Set<Protein> set = new HashSet<Protein>();
		for (String msRunID : msRunIDs) {
			set.addAll(getProtein(msRunID, conditionID, -1, accession));
		}
		return set;
	}

	public static Set<Protein> getProtein(Collection<String> msRunIDs, String conditionID, int excelRowIndex,
			String accession) {
		Set<Protein> set = new HashSet<Protein>();
		for (String msRunID : msRunIDs) {
			set.addAll(proteinStorage.get(msRunID, conditionID, excelRowIndex, accession));
		}
		return set;
	}

	public static Set<Protein> getProtein(String msRunID, String conditionID, int excelRowIndex, String accession) {
		return proteinStorage.get(msRunID, conditionID, excelRowIndex, accession);
	}

	public static Set<PSM> getPSM(String runID, String conditionID, String psmID) {
		return getPSM(runID, conditionID, -1, psmID);
	}

	public static Set<PSM> getPSM(MSRun msRun, String conditionID, String psmID) {
		return getPSM(msRun.getRunId(), conditionID, psmID);
	}

	public static PSM getSinglePSM(MSRun msRun, String conditionID, String psmID) {
		final Set<PSM> psms = getPSM(msRun, conditionID, psmID);
		if (psms == null || psms.isEmpty()) {
			return null;
		}
		if (psms.size() > 1) {
			log.warn("Retrieved psms are multiple!");
		}
		return psms.iterator().next();
	}

	public static Set<PSM> getPSM(String runID, String conditionID, int excelRowIndex, String psmID) {
		String psmId2 = psmID;
		if (psmId2 == null) {
			psmId2 = (excelRowIndex + 1) + "-" + runID;
		}
		return psmStorage.get(runID, conditionID, excelRowIndex, psmId2);
	}

	public static Set<Peptide> getPeptide(String msRunID, String conditionID, String sequence) {
		return getPeptide(msRunID, conditionID, -1, sequence);
	}

	public static Set<Peptide> getPeptide(MSRun msRun, String conditionID, String sequence) {
		return getPeptide(msRun.getRunId(), conditionID, sequence);
	}

	public static Peptide getSinglePeptide(MSRun msRun, String conditionID, String sequence) {
		final Set<Peptide> peptides = getPeptide(msRun, conditionID, sequence);
		if (peptides == null || peptides.isEmpty()) {
			return null;
		}
		if (peptides.size() > 1) {
			log.warn("Retrieved peptides are multiple!");
		}
		return peptides.iterator().next();
	}

	public static Protein getSingleProtein(String msRunID, String conditionID, String accession) {
		final Set<Protein> proteins = getProtein(msRunID, conditionID, accession);
		if (proteins == null || proteins.isEmpty()) {
			return null;
		}
		if (proteins.size() > 1) {
			log.warn("Retrieved proteins are multiple!");
		}
		return proteins.iterator().next();
	}

	public static Set<Peptide> getPeptide(String msRunID, String conditionID, int excelRowIndex, String sequence) {
		return peptideStorage.get(msRunID, conditionID, excelRowIndex, sequence);
	}

	public static Set<Peptide> getPeptide(Collection<String> msRunIDs, String conditionID, String sequence) {
		Set<Peptide> set = new HashSet<Peptide>();
		for (String msRunID : msRunIDs) {
			set.addAll(getPeptide(msRunID, conditionID, -1, sequence));
		}
		return set;
	}

	public static Set<Peptide> getPeptide(Collection<String> msRunIDs, String conditionID, int excelRowIndex,
			String sequence) {
		Set<Peptide> set = new HashSet<Peptide>();
		for (String msRunID : msRunIDs) {
			set.addAll(peptideStorage.get(msRunID, conditionID, excelRowIndex, sequence));
		}
		return set;

	}
}
