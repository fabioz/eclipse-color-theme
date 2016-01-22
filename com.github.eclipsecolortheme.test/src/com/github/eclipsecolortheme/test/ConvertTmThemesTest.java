/**
 * Copyright Fabio Zadrozny
 * 
 * License: EPL
 */
package com.github.eclipsecolortheme.test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import org.junit.Test;

import com.github.eclipsecolortheme.ColorTheme;
import com.github.eclipsecolortheme.ColorThemeManager;
import com.github.eclipsecolortheme.ColorThemeSetting;
import com.github.eclipsecolortheme.ParsedTheme;

public class ConvertTmThemesTest {

	@Test
	public void testRegularThemes() throws Exception {
		File themesDir = ColorThemeTestUtils.getThemesDir();
		File f = new File(themesDir, "monokai.xml");
		ColorTheme colorTheme;
		try(InputStream in = new FileInputStream(f)){
			ParsedTheme theme = ColorThemeManager.parseTheme(new BufferedInputStream(in), false);
			colorTheme = theme.getTheme();
			Map<String, ColorThemeSetting> entries = colorTheme.getEntries();
			
		}
		
		// Check that the same API can deal with tmThemes.
		ParsedTheme theme = ColorThemeManager.parseTheme(new ByteArrayInputStream(MONOKAI_THEME.getBytes("utf-8")), false);
		
	}
	
	
	//http://www.monokai.nl/blog/wp-content/asdev/Monokai.tmTheme
	static String MONOKAI_THEME = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			"<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" + 
			"<plist version=\"1.0\">\n" + 
			"<dict>\n" + 
			"	<key>name</key>\n" + 
			"	<string>Monokai</string>\n" + 
			"	<key>settings</key>\n" + 
			"	<array>\n" + 
			"		<dict>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>background</key>\n" + 
			"				<string>#272822</string>\n" + 
			"				<key>caret</key>\n" + 
			"				<string>#F8F8F0</string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#F8F8F2</string>\n" + 
			"				<key>invisibles</key>\n" + 
			"				<string>#49483E</string>\n" + 
			"				<key>lineHighlight</key>\n" + 
			"				<string>#49483E</string>\n" + 
			"				<key>selection</key>\n" + 
			"				<string>#49483E</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Comment</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>comment</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#75715E</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>String</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>string</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#E6DB74</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Number</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>constant.numeric</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#AE81FF</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Built-in constant</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>constant.language</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#AE81FF</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>User-defined constant</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>constant.character, constant.other</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#AE81FF</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Variable</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>variable</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Keyword</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>keyword</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#F92672</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Storage</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>storage</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#F92672</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Storage type</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>storage.type</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string>italic</string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#66D9EF</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Class name</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>entity.name.class</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string>underline</string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#A6E22E</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Inherited class</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>entity.other.inherited-class</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string>italic underline</string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#A6E22E</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Function name</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>entity.name.function</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#A6E22E</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Function argument</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>variable.parameter</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string>italic</string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#FD971F</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Tag name</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>entity.name.tag</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#F92672</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Tag attribute</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>entity.other.attribute-name</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#A6E22E</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Library function</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>support.function</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#66D9EF</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Library constant</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>support.constant</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#66D9EF</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Library class/type</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>support.type, support.class</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string>italic</string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#66D9EF</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Library variable</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>support.other.variable</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Invalid</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>invalid</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>background</key>\n" + 
			"				<string>#F92672</string>\n" + 
			"				<key>fontStyle</key>\n" + 
			"				<string></string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#F8F8F0</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"		<dict>\n" + 
			"			<key>name</key>\n" + 
			"			<string>Invalid deprecated</string>\n" + 
			"			<key>scope</key>\n" + 
			"			<string>invalid.deprecated</string>\n" + 
			"			<key>settings</key>\n" + 
			"			<dict>\n" + 
			"				<key>background</key>\n" + 
			"				<string>#AE81FF</string>\n" + 
			"				<key>foreground</key>\n" + 
			"				<string>#F8F8F0</string>\n" + 
			"			</dict>\n" + 
			"		</dict>\n" + 
			"	</array>\n" + 
			"	<key>uuid</key>\n" + 
			"	<string>D8D5E82E-3D5B-46B5-B38E-8C841C21347D</string>\n" + 
			"</dict>\n" + 
			"</plist>\n" + 
			"";
}
