package edu.scripps.yates.model.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.model.enums.AccessionType;
import edu.scripps.yates.utilities.model.factories.AccessionEx;
import edu.scripps.yates.utilities.model.factories.GeneEx;
import edu.scripps.yates.utilities.model.factories.OrganismEx;
import edu.scripps.yates.utilities.model.factories.ProteinEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Organism;
import edu.scripps.yates.utilities.taxonomy.UniprotOrganism;
import edu.scripps.yates.utilities.taxonomy.UniprotSpeciesCodeMap;

public class ProteinExExtension extends ProteinEx {
	private final static Logger log = Logger.getLogger(ProteinExExtension.class);
	/**
	 *
	 */
	private static final long serialVersionUID = 5628363522925596821L;

	public ProteinExExtension(Accession accession, Organism organism) {
		super(accession.getAccessionType(), accession.getAccession(), organism);

		List<AccessionEx> uniprotAccs = getUniprotAssociatedAccessions(accession);
		if (!uniprotAccs.isEmpty()) {
			// take the first as primary accession
			Accession previousPrimaryAcc = primaryAccession;
			final AccessionEx uniprotAcc = uniprotAccs.get(0);
			uniprotAcc.setDescription(previousPrimaryAcc.getDescription());
			uniprotAcc.setAlternativeNames(previousPrimaryAcc.getAlternativeNames());
			setPrimaryAccession(uniprotAcc);
			// add the previous one as secondary
			this.addSecondaryAccession(previousPrimaryAcc);
			// add the others as secondary
			for (int j = 1; j < uniprotAccs.size(); j++) {
				this.addSecondaryAccession(uniprotAccs.get(j));
			}
		}
		// parse the description
		// get gene
		final String gene = FastaParser.getGeneFromFastaHeader(primaryAccession.getDescription());
		if (gene != null) {
			genes.add(new GeneEx(gene));
		}
		// get taxonomy
		if (this.organism == null) {
			final String organismName = FastaParser.getOrganismNameFromFastaHeader(primaryAccession.getDescription(),
					primaryAccession.getAccession());
			if (organismName != null) {
				final UniprotOrganism uniprotOrganism = UniprotSpeciesCodeMap.getInstance().get(organismName);
				if (uniprotOrganism != null) {
					organism = new OrganismEx(String.valueOf(uniprotOrganism.getTaxonCode()));

				} else {
					organism = new OrganismEx(organismName);
				}
			} else {
				log.warn("The organism has to be not null");
			}

		}
		numInstances++;
		// log.debug(numInstances + " proteinEx");
	}

	/**
	 * If the accession provided is an IPI accession, look into the IPI to
	 * Uniprot mapping to retrieve the Uniprot accessions
	 *
	 * @param ipiAcc
	 * @return
	 */
	private List<AccessionEx> getUniprotAssociatedAccessions(Accession ipiAcc) {
		List<AccessionEx> list = new ArrayList<AccessionEx>();
		if (ipiAcc.getAccessionType().equals(AccessionType.IPI)) {
			final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance().map2Uniprot(ipiAcc.getAccession());

			if (map2Uniprot != null && !map2Uniprot.isEmpty()) {
				for (UniprotEntry uniprotEntry : map2Uniprot) {
					list.add(new AccessionEx(uniprotEntry.getAcc(), AccessionType.UNIPROT));
				}
			}
		}
		return list;
	}

}
