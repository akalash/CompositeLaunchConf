package com.github.akalash.compositeLaunchConf.ui.tab;
/**
 * 
 * @author anton
 */
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.github.akalash.compositeLaunchConf.api.ILaunchManagerProvider;

class TreeContentProvider implements ITreeContentProvider {

	private ILaunchManagerProvider managerProvider;

	public TreeContentProvider(ILaunchManagerProvider managerProvider) {
		super();
		this.managerProvider = managerProvider;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object inputElement) {		
		return (Object[]) inputElement;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ILaunchConfigurationType) {
			return managerProvider
					.getLaunchConfigurations((ILaunchConfigurationType) parentElement);
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof ILaunchConfiguration) {
			ILaunchConfiguration conf = (ILaunchConfiguration) element;
			try {
				return conf.getType();
			} catch (CoreException e) {
			}
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		ILaunchConfiguration[] confs = null;
		if (element instanceof ILaunchConfigurationType) {
			confs = managerProvider
					.getLaunchConfigurations((ILaunchConfigurationType) element);
		}
		return confs != null && confs.length != 0;
	}
}
