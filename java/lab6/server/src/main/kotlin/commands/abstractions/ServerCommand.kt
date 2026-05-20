package commands.abstractions

import connection.Response

/**
 * Represents a single executable server-side command.
 *
 * Each implementation encapsulates one operation on the collection:
 * it parses its own arguments from the serialized payload received in the [connection.Request],
 * carries out the operation via [collection.CollectionManager], and returns a [Response].
 *
 * Implementations are instantiated per request by factories registered in
 * [commands.CommandRegistry].
 */
interface ServerCommand {

    /**
     * Executes the command and returns the result envelope.
     *
     * @return a [Response] carrying the serialized result or an error message
     */
    fun execute(): Response
}
