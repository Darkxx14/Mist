val mistGroup: String by project
val mistVersion: String by project

allprojects {
    group = mistGroup
    version = mistVersion

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}
