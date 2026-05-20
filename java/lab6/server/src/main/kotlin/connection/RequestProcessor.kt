package connection

import commands.CommandExecutor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.nio.channels.SelectionKey

/**
 * Module 3 -- Command processing.
 *
 * Deserializes a raw JSON string into a [Request], delegates execution to [CommandExecutor],
 * serializes the resulting [Response], and enqueues it in the [ClientSession] write queue.
 * Switches the channel's selector interest to include [SelectionKey.OP_WRITE].
 *
 * Responsibility: deserialization, execution delegation, serialization, and write scheduling only.
 * Has no knowledge of byte transport or NIO channels beyond registering write interest.
 *
 * @property executor the command executor that resolves and runs server commands
 */
class RequestProcessor(private val executor: CommandExecutor) {

    private val logger = LoggerFactory.getLogger(RequestProcessor::class.java)
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Processes one raw [requestJson] string for [session] and enqueues the response.
     *
     * @param session the client session to write the response into
     * @param requestJson the raw JSON string received from the client
     * @param key the selection key used to register write interest after enqueueing
     */
    fun process(session: ClientSession, requestJson: String, key: SelectionKey) {
        val response = try {
            val request = json.decodeFromString<Request>(requestJson)
            logger.info("Processing command: ${request.commandName}")
            executor.execute(request)
        } catch (e: Exception) {
            logger.error("Failed to process request: ${e.message}")
            Response("unknown", "{}", "Malformed request: ${e.message}")
        }

        val responseBytes = (json.encodeToString(response) + "\n").toByteArray(Charsets.UTF_8)
        session.enqueue(responseBytes)
        key.interestOps(SelectionKey.OP_READ or SelectionKey.OP_WRITE)
        logger.info("Response enqueued for command: ${response.commandName}")
    }
}
