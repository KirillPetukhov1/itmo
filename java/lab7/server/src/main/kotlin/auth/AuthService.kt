package auth

import database.DatabaseManager
import java.security.MessageDigest

/**
 * Provides user registration and credential validation.
 * Passwords are never stored in plain text; they are hashed with MD2 before persistence.
 *
 * @property databaseManager the database manager used for user persistence
 */
class AuthService(private val databaseManager: DatabaseManager) {

    private fun hashPassword(password: String): String {
        val md = MessageDigest.getInstance("MD2")
        return md.digest(password.toByteArray(Charsets.UTF_8))
            .joinToString("") { "%02x".format(it) }
    }

    /**
     * Registers a new user with [login] and [password].
     * The password is hashed with MD2 before being stored in the database.
     *
     * @param login the desired login, must not be blank
     * @param password the desired password, must not be blank
     * @return true if the user was created, false if the login is already taken or input is blank
     */
    fun register(login: String, password: String): Boolean {
        if (login.isBlank() || password.isBlank()) return false
        if (databaseManager.findUser(login) != null) return false
        databaseManager.createUser(login, hashPassword(password))
        return true
    }

    /**
     * Checks whether [login] and [password] match the stored credentials.
     *
     * @param login the user's login
     * @param password the plain-text password to verify
     * @return true if the credentials are valid, false otherwise
     */
    fun validate(login: String, password: String): Boolean {
        if (login.isBlank()) return false
        val stored = databaseManager.findUser(login) ?: return false
        return stored == hashPassword(password)
    }
}