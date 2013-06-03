package com.github.eclipsecolortheme.mapper;

import java.util.Map;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import com.github.eclipsecolortheme.ColorThemeSetting;

public class TextEditorMapper extends GenericMapper {

	private static final String ABSTRACT_TEXT_EDITOR_COLOR_SELECTION_FOREGROUND_SYSTEM_DEFAULT = "AbstractTextEditor.Color.SelectionForeground.SystemDefault";
	private static final String ABSTRACT_TEXT_EDITOR_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT = "AbstractTextEditor.Color.SelectionBackground.SystemDefault";
	private static final String ABSTRACT_TEXT_EDITOR_COLOR_FOREGROUND_SYSTEM_DEFAULT = "AbstractTextEditor.Color.Foreground.SystemDefault";
	private static final String ABSTRACT_TEXT_EDITOR_COLOR_BACKGROUND_SYSTEM_DEFAULT = "AbstractTextEditor.Color.Background.SystemDefault";

	@Override
	public void map(Map<String, ColorThemeSetting> theme) {
		// System.out.println("Applying: " + preferences);
		putBoolean(preferences,
				ABSTRACT_TEXT_EDITOR_COLOR_BACKGROUND_SYSTEM_DEFAULT, false);
		putBoolean(preferences,
				ABSTRACT_TEXT_EDITOR_COLOR_FOREGROUND_SYSTEM_DEFAULT, false);
		putBoolean(preferences,
				ABSTRACT_TEXT_EDITOR_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT,
				false);
		putBoolean(preferences,
				ABSTRACT_TEXT_EDITOR_COLOR_SELECTION_FOREGROUND_SYSTEM_DEFAULT,
				false);
		super.map(theme);
	}

	private void putBoolean(IEclipsePreferences preferences, String key,
			boolean b) {
		if (preferences.getBoolean(key, true) != b) {
			preferences.putBoolean(key, b);
		}
	}

	@Override
	public void clear() {
		// System.out.println("Clearing TextEditorMapper: " + preferences);
		preferences
				.remove(ABSTRACT_TEXT_EDITOR_COLOR_BACKGROUND_SYSTEM_DEFAULT);
		preferences
				.remove(ABSTRACT_TEXT_EDITOR_COLOR_FOREGROUND_SYSTEM_DEFAULT);
		preferences
				.remove(ABSTRACT_TEXT_EDITOR_COLOR_SELECTION_BACKGROUND_SYSTEM_DEFAULT);
		preferences
				.remove(ABSTRACT_TEXT_EDITOR_COLOR_SELECTION_FOREGROUND_SYSTEM_DEFAULT);
		super.clear();
	}

}
