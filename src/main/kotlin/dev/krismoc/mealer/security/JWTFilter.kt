package dev.krismoc.mealer.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTFilter(
    val mealerUserDetailsService: MealerUserDetailsService,
    val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (!authHeader.isNullOrEmpty() && authHeader.startsWith("Bearer ")) {
            val jwt = authHeader.substringAfter("Bearer").trim()

            if (jwt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT tok")
            } else {
                kotlin.runCatching {
                    val email = jwtUtil.validateTokenAndReturnSubject(jwt)
                    val userDetails = mealerUserDetailsService.loadUserByUsername(email)

                    val authToken = UsernamePasswordAuthenticationToken(email, userDetails.password, userDetails.authorities)
                    if (SecurityContextHolder.getContext().authentication == null) {
                        SecurityContextHolder.getContext().authentication = authToken
                    }
                }.onFailure {
                    logger.info("Error on auth ex=$it")
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token")
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}
