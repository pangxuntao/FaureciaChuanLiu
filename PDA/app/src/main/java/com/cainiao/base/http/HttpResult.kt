package com.faurecia.http

/**
 * @Auther: pxt
 * @Date: 2021/5/30 17:55
 * @Description: com.faurecia.http
 * @Version: 1.0
 */
data class HttpResult<T>(
    val code: Int,
    val data: T?,
    val msg: String,
) {
    fun success(): Boolean {
        return code == 1
    }
    fun dataSuccess(): Boolean {
        return code == 1 && data != null
    }
}