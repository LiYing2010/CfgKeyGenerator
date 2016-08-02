package net.liying.cfgKeyGenerator.plugin

import net.liying.cfgKeyGenerator.generator.GeneratorParams
import org.eclipse.core.resources.IFile
import org.eclipse.core.runtime.Status
import org.eclipse.core.runtime.preferences.InstanceScope
import org.osgi.service.prefs.Preferences

object PreferencesManager {
	private object Key {
		const val PROJECT = "project"

		const val SOURCE_FOLDER = "sourceFolder"

		const val PACKAGE_NAME = "packageName"

		const val TOP_CLASS_NAME = "topClassName"

		const val BASE_CLASS_NAME = "baseClassName"
	}

	private fun getSubPreferences(pref: Preferences, fullPath: String): Preferences {
		var subPref: Preferences = pref

		fullPath.split("/").forEach { path ->
			if (path.isNotEmpty()) {
				subPref = subPref.node(path)
			}
		}

		return subPref
	}

	public fun saveToPreferences(cfgFile: IFile, params: GeneratorParams) {
		val fullPath = cfgFile.fullPath.makeAbsolute().toPortableString()
		val pref = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		val subPref = this.getSubPreferences(pref, fullPath)

		subPref.put(Key.PROJECT, params.projectName)
		subPref.put(Key.SOURCE_FOLDER, params.outputSrcDirName)
		subPref.put(Key.PACKAGE_NAME, params.packageName)
		subPref.put(Key.TOP_CLASS_NAME, params.topClassName)
		subPref.put(Key.BASE_CLASS_NAME, params.topClassBaseClassName)

		try {
			pref.flush()
		} catch(e: Exception) {
			val status = Status(Status.ERROR, Activator.PLUGIN_ID, Status.ERROR, e.message, e)
			Activator.getDefault()?.log?.log(status)
		}
	}

	public fun loadFromPreferences(cfgFile: IFile): GeneratorParams {
		val fullPath = cfgFile.fullPath.makeAbsolute().toPortableString()
		val pref = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		val subPref = this.getSubPreferences(pref, fullPath)

		val params = GeneratorParams()

		params.projectName = subPref.get(Key.PROJECT, "")
		params.outputSrcDirName = subPref.get(Key.SOURCE_FOLDER, "")
		params.packageName = subPref.get(Key.PACKAGE_NAME, "")
		params.topClassName = subPref.get(Key.TOP_CLASS_NAME, "CfgKey")
		params.topClassBaseClassName = subPref.get(Key.BASE_CLASS_NAME, "")

		return params
	}
}
