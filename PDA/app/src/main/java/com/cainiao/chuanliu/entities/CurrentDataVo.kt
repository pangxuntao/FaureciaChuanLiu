package com.cainiao.chuanliu.entities

data class CurrentDataVo(
    var shelfList: List<TbShelf>? = null,
    var currentOrders: List<OrderVo>? = null,
)