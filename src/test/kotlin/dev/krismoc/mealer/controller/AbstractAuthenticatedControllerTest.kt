package dev.krismoc.mealer.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.Locale

@Transactional
@AutoConfigureEmbeddedDatabase(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY)
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
abstract class AbstractAuthenticatedControllerTest {

    @Autowired
    lateinit var flyway: Flyway

    @BeforeEach
    fun setup() {
        flyway.clean()
        flyway.migrate()
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    private val mapper = jacksonObjectMapper().findAndRegisterModules()

    val token by lazy {
        getJWTToken("valid@gmail.com")
    }

    val otherToken by lazy {
        getJWTToken("other@gmail.com")
    }

    private final fun getJWTToken(email: String): String {
        val request = UserCredentialsPayload(email, "verylongsecret")
        val tokenPayLoadString = mockMvc.post("/api/v1/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(request)
        }.andReturn().response.contentAsByteArray

        val loginResponse = mapper.readValue<UserResource>(tokenPayLoadString)
        return loginResponse.token
    }
}
