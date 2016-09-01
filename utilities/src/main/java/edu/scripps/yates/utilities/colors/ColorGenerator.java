package edu.scripps.yates.utilities.colors;

import java.awt.Color;

import edu.scripps.yates.utilities.maths.Maths;

public class ColorGenerator {
	/**
	 * Scale a color by multiplying a factor. Values greater than 1 will add for
	 * bright
	 *
	 * @param c
	 * @param scale
	 * @return
	 */
	public static Color brightness(Color c, double scale) {
		int r = Math.min(255, (int) (c.getRed() * scale));
		int g = Math.min(255, (int) (c.getGreen() * scale));
		int b = Math.min(255, (int) (c.getBlue() * scale));
		return new Color(r, g, b);
	}
	// public static Color getColor(float power) {
	// float H = power * 0.4f; // Hue (note 0.4 = Green, see huge chart below)
	// float S = 0.9f; // Saturation
	// float B = 0.9f; // Brightness
	//
	// return Color.getHSBColor(H, S, B);
	// }

	private static float transition(float value, float maximum, float start_point, float end_point) {
		float f = start_point + (end_point - start_point) * value / maximum;
		if (f < 0) {
			f = 0f;
		}
		return f;
	}

	private static Triplet transitionTriplet(float value, float maximum, Triplet t1, Triplet t2) {
		float r1 = transition(value, maximum, t1.value1, t2.value1);

		float r2 = transition(value, maximum, t1.value2, t2.value2);
		float r3 = transition(value, maximum, t1.value3, t2.value3);
		return new Triplet(r1, r2, r3);
	}

	/**
	 * Gets a Color representing the value in which value is located in between
	 * a given numerical range, in between a color range
	 *
	 * @param value
	 * @param minimum
	 * @param maximum
	 * @param minColor
	 * @param maxColor
	 * @return
	 */
	public static Color getColor(double value, double minimum, double maximum, Color minColor, Color maxColor) {
		return getColor(Double.valueOf(value).floatValue(), Double.valueOf(minimum).floatValue(),
				Double.valueOf(maximum).floatValue(), minColor, maxColor);
	}

	/**
	 * Gets a Color representing the value in which value is located in between
	 * a given numerical range, in between a color range
	 *
	 * @param value
	 * @param minimum
	 * @param maximum
	 * @param minColor
	 * @param maxColor
	 * @return
	 */
	public static Color getColor(float value, float minimum, float maximum, Color minColor, Color maxColor) {
		if (value < minimum || value > maximum) {
			throw new IllegalArgumentException(value + " is not in the range [" + minimum + "," + maximum + "]");
		}
		if (minimum > maximum) {
			throw new IllegalArgumentException("Min and max are not well defined [" + minimum + "," + maximum + "]");
		}
		// transform to an scale from 0 to maximum
		float normalizedNumber = ((value - minimum) / (maximum - minimum)) * (maximum - 0) + 0;

		// System.out.println(value + " normalized to " + normalizedNumber);
		return getColor(normalizedNumber, maximum, minColor, maxColor);
	}

	private static Color getColor(float value, float maximum, Color minColor, Color maxColor) {
		final Triplet rgb_to_hsv = rgb_to_hsv(getTriplet(minColor));
		final Triplet rgb_to_hsv2 = rgb_to_hsv(getTriplet(maxColor));
		final Triplet hsvColor = getHSVInterpolation(value, maximum, rgb_to_hsv, rgb_to_hsv2);
		final Triplet rgbColor = hsv_to_rgb(hsvColor);
		return getColor(rgbColor);
	}

	private static Color getColor(Triplet color) {
		final float r = color.value1 / 255;
		final float g = color.value2 / 255;
		final float b = color.value3 / 255;
		return new Color(r, g, b);
	}

	private static Triplet getTriplet(Color color) {
		return new Triplet(color.getRed(), color.getGreen(), color.getBlue());
	}

	private static Triplet getHSVInterpolation(float value, float maximum, Triplet startHSVTriplet,
			Triplet endHSVTriplet) {
		Triplet ret = transitionTriplet(value, maximum, startHSVTriplet, endHSVTriplet);
		return ret;
	}

	private static Triplet rgb_to_hsv(Triplet triplet) {
		float r = triplet.value1;
		float g = triplet.value2;
		float b = triplet.value3;
		float maxc = Maths.max(r, g, b);
		float minc = Maths.min(r, g, b);
		float v = maxc;
		if (Float.compare(minc, maxc) == 0) {
			return new Triplet(0, 0, v);
		}

		float diff = maxc - minc;
		float s = diff / maxc;
		float rc = (maxc - r) / diff;
		float gc = (maxc - g) / diff;
		float bc = (maxc - b) / diff;
		float h;
		if (Float.compare(r, maxc) == 0) {
			h = bc - gc;
		} else if (Float.compare(g, maxc) == 0) {
			h = 2.0f + rc - bc;
		} else {
			h = 4.0f + gc - rc;
		}
		h = (h / 6.0f) % 1.0f;// comment: this calculates only the fractional
								// part of h/6
		return new Triplet(h, s, v);
	}

	private static Triplet hsv_to_rgb(Triplet triplet) {
		float h = triplet.value1;
		float s = triplet.value2;
		float v = triplet.value3;

		if (Float.compare(s, 0.0f) == 0) {
			return new Triplet(v, v, v);
		}
		int i = Double.valueOf(Math.floor(h * 6.0f)).intValue(); // comment:
																	// floor()
																	// should
																	// drop
																	// the
																	// fractional
																	// part
		float f = (h * 6.0f) - i;
		float p = v * (1.0f - s);
		float q = v * (1.0f - s * f);
		float t = v * (1.0f - s * (1.0f - f));
		if (i % 6 == 0) {
			return new Triplet(v, t, p);
		}
		if (i == 1) {
			return new Triplet(q, v, p);
		}
		if (i == 2) {
			return new Triplet(p, v, t);
		}
		if (i == 3) {
			return new Triplet(p, q, v);
		}
		if (i == 4) {
			return new Triplet(t, p, v);
		}
		if (i == 5) {
			return new Triplet(v, p, q);
		}
		return null;
		// comment: 0 <= i <= 6, so we never come here
	}
}
