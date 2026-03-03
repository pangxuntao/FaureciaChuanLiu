package com.cainiao.mylibkt.mycomon.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object FormatUtil {
    @JvmOverloads
    fun formatDate(
        date: Long,
        format: String = "yyyy-MM-dd HH:mm:ss.SSS",
    ): String {
        return formatDate(Date(date), format)
    }

    @JvmOverloads
    fun formatDate(
        date: Date,
        format: String = "yyyy-MM-dd HH:mm:ss.SSS",
    ): String {
        return SimpleDateFormat(format).format(date)
    }

    @JvmOverloads
    @Throws(ParseException::class)
    fun parse(
        date: String,
        format: String = "yyyy-MM-dd HH:mm:ss.SSS",
    ): Date? {
        return SimpleDateFormat(format).parse(date)
    }
}