package com.cainiao.base.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cainiao.base.util.ActivityManager
import com.cainiao.chuanliu.databinding.DialogErrorBinding
import com.cainiao.chuanliu.databinding.DialogOneButtonBinding
import com.cainiao.chuanliu.databinding.DialogTwoButtonBinding

/**
 * @Auther: pxt
 * @Date: 2021/6/7 10:39
 * @Description: com.faurecia.sigemen.dialog
 * @Version: 1.0
 */
object DialogUtil {
    fun showOneButtonDialog(
        title: String,
        msg: String,
        confirmText: String? = null,
        cancelable: Boolean = true,
        onclick: (() -> Unit)? = null,
    ): DialogFragment? {
        val popActivity = ActivityManager.getPopActivity() ?: return null
        return OneButtonDialog(
            title,
            msg,
            onclick,
            confirmText,
            cancelable
        ).apply { show(popActivity.supportFragmentManager, "TwoButtonDialog") }
    }

    fun showDialog(
        title: String,
        msg: String,
        onclick: ((Boolean) -> Unit)? = null,
        confirmText: String? = null,
        cancelText: String? = null,
        cancelable: Boolean = true,
    ) {
        val popActivity = ActivityManager.getPopActivity() ?: return
        TwoButtonDialog(
            title,
            msg,
            onclick,
            confirmText,
            cancelText,
            cancelable
        ).show(popActivity.supportFragmentManager, "TwoButtonDialog")
    }
}

class TwoButtonDialog(
    val title: String,
    val msg: String,
    val onclick: ((Boolean) -> Unit)? = null,
    val confirmText: String? = null,
    val cancelText: String? = null,
    val cancelable: Boolean = true,
) : DialogFragment() {

    private val binding by lazy {
        DialogTwoButtonBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.title.text = title
        binding.msg.text = msg
        confirmText?.let {
            binding.confirm.text = it
        }
        cancelText?.let {
            binding.cancel.text = it
        }
        binding.confirm.setOnClickListener {
            onclick?.invoke(true)
            dismiss()
        }
        binding.cancel.setOnClickListener {
            onclick?.invoke(false)
            dismiss()
        }
        isCancelable = cancelable
        return binding.root
    }
}
class ErrorDialog(
    val title: String,
    val msg: String,
    val code1: String,
    val code2: String,
    val code3: String,
    val onclick: ((Boolean) -> Unit)? = null,
    val confirmText: String? = null,
    val cancelText: String? = null,
) : DialogFragment() {

    private val binding by lazy {
        DialogErrorBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.title.text = title
        binding.msg.text = msg
        binding.code1.text = code1
        binding.code2.text = code2
        binding.code3.text = code3
        confirmText?.let {
            binding.confirm.text = it
        }
        cancelText?.let {
            binding.cancel.text = it
        }
        binding.confirm.setOnClickListener {
            onclick?.invoke(true)
            dismiss()
        }
        binding.cancel.setOnClickListener {
            onclick?.invoke(false)
            dismiss()
        }
        isCancelable = false
        return binding.root
    }
}

class OneButtonDialog(
    val title: String,
    val msg: String,
    val onclick: (() -> Unit)? = null,
    val confirmText: String? = null,
    val cancelable: Boolean = true,
) : DialogFragment() {

    private val binding by lazy {
        DialogOneButtonBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.title.text = title
        binding.msg.text = msg
        confirmText?.let {
            binding.confirm.text = it
        }
        binding.confirm.setOnClickListener {
            onclick?.invoke()
            dismiss()
        }
        isCancelable = cancelable
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            dialog?.setOnKeyListener { dialog, keyCode, event -> true }
        }
    }
}