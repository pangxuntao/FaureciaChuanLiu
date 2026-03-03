package com.cainiao.chuanliu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import com.cainiao.base.BaseActivity
import com.cainiao.base.ext.isNullOrEmpty
import com.cainiao.base.util.SPUtil
import com.cainiao.chuanliu.databinding.ActivityScanBinding
import com.cainiao.chuanliu.viewmodel.ScanViewModel
import com.cainiao.mylibkt.mycomon.util.FormatUtil

class ScanActivity : BaseActivity<ScanViewModel, ActivityScanBinding>(R.layout.activity_scan) {
    override fun onResume() {
        super.onResume()
        viewModel.materialCount = SPUtil.getI("materialCount", 12)
        binding.ratio.text = "${(viewModel.materialList.value?: mutableListOf()).size}/${viewModel.materialCount}"
    }
    override fun onViewInit(binding: ActivityScanBinding) {
        binding.etScan.apply {
            setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    handleInput(text.toString())
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
            setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    requestFocus()
                }
            }
        }
    }

    override fun onViewModelInit(viewModel: ScanViewModel) {
        viewModel.currentBox.observe(this) {
            binding.boxNo.setText(it)
            if (it.isNullOrEmpty()) {
                binding.pass.text = "--"
                binding.pass.setTextColor(Color.GRAY)
            }
        }
        viewModel.currentMaterial.observe(this) {
            binding.partNo.setText(it)
        }
        viewModel.currentModeBig.observe(this) {
            binding.model.text = if (viewModel.currentBox.value.isNullOrEmpty()) "----" else if (it) "高配" else "BASE"
        }
        viewModel.passData.observe(this) {
            binding.pass.text = if (viewModel.currentBox.value.isNullOrEmpty()) "--" else if (it) "PASS" else "FAIL"
            binding.pass.setTextColor(if (viewModel.currentBox.value.isNullOrEmpty()) Color.GRAY else if (it) Color.GREEN else Color.RED)
        }
        viewModel.materialList.observe(this) {
            binding.ratio.text = "${it.size}/${viewModel.materialCount}"
            binding.log.text = it.map { pair ->
                pair.first + "\t\t\t" + FormatUtil.formatDate(pair.second)
            }.toList().joinToString("\r\n")
        }
    }

    var long = System.currentTimeMillis()
    private fun handleInput(code: String) {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - long < 1000) {
            binding.etScan.setText("")
            return
        }

        long = currentTimeMillis;
        viewModel.handleInput(code.trim())
        binding.etScan.setText("")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerReceiver()
    }

    override fun onDestroy() {
        unRegisterReceiver()
        super.onDestroy()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getStringExtra("com.symbol.datawedge.data_string")?.apply {
                handleInput(this)
            }
        }
    }

    private fun registerReceiver() {
        if (Build.VERSION.SDK_INT < 33) {
            registerReceiver(receiver, IntentFilter("com.symbol.datawedge.data_string"))
        } else {
            registerReceiver(receiver, IntentFilter("com.symbol.datawedge.data_string"), RECEIVER_NOT_EXPORTED)
        }
    }

    private fun unRegisterReceiver() {
        unregisterReceiver(receiver)
    }
}