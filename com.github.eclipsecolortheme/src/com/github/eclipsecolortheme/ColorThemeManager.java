package com.github.eclipsecolortheme;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.RGB;
import org.osgi.framework.Bundle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Loads and applies color themes. */
public class ColorThemeManager {

	private Map<String, ColorTheme> themes;

	/**
	 * Cache for stock themes.
	 */
	private static Map<String, ColorTheme> stockThemes;

	/** Creates a new color theme manager. */
	public ColorThemeManager() {
		themes = new HashMap<String, ColorTheme>();
		if (stockThemes == null) {
			stockThemes = new HashMap<String, ColorTheme>();
			readStockThemes(stockThemes);

		}
		themes.putAll(stockThemes);
		readImportedThemes(themes);
	}

	private static void readStockThemes(Map<String, ColorTheme> themes) {
		IConfigurationElement[] config = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor(Activator.EXTENSION_POINT_ID_THEME);
		try {
			for (IConfigurationElement e : config) {
				String xml = e.getAttribute("file");
				String contributorPluginId = e.getContributor().getName();
				Bundle bundle = Platform.getBundle(contributorPluginId);
				InputStream input = (InputStream) bundle.getResource(xml)
						.getContent();
				ColorTheme theme = parseTheme(input);
				amendThemeEntries(theme.getEntries());
				themes.put(theme.getName(), theme);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void readImportedThemes(Map<String, ColorTheme> themes) {
		IPreferenceStore store = getPreferenceStore();

		for (int i = 1;; i++) {
			String xml = store.getString("importedColorTheme" + i);
			if (xml == null || xml.length() == 0)
				break;
			try {
				ColorTheme theme = parseTheme(new ByteArrayInputStream(
						xml.getBytes()));
				amendThemeEntries(theme.getEntries());
				themes.put(theme.getName(), theme);
			} catch (Exception e) {
				System.err.println("Error while parsing imported theme");
				e.printStackTrace();
			}
		}
	}

	public void clearImportedThemes() {
		IPreferenceStore store = getPreferenceStore();
		for (int i = 1; store.contains("importedColorTheme" + i); i++)
			store.setToDefault("importedColorTheme" + i);
		themes.clear();
		readStockThemes(themes);
	}

	private static IPreferenceStore getPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();
	}

	public static ColorTheme parseTheme(InputStream input)
			throws ParserConfigurationException, SAXException, IOException {
		ColorTheme theme = new ColorTheme();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(input);
		Element root = document.getDocumentElement();
		theme.setId(root.getAttribute("id"));
		theme.setName(root.getAttribute("name"));
		theme.setAuthor(root.getAttribute("author"));
		theme.setWebsite(root.getAttribute("website"));

		Map<String, ColorThemeSetting> entries = new HashMap<String, ColorThemeSetting>();
		NodeList entryNodes = root.getChildNodes();
		for (int i = 0; i < entryNodes.getLength(); i++) {
			Node entryNode = entryNodes.item(i);
			if (entryNode.hasAttributes()) {
				String color = entryNode.getAttributes().getNamedItem("color")
						.getNodeValue();
				Node nodeBold = entryNode.getAttributes().getNamedItem("bold");
				Node nodeItalic = entryNode.getAttributes().getNamedItem(
						"italic");
				Node nodeUnderline = entryNode.getAttributes().getNamedItem(
						"underline");
				Node nodeStrikethrough = entryNode.getAttributes()
						.getNamedItem("strikethrough");
				ColorThemeSetting setting = new ColorThemeSetting(color);
				if (nodeBold != null)
					setting.setBoldEnabled(Boolean.parseBoolean(nodeBold
							.getNodeValue()));
				if (nodeItalic != null)
					setting.setItalicEnabled(Boolean.parseBoolean(nodeItalic
							.getNodeValue()));
				if (nodeStrikethrough != null)
					setting.setStrikethroughEnabled(Boolean
							.parseBoolean(nodeStrikethrough.getNodeValue()));
				if (nodeUnderline != null)
					setting.setUnderlineEnabled(Boolean
							.parseBoolean(nodeUnderline.getNodeValue()));
				entries.put(entryNode.getNodeName(), setting);
			}
		}
		// Fabioz Change: amend entries before setting them
		amendThemeEntries(entries);
		theme.setEntries(entries);

		return theme;
	}

	private static void amendThemeEntries(Map<String, ColorThemeSetting> theme) {
		// constants from ColorThemeKeys
		applyDefault(theme, ColorThemeKeys.METHOD, ColorThemeKeys.FOREGROUND);
		applyDefault(theme, ColorThemeKeys.FIELD, ColorThemeKeys.FOREGROUND);
		applyDefault(theme, ColorThemeKeys.LOCAL_VARIABLE,
				ColorThemeKeys.FOREGROUND);
		applyDefault(theme, ColorThemeKeys.JAVADOC,
				ColorThemeKeys.MULTI_LINE_COMMENT);
		applyDefault(theme, ColorThemeKeys.JAVADOC_LINK, ColorThemeKeys.JAVADOC);
		applyDefault(theme, ColorThemeKeys.JAVADOC_TAG, ColorThemeKeys.JAVADOC);
		applyDefault(theme, ColorThemeKeys.JAVADOC_KEYWORD,
				ColorThemeKeys.JAVADOC);
		applyDefault(theme, ColorThemeKeys.OCCURRENCE_INDICATION,
				ColorThemeKeys.BACKGROUND);
		applyDefault(theme, ColorThemeKeys.WRITE_OCCURRENCE_INDICATION,
				ColorThemeKeys.OCCURRENCE_INDICATION);
		applyDefault(theme, ColorThemeKeys.DEBUG_CURRENT_INSTRUCTION_POINTER,
				ColorThemeKeys.CURRENT_LINE);
		applyDefault(theme, ColorThemeKeys.DEBUG_SECONDARY_INSTRUCTION_POINTER,
				ColorThemeKeys.CURRENT_LINE);
		applyDefault(theme, ColorThemeKeys.STDERR, ColorThemeKeys.KEYWORD);
		applyDefault(theme, ColorThemeKeys.STDIN, ColorThemeKeys.STRING);
		applyDefault(theme, ColorThemeKeys.STDOUT, ColorThemeKeys.FOREGROUND);

		applyDefault(theme, ColorThemeKeys.HYPERLINK, ColorThemeKeys.KEYWORD);
		applyDefault(theme, ColorThemeKeys.ACTIVE_HYPERLINK,
				ColorThemeKeys.KEYWORD);
	}

	private static void applyDefault(Map<String, ColorThemeSetting> theme,
			String key, RGB defaultColor) {
		if (!theme.containsKey(key)) {
			ColorThemeSetting setting = new ColorThemeSetting("255,0,0");
			theme.put(key, setting);
		}
	}

	private static void applyDefault(Map<String, ColorThemeSetting> theme,
			String key, String defaultKey) {
		if (!theme.containsKey(key)) {
			theme.put(key, theme.get(defaultKey));
		}
	}

	/**
	 * Returns all available color themes.
	 * 
	 * @return all available color themes.
	 */
	public Set<ColorTheme> getThemes() {
		return new HashSet<ColorTheme>(themes.values());
	}

	/**
	 * Returns the theme stored under the supplied name.
	 * 
	 * @param name
	 *            The name of the theme.
	 * @return The requested theme or <code>null</code> if none was stored under
	 *         the supplied name.
	 */
	public ColorTheme getTheme(String name) {
		return themes.get(name);
	}

	/**
	 * Adds the color theme to the list and saves it to the preferences.
	 * Existing themes will be overwritten with the new content.
	 * 
	 * @param content
	 *            The content of the color theme file.
	 * @return The saved color theme, or <code>null</code> if the theme was not
	 *         valid.
	 */
	public ColorTheme saveTheme(String content) {
		ColorTheme theme;
		try {
			theme = ColorThemeManager.parseTheme(new ByteArrayInputStream(
					content.getBytes()));
			String name = theme.getName();
			themes.put(name, theme);
			IPreferenceStore store = getPreferenceStore();
			for (int i = 1;; i++)
				if (!store.contains("importedColorTheme" + i)) {
					store.putValue("importedColorTheme" + i, content);
					break;
				}
			return theme;
		} catch (Exception e) {
			return null;
		}
	}
}
