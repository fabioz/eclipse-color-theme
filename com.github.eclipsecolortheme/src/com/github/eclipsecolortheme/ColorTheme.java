package com.github.eclipsecolortheme;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ColorTheme {

	private String id;
	private String name;
	private String author;
	private String website;
	private Map<String, ColorThemeSetting> entries;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public Map<String, ColorThemeSetting> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, ColorThemeSetting> entries) {
		this.entries = entries;
	}

	/**
	 * @param key
	 *            the entry (i.e.: "background")
	 * @return
	 */
	public boolean isDarkColor(String key) {
		ColorThemeSetting colorThemeSetting = entries.get(key);
		Color color = colorThemeSetting.getColor();
		double v = (color.getR() / 255.0) * 0.3 + (color.getG() / 255.0) * 0.59
				+ (color.getB() / 255.0) * 0.11;
		return v <= 0.5;
	}

	public ColorTheme createCopy() {
		ColorTheme copy = new ColorTheme();
		copy.id = id;
		copy.name = name;
		copy.author = author;
		copy.website = website;
		HashMap<String, ColorThemeSetting> map = new HashMap<String, ColorThemeSetting>();
		Set<Entry<String, ColorThemeSetting>> entrySet = entries.entrySet();
		for (Entry<String, ColorThemeSetting> entry : entrySet) {
			map.put(entry.getKey(), entry.getValue().createCopy());
		}
		copy.entries = map;
		return copy;
	}
}