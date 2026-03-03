package com.cainiao.config

import com.alibaba.druid.pool.DruidDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.transaction.TransactionManager

import javax.sql.DataSource;

@Configuration
class DruidConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    fun druid(): DataSource {
        println("druid----------------------------------")
        return DruidDataSource()
    }

    @Bean
    fun transactionManager(): TransactionManager {
        return JdbcTransactionManager(druid())
    }
}
