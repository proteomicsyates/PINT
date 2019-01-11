package edu.scripps.yates.annotations.uniprot.proteoform.japi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRemoteRetriever;
import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotPTM;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotProteoformRetriever;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.util.Pair;
import uk.ac.ebi.kraken.interfaces.uniprot.Gene;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsComment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsIsoform;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.IsoformSequenceStatus;
import uk.ac.ebi.kraken.interfaces.uniprot.features.CarbohydFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ConflictFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.CrosslinkFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.Feature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ModResFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.MutagenFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.SiteFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.VariantFeature;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.QueryResult;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtQueryBuilder;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;
import uk.ac.ebi.uniprot.dataservice.query.Query;

/**
 * It uses the Uniprot Java API to retrieve variants of proteins
 * 
 * @author Salva
 *
 */
public class UniprotProteoformJAPIRetriever implements UniprotProteoformRetriever {
	private static final Logger log = Logger.getLogger(UniprotProteoformJAPIRetriever.class);
	private final UniprotProteinLocalRetriever uplr;
	private boolean retrievePTMs = true;
	private boolean retrieveIsoforms = true;
	protected static final int defaultChunkSize = 100;

	public UniprotProteoformJAPIRetriever(UniprotProteinLocalRetriever uplr) {
		this.uplr = uplr;
	}

	/**
	 * To call before getting variants
	 * 
	 * @return
	 */
	public static UniProtService createService() {
		UniProtService service = Client.getServiceFactoryInstance().getUniProtQueryService();

		return service;
	}

	/**
	 * To call after getting variants
	 * 
	 * @param service2
	 */
	private static void stopService(UniProtService service) {
		service.stop();
	}

	@Override
	public List<Proteoform> getProteoformsFromOneEntry(String uniprotACC) {
		final Map<String, List<Proteoform>> variants = getProteoforms(uniprotACC);
		if (!variants.isEmpty()) {
			return variants.get(uniprotACC);
		}
		return Collections.emptyList();
	}

	@Override
	public Map<String, List<Proteoform>> getProteoforms(Collection<String> uniprotACCs) {
		return getProteoforms(uniprotACCs, defaultChunkSize, retrieveIsoforms, retrievePTMs, uplr);
	}

