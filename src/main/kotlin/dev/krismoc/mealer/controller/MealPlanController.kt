package dev.krismoc.mealer.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import dev.krismoc.mealer.service.MealPlanService
import dev.krismoc.mealer.service.MealPlanWithMealsDto

@RestController
@RequestMapping("/api/v1/mealplan")
class MealPlanController(val mealPlanService: MealPlanService) {

    @GetMapping
    fun getAllMealPlans(
        @RequestParam(required = false) weekNumber: Int?,
        @RequestParam(required = false) year: Int?
    ): List<MealPlanWithMealsDto> {
        if (weekNumber != null && year != null) {
            return listOf(mealPlanService.getAllMealPlans(weekNumber, year))
        }
        TODO()
    }

    @PostMapping
    fun newMealPlan(
        weekNumber: Int,
        year: Int
    ): MealPlanWithMealsDto {
        return mealPlanService.newMealPlan(weekNumber, year)
    }

    @PostMapping("/{id}/meals")
    fun addNewMealToPlan(
        @PathVariable id: Int,
        @RequestBody newMealToPlanRequest: NewMealToPlanRequest
    ): MealPlanWithMealsDto {
        return mealPlanService.addNewMeal(id, newMealToPlanRequest.name)
    }
}

data class NewMealToPlanRequest(
    val name: String
)
