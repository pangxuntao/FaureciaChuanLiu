package com.cainiao.chuanliu.entities

import com.cainiao.mywidget.recyclerview.itemdata.BaseItemDataSingleType

data class TbOrderItem @JvmOverloads constructor(
    var uuid: String? = null,
    val orderId: String? = null,
    val shelfId: String? = null,
) : BaseItemDataSingleType()
