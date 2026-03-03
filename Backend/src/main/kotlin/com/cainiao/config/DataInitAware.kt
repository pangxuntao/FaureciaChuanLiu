package com.cainiao.config

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Configuration
class DataInitAware : ApplicationContextAware {
    @Transactional
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        println("setApplicationContext")
    }
}
