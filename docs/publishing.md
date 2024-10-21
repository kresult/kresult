Publishing
==========

Lib is published to Maven Central using [central.sonatype.com](https://central.sonatype.com) and the 
[vanniktech gradle-maven-publish-plugin](https://github.com/vanniktech/gradle-maven-publish-plugin).

Currently, publishing is still done manually, but should be automated in the future...

### Staging and Publication

Use the following command to stage a release. This will build all publisheable subprojects and create a staged Maven
Release:

```bash
./gradlew publishToMavenCentral --no-configuration-cache
```

Go to [Sonatype Central / Deployments](https://central.sonatype.com/publishing/deployments) and check the staged 
release. If everything looks fine, publish it from there.