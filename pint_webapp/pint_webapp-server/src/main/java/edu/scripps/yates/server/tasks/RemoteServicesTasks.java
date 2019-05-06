package edu.scripps.yates.server.tasks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

import edu.scripps.yates.annotations.omim.OmimEntry;
import edu.scripps.yates.annotations.omim.OmimRetriever;
import edu.scripps.yates.annotations.uniprot.ProteinImplFromUniprotEntry;
import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetrievalSettings;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRetriever;
import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.CensusChroParser;
import edu.scripps.yates.census.read.CensusOutParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.DBIndexImpl;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.proteindb.persistence.ContextualSessionHandler;
import edu.scripps.yates.proteindb.persistence.Parameter;
import edu.scripps.yates.proteindb.persistence.mysql.Condition;
import edu.scripps.yates.proteindb.persistence.mysql.ConfidenceScoreType;
import edu.scripps.yates.proteindb.persistence.mysql.MsRun;
import edu.scripps.yates.proteindb.persistence.mysql.Peptide;
import edu.scripps.yates.proteindb.persistence.mysql.PeptideRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Project;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PsmRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.PtmSite;
import edu.scripps.yates.proteindb.persistence.mysql.RatioDescriptor;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedCriteria;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.proteindb.persistence.mysql.utils.PersistenceUtils;
import edu.scripps.yates.proteindb.queries.exception.MalformedQueryException;
import edu.scripps.yates.proteindb.queries.semantic.QueriablePeptideSet;
import edu.scripps.yates.proteindb.queries.semantic.QueriableProteinSet;
import edu.scripps.yates.proteindb.queries.semantic.QueryInterface;
import edu.scripps.yates.proteindb.queries.semantic.QueryResult;
import edu.scripps.yates.server.DataSet;
import edu.scripps.yates.server.DataSetsManager;
import edu.scripps.yates.server.adapters.ConditionBeanAdapter;
import edu.scripps.yates.server.adapters.GeneBeanAdapter;
import edu.scripps.yates.server.adapters.OmimEntryBeanAdapter;
import edu.scripps.yates.server.adapters.PeptideBeanAdapterFromPeptideSet;
import edu.scripps.yates.server.adapters.ProjectBeanAdapter;
import edu.scripps.yates.server.adapters.ProteinAnnotationBeanAdapter;
import edu.scripps.yates.server.adapters.ProteinBeanAdapterFromProteinSet;
import edu.scripps.yates.server.adapters.RatioDescriptorAdapter;
import edu.scripps.yates.server.cache.ServerCacheDefaultViewByProjectTag;
import edu.scripps.yates.server.cache.ServerCacheProjectBeanByProjectTag;
import edu.scripps.yates.server.cache.ServerCacheProteinAccessionsByFileKey;
import edu.scripps.yates.server.cache.ServerCacheProteinBeansByProteinDBId;
import edu.scripps.yates.server.export.DataExporter;
import edu.scripps.yates.server.grouping.GroupableExtendedPeptideBean;
import edu.scripps.yates.server.grouping.GroupableExtendedProteinBean;
import edu.scripps.yates.server.grouping.GroupableExtendedPsmBean;
import edu.scripps.yates.server.lock.LockerByTag;
import edu.scripps.yates.server.projectCreator.adapter.RemoteSSHFileReferenceAdapter;
import edu.scripps.yates.server.util.DefaultViewReader;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.server.util.ServerDataUtil;
import edu.scripps.yates.shared.exceptions.PintException;
import edu.scripps.yates.shared.exceptions.PintException.PINT_ERROR_TYPE;
import edu.scripps.yates.shared.exceptions.PintRuntimeException;
import edu.scripps.yates.shared.model.AccessionBean;
import edu.scripps.yates.shared.model.AccessionType;
import edu.scripps.yates.shared.model.AmountType;
import edu.scripps.yates.shared.model.GeneBean;
import edu.scripps.yates.shared.model.PSMBean;
import edu.scripps.yates.shared.model.PeptideBean;
import edu.scripps.yates.shared.model.PeptideRelation;
import edu.scripps.yates.shared.model.ProjectBean;
import edu.scripps.yates.shared.model.ProteinBean;
import edu.scripps.yates.shared.model.ProteinEvidence;
import edu.scripps.yates.shared.model.ProteinGroupBean;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.model.ReactomePathwayRef;
import edu.scripps.yates.shared.model.SharedAggregationLevel;
import edu.scripps.yates.shared.model.UniprotFeatureBean;
import edu.scripps.yates.shared.model.UniprotProteinExistence;
import edu.scripps.yates.shared.model.projectCreator.FileNameWithTypeBean;
import edu.scripps.yates.shared.model.projectCreator.RemoteFileWithTypeBean;
import edu.scripps.yates.shared.tasks.GetRandomProteinsAccessionsFromCensusChroFileTask;
import edu.scripps.yates.shared.tasks.GetRandomProteinsAccessionsFromCensusOutFileTask;
import edu.scripps.yates.shared.util.DefaultView;
import edu.scripps.yates.shared.util.SharedConstants;
import edu.scripps.yates.shared.util.SharedDataUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.grouping.GroupablePeptide;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.PAnalyzer;
import edu.scripps.yates.utilities.grouping.ProteinGroup;
import edu.scripps.yates.utilities.ipi.IPI2UniprotACCMap;
import edu.scripps.yates.utilities.ipi.UniprotEntry;
import edu.scripps.yates.utilities.progresscounter.ProgressCounter;
import edu.scripps.yates.utilities.progresscounter.ProgressPrintingType;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.AnnotationType;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.UniprotLineHeader;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import edu.scripps.yates.utilities.util.Pair;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.THashSet;
import gnu.trove.set.hash.TIntHashSet;

public class RemoteServicesTasks {
	private final static Logger log = Logger.getLogger(RemoteServicesTasks.class);
	private static final Map<String, Set<String>> hiddenPTMsByProject = new HashMap<String, Set<String>>();
	private static final int CHUNK_TO_RELEASE = 1000;

	public static Map<String, Collection<Protein>> getProteinsFromProject(String sessionID, String projectTag,
			String uniprotVersion, Collection<String> hiddenPTMs, String omimAPIKey) {
		final Map<String, Collection<Protein>> ret = new THashMap<String, Collection<Protein>>();
		log.info("Retrieving proteins from project " + projectTag + " and sessionID: " + sessionID);

		// get the MSRuns of the project
		final List<MsRun> msRuns = PreparedQueries.getMSRunsByProject(projectTag);
		log.info(msRuns.size() + " MSRuns in project " + projectTag);

		final List<Protein> proteinList = PreparedCriteria.getProteinsByMSRunsCriteria(msRuns);
		PersistenceUtils.addToMapByPrimaryAcc(ret, proteinList);
		log.info(ret.size() + " proteins so far");

		// final Map<String, Set<Protein>> proteins =
		// PreparedQueries.getProteinsByProjectCondition(null,
		// projectTag, null);
		log.info(ret.size() + " proteins retrieved from a prepared query");

		return ret;
	}

	private static String getProjectString(Set<String> projectTags) {
		final List<String> list = new ArrayList<String>();
		list.addAll(projectTags);
		Collections.sort(list);
		final StringBuilder sb = new StringBuilder();
		for (final String string : list) {
			if (!"".equals(sb.toString()))
				sb.append(",");
			sb.append(string);
		}
		return sb.toString();
	}

	/**
	 * Gets the uniprot annotations using a {@link UniprotProteinLocalRetriever} and
	 * using the given uniprot version, storing them in cache.<br>
	 *
	 *
	 * @param proteins
	 * @param uniprotVersion
	 * @param projectTag
	 * @param projectTag
	 * @return
	 */
	public static Map<String, Entry> annotateProteinsWithUniprot(Collection<String> proteins, String uniprotVersion,
			String projectTag) {

		try {
			LockerByTag.lock(LockerByTag.getLockForUniprotAnnotation(projectTag), null);
			log.info("Annotating " + proteins.size() + " proteins from project " + projectTag);
			final UniprotProteinLocalRetriever uplr = new UniprotProteinLocalRetriever(
					UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
					UniprotProteinRetrievalSettings.getInstance().isUseIndex(), true, true);
			uplr.setCacheEnabled(true);
			final Set<String> uniprotACCs = new THashSet<String>();
			for (final String acc : proteins) {
				final Accession accType = FastaParser.getACC(acc);
				if (accType
						.getAccessionType() == edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType.UNIPROT) {
					uniprotACCs.add(accType.getAccession());
				} else if (accType
						.getAccessionType() == edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType.IPI) {
					final List<UniprotEntry> map2Uniprot = IPI2UniprotACCMap.getInstance()
							.map2Uniprot(accType.getAccession());
					if (map2Uniprot != null && !map2Uniprot.isEmpty()) {
						for (final UniprotEntry uniprotEntry : map2Uniprot) {
							uniprotACCs.add(uniprotEntry.getAcc());
						}
					}
				}
			}

			final Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(uniprotVersion, uniprotACCs);
			log.info(annotatedProteins.size() + " annotations retrieved for " + proteins.size() + " proteins");
			return annotatedProteins;
		} finally {
			LockerByTag.unlock(LockerByTag.getLockForUniprotAnnotation(projectTag), null);
		}
	}

