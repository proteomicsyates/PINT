package edu.scripps.yates.utilities.venndata;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import edu.scripps.yates.utilities.util.ImageUtils;

public class VennData {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger
			.getLogger(VennData.class);
	private static int count = 0;
	public static int DEFAULT_CHART_WIDTH = 500;
	public static int DEFAULT_CHART_HEIGHT = 500;
	private final HashMap<String, Object> hash1 = new HashMap<String, Object>();
	private final HashMap<String, Object> hash2 = new HashMap<String, Object>();
	private final HashMap<String, Object> hash3 = new HashMap<String, Object>();

	private final Set<String> keys1 = new HashSet<String>();
	private final Set<String> keys2 = new HashSet<String>();
	private final Set<String> keys3 = new HashSet<String>();
	private final HashMap<String, String> hash = new HashMap<String, String>();
	private Integer intersection12 = null;
	private Integer intersection13 = null;
	private Integer intersection123 = null;
	private Integer intersection23 = null;
	private final String label1;
	private final String label2;
	private final String label3;
	private final String title;
	private URL url;

	public VennData(String title, String collection1Name, Collection col1,
			String collection2Name, Collection col2, String collection3Name,
			Collection col3) throws IOException {

		log.debug("Venn data processing:");
		label1 = collection1Name;
		label2 = collection2Name;
		label3 = collection3Name;
		this.title = title;
		if (col1 != null)
			log.debug("Collection 1 contains " + col1.size() + " elements");
		else
			log.debug("Collection 1 is empty!");
		if (col2 != null)
			log.debug("Collection 2 contains " + col2.size() + " elements");
		else
			log.debug("Collection 2 is empty!");
		if (col3 != null)
			log.debug("Collection 3 contains " + col3.size() + " elements");
		else
			log.debug("Collection 3 is empty!");
		log.debug("Processing collection 1");
		processCollections(col1, keys1, hash1);
		log.debug("Processing collection 2");
		processCollections(col2, keys2, hash2);
		log.debug("Processing collection 3");
		processCollections(col3, keys3, hash3);
		createChartURL(title, collection1Name, collection2Name, collection3Name);
	}

	private void processCollections(Collection<Object> referenceCollection,
			Set<String> keys1, HashMap<String, Object> hash1) {

		int uniques = 0; // number of unique objects in reference collection

		// Se trata de ir comparando dos a dos los grupos. Cuando tengan un
		// objecto en comun, se les asigna a los dos una clave de tipo cadena de
		// texto. Si un elemento no se encuentra en el otro grupo, se le
		// asignara otra cadena.

		// 1 VS (2 and 3)
		if (referenceCollection != null)
			for (Object obj1 : referenceCollection) {

				String seq = getObjectKey(obj1);
				String objKey = getUniqueKey();
				if (hash.containsKey(seq)) {
					objKey = hash.get(seq);
				} else {
					hash.put(seq, objKey);
				}
				hash1.put(objKey, obj1);
				if (keys1 == null)
					keys1 = new HashSet<String>();
				keys1.add(objKey);

			}
		if (keys1 != null)
			log.debug("Reference collection now has " + keys1.size()
					+ " string keys. " + uniques + " are uniques");

	}

	private String getObjectKey(Object obj1) {
		// if (obj1 instanceof ExtendedIdentifiedPeptide)
		// return ((ExtendedIdentifiedPeptide) obj1).getSequence();
		// if (obj1 instanceof PeptideOccurrence)
		// return ((PeptideOccurrence) obj1).getKey();
		return obj1.toString();
	}

	private String getUniqueKey() {
		return String.valueOf(VennData.count++);
	}

