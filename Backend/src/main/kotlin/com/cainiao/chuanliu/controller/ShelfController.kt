package com.cainiao.chuanliu.controller

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.cainiao.chuanliu.entities.TbShelf
import com.cainiao.chuanliu.service.ShelfService
import com.cainiao.common.ResultBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/shelf")
class ShelfController {
    @Autowired
    lateinit var service: ShelfService

    @PostMapping("/list")
    fun list() = ResultBean.ok(service.shelfList())

    @RequestMapping("/downLoadModule")
    fun downLoadModule(response: HttpServletResponse) = service.downLoadModule(response)

    @PostMapping("/import")
    fun import(@RequestParam("file") file: MultipartFile) = service.import(file)

    @PostMapping("/add")
    fun add(@RequestBody tbShelf: TbShelf) = ResultBean.of(
        success = service.save(tbShelf.apply { createTime = Date() }),
        msg = "添加"
    )

    @PostMapping("/update")
    fun update(@RequestBody tbShelf: TbShelf) = ResultBean.of(
        success = service.updateById(tbShelf),
        msg = "更新"
    )

    @PostMapping("/delete")
    fun delete(@RequestParam("uuid") uuid: String) = ResultBean.of(
        success = service.removeById(uuid),
        msg = "删除"
    )
}