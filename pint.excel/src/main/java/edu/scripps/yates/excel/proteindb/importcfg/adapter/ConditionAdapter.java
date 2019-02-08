package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.dbindex.DBIndexImpl;
import edu.scripps.yates.excel.ExcelColumn;
import edu.scripps.yates.excel.proteindb.importcfg.ExcelFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.RemoteFileReader;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.AmountType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalConditionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdentificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationExcelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.QuantificationInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.excel.util.ExcelUtils;
import edu.scripps.yates.utilities.parsers.idparser.IdentificationsParser;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.factories.AmountEx;
import edu.scripps.yates.utilities.proteomicsmodel.factories.ConditionEx;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;
import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

public class ConditionAdapter implements edu.scripps.yates.utilities.pattern.Adapter<Condition> {
	private final static Logger log = Logger.getLogger(ConditionAdapter.class);
	private final ExperimentalConditionType expConditionCfg;
	private final ExperimentalDesignType experimentalDesignCfg;
	private final Project project;
	private final ExcelFileReader excelFileReader;
	private final RemoteFileReader remoteFileReader;
	private final MsRunsType msRunsCfg;
	private final OrganismSetType organismTypeCfg;
	private static final Map<String, Condition> conditionsById = new THashMap<String, Condition>();

	private final Set<String> thereIsProteinSetReferenceFromExcelByMSRunID = new THashSet<String>();

	public ConditionAdapter(ExperimentalConditionType expConditionCfg, MsRunsType msRunsType,
			ExperimentalDesignType experimentalDesignCfg, OrganismSetType organismTypeCfg, Project project,
			ExcelFileReader excelReader, RemoteFileReader remoteFileReader) {
		this.expConditionCfg = expConditionCfg;
		this.experimentalDesignCfg = experimentalDesignCfg;
		this.project = project;
		excelFileReader = excelReader;
		this.remoteFileReader = remoteFileReader;
		msRunsCfg = msRunsType;
		this.organismTypeCfg = organismTypeCfg;
	}

