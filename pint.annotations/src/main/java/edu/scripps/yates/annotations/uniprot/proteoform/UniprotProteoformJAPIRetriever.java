package edu.scripps.yates.annotations.uniprot.proteoform;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.scripps.yates.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.annotations.uniprot.UniprotFastaRetriever;
import edu.scripps.yates.annotations.uniprot.UniprotProteinLocalRetriever;
import edu.scripps.yates.annotations.uniprot.proteoform.model.UniprotPTM;
import edu.scripps.yates.annotations.uniprot.proteoform.model.UniprotPTMAdapterFromCarbohydFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.UniprotPTMAdapterFromFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoFormAdapterFromConflictFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoformAdapterFromMutagenFeature;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoformAdapterFromNaturalVariant;
import edu.scripps.yates.annotations.uniprot.proteoform.model.ProteoformType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.FastaParser;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsComment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsIsoform;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.features.CarbohydFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ConflictFeature;
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
	private UniProtService service;
	private final UniprotProteinLocalRetriever uplr;
	private boolean retrievePTMs = true;

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
	public List<Proteoform> getVariantsFromOneEntry(String uniprotACC) {
		Map<String, List<Proteoform>> variants = getVariants(uniprotACC);
		if (!variants.isEmpty()) {
			return variants.get(uniprotACC);
		}
		return Collections.emptyList();
	}

	@Override
	public Map<String, List<Proteoform>> getVariants(Set<String> uniprotACCs) {
		Map<String, List<Proteoform>> ret = new HashMap<String, List<Proteoform>>();

		try {
			// start service
			startService();

			Query query = UniProtQueryBuilder.accessions(uniprotACCs)
					.and(UniProtQueryBuilder.or(UniProtQueryBuilder.featuresType(FeatureType.VARIANT),
							UniProtQueryBuilder.commentsType(CommentType.ALTERNATIVE_PRODUCTS),
							UniProtQueryBuilder.featuresType(FeatureType.CONFLICT),
							UniProtQueryBuilder.featuresType(FeatureType.MOD_RES),
							UniProtQueryBuilder.featuresType(FeatureType.SITE)));
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
				Proteoform originalvariant = new Proteoform(acc, originalSequence,
						mainEntry.getProteinDescription().getRecommendedName().getFields().get(0).getValue(), null,
						true);
				ret.get(acc).add(originalvariant);

				// query for variants
				Collection<Feature> features = mainEntry.getFeatures(FeatureType.VARIANT);
				for (Feature feature : features) {
					VariantFeature varSeq = (VariantFeature) feature;
					Proteoform variant = new ProteoformAdapterFromNaturalVariant(varSeq, originalSequence).adapt();
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
				// if there is no UPLR, get isoform fasta from internet,
				if (this.uplr == null) {
					for (String isoformACC : isoformsACCs) {

						Entry isoformEntry = UniprotFastaRetriever.getFastaEntry(isoformACC);
						if (isoformEntry == null || isoformEntry.getSequence() == null) {
							continue;
						}
						String isoformSequence = isoformEntry.getSequence().getValue();

						Proteoform variant = new Proteoform(isoformACC, isoformSequence,
								mainEntry.getProteinDescription().getRecommendedName().getFields().get(0).getValue(),
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
							Proteoform variant = new Proteoform(isoformACC, isoformSequence, description,
									ProteoformType.ISOFORM);
							ret.get(acc).add(variant);
						}
					}
				}
				// sequence conflicts
				Collection<Feature> conflicts = mainEntry.getFeatures(FeatureType.CONFLICT);
				for (Feature feature : conflicts) {

					ConflictFeature conflictFeature = (ConflictFeature) feature;
					Proteoform variant = new ProteoFormAdapterFromConflictFeature(conflictFeature, originalSequence).adapt();
					ret.get(acc).add(variant);
				}
				// mutagens
				Collection<Feature> mutagens = mainEntry.getFeatures(FeatureType.MUTAGEN);
				for (Feature feature : mutagens) {
					MutagenFeature mutagenFeature = (MutagenFeature) feature;
					Proteoform variant = new ProteoformAdapterFromMutagenFeature(mutagenFeature, originalSequence).adapt();
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
				}
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			stopService();
		}
		return ret;

	}

	@Override
	public Map<String, List<Proteoform>> getVariants(String... uniprotACCs) {
		List<String> asList = Arrays.asList(uniprotACCs);
		return getVariants(new HashSet<String>(asList));
	}

	public boolean isRetrievePTMs() {
		return retrievePTMs;
	}

	public void setRetrievePTMs(boolean retrievePTMs) {
		this.retrievePTMs = retrievePTMs;
	}

}
