package com.cainiao.chuanliu

import com.cainiao.base.BaseActivity
import com.cainiao.base.util.ToastUtil
import com.cainiao.chuanliu.databinding.ActivityLoadBinding
import com.cainiao.chuanliu.viewmodel.LoadViewModel

class LoadActivity : BaseActivity<LoadViewModel, ActivityLoadBinding>(R.layout.activity_load) {
    override fun onViewInit(binding: ActivityLoadBinding) {}

    override fun onViewModelInit(viewModel: LoadViewModel) {}

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