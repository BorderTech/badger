# java-common
Reusable build configuration for for BorderTech open source projects.

## Status

[![CircleCI](https://circleci.com/gh/BorderTech/java-common.svg?style=svg)](https://circleci.com/gh/BorderTech/java-common)

## qa-parent
BorderTech java projects should generally use this as their parent POM.

It runs quality assurance checks on your java code using tools such as checkstyle, pmd and findbugs.

By default qa checks do not run, you must enable them on a per-module basis in the pom.xml like so:

```xml
<properties>
	<!--
		Set qa.skip to false to run QA checks.

		Note: it's called wc.qa.skip because this functionality originated in the WComponents project.
	-->
	<wc.qa.skip>false</wc.qa.skip>
</properties>
``` 

The qa-parent inherits all of the release functionality from bordertech-parent, discussed below.

## bordertech-parent
This is the top-level pom.xml file. 
It configures the maven release plugin for open source BorderTech projects to release to Maven Central.

_Note that java projects should generally not consume this directly but instead should use qa-parent as a parent POM instead._

Projects using this must ensure the necessary POM sections are overriden - these are marked in the bordertech-parent pom, for example:

```xml
<!--
	Descendants SHOULD override the url.
-->
<url>https://github.com/bordertech/java-common/</url>
```

Once you have configured your project and environment you can release to Maven Central with a command like so:

```mvn release:clean release:prepare release:perform -Psonatype-oss-release```

or, to skip tests:

```mvn release:clean release:prepare release:perform -Psonatype-oss-release -Darguments="-DskipTests"```


Full documentation is available in the wiki under [Releasing](https://github.com/BorderTech/java-common/wiki/Releasing)

## build-tools
This is primarily a shared resources module used by qa-parent and potentially other BorderTech maven modules.

