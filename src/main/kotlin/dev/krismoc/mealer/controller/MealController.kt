package dev.krismoc.mealer.controller

import java.time.LocalDateTime
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import dev.krismoc.mealer.repository.Meal
import dev.krismoc.mealer.service.MealService

@RestController
@RequestMapping("/api/v1/meals")
class MealController(
    private val mealService: MealService
) {
    @GetMapping
    fun getAllMeals(
        @RequestParam(required = false) weekNumber: Int?,
        @RequestParam(required = false) onlyHistorical: Boolean?
    ): List<MealResource> {
        return mealService.getAllMeals().map { it.toResource() }
    }

    @GetMapping("/{id}")
    fun getMealById(@PathVariable id: Int): ResponseEntity<MealResource> {
        return mealService.getMealById(id)?.toResource()?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewMeal(@RequestBody newMealRequest: NewMealRequest): MealResource {
        return mealService.createNewMeal(newMealRequest.name).toResource()
    }

    @PutMapping("/{id}")
    fun updateMeal(@PathVariable id: Int, @RequestBody updateMealRequest: UpdateMealRequest): ResponseEntity<MealResource> {
        val meal = mealService.updateMeal(id, updateMealRequest.name)
        return meal?.let {
            ResponseEntity.ok(it.toResource())
        } ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteMeal(@PathVariable id: Int) {
        mealService.deleteMeal(id)
    }
}

data class UpdateMealRequest(
    val name: String
)

data class NewMealRequest(
    val name: String
)

data class MealResource(
    val id: Int,
    val name: String,

    val createdDate: LocalDateTime?,
    val modifiedDate: LocalDateTime?,
    val createdBy: Int?,
    val modifiedBy: Int?
)

private fun Meal.toResource(): MealResource {
    return MealResource(
        id!!,
        name,
        createdDate,
        modifiedDate,
        createdBy,
        modifiedBy
    )
}
