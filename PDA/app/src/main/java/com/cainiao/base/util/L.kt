package com.cainiao.base.util

import android.content.Context
import android.util.Log
import com.cainiao.base.App
import com.cainiao.mylibkt.mycomon.util.FormatUtil
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ExecutorService

object L {
    private var ENABLE = true
    private var ENABLE_E = false
    private var LEVEL = Level.DEBUG
    private var SAVE = false
    private var DIR = ""
    private var NAME = ""
    private const val TAG = "pxtL"
    private var threadPool: ExecutorService? = null
    private lateinit var fileName: String

    @Volatile
    private var logCount = 0
    private var bos: BufferedOutputStream? = null

    init {
//        ENABLE = ENABLE and BuildConfig.DEBUG
//        var context = BaseApplication.context
//        if (context != null) {
//            var classList:List<Class<?>> = AnnotationUtil.getClassesWithAnnotation context,
//            "com.cainiao", LogSetting.class)
//            Log.i(TAG, classList.size() + " (L.java:49)")
//            if (classList.size() > 0) {
//                Class<?> clazz = classList . get 0
//                LogSetting logBean = AnnotationUtil . getAnnotation clazz, LogSetting.class)
//                ENABLE_E = logBean.enableE()
//                ENABLE = logBean.value()
//                LEVEL = logBean.level()
//                SAVE = logBean.save()
//                DIR = logBean.path()
//                NAME = logBean.name()
//                DIR = if (TextUtils.isEmpty(DIR)) {
//                    FileUtils.getSDCardPath() + "/" + context.getPackageName()
//                } else {
//                    FileUtils.getSDCardPath() + "/" + DIR
//                }
//                if (TextUtils.isEmpty(NAME)) {
//                    NAME = "error"
//                }
//                if (!ENABLE) {
//                    Log.e(TAG, "已关闭日志输出")
//                } else if (SAVE) {
//                    threadPool = ThreadPoolUtil.newSinglePool()
//                    createFile(context)
//                }
//            }
//        }
    }

    private fun createFile(context: Context?) {
        val formatDate: String = FormatUtil.formatDate(Date(), "yyyyMMdd")
        fileName = "$DIR/$NAME-$formatDate.log"
        Log.v(TAG, "fileName: $fileName")
        val path = File(DIR)
        val file = File(fileName)
        try {
            if (!path.exists()) {
                path.mkdirs()
                if (context != null) {
                    FileUtils.notifyFileCreate(context, path)
                }
            }
            if (!file.exists()) {
                file.createNewFile()
                if (context != null) {
                    FileUtils.notifyFileCreate(context, file)
                }
            }
            bos = BufferedOutputStream(FileOutputStream(file, true))
        } catch (e: IOException) {
            e.printStackTrace()
            SAVE = false
            Log.e(TAG, "", e)
        }
    }

    fun v() {
        if (ENABLE) {
            Logger.v(TAG, getTrace())
        }
    }

    fun v(msg: String) {
        if (ENABLE) {
            Logger.v(TAG, msg + "  " + getTrace())
        }
    }

    fun v(obj: Any?) {
        if (ENABLE) {
            Logger.v(TAG, obj?.toString() + "  " + getTrace())
        }
    }

    fun v(tag: String?, msg: String) {
        if (ENABLE) {
            Logger.v(tag, msg + "  " + getTrace())
        }
    }

    fun trace() {
        if (ENABLE) {
            Logger.i(TAG, getTrace())
        }
    }

    fun i() {
        if (ENABLE) {
            Logger.i(TAG, getTrace())
        }
    }

    fun i(msg: String) {
        if (ENABLE) {
            Logger.i(TAG, msg + "  " + getTrace())
        }
    }

    fun i(obj: Any?) {
        if (ENABLE) {
            Logger.i(TAG, obj?.toString() + "  " + getTrace())
        }
    }

    fun i(tag: String, msg: String) {
        if (ENABLE) {
            Logger.i(tag, msg + "  " + getTrace())
        }
    }

    fun d() {
        if (ENABLE) {
            Logger.d(TAG, getTrace())
        }
    }

    fun d(msg: String) {
        if (ENABLE) {
            Logger.d(TAG, msg + "  " + getTrace())
        }
    }

    fun d(obj: Any?) {
        if (ENABLE) {
            Logger.d(TAG, obj?.toString() + "  " + getTrace())
        }
    }

    fun d(tag: String, msg: String) {
        if (ENABLE) {
            Logger.d(tag, msg + "  " + getTrace())
        }
    }

    fun e() {
        if (ENABLE) {
            Logger.e(TAG, getTrace())
        }
    }

    fun e(s: String) {
        if (ENABLE) {
            Logger.e(TAG, s + " " + getTrace())
        }
    }

    fun e(obj: Any?) {
        if (ENABLE) {
            if (obj is java.lang.reflect.Array) {
                Logger.e(TAG, Arrays.toString(obj as Array<Any?>?) + " " + getTrace())
            } else {
                Logger.e(TAG, obj.toString() + " " + getTrace())
            }
        }
    }

    fun e(e: Throwable) {
        if (ENABLE || ENABLE_E) {
            Logger.e(TAG, "", e)
        }
    }

    fun e(tag: String, msg: Any?) {
        if (ENABLE) {
            Logger.e(tag, msg?.toString() + "  " + getTrace())
        }
    }

    fun e(tag: String, msg: String) {
        if (ENABLE) {
            Logger.e(tag, msg + "  " + getTrace())
        }
    }

