/**
 * Copyright: Fabio Zadrozny
 * 
 * License: EPL
 */
package com.github.eclipsecolortheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.swt.graphics.RGB;
import org.w3c.dom.Document;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSObject;
import com.dd.plist.NSString;
import com.dd.plist.XMLPropertyListParser;

public class TmThemeLoader {


	public static Map<String, ColorThemeSetting> loadTmTheme(Document document, ColorTheme theme) throws Exception{
		theme.setId("");
		theme.setAuthor("");
		theme.setWebsite("");
		Map<String, ColorThemeSetting> entries = new HashMap<String, ColorThemeSetting>();
		
		NSDictionary rootDict = (NSDictionary)XMLPropertyListParser.parseDocument(document);
		NSString name = (NSString) rootDict.get("name");
		theme.setName(name.getContent());
		
		List<ColorThemeSetting> colorsWithoutForeground = new ArrayList<>();
		
		NSArray settingsRoot = (NSArray) rootDict.get("settings");
		NSObject[] array = settingsRoot.getArray();
		for (NSObject dict : array) {
			if(dict instanceof NSDictionary){
				NSDictionary nsDictionary = (NSDictionary) dict;
				HashMap<String, NSObject> rootMap = nsDictionary.getHashMap();
				NSObject settings = rootMap.get("settings");
				if(settings instanceof NSDictionary){
					NSDictionary nsDictionary2 = (NSDictionary) settings;
					HashMap<String, NSObject>  colorSettingsMap = nsDictionary2.getHashMap();
					if(colorSettingsMap.isEmpty()){
						continue;
					}
					ColorThemeSetting colorThemeSetting = new ColorThemeSetting();
					NSObject background = colorSettingsMap.get("background");
					if(background instanceof NSString){
						colorThemeSetting.setBackgroundColor(((NSString)background).getContent());
						colorThemeSetting.setUseCustomBackground(true);
					}
					
					NSObject foreground = colorSettingsMap.get("foreground");
					if(foreground instanceof NSString){
						colorThemeSetting.setColor(((NSString)foreground).getContent());
					}else{
						colorsWithoutForeground.add(colorThemeSetting);
					}
					
					NSObject fontName = colorSettingsMap.get("fontName");
					NSObject fontSize = colorSettingsMap.get("fontSize");
					if(fontName instanceof NSString && fontSize instanceof NSString){
						String fontNameStr = ((NSString) fontName).getContent();
						String fontSizeStr = ((NSString) fontSize).getContent();
						if(!fontNameStr.isEmpty() && !fontSizeStr.isEmpty()){
							colorThemeSetting.setFont(fontNameStr+"|"+fontSizeStr);
						}
					}
					
					NSObject fontStyle = colorSettingsMap.get("fontStyle");
					if(fontStyle instanceof NSString){
						NSString nsString = (NSString) fontStyle;
						String content = nsString.getContent();
						if(!content.isEmpty()){
							String[] split = content.split(" ");
							for (String string : split) {
								switch(string){
								case "italic":
									colorThemeSetting.setItalicEnabled(true);
									break;
								case "underline":
									colorThemeSetting.setItalicEnabled(true);
									break;
								case "bolde":
									colorThemeSetting.setBoldEnabled(true);
									break;
								case "strikethrough":
									colorThemeSetting.setStrikethroughEnabled(true);
									break;
								}
							}
						}
						
					}
					
					NSObject scope = rootMap.get("scope");
					if(!(scope instanceof NSString)){
						//this is the root.
						
						// Must have at least foreground/background
						Color backgroundColor = colorThemeSetting.getBackgroundColor();
						ColorThemeSetting backgroundColorSetting = new ColorThemeSetting(backgroundColor.asHex());
						entries.put(ColorThemeKeys.BACKGROUND, backgroundColorSetting);
						
						colorThemeSetting.setBackgroundColor((RGB)null);
						colorThemeSetting.setUseCustomBackground(false);
						entries.put(ColorThemeKeys.FOREGROUND, colorThemeSetting);
						
						NSString nsString = (NSString)colorSettingsMap.get("selection");
						String selection = nsString.getContent();
						ColorThemeSetting selectionColorSetting = new ColorThemeSetting(selection);
						if(selectionColorSetting.getColor().getAlpha() != 255){
							selectionColorSetting.setColor(selectionColorSetting.getColor().blend(backgroundColor));
						}
						entries.put(ColorThemeKeys.SELECTION_BACKGROUND, selectionColorSetting);
						
						// foreground for the selection is the same as the regular foreground.
						entries.put(ColorThemeKeys.SELECTION_FOREGROUND, new ColorThemeSetting(colorThemeSetting.getColor().asHex()));
						
						// Note: we're checking if the user has some alpha and if it has we apply it manually with the background we have.
						
						String lineHighlight = ((NSString)colorSettingsMap.get("lineHighlight")).getContent();
						ColorThemeSetting currentLineSetting = new ColorThemeSetting(lineHighlight);
						if(currentLineSetting.getColor().getAlpha() != 255){
							//Manual alpha blend with background
							currentLineSetting.setColor(currentLineSetting.getColor().blend(backgroundColor));
						}
						entries.put(ColorThemeKeys.CURRENT_LINE, currentLineSetting);
						
						colorSettingsMap.get("invisibles"); // We don't have such a mapping now
						colorSettingsMap.get("caret"); // We don't have such a mapping now
					}else{
						//some other scope
						String scopeStr = ((NSString)scope).getContent();
						StringTokenizer stringTokenizer = new StringTokenizer(scopeStr, ", ");
						while(stringTokenizer.hasMoreTokens()){
							String element = stringTokenizer.nextToken();
							entries.put(element, colorThemeSetting);
							onFoundElement(element, colorThemeSetting, entries, true);
						}
					}
				}
			}
		}
		ColorThemeSetting foregroundSetting = entries.get(ColorThemeKeys.FOREGROUND);
		for(ColorThemeSetting setting:colorsWithoutForeground){
			setting.setColor(foregroundSetting.getColor());
		}
		return entries;
	}

