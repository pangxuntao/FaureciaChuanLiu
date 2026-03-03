package com.cainiao.chuanliu.entities.vo

import com.cainiao.chuanliu.entities.TbShelf

data class CurrentDataVo(
    var shelfList: List<TbShelf>? = null,
    var currentOrders: List<OrderVo>? = null,
)