package edu.scripps.yates.excel.proteindb.importcfg.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import edu.scripps.yates.dbindex.DBIndexInterface;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ExperimentalDesignType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileReferenceType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FileType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.FormatType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.IdDescriptionType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.LabelType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.MsRunsType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.OrganismSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteFilesRatioType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.RemoteInfoType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleSetType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.SampleType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServerType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.ServersType;
import edu.scripps.yates.excel.proteindb.importcfg.jaxb.TissueSetType;
import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;
import gnu.trove.map.hash.THashMap;

public class ImportCfgUtil {
	private final static Logger log = Logger.getLogger(ImportCfgUtil.class);
	private static final Map<String, DBIndexInterface> dbIndexes = new THashMap<String, DBIndexInterface>();

	public static SampleType getSampleCfg(String sampleRef, SampleSetType sampleSetType) {
		if (sampleSetType != null) {
			final List<SampleType> sample = sampleSetType.getSample();
			for (SampleType sampleType : sample) {
				if (sampleType.getId().equals(sampleRef))
					return sampleType;
			}
		}
		return null;
	}

	public static ServerType getReferencedServer(String serverRef, ServersType servers) {
		if (servers != null) {
			for (ServerType serverCfg : servers.getServer()) {
				if (serverCfg.getId().equals(serverRef))
					return serverCfg;
			}

		}
		return null;
	}

	public static FileType getReferencedFile(String fileRef, FileSetType fileSetCfg) {
		if (fileSetCfg != null) {
			for (FileType fileCfg : fileSetCfg.getFile()) {
				if (fileCfg.getId().equals(fileRef))
					return fileCfg;
			}
		}
		return null;
	}

	// public static MsRunType getMSRun(String msRunRefString, MsRunsType
	// msRunsCfg) {
	// if (msRunsCfg != null) {
	// for (MsRunType msRunCfg : msRunsCfg.getMsRun()) {
	// if (msRunCfg.getId().equals(msRunRefString)) {
	//
	// return msRunCfg;
	// }
	// }
	// }
	// return null;
	//
	// }

	public static List<MsRunType> getMSRuns(String msRunRefString, MsRunsType msRunsCfg, String separator) {
		List<MsRunType> ret = new ArrayList<MsRunType>();
		if (msRunsCfg != null) {
			for (MsRunType msRunCfg : msRunsCfg.getMsRun()) {
				List<String> msRunIDs = new ArrayList<String>();
				if (msRunRefString.contains(separator)) {
					final String[] split = msRunRefString.split(separator);
					for (String string : split) {
						msRunIDs.add(string);
					}
				} else {
					msRunIDs.add(msRunRefString);
				}
				for (String msRunID : msRunIDs) {
					if (msRunID.equals(msRunCfg.getId())) {
						ret.add(msRunCfg);
					}
				}
			}
		}
		return ret;

	}

	public static List<RemoteSSHFileReference> getRemoteFiles(RemoteInfoType remoteInfoType, FormatType format,
			FileSetType fileSetCfg, ServersType servers) throws IOException {
		List<RemoteSSHFileReference> ret = new ArrayList<RemoteSSHFileReference>();
		for (FileReferenceType referenceFile : remoteInfoType.getFileRef()) {
			FileType fileCfg = ImportCfgUtil.getReferencedFile(referenceFile.getFileRef(), fileSetCfg);
			if (fileCfg != null) {
				if (fileCfg.getFormat() == format) {
					RemoteSSHFileReference remoteSSHServer = null;
					if (fileCfg.getServerRef() != null) {
						ServerType server = ImportCfgUtil.getReferencedServer(fileCfg.getServerRef(), servers);
						remoteSSHServer = new RemoteSSHFileReference(server.getHostName(), server.getUserName(),
								server.getPassword(), fileCfg.getName(),
								File.createTempFile(getPrefix(fileCfg.getName()), null));
						remoteSSHServer.setRemotePath(fileCfg.getRelativePath());
						ret.add(remoteSSHServer);
					}
				}
			}
		}
		return ret;
	}

	public static List<RemoteSSHFileReference> getRemoteFiles(RemoteFilesRatioType remoteFilesRatioType,
			FormatType format, FileSetType fileSetCfg, ServersType servers) throws IOException {
		List<RemoteSSHFileReference> ret = new ArrayList<RemoteSSHFileReference>();
		FileType fileCfg = ImportCfgUtil.getReferencedFile(remoteFilesRatioType.getFileRef(), fileSetCfg);
		if (fileCfg != null) {
			if (fileCfg.getFormat() == format) {
				RemoteSSHFileReference remoteSSHServer = null;
				if (fileCfg.getServerRef() != null) {
					ServerType server = ImportCfgUtil.getReferencedServer(fileCfg.getServerRef(), servers);
					remoteSSHServer = new RemoteSSHFileReference(server.getHostName(), server.getUserName(),
							server.getPassword(), fileCfg.getName(),
							File.createTempFile(getPrefix(fileCfg.getName()), null));
					remoteSSHServer.setRemotePath(fileCfg.getRelativePath());
					ret.add(remoteSSHServer);
				}
			}
		}

		return ret;
	}

