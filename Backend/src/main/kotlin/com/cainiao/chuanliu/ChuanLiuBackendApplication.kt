package com.cainiao.chuanliu

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication(scanBasePackages = ["com.cainiao"])
@MapperScan(basePackages = ["com.cainiao"])
@EnableTransactionManagement
class ChuanLiuBackendApplication

fun main(args: Array<String>) {
    runApplication<ChuanLiuBackendApplication>(*args)
}