	public Collection<Object> getIntersection123() {
		Set<String> intersectionKeys = getIntersection(keys1, keys2, keys3);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> getIntersection12() {
		Set<String> intersectionKeys = getIntersection(keys1, keys2);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> getIntersection23() {
		Set<String> intersectionKeys = getIntersection(keys2, keys3);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> getIntersection13() {
		Set<String> intersectionKeys = getIntersection(keys1, keys3);
		List<Object> ret = getObjectsByKeys(intersectionKeys);
		return ret;
	}

	public Collection<Object> get1In2() {
		return null;
	}

	public Collection<Object> get2In1() {
		return null;
	}

	public Collection<Object> getUniqueTo1() {
		Set<String> uniqueTo1 = getUniqueToFirstSet(keys1, keys2, keys3);
		return getObjectsByKeys(uniqueTo1);
	}

	public Collection<Object> getUniqueTo2() {
		Set<String> uniqueTo2 = getUniqueToFirstSet(keys2, keys1, keys3);
		return getObjectsByKeys(uniqueTo2);
	}

	public Collection<Object> getUniqueTo3() {
		Set<String> uniqueTo3 = getUniqueToFirstSet(keys3, keys1, keys2);
		return getObjectsByKeys(uniqueTo3);
	}

	private Set<String> getUniqueToFirstSet(Set<String> hashToIsolate,
			Set<String> hash2, Set<String> hash3) {
		Set<String> toIsolateSet2 = new HashSet<String>();
		if (hashToIsolate != null) {
			toIsolateSet2.addAll(hashToIsolate);
			Iterator<String> toIsolateIterator = toIsolateSet2.iterator();
			if (hash2 != null) {
				while (toIsolateIterator.hasNext()) {
					String item = toIsolateIterator.next();
					if (hash2.contains(item))
						toIsolateIterator.remove();
				}
			}
			toIsolateIterator = toIsolateSet2.iterator();
			if (hash3 != null) {
				while (toIsolateIterator.hasNext()) {
					String item = toIsolateIterator.next();
					if (hash3.contains(item))
						toIsolateIterator.remove();
				}
			}

		}
		return toIsolateSet2;
	}

	public Collection<Object> getUnion123() {
		Set<String> unionKeys = getUnion(keys1, keys2, keys3);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<Object> getUnion12() {
		Set<String> unionKeys = getUnion(keys1, keys2, null);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<Object> getUnion13() {
		Set<String> unionKeys = getUnion(keys1, null, keys3);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	public Collection<Object> getUnion23() {
		Set<String> unionKeys = getUnion(null, keys2, keys3);
		List<Object> ret = getObjectsByKeys(unionKeys);
		return ret;
	}

	private List<Object> getObjectsByKeys(Set<String> keys) {
		List<Object> ret = new ArrayList<Object>();

		for (String stringKey : keys) {
			if (hash1.containsKey(stringKey)) {
				ret.add(hash1.get(stringKey));
				continue;
			}
			if (hash2.containsKey(stringKey)) {
				ret.add(hash2.get(stringKey));
				continue;
			}
			if (hash3.containsKey(stringKey)) {
				ret.add(hash3.get(stringKey));
				continue;
			}

		}
		return ret;
	}

	/**
	 * Gets tbe intersection set of three string collections
	 * 
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	private Set<String> getIntersection(Set<String> list1, Set<String> list2,
			Set<String> list3) {

		if (list1 == null || list1.isEmpty() || list2 == null
				|| list2.isEmpty() || list3 == null || list3.isEmpty()) {

			return Collections.EMPTY_SET;
		}
		HashSet<String> ret = new HashSet<String>();
		for (String key : list1) {
			int numFound = 0;

			if (list2 != null)
				if (list2.contains(key))
					numFound++;

			if (list3 != null)
				if (list3.contains(key))
					numFound++;
			if (numFound == 2)
				ret.add(key);
		}

		return ret;
	}

	private Set<String> getIntersection(Set<String> list1, Set<String> list2) {

		if (list1 == null || list1.isEmpty() || list2 == null
				|| list2.isEmpty()) {

			return Collections.EMPTY_SET;
		}
		HashSet<String> ret = new HashSet<String>();
		for (String key : list1) {
			int numFound = 0;

			if (list2 != null)
				if (list2.contains(key))
					numFound++;

			if (numFound == 1)
				ret.add(key);
		}

		return ret;
	}

	/**
	 * Gets tbe union set of three string collections
	 * 
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	private Set<String> getUnion(Set<String> list1, Set<String> list2,
			Set<String> list3) {
		// Since the HashSet doesn't allow to add repeated elements, add all to
		// the set
		HashSet<String> ret = new HashSet<String>();
		if (list1 != null)
			ret.addAll(list1);
		if (list2 != null)
			ret.addAll(list2);
		if (list3 != null)
			ret.addAll(list3);

		return ret;
	}

	public int getSize1() {
		return hash1.size();
	}

	public int getSize2() {
		return hash2.size();
	}

	public int getSize3() {
		return hash3.size();
	}

	public Collection getCollection1() {
		return hash1.values();
	}

	public Collection getCollection2() {
		return hash2.values();
	}

	public Collection getCollection3() {
		return hash3.values();
	}

	/**
	 * Gets the collection that is bigger than the others
	 * 
	 * @return
	 */
	public Collection<Object> getMaxCollection() {
		HashSet<Object> ret = new HashSet<Object>();
		ret.addAll(hash1.values());
		if (hash2.size() > hash1.size()) {
			ret.clear();
			ret.addAll(hash2.values());
		}
		if (hash3.size() > hash2.size()) {
			ret.clear();
			ret.addAll(hash3.values());
		}
		return ret;
	}

	private String getDataString() {
		StringBuilder sb = new StringBuilder();
		// The first three values specify the sizes of three circles: A, B, and
		// C. For a chart with
		// only two circles, specify zero for the third value.

		int size1 = getSize1();
		int size2 = getSize2();
		int size3 = getSize3();
		sb.append(size1 + "," + size2 + "," + size3);
		// The fourth value specifies the size of the intersection of A and B.
		intersection12 = getIntersection12().size();
		sb.append("," + intersection12);
		// The fifth value specifies the size of the intersection of A and C.
		// For a chart with only
		// two circles, do not specify a value here.
		intersection13 = getIntersection13().size();
		sb.append("," + intersection13);
		// The sixth value specifies the size of the intersection of B and C.
		// For a chart with only
		// two circles, do not specify a value here.

		intersection23 = getIntersection23().size();
		sb.append("," + intersection23);

		// The seventh value specifies the size of the common intersection of A,
		// B, and C. For a
		// chart with only two circles, do not specify a value here.
		intersection123 = getIntersection123().size();
		if (intersection123 > 0)
			sb.append("," + intersection123);

		return sb.toString();
	}

	public String getIntersectionsText(String title) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#.#");

		if (title != null)
			sb.append(title + "\n");
		int union = getUnion123().size();

		if (label1 != null)
			sb.append("\n 1 -> " + label1 + " = " + getSize1() + " ("
					+ df.format(Double.valueOf(getSize1() * 100.0 / union))
					+ "% of union)");
		if (label2 != null)
			sb.append("\n 2 -> " + label2 + " = " + getSize2() + " ("
					+ df.format(Double.valueOf(getSize2() * 100.0 / union))
					+ "% of union)");
		if (label3 != null)
			sb.append("\n 3 -> " + label3 + " = " + getSize3() + " ("
					+ df.format(Double.valueOf(getSize3() * 100.0 / union))
					+ "% of union)");
		sb.append("\n");
		sb.append("\nUnion=" + union + " (100%)");

		if (label1 != null && label2 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (1,2) = "
					+ intersection12
					+ " ("
					+ df.format(Double.valueOf(intersection12 * 100.0
							/ getUnion12().size())) + "% of Union (1,2))");
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Union (1,2) = "
					+ getUnion12().size()
					+ " ("
					+ df.format(Double.valueOf(getUnion12().size() * 100.0
							/ getUnion123().size())) + "% of Total Union)");
		}
		if (label1 != null && label3 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (1,3) = "
					+ intersection13
					+ " ("
					+ df.format(Double.valueOf(intersection13 * 100.0
							/ getUnion13().size())) + "% of Union (1,3))");
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Union (1,3) = "
					+ getUnion13().size()
					+ " ("
					+ df.format(Double.valueOf(getUnion13().size() * 100.0
							/ getUnion123().size())) + "% of Total Union)");
		}
		if (label3 != null && label2 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (2,3) = " + intersection23 + " ("
					+ df.format(intersection23 * 100.0 / getUnion23().size())
					+ "% of Union (2,3))");
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Union (2,3) = "
					+ getUnion23().size()
					+ " ("
					+ df.format(getUnion23().size() * 100.0
							/ getUnion123().size()) + "% of Total Union)");
		}

		if (label1 != null && label2 != null && label3 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (1,2,3) = " + intersection123 + " ("
					+ df.format(intersection123 * 100.0 / union)
					+ "% of union)");
		}
		if (label1 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			Double just1 = getUniqueTo1().size() * 100.0 / union;
			int overlappedTo1 = getIntersection12().size()
					+ getIntersection13().size() - getIntersection123().size();
			sb.append("Just in 1 = " + getUniqueTo1().size() + " ("
					+ df.format(just1) + "% of union) ("
					+ df.format((overlappedTo1) * 100.0 / getSize1())
					+ "% overlapped)");
		}
		if (label2 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			Double just2 = getUniqueTo2().size() * 100.0 / union;
			int overlappedTo2 = getIntersection12().size()
					+ getIntersection23().size() - getIntersection123().size();
			sb.append("Just in 2 = " + getUniqueTo2().size() + " ("
					+ df.format(just2) + "% of union) ("
					+ df.format((overlappedTo2) * 100.0 / getSize2())
					+ "% overlapped)");
		}
		if (label3 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			Double just3 = getUniqueTo3().size() * 100.0 / union;
			int overlappedTo3 = getIntersection13().size()
					+ getIntersection23().size() - getIntersection123().size();
			sb.append("Just in 3 = " + getUniqueTo3().size() + " ("
					+ df.format(just3) + "% of union) ("
					+ df.format((overlappedTo3) * 100.0 / getSize3())
					+ "% overlapped)");
		}
		sb.append("\n\n");

		return sb.toString();
	}