	/**
	 * Due to the prefix that is required to create a temp file has to be at
	 * least 3 characters longer, this function will add some extra "_" if
	 * needed
	 *
	 * @param id
	 * @return
	 */
	public static String getPrefix(String id) {
		if (id == null) {
			return "___";
		}
		String ret = id;
		while (ret.length() < 3) {
			ret += "_";
		}
		return ret;
	}

	public static List<FileType> getFilesCfg(RemoteInfoType remoteInfoType, FormatType format, FileSetType fileSetCfg)
			throws IOException {
		List<FileType> ret = new ArrayList<FileType>();
		for (FileReferenceType referenceFile : remoteInfoType.getFileRef()) {
			FileType fileCfg = ImportCfgUtil.getReferencedFile(referenceFile.getFileRef(), fileSetCfg);
			if (fileCfg != null) {
				if (fileCfg.getFormat() == format) {
					ret.add(fileCfg);
				}
			}
		}
		return ret;
	}

	/**
	 * Gets a {@link DBIndexInterface} from a {@link FileType}. If the
	 * {@link FileType} is a remote file, get it from remote location. Then,
	 * store it in the dbIndex location. Otherwise
	 *
	 * @param fileCfg
	 * @return
	 */
	public static DBIndexInterface getDBIndex(FileType fileCfg, ServersType servers) {
		try {
			File fastaFile = new File(DBIndexInterface.getDBIndexPath() + File.separator + fileCfg.getName());

			if (fileCfg.getServerRef() != null) {
				ServerType serverCfg = ImportCfgUtil.getReferencedServer(fileCfg.getServerRef(), servers);
				log.debug("Getting fasta file from " + serverCfg.getHostName() + " to " + fastaFile.getAbsolutePath());

				// remote file
				RemoteSSHFileReference remoteFile = new RemoteSSHFileReference(serverCfg.getHostName(),
						serverCfg.getUserName(), serverCfg.getPassword(), fileCfg.getName(), fastaFile);
				fastaFile = remoteFile.getOutputFile();
			} else {
				final String urlString = fileCfg.getUrl();
				log.debug("Copying file from " + urlString.toString() + " to " + fastaFile.getAbsolutePath());
				final URL url = new URL(urlString).toURI().toURL();
				FileUtils.copyURLToFile(url, fastaFile);
			}
			if (fastaFile.exists()) {
				log.debug("Creating DBIndex on " + DBIndexInterface.getDBIndexPath());
				DBIndexInterface ret = new DBIndexInterface(DBIndexInterface.getDefaultDBIndexParams(fastaFile));
				// store in cache
				ImportCfgUtil.dbIndexes.put(fileCfg.getName(), ret);
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		throw new IllegalArgumentException(
				"An error occurred when building dbindex from referenced fasta file " + fileCfg.getId());
	}

	public static DBIndexInterface getDBIndexFromCache(String dbName) {
		return dbIndexes.get(dbName);
	}

	public static void saveDBIndexInCache(String fileId, DBIndexInterface dbIndex) {
		dbIndexes.put(fileId, dbIndex);
	}

	public static void clearDBIndexes() {
		dbIndexes.clear();
	}

	public static SampleType getReferencedSample(String sampleRef, ExperimentalDesignType experimentalDesign) {
		if (experimentalDesign != null) {
			return getReferencedSample(sampleRef, experimentalDesign.getSampleSet());

		}
		return null;
	}

	public static SampleType getReferencedSample(String sampleRef, SampleSetType sampleSet) {
		if (sampleSet != null) {
			for (SampleType sampleType : sampleSet.getSample()) {
				if (sampleType.getId().equals(sampleRef))
					return sampleType;
			}
		}
		return null;
	}

	public static LabelType getReferencedLabel(String labelRef, ExperimentalDesignType experimentalDesign) {
		if (experimentalDesign != null) {
			return getReferencedLabel(labelRef, experimentalDesign.getLabelSet());
		}
		return null;
	}

	private static LabelType getReferencedLabel(String labelRef, LabelSetType labelSet) {
		if (labelSet != null) {
			for (LabelType labelType : labelSet.getLabel()) {
				if (labelType.getId().equals(labelRef))
					return labelType;
			}
		}
		return null;
	}

	public static IdDescriptionType getReferencedOrganism(String organismRef, OrganismSetType organismSetType) {
		if (organismSetType != null) {
			for (IdDescriptionType organismType : organismSetType.getOrganism()) {
				if (organismType.getId().equals(organismRef))
					return organismType;
			}
		}
		return null;
	}

	public static IdDescriptionType getReferencedTissue(String tissueRef, TissueSetType tissueSetType) {
		if (tissueSetType != null) {
			for (IdDescriptionType tissueType : tissueSetType.getTissue()) {
				if (tissueType.getId().equals(tissueRef))
					return tissueType;
			}
		}
		return null;
	}
}
