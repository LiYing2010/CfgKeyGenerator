package net.liying.cfgKeyGenerator.plugin.ui.dialog.base;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class BaseGenerateInfoDialog extends Dialog {
	protected Label lblCfgFilePath;
	protected Combo cmbProject;
	protected Combo cmbSourceFolder;
	protected Text txtPackageName;
	protected Text txtTopClassName;
	protected Text txtBaseClassName;

	/**
	 * Create the dialog.
	 *
	 * @param parentShell
	 */
	public BaseGenerateInfoDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.TITLE);
	}

	protected void cmbProjectSelected(final SelectionEvent e) {
		// do nothing here, implemented in sub-class
	}

	/**
	 * Create contents of the dialog.
	 *
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 2;
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setText("Configuration File:");

		lblCfgFilePath = new Label(container, SWT.NONE);
		lblCfgFilePath.setText("FilePath");

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		lblNewLabel_2.setText("Destination:");
		new Label(container, SWT.NONE);

		Label lblProject = new Label(container, SWT.NONE);
		lblProject.setText("Project:");

		cmbProject = new Combo(container, SWT.READ_ONLY);
		cmbProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BaseGenerateInfoDialog.this.cmbProjectSelected(e);
			}
		});
		cmbProject.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setText("Source Folder:");

		cmbSourceFolder = new Combo(container, SWT.READ_ONLY);
		cmbSourceFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_3 = new Label(container, SWT.NONE);
		lblNewLabel_3.setText("Package Name:");

		txtPackageName = new Text(container, SWT.BORDER);
		txtPackageName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblNewLabel_4 = new Label(container, SWT.NONE);
		lblNewLabel_4.setText("Top Class Name:");

		txtTopClassName = new Text(container, SWT.BORDER);
		txtTopClassName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label lblBaseClassName = new Label(container, SWT.NONE);
		lblBaseClassName.setText("Base Class Name:");

		txtBaseClassName = new Text(container, SWT.BORDER);
		txtBaseClassName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		return container;
	}

	/**
	 * Create contents of the button bar.
	 *
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 320);
	}
}
