package com.github.eclipsecolortheme.test;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.github.eclipsecolortheme.Activator;

public class ColorThemeTestUtils {

	public static File getThemesDir() {
		URL url = Activator.class.getClassLoader().getResource("com/github/eclipsecolortheme/Activator.class");
		File path = new File(url.getPath());
		String fullPath = path.toString();
		int i = fullPath.lastIndexOf("com.github.eclipsecolortheme");
		if (i != -1) {
			File f = getFromCurrentDir();
			if (!f.exists()) {
				throw new AssertionError("Expected: " + f + " to exist. cwd: " + new File(".").getAbsolutePath());
			}
			return f;
		}
		IPath p = Path.fromOSString(fullPath.substring(0, i + "com.github.eclipsecolortheme".length()));
		IPath languagesDir = p.append("themes");
		File languagesDirFile = languagesDir.toFile();
		if (!languagesDirFile.exists()) {
			File f = getFromCurrentDir();
			if (!f.exists()) {
				throw new AssertionError("Expected: " + languagesDirFile + " or " + f + " to exist. cwd: "
						+ new File(".").getAbsolutePath());
			}
			return f;
		}
		return languagesDirFile;
	}

	private static File getFromCurrentDir() {
		// consider that the current dir is the com.github.eclipsecolortheme.test
		File f = new File(".");
		f = f.getAbsoluteFile();
		f = f.getParentFile().getParentFile();
		f = new File(f, "com.github.eclipsecolortheme/themes");
		return f;
	}
}
