package com.cainiao.common

import lombok.Data

@Data
class ResultBean private constructor(val code: Int?, val data: Any?, val msg: String?) {
    companion object {
        fun of(success: Boolean, msg: String?): ResultBean {
            return if (success)
                ResultBean(1, null, "${msg}成功")
            else
                ResultBean(3000, null, "${msg}失败")
        }

        fun ok(data: Any?): ResultBean {
            return ResultBean(1, data, "请求成功")
        }

        fun ok(data: Any?, msg: String?): ResultBean {
            return ResultBean(1, data, msg)
        }

        fun error(data: Any?, msg: String?): ResultBean {
            return ResultBean(3000, data, msg)
        }

        fun error(code: Int, msg: String?): ResultBean {
            return ResultBean(code, null, msg)
        }

        fun create(code: Int, data: Any?, msg: String?): ResultBean {
            return ResultBean(code, data, msg)
        }
    }
}