package edu.scripps.yates.utilities.remote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.ChannelSftp.LsEntrySelector;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * This class is used for getting remote file names by ssh
 *
 * @author Salva
 *
 */
public class RemoteSSHFileReference {
	public static final String CENSUS_CHRO_XML = "census_chro.xml";
	public static final String DTA_SELECT_FILTER_TXT = "DTASelect-filter.txt";
	private static final String defaultHostName = "shamu.scripps.edu";
	private static final String defaultUserName = "salvador";
	private static final String defaultPass = "Natjeija21";
	private static final String defaultRemoteFileName = DTA_SELECT_FILTER_TXT;
	private static final Logger log = Logger.getLogger(RemoteSSHFileReference.class);
	public static int failedConnectionAttemps = 0;
	public static final int MAX_CONNECTIONS_ATTEMPS = 5;
	private String hostName;
	private String userName;
	private String pass;
	private String remoteFileName;
	private File outputFile;
	private String remotePath;
	private boolean overrideIfExists = false;

	/**
	 * This instance of the remote server will be:<br>
	 * host name: shamu.scripps.edu<br>
	 * user name: salvador<br>
	 * default pass for salvador
	 */
	public RemoteSSHFileReference() {
		hostName = defaultHostName;
		userName = defaultUserName;
		pass = defaultPass;
		remoteFileName = defaultRemoteFileName;
	}

	/**
	 * Customized version of the server connection
	 *
	 * @param hostName
	 * @param userName
	 * @param pass
	 * @param remotefileName
	 */
	public RemoteSSHFileReference(String hostName, String userName, String pass, String remotefileName,
			File outputFile) {
		this.hostName = hostName;
		this.userName = userName;
		this.pass = pass;
		remoteFileName = remotefileName;
		this.outputFile = outputFile;
	}

	public RemoteSSHFileReference(File xmlFile) {
		outputFile = xmlFile;
	}

	// public static void main(String[] args) {
	// String extension = FilenameUtils.getExtension("DTASelect-filter.txt");
	// String name = FilenameUtils.getBaseName("DTASelect-filter.txt");
	// System.out.println(extension);
	// System.out.println(name);
	// }
	public void testConection() throws JSchException {
		Session session = null;
		JSch jsch = new JSch();

		log.debug("Getting " + remoteFileName + " file from remote location:");

		session = jsch.getSession(userName, hostName, 22);
		session.setConfig("StrictHostKeyChecking", "no");
		session.setPassword(pass);
		session.connect();
		Channel channel = session.openChannel("sftp");
		channel.connect();
		ChannelSftp sftpChannel = (ChannelSftp) channel;
		sftpChannel.exit();
		session.disconnect();
	}

	/**
	 * Gets a File from the remote server. The file should be located at a
	 * remote path stated by a call to setRemotePath()
	 *
	 * @return
	 */
	public File getRemoteFile() {

		return getRemoteFile(remotePath);
	}