	private URL createChartURL(String title, String label1, String label2,
			String label3) throws MalformedURLException {
		if (url == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("http://chart.apis.google.com/chart?chs="
					+ DEFAULT_CHART_WIDTH + "x" + DEFAULT_CHART_HEIGHT);
			sb.append("&chd=t:" + getDataString());
			sb.append("&cht=v");

			sb.append("&chdl=");
			final String listString1 = getListString(label1, getCollection1());
			final String listString2 = getListString(label2, getCollection2());
			final String listString3 = getListString(label3, getCollection3());
			sb.append(listString1);
			if (!"".equals(listString1) && !"".equals(listString2))
				sb.append("|");

			sb.append(listString2);
			if (!"".equals(listString2) && !"".equals(listString3))
				sb.append("|");

			sb.append(listString3);

			// sb.append("&chdl=" + label1 + "(" + this.list1.size() + ")|" +
			// label2
			// + "("
			// + this.list2.size() + ")|" + label3 + "(" + this.list3.size() +
			// ")");
			sb.append("&chtt=" + title);
			sb.append("&chds=0," + getMaxCollection().size());
			sb.append("&chdlp=b&chma=|10,10");
			log.info("URL created=" + sb.toString());
			url = new URL(sb.toString());
			// return new URL(
			// "https://chart.googleapis.com/chart?chs=400x300&chd=t:100,80,60,30,30,30,10"
			// +
			// "&cht=v&chl=Hello|World|asdf&chtt=Titulo");
			// }
		}
		return url;
	}

