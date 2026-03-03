package com.cainiao.common

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class ProfileUtil : ApplicationContextAware {
    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
        println("activeProfile:$activeProfile")
    }

    companion object {
        private var context: ApplicationContext? = null
        // 获取当前环境参数  exp: dev,prod,test
        val activeProfile: String
            get() {
                val profiles: Array<String>? = context?.environment?.activeProfiles
                return if (profiles?.isNotEmpty() == true) {
                    profiles[0]
                } else ""
            }
    }
}