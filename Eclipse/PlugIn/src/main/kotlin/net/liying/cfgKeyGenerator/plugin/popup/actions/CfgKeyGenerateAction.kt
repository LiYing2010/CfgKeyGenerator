package net.liying.cfgKeyGenerator.plugin.popup.actions

import net.liying.cfgKeyGenerator.generator.CfgKeyGenerator
import net.liying.cfgKeyGenerator.plugin.Activator
import net.liying.cfgKeyGenerator.plugin.ui.dialog.GenerateInfoDialog
import org.eclipse.core.resources.IFile
import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.MultiStatus
import org.eclipse.core.runtime.Status
import org.eclipse.jface.action.IAction
import org.eclipse.jface.dialogs.ErrorDialog
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.window.Window
import org.eclipse.swt.widgets.Shell
import org.eclipse.ui.IObjectActionDelegate
import org.eclipse.ui.IWorkbenchPart
import org.eclipse.ui.IWorkbenchPartSite
import java.io.PrintWriter
import java.io.StringWriter

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


		try {
			val dialog = GenerateInfoDialog(this.getShell())

			dialog.create()
			dialog.cfgFile = selectedFile
			dialog.loadInitData()

			if (dialog.open() == Window.OK) {
				CfgKeyGenerator.generate(dialog.resultParams)
			}
		} catch(e: Throwable) {
			this.showError(e)
		}
	}

	private fun getShell(): Shell? = this.site?.workbenchWindow?.shell

	private fun showError(error: Throwable) {
		val stringWriter = StringWriter()
		val printWriter = PrintWriter(stringWriter)
		error.printStackTrace(printWriter)
		printWriter.flush()

		val childStatusList = mutableListOf<Status>()
		stringWriter.toString().lineSequence().forEach { errorLine ->
			childStatusList.add(Status(IStatus.ERROR, Activator.PLUGIN_ID, errorLine))
		}

		val status = MultiStatus(Activator.PLUGIN_ID, IStatus.ERROR,
				childStatusList.toTypedArray(), error.message, error)
		ErrorDialog.openError(this.getShell(), "Error", error.message, status);
	}
}
