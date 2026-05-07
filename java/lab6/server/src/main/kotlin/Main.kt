import collection.CollectionManager
import commands.CommandExecutor
import commands.CommandRegistry
import connection.NetworkServer
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
 * Loads the collection from XML, starts the non-blocking TCP server, and registers
 * a shutdown hook that saves the collection before exit.
 *
 * The special console command [SERVER_SAVE_COMMAND] (typed directly in the server terminal)
 * triggers an immediate save without stopping the server.
 */
fun main() {
    logger.info("Server starting")

    val filePath = System.getenv(ENV_FILE) ?: System.getProperty(ENV_FILE)
        ?: run {
            logger.error("Environment variable $ENV_FILE is not set")
            return
        }

    val port = (System.getenv(ENV_PORT) ?: System.getProperty(ENV_PORT))?.toIntOrNull() ?: DEFAULT_PORT

    val idManager = IdManager()
    val collectionManager = CollectionManager(idManager)
    val fileManager = XmlFileManager(filePath)

    try {
        val loaded = fileManager.load()
        collectionManager.load(loaded)
        logger.info("Collection loaded: ${loaded.size} elements from $filePath")
    } catch (e: Exception) {
        logger.warn("Could not load collection: ${e.message}")
    }

    val registry = CommandRegistry(collectionManager)
    val executor = CommandExecutor(registry)
    val server = NetworkServer(port, executor)

    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info("Shutdown hook triggered — saving collection")
        saveCollection(fileManager, collectionManager, filePath)
        server.stop()
    })

    val serverThread = Thread({
        try {
            server.start()
        } catch (e: Exception) {
            logger.error("Server error: ${e.message}")
        }
    }, "server-thread")
    serverThread.isDaemon = true
    serverThread.start()

    logger.info("Type '$SERVER_SAVE_COMMAND' to save the collection manually, 'exit' to stop the server")

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
                server.stop()
                break
            }
            else -> logger.warn("Unknown server command: $line")
        }
    }
}

/**
 * Saves the collection to [filePath] via [fileManager].
 * Logs success or any error encountered during save.
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
