package edu.scripps.yates.annotations.uniprot;

import java.util.ArrayList;
import java.util.List;

import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.EvidenceType;

public class PTMInformation {
	private final String modificationName;
	private double massShiftAverage;
	private double massShiftMono;
	private final int position;
	private final String proteinAcc;
	private final String proteinDescription;
	private final List<EvidenceType> evidences = new ArrayList<EvidenceType>();
	private final Entry entry;

	public PTMInformation(String proteinAcc, String modificationName, String proteinDescription, int position,
			Entry entry) {
		this.modificationName = modificationName;
		this.proteinAcc = proteinAcc;
		this.proteinDescription = proteinDescription;
		if (this.modificationName != null) {
			final UniprotPTMCVTerm ptmsByID = UniprotPTMCVReader.getInstance().getPtmsByID(modificationName);
			if (ptmsByID != null) {
				try {
					final String singleValue = ptmsByID.getSingleValue(UniprotCVTermCode.MA);
					if (singleValue != null) {
						double massShiftAverage = Double.parseDouble(singleValue);
						setMassShiftAverage(massShiftAverage);
					}
				} catch (NumberFormatException e) {

				}
				try {
					final String singleValue = ptmsByID.getSingleValue(UniprotCVTermCode.MM);
					if (singleValue != null) {
						double massShiftMono = Double.parseDouble(singleValue);
						setMassShiftMono(massShiftMono);
					}
				} catch (NumberFormatException e) {

				}
			}
		}
		this.position = position;
		this.entry = entry;
	}

	/**
	 * @return the massShift
	 */
	public double getMassShiftAverage() {
		return massShiftAverage;
	}

	/**
	 * @param massShift
	 *            the massShift to set
	 */
	public void setMassShiftAverage(double massShiftAverage) {
		this.massShiftAverage = massShiftAverage;
	}

	/**
	 * @return the modificationName
	 */
	public String getModificationName() {
		return modificationName;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return the proteinAcc
	 */
	public String getProteinAcc() {
		return proteinAcc;
	}

	/**
	 * @return the proteinDescription
	 */
	public String getProteinDescription() {
		return proteinDescription;
	}

	public void addEvidence(EvidenceType evidenceType) {
		evidences.add(evidenceType);
	}

	/**
	 * @return the evidences
	 */
	public List<EvidenceType> getEvidences() {
		return evidences;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PTM in protein " + proteinAcc + "(" + proteinDescription + ")\n" + "\tMod name: " + modificationName
				+ "\n\tPosition: " + position + "\n\tMono mass shift:" + getMassShiftMono() + "\n\tAvg mass shift: "
				+ getMassShiftAverage() + "\n\tEvidences=" + getEvidencesString();
	}

	private String getEvidencesString() {
		StringBuilder sb = new StringBuilder();
		for (EvidenceType evidenceType : evidences) {
			if (evidenceType.getSource() != null && evidenceType.getSource().getDbReference() != null) {
				if (!"".equals(sb.toString()))
					sb.append("\t");
				if (evidenceType.getSource().getDbReference().getType() != null) {
					sb.append(evidenceType.getSource().getDbReference().getType());
				}
				if (evidenceType.getSource().getDbReference().getId() != null) {
					sb.append(":" + evidenceType.getSource().getDbReference().getId());
				}
			}

		}
		return sb.toString();
	}

	/**
	 * @return the entry
	 */
	public Entry getEntry() {
		return entry;
	}

	/**
	 * @return the massShiftMono
	 */
	public double getMassShiftMono() {
		return massShiftMono;
	}

	/**
	 * @param massShiftMono
	 *            the massShiftMono to set
	 */
	public void setMassShiftMono(double massShiftMono) {
		this.massShiftMono = massShiftMono;
	}
}
