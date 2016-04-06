package edu.scripps.yates.proteindb.queries.semantic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAccession;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinAnnotation;
import edu.scripps.yates.proteindb.persistence.mysql.adapter.ProteinAnnotationAdapter;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.utilities.model.enums.AccessionType;

public class ProteinAnnotator {
	private static final Logger log = Logger.getLogger(ProteinAnnotator.class);
	private static Map<String, ProteinAnnotator> instanceByVersion = new HashMap<String, ProteinAnnotator>();

	public static ProteinAnnotator getInstance(String uniprotKBVersion) {
		ProteinAnnotator instance;
		if (!instanceByVersion.containsKey(uniprotKBVersion)) {
			instance = new ProteinAnnotator(uniprotKBVersion);
			instanceByVersion.put(uniprotKBVersion, instance);
		}
		return instanceByVersion.get(uniprotKBVersion);
	}

	private final UniprotProteinRetriever uplr;
	private final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = new HashMap<String, edu.scripps.yates.utilities.proteomicsmodel.Protein>();
	private final Map<String, Set<ProteinAnnotation>> annotationsByProteinAcc = new HashMap<String, Set<ProteinAnnotation>>();

	private ProteinAnnotator(String uniprotKBVersion) {
		uplr = new UniprotProteinRetriever(uniprotKBVersion,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex());
	}

	public void annotateProteins(Map<String, Set<Protein>> proteinList) {
		log.info("Getting Uniprot annotations from " + proteinList.size() + " proteins");
		Collection<String> accessions = PersistenceUtils.getAccessionsByAccType(proteinList, AccessionType.UNIPROT);
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = getAnnotatedProteins(accessions);
		for (String accession : proteinList.keySet()) {
			final edu.scripps.yates.utilities.proteomicsmodel.Protein annotatedProtein = annotatedProteins.get(accession);
			if (annotatedProtein != null) {
				final Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> proteinAnnotations = annotatedProtein
						.getAnnotations();
				for (edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation : proteinAnnotations) {
					final Set<Protein> proteinSet = proteinList.get(accession);
					for (Protein protein : proteinSet) {
						final ProteinAnnotation proteinAnnotationNew = new ProteinAnnotationAdapter(proteinAnnotation,
								null).adapt();
						protein.getProteinAnnotations().add(proteinAnnotationNew);
						addAnnotationByProtein(protein, proteinAnnotationNew);
					}
				}
			}
		}
		log.info("Annotations retrieved for " + proteinList.size() + " proteins");
	}

	// public void annotateProteins2(Map<String, Set<QueriableProteinInterface>>
	// proteinList) {
	// log.info("Getting Uniprot annotations from " + proteinList.size() + "
	// proteins");
	// Collection<String> accessions = getAccessionsByAccType(proteinList,
	// AccessionType.UNIPROT);
	// final Map<String, edu.scripps.yates.model.Protein> annotatedProteins =
	// getAnnotatedProteins(accessions);
	// for (String accession : proteinList.keySet()) {
	// final edu.scripps.yates.model.Protein annotatedProtein =
	// annotatedProteins.get(accession);
	// if (annotatedProtein != null) {
	// final Set<edu.scripps.yates.model.ProteinAnnotation> proteinAnnotations =
	// annotatedProtein
	// .getAnnotations();
	// for (edu.scripps.yates.model.ProteinAnnotation proteinAnnotation :
	// proteinAnnotations) {
	// final Set<QueriableProteinInterface> proteinSet =
	// proteinList.get(accession);
	// for (QueriableProteinInterface protein : proteinSet) {
	// final ProteinAnnotation proteinAnnotationNew = new
	// ProteinAnnotationAdapter(proteinAnnotation,
	// null).adapt();
	// protein.getProteinAnnotations().add(proteinAnnotationNew);
	// }
	// }
	// }
	// }
	// log.info("Annotations retrieved for " + proteinList.size() + "
	// proteins");
	// }

	private void addAnnotationByProtein(Protein protein, ProteinAnnotation proteinAnnotation) {
		if (protein != null) {
			final List<ProteinAccession> accessionsByAccType = PersistenceUtils.getAccessionsByAccType(protein,
					AccessionType.UNIPROT);
			if (accessionsByAccType != null) {
				for (ProteinAccession proteinAccession : accessionsByAccType) {
					final String accession = proteinAccession.getAccession();
					if (annotationsByProteinAcc.containsKey(accession)) {
						annotationsByProteinAcc.get(accession).add(proteinAnnotation);
					} else {
						Set<ProteinAnnotation> set = new HashSet<ProteinAnnotation>();
						set.add(proteinAnnotation);
						annotationsByProteinAcc.put(accession, set);
					}
				}
			}
		}
	}

	public Set<ProteinAnnotation> getProteinAnnotationByProteinAcc(String accession) {
		if (annotationsByProteinAcc.containsKey(accession)) {
			return annotationsByProteinAcc.get(accession);
		}
		return Collections.emptySet();
	}

	private Set<String> getAccessionsByAccType(Map<String, Set<QueriableProteinInterface>> proteinList,
			AccessionType accType) {
		Set<String> ret = new HashSet<String>();
		for (Set<QueriableProteinInterface> set : proteinList.values()) {
			for (QueriableProteinInterface queriableProteinInterface : set) {
				final Set<ProteinAccession> proteinAccessions = queriableProteinInterface.getProteinAccessions();
				for (ProteinAccession proteinAccession : proteinAccessions) {
					if (proteinAccession.getAccessionType().equalsIgnoreCase(accType.name())) {
						ret.add(proteinAccession.getAccession());
					}
				}
			}
		}
		return ret;
	}

	private Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> getAnnotatedProteins(Collection<String> accessions) {
		Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> ret = new HashMap<String, edu.scripps.yates.utilities.proteomicsmodel.Protein>();
		Set<String> notAnnotatedAccs = new HashSet<String>();
		for (String acc : accessions) {
			if (annotatedProteins.containsKey(acc)) {
				ret.put(acc, annotatedProteins.get(acc));
			} else {
				notAnnotatedAccs.add(acc);
			}
		}
		log.info("Retrieving from index " + notAnnotatedAccs.size() + " protein annotations");
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins2 = uplr
				.getAnnotatedProteins(notAnnotatedAccs);
		for (String acc : annotatedProteins2.keySet()) {
			final edu.scripps.yates.utilities.proteomicsmodel.Protein protein = annotatedProteins2.get(acc);
			ret.put(acc, protein);
			annotatedProteins.put(acc, protein);
		}
		return ret;
	}

}
