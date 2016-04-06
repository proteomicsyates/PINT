package edu.scripps.yates.census.read.util;

import java.util.HashSet;
import java.util.Set;

import edu.scripps.yates.census.read.model.IsoRatio;
import edu.scripps.yates.census.read.model.IsobaricQuantifiedProtein;
import edu.scripps.yates.census.read.model.QuantifiedProteinFromCensusOut;
import edu.scripps.yates.census.read.model.interfaces.Ratio;
import edu.scripps.yates.utilities.grouping.GroupableProtein;
import edu.scripps.yates.utilities.grouping.ProteinGroup;

public class CensusChroUtil {

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if at least one
	 * {@link IsobaricQuantifiedProtein} in the group contains any
	 * {@link QuantificationLabel} as the parameter. if the proteins in the
	 * {@link ProteinGroup}are not {@link IsobaricQuantifiedProtein}, it will
	 * return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnySingletonIon(ProteinGroup proteinGroup, QuantificationLabel label) {
		if (proteinGroup != null && label != null) {
			for (GroupableProtein groupableProtein : proteinGroup) {
				if (groupableProtein instanceof IsobaricQuantifiedProtein) {
					IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
					if (quantProtein.containsAnySingletonIon(label))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if at least one
	 * {@link IsobaricQuantifiedProtein} in the group contains any
	 * {@link QuantificationLabel} DIFFERENT then the provided as the parameter.
	 * if the proteins in the {@link ProteinGroup}are not
	 * {@link IsobaricQuantifiedProtein}, it will return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnySingletonIonWithAnyOtherLabelThan(ProteinGroup proteinGroup,
			QuantificationLabel label) {
		if (proteinGroup != null && label != null) {
			final QuantificationLabel[] labels = QuantificationLabel.values();
			for (QuantificationLabel otherLabel : labels) {
				if (label != otherLabel) {
					for (GroupableProtein groupableProtein : proteinGroup) {
						if (groupableProtein instanceof IsobaricQuantifiedProtein) {
							IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
							if (quantProtein.containsAnySingletonIon(otherLabel))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if at least one
	 * {@link IsobaricQuantifiedProtein} in the group contains any
	 * {@link QuantificationLabel} as the parameter. if the proteins in the
	 * {@link ProteinGroup}are not {@link IsobaricQuantifiedProtein}, it will
	 * return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnyIon(ProteinGroup proteinGroup, QuantificationLabel label) {
		if (proteinGroup != null && label != null) {
			for (GroupableProtein groupableProtein : proteinGroup) {
				if (groupableProtein instanceof IsobaricQuantifiedProtein) {
					IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
					if (quantProtein.containsAnyIon(label))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if at least one
	 * {@link IsobaricQuantifiedProtein} in the group contains any
	 * {@link QuantificationLabel} DIFFERENT then the provided as the parameter.
	 * if the proteins in the {@link ProteinGroup}are not
	 * {@link IsobaricQuantifiedProtein}, it will return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnyIonWithAnyOtherLabelThan(ProteinGroup proteinGroup, QuantificationLabel label) {
		if (proteinGroup != null && label != null) {
			final QuantificationLabel[] labels = QuantificationLabel.values();
			for (QuantificationLabel otherLabel : labels) {
				if (label != otherLabel) {
					for (GroupableProtein groupableProtein : proteinGroup) {
						if (groupableProtein instanceof IsobaricQuantifiedProtein) {
							IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
							if (quantProtein.containsAnyIon(otherLabel))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if the
	 * {@link IsobaricQuantifiedProtein} contains any
	 * {@link QuantificationLabel} as the parameter. if the proteins in the
	 * {@link ProteinGroup}are not {@link IsobaricQuantifiedProtein}, it will
	 * return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnySingletonIon(GroupableProtein groupableProtein, QuantificationLabel label) {
		if (groupableProtein != null && label != null) {

			if (groupableProtein instanceof IsobaricQuantifiedProtein) {
				IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
				if (quantProtein.containsAnySingletonIon(label))
					return true;
			}

		}
		return false;
	}

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if the
	 * {@link IsobaricQuantifiedProtein} contains any
	 * {@link QuantificationLabel} DIFFERENT then the provided as the parameter.
	 * if the proteins in the {@link ProteinGroup}are not
	 * {@link IsobaricQuantifiedProtein}, it will return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnySingletonIonWithAnyOtherLabelThan(GroupableProtein groupableProtein,
			QuantificationLabel label) {
		if (groupableProtein != null && label != null) {
			final QuantificationLabel[] labels = QuantificationLabel.values();
			for (QuantificationLabel otherLabel : labels) {
				if (label != otherLabel) {

					if (groupableProtein instanceof IsobaricQuantifiedProtein) {
						IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
						if (quantProtein.containsAnySingletonIon(otherLabel))
							return true;
					}

				}
			}
		}
		return false;
	}

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if the
	 * {@link IsobaricQuantifiedProtein} contains any
	 * {@link QuantificationLabel} as the parameter. if the proteins in the
	 * {@link ProteinGroup}are not {@link IsobaricQuantifiedProtein}, it will
	 * return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnyIon(GroupableProtein groupableProtein, QuantificationLabel label) {
		if (groupableProtein != null && label != null) {

			if (groupableProtein instanceof IsobaricQuantifiedProtein) {
				IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
				if (quantProtein.containsAnyIon(label))
					return true;
			}

		}
		return false;
	}

	/**
	 * In case that the {@link ProteinGroup} contains
	 * {@link IsobaricQuantifiedProtein}, returns true if the
	 * {@link IsobaricQuantifiedProtein} contains any
	 * {@link QuantificationLabel} DIFFERENT then the provided as the parameter.
	 * if the proteins in the {@link ProteinGroup}are not
	 * {@link IsobaricQuantifiedProtein}, it will return false.
	 *
	 * @param proteinGroup
	 * @param label
	 * @return
	 */
	public static boolean containsAnyIonWithAnyOtherLabelThan(GroupableProtein groupableProtein,
			QuantificationLabel label) {
		if (groupableProtein != null && label != null) {
			final QuantificationLabel[] labels = QuantificationLabel.values();
			for (QuantificationLabel otherLabel : labels) {
				if (label != otherLabel) {

					if (groupableProtein instanceof IsobaricQuantifiedProtein) {
						IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) groupableProtein;
						if (quantProtein.containsAnyIon(otherLabel))
							return true;
					}

				}
			}
		}
		return false;
	}

	/**
	 * Gets the a {@link Set} of {@link IsoRatio} from a {@link ProteinGroup} if
	 * the {@link GroupableProtein} composing the {@link ProteinGroup} are
	 * instances of {@link IsobaricQuantifiedProtein}
	 *
	 * @param proteinGroup
	 * @return
	 */
	public static Set<Ratio> getRatiosFromProteinGroup(ProteinGroup proteinGroup) {
		Set<Ratio> ret = new HashSet<Ratio>();
		for (GroupableProtein protein : proteinGroup) {
			if (protein instanceof IsobaricQuantifiedProtein) {
				IsobaricQuantifiedProtein quantProtein = (IsobaricQuantifiedProtein) protein;
				ret.addAll(quantProtein.getRatios());
			} else if (protein instanceof QuantifiedProteinFromCensusOut) {
				QuantifiedProteinFromCensusOut quantProtein = (QuantifiedProteinFromCensusOut) protein;
				ret.addAll(quantProtein.getRatios());
			}
		}
		return ret;
	}

}