	protected static Map<String, List<Proteoform>> getProteoforms(Collection<String> uniprotACCs, int chunkSize,
			boolean retrieveIsoforms, boolean retrievePTMs, UniprotProteinLocalRetriever uplr) {
		final Map<String, List<Proteoform>> ret = new HashMap<String, List<Proteoform>>();
		final List<String> uniprotAccList = new ArrayList<String>();
		uniprotAccList.addAll(uniprotACCs);
		UniProtService service = null;
		try {
			// start service
			service = createService();
			service.start();
			if (retrieveIsoforms) {
				final Set<String> isoformsACCs = new HashSet<String>();
				final List<String> toQuery = new ArrayList<String>();
				for (final String acc : uniprotAccList) {
					final String isoformVersion = FastaParser.getIsoformVersion(acc);
					if (isoformVersion == null) {
						toQuery.add(acc);
					} else {
						isoformsACCs.add(acc);
					}
				}
				if (!toQuery.isEmpty()) {
					for (int index = 0; index < uniprotAccList.size(); index += chunkSize) {
						final Set<String> toQuery2 = new HashSet<String>();
						final int from = index;
						final int to = Math.min(from + chunkSize, uniprotAccList.size());
						toQuery2.addAll(toQuery.subList(from, to));
						log.info("Querying " + toQuery2.size() + " proteins from " + from + " to " + to + " out of "
								+ uniprotAccList.size());
						final Query query = UniProtQueryBuilder.accessions(toQuery2)
								.and(UniProtQueryBuilder.commentsType(CommentType.ALTERNATIVE_PRODUCTS));
						final QueryResult<UniProtEntry> result = service.getEntries(query);
						while (result.hasNext()) {
							final UniProtEntry mainEntry = result.next();
							final List<Comment> comments = mainEntry.getComments(CommentType.ALTERNATIVE_PRODUCTS);
							// store isoforms ACCs
							for (final Comment comment : comments) {
								final AlternativeProductsComment alternativeProducts = (AlternativeProductsComment) comment;
								final List<AlternativeProductsIsoform> isoforms = alternativeProducts.getIsoforms();
								for (final AlternativeProductsIsoform alternativeProductsIsoform : isoforms) {
									final String isoformACC = alternativeProductsIsoform.getIds().get(0).getValue();
									isoformsACCs.add(isoformACC);

								}
							}
						}
					}
				}
				retrieveIsoformsFirst(isoformsACCs, uplr);
			}
			for (int index = 0; index < uniprotAccList.size(); index += chunkSize) {
				final Set<String> toQuery = new HashSet<String>();
				final int from = index;
				final int to = Math.min(from + chunkSize, uniprotAccList.size());
				toQuery.addAll(uniprotAccList.subList(from, to));
				log.info("Querying " + toQuery.size() + " proteins from " + from + " to " + to + " out of "
						+ uniprotAccList.size());

				final Query query = UniProtQueryBuilder.accessions(toQuery)
						.and(UniProtQueryBuilder.or(UniProtQueryBuilder.featuresType(FeatureType.VARIANT),
								UniProtQueryBuilder.commentsType(CommentType.ALTERNATIVE_PRODUCTS),
								UniProtQueryBuilder.featuresType(FeatureType.CONFLICT),
								UniProtQueryBuilder.featuresType(FeatureType.MUTAGEN),
								UniProtQueryBuilder.featuresType(FeatureType.MOD_RES),
								UniProtQueryBuilder.featuresType(FeatureType.SITE),
								UniProtQueryBuilder.featuresType(FeatureType.CARBOHYD),
								UniProtQueryBuilder.featuresType(FeatureType.CROSSLNK)));

				final QueryResult<UniProtEntry> result = service.getEntries(query);
				while (result.hasNext()) {
					final UniProtEntry mainEntry = result.next();
					final String originalSequence = mainEntry.getSequence().getValue();
					// original variant
					final String acc = mainEntry.getPrimaryUniProtAccession().getValue();
					if (!ret.containsKey(acc)) {
						final List<Proteoform> list = new ArrayList<Proteoform>();
						ret.put(acc, list);
					}
					String description = acc;
					if (mainEntry.getProteinDescription() != null) {
						if (mainEntry.getProteinDescription().getRecommendedName() != null) {
							if (mainEntry.getProteinDescription().getRecommendedName().getFields() != null) {
								if (!mainEntry.getProteinDescription().getRecommendedName().getFields().isEmpty()) {
									description = mainEntry.getProteinDescription().getRecommendedName().getFields()
											.get(0).getValue();
								} else {
									description = "Unknown";
								}
							}
						}
					}
					String gene = null;
					if (mainEntry.getGenes() != null && !mainEntry.getGenes().isEmpty()) {
						final Gene geneObj = mainEntry.getGenes().get(0);
						if (geneObj.getGeneName() != null) {
							gene = geneObj.getGeneName().getValue();
						}
					}
					String taxonomy = null;
					if (mainEntry.getOrganism() != null) {
						if (mainEntry.getOrganism().getScientificName() != null) {
							taxonomy = mainEntry.getOrganism().getScientificName().getValue();
						}
					}
					String name = null;
					if (mainEntry.getUniProtId() != null && mainEntry.getUniProtId().getValue() != null) {
						name = mainEntry.getUniProtId().getValue();
					}
					final Proteoform originalvariant = new Proteoform(acc, originalSequence, acc, originalSequence,
							name, description, gene, taxonomy, ProteoformType.MAIN_ENTRY, true);
					ret.get(acc).add(originalvariant);

					// query for variants
					final Collection<Feature> features = mainEntry.getFeatures(FeatureType.VARIANT);
					for (final Feature feature : features) {
						final VariantFeature varSeq = (VariantFeature) feature;
						final Proteoform variant = new ProteoformAdapterFromNaturalVariant(acc, name, description,
								varSeq, originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// alternative products
					final List<Comment> comments = mainEntry.getComments(CommentType.ALTERNATIVE_PRODUCTS);
					// store isoforms ACCs
					final Set<String> isoformsACCs = new HashSet<String>();
					for (final Comment comment : comments) {
						final AlternativeProductsComment alternativeProducts = (AlternativeProductsComment) comment;
						final List<AlternativeProductsIsoform> isoforms = alternativeProducts.getIsoforms();
						for (final AlternativeProductsIsoform alternativeProductsIsoform : isoforms) {
							if (alternativeProductsIsoform
									.getIsoformSequenceStatus() != IsoformSequenceStatus.DISPLAYED) {
								final String isoformACC = alternativeProductsIsoform.getIds().get(0).getValue();
								isoformsACCs.add(isoformACC);
							}
						}
					}
					// retrieve isoform sequences
					if (retrieveIsoforms) {
						// if there is no UPLR, get isoform fasta from internet,
						if (uplr == null) {
							final Map<String, Entry> isoformEntries = UniprotProteinRemoteRetriever
									.getFASTASequencesInParallel(isoformsACCs);
							for (final String isoformACC : isoformsACCs) {
								final Entry isoformEntry = isoformEntries.get(isoformACC);
								if (isoformEntry == null || isoformEntry.getSequence() == null) {
									continue;
								}
								final String isoformSequence = isoformEntry.getSequence().getValue();
								final List<Pair<String, String>> geneNames = UniprotEntryUtil.getGeneName(isoformEntry,
										true, true);
								String gene2 = null;
								if (geneNames.isEmpty()) {
									gene2 = geneNames.get(0).getFirstelement();
								}
								final String taxonomy2 = UniprotEntryUtil.getTaxonomyName(isoformEntry);

								final String name2 = UniprotEntryUtil.getNames(isoformEntry).get(0);
								final String proteinDescription = UniprotEntryUtil.getProteinDescription(isoformEntry);
								final Proteoform variant = new Proteoform(acc, originalSequence, isoformACC,
										isoformSequence, name2, proteinDescription, gene2, taxonomy2,
										ProteoformType.ISOFORM);
								ret.get(acc).add(variant);
							}
						} else {
							final Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
							for (final String isoformACC : isoformsACCs) {
								if (entries.containsKey(isoformACC)) {
									final Entry isoformEntry = entries.get(isoformACC);
									final String isoformSequence = UniprotEntryUtil.getProteinSequence(isoformEntry);
									final String description2 = UniprotEntryUtil.getProteinDescription(isoformEntry);
									final String name2 = UniprotEntryUtil.getNames(isoformEntry).get(0);
									final List<Pair<String, String>> geneNames = UniprotEntryUtil
											.getGeneName(isoformEntry, true, true);
									String gene2 = null;
									if (geneNames.isEmpty()) {
										gene2 = geneNames.get(0).getFirstelement();
									}
									final String taxonomy2 = UniprotEntryUtil.getTaxonomyName(isoformEntry);
									final Proteoform variant = new Proteoform(acc, originalSequence, isoformACC,
											isoformSequence, name2, description2, gene2, taxonomy2,
											ProteoformType.ISOFORM);
									ret.get(acc).add(variant);
								}
							}
						}
					}
					// sequence conflicts
					final Collection<Feature> conflicts = mainEntry.getFeatures(FeatureType.CONFLICT);
					for (final Feature feature : conflicts) {

						final ConflictFeature conflictFeature = (ConflictFeature) feature;
						final Proteoform variant = new ProteoFormAdapterFromConflictFeature(acc, name, description,
								conflictFeature, originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// mutagens
					final Collection<Feature> mutagens = mainEntry.getFeatures(FeatureType.MUTAGEN);
					for (final Feature feature : mutagens) {
						final MutagenFeature mutagenFeature = (MutagenFeature) feature;
						final Proteoform variant = new ProteoformAdapterFromMutagenFeature(acc, name, description,
								mutagenFeature, originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// ptms
					if (retrievePTMs) {
						final Collection<Feature> modifiedResiduesFeatures = mainEntry.getFeatures(FeatureType.MOD_RES);
						for (final Feature feature : modifiedResiduesFeatures) {
							final ModResFeature modResFeature = (ModResFeature) feature;
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(modResFeature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<Feature> siteFeatures = mainEntry.getFeatures(FeatureType.SITE);
						for (final Feature feature : siteFeatures) {
							final SiteFeature siteFeature = (SiteFeature) feature;
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(siteFeature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<Feature> carbohydFeatures = mainEntry.getFeatures(FeatureType.CARBOHYD);
						for (final Feature feature : carbohydFeatures) {
							final CarbohydFeature carboHydFeature = (CarbohydFeature) feature;
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromCarbohydFeature(carboHydFeature)
									.adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<Feature> crosslinkFeatures = mainEntry.getFeatures(FeatureType.CROSSLNK);
						for (final Feature feature : crosslinkFeatures) {
							final CrosslinkFeature crosslinkFeature = (CrosslinkFeature) feature;
							final List<UniprotPTM> uniprotPTMs = new UniprotPTMAdapterFromCrosslinkFeature(
									crosslinkFeature).adapt();
							if (uniprotPTMs != null) {
								for (final UniprotPTM uniprotPTM : uniprotPTMs) {
									originalvariant.addPTM(uniprotPTM);
								}

							}
						}
					}
				}
			}
		} catch (final ServiceException e) {
			e.printStackTrace();
		} finally {
			stopService(service);
		}
		return ret;

	}

	private static void retrieveIsoformsFirst(Set<String> isoformsACCs, UniprotProteinLocalRetriever uplr) {
		final long t1 = System.currentTimeMillis();
		log.info("Retrieving isoform fasta sequences all at once first");
		// if there is no UPLR, get isoform fasta from internet,
		if (uplr == null) {
			final Map<String, Entry> isoformEntries = UniprotProteinRemoteRetriever
					.getFASTASequencesInParallel(isoformsACCs);
			log.info(isoformEntries.size() + " entries retrieved.");
		} else {
			final Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
			log.info(entries.size() + " entries retrieved.");
		}
		log.info("It took " + DatesUtil.getDescriptiveTimeFromMillisecs((System.currentTimeMillis() - t1)));
	}

	@Override
	public Map<String, List<Proteoform>> getProteoforms(String... uniprotACCs) {
		final List<String> asList = Arrays.asList(uniprotACCs);
		return getProteoforms(new HashSet<String>(asList));
	}

	public boolean isRetrievePTMs() {
		return retrievePTMs;
	}

	public void setRetrievePTMs(boolean retrievePTMs) {
		this.retrievePTMs = retrievePTMs;
	}

	public boolean isRetrieveIsoforms() {
		return retrieveIsoforms;
	}

	public void setRetrieveIsoforms(boolean retrieveIsoforms) {
		this.retrieveIsoforms = retrieveIsoforms;
	}

	@Override
	public Iterator<Proteoform> getProteoformIterator(Collection<String> uniprotACCs) {
		return new ProteoformRetrieverIteratorFromJAPI(uniprotACCs, retrieveIsoforms, retrievePTMs, uplr);
	}

	@Override
	public Iterator<Proteoform> getProteoformIterator(String... uniprotACCs) {
		final Set<String> set = new HashSet<String>();
		for (final String uniprotACC : uniprotACCs) {
			set.add(uniprotACC);
		}
		return getProteoformIterator(set);
	}

}
