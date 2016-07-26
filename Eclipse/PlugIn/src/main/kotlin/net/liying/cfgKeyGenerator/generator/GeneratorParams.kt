package net.liying.cfgKeyGenerator.generator

import java.io.File

import org.eclipse.core.resources.IResource

class GeneratorParams {
	public var cfgFile: File? = null

	public var outputSrcDir: IResource? = null

	public var packageName: String = ""

	public var topClassName: String = ""

	public var topClassBaseClassName: String = ""
}
