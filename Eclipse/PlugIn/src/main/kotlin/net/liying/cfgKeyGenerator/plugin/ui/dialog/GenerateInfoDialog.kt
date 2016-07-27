package net.liying.cfgKeyGenerator.plugin.ui.dialog

import net.liying.cfgKeyGenerator.generator.GeneratorParams
import net.liying.cfgKeyGenerator.plugin.ui.dialog.base.BaseGenerateInfoDialog
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Shell

class GenerateInfoDialog(parentShell: Shell?) : BaseGenerateInfoDialog(parentShell) {
	var resultParams: GeneratorParams = GeneratorParams()
	var cfgFile: IFile? = null

	public fun loadInitData() {
		this.lblCfgFilePath.text = this.cfgFile!!.toString()

		this.loadProjectList()

		this.txtTopClassName.text = "Cfgkey"

		this.txtPackageName.setFocus()
	}

	private fun loadProjectList() {
		this.cmbProject.removeAll()

		val workspaceRoot: IWorkspaceRoot = ResourcesPlugin.getWorkspace().root

		workspaceRoot.projects.forEach { project ->
			if (project.isOpen && project.hasNature(JavaCore.NATURE_ID)) {
				val javaProject: IJavaProject = JavaCore.create(project)

				this.cmbProject.add(project.name)
				this.cmbProject.setData(project.name, javaProject)

				if (this.isAncestor(this.cfgFile!!, project)) {
					this.cmbProject.select(this.cmbProject.itemCount - 1)
				}
			}
		}

		if (this.cmbProject.selectionIndex < 0 && this.cmbProject.itemCount > 0) {
			this.cmbProject.select(0)
		}

		this.loadSourceFolderList()
	}

	private fun isAncestor(descendant: IResource, ancestor: IContainer): Boolean
			= when (descendant.parent) {
		null -> false
		ancestor -> true
		else -> this.isAncestor(descendant.parent, ancestor)
	}

	private fun getSelectedProject(): IJavaProject? {
		val idx = this.cmbProject.selectionIndex
		if (idx < 0)
			return null

		val name = this.cmbProject.items[idx]
		return this.cmbProject.getData(name) as IJavaProject
	}

	override fun cmbProjectSelected(e: SelectionEvent) {
		this.loadSourceFolderList()
	}

	private fun loadSourceFolderList() {
		this.cmbSourceFolder.removeAll()

		val project: IJavaProject? = this.getSelectedProject()
		if (project == null) {
			return
		}

		val projectPath = project.path

		project.allPackageFragmentRoots.forEach { pkgFragRoot ->
			if (pkgFragRoot.parent == project &&
					pkgFragRoot.kind == IPackageFragmentRoot.K_SOURCE) {
				val srcFolderName = pkgFragRoot.path.makeRelativeTo(projectPath).toString()
				this.cmbSourceFolder.add(srcFolderName)
				this.cmbSourceFolder.setData(srcFolderName, pkgFragRoot)
			}
		}

		if (this.cmbSourceFolder.itemCount > 0) {
			this.cmbSourceFolder.select(0)
		}
	}

	private fun getSelectedSourceFolder(): IPackageFragmentRoot? {
		val idx = this.cmbSourceFolder.getSelectionIndex()
		if (idx < 0)
			return null

		val name = this.cmbSourceFolder.items[idx]
		return this.cmbSourceFolder.getData(name) as IPackageFragmentRoot
	}

	private fun checkInput(): Boolean {
		if (this.txtPackageName.text.trim().isEmpty()) {
			MessageDialog.openError(this.parentShell, "Error", "Please input package name.")
			this.txtPackageName.setFocus()
			return false
		}

		if (this.txtTopClassName.text.trim().isEmpty()) {
			MessageDialog.openError(this.parentShell, "Error", "Please input top class name.")
			this.txtTopClassName.setFocus()
			return false
		}

		return true
	}

	private fun createResultParams() {
		this.resultParams = GeneratorParams()

		this.resultParams.cfgFile = this.cfgFile!!.location.toFile().normalize()
		this.resultParams.outputSrcDir = this.getSelectedSourceFolder()
		this.resultParams.packageName = this.txtPackageName.text.trim()
		this.resultParams.topClassName = this.txtTopClassName.text.trim()
		this.resultParams.topClassBaseClassName = this.txtBaseClassName.text.trim()
	}

	override fun okPressed() {
		if (!checkInput()) {
			return
		}

		this.createResultParams()

		super.okPressed()
	}
}
