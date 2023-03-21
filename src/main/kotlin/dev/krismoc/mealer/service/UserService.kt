package dev.krismoc.mealer.service

import java.time.LocalDateTime
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import dev.krismoc.mealer.repository.UserInfo
import dev.krismoc.mealer.repository.UserRepository

@Service
class UserService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) {
    fun newUser(email: String, password: String): UserDto {
        val passwordHashed = passwordEncoder.encode(password)
        val userInfo = UserInfo(email = email, password = passwordHashed)
        return userRepository.save(userInfo).toDto()
    }

    fun getUser(email: String): UserDto? {
        return userRepository.findByEmail(email)?.toDto()
    }

    private fun UserInfo.toDto(): UserDto {
        return UserDto(email, password, createdDate)
    }
}

data class UserDto(
    val email: String,
    val password: String,
    val createdDate: LocalDateTime?
)
