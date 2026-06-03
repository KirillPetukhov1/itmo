package connection

import kotlinx.serialization.Serializable

/**
 * Envelope sent from the client to the server over TCP.
 * Every request carries credentials for server-side authentication.
 * The server identifies the command by [commandName] and deserializes
 * the full command object from [serializedCommand].
 *
 * @property commandName the name of the command to execute
 * @property serializedCommand JSON representation of the concrete command object
 * @property login the requesting user's login
 * @property password the requesting user's plain-text password
 */
@Serializable
data class Request(
    val commandName: String,
    val serializedCommand: String,
    val login: String = "",
    val password: String = ""
)