package com.github.akalash.compositeLaunchConf;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.launching.AbstractJavaLaunchConfigurationDelegate;

import com.github.akalash.compositeLaunchConf.api.ICompositeLaunchConfigurationConstants;
/**
 * When the user tries to use
 * the launch configuration, this delegate will be execution all inner launch configurations
 * @author anton
 *
 */
public class CompositeLaunchConfigurationDelegate extends
		AbstractJavaLaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate {
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		List<String> params = configuration.getAttribute(
				ICompositeLaunchConfigurationConstants.ATTR_LIST_LAUNCHES,
				(List<String>) null);
		all:for(String key:params){
			ILaunch l = getLaunchManager().getLaunchConfiguration(key).launch(mode, monitor);	
			while(!l.isTerminated()){
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					monitor.setCanceled(true);
				}
				if(monitor!=null&&monitor.isCanceled()){
					l.terminate();
					break all;
				}
			}
			
		}
		
	}
}
