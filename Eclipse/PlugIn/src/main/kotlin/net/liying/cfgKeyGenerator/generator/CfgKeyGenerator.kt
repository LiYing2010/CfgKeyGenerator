package net.liying.cfgKeyGenerator.generator

import net.liying.cfgKeyGenerator.generator.GeneratorParams.SourceType
import org.apache.commons.configuration2.AbstractConfiguration
import org.apache.commons.configuration2.builder.fluent.Configurations
import org.eclipse.core.resources.IFolder
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.ToolFactory
import org.eclipse.jdt.core.dom.AST
import org.eclipse.jdt.core.formatter.CodeFormatter

class CfgKeyGenerator {
	companion object {
		public fun generate(params: GeneratorParams) {
			val keyList = this.readCfgKeyList(params)

			val topClass = this.convertToCfgKeyClass(params, keyList)

			this.generateSource(params, topClass)
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

		private fun generateSource(params: GeneratorParams, cfgKeyClassInfo: CfgKeyClassInfo) {
			val sourceContent =
					when (params.sourceType) {
						SourceType.Kotlin -> cfgKeyClassInfo.generateKotlinSource()
						SourceType.Java -> cfgKeyClassInfo.generateJavaSource()
					}

			val fileExt =
					when (params.sourceType) {
						SourceType.Kotlin -> "kt"
						SourceType.Java -> "java"
					}
			val fileName = "${params.topClassName}.${fileExt}"

			val outputPackage = params.outputSrcDir!!.createPackageFragment(params.packageName, true, null)

			when (params.sourceType) {
				SourceType.Kotlin -> this.generateKotlinSource(outputPackage, fileName, sourceContent)
				SourceType.Java -> this.generateJavaSource(outputPackage, fileName, sourceContent)
			}
		}

		private fun generateKotlinSource(outputPackage: IPackageFragment, fileName: String, sourceContent: String) {
			val folder = outputPackage.correspondingResource as IFolder

			val sourceFile = folder.getFile(fileName)
			val inputStream = sourceContent.toByteArray().inputStream()
			sourceFile.create(inputStream, true, null)
		}

		private fun generateJavaSource(outputPackage: IPackageFragment, fileName: String, sourceContent: String) {
			val compilationUnit = outputPackage.createCompilationUnit(fileName, sourceContent, true, null)

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
