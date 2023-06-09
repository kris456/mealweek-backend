package dev.krismoc.mealer.controller

import java.time.LocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import dev.krismoc.mealer.security.JWTUtil
import dev.krismoc.mealer.service.UserDto
import dev.krismoc.mealer.service.UserService
import dev.krismoc.mealer.utils.Validator

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(val userService: UserService, val jwtUtil: JWTUtil, val authManager: AuthenticationManager) {
    // TODO: should have proper error handling. How will frontend handle the various validation errors?
    // Should we have a dedicated response object without statuscodes or return an errors response with statuscode?w
    @PostMapping("/register")
    fun registerUser(@RequestBody registrationPayload: UserCredentialsPayload): ResponseEntity<UserResource> {
        val isValidEmail = Validator.isEmail(registrationPayload.email)
        if (!isValidEmail) return ResponseEntity.badRequest().build()

        val isValidPassword = Validator.isValidPassword(registrationPayload.password)
        if (!isValidPassword) return ResponseEntity.badRequest().build()

        val existing = userService.getUser(registrationPayload.email)

        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .build()
        }

        val token = jwtUtil.generateToken(registrationPayload.email)

        return userService.newUser(registrationPayload.email, registrationPayload.password)
            .toResource(token)
            .okResponse()
    }

    @PostMapping("/login")
    fun login(@RequestBody userCredentialsPayload: UserCredentialsPayload): ResponseEntity<UserResource> {
        val auth = UsernamePasswordAuthenticationToken(userCredentialsPayload.email, userCredentialsPayload.password)
        runCatching {
            authManager.authenticate(auth)
        }.onFailure {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        val token = jwtUtil.generateToken(userCredentialsPayload.email)

        return UserResource(userCredentialsPayload.email, token)
            .okResponse()
    }

    fun UserDto.toResource(token: String): UserResource {
        return UserResource(email, token)
    }
}

data class UserCredentialsPayload(
    val email: String,
    val password: String
)

data class UserResource(
    val email: String,
    val token: String
) : Resource

fun <T : Resource> T.okResponse(): ResponseEntity<T> {
    return ResponseEntity.ok(this)
}

interface Resource