	/**
	 * Gets the uniprot annotations using a {@link UniprotProteinRetriever} and
	 * using the given uniprot version.<br>
	 * It is also annotating the position of each peptide in each protein by getting
	 * the protein sequences from the {@link UniprotProteinRetriever}, as well as
	 * the protein coverage.<br>
	 *
	 *
	 * @param proteinBeans
	 * @param uniprotVersion
	 * @param hiddenPTMs
	 * @param ignoreReferences   to speed up if they are not necessary, set to false
	 * @param ignoreDBReferences to speed up if they are not necessary, set to false
	 */
	public static void annotateProteinBeansWithUniprot(Collection<ProteinBean> proteinBeans, String uniprotVersion,
			Collection<String> hiddenPTMs, boolean ignoreReferences, boolean ignoreDBReferences) {
		final int initialProteinNumber = proteinBeans.size();

		final UniprotProteinRetriever uplr = new UniprotProteinRetriever(uniprotVersion,
				UniprotProteinRetrievalSettings.getInstance().getUniprotReleasesFolder(),
				UniprotProteinRetrievalSettings.getInstance().isUseIndex(), ignoreReferences, ignoreDBReferences);
		uplr.setCacheEnabled(true);
		if (proteinBeans.size() > 1) {
			log.info("Getting annotations from " + proteinBeans.size() + " protein beans using uniprot version: "
					+ uniprotVersion);
		}
		final Collection<String> accessions = SharedDataUtil.getPrimaryAccessions(proteinBeans,
				edu.scripps.yates.shared.model.AccessionType.UNIPROT);
		if (accessions.size() > 1) {
			log.info("Getting annotations from " + accessions.size() + " primary UniProt accessions");
		}
		final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> annotatedProteins = uplr
				.getAnnotatedProteins(accessions);
		if (annotatedProteins.size() > 1) {
			log.info("Received " + annotatedProteins.size() + " annotated proteins");
		}
		final Map<String, String> annotatedProteinsSequences = uplr.getAnnotatedProteinSequence(accessions);
		if (annotatedProteinsSequences.size() > 1) {
			log.info("Received " + annotatedProteinsSequences.size() + " annotated proteins sequences");
		}
		// keep a map of the proteins by its primery accession, in order to
		// merge if needed
		final Map<String, ProteinBean> proteinMap = new THashMap<String, ProteinBean>();
		int numSequences = 0;
		final Iterator<ProteinBean> iterator = proteinBeans.iterator();
		while (iterator.hasNext()) {
			lookForInterruption();
			ProteinBean proteinBean = iterator.next();
			if (proteinBean.isAnnotated()) {
				continue;
			}
			String accession = proteinBean.getPrimaryAccession().getAccession();
			// if (accession.contains("-")) {
			// // annotate the isoform with the same entry
			// accession = accession.substring(0, accession.indexOf("-"));
			// }
			final edu.scripps.yates.utilities.proteomicsmodel.Protein annotatedProtein = annotatedProteins
					.get(accession);
			if (annotatedProtein != null) {
				final Set<edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation> proteinAnnotations = annotatedProtein
						.getAnnotations();
				for (final edu.scripps.yates.utilities.proteomicsmodel.ProteinAnnotation proteinAnnotation : proteinAnnotations) {
					// FUNCTION
					if (proteinAnnotation.getAnnotationType() == AnnotationType.function) {
						proteinBean.addFunction(proteinAnnotation.getValue());
						// ENSAMBLE ID
					} else if (proteinAnnotation.getAnnotationType() == AnnotationType.ensemblID) {
						proteinBean.addEnsemblID(proteinAnnotation.getValue());
						// PROTEIN EXISTENCE
					} else if (!proteinAnnotation.getAnnotationType().getUniprotLineHeaders().isEmpty()
							&& proteinAnnotation.getAnnotationType().getUniprotLineHeaders()
									.contains(UniprotLineHeader.PE)) {
						proteinBean.setUniprotProteinExistence(
								UniprotProteinExistence.getFromValue(proteinAnnotation.getName()));
					} else if (proteinAnnotation.getAnnotationType().getUniprotLineHeaders()
							.contains(UniprotLineHeader.FT)) {
						final UniprotFeatureBean uniprotFeature = ServerDataUtil.getFromValue(
								proteinAnnotation.getAnnotationType().getKey(), proteinAnnotation.getName(),
								proteinAnnotation.getValue());
						if (uniprotFeature != null) {
							proteinBean.addUniprotFeature(uniprotFeature);
						}
					} else if (proteinAnnotation.getAnnotationType() == AnnotationType.reactome) {
						final String id = proteinAnnotation.getName();
						final List<String> propertyTypeValues = new ArrayList<String>();
						if (proteinAnnotation.getValue().contains(ProteinImplFromUniprotEntry.ANNOTATION_SEPARATOR)) {
							final String[] split = proteinAnnotation.getValue()
									.split(ProteinImplFromUniprotEntry.ANNOTATION_SEPARATOR);
							for (final String string : split) {
								propertyTypeValues.add(string);
							}
						} else {
							propertyTypeValues.add(proteinAnnotation.getValue());
						}
						// take the first one only
						if (!propertyTypeValues.isEmpty()) {
							if (propertyTypeValues.get(0).contains(":")) {
								final String description = propertyTypeValues.get(0).split(":")[1];
								final ReactomePathwayRef reactomePathwayRef = new ReactomePathwayRef(id, description);
								proteinBean.addReactomePathwayRef(reactomePathwayRef);
							}
						}
					} else {
						// log.info("protein ann: " +
						// proteinAnnotation.getAnnotationType());
						proteinBean.addAnnotation(new ProteinAnnotationBeanAdapter(proteinAnnotation).adapt());
					}
				}
				// override the primary accession
				proteinBean.getPrimaryAccession()
						.setDescription(annotatedProtein.getPrimaryAccession().getDescription());
				proteinBean.getPrimaryAccession().setAccession(annotatedProtein.getPrimaryAccession().getAccession());
				accession = annotatedProtein.getPrimaryAccession().getAccession();
				if (annotatedProtein.getPrimaryAccession().getAlternativeNames() != null
						&& !annotatedProtein.getPrimaryAccession().getAlternativeNames().isEmpty()) {
					proteinBean.getPrimaryAccession().getAlternativeNames().clear();
					proteinBean.getPrimaryAccession().getAlternativeNames()
							.addAll(annotatedProtein.getPrimaryAccession().getAlternativeNames());
				}
				// override the secondary accessions
				if (annotatedProtein.getSecondaryAccessions() != null) {
					for (final Accession proteinAnnotation : annotatedProtein.getSecondaryAccessions()) {
						boolean found = false;
						for (final AccessionBean proteinAccBean : proteinBean.getSecondaryAccessions()) {
							if (proteinAccBean.getAccession().equals(proteinAnnotation.getAccession())) {
								found = ignoreReferences;
							}
						}
						if (!found) {
							final AccessionBean accessionBean = new AccessionBean();
							accessionBean.setAccession(proteinAnnotation.getAccession());
							accessionBean.setAccessionType(AccessionType.UNIPROT);
							accessionBean.setAlternativeNames(proteinAnnotation.getAlternativeNames());
							accessionBean.setDescription(proteinAnnotation.getDescription());
							proteinBean.getSecondaryAccessions().add(accessionBean);
						}
					}
				}
				// gene names
				if (annotatedProtein.getGenes() != null && !annotatedProtein.getGenes().isEmpty()) {
					// discard previous ones. Include only the ones in Uniprot
					proteinBean.getGenes().clear();
					for (final Gene annotatedGene : annotatedProtein.getGenes()) {
						final GeneBean geneBean = new GeneBeanAdapter(annotatedGene).adapt();
						proteinBean.addGene(geneBean);
					}
				}
				// length
				if (annotatedProtein.getLength() > 0) {
					proteinBean.setLength(annotatedProtein.getLength());
				}
				// MW
				if (annotatedProtein.getMw() > 0.0) {
					proteinBean.setMw(annotatedProtein.getMw());
				}
			}
			// just add it to the map
			final boolean merged = addToMapAndMergeIfNecessary(proteinMap, proteinBean);
			if (merged) {
				// remove the protein from the collection (from the dataset)
				// because it has been merged to another proteinBean
				iterator.remove();
				// assign to proteinBean the object with all the information
				// merged, in order to calculate again the coverage below
				proteinBean = proteinMap.get(proteinBean.getPrimaryAccession().getAccession());
			}

			if (annotatedProteinsSequences.containsKey(accession)) {
				numSequences++;
				final String proteinSeq = annotatedProteinsSequences.get(accession).trim();
				ServerDataUtil.calculateProteinCoverage(proteinBean, proteinSeq);
			} else {
				log.debug("There is no protein sequence for protein: " + accession);
			}
			proteinBean.setAnnotated(true);
		}
		if (proteinBeans.size() > 1) {
			log.info("Annotations retrieved for " + proteinBeans.size() + " proteins");
		}
		if (numSequences > 1) {
			log.info(numSequences + " protein sequences retrieved");
		}
		if (initialProteinNumber != proteinBeans.size()) {
			log.info("Some proteins were merged. Before " + initialProteinNumber + ", after " + proteinBeans.size());
		}

	}

