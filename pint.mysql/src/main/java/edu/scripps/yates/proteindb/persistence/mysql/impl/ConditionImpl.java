package edu.scripps.yates.proteindb.persistence.mysql.impl;

import java.util.Set;

import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.THashSet;

public class ConditionImpl implements Condition {

	private final edu.scripps.yates.proteindb.persistence.mysql.Condition hibExperimentalCondition;
	private Sample sample;
	protected final static TIntObjectHashMap<Set<Protein>> proteinsByCondition = new TIntObjectHashMap<Set<Protein>>();
	protected final static TIntObjectHashMap<Set<PSM>> psmsByCondition = new TIntObjectHashMap<Set<PSM>>();
	protected final static TIntObjectHashMap<Set<Peptide>> peptidesByCondition = new TIntObjectHashMap<Set<Peptide>>();
	protected final static TIntObjectHashMap<Condition> conditionsMap = new TIntObjectHashMap<Condition>();

	public ConditionImpl(edu.scripps.yates.proteindb.persistence.mysql.Condition experimentalCondition) {
		hibExperimentalCondition = experimentalCondition;
		conditionsMap.put(hibExperimentalCondition.getId(), this);
	}

	@Override
	public Sample getSample() {
		if (sample == null) {
			if (SampleImpl.samplesMap.containsKey(hibExperimentalCondition.getSample().getId())) {
				sample = SampleImpl.samplesMap.get(hibExperimentalCondition.getSample().getId());
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
		final edu.scripps.yates.proteindb.persistence.mysql.Project hibProject = hibExperimentalCondition.getProject();
		if (ProjectImpl.projectsMap.containsKey(hibProject.getId())) {
			return ProjectImpl.projectsMap.get(hibProject.getId());
		} else {
			Project project = new ProjectImpl(hibProject);
			return project;
		}
	}

	@Override
	public Set<Protein> getProteins() {
		Set<Protein> ret = new THashSet<Protein>();

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
				if (!proteinsByCondition.containsKey(hibExperimentalCondition.getId())) {
					Set<Protein> list = new THashSet<Protein>();
					list.add(protein);
					proteinsByCondition.put(hibExperimentalCondition.getId(), list);
				} else {
					proteinsByCondition.get(hibExperimentalCondition.getId()).add(protein);
				}
			}
		}

		return ret;
	}

	@Override
	public Set<PSM> getPSMs() {
		Set<PSM> ret = new THashSet<PSM>();

		if (psmsByCondition.containsKey(hibExperimentalCondition.getId())) {
			ret.addAll(psmsByCondition.get(hibExperimentalCondition.getId()));
		} else {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Psm> hibPsms = hibExperimentalCondition.getPsms();
			for (Psm hibPsm : hibPsms) {
				PSM psm = new PSMImpl(hibPsm);
				ret.add(psm);
				if (!psmsByCondition.containsKey(hibExperimentalCondition.getId())) {
					Set<PSM> list = new THashSet<PSM>();
					list.add(psm);
					psmsByCondition.put(hibExperimentalCondition.getId(), list);
				} else {
					psmsByCondition.get(hibExperimentalCondition.getId()).add(psm);
				}
			}
		}

		return ret;
	}

	@Override
	public Set<Peptide> getPeptides() {
		Set<Peptide> ret = new THashSet<Peptide>();

		if (peptidesByCondition.containsKey(hibExperimentalCondition.getId())) {
			ret.addAll(peptidesByCondition.get(hibExperimentalCondition.getId()));
		} else {
			final Set<edu.scripps.yates.proteindb.persistence.mysql.Peptide> hibPeptides = hibExperimentalCondition
					.getPeptides();
			for (edu.scripps.yates.proteindb.persistence.mysql.Peptide hibPeptide : hibPeptides) {
				Peptide peptide = new PeptideImpl(hibPeptide);
				ret.add(peptide);
				if (!peptidesByCondition.containsKey(hibExperimentalCondition.getId())) {
					Set<Peptide> list = new THashSet<Peptide>();
					list.add(peptide);
					peptidesByCondition.put(hibExperimentalCondition.getId(), list);
				} else {
					peptidesByCondition.get(hibExperimentalCondition.getId()).add(peptide);
				}
			}
		}

		return ret;
	}

}
