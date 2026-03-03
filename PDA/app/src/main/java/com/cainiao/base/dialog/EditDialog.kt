package com.cainiao.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cainiao.chuanliu.databinding.DialogEditExceptionBinding

/**
 * @Auther: pxt
 * @Date: 2021/6/7 10:39
 * @Description: com.faurecia.sigemen.dialog
 * @Version: 1.0
 */
class EditDialog(
    var title: String,
    var defValue: String? = "",
    var callback: (String) -> Boolean,
) : DialogFragment() {

    private val binding by lazy {
        DialogEditExceptionBinding.inflate(layoutInflater, null, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding.title.text = title
        binding.name.setText(defValue)
        binding.save.setOnClickListener {
            val invoke = callback.invoke(binding.name.text.toString())
            if (invoke) dismiss()
        }
        binding.cancel.setOnClickListener { dismiss() }
        binding.clear.setOnClickListener { binding.name.setText("") }
        return binding.root
    }
}