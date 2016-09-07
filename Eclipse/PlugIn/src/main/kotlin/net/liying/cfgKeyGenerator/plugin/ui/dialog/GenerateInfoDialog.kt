package net.liying.cfgKeyGenerator.plugin.ui.dialog

import net.liying.cfgKeyGenerator.generator.GeneratorParams
import net.liying.cfgKeyGenerator.generator.GeneratorParams.SourceType
import net.liying.cfgKeyGenerator.plugin.PreferencesManager
import net.liying.cfgKeyGenerator.plugin.ui.dialog.base.BaseGenerateInfoDialog
import org.eclipse.core.resources.IContainer
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IWorkspaceRoot
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.IType
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.ui.IJavaElementSearchConstants
import org.eclipse.jdt.ui.JavaUI
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.jface.window.Window
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.widgets.Shell

class GenerateInfoDialog(parentShell: Shell?) : BaseGenerateInfoDialog(parentShell) {
	var resultParams: GeneratorParams = GeneratorParams()
	var cfgFile: IFile? = null

	public fun loadInitData() {
		this.shell.text = "Class Generation Information"
		this.shell.layout()

		this.lblCfgFilePath.text = this.cfgFile!!.name

		this.loadProjectList()

		this.loadSourceType()

		this.txtPackageName.setFocus()

		this.loadFromConfiguration()
	}

	private fun loadSourceType() {
		this.cmbSourceType.removeAll()

		SourceType.values().forEach { sourceType ->
			this.cmbSourceType.add(sourceType.name)
			this.cmbSourceType.setData(sourceType.name, sourceType)
		}

		this.cmbSourceType.select(0)
	}

	private val selectedSourceType: SourceType
		get() = this.cmbSourceType.getData(this.cmbSourceType.text) as SourceType

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

	private fun isAncestor(descendant: IResource, ancestor: IContainer): Boolean =
			when (descendant.parent) {
				null -> false
				ancestor -> true
				else -> this.isAncestor(descendant.parent, ancestor)
			}

	private val selectedProject: IJavaProject?
		get() {
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

		val project: IJavaProject? = this.selectedProject
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

	private val selectedSourceFolder: IPackageFragmentRoot?
		get() {
			val idx = this.cmbSourceFolder.selectionIndex
			if (idx < 0)
				return null

			val name = this.cmbSourceFolder.items[idx]
			return this.cmbSourceFolder.getData(name) as IPackageFragmentRoot
		}

	override fun btnBrowsePackageSelected(e: SelectionEvent) {
		val project: IJavaProject? = this.selectedProject
		if (project == null) {
			return
		}

		val pkgSelectionDialog = JavaUI.createPackageDialog(this.shell, project, 0)
		pkgSelectionDialog.setTitle("Package Selection")
		pkgSelectionDialog.setMessage("Select a package.")
		if (pkgSelectionDialog.open() == Window.OK) {
			val pkg = pkgSelectionDialog.result[0] as IPackageFragment
			this.txtPackageName.text = pkg.elementName
		}

		this.txtPackageName.setFocus()
	}

	override fun btnBrowseBaseClassSelected(e: SelectionEvent) {
		val project: IJavaProject? = this.selectedProject
		if (project == null) {
			return
		}

		val style = IJavaElementSearchConstants.CONSIDER_CLASSES
		val clsSelectionDialog = JavaUI.createTypeDialog(this.shell, null,
				project.project, style, false)
		clsSelectionDialog.setTitle("Base Class Selection")
		clsSelectionDialog.setMessage("Select a base class.")

		if (clsSelectionDialog.open() == Window.OK) {
			val baseClass = clsSelectionDialog.result[0] as IType
			this.txtBaseClassName.text = baseClass.getFullyQualifiedName('.')
		}

		this.txtBaseClassName.setFocus()
	}

	private fun checkInput(): Boolean {
		if (this.txtPackageName.text.trim().isEmpty()) {
			MessageDialog.openError(this.parentShell, "Error", "Please input package name.")
			this.txtPackageName.setFocus()
			return false
		}

		if (this.cmbTopClassName.text.trim().isEmpty()) {
			MessageDialog.openError(this.parentShell, "Error", "Please input top class name.")
			this.cmbTopClassName.setFocus()
			return false
		}

		return true
	}

	private fun createResultParams() {
		this.resultParams = GeneratorParams()

		this.resultParams.cfgFile = this.cfgFile!!.location.toFile().normalize()
		this.resultParams.projectName = this.cmbProject.text
		this.resultParams.outputSrcDir = this.selectedSourceFolder
		this.resultParams.outputSrcDirName = this.cmbSourceFolder.text
		this.resultParams.sourceType = this.selectedSourceType
		this.resultParams.packageName = this.txtPackageName.text.trim()
		this.resultParams.topClassName = this.cmbTopClassName.text.trim().capitalize()
		this.resultParams.topClassBaseClassName = this.txtBaseClassName.text.trim()
	}

	private fun loadFromConfiguration() {
		val params = PreferencesManager.loadFromPreferences(this.cfgFile!!)

		val projectName = params.projectName
		var idx = this.cmbProject.items.indexOf(projectName)
		if (idx != -1) {
			this.cmbProject.select(idx)
			this.loadSourceFolderList()
		}

		val sourceFolderName = params.outputSrcDirName
		idx = this.cmbSourceFolder.indexOf(sourceFolderName)
		if (idx != -1) {
			this.cmbSourceFolder.select(idx)
		}

		idx = this.cmbSourceType.indexOf(params.sourceType.name)
		this.cmbSourceType.select(idx)

		this.txtPackageName.text = params.packageName
		this.cmbTopClassName.text = params.topClassName
		this.txtBaseClassName.text = params.topClassBaseClassName
	}

	override fun okPressed() {
		if (!checkInput()) {
			return
		}

		this.createResultParams()

		PreferencesManager.saveToPreferences(this.cfgFile!!, this.resultParams)

		super.okPressed()
	}
}
