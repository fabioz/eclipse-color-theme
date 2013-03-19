package com.github.eclipsecolortheme.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.custom.StyleRange;

import com.github.eclipsecolortheme.ColorTheme;
import com.github.eclipsecolortheme.ColorThemeKeys;
import com.github.eclipsecolortheme.ColorThemeSetting;

public class ColorThemePreferencesPageStyledText {

	public static interface IStyledText {

		public void setText(String text);

		public void setStyleRanges(StyleRange[] ranges);

		public void setBackground(
				org.eclipse.swt.graphics.Color backgroundSwtColor);

		public void setForeground(org.eclipse.swt.graphics.Color foregroundColor);

	}

	public static void updateStyledText(IStyledText styledText, ColorTheme theme) {
		String text = ""
				+ "|keyword|public class|keyword| |class|Demo|class| {\n"
				+ "    |keyword|private static final|keyword| |class|String|class| |staticfinalfield|CONSTANT|staticfinalfield| |operator|=|operator| |string|\"String\"|string|;\n"
				+ "    |keyword|private|keyword| |class|Object|class| |field|o|field|;\n"
				+ "    |javadoc|/**\n"
				+ "     * Creates a new demo.\n"
				+ "     * @param o The object to demonstrate.\n"
				+ "     */|javadoc|\n"
				+ "    |keyword|public|keyword| Demo(|class|Object|class| |parametervariable|o|parametervariable|) {\n"
				+ "        |keyword|this|keyword|.|field|o|field| |operator|=|operator| |parametervariable|o|parametervariable|;\n"
				+ "        |class|String|class| |localvariabledeclaration|s|localvariabledeclaration| |operator|=|operator| |staticfinalfield|CONSTANT|staticfinalfield|;\n"
				+ "        |keyword|int|keyword| |localvariabledeclaration|i|localvariabledeclaration| |operator|=|operator| |number|1|number|;\n"
				+ "    }\n"
				+ "    |keyword|public static void|keyword| |methoddeclaration|main|methoddeclaration|(|class|String|class|[] |parametervariable|args|parametervariable|) {\n"
				+ "        |class|Demo|class| |localvariabledeclaration|demo|localvariabledeclaration| |operator|=|operator| |keyword|new|keyword| |method|Demo|method|();\n"
				+ "    }\n" + "}\n" + "";

		StringBuffer buf = new StringBuffer();
		StringBuffer bufInner = new StringBuffer();
		int start = -1;
		int end = -1;

		List<StyleRange> ranges = new ArrayList<StyleRange>();
		Map<String, ColorThemeSetting> entries = theme.getEntries();
		if (entries == null) {
			styledText.setText("Empty theme");
			return;
		}
		ColorThemeSetting background = entries.get(ColorThemeKeys.BACKGROUND);
		if (background == null) {
			styledText.setText("Empty background");
			return;
		}

		ColorThemeSetting foreground = entries.get(ColorThemeKeys.FOREGROUND);
		if (foreground == null) {
			styledText.setText("Empty foreground");
			return;
		}
		ColorCache cache = ColorCache.getSingleton();

		org.eclipse.swt.graphics.Color backgroundColor = cache
				.getColor(background.getColor().getRGB());
		org.eclipse.swt.graphics.Color foregroundColor = cache
				.getColor(foreground.getColor().getRGB());
		styledText.setForeground(foregroundColor);
		styledText.setBackground(backgroundColor);

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '|') {
				bufInner.setLength(0);
				if (start == -1) {
					start = buf.length();
				} else { // found end
					end = buf.length();
				}
				i++;
				c = text.charAt(i);
				while (c != '|') {
					bufInner.append(c);
					i++;
					c = text.charAt(i);
				}
				i++;
				c = text.charAt(i);

				if (end != -1) {
					ColorThemeSetting colorThemeSetting = entries.get(bufInner
							.toString());
					if (colorThemeSetting != null) {
						ranges.add(new StyleRange(start, end - start,
								cache.getColor(colorThemeSetting.getColor()
										.getRGB()), backgroundColor));
					}
					// System.out.println("start: " + start + " end: " + end);
					start = -1;
					end = -1;
				}

			}
			buf.append(c);
		}
		styledText.setText(buf.toString());
		styledText
				.setStyleRanges(ranges.toArray(new StyleRange[ranges.size()]));

	}
}
