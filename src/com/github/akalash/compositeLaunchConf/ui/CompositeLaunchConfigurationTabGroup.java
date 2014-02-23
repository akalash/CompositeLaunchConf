package com.github.akalash.compositeLaunchConf.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

import com.github.akalash.compositeLaunchConf.ui.tab.CompositeTab;


public class CompositeLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup{
	@Override
	public void createTabs(ILaunchConfigurationDialog arg0, String arg1) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new CompositeTab(),
				new CommonTab()
		};
		setTabs(tabs);		
	}
}
