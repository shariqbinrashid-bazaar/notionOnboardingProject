package com.bazaar.api.template.config

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource

@Configuration
class LiquibaseConfiguration {
    @Value("\${liquibase.datasource.template-db.changelog}")
    private val changelog = ""
    @Bean
    @ConfigurationProperties(prefix = "liquibase.datasource.template-db")
    fun liquibaseDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun liquibaseDataSource(@Qualifier("liquibaseDataSourceProperties") dataSourceProperties: DataSourceProperties): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder().build()
    }

    @Bean
    fun liquibase(@Qualifier("liquibaseDataSource") dataSource: DataSource?): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.dataSource = dataSource
        liquibase.changeLog = changelog
        return liquibase
    }
}