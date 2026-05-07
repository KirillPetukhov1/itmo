package connection

import kotlinx.serialization.Serializable

/**
 * Envelope sent from the client to the server over TCP.
 * The server identifies the command by [commandName] and deserializes
 * the full command object from [serializedCommand].
 *
 * @property commandName the name of the command to execute (e.g. "insert", "show")
 * @property serializedCommand JSON representation of the concrete command object
 */
@Serializable
data class Request(
    val commandName: String,
    val serializedCommand: String
)
