//package com.cainiao.excel
//
//import com.cainiao.ext.formatStr
//import com.cainiao.sigemenbackend.service.ProdQualifiedVo
//import com.cainiao.sigemenbackend.service.TopPartExListVo
//import com.cainiao.sigemenbackend.service.TopPartExListVo2
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
// * @Date: 2021/08/22 19:32
// * @Description: com.cainiao.sigemenbackend.excel
// * @Version: 1.0
// */
//object PoiUtils {
//    fun writeFile1(fileName: String, topPartExList: TopPartExListVo, partExList: MutableList<TopPartExListVo2>) {
//
//        println(partExList)
//        val workbook = XSSFWorkbook()
//        val sheet = workbook.createSheet("缺陷TOP区域")
//        //设置列宽, 256代表一个字符
//        for (i in 0..30) {
//            sheet.setColumnWidth(i, 256 * 12)
//        }
//        //通用样式
//        val commonStyle = PoiHelper.createCommonStyle(workbook)
//
//        /**标题**/
//        val titleRow: Row = sheet.createRow(0)
//        val titleCell = titleRow.createCell(0)
//        // 合并单元格
//        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, 20))
//        // 设置标题样式
//        val titleStyle: CellStyle = PoiHelper.createTitleStyle(workbook)
//        titleCell.cellStyle = titleStyle
//        // 为合并单元格区域设置边框
//        for (i in 0..20) {
//            val cell = titleRow.createCell(i)
//            cell.cellStyle = titleStyle
//        }
//        // 自定义行高度
//        titleRow.heightInPoints = 40f // 根据需要调整高度
//        titleCell.setCellValue("缺陷TOP区域")
//
//        /**表格**/
//        val row1: Row = sheet.createRow(1)
//        val row2: Row = sheet.createRow(2)
//        val row3: Row = sheet.createRow(3)
//        row1.heightInPoints = 20f
//        row2.heightInPoints = 20f
//        row3.heightInPoints = 20f
//        for (i in 1..topPartExList.data.size) {
//            row1.createCell(i).apply {
//                cellStyle = commonStyle
//                setCellValue(topPartExList.data[i - 1].name)
//            }
//            row2.createCell(i).apply {
//                cellStyle = commonStyle
//                setCellValue("缺陷数量")
//            }
//            row3.createCell(i).apply {
//                cellStyle = commonStyle
//                setCellValue(topPartExList.data[i - 1].qty.toDouble())
//            }
//        }
//        row1.createCell(0).apply {
//            setCellValue("分区")
//            cellStyle = commonStyle
//        }
//        row3.createCell(0).apply {
//            setCellValue("数量")
//            cellStyle = commonStyle
//        }
//        row1.createCell(20).apply {
//            setCellValue("总数")
//            cellStyle = commonStyle
//        }
//        row2.createCell(20).apply {
//            cellStyle = commonStyle
//        }
//        row3.createCell(20).apply {
//            setCellValue("数量")
//            cellStyle = commonStyle
//        }
//        //合并单元格
//        sheet.addMergedRegion(CellRangeAddress(1, 2, 0, 0))
//        sheet.addMergedRegion(CellRangeAddress(1, 2, 20, 20))
//
//        /**图表**/
//        // Create a drawing canvas on the sheet
//        val drawing = sheet.createDrawingPatriarch()
//        // Define anchor points in the worksheet to position the chart
//        val anchor = drawing.createAnchor(0, 0, 0, 0, 0, 4, 21, 34)
//        // Create the chart object
//        val chart = drawing.createChart(anchor)
//        chart.setTitleText("分区累计缺陷")
//        chart.titleOverlay = false
//        // 修改图表标题样式
//        val textBody = chart.title?.body
//        val paragraph = textBody?.getParagraph(0)
//        val textRun = paragraph?.textRuns?.get(0)
//        textRun?.fontSize = 10.0 // 设置字体大小
//        textRun?.fontColor = XDDFColor.from(PresetColor.BLACK) // 设置字体颜色
//        textRun?.isBold = true // 设置加粗
//
//        val sortData = topPartExList.data.sortedByDescending { it.qty }
//        // 创建数据源
//        val categories: XDDFDataSource<String> = XDDFDataSourcesFactory.fromArray(
//            sortData.map { it.name }.toTypedArray()
//        )
//        val barValues = XDDFDataSourcesFactory.fromArray(
//            sortData.map { it.qty }.toTypedArray()
//        )
//        val lineValues = XDDFDataSourcesFactory.fromArray(
//            sortData.map {
//                if (it.total != 0)
//                    ((it.total - it.qty) * 1.0 / it.total) * 100
//                else 100
//            }.toTypedArray()
//        )
//
//        // 创建图表轴
//        val bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM)
//        bottomAxis.setTitle("分区")
//        bottomAxis.orAddTextProperties.setFontSize(10.0)
//
//        // 添加左侧 Y 轴
//        val leftAxis = chart.createValueAxis(AxisPosition.LEFT)
//        leftAxis.setTitle("数量")
//        leftAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        leftAxis.crosses = AxisCrosses.MIN;
//
//        bottomAxis.crossAxis(leftAxis)
//        // 创建柱状图数据
//        val barData = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis) as XDDFBarChartData
//        val barSeries = barData.addSeries(categories, barValues) as XDDFBarChartData.Series
//        barSeries.setTitle("数量", null)
//        chart.plot(barData)
//
//        // 添加右侧 Y 轴
//        val rightAxis = chart.createValueAxis(AxisPosition.RIGHT)
//        rightAxis.setTitle("合格率")
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
//        lineSeries.setTitle("合格率", null)
//        chart.plot(lineData)
//        // 自定义柱状图方向
//        barData.barDirection = BarDirection.COL
//
//        //写其他sheet
//        for (exListVo2 in partExList) {
//            if (exListVo2.data.isEmpty()) continue
//            writeSheet(workbook, exListVo2)
//        }
//        println(topPartExList)
//        println(partExList)
//        // 保存工作簿到文件
//        try {
//            FileOutputStream(fileName).use { fileOut -> workbook.write(fileOut) }
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun writeSheet(
//        workbook: XSSFWorkbook,
//        vo: TopPartExListVo2,
//    ) {
//        val partName = vo.partName
//        val sheet = workbook.createSheet("$partName")
//        //设置列宽, 256代表一个字符
//        for (i in 0..2) {
//            sheet.setColumnWidth(i, 256 * 16)
//        }
//        val titleStyle1 = PoiHelper.createTitleStyle(workbook, 16)
////        titleStyle1.fillForegroundColor = IndexedColors.SKY_BLUE.getIndex()
//        val titleStyle2 = PoiHelper.createTitleStyle(workbook, 16)
////        titleStyle1.fillForegroundColor = IndexedColors.LIGHT_BLUE.getIndex()
//
//        // 创建标题行并合并单元格
//        val titleRow: Row = sheet.createRow(0)
//        val titleCell = titleRow.createCell(0)
//        val titleCell2 = titleRow.createCell(1)
//        titleCell.setCellValue("缺陷类型")
//        titleCell2.setCellValue(partName)
//        titleCell.cellStyle = titleStyle1
//        titleCell2.cellStyle = titleStyle2
//        // 自定义行高度
//        titleRow.heightInPoints = 30f // 根据需要调整高度
//        val data = vo.data.map {
//            arrayOf(it.exName, it.qty)
//        }.toTypedArray()
//        // 创建示例数据
//        val commonStyle = PoiHelper.createCommonStyle(workbook)
////        commonStyle.fillForegroundColor = IndexedColors.WHITE.getIndex()
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
//        chart.setTitleText(partName)
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
//        val categories: XDDFDataSource<String> = XDDFDataSourcesFactory.fromArray(
//            vo.data.map { it.exName }.toTypedArray()
//        )
//        val barValues = XDDFDataSourcesFactory.fromArray(
//            vo.data.map { it.qty * 1.0 }.toTypedArray()
//        )
//        val lineValues = XDDFDataSourcesFactory.fromArray(
//            vo.data.map {
//                if (it.qty == 0)
//                    100
//                else
//                    (vo.total - it.qty) * 1.0 / vo.total * 100
//            }.toTypedArray()
//        )
//
//        // 创建图表轴
//        val bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM)
//        bottomAxis.setTitle("异常")
//        bottomAxis.orAddTextProperties.setFontSize(10.0)
//
//        // 添加左侧 Y 轴
//        val leftAxis = chart.createValueAxis(AxisPosition.LEFT)
//        leftAxis.setTitle("数量")
//        leftAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        leftAxis.crosses = AxisCrosses.MIN;
//
//        bottomAxis.crossAxis(leftAxis)
//        // 创建柱状图数据
//        val barData = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis) as XDDFBarChartData
//        val barSeries = barData.addSeries(categories, barValues) as XDDFBarChartData.Series
//        barSeries.setTitle("异常", null)
//        chart.plot(barData)
//
//        // 添加右侧 Y 轴
//        val rightAxis = chart.createValueAxis(AxisPosition.RIGHT)
//        rightAxis.setTitle("合格率")
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
//        lineSeries.setTitle("合格率", null)
//        chart.plot(lineData)
//
//        // 自定义柱状图方向
//        barData.barDirection = BarDirection.COL
//    }
//
//    fun writeFile2(fileName: String, vo: ProdQualifiedVo) {
//        println(vo)
//        val workbook = XSSFWorkbook()
//        val sheet = workbook.createSheet("产能&合格率统计")
//        //设置列宽, 256代表一个字符
//        for (i in 0..30) {
//            sheet.setColumnWidth(i, 256 * 16)
//        }
//        //通用样式
//        val commonStyle = PoiHelper.createCommonStyle(workbook)
//
//        /**标题**/
//        val titleRow: Row = sheet.createRow(0)
//        val titleCell = titleRow.createCell(0)
//        // 合并单元格
//        sheet.addMergedRegion(CellRangeAddress(0, 0, 0, vo.colCount))
//        // 设置标题样式
//        val titleStyle: CellStyle = PoiHelper.createTitleStyle(workbook)
//        titleCell.cellStyle = titleStyle
//        // 为合并单元格区域设置边框
//        for (i in 0..vo.colCount) {
//            val cell = titleRow.createCell(i)
//            cell.cellStyle = titleStyle
//        }
//        // 自定义行高度
//        titleRow.heightInPoints = 40f // 根据需要调整高度
//        titleCell.setCellValue("产能&合格率统计")
//        /**表格**/
//        for (i in 0 until vo.data.size) {
//            val data = vo.data[i]
//            sheet.createRow(i + 1).apply {
//                heightInPoints = 20f
//                for (coli in 0 until data.size) {
//                    val cell = createCell(coli)
//                    cell.cellStyle = commonStyle
//                    cell.setCellValue(data["col${coli}"])
//                }
//            }
//        }
//
//        /**图表**/
//        // Create a drawing canvas on the sheet
//        val drawing = sheet.createDrawingPatriarch()
//        // Define anchor points in the worksheet to position the chart
//        val anchor = drawing.createAnchor(0, 0, 0, 0, 0, 8, vo.colCount + 1, 37)
//        // Create the chart object
//        val chart = drawing.createChart(anchor)
//        chart.setTitleText("产能&合格率统计表")
//        chart.titleOverlay = false
//        // 修改图表标题样式
//        val textBody = chart.title?.body
//        val paragraph = textBody?.getParagraph(0)
//        val textRun = paragraph?.textRuns?.get(0)
//        textRun?.fontSize = 10.0 // 设置字体大小
//        textRun?.fontColor = XDDFColor.from(PresetColor.BLACK) // 设置字体颜色
//        textRun?.isBold = true // 设置加粗
//
//        val sortData = vo.data2.sortedBy { it.date }
//        // 创建数据源
//        val categories = XDDFDataSourcesFactory.fromArray(
//            sortData.map { it.date }.toTypedArray()
//        )
//        val barValues1 = XDDFDataSourcesFactory.fromArray(
//            sortData.map { it.planQty }.toTypedArray()
//        )
//        val barValues2 = XDDFDataSourcesFactory.fromArray(
//            sortData.map { it.realQty }.toTypedArray()
//        )
//        val lineValues1 = XDDFDataSourcesFactory.fromArray(
//            sortData.map {
//                it.planRate.replace("%", "").toFloat()
//            }.toTypedArray()
//        )
//        val lineValues2 = XDDFDataSourcesFactory.fromArray(
//            sortData.map {
//                it.realRate.replace("%", "").toFloat() * 1
//            }.toTypedArray()
//        )
//
//        // 创建图表轴
//        val bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM)
//        bottomAxis.setTitle("日期")
//        bottomAxis.orAddTextProperties.setFontSize(10.0)
//
//        // 添加左侧 Y 轴
//        val leftAxis = chart.createValueAxis(AxisPosition.LEFT)
//        leftAxis.setTitle("数量")
//        leftAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        leftAxis.crosses = AxisCrosses.MIN;
//        bottomAxis.crossAxis(leftAxis)
//        // 创建柱状图数据
//        val barData = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis) as XDDFBarChartData
//        val barSeries1 = barData.addSeries(categories, barValues1) as XDDFBarChartData.Series
//        val barSeries2 = barData.addSeries(categories, barValues2) as XDDFBarChartData.Series
//        barSeries1.setTitle("预计生产数量", null)
//        barSeries2.setTitle("实际生产数量", null)
//        chart.plot(barData)
//
//        // 添加右侧 Y 轴
//        val rightAxis = chart.createValueAxis(AxisPosition.RIGHT)
//        rightAxis.setTitle("合格率")
//        rightAxis.maximum = 100.0
//        rightAxis.crosses = AxisCrosses.MAX
//        rightAxis.crossBetween = AxisCrossBetween.BETWEEN;
//        rightAxis.orAddTextProperties.setFontSize(10.0)
//
//        rightAxis.crossAxis(bottomAxis);
//        bottomAxis.crossAxis(rightAxis);
//        // 创建折线图数据
//        val lineData = chart.createData(ChartTypes.LINE, bottomAxis, rightAxis) as XDDFLineChartData
//        val lineSeries1 = lineData.addSeries(categories, lineValues1)
//        val lineSeries2 = lineData.addSeries(categories, lineValues2)
//        lineSeries1.setTitle("目标合格率", null)
//        lineSeries2.setTitle("实际合格率", null)
//        chart.plot(lineData)
//
//        // 自定义柱状图方向
//        barData.barDirection = BarDirection.COL
//        FileOutputStream(fileName).use { fileOut -> workbook.write(fileOut) }
//    }
//}
