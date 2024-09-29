package com.skfl.filestorageservice.config

import javax.sql.DataSource
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy

@Configuration
class JooqConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.fs")
    fun fsDatasourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    fun fsDatasource(): DataSource {
        return fsDatasourceProperties().initializeDataSourceBuilder().build()
    }

    @Bean
    fun fsDataSourceConnectionProvider(): DataSourceConnectionProvider {
        return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(fsDatasource()))
    }

    @Bean
    fun fsJooqConfiguration(): DefaultConfiguration {
        return DefaultConfiguration().apply {
            setDataSource(fsDatasource())
            setConnectionProvider(fsDataSourceConnectionProvider())
            setSQLDialect(SQLDialect.POSTGRES)
        }
    }

    @Bean
    fun fsJooqDslContext(): DSLContext {
        val configuration = fsJooqConfiguration()
        return DefaultDSLContext(configuration)
    }
}