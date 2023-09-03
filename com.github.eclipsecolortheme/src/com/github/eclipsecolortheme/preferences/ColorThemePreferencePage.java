package com.github.eclipsecolortheme.preferences;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
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
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.github.eclipsecolortheme.Activator;
import com.github.eclipsecolortheme.ColorTheme;
import com.github.eclipsecolortheme.ColorThemeApplier;
import com.github.eclipsecolortheme.ColorThemeManager;
import com.github.eclipsecolortheme.preferences.ColorThemePreferencesPageStyledText.IStyledText;
import com.github.eclipsecolortheme.preferences.edition.EditThemeDialog;

/** The preference page for managing color themes. */
public class ColorThemePreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	private ColorThemeManager colorThemeManager = ColorThemeManager.getSingleton();
	private Composite container;
	private List themeSelectionList;
	private Composite themeSelection;
	private Composite themeDetails;
	private Label authorLabel;
	private Button editButton;
	private Button removeButton;
	private Link websiteLink;
	private StyledText styledText;
	private Label themeDefaultMessageLabel;
	private Combo applyTo;
	private Combo reapplyOnRestart;
	private java.util.List<Control> invisibleWhenDefaultSelected = new ArrayList<Control>();
	private int initiallyApplyTo;
	private Shell shell;
