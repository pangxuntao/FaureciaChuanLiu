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
class OrderItemService : ServiceImpl<OrderItemMapper, TbOrderItem>() {

}
