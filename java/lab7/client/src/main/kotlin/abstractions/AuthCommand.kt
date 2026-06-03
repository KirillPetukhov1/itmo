package abstractions

abstract class AuthCommand(private val readerWriter: AbstractReaderWriter) : Command() {
    override val isShouldBeSent = true

    /** The login entered by the user during [start]. */
    var login: String = ""
        private set

    /** The password entered by the user during [start]. */
    var password: String = ""
        private set

    var success: Boolean = false

    override fun start(args: Array<String>) {
        readerWriter.writeRequest("Enter login:")
        login = readerWriter.readLine()?.trim() ?: throw EofException()
        require(login.isNotBlank()) { "Login must not be blank" }

        readerWriter.writeRequest("Enter password:")
        password = readerWriter.readLine()?.trim() ?: throw EofException()
        require(password.isNotBlank()) { "Password must not be blank" }
    }

    /**
     * Returns true if the server accepted the registration.
     *
     * @return true on success
     */
    fun isSuccess(): Boolean = success
}