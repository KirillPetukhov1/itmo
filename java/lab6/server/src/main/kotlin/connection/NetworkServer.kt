package connection

import commands.CommandExecutor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

/**
 * Single-threaded non-blocking TCP server.
 * Uses a [Selector] to multiplex accept, read, and write events on a single thread.
 *
 * Modules as required by the specification:
 *
 * Connection acceptance - [acceptConnection]
 * Request reading - [readRequest]
 * Command processing - [processRequest] via [CommandExecutor]
 * Response sending - [sendResponse]
 *
 * @property port the TCP port to listen on
 * @property executor the command executor that handles business logic
 */
class NetworkServer(private val port: Int, private val executor: CommandExecutor) {

    private val logger = LoggerFactory.getLogger(NetworkServer::class.java)
    private val json = Json { ignoreUnknownKeys = true }

    private val selector: Selector = Selector.open()
    private val serverChannel: ServerSocketChannel = ServerSocketChannel.open().apply {
        configureBlocking(false)
        bind(InetSocketAddress(port))
        register(selector, SelectionKey.OP_ACCEPT)
    }

    private val readBuffers: MutableMap<SocketChannel, StringBuilder> = mutableMapOf()
    private val writeQueues: MutableMap<SocketChannel, ArrayDeque<ByteArray>> = mutableMapOf()

    /**
     * Starts the server event loop.
     * Runs indefinitely until the calling thread is interrupted.
     */
    fun start() {
        logger.info("Server started on port $port")
        while (!Thread.currentThread().isInterrupted) {
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                keys.remove()
                when {
                    key.isAcceptable -> acceptConnection(key)
                    key.isReadable -> readRequest(key)
                    key.isWritable -> sendResponse(key)
                }
            }
        }
    }

    /**
     * Accepts a new client connection and registers it for read events.
     *
     * @param key the selection key with an acceptable [ServerSocketChannel]
     */
    private fun acceptConnection(key: SelectionKey) {
        val serverChannel = key.channel() as ServerSocketChannel
        val clientChannel = serverChannel.accept() ?: return
        clientChannel.configureBlocking(false)
        clientChannel.register(selector, SelectionKey.OP_READ)
        readBuffers[clientChannel] = StringBuilder()
        logger.info("New connection accepted from ${clientChannel.remoteAddress}")
    }

    /**
     * Reads available bytes from the client channel and processes complete JSON lines.
     * Each request is delimited by a newline character.
     *
     * @param key the selection key with a readable [SocketChannel]
     */
    private fun readRequest(key: SelectionKey) {
        val channel = key.channel() as SocketChannel
        val buffer = ByteBuffer.allocate(8192)
        val bytesRead = try {
            channel.read(buffer)
        } catch (e: Exception) {
            logger.warn("Read error from ${channel.remoteAddress}: ${e.message}")
            closeChannel(channel)
            return
        }
        if (bytesRead == -1) {
            logger.info("Client disconnected: ${channel.remoteAddress}")
            closeChannel(channel)
            return
        }
        buffer.flip()
        val chunk = Charsets.UTF_8.decode(buffer).toString()
        val accumulator = readBuffers.getOrPut(channel) { StringBuilder() }
        accumulator.append(chunk)

        var newlineIndex = accumulator.indexOf('\n')
        while (newlineIndex >= 0) {
            val line = accumulator.substring(0, newlineIndex).trim()
            accumulator.delete(0, newlineIndex + 1)
            if (line.isNotEmpty()) {
                logger.info("Request received from ${channel.remoteAddress}: command=${extractCommandName(line)}")
                processRequest(channel, line, key)
            }
            newlineIndex = accumulator.indexOf('\n')
        }
    }

    /**
     * Processes a single JSON request line and enqueues the serialized response for sending.
     *
     * @param channel the client channel to reply to
     * @param requestJson the raw JSON string of the [connection.Request]
     * @param key the selection key used to register write interest
     */
    private fun processRequest(channel: SocketChannel, requestJson: String, key: SelectionKey) {
        val response = try {
            val request = json.decodeFromString<connection.Request>(requestJson)
            executor.execute(request)
        } catch (e: Exception) {
            logger.error("Failed to process request: ${e.message}")
            connection.Response("unknown", "{}", "Malformed request: ${e.message}")
        }
        val responseJson = json.encodeToString(response) + "\n"
        val bytes = responseJson.toByteArray(Charsets.UTF_8)
        writeQueues.getOrPut(channel) { ArrayDeque() }.addLast(bytes)
        channel.keyFor(selector)?.interestOps(SelectionKey.OP_READ or SelectionKey.OP_WRITE)
        logger.info("Response enqueued for ${channel.remoteAddress}: command=${response.commandName}")
    }

    /**
     * Writes enqueued response bytes to the client channel.
     * Removes the write interest when the queue is empty.
     *
     * @param key the selection key with a writable [SocketChannel]
     */
    private fun sendResponse(key: SelectionKey) {
        val channel = key.channel() as SocketChannel
        val queue = writeQueues[channel] ?: return
        while (queue.isNotEmpty()) {
            val bytes = queue.first()
            val buffer = ByteBuffer.wrap(bytes)
            try {
                channel.write(buffer)
            } catch (e: Exception) {
                logger.warn("Write error to ${channel.remoteAddress}: ${e.message}")
                closeChannel(channel)
                return
            }
            if (buffer.hasRemaining()) break
            queue.removeFirst()
        }
        if (queue.isEmpty()) {
            key.interestOps(SelectionKey.OP_READ)
        }
    }

    /**
     * Closes a [SocketChannel] and removes its associated state.
     *
     * @param channel the channel to close
     */
    private fun closeChannel(channel: SocketChannel) {
        try {
            channel.close()
        } catch (_: Exception) {}
        readBuffers.remove(channel)
        writeQueues.remove(channel)
    }

    /**
     * Closes the server channel and selector, releasing all resources.
     */
    fun stop() {
        logger.info("Server stopping")
        serverChannel.close()
        selector.close()
    }

    private fun extractCommandName(json: String): String =
        Regex("\"commandName\"\\s*:\\s*\"([^\"]+)\"").find(json)?.groupValues?.get(1) ?: "unknown"
}
