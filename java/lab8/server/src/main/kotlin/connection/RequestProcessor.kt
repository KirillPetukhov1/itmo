package connection

import auth.AuthService
import commands.CommandExecutor
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

private val AUTH_EXEMPT_COMMANDS = setOf("register")

/**
 * Module 3 -- Command processing.
 *
 * Deserializes a raw JSON string into a [Request], validates credentials for all commands
 * not listed in [AUTH_EXEMPT_COMMANDS], delegates execution to [CommandExecutor],
 * and returns the resulting [Response].
 *
 * @property executor the command executor that resolves and runs server commands
 * @property authService the service used to validate user credentials
 */
class RequestProcessor(
    private val executor: CommandExecutor,
    private val authService: AuthService
) {
    private val logger = LoggerFactory.getLogger(RequestProcessor::class.java)
    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Processes one raw [requestJson] string and returns the [Response].
     *
     * @param requestJson the raw JSON string received from the client
     * @return the [Response] to send back to the client
     */
    fun process(requestJson: String): Response {
        return try {
            val request = json.decodeFromString<Request>(requestJson)
            logger.info("Processing command: ${request.commandName} for user: ${request.login}")
            if (request.commandName !in AUTH_EXEMPT_COMMANDS) {
                if (!authService.validate(request.login, request.password)) {
                    logger.warn("Authentication failed for login: ${request.login}")
                    return Response(request.commandName, "{}", "Authentication failed: invalid credentials")
                }
            }
            executor.execute(request)
        } catch (e: Exception) {
            logger.error("Failed to process request: ${e.message}")
            Response("unknown", "{}", "Malformed request: ${e.message}")
        }
    }
}