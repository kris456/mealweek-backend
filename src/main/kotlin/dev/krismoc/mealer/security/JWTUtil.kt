package dev.krismoc.mealer.security

import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

@Component
class JWTUtil(@Value("\${mealer.jwt.secret}") private val jwtSecret: String) {
    fun generateToken(email: String): String =
        JWT.create()
            .withSubject("User details")
            .withClaim("email", email)
            .withIssuedAt(Instant.now())
            .withIssuer("mealer/krismoc")
            .sign(Algorithm.HMAC256(jwtSecret))

    fun validateTokenAndReturnSubject(token: String): String {
        val verifier = JWT.require(Algorithm.HMAC256(jwtSecret))
            .withSubject("User details")
            .withIssuer("mealer/krismoc")
            .build()

        return verifier.verify(token)
            .getClaim("email")
            .asString()
    }
}
