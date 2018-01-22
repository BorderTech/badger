# java-common
Reusable build configuration for for BorderTech open source projects.

## bordertech-parent
This configures the maven release plugin for open source BorderTech projects so they can release to Maven Central.

Projects using this must ensure the necessary POM sections are overriden - these are marked in the bordertech-parent pom, for example:

```xml
<!--
	Descendants SHOULD override the url.
-->
<url>https://github.com/bordertech/java-common/</url>
```

