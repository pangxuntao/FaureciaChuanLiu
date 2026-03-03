package com.cainiao.chuanliu

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cainiao.base.BaseActivity
import com.cainiao.base.dialog.DialogUtil
import com.cainiao.chuanliu.databinding.ActivityCallScanBinding
import com.cainiao.chuanliu.entities.OrderVo
import com.cainiao.chuanliu.entities.TbOrderItem
import com.cainiao.chuanliu.viewmodel.CallScanViewModel
import com.cainiao.mycommon.utils.SoundUtil
import com.cainiao.mywidget.recyclerview.adapter.BaseAdapterSingleType
import com.cainiao.mywidget.recyclerview.holder.BaseHolderSingleType

class CallScanActivity : BaseActivity<CallScanViewModel, ActivityCallScanBinding>(R.layout.activity_call_scan) {
    private val orderList = mutableListOf<TbOrderItem>()
    private val adapter = ScanAdapter(orderList)

    override fun onViewInit(binding: ActivityCallScanBinding) {
        setTitle("Call料下单")
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.setEmptyView(binding.emptyView)
        binding.etInput.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                onScan(binding.etInput.text.toString().trim())
                binding.etInput.setText("")
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        binding.etInput.apply {
            setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    v.post { requestFocus() }
                }
            }
        }
        binding.btnConfirm.setOnClickListener {
            viewModel.confirm {
                SoundUtil.success()
                finish()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewModelInit(viewModel: CallScanViewModel) {
        viewModel.tbOrder.observe(this) {
            it?.let {
                binding.tvOrder.text = "#${it.orderCode}"
            }
        }
        viewModel.orderItems.observe(this) {
            orderList.clear()
            orderList.addAll(it ?: listOf())
        }
        viewModel.getNewOrder {
            DialogUtil.showOneButtonDialog("提示", "获取订单号失败，请重试") {
                finish()
            }
        }
    }

    override fun onScan(code: String) {
        viewModel.findShelf(code)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        DialogUtil.showDialog("", "订单未完成，确认退出吗？", { it ->
            if (it) {
                super.onBackPressed()
            }
        })
    }
}

class ScanAdapter(data: List<TbOrderItem>) : BaseAdapterSingleType<TbOrderItem, ScanHolder>(data) {
    override fun getLayoutId(): Int {
        return R.layout.item_current_order
    }
}

class ScanHolder(view: View) : BaseHolderSingleType<TbOrderItem>(view) {
    override fun initView(view: View) {
    }

    override fun bindView(orderVo: TbOrderItem) {
    }
}