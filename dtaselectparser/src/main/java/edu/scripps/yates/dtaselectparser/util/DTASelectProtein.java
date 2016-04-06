package edu.scripps.yates.dtaselectparser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import edu.scripps.yates.dbindex.IndexedProtein;

public class DTASelectProtein {
	private static final Logger log = Logger.getLogger(DTASelectProtein.class);
	private static final String ID = "Locus";
	private static final String SP_COUNT = "Spectrum Count";
	private static final String COVERAGE = "Sequence Coverage";
	private static final String LENGTH = "Length";
	private static final String MW = "MolWt";
	private static final String PI = "pI";
	private static final String DESCRIPTION = "Descriptive Name";
	private static final String NSAF = "NSAF";
	private static final String EMPAI = "EMPAI";
	private final String id;
	private final Integer spcount;
	private final Integer length;
	private final double coverage;
	private final double pi;
	private final String description;
	private final double mw;
	private final List<DTASelectPSM> psms = new ArrayList<DTASelectPSM>();
	private final double nsaf_norm;
	private final Double nsaf;
	private final Double empai;
	private final List<DTASelectProteinGroup> proteinGroups = new ArrayList<DTASelectProteinGroup>();
	private String searchEngine;

	public DTASelectProtein(String lineToParse,
			HashMap<String, Integer> positions) {
		final String[] elements = lineToParse.split("\t");
		id = elements[positions.get(ID)];
		spcount = Integer.parseInt(elements[positions.get(SP_COUNT)]);
		coverage = Double.parseDouble(elements[positions.get(COVERAGE)]
				.replace("%", ""));

		length = Integer.parseInt(elements[positions.get(LENGTH)]);
		mw = Double.parseDouble(elements[positions.get(MW)]);
		pi = Double.valueOf(elements[positions.get(PI)]);
		description = elements[positions.get(DESCRIPTION)];

		nsaf_norm = Double.valueOf(spcount) / Double.valueOf(length);

		if (positions.containsKey(NSAF))
			nsaf = Double.valueOf(elements[positions.get(NSAF)]);
		else
			nsaf = null;
		if (positions.containsKey(EMPAI))
			empai = Double.parseDouble(elements[positions.get(EMPAI)]);
		else
			empai = null;
	}

	public DTASelectProtein(IndexedProtein indexedProtein) {
		id = indexedProtein.getAccession();
		description = indexedProtein.getFastaDefLine();
		spcount = null;
		coverage = -1;
		length = null;
		mw = -1;
		pi = -1;
		nsaf_norm = -1;
		nsaf = null;
		empai = null;
	}

	/**
	 * SPC / LENGTH
	 * 
	 * @return
	 */
	public double getRatio() {
		if (spcount != null && length != null)
			return spcount / length;
		return Double.NaN;
	}

	public Integer getLength() {
		return length;
	}

	public Integer getSpectrumCount() {
		return spcount;
	}

	/**
	 * Gets the empai number calculated as ((Math.pow(10, coverage / 100)) - 1)
	 * 
	 * @return
	 */
	public double getEmpaiCov() {
		return ((Math.pow(10, coverage / 100)) - 1);
	}

	/**
	 * Gets the empai number as reported in the DTASelect
	 * 
	 * @return
	 */
	public Double getEmpai() {
		return empai;
	}

	public double getCoverage() {
		return coverage;
	}

	public List<DTASelectPSM> getPSMs() {
		return psms;
	}

	public void addPSM(DTASelectPSM psm) {
		psms.add(psm);

	}

	/**
	 * @return the id
	 */
	public String getLocus() {
		return id;
	}

	/**
	 * @return the pi
	 */
	public double getPi() {
		return pi;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the mw
	 */
	public double getMw() {
		return mw;
	}

	/**
	 * @return the nsaf_norm
	 */
	public double getNsaf_norm() {
		return nsaf_norm;
	}

	/**
	 * @return the nsaf
	 */
	public Double getNsaf() {
		return nsaf;
	}

	public void addProteinGroup(DTASelectProteinGroup dtaSelectProteinGroup) {
		proteinGroups.add(dtaSelectProteinGroup);

	}

	/**
	 * @return the proteinGroup
	 */
	public List<DTASelectProteinGroup> getProteinGroups() {
		return proteinGroups;
	}

	/**
	 * 
	 * @return
	 */
	public List<DTASelectProtein> getSibilingProteinsInGroup() {
		List<DTASelectProtein> ret = new ArrayList<DTASelectProtein>();
		for (DTASelectProteinGroup dtaSelectProteinGroup : proteinGroups) {
			for (DTASelectProtein dtaSelectProtein : dtaSelectProteinGroup) {
				if (!dtaSelectProtein.getLocus().equals(getLocus()))
					ret.add(dtaSelectProtein);
			}
		}
		// if (!ret.isEmpty())
		// log.info("Protein with " + ret.size() + " other proteins in "
		// + proteinGroups.size() + " groups");
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getLocus();
	}

	public void setSearchEngine(String searchEngine) {
		this.searchEngine = searchEngine;
	}

	/**
	 * @return the searchEngine
	 */
	public String getSearchEngine() {
		return searchEngine;
	}

}
