plugins {
    java
}

val branch = BuildInformation.branch()
val module = BuildInformation.module(project)
val version = BuildInformation.version(project)

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

dependencies {
    compileOnly(libs.paper.api)
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveBaseName.set("mist-$module")
    archiveVersion.set(version)
    archiveClassifier.set(branch)
}
