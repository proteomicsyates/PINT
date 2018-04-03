package edu.scripps.yates.annotations.uniprot.proteoform.xml;

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
import edu.scripps.yates.annotations.uniprot.proteoform.Proteoform;
import edu.scripps.yates.annotations.uniprot.proteoform.ProteoformType;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotPTM;
import edu.scripps.yates.annotations.uniprot.proteoform.UniprotProteoformRetriever;
import edu.scripps.yates.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.annotations.uniprot.xml.Entry;
import edu.scripps.yates.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.annotations.uniprot.xml.IsoformType;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;

/**
 * It uses the Uniprot local XML files to get the variants of proteins
 * 
 * @author Salva
 *
 */
public class UniprotProteoformRetrieverFromXML implements UniprotProteoformRetriever {
	private static final Logger log = Logger.getLogger(UniprotProteoformRetrieverFromXML.class);
	private final UniprotProteinLocalRetriever uplr;
	private boolean retrievePTMs = true;
	private boolean retrieveIsoforms = true;

	public UniprotProteoformRetrieverFromXML(UniprotProteinLocalRetriever uplr) {
		this.uplr = uplr;
	}

	@Override
	public List<Proteoform> getProteoformsFromOneEntry(String uniprotACC) {
		Map<String, List<Proteoform>> proteoforms = getProteoforms(uniprotACC);
		if (!proteoforms.isEmpty()) {
			return proteoforms.get(uniprotACC);
		}
		return Collections.emptyList();
	}

	@Override
	public Map<String, List<Proteoform>> getProteoforms(Set<String> uniprotACCs) {

		Map<String, List<Proteoform>> ret = new HashMap<String, List<Proteoform>>();
		List<String> uniprotAccList = new ArrayList<String>();
		uniprotAccList.addAll(uniprotACCs);
		try {

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
					Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(null, toQuery, true);
					for (String acc : toQuery) {
						if (annotatedProteins.containsKey(acc)) {
							Entry entry = annotatedProteins.get(acc);
							List<CommentType> alternativeProducts = UniprotEntryUtil.getComments(entry,
									uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType.ALTERNATIVE_PRODUCTS);
							for (CommentType comment : alternativeProducts) {
								if (comment.getIsoform() != null) {
									List<IsoformType> isoforms = comment.getIsoform();
									for (IsoformType alternativeProductsIsoform : isoforms) {
										String isoformACC = alternativeProductsIsoform.getId().get(0);
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
				}
				retrieveIsoformsFirst(isoformsACCs);
			}
			Map<String, Entry> annotatedProteins = uplr.getAnnotatedProteins(null, uniprotAccList, true);
			for (String acc : uniprotAccList) {
				if (annotatedProteins.containsKey(acc)) {
					Entry entry = annotatedProteins.get(acc);

					String originalSequence = UniprotEntryUtil.getProteinSequence(entry);
					// original variant
					if (!ret.containsKey(acc)) {
						List<Proteoform> list = new ArrayList<Proteoform>();
						ret.put(acc, list);
					}
					String description = UniprotEntryUtil.getProteinDescription(entry);
					String taxonomy = UniprotEntryUtil.getTaxonomy(entry);
					List<String> genes = UniprotEntryUtil.getGeneName(entry, true, true);
					String gene = null;
					if (genes != null && !genes.isEmpty()) {
						gene = genes.get(0);
					}
					Proteoform originalvariant = new Proteoform(acc, acc, originalSequence, description, gene, taxonomy,
							null, true);
					ret.get(acc).add(originalvariant);

					// query for variants
					List<FeatureType> features = UniprotEntryUtil.getFeatures(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.VARIANT);
					for (FeatureType feature : features) {

						Proteoform variant = new ProteoformAdapterFromNaturalVariant(acc, description, feature,
								originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// alternative products
					List<CommentType> alternativeProductsComments = UniprotEntryUtil.getComments(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType.ALTERNATIVE_PRODUCTS);
					// store isoforms ACCs
					Set<String> isoformsACCs = new HashSet<String>();
					for (CommentType alternativeProductsComment : alternativeProductsComments) {
						List<IsoformType> isoforms = alternativeProductsComment.getIsoform();
						for (IsoformType alternativeProductsIsoform : isoforms) {
							String isoformACC = alternativeProductsIsoform.getId().get(0);
							if ("1".equals(FastaParser.getIsoformVersion(isoformACC))) {
								// no isoform
								continue;
							}
							isoformsACCs.add(isoformACC);

						}
					}
					// retrieve isoform sequences
					if (retrieveIsoforms) {

						Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
						for (String isoformACC : isoformsACCs) {
							if (entries.containsKey(isoformACC)) {
								Entry isoformEntry = entries.get(isoformACC);
								String isoformSequence = UniprotEntryUtil.getProteinSequence(isoformEntry);
								String description2 = UniprotEntryUtil.getProteinDescription(isoformEntry);
								String taxonomy2 = UniprotEntryUtil.getTaxonomy(isoformEntry);
								List<String> genes2 = UniprotEntryUtil.getGeneName(isoformEntry, true, true);
								String gene2 = null;
								if (genes2 != null && !genes2.isEmpty()) {
									gene2 = genes2.get(0);
								}
								Proteoform variant = new Proteoform(acc, isoformACC, isoformSequence, description2,
										gene2, taxonomy2, ProteoformType.ISOFORM);
								ret.get(acc).add(variant);
							}
						}
					}

					// sequence conflicts
					Collection<FeatureType> conflicts = UniprotEntryUtil.getFeatures(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CONFLICT);
					for (FeatureType feature : conflicts) {
						Proteoform variant = new ProteoformAdapterFromConflictFeature(acc, description, feature,
								originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// mutagens
					Collection<FeatureType> mutagens = UniprotEntryUtil.getFeatures(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.MUTAGEN);
					for (FeatureType feature : mutagens) {
						Proteoform variant = new ProteoformAdapterFromMutagenFeature(acc, description, feature,
								originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// ptms
					if (retrievePTMs) {
						Collection<FeatureType> modifiedResiduesFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.MOD_RES);
						for (FeatureType feature : modifiedResiduesFeatures) {
							UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						Collection<FeatureType> siteFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.SITE);
						for (FeatureType feature : siteFeatures) {
							UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						Collection<FeatureType> carbohydFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CARBOHYD);
						for (FeatureType feature : carbohydFeatures) {
							UniprotPTM uniprotPTM = new UniprotPTMAdapterFromCarbohydFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						Collection<FeatureType> crosslinkFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CROSSLNK);
						for (FeatureType feature : crosslinkFeatures) {
							List<UniprotPTM> uniprotPTMs = new UniprotPTMAdapterFromCrosslinkFeature(feature).adapt();
							if (uniprotPTMs != null) {
								for (UniprotPTM uniprotPTM : uniprotPTMs) {
									originalvariant.addPTM(uniprotPTM);
								}

							}
						}
					}
				}
			}
		} finally {

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
	public Map<String, List<Proteoform>> getProteoforms(String... uniprotACCs) {
		List<String> asList = Arrays.asList(uniprotACCs);
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

}
