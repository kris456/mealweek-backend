package dev.krismoc.mealer.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.proc.SecurityContext
import dev.krismoc.mealer.security.HMAC256Algorithm
import dev.krismoc.mealer.security.WebSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.crypto.spec.SecretKeySpec

@RestController
class TestController {
    @GetMapping("/security-test")
    fun getAndTestSecurity(): String {
        return "Ok"
    }
}


class SecurityTest: AbstractAuthenticatedControllerTest() {
    val otherSecret: String = "someothersecretthatisnotsosecret1234567890"
    val otherJwtEncoder  : NimbusJwtEncoder
        get()  {
            val secret = SecretKeySpec(otherSecret.toByteArray(), HMAC256Algorithm)
            val immutable = ImmutableSecret<SecurityContext>(secret)
            return NimbusJwtEncoder(immutable)
        }

    @Autowired
    lateinit var securityConfig: WebSecurityConfig

    @Test
    fun `Should return unauthorized when jwt is not signed with our secret`() {
        val decodedToken = securityConfig.jwtDecoder()?.decode(token)!!

        val jwtClaims = JwtClaimsSet.builder().claims { it.putAll(decodedToken.claims)}.build()
        val headers = JwsHeader.with(MacAlgorithm.HS256).build()
        val invalidToken = otherJwtEncoder.encode(JwtEncoderParameters.from(headers,jwtClaims)).tokenValue
        mockMvc.get("/security-test") {
            headers{
                setBearerAuth(invalidToken)
            }
        }.andExpect {
            this.status { isUnauthorized() }
        }
    }

    @Test
    fun `Should get unauthorized when bearer token is incorrect`() {
        mockMvc.post("/security-test") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(NewMealRequest("NoeHeltAnnet"))

            headers {
                setBearerAuth("notcorrect")
            }

        }.andExpect {
            status { isUnauthorized() }
        }
    }
}