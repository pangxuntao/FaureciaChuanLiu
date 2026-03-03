package com.cainiao.chuanliu

import android.content.Intent
import android.os.Bundle
import com.cainiao.base.BaseActivity
import com.cainiao.chuanliu.databinding.ActivityMainBinding
import com.cainiao.chuanliu.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(R.layout.activity_main) {
    override fun onViewInit(binding: ActivityMainBinding) {}

    override fun onViewModelInit(viewModel: MainViewModel) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.FLAVOR == "装料拉动") {
            startActivity(Intent(this, LoadActivity::class.java))
        } else {
            startActivity(Intent(this, CallActivity::class.java))
        }
        finish()
    }
}