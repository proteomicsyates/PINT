package edu.scripps.yates.excel.proteindb.importcfg.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.model.interfaces.QuantifiedPSMInterface;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.dtaselect.ProteinDTASelectParser;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.dtaselectparser.util.DTASelectProtein;
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
import edu.scripps.yates.utilities.model.factories.AmountEx;
import edu.scripps.yates.utilities.model.factories.ConditionEx;
import edu.scripps.yates.utilities.model.factories.PSMEx;
import edu.scripps.yates.utilities.model.factories.PeptideEx;
import edu.scripps.yates.utilities.model.factories.ProteinEx;
import edu.scripps.yates.utilities.proteomicsmodel.Accession;
import edu.scripps.yates.utilities.proteomicsmodel.Amount;
import edu.scripps.yates.utilities.proteomicsmodel.Condition;
import edu.scripps.yates.utilities.proteomicsmodel.Gene;
import edu.scripps.yates.utilities.proteomicsmodel.PSM;
import edu.scripps.yates.utilities.proteomicsmodel.Peptide;
import edu.scripps.yates.utilities.proteomicsmodel.Project;
import edu.scripps.yates.utilities.proteomicsmodel.Protein;
import edu.scripps.yates.utilities.proteomicsmodel.Sample;
import edu.scripps.yates.utilities.proteomicsmodel.Score;
import edu.scripps.yates.utilities.proteomicsmodel.staticstorage.StaticProteomicsModelStorage;
import edu.scripps.yates.utilities.proteomicsmodel.utils.ModelUtils;

public class ConditionAdapter implements edu.scripps.yates.utilities.pattern.Adapter<Condition> {
	private final static Logger log = Logger.getLogger(ConditionAdapter.class);
	private final ExperimentalConditionType expConditionCfg;
	private final ExperimentalDesignType experimentalDesignCfg;
	private final Project project;
	private final ExcelFileReader excelFileReader;
	private final RemoteFileReader remoteFileReader;
	private final MsRunsType msRunsCfg;
	private final OrganismSetType organismTypeCfg;
	private static final Map<String, Condition> conditionsById = new HashMap<String, Condition>();
	private static final Map<String, Map<String, Set<Protein>>> proteinsByRunIDAndAcc = new HashMap<String, Map<String, Set<Protein>>>();
	private static final Map<String, Set<Peptide>> peptidesByRunID = new HashMap<String, Set<Peptide>>();
	private static final Map<String, Set<PSM>> psmsByRunID = new HashMap<String, Set<PSM>>();
	private final Set<String> thereIsProteinSetReferenceFromExcelByMSRunID = new HashSet<String>();

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

