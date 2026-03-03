package com.cainiao.chuanliu

import android.content.Context
import android.os.Environment
import com.cainiao.base.App
import com.cainiao.base.util.FileUtils
import com.cainiao.base.util.L
import com.cainiao.chuanliu.entities.Data
import com.cainiao.chuanliu.room.ScanBean
import com.cainiao.mycommon.action.CallableAction
import com.cainiao.myexcel.ExcelUtil
import com.cainiao.myexcel.MyExcel
import java.io.File

object FileUtil {
    val PATH = "${App.app.getExternalFilesDir("")}"
    val EXPORT = "$PATH/export"
    val IMPORT = "$PATH/import"

    init {
        initPath(IMPORT)
        initPath(EXPORT)
    }

    fun initPath(path: String): String {
        L.e("pxt getSDCardPathPATH:${path}")
        val mkdirs = File(path).mkdirs()
        L.e("pxt mkdirs:$mkdirs")
        val createPath = FileUtils.createPath(path)
        L.e("pxt createPath:$createPath")
        FileUtils.notifyFileCreate(App.context, File(path))
        return path
    }

    fun export(date: String, data: List<ScanBean>, onSuccess: (Boolean) -> Unit) {
        val dataPath = "$PATH/$date.xlsx"
        MyExcel.write(dataPath, ScanBean::class.java, data) {
            L.e(dataPath)
            L.e(it)
            onSuccess.invoke(it == true)
            return@write true
        }.start()
    }

    private fun getFileRoot(context: Context): String {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            val external = context.getExternalFilesDir(null)
            if (external != null) {
                return external.absolutePath
            }
        }
        return context.filesDir.absolutePath
    }

    fun readDataFromFile(callback: CallableAction.Callback<List<Data>?>) {
        val dataList: MutableList<Data> = ArrayList<Data>()
        var file = File("$IMPORT/import.xlsx")
        if (!file.exists()) {
            callback.onResult(dataList)
            return
        }
        L.i()
        ExcelUtil.read(file.path, Data::class.java) { data ->
            L.i(data)
            dataList.addAll(data)
            callback.onResult(dataList)
            true
        }.start()
    }
}