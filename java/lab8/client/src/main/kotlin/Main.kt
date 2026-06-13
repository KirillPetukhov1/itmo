import connection.TcpClient
import i18n.BundleKeys
import i18n.LocaleManager
import javafx.application.Application
import javafx.stage.Stage
import net.ServerGateway
import session.AppSession
import view.AuthView
import view.MainView
import viewmodel.AuthViewModel
import viewmodel.MainViewModel

private const val DEFAULT_HOST = "localhost"
private const val DEFAULT_PORT = 8080
private const val ENV_HOST = "LAB6_HOST"
private const val ENV_PORT = "LAB6_PORT"

/**
 * JavaFX application entry point for the graphical client.
 *
 * Reads the server host and port from the environment variables [ENV_HOST] and [ENV_PORT],
 * falling back to [DEFAULT_HOST] and [DEFAULT_PORT] when they are absent.
 * Constructs the shared [AppSession], [TcpClient], and [ServerGateway] singletons and
 * passes them through the view hierarchy so every screen shares the same connection and
 * session state.
 *
 * Application flow:
 * 1. [start] shows [AuthView] on the primary stage.
 * 2. After successful login [AuthView] calls [showMainWindow].
 * 3. [showMainWindow] replaces the auth scene with the main application scene.
 */
class MainApp : Application() {

    private lateinit var session: AppSession
    private lateinit var gateway: ServerGateway

    override fun start(primaryStage: Stage) {
        val host = System.getenv(ENV_HOST) ?: DEFAULT_HOST
        val port = System.getenv(ENV_PORT)?.toIntOrNull() ?: DEFAULT_PORT

        session = AppSession()
        val tcpClient = TcpClient(host, port)
        gateway = ServerGateway(tcpClient, session)

        showAuthWindow(primaryStage)
    }

    /**
     * Shows the authentication screen on [stage].
     * Called once at startup and again after the user logs out.
     *
     * @param stage the primary stage
     */
    private fun showAuthWindow(stage: Stage) {
        val authViewModel = AuthViewModel(gateway, session)
        AuthView(stage, authViewModel) {
            showMainWindow(stage)
        }.show()
    }

    /**
     * Replaces the authentication scene with the main application scene.
     * Called by [AuthView] after the user logs in successfully.
     *
     * @param stage the primary stage that was showing the auth screen
     */
    private fun showMainWindow(stage: Stage) {
        val mainViewModel = MainViewModel(gateway, session)
        MainView(stage, mainViewModel) {
            mainViewModel.stopPolling()
            session.clear()
            showAuthWindow(stage)
        }.show()
    }
}

/**
 * Kotlin entry point. Delegates to [Application.launch] which bootstraps the JavaFX
 * runtime and calls [MainApp.start] on the JavaFX Application Thread.
 */
fun main() {
    Application.launch(MainApp::class.java)
}