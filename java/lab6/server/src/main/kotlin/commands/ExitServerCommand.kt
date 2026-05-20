package commands

import connection.Response
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * Acknowledges a client exit request.
 * The server itself does not stop; only the client terminates.
 */
class ExitServerCommand : ServerCommand {

    override fun execute(): Response =
        Response("exit", buildJsonObject { put("exit", true) }.toString())
}
