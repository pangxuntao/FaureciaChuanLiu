package com.cainiao.chuanliu.entities.vo

import com.cainiao.chuanliu.entities.TbOrder
import com.cainiao.chuanliu.entities.TbOrderItem

data class OrderVo(
    var order: TbOrder,
    var orderItems: List<TbOrderItem>
)