/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package edu.scripps.yates.dbindex.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.scripps.yates.dbindex.ProteinCache;

/**
 * 
 * @author Harshil
 */

public class FastaDefReader {
	private static BufferedReader br;
	private static int sizeofDefs = 40;
	public ProteinCache protCache;

	private final List defs = new ArrayList();

	public void setDefs(String fileName) {
		protCache = new ProteinCache();
		try {
			File f = new File(fileName);
			br = new BufferedReader(new FileReader(f));
			String line;
			int i = 0;
			line = br.readLine();

			while (line != null) {

				if (line.startsWith(">")) {
					// System.out.println(line);
					defs.add(line.substring(1, sizeofDefs + 1));
					protCache.addProtein((String) defs.get(i));
					i++;
				}
				line = br.readLine();
			}
			br.close();

		} catch (FileNotFoundException ex) {
			Logger.getLogger(FastaDefReader.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IOException ex) {
			Logger.getLogger(FastaDefReader.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	public List getDefs() {
		return defs;

	}

	public void setProteinCache() {
		// ProteinCache protCache = ProteinCache.getInstance();
		// protCache.addProtein();

	}

	public ProteinCache getproteinCache() {
		return protCache;
	}

	public static void main(String args[]) throws FileNotFoundException,
			IOException {
		File f = new File(
				"C:\\Users\\Harshil\\Documents\\NetBeansProjects\\blaz_data\\small.fasta");
		br = new BufferedReader(new FileReader(f));
		String line;
		line = br.readLine();
		ArrayList defs = new ArrayList();
		while (line != null) {
			if (line.startsWith(">")) {
				System.out.println(line);
				defs.add(line.substring(1, sizeofDefs + 1));

			}
			line = br.readLine();
		}
		br.close();
		System.out.println(defs);
		System.out.println(defs.get(1).toString().length());
	}
}
