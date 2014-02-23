package com.github.akalash.compositeLaunchConf.ui.tab;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.JavaDebugImages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;

import com.github.akalash.compositeLaunchConf.api.ICompositeLaunchConfigurationConstants;
import com.github.akalash.compositeLaunchConf.api.ILaunchManagerProvider;
import com.github.akalash.compositeLaunchConf.api.ITableViewerInput;

public class CompositeTab extends AbstractLaunchConfigurationTab implements
		ILaunchManagerProvider, ITableViewerInput {

	private TreeViewer fTreeViewer;
	private TableViewer fTableViewer;
	private Button fUpButton;
	private Button fDownButton;
	private Button fParametersRemoveButton;

	private class CompositeTabListener extends SelectionAdapter implements
			ModifyListener {

		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}

		/*
		 * All SelectionEvent on tab
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			Object source = e.getSource();
			if (source == fTableViewer.getTable() || source == fTableViewer) {
				setParametersButtonsEnableState();
			} else if (source == fTreeViewer.getTree() || source == fTreeViewer) {
				try {
					handleParametersTreeState(e);
				} catch (CoreException e1) {
				}
			} else if (source == fUpButton) {
				handleParametersUpSelected();
			} else if (source == fDownButton) {
				handleParametersDownSelected();
			} else if (source == fParametersRemoveButton) {
				handleParametersRemoveButtonSelected();
			}
		}

	}

	private CompositeTabListener fListener = new CompositeTabListener();

	@Override
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, 1, 1,
				GridData.FILL_HORIZONTAL | GridData.BEGINNING);
		setControl(comp);

		Composite paramcomp = SWTFactory.createComposite(comp, comp.getFont(),
				3, 1, GridData.FILL_HORIZONTAL | GridData.GRAB_VERTICAL, 0, 0);

		
		  SWTFactory .createLabel( paramcomp,
		  LauncherMessages.CompositeTab_tree_launch_config, 1);
		  SWTFactory .createLabel( paramcomp,
				  LauncherMessages.CompositeTab_select_table_launch_config, 2);

		// Composite Tree
		Tree ptree = new Tree(paramcomp, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL
				| SWT.H_SCROLL);
		fTreeViewer = new TreeViewer(ptree);
		GridData gd = new GridData(GridData.FILL | GridData.GRAB_VERTICAL);
		ptree.setLayoutData(gd);
		fTreeViewer.setContentProvider(new TreeContentProvider(this));
		fTreeViewer.setLabelProvider(new TreeStyledCellLabelProvider(this));
		ptree.addSelectionListener(fListener);
		// -------------------------------------------------------
		// Composite Table
		Table ptable = new Table(paramcomp, SWT.FULL_SELECTION | SWT.BORDER);
		fTableViewer = new TableViewer(ptable);
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
		ptable.setLayoutData(gd);
		TableColumn column1 = new TableColumn(ptable, SWT.NONE);
		column1.setText(LauncherMessages.CompositeTab_launch_config); 
		TableColumn column2 = new TableColumn(ptable, SWT.NONE);
		column2.setText(LauncherMessages.CompositeTab_launch_config_type); 
		TableLayout tableLayout = new TableLayout();
		ptable.setLayout(tableLayout);
		tableLayout.addColumnData(new ColumnWeightData(100));
		tableLayout.addColumnData(new ColumnWeightData(100));
		ptable.setHeaderVisible(true);
		ptable.setLinesVisible(true);
		ptable.addSelectionListener(fListener);

		fTableViewer.setContentProvider(new TableContentProvider());
		fTableViewer.setLabelProvider(new TableLabelProvider(this));
		// -------------------------------------------------------
		// Composite Buttons
		Composite envcomp = SWTFactory.createComposite(paramcomp,
				paramcomp.getFont(), 1, 1, GridData.VERTICAL_ALIGN_BEGINNING
						| GridData.HORIZONTAL_ALIGN_FILL, 0, 0);

		fUpButton = createPushButton(
				envcomp,
				LauncherMessages.CompositeTab_button_up,
				null);
		fUpButton.addSelectionListener(fListener);

		fDownButton = createPushButton(
				envcomp,
				LauncherMessages.CompositeTab_button_down, 
				null);
		fDownButton.addSelectionListener(fListener);

		fParametersRemoveButton = createPushButton(
				envcomp,
				LauncherMessages.CompositeTab_button_delete, 
				null);
		fParametersRemoveButton.addSelectionListener(fListener);

		setParametersButtonsEnableState();
		Dialog.applyDialogFont(parent);

	}

	private void handleParametersUpSelected() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();
		String key = (String) selection.getFirstElement();
		Map<String, ILaunchConfiguration> params = getViewerInput();
		Map<String, ILaunchConfiguration> head = new LinkedHashMap<>();
		String prev = null;
		for (Entry<String, ILaunchConfiguration> e : params.entrySet()) {
			if (key.equals(e.getKey()) && prev != null) {
				head.remove(prev);
				head.put(e.getKey(), e.getValue());
				head.put(prev, params.get(prev));
			} else {
				head.put(e.getKey(), e.getValue());
			}
			prev = e.getKey();
		}

		params.clear();
		params.putAll(head);
		fTableViewer.refresh();
		updateLaunchConfigurationDialog();
	}

	private void handleParametersDownSelected() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();
		String key = (String) selection.getFirstElement();
		Map<String, ILaunchConfiguration> params = getViewerInput();
		boolean find = false;
		Map<String, ILaunchConfiguration> tail = new LinkedHashMap<>();
		Map<String, ILaunchConfiguration> head = new LinkedHashMap<>();
		for (Entry<String, ILaunchConfiguration> e : params.entrySet()) {
			if (key.equals(e.getKey()) || find) {
				tail.put(e.getKey(), e.getValue());
			} else if (!tail.isEmpty()) {
				find = true;
				head.put(e.getKey(), e.getValue());
			} else {
				head.put(e.getKey(), e.getValue());
			}
		}
		params.clear();
		params.putAll(head);
		params.putAll(tail);
		fTableViewer.refresh();
		updateLaunchConfigurationDialog();
	}

	private void handleParametersRemoveButtonSelected() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();
		Object[] keys = selection.toArray();
		for (Object key : keys) {
			getViewerInput().remove((String)key);
		}
		fTableViewer.refresh();
		fTreeViewer.refresh();
		setParametersButtonsEnableState();
		updateLaunchConfigurationDialog();
	}

	private void handleParametersTreeState(SelectionEvent event)
			throws CoreException {
		if (event.detail == SWT.CHECK) {
			if (event.item.getData() instanceof ILaunchConfiguration) {
				boolean check = ((Tree) event.widget).getSelection()[0]
						.getChecked();
				ILaunchConfiguration conf = (ILaunchConfiguration) event.item
						.getData();
				if (getViewerInput().containsKey(conf.getName()) && !check) {
					getViewerInput().remove(conf.getName());
				} else if (check) {
					getViewerInput().put(conf.getName(), conf);
				}
				fTableViewer.refresh();
				updateLaunchConfigurationDialog();
			}
		}
	}

	/**
	 * Set the enabled state of the three environment variable-related buttons
	 * based on the selection in the Table widget.
	 */
	private void setParametersButtonsEnableState() {
		IStructuredSelection selection = (IStructuredSelection) fTableViewer
				.getSelection();
		boolean tableIsEmpty = selection.size() < 1;
		fUpButton.setEnabled(!tableIsEmpty);
		fDownButton.setEnabled(!tableIsEmpty);
		fParametersRemoveButton.setEnabled(!tableIsEmpty);
	}

	@SuppressWarnings("unchecked")
	public Map<String, ILaunchConfiguration> getViewerInput() {
		return (Map<String, ILaunchConfiguration>) fTableViewer.getInput();
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {

		List<String> l = new ArrayList<>();
		for (String s : getViewerInput().keySet()) {
			try {
				l.add(getViewerInput().get(s).getMemento());
			} catch (CoreException e) {
			}
		}
		configuration.setAttribute(
				ICompositeLaunchConfigurationConstants.ATTR_LIST_LAUNCHES, l);
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	/**
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(ILaunchConfiguration)
	 */
	@Override
	public void initializeFrom(ILaunchConfiguration config) {

		Map<String, ILaunchConfiguration> input = new LinkedHashMap<String, ILaunchConfiguration>();

		try {
			List<String> params = config.getAttribute(
					ICompositeLaunchConfigurationConstants.ATTR_LIST_LAUNCHES,
					(List<String>) null);

			if (params != null) {
				ILaunchConfiguration conf;
				for (String m : params) {
					conf = getLaunchManager().getLaunchConfiguration(m);
					if (conf != null && conf.exists()) {
						input.put(conf.getName(), conf);
					}
				}

			}
		} catch (CoreException e) {
		}

		fTreeViewer.setInput(getLaunchManager().getLaunchConfigurationTypes());
		fTableViewer.setInput(input);
	}

	@Focus
	public void setFocus() {
		fTreeViewer.getControl().setFocus();
	}

	@Override
	public String getName() {
		return LauncherMessages.CompositeTab_name_tab;
	}

	@Override
	public String getId() {
		return "com.github.akalash.compositeLaunchConf.ui.compositeTap"; //$NON-NLS-1$
	}

	@Override
	public Image getImage() {
		return JavaDebugImages.get(JavaDebugImages.IMG_VIEW_ARGUMENTS_TAB);
	}

	@Override
	public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
		// do nothing when activated
	}

	@Override
	public void deactivated(ILaunchConfigurationWorkingCopy workingCopy) {
		// do nothing when de-activated
	}

	@Override
	public ILaunchConfiguration[] getLaunchConfigurations(
			ILaunchConfigurationType type) {
		try {
			return getLaunchManager().getLaunchConfigurations(type);
		} catch (CoreException e) {
		}
		return null;
	}
}
