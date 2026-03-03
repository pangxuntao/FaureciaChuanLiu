//package com.cainiao.sigemenbackend.excel
//
//import org.apache.poi.ss.usermodel.*
//import org.apache.poi.ss.util.CellRangeAddress
//import org.apache.poi.xddf.usermodel.PresetColor
//import org.apache.poi.xddf.usermodel.XDDFColor
//import org.apache.poi.xddf.usermodel.chart.*
//import org.apache.poi.xssf.usermodel.XSSFWorkbook
//import java.io.FileOutputStream
//import java.io.IOException
//
//
///**
// * @Auther: pxt
// * @Date: 2021/08/22 19:33
// * @Description: com.cainiao.sigemenbackend.excel
// * @Version: 1.0
// */
//object ExcelWriter {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        val workbook = XSSFWorkbook()
//        val sheet = workbook.createSheet("BarChart")
//        //设置列宽, 256代表一个字符
//        for (i in 0..29) {
//            sheet.setColumnWidth(i, 256 * 8)
//        }
//        //通用样式
//        val commonStyle = PoiHelper.createCommonStyle(workbook)
//
//        // 创建标题行并合并单元格
//        val titleRow: Row = sheet.createRow(0)
//        val titleCell = titleRow.createCell(0)
//        titleCell.setCellValue("This is the Title")
//        // 合并单元格
//        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 21))
//        // 设置标题样式
//        val titleStyle: CellStyle = PoiHelper.createTitleStyle(workbook)
//        titleCell.cellStyle = titleStyle
//        // 为合并单元格区域设置边框
//        for (i in 0..21) {
//            val cell = if (i == 0) titleCell else titleRow.createCell(i)
//            cell.cellStyle = titleStyle
//        }
//        // 自定义行高度
//        titleRow.heightInPoints = 40f // 根据需要调整高度
//
//        // 创建示例数据
//        val data = arrayOf(
//            arrayOf<Any>(
//                "分区",
//                "A",
//                "B",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "C",
//                "总数"
//            ),
//            arrayOf<Any>(
//                "",
//                "",
//                10,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                15,
//                ""
//            ),
//            arrayOf<Any>("总数", "B", 20, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 25, 500),
//        )
//
//        var rowCount = 1
//        for (aData in data) {
//            val row: Row = sheet.createRow(rowCount++)
//            row.heightInPoints = 20f
//            var columnCount = 0
//            for (field in aData) {
//                val cell = row.createCell(columnCount++)
//                cell.cellStyle = commonStyle
//                if (field is String) {
//                    cell.setCellValue(field)
//                } else if (field is Int) {
//                    cell.setCellValue(field.toDouble())
//                }
//            }
//        }
//
//        //合并单元格
//        sheet.addMergedRegion(CellRangeAddress(1, 2, 0, 0))
//        sheet.addMergedRegion(CellRangeAddress(1, 2, 21, 21))
//
//
//        // Create a drawing canvas on the sheet
//        val drawing = sheet.createDrawingPatriarch()
//        // Define anchor points in the worksheet to position the chart
//        val anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, 22, 34)
//        // Create the chart object
//        val chart = drawing.createChart(anchor)
//        chart.setTitleText("Bar Chart Example")
//        chart.titleOverlay = false
//        // 修改图表标题样式
//        val textBody = chart.title?.body
//        val paragraph = textBody?.getParagraph(0)
//        val textRun = paragraph?.textRuns?.get(0)
//        textRun?.fontSize = 10.0 // 设置字体大小
//        textRun?.fontColor = XDDFColor.from(PresetColor.BLACK) // 设置字体颜色
//        textRun?.isBold = true // 设置加粗
//
//        // 创建数据源
//        val categories: XDDFDataSource<String> = XDDFDataSourcesFactory.fromArray(arrayOf("A", "B", "C", "D"))
//        val barValues = XDDFDataSourcesFactory.fromArray(arrayOf(10.0, 20.0, 30.0, 40.0))
//        val lineValues = XDDFDataSourcesFactory.fromArray(arrayOf(10.0, 20.0, 30.0, 40.0))
//
//        // 创建图表轴
//        val bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM)
//        bottomAxis.setTitle("Category")
//        bottomAxis.orAddTextProperties.setFontSize(10.0)
//
//        // 添加左侧 Y 轴
//        val leftAxis = chart.createValueAxis(AxisPosition.LEFT)
//        leftAxis.setTitle("Bar Values222")
//        leftAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        leftAxis.crosses = AxisCrosses.MIN;
//
//        bottomAxis.crossAxis(leftAxis)
//        // 创建柱状图数据
//        val barData = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis) as XDDFBarChartData
//        val barSeries = barData.addSeries(categories, barValues) as XDDFBarChartData.Series
//        barSeries.setTitle("Bar Values111", null)
//        chart.plot(barData)
//
//        // 添加右侧 Y 轴
//        val rightAxis = chart.createValueAxis(AxisPosition.RIGHT)
//        rightAxis.setTitle("Line Values")
//        rightAxis.maximum = 100.0
//        rightAxis.crosses = AxisCrosses.MAX
//        rightAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        rightAxis.orAddTextProperties.setFontSize(10.0)
//
//        rightAxis.crossAxis(bottomAxis);
//        bottomAxis.crossAxis(rightAxis);
//        // 创建折线图数据
//        val lineData = chart.createData(ChartTypes.LINE, bottomAxis, rightAxis) as XDDFLineChartData
//        val lineSeries = lineData.addSeries(categories, lineValues)
//
//        lineSeries.setTitle("Line Values", null)
//        chart.plot(lineData)
//        // 自定义柱状图方向
//        barData.barDirection = BarDirection.COL
//
//        //写其他sheet
//        writeSheet(workbook, "缺陷x", "缺陷x")
//
//        // 保存工作簿到文件
//        try {
//            FileOutputStream("ComboChartExample.xlsx").use { fileOut -> workbook.write(fileOut) }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun writeSheet(
//        workbook: XSSFWorkbook,
//        sheetName: String,
//        exName: String,
//    ) {
//        val sheet = workbook.createSheet(sheetName)
//        //设置列宽, 256代表一个字符
//        for (i in 0..2) {
//            sheet.setColumnWidth(i, 256 * 8)
//        }
//        val titleStyle1 = PoiHelper.createTitleStyle(workbook, 16)
//        titleStyle1.fillForegroundColor = IndexedColors.SKY_BLUE.getIndex()
//        val titleStyle2 = PoiHelper.createTitleStyle(workbook, 16)
//        titleStyle1.fillForegroundColor = IndexedColors.LIGHT_BLUE.getIndex()
//
//        // 创建标题行并合并单元格
//        val titleRow: Row = sheet.createRow(0)
//        val titleCell = titleRow.createCell(0)
//        val titleCell2 = titleRow.createCell(1)
//        titleCell.setCellValue("缺陷类型")
//        titleCell2.setCellValue(exName)
//        titleCell.cellStyle = titleStyle1
//        titleCell2.cellStyle = titleStyle2
//        // 自定义行高度
//        titleRow.heightInPoints = 30f // 根据需要调整高度
//
//        // 创建示例数据
//        val data = arrayOf(
//            arrayOf<Any>(
//                "缺陷1",
//                "3",
//            ),
//            arrayOf<Any>(
//                "缺陷2",
//                "2",
//            ),
//            arrayOf<Any>(
//                "缺陷3",
//                "3",
//            ),
//            arrayOf<Any>(
//                "缺陷4",
//                "5",
//            ),
//        )
//        val commonStyle = PoiHelper.createCommonStyle(workbook)
//        commonStyle.fillForegroundColor = IndexedColors.WHITE.getIndex()
//        var rowCount = 1
//        for (aData in data) {
//            val row: Row = sheet.createRow(rowCount++)
//            row.heightInPoints = 20f
//            var columnCount = 0
//            for (field in aData) {
//                val cell = row.createCell(columnCount++)
//                cell.cellStyle = commonStyle
//                if (field is String) {
//                    cell.setCellValue(field)
//                } else if (field is Int) {
//                    cell.setCellValue(field.toDouble())
//                }
//            }
//        }
//
//        // Create a drawing canvas on the sheet
//        val drawing = sheet.createDrawingPatriarch()
//        // Define anchor points in the worksheet to position the chart
//        val anchor = drawing.createAnchor(0, 0, 0, 0, 2, 0, 12, 12)
//        // Create the chart object
//        val chart = drawing.createChart(anchor)
//        chart.setTitleText(sheetName)
//        chart.titleOverlay = false
//        // 修改图表标题样式
//        val textBody = chart.title?.body
//        val paragraph = textBody?.getParagraph(0)
//        val textRun = paragraph?.textRuns?.get(0)
//        textRun?.fontSize = 15.0 // 设置字体大小
//        textRun?.fontColor = XDDFColor.from(PresetColor.BLACK) // 设置字体颜色
//        textRun?.isBold = true // 设置加粗
//
//        // 创建数据源
//        val categories: XDDFDataSource<String> = XDDFDataSourcesFactory.fromArray(arrayOf("A", "B", "C", "D"))
//        val barValues = XDDFDataSourcesFactory.fromArray(arrayOf(10.0, 20.0, 30.0, 40.0))
//        val lineValues = XDDFDataSourcesFactory.fromArray(arrayOf(10.0, 20.0, 30.0, 40.0))
//
//        // 创建图表轴
//        val bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM)
//        bottomAxis.setTitle("Category")
//        bottomAxis.orAddTextProperties.setFontSize(10.0)
//
//        // 添加左侧 Y 轴
//        val leftAxis = chart.createValueAxis(AxisPosition.LEFT)
//        leftAxis.setTitle("Bar Values222")
//        leftAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        leftAxis.crosses = AxisCrosses.MIN;
//
//        bottomAxis.crossAxis(leftAxis)
//        // 创建柱状图数据
//        val barData = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis) as XDDFBarChartData
//        val barSeries = barData.addSeries(categories, barValues) as XDDFBarChartData.Series
//        barSeries.setTitle("Bar Values111", null)
//        chart.plot(barData)
//
//        // 添加右侧 Y 轴
//        val rightAxis = chart.createValueAxis(AxisPosition.RIGHT)
//        rightAxis.setTitle("Line Values")
//        rightAxis.maximum = 100.0
//        rightAxis.crosses = AxisCrosses.MAX
//        rightAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        rightAxis.orAddTextProperties.setFontSize(10.0)
//
//        rightAxis.crossAxis(bottomAxis);
//        bottomAxis.crossAxis(rightAxis);
//        // 创建折线图数据
//        val lineData = chart.createData(ChartTypes.LINE, bottomAxis, rightAxis) as XDDFLineChartData
//        val lineSeries = lineData.addSeries(categories, lineValues)
//
//        lineSeries.setTitle("Line Values", null)
//        chart.plot(lineData)
//
//        // 自定义柱状图方向
//        barData.barDirection = BarDirection.COL
//    }
//}
