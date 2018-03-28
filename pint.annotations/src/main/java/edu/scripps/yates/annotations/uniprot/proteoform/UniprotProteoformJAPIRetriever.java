package edu.scripps.yates.annotations.uniprot.proteoform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import edu.scripps.yates.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinRemoteRetriever;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoFormAdapterFromConflictFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoformAdapterFromMutagenFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoformAdapterFromNaturalVariant;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.model.UniprotPTM;
import edu.scripps.yates.annotations.uniprot.proteoform.model.UniprotPTMAdapterFromCarbohydFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.UniprotPTMAdapterFromCrosslinkFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.UniprotPTMAdapterFromFeature;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsComment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsIsoform;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
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
	private UniProtService service;
	private final UniprotProteinLocalRetriever uplr;
	private boolean retrievePTMs = true;
	private boolean retrieveIsoforms = true;
	private int defaultChunkSize = 100;

	public UniprotProteoformJAPIRetriever(UniprotProteinLocalRetriever uplr) {
		this.uplr = uplr;
	}

	/**
	 * To call before getting variants
	 */
	public void startService() {
		service = Client.getServiceFactoryInstance().getUniProtQueryService();
		service.start();
	}

	/**
	 * To call after getting variants
	 */
	public void stopService() {
		service.stop();
	}

	@Override
	public List<Proteoform> getProteoformsFromOneEntry(String uniprotACC) {
		Map<String, List<Proteoform>> variants = getProteoform(uniprotACC);
		if (!variants.isEmpty()) {
			return variants.get(uniprotACC);
		}
		return Collections.emptyList();
	}

	@Override
	public Map<String, List<Proteoform>> getProteoform(Set<String> uniprotACCs) {
		return getProteoforms(uniprotACCs, defaultChunkSize);
	}

	public Map<String, List<Proteoform>> getProteoforms(Set<String> uniprotACCs, int chunkSize) {
		Map<String, List<Proteoform>> ret = new HashMap<String, List<Proteoform>>();
		List<String> uniprotAccList = new ArrayList<String>();
		uniprotAccList.addAll(uniprotACCs);
		try {
			// start service
			startService();
			if (retrieveIsoforms) {
				Set<String> isoformsACCs = new HashSet<String>();
				List<String> toQuery = new ArrayList<String>();
				for (String acc : uniprotAccList) {
					String isoformVersion = FastaParser.getIsoformVersion(acc);
					if (isoformVersion == null || "1".equals(isoformVersion)) {
						toQuery.add(acc);
					} else {
						isoformsACCs.add(acc);
					}
				}
				if (!toQuery.isEmpty()) {
					for (int index = 0; index < uniprotAccList.size(); index += chunkSize) {
						Set<String> toQuery2 = new HashSet<String>();
						int from = index;
						int to = Math.min(from + chunkSize, uniprotAccList.size());
						toQuery2.addAll(toQuery.subList(from, to));
						log.info("Querying " + toQuery2.size() + " proteins from " + from + " to " + to);
						Query query = UniProtQueryBuilder.accessions(toQuery2)
								.and(UniProtQueryBuilder.commentsType(CommentType.ALTERNATIVE_PRODUCTS));
						QueryResult<UniProtEntry> result = service.getEntries(query);
						while (result.hasNext()) {
							UniProtEntry mainEntry = result.next();
							List<Comment> comments = mainEntry.getComments(CommentType.ALTERNATIVE_PRODUCTS);
							// store isoforms ACCs
							for (Comment comment : comments) {
								AlternativeProductsComment alternativeProducts = (AlternativeProductsComment) comment;
								List<AlternativeProductsIsoform> isoforms = alternativeProducts.getIsoforms();
								for (AlternativeProductsIsoform alternativeProductsIsoform : isoforms) {
									String isoformACC = alternativeProductsIsoform.getIds().get(0).getValue();
									if ("1".equals(FastaParser.getIsoformVersion(isoformACC))) {
										// no isoform
										continue;
									}
									isoformsACCs.add(isoformACC);

								}
							}
						}
					}
				}
				retrieveIsoformsFirst(isoformsACCs);
			}
			for (int index = 0; index < uniprotAccList.size(); index += chunkSize) {
				Set<String> toQuery = new HashSet<String>();
				int from = index;
				int to = Math.min(from + chunkSize, uniprotAccList.size());
				toQuery.addAll(uniprotAccList.subList(from, to));
				log.info("Querying " + toQuery.size() + " proteins from " + from + " to " + to);

				Query query = UniProtQueryBuilder.accessions(toQuery)
						.and(UniProtQueryBuilder.or(UniProtQueryBuilder.featuresType(FeatureType.VARIANT),
								UniProtQueryBuilder.commentsType(CommentType.ALTERNATIVE_PRODUCTS),
								UniProtQueryBuilder.featuresType(FeatureType.CONFLICT),
								UniProtQueryBuilder.featuresType(FeatureType.MUTAGEN),
								UniProtQueryBuilder.featuresType(FeatureType.MOD_RES),
								UniProtQueryBuilder.featuresType(FeatureType.SITE),
								UniProtQueryBuilder.featuresType(FeatureType.CARBOHYD),
								UniProtQueryBuilder.featuresType(FeatureType.CROSSLNK)));

				QueryResult<UniProtEntry> result = service.getEntries(query);
				while (result.hasNext()) {
					UniProtEntry mainEntry = result.next();
					String originalSequence = mainEntry.getSequence().getValue();
					// original variant
					String acc = mainEntry.getPrimaryUniProtAccession().getValue();
					if (!ret.containsKey(acc)) {
						List<Proteoform> list = new ArrayList<Proteoform>();
						ret.put(acc, list);
					}
					Proteoform originalvariant = new Proteoform(acc, acc, originalSequence,
							mainEntry.getProteinDescription().getRecommendedName().getFields().get(0).getValue(), null,
							true);
					ret.get(acc).add(originalvariant);

					// query for variants
					Collection<Feature> features = mainEntry.getFeatures(FeatureType.VARIANT);
					for (Feature feature : features) {
						VariantFeature varSeq = (VariantFeature) feature;
						Proteoform variant = new ProteoformAdapterFromNaturalVariant(acc, varSeq, originalSequence)
								.adapt();
						ret.get(acc).add(variant);
					}
					// alternative products
					List<Comment> comments = mainEntry.getComments(CommentType.ALTERNATIVE_PRODUCTS);
					// store isoforms ACCs
					Set<String> isoformsACCs = new HashSet<String>();
					for (Comment comment : comments) {
						AlternativeProductsComment alternativeProducts = (AlternativeProductsComment) comment;
						List<AlternativeProductsIsoform> isoforms = alternativeProducts.getIsoforms();
						for (AlternativeProductsIsoform alternativeProductsIsoform : isoforms) {
							String isoformACC = alternativeProductsIsoform.getIds().get(0).getValue();
							if ("1".equals(FastaParser.getIsoformVersion(isoformACC))) {
								// no isoform
								continue;
							}
							isoformsACCs.add(isoformACC);

						}
					}
					// retrieve isoform sequences
					if (retrieveIsoforms) {
						// if there is no UPLR, get isoform fasta from internet,
						if (this.uplr == null) {
							Map<String, Entry> isoformEntries = UniprotProteinRemoteRetriever
									.getFASTASequencesInParallel(isoformsACCs);
							for (String isoformACC : isoformsACCs) {
								Entry isoformEntry = isoformEntries.get(isoformACC);
								if (isoformEntry == null || isoformEntry.getSequence() == null) {
									continue;
								}
								String isoformSequence = isoformEntry.getSequence().getValue();

								Proteoform variant = new Proteoform(acc,
										isoformACC, isoformSequence, mainEntry.getProteinDescription()
												.getRecommendedName().getFields().get(0).getValue(),
										ProteoformType.ISOFORM);
								ret.get(acc).add(variant);
							}
						} else {
							Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
							for (String isoformACC : isoformsACCs) {
								if (entries.containsKey(isoformACC)) {
									Entry isoformEntry = entries.get(isoformACC);
									String isoformSequence = UniprotEntryUtil.getProteinSequence(isoformEntry);
									String description = UniprotEntryUtil.getProteinDescription(isoformEntry);
									Proteoform variant = new Proteoform(acc, isoformACC, isoformSequence, description,
											ProteoformType.ISOFORM);
									ret.get(acc).add(variant);
								}
							}
						}
					}
					// sequence conflicts
					Collection<Feature> conflicts = mainEntry.getFeatures(FeatureType.CONFLICT);
					for (Feature feature : conflicts) {

						ConflictFeature conflictFeature = (ConflictFeature) feature;
						Proteoform variant = new ProteoFormAdapterFromConflictFeature(acc, conflictFeature,
								originalSequence).adapt();
						ret.get(acc).add(variant);
					}
					// mutagens
					Collection<Feature> mutagens = mainEntry.getFeatures(FeatureType.MUTAGEN);
					for (Feature feature : mutagens) {
						MutagenFeature mutagenFeature = (MutagenFeature) feature;
						Proteoform variant = new ProteoformAdapterFromMutagenFeature(acc, mutagenFeature,
								originalSequence).adapt();
						ret.get(acc).add(variant);
					}
					// ptms
					if (retrievePTMs) {
						Collection<Feature> modifiedResiduesFeatures = mainEntry.getFeatures(FeatureType.MOD_RES);
						for (Feature feature : modifiedResiduesFeatures) {
							ModResFeature modResFeature = (ModResFeature) feature;
							UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(modResFeature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						Collection<Feature> siteFeatures = mainEntry.getFeatures(FeatureType.SITE);
						for (Feature feature : siteFeatures) {
							SiteFeature siteFeature = (SiteFeature) feature;
							UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(siteFeature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						Collection<Feature> carbohydFeatures = mainEntry.getFeatures(FeatureType.CARBOHYD);
						for (Feature feature : carbohydFeatures) {
							CarbohydFeature carboHydFeature = (CarbohydFeature) feature;
							UniprotPTM uniprotPTM = new UniprotPTMAdapterFromCarbohydFeature(carboHydFeature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						Collection<Feature> crosslinkFeatures = mainEntry.getFeatures(FeatureType.CROSSLNK);
						for (Feature feature : crosslinkFeatures) {
							CrosslinkFeature crosslinkFeature = (CrosslinkFeature) feature;
							List<UniprotPTM> uniprotPTMs = new UniprotPTMAdapterFromCrosslinkFeature(crosslinkFeature)
									.adapt();
							if (uniprotPTMs != null) {
								for (UniprotPTM uniprotPTM : uniprotPTMs) {
									originalvariant.addPTM(uniprotPTM);
								}

							}
						}
					}
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} finally {
			stopService();
		}
		return ret;

	}

	private void retrieveIsoformsFirst(Set<String> isoformsACCs) {
		long t1 = System.currentTimeMillis();
		log.info("Retrieving isoform fasta sequences all at once first");
		// if there is no UPLR, get isoform fasta from internet,
		if (this.uplr == null) {
			Map<String, Entry> isoformEntries = UniprotProteinRemoteRetriever.getFASTASequencesInParallel(isoformsACCs);
			log.info(isoformEntries.size() + " entries retrieved.");
		} else {
			Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
			log.info(entries.size() + " entries retrieved.");
		}
		log.info("It took " + DatesUtil.getDescriptiveTimeFromMillisecs((System.currentTimeMillis() - t1)));
	}

	@Override
	public Map<String, List<Proteoform>> getProteoform(String... uniprotACCs) {
		List<String> asList = Arrays.asList(uniprotACCs);
		return getProteoform(new HashSet<String>(asList));
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

}
