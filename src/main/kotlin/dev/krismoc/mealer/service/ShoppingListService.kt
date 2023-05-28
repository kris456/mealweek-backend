package dev.krismoc.mealer.service

import org.springframework.stereotype.Service
import dev.krismoc.mealer.repository.ShoppingItem
import dev.krismoc.mealer.repository.ShoppingItemRepository
import dev.krismoc.mealer.repository.ShoppingList
import dev.krismoc.mealer.repository.ShoppingListRepository

@Service
class ShoppingListService(
    private val shoppingListRepository: ShoppingListRepository,
    private val shoppingItemRepository: ShoppingItemRepository
) {
    fun getShoppingListByYearAndWeek(year: Int, weekNumber: Int): List<ShoppingList> {
        return shoppingListRepository.findByWeeknumberAndYear(weekNumber, year)
    }

    fun getShoppingListByYear(year: Int): List<ShoppingList> {
        return shoppingListRepository.findByYear(year)
    }

    fun getAllShoppingLists(): List<ShoppingList> {
        return shoppingListRepository.findAll().toList()
    }

    fun createNewShoppingList(weekNumber: Int, year: Int): ShoppingList {
        val newShoppinglist = ShoppingList(
            weeknumber = weekNumber,
            year = year
        )
        return shoppingListRepository.save(newShoppinglist)
    }

    fun deleteShoppingList(id: Int) {
        return shoppingListRepository.deleteById(id)
    }

    fun addNewItemToShoppingList(name: String, listId: Int): ShoppingList {
        // TODO: error handling if not existing shoppingList
        val existingItem = shoppingItemRepository.findByName(name)
        if (existingItem != null) return shoppingListRepository.findById(listId).orElseThrow()

        val newItem = ShoppingItem(
            name = name,
            shoppingListId = listId
        )
        shoppingItemRepository.save(newItem)
        return shoppingListRepository.findById(listId).orElseThrow()
    }

    fun getAllItemsInShoppinglist(listId: Int) {
    }
}
