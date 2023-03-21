package dev.krismoc.mealer.utils

object Validator {
    private const val MINIMUM_PASSWORD_LENGTH = 8
    private val emailRegex = Regex("^[a-zA-Z0-9.!#\$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)+\$")
    fun isEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    // TODO: this is a very basic password check. Need to include common password requirements
    fun isValidPassword(password: String): Boolean {
        return password.length >= MINIMUM_PASSWORD_LENGTH
    }
}
