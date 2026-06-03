package commands.implementations

import auth.AuthService
import commands.abstractions.ServerCommand
import commands.requireString
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Registers a new user account.
 * This command is exempt from authentication checks in [connection.RequestProcessor].
 *
 * @property authService the authentication service used for registration
 * @property payload serialized JSON containing "login" and "password" fields
 */
class RegisterServerCommand(
    private val authService: AuthService,
    private val payload: String
) : ServerCommand {

    override fun execute(): Response {
        val login = requireString(payload, "login")
        val password = requireString(payload, "password")
        val success = authService.register(login, password)
        return if (success) {
            Response("register", buildJsonObject { put("success", true) }.toString())
        } else {
            Response(
                commandName = "register",
                serializedResult = buildJsonObject { put("success", false) }.toString(),
                errorMessage = "User '$login' already exists"
            )
        }
    }
}