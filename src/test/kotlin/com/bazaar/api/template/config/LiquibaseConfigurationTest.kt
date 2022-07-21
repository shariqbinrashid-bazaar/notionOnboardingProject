package com.bazaar.api.template.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.Map

internal class LiquibaseConfigurationTest {
    var liquibaseConfiguration: LiquibaseConfiguration = LiquibaseConfiguration()

    @BeforeEach
    fun beforeEach() {
        liquibaseConfiguration = LiquibaseConfiguration()
    }

    @Nested
    internal inner class DataSourceProperties {
        @Test
        fun dataSourcePropertiesTest() {
            Assertions.assertNotNull(liquibaseConfiguration.liquibaseDataSourceProperties())
        }
    }

    @Nested
    internal inner class DataSource {
        @Test
        fun dataSourceTest() {
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.username = "root"
            dataSourceProperties.password = "my-secret-pw"
            dataSourceProperties.url = "jdbc:mysql://localhost:3306/bazaar_template"
            Assertions.assertNotNull(liquibaseConfiguration.liquibaseDataSource(dataSourceProperties))
        }
    }

    @Nested
    internal inner class TemplateLiquibase {
        @Test
        fun liquibase() {
/*
            val factory = EntityManagerFactoryBuilder(
                    HibernateJpaVendorAdapter(), Map.of<String, Any?>(), null)
*/
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.username = "root"
            dataSourceProperties.password = "my-secret-pw"
            dataSourceProperties.url = "jdbc:mysql://localhost:3306/bazaar_template"
            val dataSource: javax.sql.DataSource = liquibaseConfiguration.liquibaseDataSource(dataSourceProperties)
            val liquibase = liquibaseConfiguration.liquibase(dataSource)
            Assertions.assertNotNull(liquibase)
            Assertions.assertEquals("", liquibase?.changeLog)
            Assertions.assertEquals(dataSource, liquibase?.dataSource)
        }
    }

}