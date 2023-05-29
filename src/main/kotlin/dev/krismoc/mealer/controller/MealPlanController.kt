package dev.krismoc.mealer.controller

import dev.krismoc.mealer.service.AllFilter
import dev.krismoc.mealer.service.ByWeek
import dev.krismoc.mealer.service.ByWeekAndYear
import dev.krismoc.mealer.service.ByYear
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import dev.krismoc.mealer.service.MealPlanService
import dev.krismoc.mealer.service.MealPlanWithMealsDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.IllegalArgumentException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity

@RestController
@RequestMapping("/api/v1/mealplan")
class MealPlanController(val mealPlanService: MealPlanService) {

    @GetMapping
    fun getAllMealPlans(
        @RequestParam(required = false) weekNumber: Int?,
        @RequestParam(required = false) year: Int?
    ): List<MealPlanWithMealsDto> {
        val filter = when{
            weekNumber == null && year == null -> AllFilter
            year != null && weekNumber != null -> ByWeekAndYear(weekNumber, year)
            year != null -> ByYear(year)
            weekNumber != null -> ByWeek(weekNumber)
            else -> throw IllegalArgumentException("Invalid request params")
        }

        return mealPlanService.getAllMealPlans(filter)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun newMealPlan(
        @RequestBody payload: NewMealPlanPayload
    ): MealPlanWithMealsDto {
        return mealPlanService.newMealPlan(payload.weekNumber, payload.year)
    }

    @PostMapping("/{id}/meals")
    @ResponseStatus(HttpStatus.CREATED)
    fun addMealsToPlan(
        @PathVariable id: Int,
        @RequestBody newMealToPlanRequest: NewMealToMealPlanRequest
    ): ResponseEntity<MealPlanWithMealsDto> {
        val newMeal = mealPlanService.addNewMeal(id, newMealToPlanRequest.name, newMealToPlanRequest.weekDayIso)
        return if (newMeal != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(newMeal)
        } else {
            ResponseEntity.notFound().build()
        }

    }
}

data class NewMealToMealPlanRequest(
    val name: String,
    val weekDayIso: Int
)
data class NewMealPlanPayload(
    val weekNumber: Int,
    val year: Int
)
