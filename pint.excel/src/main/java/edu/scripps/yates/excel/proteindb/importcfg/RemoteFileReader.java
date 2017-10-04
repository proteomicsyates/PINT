package edu.scripps.yates.excel.proteindb.importcfg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.census.analysis.QuantCondition;
import edu.scripps.yates.census.read.CensusChroParser;
import edu.scripps.yates.census.read.CensusOutParser;
import edu.scripps.yates.census.read.model.IonSerie.IonSerieType;
import edu.scripps.yates.census.read.model.RatioDescriptor;
import edu.scripps.yates.census.read.model.interfaces.QuantParser;
import edu.scripps.yates.census.read.util.IonExclusion;
import edu.scripps.yates.census.read.util.QuantificationLabel;
import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.dbindex.io.DBIndexSearchParams;
import edu.scripps.yates.dbindex.io.DBIndexSearchParamsImpl;
import edu.scripps.yates.dtaselectparser.DTASelectParser;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FastaDisgestionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServersType;
import edu.scripps.yates.excel.proteindb.importcfg.util.ImportCfgUtil;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import gnu.trove.map.hash.THashMap;

public class RemoteFileReader {
	private final static Logger log = Logger.getLogger(RemoteFileReader.class);
	private final Map<String, RemoteSSHFileReference> remoteFiles = new THashMap<String, RemoteSSHFileReference>();
	private final Map<String, FormatType> types = new THashMap<String, FormatType>();
	private final Map<String, CensusChroParser> censusChroParsers = new THashMap<String, CensusChroParser>();
	private final Map<String, CensusOutParser> censusOutParsers = new THashMap<String, CensusOutParser>();

	private final Map<String, DTASelectParser> dtaSelectParsers = new THashMap<String, DTASelectParser>();
	private final Map<String, FastaDisgestionType> fastaDigestionByFileId = new THashMap<String, FastaDisgestionType>();

	private final List<IonExclusion> censusIonExlusions = new ArrayList<IonExclusion>();
	private final Map<String, Map<QuantCondition, QuantificationLabel>> labelsByConditionsByFileID;
	private final Map<String, List<RatioDescriptor>> ratioDescriptorsByFile;

	/**
	 *
	 * @param fileSet
	 * @param servers
	 * @param fastaIndexFolder
	 *            the folder in which the fasta files will be indexed in
	 *            necessary. If null, the fasta index will be created in a TEMP
	 *            folder of the system
	 * @throws IOException
	 */
	public RemoteFileReader(FileSetType fileSet, ServersType servers, File fastaIndexFolder,
			Map<String, Map<QuantCondition, QuantificationLabel>> labelsByConditionsByFileID,
			Map<String, List<RatioDescriptor>> ratioDescriptorsByFile) throws IOException {

		// TODO change this by a configurable thing in fileSetType
		censusIonExlusions.add(new IonExclusion(IonSerieType.B, 1));
		censusIonExlusions.add(new IonExclusion(IonSerieType.Y, 1));
		this.labelsByConditionsByFileID = labelsByConditionsByFileID;
		this.ratioDescriptorsByFile = ratioDescriptorsByFile;
		final List<FileType> files = fileSet.getFile();

		for (FileType fileType : files) {
			RemoteSSHFileReference remoteFile = null;
			// if it is not an excel file
			if (fileType.getFormat() != FormatType.EXCEL) {
				String id = fileType.getId();

				if (fileType.getServerRef() != null) {
					ServerType server = ImportCfgUtil.getReferencedServer(fileType.getServerRef(), servers);
					File file = null;
					if (fileType.getFormat() == FormatType.FASTA && fastaIndexFolder != null) {
						file = new File(fastaIndexFolder.getAbsolutePath() + File.separator + fileType.getName());
						// store the getCleavageAas
						if (fileType.getFastaDigestion() != null) {
							fastaDigestionByFileId.put(id, fileType.getFastaDigestion());
						}
					} else {
						file = File.createTempFile(ImportCfgUtil.getPrefix(id), "xml");
					}
					remoteFile = new RemoteSSHFileReference(server.getHostName(), server.getUserName(),
							server.getPassword(), fileType.getName(), file);
					log.debug("Added remote file location:" + fileType.getRelativePath());
					remoteFile.setRemotePath(fileType.getRelativePath());
				} else if (fileType.getUrl() != null) {
					// is in local file system
					try {
						URL url = new URL(fileType.getUrl()).toURI().toURL();
						File destinationFile = null;
						if (fileType.getFormat() == FormatType.FASTA) {
							if (fastaIndexFolder != null) {
								destinationFile = new File(
										fastaIndexFolder.getAbsolutePath() + File.separator + fileType.getName());
								FileUtils.copyURLToFile(url, destinationFile);
							} else {
								// if it is a FASTA file, copy it to the dbIndex
								// location
								log.debug("Copying fasta file to the dbIndex location");
								destinationFile = new File(
										DBIndexInterface.getDBIndexPath() + File.separator + fileType.getName());

								log.debug("Copying file from URL " + url.toString() + " to the dbIndex location "
										+ destinationFile.getAbsolutePath());
								FileUtils.copyURLToFile(url, destinationFile);
							}
							if (fileType.getFastaDigestion() != null)
								fastaDigestionByFileId.put(id, fileType.getFastaDigestion());
						} else {
							final String decode = URLDecoder.decode(URLDecoder.decode(url.getPath(), "UTF-8"), "UTF-8");
							destinationFile = new File(decode);
						}
						if (!destinationFile.exists())
							throw new IOException("Error copying file to " + destinationFile.getAbsolutePath());
						remoteFile = new RemoteSSHFileReference(destinationFile);

					} catch (URISyntaxException e) {
						e.printStackTrace();
						throw new IllegalArgumentException(
								"The URL of the file id '" + id + "' is not well formed: " + fileType.getUrl());
					} catch (Exception e) {
						e.printStackTrace();
						throw new IllegalArgumentException(e);
					}

				}

				if (remoteFile != null) {
					remoteFiles.put(id, remoteFile);
					types.put(id, fileType.getFormat());
				}
			}

		}

	}

