package dev.krismoc.mealer.controller

import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import dev.krismoc.mealer.repository.ShoppingList
import dev.krismoc.mealer.service.ShoppingListService

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/v1/shopping-list")
class ShoppingListController(val service: ShoppingListService) {
    @PostMapping
    fun createShoppingList(@RequestBody request: CreateShoppingListRequest): ShoppingList {
        return service.createNewShoppingList(request.weekNumber, request.year)
    }

    @GetMapping
    fun getShoppingList(
        @RequestParam year: Int,
        @RequestParam(required = false) weekNumber: Int?
    ): List<ShoppingList> {
        if (weekNumber == null) return service.getShoppingListByYear(year)

        return service.getShoppingListByYearAndWeek(year, weekNumber)
    }

    @DeleteMapping("/{id}")
    fun deleteShoppingList(@PathVariable id: Int) {
        service.deleteShoppingList(id)
    }
}

@RestController
@RequestMapping("/api/v1/shopping-list/{listId}/items")
class ShoppingListItemsController(val service: ShoppingListService) {
    @GetMapping
    fun getAllItems(@PathVariable listId: String) {
    }

    @PostMapping("/")
    fun addNewItem(@RequestBody itemRequest: ItemRequest, @PathVariable listId: Int): ShoppingList {
        return service.addNewItemToShoppingList(itemRequest.name, listId)
    }
}

data class ItemRequest(
    val name: String
)

data class CreateShoppingListRequest(
    val weekNumber: Int,
    val year: Int
)
