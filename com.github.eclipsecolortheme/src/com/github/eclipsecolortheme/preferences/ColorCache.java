package com.github.eclipsecolortheme.preferences;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorCache {

	// Used to check that we're only accessing it in the UI thread.
	private static Thread lastThread;

	private static void checkThread() throws AssertionError {
		if (lastThread == null) {
			lastThread = Thread.currentThread();
		} else {
			if (lastThread != Thread.currentThread()) {
				throw new AssertionError(
						"This class can only be accessed by the UI thread.");
			}
		}
	}

	private static ColorCache colorCache;

	public static final ColorCache getSingleton() {
		checkThread();
		if (colorCache == null) {
			colorCache = new ColorCache();
		}
		return colorCache;
	}

	private Map<RGB, Color> rgbToColor = new HashMap<RGB, Color>();

	public Color getColor(RGB rgb) {
		checkThread();
		Color color = (Color) rgbToColor.get(rgb);
		if (color == null) {
			color = new Color(Display.getCurrent(), rgb);
			rgbToColor.put(rgb, color);
		}
		return color;
	}

	public void dispose() {
		checkThread();
		Collection<Color> values = rgbToColor.values();
		for (Color color : values) {
			color.dispose();
		}
		rgbToColor.clear();
	}
}
