package com.github.eclipsecolortheme.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.junit.Test;

import com.github.eclipsecolortheme.ColorTheme;
import com.github.eclipsecolortheme.ColorThemeKeys;
import com.github.eclipsecolortheme.ColorThemeSetting;
import com.github.eclipsecolortheme.preferences.ColorThemePreferencesPageStyledText;
import com.github.eclipsecolortheme.preferences.ColorThemePreferencesPageStyledText.IStyledText;

public class ColorthemePreferencePageTest {

	@Test
	public void testStyledText() throws Exception {
		IStyledText styledText = new IStyledText() {

			public void setText(String text) {
				Assert.assertFalse(text.contains("|"));
			}

			public void setStyleRanges(StyleRange[] ranges) {
				Assert.assertEquals(8, ranges.length); // Only for what we have
														// colors (i.e.: in the
														// test only keywords).
			}

			public void setBackground(Color backgroundSwtColor) {
				Assert.assertNotNull(backgroundSwtColor);
			}

			public void setForeground(Color foregroundColor) {
				Assert.assertNotNull(foregroundColor);

			}
		};
		ColorTheme theme = new ColorTheme();
		Map<String, ColorThemeSetting> entries = new HashMap<String, ColorThemeSetting>();
		theme.setEntries(entries);
		entries.put(ColorThemeKeys.BACKGROUND, new ColorThemeSetting("#000000"));
		entries.put(ColorThemeKeys.FOREGROUND, new ColorThemeSetting("#ffffff"));
		entries.put(ColorThemeKeys.KEYWORD, new ColorThemeSetting("#ffffff"));
		ColorThemePreferencesPageStyledText.updateStyledText(styledText, theme);
	}
}
