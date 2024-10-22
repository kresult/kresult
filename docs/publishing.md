Publishing
==========

Lib is published to Maven Central using [central.sonatype.com](https://central.sonatype.com) and the 
[vanniktech gradle-maven-publish-plugin](https://github.com/vanniktech/gradle-maven-publish-plugin).

Currently, publishing is still done manually, but should be automated in the future...

### Workflow

1. Make sure everything staged for the release is merged to `develop`
2. Check-out develop
   - Bump the version number in `version.txt`
   - Regenerate docs and kover report running: 
     ```bash 
     ./gradlew :clean :knit :dokkaHtmlMultiModule :check :koverHtmlReport
     ```
3. Create a [Pull Request](https://github.com/kresult/kresult/compare/main...develop), named `Release/X.Y.Z`, with 
   label `release` from `develop` to `main` (*containing the version you want to release after the slash*)
4. Get a review from (another) maintainer
5. Merge the PR
6. Create a new [Release](https://github.com/kresult/kresult/releases/new?target=main) with Target: `main` and 
   Tag: `vX.Y.Z` on GitHub. Generate release-notes.
7. Stage and publish the release on Maven Central

### Staging and Publication

Use the following command to stage a release. This will build all publisheable subprojects and create a staged Maven
Release:

```bash
./gradlew publishToMavenCentral --no-configuration-cache
```

Go to [Sonatype Central / Deployments](https://central.sonatype.com/publishing/deployments) and check the staged 
release. If everything looks fine, publish it from there.