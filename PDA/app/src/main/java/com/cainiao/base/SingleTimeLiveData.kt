package com.cainiao.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @Auther: pxt
 * @Date: 2021/5/31 13:01
 * @Description: com.faurecia.base
 * @Version: 1.0
 */
class SingleTimeLiveData<T> : MutableLiveData<T>() {
    private val observerMap = mutableMapOf<Observer<in T>, AtomicBoolean>()
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        observerMap[observer] = AtomicBoolean(false)
        super.observe(owner) { value ->
            val mPending = observerMap.get(observer)
            if (mPending?.compareAndSet(true, false) == true) {
                observer.onChanged(value);
            }
        }
    }

    override fun setValue(value: T?) {
        observerMap.values.forEach {
            it.set(true)
        }
        super.setValue(value);
    }

    override fun postValue(value: T?) {
        observerMap.values.forEach {
            it.set(true)
        }
        super.postValue(value)
    }


    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    fun call() {
        setValue(null);
    }

    /**
     * Used for cases where T is mutableList, to make listener receive new data.
     */
    fun notifyListChange() {
        observerMap.keys.forEach {
            if (value != null) {
                it.onChanged(value!!)
            }
        }
    }
}