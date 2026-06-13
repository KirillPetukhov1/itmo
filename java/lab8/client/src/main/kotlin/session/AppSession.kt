package session

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty

/**
 * Holds the authenticated-user state for the current GUI session.
 *
 * All three state variables are exposed both as plain Kotlin properties and as JavaFX
 * observable properties so views can bind to them without additional listeners.
 * Mutation of any Kotlin property automatically updates the backing observable property
 * and vice versa.
 *
 * Thread safety: property mutations must happen on the JavaFX Application Thread when
 * they trigger bound UI updates. [ServerGateway] methods populate a local result on a
 * background thread and then delegate session updates to the Application Thread via
 * [javafx.application.Platform.runLater].
 *
 * @property currentLoginProperty observable login of the authenticated user; empty string when not authenticated
 * @property currentPasswordProperty observable password of the authenticated user; never displayed
 * @property isAuthenticatedProperty observable flag that is true after a successful login or registration
 */
class AppSession {

    /** Observable login of the currently authenticated user. */
    val currentLoginProperty = SimpleStringProperty("")

    /**
     * The current user login.
     * Setting this value also updates [currentLoginProperty].
     */
    var currentLogin: String
        get() = currentLoginProperty.get()
        set(value) = currentLoginProperty.set(value)

    /** Observable password of the currently authenticated user. */
    val currentPasswordProperty = SimpleStringProperty("")

    /**
     * The current user password.
     * Setting this value also updates [currentPasswordProperty].
     */
    var currentPassword: String
        get() = currentPasswordProperty.get()
        set(value) = currentPasswordProperty.set(value)

    /** Observable authentication flag. */
    val isAuthenticatedProperty = SimpleBooleanProperty(false)

    /**
     * True when the user has successfully authenticated in the current session.
     * Setting this value also updates [isAuthenticatedProperty].
     */
    var isAuthenticated: Boolean
        get() = isAuthenticatedProperty.get()
        set(value) = isAuthenticatedProperty.set(value)

    /**
     * Stores the credentials obtained after a successful authentication.
     * Marks the session as authenticated.
     *
     * @param login the authenticated user login
     * @param password the plain-text password used for subsequent requests
     */
    fun authenticate(login: String, password: String) {
        currentLogin = login
        currentPassword = password
        isAuthenticated = true
    }

    /**
     * Clears all session state, effectively logging the user out.
     * Resets [isAuthenticated] to false and clears both credential fields.
     */
    fun clear() {
        currentLogin = ""
        currentPassword = ""
        isAuthenticated = false
    }
}