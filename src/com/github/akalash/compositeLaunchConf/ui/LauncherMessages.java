package com.github.akalash.compositeLaunchConf.ui;

import org.eclipse.osgi.util.NLS;

public class LauncherMessages extends NLS {
	private static final String BUNDLE_NAME = "com.github.akalash.compositeLaunchConf.ui.messages"; //$NON-NLS-1$
	public static String CompositeTab_button_delete;
	public static String CompositeTab_button_down;
	public static String CompositeTab_button_up;
	public static String CompositeTab_launch_config;
	public static String CompositeTab_launch_config_type;
	public static String CompositeTab_name_tab;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, LauncherMessages.class);
	}

	private LauncherMessages() {
	}
}