			for (IdentificationExcelType excelInfo : expConditionCfg.getIdentificationInfo().getExcelIdentInfo()) {
				if (excelFileReader != null) {

					List<MsRunType> msRunCfgs = ImportCfgUtil.getMSRuns(excelInfo.getMsRunRef(), msRunsCfg,
							ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
					for (MsRunType msRunCfg : msRunCfgs) {
						thereIsProteinSetReferenceFromExcelByMSRunID.add(msRunCfg.getId());
						final ProteinsAdapterByExcel proteinsAdapterByExcel = new ProteinsAdapterByExcel(excelInfo,
								excelFileReader, ret, msRunCfg, sample);
						final Set<Protein> proteins = proteinsAdapterByExcel.adapt();
						log.info(proteins.size() + " proteins from MS RUN: " + excelInfo.getMsRunRef());
						addProteinsByRunID(project.getTag(), msRunCfg.getId(), proteins);

						for (Protein protein : proteins) {
							// protein-condition
							protein.addCondition(ret);
							// condition-protein

							ret.addProtein(protein);

							final Set<PSM> psMs = protein.getPSMs();
							if (psMs != null) {
								addPSMsByRunID(project.getTag(), msRunCfg.getId(), psMs);
								for (PSM psm : psMs) {
									if (psm == null)
										continue;
									ret.addPSM(psm);
									// protein-psm
									protein.addPSM(psm);
									// psm-protein
									psm.addProtein(protein);
									// psm-condition
									psm.addCondition(ret);
								}
							}
							final Set<Peptide> peptides = protein.getPeptides();
							if (peptides != null) {
								addPeptidesByRunID(project.getTag(), msRunCfg.getId(), peptides);
								for (Peptide peptide : peptides) {
									ret.addPeptide(peptide);
									peptide.addCondition(ret);
								}
							}
							// they should be created anyway in the
							// ProteinsAdapterByExcel.java
							// // create the peptides if not created before
							// if (excelInfo.getPeptideSequence() == null) {
							// ModelUtils.createPeptides(protein);
							// final Set<Peptide> peptides =
							// protein.getPeptides();
							// if (peptides != null && !peptides.isEmpty()) {
							// for (Peptide peptide : peptides) {
							// // peptide-condition
							// peptide.addCondition(ret);
							// // condition-peptide
							// ret.addPeptide(peptide);
							// // protein-peptide
							// protein.addPeptide(peptide);
							// // peptide-protein
							// peptide.addProtein(protein);
							// if (peptide.getPSMs() != null) {
							// for (PSM psm : peptide.getPSMs()) {
							// // protein-psm
							// psm.addProtein(protein);
							// protein.addPSM(psm);
							// // psm-condition
							// psm.addCondition(ret);
							// ret.addPSM(psm);
							// // psm-peptide
							// psm.setPeptide(peptide);
							// // peptide-psm
							// peptide.addPSM(psm);
							// }
							// }
							// }
							// }
							// }
							// } else {
							// // in case of not define the PSMs but the
							// Peptides
							// Set<Peptide> peptides = protein.getPeptides();
							// for (Peptide peptide : peptides) {
							// // peptide-condition
							// peptide.addCondition(ret);
							// // condition-peptide
							// ret.addPeptide(peptide);
							// // protein-peptide
							// protein.addPeptide(peptide);
							// // peptide-protein
							// peptide.addProtein(protein);
							// if (peptide.getPSMs() != null) {
							// for (PSM psm : peptide.getPSMs()) {
							// // protein-psm
							// psm.addProtein(protein);
							// protein.addPSM(psm);
							// // psm-condition
							// psm.addCondition(ret);
							// ret.addPSM(psm);
							// // psm-peptide
							// psm.setPeptide(peptide);
							// // peptide-psm
							// peptide.addPSM(psm);
							// }
							// }
							// }

						}
					}
				}
			}

		}

		if (expConditionCfg.getIdentificationInfo() != null && remoteFileReader != null) {

			final List<RemoteInfoType> identRemoteFilesInfo = expConditionCfg.getIdentificationInfo()
					.getRemoteFilesIdentInfo();

			for (RemoteInfoType remoteInfoCfg : identRemoteFilesInfo) {
				List<MsRunType> msRunCfgs = ImportCfgUtil.getMSRuns(remoteInfoCfg.getMsRunRef(), msRunsCfg,
						ExcelUtils.MULTIPLE_ITEM_SEPARATOR);
				for (MsRunType msRunCfg : msRunCfgs) {

					// if (msRunCfg.getFastaFileRef() == null) {
					final Map<String, Protein> remoteProteins = new ProteinsAdapterByRemoteFiles(remoteInfoCfg,
							remoteFileReader, ret, msRunCfg, organismTypeCfg).adapt();

					// if there is already proteins related to this MSRun, merge
					// with them
					if (getProteinsByRunID(project.getTag(), msRunCfg.getId()) != null
							&& !getProteinsByRunID(project.getTag(), msRunCfg.getId()).isEmpty()) {
						// only add other proteins if there is not a reference
						// protein set from excel
						final boolean addProteinsNotInOriginalProteinSet = !thereIsProteinSetReferenceFromExcelByMSRunID
								.contains(msRunCfg.getId());
						mergeProteins(getProteinsByRunID(project.getTag(), msRunCfg.getId()), remoteProteins,
								addProteinsNotInOriginalProteinSet, project.getTag(), msRunCfg.getId());
						for (Protein protein : getProteinsByRunID(project.getTag(), msRunCfg.getId())) {
							if (protein.getOrganism() == null) {
								log.info(protein);
							}
							ret.addProtein(protein);
							final Set<PSM> psMs = protein.getPSMs();
							addPSMsByRunID(project.getTag(), msRunCfg.getId(), psMs);
							for (PSM psm : psMs) {
								ret.addPSM(psm);
							}
							final Set<Peptide> peptides = protein.getPeptides();
							for (Peptide peptide : peptides) {
								ret.addPeptide(peptide);
							}
							addPeptidesByRunID(project.getTag(), msRunCfg.getId(), peptides);
						}
					} else {
						log.info(
								"Adding proteins directly from remote files into condition " + expConditionCfg.getId());
						addProteinsByRunID(project.getTag(), msRunCfg.getId(), remoteProteins.values());
						for (Protein protein : remoteProteins.values()) {
							ret.addProtein(protein);
							final Set<PSM> psMs = protein.getPSMs();
							addPSMsByRunID(project.getTag(), msRunCfg.getId(), psMs);
							for (PSM psm : psMs) {
								ret.addPSM(psm);
							}
							final Set<Peptide> peptides = protein.getPeptides();
							for (Peptide peptide : peptides) {
								ret.addPeptide(peptide);
							}
							addPeptidesByRunID(project.getTag(), msRunCfg.getId(), peptides);
						}
					}
				}
			}
		}
		// create amounts
		createAmounts(expConditionCfg.getQuantificationInfo(), ret);

