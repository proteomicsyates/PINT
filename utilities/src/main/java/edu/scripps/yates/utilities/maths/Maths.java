package edu.scripps.yates.utilities.maths;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.rank.Median;

public class Maths {
	private Maths() {
	}

	/**
	 * Returns the maximum value in the array a[], -infinity if no such value.
	 */
	public static double max(double[] a) {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < a.length; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Returns the maximum value in the subarray a[lo..hi], -infinity if no such
	 * value.
	 */
	public static double max(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double max = Double.NEGATIVE_INFINITY;
		for (int i = lo; i <= hi; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	public static double max(Double... doubles) {
		double max = -Double.MAX_VALUE;
		for (Double double1 : doubles) {
			if (max < double1) {
				max = double1;
			}
		}
		return max;
	}

	public static float max(Float... numbers) {
		float max = -Float.MAX_VALUE;
		for (Float number : numbers) {
			if (max < number) {
				max = number;
			}
		}
		return max;
	}

	public static double min(Double... doubles) {
		double min = Double.MAX_VALUE;
		for (Double double1 : doubles) {
			if (min > double1) {
				min = double1;
			}
		}
		return min;
	}

	public static float min(Float... numbers) {
		float min = Float.MAX_VALUE;
		for (Float number : numbers) {
			if (min > number) {
				min = number;
			}
		}
		return min;
	}

	/**
	 * Returns the maximum value in the array a[], Integer.MIN_VALUE if no such
	 * value.
	 */
	public static int max(Integer[] a) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max)
				max = a[i];
		}
		return max;
	}

	/**
	 * Returns the minimum value in the array a[], +infinity if no such value.
	 */
	public static double min(double[] a) {
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < a.length; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Returns the minimum value in the subarray a[lo..hi], +infinity if no such
	 * value.
	 */
	public static double min(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double min = Double.POSITIVE_INFINITY;
		for (int i = lo; i <= hi; i++) {
			if (Double.isNaN(a[i]))
				return Double.NaN;
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Returns the minimum value in the array a[], Integer.MAX_VALUE if no such
	 * value.
	 */
	public static int min(Integer[] a) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < a.length; i++) {
			if (a[i] < min)
				min = a[i];
		}
		return min;
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static double mean(Double[] a) {
		if (a.length == 0)
			return Double.NaN;
		double sum = sum(a);
		return sum / a.length;
	}

	/**
	 * Returns the average value in the subarray a[lo..hi], NaN if no such
	 * value.
	 */
	public static double mean(double[] a, int lo, int hi) {
		int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		double sum = sum(a, lo, hi);
		return sum / length;
	}

	/**
	 * Returns the average value in the array a[], NaN if no such value.
	 */
	public static double mean(Integer[] a) {
		if (a.length == 0)
			return Double.NaN;
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i];
		}
		return sum / a.length;
	}

	/**
	 * Returns the sample variance in the array a[], NaN if no such value.
	 */
	public static double var(Double[] a) {
		if (a.length == 0)
			return Double.NaN;
		double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / (a.length - 1);
	}

	/**
	 * Returns the sample variance in the subarray a[lo..hi], NaN if no such
	 * value.
	 */
	public static double var(double[] a, int lo, int hi) {
		int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		double avg = mean(a, lo, hi);
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / (length - 1);
	}

	/**
	 * Returns the sample variance in the array a[], NaN if no such value.
	 */
	public static double var(Integer[] a) {
		if (a.length == 0)
			return Double.NaN;
		double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / (a.length - 1);
	}

	/**
	 * Returns the population variance in the array a[], NaN if no such value.
	 */
	public static double varp(Double[] a) {
		if (a.length == 0)
			return Double.NaN;
		double avg = mean(a);
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / a.length;
	}

