package dev.krismoc.mealer.service

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import dev.krismoc.mealer.repository.Meal
import dev.krismoc.mealer.repository.MealPlan
import dev.krismoc.mealer.repository.MealPlanRepository
import dev.krismoc.mealer.repository.MealRepository

fun <T> Result<T>.getOrElseReturnIfDuplicate(fn: () -> T): T =
    getOrElse {
        when (it.cause) {
            is DuplicateKeyException -> fn()
            else -> throw it
        }
    }

@Service
class MealPlanService(
    private val mealPlanRepository: MealPlanRepository,
    private val mealRepository: MealRepository
) {

    fun getAllMealPlans(weekNumber: Int, year: Int): MealPlanWithMealsDto {
        val mealPlan = mealPlanRepository.findByWeekNumberAndYear(weekNumber, year)

        return mealPlan.toDto()
    }

    fun newMealPlan(weekNumber: Int, year: Int): MealPlanWithMealsDto {
        val newPLan = MealPlan(weekNumber = weekNumber, year = year, mealRefs = mutableSetOf())
        return mealPlanRepository.save(newPLan).toDto()
    }

    fun getAllMeals(): List<Meal> {
        return mealRepository.findAll().toList()
    }

    fun addNewMeal(mealPlanId: Int, name: String): MealPlanWithMealsDto {
        // TODO: what if it is not found?
        val mealPlan = mealPlanRepository.findByIdOrNull(mealPlanId)
        requireNotNull(mealPlan)

        val createdMeal = runCatching {
            val meal = Meal(name = name)
            mealRepository.save(meal)
        }.getOrElseReturnIfDuplicate {
            mealRepository.findByName(name)
        }

        mealPlan.addMeal(createdMeal)
        mealPlanRepository.save(mealPlan)

        return mealPlan.toDto()
    }

    private fun MealPlan.meals(): List<Meal>? {
        return this.mealRefs.map {
            mealRepository.findByIdOrNull(it.mealId) ?: return null
        }
    }

    fun MealPlan.toDto(): MealPlanWithMealsDto {
        val meals = this.meals() ?: throw IllegalStateException("Bad mealrefs")

        return MealPlanWithMealsDto(
            id!!,
            weekNumber,
            year,
            meals
        )
    }
}

data class MealPlanWithMealsDto(
    val id: Int,
    val weekNumber: Int,
    val year: Int,
    val meals: List<Meal>
)
