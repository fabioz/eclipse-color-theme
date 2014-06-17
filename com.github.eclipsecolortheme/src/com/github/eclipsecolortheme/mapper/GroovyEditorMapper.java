package com.github.eclipsecolortheme.mapper;

import java.util.Map;

import com.github.eclipsecolortheme.ColorThemeSetting;

public class GroovyEditorMapper extends GenericMapper {

	private static final String GROOVY_EDITOR_GROOVY_DOC_TAG_ENABLED = "groovy.editor.groovyDoc.tag.enabled";
	private static final String GROOVY_EDITOR_GROOVY_DOC_KEYWORD_ENABLED = "groovy.editor.groovyDoc.keyword.enabled";
	private static final String GROOVY_EDITOR_GROOVY_DOC_LINK_ENABLED = "groovy.editor.groovyDoc.link.enabled";

	@Override
	public void map(Map<String, ColorThemeSetting> theme) {
		preferences.putBoolean(GROOVY_EDITOR_GROOVY_DOC_TAG_ENABLED, true);
		preferences.putBoolean(GROOVY_EDITOR_GROOVY_DOC_KEYWORD_ENABLED, true);
		preferences.putBoolean(GROOVY_EDITOR_GROOVY_DOC_LINK_ENABLED, true);
		super.map(theme);
	}

	@Override
	public void clear() {
		preferences.remove(GROOVY_EDITOR_GROOVY_DOC_TAG_ENABLED);
		preferences.remove(GROOVY_EDITOR_GROOVY_DOC_KEYWORD_ENABLED);
		preferences.remove(GROOVY_EDITOR_GROOVY_DOC_LINK_ENABLED);
		super.clear();
	}
}