	/**
	 * add a protein to the map by its primary acc. If there is already a protein
	 * with the same primary acc, that protein will be merged to the one in the map
	 * and the function will return true.
	 *
	 * @param proteinMap  protein map
	 * @param proteinBean protein to add
	 * @return true if a merge has been done.
	 */
	private static boolean addToMapAndMergeIfNecessary(Map<String, ProteinBean> proteinMap, ProteinBean proteinBean) {
		final String primaryAcc = proteinBean.getPrimaryAccession().getAccession();
		if (!proteinMap.containsKey(primaryAcc)) {
			proteinMap.put(primaryAcc, proteinBean);
			return false;
		} else {
			log.info("Merging two instances of protein " + primaryAcc);
			SharedDataUtil.mergeProteinBeans(proteinMap.get(primaryAcc), proteinBean);
			return true;
		}
	}

	/**
	 * Gets a list of score names associated to a collection of PSMs in a collection
	 * of projects.
	 *
	 * @param projectNames
	 * @return a non repeated and alphabethically sorted list of score names
	 * @throws IllegalArgumentException
	 */
	public static List<String> getPSMScoreNamesFromProjects(Collection<String> projectNames)
			throws IllegalArgumentException {
		final List<String> ret = new ArrayList<String>();
		// for (String projectName : projectNames) {
		// get the PSM scores from the projects
		final List<String> psmScoresByProject = PreparedQueries.getPSMScoreNames();
		for (final String psmScore : psmScoresByProject) {
			if (!ret.contains(psmScore))
				ret.add(psmScore);
		}
		// }
		Collections.sort(ret);
		return ret;

	}

	/**
	 * Gets a list of score names associated to a collection of proteins in a
	 * collection of projects.
	 *
	 * @param projectNames
	 * @return a non repeated and alphabethically sorted list of score names
	 * @throws IllegalArgumentException
	 */
	public static List<String> getProteinScoreNamesFromProjects(Collection<String> projectNames)
			throws IllegalArgumentException {
		final List<String> ret = new ArrayList<String>();
		// for (String projectName : projectNames) {
		// get the PSM scores from the projects
		final List<String> psmScoresByProject = PreparedQueries.getProteinScoreNames();
		for (final String psmScore : psmScoresByProject) {
			if (!ret.contains(psmScore))
				ret.add(psmScore);
		}
		// }
		Collections.sort(ret);
		return ret;

	}

	/**
	 * Gets a list of score names associated to a collection of proteins in a
	 * collection of projects.
	 *
	 * @param projectNames
	 * @return a non repeated and alphabethically sorted list of score names
	 * @throws IllegalArgumentException
	 */
	public static List<String> getPeptideScoreNamesFromProjects(Collection<String> projectNames)
			throws IllegalArgumentException {
		final List<String> ret = new ArrayList<String>();
		// for (String projectName : projectNames) {
		// get the PSM scores from the projects
		final List<String> peptideScoresByProject = PreparedQueries.getPeptideScoreNames();
		for (final String peptideScore : peptideScoresByProject) {
			if (!ret.contains(peptideScore))
				ret.add(peptideScore);
		}
		// }
		Collections.sort(ret);
		return ret;

	}

	/**
	 * Gets a list of score type names associated to a collection of PSMs in a
	 * collection of projects.
	 *
	 * @param projectNames
	 * @return a non repeated and alphabethically sorted list of score names
	 * @throws IllegalArgumentException
	 */
	public static List<String> getPSMScoreTypeNamesFromProjects(Collection<String> projectNames)
			throws IllegalArgumentException {
		final List<String> ret = new ArrayList<String>();
		// for (String projectName : projectNames) {
		// get the PSM scores from the projects
		final List<ConfidenceScoreType> psmScoresByProject = PreparedQueries.getPSMScoreTypeNames();
		for (final ConfidenceScoreType psmScore : psmScoresByProject) {
			if (!ret.contains(psmScore.getName()))
				ret.add(psmScore.getName());
		}
		// }
		Collections.sort(ret);
		return ret;

	}

	/**
	 * Gets a list of PTM score names associated to a collection of PSMs in a
	 * collection of projects.
	 *
	 * @param projectNames
	 * @return a non repeated and alphabethically sorted list of score names
	 * @throws IllegalArgumentException
	 */
	public static List<String> getPTMScoreNamesFromProjects(Collection<String> projectNames)
			throws IllegalArgumentException {
		final List<String> ret = new ArrayList<String>();
		for (final String projectName : projectNames) {
			// get the PTM scores from the projects
			final List<PtmSite> ptmSitesByProject = PreparedQueries.getPTMSitesWithScoresByProject(projectName);
			for (final PtmSite ptmSite : ptmSitesByProject) {
				if (!ret.contains(ptmSite.getConfidenceScoreName()))
					ret.add(ptmSite.getConfidenceScoreName());
			}
		}
		Collections.sort(ret);
		return ret;
	}

	public static List<ProteinGroupBean> groupProteins(Collection<ProteinBean> proteins,
			boolean separateNonConclusiveProteins, boolean psmCentric) {
		log.info("Grouping " + proteins.size() + " protein beans");
		log.info("separateNonConclusiveProteins=" + separateNonConclusiveProteins);
		final PAnalyzer panalyzer = new PAnalyzer(separateNonConclusiveProteins);
		// get the groupable proteins
		final List<GroupableProtein> groupableProteins = new ArrayList<GroupableProtein>();
		for (final ProteinBean proteinBean : proteins) {
			groupableProteins.add(new GroupableExtendedProteinBean(proteinBean, psmCentric));

		}
		log.info("Grouping " + groupableProteins.size() + " protein objects from the database ");

		final List<ProteinGroup> groups = panalyzer.run(groupableProteins);

		final List<ProteinGroupBean> ret = new ArrayList<ProteinGroupBean>();
		for (final ProteinGroup proteinGroup : groups) {
			final ProteinGroupBean proteinGroupBean = new ProteinGroupBean();
			for (final GroupableProtein groupableProtein : proteinGroup) {
				final ProteinBean proteinBean = ((GroupableExtendedProteinBean) groupableProtein).getProtein();
				proteinBean.setEvidence(ProteinEvidence.valueOf(groupableProtein.getEvidence().name()));
				proteinGroupBean.add(proteinBean);
				if (psmCentric) {
					final List<GroupablePeptide> groupablePSMs = groupableProtein.getGroupablePeptides();
					for (final GroupablePeptide groupablePSM : groupablePSMs) {
						final PeptideRelation peptideRelationByName = PeptideRelation
								.getPeptideRelationByName(groupablePSM.getRelation().name());
						final PSMBean psm = ((GroupableExtendedPsmBean) groupablePSM).getPsm();
						// log.info(peptideRelationByName + "\t with " +
						// psm.getProteins().size() + "("
						// + psm.getPrimaryAccessions().size() + ")");
						psm.setRelation(peptideRelationByName);
						if (psm.getPeptideBean() != null) {
							psm.getPeptideBean().setRelation(peptideRelationByName);
						}
					}
				} else {
					final List<GroupablePeptide> groupablePeptides = groupableProtein.getGroupablePeptides();
					for (final GroupablePeptide groupablePeptide : groupablePeptides) {
						final PeptideRelation peptideRelationByName = PeptideRelation
								.getPeptideRelationByName(groupablePeptide.getRelation().name());
						final PeptideBean peptide = ((GroupableExtendedPeptideBean) groupablePeptide).getPeptide();
						if (peptide != null) {
							peptide.setRelation(peptideRelationByName);
						} else {
							final String peptideSequence = ((GroupableExtendedPeptideBean) groupablePeptide)
									.getSequence();
							proteinBean.addPeptideRelation(peptideSequence, peptideRelationByName);
						}

					}
				}
			}

			ret.add(proteinGroupBean);

		}
		log.info("Returning " + ret.size() + " protein bean groups");
		return ret;
	}

