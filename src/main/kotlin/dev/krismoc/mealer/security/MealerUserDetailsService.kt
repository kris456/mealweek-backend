package dev.krismoc.mealer.security

import dev.krismoc.mealer.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class MealerUserDetailsService(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username) ?: throw UsernameNotFoundException("Could not find user with email=$username")
        return User(
            user.email,
            user.password,
            listOf(
                SimpleGrantedAuthority("ROLE_USER")
            )
        )
    }
}
