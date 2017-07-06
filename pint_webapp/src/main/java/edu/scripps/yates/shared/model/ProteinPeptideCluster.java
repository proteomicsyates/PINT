package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults;

public class ProteinPeptideCluster implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7422548863426356304L;

	private ContainsPeptides peptideProvider;
	private Set<PeptideBean> peptides = new HashSet<PeptideBean>();
	private Set<Integer> peptideUniqueIdentifiers = new HashSet<Integer>();
	private Set<ProteinBean> proteinSet = new HashSet<ProteinBean>();
	private Set<Integer> proteinUniqueIdentifier = new HashSet<Integer>();
	private Map<String, ProteinBean> proteinMap;
	private ProteinPeptideClusterAlignmentResults aligmentResults = new ProteinPeptideClusterAlignmentResults();

	public ProteinPeptideCluster() {

	}

	public ProteinPeptideCluster(ContainsPeptides peptideProvider) {
		if (peptideProvider instanceof ProteinGroupBean) {
			this.peptideProvider = ((ProteinGroupBean) peptideProvider).cloneToLightProteinGroupBean();
		} else if (peptideProvider instanceof ProteinBean) {
			this.peptideProvider = ((ProteinBean) peptideProvider).cloneToLightProteinBean();
		}
		process(peptideProvider);
	}

	private void process(ContainsPeptides peptideProvider2) {
		// insert the peptide provider: proteinGroup or protein
		if (peptideProvider2 instanceof ProteinBean) {
			final ProteinBean protein = (ProteinBean) peptideProvider2;
			proteinSet.add(protein.cloneToLightProteinBean());
		} else {
			final ProteinGroupBean proteinGroup = (ProteinGroupBean) peptideProvider2;
			for (ProteinBean protein : proteinGroup) {
				proteinSet.add(protein.cloneToLightProteinBean());
			}
		}
		final List<PeptideBean> peptides2 = peptideProvider2.getPeptides();
		for (PeptideBean peptideBean : peptides2) {
			if (peptideUniqueIdentifiers.contains(peptideBean.getPeptideBeanUniqueIdentifier())) {
				continue;
			}
			peptideUniqueIdentifiers.add(peptideBean.getPeptideBeanUniqueIdentifier());
			peptides.add(peptideBean.cloneToLightPeptideBean());
			final Set<ProteinBean> proteins = peptideBean.getProteins();
			for (ProteinBean proteinBean : proteins) {
				if (proteinBean.equals(peptideProvider2)) {
					continue;
				}
				if (proteinUniqueIdentifier.contains(proteinBean.getProteinBeanUniqueIdentifier())) {
					continue;
				}
				proteinUniqueIdentifier.add(proteinBean.getProteinBeanUniqueIdentifier());
				ProteinBean lightProteinBean = proteinBean.cloneToLightProteinBean();
				proteinSet.add(lightProteinBean);
				if (!proteinBean.equals(peptideProvider2)) {
					process(proteinBean);
				}
			}

		}

	}

	/**
	 * @return the peptideProvider
	 */
	public ContainsPeptides getPeptideProvider() {
		return peptideProvider;
	}

	/**
	 * @param peptideProvider
	 *            the peptideProvider to set
	 */
	public void setPeptideProvider(ContainsPeptides peptideProvider) {
		this.peptideProvider = peptideProvider;
	}

	/**
	 * @return the peptides
	 */
	public Set<PeptideBean> getPeptides() {
		return peptides;
	}

	/**
	 * @param peptides
	 *            the peptides to set
	 */
	public void setPeptides(Set<PeptideBean> peptides) {
		this.peptides = peptides;
	}

	/**
	 * @param proteins
	 *            the proteins to set
	 */
	public void setProteins(Set<ProteinBean> proteins) {
		proteinSet = proteins;
	}

	public Map<PeptideBean, Set<ProteinBean>> getRelationships() {
		Map<PeptideBean, Set<ProteinBean>> ret = new HashMap<PeptideBean, Set<ProteinBean>>();
		List<PeptideBean> peptides = peptideProvider.getPeptides();
		for (PeptideBean peptideBean : peptides) {

			final List<AccessionBean> primaryAccessions = peptideBean.getPrimaryAccessions();
			Set<ProteinBean> proteins = new HashSet<ProteinBean>();
			for (AccessionBean acc : primaryAccessions) {
				proteins.add(getProteinMap().get(acc.getAccession()));
			}
			if (ret.containsKey(peptideBean)) {
				ret.get(peptideBean).addAll(proteins);
			} else {
				ret.put(peptideBean, proteins);
			}
		}
		return ret;
	}

	private Map<String, ProteinBean> getProteinMap() {
		if (proteinMap == null) {
			proteinMap = new HashMap<String, ProteinBean>();
			for (ProteinBean protein : proteinSet) {
				proteinMap.put(protein.getPrimaryAccession().getAccession(), protein);
			}
		}
		return proteinMap;
	}

	public Map<PeptideBean, Set<ProteinBean>> getExtendedRelationships() {
		Map<PeptideBean, Set<ProteinBean>> ret = new HashMap<PeptideBean, Set<ProteinBean>>();
		Set<String> seqs = new HashSet<String>();
		for (ProteinBean proteinBean : proteinSet) {
			List<PeptideBean> peptides = proteinBean.getPeptides();
			for (PeptideBean peptideBean : peptides) {

				final List<AccessionBean> primaryAccessions = peptideBean.getPrimaryAccessions();
				Set<ProteinBean> proteins = new HashSet<ProteinBean>();
				for (AccessionBean acc : primaryAccessions) {
					proteins.add(getProteinMap().get(acc.getAccession()));
				}
				if (seqs.contains(peptideBean.getSequence()) && !ret.containsKey(peptideBean)) {
					System.out.println("asdf");
				}

				seqs.add(peptideBean.getSequence());
				if (ret.containsKey(peptideBean)) {
					ret.get(peptideBean).addAll(proteins);
				} else {
					ret.put(peptideBean, proteins);
				}
			}
		}
		return ret;
	}

	/**
	 * @return the peptideUniqueIdentifiers
	 */
	public Set<Integer> getPeptideUniqueIdentifiers() {
		return peptideUniqueIdentifiers;
	}

	/**
	 * @param peptideUniqueIdentifiers
	 *            the peptideUniqueIdentifiers to set
	 */
	public void setPeptideUniqueIdentifiers(HashSet<Integer> peptideUniqueIdentifiers) {
		this.peptideUniqueIdentifiers = peptideUniqueIdentifiers;
	}

	/**
	 * @return the proteinSet
	 */
	public Set<ProteinBean> getProteinSet() {
		return proteinSet;
	}

	/**
	 * @param proteinSet
	 *            the proteinSet to set
	 */
	public void setProteinSet(Set<ProteinBean> proteinSet) {
		this.proteinSet = proteinSet;
	}

	/**
	 * @return the proteinUniqueIdentifier
	 */
	public Set<Integer> getProteinUniqueIdentifier() {
		return proteinUniqueIdentifier;
	}

	/**
	 * @param proteinUniqueIdentifier
	 *            the proteinUniqueIdentifier to set
	 */
	public void setProteinUniqueIdentifier(HashSet<Integer> proteinUniqueIdentifier) {
		this.proteinUniqueIdentifier = proteinUniqueIdentifier;
	}

	/**
	 * @return the aligmentResults
	 */
	public ProteinPeptideClusterAlignmentResults getAligmentResults() {
		return aligmentResults;
	}

	/**
	 * @param aligmentResults
	 *            the aligmentResults to set
	 */
	public void setAligmentResults(ProteinPeptideClusterAlignmentResults aligmentResults) {
		this.aligmentResults = aligmentResults;
	}
}
