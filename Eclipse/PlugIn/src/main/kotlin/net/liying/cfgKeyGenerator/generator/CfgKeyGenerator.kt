package net.liying.cfgKeyGenerator.generator

import org.apache.commons.configuration2.AbstractConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.formatter.CodeFormatter

class CfgKeyGenerator {
	companion object {
		public fun generate(params: GeneratorParams) {
			val keyList = this.readCfgKeyList(params)

			val topClass = this.convertToCfgKeyClass(params, keyList)

			this.generateJavaSource(params, topClass)
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

						if (idx != subKeyList.size - 1) {
							cfgKeyClassInfo = cfgKeyClassInfo.getSubClass(subKey)
						} else {
							cfgKeyClassInfo.addSubKey(subKey)
						}
					}
				}
			}

			return topClassInfo
		}

		private fun generateJavaSource(params: GeneratorParams, cfgKeyClassInfo: CfgKeyClassInfo) {
			val javaSource = cfgKeyClassInfo.generateJavaSource()

			val outputPackage = params.outputSrcDir!!.createPackageFragment(params.packageName, true, null)
			val compilationUnit = outputPackage.createCompilationUnit(params.topClassName + ".java", javaSource, true, null)

			this.format(compilationUnit)
		}

		private fun format(unit: ICompilationUnit) {
			unit.becomeWorkingCopy(null)

			val formatter = ToolFactory.createCodeFormatter(null)
			val range = unit.sourceRange

			val formatEdit = formatter.format(CodeFormatter.K_COMPILATION_UNIT,
					unit.source, range.offset, range.length, 0, "\n")

			if (formatEdit.hasChildren()) {
				unit.applyTextEdit(formatEdit, null)
				unit.reconcile(AST.JLS8, false, null, null)
			}

			unit.commitWorkingCopy(true, null)
		}
	}
}
