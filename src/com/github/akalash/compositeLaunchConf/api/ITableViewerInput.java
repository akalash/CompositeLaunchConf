package com.github.akalash.compositeLaunchConf.api;

import java.util.Map;

import org.eclipse.debug.core.ILaunchConfiguration;

public interface ITableViewerInput {
	public Map<String, ILaunchConfiguration> getViewerInput();
}