	/**
	 * @return the remoteFiles
	 */
	public Map<String, RemoteSSHFileReference> getRemoteFiles() {
		return remoteFiles;
	}

	public CensusChroParser getCensusChroParser(String fileId) {
		List<String> fileIds = new ArrayList<String>();
		fileIds.add(fileId);
		return getCensusChroParser(fileIds);
	}

	public CensusOutParser getCensusOutParser(String fileId) {
		List<String> fileIds = new ArrayList<String>();
		fileIds.add(fileId);
		return getCensusOutParser(fileIds);
	}

	public CensusChroParser getCensusChroParser(Collection<String> fileIds) {
		Map<File, String> fileIDByFiles = new THashMap<File, String>();
		for (String fileId : fileIds) {
			if (remoteFiles.containsKey(fileId) && types.containsKey(fileId)) {
				final RemoteSSHFileReference remoteSSHServer = remoteFiles.get(fileId);
				if (types.get(fileId) == FormatType.CENSUS_CHRO_XML) {
					final File file = remoteSSHServer.getRemoteFile();
					if (file != null)
						fileIDByFiles.put(file, fileId);

				}
			}
		}
		if (!fileIDByFiles.isEmpty()) {
			final String key = getKey(fileIDByFiles.keySet());
			if (censusChroParsers.containsKey(key)) {
				return censusChroParsers.get(key);
			} else {
				try {
					CensusChroParser parser = new CensusChroParser();
					for (File file : fileIDByFiles.keySet()) {
						final String fileID = fileIDByFiles.get(file);
						final List<RatioDescriptor> ratioDescriptors = ratioDescriptorsByFile.get(fileID);
						if (ratioDescriptors.size() == 1) {
							final RatioDescriptor ratioDescriptor = ratioDescriptors.get(0);
							parser.addFile(file, labelsByConditionsByFileID.get(fileID), ratioDescriptor.getLabel1(),
									ratioDescriptor.getLabel2());
						} else {
							QuantificationLabel labelL = null;
							QuantificationLabel labelM = null;
							QuantificationLabel labelH = null;
							for (RatioDescriptor ratioDescriptor : ratioDescriptors) {
								final QuantificationLabel label1 = ratioDescriptor.getLabel1();
								if (label1.isLight()) {
									labelL = label1;
								}
								if (label1.isHeavy()) {
									labelH = label1;
								}
								if (!label1.isHeavy() && label1.isHeavy()) {
									labelM = label1;
								}
								final QuantificationLabel label2 = ratioDescriptor.getLabel2();
								if (label2.isLight()) {
									labelL = label2;
								}
								if (label2.isHeavy()) {
									labelH = label2;
								}
								if (!label2.isHeavy() && label2.isHeavy()) {
									labelM = label2;
								}
							}
							parser.addFile(file, labelsByConditionsByFileID.get(fileID), labelL, labelM, labelH);

						}
					}

					parser.addIonExclusions(censusIonExlusions);
					censusChroParsers.put(key, parser);
					return parser;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					log.warn(e.getMessage());
				}
			}
		}
		return null;

	}

