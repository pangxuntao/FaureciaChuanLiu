package com.cainiao.chuanliu.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.util.*

data class TbShelf(
    @TableId(type = IdType.ASSIGN_UUID)
    var uuid: String? = null,
    var shelfCode: String? = null,
    var materialName: String? = null,
    var materialQty: Int? = null,
    var materialCode: String? = null,
    var createTime: Date? = null,
    var updateTime: Date? = null,
    var deleted: Boolean? = null,
)