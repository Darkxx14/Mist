val mistGroup: String by project
val mistVersion: String by project

allprojects {
    group = mistGroup
    version = mistVersion

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.xyrisdev.com/repository/maven-public/")
    }
}
