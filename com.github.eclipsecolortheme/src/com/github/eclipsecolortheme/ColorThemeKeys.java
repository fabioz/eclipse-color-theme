package com.github.eclipsecolortheme;

import java.util.HashSet;
import java.util.Set;

/** Keys that can be used in a color theme. */
@SuppressWarnings("nls")
public class ColorThemeKeys {
	// Editor related colors.
	public static final String FOREGROUND = "foreground";
	public static final String BACKGROUND = "background";
	public static final String SELECTION_FOREGROUND = "selectionForeground";
	public static final String SELECTION_BACKGROUND = "selectionBackground";
	public static final String CURRENT_LINE = "currentLine";
	public static final String LINE_NUMBER = "lineNumber";
	public static final String SEARCH_RESULT_INDICATION = "searchResultIndication";
	public static final String FILTERED_SEARCH_RESULT_INDICATION = "filteredSearchResultIndication";
	public static final String OCCURRENCE_INDICATION = "occurrenceIndication";
	public static final String WRITE_OCCURRENCE_INDICATION = "writeOccurrenceIndication";
	public static final String DELETION_INDICATION = "deletionIndication";
	public static final String FIND_SCOPE = "findScope";
	public static final String SINGLE_LINE_COMMENT = "singleLineComment";
	public static final String MULTI_LINE_COMMENT = "multiLineComment";
	public static final String COMMENT_TASK_TAG = "commentTaskTag";
	public static final String SOURCE_HOVER_BACKGROUND = "sourceHoverBackground";
	public static final String NUMBER = "number";
	public static final String STRING = "string";
	public static final String BRACKET = "bracket";
	public static final String OPERATOR = "operator";
	public static final String KEYWORD = "keyword";
	public static final String CLASS = "class";
	public static final String INTERFACE = "interface";
	public static final String ENUM = "enum";
	public static final String METHOD = "method";
	public static final String METHOD_DECLARATION = "methodDeclaration";
	public static final String ANNOTATION = "annotation";
	public static final String LOCAL_VARIABLE = "localVariable";
	public static final String LOCAL_VARIABLE_DECLARATION = "localVariableDeclaration";
	public static final String INHERITED_METHOD = "inheritedMethod";
	public static final String ABSTRACT_METHOD = "abstractMethod";
	public static final String STATIC_METHOD = "staticMethod";
	public static final String JAVADOC = "javadoc";
	public static final String JAVADOC_TAG = "javadocTag";
	public static final String JAVADOC_KEYWORD = "javadocKeyword";
	public static final String JAVADOC_LINK = "javadocLink";
	public static final String FIELD = "field";
	public static final String STATIC_FIELD = "staticField";
	public static final String STATIC_FINAL_FIELD = "staticFinalField";
	public static final String PARAMETER_VARIABLE = "parameterVariable";
	public static final String TYPE_ARGUMENT = "typeArgument";
	public static final String TYPE_PARAMETER = "typeParameter";
	public static final String DEPRECATED_MEMBER = "deprecatedMember";
	public static final String DEBUG_CURRENT_INSTRUCTION_POINTER = "debugCurrentInstructionPointer";
	public static final String DEBUG_SECONDARY_INSTRUCTION_POINTER = "debugSecondaryInstructionPointer";
	public static final String CONSTANT = "constant";

	// Colors not in the default (so, all of those must have nice defaults).
	public static final String STDERR = "stderr";
	public static final String STDIN = "stdin";
	public static final String STDOUT = "stdout";
	public static final String HYPERLINK = "hyperlink";
	public static final String ACTIVE_HYPERLINK = "activeHyperlink";
	public static final String MATCHING_BRACKET = "matchingBracket";
	public static final String SEARCH_VIEW_MATCH_HIGHLIGHT = "searchViewMatchHighlight";

