import auth.AuthService
import collection.CollectionManager
import commands.CommandExecutor
import commands.CommandRegistry
import connection.MultiThreadedServer
import connection.RequestProcessor
import database.DatabaseManager
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ServerMain")
private const val DEFAULT_PORT = 8080
private const val ENV_PORT = "LAB6_PORT"
private const val ENV_DB_USER = "DB_USER"
private const val ENV_DB_PASSWORD = "DB_PASSWORD"
private const val DB_URL = "jdbc:postgresql://pg/studs"

/**
 * Application entry point for the server module.
 *
 * Reads the TCP port from the [ENV_PORT] environment variable (default [DEFAULT_PORT]).
 * Reads database credentials from [ENV_DB_USER] and [ENV_DB_PASSWORD] environment variables.
 * Initializes the database schema, loads the in-memory collection, starts the
 * [MultiThreadedServer] in a daemon thread, and enters the admin console loop.
 */
fun main() {
    logger.info("Server starting")

    val port = (System.getenv(ENV_PORT) ?: System.getProperty(ENV_PORT))
        ?.toIntOrNull() ?: DEFAULT_PORT

    val dbUser = System.getenv(ENV_DB_USER) ?: System.getProperty(ENV_DB_USER)
    ?: run { logger.error("Environment variable $ENV_DB_USER is not set"); return }
    val dbPassword = System.getenv(ENV_DB_PASSWORD) ?: System.getProperty(ENV_DB_PASSWORD)
    ?: run { logger.error("Environment variable $ENV_DB_PASSWORD is not set"); return }

    val databaseManager = try {
        DatabaseManager(DB_URL, dbUser, dbPassword).also { it.initSchema() }
    } catch (e: Exception) {
        logger.error("Failed to connect to database: ${e.message}")
        return
    }

    val collectionManager = CollectionManager(databaseManager)

    try {
        val (loadedProducts, loadedCreators) = databaseManager.loadProducts()
        collectionManager.load(loadedProducts, loadedCreators)
        logger.info("Collection loaded: ${loadedProducts.size} elements")
    } catch (e: Exception) {
        logger.warn("Could not load collection from database: ${e.message}")
    }

    val authService = AuthService(databaseManager)
    val registry = CommandRegistry(collectionManager, databaseManager, authService)
    val executor = CommandExecutor(registry)
    val processor = RequestProcessor(executor, authService)
    val server = MultiThreadedServer(port, processor)

    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info("Shutdown hook triggered")
        server.stop()
    })

    val serverThread = Thread({ server.start() }, "server-main")
    serverThread.isDaemon = true
    serverThread.start()

    logger.info("Type 'exit' to stop the server")
    val stdin = System.`in`.bufferedReader()
    while (true) {
        val line = stdin.readLine()?.trim() ?: break
        if (line.lowercase() == "exit") {
            logger.info("Server exit command received")
            server.stop()
            break
        } else {
            logger.warn("Unknown server command: $line")
        }
    }
}