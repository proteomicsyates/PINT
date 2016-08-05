package edu.scripps.yates.proteindb.persistence.mysql.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.Sample;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;

public class ConditionAdapter
		implements Adapter<edu.scripps.yates.proteindb.persistence.mysql.Condition>, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 453472663462624567L;
	private final static Logger log = Logger.getLogger(ConditionAdapter.class);
	private final edu.scripps.yates.utilities.proteomicsmodel.Condition expConditionModel;
	private final Project hibProject;
	public final static Map<Integer, edu.scripps.yates.proteindb.persistence.mysql.Condition> map = new HashMap<Integer, edu.scripps.yates.proteindb.persistence.mysql.Condition>();
	public final static Map<String, edu.scripps.yates.proteindb.persistence.mysql.Condition> conditionsByNameAndProject = new HashMap<String, edu.scripps.yates.proteindb.persistence.mysql.Condition>();

	/**
	 *
	 * @param experimentalCondition
	 * @param project
	 */
	public ConditionAdapter(edu.scripps.yates.utilities.proteomicsmodel.Condition experimentalCondition,
			Project hibProject) {
		expConditionModel = experimentalCondition;
		this.hibProject = hibProject;

	}

	@Override
	public edu.scripps.yates.proteindb.persistence.mysql.Condition adapt() {
		if (map.containsKey(expConditionModel.hashCode())) {
			return map.get(expConditionModel.hashCode());
		}
		if (conditionsByNameAndProject
				.containsKey(expConditionModel.getName() + expConditionModel.getProject().getTag())) {
			return conditionsByNameAndProject
					.get(expConditionModel.getName() + expConditionModel.getProject().getTag());
		}

		final Sample hibSample = new SampleAdapter(expConditionModel.getSample()).adapt();
		edu.scripps.yates.proteindb.persistence.mysql.Condition ret = new Condition(hibProject, hibSample,
				expConditionModel.getName());
		map.put(expConditionModel.hashCode(), ret);
		conditionsByNameAndProject.put(expConditionModel.getName() + expConditionModel.getProject().getTag(), ret);
		ret.setDescription(expConditionModel.getDescription());
		ret.setUnit(expConditionModel.getUnit());
		ret.setValue(expConditionModel.getValue());

		log.info("Adapting experimental condition: " + expConditionModel.getName() + " from project: "
				+ hibProject.getName());

		// get all proteins with accessions from Uniprot and without
		// description or without length or without MW
		annotateMissingProteinsFromUniprot();

		final Set<Protein> proteins = expConditionModel.getProteins();
		if (proteins != null && !proteins.isEmpty()) {
			final int size = proteins.size();
			log.info("containing " + size + " proteins");

			int i = 0;
			final Iterator<Protein> iterator = proteins.iterator();
			while (iterator.hasNext()) {
				i++;
				if (i % 100 == 0) {
					log.debug(i * 100 / size + "% of proteins adapted in condition " + expConditionModel.getName()
							+ " from project: " + hibProject.getTag());
				}
				// log.debug(++i + "/" + size + " protein amounts");
				Protein protein = iterator.next();
				final Set<Peptide> peptides = protein.getPeptides();

				for (Peptide peptide : peptides) {
					final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide = new PeptideAdapter(peptide,
							hibProject, false).adapt();
					ret.getPeptides().add(hibPeptide);
				}

				final Set<PSM> psMs = protein.getPSMs();
				for (PSM psm : psMs) {
					final Psm hibPSM = new PSMAdapter(psm, hibProject, false).adapt();
					ret.getPsms().add(hibPSM);
				}

				// ret.getProteins().add(new ProteinAdapter(protein, hibProject,
				// false).adapt());

				final edu.scripps.yates.proteindb.persistence.mysql.Protein hibProtein = new ProteinAdapter(protein,
						hibProject, true).adapt();
				ret.getProteins().add(hibProtein);
			}

			// now try to relate all the objects
			log.info("Relating identification objects on condition " + ret.getName());

			for (Protein protein : proteins) {
				final edu.scripps.yates.proteindb.persistence.mysql.Protein hibProtein = new ProteinAdapter(protein,
						hibProject, false).adapt();
				final Set<Peptide> peptides2 = protein.getPeptides();
				for (Peptide peptide : peptides2) {
					final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide = new PeptideAdapter(peptide,
							hibProject, false).adapt();
					hibProtein.getPeptides().add(hibPeptide);
					hibPeptide.getProteins().add(hibProtein);
					final Set<PSM> psms = peptide.getPSMs();
					for (PSM psm : psms) {
						final Psm hibPSM = new PSMAdapter(psm, hibProject, false).adapt();
						hibPSM.setPeptide(hibPeptide);
						hibPSM.getProteins().add(hibProtein);
						hibPeptide.getPsms().add(hibPSM);
						hibProtein.getPsms().add(hibPSM);
					}
				}
				final Set<PSM> psms2 = protein.getPSMs();
				for (PSM psm2 : psms2) {
					final Psm hibPSM = new PSMAdapter(psm2, hibProject, false).adapt();
					hibProtein.getPsms().add(hibPSM);
					hibPSM.getProteins().add(hibProtein);
					final Peptide peptide = psm2.getPeptide();
					final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide = new PeptideAdapter(peptide,
							hibProject, false).adapt();
					hibPSM.setPeptide(hibPeptide);
					hibPeptide.getProteins().add(hibProtein);
					hibPeptide.getPsms().add(hibPSM);

				}
			}
			log.info("Relationships done");
		} else {
			log.info("Condition without proteins");
			log.info("Getting PSMs and Peptides directly from condition");
			final Set<PSM> psMs = expConditionModel.getPSMs();
			log.debug(psMs.size() + " psms in condition " + expConditionModel.getName());
			for (PSM psm : psMs) {
				final Psm hibPSM = new PSMAdapter(psm, hibProject, false).adapt();
				final edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide = new PeptideAdapter(
						psm.getPeptide(), hibProject, false).adapt();
				hibPSM.setPeptide(hibPeptide);
				ret.getPsms().add(hibPSM);
				ret.getPeptides().add(hibPeptide);
			}
		}

		// final Set<PSMAmount> peptideAmounts = expConditionModel
		// .getPeptideAmounts();
		// if (peptideAmounts != null && !peptideAmounts.isEmpty()) {
		// final int size = peptideAmounts.size();
		// log.info("containing " + size + " peptide amounts");
		//
		// int i = 0;
		// final Iterator<PSMAmount> iterator = peptideAmounts.iterator();
		// while (iterator.hasNext()) {
		// // log.debug(++i + "/" + size + " peptide amounts");
		// PSMAmount peptideAmount = null;
		// try {
		// peptideAmount = iterator.next();
		// } catch (ConcurrentModificationException e) {
		// e.printStackTrace();
		// log.info(e.getMessage());
		// }
		// objectAdapted.getPeptideAmounts().add(
		// new PeptideAmountAdapter(peptideAmount, hibProject,
		// objectAdapted).adapt());
		// }
		// }

		return ret;
	}

	private void annotateMissingProteinsFromUniprot() {
		Set<String> uniprotAccs = new HashSet<String>();
		final Set<Protein> proteins = expConditionModel.getProteins();
		for (Protein protein : proteins) {
			final Accession primaryAccession = protein.getPrimaryAccession();
			if (!uniprotAccs.contains(primaryAccession.getAccession())
					&& primaryAccession.getAccessionType().equals(AccessionType.UNIPROT)) {
				if (primaryAccession.getDescription() == null || protein.getLength() <= 0 || protein.getMW() <= 0) {
					uniprotAccs.add(primaryAccession.getAccession());
				}
			}
		}

		// get all the information together
		// they will be stored locally for further accession on the protein
		// adapter
		if (!uniprotAccs.isEmpty()) {
			log.info("Retrieving information from Uniprot of " + uniprotAccs.size() + " proteins");
			UniprotProteinRetriever uplr = new UniprotProteinRetriever(null,
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex());
			final Map<String, Protein> annotatedProteins = uplr.getAnnotatedProteins(uniprotAccs);
			log.info(annotatedProteins.size() + " Proteins retrieved from Uniprot");
		}
	}

	protected static void clearStaticInformation() {
		map.clear();
	}
}
