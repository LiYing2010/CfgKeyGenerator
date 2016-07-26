package net.liying.cfgKeyGenerator.generator

import java.io.StringWriter

import freemarker.template.Configuration

class CfgKeyClassInfo(val keyPrefix: String?, val className: String) {
	public var topClass: Boolean = false

	public var packageName: String = ""

	public var baseClassName: String? = null

	public val allFullKeyList = sortedSetOf<String>()

	public val allSubKeyList = sortedSetOf<String>()

	public val subKeyMap = mutableMapOf<String, String>()

	public val subKeyList = mutableListOf<String>()

	public val subClassMap = mutableMapOf<String, CfgKeyClassInfo>()

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


	public fun generateJavaSource(): String {
		val cfg = Configuration(Configuration.VERSION_2_3_25)

		cfg.setClassForTemplateLoading(CfgKeyClassInfo::class.java, "")

		val template = cfg.getTemplate("template/CfgKeyClass.ftl", "UTF-8")

		val writer = StringWriter()

		val root = mutableMapOf<String, Any>()
		root.put("cfgKeyClass", this)

		template.process(root, writer)

		return writer.toString()
	}
}
