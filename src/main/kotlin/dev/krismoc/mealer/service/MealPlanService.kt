package dev.krismoc.mealer.service

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import dev.krismoc.mealer.repository.Meal
import dev.krismoc.mealer.repository.MealPlan
import dev.krismoc.mealer.repository.MealPlanRepository
import dev.krismoc.mealer.repository.MealRepository


@Service
class MealPlanService(
    private val mealPlanRepository: MealPlanRepository,
    private val mealRepository: MealRepository,
    private val userService: UserService
) {

    fun getAllMealPlans(filter: MealplanFilter): List<MealPlanWithMealsDto> {
        val userId = userService.currentUser().id!!
        val plans = when(filter) {
            is AllFilter -> mealPlanRepository.findAllByCreatedBy(userId)
            is ByWeekAndYear -> mealPlanRepository.findByWeeknumberAndYearAndCreatedBy(filter.weekNumber, filter.year, userId)
            is ByYear -> mealPlanRepository.findByYearAndCreatedBy(filter.year, userId)
            is ByWeek -> mealPlanRepository.findByWeeknumberAndCreatedBy(filter.weekNumber, userId)
        }

        return plans.map { it.toDto()}
    }

    fun newMealPlan(weekNumber: Int, year: Int): MealPlanWithMealsDto {
        val newPLan = MealPlan(weeknumber = weekNumber, year = year)
        return mealPlanRepository.save(newPLan).toDto()
    }

    fun addNewMeal(mealPlanId: Int, name: String, weekDayIso: Int): MealPlanWithMealsDto {
        val mealPlan = mealPlanRepository.findByIdOrNull(mealPlanId)
        requireNotNull(mealPlan)

        val createdMeal = runCatching {
            val meal = Meal(name = name)
            mealRepository.save(meal)
        }.getOrElseReturnIfDuplicate {
            mealRepository.findByName(name)
        }

        mealPlan.addMeal(createdMeal, weekDayIso)
        mealPlanRepository.save(mealPlan)

        return mealPlan.toDto()
    }

    fun MealPlan.toDto(): MealPlanWithMealsDto {
        val mealsDto = meals.map {
            val meal = mealRepository.findById(it.meal).get()
            MealDto(meal.id!!, meal.name, it.weekdayIso)
        }

        return MealPlanWithMealsDto(
            id = id!!,
            weekNumber = weeknumber,
            year = year,
            meals = mealsDto
        )
    }
}

data class MealDto(
    val id: Int,
    val name: String,
    val weekDayIso: Int
)

data class MealPlanWithMealsDto(
    val id: Int,
    val weekNumber: Int,
    val year: Int,
    val meals: List<MealDto>
)

fun <T> Result<T>.getOrElseReturnIfDuplicate(fn: () -> T): T =
    getOrElse {
        when (it.cause) {
            is DuplicateKeyException -> fn()
            else -> throw it
        }
    }

sealed class MealplanFilter

object AllFilter : MealplanFilter()
data class ByWeek(val weekNumber: Int): MealplanFilter()
data class ByYear(val year: Int) : MealplanFilter()
data class ByWeekAndYear(val weekNumber: Int, val year: Int): MealplanFilter()
