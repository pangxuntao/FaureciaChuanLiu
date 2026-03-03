package com.cainiao.base.ext

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @Auther: pxt
 * @Date: 2021/5/31 18:19
 * @Description: com.faurecia.ext
 * @Version: 1.0
 */

val gson = Gson()

fun String?.isNullOrEmpty(): Boolean {
    return this?.isEmpty() ?: true
}

inline fun <reified T> String?.jsonToList(): List<T> {
    return this?.let {
        gson.fromJson(it, object : TypeToken<List<T>>() {}.type)
    } ?: mutableListOf()
}

inline fun <reified T> List<T>?.listToJson(): String {
    return this?.let {
        gson.toJson(it)
    } ?: "[]"
}