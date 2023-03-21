package dev.krismoc.mealer.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import dev.krismoc.mealer.repository.Meal
import dev.krismoc.mealer.repository.MealRepository

@Service
class MealService(val mealRepository: MealRepository) {
    fun createNewMeal(name: String): Meal {
        val meal = Meal(name = name)
        return mealRepository.save(meal)
    }

    fun updateMeal(id: Int, newName: String): Meal? {
        val meal = mealRepository.findByIdOrNull(id) ?: return null
        val updatedMeal = meal.copy(name = newName)

        return mealRepository.save(updatedMeal)
    }

    fun getAllMeals(): List<Meal> {
        return mealRepository.findAll().toList()
    }

    fun getMealById(id: Int): Meal? {
        return mealRepository.findByIdOrNull(id)
    }

    fun deleteMeal(id: Int) {
        mealRepository.deleteById(id)
    }
}
