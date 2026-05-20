package connection

import org.slf4j.LoggerFactory
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

/**
 * Module 1 -- Connection acceptance.
 *
 * Accepts incoming TCP connections from the [ServerSocketChannel] and registers each new
 * [SocketChannel] with the [Selector] for read events.
 * Creates a [ClientSession] for every accepted connection and stores it in [sessions].
 *
 * Responsibility: accept and register only -- no reading, writing, or command processing.
 *
 * @property selector the shared NIO selector
 * @property sessions the shared map from channel to session, populated on each accepted connection
 */
class ConnectionAcceptor(
    private val selector: Selector,
    private val sessions: MutableMap<SocketChannel, ClientSession>
) {

    private val logger = LoggerFactory.getLogger(ConnectionAcceptor::class.java)

    /**
     * Accepts a new client connection from the [ServerSocketChannel] associated with [key].
     * Registers the resulting [SocketChannel] for [SelectionKey.OP_READ] and creates a session.
     *
     * @param key the selection key whose channel is ready to accept
     */
    fun accept(key: SelectionKey) {
        val serverChannel = key.channel() as ServerSocketChannel
        val clientChannel = serverChannel.accept() ?: return
        clientChannel.configureBlocking(false)
        clientChannel.register(selector, SelectionKey.OP_READ)
        sessions[clientChannel] = ClientSession(clientChannel)
        logger.info("New connection accepted from ${clientChannel.remoteAddress}")
    }
}
