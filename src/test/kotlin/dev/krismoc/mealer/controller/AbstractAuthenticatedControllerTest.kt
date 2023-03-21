package dev.krismoc.mealer.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@Transactional
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@SpringBootTest
@AutoConfigureMockMvc
abstract class AbstractAuthenticatedControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper().findAndRegisterModules()

    val token by lazy {
        getJWTToken()
    }

    private final fun getJWTToken(): String {
        val request = UserCredentialsPayload("valid@gmail.com", "verylongsecret")
        val tokenPayLoadString = mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andReturn().response.contentAsByteArray

        val loginResponse = mapper.readValue<UserResource>(tokenPayLoadString)
        return loginResponse.token
    }
}
