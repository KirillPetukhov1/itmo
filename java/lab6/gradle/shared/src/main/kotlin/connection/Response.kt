package connection

import kotlinx.serialization.Serializable

/**
 * Envelope sent from the server to the client over TCP.
 * Contains the result payload that the client passes to [Command.finish].
 *
 * @property commandName the name of the command that was executed
 * @property serializedResult JSON representation of the concrete command object
 *           with result fields populated by the server
 * @property errorMessage non-null when the server encountered an error; the client
 *           displays this message instead of calling finish
 */
@Serializable
data class Response(
    val commandName: String,
    val serializedResult: String,
    val errorMessage: String? = null
)
