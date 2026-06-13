package viewmodel

import i18n.BundleKeys
import i18n.LocaleManager
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import net.GatewayResult
import net.ServerGateway
import session.AppSession

/**
 * ViewModel for the authentication and registration screen.
 *
 * Holds all observable state consumed by [view.AuthView] and exposes [submit] as the
 * single action that drives both login and registration depending on [mode].
 * Network calls are dispatched on a daemon background thread; all result handling runs
 * on the JavaFX Application Thread via [Platform.runLater].
 *
 * @property gateway the server facade used for login and register requests
 * @property session the shared session updated with credentials on successful login
 */
class AuthViewModel(
    private val gateway: ServerGateway,
    private val session: AppSession
) {

    /**
     * Distinguishes the two operating modes of the auth screen.
     */
    enum class Mode { LOGIN, REGISTER }

    /** The currently displayed form mode. */
    val mode = SimpleObjectProperty(Mode.LOGIN)

    /** Text entered in the login field. */
    val loginText = SimpleStringProperty("")

    /** Text entered in the password field. */
    val passwordText = SimpleStringProperty("")

    /** Non-empty when a server or validation error should be shown in red. */
    val errorText = SimpleStringProperty("")

    /**
     * Non-empty after a successful registration.
     * The view binds this to a green info label and clears it when [mode] changes.
     */
    val successText = SimpleStringProperty("")

    /** True while a network request is in flight. Bound to control disable states. */
    val isLoading = SimpleBooleanProperty(false)

    /**
     * Switches between [Mode.LOGIN] and [Mode.REGISTER], clearing both message labels
     * and resetting the password field.
     */
    fun toggleMode() {
        mode.set(if (mode.get() == Mode.LOGIN) Mode.REGISTER else Mode.LOGIN)
        errorText.set("")
        successText.set("")
        passwordText.set("")
    }

    /**
     * Validates the current field values and submits the request to the server.
     *
     * In [Mode.LOGIN] mode a successful response stores the credentials in [session] and
     * invokes [onLoginSuccess]. In [Mode.REGISTER] mode a successful response switches
     * the screen back to [Mode.LOGIN] and populates [successText] with a confirmation
     * message so the user knows to log in.
     *
     * On validation failure [errorText] is set immediately without a network call.
     * On server failure [errorText] is set after the response arrives.
     *
     * @param onLoginSuccess called on the JavaFX Application Thread after a successful login
     */
    fun submit(onLoginSuccess: () -> Unit) {
        val login = loginText.get().trim()
        val password = passwordText.get().trim()

        if (login.isBlank() || password.isBlank()) {
            errorText.set(LocaleManager[BundleKeys.AUTH_ERROR_BLANK])
            return
        }

        val currentMode = mode.get()
        isLoading.set(true)
        errorText.set("")
        successText.set("")

        Thread {
            val result = when (currentMode) {
                Mode.LOGIN -> gateway.login(login, password)
                Mode.REGISTER -> gateway.register(login, password)
            }

            Platform.runLater {
                isLoading.set(false)
                when (result) {
                    is GatewayResult.Success -> handleSuccess(currentMode, login, password, onLoginSuccess)
                    is GatewayResult.Failure -> errorText.set(result.message)
                }
            }
        }.apply { isDaemon = true }.start()
    }

    private fun handleSuccess(
        submittedMode: Mode,
        login: String,
        password: String,
        onLoginSuccess: () -> Unit
    ) {
        when (submittedMode) {
            Mode.LOGIN -> {
                session.authenticate(login, password)
                onLoginSuccess()
            }
            Mode.REGISTER -> {
                successText.set(LocaleManager[BundleKeys.MSG_REGISTER_OK])
                loginText.set("")
                passwordText.set("")
                mode.set(Mode.LOGIN)
            }
        }
    }
}