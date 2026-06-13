package connection

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors

private const val FIXED_POOL_SIZE = 4

/**
 * Multithreaded TCP server replacing the single-threaded NIO event loop from lab 6.
 *
 * Threading model:
 * - A [java.util.concurrent.Executors.newCachedThreadPool] reads each accepted client connection.
 * - A new [Thread] is created per request to process it via [RequestProcessor].
 * - A [java.util.concurrent.Executors.newFixedThreadPool] of size [FIXED_POOL_SIZE] sends responses.
 *
 * @property port the TCP port to bind to
 * @property processor the request processor that validates credentials and produces responses
 */
class MultiThreadedServer(private val port: Int, private val processor: RequestProcessor) {

    private val logger = LoggerFactory.getLogger(MultiThreadedServer::class.java)
    private val json = Json { ignoreUnknownKeys = true }
    private val readPool = Executors.newCachedThreadPool()
    private val sendPool = Executors.newFixedThreadPool(FIXED_POOL_SIZE)

    @Volatile
    private var running = false
    private lateinit var serverSocket: ServerSocket

    /**
     * Starts the server, blocking the calling thread until [stop] is invoked or an error occurs.
     */
    fun start() {
        serverSocket = ServerSocket(port)
        running = true
        logger.info("Server started on port $port")
        while (running && !serverSocket.isClosed) {
            val client = try {
                serverSocket.accept()
            } catch (e: Exception) {
                if (running) logger.warn("Accept error: ${e.message}")
                break
            }
            logger.info("New connection from ${client.remoteSocketAddress}")
            readPool.submit { handleClient(client) }
        }
    }

    /**
     * Stops the server by closing the server socket and shutting down both thread pools.
     */
    fun stop() {
        running = false
        if (::serverSocket.isInitialized) serverSocket.close()
        readPool.shutdown()
        sendPool.shutdown()
        logger.info("Server stopped")
    }

    private fun handleClient(socket: Socket) {
        try {
            val line = BufferedReader(InputStreamReader(socket.getInputStream(), Charsets.UTF_8))
                .readLine() ?: run { socket.close(); return }
            logger.info("Request received, length=${line.length}")
            Thread {
                val response = processor.process(line)
                sendPool.submit { sendResponse(socket, response) }
            }.start()
        } catch (e: Exception) {
            logger.warn("Error reading from ${socket.remoteSocketAddress}: ${e.message}")
            try { socket.close() } catch (_: Exception) {}
        }
    }

    private fun sendResponse(socket: Socket, response: Response) {
        try {
            val writer = OutputStreamWriter(socket.getOutputStream(), Charsets.UTF_8)
            writer.write(json.encodeToString(response) + "\n")
            writer.flush()
            logger.info("Response sent for command: ${response.commandName}")
        } catch (e: Exception) {
            logger.warn("Error sending response: ${e.message}")
        } finally {
            try { socket.close() } catch (_: Exception) {}
        }
    }
}