package dev.krismoc.mealer.service

import java.time.LocalDateTime
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import dev.krismoc.mealer.repository.UserInfo
import dev.krismoc.mealer.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.jwt.Jwt

@Service
class UserService(val userRepository: UserRepository, val passwordEncoder: PasswordEncoder) {
    fun currentUser(): UserInfo {
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        val email = jwt.getClaimAsString("email")
        return userRepository.findByEmail(email) ?: throw UsernameNotFoundException("Could not find user")
    }
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