	private static String[][] TM_TO_COLOR_THEME_MAPPINGS = new String[][]{
		//NOTE: The order of the elements is important!
		//Non-repeatable with higher priority replacements at the top.
		//Note that the color theme entries may be repeated (as we have less colors)
		//and the TM entries should not be repeated.
		new String[]{"comment.block.documentation", ColorThemeKeys.JAVADOC_KEYWORD},
		new String[]{"comment.block", ColorThemeKeys.MULTI_LINE_COMMENT},
		new String[]{"comment", ColorThemeKeys.SINGLE_LINE_COMMENT},
		
		new String[]{"constant.numeric", ColorThemeKeys.NUMBER},
		new String[]{"constant.language", ColorThemeKeys.CONSTANT},
		new String[]{"constant.character", ColorThemeKeys.LOCAL_VARIABLE_DECLARATION},
		new String[]{"constant.other", ColorThemeKeys.ENUM},
		new String[]{"constant", ColorThemeKeys.CONSTANT},
		
		new String[]{"entity.other.inherited-class", ColorThemeKeys.INHERITED_METHOD}, //Subclass declaration?
		new String[]{"entity.name.function", ColorThemeKeys.METHOD_DECLARATION},
		new String[]{"entity.name.tag", ColorThemeKeys.JAVADOC_TAG},
		new String[]{"entity.other.attribute-name", ColorThemeKeys.FIELD}, //Tag attribute
		new String[]{"entity.name", ColorThemeKeys.CLASS},
		
		new String[]{"string", ColorThemeKeys.STRING},
		
		new String[]{"variable.parameter", ColorThemeKeys.PARAMETER_VARIABLE},
		new String[]{"variable", ColorThemeKeys.LOCAL_VARIABLE},
		
		new String[]{"keyword", ColorThemeKeys.KEYWORD},
		
		new String[]{"storage.type", ColorThemeKeys.METHOD},
		
		new String[]{"support.function", ColorThemeKeys.STATIC_METHOD}, 
		new String[]{"support.class", ColorThemeKeys.TYPE_ARGUMENT}, 
		new String[]{"support.type", ColorThemeKeys.TYPE_PARAMETER}, 
		new String[]{"support.constant", ColorThemeKeys.STATIC_FINAL_FIELD},
		new String[]{"support.variable", ColorThemeKeys.ABSTRACT_METHOD}, 
		new String[]{"support", ColorThemeKeys.ABSTRACT_METHOD}, 
		
		new String[]{"invalid.deprecated", ColorThemeKeys.DEPRECATED_MEMBER},
		new String[]{"invalid", ColorThemeKeys.DELETION_INDICATION},
		
		new String[]{"punctuation", ColorThemeKeys.OPERATOR},
		
		// Lower priority replacements at the bottom (could repeat entries on some side)
		new String[]{"storage", ColorThemeKeys.KEYWORD},
		new String[]{"name", ColorThemeKeys.METHOD_DECLARATION},
		new String[]{"entity", ColorThemeKeys.CONSTANT},
		new String[]{"markup", ColorThemeKeys.KEYWORD},
		new String[]{"markup.quote", ColorThemeKeys.STRING},
		new String[]{"markup.other", ColorThemeKeys.INTERFACE},
		new String[]{"markup.list", ColorThemeKeys.STRING},
		new String[]{"markup.heading", ColorThemeKeys.KEYWORD},
		new String[]{"punctuation.definition", ColorThemeKeys.KEYWORD},
	};
	
