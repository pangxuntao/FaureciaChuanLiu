package com.cainiao.chuanliu.entities

import java.util.Date

data class TbShelf(
    var uuid: String? = null,
    var shelfCode: String? = null,
    var materialName: String? = null,
    var materialQty: Int? = null,
    var materialCode: String? = null,
    var createTime: Long? = null,
    var updateTime: Long? = null,
    var deleted: Boolean? = null,
)