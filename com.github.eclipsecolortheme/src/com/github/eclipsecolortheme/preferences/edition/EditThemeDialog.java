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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
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
	private ColorEditor fAppearanceColorEditor;
	private Button fAppearanceColorDefault;
	private Button fFontBoldCheckBox;
	private Button fFontItalicCheckBox;
	private Button fFontStrikeThroughCheckBox;
	private Button fFontUnderlineCheckBox;
	private Tree fAppearanceColorList;
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
		name.setText("");
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
			TreeItem item = new TreeItem(fAppearanceColorList, SWT.NONE);
			item.setText(entry);
			updateTreeItem(item, entry, entries, entries.get(entry).getColor()
					.getRGB());
		}

		fAppearanceColorList.getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fAppearanceColorList != null
						&& !fAppearanceColorList.isDisposed()) {
					fAppearanceColorList.select(fAppearanceColorList.getItem(0));
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

		fAppearanceColorList = new Tree(editorComposite, SWT.SINGLE
				| SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_BOTH);
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.heightHint = convertHeightInCharsToPixels(8);
		fAppearanceColorList.setLayoutData(gd);

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

		fAppearanceColorEditor = new ColorEditor(stylesComposite);
		Button foregroundColorButton = fAppearanceColorEditor.getButton();
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		foregroundColorButton.setLayoutData(gd);

		SelectionListener colorDefaultSelectionListener = new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				boolean systemDefault = fAppearanceColorDefault.getSelection();
				fAppearanceColorEditor.getButton().setEnabled(!systemDefault);

				// int i = fAppearanceColorList.getSelectionIndex();
				// String key = fAppearanceColorListModel[i][2];
				// if (key != null) {
				// fOverlayStore.setValue(key, systemDefault);
				// }
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};

		fAppearanceColorDefault = new Button(stylesComposite, SWT.CHECK);
		fAppearanceColorDefault.setText("System default");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.horizontalSpan = 2;
		fAppearanceColorDefault.setLayoutData(gd);
		fAppearanceColorDefault.setVisible(false);
		fAppearanceColorDefault
				.addSelectionListener(colorDefaultSelectionListener);

		fAppearanceColorList.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}

			public void widgetSelected(SelectionEvent e) {
				handleAppearanceColorListSelection();
			}
		});
		foregroundColorButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				// do nothing
			}

			public void widgetSelected(SelectionEvent e) {
				TreeItem selectedTreeItem = getSelectedTreeItem();
				if (selectedTreeItem == null) {
					return;
				}
				String key = selectedTreeItem.getText();
				updateEntry(selectedTreeItem, key);

				onAppearanceRelatedPreferenceChanged();
			}
		});

		fFontBoldCheckBox = addStyleCheckBox(stylesComposite, "Bold");
		fFontItalicCheckBox = addStyleCheckBox(stylesComposite, "Italic");
		fFontUnderlineCheckBox = addStyleCheckBox(stylesComposite, "Underline");
		fFontStrikeThroughCheckBox = addStyleCheckBox(stylesComposite,
				"Strikethrough");
	}

	protected SelectionListener fStyleCheckBoxListener = new SelectionListener() {
		public void widgetDefaultSelected(SelectionEvent e) {
			// do nothing
		}

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

			onAppearanceRelatedPreferenceChanged();
		}
	};

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
		fAppearanceColorEditor.setColorValue(rgb);
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

		boolean enable = !ColorThemeKeys.KEYS_WITHOUT_STYLE.contains(key);
		fFontBoldCheckBox.setEnabled(enable);
		fFontItalicCheckBox.setEnabled(enable);
		fFontUnderlineCheckBox.setEnabled(enable);
		fFontStrikeThroughCheckBox.setEnabled(enable);
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
		Button result = new Button(parent, SWT.CHECK);
		result.setText(text);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
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
		if (name.getText().length() == 0) {
			setErrorMessage("Please provide a name for the theme.");
			valid = false;
		}
		if (name.getText().length() > 30) {
			setErrorMessage("The name of the theme should have at most 30 chars.");
			valid = false;
		}
		if (existingThemeNames.contains(name.getText())) {
			setErrorMessage("Please provide a different name for the theme.");
			valid = false;
		}
		if (valid) {
			setErrorMessage(null);
		}
		return valid;
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void saveInput() {
		theme.setName(name.getText());
		theme.setAuthor(author.getText());
		theme.setWebsite(website.getText());
	}

	@Override
	protected void okPressed() {
		saveInput();
		super.okPressed();
	}

	private TreeItem getSelectedTreeItem() {
		TreeItem[] selection = fAppearanceColorList.getSelection();
		if (selection != null && selection.length > 0) {
			return selection[0];
		}
		return null;
	}

	private void updateEntry(TreeItem selectedTreeItem, String key) {
		Map<String, ColorThemeSetting> entries = theme.getEntries();

		ColorThemeSetting setting = entries.get(key);
		RGB colorValue = fAppearanceColorEditor.getColorValue();
		entries.put(key, setting.createCopy(colorValue));

		updateTreeItem(selectedTreeItem, key, entries, colorValue);
	}

	private void updateTreeItem(TreeItem selectedTreeItem, String key,
			Map<String, ColorThemeSetting> entries, RGB colorValue) {
		org.eclipse.swt.graphics.Color foreground;
		org.eclipse.swt.graphics.Color background;

		if (key.equals(ColorThemeKeys.BACKGROUND)) {
			foreground = getColor(colorValue);
			background = getColor(entries.get(ColorThemeKeys.FOREGROUND)
					.getColor().getRGB());

			TreeItem[] items = fAppearanceColorList.getItems();
			for (TreeItem treeItem : items) {
				treeItem.setBackground(foreground);
			}
			fAppearanceColorList.setBackground(foreground);

		} else {

			if (ColorThemeKeys.KEYS_WITHOUT_STYLE.contains(selectedTreeItem
					.getText())) {
				if (Color.isDarkColor(colorValue.red, colorValue.green,
						colorValue.blue)) {
					foreground = getColor(new RGB(255, 255, 255));
				} else {
					foreground = getColor(new RGB(0, 0, 0));
				}
				background = getColor(colorValue);
			} else {
				foreground = getColor(colorValue);
				background = getColor(entries.get(ColorThemeKeys.BACKGROUND)
						.getColor().getRGB());
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