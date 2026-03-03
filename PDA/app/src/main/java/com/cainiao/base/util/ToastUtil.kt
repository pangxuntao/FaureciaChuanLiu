package com.cainiao.base.util

import android.view.Gravity
import com.cainiao.base.App
import com.hjq.toast.ToastUtils

/**
 * @Auther: pxt
 * @Date: 2021/5/31 14:02
 * @Description: com.faurecia.base
 * @Version: 1.0
 */
object ToastUtil {
    init {
        L.e("ToastUtil init "+ App.app)
        ToastUtils.init(App.app)
        ToastUtils.setGravity(Gravity.CENTER)
    }

    fun show(msg: String?) {
        ToastUtils.cancel()
        ToastUtils.show(msg)
    }

    fun show(msg: Int?) {
        ToastUtils.cancel()
        ToastUtils.show(msg)
    }
}