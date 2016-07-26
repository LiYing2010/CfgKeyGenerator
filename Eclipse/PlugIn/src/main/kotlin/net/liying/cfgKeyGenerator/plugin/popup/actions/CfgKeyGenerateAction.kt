package net.liying.cfgKeyGenerator.plugin.popup.actions

import net.liying.cfgKeyGenerator.generator.CfgKeyGenerator
import net.liying.cfgKeyGenerator.plugin.ui.dialog.GenerateInfoDialog

import org.eclipse.core.resources.IFile

import org.eclipse.jface.action.IAction
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection

import org.eclipse.jface.window.Window

import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.IWorkbenchPartSite

class CfgKeyGenerateAction : IObjectActionDelegate {
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
		if (!(this.selection is IStructuredSelection)) {
			return
		}

		val selectedFile = (this.selection as IStructuredSelection).toArray()[0]
		if (!(selectedFile is IFile)) {
			return
		}

		var shell = this.site?.workbenchWindow?.shell
		val dialog = GenerateInfoDialog(shell)

		dialog.create()
		dialog.cfgFile = selectedFile
		dialog.loadInitData()

		if (dialog.open() == Window.OK) {
			CfgKeyGenerator.generate(dialog.resultParams)
		}
	}
}
