package net.liying.cfgKeyGenerator.generator

import java.io.File

import org.eclipse.jdt.core.IPackageFragmentRoot

class GeneratorParams {
	public var cfgFile: File? = null

	public var projectName: String = ""

	public var outputSrcDir: IPackageFragmentRoot? = null

	public var outputSrcDirName: String = ""

	public var packageName: String = ""

	public var topClassName: String = ""

	public var topClassBaseClassName: String = ""
}