    fun e(msg: String, e: Throwable) {
        if (ENABLE || ENABLE_E) {
            Logger.e(TAG, "$msg  ", e)
        }
    }

    fun e(tag: String, msg: String, e: Throwable) {
        if (ENABLE || ENABLE_E) {
            Logger.e(tag, "$msg  ", e)
        }
    }

    private fun getTrace(): String {
        val element = getTargetStackTraceElement()
        return if (element == null) "" else " " + element.methodName + "()(" + element.fileName + ":" + element.lineNumber + ")"
    }

    private fun getTargetStackTraceElement(): StackTraceElement? {
        var targetStackTrace: StackTraceElement? = null
        var shouldTrace = false
        var shouldStop = false
        val stackTrace = Thread.currentThread().stackTrace
        for (stackTraceElement in stackTrace) {
            //找到来自L.kt 的track
            if (stackTraceElement.className == L::class.java.name) {
                shouldTrace = true
            }
            //L.kt 的下一个trace，不过有可能是Log.kt, 先暂时保存 targetStackTrace
            if (shouldTrace && stackTraceElement.className != L::class.java.name && targetStackTrace == null) {
                targetStackTrace = stackTraceElement
            }
            //如果有来自 LogKt 的trace, 则继续找下一个 trace
            if (stackTraceElement.className == "com.cainiao.mylibkt.mycomon.extension.LogExtensionKt") {
                shouldStop = true
            }
            if (shouldStop && stackTraceElement.className != "com.cainiao.mylibkt.mycomon.extension.LogExtensionKt") {
                targetStackTrace = stackTraceElement
                break
            }
        }
        return targetStackTrace
    }

    internal object Logger {
        fun v(tag: String?, msg: String) {
            try {
                if (LEVEL.asInt() > Level.TRACE.asInt()) {
                    return
                }
                Log.v(tag, msg)
            } catch (e: RuntimeException) {
                printSingleColor(tag, 36, 0, msg)
            }
        }

        fun d(tag: String, msg: String) {
            try {
                if (LEVEL.asInt() > Level.DEBUG.asInt()) {
                    return
                }
                Log.d(tag, msg)
                save("d/$tag $msg", false)
            } catch (e: RuntimeException) {
                printSingleColor(tag, 34, 0, msg)
            }
        }

        fun i(tag: String, msg: String) {
            try {
                if (LEVEL.asInt() > Level.INFO.asInt()) {
                    return
                }
                Log.i(tag, msg)
                save("i/$tag $msg", false)
            } catch (e: RuntimeException) {
                printSingleColor(tag, 33, 0, msg)
            }
        }

        fun e(tag: String, msg: String) {
            try {
                Log.e(tag, msg)
                save("e/$tag $msg", false)
            } catch (e: RuntimeException) {
                System.err.println("$tag---$msg")
            }
        }

        fun e(tag: String, msg: String, throwable: Throwable) {
            try {
                Log.e(tag, msg, throwable)
                save("e/" + tag + " " + msg + "" + throwable.message + " " + getTrace(), false)
            } catch (e: RuntimeException) {
                System.err.println("$tag---$msg")
                throwable.printStackTrace()
            }
        }

        fun save(msg: String) {
            save("s/ $msg", true)
        }

        private fun save(msg: String, force: Boolean) {
            if (SAVE || force) {
                threadPool!!.execute(
                    WriteRunnable(
                        """
                    ${msg.trim { it <= ' ' }}
                    
                    
                    """.trimIndent()
                    )
                )
            }
        }

        /**
         * @param tag     前面的字符
         * @param code    颜色代号：背景颜色代号(41-46)；前景色代号(31-36)
         * @param n       数字+m：1加粗；3斜体；4下划线
         * @param content 要打印的内容
         */
        private fun printSingleColor(tag: String?, code: Int, n: Int, content: String) {
            System.out.format("%s---\u001b[%d;%dm%s", tag, code, n, content)
            System.out.format("\n\u001b[%d;%dm", 30, 0)
        }

        private class WriteRunnable internal constructor(var log: String) : Runnable {
            override fun run() {
                logCount++
                if (bos == null) {
                    synchronized(L::class.java) {
                        if (bos == null) {
                            createFile(App.context)
                        }
                    }
                }
                if (logCount >= 10000) {
                    logCount = 0
                    var file = File(fileName)
                    if (file.length() > 10 * 1024 * 1024) {
                        val formatDate: String = FormatUtil.formatDate(Date(), "yyyyMMdd")
                        fileName = "$DIR/$NAME-$formatDate.log"
                        file = File(fileName)
                        if (file.exists()) {
                            var i = 1
                            while (file.exists()) {
                                fileName = DIR + "/" + NAME + "-" + formatDate + ++i + ".log"
                                file = File(fileName)
                            }
                        }
                        try {
                            bos!!.flush()
                            file.createNewFile()
                            bos = BufferedOutputStream(FileOutputStream(file, true))
                        } catch (e: IOException) {
                            e.printStackTrace()
                            SAVE = false
                        }
                    }
                }
                try {
                    val formatDate: String =
                        FormatUtil.formatDate(Date(), "yyyy-MM-dd HH:mm:ss.SSS ")
                    bos!!.write(formatDate.toByteArray())
                    bos!!.write(log.toByteArray())
                    bos!!.flush()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e(TAG, "", e)
                }
            }
        }
    }
}