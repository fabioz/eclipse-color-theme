package com.github.eclipsecolortheme.preferences.edition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.github.eclipsecolortheme.Color;
import com.github.eclipsecolortheme.ColorTheme;
import com.github.eclipsecolortheme.ColorThemeKeys;
import com.github.eclipsecolortheme.ColorThemeSetting;
import com.github.eclipsecolortheme.preferences.ColorThemePreferencesPageStyledText;
import com.github.eclipsecolortheme.preferences.ColorThemePreferencesPageStyledText.IStyledText;

@SuppressWarnings("nls")
public class EditThemeDialog extends TitleAreaDialog {

	private ColorTheme theme;
	private Text author;
	private Text website;
	private Text name;
	private ColorEditor fForegroundColorEditor;
	private Button fFontBoldCheckBox;
	private Button fFontItalicCheckBox;
	private Button fFontStrikeThroughCheckBox;
	private Button fFontUnderlineCheckBox;
	private Tree fAppearanceColorTree;
	
	private Button fUseCustomFont;
	private Button fUseCustomBackground;
	private ColorEditor fBackgroundColorEditor;
	private Button fCustomFont;
	
	private StyledText styledText;
	private Map<RGB, org.eclipse.swt.graphics.Color> colors = new HashMap<RGB, org.eclipse.swt.graphics.Color>();
	private Set<String> existingThemeNames;
	private KeyListener listener = new KeyListener() {

		public void keyReleased(KeyEvent e) {
			isValidInput();
		}

		public void keyPressed(KeyEvent e) {
		}
	};

	@Override
	public boolean close() {
		boolean ret = super.close();
		Set<Entry<RGB, org.eclipse.swt.graphics.Color>> entrySet = colors
				.entrySet();
		for (Entry<RGB, org.eclipse.swt.graphics.Color> entry : entrySet) {
			entry.getValue().dispose();
		}
		colors.clear();
		return ret;
	}

	private org.eclipse.swt.graphics.Color getColor(RGB colorValue) {
		org.eclipse.swt.graphics.Color color = colors.get(colorValue);
		if (color != null && !color.isDisposed()) {
			return color;
		}
		color = new org.eclipse.swt.graphics.Color(Display.getCurrent(),
				colorValue);
		colors.put(colorValue, color);
		return color;
	}

	public EditThemeDialog(Shell parentShell, ColorTheme theme,
			Set<ColorTheme> set) {
		super(parentShell);
		this.theme = theme.createCopy();
		this.existingThemeNames = new HashSet<String>();
		for (ColorTheme t : set) {
			existingThemeNames.add(t.getName());
		}
	}

	@Override
	public void create() {
		super.create();
		setTitle("Theme edition");
		setMessage("Create new theme based on: " + this.theme.getName(),
				IMessageProvider.INFORMATION);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		Label label1 = new Label(parent, SWT.NONE);
		label1.setText("Name");

		name = new Text(parent, SWT.BORDER);
		name.setLayoutData(gridData);
		String themeName = this.theme.getName();
		// if (!themeName.endsWith("*")) {
		// themeName += '*';
		// }
		name.setText(themeName);
		name.addKeyListener(listener);

		label1 = new Label(parent, SWT.NONE);
		label1.setText("Author");

		author = new Text(parent, SWT.BORDER);
		author.setLayoutData(gridData);
		author.setText(this.theme.getAuthor() != null ? this.theme.getAuthor()
				: "");
		author.addKeyListener(listener);

		label1 = new Label(parent, SWT.NONE);
		label1.setText("Website");

		website = new Text(parent, SWT.BORDER);
		website.setLayoutData(gridData);
		website.setText(this.theme.getWebsite() != null ? this.theme
				.getWebsite() : "");
		website.addKeyListener(listener);

		createColorOptions(parent);
		initializeList();

		styledText = new StyledText(parent, SWT.BORDER);
		styledText.setLayoutData(gridData);
		styledText.setText("");
		styledText.setLayoutData(GridDataFactory.fillDefaults()
				.grab(false, false).span(2, 1).create());

		onAppearanceRelatedPreferenceChanged();

		return parent;
	}