	public File getRemoteFile(String remotePath) {
		if (outputFile != null && outputFile.exists() && outputFile.length() > 0
				&& ((remotePath != null && remotePath.equals(this.remotePath)) || remotePath == null)
				&& !overrideIfExists)
			return outputFile;
		if (remotePath == null)
			throw new IllegalArgumentException(
					"Remote path has not been stated. Call to setRemotePath() before to getRemoteFile()");
		this.remotePath = remotePath;
		JSch jsch = new JSch();
		Session session = null;
		try {
			log.debug("Getting " + remoteFileName + " file from remote location:");
			log.debug(remotePath);
			String extension = FilenameUtils.getExtension(remoteFileName);
			String fileName = FilenameUtils.getBaseName(remoteFileName);
			if (outputFile == null) {
				outputFile = File.createTempFile(fileName, extension);
				outputFile.deleteOnExit();
			}
			if (!outputFile.exists()) {
				final File parentFolder = outputFile.getParentFile();
				if (!parentFolder.exists()) {
					parentFolder.mkdirs();
				}
			}
			FileOutputStream fos = new FileOutputStream(outputFile);
			session = jsch.getSession(userName, hostName, 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(pass);
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;

			final StringTokenizer stringTokenizer = new StringTokenizer(remotePath, "/");
			boolean first = true;
			while (stringTokenizer.hasMoreTokens()) {
				String folder = stringTokenizer.nextToken();
				if (first) {
					folder = "/" + folder;
					first = false;
				}
				log.debug("cd " + folder);
				sftpChannel.cd(folder);
			}
			log.debug("Folder found. Now trying to retrieve the file: " + remoteFileName);
			sftpChannel.get(remoteFileName, fos);
			sftpChannel.exit();
			session.disconnect();
			failedConnectionAttemps = 0;
			log.debug("File retrieved at '" + outputFile.getAbsolutePath() + "' location ("
					+ getFileLengthString(outputFile) + ")");
			return outputFile;
		} catch (JSchException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			failedConnectionAttemps++;
		} catch (SftpException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			failedConnectionAttemps++;
		} catch (IOException e) {
			e.printStackTrace();
			log.warn(e.getMessage());
			failedConnectionAttemps++;
		}
		return null;
	}

	private String getFileLengthString(File file) {
		Long length = file.length();
		String ret = length + " bytes";
		if (length / 1024 > 1) {
			length = length / 1024;
			ret = length + " Kbytes";
			if (length / 1024 > 1) {
				length = length / 1024;
				ret = length + " Mbytes";
				if (length / 1024 > 1) {
					length = length / 1024;
					ret = length + " Gbytes";
				}
			}
		}
		return ret;
	}

	/**
	 * @return the hostName
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * @return the remoteFileName
	 */
	public String getRemoteFileName() {
		return remoteFileName;
	}

	/**
	 * @param remoteFileName
	 *            the remoteFileName to set
	 */
	public void setRemoteFileName(String remoteFileName) {
		this.remoteFileName = remoteFileName;
	}

	/**
	 * @return the outputFile
	 */
	public File getOutputFile() {
		return outputFile;
	}

	/**
	 * @param outputFile
	 *            the outputFile to set
	 */
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * @return the remotePath
	 */
	public String getRemotePath() {
		return remotePath;
	}

	/**
	 * @param remotePath
	 *            the remotePath to set
	 */
	public void setRemotePath(String remotePath) {
		this.remotePath = remotePath;
	}

	/**
	 * @param overrideIfExists
	 *            the overrideIfExists to set
	 */
	public void setOverrideIfExists(boolean overrideIfExists) {
		this.overrideIfExists = overrideIfExists;
	}

	/**
	 * Checks the login connection to a remote host using SFTP connection
	 *
	 * @param hostName
	 * @param userName
	 * @param password
	 * @return
	 */
	public static boolean loginCheck(String hostName, String userName, String password) {
		Session session = null;

		log.debug("Checking login access to " + hostName);
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(userName, hostName, 22);

			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;
			sftpChannel.exit();
			session.disconnect();
			log.debug("Login ok");
			return true;
		} catch (JSchException e) {
			log.debug("Error in login: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Check the availability of a file using a SSH connection.
	 *
	 * @return a string error message if the connection has failed for any
	 *         reason or null if everything is OK.
	 */
	public String isAvailable() {
		if (remoteFileName == null || "".equals(remoteFileName))
			return "Output file cannot be a folder. Please specify the file name";
		// if already exists, but overriding is not allowed
		if (outputFile != null && outputFile.exists() && outputFile.length() > 0 && !overrideIfExists)
			return null;
		if (remotePath == null)
			throw new IllegalArgumentException(
					"Remote path has not been stated. Call to setRemotePath() before to getRemoteFile()");
		JSch jsch = new JSch();
		Session session = null;
		try {
			log.debug("Checking " + remoteFileName + " file from remote location:");
			log.debug(remotePath);
			session = jsch.getSession(userName, hostName, 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(pass);
			session.connect();

			Channel channel = session.openChannel("sftp");
			channel.connect();
			ChannelSftp sftpChannel = (ChannelSftp) channel;

			sftpChannel.cd(remotePath);

			// sftpChannel.get(remoteFileName, fos);
			sftpChannel.ls(remoteFileName, new LsEntrySelector() {
				@Override
				public int select(LsEntry arg0) {
					if (arg0.getFilename().equals(remoteFileName))
						return 1;
					return 0;
				}
			});
			sftpChannel.exit();
			session.disconnect();
			failedConnectionAttemps = 0;
			log.debug("File available");
			return null;
		} catch (JSchException e) {
			log.warn(e.getMessage());
			failedConnectionAttemps++;
			return e.getMessage();
		} catch (SftpException e) {
			log.warn(e.getMessage());
			failedConnectionAttemps++;
			return e.getMessage();
		}

	}
}
