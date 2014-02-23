package com.github.akalash.compositeLaunchConf.api;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;

public interface ILaunchManagerProvider {
	public ILaunchConfiguration[] getLaunchConfigurations(
			ILaunchConfigurationType type);
}
