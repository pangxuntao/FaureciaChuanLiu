package com.cainiao.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cainiao.base.ext.isNullOrEmpty
import com.cainiao.mycommon.app.scan.ScannerReceiver
import java.lang.reflect.ParameterizedType

/**
 * @Auther: pxt
 * @Date: 2021/5/30 20:27
 * @Description: com.faurecia.base
 * @Version: 1.0
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<VM : ViewModel, VDB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
) : AppCompatActivity(), ScannerReceiver {
    protected val viewModel: VM by lazy {
        ViewModelProvider.NewInstanceFactory().create(getViewModelClass())
    }
    protected val binding: VDB by lazy {
        DataBindingUtil.setContentView(this, layoutResId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
        onViewInit(binding)
        onViewModelInit(viewModel)
    }

    abstract fun onViewInit(binding: VDB)
    abstract fun onViewModelInit(viewModel: VM)

    private fun getViewModelClass(): Class<VM> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

    private fun getBindingClass(): Class<VDB> {
        return (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VDB>
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(this)
    }

    override fun onPause() {
        super.onPause()
        unRegisterReceiver(this)
    }

    override fun onReceive(code: String?) {
        if (code.isNullOrEmpty()) return
        onScan(code!!.trim())
    }

    open fun onScan(code: String) {

    }

    fun callStartActivity(clazz: Class<out Activity>, init: (Intent.() -> Unit)? = null) {
        startActivity(Intent(this, clazz).apply {
            init?.invoke(this)
        })
    }
}