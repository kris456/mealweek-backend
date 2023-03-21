package dev.krismoc.mealer.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListRepository : CrudRepository<ShoppingList, Int> {
    fun findByWeekNumberAndYear(weekNumber: Int, year: Int): List<ShoppingList>
    fun findByYear(year: Int): List<ShoppingList>
}

@Repository
interface ShoppingItemRepository : CrudRepository<ShoppingItem, Int> {
    fun findByName(name: String): ShoppingItem?
}

@Repository
interface MealPlanRepository : CrudRepository<MealPlan, Int> {
    fun findByWeekNumberAndYear(weekNumber: Int, year: Int): MealPlan
}

@Repository
interface MealRepository : CrudRepository<Meal, Int> {
    fun findByName(name: String): Meal
}

@Repository
interface UserRepository : CrudRepository<UserInfo, Int> {
    fun findByEmail(email: String): UserInfo?
}
/*
@Repository
interface MealPlanCombinedMealRepository : CrudRepository<MealPlanCombinedMeal, Int> {
    @Query("SELECT mp.id AS mealPlanId, weekNumber FROM mealPlan_meals mpm INNER JOIN mealPlan mp ON mpm.mealPlanId = mp.id INNER JOIN  meal m ON m.id = mpm.mealId WHERE mp.weekNumber=:weekNumber AND mp.year=:year")
    fun getWholeMealPlan(weekNumber: Int, year: Int): MealPlanWithMeals
}
*/
