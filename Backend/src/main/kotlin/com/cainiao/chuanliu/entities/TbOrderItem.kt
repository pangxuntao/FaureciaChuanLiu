package com.cainiao.chuanliu.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId

data class TbOrderItem @JvmOverloads constructor(
    @TableId(type = IdType.ASSIGN_UUID)
    var uuid: String? = null,
    val orderId: String? = null,
    val shelfId: String? = null,
)
