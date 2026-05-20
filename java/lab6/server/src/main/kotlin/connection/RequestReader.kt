package connection

import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel

private const val BUFFER_SIZE = 8192

/**
 * Module 2 -- Request reading.
 *
 * Reads available bytes from a client [SocketChannel] into the session's [ClientSession.readBuffer].
 * Extracts complete newline-delimited JSON request strings and passes each one to [onRequest].
 * Handles client disconnection and read errors by invoking [onClose].
 *
 * Responsibility: byte reading and message framing only -- no deserialization or command logic.
 *
 * @property onRequest callback invoked with (session, rawJson) for each complete request line
 * @property onClose callback invoked with the session when the channel must be closed
 */
class RequestReader(
    private val onRequest: (ClientSession, String, SelectionKey) -> Unit,
    private val onClose: (ClientSession) -> Unit
) {

    private val logger = LoggerFactory.getLogger(RequestReader::class.java)

    /**
     * Reads all available bytes from the channel associated with [key], appends them to the
     * session buffer, and dispatches any complete lines to [onRequest].
     *
     * @param key the selection key whose channel is ready to read
     * @param session the [ClientSession] owning the read buffer
     */
    fun read(key: SelectionKey, session: ClientSession) {
        val channel = key.channel() as SocketChannel
        val buffer = ByteBuffer.allocate(BUFFER_SIZE)

        val bytesRead = try {
            channel.read(buffer)
        } catch (e: Exception) {
            logger.warn("Read error from ${channel.remoteAddress}: ${e.message}")
            onClose(session)
            return
        }

        if (bytesRead == -1) {
            logger.info("Client disconnected: ${channel.remoteAddress}")
            onClose(session)
            return
        }

        buffer.flip()
        session.readBuffer.append(Charsets.UTF_8.decode(buffer))

        var newlineIndex = session.readBuffer.indexOf('\n')
        while (newlineIndex >= 0) {
            val line = session.readBuffer.substring(0, newlineIndex).trim()
            session.readBuffer.delete(0, newlineIndex + 1)
            if (line.isNotEmpty()) {
                logger.info("Request received from ${channel.remoteAddress}: line length=${line.length}")
                onRequest(session, line, key)
            }
            newlineIndex = session.readBuffer.indexOf('\n')
        }
    }
}