	public CensusOutParser getCensusOutParser(Collection<String> fileIds) {
		Map<File, String> fileIDByFiles = new THashMap<File, String>();
		for (String fileId : fileIds) {
			if (remoteFiles.containsKey(fileId) && types.containsKey(fileId)) {
				final RemoteSSHFileReference remoteSSHServer = remoteFiles.get(fileId);
				if (types.get(fileId) == FormatType.CENSUS_OUT_TXT) {
					final File file = remoteSSHServer.getRemoteFile();
					if (file != null)
						fileIDByFiles.put(file, fileId);

				}
			}
		}
		if (!fileIDByFiles.isEmpty()) {
			final String key = getKey(fileIDByFiles.keySet());
			if (censusOutParsers.containsKey(key)) {
				return censusOutParsers.get(key);
			} else {
				try {
					CensusOutParser parser = new CensusOutParser();
					for (File file : fileIDByFiles.keySet()) {
						final String fileID = fileIDByFiles.get(file);
						final List<RatioDescriptor> ratioDescriptors = ratioDescriptorsByFile.get(fileID);
						if (ratioDescriptors == null) {
							// if there is no ratio descriptor for this file is
							// because maybe it is an excel file and the ratio
							// descriptor will be in the excel reader
							continue;
						}
						if (ratioDescriptors.size() == 1) {
							final RatioDescriptor ratioDescriptor = ratioDescriptors.get(0);
							parser.addFile(file, labelsByConditionsByFileID.get(fileID), ratioDescriptor.getLabel1(),
									ratioDescriptor.getLabel2());
						} else {
							QuantificationLabel labelL = null;
							QuantificationLabel labelM = null;
							QuantificationLabel labelH = null;
							for (RatioDescriptor ratioDescriptor : ratioDescriptors) {
								final QuantificationLabel label1 = ratioDescriptor.getLabel1();
								if (label1.isLight()) {
									labelL = label1;
								}
								if (label1.isHeavy()) {
									labelH = label1;
								}
								if (!label1.isHeavy() && !label1.isLight()) {
									labelM = label1;
								}
								final QuantificationLabel label2 = ratioDescriptor.getLabel2();
								if (label2.isLight()) {
									labelL = label2;
								}
								if (label2.isHeavy()) {
									labelH = label2;
								}
								if (!label2.isHeavy() && !label2.isLight()) {
									labelM = label2;
								}
							}
							if (labelL == null) {
								throw new IllegalArgumentException("If this experiment is a triple label experiment:\n"
										+ "Label LIGHT is not defined in a triple label experiment (reading file '"
										+ fileID
										+ "')\nEither you have to define the label LIGHT or you have to define LIGHT/MEDIUM and LIGHT/HEAVY ratios\n"
										+ "Otherwise, you may have selected the same input file for two different ratios.");
							}
							if (labelM == null) {
								throw new IllegalArgumentException("If this experiment is a triple label experiment:\n"
										+ "Label MEDIUM is not defined in a triple label experiment (reading file '"
										+ fileID
										+ "')\nEither you have to define the label MEDIUM or you have to define LIGHT/MEDIUM and MEDIUM/HEAVY ratios\n"
										+ "Otherwise, you may have selected the same input file for two different ratios.");
							}
							if (labelH == null) {
								throw new IllegalArgumentException("If this experiment is a triple label experiment:\n"
										+ "Label HEAVY is not defined in a triple label experiment (reading file '"
										+ fileID
										+ "')\nEither you have to define the label HEAVY or you have to define LIGHT/HEAVY and MEDIUM/HEAVY ratios\n"
										+ "Otherwise, you may have selected the same input file for two different ratios.");
							}
							parser.addFile(file, labelsByConditionsByFileID.get(fileID), labelL, labelM, labelH);
						}
					} // only return parser if it has at least one file to read
					if (!parser.getRemoteFileRetrievers().isEmpty()) {
						censusOutParsers.put(key, parser);
						return parser;
					}

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					log.warn(e.getMessage());
				}
			}
		}
		return null;
	}

