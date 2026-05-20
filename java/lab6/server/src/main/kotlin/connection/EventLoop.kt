package connection

import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel

/**
 * Single-threaded non-blocking TCP event loop.
 * Owns the [Selector], [ServerSocketChannel], and session map.
 * Delegates each event type to the dedicated module responsible for handling it.
 *
 * The four required modules are wired here:
 * accept events ? [ConnectionAcceptor]
 * read events   ? [RequestReader]
 * processing    ? [RequestProcessor] (invoked from [RequestReader]'s onRequest callback)
 * write events  ? [ResponseSender]
 *
 * [EventLoop] itself is only responsible for the select loop and event dispatch -- SRP.
 *
 * @property port the TCP port to bind to
 * @property processor the command processing module
 */
class EventLoop(private val port: Int, private val processor: RequestProcessor) {

    private val logger = LoggerFactory.getLogger(EventLoop::class.java)

    private val selector: Selector = Selector.open()
    private val serverChannel: ServerSocketChannel = ServerSocketChannel.open().apply {
        configureBlocking(false)
        bind(InetSocketAddress(port))
        register(selector, SelectionKey.OP_ACCEPT)
    }
    private val sessions: MutableMap<SocketChannel, ClientSession> = mutableMapOf()

    private val acceptor = ConnectionAcceptor(selector, sessions)
    private val sender = ResponseSender(onClose = ::closeSession)
    private val reader = RequestReader(
        onRequest = { session, json, key -> processor.process(session, json, key) },
        onClose = ::closeSession
    )

    /**
     * Starts the event loop, blocking the calling thread until it is interrupted.
     */
    fun start() {
        logger.info("Event loop started on port $port")
        while (!Thread.currentThread().isInterrupted) {
            selector.select()
            val keys = selector.selectedKeys().iterator()
            while (keys.hasNext()) {
                val key = keys.next()
                keys.remove()
                when {
                    key.isAcceptable -> acceptor.accept(key)
                    key.isReadable -> dispatchRead(key)
                    key.isWritable -> dispatchWrite(key)
                }
            }
        }
    }

    /**
     * Stops the event loop, closing the server channel and selector.
     */
    fun stop() {
        logger.info("Event loop stopping")
        serverChannel.close()
        selector.close()
    }

    private fun dispatchRead(key: SelectionKey) {
        val session = sessions[key.channel() as SocketChannel] ?: return
        reader.read(key, session)
    }

    private fun dispatchWrite(key: SelectionKey) {
        val session = sessions[key.channel() as SocketChannel] ?: return
        sender.send(key, session)
    }

    private fun closeSession(session: ClientSession) {
        try { session.channel.close() } catch (_: Exception) {}
        sessions.remove(session.channel)
        logger.info("Session closed")
    }
}