	public static void onFoundElement(
			String element, 
			ColorThemeSetting colorThemeSetting, 
			Map<String, ColorThemeSetting> entries, 
			boolean isFoundInTextmate) {
		if(isFoundInTextmate){
			for(String[] s:TM_TO_COLOR_THEME_MAPPINGS){
				if(element.startsWith(s[0])){
					entries.put(s[1], colorThemeSetting.createCopy());
					return;
				}
			}
		}else{
			//Found in color theme (must be mapped to textmate)
			for(String[] s:TM_TO_COLOR_THEME_MAPPINGS){
				if(element.equals(s[1])){
					entries.put(s[0], colorThemeSetting.createCopy());
					//Unlike the other way, we'll set many entries on
					//TM from one in the color theme.
				}
			}
		}
	}

	public static void ammendTmEntries(Map<String, ColorThemeSetting> theme) {
		applyDefault(theme, "markup.underline", ColorThemeKeys.JAVADOC_LINK);
		applyDefault(theme, "markup.link", ColorThemeKeys.JAVADOC_LINK);
		applyDefault(theme, "markup.italic", ColorThemeKeys.STRING);
		applyDefault(theme, "markup.bold", ColorThemeKeys.STRING);
		applyDefault(theme, "markup.heading", ColorThemeKeys.KEYWORD);
		applyDefault(theme, "markup.list", ColorThemeKeys.NUMBER);
		applyDefault(theme, "markup.quote", ColorThemeKeys.STRING);
		applyDefault(theme, "markup.raw", ColorThemeKeys.STRING);
		applyDefault(theme, "markup.other", ColorThemeKeys.STRING);
	}

	private static void applyDefault(Map<String, ColorThemeSetting> theme, String key, String base) {
		ColorThemeSetting existing = theme.get(key);
		if(existing == null){
			ColorThemeSetting colorThemeSetting = theme.get(base);
			if(colorThemeSetting == null){
				colorThemeSetting = theme.get(ColorThemeKeys.FOREGROUND);
			}
			ColorThemeSetting cp = colorThemeSetting.createCopy();
			cp.setUnderlineEnabled(true);
			theme.put(key, cp);
		}
	}
}


// Things from color theme
//	
//	foreground*
//	background*
//	selectionForeground*
//	selectionBackground*
//	currentLine
//	lineNumber
//	searchResultIndication
//	filteredSearchResultIndication
//	occurrenceIndication
//	writeOccurrenceIndication
//	findScope
//	sourceHoverBackground
//	singleLineComment*
//	multiLineComment
//	commentTaskTag
//	javadoc
//	javadocLink
//	javadocTag
//	javadocKeyword
//	class*
//	interface
//	method*
//	methodDeclaration*
//	bracket
//	number*
//	string*
//	operator
//	keyword*
//	annotation*
//	staticMethod
//	localVariable*
//	localVariableDeclaration*
//	field*
//	staticField*
//	staticFinalField
//	deprecatedMember
//	deletionIndication
//	enum*
//	inheritedMethod
//	abstractMethod
//	parameterVariable
//	typeArgument
//	typeParameter
//	constant*
//	 

// Things from textmate

