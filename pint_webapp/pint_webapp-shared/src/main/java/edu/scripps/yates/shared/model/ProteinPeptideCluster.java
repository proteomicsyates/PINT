package edu.scripps.yates.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.model.interfaces.ContainsPeptides;
import edu.scripps.yates.shared.util.ProteinPeptideClusterAlignmentResults;

public class ProteinPeptideCluster implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7422548863426356304L;

	private ContainsPeptides lightPeptideProvider;
	private Set<PeptideBean> peptides = new HashSet<PeptideBean>();
	private Set<Integer> peptideUniqueIdentifiers = new HashSet<Integer>();
	private Set<ProteinBean> proteinSet = new HashSet<ProteinBean>();
	private Set<Integer> proteinUniqueIdentifier = new HashSet<Integer>();
	private Map<String, ProteinBean> proteinMap;
	private ProteinPeptideClusterAlignmentResults aligmentResults = new ProteinPeptideClusterAlignmentResults();

	public ProteinPeptideCluster() {

	}

	public ProteinPeptideCluster(ContainsPeptides peptideProvider) throws PintException {
		if (peptideProvider instanceof ProteinGroupBean) {
			this.lightPeptideProvider = ((ProteinGroupBean) peptideProvider).cloneToLightProteinGroupBean();
		} else if (peptideProvider instanceof ProteinBean) {
			this.lightPeptideProvider = ((ProteinBean) peptideProvider).cloneToLightProteinBean();
		}
		process(peptideProvider);
	}

	private void process(ContainsPeptides peptideProvider) throws PintException {
		// insert the peptide provider: proteinGroup or protein
		if (peptideProvider instanceof ProteinBean) {
			final ProteinBean protein = (ProteinBean) peptideProvider;
			proteinSet.add(protein.cloneToLightProteinBean());
			if (lightPeptideProvider instanceof ProteinBean) {
				proteinSet.add((ProteinBean) lightPeptideProvider);
			}
		} else {
			final ProteinGroupBean proteinGroup = (ProteinGroupBean) peptideProvider;
			for (final ProteinBean protein : proteinGroup) {
				proteinSet.add(protein.cloneToLightProteinBean());
			}
		}
		// I create a new list so that I avoid concurrent modification exceptions
		final List<PeptideBean> peptides2 = new ArrayList<PeptideBean>();
		peptides2.addAll(peptideProvider.getPeptides());
		if (peptides2.isEmpty()) {
			// the peptides where not retrieved yet from database
			throw new PintException(
					"This peptide provider should have peptides. Have you called to DataSet.getPeptidesFromPeptideProvider(peptideProvider) at the server side?",
					PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
		final Set<String> seqs = new HashSet<String>();
		final Set<ProteinBean> proteinsToFollowUp = new HashSet<ProteinBean>();
		for (final PeptideBean peptideBean : peptides2) {
			if (peptideUniqueIdentifiers.contains(peptideBean.getPeptideBeanUniqueIdentifier())) {
				continue;
			}
			if (seqs.contains(peptideBean.getSequence())) {
				continue;
			}
			seqs.add(peptideBean.getSequence());
			peptideUniqueIdentifiers.add(peptideBean.getPeptideBeanUniqueIdentifier());
			peptides.add(peptideBean.cloneToLightPeptideBean());
			final Set<ProteinBean> proteins = peptideBean.getProteins();
			for (final ProteinBean proteinBean : proteins) {
				if (proteinBean.equals(peptideProvider)) {
					continue;
				}
				if (proteinUniqueIdentifier.contains(proteinBean.getProteinBeanUniqueIdentifier())) {
					continue;
				}
				proteinUniqueIdentifier.add(proteinBean.getProteinBeanUniqueIdentifier());
				final ProteinBean lightProteinBean = proteinBean.cloneToLightProteinBean();
				proteinSet.add(lightProteinBean);
				if (!proteinBean.equals(peptideProvider)) {
					proteinsToFollowUp.add(proteinBean);
				}
			}

		}
		for (final ProteinBean proteinBean : proteinsToFollowUp) {
			process(proteinBean);
		}

	}

	/**
	 * @return the peptideProvider
	 */
	public ContainsPeptides getLightPeptideProvider() {
		return lightPeptideProvider;
	}

	/**
	 * @param peptideProvider the peptideProvider to set
	 */
	public void setLightPeptideProvider(ContainsPeptides peptideProvider) {
		this.lightPeptideProvider = peptideProvider;
	}

	/**
	 * @return the peptides
	 */
	public Set<PeptideBean> getPeptides() {
		return peptides;
	}

	/**
	 * @param peptides the peptides to set
	 */
	public void setPeptides(Set<PeptideBean> peptides) {
		this.peptides = peptides;
	}

	/**
	 * @param proteins the proteins to set
	 */
	public void setProteins(Set<ProteinBean> proteins) {
		proteinSet = proteins;
	}

	public Map<PeptideBean, Set<ProteinBean>> getRelationships() {
		final Map<PeptideBean, Set<ProteinBean>> ret = new HashMap<PeptideBean, Set<ProteinBean>>();
		final List<PeptideBean> peptides = lightPeptideProvider.getPeptides();
		for (final PeptideBean peptideBean : peptides) {

			final List<AccessionBean> primaryAccessions = peptideBean.getPrimaryAccessions();
			final Set<ProteinBean> proteins = new HashSet<ProteinBean>();
			for (final AccessionBean acc : primaryAccessions) {
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
			for (final ProteinBean protein : proteinSet) {
				proteinMap.put(protein.getPrimaryAccession().getAccession(), protein);
			}
		}
		return proteinMap;
	}

	public Map<PeptideBean, Set<ProteinBean>> getExtendedRelationships() {
		final Map<PeptideBean, Set<ProteinBean>> ret = new HashMap<PeptideBean, Set<ProteinBean>>();
		final Set<String> seqs = new HashSet<String>();
		for (final ProteinBean proteinBean : proteinSet) {
			final List<PeptideBean> peptides = proteinBean.getPeptides();
			for (final PeptideBean peptideBean : peptides) {

				final List<AccessionBean> primaryAccessions = peptideBean.getPrimaryAccessions();
				final Set<ProteinBean> proteins = new HashSet<ProteinBean>();
				for (final AccessionBean acc : primaryAccessions) {
					proteins.add(getProteinMap().get(acc.getAccession()));
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
	 * @param peptideUniqueIdentifiers the peptideUniqueIdentifiers to set
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
	 * @param proteinSet the proteinSet to set
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
	 * @param proteinUniqueIdentifier the proteinUniqueIdentifier to set
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
	 * @param aligmentResults the aligmentResults to set
	 */
	public void setAligmentResults(ProteinPeptideClusterAlignmentResults aligmentResults) {
		this.aligmentResults = aligmentResults;
	}
}
