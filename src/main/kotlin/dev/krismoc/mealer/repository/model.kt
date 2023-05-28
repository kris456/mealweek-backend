package dev.krismoc.mealer.repository

import java.time.LocalDateTime
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import com.fasterxml.jackson.annotation.JsonIgnore

@Table("shopping_list")
data class ShoppingList(
    @JsonIgnore
    @Id
    val id: Int? = null,
    val weeknumber: Int,
    val year: Int,

    @Column("shopping_list_id")
    val items: Set<ShoppingItem> = emptySet(),

    @CreatedDate
    val createdDate: LocalDateTime? = null,
    @LastModifiedDate
    val modifiedDate: LocalDateTime? = null,

    @CreatedBy
    val createdBy: Int? = null,
    @LastModifiedBy
    val modifiedBy: Int? = null
)

@Table("shopping_item")
data class ShoppingItem(
    @Id
    val id: Int? = null,

    val name: String,
    val shoppingListId: Int,
    @CreatedDate
    val createdDate: LocalDateTime? = null,
    @LastModifiedDate
    val modifiedDate: LocalDateTime? = null,

    @CreatedBy
    val createdBy: Int? = null,
    @LastModifiedBy
    val modifiedBy: Int? = null
)

@Table("meal")
data class Meal(
    @Id
    val id: Int? = null,

    val name: String,

    @CreatedDate
    val createdDate: LocalDateTime? = null,
    @LastModifiedDate
    val modifiedDate: LocalDateTime? = null,

    @CreatedBy
    val createdBy: Int? = null,
    @LastModifiedBy
    val modifiedBy: Int? = null
)

@Table("mealplan")
data class MealPlan(
    @Id
    val id: Int? = null,

    val weeknumber: Int,
    val year: Int,

    val meals: MutableSet<MealPlanMeals> = HashSet(),

    @CreatedDate
    val createdDate: LocalDateTime? = null,
    @LastModifiedDate
    val modifiedDate: LocalDateTime? = null,

    @CreatedBy
    val createdBy: Int? = null,
    @LastModifiedBy
    val modifiedBy: Int? = null
) {
    fun addMeal(meal: Meal, weekdayIso: Int){
        val mealPlanMeals = MealPlanMeals( id!!, meal.id!!,weekdayIso )
        meals.add( mealPlanMeals )
    }
}

@Table("mealplan_meals")
data class MealPlanMeals(
    val mealplan: Int,

    val meal: Int,
    val weekdayIso: Int,

    @CreatedDate
    val createdDate: LocalDateTime? = null,
    @LastModifiedDate
    val modifiedDate: LocalDateTime? = null,

    @CreatedBy
    val createdBy: Int? = null,
    @LastModifiedBy
    val modifiedBy: Int? = null

)

data class UserInfo(
    @Id
    val id: Int? = null,
    val email: String,
    val password: String,

    @CreatedDate
    val createdDate: LocalDateTime? = null
)
