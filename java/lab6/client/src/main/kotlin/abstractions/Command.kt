package abstractions

import kotlinx.serialization.Transient

/**
 * Base class for all client-side commands.
 *
 * The lifecycle of a command:
 * 1. [start] — validates arguments and reads any user input; called before network interaction
 * 2. The command object is serialized and sent to the server when [isShouldBeSent] is true
 * 3. The server fills result fields and sends the command object back
 * 4. [finish] — displays the result received from the server
 *
 * @property description human-readable description shown by the help command
 * @property isShouldBeSent true when the command requires server interaction
 */
abstract class Command {

    @Transient
    abstract val description: String

    @Transient
    abstract val isShouldBeSent: Boolean

    /**
     * Validates arguments and collects any necessary user input.
     *
     * @param args the tokenised command line including the command name at index 0
     * @throws IllegalArgumentException if the argument count or format is wrong
     */
    abstract fun start(args: Array<String>)

    /**
     * Displays the result after the server response has been applied to this object.
     */
    abstract fun finish()
}
