package dev.krismoc.mealer.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import dev.krismoc.mealer.repository.Meal
import dev.krismoc.mealer.repository.MealRepository
import dev.krismoc.mealer.repository.UserRepository
import org.springframework.security.access.AccessDeniedException

@Service
class UserMealService(val mealRepository: MealRepository, val userService: UserService) {
    fun createNewMeal(name: String): Meal {
        val meal = Meal(name = name)
        return mealRepository.save(meal)
    }

    fun updateMeal(id: Int, newName: String): Meal? {
        val meal = mealRepository.findByIdOrNull(id) ?: return null
        val userId = userService.currentUser().id
        if (meal.createdBy != userId) {
            throw AccessDeniedException("Not allowed to update meal")
        }

        val updatedMeal = meal.copy(name = newName)

        return mealRepository.save(updatedMeal)
    }

    fun getAllMeals(): List<Meal> {
        val userId = userService.currentUser().id!!
        return mealRepository.findAllByCreatedBy(userId).toList()
    }

    fun getMealById(id: Int): Meal? {
        val meal = mealRepository.findByIdOrNull(id)
        val userId = userService.currentUser().id!!

        return meal?.takeIf { it.createdBy == userId }
    }

    fun deleteMeal(id: Int) {
        val meal = getMealById(id)

        if(meal != null) {
            mealRepository.deleteById(id)
        }
    }
}