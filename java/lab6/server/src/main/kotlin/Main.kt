import collection.CollectionManager
import commands.CommandExecutor
import commands.CommandRegistry
import connection.EventLoop
import connection.RequestProcessor
import files.XmlFileManager
import objectCreation.IdManager
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ServerMain")

private const val DEFAULT_PORT = 8080
private const val ENV_FILE = "LAB5_FILENAME"
private const val ENV_PORT = "LAB6_PORT"
private const val SERVER_SAVE_COMMAND = "server_save"

/**
 * Application entry point for the server module.
 *
 * Reads the collection file path from the [ENV_FILE] environment variable.
 * Reads the TCP port from the [ENV_PORT] environment variable (default [DEFAULT_PORT]).
 * Wires all modules together, starts the [EventLoop] in a daemon thread, and enters
 * the administrator console loop in the main thread.
 * A shutdown hook saves the collection before the JVM exits.
 */
fun main() {
    logger.info("Server starting")

    val filePath = System.getenv(ENV_FILE) ?: System.getProperty(ENV_FILE)
        ?: run {
            logger.error("Environment variable $ENV_FILE is not set")
            return
        }

    val port = (System.getenv(ENV_PORT) ?: System.getProperty(ENV_PORT))?.toIntOrNull() ?: DEFAULT_PORT

    val collectionManager = CollectionManager(IdManager())
    val fileManager = XmlFileManager(filePath)

    try {
        val loaded = fileManager.load()
        collectionManager.load(loaded)
        logger.info("Collection loaded: ${loaded.size} elements from $filePath")
    } catch (e: Exception) {
        logger.warn("Could not load collection: ${e.message}")
    }

    val executor = CommandExecutor(CommandRegistry(collectionManager))
    val processor = RequestProcessor(executor)
    val eventLoop = EventLoop(port, processor)

    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info("Shutdown hook triggered -- saving collection")
        saveCollection(fileManager, collectionManager, filePath)
        eventLoop.stop()
    })

    val loopThread = Thread({ eventLoop.start() }, "event-loop")
    loopThread.isDaemon = true
    loopThread.start()

    logger.info("Type '$SERVER_SAVE_COMMAND' to save, 'exit' to stop")
    val stdin = System.`in`.bufferedReader()
    while (true) {
        val line = stdin.readLine()?.trim() ?: break
        when (line.lowercase()) {
            SERVER_SAVE_COMMAND -> {
                saveCollection(fileManager, collectionManager, filePath)
                logger.info("Collection saved manually")
            }
            "exit" -> {
                logger.info("Server exit command received")
                saveCollection(fileManager, collectionManager, filePath)
                eventLoop.stop()
                break
            }
            else -> logger.warn("Unknown server command: $line")
        }
    }
}

/**
 * Saves the collection to [filePath] via [fileManager].
 *
 * @param fileManager the XML file manager
 * @param collectionManager the collection to persist
 * @param filePath the target file path (used only for logging)
 */
private fun saveCollection(
    fileManager: XmlFileManager,
    collectionManager: CollectionManager,
    filePath: String
) {
    try {
        fileManager.save(collectionManager.products)
        logger.info("Collection saved to $filePath (${collectionManager.products.size} elements)")
    } catch (e: Exception) {
        logger.error("Failed to save collection: ${e.message}")
    }
}
