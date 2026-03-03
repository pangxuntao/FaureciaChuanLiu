package com.cainiao.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class BeanConfig {
    val eStation = 0

    @Bean
    fun objectMapper(): ObjectMapper? {
        return ObjectMapper()
    }

//    @Bean
//    fun xmlMapper(): XmlMapper? {
//        val xmlMapper = XmlMapper()
//        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true)
//        return xmlMapper
//    }
}
