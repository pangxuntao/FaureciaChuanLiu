package com.cainiao.chuanliu.entities

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import java.util.*

data class TbOrder @JvmOverloads constructor(
    @TableId(type = IdType.ASSIGN_UUID)
    var uuid: String? = null,
    val orderCode: String? = null,
    var createBy: String? = null,
    var status: Int = 0,// 0已创建 1已下载 2配货中 3已完成
    var createTime: Date? = null,
    var downloadTime: Date? = null,
    var startTime: Date? = null,
    var finishTime: Date? = null,
)
