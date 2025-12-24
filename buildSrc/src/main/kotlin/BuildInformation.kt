import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object BuildInformation {

    fun module(project: Project): String = project.name

    fun version(project: Project): String = project.version.toString()

    fun branch(): String =
        System.getenv("GIT_BRANCH")
            ?: System.getenv("BRANCH_NAME")
            ?: runCatching {
                val out = ByteArrayOutputStream()

                ProcessBuilder("git", "rev-parse", "--abbrev-ref", "HEAD")
                    .redirectErrorStream(true)
                    .start()
                    .inputStream
                    .bufferedReader()
                    .readLine()
            }.getOrNull()
            ?: "local"
}
