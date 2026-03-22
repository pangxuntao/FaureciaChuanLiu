package com.cainiao.chuanliu

import android.Manifest
import com.cainiao.base.BaseActivity
import com.cainiao.base.dialog.DialogUtil
import com.cainiao.base.util.ToastUtil
import com.cainiao.chuanliu.databinding.ActivityExportBinding
import com.cainiao.chuanliu.viewmodel.ExportViewModel
import com.cainiao.mycommon.utils.SoundUtil
import com.cainiao.mylibkt.mycomon.util.FormatUtil
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.permissionx.guolindev.PermissionX

class ExportActivity : BaseActivity<ExportViewModel, ActivityExportBinding>(R.layout.activity_export) {
    var startTime: Long = 0
    var endTime: Long = 0
    override fun onViewInit(binding: ActivityExportBinding) {
        binding.startTime.setOnClickListener {
            pickDate()
        }
        binding.endTime.setOnClickListener {
            pickDate()
        }
        binding.export.setOnClickListener {
            PermissionX.init(this).permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ).request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    println("pxtExport startTime2: ${FormatUtil.formatDate(startTime,"yyyy-MM-dd HH:mm:ss")}")
                    println("pxtExport endTime2: ${FormatUtil.formatDate(endTime + 24L * 60 * 60 * 1000,"yyyy-MM-dd HH:mm:ss")}")
                    viewModel.export(
                        binding.startTime.text.toString() + "_" + binding.endTime.text.toString(),
                        startTime,
                        endTime + 24L * 60 * 60 * 1000
                    )
                } else {
                    SoundUtil.ao()
                    ToastUtil.show("导出数据需要文件读写权限")
                }
            }
        }
        binding.clear.setOnClickListener {
            DialogUtil.showDialog("提示", "清空所有数据后无法恢复", {
                if(it){
                    viewModel.clearAll()
                }
            })
        }
    }

    private fun pickDate() {
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        val constraintsBuilder = CalendarConstraints.Builder()
        builder.setCalendarConstraints(constraintsBuilder.build())
        val datePicker = builder
            .setTitleText("请选择导出时间")
            .build()

        datePicker.show(supportFragmentManager, "date_picker")

        datePicker.addOnPositiveButtonClickListener { selection ->
            startTime = selection.first - 8L * 60 * 60 * 1000
            endTime = selection.second - 8L * 60 * 60 * 1000
            println("pxtExport startTime: ${FormatUtil.formatDate(startTime,"yyyy-MM-dd HH:mm:ss")}")
            println("pxtExport endTime: ${FormatUtil.formatDate(endTime,"yyyy-MM-dd HH:mm:ss")}")
            binding.startTime.text = FormatUtil.formatDate(startTime,"yyyy-MM-dd")
            binding.endTime.text = FormatUtil.formatDate(endTime,"yyyy-MM-dd")
            binding.export.isEnabled = true
        }
    }

    override fun onViewModelInit(viewModel: ExportViewModel) {

    }
}