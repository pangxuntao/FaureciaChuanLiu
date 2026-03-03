package com.cainiao.chuanliu.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cainiao.base.BaseViewModel
import com.cainiao.chuanliu.entities.OrderVo
import com.cainiao.chuanliu.entities.ShelfRepository
import com.cainiao.chuanliu.entities.TbOrder
import com.cainiao.chuanliu.entities.TbShelf
import com.cainiao.mycommon.utils.ToastUtil
import com.faurecia.http.RetrofitUtil

class CallViewModel() : BaseViewModel() {
    private val shelfList = ShelfRepository.shelfList
    val currentOrder = MutableLiveData<List<OrderVo>>()
    fun refresh() {
        subscript(RetrofitUtil.apiLoading.currentData(), {
            if (it.dataSuccess()) {
                shelfList.clear()
                shelfList.addAll(it.data?.shelfList ?: listOf())
                currentOrder.postValue(it.data?.currentOrders ?: listOf())
            }
        })
        {
            ToastUtil.error(it.message ?: "网络异常")
        }
    }
}