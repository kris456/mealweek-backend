package dev.krismoc.mealer.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

class MealControllerTest : AbstractAuthenticatedControllerTest() {
    @Test
    fun `Should be able to create meal`() {
        mockMvc.post("/api/v1/meals") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(NewMealRequest("NoeHeltAnnet"))

            headers {
                setBearerAuth(token)
            }

        }.andExpect {
            status { isCreated() }
            jsonPath("$.name") { value("NoeHeltAnnet") }
            jsonPath("$.id") { value(100) }
            jsonPath("$.createdBy") { value(2) }
        }
    }

    @Test
    fun `Should not be able to create a meal when body is not provided`() {
        mockMvc.post("/api/v1/meals") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `Should be able to get meals`() {
        mockMvc.get("/api/v1/meals") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(15) }
        }
    }

    @Test
    fun `Should only get own meals`() {
        mockMvc.post("/api/v1/meals") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(NewMealRequest("NoeHeltAnnet"))

            headers {
                setBearerAuth(otherToken)
            }
        }.andExpect {
            status { isCreated() }
        }

        mockMvc.get("/api/v1/meals") {
            headers {
                setBearerAuth(otherToken)
            }
        }.andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
        }
    }

    @Test
    fun `Should be able to get first meal`() {
        mockMvc.get("/api/v1/meals/1") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("test") }
        }
    }

    @Test
    fun `Should respond 404 when meal does not exist`() {
        mockMvc.get("/api/v1/meals/101") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `Should be able to update first meal`() {
        mockMvc.put("/api/v1/meals/1") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(UpdateMealRequest("newname"))
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("newname") }
        }
    }

    @Test
    fun `Should not be able to update non-existing meal`() {
        mockMvc.put("/api/v1/meals/100") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(UpdateMealRequest("newname"))
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isNotFound() }
        }
    }

    @Test
    fun `Should be able to delete first meal`() {
        mockMvc.delete("/api/v1/meals/1") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun `Should return ok when deleting non existing meal`() {
        mockMvc.delete("/api/v1/meals/100") {
            headers {
                setBearerAuth(token)
            }
        }.andExpect {
            status { isOk() }
        }
    }
}
