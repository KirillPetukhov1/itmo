package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Requests all collection elements from the server and displays them sorted by default order.
 */
@Serializable
class ShowCommand : Command() {

    @Transient
    override val description =
        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении"

    @Transient
    override val isShouldBeSent = true

    private var lines: List<String> = emptyList()

    override fun start(args: Array<String>) {
        require(args.size == 1) { "show takes no arguments" }
    }

    /**
     * Populates [lines] from the server response JSON.
     *
     * @param responseJson the serialised result containing a "lines" JSON array
     */
    fun applyResult(responseJson: String) {
        lines = Json.parseToJsonElement(responseJson).jsonObject["lines"]
            ?.jsonArray
            ?.map { it.jsonPrimitive.content }
            ?: emptyList()
    }

    override fun finish() {
        if (lines.isEmpty()) {
            println("The collection does not contain any items.")
        } else {
            lines.forEach(::println)
        }
    }
}
