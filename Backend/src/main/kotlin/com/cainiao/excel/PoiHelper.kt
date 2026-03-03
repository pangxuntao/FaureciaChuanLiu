package com.cainiao.excel

import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object PoiHelper {
    fun createCommonStyle(workbook: XSSFWorkbook, fontSize: Int = 12): XSSFCellStyle {
        return workbook.createCellStyle().apply {
            val font = workbook.createFont()
            font.fontHeightInPoints = fontSize.toShort() // 设置字体大小
            setFont(font)
            // 水平居中
            alignment = HorizontalAlignment.CENTER
            // 垂直居中
            verticalAlignment = VerticalAlignment.CENTER
            // 设置边框样式
            borderTop = BorderStyle.MEDIUM;
            borderBottom = BorderStyle.MEDIUM;
            borderLeft = BorderStyle.MEDIUM;
            borderRight = BorderStyle.MEDIUM;
            // 设置边框颜色
            topBorderColor = IndexedColors.BLACK.getIndex();
            bottomBorderColor = IndexedColors.BLACK.getIndex();
            leftBorderColor = IndexedColors.BLACK.getIndex();
            rightBorderColor = IndexedColors.BLACK.getIndex();
//            fillForegroundColor = IndexedColors.LIGHT_BLUE.getIndex()
        }
    }

    fun createTitleStyle(workbook: XSSFWorkbook, fontSize: Int = 30): XSSFCellStyle {
        return workbook.createCellStyle().apply {
            val titleFont = workbook.createFont()
            titleFont.fontHeightInPoints = fontSize.toShort() // 设置字体大小
            titleFont.bold = true // 设置字体加粗
            setFont(titleFont)
            alignment = HorizontalAlignment.CENTER // 水平居中
            verticalAlignment = VerticalAlignment.CENTER // 垂直居中
            // 设置边框样式
            borderTop = BorderStyle.MEDIUM;
            borderBottom = BorderStyle.MEDIUM;
            borderLeft = BorderStyle.MEDIUM;
            borderRight = BorderStyle.MEDIUM;
            // 设置边框颜色
            topBorderColor = IndexedColors.BLACK.getIndex();
            bottomBorderColor = IndexedColors.BLACK.getIndex();
            leftBorderColor = IndexedColors.BLACK.getIndex();
            rightBorderColor = IndexedColors.BLACK.getIndex();
        }
    }
}
