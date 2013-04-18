package com.github.eclipsecolortheme;

import org.eclipse.swt.graphics.RGB;

public class ColorThemeSetting {

	private Color color;
	private Boolean boldEnabled;
	private Boolean italicEnabled;
	private Boolean underlineEnabled;
	private Boolean strikethroughEnabled;

	private ColorThemeSetting() {
		// internal use
	}

	public ColorThemeSetting(String color) {
		this.color = new Color(color);
	}

	public void setBoldEnabled(Boolean boldEnabled) {
		this.boldEnabled = boldEnabled;
	}

	public void setItalicEnabled(Boolean italicEnabled) {
		this.italicEnabled = italicEnabled;
	}

	public void setStrikethroughEnabled(Boolean strikethroughEnabled) {
		this.strikethroughEnabled = strikethroughEnabled;
	}

	public void setUnderlineEnabled(Boolean underlineEnabled) {
		this.underlineEnabled = underlineEnabled;
	}

	public Color getColor() {
		return color;
	}

	public Boolean isBoldEnabled() {
		return boldEnabled != null ? boldEnabled : false;
	}

	public Boolean isItalicEnabled() {
		return italicEnabled != null ? italicEnabled : false;
	}

	public Boolean isUnderlineEnabled() {
		return underlineEnabled != null ? underlineEnabled : false;
	}

	public Boolean isStrikethroughEnabled() {
		return strikethroughEnabled != null ? strikethroughEnabled : false;
	}

	public ColorThemeSetting createCopy() {
		ColorThemeSetting setting = new ColorThemeSetting();
		setting.color = color;
		setting.boldEnabled = boldEnabled;
		setting.italicEnabled = italicEnabled;
		setting.strikethroughEnabled = strikethroughEnabled;
		setting.underlineEnabled = underlineEnabled;
		return setting;
	}

	public ColorThemeSetting createCopy(RGB colorValue) {
		ColorThemeSetting copy = createCopy();
		copy.color = new Color(colorValue);
		return copy;
	}

}
