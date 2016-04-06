package edu.scripps.yates.dbindex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Merge ranges
 * 
 */
public class MergeIntervals {

	private MergeIntervals() {
	}

	public static ArrayList<Interval> mergeIntervals(
			ArrayList<Interval> intervals) {

		int size = intervals.size();

		if (size < 2) {
			return intervals;
		}
		Collections.sort(intervals, INTERVAL_ORDER);
		ArrayList<Interval> ret = new ArrayList<Interval>();

		double start = intervals.get(0).getStart();
		double end = intervals.get(0).getEnd();
		for (int i = 1; i < size; i++) {
			Interval itv = intervals.get(i);
			if (end >= itv.getStart()) {
				// merge 2
				end = Math.max(end, itv.getEnd());
				// System.out.println("Merging intervals: " + start + "-" + end
				// + " and: " + itv.getStart() + "-" + itv.getEnd());
			} else {
				ret.add(new Interval(start, end));
				start = itv.getStart();
				end = itv.getEnd();
			}
		}
		// the last set of intervals are not added
		ret.add(new Interval(start, end));

		return ret;
	}

	// sort intervals by start
	static final Comparator<Interval> INTERVAL_ORDER = new Comparator<Interval>() {
		@Override
		public int compare(Interval i, Interval j) {
			return new Double(i.getStart()).compareTo(new Double(j.getStart()));
		}
	};
}
