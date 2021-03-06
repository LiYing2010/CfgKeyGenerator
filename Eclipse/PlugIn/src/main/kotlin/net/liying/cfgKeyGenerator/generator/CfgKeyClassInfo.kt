package net.liying.cfgKeyGenerator.generator

import freemarker.template.Configuration
import net.liying.cfgKeyGenerator.generator.GeneratorParams.SourceType
import java.io.StringWriter

class CfgKeyClassInfo(val keyPrefix: String?, val className: String) {
	public var topClass: Boolean = false

	public var packageName: String = ""

	public var baseClassName: String? = null

	public val allFullKeyList = sortedSetOf<String>()

	public val allSubKeyList = sortedSetOf<String>()

	public val subKeyMap = mutableMapOf<String, String>().toSortedMap()

	public val subKeyList = sortedSetOf<String>()

	public val subClassMap = mutableMapOf<String, CfgKeyClassInfo>().toSortedMap()

	// =========================================================================

	public fun addSubClass(subClassName: String): CfgKeyClassInfo {
		var subClassKeyPrefix = subClassName
		if (this.keyPrefix != null && this.keyPrefix.isNotEmpty()) {
			subClassKeyPrefix = this.keyPrefix + "." + subClassKeyPrefix
		}

		val subClass = CfgKeyClassInfo(subClassKeyPrefix, subClassName)

		this.subKeyList.add(subClassName)
		this.subClassMap.put(subClassName, subClass)

		return subClass
	}

	public fun getSubClass(subClassName: String): CfgKeyClassInfo {
		if (this.subClassMap.contains(subClassName)) {
			return this.subClassMap.get(subClassName)!!
		} else {
			return this.addSubClass(subClassName)
		}
	}

	public fun addSubKey(subKey: String) {
		var fullKey = subKey
		if (this.keyPrefix != null && this.keyPrefix.isNotEmpty()) {
			fullKey = this.keyPrefix + "." + fullKey
		}

		this.subKeyMap.put(subKey, fullKey)
	}

	// =========================================================================

	private fun generateSource(sourceType: SourceType): String {
		val cfg = Configuration(Configuration.VERSION_2_3_25)

		cfg.setClassForTemplateLoading(CfgKeyClassInfo::class.java, "")

		val templateName = when (sourceType) {
			SourceType.Kotlin -> "CfgKeyClass_Kotlin.ftl"
			SourceType.Java -> "CfgKeyClass_Java.ftl"
		}
		val template = cfg.getTemplate("template/${templateName}", "UTF-8")

		val writer = StringWriter()

		val root = mutableMapOf<String, Any>()
		root.put("cfgKeyClass", this)

		template.process(root, writer)

		return writer.toString()
	}

	public fun generateKotlinSource() = this.generateSource(SourceType.Kotlin)

	public fun generateJavaSource() = this.generateSource(SourceType.Java)
}
