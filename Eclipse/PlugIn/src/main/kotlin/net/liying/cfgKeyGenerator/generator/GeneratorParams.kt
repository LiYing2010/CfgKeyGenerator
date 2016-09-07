package net.liying.cfgKeyGenerator.generator

import java.io.File

import org.eclipse.jdt.core.IPackageFragmentRoot

class GeneratorParams {
	enum class SourceType {
		Java, Kotlin
	}

	public var cfgFile: File? = null

	public var projectName = ""

	public var outputSrcDir: IPackageFragmentRoot? = null

	public var outputSrcDirName = ""

	public var sourceType = SourceType.Kotlin

	public var packageName = ""

	public var topClassName = ""

	public var topClassBaseClassName = ""
}
