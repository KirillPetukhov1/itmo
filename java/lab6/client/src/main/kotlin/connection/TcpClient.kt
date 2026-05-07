package connection

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException

private const val CONNECT_TIMEOUT_MS = 3000
private const val READ_TIMEOUT_MS = 5000
private const val RETRY_DELAY_MS = 2000L
private const val MAX_RETRIES = 3

/**
 * TCP client that communicates with the server using [InputStreamReader] and [OutputStreamWriter].
 * Requests are newline-delimited JSON strings.
 * Handles temporary server unavailability with a configurable retry policy.
 *
 * @property host the server hostname or IP address
 * @property port the server TCP port
 */
class TcpClient(private val host: String, private val port: Int) {

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Sends [request] to the server and returns the [Response].
     * Retries up to [MAX_RETRIES] times when the server is temporarily unavailable.
     *
     * @param request the request to send
     * @return the response from the server
     * @throws IllegalStateException if the server could not be reached after all retries
     */
    fun send(request: Request): Response {
        val requestJson = json.encodeToString(request) + "\n"
        var lastException: Exception? = null

        repeat(MAX_RETRIES) { attempt ->
            try {
                return sendOnce(requestJson)
            } catch (e: SocketTimeoutException) {
                println("Server not responding, retrying (${attempt + 1}/$MAX_RETRIES)...")
                lastException = e
                Thread.sleep(RETRY_DELAY_MS)
            } catch (e: java.net.ConnectException) {
                println("Server unavailable, retrying (${attempt + 1}/$MAX_RETRIES)...")
                lastException = e
                Thread.sleep(RETRY_DELAY_MS)
            } catch (e: java.io.IOException) {
                println("Connection error, retrying (${attempt + 1}/$MAX_RETRIES)...")
                lastException = e
                Thread.sleep(RETRY_DELAY_MS)
            }
        }

        throw IllegalStateException(
            "Server unavailable after $MAX_RETRIES attempts: ${lastException?.message}"
        )
    }

    private fun sendOnce(requestJson: String): Response {
        Socket().use { socket ->
            socket.connect(InetSocketAddress(host, port), CONNECT_TIMEOUT_MS)
            socket.soTimeout = READ_TIMEOUT_MS

            OutputStreamWriter(socket.getOutputStream(), Charsets.UTF_8).apply {
                write(requestJson)
                flush()
            }

            val reader = BufferedReader(InputStreamReader(socket.getInputStream(), Charsets.UTF_8))
            val responseLine = reader.readLine()
                ?: throw java.io.IOException("Server closed connection without response")

            return json.decodeFromString<Response>(responseLine)
        }
    }
}
