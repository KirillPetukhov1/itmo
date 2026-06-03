package commands

import abstractions.AbstractReaderWriter
import abstractions.AuthCommand
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Interactively reads a user's login and password and sends them to the server for validation.
 * On success, [manager.CommandManager] stores the credentials for all subsequent requests.
 * The credentials are transmitted in the [connection.Request] envelope, not in the payload.
 *
 * @property readerWriter the I/O channel used to prompt for and read input
 */
class LoginCommand(private val readerWriter: AbstractReaderWriter) : AuthCommand(readerWriter) {

    override val description = "Log in to the server"

    /**
     * Returns an empty JSON payload because credentials are transmitted in the
     * [connection.Request] envelope and validated by the server before command execution.
     *
     * @return empty JSON object string
     */
    fun toPayload(): String = "{}"

    /**
     * Applies the server result to this command.
     *
     * @param resultJson the serialized result JSON received from the server
     */
    fun applyResult(resultJson: String) {
        success = try {
            Json.parseToJsonElement(resultJson).jsonObject["success"]
                ?.jsonPrimitive?.booleanOrNull ?: false
        } catch (_: Exception) {
            false
        }
    }

    override fun finish() {
        if (success) readerWriter.write("Logged in as $login.")
    }
}