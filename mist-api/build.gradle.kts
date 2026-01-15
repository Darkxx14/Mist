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
    withJavadocJar()
}

dependencies {
    compileOnly(libs.paper.api)
}

tasks.named<Jar>("jar") {
    archiveFileName.set(
        "mist-$module-$buildVersion-$commitShort.jar"
    )
}

tasks.named<Jar>("javadocJar") {
    archiveFileName.set(
        "mist-$module-$buildVersion-$commitShort-javadoc.jar"
    )
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
    title = "mist-api"
}