package com.github.eclipsecolortheme.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.github.eclipsecolortheme.Color;
import com.github.eclipsecolortheme.ColorThemeSetting;

public class ColorThemeSettingTest {
    private ColorThemeSetting setting;

    @Before
    public void setUp() {
        setting = new ColorThemeSetting("#00FFAA");
    }

    @Test
    public void initialValues() {
        assertThat(setting.isBoldEnabled(), is(false));
        assertThat(setting.isItalicEnabled(), is(false));
        assertThat(setting.isStrikethroughEnabled(), is(false));
        assertThat(setting.isUnderlineEnabled(), is(false));
    }

    @Test
    public void getColor() {
        Color color = setting.getColor();
        assertThat(color, notNullValue());
        assertThat(color.asHex(), is("#00FFAA"));
        
        color = setting.getBackgroundColor();
        assertThat(color, nullValue());
        setting.setBackgroundColor("#00ff00");
        color = setting.getBackgroundColor();
        assertThat(color.asHex(), is("#00FF00"));
    }

    @Test
    public void decorationBold() {
        setting.setBoldEnabled(true);
        assertThat(setting.isBoldEnabled(), is(true));
        setting.setBoldEnabled(false);
        assertThat(setting.isBoldEnabled(), is(false));
    }

    @Test
    public void decorationItalic() {
        setting.setBoldEnabled(true);
        assertThat(setting.isBoldEnabled(), is(true));
        setting.setBoldEnabled(false);
        assertThat(setting.isBoldEnabled(), is(false));
    }

    @Test
    public void decorationUnderline() {
        setting.setBoldEnabled(true);
        assertThat(setting.isBoldEnabled(), is(true));
        setting.setBoldEnabled(false);
        assertThat(setting.isBoldEnabled(), is(false));
    }

    @Test
    public void decorationStrikethrough() {
        setting.setStrikethroughEnabled(true);
        assertThat(setting.isStrikethroughEnabled(), is(true));
        setting.setStrikethroughEnabled(false);
        assertThat(setting.isStrikethroughEnabled(), is(false));
    }
}