	public static Set<Condition> getExperimentalConditionsFromProject(String projectTag) throws PintException {
		if (projectTag == null)
			throw new IllegalArgumentException("project name is null");

		final Set<Condition> conditionsSet = new HashSet<Condition>();

		final List<Parameter<String>> listParameter = new ArrayList<Parameter<String>>();
		listParameter.add(new Parameter<String>("tag", projectTag));
		try {
			final List<Project> retrieveList = ContextualSessionHandler.retrieveList(listParameter, Project.class);
			if (!retrieveList.isEmpty()) {
				final Project project = retrieveList.get(0);
				conditionsSet.addAll(project.getConditions());
			}
		} catch (final HibernateException e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}

		return conditionsSet;

	}

	public static Set<String> getMsRunsFromProject(String projectTag) throws PintException {
		if (projectTag == null)
			throw new IllegalArgumentException("project name is null");

		final Set<String> ret = new HashSet<String>();

		try {
			final List<MsRun> msruns = PreparedQueries.getMSRunsByProject(projectTag);
			for (final MsRun msRun : msruns) {
				ret.add(msRun.getRunId());
			}
		} catch (final HibernateException e) {
			throw new PintException(e, PINT_ERROR_TYPE.DB_ACCESS_ERROR);
		} finally {
		}

		return ret;

	}

	public static List<RatioDescriptorBean> getRatioDescriptorsFromProjects(Set<String> projectNames) {
		final List<RatioDescriptorBean> ret = new ArrayList<RatioDescriptorBean>();
		for (final String projectName : projectNames) {
			// PSM
			final List<RatioDescriptor> psmRatioDescriptorsByProject = PreparedQueries
					.getPSMRatioDescriptorsByProject(projectName);
			for (final RatioDescriptor ratioDescriptor : psmRatioDescriptorsByProject) {
				final PsmRatioValue prv = null;
				ret.add(new RatioDescriptorAdapter(ratioDescriptor, SharedAggregationLevel.PSM, prv).adapt());
			}
			// Peptide
			final List<RatioDescriptor> peptideRatioDescriptorsByProject = PreparedQueries
					.getPeptideRatioDescriptorsByProject(projectName);
			for (final RatioDescriptor ratioDescriptor : peptideRatioDescriptorsByProject) {
				final PeptideRatioValue prv = null;
				ret.add(new RatioDescriptorAdapter(ratioDescriptor, SharedAggregationLevel.PEPTIDE, prv).adapt());
			}
			// Protein
			final List<RatioDescriptor> proteinRatioDescriptorsByProject = PreparedQueries
					.getProteinRatioDescriptorsByProject(projectName);
			for (final RatioDescriptor ratioDescriptor : proteinRatioDescriptorsByProject) {
				final ProteinRatioValue prv = null;
				ret.add(new RatioDescriptorAdapter(ratioDescriptor, SharedAggregationLevel.PROTEIN, prv).adapt());
			}
		}
		return ret;
	}

	/**
	 * Gets the list of {@link ProjectBean} in the DB, NOT including the hidden
	 * projects
	 *
	 * @return
	 */
	public static Set<ProjectBean> getProjectBeans() {
		return getProjectBeans(false);
	}

	public static Set<ProjectBean> getProjectBeans(boolean includeHidden) {
		final Set<ProjectBean> ret = new HashSet<ProjectBean>();
		log.info("Getting projects (including hidden ones= " + includeHidden);
		final List<edu.scripps.yates.proteindb.persistence.mysql.Project> retrieveList = ContextualSessionHandler
				.retrieveList(edu.scripps.yates.proteindb.persistence.mysql.Project.class);
		log.info(retrieveList.size() + " projects retrieved");
		for (final edu.scripps.yates.proteindb.persistence.mysql.Project project : retrieveList) {
			if (!includeHidden && project.isHidden())
				continue;
			final ProjectBean projectBean = new ProjectBeanAdapter(project, false, false).adapt();
			ServerCacheProjectBeanByProjectTag.getInstance().addtoCache(projectBean, projectBean.getTag());
			ret.add(projectBean);
		}
		log.info(ret.size() + " projects adapted");
		return ret;
	}

	public static List<String> getRandomProteinAccessionsFromCensusChro(int jobID, File censusChroFile,
			FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues) throws FileNotFoundException {
		log.info("Getting " + numRandomValues + " accessions from census chro file: "
				+ censusChroFile.getAbsolutePath());

		// create a Task
		final GetRandomProteinsAccessionsFromCensusChroFileTask task = new GetRandomProteinsAccessionsFromCensusChroFileTask(
				censusChroFile.getAbsolutePath(), censusChroFile.length(), null);
		// register task
		ServerTaskRegister.getInstance().registerTask(task);

		try {
			final List<String> parsedAccs = new ArrayList<String>();
			if (numRandomValues == 0)
				return parsedAccs;
			if (ServerCacheProteinAccessionsByFileKey.getInstance().contains(getFileKey(censusChroFile))) {
				parsedAccs.addAll(
						ServerCacheProteinAccessionsByFileKey.getInstance().getFromCache(getFileKey(censusChroFile)));
			} else {
				final CensusChroParser parser = new CensusChroParser(censusChroFile, QuantificationLabel.LIGHT,
						new QuantCondition("light condition"), QuantificationLabel.HEAVY,
						new QuantCondition("heavy condition"));
				if (fileNameWithTypeFasta != null) {
					parser.setDbIndex(getDBIndexInterface(jobID, fileNameWithTypeFasta));
				}
				final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();
				if (proteinMap != null && !proteinMap.isEmpty()) {

					for (final String proteinAcc : proteinMap.keySet()) {
						// get the ProteinImpl in order to get the appropiate
						// parsed accession
						// edu.scripps.yates.utilities.proteomicsmodel.Protein
						// protein = new ProteinImplFromQuantifiedProtein(
						// proteinMap.get(proteinAcc), null);
						parsedAccs.add(getAccession(proteinMap.get(proteinAcc)));

					}
					// add to cache
					ServerCacheProteinAccessionsByFileKey.getInstance().addtoCache(parsedAccs,
							getFileKey(censusChroFile));
				}
			}
			Collections.shuffle(parsedAccs);
			final List<String> ret = new ArrayList<String>();
			for (final String parsedAcc : parsedAccs) {
				ret.add(parsedAcc);
				if (ret.size() == numRandomValues)
					break;
			}
			log.info("returning " + ret.size() + " protein accessions from census chro file");
			return ret;
		} catch (final IOException e) {

			e.printStackTrace();
			return Collections.emptyList();
		} finally {
			ServerTaskRegister.getInstance().endTask(task);
		}
	}

	private static String getAccession(QuantifiedProteinInterface quantProtein) {
		Accession primaryAccession = quantProtein.getPrimaryAccession();
		final Accession acc = FastaParser.getACC(primaryAccession.getAccession());
		if (acc.getAccessionType() == edu.scripps.yates.utilities.proteomicsmodel.enums.AccessionType.IPI) {
			final Pair<Accession, Set<Accession>> pair = IPI2UniprotACCMap.getInstance()
					.getPrimaryAndSecondaryAccessionsFromIPI(primaryAccession);
			if (pair.getFirstelement() != null) {
				primaryAccession = pair.getFirstelement();
			}

		}
		return primaryAccession.getAccession();
	}

	public static List<String> getRandomProteinAccessionsFromCensusOut(int jobID,
			RemoteSSHFileReference remoteSSHCensusRef, FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues)
			throws FileNotFoundException {
		log.info("Getting " + numRandomValues + " accessions from census out file at: "
				+ remoteSSHCensusRef.getHostName() + " " + remoteSSHCensusRef.getRemotePath());

		return getRandomProteinAccessionsFromCensusOut(jobID, remoteSSHCensusRef.getRemoteFile(), fileNameWithTypeFasta,
				numRandomValues);
	}

