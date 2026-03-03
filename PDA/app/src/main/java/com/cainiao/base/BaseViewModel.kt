package com.cainiao.base

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.faurecia.http.HttpResult
import com.cainiao.base.util.ToastUtil
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Auther: pxt
 * @Date: 2021/5/30 20:28
 * @Description: com.faurecia.base
 * @Version: 1.0
 */
abstract class BaseViewModel : ViewModel() {
    @SuppressLint("CheckResult")
    protected fun <T> subscript(
        observable: Observable<HttpResult<T>>,
        onNext: Consumer<HttpResult<T>>,
        onError: Consumer<Throwable>? = null,
    ) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.success()) {
                    onNext.accept(it)
                } else {
                    onError?.accept(Exception(it.msg)) ?: run {
                        ToastUtil.show("网络异常: ${it.msg}")
                    }
                }
            }) {
                Log.e("Network", "", it)
                onError?.accept(it) ?: run {
                    ToastUtil.show("网络异常: ${it.message}")
                }
            }
    }
}