package com.cainiao.chuanliu.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.cainiao.myexcel.annotation.ExcelHead
import com.cainiao.myexcel.annotation.ExcelIgnore
import com.cainiao.mywidget.recyclerview.itemdata.BaseItemDataSingleType

@Entity(tableName = "scanData")
data class ScanBean @JvmOverloads constructor(
    @ExcelIgnore
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    var code: String = "",
    @ExcelIgnore
    var time: Long = 0,
    @ExcelIgnore
    var success: Boolean = false,
    @ExcelIgnore
    var error: Boolean = false,
    @Ignore
    @ExcelHead("时间")
    var stringTime: String = ""
)