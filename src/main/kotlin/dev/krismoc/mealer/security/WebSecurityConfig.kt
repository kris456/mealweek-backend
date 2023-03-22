package dev.krismoc.mealer.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(val filter: JWTFilter, val authenticationProvider: DaoAuthenticationProvider) {
    @Bean
    fun configure(http: HttpSecurity): SecurityFilterChain {
        http.cors().and()
            .csrf().disable()
            //.csrf().ignoringAntMatchers("/api/auth/**").and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .formLogin().disable()
            .httpBasic().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/auth/**").permitAll()
            .requestMatchers("/api/user/**").hasRole("USER")
            .anyRequest().authenticated()

        http.authenticationProvider(authenticationProvider)

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
