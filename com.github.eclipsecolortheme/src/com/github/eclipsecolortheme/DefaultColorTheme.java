/**
 * Copyright: Fabio Zadrozny
 * 
 * License: EPL
 */
package com.github.eclipsecolortheme;

import java.io.ByteArrayInputStream;

public class DefaultColorTheme {

	private static ColorTheme defaultTheme;

	public final static ColorTheme getDefaultTheme() {
		if (defaultTheme == null) {
			String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
					+ "<colorTheme id=\"26605\" name=\"Eclipse Light\" modified=\"2015-05-15 12:00:00\" author=\"Fabio Zadrozny\">\n"
					+ "    <annotation color=\"#646464\" />\n" 
                    + "    <background color=\"#FFFFFF\" />\n"
					+ "    <bracket color=\"#000000\" />\n" 
                    + "    <class color=\"#000000\" bold=\"true\"/>\n"
					+ "    <commentTaskTag color=\"#666666\" bold=\"true\" />\n"
					+ "    <constant color=\"#000000\" italic=\"true\" />\n" 
                    + "    <currentLine color=\"#E8F2FE\" />\n"
					+ "    <deletionIndication color=\"#9B5656\" />\n"
					+ "    <deprecatedMember color=\"#000000\" strikethrough=\"true\" />\n"
					+ "    <enum color=\"#000F60\" />\n" 
                    + "    <field color=\"#0000C0\" />\n"
					+ "    <filteredSearchResultIndication color=\"#CECCF7\" />\n"
					+ "    <findScope color=\"#B0B0B0\" />\n" 
                    + "    <foreground color=\"#000000\" />\n"
					+ "    <inheritedMethod color=\"#000000\" />\n"
					+ "    <interface color=\"#000000\" italic=\"false\" underline=\"false\" strikethrough=\"false\" />\n"
					+ "    <javadoc color=\"#3F5FBF\" />\n"
					+ "    <javadocKeyword color=\"#7F9FBF\" bold=\"false\" />\n"
					+ "    <javadocLink color=\"#3F3FBF\" italic=\"false\" />\n"
					+ "    <javadocTag color=\"#7F7F9F\" bold=\"true\" italic=\"false\" />\n"
					+ "    <keyword color=\"#7F0055\" bold=\"true\" />\n" 
                    + "    <lineNumber color=\"#000000\" />\n"
					+ "    <matchingBracket color=\"#408080\" />\n" 
                    + "    <localVariable color=\"#000000\" />\n"
					+ "    <localVariableDeclaration color=\"#000000\" />\n"
					+ "    <method color=\"#000000\" bold=\"true\"/>\n"
					+ "    <methodDeclaration color=\"#000000\" />\n" 
                    + "    <multiLineComment color=\"#3F7F5F\" />\n"
					+ "    <number color=\"#000000\" />\n" 
                    + "    <occurrenceIndication color=\"#B0B0B0\" />\n"
					+ "    <operator color=\"#000000\" />\n" 
                    + "    <parameterVariable color=\"#000000\" />\n"
					+ "    <searchResultIndication color=\"#CECCF7\" />\n"
					+ "    <selectionBackground color=\"#328EFE\" />\n"
					+ "    <selectionForeground color=\"#FFFFFF\" />\n"
					+ "    <singleLineComment color=\"#3F7F5F\" />\n"
					+ "    <sourceHoverBackground color=\"#B0B0B0\" />\n"
					+ "    <staticField color=\"#0000C0\" italic=\"true\" />\n"
					+ "    <staticFinalField color=\"#843CAA\" italic=\"true\" />\n"
					+ "    <staticMethod color=\"#000000\" italic=\"true\" />\n" 
                    + "    <string color=\"#2A00FF\" />\n"
					+ "    <typeArgument color=\"#000F60\" />\n"
					+ "    <typeParameter color=\"#135756\" bold=\"true\" />\n"
					+ "    <writeOccurrenceIndication color=\"#EFC090\" />\n" 
                    + "</colorTheme>";
			try {
				ParsedTheme parseTheme = ColorThemeManager.parseTheme(new ByteArrayInputStream(s.getBytes("utf-8")),
						false);
				defaultTheme = parseTheme.getTheme();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return defaultTheme;
	}
}
