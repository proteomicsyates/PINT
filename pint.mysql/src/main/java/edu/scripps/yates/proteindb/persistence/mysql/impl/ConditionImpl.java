package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;

public class ConditionImpl implements Condition {

	private final edu.scripps.yates.proteindb.persistence.mysql.Condition hibExperimentalCondition;
	private Sample sample;
	protected final static HashMap<Integer, Set<Protein>> proteinsByCondition = new HashMap<Integer, Set<Protein>>();
	protected final static HashMap<Integer, Set<PSM>> psmsByCondition = new HashMap<Integer, Set<PSM>>();
	protected final static HashMap<Integer, Set<Peptide>> peptidesByCondition = new HashMap<Integer, Set<Peptide>>();
	protected final static HashMap<Integer, Condition> conditionsMap = new HashMap<Integer, Condition>();

	public ConditionImpl(
			edu.scripps.yates.proteindb.persistence.mysql.Condition experimentalCondition) {
		hibExperimentalCondition = experimentalCondition;
		conditionsMap.put(hibExperimentalCondition.getId(), this);
	}

	@Override
	public Sample getSample() {
		if (sample == null) {
			if (SampleImpl.samplesMap.containsKey(hibExperimentalCondition
					.getSample().getId())) {
				sample = SampleImpl.samplesMap.get(hibExperimentalCondition
						.getSample().getId());
			} else {
				sample = new SampleImpl(hibExperimentalCondition.getSample());
			}
		}
		return sample;
	}

	@Override
	public String getUnit() {
		return hibExperimentalCondition.getUnit();
	}

	@Override
	public Double getValue() {
		return hibExperimentalCondition.getValue();
	}

	@Override
	public String getDescription() {
		return hibExperimentalCondition.getDescription();
	}

	@Override
	public String getName() {
		return hibExperimentalCondition.getName();
	}

	@Override
	public Project getProject() {
		final edu.scripps.yates.proteindb.persistence.mysql.Project hibProject = hibExperimentalCondition
				.getProject();
		if (ProjectImpl.projectsMap.containsKey(hibProject.getId())) {
			return ProjectImpl.projectsMap.get(hibProject.getId());
		} else {
			Project project = new ProjectImpl(hibProject);
			return project;
		}
	}

	@Override
	public Set<Protein> getProteins() {
		Set<Protein> ret = new HashSet<Protein>();

		if (proteinsByCondition.containsKey(hibExperimentalCondition.getId())) {
			ret.addAll(proteinsByCondition.get(hibExperimentalCondition.getId()));
		} else {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Protein> proteins = hibExperimentalCondition
					.getProteins();
			for (edu.scripps.yates.proteindb.persistence.mysql.Protein hibProtein : proteins) {
				Protein protein = null;
				if (ProteinImpl.proteinMap.containsKey(hibProtein.getId())) {
					ret.add(ProteinImpl.proteinMap.get(hibProtein.getId()));
				} else {
					protein = new ProteinImpl(hibProtein);
					ret.add(protein);
				}
				// add to the map by condition
				if (!proteinsByCondition.containsKey(hibExperimentalCondition
						.getId())) {
					Set<Protein> list = new HashSet<Protein>();
					list.add(protein);
					proteinsByCondition.put(hibExperimentalCondition.getId(),
							list);
				} else {
					proteinsByCondition.get(hibExperimentalCondition.getId())
							.add(protein);
				}
			}
		}

		return ret;
	}

	@Override
	public Set<PSM> getPSMs() {
		Set<PSM> ret = new HashSet<PSM>();

		if (psmsByCondition.containsKey(hibExperimentalCondition.getId())) {
			ret.addAll(psmsByCondition.get(hibExperimentalCondition.getId()));
		} else {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Psm> hibPsms = hibExperimentalCondition
					.getPsms();
			for (Psm hibPsm : hibPsms) {
				PSM psm = new PSMImpl(hibPsm);
				ret.add(psm);
				if (!psmsByCondition.containsKey(hibExperimentalCondition
						.getId())) {
					Set<PSM> list = new HashSet<PSM>();
					list.add(psm);
					psmsByCondition.put(hibExperimentalCondition.getId(), list);
				} else {
					psmsByCondition.get(hibExperimentalCondition.getId()).add(
							psm);
				}
			}
		}

		return ret;
	}

	@Override
	public Set<Peptide> getPeptides() {
		Set<Peptide> ret = new HashSet<Peptide>();

		if (peptidesByCondition.containsKey(hibExperimentalCondition.getId())) {
			ret.addAll(peptidesByCondition.get(hibExperimentalCondition.getId()));
		} else {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Peptide> hibPeptides = hibExperimentalCondition
					.getPeptides();
			for (edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide : hibPeptides) {
				Peptide peptide = new PeptideImpl(hibPeptide);
				ret.add(peptide);
				if (!peptidesByCondition.containsKey(hibExperimentalCondition
						.getId())) {
					Set<Peptide> list = new HashSet<Peptide>();
					list.add(peptide);
					peptidesByCondition.put(hibExperimentalCondition.getId(),
							list);
				} else {
					peptidesByCondition.get(hibExperimentalCondition.getId())
							.add(peptide);
				}
			}
		}

		return ret;
	}

}
