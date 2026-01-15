rootProject.name = "Mist"

include(
        "mist-api",
        "mist-paper"
)

pluginManagement {
        repositories {
                gradlePluginPortal()
                mavenCentral()
                maven("https://repo.papermc.io/repository/maven-public/")
        }
}

plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}