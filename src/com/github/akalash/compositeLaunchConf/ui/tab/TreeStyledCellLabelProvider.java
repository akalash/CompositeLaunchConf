package com.github.akalash.compositeLaunchConf.ui.tab;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.widgets.TreeItem;

import com.github.akalash.compositeLaunchConf.api.ITableViewerInput;
/**
 * 
 * @author anton
 */
class TreeStyledCellLabelProvider extends StyledCellLabelProvider {
	private ITableViewerInput table;
	
	
	public TreeStyledCellLabelProvider(ITableViewerInput table) {
		super();
		this.table = table;
	}


	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		TreeItem ti = (TreeItem) cell.getItem();
		StyledString text = new StyledString();
		if (element instanceof ILaunchConfigurationType) {
			ILaunchConfigurationType confType = (ILaunchConfigurationType) element;
			text.append(confType.getName());
			cell.setImage(DebugUITools.getImage(confType.getIdentifier()));
		} else if (element instanceof ILaunchConfiguration){
			ILaunchConfiguration conf = (ILaunchConfiguration) element;
			text.append(conf.getName());
			ti.setChecked(table.getViewerInput().containsKey(conf.getName()));
			try {
				cell.setImage(DebugUITools.getImage(conf.getType()
						.getIdentifier()));
			} catch (CoreException e) {
			}
		}
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		super.update(cell);

	}
}

