package net.liying.cfgKeyGenerator.generator

import org.apache.commons.configuration2.AbstractConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations

import org.eclipse.core.resources.IResource

class CfgKeyGenerator {
	companion object {
		public fun generate(params: GeneratorParams) {
			val keyList = this.readCfgKeyList(params)

			val topClass = this.convertToCfgKeyClass(params, keyList)

			this.outputJavaSource(params, topClass)
		}

		private fun readCfgKeyList(params: GeneratorParams): List<String> {
			val configs = Configurations()
			val cfg: AbstractConfiguration =
					if (params.cfgFile!!.extension.equals("xml", true)) {
						configs.xml(params.cfgFile)
					} else {
						configs.properties(params.cfgFile)
					}

			val keyList = mutableListOf<String>()

			cfg.keys.forEach { key ->
				keyList.add(trimNumericsuffix(key))
			}

			return keyList
		}

		private fun trimNumericsuffix(key: String): String {
			var trimedKey = key

			while (trimedKey.contains(".")) {
				val suffix = trimedKey.substringAfterLast(".")

				if (isNumeric(suffix)) {
					trimedKey = trimedKey.substringBeforeLast(".")
				} else {
					break
				}
			}

			return trimedKey
		}

		private fun isNumeric(s: String): Boolean =
				try {
					s.toDouble()
					true
				} catch (e: Exception) {
					false
				}


		private fun convertToCfgKeyClass(params: GeneratorParams, fullKeyList: List<String>): CfgKeyClassInfo {
			val topClassInfo = CfgKeyClassInfo(null, params.topClassName)
			topClassInfo.topClass = true
			topClassInfo.baseClassName = params.topClassBaseClassName
			topClassInfo.packageName = params.packageName
			topClassInfo.allFullKeyList.addAll(fullKeyList)

			fullKeyList.forEach { fullKey ->
				if (fullKey.isNotEmpty()) {
					var cfgKeyClassInfo = topClassInfo

					val subKeyList = fullKey.split(".")
					subKeyList.forEachIndexed { idx, subKey ->
						topClassInfo.allSubKeyList.add(subKey)

						if (idx != fullKey.count() - 1) {
							cfgKeyClassInfo = cfgKeyClassInfo.getSubClass(subKey)
						} else {
							cfgKeyClassInfo.addSubKey(subKey)
						}
					}
				}
			}

			return topClassInfo
		}

		private fun outputJavaSource(params: GeneratorParams, cfgKeyClassInfo: CfgKeyClassInfo) {
			// TODO: use Eclipse workspace API to create Java source file

			val srcDir = params.outputSrcDir!!.location.toFile().normalize()
			val packageDir = srcDir.resolve(params.packageName.replace(".", "/"))
			val sourceFile = packageDir.resolve(params.topClassName + ".java")

			val javaSource = cfgKeyClassInfo.generateJavaSource()

			sourceFile.parentFile.mkdirs()
			sourceFile.writeText(javaSource, Charsets.UTF_8)

			// refresh the source file output folder
			params.outputSrcDir!!.refreshLocal(IResource.DEPTH_INFINITE, null)
		}
	}
}
