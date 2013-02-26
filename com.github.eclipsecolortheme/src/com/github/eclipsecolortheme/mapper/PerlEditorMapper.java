package com.github.eclipsecolortheme.mapper;

import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.github.eclipsecolortheme.ColorThemeMapping;
import com.github.eclipsecolortheme.ColorThemeSetting;

public class PerlEditorMapper extends GenericMapper {

	private static final String ABSTRACT_TEXT_EDITOR_COLOR_FOREGROUND_SYSTEM_DEFAULT = "AbstractTextEditor.Color.Foreground.SystemDefault";
	private static final String ABSTRACT_TEXT_EDITOR_COLOR_BACKGROUND_SYSTEM_DEFAULT = "AbstractTextEditor.Color.Background.SystemDefault";

	private class Mapping extends ColorThemeMapping {

		public Mapping(String pluginKey, String themeKey) {
			super(pluginKey, themeKey);
		}

		@Override
		public void putPreferences(IEclipsePreferences preferences,
				ColorThemeSetting setting) {
			preferences.put(pluginKey, setting.getColor().asRGB());
			if (setting.isBoldEnabled() != null)
				preferences.putBoolean(pluginKey + "Bold",
						setting.isBoldEnabled());
		}

	}

	@Override
	protected ColorThemeMapping createMapping(String pluginKey, String themeKey) {
		return new Mapping(pluginKey, themeKey);
	}

	@Override
	public void map(Map<String, ColorThemeSetting> theme) {
		preferences.putBoolean(
				ABSTRACT_TEXT_EDITOR_COLOR_BACKGROUND_SYSTEM_DEFAULT, false);
		preferences.putBoolean(
				ABSTRACT_TEXT_EDITOR_COLOR_FOREGROUND_SYSTEM_DEFAULT, false);
		super.map(theme);
	}

	@Override
	public void clear() {
		preferences
				.remove(ABSTRACT_TEXT_EDITOR_COLOR_BACKGROUND_SYSTEM_DEFAULT);
		preferences
				.remove(ABSTRACT_TEXT_EDITOR_COLOR_FOREGROUND_SYSTEM_DEFAULT);
		super.clear();
	}
}
