package com.github.eclipsecolortheme;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;

public class ColorThemeMapping {

	protected String pluginKey;
	protected String themeKey;

	public ColorThemeMapping(String pluginKey, String themeKey) {
		this.pluginKey = pluginKey;
		this.themeKey = themeKey;
	}

	public void putPreferences(IEclipsePreferences preferences,
			ColorThemeSetting setting) {
		Color color = setting.getColor();
		if(color == null){
			System.err.println("Error: cannot map color: "+pluginKey+" because foreground color is null.");
			return;
		}
		String string = color.toString();
		String curr = preferences.get(pluginKey, "");
		if (!string.equals(curr)) {
			preferences.put(pluginKey, string);
		}
	}

	public void removePreferences(IEclipsePreferences preferences) {
		preferences.remove(pluginKey);
	}

	public Object getThemeKey() {
		return themeKey;
	}

}