	private void initializeList() {
		Map<String, ColorThemeSetting> entries = this.theme.getEntries();
		ArrayList<String> keys = new ArrayList<String>(entries.keySet());
		Collections.sort(keys);
		for (String entry : keys) {
			TreeItem item = new TreeItem(fAppearanceColorTree, SWT.NONE);
			item.setText(entry);
			ColorThemeSetting colorThemeSetting = entries.get(entry);
			Color color = colorThemeSetting.getColor();
			if(color == null){
				System.err.println("Color for entry: "+entry+" is null.");
			}else{
				updateTreeItem(item, entry, entries, color.getRGB());
			}
		}

		fAppearanceColorTree.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fAppearanceColorTree != null
						&& !fAppearanceColorTree.isDisposed()) {
					fAppearanceColorTree.select(fAppearanceColorTree.getItem(0));
					handleAppearanceColorListSelection();
				}
			}
		});

	}

	private void createColorOptions(Composite appearanceComposite) {
		GridLayout layout;
		Label l = new Label(appearanceComposite, SWT.LEFT);
		GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		gd.heightHint = convertHeightInCharsToPixels(1) / 2;
		l.setLayoutData(gd);

		l = new Label(appearanceComposite, SWT.LEFT);
		l.setText("Appearance color options:");
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		l.setLayoutData(gd);

		Composite editorComposite = new Composite(appearanceComposite, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 2;
		layout.marginWidth = 0;
		editorComposite.setLayout(layout);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		gd.grabExcessVerticalSpace = true;
		editorComposite.setLayoutData(gd);

		fAppearanceColorTree = new Tree(editorComposite, SWT.SINGLE
				| SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_BOTH);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = convertHeightInCharsToPixels(8);
		fAppearanceColorTree.setLayoutData(gd);

		fAppearanceColorTree.addListener(SWT.EraseItem, new Listener() {
			public void handleEvent(Event event) {
				event.detail &= ~SWT.HOT;
				if ((event.detail & SWT.SELECTED) == 0)
					return; // / item not selected

				Tree table = (Tree) event.widget;
				TreeItem item = (TreeItem) event.item;
				int clientWidth = table.getClientArea().width;

				GC gc = event.gc;

				// gc.setBackground(colorBackground);
				// gc.setForeground(colorForeground);
				//
				gc.setForeground(item.getForeground());
				gc.setBackground(item.getBackground());
				gc.fillRectangle(0, event.y, clientWidth - 1, event.height - 1);
				gc.drawRectangle(0, event.y, clientWidth - 1, event.height - 1);
				event.detail &= ~SWT.SELECTED;
			}
		});

		Composite stylesComposite = new Composite(editorComposite, SWT.NONE);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		l = new Label(stylesComposite, SWT.LEFT);
		l.setText("Color:");
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		l.setLayoutData(gd);

		fForegroundColorEditor = createColorEditor(stylesComposite, true);
		
		fAppearanceColorTree.addSelectionListener(new SelectionAdapter() {
			
			public void widgetSelected(SelectionEvent e) {
				handleAppearanceColorListSelection();
			}
		});

		fFontBoldCheckBox = addStyleCheckBox(stylesComposite, "Bold");
		fFontItalicCheckBox = addStyleCheckBox(stylesComposite, "Italic");
		fFontUnderlineCheckBox = addStyleCheckBox(stylesComposite, "Underline");
		fFontStrikeThroughCheckBox = addStyleCheckBox(stylesComposite,
				"Strikethrough");
		fUseCustomBackground = addStyleCheckBox(stylesComposite, "Custom Background?", 1);
		fBackgroundColorEditor = createColorEditor(stylesComposite, false);
		fUseCustomFont = addStyleCheckBox(stylesComposite, "Custom Font?", 1);
		
		fCustomFont = new Button(stylesComposite, SWT.PUSH);
		configFontButton();
	}

	private ColorEditor createColorEditor(Composite stylesComposite, final boolean foregroundColor) {
		GridData gd;
		ColorEditor colorEditor = new ColorEditor(stylesComposite);
		Button foregroundColorButton = colorEditor.getButton();
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		foregroundColorButton.setLayoutData(gd);

		foregroundColorButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				TreeItem selectedTreeItem = getSelectedTreeItem();
				if (selectedTreeItem == null) {
					return;
				}
				String key = selectedTreeItem.getText();
				updateEntry(selectedTreeItem, key, foregroundColor);

				onAppearanceRelatedPreferenceChanged();
			}
		});
		return colorEditor;
	}
	
	private void updateEntry(TreeItem selectedTreeItem, String key, boolean foregroundColor) {
		Map<String, ColorThemeSetting> entries = theme.getEntries();

		ColorThemeSetting setting = entries.get(key);
		if(foregroundColor){
			RGB colorValue = fForegroundColorEditor.getColorValue();
			entries.put(key, setting.createCopy(colorValue));
			updateTreeItem(selectedTreeItem, key, entries, colorValue);
		}else{
			RGB colorValue = fBackgroundColorEditor.getColorValue();
			ColorThemeSetting cp = setting.createCopy();
			cp.setBackgroundColor(colorValue);
			entries.put(key, cp);
		}

	}


	protected SelectionListener fStyleCheckBoxListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TreeItem selectedTreeItem = getSelectedTreeItem();
			if (selectedTreeItem == null) {
				return;
			}
			String key = selectedTreeItem.getText();

			Map<String, ColorThemeSetting> entries = theme.getEntries();
			ColorThemeSetting setting = entries.get(key);

			setting.setBoldEnabled(fFontBoldCheckBox.getSelection());
			setting.setItalicEnabled(fFontItalicCheckBox.getSelection());
			setting.setUnderlineEnabled(fFontUnderlineCheckBox.getSelection());
			setting.setStrikethroughEnabled(fFontStrikeThroughCheckBox
					.getSelection());
			setting.setUseCustomFont(fUseCustomFont.getSelection());
			setting.setUseCustomBackground(fUseCustomBackground.getSelection());
			if(!setting.useCustomBackground()){
				fBackgroundColorEditor.getButton().setEnabled(false);
			}else{
				fBackgroundColorEditor.getButton().setEnabled(true);
			}
			if(!setting.useCustomFont()){
				fCustomFont.setEnabled(false);
			}else{
				fCustomFont.setEnabled(true);
			}

			onAppearanceRelatedPreferenceChanged();
		}
	};
	
	private void configFontButton() {
		fCustomFont.setText("Select Font");
		fCustomFont.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                FontDialog fontDialog = new FontDialog(fCustomFont.getShell());
                
        		TreeItem selectedTreeItem = getSelectedTreeItem();
        		if (selectedTreeItem == null) {
        			return;
        		}
    			String key = selectedTreeItem.getText();
    			
    			Map<String, ColorThemeSetting> entries = theme.getEntries();
    			ColorThemeSetting setting = entries.get(key);
    			if(setting == null){
    				return;
    			}
				FontData chosenFont = setting.getFont();
				if(chosenFont != null){
					fontDialog.setFontList(new FontData[]{chosenFont});
				}
    			FontData font = fontDialog.open();
    			if (font != null) {
    				ColorThemeSetting copy = setting.createCopy();
    				copy.setFont(font);
    				entries.put(key, copy);
    				handleAppearanceColorListSelection();
    			}
    		}
		});		
	}

	protected void handleAppearanceColorListSelection() {
		TreeItem selectedTreeItem = getSelectedTreeItem();
		if (selectedTreeItem == null) {
			return;
		}
		String key = selectedTreeItem.getText();

		Map<String, ColorThemeSetting> entries = theme.getEntries();
		ColorThemeSetting setting = entries.get(key);
		Color color = setting.getColor();

		if (color == null) {
			color = new Color("#ffffff");
		}
		RGB rgb = color.getRGB();
		fForegroundColorEditor.setColorValue(rgb);
		
		Color backgroundColor = setting.getBackgroundColor();
		if(backgroundColor != null){
			fBackgroundColorEditor.setColorValue(backgroundColor.getRGB());
		}else{
			fBackgroundColorEditor.setColorValue(new RGB(255,255,255));
		}
		
		if (setting != null && setting.isBoldEnabled()) {
			fFontBoldCheckBox.setSelection(true);
		} else {
			fFontBoldCheckBox.setSelection(false);
		}
		if (setting != null && setting.isItalicEnabled()) {
			fFontItalicCheckBox.setSelection(true);
		} else {
			fFontItalicCheckBox.setSelection(false);
		}
		if (setting != null && setting.isUnderlineEnabled()) {
			fFontUnderlineCheckBox.setSelection(true);
		} else {
			fFontUnderlineCheckBox.setSelection(false);
		}
		if (setting != null && setting.isStrikethroughEnabled()) {
			fFontStrikeThroughCheckBox.setSelection(true);
		} else {
			fFontStrikeThroughCheckBox.setSelection(false);
		}
		if (setting != null && setting.useCustomFont()) {
			fUseCustomFont.setSelection(true);
			fCustomFont.setEnabled(true);
		} else {
			fUseCustomFont.setSelection(false);
			fCustomFont.setEnabled(false);
		}
		FontData font = setting.getFont();
		if(font == null){
			fCustomFont.setText("Select Font");
		}else{
			fCustomFont.setText(ColorThemeSetting.fontToString(font));
		}
		if (setting != null && setting.useCustomBackground()) {
			fUseCustomBackground.setSelection(true);
			fBackgroundColorEditor.getButton().setEnabled(true);
		} else {
			fUseCustomBackground.setSelection(false);
			fBackgroundColorEditor.getButton().setEnabled(false);
		}
		

		boolean enable = !ColorThemeKeys.KEYS_WITHOUT_STYLE.contains(key);
		fFontBoldCheckBox.setEnabled(enable);
		fFontItalicCheckBox.setEnabled(enable);
		fFontUnderlineCheckBox.setEnabled(enable);
		fFontStrikeThroughCheckBox.setEnabled(enable);
		fUseCustomFont.setEnabled(enable);
		fUseCustomBackground.setEnabled(enable);
		if(enable == false){
			fBackgroundColorEditor.getButton().setEnabled(false);
			fCustomFont.setEnabled(false);
		}
	}

	private void onAppearanceRelatedPreferenceChanged() {
		styledText.setFont(JFaceResources.getTextFont());
		ColorThemePreferencesPageStyledText.updateStyledText(new IStyledText() {

			public void setText(String text) {
				styledText.setText(text);
			}

			public void setStyleRanges(StyleRange[] ranges) {
				styledText.setStyleRanges(ranges);
			}

			public void setBackground(
					org.eclipse.swt.graphics.Color backgroundSwtColor) {
				styledText.setBackground(backgroundSwtColor);
			}

			public void setForeground(
					org.eclipse.swt.graphics.Color foregroundColor) {
				styledText.setForeground(foregroundColor);
			}

		}, theme);
	}

	protected Button addStyleCheckBox(Composite parent, String text) {
		return this.addStyleCheckBox(parent, text, 2);
	}
	protected Button addStyleCheckBox(Composite parent, String text, int horizontalSpan) {
		Button result = new Button(parent, SWT.CHECK);
		result.setText(text);
		GridData gd = new GridData();
		gd.horizontalSpan = horizontalSpan;
		result.setLayoutData(gd);
		result.addSelectionListener(fStyleCheckBoxListener);
		return result;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		createOkButton(parent, OK, "Ok", true);
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	protected Button createOkButton(Composite parent, int id, String label,
			boolean defaultButton) {
		// increment the number of columns in the button bar
		((GridLayout) parent.getLayout()).numColumns++;
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.setData(new Integer(id));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}

	private boolean isValidInput() {
		boolean valid = true;
		String currName = getName();
		if (currName.length() == 0) {
			setErrorMessage("Please provide a name for the theme.");
			valid = false;
		}
		if (currName.length() > 30) {
			setErrorMessage("The name of the theme should have at most 30 chars.");
			valid = false;
		}
		if (existingThemeNames.contains(currName)) {
			setMessage("Note: will override a theme with the same name.",
					IMessageProvider.INFORMATION);
		} else {
			setMessage(null, IMessageProvider.INFORMATION);
		}
		if (valid) {
			setErrorMessage(null);
		}
		return valid;
	}

	private String getName() {
		return name.getText().trim();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void saveInput() {
		theme.setName(getName());
		theme.setAuthor(author.getText());
		theme.setWebsite(website.getText());
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	private TreeItem getSelectedTreeItem() {
		TreeItem[] selection = fAppearanceColorTree.getSelection();
		if (selection != null && selection.length > 0) {
			return selection[0];
		}
		return null;
	}



	private void updateTreeItem(TreeItem selectedTreeItem, String key,
			Map<String, ColorThemeSetting> entries, RGB colorValue) {
		org.eclipse.swt.graphics.Color foreground;
		org.eclipse.swt.graphics.Color background;

		if (key.equals(ColorThemeKeys.BACKGROUND)) {
			foreground = getColor(entries.get(ColorThemeKeys.FOREGROUND)
					.getColor().getRGB());
			background = getColor(colorValue);

			// update the background of the needed items
			TreeItem[] items = fAppearanceColorTree.getItems();
			for (TreeItem treeItem : items) {
				if (!ColorThemeKeys.KEYS_BACKGROUND_RELATED.contains(treeItem
						.getText(0))) {
					treeItem.setBackground(background);
				}
			}
			fAppearanceColorTree.setBackground(background);

		} else {

			if (ColorThemeKeys.KEYS_BACKGROUND_RELATED
					.contains(selectedTreeItem.getText())) {
				foreground = getColor(entries.get(ColorThemeKeys.FOREGROUND)
						.getColor().getRGB());
				background = getColor(colorValue);
			} else {
				foreground = getColor(colorValue);
				background = getColor(entries.get(ColorThemeKeys.BACKGROUND)
						.getColor().getRGB());
			}

			// update the foreground of the needed items
			if (key.equals(ColorThemeKeys.FOREGROUND)) {
				TreeItem[] items = fAppearanceColorTree.getItems();
				for (TreeItem treeItem : items) {
					if (ColorThemeKeys.KEYS_BACKGROUND_RELATED
							.contains(treeItem.getText(0))) {
						treeItem.setForeground(foreground);
					}
				}
			}

		}
		selectedTreeItem.setForeground(foreground);
		selectedTreeItem.setBackground(background);
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);

		ColorTheme t = new ColorTheme();
		t.setName("theme");
		Map<String, ColorThemeSetting> entries = new HashMap<String, ColorThemeSetting>();
		t.setEntries(entries);
		entries.put(ColorThemeKeys.BACKGROUND, new ColorThemeSetting("#000000"));
		entries.put(ColorThemeKeys.FOREGROUND, new ColorThemeSetting("#ffffff"));
		entries.put(ColorThemeKeys.KEYWORD, new ColorThemeSetting("#ff0000"));

		t.setEntries(entries);
		Set<ColorTheme> s = new HashSet<ColorTheme>();
		s.add(t);
		EditThemeDialog dialog = new EditThemeDialog(shell, t, s);
		dialog.open();
		// while (!shell.isDisposed()) {
		// if (!display.readAndDispatch()) {
		// display.sleep();
		// }
		// }
		display.dispose();
	}

	public ColorTheme getTheme() {
		return this.theme;
	}
}