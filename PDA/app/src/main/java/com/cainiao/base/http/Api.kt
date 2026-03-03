package com.cainiao.base.http

import com.cainiao.chuanliu.entities.CurrentDataVo
import com.cainiao.chuanliu.entities.OrderVo
import com.cainiao.chuanliu.entities.TbOrder
import com.cainiao.chuanliu.entities.TbShelf
import com.faurecia.http.HttpResult
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface Api {
    @POST("/android/currentData")
    fun currentData(): Observable<HttpResult<CurrentDataVo>>

    @POST("/android/newOrder")
    fun newOrder(): Observable<HttpResult<TbOrder>>

    @POST("/android/createOrder")
    fun createOrder(@Body orderVo: OrderVo): Observable<HttpResult<Boolean>>

    @POST("/android/orderHistory")
    fun orderHistory(): Observable<HttpResult<List<OrderVo>>>
}