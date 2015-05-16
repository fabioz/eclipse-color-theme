package com.github.eclipsecolortheme.test;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.github.eclipsecolortheme.Activator;


public class ColorThemeTestUtils {
	
    public static File getThemesDir() {
        URL url = Activator.class.getClassLoader()
                .getResource("com/github/eclipsecolortheme/Activator.class");
        File path = new File(url.getPath());
        String fullPath = path.toString();
        int i = fullPath.lastIndexOf("com.github.eclipsecolortheme");
        IPath p = Path.fromOSString(fullPath.substring(0, i+"com.github.eclipsecolortheme".length()));
        IPath languagesDir = p.append("themes");
        File languagesDirFile = languagesDir.toFile();
        return languagesDirFile;
    }
}
