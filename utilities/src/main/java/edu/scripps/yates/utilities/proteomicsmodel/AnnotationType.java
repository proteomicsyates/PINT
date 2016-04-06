package edu.scripps.yates.utilities.proteomicsmodel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

public class AnnotationType {
	private static final Logger log = Logger.getLogger(AnnotationType.class);

	private static AnnotationType[] values = loadFromFile();

	private final String keyword;
	private final Set<UniprotLineHeader> ulhs = new HashSet<UniprotLineHeader>();
	private final static String UNIPROT_ANNOTATIONS_SEPARATOR = "-!-";
	public static final AnnotationType uniprotKeyword = AnnotationType.translateStringToAnnotationType("KEYWORD");

	public static final AnnotationType GO = AnnotationType.translateStringToAnnotationType("GO");

	public static final AnnotationType strain = AnnotationType.translateStringToAnnotationType("STRAIN");
	public static final AnnotationType transposon = AnnotationType.translateStringToAnnotationType("TRANSPOSON");
	public static final AnnotationType tissue = AnnotationType.translateStringToAnnotationType("TISSUE");
	public static final AnnotationType status = AnnotationType.translateStringToAnnotationType("STATUS");
	public static final AnnotationType plasmid = AnnotationType.translateStringToAnnotationType("PLASMID");
	public static final AnnotationType catalytic_activity = AnnotationType
			.translateStringToAnnotationType("CATALYTIC ACTIVITY");
	public static final AnnotationType function = AnnotationType.translateStringToAnnotationType("FUNCTION");
	public static final AnnotationType domain = AnnotationType.translateStringToAnnotationType("DOMAIN");
	public static final AnnotationType pathway = AnnotationType.translateStringToAnnotationType("PATHWAY");
	public static final AnnotationType subunit = AnnotationType.translateStringToAnnotationType("SUBUNIT");

	public static final AnnotationType subcellular_location = AnnotationType
			.translateStringToAnnotationType("SUBCELLULAR LOCATION");
	public static final AnnotationType disease = AnnotationType.translateStringToAnnotationType("DISEASE");
	public static final AnnotationType biophysicochemical_properties = AnnotationType
			.translateStringToAnnotationType("BIOPHYSICOCHEMICAL PROPERTIES");
	public static final AnnotationType interation = AnnotationType.translateStringToAnnotationType("INTERACTION");
	public static final AnnotationType alternative_products = AnnotationType
			.translateStringToAnnotationType("ALTERNATIVE PRODUCTS");
	public static final AnnotationType manualAnnotation = AnnotationType
			.translateStringToAnnotationType("manual annotation");
	public static final AnnotationType sequenceVersion = AnnotationType
			.translateStringToAnnotationType("sequence version");
	public static final AnnotationType entry_creation_date = AnnotationType
			.translateStringToAnnotationType("ENTRY CREATION DATE");
	public static final AnnotationType entry_modified = AnnotationType
			.translateStringToAnnotationType("ENTRY MODIFIED");
	public static final AnnotationType entry_version = AnnotationType.translateStringToAnnotationType("ENTRY VERSION");
	public static final AnnotationType sequence_modified = AnnotationType
			.translateStringToAnnotationType("SEQUENCE MODIFIED");
	public static final AnnotationType ensemblID = AnnotationType.translateStringToAnnotationType("Ensembl");

	/**
	 * Constructor with one keyword and one or more {@link UniprotLineHeader}
	 *
	 * @param keyword
	 * @param ulhs
	 */
	private AnnotationType(String keyword, UniprotLineHeader ulh) {
		this.keyword = keyword;
		if (ulh != null)
			ulhs.add(ulh);

	}

	private AnnotationType(String keyword, Collection<UniprotLineHeader> ulhs) {
		this.keyword = keyword;
		if (ulhs != null)
			this.ulhs.addAll(ulhs);

	}

