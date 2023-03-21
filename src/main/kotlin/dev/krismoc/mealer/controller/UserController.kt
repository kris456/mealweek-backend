package dev.krismoc.mealer.controller

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import dev.krismoc.mealer.service.UserDto
import dev.krismoc.mealer.service.UserService

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    val userService: UserService
) {

    @GetMapping("/info")
    fun getUserInfo(): UserDto? {
        val email = SecurityContextHolder.getContext().authentication.principal as String
        return userService.getUser(email)
    }
}
