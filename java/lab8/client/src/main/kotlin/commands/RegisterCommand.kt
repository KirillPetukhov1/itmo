package commands

import abstractions.AbstractReaderWriter
import abstractions.AuthCommand
import kotlinx.serialization.json.*

/**
 * Interactively reads a new user's login and password, sends a registration request to the server,
 * and reports the result.
 *
 * @property readerWriter the I/O channel used to prompt for and read input
 */
class RegisterCommand(private val readerWriter: AbstractReaderWriter) : AuthCommand(readerWriter) {

    override val description = "register : Register a new user account"

    /**
     * Builds the JSON payload carrying the new user's credentials for the server.
     *
     * @return JSON string with "login" and "password" fields
     */
    fun toPayload(): String =
        buildJsonObject {
            put("login", login)
            put("password", password)
        }.toString()

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
        if (success) readerWriter.write("Registration successful. Use 'login' to authenticate.")
    }
}