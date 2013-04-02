package com.github.eclipsecolortheme.preferences;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.github.eclipsecolortheme.Activator;
import com.github.eclipsecolortheme.ColorTheme;
import com.github.eclipsecolortheme.ColorThemeApplier;
import com.github.eclipsecolortheme.ColorThemeManager;
import com.github.eclipsecolortheme.preferences.ColorThemePreferencesPageStyledText.IStyledText;

/** The preference page for managing color themes. */
public class ColorThemePreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	private ColorThemeManager colorThemeManager = new ColorThemeManager();
	private Composite container;
	private List themeSelectionList;
	private Composite themeSelection;
	private Composite themeDetails;
	private Label authorLabel;
	private Link websiteLink;
	private StyledText styledText;

	/** Creates a new color theme preference page. */
	public ColorThemePreferencePage() {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		container = new Composite(parent, SWT.NONE);
		GridData gridData = new GridData();
		GridLayout containerLayout = new GridLayout(1, true);
		containerLayout.marginWidth = 0;
		container.setLayout(containerLayout);

		gridData = new GridData(GridData.FILL_BOTH);
		themeSelection = new Composite(container, SWT.NONE);
		GridLayout themeSelectionLayout = new GridLayout(2, false);
		themeSelectionLayout.marginWidth = 0;
		themeSelectionLayout.marginHeight = 0;
		themeSelection.setLayout(themeSelectionLayout);
		themeSelection.setLayoutData(gridData);

		gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.minimumWidth = 120;
		themeSelectionList = new List(themeSelection, SWT.BORDER | SWT.V_SCROLL);
		themeSelectionList.setLayoutData(gridData);
		fillThemeSelectionList();

		gridData = new GridData(GridData.FILL_BOTH);
		gridData.widthHint = 400;
		gridData.verticalAlignment = SWT.TOP;
		GridLayout themeDetailsLayout = new GridLayout(1, true);
		themeDetailsLayout.marginWidth = 0;
		themeDetailsLayout.marginHeight = 0;
		themeDetails = new Composite(themeSelection, SWT.NONE);
		themeDetails.setLayoutData(gridData);
		themeDetails.setLayout(themeDetailsLayout);
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 306;
		styledText = new StyledText(themeDetails, SWT.BORDER);
		styledText.setLayoutData(gridData);
		styledText.setText("");
		authorLabel = new Label(themeDetails, SWT.NONE);
		GridDataFactory.swtDefaults().grab(true, false).applyTo(authorLabel);
		websiteLink = new Link(themeDetails, SWT.NONE);
		GridDataFactory.swtDefaults().grab(true, false).applyTo(websiteLink);

		themeSelectionList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				updateDetails(colorThemeManager.getTheme(themeSelectionList
						.getSelection()[0]));
			}
		});

		String activeThemeName = getPreferenceStore().getString(
				Activator.CURRENT_COLOR_THEME);
		if (colorThemeManager.getTheme(activeThemeName) == null)
			activeThemeName = "Default";
		themeSelectionList.setSelection(new String[] { activeThemeName });
		updateDetails(colorThemeManager.getTheme(activeThemeName));

		Link ectLink = new Link(container, SWT.NONE);
		ectLink.setText("Download more themes or create your own on "
				+ "<a>eclipsecolorthemes.org</a>.");
		setLinkTarget(ectLink, "http://eclipsecolorthemes.org");
		return container;
	}

	private void fillThemeSelectionList() {
		Set<ColorTheme> themes = colorThemeManager.getThemes();
		java.util.List<String> themeNames = new LinkedList<String>();
		for (ColorTheme theme : themes)
			themeNames.add(theme.getName());
		Collections.sort(themeNames, String.CASE_INSENSITIVE_ORDER);
		themeNames.add(0, "Default");
		themeSelectionList.setItems(themeNames.toArray(new String[themeNames
				.size()]));
	}

	private static void setLinkTarget(Link link, final String target) {
		link.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Program.launch(target);
			}
		});
	}

	private void updateDetails(ColorTheme theme) {
		if (theme == null)
			themeDetails.setVisible(false);
		else {
			authorLabel.setText("Created by " + theme.getAuthor());
			String website = theme.getWebsite();
			if (website == null || website.length() == 0)
				websiteLink.setVisible(false);
			else {
				websiteLink.setText("<a>" + website + "</a>");
				for (Listener listener : websiteLink
						.getListeners(SWT.Selection))
					websiteLink.removeListener(SWT.Selection, listener);
				setLinkTarget(websiteLink, website);
				websiteLink.setVisible(true);
			}

			styledText.setFont(JFaceResources.getTextFont());
			ColorThemePreferencesPageStyledText.updateStyledText(
					new IStyledText() {

						public void setText(String text) {
							styledText.setText(text);
						}

						public void setStyleRanges(StyleRange[] ranges) {
							styledText.setStyleRanges(ranges);
						}

						public void setBackground(Color backgroundSwtColor) {
							styledText.setBackground(backgroundSwtColor);
						}

						public void setForeground(Color foregroundColor) {
							styledText.setForeground(foregroundColor);
						}

					}, theme);
			themeDetails.setVisible(true);
			authorLabel.pack();
			websiteLink.pack();
		}
	}

	private static final Set<String> IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN = new HashSet<String>();
	static {
		IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN
				.add("org.eclipse.cdt.ui.editor.CEditor");
		IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN
				.add("com.brainwy.liclipse.editor.common.LiClipseEditor");
		IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN
				.add("org.python.pydev.editor.PythonEditor");
	}

	@Override
	public boolean performOk() {
		IWorkbenchPage activePage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		try {
			java.util.List<IEditorReference> editorsToClose = new ArrayList<IEditorReference>();
			Map<IEditorInput, String> editorsToReopen = new HashMap<IEditorInput, String>();
			for (IEditorReference editor : activePage.getEditorReferences()) {
				String id = editor.getId();
				/*
				 * C++ editors are not closed/reopened because it messes their
				 * colors up. TODO: Make this configurable in the mapping file.
				 */
				if (!IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN.contains(id)) {
					editorsToClose.add(editor);
					editorsToReopen.put(editor.getEditorInput(), id);
				}
			}

			if (!editorsToClose.isEmpty()) {
				if (!MessageDialog
						.openConfirm(
								getShell(),
								"Reopen Editors",
								"In order to change the color theme, some editors have to be closed and reopened.")) {
					return false;
				}

				activePage.closeEditors(editorsToClose
						.toArray(new IEditorReference[editorsToClose.size()]),
						true);
			}

			String selectedThemeName = themeSelectionList.getSelection()[0];
			getPreferenceStore().setValue("colorTheme", selectedThemeName);
			ColorTheme theme = colorThemeManager.getTheme(selectedThemeName);
			ColorThemeApplier.applyTheme.call(theme);

			for (IEditorInput editorInput : editorsToReopen.keySet())
				activePage.openEditor(editorInput,
						editorsToReopen.get(editorInput));
		} catch (PartInitException e) {
			// TODO: Show a proper error message (StatusManager).
			e.printStackTrace();
		}

		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		getPreferenceStore().setToDefault("colorTheme");
		colorThemeManager.clearImportedThemes();
		reloadThemeSelectionList();
		super.performDefaults();
	}

	@Override
	protected void contributeButtons(Composite parent) {
		((GridLayout) parent.getLayout()).numColumns++;

		Button button = new Button(parent, SWT.NONE);
		button.setText("&Import a theme...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				FileDialog dialog = new FileDialog(getShell());
				String file = dialog.open();
				ColorTheme theme;
				try {
					String content = readFile(new File(file));
					theme = colorThemeManager.saveTheme(content);
				} catch (IOException e) {
					theme = null;
				}
				if (theme != null) {
					reloadThemeSelectionList();
				} else {
					MessageBox box = new MessageBox(getShell(), SWT.OK);
					box.setText("Theme not imported");
					box.setMessage("This is not a valid theme file.");
					box.open();
				}
			}
		});
	}

	private void reloadThemeSelectionList() {
		themeSelectionList.removeAll();
		fillThemeSelectionList();
		themeSelectionList.setSelection(new String[] { "Default" });
		updateDetails(null);
		container.pack();
	}

	private static String readFile(File file) throws IOException {
		Reader in = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		char[] chars = new char[1 << 16];
		int length;
		while ((length = in.read(chars)) > 0)
			sb.append(chars, 0, length);
		return sb.toString();
	}
}