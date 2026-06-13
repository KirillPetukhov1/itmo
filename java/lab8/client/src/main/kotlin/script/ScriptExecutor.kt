package script

import net.GatewayResult
import net.ServerGateway
import objects.Color
import objects.Coordinates
import objects.Country
import objects.Person
import objects.Product
import objects.UnitOfMeasure
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * Synchronous executor for command scripts read from a text file.
 *
 * Each line of the script is either a blank line, a comment (starts with '#'), or a command
 * in the same format used by the interactive console client. Commands that require a product
 * object read the product fields from the subsequent lines of the same file, one field per line,
 * in the order defined by the original interactive form:
 *
 * 1. name
 * 2. coordinates.x
 * 3. coordinates.y
 * 4. price
 * 5. part number (empty = null)
 * 6. manufacture cost (empty = null)
 * 7. unit of measure (empty = null)
 * 8. owner name
 * 9. owner height
 * 10. owner hair color
 * 11. owner nationality (empty = null)
 *
 * All gateway calls are made synchronously on the calling thread. The caller is responsible
 * for invoking [execute] on a background thread.
 *
 * Nested [execute_script] commands are supported up to [MAX_DEPTH] levels deep to prevent
 * infinite recursion.
 *
 * @property gateway the server facade used to dispatch each command
 */
class ScriptExecutor(private val gateway: ServerGateway) {

    /**
     * Reads the file at [filePath], executes every recognised command in order, and returns
     * the execution log as a list of human-readable strings.
     *
     * @param filePath absolute or relative path to the script file
     * @return the execution log; one entry per command outcome or error
     */
    fun execute(filePath: String): List<String> {
        val log = mutableListOf<String>()
        try {
            BufferedReader(FileReader(filePath)).use { reader ->
                runReader(reader, log, depth = 0)
            }
        } catch (e: IOException) {
            log.add("ERROR reading '$filePath': ${e.message}")
        }
        return log
    }

    private fun runReader(reader: BufferedReader, log: MutableList<String>, depth: Int) {
        if (depth > MAX_DEPTH) {
            log.add("ERROR: maximum nesting depth ($MAX_DEPTH) exceeded")
            return
        }
        var line = reader.readLine()
        while (line != null) {
            val trimmed = line.trim()
            if (trimmed.isNotBlank() && !trimmed.startsWith("#")) {
                val parts = trimmed.split("\\s+".toRegex(), limit = 2)
                val cmd = parts[0].lowercase()
                val arg = parts.getOrElse(1) { "" }.trim()
                dispatchCommand(cmd, arg, reader, log, depth)
            }
            line = reader.readLine()
        }
    }

    private fun dispatchCommand(
        cmd: String,
        arg: String,
        reader: BufferedReader,
        log: MutableList<String>,
        depth: Int
    ) {
        when (cmd) {
            "exit" -> return
            "help" -> log.add(
                "help: available commands: insert, update, remove_key, clear, " +
                        "remove_lower, replace_if_greater, remove_greater_key, info, show, " +
                        "count_greater_than_price, print_unique_unit_of_measure, " +
                        "print_field_descending_price, execute_script, exit"
            )
            "info"                        -> doInfo(log)
            "show"                        -> doShow(log)
            "insert"                      -> doInsert(arg, reader, log)
            "update"                      -> doUpdate(arg, reader, log)
            "remove_key"                  -> doRemoveKey(arg, log)
            "clear"                       -> doClear(log)
            "remove_lower"                -> doRemoveLower(reader, log)
            "replace_if_greater"          -> doReplaceIfGreater(arg, reader, log)
            "remove_greater_key"          -> doRemoveGreaterKey(arg, log)
            "count_greater_than_price"    -> doCountGreaterPrice(arg, log)
            "print_unique_unit_of_measure"-> doUniqueUnit(log)
            "print_field_descending_price"-> doPricesDesc(log)
            "execute_script"              -> doNestedScript(arg, log, depth)
            else                          -> log.add("UNKNOWN: $cmd")
        }
    }

