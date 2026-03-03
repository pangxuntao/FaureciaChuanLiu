package com.cainiao.base.util

import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

/**
 * @Auther: pxt
 * @Date: 2021/6/7 16:16
 * @Description: com.faurecia.util
 * @Version: 1.0
 */
object ActivityManager {
    private val list = mutableListOf<FragmentActivity>()
    private var topActivity: WeakReference<FragmentActivity>? = null
    fun add(activity: FragmentActivity) {
        list.add(activity)
    }

    fun remove(activity: FragmentActivity) {
        list.add(activity)
    }

    fun setTopActivity(activity: FragmentActivity) {
        topActivity?.clear()
        topActivity = WeakReference(activity)
    }

    fun getPopActivity(): FragmentActivity? {
        return topActivity?.get()
    }
}