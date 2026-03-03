package com.cainiao.ext

import ch.qos.logback.classic.Logger
import com.fasterxml.jackson.databind.ObjectMapper
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.number


/**
 * @Auther: pxt
 * @Date: 2021/08/22 14:29
 * @Description: com.cainiao.ext
 * @Version: 1.0
 */
fun String.toSnakeCase(): String {
    val result = StringBuilder()
    this.forEach { char ->
        if (char.isUpperCase()) {
            // 如果是大写字母，先追加一个下划线，再追加转换为小写的大写字母
            if (result.isNotEmpty()) {
                result.append('_')
            }
            result.append(char.lowercaseChar())
        } else {
            // 如果是小写字母，直接追加
            result.append(char)
        }
    }
    return result.toString()
}

fun List<Any?>?.isNullOrEmpty(): Boolean {
    return this?.isEmpty() ?: true
}

var jackson = ObjectMapper()

fun Logger.logger(log: Any) {
    info(jackson.writeValueAsString(log))
}

fun Logger.logger(tag: String?, log: Any) {
    info(tag, jackson.writeValueAsString(log))
}

fun Double.formatStr(): String {
    return String.format("%.2f", this) + "%";
}

fun Boolean.toSuccess(): String {
    return if (this) {
        "成功"
    } else {
        "失败"
    }
}
