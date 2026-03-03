package com.cainiao.chuanliu.service

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.cainiao.chuanliu.entities.vo.OrderVo
import com.cainiao.chuanliu.entities.TbOrder
import com.cainiao.chuanliu.entities.TbOrderItem
import com.cainiao.chuanliu.mapper.OrderItemMapper
import com.cainiao.chuanliu.mapper.OrderMapper
import com.cainiao.ext.isNullOrEmpty
import com.cainiao.ext.toSnakeCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Service
class OrderService : ServiceImpl<OrderMapper, TbOrder>() {
    @Autowired
    lateinit var orderItemService: OrderItemService

    fun currentOrders(): List<OrderVo> {
        val orders = list(QueryWrapper<TbOrder>().apply {
            `in`(TbOrder::status.name, 0, 1, 2)
        })
        if (orders.isNullOrEmpty()) return emptyList()
        return orders.map {
            OrderVo(
                order = it, orderItems = orderItemService.list(
                    QueryWrapper<TbOrderItem>().apply {
                        eq(TbOrderItem::orderId.name.toSnakeCase(), it.uuid)
                    }
                ))
        }.toList()
    }

    fun newOrder(): TbOrder {
        val startOfToday: LocalDateTime = LocalDate.now().atStartOfDay()
        val startOfTomorrow: LocalDateTime = LocalDate.now().plusDays(1).atStartOfDay()
        val one = getOne(QueryWrapper<TbOrder>().apply {
            ge(TbOrder::createTime.name.toSnakeCase(), startOfToday)
            lt(TbOrder::createTime.name.toSnakeCase(), startOfTomorrow)
            orderByDesc(TbOrder::createTime.name.toSnakeCase())
            last("LIMIT 1")
        })
        val now = LocalDate.now()
        val todayStr = now.format(DateTimeFormatter.ofPattern("yyMMdd"))
        val orderCode = if (one == null) {
            "ORD-${todayStr}-001"
        } else {
            val num = one.orderCode!!.split("-")[2].toInt() + 1
            "ORD-${todayStr}-${num.toString().padStart(3, '0')}"
        }
        val tbOrder = TbOrder(
            orderCode = orderCode,
            createTime = Date(),
            createBy = "",
            status = -1,
        )
        save(tbOrder) || throw RuntimeException("订单保存失败")
        return tbOrder
    }

    @Transactional
    fun createOrder(orderVo: OrderVo): Boolean {
        save(orderVo.order.apply {
            createTime = Date()
            status = 0
        }) || throw RuntimeException("订单保存失败")

        orderItemService.saveBatch(orderVo.orderItems) || throw RuntimeException("订单项保存失败")
        return true
    }

}
