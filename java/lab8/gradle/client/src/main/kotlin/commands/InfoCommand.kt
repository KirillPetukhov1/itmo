package commands

import abstractions.Command
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * Requests collection metadata from the server and displays it.
 */
@Serializable
class InfoCommand : Command() {

    @Transient
    override val description =
        "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)"

    @Transient
    override val isShouldBeSent = true

    var info: String = ""

    override fun start(args: Array<String>) {
        require(args.size == 1) { "info takes no arguments" }
    }

    /**
     * Populates [info] from a server response JSON object with an "info" field.
     *
     * @param responseJson the serialised result returned by the server
     */
    fun applyResult(responseJson: String) {
        info = Json.parseToJsonElement(responseJson).jsonObject["info"]?.jsonPrimitive?.content ?: ""
    }

    override fun finish() = println(info)
}
