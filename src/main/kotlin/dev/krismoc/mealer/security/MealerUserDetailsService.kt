package dev.krismoc.mealer.security

import dev.krismoc.mealer.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class MealerUserDetailsService(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("Could not find user with email=$username")
        return user.run {
            LocalUserDetails(
                id!!,
                email,
                password,
                listOf(
                    SimpleGrantedAuthority("ROLE_USER")
                )
            )
        }
    }
}

open class LocalUserDetails(
    val id: Int,
    val email: String,
    private val password: String,
    private val authorities: List<GrantedAuthority>,
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities.toMutableList()
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}

