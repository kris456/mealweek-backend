package dev.krismoc.mealer.repository

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class UserIdProvider(val userRepository: UserRepository) : AuditorAware<Int> {
    override fun getCurrentAuditor(): Optional<Int> {
        val principal = SecurityContextHolder.getContext().authentication.principal
        val id = when(principal) {
            is Jwt -> {
                val email = principal.getClaimAsString("email")
                userRepository.findByEmail(email)?.id!!
            }

            else -> {0}
        }

        return Optional.ofNullable(id)
    }
}
