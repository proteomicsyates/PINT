package edu.scripps.yates.utilities.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

public class FileUtils {

	public static void mergeFiles(Collection<File> files, File mergedFile, boolean skipHeaderOfNotFirstFiles) {
		File[] fileArray = new File[files.size()];
		int i = 0;
		for (File file : files) {
			fileArray[i] = file;
			i++;
		}
		mergeFiles(fileArray, mergedFile, skipHeaderOfNotFirstFiles);
	}

	public static void mergeFiles(File[] files, File mergedFile, boolean skipHeaderOfNotFirstFiles) {
		// if the file already exists, delete it
		if (mergedFile.exists())
			mergedFile.delete();
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			fstream = new FileWriter(mergedFile, true);
			out = new BufferedWriter(fstream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		boolean firstFile = true;
		for (File f : files) {
			System.out.println("merging: " + f.getName());
			FileInputStream fis;
			try {
				fis = new FileInputStream(f);
				BufferedReader in = new BufferedReader(new InputStreamReader(fis));

				String aLine;
				boolean firstLine = true;
				while ((aLine = in.readLine()) != null) {
					if (skipHeaderOfNotFirstFiles && (!firstFile && firstLine)) {
						firstLine = false;
						continue;
					}

					out.write(aLine);
					out.newLine();

				}
				firstLine = false;
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			firstFile = false;
		}

		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Delete a folder if it is empty. if recursivelly is true, also delete the
	 * parent folders if they are empty.
	 *
	 * @param folder
	 * @param recursivelly
	 */
	public static void removeFolderIfEmtpy(File folder, boolean recursivelly) {
		if (folder == null || !folder.isDirectory())
			return;
		if (folder.exists() && folder.list().length == 0) {
			folder.delete();

			if (recursivelly) {
				removeFolderIfEmtpy(folder.getParentFile(), recursivelly);
			}
		}
	}

	/**
	 * By default File#delete fails for non-empty directories, it works like
	 * "rm". We need something a little more brutual - this does the equivalent
	 * of "rm -r"
	 *
	 * @param path
	 *            Root File Path
	 * @return true iff the file and all sub files/directories have been removed
	 * @throws FileNotFoundException
	 */
	public static boolean deleteFolderRecursive(File path) throws FileNotFoundException {
		if (!path.exists())
			throw new FileNotFoundException(path.getAbsolutePath());
		boolean ret = true;
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				ret = ret && FileUtils.deleteFolderRecursive(f);
			}
		}
		return ret && path.delete();
	}
}
