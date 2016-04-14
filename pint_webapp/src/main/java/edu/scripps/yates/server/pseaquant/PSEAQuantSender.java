package edu.scripps.yates.server.pseaquant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import edu.scripps.yates.proteindb.persistence.mysql.Gene;
import edu.scripps.yates.proteindb.persistence.mysql.Protein;
import edu.scripps.yates.proteindb.persistence.mysql.ProteinRatioValue;
import edu.scripps.yates.proteindb.persistence.mysql.Psm;
import edu.scripps.yates.proteindb.persistence.mysql.access.PreparedQueries;
import edu.scripps.yates.server.util.FileManager;
import edu.scripps.yates.shared.model.RatioDescriptorBean;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantAnnotationDatabase;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantCVTol;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantLiteratureBias;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantQuantType;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantReplicate;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantResult;
import edu.scripps.yates.shared.thirdparty.pseaquant.PSEAQuantSupportedOrganism;
import edu.scripps.yates.utilities.maths.Maths;

public class PSEAQuantSender {
	private final static Logger log = Logger.getLogger(PSEAQuantSender.class);
	private final String email;
	private final PSEAQuantSupportedOrganism organism;
	private final List<PSEAQuantReplicate> replicates;
	private final RatioDescriptorBean ratioDescriptor;
	private final long numberOfSamplings;
	private final PSEAQuantAnnotationDatabase annotationDatabase;
	private final PSEAQuantCVTol cvTol;
	private final PSEAQuantQuantType quantType;
	private final Double cvTolFactor;
	private final PSEAQuantLiteratureBias literatureBias;

	public enum RATIO_AVERAGING {
		AVERAGE, MEDIAN
	};

	private final RATIO_AVERAGING ratioAveraging;

	public PSEAQuantSender(String email, PSEAQuantSupportedOrganism organism, List<PSEAQuantReplicate> replicates,
			RatioDescriptorBean ratioDescriptor, long numberOfSamplings, PSEAQuantQuantType quantType,
			PSEAQuantAnnotationDatabase annotationDatabase, PSEAQuantCVTol cvTol, Double cvTolFactor,
			PSEAQuantLiteratureBias literatureBias, RATIO_AVERAGING ratioAveraging) {

		this.email = email;
		this.organism = organism;
		this.replicates = replicates;
		this.ratioDescriptor = ratioDescriptor;
		this.numberOfSamplings = numberOfSamplings;
		this.annotationDatabase = annotationDatabase;
		this.cvTol = cvTol;
		this.cvTolFactor = cvTolFactor;
		this.literatureBias = literatureBias;
		this.ratioAveraging = ratioAveraging;
		this.quantType = quantType;
	}

