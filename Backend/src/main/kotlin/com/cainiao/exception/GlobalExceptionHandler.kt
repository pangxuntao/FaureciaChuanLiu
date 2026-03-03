package com.cainiao.exception

import ch.qos.logback.classic.Logger
import com.cainiao.common.ResultBean
import com.cainiao.ext.logger
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.stereotype.Component
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest

/**
 * @Auther: pxt
 * @Date: 2021/08/22 22:53
 * @Description: com.cainiao.exception
 * @Version: 1.0
 */
@RestControllerAdvice
@Component
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(this::class.java) as Logger

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResultBean {
        e.printStackTrace()
        logger.logger(e.message ?: "")
        return ResultBean.error(
            -1,
            e.message
        )
    }


    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handle(e: MissingServletRequestParameterException, request: HttpServletRequest): ResultBean {
        logger.logger("请求:${request.requestURI}, 缺少必要参数:${e.message?.split("\'")?.get(1)}")
        e.printStackTrace()
        return ResultBean.error(
            -1,
            "请求:${request.requestURI}, 缺少必要参数:${e.message?.split("\'")?.get(1)}"
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handle(e: HttpMessageNotReadableException, request: HttpServletRequest): ResultBean {
        e.printStackTrace()
        logger.logger("请求:${request.requestURI}, 缺少请求体")
        return ResultBean.error(
            -1,
            "请求:${request.requestURI}, 缺少请求体"
        )
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handle(e: HttpRequestMethodNotSupportedException, request: HttpServletRequest): ResultBean {
        e.printStackTrace()
        logger.logger("请求:${request.requestURI}, 不支持的请求方法:${e.message?.split("\'")?.get(1)}")
        return ResultBean.error(
            -1,
            "请求:${request.requestURI}, 不支持的请求方法:${e.message?.split("\'")?.get(1)}"
        )
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handle(e: DuplicateKeyException, request: HttpServletRequest): ResultBean {
        e.printStackTrace()
        var msg = "请求:${request.requestURI}, 索引冲突:${e.message}"
        logger.logger(msg)

        val split = e.message?.split("'")
        split?.let {
            if (it.size > 3) {
                msg = "请求:${request.requestURI}, 索引冲突:${it[3]}-${it[1]}"
            }
        }
        return ResultBean.error(
            -1,
            msg
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handle(e: DataIntegrityViolationException, request: HttpServletRequest): ResultBean {
        e.printStackTrace()
        val message = e.message
        var msg = "请求:${request.requestURI}, 数据库异常:$message"

        if (message?.contains(":") == true) {
            val index = message.lastIndexOf(":")
            if (index > -1) {
                msg = "请求:${request.requestURI}, 缺少默认值${message.substring(index)}"
            }
        }

        logger.logger(msg)
        return ResultBean.error(
            -1,
            msg
        )
    }
}
