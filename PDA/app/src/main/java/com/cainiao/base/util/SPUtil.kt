package com.cainiao.base.util

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.cainiao.base.App


/**
 * @Auther: pxt
 * @Date: 2021/5/23
 * @Description: 
 * @Version: 1.0
 */
object SPUtil {
    private val sp: SharedPreferences

    init {
        val context = App.app
        sp = context.getSharedPreferences(context.packageName + "." + SPUtil::class.java.name, Context.MODE_PRIVATE)
    }

    fun getString(key: String?, def: String?): String? {
        checkInit()
        return getS(key, def)
    }

    fun getInt(key: String?, def: Int): Int {
        checkInit()
        return getI(key, def)
    }

    fun getBoolean(key: String?, def: Boolean): Boolean {
        checkInit()
        return getB(key, def)
    }

    fun getLong(key: String?, def: Long): Long {
        checkInit()
        return getL(key, def)
    }

    fun getFloat(key: String?, def: Float): Float {
        checkInit()
        return getF(key, def)
    }

    fun getStringSet(key: String?, def: Set<String?>?): Set<String?>? {
        checkInit()
        return getStrSet(key, def)
    }

    fun getAll(): Map<String?, *>? {
        checkInit()
        return getA()
    }

    fun <T> get(key: String?, def: T?): T? {
        checkInit()
        return (getO(key, def) as T?)
    }

    fun <T> getNullSafe(key: String?, def: T): T {
        checkInit()
        return (getO(key, def) as T)
    }

    fun put(key: String?, value: Any?) {
        checkInit()
        putO(key, value)
    }

    fun registerListener(listener: OnSharedPreferenceChangeListener?) {
        checkInit()
        registerListenerL(listener)
    }

    fun unregisterListener(listener: OnSharedPreferenceChangeListener?) {
        checkInit()
        unregisterListenerL(listener)
    }

    fun getS(key: String?, def: String?): String? {
        return sp.getString(key, def)
    }

    fun getI(key: String?, def: Int): Int {
        return sp.getInt(key, def)
    }

    fun getB(key: String?, def: Boolean): Boolean {
        return sp.getBoolean(key, def)
    }

    fun getL(key: String?, def: Long): Long {
        return sp.getLong(key, def)
    }

    fun getF(key: String?, def: Float): Float {
        return sp.getFloat(key, def)
    }

    fun getStrSet(key: String?, def: Set<String?>?): Set<String?>? {
        return sp.getStringSet(key, def)
    }

    fun getA(): Map<String?, *>? {
        return sp.all
    }

    fun getO(key: String?, def: Any?): Any? {
        return if (key == null) {
            throw NullPointerException("key is null")
        } else if (def is String) {
            sp.getString(key, def as String?)
        } else if (def is Int) {
            sp.getInt(key, def)
        } else if (def is Boolean) {
            sp.getBoolean(key, def)
        } else if (def is Float) {
            sp.getFloat(key, def)
        } else if (def is Long) {
            sp.getLong(key, def)
        } else if (def is Set<*>) {
            sp.getStringSet(key, def as Set<String>?)
        } else {
            throw java.lang.RuntimeException("key in wrong itemType")
        }
    }

    private fun putO(key: String?, value: Any?) {
        if (key == null) {
            throw NullPointerException("key is null")
        } else if (value is String) {
            sp.edit().putString(key, value as String?).apply()
        } else if (value is Int) {
            sp.edit().putInt(key, value).apply()
        } else if (value is Boolean) {
            sp.edit().putBoolean(key, value).apply()
        } else if (value is Float) {
            sp.edit().putFloat(key, value).apply()
        } else if (value is Long) {
            sp.edit().putLong(key, value).apply()
        } else if (value is Set<*>) {
            sp.edit().putStringSet(key, value as Set<String?>?).apply()
        } else {
            throw java.lang.RuntimeException("key in wrong itemType")
        }
    }

    fun registerListenerL(listener: OnSharedPreferenceChangeListener?) {
        sp.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterListenerL(listener: OnSharedPreferenceChangeListener?) {
        sp.unregisterOnSharedPreferenceChangeListener(listener)
    }

    private fun checkInit() {
        if (sp == null) {
            throw RuntimeException("SpUtil not initialized")
        }
    }
}