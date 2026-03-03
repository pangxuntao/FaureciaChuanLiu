package com.cainiao.chuanliu.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.cainiao.myexcel.annotation.ExcelIgnore
import com.cainiao.mywidget.recyclerview.itemdata.BaseItemDataSingleType

data class ScanHistory @JvmOverloads constructor(
    var name: String = "",
    var code: String = "",
    var time: Long = 0,
    var success: Boolean = false,
    var error: String = "",
) : BaseItemDataSingleType()