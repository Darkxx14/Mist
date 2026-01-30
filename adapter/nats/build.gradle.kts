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
    compileOnly(project(":mist-api"))

    compileOnly(libs.jnats)
    compileOnly(libs.protobuf)
    compileOnly(libs.jetbrains.annotations)
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveVersion.set(buildVersion)
    archiveClassifier.set(commitShort)
}