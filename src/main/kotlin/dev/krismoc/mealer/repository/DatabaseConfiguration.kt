package dev.krismoc.mealer.repository

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@EnableJdbcRepositories
@EnableJdbcAuditing
class DatabaseConfiguration : AbstractJdbcConfiguration() {
    override fun userConverters(): MutableList<*> {
        return super.userConverters()
    }
}