	public PSEAQuantResult send() {
		PSEAQuantResult ret = new PSEAQuantResult();
		BufferedReader rd = null;
		try {
			log.info("Sending request to PSEAQuant");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addTextBody(quantType.getParameterName(), quantType.getQuanttype());
			builder.addTextBody("email", email, ContentType.TEXT_PLAIN);
			builder.addTextBody(organism.getParameterName(), organism.getOrganismName(), ContentType.TEXT_PLAIN);
			builder.addTextBody("Sampling", String.valueOf(numberOfSamplings), ContentType.TEXT_PLAIN);
			builder.addTextBody(annotationDatabase.getParameterName(), annotationDatabase.getAnnotationDBName(),
					ContentType.TEXT_PLAIN);
			builder.addTextBody(cvTol.getParameterName(), cvTol.getCvTol(), ContentType.TEXT_PLAIN);
			if (cvTolFactor != null) {
				builder.addTextBody("CVTolFactor", String.valueOf(cvTolFactor), ContentType.TEXT_PLAIN);
			}
			builder.addTextBody(literatureBias.getParameterName(), literatureBias.getLiteratureBias(),
					ContentType.TEXT_PLAIN);
			builder.addTextBody("origin", "SUPER-COOL-PINT", ContentType.TEXT_PLAIN);
			builder.addTextBody("submit", "submit", ContentType.TEXT_PLAIN);

			File inputDataFile = createInputFile();
			// builder.addBinaryBody("file", ratioFile);
			ret.setLinkToRatios(FilenameUtils.getName(inputDataFile.getAbsolutePath()));

			FileBody fileBody = new FileBody(inputDataFile);
			builder.addPart("file", fileBody);
			// ... add more parameters

			URI url = getPSEAQuantURL();
			HttpPost httppost = new HttpPost(url);
			final HttpEntity build = builder.build();

			httppost.setEntity(build);

			log.info("Post request is built. Sending...");
			HttpResponse response = null;
			response = HttpClientBuilder.create().build().execute(httppost);
			log.info("Response received. Status code: " + response.getStatusLine().getStatusCode());

			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				if (line.contains("http://sealion.scripps.edu:18080/PSEA-Quant/output/")) {
					String outputURL = line.split("=")[1].split(">")[0];
					ret.setLinkToResults(outputURL);
				}
				result.append(line);
				log.info(line);

			}

		} catch (Exception e) {
			e.printStackTrace();
			ret.setErrorMessage(e.getMessage());
		} finally {
			if (rd != null) {
				try {
					rd.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	private File createInputFile() {
		Random random = new Random();
		log.info("Creating  ratio file");
		Map<String, Map<Integer, List<Double>>> valuesPerGeneAndReplicate = null;
		if (quantType == PSEAQuantQuantType.BASED) {
			valuesPerGeneAndReplicate = getRatiosFromReplicates();
		} else {
			valuesPerGeneAndReplicate = getSPCsFromReplicates();
		}
		final File pseaQuantFolder = FileManager.getPSEAQuantFolder();
		final File newRatioFile = new File(
				pseaQuantFolder.getAbsolutePath() + File.separator + "PSEA-Quant_input_" + random.nextInt() + ".txt");
		writeRatioFile(valuesPerGeneAndReplicate, newRatioFile);
		log.info("Ratio file created");
		return newRatioFile;
	}

	private void writeRatioFile(Map<String, Map<Integer, List<Double>>> ratiosPerGeneAndReplicate, File inputFile) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(inputFile));
			for (String geneSymbol : ratiosPerGeneAndReplicate.keySet()) {
				bw.write(geneSymbol + "\t");
				final Map<Integer, List<Double>> valuesPerReplicate = ratiosPerGeneAndReplicate.get(geneSymbol);
				for (int numReplicate = 1; numReplicate <= replicates.size(); numReplicate++) {
					List<Double> valueList = valuesPerReplicate.get(numReplicate);
					if (valueList == null || valueList.isEmpty()) {
						bw.write(0 + "\t");
					} else {
						if (quantType == PSEAQuantQuantType.BASED) {
							switch (ratioAveraging) {
							case AVERAGE:
								final double median = new Median().evaluate(getArray(valueList));
								bw.write(String.valueOf(median) + "\t");
								break;
							case MEDIAN:
								final double mean = Maths.mean(valueList.toArray(new Double[0]));
								bw.write(String.valueOf(mean) + "\t");
								break;
							default:
								break;
							}
						} else {
							// in case of spectral counts, just sum the values
							double total = 0.0;
							for (Double double1 : valueList) {
								total += double1;
							}
							bw.write(String.valueOf(total) + "\t");
						}
					}
				}
				bw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private double[] getArray(List<Double> ratioList) {
		double[] ret = new double[ratioList.size()];
		int i = 0;
		for (Double d : ratioList) {
			ret[i++] = d;
		}
		return ret;
	}

	/**
	 * Returns a map with key being the gene symbol, and the values: <br>
	 * A List of proteinRatioValues per replicate
	 *
	 * @return
	 */
	private Map<String, Map<Integer, List<Double>>> getRatiosFromReplicates() {
		Map<String, Map<Integer, List<Double>>> ret = new HashMap<String, Map<Integer, List<Double>>>();
		int numReplicate = 0;
		for (PSEAQuantReplicate pseaQuantReplicate : replicates) {
			numReplicate++;
			List<Protein> proteinsInReplicate = new ArrayList<Protein>();
			final List<Map<String, String>> listOfPairs = pseaQuantReplicate.getListOfPairs();

			for (Map<String, String> pair : listOfPairs) {
				for (String projectName : pair.keySet()) {
					if (pseaQuantReplicate.isCondition()) {
						final String conditionName = pair.get(projectName);
						log.info("Getting proteins in condition: " + conditionName);
						final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
								.getProteinsByProjectCondition(projectName, conditionName);
						log.info(proteinsByProjectCondition.size() + " proteins in condition: " + conditionName);
						addToProteinList(proteinsInReplicate, proteinsByProjectCondition);
					} else if (pseaQuantReplicate.isMsRun()) {
						final String msrunID = pair.get(projectName);
						log.info("Getting proteins in MSRun: " + msrunID);
						final List<Protein> proteinsWithMSRun = PreparedQueries.getProteinsWithMSRun(projectName,
								msrunID);
						log.info(proteinsWithMSRun.size() + " proteins in MSRun: " + msrunID);
						proteinsInReplicate.addAll(proteinsWithMSRun);
					}
				}
			}
			// iterate over the proteinsInReplicate:
			for (Protein protein : proteinsInReplicate) {
				// use just the proteins with the taxonomy
				// if (!protein.getOrganism().getTaxonomyId()
				// .equals(String.valueOf(organism.getNcbiID()))) {
				// log.info("Skipping protein "
				// + PersistenceUtils.getPrimaryAccession(protein)
				// + " having a different taxonomy than "
				// + organism.getOrganismName() + "("
				// + organism.getNcbiID() + ")");
				// continue;
				// }

				// TODO FILTER BY ORGANISM

				String geneName = getGeneName(protein);
				if (geneName != null) {
					Map<Integer, List<Double>> ratiosPerReplicates = null;
					if (ret.containsKey(geneName)) {
						ratiosPerReplicates = ret.get(geneName);
					} else {
						ratiosPerReplicates = new HashMap<Integer, List<Double>>();
						ret.put(geneName, ratiosPerReplicates);
					}
					List<Double> values = getProteinRatios(protein);

					if (!values.isEmpty()) {
						if (ratiosPerReplicates.containsKey(numReplicate)) {
							ratiosPerReplicates.get(numReplicate).addAll(values);
						} else {
							List<Double> list = new ArrayList<Double>();
							list.addAll(values);
							ratiosPerReplicates.put(numReplicate, list);
						}
					}
				}
			}
		}
		return ret;
	}

	private Map<String, Map<Integer, List<Double>>> getSPCsFromReplicates() {
		Map<String, Map<Integer, List<Double>>> ret = new HashMap<String, Map<Integer, List<Double>>>();
		Map<String, Map<Integer, Set<String>>> retTMP = new HashMap<String, Map<Integer, Set<String>>>();
		int numReplicate = 0;
		for (PSEAQuantReplicate pseaQuantReplicate : replicates) {
			numReplicate++;
			List<Protein> proteinsInReplicate = new ArrayList<Protein>();
			final List<Map<String, String>> listOfPairs = pseaQuantReplicate.getListOfPairs();

			for (Map<String, String> pair : listOfPairs) {
				for (String projectName : pair.keySet()) {
					if (pseaQuantReplicate.isCondition()) {
						final String conditionName = pair.get(projectName);
						log.info("Getting proteins in condition: " + conditionName);
						final Map<String, Set<Protein>> proteinsByProjectCondition = PreparedQueries
								.getProteinsByProjectCondition(projectName, conditionName);
						log.info(proteinsByProjectCondition.size() + " proteins in condition: " + conditionName);
						addToProteinList(proteinsInReplicate, proteinsByProjectCondition);
					} else if (pseaQuantReplicate.isMsRun()) {
						final String msrunID = pair.get(projectName);
						log.info("Getting proteins in MSRun: " + msrunID);
						final List<Protein> proteinsWithMSRun = PreparedQueries.getProteinsWithMSRun(projectName,
								msrunID);
						log.info(proteinsWithMSRun.size() + " proteins in MSRun: " + msrunID);
						proteinsInReplicate.addAll(proteinsWithMSRun);
					}
				}
			}
			// iterate over the proteinsInReplicate:
			for (Protein protein : proteinsInReplicate) {
				// TODO
				// use just the proteins with the taxonomy
				// if (!protein.getOrganism().getTaxonomyId()
				// .equals(String.valueOf(organism.getNcbiID()))) {
				// log.info("Skipping protein "
				// + PersistenceUtils.getPrimaryAccession(protein)
				// + " having a different taxonomy than "
				// + organism.getOrganismName() + "("
				// + organism.getNcbiID() + ")");
				// continue;
				// }
				String geneName = getGeneName(protein);
				if (geneName != null) {
					Map<Integer, Set<String>> ratiosPerReplicates = null;
					if (ret.containsKey(geneName)) {
						ratiosPerReplicates = retTMP.get(geneName);
					} else {
						ratiosPerReplicates = new HashMap<Integer, Set<String>>();
						retTMP.put(geneName, ratiosPerReplicates);
					}
					Set<String> psmIds = getProteinPSMIds(protein);

					if (!psmIds.isEmpty()) {
						if (ratiosPerReplicates.containsKey(numReplicate)) {
							ratiosPerReplicates.get(numReplicate).addAll(psmIds);
						} else {
							Set<String> set = new HashSet<String>();
							set.addAll(psmIds);
							ratiosPerReplicates.put(numReplicate, set);
						}
					}
				}
			}
		}

		// convert retTM to ret
		for (String key : retTMP.keySet()) {
			Map<Integer, List<Double>> retMap = new HashMap<Integer, List<Double>>();
			ret.put(key, retMap);
			final Map<Integer, Set<String>> map = retTMP.get(key);
			for (Integer numRep : map.keySet()) {
				final Set<String> set = map.get(numRep);
				List<Double> list = new ArrayList<Double>();
				list.add(Double.valueOf(set.size()));
				retMap.put(numRep, list);
			}

		}
		return ret;
	}

	private Set<String> getProteinPSMIds(Protein protein) {
		Set<String> ret = new HashSet<String>();

		final Set<Psm> psms = protein.getPsms();
		for (Psm psm : psms) {
			ret.add(psm.getPsmId());
		}

		return ret;
	}

	private List<Double> getProteinRatios(Protein protein) {
		List<Double> ret = new ArrayList<Double>();
		if (protein != null) {
			final Set<ProteinRatioValue> proteinRatioValues = protein.getProteinRatioValues();
			for (ProteinRatioValue proteinRatioValue : proteinRatioValues) {
				if (proteinRatioValue.getRatioDescriptor().getDescription().equals(ratioDescriptor.getRatioName())) {
					final double value = proteinRatioValue.getValue();
					if (proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition1Id().getName()
							.equals(ratioDescriptor.getCondition1Name())) {
						if (proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition2Id().getName()
								.equals(ratioDescriptor.getCondition2Name())) {
							ret.add(transformToNonLogValue(value));
						}
					} else if (proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition1Id().getName()
							.equals(ratioDescriptor.getCondition2Name())) {
						if (proteinRatioValue.getRatioDescriptor().getConditionByExperimentalCondition2Id().getName()
								.equals(ratioDescriptor.getCondition1Name())) {
							// get the inverse
							// as it is log2, the inverse is changing the sign
							ret.add(transformToNonLogValue(-value));
						}
					}
				}
			}
		}
		return ret;
	}

	private double transformToNonLogValue(double value) {
		final double pow = Math.pow(2, value);
		return pow;
	}

	/**
	 * Get gene symbol from the protein, getting the one that is annotated as
	 * primary (if available)
	 *
	 * @param protein
	 * @return
	 */
	private String getGeneName(Protein protein) {
		if (protein != null) {
			final Set<Gene> genes = protein.getGenes();
			if (genes != null) {
				for (Gene gene : genes) {
					if ("primary".equalsIgnoreCase(gene.getGeneType())) {
						return gene.getGeneId();
					}
				}
				if (!genes.isEmpty()) {
					return genes.iterator().next().getGeneId();
				}
			}
		}
		return null;
	}

	private void addToProteinList(List<Protein> proteinsList, Map<String, Set<Protein>> proteinMap) {
		for (Set<Protein> proteinSet : proteinMap.values()) {
			proteinsList.addAll(proteinSet);
		}
	}

	private URI getPSEAQuantURL() throws URISyntaxException {
		return new URI("http://sealion.scripps.edu:18080/PSEA-Quant/PSEAForm");
	}
}
