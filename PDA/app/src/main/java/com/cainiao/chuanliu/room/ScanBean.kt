package com.cainiao.chuanliu.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.cainiao.myexcel.annotation.ExcelIgnore

@Entity(tableName = "scanData")
data class ScanBean @JvmOverloads constructor(
    @ExcelIgnore
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var box: String = "",
    var code: String = "",
    @ExcelIgnore
    var time: Long = 0,
    @Ignore
    var stringTime: String = ""
) {
}