	@Override
	public Condition adapt() {

		log.info("processing experimental condition: " + expConditionCfg.getId());
		final SampleType sampleCfg = ImportCfgUtil.getSampleCfg(expConditionCfg.getSampleRef(),
				experimentalDesignCfg.getSampleSet());
		Sample sample = null;
		if (sampleCfg != null) {
			sample = new SampleAdapter(sampleCfg, experimentalDesignCfg.getOrganismSet(),
					experimentalDesignCfg.getTissueSet(), experimentalDesignCfg.getLabelSet()).adapt();
		}
		// take from the map, just in case the object has been already created
		// from the peptide ratios
		ConditionEx ret = null;

		// because sometimes the condition id is the same in different
		// experiments
		if (getConditionById(project.getTag(), expConditionCfg.getId()) == null) {
			ret = new ConditionEx(expConditionCfg.getId(), sample, project);
			addConditionById(project.getTag(), expConditionCfg.getId(), ret);
		} else {
			ret = (ConditionEx) getConditionById(project.getTag(), expConditionCfg.getId());
		}

		ret.setSample(sample);
		ret.setProject(project);
		ret.setDescription(expConditionCfg.getDescription());

		// proteins by excel
		if (expConditionCfg.getIdentificationInfo() != null
				&& !expConditionCfg.getIdentificationInfo().getExcelIdentInfo().isEmpty()) {
			log.info("containing " + expConditionCfg.getIdentificationInfo().getExcelIdentInfo().size()
					+ "  excel information");
			for (final IdentificationExcelType excelInfo : expConditionCfg.getIdentificationInfo()
					.getExcelIdentInfo()) {
				if (excelFileReader != null) {

					final List<MsRunType> msRunCfgs = ImportCfgUtil.getMSRuns(excelInfo.getMsRunRef(), msRunsCfg,
							ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
					for (final MsRunType msRunType : msRunCfgs) {
						thereIsProteinSetReferenceFromExcelByMSRunID.add(msRunType.getId());
					}
					final List<String> msRunIDs = msRunCfgs.stream().map(msRunType -> msRunType.getId())
							.collect(Collectors.toList());
					final ProteinsAdapterByExcel proteinsAdapterByExcel = new ProteinsAdapterByExcel(excelInfo,
							excelFileReader, ret, msRunCfgs, sample, project);
					final Set<Protein> proteins = proteinsAdapterByExcel.adapt();
					log.info(proteins.size() + " proteins from MS RUN(s): " + excelInfo.getMsRunRef());

					for (final Protein protein : proteins) {
						StaticProteomicsModelStorage.addProtein(protein, msRunIDs, expConditionCfg.getId());
						// protein-condition
						protein.addCondition(ret);
						// condition-protein

						ret.addProtein(protein);

						final List<PSM> psMs = protein.getPSMs();
						if (psMs != null) {
							for (final PSM psm : psMs) {
								if (psm == null)
									continue;
								StaticProteomicsModelStorage.addPSM(psm, msRunIDs.get(0), expConditionCfg.getId());
								ret.addPSM(psm);
								// protein-psm
								protein.addPSM(psm, true);
								// psm-condition
								psm.addCondition(ret);
							}
						}
						final Set<Peptide> peptides = protein.getPeptides();
						if (peptides != null) {
							for (final Peptide peptide : peptides) {
								StaticProteomicsModelStorage.addPeptide(peptide, msRunIDs, expConditionCfg.getId());
								ret.addPeptide(peptide);
								peptide.addCondition(ret);
							}
						}
					}
				}
			}
		}

		if (expConditionCfg.getIdentificationInfo() != null && remoteFileReader != null) {

			final List<RemoteInfoType> identRemoteFilesInfo = expConditionCfg.getIdentificationInfo()
					.getRemoteFilesIdentInfo();

			for (final RemoteInfoType remoteInfoCfg : identRemoteFilesInfo) {
				final List<MsRunType> msRunCfgs = ImportCfgUtil.getMSRuns(remoteInfoCfg.getMsRunRef(), msRunsCfg,
						ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
				final List<String> msRunIDs = msRunCfgs.stream().map(msRunType -> msRunType.getId())
						.collect(Collectors.toList());
				// if (msRunCfg.getFastaFileRef() == null) {
				final Map<String, Protein> remoteProteins = new ProteinsAdapterByRemoteFiles(remoteInfoCfg,
						remoteFileReader, ret, msRunCfgs, organismTypeCfg).adapt();

				// if there is already proteins related to any of these MSRuns,
				// merge with them
				final String acc = null;
				if (StaticProteomicsModelStorage.getProtein(msRunIDs, expConditionCfg.getId(), acc) != null
						&& !StaticProteomicsModelStorage.getProtein(msRunIDs, expConditionCfg.getId(), acc).isEmpty()) {
					// only add other proteins if there is not a reference
					// protein set from excel
					boolean addProteinsNotInOriginalProteinSet = true;
					for (final MsRunType msRunType : msRunCfgs) {
						if (thereIsProteinSetReferenceFromExcelByMSRunID.contains(msRunType.getId())) {
							addProteinsNotInOriginalProteinSet = false;
						}
					}

					mergeProteins(StaticProteomicsModelStorage.getProtein(msRunIDs, expConditionCfg.getId(), acc),
							remoteProteins, addProteinsNotInOriginalProteinSet, project.getTag(), msRunIDs);
					for (final Protein protein : StaticProteomicsModelStorage.getProtein(msRunIDs,
							expConditionCfg.getId(), acc)) {
						if (protein.getOrganism() == null) {
							log.info(protein);
						}
						ret.addProtein(protein);
						final List<PSM> psMs = protein.getPSMs();

						for (final PSM psm : psMs) {
							StaticProteomicsModelStorage.addPSM(psm, msRunIDs.get(0), expConditionCfg.getId());
							ret.addPSM(psm);
						}
						final Set<Peptide> peptides = protein.getPeptides();
						for (final Peptide peptide : peptides) {
							StaticProteomicsModelStorage.addPeptide(peptide, msRunIDs, expConditionCfg.getId());
							ret.addPeptide(peptide);
						}
					}
				} else {
					log.info("Adding proteins directly from remote files into condition " + expConditionCfg.getId());
					for (final Protein protein : remoteProteins.values()) {
						StaticProteomicsModelStorage.addProtein(protein, msRunIDs, expConditionCfg.getId());
						ret.addProtein(protein);
						final List<PSM> psMs = protein.getPSMs();
						for (final PSM psm : psMs) {
							StaticProteomicsModelStorage.addPSM(psm, msRunIDs.get(0), expConditionCfg.getId());
							ret.addPSM(psm);
						}
						final Set<Peptide> peptides = protein.getPeptides();
						for (final Peptide peptide : peptides) {
							ret.addPeptide(peptide);
							StaticProteomicsModelStorage.addPeptide(peptide, msRunIDs, expConditionCfg.getId());
						}
					}
				}
			}
		}
		// create amounts
		try {
			createAmounts(expConditionCfg.getQuantificationInfo(), ret);
		} catch (final IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}

		log.info(ret.getProteins().size() + " proteins in exp condition " + ret.getName());
		log.info(ret.getPSMs().size() + " psms in exp condition " + ret.getName());
		log.info(ret.getPeptides().size() + " peptides in exp condition " + ret.getName());
		return ret;
	}

	private void createAmounts(QuantificationInfoType quantificationInfo, Condition condition) throws IOException {
		if (quantificationInfo != null) {
			if (quantificationInfo.getExcelQuantInfo() != null && !quantificationInfo.getExcelQuantInfo().isEmpty())
				createAmountsFromExcel(quantificationInfo.getExcelQuantInfo(), condition);
			if (quantificationInfo.getRemoteFilesQuantInfo() != null
					&& !quantificationInfo.getRemoteFilesQuantInfo().isEmpty())
				createAmountsFromRemoteFiles(quantificationInfo.getRemoteFilesQuantInfo(), condition);
		}
	}

	private void createAmountsFromRemoteFiles(List<RemoteInfoType> remoteFilesInfo, Condition condition)
			throws IOException {
		log.info("Creating amounts from " + remoteFilesInfo.size() + " remote files");
		for (final RemoteInfoType remoteInfoType : remoteFilesInfo) {

			final List<MsRunType> msRunCfgs = ImportCfgUtil.getMSRuns(remoteInfoType.getMsRunRef(), msRunsCfg,
					ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
			final List<String> msRunIDs = msRunCfgs.stream().map(msRunType -> msRunType.getId())
					.collect(Collectors.toList());
			DBIndexImpl dbIndex = null;
			final QuantParser quantParser = remoteFileReader.getQuantParser(remoteInfoType.getFileRef());
			final IdentificationsParser identificationsParser = remoteFileReader
					.getDTASelectFilterParser(remoteInfoType.getFileRef());
			dbIndex = remoteFileReader.getFastaDBIndex(remoteInfoType);

			if (quantParser != null) {
				final String remoteFileName = FilenameUtils
						.getName(quantParser.getRemoteFileRetrievers().get(0).getOutputFile().getAbsolutePath());
				log.info("Census quant parser for file " + remoteFileName);
				if (remoteInfoType.getDiscardDecoys() != null)
					quantParser.setDecoyPattern(remoteInfoType.getDiscardDecoys());
				if (dbIndex != null) {
					quantParser.setDbIndex(dbIndex);
				}
				final Map<String, QuantifiedPSMInterface> quantPSMsMap = quantParser.getPSMMap();

				if (quantPSMsMap != null) {
					for (final String msRunRef : msRunIDs) {

						final Set<PSM> runPSMs = StaticProteomicsModelStorage.getPSM(msRunRef, condition.getName(),
								null);
						for (final PSM runPSM : runPSMs) {
							final String psmIdentifier = runPSM.getIdentifier();

							final QuantifiedPSMInterface quantifiedPSM = quantPSMsMap.get(psmIdentifier);
							if (quantifiedPSM == null) {
								// log.warn(runPSM.getPSMIdentifier()
								// + " doesn't have quantitative information.
								// Skipping it...");
								continue;
							}
							final Set<Amount> amounts = quantifiedPSM.getAmounts();
							for (final Amount amount : amounts) {
								runPSM.addAmount(amount);
							}
						}
					}
				}
			} else if (identificationsParser != null) {
				// TODO
				log.info("Identifications parser is not null");
				if (remoteInfoType.getDiscardDecoys() != null)
					identificationsParser.setDecoyPattern(remoteInfoType.getDiscardDecoys());
				if (dbIndex != null) {
					identificationsParser.setDbIndex(dbIndex);
				}

				try {
					final Map<String, Protein> dtaSelectProteinsMap = identificationsParser.getProteinMap();

					if (dtaSelectProteinsMap != null) {
						// map the proteins with the appropriate parsed
						// accession
						final Map<String, Protein> dtaSelectProteinsMap2 = new THashMap<String, Protein>();
						for (final Protein identifiedProtein : dtaSelectProteinsMap.values()) {
							dtaSelectProteinsMap2.put(identificationsParser
									.getProteinAccessionFromProtein(identifiedProtein).getAccession(),
									identifiedProtein);
						}
						for (final String msRunRef : msRunIDs) {
							final String acc = null;
							final Set<Protein> runProteins = StaticProteomicsModelStorage.getProtein(msRunRef,
									condition.getName(), acc);
							for (final Protein runProtein : runProteins) {

								final Protein dtaSelectProtein = dtaSelectProteinsMap2.get(runProtein.getAccession());
								if (dtaSelectProtein == null) {
									log.warn(runProtein.getAccession()
											+ " doesn't have quantitative information. Skipping it...");
									continue;
								}
								// SPECTRAL COUNT AMOUNT
								if (dtaSelectProtein.getSpectrumCount() != null) {
									final AmountEx spcAmount = new AmountEx(dtaSelectProtein.getSpectrumCount(),
											edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType.SPC,
											condition);
									runProtein.addAmount(spcAmount);
								}
								// NSAF AMOUNT
								if (dtaSelectProtein.getNsaf() != null) {
									final AmountEx nsafAmount = new AmountEx(dtaSelectProtein.getNsaf(),
											edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType.NSAF,
											condition);
									runProtein.addAmount(nsafAmount);
								}
								// EMPAI AMOUNT
								if (dtaSelectProtein.getEmpai() != null) {
									final AmountEx empaiAmount = new AmountEx(dtaSelectProtein.getEmpai(),
											edu.scripps.yates.utilities.proteomicsmodel.enums.AmountType.EMPAI,
											condition);
									runProtein.addAmount(empaiAmount);
								}
							}
						}
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void createAmountsFromExcel(List<QuantificationExcelType> excelQuantInfo, Condition condition) {
		// FROM EXCEL
		if (excelQuantInfo != null) {
			for (final QuantificationExcelType quantExcelCfg : excelQuantInfo) {
				final String msRunRefString = quantExcelCfg.getMsRunRef();

				final List<AmountType> psmAmountsCfg = quantExcelCfg.getPsmAmount();
				final List<AmountType> peptideAmountsCfg = quantExcelCfg.getPeptideAmount();
				final List<AmountType> proteinAmountsCfg = quantExcelCfg.getProteinAmount();

				final List<String> msRunRefs = new ArrayList<String>();
				if (msRunRefString.contains(ExcelUtils.MULTIPLE_ITEM_SEPARATOR)) {
					msRunRefs.addAll(Arrays.asList(msRunRefString.split(ExcelUtils.MULTIPLE_ITEM_SEPARATOR)));
				} else {
					msRunRefs.add(msRunRefString);
				}
				for (final String msRunRef : msRunRefs) {
					// PSM AMOUNTS
					if (psmAmountsCfg != null && !psmAmountsCfg.isEmpty()) {
						createPSMAmountsFromExcel(psmAmountsCfg, condition, msRunRef);
					}

				}
				// Peptide AMOUNTS
				if (peptideAmountsCfg != null && !peptideAmountsCfg.isEmpty()) {
					createPeptideAmountsFromExcel(peptideAmountsCfg, condition, msRunRefs);
				}
				// protein AMOUNTS
				if (proteinAmountsCfg != null && !proteinAmountsCfg.isEmpty()) {
					createProteinAmountsFromExcel(proteinAmountsCfg, condition, msRunRefs);
				}
			}
		}

	}

	private void createProteinAmountsFromExcel(List<AmountType> proteinAmountsCfg, Condition condition,
			List<String> msRunIDs) {
		final Set<Protein> runProteins = StaticProteomicsModelStorage.getProtein(msRunIDs, condition.getName(), null);
		for (final AmountType amountCfg : proteinAmountsCfg) {
			final ExcelColumn excelColumn = excelFileReader.getExcelColumnFromReference(amountCfg.getColumnRef());
			for (int rowIndex = 0; rowIndex < excelColumn.getValues().size(); rowIndex++) {
				final Amount proteinAmount = new AmountAdapterByExcel(rowIndex, amountCfg, excelFileReader, condition)
						.adapt();
				if (proteinAmount != null) {
					// assign just to the proteins in that rowIndex
					// final Set<Protein> rowProteins =
					// ProteinsAdapterByExcel.getProteinsByMSRunAndRowIndex(msRunRef,
					// rowIndex);
					final Set<Protein> rowProteins = StaticProteomicsModelStorage.getProtein(msRunIDs,
							condition.getName(), rowIndex, null);
					for (final Protein rowProtein : rowProteins) {
						if (runProteins != null) {
							if (runProteins.contains(rowProtein)) {
								rowProtein.addAmount(proteinAmount);
							}
						}
					}
				}
			}
		}
	}

	private void createPSMAmountsFromExcel(List<AmountType> psmAmountsCfg, Condition condition, String msRunRef) {
		final Set<PSM> runPSMs = StaticProteomicsModelStorage.getPSM(msRunRef, condition.getName(), null);
		for (final AmountType amountCfg : psmAmountsCfg) {
			final ExcelColumn excelColumn = excelFileReader.getExcelColumnFromReference(amountCfg.getColumnRef());
			for (int rowIndex = 0; rowIndex < excelColumn.getValues().size(); rowIndex++) {
				final Amount psmAmount = new AmountAdapterByExcel(rowIndex, amountCfg, excelFileReader, condition)
						.adapt();
				if (psmAmount != null) {
					// assign just to the psms in that rowIndex
					// final Set<PSM> rowPSMs =
					// PSMAdapterByExcel.getPSMsByMSRunAndRow(rowIndex,
					// msRunRef);
					final String psmId = rowIndex + msRunRef;
					final Set<PSM> rowPSMs = StaticProteomicsModelStorage.getPSM(psmId, condition.getName(), rowIndex,
							null);
					for (final PSM rowPSM : rowPSMs) {
						if (runPSMs.contains(rowPSM)) {
							rowPSM.addAmount(psmAmount);
						}
					}
				}
			}
		}
	}

	private void createPeptideAmountsFromExcel(List<AmountType> peptideAmountsCfg, Condition condition,
			List<String> msRunRefs) {
		final String seq = null;
		final Set<Peptide> runPeptides = StaticProteomicsModelStorage.getPeptide(msRunRefs, condition.getName(), seq);
		for (final AmountType amountCfg : peptideAmountsCfg) {
			final ExcelColumn excelColumn = excelFileReader.getExcelColumnFromReference(amountCfg.getColumnRef());
			for (int rowIndex = 0; rowIndex < excelColumn.getValues().size(); rowIndex++) {
				final Amount peptideAmount = new AmountAdapterByExcel(rowIndex, amountCfg, excelFileReader, condition)
						.adapt();
				if (peptideAmount != null) {
					// assign just to the peptides in that rowIndex
					// final Set<Peptide> rowPeptides =
					// PeptideAdapterByExcel.peptideByRowIndex.get(rowIndex);
					final Set<Peptide> rowPeptides = StaticProteomicsModelStorage.getPeptide(msRunRefs,
							condition.getName(), rowIndex, null);
					for (final Peptide rowPeptide : rowPeptides) {
						if (runPeptides.contains(rowPeptide)) {
							rowPeptide.addAmount(peptideAmount);
						}
					}
				}
			}
		}
	}

	/**
	 * This function will merge the proteins from originalProteins with the
	 * otherProteins and will return the final Set.
	 *
	 * @param originalProteins
	 * @param proteinsbyrunidfromexcel
	 * @param addProteinsNotInOriginalProteinSet means that if some protein is in
	 *                                           otherProteins map that is not
	 *                                           present on originalProtein map, if
	 *                                           true, it will be incorporated to
	 *                                           the resulting set. Otherwise, it
	 *                                           will be ignored.
	 * @param msRunID
	 * @param projectTag
	 * @return
	 */
	private void mergeProteins(Collection<Protein> originalProteins, Map<String, Protein> otherProteins,
			boolean addProteinsNotInOriginalProteinSet, String projectTag, List<String> msRunIDs) {
		log.info("merging " + originalProteins.size() + " with " + otherProteins.size() + " proteins");
		// index original proteins by accessions
		final Map<String, Set<Protein>> originalProteinsMap = new THashMap<String, Set<Protein>>();
		ModelUtils.addToMap(originalProteinsMap, originalProteins);

		int numValid = 0;
		for (final String otherProteinAccPrimitive : otherProteins.keySet()) {
			// otherProteinAccPrimitive can be IPI1090020.2 and we here split it
			// in IPI1090020.2 and IPI1090020
			final Set<String> otherProteinAccVersions = new THashSet<String>();
			otherProteinAccVersions.add(otherProteinAccPrimitive);
			if (otherProteinAccPrimitive.contains(".")) {
				otherProteinAccVersions.add(otherProteinAccPrimitive.split("\\.")[0]);
			}
			for (final String otherProteinAcc : otherProteinAccVersions) {

				if (originalProteinsMap.containsKey(otherProteinAcc)) {
					final Protein otherProtein = otherProteins.get(otherProteinAccPrimitive);
					for (final Protein originalProtein : originalProteinsMap.get(otherProteinAcc)) {
						// merge protein
						originalProtein.mergeWithProtein(otherProtein);
					}
					numValid++;
				} else if (addProteinsNotInOriginalProteinSet) {
					final Protein otherProtein = otherProteins.get(otherProteinAccPrimitive);
					StaticProteomicsModelStorage.addProtein(otherProtein, msRunIDs, expConditionCfg.getId());
					numValid++;
				} else {
					// log.debug(otherProteinAcc + " skipped");
				}
			}
		}
		log.info(originalProteinsMap.size() + " original proteins");
		log.info(otherProteins.size() + " proteins to merge");
		log.info(numValid + " proteins merged out of " + otherProteins.size());
		// for (String originalProteinAcc : originalProteinsMap.keySet()) {
		// // proteins in the "otherProteins" that are the same than original:
		// // merge them
		// if (otherProteins.containsKey(originalProteinAcc)) {
		// Protein otherProtein = otherProteins.get(originalProteinAcc);
		// for (Protein originalProtein : originalProteinsMap
		// .get(originalProteinAcc)) {
		// // merge protein
		// mergeProteins(originalProtein, otherProtein);
		// }
		// }
		// }

		// if

	}

	public static Condition getConditionById(String projectTag, String conditionId) {
		final String key = projectTag + conditionId;
		return conditionsById.get(key);
	}

	public static void addConditionById(String projectTag, String conditionId, Condition condition) {
		final String key = projectTag + conditionId;
		conditionsById.put(key, condition);
	}

	protected static void clearStaticInformation() {
		conditionsById.clear();

	}

}
