import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    java
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
}

val branch = BuildInformation.branch()
val module = BuildInformation.module(project)
val buildVersion = BuildInformation.version(project)

val commit = BuildInformation.commit()
val commitShort = BuildInformation.commitShort()

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    maven("https://repo.xyrisdev.com/repository/maven-public/")
}

dependencies {
    implementation(project(":mist-api"))

    compileOnly(libs.paper.api)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.withType<Javadoc>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<AbstractArchiveTask>().configureEach {
    archiveVersion.set(buildVersion)
    archiveClassifier.set(commitShort)
}

paper {
    name = "Mist-Examples"
    version = "$buildVersion-$commitShort"
    apiVersion = "1.21"

    description = "API Examples of Mist."
    website = "github.com/Darkxx14/Mist/"

    main = "com.xyrisdev.mist.MistExamplePlugin"

    foliaSupported = true

    authors = listOf("darkxx16")

    serverDependencies {
        register("Mist") {
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
            required = true
        }
    }
}

tasks.generatePaperPluginDescription {
    useDefaultCentralProxy()
}