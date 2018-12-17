package edu.scripps.yates.annotations.uniprot.proteoform.xml;

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
import edu.scripps.yates.annotations.util.UniprotEntryEBIUtil;
import edu.scripps.yates.utilities.annotations.uniprot.UniprotEntryUtil;
import edu.scripps.yates.utilities.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.IsoformType;
import edu.scripps.yates.utilities.dates.DatesUtil;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.util.Pair;

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
	private boolean retrieveProteoforms = true;
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
		return getProteoformsFromList(uniprotACCs, retrieveProteoforms, retrieveIsoforms,
				retrieveProteoforms & retrievePTMs, uniprotVersion, uplr);
	}

	/**
	 * 
	 * @param uniprotACCs
	 * @param retrieveProteoforms
	 *            if false, only main entry will be retrieved. if true, variants
	 *            and alternative products will be retrieved
	 * @param retrieveIsoforms
	 *            if true, the isoforms will be rertieved
	 * @param retrievePTMs
	 *            if true, the PTM annotations will be retrieved
	 * @param uniprotVersion
	 * @param uplr
	 * @return
	 */
	protected static Map<String, List<Proteoform>> getProteoformsFromList(Collection<String> uniprotACCs,
			boolean retrieveProteoforms, boolean retrieveIsoforms, boolean retrievePTMs, String uniprotVersion,
			UniprotProteinLocalRetriever uplr) {
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
					if (isoformVersion == null) {
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
							final List<CommentType> alternativeProducts = UniprotEntryEBIUtil.getComments(mainEntry,
									uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType.ALTERNATIVE_PRODUCTS);
							for (final CommentType comment : alternativeProducts) {
								if (comment.getIsoform() != null) {
									final List<IsoformType> isoforms = comment.getIsoform();
									for (final IsoformType alternativeProductsIsoform : isoforms) {
										final String isoformACC = alternativeProductsIsoform.getId().get(0);
										if (FastaParser.getIsoformVersion(isoformACC) == null) {
											// no isoform
											continue;
										}
										if ("displayed".equals(alternativeProductsIsoform.getSequence().getType())) {
											// this is the main form
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
					final Map<String, Entry> isoforms = retrieveIsoformsFirst(isoformsACCs, uplr);
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
				if (isoformVersion != null) {
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
					final String name = UniprotEntryUtil.getNames(entry).get(0);
					final String taxonomy = UniprotEntryUtil.getTaxonomyName(entry);
					final List<Pair<String, String>> genes = UniprotEntryUtil.getGeneName(entry, true, true);
					String gene = null;
					if (genes != null && !genes.isEmpty()) {
						gene = genes.get(0).getFirstelement();
					}
					final Proteoform originalvariant = new Proteoform(acc, originalSequence, acc, originalSequence,
							name, description, gene, taxonomy, ProteoformType.MAIN_ENTRY, true);
					ret.get(acc).add(originalvariant);

					// query for variants
					if (retrieveProteoforms) {
						final List<FeatureType> features = UniprotEntryEBIUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.VARIANT);
						for (final FeatureType feature : features) {

							final Proteoform variant = new ProteoformAdapterFromNaturalVariant(acc, name, description,
									feature, originalSequence, gene, taxonomy).adapt();
							ret.get(acc).add(variant);
						}
					}
					if (retrieveIsoforms) {
						// alternative products
						final List<CommentType> alternativeProductsComments = UniprotEntryEBIUtil.getComments(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType.ALTERNATIVE_PRODUCTS);
						// store isoforms ACCs
						final Set<String> isoformsACCs = new HashSet<String>();
						for (final CommentType alternativeProductsComment : alternativeProductsComments) {
							final List<IsoformType> isoforms = alternativeProductsComment.getIsoform();
							for (final IsoformType alternativeProductsIsoform : isoforms) {
								final String isoformACC = alternativeProductsIsoform.getId().get(0);
								if (alternativeProductsIsoform.getSequence() != null
										&& alternativeProductsIsoform.getSequence().getRef() != null) {
									isoformSequenceReferences.put(isoformACC,
											alternativeProductsIsoform.getSequence().getRef());
								}
								if (alternativeProductsIsoform.getSequence() != null
										&& !"displayed".equals(alternativeProductsIsoform.getSequence().getType())) {
									isoformsACCs.add(isoformACC);
								}
							}
						}
						// retrieve isoform sequences

						for (final String isoformACC : isoformsACCs) {
							if (annotatedProteins.containsKey(isoformACC)) {
								final Entry isoformEntry = annotatedProteins.get(isoformACC);
								final String isoformSequence = UniprotEntryUtil.getProteinSequence(isoformEntry);
								final String name2 = UniprotEntryUtil.getNames(isoformEntry).get(0);
								final String description2 = UniprotEntryUtil.getProteinDescription(isoformEntry);
								final String taxonomy2 = UniprotEntryUtil.getTaxonomyName(isoformEntry);
								final List<Pair<String, String>> genes2 = UniprotEntryUtil.getGeneName(isoformEntry,
										true, true);
								String gene2 = null;
								if (genes2 != null && !genes2.isEmpty()) {
									gene2 = genes2.get(0).getFirstelement();
								}

								final Proteoform variant = new Proteoform(acc, originalSequence, isoformACC,
										isoformSequence, name2, description2, gene2, taxonomy2, ProteoformType.ISOFORM);
								ret.get(acc).add(variant);
							}
						}
					}
					if (retrieveProteoforms) {
						// sequence conflicts
						final Collection<FeatureType> conflicts = UniprotEntryEBIUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CONFLICT);
						for (final FeatureType feature : conflicts) {
							final Proteoform variant = new ProteoformAdapterFromConflictFeature(acc, name, description,
									feature, originalSequence, gene, taxonomy).adapt();
							ret.get(acc).add(variant);
						}
						// mutagens
						final Collection<FeatureType> mutagens = UniprotEntryEBIUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.MUTAGEN);
						for (final FeatureType feature : mutagens) {
							final Proteoform variant = new ProteoformAdapterFromMutagenFeature(acc, name, description,
									feature, originalSequence, gene, taxonomy).adapt();
							ret.get(acc).add(variant);
						}
					}
					// ptms
					if (retrievePTMs) {
						final Collection<FeatureType> modifiedResiduesFeatures = UniprotEntryEBIUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.MOD_RES);
						for (final FeatureType feature : modifiedResiduesFeatures) {
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<FeatureType> siteFeatures = UniprotEntryEBIUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.SITE);
						for (final FeatureType feature : siteFeatures) {
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<FeatureType> carbohydFeatures = UniprotEntryEBIUtil.getFeatures(entry,
								uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType.CARBOHYD);
						for (final FeatureType feature : carbohydFeatures) {
							final UniprotPTM uniprotPTM = new UniprotPTMAdapterFromCarbohydFeature(feature).adapt();
							if (uniprotPTM != null) {
								originalvariant.addPTM(uniprotPTM);
							}
						}
						final Collection<FeatureType> crosslinkFeatures = UniprotEntryEBIUtil.getFeatures(entry,
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

	private static Map<String, Entry> retrieveIsoformsFirst(Set<String> isoformsACCs,
			UniprotProteinLocalRetriever uplr) {
		final long t1 = System.currentTimeMillis();
		log.debug("Retrieving isoform fasta sequences all at once first ");
		// if there is no UPLR, get isoform fasta from internet,
		if (uplr == null) {
			final Map<String, Entry> isoformEntries = UniprotProteinRemoteRetriever
					.getFASTASequencesInParallel(isoformsACCs);
			log.info(isoformEntries.size() + " isoform fasta sequences entries retrieved.");
			log.debug("It took " + DatesUtil.getDescriptiveTimeFromMillisecs((System.currentTimeMillis() - t1)));
			return isoformEntries;
		} else {
			final Map<String, Entry> entries = uplr.getAnnotatedProteins(null, isoformsACCs);
			log.debug(entries.size() + " isoform fasta sequences entries retrieved.");
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

	@Override
	public Iterator<Proteoform> getProteoformIterator(Collection<String> uniprotACCs) {
		return new ProteoformRetrieverIteratorFromXML(uniprotACCs, retrieveProteoforms, retrieveIsoforms, retrievePTMs,
				uniprotVersion, uplr);
	}

	@Override
	public Iterator<Proteoform> getProteoformIterator(String... uniprotACCs) {
		final Set<String> set = new HashSet<String>();
		for (final String uniprotACC : uniprotACCs) {
			set.add(uniprotACC);
		}
		return getProteoformIterator(set);
	}

	public boolean isRetrieveProteoforms() {
		return retrieveProteoforms;
	}

	/**
	 * whether to search for variants and alternative products or not. If this
	 * is false, PTMs will not be retrieved
	 * 
	 * @param retrieveProteoforms
	 */
	public void setRetrieveProteoforms(boolean retrieveProteoforms) {
		this.retrieveProteoforms = retrieveProteoforms;
	}

}
