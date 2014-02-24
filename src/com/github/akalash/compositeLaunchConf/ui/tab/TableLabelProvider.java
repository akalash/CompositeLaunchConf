package com.github.akalash.compositeLaunchConf.ui.tab;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.github.akalash.compositeLaunchConf.api.ITableViewerInput;
/**
 * 
 * @author anton
 */
class TableLabelProvider implements ITableLabelProvider {
	
	private ITableViewerInput table;
	
	
	public TableLabelProvider(ITableViewerInput table) {
		super();
		this.table = table;
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex == 0) {
			return element.toString();
		}
		try {
			return table.getViewerInput().get((String) element).getType().getName();
		} catch (CoreException e) {}
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}
}
