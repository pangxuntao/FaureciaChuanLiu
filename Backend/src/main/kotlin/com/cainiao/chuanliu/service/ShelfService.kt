package com.cainiao.chuanliu.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.cainiao.chuanliu.entities.TbShelf
import com.cainiao.chuanliu.mapper.ShelfMapper
import com.cainiao.common.ResultBean
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.servlet.http.HttpServletResponse

@Service
class ShelfService : ServiceImpl<ShelfMapper, TbShelf>() {
    @Autowired
    lateinit var objectMapper: ObjectMapper

    fun shelfList(): List<TbShelf> {
        return list(QueryWrapper<TbShelf>().apply {
            ne(TbShelf::deleted.name, true)
        })
    }

    @Transactional
    fun import(file: MultipartFile): ResultBean {
        if (file.isEmpty) {
            return ResultBean.error(3000, "文件不能为空")
        }
        file.inputStream.use {
            val shelfList = mutableListOf<TbShelf>()
            val workbook = XSSFWorkbook(it)
            val sheet: Sheet = workbook.getSheetAt(0)
            val rowIterator = sheet.iterator()
            val date = Date()
            while (rowIterator.hasNext()) {
                try {
                    val rowData: MutableList<String> = ArrayList()
                    val row = rowIterator.next()
                    val cellIterator = row.cellIterator()
                    while (cellIterator.hasNext()) {
                        val cell = cellIterator.next()
                        rowData.add(cell.toString())
                    }
                    if (rowData.size >= 3) {
                        shelfList.add(
                            TbShelf(
                                shelfCode = rowData[0],
                                materialName = rowData[1],
                                materialQty = rowData[2].toDouble().toInt(),
                                materialCode = rowData[3],
                                createTime = date,
                                deleted = false,
                            )
                        )
                    }
                } catch (e: Exception) {
                    println(e.message)
                    e.printStackTrace()
                }
            }
            if (shelfList.isNotEmpty()) {
                remove(QueryWrapper())
                saveBatch(shelfList)
            }
        }
        return ResultBean.ok("导入成功")
    }

    fun downLoadModule(response: HttpServletResponse) {
        val resource = ClassPathResource("货架导入模板.xlsx")
        val fileName = resource.uri.toString().substring(resource.uri.toString().lastIndexOf("/") + 1)
        response.characterEncoding = "UTF-8";
        response.contentType = "application/vnd.ms-excel"
        //默认Excel名称
        response.setHeader("Content-Disposition", "attachment;fileName=$fileName")
        response.flushBuffer()

        resource.inputStream.use {
            // 将输入流写入输出流
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (it.read(buffer).also { bytesRead = it } != -1) {
                response.outputStream.write(buffer, 0, bytesRead)
            }
            response.outputStream.flush()
            response.outputStream.close()
        }
    }
}
