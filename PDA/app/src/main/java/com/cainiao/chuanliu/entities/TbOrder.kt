package com.cainiao.chuanliu.entities

import com.cainiao.mywidget.recyclerview.itemdata.BaseItemDataSingleType
import java.util.Date

data class TbOrder @JvmOverloads constructor(
    var uuid: String? = null,
    val orderCode: String? = null,
    var createBy: String? = null,
    var status: Int = 0,// 0已创建 1已下载 2配货中 3已完成
    var createTime: Long? = null,
    var downloadTime: Long? = null,
    var startTime: Long? = null,
    var finishTime: Long? = null,
)