	public static List<String> getRandomProteinAccessionsFromCensusOut(int jobID, File censusOutFile,
			FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues) throws FileNotFoundException {
		log.info(
				"Getting " + numRandomValues + " accessions from census chro file: " + censusOutFile.getAbsolutePath());

		// create a Task
		final GetRandomProteinsAccessionsFromCensusOutFileTask task = new GetRandomProteinsAccessionsFromCensusOutFileTask(
				censusOutFile.getAbsolutePath(), censusOutFile.length(), null);
		// register task
		ServerTaskRegister.getInstance().registerTask(task);

		try {
			final List<String> parsedAccs = new ArrayList<String>();
			if (numRandomValues == 0)
				return parsedAccs;
			if (ServerCacheProteinAccessionsByFileKey.getInstance().contains(getFileKey(censusOutFile))) {
				parsedAccs.addAll(
						ServerCacheProteinAccessionsByFileKey.getInstance().getFromCache(getFileKey(censusOutFile)));
			} else {
				final CensusOutParser parser = new CensusOutParser(censusOutFile, QuantificationLabel.LIGHT,
						new QuantCondition("light condition"), QuantificationLabel.HEAVY,
						new QuantCondition("heavy condition"));
				if (fileNameWithTypeFasta != null) {
					parser.setDbIndex(getDBIndexInterface(jobID, fileNameWithTypeFasta));
				}
				final Map<String, QuantifiedProteinInterface> proteinMap = parser.getProteinMap();
				if (proteinMap != null) {

					for (final String proteinAcc : proteinMap.keySet()) {
						// get the ProteinImpl in order to get the appropiate
						// parsed accession
						// edu.scripps.yates.utilities.proteomicsmodel.Protein
						// protein = new ProteinImplFromQuantifiedProtein(
						// proteinMap.get(proteinAcc), null);
						parsedAccs.add(getAccession(proteinMap.get(proteinAcc)));

					}
					// add to cache
					ServerCacheProteinAccessionsByFileKey.getInstance().addtoCache(parsedAccs,
							getFileKey(censusOutFile));
				}
			}
			Collections.shuffle(parsedAccs);
			final List<String> ret = new ArrayList<String>();
			for (final String parsedAcc : parsedAccs) {
				ret.add(parsedAcc);
				if (ret.size() == numRandomValues)
					break;
			}
			log.info("returning " + ret.size() + " protein accessions from census chro file");
			return ret;
		} catch (final IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		} finally {
			ServerTaskRegister.getInstance().endTask(task);
		}
	}

	private static DBIndexImpl getDBIndexInterface(int jobID, FileNameWithTypeBean fileNameWithTypeFasta) {
		if (fileNameWithTypeFasta == null)
			return null;
		if (fileNameWithTypeFasta instanceof RemoteFileWithTypeBean) {
			return getDBIndexInterface((RemoteFileWithTypeBean) fileNameWithTypeFasta);
		} else {
			final File fastaFile = FileManager.getDataFile(jobID, fileNameWithTypeFasta.getFileName(),
					fileNameWithTypeFasta.getId(), fileNameWithTypeFasta.getFileFormat());
			final DBIndexImpl ret = new DBIndexImpl(DBIndexImpl.getDefaultDBIndexParams(fastaFile));
			return ret;
		}
	}

	private static DBIndexImpl getDBIndexInterface(RemoteFileWithTypeBean remoteFileNameWithTypeFasta) {
		if (remoteFileNameWithTypeFasta == null)
			return null;
		final RemoteSSHFileReference remoteSSHFastaFile = new RemoteSSHFileReferenceAdapter(remoteFileNameWithTypeFasta)
				.adapt();
		final File fastaFile = remoteSSHFastaFile.getRemoteFile();
		final DBIndexImpl ret = new DBIndexImpl(DBIndexImpl.getDefaultDBIndexParams(fastaFile));
		return ret;
	}

	public static List<String> getRandomProteinAccessionsFromCensusChro(int jobID,
			RemoteSSHFileReference remoteSSHCensusRef, FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues)
			throws FileNotFoundException {
		log.info("Getting " + numRandomValues + " accessions from census chro file at: "
				+ remoteSSHCensusRef.getHostName() + " " + remoteSSHCensusRef.getRemotePath());

		return getRandomProteinAccessionsFromCensusChro(jobID, remoteSSHCensusRef.getRemoteFile(),
				fileNameWithTypeFasta, numRandomValues);
		// List<String> ret = new ArrayList<String>();
		// if (numRandomValues == 0)
		// return ret;
		// CensusChroParser parser = new CensusChroParser(remoteSSHCensusRef,
		// null);
		// if (fileNameWithTypeFasta != null)
		// parser.setDbIndex(getDBIndexInterface(jobID, projectFilesPath,
		// fileNameWithTypeFasta));
		// final Map<String, QuantifiedProtein> proteinMap = parser
		// .getProteinMap();
		// if (proteinMap != null) {
		//
		// List<String> keyList = new ArrayList<String>();
		// keyList.addAll(proteinMap.keySet());
		// Collections.shuffle(keyList);
		// for (String proteinAcc : keyList) {
		// // get the ProteinImpl in order to get the appropiate parsed
		// // accession
		// edu.scripps.yates.model.Protein protein = new
		// ProteinImplFromQuantifiedProtein(
		// proteinMap.get(proteinAcc), null);
		// ret.add(protein.getAccession());
		// if (ret.size() == numRandomValues)
		// break;
		// }
		//
		// }
		// log.info("returning " + ret.size()
		// + " protein accessions from census chro file");
		// return ret;
	}

	public static List<String> getRandomProteinAccessionsFromDTASelectFile(int jobID, File dtaSelectFilterFile,
			FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues) throws IOException {
		log.info("Getting " + numRandomValues + " accessions from DTASelect-filter file: "
				+ dtaSelectFilterFile.getAbsolutePath());
		// create a Task
		final GetRandomProteinsAccessionsFromCensusChroFileTask task = new GetRandomProteinsAccessionsFromCensusChroFileTask(
				dtaSelectFilterFile.getAbsolutePath(), dtaSelectFilterFile.length(), null);
		// register task
		ServerTaskRegister.getInstance().registerTask(task);
		try {
			final List<String> parsedAccs = new ArrayList<String>();
			if (numRandomValues == 0)
				return parsedAccs;
			if (ServerCacheProteinAccessionsByFileKey.getInstance().contains(getFileKey(dtaSelectFilterFile))) {
				parsedAccs.addAll(ServerCacheProteinAccessionsByFileKey.getInstance()
						.getFromCache(getFileKey(dtaSelectFilterFile)));
			} else {

				final DTASelectParser parser = new DTASelectParser(dtaSelectFilterFile.toURI().toURL());
				if (fileNameWithTypeFasta != null)
					parser.setDbIndex(getDBIndexInterface(jobID, fileNameWithTypeFasta));
				final Map<String, edu.scripps.yates.utilities.proteomicsmodel.Protein> proteinMap = parser
						.getProteinMap();
				for (final String proteinAcc : proteinMap.keySet()) {
					// get the ProteinImpl in order to get the parsed Accession
					final Accession acc = FastaParser.getACC(proteinAcc);
					parsedAccs.add(acc.getAccession());
				}
				// add to cache
				ServerCacheProteinAccessionsByFileKey.getInstance().addtoCache(parsedAccs,
						getFileKey(dtaSelectFilterFile));
			}
			final List<String> ret = new ArrayList<String>();
			Collections.shuffle(parsedAccs);
			for (final String acc : parsedAccs) {
				ret.add(acc);
				if (ret.size() == numRandomValues)
					break;
			}

			log.info("returning " + ret.size() + " protein accessions from DTASelect-filter file");
			return ret;
		} finally {
			ServerTaskRegister.getInstance().endTask(task);
		}
	}

	private static String getFileKey(File file) {
		return String.valueOf(file.length());
	}

