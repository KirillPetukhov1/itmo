package connection

import java.nio.channels.SocketChannel

/**
 * Holds all mutable state associated with a single connected client.
 * Separates per-client state from the modules that operate on it -- SRP.
 *
 * @property channel the non-blocking [SocketChannel] for this client
 * @property readBuffer accumulates incoming bytes until a complete newline-delimited message arrives
 * @property writeQueue ordered queue of serialized response byte arrays waiting to be sent
 */
class ClientSession(val channel: SocketChannel) {

    val readBuffer: StringBuilder = StringBuilder()
    val writeQueue: ArrayDeque<ByteArray> = ArrayDeque()

    /**
     * Returns true when there is at least one response waiting to be sent.
     *
     * @return true if [writeQueue] is non-empty
     */
    fun hasPendingWrites(): Boolean = writeQueue.isNotEmpty()

    /**
     * Enqueues [bytes] for sending.
     *
     * @param bytes the serialized response to enqueue
     */
    fun enqueue(bytes: ByteArray) = writeQueue.addLast(bytes)

    /**
     * Returns the next response to send without removing it.
     *
     * @return the first element in [writeQueue]
     */
    fun peekNext(): ByteArray = writeQueue.first()

    /**
     * Removes the front response from the queue after it has been fully sent.
     */
    fun pollNext() = writeQueue.removeFirst()
}
