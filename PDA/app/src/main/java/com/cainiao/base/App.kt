package com.cainiao.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import com.cainiao.base.util.ActivityManager
import com.cainiao.chuanliu.room.AppDatabase
import com.cainiao.mycommon.utils.SoundUtil
import com.cainiao.mycommon.utils.ToastUtil

/**
 * @Auther: pxt
 * @Date: 2021/5/30 20:44
 * @Description: com.faurecia.base
 * @Version: 1.0
 */
class App : Application(), Application.ActivityLifecycleCallbacks {
    companion object {
        lateinit var app: App
        lateinit var context: Context
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        context = this
        registerActivityLifecycleCallbacks(this)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "my-database"
        ).build()
        SoundUtil.init(this)
        ToastUtil.init(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityManager.add(activity as FragmentActivity)
        ActivityManager.setTopActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        ActivityManager.setTopActivity(activity as FragmentActivity)
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        ActivityManager.remove(activity as FragmentActivity)
    }
}