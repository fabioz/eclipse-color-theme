package com.github.eclipsecolortheme;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.swt.graphics.FontData;

public class ColorThemeSemanticHighlightingMapping extends ColorThemeMapping {
	protected String separator = ".";

	public ColorThemeSemanticHighlightingMapping(String pluginKey,
			String themeKey) {
		super(pluginKey, themeKey);
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	@Override
	public void putPreferences(IEclipsePreferences preferences,
			ColorThemeSetting setting) {
		preferences.putBoolean(pluginKey + separator + "enabled", true);
		preferences.put(pluginKey + separator + "color", setting.getColor()
				.asRGB());
		Color backgroundColor = setting.getBackgroundColor();
		if(backgroundColor != null){
			preferences.put(pluginKey + separator + "backgroundColor", backgroundColor.asRGB());
		}
		if (setting.isBoldEnabled() != null)
			preferences.putBoolean(pluginKey + separator + "bold",
					setting.isBoldEnabled());
		if (setting.isItalicEnabled() != null)
			preferences.putBoolean(pluginKey + separator + "italic",
					setting.isItalicEnabled());
		if (setting.isUnderlineEnabled() != null)
			preferences.putBoolean(pluginKey + separator + "underline",
					setting.isUnderlineEnabled());
		if (setting.isStrikethroughEnabled() != null)
			preferences.putBoolean(pluginKey + separator + "strikethrough",
					setting.isStrikethroughEnabled());
		if (setting.isBackgroundEnabled() != null)
			preferences.putBoolean(pluginKey + separator + "backgroundEnabled",
					setting.isBackgroundEnabled());
		if (setting.useCustomFont() != null){
			preferences.putBoolean(pluginKey + separator + "useCustomFont",
					setting.useCustomFont());
		}
		
		FontData font = setting.getFont();
		if(font != null){
			preferences.put(pluginKey + separator + "font", font.toString());
		}
	}

	public void removePreferences(IEclipsePreferences preferences) {
		preferences.remove(pluginKey + separator + "enabled");
		preferences.remove(pluginKey + separator + "color");
		preferences.remove(pluginKey + separator + "bold");
		preferences.remove(pluginKey + separator + "italic");
		preferences.remove(pluginKey + separator + "underline");
		preferences.remove(pluginKey + separator + "strikethrough");
		preferences.remove(pluginKey + separator + "backgroundColor");
		preferences.remove(pluginKey + separator + "backgroundEnabled");
		preferences.remove(pluginKey + separator + "font");
		preferences.remove(pluginKey + separator + "useCustomFont");
	}
}
