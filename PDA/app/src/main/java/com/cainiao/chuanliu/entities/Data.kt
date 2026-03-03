package com.cainiao.chuanliu.entities

import com.cainiao.myexcel.annotation.ExcelReadIndex
import com.cainiao.mywidget.MyPopupWindow

data class Data(
    @ExcelReadIndex(1)
    val code: String = "",
    @ExcelReadIndex(2)
    val name1: String = "",
    @ExcelReadIndex(9)
    val check: String = ""
) : MyPopupWindow.PopupWindowItemData() {
    override fun getName(): String {
        return "${name1} [${code}]"
    }
}
