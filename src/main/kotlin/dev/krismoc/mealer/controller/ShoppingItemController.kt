package dev.krismoc.mealer.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/items")
class ShoppingItemController {
    @GetMapping
    fun getAllItems() {
    }
}
