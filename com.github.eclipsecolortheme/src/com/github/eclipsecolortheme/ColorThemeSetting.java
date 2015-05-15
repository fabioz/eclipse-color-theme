package com.github.eclipsecolortheme;

import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;

public class ColorThemeSetting {
	private Color color;
	private Color backgroundColor; // null means transparent background
	private Boolean boldEnabled;
	private Boolean italicEnabled;
	private Boolean underlineEnabled;
	private Boolean strikethroughEnabled;
	private Boolean useCustomBackground;
	private FontData font;
	private Boolean useCustomFont;

	public ColorThemeSetting(String color) {
		this.color = new Color(color);
	}

	public ColorThemeSetting() {
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
	
	public void setUseCustomBackground(Boolean useCustomBackground) {
		this.useCustomBackground = useCustomBackground;
	}

	public void setUnderlineEnabled(Boolean underlineEnabled) {
		this.underlineEnabled = underlineEnabled;
	}
	public void setUseCustomFont(Boolean useCustomFont) {
		this.useCustomFont = useCustomFont;
	}
	


	public Color getColor() {
		return color;
	}

	public String getHexColorOrNull() {
		if (color == null) {
			return "null";
		}
		return color.asHex();
	}

	public String getHexColorOrEmpty() {
		if (color == null) {
			return "";
		}
		return color.asHex();
	}
	
	public void setBackgroundColor(String backgroundColorStr) {
		this.backgroundColor = new Color(backgroundColorStr);
	}

	public void setFont(String string) {
		this.font = stringToFont(string);
	}
	

	public FontData getFont() {
		return this.font;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public String getBackgroundHexColorOrNull() {
		if (backgroundColor == null) {
			return "null";
		}
		return backgroundColor.asHex();
	}
	
	public String getBackgroundHexColorOrEmpty() {
		if (backgroundColor == null) {
			return "";
		}
		return backgroundColor.asHex();
	}

	public Boolean isBoldEnabled() {
		return boldEnabled != null ? boldEnabled : false;
	}

	public Boolean useCustomFont() {
		return useCustomFont != null ? useCustomFont : false;
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
	
	public Boolean useCustomBackground() {
		return useCustomBackground != null ? useCustomBackground : false;
	}

	public ColorThemeSetting createCopy() {
		ColorThemeSetting setting = new ColorThemeSetting();
		setting.color = color;
		setting.boldEnabled = boldEnabled;
		setting.italicEnabled = italicEnabled;
		setting.strikethroughEnabled = strikethroughEnabled;
		setting.underlineEnabled = underlineEnabled;
		setting.backgroundColor = backgroundColor;
		setting.useCustomBackground = useCustomBackground;
		setting.useCustomFont = useCustomFont;
		setting.font = font;
		return setting;
	}

	public ColorThemeSetting createCopy(RGB colorValue) {
		ColorThemeSetting copy = createCopy();
		copy.color = new Color(colorValue);
		return copy;
	}

	@Override
	public String toString() {
		return "ColorThemeSetting: " + getColor().getRGB();
	}

	public static String fontToString(FontData fontData) {
		return fontData.getName()+"|"+((int)fontData.height);
	}
	
	public static FontData stringToFont(String font) {
		if(font == null){
			return null;
		}
		int lastIndexOf = font.lastIndexOf('|');
		if(lastIndexOf == -1){
			return null;
		}
		return new FontData(font.substring(0, lastIndexOf), Integer.parseInt(font.substring(lastIndexOf+1)), 0);
	}

}
