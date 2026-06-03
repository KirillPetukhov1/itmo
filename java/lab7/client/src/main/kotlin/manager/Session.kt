package manager

class Session(
    var currentLogin: String = "",
    var currentPassword: String = "",
    var isAuthenticated: Boolean = false
)