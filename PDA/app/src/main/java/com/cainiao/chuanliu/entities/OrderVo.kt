package com.cainiao.chuanliu.entities

import com.cainiao.mywidget.recyclerview.itemdata.BaseItemDataSingleType

data class OrderVo(
    var order: TbOrder,
    var orderItems: List<TbOrderItem>
) : BaseItemDataSingleType()