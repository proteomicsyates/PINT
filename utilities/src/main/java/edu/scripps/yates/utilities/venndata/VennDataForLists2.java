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
import java.util.Map;
import java.util.Set;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileImageOutputStream;

import edu.scripps.yates.utilities.util.ImageUtils;

public class VennDataForLists2<T extends ContainsMultipleKeys> {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VennDataForLists2.class);
	public static int DEFAULT_CHART_WIDTH = 500;
	public static int DEFAULT_CHART_HEIGHT = 500;
	private final Map<String, Set<T>> setsByName = new HashMap<String, Set<T>>();

	private final Integer intersection12 = null;
	private final Integer intersection13 = null;
	private final Integer intersection123 = null;
	private final Integer intersection23 = null;

	private final List<String> labels = new ArrayList<String>();
	private final String title;
	private URL url;
	private final VennDataUtils<T> utils = new VennDataUtils<T>();

	public VennDataForLists2(String title, Map<String, Collection<T>> collectionsByName) throws IOException {

		log.debug("Venn data processing:");
		this.title = title;
		Set<Set<T>> collections = new HashSet<Set<T>>();
		for (String string : collectionsByName.keySet()) {
			labels.add(string);
			final Collection<T> collection = collectionsByName.get(string);
			if (!collection.isEmpty()) {
				log.debug("Collection 1 contains " + collection.size() + " elements");
				Set<T> set = new HashSet<T>();
				set.addAll(collection);
				setsByName.put(string, set);
				collections.add(set);
			} else {
				log.debug("Collection 1 is empty!");
			}
		}
		String name1 = labels.get(0);
		String name2 = labels.get(1);

		String name3 = null;
		if (labels.size() >= 3)
			name3 = labels.get(2);
		createChartURL(title, name1, name2, name3);
	}

	private Set<T> getCollection(String name) {
		if (setsByName.containsKey(name)) {
			return setsByName.get(name);
		} else {
			return Collections.emptySet();
		}
	}

	public Set<T> getUniqueTo(String... names) {
		// get the set of names
		final Set<T> intersection = getIntersection(names);
		// get the elements that are in the intersection but not in the rest
		Set<String> nameSet = new HashSet<String>();
		for (String name : names) {
			nameSet.add(name);
		}
		Set<T> ret = new HashSet<T>();
		for (T t : intersection) {
			boolean found = false;
			for (String name : this.labels) {
				if (!nameSet.contains(name)) {
					// this is another collection
					Set<T> collection = getCollection(name);
					if (collection.contains(t)) {
						found = true;
						break;
					}
				}
			}
			if (!found) {
				ret.add(t);
			}
		}
		return ret;
	}

	/**
	 * Gets tbe intersection set of collections
	 *
	 * @param names
	 * @return
	 */
	private Set<T> getIntersection(String... names) {

		Set<T> ret = new HashSet<T>();
		List<String> nameList = new ArrayList<String>();
		for (String name : names) {
			nameList.add(name);
		}
		// take the first collection
		Set<T> list1 = getCollection(nameList.get(0));

		for (T t : list1) {
			boolean found = true;
			for (int j = 1; j < nameList.size(); j++) {
				if (!getCollection(nameList.get(j)).contains(t)) {
					found = false;
					break;
				}
			}
			if (found) {
				ret.add(t);
			}
		}
		return ret;
	}

	/**
	 * Gets the union set of collections
	 *
	 * @param names
	 * @return
	 */
	private Set<T> getUnion(String... names) {

		Set<T> ret = new HashSet<T>();
		for (String name : names) {
			final Set<T> set = getCollection(name);
			ret.addAll(set);
		}
		return ret;
	}

	public int getSize(String name) {
		return getCollection(name).size();
	}

	/**
	 * Gets the collection that is bigger than the others
	 *
	 * @return
	 */
	public Set<T> getMaxCollection() {
		Set<T> ret = new HashSet<T>();
		int maxSize = -1;
		for (String name : labels) {
			if (getSize(name) > maxSize) {
				ret.clear();
				ret.addAll(getCollection(name));
				maxSize = getSize(name);
			}
		}
		return ret;
	}

	private String getDataString(String name1, String name2, String name3) {
		StringBuilder sb = new StringBuilder();
		// The first three values specify the sizes of three circles: A, B, and
		// C. For a chart with
		// only two circles, specify zero for the third value.

		int size1 = getSize(name1);
		int size2 = getSize(name2);
		int size3 = getSize(name3);
		sb.append(size1 + "," + size2 + "," + size3);
		// The fourth value specifies the size of the intersection of A and B.
		int intersection12 = getIntersection(name1, name2).size();
		sb.append("," + intersection12);
		// The fifth value specifies the size of the intersection of A and C.
		// For a chart with only
		// two circles, do not specify a value here.
		int intersection13 = getIntersection(name1, name3).size();
		sb.append("," + intersection13);
		// The sixth value specifies the size of the intersection of B and C.
		// For a chart with only
		// two circles, do not specify a value here.

		int intersection23 = getIntersection(name2, name3).size();
		sb.append("," + intersection23);

		// The seventh value specifies the size of the common intersection of A,
		// B, and C. For a
		// chart with only two circles, do not specify a value here.
		int intersection123 = getIntersection(name1, name2, name3).size();
		if (intersection123 > 0)
			sb.append("," + intersection123);

		return sb.toString();
	}

	public String getIntersectionsText(String... names) {
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#.#");

		int unionSize = getUnion(names).size();

		int numCollection = 1;
		for (String name : names) {
			sb.append("\n " + numCollection++ + " -> " + name + " = " + getSize(name) + " ("
					+ df.format(Double.valueOf(getSize(name) * 100.0 / unionSize)) + "% of union)");
		}

		sb.append("\n");
		sb.append("\nUnion=" + unionSize + " (100%)");

		// if (label1 != null && label2 != null) {
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// sb.append("Overlap (1,2) = " + intersection12 + " ("
		// + df.format(Double.valueOf(intersection12 * 100.0 /
		// getUnion12().size())) + "% of Union (1,2))");
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// sb.append("Union (1,2) = " + getUnion12().size() + " ("
		// + df.format(Double.valueOf(getUnion12().size() * 100.0 /
		// getUnion123().size()))
		// + "% of Total Union)");
		// }
		// if (label1 != null && label3 != null) {
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// sb.append("Overlap (1,3) = " + intersection13 + " ("
		// + df.format(Double.valueOf(intersection13 * 100.0 /
		// getUnion13().size())) + "% of Union (1,3))");
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// sb.append("Union (1,3) = " + getUnion13().size() + " ("
		// + df.format(Double.valueOf(getUnion13().size() * 100.0 /
		// getUnion123().size()))
		// + "% of Total Union)");
		// }
		// if (label3 != null && label2 != null) {
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// sb.append("Overlap (2,3) = " + intersection23 + " ("
		// + df.format(intersection23 * 100.0 / getUnion23().size()) + "% of
		// Union (2,3))");
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// sb.append("Union (2,3) = " + getUnion23().size() + " ("
		// + df.format(getUnion23().size() * 100.0 / getUnion123().size()) + "%
		// of Total Union)");
		// }
		//
		// if (label1 != null && label2 != null && label3 != null) {
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// sb.append("Overlap (1,2,3) = " + intersection123 + " (" +
		// df.format(intersection123 * 100.0 / unionSize)
		// + "% of union)");
		// }
		// if (label1 != null) {
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// Double just1 = getUniqueTo1().size() * 100.0 / unionSize;
		// int overlappedTo1 = getIntersection12().size() +
		// getIntersection13().size() - getIntersection123().size();
		// sb.append("Just in 1 = " + getUniqueTo1().size() + " (" +
		// df.format(just1) + "% of union) ("
		// + df.format((overlappedTo1) * 100.0 / getSize1()) + "% overlapped)");
		// }
		// if (label2 != null) {
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// Double just2 = getUniqueTo2().size() * 100.0 / unionSize;
		// int overlappedTo2 = getIntersection12().size() +
		// getIntersection23().size() - getIntersection123().size();
		// sb.append("Just in 2 = " + getUniqueTo2().size() + " (" +
		// df.format(just2) + "% of union) ("
		// + df.format((overlappedTo2) * 100.0 / getSize2()) + "% overlapped)");
		// }
		// if (label3 != null) {
		// if (!"".equals(sb.toString()))
		// sb.append("\n");
		// Double just3 = getUniqueTo3().size() * 100.0 / unionSize;
		// int overlappedTo3 = getIntersection13().size() +
		// getIntersection23().size() - getIntersection123().size();
		// sb.append("Just in 3 = " + getUniqueTo3().size() + " (" +
		// df.format(just3) + "% of union) ("
		// + df.format((overlappedTo3) * 100.0 / getSize3()) + "% overlapped)");
		// }
		sb.append("\n\n");

		return sb.toString();
	}

	private URL createChartURL(String title, String label1, String label2, String label3) throws MalformedURLException {
		if (url == null) {
			StringBuilder sb = new StringBuilder();
			sb.append("http://chart.apis.google.com/chart?chs=" + DEFAULT_CHART_WIDTH + "x" + DEFAULT_CHART_HEIGHT);
			sb.append("&chd=t:" + getDataString(label1, label2, label3));
			sb.append("&cht=v");

			sb.append("&chdl=");
			final String listString1 = getListString(label1, getCollection(label1));
			final String listString2 = getListString(label2, getCollection(label2));
			final String listString3 = getListString(label3, getCollection(label3));
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

	public void saveVennDiagramToFile(File outputFile, String label1, String label2, String label3) throws IOException {
		URL url = createChartURL(title, label1, label2, label3);
		final Image image = getImageFromURL(url);
		saveToFile(image, outputFile);
	}

	public URL getImageURL(String label1, String label2, String label3) throws MalformedURLException {
		return createChartURL(title, label1, label2, label3);
	}
}
