package com.github.akalash.compositeLaunchConf.api;

import java.util.Map;

import org.eclipse.debug.core.ILaunchConfiguration;
/**
 * @author anton
 */
public interface ITableViewerInput {
	public Map<String, ILaunchConfiguration> getViewerInput();
}
