package com.github.eclipsecolortheme;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
								((GenericMapper) mapper).parseMappings(input);
							}
						}
					}
					editors.add(mapper);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// printEditors();
	}

	/**
	 * Helper function. Should not be used in final code!
	 */
	private static void printEditors() {
		if (editors == null) {
			System.out.println("NULL editors");
		} else {
			ArrayList<ThemePreferenceMapper> lst = new ArrayList<ThemePreferenceMapper>(
					editors);
			Collections.sort(lst, new Comparator<ThemePreferenceMapper>() {

				public int compare(ThemePreferenceMapper o1,
						ThemePreferenceMapper o2) {
					return o1.getPluginId().compareTo(o2.getPluginId());
				}
			});
			for (ThemePreferenceMapper editor : lst) {
				System.out.println("Editor for plugin:" + editor.getPluginId());
			}
		}

	}

	public interface ICallback<X> {

		void call(X arg);
	}

	/**
	 * This is the callback called from the preferences page to apply the theme.
	 * The default implementation just calls the applyThemeInternal, but it may
	 * be replaced for other clients that want to do more stuff as needed.
	 */
	public static ICallback<ColorTheme> applyTheme = new ICallback<ColorTheme>() {

		public void call(ColorTheme theme) {
			applyThemeInternal(theme);
		}
	};

	public static void applyThemeInternal(ColorTheme theme) {
		applyThemeInternal(theme, null);
	}

	/**
	 * Changes the preferences of other plugins to apply the color theme.
	 * 
	 * @param theme
	 *            the theme to be applied. If null will just remove the colors
	 *            set and restore the defaults.
	 * 
	 */
	public static void applyThemeInternal(ColorTheme theme,
			String applyToPluginId) {
		loadEditors();

		for (ThemePreferenceMapper editor : editors) {
			boolean restorePluginId = false;
			if (applyToPluginId != null) {
				String editorPluginId = editor.getPluginId();
				if (editorPluginId.equals("org.eclipse.ui.editors")) {
					restorePluginId = true;
					editor.setPluginId(applyToPluginId);
				} else {
					if (!applyToPluginId.equals(editorPluginId)) {
						continue;
					}
				}
			}
			try {
				if (theme != null) {
					Map<String, Map<String, ColorThemeMapping>> mappings = theme.getMappings();
					if(mappings != null){
						editor.map(theme.getEntries(), mappings.get(applyToPluginId));
						
					}else{
						editor.map(theme.getEntries(), null);
					}
				} else {
					editor.clear();
				}

				try {
					editor.flush();
				} catch (BackingStoreException e) {
					// TODO: Show a proper error message (StatusManager).
					e.printStackTrace();
				}
			} finally {
				if (restorePluginId) {
					editor.setPluginId("org.eclipse.ui.editors");
				}
			}
		}
	}
}
