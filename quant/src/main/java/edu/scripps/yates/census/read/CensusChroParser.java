package edu.scripps.yates.census.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.analysis.util.KeyUtils;
import edu.scripps.yates.census.quant.xml.ProteinType;
import edu.scripps.yates.census.quant.xml.ProteinType.Peptide;
import edu.scripps.yates.census.quant.xml.RelexChro;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPSM;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedPeptide;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedProtein;
import edu.scripps.yates.census.read.model.QuantStaticMaps;
import edu.scripps.yates.census.read.model.QuantifiedProteinFromDBIndexEntry;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedProteinInterface;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.IndexedProtein;
import edu.scripps.yates.utilities.fasta.FastaParser;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public class CensusChroParser extends AbstractIsobaricQuantParser {
	private static final Logger log = Logger.getLogger(CensusChroParser.class);

	public CensusChroParser() {
		super();
	}

	public CensusChroParser(List<RemoteSSHFileReference> remoteSSHServers,
			List<Map<QuantCondition, QuantificationLabel>> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(remoteSSHServers, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusChroParser(Map<QuantCondition, QuantificationLabel> labelsByConditions,
			Collection<RemoteSSHFileReference> remoteSSHServers, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) {
		super(labelsByConditions, remoteSSHServers, labelNumerator, labelDenominator);
	}

	public CensusChroParser(RemoteSSHFileReference remoteSSHServer,
			Map<QuantCondition, QuantificationLabel> labelsByConditions, QuantificationLabel labelNumerator,
			QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(remoteSSHServer, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusChroParser(File xmlFile, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFile, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusChroParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusChroParser(File[] xmlFiles, Map<QuantCondition, QuantificationLabel>[] labelsByConditions,
			QuantificationLabel[] labelNumerator, QuantificationLabel[] labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusChroParser(Collection<File> xmlFiles, Map<QuantCondition, QuantificationLabel> labelsByConditions,
			QuantificationLabel labelNumerator, QuantificationLabel labelDenominator) throws FileNotFoundException {
		super(xmlFiles, labelsByConditions, labelNumerator, labelDenominator);
	}

	public CensusChroParser(RemoteSSHFileReference remoteServer, QuantificationLabel label1, QuantCondition cond1,
			QuantificationLabel label2, QuantCondition cond2) {
		super(remoteServer, label1, cond1, label2, cond2);
	}

	public CensusChroParser(File inputFile, QuantificationLabel label1, QuantCondition cond1,
			QuantificationLabel label2, QuantCondition cond2) throws FileNotFoundException {
		super(inputFile, label1, cond1, label2, cond2);
	}

	private RelexChro unmarshall(InputStream inputStream) {
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(RelexChro.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			RelexChro ret = (RelexChro) unmarshaller.unmarshal(inputStream);
			return ret;
		} catch (JAXBException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return null;
	}

	private RelexChro unmarshall(File file) {
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(RelexChro.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			RelexChro ret = (RelexChro) unmarshaller.unmarshal(file);
			return ret;
		} catch (JAXBException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param writeFiles
	 *            whether to write output files necessary to run SanXot program
	 */

	@Override
	public void process() {
		processed = false;
		log.info("reading census chro file...");

		try {

			boolean someValidFile = false;
			for (RemoteSSHFileReference remoteFileRetriever : remoteFileRetrievers) {
				final File remoteFile = remoteFileRetriever.getRemoteFile();
				if (remoteFile == null || !remoteFile.exists())
					continue;
				log.info("Unmarshalling " + remoteFile.getAbsolutePath());
				final RelexChro relex = unmarshall(remoteFile);
				if (relex == null) {
					log.warn("Error reading remote file " + remoteFileRetriever.getRemoteFileName() + " from "
							+ remoteFileRetriever.getHostName() + " at " + remoteFileRetriever.getRemotePath());
					continue;
				} else {
					someValidFile = true;
				}
				String experimentKey = FilenameUtils.getBaseName(remoteFileRetriever.getOutputFile().getAbsolutePath());
				String fileName = FilenameUtils.getName(remoteFileRetriever.getOutputFile().getAbsolutePath());
				log.info(experimentKey);
				log.info(relex.getVersion());
				if (!"Census v. 2.36 Chro file".equals(relex.getVersion())) {
					log.warn(
							"WARNING THE VERSION OF THE CENSUS FILE IS NOT THE EXPECTED. MAYBE SOMETHING WILL BE WRONG.");
				}

				final List<ProteinType> proteins = relex.getProtein();
				log.info(proteins.size() + " proteins readed in census chro xml file");
				log.info("Iterating proteins and getting their peptides for searching them on the Fasta database");
				int numDecoy = 0;
				// get all the Quantified PSMs first
				Set<QuantifiedPSMInterface> psms = new HashSet<QuantifiedPSMInterface>();
				int numTotalPeptides = 0;
				int totalProteins = proteins.size();
				int counter = 0;
				DecimalFormat df = new DecimalFormat("#.#");
				for (ProteinType protein : proteins) {

					counter++;
					if (counter % 100 == 0)
						log.info("Processing protein " + df.format(Double.valueOf(counter) * 100 / totalProteins) + " %"
								+ " and  " + numTotalPeptides + " PSMs analyzed");
					// apply the pattern if available
					if (decoyPattern != null) {

						final Matcher matcher = decoyPattern.matcher(protein.getLocus());

						if (matcher.find()) {
							numDecoy++;
							continue;
						}
					}

					// take the protein from map if available. It is possible
					// that the protein has been already created if we are
					// processing different census chro files in the same parser
					QuantifiedProteinInterface quantifiedProtein = null;
					final String proteinKey = KeyUtils.getProteinKey(protein);

					if (QuantStaticMaps.proteinMap.containsKey(proteinKey)) {
						quantifiedProtein = QuantStaticMaps.proteinMap.getItem(proteinKey);
					} else {
						quantifiedProtein = new IsobaricQuantifiedProtein(protein);
					}
					QuantStaticMaps.proteinMap.addItem(quantifiedProtein);
					quantifiedProtein.addFileName(fileName);
					final List<Peptide> peptideList = protein.getPeptide();
					if (peptideList != null) {
						for (Peptide peptide : peptideList) {
							numTotalPeptides++;
							if (peptide.getFrag() != null && peptide.getFrag().getBr() != null
									&& !"".equals(peptide.getFrag().getBr())) {
								IsobaricQuantifiedPSM quantifiedPSM = null;
								final String spectrumKey = KeyUtils.getSpectrumKey(peptide, chargeStateSensible);
								if (QuantStaticMaps.psmMap.containsKey(spectrumKey)) {
									quantifiedPSM = (IsobaricQuantifiedPSM) QuantStaticMaps.psmMap.getItem(spectrumKey);
								} else {
									quantifiedPSM = new IsobaricQuantifiedPSM(peptide,
											labelsByConditionsByFile.get(remoteFileRetriever), spectrumToIonsMap,
											peptideToSpectraMap, ionExclusions, chargeStateSensible);
								}
								final String spectrumKey2 = KeyUtils.getSpectrumKey(quantifiedPSM, chargeStateSensible);
								final String peptideKey = KeyUtils.getSequenceChargeKey(quantifiedPSM,
										chargeStateSensible);
								quantifiedPSM.addSpectrumToIonsMaps(spectrumKey2, spectrumToIonsMap, ionKeys);
								addToMap(peptideKey, peptideToSpectraMap, spectrumKey2);
								QuantStaticMaps.psmMap.addItem(quantifiedPSM);
								quantifiedPSM.addFileName(fileName);
								psms.add(quantifiedPSM);
								// add to map
								if (!localPsmMap.containsKey(spectrumKey)) {
									localPsmMap.put(spectrumKey, quantifiedPSM);
									if (localPsmMap.size() % 1000 == 0) {
										log.info(localPsmMap.size() + " psms processed...");
									}
								}

								// create the peptide
								IsobaricQuantifiedPeptide quantifiedPeptide = null;
								if (QuantStaticMaps.peptideMap.containsKey(peptideKey)) {
									quantifiedPeptide = (IsobaricQuantifiedPeptide) QuantStaticMaps.peptideMap
											.getItem(peptideKey);
								} else {
									quantifiedPeptide = new IsobaricQuantifiedPeptide(quantifiedPSM,
											distinguishModifiedPeptides);
								}
								QuantStaticMaps.peptideMap.addItem(quantifiedPeptide);
								quantifiedPeptide.addFileName(fileName);
								quantifiedPSM.setQuantifiedPeptide(quantifiedPeptide);
								// add peptide to map
								if (!localPeptideMap.containsKey(peptideKey)) {
									localPeptideMap.put(peptideKey, quantifiedPeptide);
									if (localPeptideMap.size() % 1000 == 0) {
										log.info(localPeptideMap.size() + " peptides processed...");
									}

								}

								if (dbIndex != null) {
									final String seq = quantifiedPSM.getFullSequence();

									String cleanSeq = FastaParser.cleanSequence(seq);

									final Set<IndexedProtein> indexedProteins = dbIndex.getProteins(cleanSeq);
									if (indexedProteins.isEmpty()) {
										peptidesMissingInDB.add(cleanSeq);
										if (!ignoreNotFoundPeptidesInDB) {
											throw new IllegalArgumentException("The peptide " + cleanSeq
													+ " is not found in Fasta DB.\nReview the default indexing parameters such as the number of allowed misscleavages.");
										}
										// log.warn("The peptide " + cleanSeq +
										// " is not found in Fasta DB.");
										// continue;
									}
									// create a new Quantified Protein for each
									// indexedProtein
									for (IndexedProtein indexedProtein : indexedProteins) {
										// apply the pattern if available
										if (decoyPattern != null) {
											final Matcher matcher = decoyPattern.matcher(indexedProtein.getAccession());
											if (matcher.find()) {
												continue;
											}
										}
										String proteinKey2 = KeyUtils.getProteinKey(indexedProtein);

										QuantifiedProteinInterface newQuantifiedProtein = null;
										if (QuantStaticMaps.proteinMap.containsKey(proteinKey2)) {
											newQuantifiedProtein = QuantStaticMaps.proteinMap.getItem(proteinKey2);

										} else {
											newQuantifiedProtein = new QuantifiedProteinFromDBIndexEntry(
													indexedProtein);
										}
										QuantStaticMaps.proteinMap.addItem(newQuantifiedProtein);
										registerProteins(newQuantifiedProtein, experimentKey, quantifiedPSM,
												quantifiedPeptide, remoteFileRetriever.getRemotePath());

									}
								}
								// register protein
								registerProteins(quantifiedProtein, experimentKey, quantifiedPSM, quantifiedPeptide,
										remoteFileRetriever.getRemotePath());

							}
						}
					}
				}
				log.info(psms.size() + " out of " + numTotalPeptides
						+ " PSMs in Census file containing non empty series");
				log.info(psms.size() + " PSMs from this parser. " + QuantStaticMaps.psmMap.size()
						+ " PSMs in the system");
				log.info(localProteinMap.size() + " Proteins created");
				log.info(localProteinMap.size() + " Proteins from this parser. " + QuantStaticMaps.proteinMap.size()
						+ " Proteins in the system");
				log.info(numDecoy + " decoy Proteins were discarded out of " + proteins.size() + " Proteins in "
						+ experimentKey);
				log.info(localPeptideMap.size() + " Peptides created");
				log.info(localPeptideMap.size() + " Peptides from this parser. " + QuantStaticMaps.peptideMap.size()
						+ " Peptides in the system");
			}
			if (!someValidFile)
				throw new IllegalArgumentException("some error occurred while reading the files");

			processed = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// if (processed) {
			// // to create the peptides at the end
			// peptideMap.clear();
			// peptideMap.putAll(
			// QuantifiedPeptide.getQuantifiedPeptides(getPSMMap().values(),
			// distinguishModifiedPeptides));
			// }
		}
	}

	private void registerProteins(QuantifiedProteinInterface protein, String experimentKey, QuantifiedPSMInterface psm,
			IsobaricQuantifiedPeptide peptide, String remoteFilePath) {
		// add protein to protein map
		taxonomies.addAll(protein.getTaxonomies());
		// add to protein-experiment map
		addToMap(experimentKey, experimentToProteinsMap, protein.getAccession());
		// add psm to the protein
		protein.addPSM(psm);
		// add peptide to the protein
		protein.addPeptide(peptide);
		// add protein to the psm
		psm.addQuantifiedProtein(protein);
		// add to the map (if it was already
		// there is not a problem, it will be
		// only once)
		addToMap(protein.getAccession(), proteinToPeptidesMap, psm.getKey());
		// add protein to protein map
		localProteinMap.put(protein.getAccession(), protein);
		// log size
		if (localProteinMap.size() % 1000 == 0) {
			log.info(localProteinMap.size() + " proteins processed..." + localPsmMap.size() + " psms processed..."
					+ localPeptideMap.size() + " peptides processed...");
		}

	}

}
