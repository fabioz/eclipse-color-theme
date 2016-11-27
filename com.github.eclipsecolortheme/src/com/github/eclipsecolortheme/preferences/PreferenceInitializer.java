package com.github.eclipsecolortheme.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.eclipsecolortheme.Activator;

/** Initializes this plugin's preferences. */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(Activator.CURRENT_COLOR_THEME, "default");
		store.setDefault(Activator.APPLY_THEME_TO,
				Activator.APPLY_THEME_TO_LICLIPSE);
		store.setDefault("forceDefaultBG", false);

		//Ideally we'd use ScrollBar.getScrollbarsMode(), but that'd require creating a widget
		//that has a scrollbar, so, let's make this only true on windows where the issue is
		//more annoying.
		store.setDefault(Activator.THEME_STYLED_TEXT_SCROLLBARS, PlatformUtils.isWindowsPlatform());
	}
}
