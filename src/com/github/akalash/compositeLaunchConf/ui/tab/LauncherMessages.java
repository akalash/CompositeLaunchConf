package com.github.akalash.compositeLaunchConf.ui.tab;

import org.eclipse.osgi.util.NLS;
/**
 * Internalization class
 * @author anton
 */
class LauncherMessages extends NLS {
	private static final String BUNDLE_NAME = "com.github.akalash.compositeLaunchConf.ui.tab.messages"; //$NON-NLS-1$

	public static String CompositeTab_button_delete;

	public static String CompositeTab_button_down;

	public static String CompositeTab_button_up;

	public static String CompositeTab_launch_config;
	public static String CompositeTab_launch_config_type;

	public static String CompositeTab_name_tab;
	public static String CompositeTab_select_table_launch_config;
	public static String CompositeTab_tree_launch_config;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, LauncherMessages.class);
	}

	private LauncherMessages() {
	}
}