	public static List<String> getRandomProteinAccessionsFromDTASelectFile(int jobID,
			RemoteSSHFileReference dtaSelectFilterFile, FileNameWithTypeBean fileNameWithTypeFasta, int numRandomValues)
			throws IOException {
		log.info("Getting " + numRandomValues + " accessions from DTASelect-filter file at: "
				+ dtaSelectFilterFile.getHostName() + " - " + dtaSelectFilterFile.getRemotePath());
		return getRandomProteinAccessionsFromDTASelectFile(jobID, dtaSelectFilterFile.getRemoteFile(),
				fileNameWithTypeFasta, numRandomValues);
		// List<String> ret = new ArrayList<String>();
		// if (numRandomValues == 0)
		// return ret;
		// DTASelectParser parser = new DTASelectParser(dtaSelectFilterFile);
		// if (fileNameWithTypeFasta != null) {
		// parser.setDbIndex(getDBIndexInterface(jobID, projectFilesPath,
		// fileNameWithTypeFasta));
		// }
		// final Map<String, DTASelectProtein> proteinMap = parser
		// .getDTASelectProteins();
		// if (proteinMap != null) {
		// List<String> keyList = new ArrayList<String>();
		// keyList.addAll(proteinMap.keySet());
		// Collections.shuffle(keyList);
		// for (String proteinAcc : keyList) {
		// // get the ProteinImpl in order to get the parsed Accession
		// edu.scripps.yates.model.Protein protein = new
		// ProteinImplFromDTASelect(
		// proteinMap.get(proteinAcc));
		// ret.add(protein.getAccession());
		// if (ret.size() == numRandomValues)
		// break;
		// }
		// }
		// log.info("returning " + ret.size()
		// + " protein accessions from DTASelect-filter file");
		// return ret;
	}

	/**
	 * Due to the fact that not always the {@link ProteinBean} is linked to its
	 * {@link PSMBean}, this method will assure that if the 'getPSMs()' method on
	 * {@link ProteinBean} is not returning nothing, it will look into the database
	 * to get the corresponding PSMs
	 *
	 * @param proteinBean
	 * @param linkProteinWithPSMs if yes, subsequent calls to 'getPSMs()' on the
	 *                            proteinBean will return a non empty list of
	 *                            PSMBeans
	 * @return
	 */
	// public static List<PSMBean> getPSMsFromProtein(ProteinBean proteinBean,
	// boolean linkProteinWithPSMs) {
	// List<PSMBean> ret = new ArrayList<PSMBean>();
	//
	// if (proteinBean != null) {
	// final List<PSMBean> psms = proteinBean.getPsms();
	// if (psms == null || psms.isEmpty()) {
	// final TIntHashSet psmIds = proteinBean.getPsmIds();
	// if (psmIds != null && !psmIds.isEmpty()) {
	// final List<PSMBean> psMs2 = getPSMs(psmIds, null);
	// if (linkProteinWithPSMs) {
	// for (PSMBean psmBean : psMs2) {
	// proteinBean.addPSM(psmBean);
	// }
	// }
	// ret.addAll(psMs2);
	// }
	// } else {
	// // the proteinBean contains the PSMBeans
	// ret.addAll(psms);
	// }
	// }
	//
	// return ret;
	// }

