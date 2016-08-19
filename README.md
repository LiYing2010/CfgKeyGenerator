This is a source code generation tool which can generate constant declaration for keys in your configuration files.
So, in your application source, you can use the generated configuration key constants, instead of hard coding string.


Current version
===========
v1.0.0 released at 2016/08/02


Install
===========
Eclipse update site:

   http://www.liying-cn.net/updateSite/



Configuration File Supported
===========
- properties
- XML
- INI (TODO)
- YAML (TODO)
- JSON (TODO)
- the others... (if you need support for some other configuration files, just tell me)


Feature
===========
- There is an Eclipse plug-in so you can generate CfgKey source code from the Eclipse IDE

You can select a configuration file, and then select "CfgKey Generator" => "Generate" from the popup menu:

<img src="/doc/img/popupMenu.png" style="width: 400px;" title="Popup menu" />

Then you can input the code generation options in the popup dialog:

<img src="/doc/img/dialog-ClassGenerationInfomation.png" style="width: 400px;" title="Dialog" />



Examples
===========

(1)For the following configuration file:

Filename: AppConfig.xml

File content:


```xml

	<?xml version="1.0" encoding="UTF-8"?>
	<AppConfig>
		<LoginInfo>
			<UserName>Tom</UserName>
			<Password>PasswordForTom</Password>
		</LoginInfo>
	</AppConfig>
```

The generated source code will be:

```java

	......
	public class CfgKey {
		public static class LoginInfo {
			public static final String Password = "LoginInfo.Password";

			public static final String UserName = "LoginInfo.UserName";
		}
	}
	......

```


And then you can use these configuration key constants in your application code:


```java

	......
	Configuration cfg = CfgManager.getCfg();
	String userName = cfg.getString(CfgKey.LoginInfo.UserName);
	String password = cfg.getString(CfgKey.LoginInfo.Password);
	......

```



(2)For the following message string resource file:


Filename: message.properties

File content:

```properties

	error.conversion.integer	=	Please input an Integer.
	error.conversion.number		=	Please input a Number.
	error.conversion.date		=	Please input a Date.

```

The generated source code will be:

```java

	......
	public class MsgKey {
		public static class Error {
			public static class Conversion {
				public static final String Date = "error.conversion.date";

				public static final String Integer = "error.conversion.integer";

				public static final String Number = "error.conversion.number";
			}
		}
	}
	......

```


And then you can use these message key constants in your application code:


```java

	......
	if (isNotInteger(inputStr)) {
		String errorMsg = MsgUtils.getMessage(MsgKey.Error.Conversion.Integer);
		ShowError(errorMsg);
	}
	......

```

