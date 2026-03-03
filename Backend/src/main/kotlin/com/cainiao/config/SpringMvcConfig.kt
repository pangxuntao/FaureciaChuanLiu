package com.cainiao.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.StandardCharsets


@Configuration
class SpringMvcConfig : WebMvcConfigurer {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**") // 允许所有路径
                    .allowedOriginPatterns("*")
//                    .allowedOrigins("*") // 允许特定的前端地址
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS") // 允许的 HTTP 方法
                    .allowedHeaders("*") // 允许的请求头
                    .allowCredentials(true) // 允许携带凭证
            }
        }
    }
    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>?>) {
        converters.add(responseBodyConverter())
    }

    @Bean
    fun responseBodyConverter(): HttpMessageConverter<String?>? {
        return StringHttpMessageConverter(
            StandardCharsets.UTF_8
        )
    }
}
