package com.github.eclipsecolortheme.mapper;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.github.eclipsecolortheme.ColorThemeMapping;
import com.github.eclipsecolortheme.ColorThemeSetting;

public class LiClipseEditorMapper extends GenericMapper {

	private class Mapping extends ColorThemeMapping {

		public static final int NORMAL = 0;

		public static final int BOLD = 1 << 0;

		public static final int ITALIC = 1 << 1;

		// See: TextAttribute.UNDERLINE
		public static final int UNDERLINE = 1 << 30;

		// See: TextAttribute.STRIKETHROUGH
		public static final int STRIKETHROUGH = 1 << 29;

		public Mapping(String pluginKey, String themeKey) {
			super(pluginKey, themeKey);
		}

		@Override
		public void putPreferences(IEclipsePreferences preferences,
				ColorThemeSetting setting) {
			preferences.put(pluginKey + "_COLOR", setting.getColor().asRGB());

			String styleKey = pluginKey + "_STYLE";
			int styleVal = NORMAL;
			if (setting.isBoldEnabled() != null && setting.isBoldEnabled()) {
				styleVal |= BOLD;
			}
			if (setting.isItalicEnabled() != null && setting.isItalicEnabled()) {
				styleVal |= ITALIC;
			}
			if (setting.isUnderlineEnabled() != null
					&& setting.isUnderlineEnabled()) {
				styleVal |= UNDERLINE;
			}
			if (setting.isStrikethroughEnabled() != null
					&& setting.isStrikethroughEnabled()) {
				styleVal |= STRIKETHROUGH;
			}
			preferences.putInt(styleKey, styleVal);
		}
	}

	@Override
	protected ColorThemeMapping createMapping(String pluginKey, String themeKey) {
		return new Mapping(pluginKey, themeKey);
	}

	@Override
	public void clear() {
		for (String pluginKey : mappings.keySet()) {
			preferences.remove(pluginKey + "_COLOR");
			preferences.remove(pluginKey + "_STYLE");
		}
	}

}
