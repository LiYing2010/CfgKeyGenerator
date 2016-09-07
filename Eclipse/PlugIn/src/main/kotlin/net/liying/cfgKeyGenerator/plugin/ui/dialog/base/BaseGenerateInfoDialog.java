package net.liying.cfgKeyGenerator.plugin.ui.dialog.base;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BaseGenerateInfoDialog extends Dialog {
	protected Label lblCfgFilePath;
	protected Combo cmbProject;
	protected Combo cmbSourceFolder;
	protected Combo cmbSourceType;
	protected Text txtPackageName;
	protected Button btnBrowsePackage;
	protected Combo cmbTopClassName;
	protected Text txtBaseClassName;
	protected Button btnBrowseBaseClass;

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

	protected void btnBrowsePackageSelected(final SelectionEvent e) {
		// do nothing here, implemented in sub-class
	}

	protected void btnBrowseBaseClassSelected(final SelectionEvent e) {
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
		container.setLayout(new FormLayout());

		Label lblCfgFilePathTitle = new Label(container, SWT.NONE);
		FormData fd_lblCfgFilePathTitle = new FormData();
		fd_lblCfgFilePathTitle.top = new FormAttachment(0, 13);
		fd_lblCfgFilePathTitle.left = new FormAttachment(0, 11);
		lblCfgFilePathTitle.setLayoutData(fd_lblCfgFilePathTitle);
		lblCfgFilePathTitle.setText("Configuration File:");

		lblCfgFilePath = new Label(container, SWT.NONE);
		FormData fd_lblCfgFilePath = new FormData();
		fd_lblCfgFilePath.left = new FormAttachment(lblCfgFilePathTitle, 130, SWT.LEFT);
		fd_lblCfgFilePath.right = new FormAttachment(lblCfgFilePathTitle, 450, SWT.LEFT);
		fd_lblCfgFilePath.top = new FormAttachment(lblCfgFilePathTitle, 0, SWT.CENTER);
		lblCfgFilePath.setLayoutData(fd_lblCfgFilePath);
		lblCfgFilePath.setText("FilePath");

		Label lblDestination = new Label(container, SWT.NONE);
		FormData fd_lblDestination = new FormData();
		fd_lblDestination.top = new FormAttachment(lblCfgFilePathTitle, 24, SWT.TOP);
		fd_lblDestination.left = new FormAttachment(lblCfgFilePathTitle, 0, SWT.LEFT);
		lblDestination.setLayoutData(fd_lblDestination);
		lblDestination.setText("Destination:");

		Label lblProject = new Label(container, SWT.NONE);
		FormData fd_lblProject = new FormData();
		fd_lblProject.top = new FormAttachment(lblDestination, 24, SWT.TOP);
		fd_lblProject.left = new FormAttachment(lblDestination, 16, SWT.LEFT);
		lblProject.setLayoutData(fd_lblProject);
		lblProject.setText("Project:");

		cmbProject = new Combo(container, SWT.READ_ONLY);
		FormData fd_cmbProject = new FormData();
		fd_cmbProject.left = new FormAttachment(lblCfgFilePath, 0, SWT.LEFT);
		fd_cmbProject.right = new FormAttachment(lblCfgFilePath, 0, SWT.RIGHT);
		fd_cmbProject.top = new FormAttachment(lblProject, 0, SWT.CENTER);
		cmbProject.setLayoutData(fd_cmbProject);
		cmbProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BaseGenerateInfoDialog.this.cmbProjectSelected(e);
			}
		});

		Label lblSourceFolder = new Label(container, SWT.NONE);
		FormData fd_lblSourceFolder = new FormData();
		fd_lblSourceFolder.top = new FormAttachment(lblProject, 32, SWT.TOP);
		fd_lblSourceFolder.left = new FormAttachment(lblProject, 0, SWT.LEFT);
		lblSourceFolder.setLayoutData(fd_lblSourceFolder);
		lblSourceFolder.setText("Source Folder:");

		cmbSourceFolder = new Combo(container, SWT.READ_ONLY);
		FormData fd_cmbSourceFolder = new FormData();
		fd_cmbSourceFolder.left = new FormAttachment(lblCfgFilePath, 0, SWT.LEFT);
		fd_cmbSourceFolder.right = new FormAttachment(lblCfgFilePath, 0, SWT.RIGHT);
		fd_cmbSourceFolder.top = new FormAttachment(lblSourceFolder, 0, SWT.CENTER);
		cmbSourceFolder.setLayoutData(fd_cmbSourceFolder);

		Label lblSourceType = new Label(container, SWT.NONE);
		lblSourceType.setText("Source Type:");
		FormData fd_lblSourceType = new FormData();
		fd_lblSourceType.left = new FormAttachment(lblProject, 0, SWT.LEFT);
		fd_lblSourceType.top = new FormAttachment(lblSourceFolder, 32, SWT.TOP);
		lblSourceType.setLayoutData(fd_lblSourceType);

		cmbSourceType = new Combo(container, SWT.READ_ONLY);
		FormData fd_cmbSourceType = new FormData();
		fd_cmbSourceType.right = new FormAttachment(lblCfgFilePath, 0, SWT.RIGHT);
		fd_cmbSourceType.top = new FormAttachment(lblSourceType, 0, SWT.CENTER);
		fd_cmbSourceType.left = new FormAttachment(lblCfgFilePath, 0, SWT.LEFT);
		cmbSourceType.setLayoutData(fd_cmbSourceType);

		Label lblPackageName = new Label(container, SWT.NONE);
		FormData fd_lblPackageName = new FormData();
		fd_lblPackageName.left = new FormAttachment(lblProject, 0, SWT.LEFT);
		fd_lblPackageName.top = new FormAttachment(lblSourceType, 32, SWT.TOP);
		lblPackageName.setLayoutData(fd_lblPackageName);
		lblPackageName.setText("Package Name:");

		txtPackageName = new Text(container, SWT.BORDER);
		FormData fd_txtPackageName = new FormData();
		fd_txtPackageName.right = new FormAttachment(lblCfgFilePath, -76, SWT.RIGHT);
		fd_txtPackageName.top = new FormAttachment(lblPackageName, 0, SWT.CENTER);
		fd_txtPackageName.left = new FormAttachment(lblCfgFilePath, 0, SWT.LEFT);
		txtPackageName.setLayoutData(fd_txtPackageName);

		btnBrowsePackage = new Button(container, SWT.NONE);
		btnBrowsePackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BaseGenerateInfoDialog.this.btnBrowsePackageSelected(e);
			}
		});
		FormData fd_btnBrowsePackage = new FormData();
		fd_btnBrowsePackage.right = new FormAttachment(lblCfgFilePath, 0, SWT.RIGHT);
		fd_btnBrowsePackage.left = new FormAttachment(lblCfgFilePath, -72, SWT.RIGHT);
		fd_btnBrowsePackage.top = new FormAttachment(lblPackageName, 0, SWT.CENTER);
		btnBrowsePackage.setLayoutData(fd_btnBrowsePackage);
		btnBrowsePackage.setText("Browse...");

		Label lblTopClassName = new Label(container, SWT.NONE);
		FormData fd_lblTopClassName = new FormData();
		fd_lblTopClassName.left = new FormAttachment(lblProject, 0, SWT.LEFT);
		fd_lblTopClassName.top = new FormAttachment(lblPackageName, 32, SWT.TOP);
		lblTopClassName.setLayoutData(fd_lblTopClassName);
		lblTopClassName.setText("Top Class Name:");

		cmbTopClassName = new Combo(container, SWT.NONE);
		cmbTopClassName.setItems(new String[] { "CfgKey", "MsgKey" });
		FormData fd_cmbTopClassName = new FormData();
		fd_cmbTopClassName.right = new FormAttachment(lblCfgFilePath, 0, SWT.RIGHT);
		fd_cmbTopClassName.top = new FormAttachment(lblTopClassName, 0, SWT.CENTER);
		fd_cmbTopClassName.left = new FormAttachment(lblCfgFilePath, 0, SWT.LEFT);
		cmbTopClassName.setLayoutData(fd_cmbTopClassName);
		cmbTopClassName.setText("CfgKey");

		Label lblBaseClassName = new Label(container, SWT.NONE);
		FormData fd_lblBaseClassName = new FormData();
		fd_lblBaseClassName.left = new FormAttachment(lblProject, 0, SWT.LEFT);
		fd_lblBaseClassName.top = new FormAttachment(lblTopClassName, 32, SWT.TOP);
		lblBaseClassName.setLayoutData(fd_lblBaseClassName);
		lblBaseClassName.setText("Base Class Name:");

		txtBaseClassName = new Text(container, SWT.BORDER);
		FormData fd_txtBaseClassName = new FormData();
		fd_txtBaseClassName.right = new FormAttachment(lblCfgFilePath, -76, SWT.RIGHT);
		fd_txtBaseClassName.top = new FormAttachment(lblBaseClassName, 0, SWT.CENTER);
		fd_txtBaseClassName.left = new FormAttachment(lblCfgFilePath, 0, SWT.LEFT);
		txtBaseClassName.setLayoutData(fd_txtBaseClassName);

		btnBrowseBaseClass = new Button(container, SWT.NONE);
		btnBrowseBaseClass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BaseGenerateInfoDialog.this.btnBrowseBaseClassSelected(e);
			}
		});
		btnBrowseBaseClass.setText("Browse...");
		FormData fd_btnBrowseBaseClass = new FormData();
		fd_btnBrowseBaseClass.right = new FormAttachment(lblCfgFilePath, 0, SWT.RIGHT);
		fd_btnBrowseBaseClass.left = new FormAttachment(lblCfgFilePath, -72, SWT.RIGHT);
		fd_btnBrowseBaseClass.top = new FormAttachment(lblBaseClassName, 0, SWT.CENTER);
		btnBrowseBaseClass.setLayoutData(fd_btnBrowseBaseClass);

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
		return new Point(490, 340);
	}
}