	public static Set<String> getHiddenPTMs(String projectTag) throws PintException {
		try {
			if (hiddenPTMsByProject.containsKey(projectTag)) {
				return hiddenPTMsByProject.get(projectTag);
			}
			// return RemoteServicesTasks.getHiddenPTMs(projectTag);
			log.info("Getting hidden PTMs from project " + projectTag);
			final DefaultView defaultView = getDefaultViewByProject(projectTag);
			if (defaultView != null) {
				final Set<String> hiddenPTMs = defaultView.getHiddenPTMs();
				if (hiddenPTMs != null) {
					log.info("There are " + hiddenPTMs.size() + " hidden PTMs for project " + projectTag);
					for (final String hiddenPTM : hiddenPTMs) {
						log.info("Hidden PTM:" + hiddenPTM);
					}
					hiddenPTMsByProject.put(projectTag, hiddenPTMs);
					return hiddenPTMs;
				}
			}
			return Collections.emptySet();
		} catch (final Exception e) {
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	public static Set<String> getHiddenPTMs(Collection<String> projectTags) throws PintException {
		final Set<String> hiddenPTMs = new HashSet<String>();
		for (final String projectTag : projectTags) {
			hiddenPTMs.addAll(getHiddenPTMs(projectTag));
		}

		return hiddenPTMs;
	}

	public static DefaultView getDefaultViewByProject(String projectTag) throws PintException {
		try {
			// look into cache
			// DISABLED FOR THE MOMENT, TO ALLOW CHANGES IN THE SERVER
			// WITHOUT THE NEED OF RESTARTING TOMCAT
			// if (ServerCacheDefaultViewByProjectTag.getInstance().contains(
			// projectTag)) {
			// return ServerCacheDefaultViewByProjectTag.getInstance()
			// .getFromCache(projectTag);
			// } else {
			log.info("Reading default view for project " + projectTag);
			final DefaultViewReader defaultViewReader = new DefaultViewReader(projectTag);
			log.info("Default view reader created");

			final DefaultView defaultView = defaultViewReader.getDefaultView();
			if (defaultView != null) {
				// store in cache
				ServerCacheDefaultViewByProjectTag.getInstance().addtoCache(defaultView, projectTag);
				return defaultView;
			}
			// }
			log.info("Default view is null");
			return null;
		} catch (final Exception e) {
			log.error(e);
			e.printStackTrace();
			if (e instanceof PintException)
				throw e;
			throw new PintException(e, PINT_ERROR_TYPE.INTERNAL_ERROR);
		}
	}

	public static Map<String, Pair<DataSet, String>> batchQuery(File batchQueryFile, Set<String> projectTags,
			boolean testMode, boolean psmCentric, boolean includePeptides) {
		final Map<String, Pair<DataSet, String>> ret = new THashMap<String, Pair<DataSet, String>>();
		String queryText;
		BufferedReader br = null;
		final String projectString = getProjectString(projectTags);
		try {
			final InputStream fis = new FileInputStream(batchQueryFile);
			final InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
			br = new BufferedReader(isr);
			int queryNumber = 0;
			while ((queryText = br.readLine()) != null) {
				if ("".equals(queryText))
					continue;
				queryNumber++;
				ContextualSessionHandler.beginGoodTransaction();
				log.info("Query readed:" + queryText);
				final String sessionID = String.valueOf(queryNumber);

				final QueryInterface expressionTree = new QueryInterface(projectTags, queryText, testMode, psmCentric);

				final QueryResult result = expressionTree.getQueryResults();

				final Map<String, QueriableProteinSet> proteins = result.getProteins();
				log.info(proteins.size() + " proteins comming from command '" + queryText + "'");

				// adapt experimental conditions first, so that the protein to
				// condition mapper is ready
				for (final String projectTag : projectTags) {
					final List<Condition> conditions = PreparedCriteria.getCriteriaForConditionsInProject(projectTag);
					for (final Condition condition : conditions) {
						new ConditionBeanAdapter(condition, true, includePeptides).adapt();
					}
				}

				final List<ProteinBean> proteinBeans = new ArrayList<ProteinBean>();
				int i = 0;
				final DataSet dataSet = DataSetsManager.getDataSet(sessionID, projectString, true, psmCentric,
						getHiddenPTMs(projectTags));
				for (final String proteinAcc : proteins.keySet()) {
					final ProteinBean proteinBeanAdapted = new ProteinBeanAdapterFromProteinSet(
							proteins.get(proteinAcc), RemoteServicesTasks.getHiddenPTMs(projectTags), psmCentric,
							includePeptides).adapt();

					// add to current dataset
					dataSet.addProtein(proteinBeanAdapted);
					i++;
					log.debug(i + " / " + proteins.size() + " proteins adapted to beans");
					// final ProteinBean proteinBeanFromProtein = new
					// ProteinBeanAdapter(protein).adapt();
					proteinBeans.add(proteinBeanAdapted);
				}
				log.info(proteinBeans.size() + " protein beans created");

				// peptides
				final Map<String, QueriablePeptideSet> peptideMap = result.getPeptides();
				log.info(peptideMap.size() + " Peptides comming from command '" + queryText + "'");
				i = 0;
				for (final String sequence : peptideMap.keySet()) {
					i++;
					final QueriablePeptideSet queriablePeptide = peptideMap.get(sequence);

					final PeptideBean peptideBeanAdapted = new PeptideBeanAdapterFromPeptideSet(queriablePeptide,
							getHiddenPTMs(projectTags), psmCentric).adapt();
					// add to current dataset
					dataSet.addPeptide(peptideBeanAdapted);
					log.debug(i + " / " + peptideMap.size() + " peptides adapted to beans");
				}

				log.info("Exporting protein beans to file");
				final String urlString = FilenameUtils
						.getName(DataExporter.exportProteins(proteinBeans, queryText, psmCentric).getAbsolutePath());

				final Pair<DataSet, String> pair = new Pair<DataSet, String>(dataSet, urlString);
				// set datasetReady
				dataSet.setReady(true);
				log.info("Dataset created: " + dataSet);

				ret.put(queryText, pair);
				// ContextualSessionHandler.finishGoodTransaction();

			}

		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final java.io.IOException e) {
			e.printStackTrace();
		} catch (final MalformedQueryException e) {
			e.printStackTrace();
		} catch (final PintException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (final java.io.IOException e) {
				e.printStackTrace();
			}
		}
		return ret;
	}

	public static void annotateProteinsOMIM(Collection<ProteinBean> proteinBeans, String omimAPIKey) {
		try {
			// if (true) {
			// return;
			// }
			log.info("Getting OMIM annotations...");

			int numProteinsWithOMIMAnnotations = 0;

			final Map<String, List<ProteinBean>> proteinMap = new THashMap<String, List<ProteinBean>>();
			for (final ProteinBean proteinBean : proteinBeans) {
				if (proteinBean.getOrganism() != null && proteinBean.getOrganism().getNcbiTaxID() != null
						&& proteinBean.getOrganism().getNcbiTaxID().equals("9606")) {
					final String accession = proteinBean.getPrimaryAccession().getAccession();
					if (proteinMap.containsKey(accession)) {
						proteinMap.get(accession).add(proteinBean);
					} else {
						final List<ProteinBean> list = new ArrayList<ProteinBean>();
						list.add(proteinBean);
						proteinMap.put(accession, list);
					}
				}
			}
			if (proteinMap.isEmpty()) {
				log.info("Non human proteins found. Skipping OMIM retrieval.");
				return;
			}
			log.info("Getting OMIM annotations from " + proteinMap.size() + " proteins");
			final Map<String, List<OmimEntry>> associatedOmimEntries = OmimRetriever
					.getAssociatedOmimEntries(omimAPIKey, proteinMap.keySet());
			log.info("Assigning " + associatedOmimEntries.size() + " OMIM entries to proteins");
			for (final String acc : associatedOmimEntries.keySet()) {

				log.debug("Assigning OMIM entry to protein " + acc);
				if (proteinMap.containsKey(acc)) {
					final List<ProteinBean> list = proteinMap.get(acc);
					if (list != null) {
						log.debug("Assigning OMIM entry to " + list.size() + " proteins with acc: " + acc);
						for (final ProteinBean proteinBean : list) {
							final List<OmimEntry> omimEntries = associatedOmimEntries.get(acc);
							if (omimEntries != null) {
								log.debug("Assigning " + omimEntries.size() + " OMIM entries to " + list.size()
										+ " proteins with acc: " + acc);
								for (final OmimEntry omimEntry : omimEntries) {
									log.debug("adapting OMIM entry");
									proteinBean.addOMIMEntry(new OmimEntryBeanAdapter(omimEntry).adapt());
									log.debug("adapted OMIM entry");
									numProteinsWithOMIMAnnotations++;
								}
							}
						}
					}
				}
			}
			log.info("OMIM annotations retrieved for " + numProteinsWithOMIMAnnotations + "...");

		} catch (final Exception e) {
			e.printStackTrace();
			log.warn("Error when trying to get the OMIM information: '" + e.getMessage() + "'");
		}
		// TESTING PURPOSES
		// int omimID = new
		// Omim2UniprotIDMap().getOmimToUniprot().keySet().iterator().next();
		// TIntHashSet omimIDs = new TIntHashSet();
		// omimIDs.add(omimID);
		// final TIntObjectHashMap< OmimEntry> retrieveOmimEntries =
		// OmimRetriever.retrieveOmimEntries(omimAPIKey, omimIDs);
		// proteinBeans.get(0).addOMIMEntry(
		// new
		// OmimEntryBeanAdapter(retrieveOmimEntries.values().iterator().next()).adapt());

	}

	public static Set<ProteinBean> createProteinBeans(String sessionID, Map<String, Collection<Protein>> proteins,
			Set<String> hiddenPTMs, boolean psmCentric, boolean includePeptides) {
		log.info("Creating protein beans from " + proteins.size() + " different Proteins");
		final Set<ProteinBean> ret = new THashSet<ProteinBean>();
		int numProteins = 0;
		for (final String proteinAcc : proteins.keySet()) {
			if (numProteins == SharedConstants.MAX_NUM_PROTEINS)
				break;

			final Collection<Protein> proteinSet = proteins.get(proteinAcc);
			final ProteinBean proteinBeanAdapted = new ProteinBeanAdapterFromProteinSet(proteinSet, hiddenPTMs,
					psmCentric).adapt();
			if (sessionID != null) {
				DataSetsManager.getDataSet(sessionID, null, true, psmCentric, hiddenPTMs)
						.addProtein(proteinBeanAdapted);
			}
			ret.add(proteinBeanAdapted);
			log.info(numProteins++ + " / " + proteins.size() + " proteins: "
					+ proteinBeanAdapted.getPrimaryAccession().getAccession());
			//
		}
		log.info(ret.size() + "  proteins converted to beans");

		return ret;
	}

	public static Set<ProteinBean> createProteinBeansInParallel(String sessionID, Map<String, Set<Protein>> proteins,
			Set<String> hiddenPTMs, boolean psmCentric) {
		log.info("Creating protein beans from " + proteins.size() + " different Proteins");
		final Set<ProteinBean> ret = new THashSet<ProteinBean>();
		int numProteins = 0;
		for (final String proteinAcc : proteins.keySet()) {
			if (numProteins == SharedConstants.MAX_NUM_PROTEINS)
				break;

			final Set<Protein> proteinSet = proteins.get(proteinAcc);
			final ProteinBean proteinBeanAdapted = new ProteinBeanAdapterFromProteinSet(proteinSet, hiddenPTMs,
					psmCentric).adapt();
			if (sessionID != null) {
				DataSetsManager.getDataSet(sessionID, null, true, psmCentric, hiddenPTMs)
						.addProtein(proteinBeanAdapted);
			}
			ret.add(proteinBeanAdapted);
			log.info(numProteins++ + " / " + proteins.size() + " proteins: "
					+ proteinBeanAdapted.getPrimaryAccession().getAccession());
			//
		}
		log.info(ret.size() + "  proteins converted to beans");

		return ret;
	}

	/**
	 * Creates the Set of {@link ProteinBean} from the proteins. It also creates the
	 * {@link PSMBean}s that will be linked with the {@link ProteinBean}s. <br>
	 * Note that {@link PeptideBean}s are not created yet, so you will need to call
	 * to createPeptideBeans or createPeptideBeansFromPeptideMap after this.
	 * 
	 * @param sessionID
	 * @param proteins
	 * @param hiddenPTMs
	 * @param projectTagString
	 * @param proteinLevelQuery
	 * @param psmCentric
	 * @param includePeptides   to be used in the creation of the proteinBeans
	 * @return
	 */
	public static Set<ProteinBean> createProteinBeansFromQueriableProteins(String sessionID,
			Map<String, QueriableProteinSet> proteins, Set<String> hiddenPTMs, String projectTagString,
			boolean proteinLevelQuery, boolean psmCentric, boolean includePeptides) {
		log.info("Creating protein beans from " + proteins.size() + " different Proteins");
		final Set<ProteinBean> ret = new THashSet<ProteinBean>();
		final int numProteins = 0;
		final ProgressCounter counter = new ProgressCounter(proteins.size(), ProgressPrintingType.PERCENTAGE_STEPS, 0,
				true);
		counter.setSuffix("proteins converted to beans");
		log.info("First, we are going to query all associated peptide ids from " + proteins.size() + " proteins");
		final TIntSet proteinIDs = new TIntHashSet();
		for (final QueriableProteinSet queriableProteinSet : proteins.values()) {
			for (final Protein protein : queriableProteinSet.getAllProteins()) {
				proteinIDs.add(protein.getId());
			}
		}
		log.info("Querying peptide IDs from " + proteinIDs.size() + " protein IDs");
		final TIntSet peptideIdsFromProteins = PreparedCriteria
				.getPeptideIdsFromProteinIDsUsingNewProteinPeptideMapper(proteinIDs);
		log.info("Querying peptide objects from " + peptideIdsFromProteins.size() + " peptide IDs");
		final List<Peptide> peptides = PreparedCriteria.getPeptidesFromPeptideIDs(peptideIdsFromProteins, true, 500);
		log.info(peptides.size() + " peptides retrieved that will be stored in the cache.");

		for (final String proteinAcc : proteins.keySet()) {
			counter.increment();
			final String printIfNecessary = counter.printIfNecessary();
			if (!"".equals(printIfNecessary)) {
				log.info(printIfNecessary);
			}
			lookForInterruption();
			if (numProteins == SharedConstants.MAX_NUM_PROTEINS)// test purposes
				break;

			final QueriableProteinSet proteinSet = proteins.get(proteinAcc);
			ProteinBean proteinBeanAdapted = null;
			// added in Jan 2018 to reuse the proteinBeans in the cache that
			// will survive there if the query is all about protein level.
			final Integer proteinDBId = proteinSet.getIndividualProteins().iterator().next().getId();
			if (proteinLevelQuery && ServerCacheProteinBeansByProteinDBId.getInstance().contains(proteinDBId)) {
				proteinBeanAdapted = ServerCacheProteinBeansByProteinDBId.getInstance().getFromCache(proteinDBId);
			} else {
				proteinBeanAdapted = new ProteinBeanAdapterFromProteinSet(proteinSet, hiddenPTMs, psmCentric,
						includePeptides).adapt();
			}
			if (sessionID != null) {
				DataSetsManager.getDataSet(sessionID, projectTagString, true, psmCentric, hiddenPTMs)
						.addProtein(proteinBeanAdapted);
			}
			ret.add(proteinBeanAdapted);

			//

		}
		log.info(counter.printIfNecessary());
		log.info(ret.size() + "  proteins converted to beans");
		return ret;
	}

	private static void lookForInterruption() {
		if (Thread.currentThread().isInterrupted()) {
			log.info("This thread has been interrupted by other thread.");
			throw new PintRuntimeException(PINT_ERROR_TYPE.THREAD_INTERRUPTED);
		}

	}

	private static Map<String, Map<String, Set<AmountType>>> psmAmountMapByProject = new THashMap<String, Map<String, Set<AmountType>>>();

	public static Set<AmountType> getPSMAmountTypesByCondition(String projectTag, String conditionName)
			throws PintException {
		if (psmAmountMapByProject.containsKey(projectTag)) {
			final Map<String, Set<AmountType>> amountTypesByConditionName = psmAmountMapByProject.get(projectTag);
			if (amountTypesByConditionName.containsKey(conditionName)) {
				return amountTypesByConditionName.get(conditionName);
			}
		}
		if (!psmAmountMapByProject.containsKey(projectTag)) {
			final Map<String, Set<AmountType>> map = new HashMap<String, Set<AmountType>>();
			psmAmountMapByProject.put(projectTag, map);
		}
		if (!psmAmountMapByProject.get(projectTag).containsKey(conditionName)) {
			final Set<AmountType> set = new HashSet<AmountType>();
			psmAmountMapByProject.get(projectTag).put(conditionName, set);
		}
		final Set<AmountType> set = new HashSet<AmountType>();
		final Set<Condition> conditions = RemoteServicesTasks.getExperimentalConditionsFromProject(projectTag);
		final Map<Condition, Set<edu.scripps.yates.proteindb.persistence.mysql.AmountType>> amountTypeMap = PreparedQueries
				.getPSMAmountTypesByConditions(conditions);
		for (final Condition condition : amountTypeMap.keySet()) {
			if (!psmAmountMapByProject.get(projectTag).containsKey(condition.getName())) {
				final Set<AmountType> set2 = new HashSet<AmountType>();
				psmAmountMapByProject.get(projectTag).put(condition.getName(), set2);
			}

			for (final edu.scripps.yates.proteindb.persistence.mysql.AmountType amountType : amountTypeMap
					.get(condition)) {
				psmAmountMapByProject.get(projectTag).get(condition.getName())
						.add(AmountType.fromValue(amountType.getName()));
				if (condition.getName().equals(conditionName)) {
					final AmountType fromValue = AmountType.fromValue(amountType.getName());
					set.add(fromValue);
				}
			}
		}
		if (!set.isEmpty()) {
			psmAmountMapByProject.get(projectTag).get(conditionName).addAll(set);
		}
		return set;
	}

	private static Map<String, Map<String, Set<AmountType>>> peptideAmountMapByProject = new THashMap<String, Map<String, Set<AmountType>>>();

	public static Set<AmountType> getPeptideAmountTypesByCondition(String projectTag, String conditionName)
			throws PintException {
		if (peptideAmountMapByProject.containsKey(projectTag)) {
			final Map<String, Set<AmountType>> amountTypesByConditionName = peptideAmountMapByProject.get(projectTag);
			if (amountTypesByConditionName.containsKey(conditionName)) {
				return amountTypesByConditionName.get(conditionName);
			}
		}
		if (!peptideAmountMapByProject.containsKey(projectTag)) {
			final Map<String, Set<AmountType>> map = new HashMap<String, Set<AmountType>>();
			peptideAmountMapByProject.put(projectTag, map);
		}
		if (!peptideAmountMapByProject.get(projectTag).containsKey(conditionName)) {
			final Set<AmountType> set = new HashSet<AmountType>();
			peptideAmountMapByProject.get(projectTag).put(conditionName, set);
		}
		final Set<AmountType> set = new HashSet<AmountType>();
		final Set<Condition> conditions = RemoteServicesTasks.getExperimentalConditionsFromProject(projectTag);
		final Map<Condition, Set<edu.scripps.yates.proteindb.persistence.mysql.AmountType>> amountTypeMap = PreparedQueries
				.getPeptideAmountTypesByConditions(conditions);
		for (final Condition condition : amountTypeMap.keySet()) {
			if (!peptideAmountMapByProject.get(projectTag).containsKey(condition.getName())) {
				final Set<AmountType> set2 = new HashSet<AmountType>();
				peptideAmountMapByProject.get(projectTag).put(condition.getName(), set2);
			}

			for (final edu.scripps.yates.proteindb.persistence.mysql.AmountType amountType : amountTypeMap
					.get(condition)) {
				peptideAmountMapByProject.get(projectTag).get(condition.getName())
						.add(AmountType.fromValue(amountType.getName()));
				if (condition.getName().equals(conditionName)) {
					final AmountType fromValue = AmountType.fromValue(amountType.getName());
					set.add(fromValue);
				}
			}
		}
		if (!set.isEmpty()) {
			peptideAmountMapByProject.get(projectTag).get(conditionName).addAll(set);
		}
		return set;
	}

	private static Map<String, Map<String, Set<AmountType>>> proteinAmountMapByProject = new THashMap<String, Map<String, Set<AmountType>>>();

	public static Set<AmountType> getProteinAmountTypesByCondition(String projectTag, String conditionName)
			throws PintException {
		if (proteinAmountMapByProject.containsKey(projectTag)) {
			final Map<String, Set<AmountType>> amountTypesByConditionName = proteinAmountMapByProject.get(projectTag);
			if (amountTypesByConditionName.containsKey(conditionName)) {
				return amountTypesByConditionName.get(conditionName);
			}
		}
		if (!proteinAmountMapByProject.containsKey(projectTag)) {
			final Map<String, Set<AmountType>> map = new HashMap<String, Set<AmountType>>();
			proteinAmountMapByProject.put(projectTag, map);
		}
		if (!proteinAmountMapByProject.get(projectTag).containsKey(conditionName)) {
			final Set<AmountType> set = new HashSet<AmountType>();
			proteinAmountMapByProject.get(projectTag).put(conditionName, set);
		}
		final Set<AmountType> set = new HashSet<AmountType>();
		final Set<Condition> conditions = RemoteServicesTasks.getExperimentalConditionsFromProject(projectTag);
		final Map<Condition, Set<edu.scripps.yates.proteindb.persistence.mysql.AmountType>> amountTypeMap = PreparedQueries
				.getProteinAmountTypesByConditions(conditions);
		for (final Condition condition : amountTypeMap.keySet()) {
			if (!proteinAmountMapByProject.get(projectTag).containsKey(condition.getName())) {
				final Set<AmountType> set2 = new HashSet<AmountType>();
				proteinAmountMapByProject.get(projectTag).put(condition.getName(), set2);
			}
			for (final edu.scripps.yates.proteindb.persistence.mysql.AmountType amountType : amountTypeMap
					.get(condition)) {

				final AmountType amountTypeFromValue = AmountType.fromValue(amountType.getName());
				proteinAmountMapByProject.get(projectTag).get(condition.getName()).add(amountTypeFromValue);
				if (condition.getName().equals(conditionName)) {
					final AmountType fromValue = amountTypeFromValue;
					set.add(fromValue);
				}
			}
		}
		if (!set.isEmpty()) {
			proteinAmountMapByProject.get(projectTag).get(conditionName).addAll(set);
		}
		return set;
	}

	public static Map<String, Date> getUploadDatesFromProjects(Set<String> projectTags) {
		final Map<String, Date> ret = new HashMap<String, Date>();
		final Set<ProjectBean> projectBeans = getProjectBeans();
		for (final ProjectBean projectBean : projectBeans) {
			if (projectTags.contains(projectBean.getTag())) {
				ret.put(projectBean.getTag(), projectBean.getUploadedDate());
			}
		}
		return ret;
	}

}
