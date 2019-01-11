package edu.scripps.yates.client.style;

public class Color {
	public final static Color WHITE = new Color(255, 255, 255);
	public final static Color LIGHT_GRAY = new Color(192, 192, 192);
	public final static Color GRAY = new Color(128, 128, 128);
	public final static Color DARK_GRAY = new Color(64, 64, 64);
	public final static Color BLACK = new Color(0, 0, 0);
	public final static Color RED = new Color(255, 0, 0);
	public final static Color PINK = new Color(255, 175, 175);
	public final static Color ORANGE = new Color(255, 200, 0);
	public final static Color YELLOW = new Color(255, 255, 0);
	public final static Color GREEN = new Color(0, 255, 0);
	public final static Color MAGENTA = new Color(255, 0, 255);
	public final static Color CYAN = new Color(0, 255, 255);
	public final static Color BLUE = new Color(0, 0, 255);
	public static final Color NONE = new Color("");

	private int r, g, b;

	// only for special cases, like no color, or maybe named colors
	private String colorText = null;

	private Color(String colorText) {
		this.colorText = colorText;
	}

	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public int getRed() {
		return r;
	}

	public int getGreen() {
		return g;
	}

	public int getBlue() {
		return b;
	}

	public String getHexValue() {
		if (colorText != null) {
			return colorText;
		}

		return "#" + pad(Integer.toHexString(r)) + pad(Integer.toHexString(g)) + pad(Integer.toHexString(b));
	}

	private String pad(String in) {
		if (in.length() == 0) {
			return "00";
		}
		if (in.length() == 1) {
			return "0" + in;
		}
		return in;
	}

	@Override
	public String toString() {
		if (colorText != null) {
			return colorText;
		}
		return "red=" + r + ", green=" + g + ", blue=" + b;
	}
}
