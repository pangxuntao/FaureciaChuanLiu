package com.cainiao.chuanliu.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cainiao.base.BaseViewModel
import com.cainiao.chuanliu.entities.OrderVo
import com.cainiao.chuanliu.entities.ShelfRepository
import com.cainiao.chuanliu.entities.TbOrder
import com.cainiao.chuanliu.entities.TbOrderItem
import com.cainiao.chuanliu.entities.TbShelf
import com.cainiao.mycommon.utils.SoundUtil
import com.cainiao.mycommon.utils.ToastUtil
import com.faurecia.http.RetrofitUtil

class CallScanViewModel() : BaseViewModel() {
    private val shelfList = ShelfRepository.shelfList
    val tbOrder = MutableLiveData<TbOrder>()
    val orderItems = MutableLiveData<MutableList<TbOrderItem>>(mutableListOf())
    fun getNewOrder(error: () -> Unit) {
        subscript(RetrofitUtil.apiLoading.newOrder(), {
            if (it.dataSuccess()) {
                tbOrder.postValue(it.data)
            } else {
                ToastUtil.error(it.msg ?: "网络异常")
                error.invoke()
            }
        })
        {
            ToastUtil.error(it.message ?: "网络异常")
            error.invoke()
        }
    }

    fun findShelf(code: String) {
        shelfList.find {
            it.shelfCode == code
        }?.also {
            orderItems.value?.add(
                TbOrderItem(
                    orderId = tbOrder.value?.uuid,
                    shelfId = it.uuid
                )
            )
            SoundUtil.jiu()
        } ?: run {
            ToastUtil.error("未找到货架号：$code")
        }
    }

    fun confirm(callback: () -> Unit) {
        subscript(RetrofitUtil.apiLoading.createOrder(
            OrderVo(order = tbOrder.value!!, orderItems = orderItems.value!!)
        ), {
            if (it.data == true) {
                callback()
            }
        })
        {
            ToastUtil.error(it.message ?: "网络异常")
        }
    }
}