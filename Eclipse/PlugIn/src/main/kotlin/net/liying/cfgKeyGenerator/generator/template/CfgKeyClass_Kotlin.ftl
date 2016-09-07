<#if cfgKeyClass.topClass>
<#include "fileComment.ftl">

package ${cfgKeyClass.packageName}

</#if>

class ${cfgKeyClass.className?cap_first} <#if cfgKeyClass.baseClassName?has_content> : ${cfgKeyClass.baseClassName} </#if> {
<#if cfgKeyClass.topClass>
	object AllKeys {
		val _Array_ = arrayOf(
		<#list cfgKeyClass.allFullKeyList as fullKey>
			"${fullKey}" <#sep>,
		</#list>
		)

		<#list cfgKeyClass.allSubKeyList as subKey>
		const val ${subKey?cap_first} = "${subKey}"

		</#list>
	}
</#if>

	companion object {
	<#if cfgKeyClass.keyPrefix??>
		const val _KEY_ = "${cfgKeyClass.keyPrefix}"
	</#if>

	<#if (cfgKeyClass.subKeyList?size > 0) >
		val _SUB_KEYS_ = arrayOf(
		<#list cfgKeyClass.subKeyList as subKey>
			"${subKey}" <#sep>,
		</#list>
		)
	</#if>

		<#list cfgKeyClass.subKeyMap?keys as subKey>

		const val ${subKey?cap_first} = "${cfgKeyClass.subKeyMap[subKey]}"
		</#list>
	}

	<#list cfgKeyClass.subClassMap?keys as subClassName>
		<#assign subClass = cfgKeyClass.subClassMap[subClassName]>
		${subClass.generateKotlinSource()}
	</#list>
}
