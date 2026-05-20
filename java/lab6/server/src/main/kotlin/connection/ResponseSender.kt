package connection

import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.SocketChannel

/**
 * Module 4 -- Response sending.
 *
 * Writes enqueued response bytes from [ClientSession.writeQueue] into the client [SocketChannel].
 * Handles partial writes -- if the TCP send buffer is full, the remaining bytes are left in the
 * queue and will be sent on the next [SelectionKey.OP_WRITE] event.
 * Removes [SelectionKey.OP_WRITE] interest when the queue is empty.
 * Invokes [onClose] on write errors.
 *
 * Responsibility: byte writing and flow control only -- no serialization or command logic.
 *
 * @property onClose callback invoked with the session when the channel must be closed
 */
class ResponseSender(private val onClose: (ClientSession) -> Unit) {

    private val logger = LoggerFactory.getLogger(ResponseSender::class.java)

    /**
     * Sends as many enqueued response bytes as the channel will accept right now.
     *
     * @param key the selection key whose channel is ready to write
     * @param session the [ClientSession] owning the write queue
     */
    fun send(key: SelectionKey, session: ClientSession) {
        val channel = key.channel() as SocketChannel

        while (session.hasPendingWrites()) {
            val bytes = session.peekNext()
            val buffer = ByteBuffer.wrap(bytes)

            try {
                channel.write(buffer)
            } catch (e: Exception) {
                logger.warn("Write error to ${channel.remoteAddress}: ${e.message}")
                onClose(session)
                return
            }

            if (buffer.hasRemaining()) break
            session.pollNext()
        }

        if (!session.hasPendingWrites()) {
            key.interestOps(SelectionKey.OP_READ)
            logger.info("Response fully sent to ${channel.remoteAddress}")
        }
    }
}
