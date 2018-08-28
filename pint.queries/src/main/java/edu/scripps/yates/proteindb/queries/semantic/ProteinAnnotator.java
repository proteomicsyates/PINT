package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ProteinAnnotator {
	private static final Logger log = Logger.getLogger(ProteinAnnotator.class);
	private static Map<String, ProteinAnnotator> instanceByVersion = new THashMap<String, ProteinAnnotator>();

	public static ProteinAnnotator getInstance(String uniprotKBVersion) {
		ProteinAnnotator instance;
		if (!instanceByVersion.containsKey(uniprotKBVersion)) {
			instance = new ProteinAnnotator(uniprotKBVersion);
			instanceByVersion.put(uniprotKBVersion, instance);
		}
		return instanceByVersion.get(uniprotKBVersion);
	}

	private final UniprotProteinRetriever uplr;
	private final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = new THashMap<String, edu.scripps.yates.utilities.proteomicsmodel.Protein>();
	private final Map<String, Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation>> annotationsByProteinAcc = new THashMap<String, Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation>>();

	private ProteinAnnotator(String uniprotKBVersion) {
		uplr = new UniprotProteinRetriever(uniprotKBVersion,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
	}

	/**
	 * method to get uniprot annotations from a protein Map. The annotations are
	 * kept in a map separately and can be accessed by
	 * getProteinAnnotationByProteinAcc(ProteinACC) method
	 * 
	 * @param proteinMap
	 */
	public void annotateProteins(Map<String, Set<Protein>> proteinMap) {
		log.info("Getting Uniprot annotations from " + proteinMap.size() + " proteins");
		final Set<String> accessions = proteinMap.keySet();
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = getAnnotatedProteins(
				accessions);
		for (final String accession : proteinMap.keySet()) {
			final edu.scripps.yates.utilities.proteomicsmodel.Protein annotatedProtein = annotatedProteins
					.get(accession);
			if (annotatedProtein != null) {
				final Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> proteinAnnotations = annotatedProtein
						.getAnnotations();
				for (final edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation : proteinAnnotations) {
					final Set<Protein> proteinSet = proteinMap.get(accession);
					for (final Protein protein : proteinSet) {
						addAnnotationByProtein(protein, proteinAnnotation);
					}
				}

			}
		}
		log.info("Annotations retrieved for " + proteinMap.size() + " proteins");
	}

	// private void addAnnotationByProtein(Protein protein, ProteinAnnotation
	// proteinAnnotation) {
	// if (protein != null) {
	// final List<ProteinAccession> accessionsByAccType =
	// PersistenceUtils.getAccessionsByAccType(protein,
	// AccessionType.UNIPROT);
	// if (accessionsByAccType != null) {
	// for (final ProteinAccession proteinAccession : accessionsByAccType) {
	// final String accession = proteinAccession.getAccession();
	// if (annotationsByProteinAcc.containsKey(accession)) {
	// annotationsByProteinAcc.get(accession).add(proteinAnnotation);
	// } else {
	// final Set<ProteinAnnotation> set = new THashSet<ProteinAnnotation>();
	// set.add(proteinAnnotation);
	// annotationsByProteinAcc.put(accession, set);
	// }
	// }
	// }
	// }
	// }
	private void addAnnotationByProtein(Protein protein,
			edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation) {
		if (protein != null) {
			final String uniprotACC = FastaParser.getUniProtACC(protein.getAcc());

			if (uniprotACC != null) {

				if (annotationsByProteinAcc.containsKey(uniprotACC)) {
					annotationsByProteinAcc.get(uniprotACC).add(proteinAnnotation);
				} else {
					final Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> set = new THashSet<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation>();
					set.add(proteinAnnotation);
					annotationsByProteinAcc.put(uniprotACC, set);
				}

			}
		}
	}

	public void clearAnnotations() {
		annotatedProteins.clear();
		annotationsByProteinAcc.clear();
	}

	public Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> getProteinAnnotationByProteinAcc(
			String accession) {
		if (annotationsByProteinAcc.containsKey(accession)) {
			return annotationsByProteinAcc.get(accession);
		}
		return Collections.emptySet();
	}

	private Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> getAnnotatedProteins(
			Collection<String> accessions) {
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> ret = new THashMap<String, edu.scripps.yates.utilities.proteomicsmodel.Protein>();
		final Set<String> notAnnotatedAccs = new THashSet<String>();
		for (final String acc : accessions) {
			if (annotatedProteins.containsKey(acc)) {
				ret.put(acc, annotatedProteins.get(acc));
			} else {
				notAnnotatedAccs.add(acc);
			}
		}
		log.info("Retrieving from index " + notAnnotatedAccs.size() + " protein annotations");
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins2 = uplr
				.getAnnotatedProteins(notAnnotatedAccs);
		for (final String acc : annotatedProteins2.keySet()) {
			final edu.scripps.yates.utilities.proteomicsmodel.Protein protein = annotatedProteins2.get(acc);
			ret.put(acc, protein);
			annotatedProteins.put(acc, protein);
		}
		return ret;
	}

}
