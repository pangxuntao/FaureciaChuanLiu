package com.cainiao.base.util

import android.os.Looper

/**
 * @Auther: pxt
 * @Date: 2021/6/7 16:00
 * @Description: com.faurecia.util
 * @Version: 1.0
 */
object ThreadUtils {
    fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }
}