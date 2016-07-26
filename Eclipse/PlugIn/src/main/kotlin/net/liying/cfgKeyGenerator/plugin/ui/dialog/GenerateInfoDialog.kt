package net.liying.cfgKeyGenerator.plugin.ui.dialog

import net.liying.cfgKeyGenerator.generator.GeneratorParams
import net.liying.cfgKeyGenerator.plugin.ui.dialog.base.BaseGenerateInfoDialog

import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.IPath

import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.JavaCore

import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Shell

class GenerateInfoDialog(parentShell: Shell?) : BaseGenerateInfoDialog(parentShell) {
	var resultParams: GeneratorParams = GeneratorParams()
	var cfgFile: IFile? = null

	public fun loadInitData() {
		this.lblCfgFilePath.text = this.cfgFile!!.toString()

		this.loadProjectList()
	}

	private fun loadProjectList() {
		this.cmbProject.removeAll()

		val workspaceRoot: IWorkspaceRoot = ResourcesPlugin.getWorkspace().root

		workspaceRoot.projects.forEach { project ->
			if (project.isOpen && project.hasNature(JavaCore.NATURE_ID)) {
				val javaProject: IJavaProject = JavaCore.create(project)

				this.cmbProject.add(project.name)
				this.cmbProject.setData(project.name, javaProject)
			}
		}

		if (this.cmbProject.itemCount > 0) {
			this.cmbProject.select(0)
			this.loadSourceFolderList()
		}
	}

	private fun getSelectedProject(): IJavaProject? {
		val idx = this.cmbProject.getSelectionIndex()
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
				val srcFolderResource = pkgFragRoot.resource
				val srcFolderName = pkgFragRoot.path.makeRelativeTo(projectPath).toString()
				this.cmbSourceFolder.add(srcFolderName)
				this.cmbSourceFolder.setData(srcFolderName, srcFolderResource)
			}
		}

		if (this.cmbSourceFolder.itemCount > 0) {
			this.cmbSourceFolder.select(0)
		}
	}

	private fun getSelectedSourceFolder(): IResource? {
		val idx = this.cmbSourceFolder.getSelectionIndex()
		if (idx < 0)
			return null

		val name = this.cmbSourceFolder.items[idx]
		return this.cmbSourceFolder.getData(name) as IResource
	}

	private fun checkInput(): Boolean {
		// TODO
		return true
	}

	private fun createResultParams() {
		this.resultParams = GeneratorParams()

		this.resultParams.cfgFile = this.cfgFile!!.location.toFile().normalize()
		this.resultParams.outputSrcDir = this.getSelectedSourceFolder()!!.location.toFile().normalize()
		this.resultParams.packageName = this.txtPackageName.text
		this.resultParams.topClassName = this.txtTopClassName.text
		this.resultParams.topClassBaseClassName = this.txtBaseClassName.text
	}

	override fun okPressed() {
		if (!checkInput()) {
			return
		}

		this.createResultParams()

		super.okPressed()
	}
}
