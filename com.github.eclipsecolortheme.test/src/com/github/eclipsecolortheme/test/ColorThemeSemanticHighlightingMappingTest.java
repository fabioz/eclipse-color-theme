package com.github.eclipsecolortheme.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.eclipse.swt.graphics.FontData;
import org.junit.Before;
import org.junit.Test;

import com.github.eclipsecolortheme.ColorThemeSemanticHighlightingMapping;
import com.github.eclipsecolortheme.ColorThemeSetting;
import com.github.eclipsecolortheme.test.mock.MockEclipsePreferences;

public class ColorThemeSemanticHighlightingMappingTest {
    private ColorThemeSemanticHighlightingMapping mapping;
    private MockEclipsePreferences mockPreferences;
    private ColorThemeSetting setting;

    @Before
    public void setUp() {
        mapping = new ColorThemeSemanticHighlightingMapping("something", "foreground");
        mockPreferences = new MockEclipsePreferences();
        setting = new ColorThemeSetting("#336699");
    }

    @Test
    public void putPreferences() {
        mapping.putPreferences(mockPreferences, setting);
        assertThat(mockPreferences.getBoolean("something.enabled", false), is(true));
        assertThat(mockPreferences.get("something.color", null), is("51,102,153"));
    }

    @Test
    public void putPreferencesWithTextStyle() {
    	setting.setBackgroundColor("#ff0000");
    	String fontStr = ColorThemeSetting.fontToString(new FontData("Courier New", 10, 0));
		setting.setFont(fontStr);
        setting.setBoldEnabled(true);
        setting.setItalicEnabled(true);
        setting.setUnderlineEnabled(true);
        setting.setStrikethroughEnabled(true);
        setting.setUseCustomBackground(true);
        setting.setUseCustomFont(true);
        mapping.putPreferences(mockPreferences, setting);
        assertThat(mockPreferences.getBoolean("something.bold", false), is(true));
        assertThat(mockPreferences.getBoolean("something.italic", false), is(true));
        assertThat(mockPreferences.getBoolean("something.underline", false), is(true));
        assertThat(mockPreferences.getBoolean("something.strikethrough", false), is(true));
        assertThat(mockPreferences.getBoolean("something.useCustomBackground", false), is(true));
        assertThat(mockPreferences.getBoolean("something.useCustomFont", false), is(true));
        assertThat(mockPreferences.get("something.backgroundColor", null), is("255,0,0"));
        assertThat(mockPreferences.get("something.font", null), is(fontStr));
    }

    @Test
    public void removePreferences() {
        setting.setBoldEnabled(true);
        String fontStr = ColorThemeSetting.fontToString(new FontData("Courier New", 10, 0));
        setting.setFont(fontStr);
        setting.setItalicEnabled(true);
        setting.setUnderlineEnabled(true);
        setting.setStrikethroughEnabled(true);
        setting.setUseCustomBackground(true);
        setting.setUseCustomFont(true);
        setting.setBackgroundColor("#00ff00");
        mapping.putPreferences(mockPreferences, setting);
        mapping.removePreferences(mockPreferences);
        assertThat(mockPreferences.getBoolean("something.enabled", false), is(false));
        assertThat(mockPreferences.get("something.color", ""), is(""));
        assertThat(mockPreferences.get("something.backgroundColor", ""), is(""));
        assertThat(mockPreferences.getBoolean("something.bold", false), is(false));
        assertThat(mockPreferences.getBoolean("something.italic", false), is(false));
        assertThat(mockPreferences.getBoolean("something.underline", false), is(false));
        assertThat(mockPreferences.getBoolean("something.strikethrough", false), is(false));
        assertThat(mockPreferences.getBoolean("something.useCustomBackground", false), is(false));
        assertThat(mockPreferences.getBoolean("something.useCustomFont", false), is(false));
    }
}
