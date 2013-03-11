package com.github.eclipsecolortheme;

import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;

import com.github.eclipsecolortheme.mapper.GenericMapper;
import com.github.eclipsecolortheme.mapper.ThemePreferenceMapper;

public class ColorThemeApplier {

	private static Set<ThemePreferenceMapper> editors;

	public static void loadEditors() {
		if (editors != null) {
			return;
		}
		editors = new HashSet<ThemePreferenceMapper>();
		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						Activator.EXTENSION_POINT_ID_MAPPER);
		try {
			for (IConfigurationElement e : config) {
				final Object o = e.createExecutableExtension("class");
				if (o instanceof ThemePreferenceMapper) {
					String pluginId = e.getAttribute("pluginId");
					ThemePreferenceMapper mapper = (ThemePreferenceMapper) o;
					mapper.setPluginId(pluginId);
					if (o instanceof GenericMapper) {
						String xml = e.getAttribute("xml");
						String contributorPluginId = e.getContributor()
								.getName();
						Bundle bundle = Platform.getBundle(contributorPluginId);
						if (bundle != null) {
							URL resource = bundle.getResource(xml);
							if (resource != null) {
								InputStream input = (InputStream) resource
										.getContent();
								((GenericMapper) mapper).parseMapping(input);
							}
						}
					}
					editors.add(mapper);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Changes the preferences of other plugins to apply the color theme.
	 * 
	 * @param theme
	 *            the theme to be applied. If null will just remove the colors
	 *            set and restore the defaults.
	 * 
	 */
	public static void applyTheme(ColorTheme theme) {
		loadEditors();
		for (ThemePreferenceMapper editor : editors) {
			if (theme != null) {
				Map<String, ColorThemeSetting> entries = theme.getEntries();
				editor.map(entries);
			} else {
				editor.clear();
			}

			try {
				editor.flush();
			} catch (BackingStoreException e) {
				// TODO: Show a proper error message (StatusManager).
				e.printStackTrace();
			}
		}
	}
}