	// Not in default: compare editor
	public static final String COMPARE_EDITOR_OUTGOING_COLOR = "compareOutgoing";
	public static final String COMPARE_EDITOR_INCOMING_COLOR = "compareIncoming";
	public static final String COMPARE_EDITOR_CONFLICTING_COLOR = "compareConflicting";
	public static final String COMPARE_EDITOR_RESOLVED_COLOR = "compareResolved";
	
	
	public static final Set<String> KEYS_BACKGROUND_RELATED = new HashSet<String>();
	static {
		KEYS_BACKGROUND_RELATED.add(BACKGROUND);
		KEYS_BACKGROUND_RELATED.add(SELECTION_BACKGROUND);
		KEYS_BACKGROUND_RELATED.add(CURRENT_LINE);
		KEYS_BACKGROUND_RELATED.add(SEARCH_RESULT_INDICATION);
		KEYS_BACKGROUND_RELATED.add(FILTERED_SEARCH_RESULT_INDICATION);
		KEYS_BACKGROUND_RELATED.add(OCCURRENCE_INDICATION);
		KEYS_BACKGROUND_RELATED.add(WRITE_OCCURRENCE_INDICATION);
		KEYS_BACKGROUND_RELATED.add(FIND_SCOPE);
		KEYS_BACKGROUND_RELATED.add(SOURCE_HOVER_BACKGROUND);
		KEYS_BACKGROUND_RELATED.add(DEBUG_CURRENT_INSTRUCTION_POINTER);
		KEYS_BACKGROUND_RELATED.add(DEBUG_SECONDARY_INSTRUCTION_POINTER);
		KEYS_BACKGROUND_RELATED.add(MATCHING_BRACKET);
		KEYS_BACKGROUND_RELATED.add(SEARCH_VIEW_MATCH_HIGHLIGHT);
	}

	public static final Set<String> KEYS_WITHOUT_STYLE = new HashSet<String>();
	
	static {
		KEYS_WITHOUT_STYLE.add(BACKGROUND);
		KEYS_WITHOUT_STYLE.add(SELECTION_FOREGROUND);
		KEYS_WITHOUT_STYLE.add(SELECTION_BACKGROUND);
		KEYS_WITHOUT_STYLE.add(CURRENT_LINE);
		KEYS_WITHOUT_STYLE.add(LINE_NUMBER);
		KEYS_WITHOUT_STYLE.add(SEARCH_RESULT_INDICATION);
		KEYS_WITHOUT_STYLE.add(FILTERED_SEARCH_RESULT_INDICATION);
		KEYS_WITHOUT_STYLE.add(OCCURRENCE_INDICATION);
		KEYS_WITHOUT_STYLE.add(WRITE_OCCURRENCE_INDICATION);
		KEYS_WITHOUT_STYLE.add(FIND_SCOPE);
		KEYS_WITHOUT_STYLE.add(SOURCE_HOVER_BACKGROUND);
		KEYS_WITHOUT_STYLE.add(DEBUG_CURRENT_INSTRUCTION_POINTER);
		KEYS_WITHOUT_STYLE.add(DEBUG_SECONDARY_INSTRUCTION_POINTER);

		KEYS_WITHOUT_STYLE.add(STDERR);
		KEYS_WITHOUT_STYLE.add(STDIN);
		KEYS_WITHOUT_STYLE.add(STDOUT);
		KEYS_WITHOUT_STYLE.add(HYPERLINK);
		KEYS_WITHOUT_STYLE.add(ACTIVE_HYPERLINK);
		KEYS_WITHOUT_STYLE.add(MATCHING_BRACKET);
		KEYS_WITHOUT_STYLE.add(SEARCH_VIEW_MATCH_HIGHLIGHT);
		
		KEYS_WITHOUT_STYLE.add(COMPARE_EDITOR_OUTGOING_COLOR);
		KEYS_WITHOUT_STYLE.add(COMPARE_EDITOR_INCOMING_COLOR);
		KEYS_WITHOUT_STYLE.add(COMPARE_EDITOR_CONFLICTING_COLOR);
		KEYS_WITHOUT_STYLE.add(COMPARE_EDITOR_RESOLVED_COLOR);
	}
}
