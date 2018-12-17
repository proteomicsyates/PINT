package edu.scripps.yates.annotations.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.scripps.yates.utilities.annotations.uniprot.xml.CommentType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.DbReferenceType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.Entry;
import edu.scripps.yates.utilities.annotations.uniprot.xml.FeatureType;
import edu.scripps.yates.utilities.annotations.uniprot.xml.PropertyType;
import gnu.trove.set.hash.THashSet;
import uk.ac.ebi.kraken.interfaces.go.GoCategory;
import uk.ac.ebi.kraken.interfaces.go.GoTerm;
import uk.ac.ebi.kraken.model.go.GoIdImpl;
import uk.ac.ebi.kraken.model.go.GoTermImpl;
import uk.ac.ebi.kraken.model.go.GoTermNameImpl;

public class UniprotEntryEBIUtil {
	/**
	 * Retrieves the GO ID terms of all the cellularLocations
	 * 
	 * @param entry
	 * @return
	 */
	public static Set<GoTerm> getGOTermsForCellularLocations(Entry entry) {
		return getGOTermsForCellularLocations(entry);
	}

	/**
	 * Retrieves the GO ID terms of all the cellularLocations that are from an
	 * specific source.<br>
	 * 
	 * 
	 * @param entry
	 * @param sources
	 *            any list of sources. Valid sources can be UniProtKB,
	 *            UniProtKB-KW, Ensembl, Reactome, UniProtKB-SubCell, HPA,
	 *            ProtInc, or NULL if you want any source to be considered.<br>
	 *            According to Uniprot webpage:
	 *            <ul>
	 *            <li>"UniprotKB" source is inferred from direct assay,</li>
	 *            <li>"UniprotKB-SubCell","UniprotKB-KW" and "Ensembl" are
	 *            inferred from electronic annotation,</li>
	 *            <li>"Reactome" is traceable from author statement.</li>
	 *            </ul>
	 * @return
	 */
	public static Set<GoTerm> getGOTermsForCellularLocations(Entry entry, String... sources) {
		final Set<GoTerm> ret = new THashSet<GoTerm>();
		if (entry != null) {
			if (entry.getDbReference() != null) {
				for (final DbReferenceType dbReference : entry.getDbReference()) {
					if (dbReference.getType() != null && "GO".equals(dbReference.getType())) {
						final List<PropertyType> properties = dbReference.getProperty();
						if (properties != null) {
							final List<PropertyType> propertiesTerm = filterPropertiesByType(properties, "term");
							final List<PropertyType> propertiesProject = filterPropertiesByType(properties, "project");
							for (final PropertyType property : propertiesTerm) {
								if (property.getValue().startsWith("C:")) { // cellular
									// component
									final String termName = property.getValue().substring(2);
									if (sources == null) {
										final GoTerm goTerm = new GoTermImpl();
										final GoIdImpl goId = new GoIdImpl();
										goId.setValue(dbReference.getId());
										goTerm.setGoId(goId);
										goTerm.setGoCategory(GoCategory.C);
										final GoTermNameImpl goTermName = new GoTermNameImpl();
										goTermName.setValue(termName);
										goTerm.setGoTermName(goTermName);
										ret.add(goTerm);

									} else {
										boolean found = false;
										for (final String source : sources) {
											for (final PropertyType propertyProject : propertiesProject) {
												if (source.equals(propertyProject.getValue())) {
													found = true;
													break;
												}
											}
										}
										if (found) {
											final GoTerm goTerm = new GoTermImpl();
											final GoIdImpl goId = new GoIdImpl();
											goId.setValue(dbReference.getId());
											goTerm.setGoId(goId);
											goTerm.setGoCategory(GoCategory.C);
											final GoTermNameImpl goTermName = new GoTermNameImpl();
											goTermName.setValue(termName);
											goTerm.setGoTermName(goTermName);
											ret.add(goTerm);
										}
									}
								}

							}
						}
					}
				}
			}
		}
		return ret;
	}

	private static List<PropertyType> filterPropertiesByType(Collection<PropertyType> properties, String type) {
		final List<PropertyType> ret = new ArrayList<PropertyType>();
		for (final PropertyType propertyType : properties) {
			if (propertyType.getType().equals(type)) {
				ret.add(propertyType);
			}
		}
		return ret;
	}

	public static List<CommentType> getComments(Entry entry,
			uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType type) {
		final List<CommentType> ret = new ArrayList<CommentType>();
		final String typeString = type.toXmlDisplayName();
		if (entry != null) {
			if (entry.getComment() != null) {
				for (final CommentType comment : entry.getComment()) {
					if (comment.getType().equals(typeString)) {
						ret.add(comment);
					}
				}
			}
		}
		return ret;
	}

	public static List<FeatureType> getFeatures(Entry entry,
			uk.ac.ebi.kraken.interfaces.uniprot.features.FeatureType type) {
		final List<FeatureType> ret = new ArrayList<FeatureType>();
		final String typeString = type.getValue();
		if (entry != null) {
			if (entry.getFeature() != null) {
				for (final FeatureType feature : entry.getFeature()) {
					if (feature.getType().equals(typeString)) {
						ret.add(feature);
					}
				}
			}
		}
		return ret;
	}
}
