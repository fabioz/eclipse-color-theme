package com.github.eclipsecolortheme;

import org.eclipse.swt.graphics.RGB;

public final class Color {

	private int r;
	private int g;
	private int b;
	private int a;

	public int getAlpha() {
		return a;
	}

	public Color(String value) {
		if (value != null) {
			if (value.startsWith("#")) {
				r = Integer.parseInt(value.substring(1, 3), 16);
				g = Integer.parseInt(value.substring(3, 5), 16);
				b = Integer.parseInt(value.substring(5, 7), 16);
				if (value.length() > 7) {
					a = Integer.parseInt(value.substring(7, 9), 16);
				} else {
					a = 255;
				}

				if (r < 0) {
					r = 0;
				}
				if (g < 0) {
					g = 0;
				}
				if (b < 0) {
					b = 0;
				}
				if (r > 255) {
					r = 255;
				}
				if (g > 255) {
					g = 255;
				}
				if (b > 255) {
					b = 255;
				}
			} else {
				// Not in hexa: i.e.: r,g,b comma-separated.
				String[] s = value.split("\\,");
				if (s.length == 3) {
					r = Integer.parseInt(s[0]);
					g = Integer.parseInt(s[1]);
					b = Integer.parseInt(s[2]);
					a = 255;

				} else {
					System.err.println("Unable to recognize: " + value);
					r = 0;
					g = 0;
					b = 0;
					a = 255;
				}
			}
		} else {
			r = 0;
			g = 0;
			b = 0;
			a = 255;
		}
	}

	public Color(int r, int g, int b) {
		this(r, g, b, 255);

	}

	public Color(RGB colorValue) {
		this(colorValue.red, colorValue.green, colorValue.blue);
	}

	public Color(RGB colorValue, int a) {
		this(colorValue.red, colorValue.green, colorValue.blue, a);
	}

	public Color(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;

		if (r < 0) {
			r = 0;
		}
		if (g < 0) {
			g = 0;
		}
		if (b < 0) {
			b = 0;
		}
		if (a < 0) {
			a = 0;
		}
		if (r > 255) {
			r = 255;
		}
		if (g > 255) {
			g = 255;
		}
		if (b > 255) {
			b = 255;
		}
		if (a > 255) {
			a = 255;
		}
	}

	public int getR() {
		return r;
	}

	public int getG() {
		return g;
	}

	public int getB() {
		return b;
	}

	public String asHex() {
		String hexr = Integer.toHexString(r).toUpperCase();
		String hexg = Integer.toHexString(g).toUpperCase();
		String hexb = Integer.toHexString(b).toUpperCase();
		return "#" + (hexr.length() == 2 ? hexr : "0" + hexr) + "" + (hexg.length() == 2 ? hexg : "0" + hexg) + ""
				+ (hexb.length() == 2 ? hexb : "0" + hexb);
	}

	public String asRGB() {
		return r + "," + g + "," + b;
	}

	@Override
	public String toString() {
		return r + "," + g + "," + b;
	}

	public RGB getRGB() {
		return new RGB(r, g, b);
	}

	public boolean isDarkColor() {
		return isDarkColor(r, g, b);
	}

	public static boolean isDarkColor(int r, int g, int b) {
		double v = (r / 255.0) * 0.3 + (g / 255.0) * 0.59 + (b / 255.0) * 0.11;
		return v <= 0.5;
	}

	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Color)) {
			return false;
		}
		Color other = (Color) object;
		return (other.r == this.r) && (other.g == this.g) && (other.b == this.b);
	}

	public int hashCode() {
		return (b << 16) | (g << 8) | r;
	}

	public RGB redistributeRGB(double r, double g, double b) {
		double threshold = 255.999;
		double m = Math.max(r, g);
		m = Math.max(m, b);
		if (m <= threshold) {
			return new RGB((int) r, (int) b, (int) g);
		}
		double total = r + g + b;
		if (total >= threshold * 3) {
			return new RGB((int) threshold, (int) threshold, (int) threshold);
		}
		double x = (3. * threshold - total) / (3. * m - total);
		double gray = threshold - x * m;
		return new RGB((int) (gray + x * r), (int) (gray + x * g), (int) (gray + x * b));
	}

	public RGB lighterRGB(double percentage) {
		return lighterRGB(percentage, 0);
	}

	private RGB lighterRGB(double percentage, int attempt) {
		double diff = 255.0 * percentage;
		double newR = (r + diff);
		double newG = (g + diff);
		double newB = (b + diff);
		if (newR > 255 || newG > 255 || newB > 255) {
			if (attempt == 3) {
				if (newR > 255) {
					newR = 255;
				}
				if (newG > 255) {
					newG = 255;
				}
				if (newB > 255) {
					newB = 255;
				}
			} else {
				return lighterRGB(percentage - 0.03, attempt + 1);
			}
		}
		if (newR < 0) {
			newR = 0;
		}
		if (newG < 0) {
			newG = 0;
		}
		if (newB < 0) {
			newB = 0;
		}
		return new RGB((int) newR, (int) newG, (int) newB);
	}

	public static double getLuminance(RGB rgb) {
		return (0.2126 * rgb.red + 0.7152 * rgb.green + 0.0722 * rgb.blue);
	}

	public double getLuminance() {
		return (0.2126 * r + 0.7152 * g + 0.0722 * b);
	}

	public RGB darkerRGB(double percentage) {
		return darkerRGB(percentage, 0);
	}

	private RGB darkerRGB(double percentage, int attempt) {
		double diff = 255 * percentage;
		double newR = (r - diff);
		double newG = (g - diff);
		double newB = (b - diff);
		if (newR < 0 || newG < 0 || newB < 0) {
			if (attempt == 3) {
				if (newR < 0) {
					newR = 0;
				}
				if (newG < 0) {
					newG = 0;
				}
				if (newB < 0) {
					newB = 0;
				}
			} else {
				return darkerRGB(percentage - 0.03, attempt + 1);
			}
		}
		if (newR > 255) {
			newR = 255;
		}
		if (newG > 255) {
			newG = 255;
		}
		if (newB > 255) {
			newB = 255;
		}
		return new RGB((int) newR, (int) newG, (int) newB);
	}

	public Color blend(Color backgroundColor) {
		double localR = r / 255.0;
		double localG = g / 255.0;
		double localB = b / 255.0;
		double a = this.a / 255.0;

		double oR = backgroundColor.r / 255.0;
		double oG = backgroundColor.g / 255.0;
		double oB = backgroundColor.b / 255.0;

		return new Color((int) ((localR * a) + (oR * (1.0 - a)) * 255.0),
				(int) ((localG * a) + (oG * (1.0 - a)) * 255.0), (int) ((localB * a) + (oB * (1.0 - a)) * 255.0), 255);

	}

}
