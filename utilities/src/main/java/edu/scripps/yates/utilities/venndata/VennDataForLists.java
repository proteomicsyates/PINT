package edu.scripps.yates.utilities.venndata;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import edu.scripps.yates.utilities.util.ImageUtils;

public class VennDataForLists<T extends ContainsMultipleKeys> {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VennDataForLists.class);
	public static int DEFAULT_CHART_WIDTH = 500;
	public static int DEFAULT_CHART_HEIGHT = 500;
	private final Set<T> set1 = new HashSet<T>();
	private final Set<T> set2 = new HashSet<T>();
	private final Set<T> set3 = new HashSet<T>();

	private String intersection12 = null;
	private String intersection13 = null;
	private String intersection123 = null;
	private String intersection23 = null;

	private final String label1;
	private final String label2;
	private final String label3;
	private final String title;
	private URL url;
	private final VennDataUtils<T> utils = new VennDataUtils<T>();

	public VennDataForLists(String title, String collection1Name, Collection<T> col1, String collection2Name,
			Collection<T> col2, String collection3Name, Collection<T> col3) throws IOException {

		log.debug("Venn data processing:");
		label1 = collection1Name;
		label2 = collection2Name;
		label3 = collection3Name;
		this.title = title;
		if (col1 != null) {
			if (col1.isEmpty()) {
				throw new IllegalArgumentException(
						"Collection 1 is empty. Pass a null collection if you want to compare with an empty collection.");
			}
			log.debug("Collection 1 contains " + col1.size() + " elements");
			this.set1.addAll(col1);

		} else {
			log.debug("Collection 1 is empty!");

		}
		if (col2 != null) {
			if (col2.isEmpty()) {
				throw new IllegalArgumentException(
						"Collection 2 is empty. Pass a null collection if you want to compare with an empty collection.");
			}
			log.debug("Collection 2 contains " + col2.size() + " elements");
			this.set2.addAll(col2);

		} else {

			log.debug("Collection 2 is empty!");
		}
		if (col3 != null) {
			if (col3.isEmpty()) {
				throw new IllegalArgumentException(
						"Collection 3 is empty. Pass a null collection if you want to compare with an empty collection.");
			}
			log.debug("Collection 3 contains " + col3.size() + " elements");
			this.set3.addAll(col3);

		} else {
			log.debug("Collection 3 is empty!");

		}
		createChartURL(title, collection1Name, collection2Name, collection3Name);
	}

	public Set<T> getIntersection123() {
		return getIntersection(set1, set2, set3);

	}

	public String getIntersectionSizeString123() {
		if (label1 == null || label2 == null || label3 == null) {
			return "";
		}
		final Set<T> intersection = getIntersection(set1, set2, set3);

		return String.valueOf(intersection.size());

	}

	public Set<T> getIntersection12() {
		return getIntersection(set1, set2, null);
	}

	public String getIntersectionSizeString12() {
		if (label1 == null || label2 == null) {
			return "";
		}
		final Set<T> intersection = getIntersection(set1, set2, null);
		return String.valueOf(intersection.size());

	}

	public Set<T> getIntersection23() {
		return getIntersection(set2, set3, null);

	}

	public String getIntersectionSizeString23() {
		if (label2 == null || label3 == null) {
			return "";
		}
		final Set<T> intersection = getIntersection(set2, set3, null);
		return String.valueOf(intersection.size());

	}

	public Set<T> getIntersection13() {
		return getIntersection(set1, set3, null);
	}

	public String getIntersectionSizeString13() {
		if (label1 == null || label3 == null) {
			return "";
		}
		final Set<T> intersection = getIntersection(set1, set3, null);

		return String.valueOf(intersection.size());

	}

	public Set<T> get1In2() {
		return null;
	}

	public Set<T> get2In1() {
		return null;
	}

	public Set<T> getUniqueTo1() {
		return getUniqueToFirstSet(set1, set2, set3);
	}

	public Set<T> getUniqueTo2() {
		return getUniqueToFirstSet(set2, set1, set3);
	}

	public Set<T> getUniqueTo3() {
		return getUniqueToFirstSet(set3, set1, set2);
	}

	private Set<T> getUniqueToFirstSet(Set<T> hashToIsolate, Set<T> hash2, Set<T> hash3) {
		Set<T> toIsolateSet2 = new HashSet<T>();
		if (hashToIsolate != null) {
			toIsolateSet2.addAll(hashToIsolate);
			Iterator<T> toIsolateIterator = toIsolateSet2.iterator();
			if (hash2 != null) {
				while (toIsolateIterator.hasNext()) {
					T item = toIsolateIterator.next();
					if (hash2.contains(item))
						toIsolateIterator.remove();
				}
			}
			toIsolateIterator = toIsolateSet2.iterator();
			if (hash3 != null) {
				while (toIsolateIterator.hasNext()) {
					T item = toIsolateIterator.next();
					if (hash3.contains(item))
						toIsolateIterator.remove();
				}
			}

		}
		return toIsolateSet2;
	}

	public Set<T> getUnion123() {
		return getUnion(set1, set2, set3);
	}

	public Set<T> getUnion12() {
		return getUnion(set1, set2, null);
	}

	public Set<T> getUnion13() {
		return getUnion(set1, null, set3);
	}

	public Set<T> getUnion23() {
		return getUnion(null, set2, set3);
	}

	/**
	 * Gets tbe intersection set of three string collections
	 *
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	private Set<T> getIntersection(Set<T> list1, Set<T> list2, Set<T> list3) {

		int numNonEmptySets = 0;
		if (list1 != null && !list1.isEmpty()) {
			numNonEmptySets++;
		}
		if (list2 != null && !list2.isEmpty()) {
			numNonEmptySets++;
		}
		if (list3 != null && !list3.isEmpty()) {
			numNonEmptySets++;
		}
		HashSet<T> ret = new HashSet<T>();
		for (T t : list1) {
			int numFound = 0;

			if (list2 != null)
				if (list2.contains(t))
					numFound++;

			if (list3 != null)
				if (list3.contains(t))
					numFound++;
			if (numFound == numNonEmptySets - 1)
				ret.add(t);
		}

		return ret;
	}

	/**
	 * Gets tbe union set of three T collections
	 *
	 * @param list1
	 * @param list2
	 * @param list3
	 * @return
	 */
	private Set<T> getUnion(Set<T> list1, Set<T> list2, Set<T> list3) {
		// Since the HashSet doesn't allow to add repeated elements, add all to
		// the set
		HashSet<T> ret = new HashSet<T>();
		if (list1 != null)
			ret.addAll(list1);
		if (list2 != null)
			ret.addAll(list2);
		if (list3 != null)
			ret.addAll(list3);

		return ret;
	}

	public int getSize1() {
		return set1.size();
	}

	public int getSize2() {
		return set2.size();
	}

	public int getSize3() {
		return set3.size();
	}

	public Collection<T> getCollection1() {
		return set1;
	}

	public Collection<T> getCollection2() {
		return set2;
	}

	public Collection<T> getCollection3() {
		return set3;
	}

	/**
	 * Gets the collection that is bigger than the others
	 *
	 * @return
	 */
	public Set<T> getMaxCollection() {
		Set<T> ret = new HashSet<T>();
		ret.addAll(set1);
		if (set2 != null && set2.size() > set1.size()) {
			ret.clear();
			ret.addAll(set2);
		}
		if (set3 != null && set3.size() > set2.size()) {
			ret.clear();
			ret.addAll(set3);
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
		sb.append(size1);
		sb.append(",").append(size2);
		sb.append(",").append(size3);
		// The fourth value specifies the size of the intersection of A and B.
		intersection12 = getIntersectionSizeString12();
		sb.append("," + intersection12);
		// The fifth value specifies the size of the intersection of A and C.
		// For a chart with only two circles, do not specify a value here.
		if (label3 != null) {
			intersection13 = getIntersectionSizeString13();
			sb.append("," + intersection13);
		}
		// The sixth value specifies the size of the intersection of B and C.
		// For a chart with only
		// two circles, do not specify a value here.
		if (label3 != null) {
			intersection23 = getIntersectionSizeString23();
			sb.append("," + intersection23);
		}
		// The seventh value specifies the size of the common intersection of A,
		// B, and C. For a
		// chart with only two circles, do not specify a value here.
		if (label3 != null) {
			intersection123 = getIntersectionSizeString123();
			sb.append("," + intersection123);
		}
		return sb.toString();
	}

	public String getIntersectionsText() {
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#.#");

		int union = getUnion123().size();

		if (label1 != null)
			sb.append("\n 1 -> " + label1 + " = " + getSize1() + " ("
					+ df.format(Double.valueOf(getSize1() * 100.0 / union)) + "% of union)");
		if (label2 != null)
			sb.append("\n 2 -> " + label2 + " = " + getSize2() + " ("
					+ df.format(Double.valueOf(getSize2() * 100.0 / union)) + "% of union)");
		if (label3 != null)
			sb.append("\n 3 -> " + label3 + " = " + getSize3() + " ("
					+ df.format(Double.valueOf(getSize3() * 100.0 / union)) + "% of union)");
		sb.append("\n");
		sb.append("\nUnion=" + union + " (100%)");

		if (label1 != null && label2 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (1,2) = " + intersection12 + " ("
					+ df.format(Double.valueOf(Double.valueOf(intersection12) * 100.0 / getUnion12().size()))
					+ "% of Union (1,2))");
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Union (1,2) = " + getUnion12().size() + " ("
					+ df.format(Double.valueOf(getUnion12().size() * 100.0 / getUnion123().size()))
					+ "% of Total Union)");
		}
		if (label1 != null && label3 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (1,3) = " + intersection13 + " ("
					+ df.format(Double.valueOf(Double.valueOf(intersection13) * 100.0 / getUnion13().size()))
					+ "% of Union (1,3))");
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Union (1,3) = " + getUnion13().size() + " ("
					+ df.format(Double.valueOf(getUnion13().size() * 100.0 / getUnion123().size()))
					+ "% of Total Union)");
		}
		if (label3 != null && label2 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (2,3) = " + intersection23 + " ("
					+ df.format(Double.valueOf(intersection23) * 100.0 / getUnion23().size()) + "% of Union (2,3))");
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Union (2,3) = " + getUnion23().size() + " ("
					+ df.format(getUnion23().size() * 100.0 / getUnion123().size()) + "% of Total Union)");
		}

		if (label1 != null && label2 != null && label3 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			sb.append("Overlap (1,2,3) = " + intersection123 + " ("
					+ df.format(Double.valueOf(intersection123) * 100.0 / union) + "% of union)");
		}
		if (label1 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			Double just1 = getUniqueTo1().size() * 100.0 / union;
			int overlappedTo1 = getIntersection12().size() + getIntersection13().size() - getIntersection123().size();
			sb.append("Just in 1 = " + getUniqueTo1().size() + " (" + df.format(just1) + "% of union) ("
					+ df.format((overlappedTo1) * 100.0 / getSize1()) + "% overlapped)");
		}
		if (label2 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			Double just2 = getUniqueTo2().size() * 100.0 / union;
			int overlappedTo2 = getIntersection12().size() + getIntersection23().size() - getIntersection123().size();
			sb.append("Just in 2 = " + getUniqueTo2().size() + " (" + df.format(just2) + "% of union) ("
					+ df.format((overlappedTo2) * 100.0 / getSize2()) + "% overlapped)");
		}
		if (label3 != null) {
			if (!"".equals(sb.toString()))
				sb.append("\n");
			Double just3 = getUniqueTo3().size() * 100.0 / union;
			int overlappedTo3 = getIntersection13().size() + getIntersection23().size() - getIntersection123().size();
			sb.append("Just in 3 = " + getUniqueTo3().size() + " (" + df.format(just3) + "% of union) ("
					+ df.format((overlappedTo3) * 100.0 / getSize3()) + "% overlapped)");
		}
		sb.append("\n\n");

		return sb.toString();
	}

	private URL createChartURL(String title, String label1, String label2, String label3) throws MalformedURLException {
		if (url == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("http://chart.apis.google.com/chart?chs=" + DEFAULT_CHART_WIDTH + "x" + DEFAULT_CHART_HEIGHT);
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
					"It is not possible to reach the URL: " + url + ". Check the internet connection.");
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

	private void saveGraphicJpeg(BufferedImage chart, File outputFile, float quality) throws IOException {
		// Setup correct compression for jpeg.
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
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
