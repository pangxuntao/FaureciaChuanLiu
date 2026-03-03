package com.cainiao.chuanliu.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.cainiao.chuanliu.entities.TbShelf
import com.cainiao.chuanliu.entities.TbOrderItem
import com.cainiao.chuanliu.entities.TbOrder

interface ShelfMapper : BaseMapper<TbShelf>
interface OrderMapper : BaseMapper<TbOrder>
interface OrderItemMapper : BaseMapper<TbOrderItem>
