package com.cainiao.chuanliu

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cainiao.base.BaseActivity
import com.cainiao.base.util.ToastUtil
import com.cainiao.chuanliu.databinding.ActivityCallBinding
import com.cainiao.chuanliu.entities.OrderVo
import com.cainiao.chuanliu.entities.TbOrder
import com.cainiao.chuanliu.viewmodel.CallViewModel
import com.cainiao.mywidget.recyclerview.adapter.BaseAdapterSingleType
import com.cainiao.mywidget.recyclerview.holder.BaseHolderSingleType

class CallActivity : BaseActivity<CallViewModel, ActivityCallBinding>(R.layout.activity_call) {
    private val orderList = mutableListOf<OrderVo>()
    private val adapter = CallAdapter(orderList)
    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onViewInit(binding: ActivityCallBinding) {
        setTitle("佛吉亚Call料")
        binding.recycler.adapter = adapter
        binding.recycler.setEmptyView(binding.emptyView)
        binding.btnScan.setOnClickListener {
            callStartActivity(CallScanActivity::class.java)
        }
        binding.btnShelf.setOnClickListener { }
        binding.btnHistory.setOnClickListener { }
        binding.btnSetting.setOnClickListener { }
    }

    override fun onViewModelInit(viewModel: CallViewModel) {
        viewModel.currentOrder.observe(this) {
            orderList.clear()
            orderList.addAll(it ?: listOf())
            adapter.notifyDataSetChanged()
        }
    }

    var lastClick = 0L
    override fun onBackPressed() {
        if (System.currentTimeMillis() - lastClick < 1500) {
            super.onBackPressed()
        } else {
            ToastUtil.show("再次返回退出")
            lastClick = System.currentTimeMillis()
        }
    }
}

class CallAdapter(data: List<OrderVo>) : BaseAdapterSingleType<OrderVo, CallHolder>(data) {
    override fun getLayoutId(): Int {
        return R.layout.item_current_order
    }
}

class CallHolder(view: View) : BaseHolderSingleType<OrderVo>(view) {
    override fun initView(view: View) {
    }

    override fun bindView(orderVo: OrderVo) {
    }
}