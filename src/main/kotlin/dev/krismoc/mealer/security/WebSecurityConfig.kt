package dev.krismoc.mealer.security

import com.nimbusds.jose.jwk.source.ImmutableSecret
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.security.web.SecurityFilterChain
import javax.crypto.spec.SecretKeySpec

const val HMAC256Algorithm = "HmacSHA256"

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    @Value("\${mealer.jwt.secret}") val jwtSecret: String
) {
    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.cors().and()
            .csrf().disable()
            //.csrf().ignoringAntMatchers("/api/auth/**").and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .formLogin().disable()
            .httpBasic().disable()
            .oauth2ResourceServer { it.jwt() }
            .exceptionHandling {
                it.authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                    .accessDeniedHandler(BearerTokenAccessDeniedHandler())
            }
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/api/user/**").hasRole("USER")
            .anyRequest().authenticated()

        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder? {
        val secret = SecretKeySpec(jwtSecret.toByteArray(), HMAC256Algorithm)
        return NimbusJwtDecoder.withSecretKey(secret).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder? {
        val secret = SecretKeySpec(jwtSecret.toByteArray(), HMAC256Algorithm)
        val immutable = ImmutableSecret<SecurityContext>(secret)
        return NimbusJwtEncoder(immutable)
    }
}