	private String getListString(String label, Collection list) {
		if (list != null && !list.isEmpty())
			return label + "(" + list.size() + ")";
		return "";
	}

	private Image getImageFromURL(URL url) {
		Image image;
		try {
			image = Toolkit.getDefaultToolkit().createImage(url);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		if (image == null)
			throw new IllegalArgumentException(
					"It is not possible to reach the URL: " + url
							+ ". Check the internet connection.");
		return image;
	}

	private void saveToFile(Image image, File outputFile) throws IOException {
		String filename = outputFile.getName();

		int extPoint = filename.lastIndexOf('.');

		if (extPoint < 0) {
			throw new IOException("Illegal filename, no extension used.");
		}

		// Determine the extension of the filename.
		String ext = filename.substring(extPoint + 1);

		// Handle jpg without transparency.
		if (ext.toLowerCase().equals("jpg") || ext.toLowerCase().equals("jpeg")) {
			BufferedImage chart = ImageUtils.toBufferedImage(image);
			// BufferedImage chart = (BufferedImage) getChartImage(false);

			// Save our graphic.
			saveGraphicJpeg(chart, outputFile, 1.0f);
		} else {
			BufferedImage chart = ImageUtils.toBufferedImage(image);
			// BufferedImage chart = (BufferedImage) getChartImage(true);

			ImageIO.write(chart, ext, outputFile);
		}
	}

	private void saveGraphicJpeg(BufferedImage chart, File outputFile,
			float quality) throws IOException {
		// Setup correct compression for jpeg.
		Iterator<ImageWriter> iter = ImageIO
				.getImageWritersByFormatName("jpeg");
		ImageWriter writer = iter.next();
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		iwp.setCompressionQuality(quality);

		// Output the image.
		FileImageOutputStream output = new FileImageOutputStream(outputFile);
		writer.setOutput(output);
		IIOImage image = new IIOImage(chart, null, null);
		writer.write(null, image, iwp);
		writer.dispose();

	}

	public void saveVennDiagramToFile(File outputFile) throws IOException {
		URL url = createChartURL(title, label1, label2, label3);
		final Image image = getImageFromURL(url);
		saveToFile(image, outputFile);
	}

	public URL getImageURL() throws MalformedURLException {
		return createChartURL(title, label1, label2, label3);
	}
}
