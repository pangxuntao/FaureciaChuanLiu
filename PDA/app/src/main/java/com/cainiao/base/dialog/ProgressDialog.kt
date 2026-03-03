package com.cainiao.base.dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cainiao.base.util.ActivityManager
import com.cainiao.base.util.L
import com.cainiao.chuanliu.databinding.DialogLoadingBinding

/**
 * @Auther: pxt
 * @Date: 2021/6/7 15:34
 * @Description: com.faurecia.sigemen.dialog
 * @Version: 1.0
 */
object ProgressDialog {
    private val dialog = PDialog()
    private val handler = Handler(Looper.getMainLooper())

    fun show(msg: String? = null, cancelable: Boolean = true) {
        handler.post {
            val popActivity = ActivityManager.getPopActivity()
            L.e("topActivity: $popActivity")
            if (popActivity == null) {
                dismiss()
            } else {
                doShow(popActivity.supportFragmentManager, msg, cancelable)
            }
        }
    }

    private fun doShow(fragmentManager: FragmentManager, msg: String?, cancelable: Boolean = true) {
        try {
            L.e("doShow: ${dialog.isVisible}")
            dialog.isCancelable = cancelable
            if (!dialog.isVisible) {
                dialog.show(fragmentManager, "ProgressDialog" + System.currentTimeMillis())
            }
            dialog.setMsg(msg)
        } catch (e: Exception) {
            L.e(e.message)
            dismiss()
        }
    }

    fun dismiss() {
        handler.postDelayed({
            try {
                dialog.dismissAllowingStateLoss()
            } catch (e: Exception) {
                L.e(e.message)
            }
        }, 1000)
    }
}

class PDialog : DialogFragment() {
    private var binding: DialogLoadingBinding? = null
    private var message: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLoadingBinding.inflate(inflater, null, false)
        updateMsg()
        return binding?.root
    }

    fun setMsg(msg: String?) {
        this.message = msg
        updateMsg()
    }

    private fun updateMsg() {
        binding?.msg?.text = message ?: "加载中"
    }
}