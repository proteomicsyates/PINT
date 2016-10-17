package edu.scripps.yates.annotations.go;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoEntry {
	private final String db;
	private final String id;
	private final String splice;
	private final String symbol;
	private final String taxon;
	private final String qualifier;
	private final String goID;
	private final String goName;
	private final String reference;
	private final String evidence;
	private final String with;
	private final GoType aspect;
	private final Date date;
	private final String source;
	private String toLine;
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	public GoEntry(String tsvLine) {
		if (!tsvLine.contains("\t")) {
			id = tsvLine;
			db = null;
			splice = null;
			symbol = null;
			taxon = null;
			qualifier = null;
			goID = null;
			goName = null;
			reference = null;
			evidence = null;
			with = null;
			aspect = null;
			date = null;
			source = null;
			return;
		}
		final String[] split = tsvLine.split("\t");
		if (split.length < 14) {
			throw new IllegalArgumentException(tsvLine + " contains a different number of columns than expected (14)");
		}
		db = split[GORetriever.indexByColumnName.get("DB")];
		id = split[GORetriever.indexByColumnName.get("ID")];
		splice = split[GORetriever.indexByColumnName.get("Splice")];
		symbol = split[GORetriever.indexByColumnName.get("Symbol")];
		taxon = split[GORetriever.indexByColumnName.get("Taxon")];
		qualifier = split[GORetriever.indexByColumnName.get("Qualifier")];
		goID = split[GORetriever.indexByColumnName.get("GO ID")];
		goName = split[GORetriever.indexByColumnName.get("GO Name")];
		reference = split[GORetriever.indexByColumnName.get("Reference")];
		evidence = split[GORetriever.indexByColumnName.get("Evidence")];
		with = split[GORetriever.indexByColumnName.get("With")];

		final String name = split[GORetriever.indexByColumnName.get("Aspect")];
		if (name.equals("null")) {
			aspect = null;
		} else {
			aspect = GoType.getByName(name);
		}
		final String dateString = split[GORetriever.indexByColumnName.get("Date")];
		try {
			if (dateString.equals("null")) {
				date = null;
			} else {
				date = dateFormat.parse(dateString);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date in 12th column must be in yyyyMMdd format: '" + dateString + "'");
		}
		source = split[GORetriever.indexByColumnName.get("Source")];
	}

	/**
	 * @return the db
	 */
	public String getDb() {
		return db;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the splice
	 */
	public String getSplice() {
		return splice;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @return the taxon
	 */
	public String getTaxon() {
		return taxon;
	}

	/**
	 * @return the qualifier
	 */
	public String getQualifier() {
		return qualifier;
	}

	/**
	 * @return the goID
	 */
	public String getGoID() {
		return goID;
	}

	/**
	 * @return the goNameReference
	 */
	public String getGoName() {
		return goName;
	}

	/**
	 * @return the evidence
	 */
	public String getEvidence() {
		return evidence;
	}

	/**
	 * @return the with
	 */
	public String getWith() {
		return with;
	}

	/**
	 * @return the aspect
	 */
	public GoType getAspect() {
		return aspect;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	public String toLine() {
		if (toLine == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(db).append("\t");
			sb.append(id).append("\t");
			sb.append(splice).append("\t");
			sb.append(symbol).append("\t");
			sb.append(taxon).append("\t");
			sb.append(qualifier).append("\t");
			sb.append(goID).append("\t");
			sb.append(goName).append("\t");
			sb.append(reference).append("\t");
			sb.append(evidence).append("\t");
			sb.append(with).append("\t");
			sb.append(aspect).append("\t");
			if (date != null) {
				sb.append(dateFormat.format(date)).append("\t");
			} else {
				sb.append("null").append("\t");
			}
			sb.append(source);
			toLine = sb.toString();
		}
		return toLine;
	}

	@Override
	public String toString() {
		return toLine();
	}
}
