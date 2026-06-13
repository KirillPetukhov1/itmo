package commands.implementations

import commands.abstractions.ServerCommand
import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Acknowledges a successful login.
 * Credential validation is performed by [connection.RequestProcessor] before this command
 * executes; if execution reaches here, the credentials are already confirmed valid.
 */
class LoginServerCommand : ServerCommand {

    override fun execute(): Response =
        Response("login", buildJsonObject { put("success", true) }.toString())
}