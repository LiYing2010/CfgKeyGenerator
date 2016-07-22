package net.liying.cfgKeyGenerator.plugin.popup.actions

import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.ISelection
import org.eclipse.ui.IActionDelegate
import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.IWorkbenchPartSite

import org.eclipse.swt.widgets.Display
import org.eclipse.jface.dialogs.MessageDialog

class CfgKeyGenerateAction: IObjectActionDelegate {
	private var site: IWorkbenchPartSite? = null

	private var selection: ISelection? = null

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	override fun setActivePart(action: IAction, targetPart: IWorkbenchPart) {
		this.site = targetPart.site
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	override fun selectionChanged(action: IAction, selection: ISelection) {
		this.selection = selection
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	override fun run(action: IAction) {
		// TODO
		Display.getDefault().asyncExec {
			MessageDialog.openInformation(null, "Hello World", "Hello World!");
		}
	}
}
