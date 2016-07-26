<#if cfgKeyClass.topClass>
<#include "fileComment.ftl">

package ${cfgKeyClass.packageName};

// CHECKSTYLE:OFF
</#if>

public <#if !cfgKeyClass.topClass>static</#if> class ${cfgKeyClass.className?cap_first} <#if cfgKeyClass.baseClassName??> extends ${cfgKeyClass.baseClassName} </#if> {
<#if cfgKeyClass.allFullKeyList??>
	public static class AllKeys {
		public static final String[] _Array_ = {
		<#list cfgKeyClass.allFullKeyList as fullKey>
			"${fullKey}",
		</#list>
		};

		<#list cfgKeyClass.allSubKeyList as subKey>
			public static final String ${subKey?cap_first} = "${subKey}";

		</#list>
	}

</#if>

<#if cfgKeyClass.keyPrefix??>
	public static final String _KEY_ = "${cfgKeyClass.keyPrefix}";
</#if>

<#if (cfgKeyClass.subKeyList?size > 0) >
	public static final String[] _SUB_KEYS_ = {
	<#list cfgKeyClass.subKeyList as subKey>
		"${subKey}",
	</#list>
	};
</#if>

	<#list cfgKeyClass.subKeyMap?keys as subKey>

	public static final String ${subKey?cap_first} = "${cfgKeyClass.subKeyMap[subKey]}";
	</#list>

	<#list cfgKeyClass.subClassMap?keys as subClassName>
		<#assign subClass = cfgKeyClass.subClassMap[subClassName]>
		${subClass.generateJavaSource()}
	</#list>
}
