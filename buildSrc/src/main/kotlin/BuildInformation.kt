import org.gradle.api.Project
import java.io.ByteArrayOutputStream

object BuildInformation {

    fun module(project: Project): String =
        project.name

    fun version(project: Project): String =
        project.version.toString()

    fun branch(): String =
        env("GIT_BRANCH")
            ?: env("BRANCH_NAME")
            ?: git("rev-parse", "--abbrev-ref", "HEAD")
            ?: "local"

    fun commit(): String =
        env("GIT_COMMIT")
            ?: env("COMMIT_SHA")
            ?: git("rev-parse", "HEAD")
            ?: "unknown"

    fun commitShort(): String = commit().takeIf { it.length >= 7 }?.substring(0, 7) ?: commit()

    private fun env(key: String): String? = System.getenv(key)?.takeIf { it.isNotBlank() }

    private fun git(vararg args: String): String? =
        runCatching {
            val out = ByteArrayOutputStream()

            ProcessBuilder("git", *args)
                .redirectErrorStream(true)
                .start()
                .inputStream
                .bufferedReader()
                .readLine()
                ?.trim()
                ?.takeIf { it.isNotBlank() }
        }.getOrNull()
}
