package dev.krismoc.mealer.controller

import java.util.stream.Stream
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import dev.krismoc.mealer.service.AllFilter
import dev.krismoc.mealer.service.MealplanFilter

class MealPlanControllerTest : AbstractAuthenticatedControllerTest() {
    @Test
    fun `Should be able to create mealplan`() {
        mockMvc.post("/api/v1/mealplan") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(NewMealPlanPayload(1, 2023))

            headers {
                setBearerAuth(token)
            }

        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { value(100) }
            jsonPath("$.weekNumber") { value(1) }
            jsonPath("$.year") { value(2023) }
        }
    }

    @Test
    fun `Should be able to get all mealplans with all filter`() {
        mockMvc.get("/api/v1/mealplan") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(NewMealPlanPayload(1, 2023))

            headers {
                setBearerAuth(token)
            }

        }.andExpect {
            status { isOk()}
            jsonPath("$.length()"){value(1)}
            jsonPath("$[0].id") { value(1) }
            jsonPath("$[0].weekNumber") { value(1) }
            jsonPath("$[0].year") { value(2022) }
        }
    }

    @Test
    fun `Should be able to add meal to mealplan`() {
        mockMvc.post("/api/v1/mealplan/1/meals") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(NewMealToMealPlanRequest("lasagne", 1))

            headers {
                setBearerAuth(token)
            }

        }.andExpect {
            status { isCreated() }
            jsonPath("$.id") { value(1) }
            jsonPath("$.weekNumber") { value(1) }
            jsonPath("$.year") { value(2022) }
            jsonPath("$.meals.length()") { value(1) }
            jsonPath("$.meals[0].name") { value("lasagne") }
        }
    }
}