	/**
	 * Returns the population variance in the subarray a[lo..hi], NaN if no such
	 * value.
	 */
	public static double varp(double[] a, int lo, int hi) {
		int length = hi - lo + 1;
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		if (length == 0)
			return Double.NaN;
		double avg = mean(a, lo, hi);
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += (a[i] - avg) * (a[i] - avg);
		}
		return sum / length;
	}

	/**
	 * Returns the sample standard deviation in the array a[], NaN if no such
	 * value.
	 */
	public static double stddev(Double[] a) {
		return Math.sqrt(var(a));
	}

	/**
	 * Returns the sample standard deviation in the subarray a[lo..hi], NaN if
	 * no such value.
	 */
	public static double stddev(double[] a, int lo, int hi) {
		return Math.sqrt(var(a, lo, hi));
	}

	/**
	 * Returns the sample standard deviation in the array a[], NaN if no such
	 * value.
	 */
	public static double stddev(Integer[] a) {
		return Math.sqrt(var(a));
	}

	/**
	 * Returns the population standard deviation in the array a[], NaN if no
	 * such value.
	 */
	public static double stddevp(Double[] a) {
		return Math.sqrt(varp(a));
	}

	/**
	 * Returns the population standard deviation in the subarray a[lo..hi], NaN
	 * if no such value.
	 */
	public static double stddevp(double[] a, int lo, int hi) {
		return Math.sqrt(varp(a, lo, hi));
	}

	/**
	 * Returns the sum of all values in the array a[].
	 */
	public static double sum(Double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Returns the sum of all values in the subarray a[lo..hi].
	 */
	public static double sum(double[] a, int lo, int hi) {
		if (lo < 0 || hi >= a.length || lo > hi)
			throw new RuntimeException("Subarray indices out of bounds");
		double sum = 0.0;
		for (int i = lo; i <= hi; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Returns the sum of all values in the array a[].
	 */
	public static int sum(int[] a) {
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum;
	}

	/**
	 * Checks if the value is equals to Double.MAX_VALUE or Double.MIN_VALUE or
	 * the negative of these numbers
	 *
	 * @param value
	 * @return
	 */
	public static boolean isMaxOrMinValue(double value) {
		if (Double.compare(Double.MAX_VALUE, value) == 0 || Double.compare(Double.MIN_VALUE, value) == 0)
			return true;
		if (Double.compare(Double.MAX_VALUE, -value) == 0 || Double.compare(Double.MIN_VALUE, -value) == 0)
			return true;
		return false;
	}

	/**
	 * Calculates de median absolute deviation (MAD)
	 *
	 * @param values
	 * @return
	 */
	public static double mad(double[] values) {
		final Median medianCalculator = new Median();
		double median = medianCalculator.evaluate(values);

		double[] tmp = new double[values.length];
		for (int i = 0; i < values.length; i++) {
			double value = values[i];
			tmp[i] = Math.abs(value - median);
		}
		double ret = medianCalculator.evaluate(tmp);
		return ret;
	}

	/**
	 * This is an outliers test: Mi=0.6745(xi−x~)/MAD where MAD is the median
	 * absolute deviation x~ is the median of the population and xi is the value
	 * to test wether is an outlier or not.<br>
	 *
	 *
	 * @param valueToTest
	 * @param populationValues
	 * @return if the return value is greater than 3.5, then it should be
	 *         considered as a potential outlier.
	 */
	public static double iglewiczHoaglinTest(double valueToTest, double[] populationValues) {
		final double factor = 0.6745;
		final Median medianCalculator = new Median();
		double populationMedian = medianCalculator.evaluate(populationValues);
		double mad = mad(populationValues);

		double ret = Math.abs(factor * (valueToTest - populationMedian) / mad);
		return ret;

	}

	/**
	 * Z-score calculation: (value-mean)/stdev a z-score greater than 3 could be
	 * considered as an outlier
	 *
	 * @param valueToTest
	 * @param populationValues
	 * @return
	 */
	public static double zScore(double valueToTest, double[] populationValues) {

		double populationMean = new Mean().evaluate(populationValues);
		double populationSTD = new StandardDeviation().evaluate(populationValues);

		double ret = (valueToTest - populationMean) / populationSTD;
		return ret;

	}

	public static void main(String[] args) {
		double[] data = { 1.0, 1.02, 1.4, 2, 1.01, -12, -2, -21.2, -0.1, -2.1, 0.02 };

		for (int i = 0; i < 100; i++) {
			List<Double> dataSet = new ArrayList<Double>();
			for (double double1 : data) {
				dataSet.add(double1);
			}
			double[] dataTMP = new double[data.length];
			int j = 0;
			Collections.shuffle(dataSet);
			for (double d : dataSet) {
				System.out.print(d + "\t");
				dataTMP[j++] = d;
			}

			System.out.println(iglewiczHoaglinTest(3.0, dataTMP));
		}

	}
}