		log.info(ret.getProteins().size() + " proteins in exp condition " + ret.getName());
		log.info(ret.getPSMs().size() + " psms in exp condition " + ret.getName());
		log.info(ret.getPeptides().size() + " peptides in exp condition " + ret.getName());
		return ret;
	}

	private void createAmounts(QuantificationInfoType quantificationInfo, Condition condition) {
		if (quantificationInfo != null) {
			if (quantificationInfo.getExcelQuantInfo() != null && !quantificationInfo.getExcelQuantInfo().isEmpty())
				createAmountsFromExcel(quantificationInfo.getExcelQuantInfo(), condition);
			if (quantificationInfo.getRemoteFilesQuantInfo() != null
					&& !quantificationInfo.getRemoteFilesQuantInfo().isEmpty())
				createAmountsFromRemoteFiles(quantificationInfo.getRemoteFilesQuantInfo(), condition);
		}
	}

	private void createAmountsFromRemoteFiles(List<RemoteInfoType> remoteFilesInfo, Condition condition) {
		log.info("Creating amounts from " + remoteFilesInfo.size() + " remote files");
		for (RemoteInfoType remoteInfoType : remoteFilesInfo) {

			String msRunRef = remoteInfoType.getMsRunRef();
			DBIndexInterface dbIndex = null;
			QuantParser quantParser = remoteFileReader.getQuantParser(remoteInfoType.getFileRef());
			DTASelectParser dtaSelectParser = remoteFileReader.getDTASelectFilterParser(remoteInfoType.getFileRef());
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
					Set<PSM> runPSMs = getPSMsByRunID(project.getTag(), msRunRef);
					for (PSM runPSM : runPSMs) {
						final String psmIdentifier = runPSM.getPSMIdentifier();

						final QuantifiedPSMInterface quantifiedPSM = quantPSMsMap.get(psmIdentifier);
						if (quantifiedPSM == null) {
							// log.warn(runPSM.getPSMIdentifier()
							// + " doesn't have quantitative information.
							// Skipping it...");
							continue;
						}
						Set<Amount> amounts = quantifiedPSM.getAmounts();
						for (Amount amount : amounts) {
							runPSM.addAmount(amount);
						}
					}
				}
			} else if (dtaSelectParser != null) {
				// TODO
				log.info("DTA Select parser is not null");
				if (remoteInfoType.getDiscardDecoys() != null)
					dtaSelectParser.setDecoyPattern(remoteInfoType.getDiscardDecoys());
				if (dbIndex != null) {
					dtaSelectParser.setDbIndex(dbIndex);
				}

				try {
					Map<String, DTASelectProtein> dtaSelectProteinsMap = dtaSelectParser.getDTASelectProteins();

					if (dtaSelectProteinsMap != null) {
						// map the proteins with the appropiate parsed accession
						Map<String, DTASelectProtein> dtaSelectProteinsMap2 = new HashMap<String, DTASelectProtein>();
						for (DTASelectProtein dtaSelectProtein : dtaSelectProteinsMap.values()) {
							dtaSelectProteinsMap2.put(ProteinDTASelectParser
									.getProteinAccessionFromDTASelectProtein(dtaSelectProtein).getAccession(),
									dtaSelectProtein);
						}
						Set<Protein> runProteins = getProteinsByRunID(project.getTag(), msRunRef);
						for (Protein runProtein : runProteins) {

							final DTASelectProtein dtaSelectProtein = dtaSelectProteinsMap2
									.get(runProtein.getAccession());
							if (dtaSelectProtein == null) {
								log.warn(runProtein.getAccession()
										+ " doesn't have quantitative information. Skipping it...");
								continue;
							}
							// SPECTRAL COUNT AMOUNT
							if (dtaSelectProtein.getSpectrumCount() != null) {
								AmountEx spcAmount = new AmountEx(dtaSelectProtein.getSpectrumCount(),
										edu.scripps.yates.utilities.model.enums.AmountType.SPC, condition);
								runProtein.addAmount(spcAmount);
							}
							// NSAF AMOUNT
							if (dtaSelectProtein.getNsaf() != null) {
								AmountEx nsafAmount = new AmountEx(dtaSelectProtein.getNsaf(),
										edu.scripps.yates.utilities.model.enums.AmountType.NSAF, condition);
								runProtein.addAmount(nsafAmount);
							}
							// EMPAI AMOUNT
							if (dtaSelectProtein.getEmpai() != null) {
								AmountEx empaiAmount = new AmountEx(dtaSelectProtein.getEmpai(),
										edu.scripps.yates.utilities.model.enums.AmountType.EMPAI, condition);
								runProtein.addAmount(empaiAmount);
							}
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private void createAmountsFromExcel(List<QuantificationExcelType> excelQuantInfo, Condition condition) {
		// FROM EXCEL
		if (excelQuantInfo != null) {
			for (QuantificationExcelType quantExcelCfg : excelQuantInfo) {
				final String msRunRefString = quantExcelCfg.getMsRunRef();

				final List<AmountType> psmAmountsCfg = quantExcelCfg.getPsmAmount();
				final List<AmountType> peptideAmountsCfg = quantExcelCfg.getPeptideAmount();
				final List<AmountType> proteinAmountsCfg = quantExcelCfg.getProteinAmount();

				List<String> msRunRefs = new ArrayList<String>();
				if (msRunRefString.contains(ExcelUtils.MULTIPLE_ITEM_SEPARATOR)) {
					msRunRefs.addAll(Arrays.asList(msRunRefString.split(ExcelUtils.MULTIPLE_ITEM_SEPARATOR)));
				} else {
					msRunRefs.add(msRunRefString);
				}
				for (String msRunRef : msRunRefs) {
					// PSM AMOUNTS
					if (psmAmountsCfg != null && !psmAmountsCfg.isEmpty()) {
						createPSMAmountsFromExcel(psmAmountsCfg, condition, msRunRef);
					}
					// Peptide AMOUNTS
					if (peptideAmountsCfg != null && !peptideAmountsCfg.isEmpty()) {
						createPeptideAmountsFromExcel(peptideAmountsCfg, condition, msRunRef);
					}
					// protein AMOUNTS
					if (proteinAmountsCfg != null && !proteinAmountsCfg.isEmpty()) {
						createProteinAmountsFromExcel(proteinAmountsCfg, condition, msRunRef);
					}
				}
			}
		}

	}

	private void createProteinAmountsFromExcel(List<AmountType> proteinAmountsCfg, Condition condition,
			String msRunRef) {
		final Set<Protein> runProteins = getProteinsByRunID(project.getTag(), msRunRef);
		for (AmountType amountCfg : proteinAmountsCfg) {
			final ExcelColumn excelColumn = excelFileReader.getExcelColumnFromReference(amountCfg.getColumnRef());
			for (int rowIndex = 0; rowIndex < excelColumn.getValues().size(); rowIndex++) {
				final Amount proteinAmount = new AmountAdapterByExcel(rowIndex, amountCfg, excelFileReader, condition)
						.adapt();
				if (proteinAmount != null) {
					// assign just to the proteins in that rowIndex
					// final Set<Protein> rowProteins =
					// ProteinsAdapterByExcel.getProteinsByMSRunAndRowIndex(msRunRef,
					// rowIndex);
					final Set<Protein> rowProteins = StaticProteomicsModelStorage.getProtein(msRunRef,
							condition.getName(), rowIndex, null);
					for (Protein rowProtein : rowProteins) {
						if (runProteins != null) {
							if (runProteins.contains(rowProtein)) {
								((ProteinEx) rowProtein).addAmount(proteinAmount);
							}
						}
					}
				}
			}
		}
	}

	private void createPSMAmountsFromExcel(List<AmountType> psmAmountsCfg, Condition condition, String msRunRef) {
		final Set<PSM> runPSMs = getPSMsByRunID(project.getTag(), msRunRef);
		for (AmountType amountCfg : psmAmountsCfg) {
			final ExcelColumn excelColumn = excelFileReader.getExcelColumnFromReference(amountCfg.getColumnRef());
			for (int rowIndex = 0; rowIndex < excelColumn.getValues().size(); rowIndex++) {
				final Amount psmAmount = new AmountAdapterByExcel(rowIndex, amountCfg, excelFileReader, condition)
						.adapt();
				if (psmAmount != null) {
					// assign just to the psms in that rowIndex
					// final Set<PSM> rowPSMs =
					// PSMAdapterByExcel.getPSMsByMSRunAndRow(rowIndex,
					// msRunRef);
					String psmId = rowIndex + msRunRef;
					final Set<PSM> rowPSMs = StaticProteomicsModelStorage.getPSM(psmId, condition.getName(), rowIndex,
							null);
					for (PSM rowPSM : rowPSMs) {
						if (runPSMs.contains(rowPSM)) {
							((PSMEx) rowPSM).addAmount(psmAmount);
						}
					}
				}
			}
		}
	}

	private void createPeptideAmountsFromExcel(List<AmountType> peptideAmountsCfg, Condition condition,
			String msRunRef) {
		final Set<Peptide> runPeptides = getPeptidesByRunID(project.getTag(), msRunRef);
		for (AmountType amountCfg : peptideAmountsCfg) {
			final ExcelColumn excelColumn = excelFileReader.getExcelColumnFromReference(amountCfg.getColumnRef());
			for (int rowIndex = 0; rowIndex < excelColumn.getValues().size(); rowIndex++) {
				final Amount peptideAmount = new AmountAdapterByExcel(rowIndex, amountCfg, excelFileReader, condition)
						.adapt();
				if (peptideAmount != null) {
					// assign just to the peptides in that rowIndex
					// final Set<Peptide> rowPeptides =
					// PeptideAdapterByExcel.peptideByRowIndex.get(rowIndex);
					final Set<Peptide> rowPeptides = StaticProteomicsModelStorage.getPeptide(msRunRef,
							condition.getName(), rowIndex, null);
					for (Peptide rowPeptide : rowPeptides) {
						if (runPeptides.contains(rowPeptide)) {
							((PeptideEx) rowPeptide).addAmount(peptideAmount);
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
	 * @param addProteinsNotInOriginalProteinSet
	 *            means that if some protein is in otherProteins map that is not
	 *            present on originalProtein map, if true, it will be
	 *            incorporated to the resulting set. Otherwise, it will be
	 *            ignored.
	 * @param msRunID
	 * @param projectTag
	 * @return
	 */
	private void mergeProteins(Collection<Protein> originalProteins, Map<String, Protein> otherProteins,
			boolean addProteinsNotInOriginalProteinSet, String projectTag, String msRunID) {
		log.info("merging " + originalProteins.size() + " with " + otherProteins.size() + " proteins");
		// index original proteins by accessions
		Map<String, Set<Protein>> originalProteinsMap = new HashMap<String, Set<Protein>>();
		ModelUtils.addToMap(originalProteinsMap, originalProteins);

		int numValid = 0;
		for (String otherProteinAccPrimitive : otherProteins.keySet()) {
			// otherProteinAccPrimitive can be IPI1090020.2 and we here split it
			// in IPI1090020.2 and IPI1090020
			Set<String> otherProteinAccVersions = new HashSet<String>();
			otherProteinAccVersions.add(otherProteinAccPrimitive);
			if (otherProteinAccPrimitive.contains(".")) {
				otherProteinAccVersions.add(otherProteinAccPrimitive.split("\\.")[0]);
			}
			for (String otherProteinAcc : otherProteinAccVersions) {

				if (originalProteinsMap.containsKey(otherProteinAcc)) {
					Protein otherProtein = otherProteins.get(otherProteinAccPrimitive);
					for (Protein originalProtein : originalProteinsMap.get(otherProteinAcc)) {
						// merge protein
						mergeProteins(originalProtein, otherProtein);
					}
					numValid++;
				} else if (addProteinsNotInOriginalProteinSet) {
					Protein otherProtein = otherProteins.get(otherProteinAccPrimitive);
					// add to the map
					addProteinByRunID(projectTag, msRunID, otherProtein);
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

	private void addProteinByRunID(String projectTag, String msRunID, Protein otherProtein) {
		Set<Protein> proteins = new HashSet<Protein>();
		proteins.add(otherProtein);
		addProteinsByRunID(projectTag, msRunID, proteins);
	}

	private void mergeProteins(Protein originalProtein, Protein otherProtein) {
		if (originalProtein.hashCode() == otherProtein.hashCode())
			return;
		// scores
		if (otherProtein.getScores() != null) {
			for (Score score : otherProtein.getScores()) {
				originalProtein.addScore(score);
			}
		}

		// psms
		if (otherProtein.getPSMs() != null) {
			for (PSM psm : otherProtein.getPSMs()) {
				// add if different psmID
				boolean found = false;
				for (PSM originalPSM : originalProtein.getPSMs()) {
					if (originalPSM.getPSMIdentifier().equals(psm.getPSMIdentifier())) {
						found = true;
						break;
					}
				}
				if (!found)
					originalProtein.addPSM(psm);
				// else
				// log.info("Skipping this");
			}
		}

		// peptides
		if (otherProtein.getPeptides() != null) {
			for (Peptide peptide : otherProtein.getPeptides()) {
				originalProtein.addPeptide(peptide);
			}
		}
		// length
		if (originalProtein.getLength() <= 0)
			originalProtein.setLength(otherProtein.getLength());

		// pi
		if (originalProtein.getPi() <= 0)
			originalProtein.setPi(otherProtein.getPi());

		// mw
		if (originalProtein.getMW() <= 0)
			originalProtein.setMw(otherProtein.getMW());

		// organism
		if (originalProtein.getOrganism() == null)
			originalProtein.setOrganism(otherProtein.getOrganism());

		// conditions
		if (otherProtein.getConditions() != null) {
			for (Condition condition : otherProtein.getConditions()) {
				originalProtein.addCondition(condition);
			}
		}

		// msrun
		if (originalProtein.getMSRun() == null)
			originalProtein.setMSRun(otherProtein.getMSRun());

		// genes
		if (otherProtein.getGenes() != null) {
			for (Gene gene : otherProtein.getGenes()) {
				originalProtein.addGene(gene);
			}
		}

	}

	public static Condition getConditionById(String projectTag, String conditionId) {
		String key = projectTag + conditionId;
		return conditionsById.get(key);
	}

	public static void addConditionById(String projectTag, String conditionId, Condition condition) {
		String key = projectTag + conditionId;
		conditionsById.put(key, condition);
	}

	private static void addProteinsByRunID(String projectTag, String msrunID, Collection<Protein> proteins) {

		// because sometimes the run
		// id is the same in
		// different experiments
		String key = projectTag + msrunID;
		if (proteinsByRunIDAndAcc.containsKey(key)) {
			for (Protein protein : proteins) {
				final String accession = protein.getPrimaryAccession().getAccession();
				if (proteinsByRunIDAndAcc.get(key).containsKey(accession)) {
					proteinsByRunIDAndAcc.get(key).get(accession).add(protein);
				} else {
					Set<Protein> set = new HashSet<Protein>();
					set.add(protein);
					proteinsByRunIDAndAcc.get(key).put(accession, set);
				}
				final List<Accession> secondaryAccessions = protein.getSecondaryAccessions();
				if (secondaryAccessions != null) {
					for (Accession accession2 : secondaryAccessions) {
						if (proteinsByRunIDAndAcc.get(key).containsKey(accession2.getAccession())) {
							proteinsByRunIDAndAcc.get(key).get(accession2.getAccession()).add(protein);
						} else {
							Set<Protein> set = new HashSet<Protein>();
							set.add(protein);
							proteinsByRunIDAndAcc.get(key).put(accession2.getAccession(), set);
						}
					}
				}
			}
		} else {
			Map<String, Set<Protein>> map = new HashMap<String, Set<Protein>>();
			for (Protein protein : proteins) {
				final String accession = protein.getPrimaryAccession().getAccession();
				if (map.containsKey(accession)) {
					map.get(accession).add(protein);
				} else {
					Set<Protein> set = new HashSet<Protein>();
					set.add(protein);
					map.put(accession, set);
				}
				final List<Accession> secondaryAccessions = protein.getSecondaryAccessions();
				if (secondaryAccessions != null) {
					for (Accession accession2 : secondaryAccessions) {
						if (map.containsKey(accession2.getAccession())) {
							map.get(accession2.getAccession()).add(protein);
						} else {
							Set<Protein> set = new HashSet<Protein>();
							set.add(protein);
							map.put(accession2.getAccession(), set);
						}
					}
				}
			}
			proteinsByRunIDAndAcc.put(key, map);

		}
	}

	public static Set<Protein> getProteinsByRunID(String projectTag, String msRunRef) {
		String key = projectTag + msRunRef;
		final Map<String, Set<Protein>> map = proteinsByRunIDAndAcc.get(key);

		Set<Protein> set = new HashSet<Protein>();
		if (map != null) {
			for (Set<Protein> proteinSet : map.values()) {
				for (Protein protein : proteinSet) {
					set.add(protein);
				}
			}
		}
		return set;
	}

	private static void addPeptidesByRunID(String projectTag, String msrunID, Collection<Peptide> peptides) {
		// because sometimes the run
		// id is the same in
		// different experiments
		String key = projectTag + msrunID;
		if (peptidesByRunID.containsKey(key)) {
			peptidesByRunID.get(key).addAll(peptides);
		} else {
			Set<Peptide> set = new HashSet<Peptide>();
			set.addAll(peptides);
			peptidesByRunID.put(key, set);
		}
	}

	public static Set<Peptide> getPeptidesByRunID(String projectTag, String msRunRef) {
		String key = projectTag + msRunRef;
		return peptidesByRunID.get(key);
	}

	private static void addPSMsByRunID(String projectTag, String msrunID, Collection<PSM> psms) {

		// because sometimes the run
		// id is the same in
		// different experiments
		String key = projectTag + msrunID;
		if (psmsByRunID.containsKey(key)) {
			psmsByRunID.get(key).addAll(psms);
		} else {
			Set<PSM> set = new HashSet<PSM>();
			set.addAll(psms);
			psmsByRunID.put(key, set);
		}
	}

	public static Set<PSM> getPSMsByRunID(String projectTag, String msRunRef) {
		String key = projectTag + msRunRef;
		return psmsByRunID.get(key);
	}

	protected static void clearStaticInformation() {
		psmsByRunID.clear();
		peptidesByRunID.clear();
		proteinsByRunIDAndAcc.clear();
		conditionsById.clear();

	}

	/**
	 * @return the proteinsbyrunid
	 */
	public static Map<String, Map<String, Set<Protein>>> getProteinsbyrunidAndPrimaryAcc() {
		return proteinsByRunIDAndAcc;
	}
}
