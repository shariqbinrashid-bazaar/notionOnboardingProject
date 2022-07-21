package com.bazaar.api.template.config

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import java.util.Map
import javax.persistence.EntityManagerFactory

class PersistenceTemplateConfigurationTest {
    private var persistenceTemplateConfiguration: PersistenceTemplateConfiguration? = null
    private val DRIVER_CLASS = "org.h2.Driver"
    private val URL = "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;MODE=MYSQL"
    private val USERNAME = "sa"
    private val PASSWORD = "sa"


    @BeforeEach
    fun beforeEach() {
        persistenceTemplateConfiguration = PersistenceTemplateConfiguration()
    }

    @Nested
    internal inner class DataSourceProperties {
        @Test
        fun dataSourcePropertiesTest() {
            Assertions.assertNotNull(persistenceTemplateConfiguration!!.templateDataSourceProperties())
        }
    }

    @Nested
    internal inner class DataSource {
        @Test
        fun dataSourceTest() {
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.url = URL
            dataSourceProperties.username = USERNAME
            dataSourceProperties.password = PASSWORD
            dataSourceProperties.driverClassName = DRIVER_CLASS
            Assertions.assertNotNull(persistenceTemplateConfiguration!!.templateDataSource(dataSourceProperties))
        }


    }

    @Nested
    internal inner class TemplateEntityManagerFactory {
        @Test
        fun templateEntityManagerFactoryTest() {
            val factory = EntityManagerFactoryBuilder(
                HibernateJpaVendorAdapter(), Map.of<String, Any>(), null
            )
            val dataSourceProperties = org.springframework.boot.autoconfigure.jdbc.DataSourceProperties()
            dataSourceProperties.url = URL
            dataSourceProperties.username = USERNAME
            dataSourceProperties.password = PASSWORD
            dataSourceProperties.driverClassName = DRIVER_CLASS
            val dataSource = persistenceTemplateConfiguration!!.templateDataSource(dataSourceProperties)
            Assertions.assertNotNull(persistenceTemplateConfiguration!!.templateEntityManagerFactory(dataSource, factory))
        }
    }

    @Nested
    internal inner class TestPlatformTransactionManager {
        @Test
        fun platformTransactionManagerTest() {
            val managerMock: EntityManagerFactory = Mockito.mock(EntityManagerFactory::class.java)
            Assertions.assertNotNull(persistenceTemplateConfiguration!!.templateTransactionManager(managerMock))
        }
    }
}