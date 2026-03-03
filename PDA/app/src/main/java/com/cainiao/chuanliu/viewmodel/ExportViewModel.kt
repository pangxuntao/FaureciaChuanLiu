package com.cainiao.chuanliu.viewmodel

import androidx.lifecycle.viewModelScope
import com.cainiao.base.App
import com.cainiao.base.BaseViewModel
import com.cainiao.base.dialog.DialogUtil
import com.cainiao.base.dialog.ProgressDialog
import com.cainiao.base.util.L
import com.cainiao.base.util.ToastUtil
import com.cainiao.chuanliu.FileUtil
import com.cainiao.mycommon.utils.SoundUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExportViewModel : BaseViewModel() {
    private val userDao by lazy {
        App.database.scanDao()
    }

    fun export(name: String, start: Long, end: Long) {
        ProgressDialog.show(msg = "导出中", cancelable = false)
        viewModelScope.launch(Dispatchers.IO) {
            val allScanBean = userDao.getAllScanBean(start, end)
            allScanBean.forEach {
                it.stringTime = formatDate(it.time)
            }
            L.e(allScanBean)
            if (allScanBean.isEmpty()) {
                ProgressDialog.dismiss()
                DialogUtil.showOneButtonDialog("提示", "没有要导出的数据")
            } else {
                FileUtil.export(name.replace("-", ""), allScanBean) {
                    if (it) {
                        SoundUtil.success()
                        ToastUtil.show("数据保存成功")
                    } else {
                        ToastUtil.show("数据保存失败")
                        SoundUtil.success()
                    }
                    ProgressDialog.dismiss()
                }
            }
        }
    }

    private fun formatDate(date: Long?): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(date)
    }

    fun clearAll() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.clear()
            ToastUtil.show("清空完成.")
        }
    }
}