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
	private final String uniprotVersion;

	public UniprotProteoformRetrieverFromXML(UniprotProteinLocalRetriever uplr, String uniprotVersion) {
		this.uplr = uplr;
		this.uniprotVersion = uniprotVersion;
	}

	@Override
	public List<Proteoform> getProteoformsFromOneEntry(String uniprotACC) {
		final Map<String, List<Proteoform>> proteoforms = getProteoforms(uniprotACC);
		if (!proteoforms.isEmpty()) {
			return proteoforms.get(uniprotACC);
		}
		return Collections.emptyList();
	}

	@Override
	public Map<String, List<Proteoform>> getProteoforms(Collection<String> uniprotACCs) {

		final Map<String, List<Proteoform>> ret = new HashMap<String, List<Proteoform>>();
		final List<String> uniprotAccList = new ArrayList<String>();
		uniprotAccList.addAll(uniprotACCs);
		try {
			Map<String, Entry> annotatedProteins = new HashMap<String, Entry>();
			if (retrieveIsoforms) {
				final Set<String> isoformsACCs = new HashSet<String>();
				final List<String> mainIsoforms = new ArrayList<String>();
				for (final String acc : uniprotAccList) {
					final String isoformVersion = FastaParser.getIsoformVersion(acc);
					if (isoformVersion == null || "1".equals(isoformVersion)) {
						mainIsoforms.add(acc);
					} else {
						isoformsACCs.add(acc);
					}
				}
				if (!mainIsoforms.isEmpty()) {
					annotatedProteins = uplr.getAnnotatedProteins(uniprotVersion, mainIsoforms, true);
					for (final String acc : mainIsoforms) {
						if (annotatedProteins.containsKey(acc)) {
							final Entry mainEntry = annotatedProteins.get(acc);
							final List<CommentType> alternativeProducts = UniprotEntryUtil.getComments(mainEntry,
									uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType.ALTERNATIVE_PRODUCTS);
							for (final CommentType comment : alternativeProducts) {
								if (comment.getIsoform() != null) {
									final List<IsoformType> isoforms = comment.getIsoform();
									for (final IsoformType alternativeProductsIsoform : isoforms) {
										final String isoformACC = alternativeProductsIsoform.getId().get(0);
										if ("1".equals(FastaParser.getIsoformVersion(isoformACC))) {
											// no isoform
											continue;
										}
										isoformsACCs.add(isoformACC);
									}
								}
							}
						} else {
							// not found
						}
					}
				}
				if (!isoformsACCs.isEmpty()) {
					final Map<String, Entry> isoforms = retrieveIsoformsFirst(isoformsACCs);
					annotatedProteins.putAll(isoforms);
				}
			} else {
				annotatedProteins = uplr.getAnnotatedProteins(null, uniprotAccList, true);
			}
			// to not repeat proteoforms
			final Set<String> accSet = new HashSet<String>();

			for (String acc : uniprotAccList) {
				final Map<String, String> isoformSequenceReferences = new HashMap<String, String>();
				final String isoformVersion = FastaParser.getIsoformVersion(acc);
				if (isoformVersion != null && !"1".equals(isoformVersion)) {
					acc = FastaParser.getNoIsoformAccession(acc);
				}
				if (accSet.contains(acc)) {
					continue;
				}
				accSet.add(acc);
				if (annotatedProteins.containsKey(acc)) {
					final Entry entry = annotatedProteins.get(acc);

					final String originalSequence = UniprotEntryUtil.getProteinSequence(entry);
					// original variant
					if (!ret.containsKey(acc)) {
						final List<Proteoform> list = new ArrayList<Proteoform>();
						ret.put(acc, list);
					}
					final String description = UniprotEntryUtil.getProteinDescription(entry);
					final String taxonomy = UniprotEntryUtil.getTaxonomy(entry);
					final List<String> genes = UniprotEntryUtil.getGeneName(entry, true, true);
					String gene = null;
					if (genes != null && !genes.isEmpty()) {
						gene = genes.get(0);
					}
					final Proteoform originalvariant = new Proteoform(acc, originalSequence, acc, originalSequence,
							description, gene, taxonomy, ProteoformType.MAIN_ENTRY, true);
					ret.get(acc).add(originalvariant);

					// query for variants
					final List<FeatureType> features = UniprotEntryUtil.getFeatures(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.VARIANT);
					for (final FeatureType feature : features) {

						final Proteoform variant = new ProteoformAdapterFromNaturalVariant(acc, description, feature,
								originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// alternative products
					final List<CommentType> alternativeProductsComments = UniprotEntryUtil.getComments(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType.ALTERNATIVE_PRODUCTS);
					// store isoforms ACCs
					final Set<String> isoformsACCs = new HashSet<String>();
					for (final CommentType alternativeProductsComment : alternativeProductsComments) {
						final List<IsoformType> isoforms = alternativeProductsComment.getIsoform();
						for (final IsoformType alternativeProductsIsoform : isoforms) {
							final String isoformACC = alternativeProductsIsoform.getId().get(0);
							if ("1".equals(FastaParser.getIsoformVersion(isoformACC))) {
								// no isoform
								continue;
							}
							if (alternativeProductsIsoform.getSequence() != null
									&& alternativeProductsIsoform.getSequence().getRef() != null) {
								isoformSequenceReferences.put(isoformACC,
										alternativeProductsIsoform.getSequence().getRef());
							}
							isoformsACCs.add(isoformACC);

						}
					}
					// retrieve isoform sequences
					if (retrieveIsoforms) {
						for (final String isoformACC : isoformsACCs) {
							if (annotatedProteins.containsKey(isoformACC)) {
								final Entry isoformEntry = annotatedProteins.get(isoformACC);
								final String isoformSequence = UniprotEntryUtil.getProteinSequence(isoformEntry);
								final String description2 = UniprotEntryUtil.getProteinDescription(isoformEntry);
								final String taxonomy2 = UniprotEntryUtil.getTaxonomy(isoformEntry);
								final List<String> genes2 = UniprotEntryUtil.getGeneName(isoformEntry, true, true);
								String gene2 = null;
								if (genes2 != null && !genes2.isEmpty()) {
									gene2 = genes2.get(0);
								}

								final Proteoform variant = new Proteoform(acc, originalSequence, isoformACC,
										isoformSequence, description2, gene2, taxonomy2, ProteoformType.ISOFORM);
								ret.get(acc).add(variant);
							}
						}
					}

					// sequence conflicts
					final Collection<FeatureType> conflicts = UniprotEntryUtil.getFeatures(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CONFLICT);
					for (final FeatureType feature : conflicts) {
						final Proteoform variant = new ProteoformAdapterFromConflictFeature(acc, description, feature,
								originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// mutagens
					final Collection<FeatureType> mutagens = UniprotEntryUtil.getFeatures(entry,
							uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.MUTAGEN);
					for (final FeatureType feature : mutagens) {
						final Proteoform variant = new ProteoformAdapterFromMutagenFeature(acc, description, feature,
								originalSequence, gene, taxonomy).adapt();
						ret.get(acc).add(variant);
					}
					// ptms
					if (retrievePTMs) {
						final Collection<FeatureType> modifiedResiduesFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.MOD_RES);
						for (final FeatureType feature : modifiedResiduesFeatures) {
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<FeatureType> siteFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.SITE);
						for (final FeatureType feature : siteFeatures) {
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<FeatureType> carbohydFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CARBOHYD);
						for (final FeatureType feature : carbohydFeatures) {
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromCarbohydFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<FeatureType> crosslinkFeatures = UniprotEntryUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CROSSLNK);
						for (final FeatureType feature : crosslinkFeatures) {
							final List<UniprotPTM> uniprotPTMs = new UniprotPTMAdapterFromCrosslinkFeature(feature)
									.adapt();
							if (uniprotPTMs != null) {
								for (final UniprotPTM uniprotPTM : uniprotPTMs) {
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

	private Map<String, Entry> retrieveIsoformsFirst(Set<String> isoformsACCs) {
		final long t1 = System.currentTimeMillis();
		log.debug("Retrieving isoform fasta sequences all at once first");
		// if there is no UPLR, get isoform fasta from internet,
		if (uplr == null) {
			final Map<String, Entry> isoformEntries = UniprotProteinRemoteRetriever
					.getFASTASequencesInParallel(isoformsACCs);
			log.info(isoformEntries.size() + " isoform fasta sequences entries retrieved.");
			log.debug("It took " + DatesUtil.getDescriptiveTimeFromMillisecs((System.currentTimeMillis() - t1)));
			return isoformEntries;
		} else {
			final Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
			log.info(entries.size() + " isoform fasta sequences entries retrieved.");
			log.debug("It took " + DatesUtil.getDescriptiveTimeFromMillisecs((System.currentTimeMillis() - t1)));
			return entries;
		}

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

}
