package edu.scripps.yates.annotations.uniprot.variant;

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
import edu.scripps.yates.annotations.uniprot.variant.model.Variant;
import edu.scripps.yates.annotations.uniprot.variant.model.VariantAdapterFromConflictFeature;
import edu.scripps.yates.annotations.uniprot.variant.model.VariantAdapterFromMutagenFeature;
import edu.scripps.yates.annotations.uniprot.variant.model.VariantAdapterFromNaturalVariant;
import edu.scripps.yates.annotations.uniprot.variant.model.VariantType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.fasta.FastaParser;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsComment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.AlternativeProductsIsoform;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.features.ConflictFeature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.Feature;
import uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType;
import uk.ac.ebi.kraken.interfaces.uniprot.features.MutagenFeature;
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
public class UniprotVariantJAPIRetriever implements UniprotVariantRetriever {
	private UniProtService service;
	private final UniprotProteinLocalRetriever uplr;

	public UniprotVariantJAPIRetriever(UniprotProteinLocalRetriever uplr) {
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
	public List<Variant> getVariantsFromOneEntry(String uniprotACC) {
		Map<String, List<Variant>> variants = getVariants(uniprotACC);
		if (!variants.isEmpty()) {
			return variants.get(uniprotACC);
		}
		return Collections.emptyList();
	}

	@Override
	public Map<String, List<Variant>> getVariants(Set<String> uniprotACCs) {
		Map<String, List<Variant>> ret = new HashMap<String, List<Variant>>();

		try {
			// start service
			startService();

			Query query = UniProtQueryBuilder.accessions(uniprotACCs)
					.and(UniProtQueryBuilder.or(UniProtQueryBuilder.featuresType(FeatureType.VARIANT),
							UniProtQueryBuilder.commentsType(CommentType.ALTERNATIVE_PRODUCTS),
							UniProtQueryBuilder.featuresType(FeatureType.CONFLICT)));
			QueryResult<UniProtEntry> result = service.getEntries(query);
			while (result.hasNext()) {
				UniProtEntry mainEntry = result.next();
				String originalSequence = mainEntry.getSequence().getValue();
				// original variant
				String acc = mainEntry.getPrimaryUniProtAccession().getValue();
				if (!ret.containsKey(acc)) {
					List<Variant> list = new ArrayList<Variant>();
					ret.put(acc, list);
				}
				Variant originalvariant = new Variant(acc, originalSequence,
						mainEntry.getProteinDescription().getRecommendedName().getFields().get(0).getValue(), null,
						true);
				ret.get(acc).add(originalvariant);

				// query for variants
				Collection<Feature> features = mainEntry.getFeatures(FeatureType.VARIANT);
				for (Feature feature : features) {
					VariantFeature varSeq = (VariantFeature) feature;
					Variant variant = new VariantAdapterFromNaturalVariant(varSeq, originalSequence).adapt();
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

						Variant variant = new Variant(isoformACC, isoformSequence,
								mainEntry.getProteinDescription().getRecommendedName().getFields().get(0).getValue(),
								VariantType.ISOFORM);
						ret.get(acc).add(variant);
					}
				} else {
					Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
					for (String isoformACC : isoformsACCs) {
						if (entries.containsKey(isoformACC)) {
							Entry isoformEntry = entries.get(isoformACC);
							String isoformSequence = UniprotEntryUtil.getProteinSequence(isoformEntry);
							String description = UniprotEntryUtil.getProteinDescription(isoformEntry);
							Variant variant = new Variant(isoformACC, isoformSequence, description,
									VariantType.ISOFORM);
							ret.get(acc).add(variant);
						}
					}
				}
				// sequence conflicts
				Collection<Feature> conflicts = mainEntry.getFeatures(FeatureType.CONFLICT);
				for (Feature feature : conflicts) {

					ConflictFeature conflictFeature = (ConflictFeature) feature;
					Variant variant = new VariantAdapterFromConflictFeature(conflictFeature, originalSequence).adapt();
					ret.get(acc).add(variant);
				}
				// sequence conflicts
				Collection<Feature> mutagens = mainEntry.getFeatures(FeatureType.MUTAGEN);
				for (Feature feature : mutagens) {

					MutagenFeature mutagenFeature = (MutagenFeature) feature;
					Variant variant = new VariantAdapterFromMutagenFeature(mutagenFeature, originalSequence).adapt();
					ret.get(acc).add(variant);
				}
			}
		} catch (

		ServiceException e)

		{
			e.printStackTrace();
		} catch (

		URISyntaxException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (

		IOException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (

		InterruptedException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally

		{
			stopService();
		}
		return ret;

	}

	@Override
	public Map<String, List<Variant>> getVariants(String... uniprotACCs) {
		List<String> asList = Arrays.asList(uniprotACCs);
		return getVariants(new HashSet<String>(asList));
	}

}