	private static AnnotationType[] loadFromFile() {
		final String fileName = edu.scripps.yates.utilities.properties.Properties
				.getPropertyValue("uniprot.annotation.types.file");
		log.debug("Loading annotations from internal file:" + fileName);
		HashMap<String, AnnotationType> ret = new HashMap<String, AnnotationType>();
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			// File file = new ClassPathResource(fileName).getFile();
			BufferedReader br = new BufferedReader(new InputStreamReader(classLoader.getResourceAsStream(fileName)));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || "".equals(line))
					continue;
				String keyword = null;
				final String[] split = line.split("\t");
				if (split.length > 1) {
					keyword = split[0].trim();
					List<UniprotLineHeader> uhls = new ArrayList<UniprotLineHeader>();
					for (int i = 1; i < split.length; i++) {
						final UniprotLineHeader translateStringToUniprotLineHeader = UniprotLineHeader
								.translateStringToUniprotLineHeader(split[i]);
						if (translateStringToUniprotLineHeader != null)
							uhls.add(translateStringToUniprotLineHeader);
					}

					if (!ret.containsKey(keyword.toLowerCase())) {
						AnnotationType annotationType = new AnnotationType(keyword, uhls);
						ret.put(keyword.toLowerCase(), annotationType);
					} else {
						ret.get(keyword.toLowerCase()).getUniprotLineHeaders().addAll(uhls);
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			log.warn("Error while loading annotations from file:" + e.getMessage());
		}
		log.debug("returning " + ret.size() + " annotations");
		AnnotationType[] array = new AnnotationType[ret.size()];
		int i = 0;
		for (AnnotationType annotationType : ret.values()) {
			array[i++] = annotationType;
		}
		return array;
	}

	public static HashMap<AnnotationType, List<String>> parseAnnotations(String text) {
		HashMap<AnnotationType, List<String>> ret = new HashMap<AnnotationType, List<String>>();
		if (text.contains(UNIPROT_ANNOTATIONS_SEPARATOR)) {
			final String[] split = text.split(UNIPROT_ANNOTATIONS_SEPARATOR);
			for (String annotation : split) {
				final int indexOf = annotation.indexOf(":");
				if (indexOf > -1) {
					String key = annotation.substring(0, indexOf).trim();
					AnnotationType annotationType = translateStringToAnnotationType(key);
					if (annotationType == null)
						continue;
					annotation = annotation.substring(indexOf + 1).trim();
					if (ret.containsKey(annotationType)) {
						ret.get(annotationType).add(annotation);
					} else {
						List<String> list = new ArrayList<String>();
						list.add(annotation);
						ret.put(annotationType, list);
					}
				}
			}
		}
		return ret;
	}

	public static AnnotationType translateStringToAnnotationType(String key) {
		final AnnotationType[] values = AnnotationType.values();
		for (AnnotationType annotationType : values) {

			if (annotationType.keyword.equalsIgnoreCase(key))
				return annotationType;
		}
		return null;
	}

	public static AnnotationType[] values() {
		return values;
	}

	public Set<UniprotLineHeader> getUniprotLineHeaders() {
		return ulhs;
	}

	public static AnnotationType translateStringToAnnotationType(String key, AnnotationType defaultAnnotationType) {
		final AnnotationType[] values = AnnotationType.values();
		for (AnnotationType annotationType : values) {
			if (annotationType.keyword.equalsIgnoreCase(key))
				return annotationType;
		}
		return defaultAnnotationType;
	}

	public String getKey() {
		return keyword;
	}

	public static Set<AnnotationType> getAnnotationTypesByUniprotLineHeader(UniprotLineHeader ulh) {
		Set<AnnotationType> ret = new HashSet<AnnotationType>();
		final AnnotationType[] values = AnnotationType.values();
		for (AnnotationType annotationType : values) {
			for (UniprotLineHeader ulh2 : annotationType.ulhs) {
				// System.out.println(ulh2.name());
				if (ulh2.name().equalsIgnoreCase(ulh.name())) {
					ret.add(annotationType);
					break;
				}
			}

		}
		return ret;
	}

	public static void main(String[] args) {
		final AnnotationType[] values2 = AnnotationType.values();
		System.out.println(values2.length);
		List<String> keys = new ArrayList<String>();
		System.out.print("[");
		for (AnnotationType annotationType : values2) {
			if (!keys.contains(annotationType.getKey()))
				keys.add(annotationType.getKey().toLowerCase());
		}
		Collections.sort(keys);
		for (String key : keys) {

			System.out.print(key + ", ");
		}
		System.out.print("]");
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return keyword;
	}
}
