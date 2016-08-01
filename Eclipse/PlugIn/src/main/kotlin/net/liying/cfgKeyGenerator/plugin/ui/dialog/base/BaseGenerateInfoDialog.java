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
	protected Text txtPackageName;
	private Button btnBrowsePackage;
	protected Combo cmbTopClassName;
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

	protected void btnBrowsePackageSelected(final SelectionEvent e) {
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

		Label lblNewLabel = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.top = new FormAttachment(0, 13);
		fd_lblNewLabel.left = new FormAttachment(0, 11);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Configuration File:");

		lblCfgFilePath = new Label(container, SWT.NONE);
		FormData fd_lblCfgFilePath = new FormData();
		fd_lblCfgFilePath.right = new FormAttachment(0, 433);
		fd_lblCfgFilePath.top = new FormAttachment(0, 13);
		fd_lblCfgFilePath.left = new FormAttachment(0, 114);
		lblCfgFilePath.setLayoutData(fd_lblCfgFilePath);
		lblCfgFilePath.setText("FilePath");

		Label lblNewLabel_2 = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel_2 = new FormData();
		fd_lblNewLabel_2.top = new FormAttachment(0, 36);
		fd_lblNewLabel_2.left = new FormAttachment(0, 11);
		lblNewLabel_2.setLayoutData(fd_lblNewLabel_2);
		lblNewLabel_2.setText("Destination:");

		Label lblProject = new Label(container, SWT.NONE);
		FormData fd_lblProject = new FormData();
		fd_lblProject.top = new FormAttachment(0, 63);
		fd_lblProject.left = new FormAttachment(0, 21);
		lblProject.setLayoutData(fd_lblProject);
		lblProject.setText("Project:");

		cmbProject = new Combo(container, SWT.READ_ONLY);
		FormData fd_cmbProject = new FormData();
		fd_cmbProject.right = new FormAttachment(0, 433);
		fd_cmbProject.top = new FormAttachment(0, 59);
		fd_cmbProject.left = new FormAttachment(0, 118);
		cmbProject.setLayoutData(fd_cmbProject);
		cmbProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BaseGenerateInfoDialog.this.cmbProjectSelected(e);
			}
		});

		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel_1 = new FormData();
		fd_lblNewLabel_1.top = new FormAttachment(0, 94);
		fd_lblNewLabel_1.left = new FormAttachment(0, 21);
		lblNewLabel_1.setLayoutData(fd_lblNewLabel_1);
		lblNewLabel_1.setText("Source Folder:");

		cmbSourceFolder = new Combo(container, SWT.READ_ONLY);
		FormData fd_cmbSourceFolder = new FormData();
		fd_cmbSourceFolder.right = new FormAttachment(0, 433);
		fd_cmbSourceFolder.top = new FormAttachment(0, 90);
		fd_cmbSourceFolder.left = new FormAttachment(0, 118);
		cmbSourceFolder.setLayoutData(fd_cmbSourceFolder);

		Label lblNewLabel_3 = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel_3 = new FormData();
		fd_lblNewLabel_3.top = new FormAttachment(0, 131);
		fd_lblNewLabel_3.left = new FormAttachment(0, 21);
		lblNewLabel_3.setLayoutData(fd_lblNewLabel_3);
		lblNewLabel_3.setText("Package Name:");

		txtPackageName = new Text(container, SWT.BORDER);
		FormData fd_txtPackageName = new FormData();
		fd_txtPackageName.right = new FormAttachment(0, 365);
		fd_txtPackageName.top = new FormAttachment(0, 127);
		fd_txtPackageName.left = new FormAttachment(0, 118);
		txtPackageName.setLayoutData(fd_txtPackageName);

		btnBrowsePackage = new Button(container, SWT.NONE);
		btnBrowsePackage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BaseGenerateInfoDialog.this.btnBrowsePackageSelected(e);
			}
		});
		FormData fd_btnBrowsePackage = new FormData();
		fd_btnBrowsePackage.right = new FormAttachment(0, 433);
		fd_btnBrowsePackage.left = new FormAttachment(0, 370);
		fd_btnBrowsePackage.top = new FormAttachment(0, 125);
		btnBrowsePackage.setLayoutData(fd_btnBrowsePackage);
		btnBrowsePackage.setText("Browse...");

		Label lblNewLabel_4 = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel_4 = new FormData();
		fd_lblNewLabel_4.top = new FormAttachment(0, 167);
		fd_lblNewLabel_4.left = new FormAttachment(0, 21);
		lblNewLabel_4.setLayoutData(fd_lblNewLabel_4);
		lblNewLabel_4.setText("Top Class Name:");

		cmbTopClassName = new Combo(container, SWT.NONE);
		cmbTopClassName.setItems(new String[] {"CfgKey", "MsgKey"});
		FormData fd_cmbTopClassName = new FormData();
		fd_cmbTopClassName.right = new FormAttachment(0, 433);
		fd_cmbTopClassName.top = new FormAttachment(0, 164);
		fd_cmbTopClassName.left = new FormAttachment(0, 118);
		cmbTopClassName.setLayoutData(fd_cmbTopClassName);
		cmbTopClassName.setText("CfgKey");

		Label lblBaseClassName = new Label(container, SWT.NONE);
		FormData fd_lblBaseClassName = new FormData();
		fd_lblBaseClassName.top = new FormAttachment(0, 196);
		fd_lblBaseClassName.left = new FormAttachment(0, 21);
		lblBaseClassName.setLayoutData(fd_lblBaseClassName);
		lblBaseClassName.setText("Base Class Name:");

		txtBaseClassName = new Text(container, SWT.BORDER);
		FormData fd_txtBaseClassName = new FormData();
		fd_txtBaseClassName.right = new FormAttachment(0, 433);
		fd_txtBaseClassName.top = new FormAttachment(0, 193);
		fd_txtBaseClassName.left = new FormAttachment(0, 118);
		txtBaseClassName.setLayoutData(fd_txtBaseClassName);

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
		return new Point(450, 311);
	}
}
