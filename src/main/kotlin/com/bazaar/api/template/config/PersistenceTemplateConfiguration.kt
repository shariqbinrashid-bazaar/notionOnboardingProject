package com.bazaar.api.template.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EntityScan("com.bazaar.api.template")
@EnableJpaRepositories(
    entityManagerFactoryRef = "templateEntityManagerFactory",
    transactionManagerRef = "templateTransactionManager",
    basePackages = ["com.bazaar.api.template"]
)
@EnableTransactionManagement
class PersistenceTemplateConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "bazaar.datasource.template-db")
    fun templateDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary //    @ConfigurationProperties(prefix = "bazaar.datasource.template-db")
    fun templateDataSource(@Qualifier("templateDataSourceProperties") dataSourceProperties: DataSourceProperties): DataSource {
        return dataSourceProperties.initializeDataSourceBuilder().build()
    }

    @Bean
    @Primary
    fun templateEntityManagerFactory(
        @Qualifier("templateDataSource") hubDataSource: DataSource?,
        builder: EntityManagerFactoryBuilder
    ): LocalContainerEntityManagerFactoryBean {
        return builder
            .dataSource(hubDataSource)
            .packages("com.bazaar.api.template")
            .persistenceUnit("template")
            .build()
    }

    @Bean
    @Primary
    fun templateTransactionManager(@Qualifier("templateEntityManagerFactory") factory: EntityManagerFactory?): PlatformTransactionManager {
        return JpaTransactionManager(factory!!)
    }
}