	private String getKey(Collection<File> fileCollection) {
		List<File> list = new ArrayList<File>();
		list.addAll(fileCollection);
		// sort files by its name
		Collections.sort(list, new java.util.Comparator<File>() {
			@Override
			public int compare(File arg0, File arg1) {
				return arg0.getAbsolutePath().compareTo(arg1.getAbsolutePath());
			}
		});
		// get the append of all the paths and names
		String key = "";
		for (File file : list) {
			key += file.getAbsolutePath();
		}
		return key;
	}

	public DTASelectParser getDTASelectFilterParser(String fileId) {
		if (remoteFiles.containsKey(fileId) && types.containsKey(fileId)) {
			final RemoteSSHFileReference remoteSSHServer = remoteFiles.get(fileId);
			if (types.get(fileId) == FormatType.DTA_SELECT_FILTER_TXT) {
				try {
					DTASelectParser parser = new DTASelectParser(fileId, remoteSSHServer);
					return parser;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					log.warn(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					log.warn(e.getMessage());
				}
			}
		}
		return null;
	}

	public DTASelectParser getDTASelectFilterParser(Collection<String> fileIds) {
		List<File> files = new ArrayList<File>();
		for (String fileId : fileIds) {
			if (remoteFiles.containsKey(fileId) && types.containsKey(fileId)) {
				final RemoteSSHFileReference remoteSSHServer = remoteFiles.get(fileId);
				if (types.get(fileId) == FormatType.DTA_SELECT_FILTER_TXT) {
					final File file = remoteSSHServer.getRemoteFile();
					if (file != null)
						files.add(file);
				}
			}
		}
		if (!files.isEmpty()) {
			final String key = getKey(files);
			if (dtaSelectParsers.containsKey(key)) {
				return dtaSelectParsers.get(key);
			} else {
				try {
					DTASelectParser parser = new DTASelectParser(files);
					dtaSelectParsers.put(key, parser);
					return parser;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					log.warn(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
					log.warn(e.getMessage());
				}
			}
		}
		return null;
	}

	public CensusChroParser getCensusChroParser(List<FileReferenceType> fileRef) {
		if (fileRef != null) {
			for (FileReferenceType fileReferenceType : fileRef) {
				final CensusChroParser censusParser = getCensusChroParser(fileReferenceType.getFileRef());
				if (censusParser != null)
					return censusParser;
			}
		}
		return null;
	}

	public QuantParser getQuantParser(List<FileReferenceType> fileRef) {
		if (fileRef != null) {
			for (FileReferenceType fileReferenceType : fileRef) {
				final QuantParser quantParser = getQuantParser(fileReferenceType.getFileRef());
				if (quantParser != null)
					return quantParser;
			}
		}
		return null;
	}

	public CensusOutParser getCensusOutParser(List<FileReferenceType> fileRef) {
		if (fileRef != null) {
			for (FileReferenceType fileReferenceType : fileRef) {
				final CensusOutParser censusParser = getCensusOutParser(fileReferenceType.getFileRef());
				if (censusParser != null)
					return censusParser;
			}
		}
		return null;
	}

	public DTASelectParser getDTASelectFilterParser(List<FileReferenceType> fileRef) {
		if (fileRef != null) {
			for (FileReferenceType fileReferenceType : fileRef) {
				final DTASelectParser dtaSelectParser = getDTASelectFilterParser(fileReferenceType.getFileRef());
				if (dtaSelectParser != null)
					return dtaSelectParser;
			}
		}
		return null;
	}

	public DBIndexInterface getFastaDBIndex(RemoteInfoType remoteInfoType) {
		try {
			final RemoteSSHFileReference remoteFastaFile = getRemoteFastaFile(remoteInfoType);
			if (remoteFastaFile != null) {
				return getFastaDBIndex(getRemoteFastaFileId(remoteInfoType), remoteFastaFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return null;
	}

	public DBIndexInterface getFastaDBIndex(String fastaFileRef) {

		final RemoteSSHFileReference remoteFastaFile = getRemoteFastaFile(fastaFileRef);
		if (remoteFastaFile != null) {
			return getFastaDBIndex(fastaFileRef, remoteFastaFile);
		}

		return null;
	}

	private DBIndexInterface getFastaDBIndex(String fastaFileId, RemoteSSHFileReference remoteFastaFile) {
		try {
			log.info("Trying to get from cache the DBIndex referenced by " + fastaFileId);
			if (ImportCfgUtil.getDBIndexFromCache(fastaFileId) != null)
				return ImportCfgUtil.getDBIndexFromCache(fastaFileId);
			log.info("Not found in cache. TRying to build a new DBIndex referenced by " + fastaFileId);
			FastaDisgestionType fastaDigestionType = getFastaDigestionType(fastaFileId);
			File fastaFile = remoteFastaFile.getRemoteFile();
			if (fastaFile != null && fastaFile.exists()) {
				log.info("Building DBIndex from fasta file at: " + fastaFile.getAbsolutePath());
				final DBIndexSearchParams defaultDBIndexParams = DBIndexInterface.getDefaultDBIndexParams(fastaFile);
				if (fastaDigestionType != null) {
					((DBIndexSearchParamsImpl) defaultDBIndexParams).setEnzymeArr(
							fastaDigestionType.getCleavageAAs().toCharArray(), fastaDigestionType.getMisscleavages(),
							false);
					((DBIndexSearchParamsImpl) defaultDBIndexParams)
							.setEnzymeOffset(fastaDigestionType.getEnzymeOffset());
					((DBIndexSearchParamsImpl) defaultDBIndexParams)
							.setMaxMissedCleavages(fastaDigestionType.getMisscleavages());
					((DBIndexSearchParamsImpl) defaultDBIndexParams)
							.setEnzymeNocutResidues(fastaDigestionType.getEnzymeNoCutResidues());
					((DBIndexSearchParamsImpl) defaultDBIndexParams)
							.setH2OPlusProtonAdded(fastaDigestionType.isIsH2OPlusProtonAdded());
				}
				DBIndexInterface ret = new DBIndexInterface(defaultDBIndexParams);
				ImportCfgUtil.saveDBIndexInCache(fastaFileId, ret);
				return ret;
			} else {
				log.warn("Fasta file not found at: " + remoteFastaFile.getHostName() + " path:"
						+ remoteFastaFile.getRemotePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return null;
	}

	private FastaDisgestionType getFastaDigestionType(String fastaFileId) {
		return fastaDigestionByFileId.get(fastaFileId);
	}

	public File getFastaFile(String fileRef) {

		final RemoteSSHFileReference remoteFastaFile = getRemoteFastaFile(fileRef);
		if (remoteFastaFile != null) {
			return remoteFastaFile.getOutputFile();
		}

		return null;
	}

	private RemoteSSHFileReference getRemoteFastaFile(String fileRef) {

		if (fileRef != null) {

			if (types.get(fileRef) == FormatType.FASTA) {
				return remoteFiles.get(fileRef);
			}

		}

		return null;
	}

	private RemoteSSHFileReference getRemoteFastaFile(RemoteInfoType remoteInfoType) {
		if (remoteInfoType != null) {
			for (FileReferenceType fileRef : remoteInfoType.getFileRef()) {
				final RemoteSSHFileReference remoteFastaFile = getRemoteFastaFile(fileRef.getFileRef());
				if (remoteFastaFile != null)
					return remoteFastaFile;

			}
		}
		return null;
	}

	private String getRemoteFastaFileId(RemoteInfoType remoteInfoType) {
		if (remoteInfoType != null) {
			for (FileReferenceType fileRef : remoteInfoType.getFileRef()) {
				final RemoteSSHFileReference remoteFastaFile = getRemoteFastaFile(fileRef.getFileRef());
				if (remoteFastaFile != null)
					return fileRef.getFileRef();

			}
		}
		return null;
	}

	/**
	 * Gets either a census out parser or a census chro parser referred by the
	 * fileRef
	 *
	 * @param fileRef
	 * @return
	 */
	public QuantParser getQuantParser(String fileRef) {
		final CensusChroParser censusChroParser = getCensusChroParser(fileRef);
		if (censusChroParser != null) {
			return censusChroParser;
		}
		final CensusOutParser censusOutParser = getCensusOutParser(fileRef);
		if (censusOutParser != null) {
			return censusOutParser;
		}
		return null;
	}

}
