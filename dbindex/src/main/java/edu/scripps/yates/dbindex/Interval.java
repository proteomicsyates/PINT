package edu.scripps.yates.dbindex;

public class Interval {
	private final double start;
	private final double end;

	public Interval(double start, double end) {
		this.start = start;
		this.end = end;
	}

	public double getStart() {
		return start;
	}

	public double getEnd() {
		return end;
	}

	@Override
	public String toString() {
		return "Interval{" + "start=" + start + ", end=" + end + '}';
	}

	public static Interval massRangeToInterval(MassRange range) {
		double mass = range.getPrecMass();
		double tol = range.getTolerance();
		double massLow = mass - tol;
		if (massLow < 0f) {
			massLow = 0f;
		}
		double massHigh = mass + tol;

		return new Interval(massLow, massHigh);

	}
}