/*

comment — for comments.

    line — line comments, we specialize further so that the type of comment start character(s) can be extracted from the scope.
        double-slash — // comment
        double-dash — -- comment
        number-sign — # comment
        percentage — % comment
        character — other types of line comments.

    block — multi-line comments like / * … * / and <!-- … -->.
        documentation — embedded documentation.

constant — various forms of constants.

    numeric — those which represent numbers, e.g. 42, 1.3f, 0x4AB1U.
    character — those which represent characters, e.g. &lt;, \e, \031.
    escape — escape sequences like \e would be constant.character.escape.
    language — constants (generally) provided by the language which are “special” like true, false, nil, YES, NO, etc.
    other — other constants, e.g. colors in CSS.

entity — an entity refers to a larger part of the document, for example a chapter, class, function, or tag. We do not scope the entire entity as entity.* (we use meta.* for that). But we do use entity.* for the “placeholders” in the larger entity, e.g. if the entity is a chapter, we would use entity.name.section for the chapter title.

name — we are naming the larger entity.
    function — the name of a function.
    type — the name of a type declaration or class.
    tag — a tag name.
    section — the name is the name of a section/heading.
other — other entities.
    inherited-class — the superclass/baseclass name.
    attribute-name — the name of an attribute (mainly in tags).

invalid — stuff which is “invalid”.

    illegal — illegal, e.g. an ampersand or lower-than character in HTML (which is not part of an entity/tag).
    deprecated — for deprecated stuff e.g. using an API function which is deprecated or using styling with strict HTML.

keyword — keywords (when these do not fall into the other groups).

    control — mainly related to flow control like continue, while, return, etc.
    operator — operators can either be textual (e.g. or) or be characters.
    other — other keywords.

markup — this is for markup languages and generally applies to larger subsets of the text.

    underline — underlined text.
        link — this is for links, as a convenience this is derived from markup.underline so that if there is no theme rule which specifically targets markup.underline.link then it will inherit the underline style.
    bold — bold text (text which is strong and similar should preferably be derived from this name).
    heading — a section header. Optionally provide the heading level as the next element, for example markup.heading.2.html for <h2>…</h2> in HTML.
    italic — italic text (text which is emphasized and similar should preferably be derived from this name).
    list — list items.
        numbered — numbered list items.
        unnumbered — unnumbered list items.
    quote — quoted (sometimes block quoted) text.
    raw — text which is verbatim, e.g. code listings. Normally spell checking is disabled for markup.raw.
    other — other markup constructs.

meta — the meta scope is generally used to markup larger parts of the document. For example the entire line which declares a function would be meta.function and the subsets would be storage.type, entity.name.function, variable.parameter etc. and only the latter would be styled. Sometimes the meta part of the scope will be used only to limit the more general element that is styled, most of the time meta scopes are however used in scope selectors for activation of bundle items. For example in Objective-C there is a meta scope for the interface declaration of a class and the implementation, allowing the same tab-triggers to expand differently, depending on context.

storage — things relating to “storage”.

    type — the type of something, class, function, int, var, etc.
    modifier — a storage modifier like static, final, abstract, etc.

string — strings.

    quoted — quoted strings.
        single — single quoted strings: 'foo'.
        double — double quoted strings: "foo".
        triple — triple quoted strings: """Python""".
        other — other types of quoting: $'shell', %s{...}.
    unquoted — for things like here-docs and here-strings.
    interpolated — strings which are “evaluated”: `date`, $(pwd).
    regexp — regular expressions: /(\w+)/.
    other — other types of strings (should rarely be used).

support — things provided by a framework or library should be below support.

    function — functions provided by the framework/library. For example NSLog in Objective-C is support.function.
    class — when the framework/library provides classes.
    type — types provided by the framework/library, this is probably only used for languages derived from C, which has typedef (and struct). Most other languages would introduce new types as classes.
    constant — constants (magic values) provided by the framework/library.
    variable — variables provided by the framework/library. For example NSApp in AppKit.
    other — the above should be exhaustive, but for everything else use support.other.

variable — variables. Not all languages allow easy identification (and thus markup) of these.

    parameter — when the variable is declared as the parameter.
    language — reserved language variables like this, super, self, etc.
    other — other variables, like $some_variables.

*/