//	private Button themeStyledTextScrollbars;

	/** Creates a new color theme preference page. */
	public ColorThemePreferencePage() {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	public void setShell(Shell shell) {
		this.shell = shell;
	}

	@Override
	public Shell getShell() {
		if (this.shell != null) {
			return this.shell;
		}
		return super.getShell();
	}

	@Override
	public Control createContents(Composite parent) {
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
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.verticalAlignment = SWT.TOP;

		GridLayout themeDetailsLayout = new GridLayout(2, true);
		themeDetailsLayout.marginWidth = 0;
		themeDetailsLayout.marginHeight = 0;

		themeDetails = new Composite(themeSelection, SWT.NONE);
		themeDetails.setLayoutData(gridData);
		themeDetails.setLayout(themeDetailsLayout);

		// Option to apply preferences to the whole ide
		// Apply theme to:
		applyTo = new Combo(themeDetails, SWT.READ_ONLY);
		applyTo.setToolTipText("Please choose where the colors should be applied to.\n\n"
				+ "Applying to all views / whole IDE will also change the Appearance\ntheme to the Base LiClipse Theme and may need a restart.");
		applyTo.add("Apply only to LiClipse Editors");
		applyTo.add("Apply to all Editors");
//		applyTo.add("Apply to all Editors and Known Views");
		applyTo.add("Apply to the whole IDE (including preferences and dialogs).");
		createGridDataFactory().align(SWT.FILL, SWT.CENTER).applyTo(applyTo);
		IPreferenceStore store = getPreferenceStore();
		initiallyApplyTo = store.getInt(Activator.APPLY_THEME_TO);
		if (initiallyApplyTo > 2) {
			initiallyApplyTo = 2;
		}
		applyTo.select(initiallyApplyTo);

		// On Restart:
		reapplyOnRestart = new Combo(themeDetails, SWT.READ_ONLY);
		reapplyOnRestart.setText("Reapply settings on restart.");
		reapplyOnRestart.add("Reapply settings on restart");
		reapplyOnRestart.setToolTipText(
				"By default, the settings are re-applied on a restart to keep up with new plugins and changes in the theme,\n"
						+ "but if you wish to manually configure colors or have different colors for each plugin, this may be turned off.");
		reapplyOnRestart.add("Don't reapply settings on restart");
		createGridDataFactory().align(SWT.FILL, SWT.CENTER).applyTo(reapplyOnRestart);
		reapplyOnRestart.select(store.getInt(Activator.REAPPLY_ON_RESTART));
		// End Option to apply preferences to the whole ide

//		themeStyledTextScrollbars = new Button(themeDetails, SWT.CHECK);
//		themeStyledTextScrollbars.setText("Apply scrollbar theming customization to StyledText editors?");
//		GridDataFactory.swtDefaults().span(2, 1).grab(true, false).applyTo(themeStyledTextScrollbars);
//		themeStyledTextScrollbars.setSelection(store.getBoolean(Activator.THEME_STYLED_TEXT_SCROLLBARS));

		// Message for default.
		themeDefaultMessageLabel = new Label(themeDetails, SWT.NONE);
		themeDefaultMessageLabel.setText("");
		GridDataFactory.swtDefaults().span(2, 1).grab(true, false).applyTo(themeDefaultMessageLabel);
		themeDefaultMessageLabel.setVisible(false);
		// End Message for default.

		gridData = new GridData(GridData.FILL_BOTH);
		gridData.heightHint = 306;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;

		styledText = new StyledText(themeDetails, SWT.BORDER);
		styledText.setLayoutData(gridData);
		styledText.setText("");
		invisibleWhenDefaultSelected.add(styledText);

		authorLabel = new Label(themeDetails, SWT.NONE);
		createGridDataFactory().applyTo(authorLabel);
		invisibleWhenDefaultSelected.add(authorLabel);

		websiteLink = new Link(themeDetails, SWT.NONE);
		createGridDataFactory().applyTo(websiteLink);
		invisibleWhenDefaultSelected.add(websiteLink);

		editButton = new Button(themeDetails, SWT.NONE);
		editButton.setText("Edit theme");
		invisibleWhenDefaultSelected.add(editButton);
		GridData grab = createGridDataFactoryNoSpan().create();
		editButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String[] selection = themeSelectionList.getSelection();
				if (selection != null && selection.length > 0) {
					EditThemeDialog dialog = new EditThemeDialog(getShell(), colorThemeManager.getTheme(selection[0]),
							colorThemeManager.getThemes());
					if (dialog.open() == Window.OK) {
						ColorTheme theme = dialog.getTheme();
						String content = theme.toXML();
						saveEditedTheme(content);
					}
				}
			}
		});
		grab.horizontalAlignment = SWT.FILL;
		editButton.setLayoutData(grab);

		removeButton = new Button(themeDetails, SWT.NONE);
		removeButton.setText("Remove theme");
		invisibleWhenDefaultSelected.add(removeButton);
		grab = createGridDataFactoryNoSpan().create();
		removeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				String[] selection = themeSelectionList.getSelection();
				if (selection != null && selection.length > 0) {
					ColorTheme themeToRemove = colorThemeManager.getTheme(selection[0]);
					String importedThemeId = themeToRemove.getImportedThemeId();
					if (importedThemeId == null || importedThemeId.length() == 0) {
						MessageDialog.openInformation(shell, "Unable to remove", "Builtin themes cannot be removed.");
						return;
					}
					if (MessageDialog.openQuestion(shell, "Confirm removal",
							"Are you sure you want to remove the theme: " + themeToRemove.getName())) {
						removeTheme(themeToRemove);
					}
				}
			}
		});
		grab.horizontalAlignment = SWT.FILL;
		removeButton.setLayoutData(grab);

		themeSelectionList.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				updateDetails(colorThemeManager.getTheme(themeSelectionList.getSelection()[0]));
			}
		});

		String activeThemeName = getPreferenceStore().getString(Activator.CURRENT_COLOR_THEME);
		if (colorThemeManager.getTheme(activeThemeName) == null)
			activeThemeName = "Default";
		themeSelectionList.setSelection(new String[] { activeThemeName });
		updateDetails(colorThemeManager.getTheme(activeThemeName));

		Link ectLink = new Link(container, SWT.NONE);
		ectLink.setText("Download more themes or create your own on " + "<a>eclipsecolorthemes.org</a>.");
		setLinkTarget(ectLink, "http://eclipsecolorthemes.org");

		// store the selection!
		lastSelectedThemeName = themeSelectionList.getSelection()[0];
		lastApplyToWholeIDESelected = applyTo.getSelectionIndex();

		return container;
	}

	private GridDataFactory createGridDataFactory() {
		return GridDataFactory.swtDefaults().grab(true, false).span(2, 1);
	}

	private GridDataFactory createGridDataFactoryNoSpan() {
		return GridDataFactory.swtDefaults().grab(true, false).span(1, 1);
	}

	private void fillThemeSelectionList() {
		Set<ColorTheme> themes = colorThemeManager.getThemes();
		java.util.List<String> themeNames = new LinkedList<String>();
		for (ColorTheme theme : themes)
			themeNames.add(theme.getName());
		Collections.sort(themeNames, String.CASE_INSENSITIVE_ORDER);
		themeNames.add(0, "Default");
		themeSelectionList.setItems(themeNames.toArray(new String[themeNames.size()]));
	}

	private static void setLinkTarget(Link link, final String target) {
		link.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Program.launch(target);
			}
		});
	}

	private void updateDetails(ColorTheme theme) {
		if (theme == null) {
			// themeDetails.setVisible(false);
			for (Control c : invisibleWhenDefaultSelected) {
				c.setVisible(false);
			}
			themeDefaultMessageLabel.setText("When default is chosen, applying the choice will\n"
					+ "reset all colors to the default Eclipse configuration\n"
					+ "(but only on the scope selected above).\n" + "\n" + "A restart may be required afterwards.");

			this.themeDefaultMessageLabel.setVisible(true);
//			this.themeStyledTextScrollbars.setVisible(false);
		} else {
			authorLabel.setText("Created by " + theme.getAuthor());
			String website = theme.getWebsite();
			if (website == null || website.length() == 0)
				websiteLink.setVisible(false);
			else {
				websiteLink.setText("<a>" + website + "</a>");
				for (Listener listener : websiteLink.getListeners(SWT.Selection))
					websiteLink.removeListener(SWT.Selection, listener);
				setLinkTarget(websiteLink, website);
				websiteLink.setVisible(true);
			}

			styledText.setFont(JFaceResources.getTextFont());
			ColorThemePreferencesPageStyledText.updateStyledText(new IStyledText() {

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

			Display display = Display.getCurrent();
			if (display != null) {
				// fix the following issue: upon opening it, swt may change its
				// background colors afterward, so, we need to reapply it.
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						Control control = themeDefaultMessageLabel;
						if (control != null && !control.isDisposed()) {
							updateDetails(colorThemeManager.getTheme(themeSelectionList.getSelection()[0]));
						}
					}
				});
			}
			// themeDetails.setVisible(true);
			for (Control c : invisibleWhenDefaultSelected) {
				c.setVisible(true);
			}
			themeDefaultMessageLabel.setText("");
			this.themeDefaultMessageLabel.setVisible(false);
//			this.themeStyledTextScrollbars.setVisible(true);
		}
		// applyTo.pack(true);
		themeDefaultMessageLabel.pack(true);
		authorLabel.pack(true);
		websiteLink.pack(true);
		themeDetails.pack(true);
	}

	private static final Set<String> IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN = new HashSet<String>();
	static {
		IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN.add("org.eclipse.cdt.ui.editor.");
		// -- the colors for the folding bar is not updated properly, so, leave
		// the ones below there!
		// IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN.add("com.brainwy.liclipse.");
		// IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN.add("org.python.pydev.editor.");
	}

	private String lastSelectedThemeName = null;
	private int lastApplyToWholeIDESelected = -1;

	@Override
	protected void performApply() {
		// force it to happen!
		lastSelectedThemeName = null;
		lastApplyToWholeIDESelected = -1;
		performOk();
		lastSelectedThemeName = themeSelectionList.getSelection()[0];
		lastApplyToWholeIDESelected = applyTo.getSelectionIndex();
	}

	@Override
	public boolean performOk() {
		performOk(true);
		return super.performOk();
	}

	/**
	 * @param canAskQuestions whether we can ask questions to the user here.
	 * @return a boolean where true means we should've asked for a restart but
	 *         didn't.
	 */
	public boolean performOk(boolean canAskQuestions) {
		boolean shouldHaveAskedRestart = false;

		try {
			String selectedThemeName = themeSelectionList.getSelection()[0];
			// Do this one regardless of the others as it only affects a
			// restart.
			IPreferenceStore preferenceStore = getPreferenceStore();

			preferenceStore.setValue(Activator.REAPPLY_ON_RESTART, reapplyOnRestart.getSelectionIndex());

//			boolean changedThemeStyledTextToolbars = preferenceStore
//					.getBoolean(Activator.THEME_STYLED_TEXT_SCROLLBARS) != themeStyledTextScrollbars.getSelection();
//			if (changedThemeStyledTextToolbars) {
//				preferenceStore.setValue(Activator.THEME_STYLED_TEXT_SCROLLBARS,
//						themeStyledTextScrollbars.getSelection());
//			}

			int applyToWholeIDESelected = applyTo.getSelectionIndex();
			boolean onlyLiClipseEditors = applyToWholeIDESelected == Activator.APPLY_THEME_TO_LICLIPSE;

			java.util.List<IEditorReference> editorsToClose = new ArrayList<IEditorReference>();
			Map<IEditorInput, String> editorsToReopen = new HashMap<IEditorInput, String>();
			IWorkbenchPage activePage = getActivePage();
			if (activePage != null) {
				for (IEditorReference editor : activePage.getEditorReferences()) {
					String id = editor.getId();
					if (onlyLiClipseEditors) {
						if (!id.startsWith("com.brainwy.liclipse.")) {
							continue;
						}
						if (!id.startsWith("org.brainwy.liclipsetext.")) {
							continue;
						}
					}
					/*
					 * C++ editors are not closed/reopened because it messes their colors up. TODO:
					 * Make this configurable in the mapping file.
					 */
					boolean needsStart = true;
					for (String editorId : IDS_FOR_EDITORS_THAT_DONT_NEED_REOPEN) {
						if (id.startsWith(editorId)) {
							needsStart = false;
							break;
						}
					}
					if (needsStart && canAskQuestions) { // if we can't ask,
															// don't collect
															// editors
						editorsToClose.add(editor);
						editorsToReopen.put(editor.getEditorInput(), id);
					}
				}
			}

			if (lastSelectedThemeName != null && lastSelectedThemeName.equals(selectedThemeName)) {
				if (lastApplyToWholeIDESelected == applyTo.getSelectionIndex() //&& !changedThemeStyledTextToolbars
				        ) {
					// everything matches: as we already applied, do nothing and
					// return.
					return true;
				}
			}

			preferenceStore.setValue(Activator.CURRENT_COLOR_THEME, selectedThemeName);
			preferenceStore.setValue(Activator.APPLY_THEME_TO, applyToWholeIDESelected);
			boolean restart = false;
			if ((applyToWholeIDESelected != initiallyApplyTo && (initiallyApplyTo == Activator.APPLY_THEME_TO_WHOLE_IDE
					|| applyToWholeIDESelected == Activator.APPLY_THEME_TO_WHOLE_IDE
					|| initiallyApplyTo == Activator.APPLY_THEME_TO_KNOWN_PARTS
					|| applyToWholeIDESelected == Activator.APPLY_THEME_TO_KNOWN_PARTS))) {
				if (canAskQuestions) {
					if (MessageDialog.openQuestion(getShell(), "Restart?",
							"A restart may be required to properly apply the required changes.\n\nRestart now?")) {
						restart = true;
					}
				} else {
					shouldHaveAskedRestart = true;
				}
			}

			boolean reopen = false;
			if (!restart) {
				// only check editors if we won't be restarting
				if (!editorsToClose.isEmpty()) {
					if (MessageDialog.openQuestion(getShell(), "Reopen Editors",
							"In order to update the colors properly, some editors may have to be closed and reopened.\n\nDo you want to close/reopen the editors?")) {
						reopen = true;
						activePage.closeEditors(editorsToClose.toArray(new IEditorReference[editorsToClose.size()]),
								true);
					}

				}
			}
			ColorTheme theme = colorThemeManager.getTheme(selectedThemeName);
			ColorThemeApplier.applyTheme.call(theme);

			if (restart) {
				PlatformUI.getWorkbench().restart();
				return true;
			}
			if (reopen) {
				for (IEditorInput editorInput : editorsToReopen.keySet()) {
					activePage.openEditor(editorInput, editorsToReopen.get(editorInput));
				}
			}
		} catch (PartInitException e) {
			// TODO: Show a proper error message (StatusManager).
			e.printStackTrace();
		}

		return shouldHaveAskedRestart;
	}

	private IWorkbenchPage getActivePage() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
			if (activeWorkbenchWindow != null) {
				return activeWorkbenchWindow.getActivePage();
			}
		}
		return null;
	}

	@Override
	protected void performDefaults() {
		if (!MessageDialog.openConfirm(getShell(), "Confirm",
				"Are you sure?\nPressing Ok will remove all imported themes as well as any theme you created.")) {
			return;
		}
		getPreferenceStore().setToDefault(Activator.CURRENT_COLOR_THEME);
		// getPreferenceStore().setToDefault(Activator.APPLY_THEME_TO);
		colorThemeManager.clearImportedThemes();
		reloadThemeSelectionList(null);
		super.performDefaults();
	}

	@Override
	protected void contributeButtons(Composite parent) {
		((GridLayout) parent.getLayout()).numColumns += 2;

		Button button = new Button(parent, SWT.NONE);
		button.setText("&Import a theme...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				FileDialog dialog = new FileDialog(getShell());
				String file = dialog.open();
				if (file != null) {
					String content;
					try {
						content = readFile(new File(file));
					} catch (IOException e) {
						content = null;
					}
					importThemeFromContents(content);
				}
			}
		});

		button = new Button(parent, SWT.NONE);
		button.setText("&Export selected theme...");
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				String file = dialog.open();
				if (file != null) {
					String[] selection = themeSelectionList.getSelection();
					if (selection != null && selection.length > 0) {
						ColorTheme themeToExport = colorThemeManager.getTheme(selection[0]);
						if (themeToExport != null) {
							String xml = themeToExport.toXML();
							File file2 = new File(file);
							if (file2.exists()) {
								if (!MessageDialog.openQuestion(getShell(), "File exists",
										"File: " + file2 + " already exists.\nOverride?")) {
									return;
								}
							}
							try {

								FileOutputStream fileOutputStream = new FileOutputStream(file2);
								try {
									BufferedOutputStream buf = new BufferedOutputStream(fileOutputStream);
									try {
										buf.write(xml.getBytes("utf-8"));
									} finally {
										buf.close();
									}
								} finally {
									fileOutputStream.close();
								}
							} catch (Exception e) {
								MessageDialog.openError(getShell(), "Error exporting theme",
										"Error exporting theme: " + e.getMessage());
								return;
							}
							MessageDialog.openInformation(getShell(), "Theme exported",
									"Theme " + themeToExport.getName() + " exported.");
						}
					}
				}
			}
		});
	}

	private void reloadThemeSelectionList(ColorTheme newTheme) {
		themeSelectionList.removeAll();
		fillThemeSelectionList();
		if (newTheme == null) {
			themeSelectionList.setSelection(new String[] { "Default" });

		} else {
			themeSelectionList.setSelection(new String[] { newTheme.getName() });

		}
		updateDetails(newTheme);
		// container.pack(true);
	}

	/**
	 * Remove an existing theme.
	 */
	private void removeTheme(ColorTheme theme) {
		if (theme != null) {
			colorThemeManager.removeTheme(theme);
			reloadThemeSelectionList(null);
		}
	}

	/**
	 * If the name already exists, replace it, otherwise create a new one.
	 */
	private void saveEditedTheme(String content) {
		ColorTheme theme;
		if (content != null) {
			theme = colorThemeManager.saveEditedTheme(content);
			if (theme != null) {
				reloadThemeSelectionList(theme);
			} else {
				MessageBox box = new MessageBox(getShell(), SWT.OK);
				box.setText("Theme not saved");
				box.setMessage("Contents not vallid.");
				box.open();
			}
		}
	}

	private void importThemeFromContents(String content) {
		ColorTheme theme;
		if (content != null) {
			theme = colorThemeManager.saveTheme(content);
			if (theme != null) {
				reloadThemeSelectionList(theme);
			} else {
				MessageBox box = new MessageBox(getShell(), SWT.OK);
				box.setText("Theme not imported");
				box.setMessage("This is not a valid theme file.");
				box.open();
			}
		}
	}

	private static String readFile(File file) throws IOException {
		Reader in = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		char[] chars = new char[1 << 16];
		int length;
		try {
			while ((length = in.read(chars)) > 0)
				sb.append(chars, 0, length);
		} finally {
			in.close();
		}
		return sb.toString();
	}

	// @Override
	// protected void contributeButtons(Composite parent) {
	// ((GridLayout) parent.getLayout()).numColumns++;
	//
	// Button button = new Button(parent, SWT.NONE);
	// button.setText("&Import a theme...");
	// button.addSelectionListener(new SelectionAdapter() {
	// @Override
	// public void widgetSelected(SelectionEvent event) {
	// FileDialog dialog = new FileDialog(getShell());
	// String file = dialog.open();
	// BufferedInputStream bufferedFileStream = null;
	// try {
	// bufferedFileStream = new BufferedInputStream(new FileInputStream(file));
	// colorThemeManager.saveTheme(bufferedFileStream);
	// } catch (CharConversionException e) {
	// showErrorMessage("Invalid file encoding.");
	// return;
	// } catch (Exception e) {
	// showErrorMessage("This is not a valid theme file.");
	// return;
	// } finally {
	// try {
	// if(bufferedFileStream != null)
	// bufferedFileStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// reloadThemeSelectionList();
	// }
	// });
	// }

	protected void showErrorMessage(String message) {
		MessageBox box = new MessageBox(getShell(), SWT.OK);
		box.setText("Theme not imported");
		box.setMessage(message);
		box.open();
	}

	private void reloadThemeSelectionList() {
		themeSelectionList.removeAll();
		fillThemeSelectionList();
		themeSelectionList.setSelection(new String[] { "Default" });
		updateDetails(null);
		// container.pack(true);
	}
}