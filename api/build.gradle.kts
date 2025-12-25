plugins {
    java
}

val branch = BuildInformation.branch()
val module = BuildInformation.module(project)
val buildVersion = BuildInformation.version(project)

val commit = BuildInformation.commit()
val commitShort = BuildInformation.commitShort()

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    compileOnly(libs.paper.api)
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveBaseName.set("mist-$module")
    archiveVersion.set(buildVersion)
    archiveClassifier.set("+$commitShort")
}