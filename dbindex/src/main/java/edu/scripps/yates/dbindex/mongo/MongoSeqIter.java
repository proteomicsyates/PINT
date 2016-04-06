
package edu.scripps.yates.dbindex.mongo;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

import edu.scripps.yates.dbindex.IndexedSequence;

/**
 * Custom iterator class that returns single peptides at a time as an
 * IndexedSequence from a mongo DBCursor
 *
 * @author gstupp
 */
public class MongoSeqIter implements Iterator<IndexedSequence> {

	private int intMass;
	private String sequence;
	private ListIterator<String> peptideSeq;
	private final MongoCursor<Document> cursor;
	private Document obj; // sequence mongo object {'_id"- mass, 's'- list of
							// sequences}
	public int count = 0;
	private IndexedSequence indSeq = null;
	private IndexedSequence nextSeq = null;

	// constructor
	public MongoSeqIter(FindIterable<Document> cursor) {
		this.cursor = cursor.iterator();
		count = 0;
	}

	@Override
	public IndexedSequence next() throws NoSuchElementException {
		if (nextSeq != null) {
			indSeq = nextSeq;
			nextSeq = null;
			return indSeq;
		}
		if (peptideSeq != null && peptideSeq.hasNext()) {
			sequence = peptideSeq.next();
			indSeq = new IndexedSequence((float) intMass / 1000, sequence, sequence.length(), "---", "---");
			count++;
			return indSeq;
		} else {
			try {
				obj = cursor.next();
			} catch (NoSuchElementException e) {
				cursor.close();
				throw new NoSuchElementException();
			}
			// System.out.println("obj-" + obj);
			intMass = ((Number) obj.get("_id")).intValue(); // may have this
															// stored as a
															// Double or Int....
			peptideSeq = ((List<String>) obj.get("s")).listIterator();
			sequence = peptideSeq.next();
			indSeq = new IndexedSequence((float) intMass / 1000, sequence, sequence.length(), "---", "---");
			count++;
			return indSeq;
		}

	}

	@Override
	public boolean hasNext() {
		if (nextSeq == null) {
			try {
				nextSeq = next();
			} catch (Exception e) {
				cursor.close();
				return false;
			}
			return true;
		} else {
			return true;
		}
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}
}