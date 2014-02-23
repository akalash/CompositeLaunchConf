package com.github.akalash.compositeLaunchConf.ui.provider;

import java.util.Map;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TableContentProvider implements IStructuredContentProvider {
	public Object[] getElements(Object inputElement) {
		return ((Map<?, ?>) inputElement).keySet().toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
