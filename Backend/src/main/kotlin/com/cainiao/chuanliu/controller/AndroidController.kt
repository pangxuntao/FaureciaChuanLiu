package com.cainiao.chuanliu.controller

import com.cainiao.chuanliu.entities.vo.CurrentDataVo
import com.cainiao.chuanliu.entities.vo.OrderVo
import com.cainiao.chuanliu.service.OrderService
import com.cainiao.chuanliu.service.ShelfService
import com.cainiao.common.ResultBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/android")
class AndroidController {
    @Autowired
    lateinit var shelfService: ShelfService

    @Autowired
    lateinit var orderService: OrderService

    @PostMapping("/currentData")
    fun currentData() = ResultBean.ok(
        CurrentDataVo(
            shelfList = shelfService.shelfList(),
            currentOrders = orderService.currentOrders()
        )
    )

    @PostMapping("/newOrder")
    fun newOrder() = ResultBean.ok(orderService.newOrder())

    @PostMapping("/createOrder")
    fun createOrder(@RequestBody orderVo: OrderVo) = ResultBean.ok(
        orderService.createOrder(orderVo),
        "保存成功"
    )
}