    private fun doInfo(log: MutableList<String>) {
        when (val r = gateway.info()) {
            is GatewayResult.Success -> log.add("INFO: ${r.value}")
            is GatewayResult.Failure -> log.add("ERROR info: ${r.message}")
        }
    }

    private fun doShow(log: MutableList<String>) {
        when (val r = gateway.show()) {
            is GatewayResult.Success -> {
                log.add("SHOW: ${r.value.size} element(s)")
                r.value.forEach { e ->
                    log.add("  [${e.key}] ${e.product.name}" +
                            "  price=${e.product.price}  creator=${e.creatorLogin}")
                }
            }
            is GatewayResult.Failure -> log.add("ERROR show: ${r.message}")
        }
    }

    private fun doInsert(arg: String, reader: BufferedReader, log: MutableList<String>) {
        if (arg.isBlank() || arg == "null") {
            log.add("ERROR insert: key required as argument after 'insert'")
            return
        }
        val product = readProduct(reader)
        if (product == null) {
            log.add("ERROR insert [$arg]: failed to parse product fields")
            return
        }
        when (val r = gateway.insert(arg, product)) {
            is GatewayResult.Success -> log.add("INSERT [$arg]: OK")
            is GatewayResult.Failure -> log.add("ERROR insert [$arg]: ${r.message}")
        }
    }

    private fun doUpdate(arg: String, reader: BufferedReader, log: MutableList<String>) {
        val id = arg.toLongOrNull()
        if (id == null) {
            log.add("ERROR update: id must be a number, got '$arg'")
            return
        }
        val product = readProduct(reader)
        if (product == null) {
            log.add("ERROR update [$id]: failed to parse product fields")
            return
        }
        when (val r = gateway.update(id, product)) {
            is GatewayResult.Success -> log.add("UPDATE [$id]: OK")
            is GatewayResult.Failure -> log.add("ERROR update [$id]: ${r.message}")
        }
    }

    private fun doRemoveKey(arg: String, log: MutableList<String>) {
        if (arg.isBlank() || arg == "null") {
            log.add("ERROR remove_key: key required as argument")
            return
        }
        when (val r = gateway.removeKey(arg)) {
            is GatewayResult.Success -> log.add("REMOVE_KEY [$arg]: OK")
            is GatewayResult.Failure -> log.add("ERROR remove_key [$arg]: ${r.message}")
        }
    }

    private fun doClear(log: MutableList<String>) {
        when (val r = gateway.clear()) {
            is GatewayResult.Success -> log.add("CLEAR: OK")
            is GatewayResult.Failure -> log.add("ERROR clear: ${r.message}")
        }
    }

    private fun doRemoveLower(reader: BufferedReader, log: MutableList<String>) {
        val product = readProduct(reader)
        if (product == null) {
            log.add("ERROR remove_lower: failed to parse product fields")
            return
        }
        when (val r = gateway.removeLower(product)) {
            is GatewayResult.Success -> log.add("REMOVE_LOWER: removed ${r.value} element(s)")
            is GatewayResult.Failure -> log.add("ERROR remove_lower: ${r.message}")
        }
    }

    private fun doReplaceIfGreater(arg: String, reader: BufferedReader, log: MutableList<String>) {
        if (arg.isBlank() || arg == "null") {
            log.add("ERROR replace_if_greater: key required as argument")
            return
        }
        val product = readProduct(reader)
        if (product == null) {
            log.add("ERROR replace_if_greater [$arg]: failed to parse product fields")
            return
        }
        when (val r = gateway.replaceIfGreater(arg, product)) {
            is GatewayResult.Success -> {
                val status = if (r.value) "replaced" else "not replaced (value not greater)"
                log.add("REPLACE_IF_GREATER [$arg]: $status")
            }
            is GatewayResult.Failure -> log.add("ERROR replace_if_greater [$arg]: ${r.message}")
        }
    }

    private fun doRemoveGreaterKey(arg: String, log: MutableList<String>) {
        if (arg.isBlank() || arg == "null") {
            log.add("ERROR remove_greater_key: key required as argument")
            return
        }
        when (val r = gateway.removeGreaterKey(arg)) {
            is GatewayResult.Success ->
                log.add("REMOVE_GREATER_KEY [$arg]: removed ${r.value} element(s)")
            is GatewayResult.Failure ->
                log.add("ERROR remove_greater_key [$arg]: ${r.message}")
        }
    }

