package dev.krismoc.mealer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import io.zonky.test.db.AutoConfigureEmbeddedDatabase

@Transactional
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val mapper = ObjectMapper()

    @Test
    fun `Should return badrequest on invalid email`() {
        val request = UserCredentialsPayload("invalid@withoutDot", "secret")
        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun `Should be able to register user`() {
        val request = UserCredentialsPayload("valid@email.com", "verylongsecret")
        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.email") { value(request.email) }
                jsonPath("$.token") {
                    isNotEmpty()
                    exists()
                }
            }
        }
    }

    @Test
    fun `Should be able to login user`() {
        val request = UserCredentialsPayload("valid@gmail.com", "verylongsecret")
        mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect {
            status { isOk() }
            content {
                jsonPath("$.email") { value(request.email) }
                jsonPath("$.token") {
                    isNotEmpty()
                    exists()
                }
            }
        }
    }

    @Test
    fun `Should return conflict when email already exists`() {
        val request = UserCredentialsPayload("valid@gmail.com", "password123")
        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect {
            status { isConflict() }
        }
    }

    @Test
    fun `Should return badrequest when password is short`() {
        val request = UserCredentialsPayload("new@gmail.com", "123")
        mockMvc.post("/api/v1/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
