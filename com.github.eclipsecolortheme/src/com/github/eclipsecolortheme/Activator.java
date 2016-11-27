package com.github.eclipsecolortheme;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/** This plugin's activator. */
public class Activator extends AbstractUIPlugin {

	public static final String CURRENT_COLOR_THEME = "colorTheme";

	public static final String APPLY_THEME_TO = "APPLY_THEME_TO";
	public static final int APPLY_THEME_TO_LICLIPSE = 0;
	public static final int APPLY_THEME_TO_ALL_EDITORS = 1;
	public static final int APPLY_THEME_TO_KNOWN_PARTS = 2;
	public static final int APPLY_THEME_TO_WHOLE_IDE = 3;

	public static final String REAPPLY_ON_RESTART = "REAPPLY_ON_RESTART";
	public static final int REAPPLY_ON_RESTART_YES = 0; // Default is 0 (we
														// don't even need to
														// put it in the
														// settings).
	public static final int REAPPLY_ON_RESTART_NO = 1;

	public static final String PLUGIN_ID = "com.github.eclipsecolortheme";
	public static final String EXTENSION_POINT_ID_MAPPER = PLUGIN_ID
			+ ".mapper";
	public static final String EXTENSION_POINT_ID_THEME = PLUGIN_ID + ".theme";

	public static final String THEME_STYLED_TEXT_SCROLLBARS = "THEME_STYLED_TEXT_SCROLLBARS";
//	public static final String THEME_TREE_TABLE_SCROLLBARS = "THEME_TREE_TABLE_SCROLLBARS";

	private static Activator plugin;

	/** Creates a new activator. */
	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public ColorTheme getCurrentTheme() {
		return ColorThemeManager.getSingleton().getCurrentTheme();
	}
}