    private fun doCountGreaterPrice(arg: String, log: MutableList<String>) {
        val price = arg.toLongOrNull()
        if (price == null) {
            log.add("ERROR count_greater_than_price: price must be a number, got '$arg'")
            return
        }
        when (val r = gateway.countGreaterThanPrice(price)) {
            is GatewayResult.Success ->
                log.add("COUNT_GREATER_PRICE [$price]: ${r.value} element(s)")
            is GatewayResult.Failure ->
                log.add("ERROR count_greater_than_price: ${r.message}")
        }
    }

    private fun doUniqueUnit(log: MutableList<String>) {
        when (val r = gateway.uniqueUnitOfMeasure()) {
            is GatewayResult.Success ->
                log.add("UNIQUE_UNITS: ${r.value.ifEmpty { listOf("(none)") }}")
            is GatewayResult.Failure ->
                log.add("ERROR print_unique_unit_of_measure: ${r.message}")
        }
    }

    private fun doPricesDesc(log: MutableList<String>) {
        when (val r = gateway.pricesDescending()) {
            is GatewayResult.Success -> log.add("PRICES_DESC: ${r.value}")
            is GatewayResult.Failure -> log.add("ERROR print_field_descending_price: ${r.message}")
        }
    }

    private fun doNestedScript(arg: String, log: MutableList<String>, depth: Int) {
        if (arg.isBlank()) {
            log.add("ERROR execute_script: file path required")
            return
        }
        log.add(">> entering nested script: $arg")
        try {
            BufferedReader(FileReader(arg)).use { nested ->
                runReader(nested, log, depth + 1)
            }
        } catch (e: IOException) {
            log.add("ERROR execute_script '$arg': ${e.message}")
        }
        log.add(">> exited nested script: $arg")
    }

    /**
     * Reads exactly eleven lines from [reader] and builds a [Product].
     * Returns null if any required line is missing or contains an invalid value.
     *
     * @param reader the reader positioned just before the first product field line
     * @return the parsed [Product], or null on any parse error
     */
    private fun readProduct(reader: BufferedReader): Product? {
        try {
            val name = reader.readLine()?.trim()?.takeIf { it.isNotBlank() } ?: return null
            val coordX = reader.readLine()?.trim()?.toLongOrNull() ?: return null
            val coordY = reader.readLine()?.trim()?.toDoubleOrNull() ?: return null
            val price = reader.readLine()?.trim()?.toLongOrNull() ?: return null
            val partNumber = reader.readLine()?.trim()?.takeIf { it.isNotBlank() }
            val manufactureCost = reader.readLine()?.trim()?.toLongOrNull()
            val unit = reader.readLine()?.trim()?.takeIf { it.isNotBlank() }
                ?.let { s -> try { UnitOfMeasure.valueOf(s) } catch (_: Exception) { null } }
            val ownerName = reader.readLine()?.trim()?.takeIf { it.isNotBlank() } ?: return null
            val ownerHeight = reader.readLine()?.trim()?.toLongOrNull() ?: return null
            val hairColorStr = reader.readLine()?.trim() ?: return null
            val hairColor = try {
                Color.valueOf(hairColorStr)
            } catch (_: Exception) {
                return null
            }
            val nationality = reader.readLine()?.trim()?.takeIf { it.isNotBlank() }
                ?.let { s -> try { Country.valueOf(s) } catch (_: Exception) { null } }
            return Product(
                name = name,
                coordinates = Coordinates(x = coordX, y = coordY),
                price = price,
                partNumber = partNumber,
                manufactureCost = manufactureCost,
                unitOfMeasure = unit,
                owner = Person(
                    name = ownerName,
                    height = ownerHeight,
                    hairColor = hairColor,
                    nationality = nationality
                )
            )
        } catch (_: Exception) {
            return null
        }
    }

    private companion object {
        const val MAX_DEPTH = 10
    }
}