package com.cainiao.chuanliu

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import com.cainiao.base.BaseActivity
import com.cainiao.base.util.SPUtil
import com.cainiao.base.util.ToastUtil
import com.cainiao.chuanliu.databinding.ActivityConfigBinding
import com.cainiao.chuanliu.viewmodel.ConfigViewModel
import com.cainiao.mycommon.utils.SoundUtil

class ConfigActivity : BaseActivity<ConfigViewModel, ActivityConfigBinding>(R.layout.activity_config) {
    override fun onViewInit(binding: ActivityConfigBinding) {
        binding.materialCount.setText("${SPUtil.getI("materialCount", 12)}")
        binding.config1.apply {
            title.text = "高配箱号"
            content.setText(SPUtil.getNullSafe("config1content", ""))
            spinner.setSelection(SPUtil.getNullSafe("config1spinner", 0))
            pos1.setText(handleInt(SPUtil.getInt("config1pos1", -1)))
            pos2.setText(handleInt(SPUtil.getInt("config1pos2", -1)))
        }
        binding.config2.apply {
            title.text = "高配零件规则"
            content.setText(SPUtil.getNullSafe("config2content", ""))
            spinner.setSelection(SPUtil.getNullSafe("config2spinner", 0))
            pos1.setText(handleInt(SPUtil.getInt("config2pos1", -1)))
            pos2.setText(handleInt(SPUtil.getInt("config2pos2", -1)))
        }
        binding.config3.apply {
            title.text = "低配箱号"
            content.setText(SPUtil.getNullSafe("config3content", ""))
            spinner.setSelection(SPUtil.getNullSafe("config3spinner", 0))
            pos1.setText(handleInt(SPUtil.getInt("config3pos1", -1)))
            pos2.setText(handleInt(SPUtil.getInt("config3pos2", -1)))
        }
        binding.config4.apply {
            title.text = "低配零件规则"
            content.setText(SPUtil.getNullSafe("config4content", ""))
            spinner.setSelection(SPUtil.getNullSafe("config4spinner", 0))
            pos1.setText(handleInt(SPUtil.getInt("config4pos1", -1)))
            pos2.setText(handleInt(SPUtil.getInt("config4pos2", -1)))
        }
        binding.back.setOnClickListener { finish() }
        binding.confirm.setOnClickListener {
            val count = binding.materialCount.text.toString()
            if (TextUtils.isEmpty(count) || count.toInt() == 0) {
                SoundUtil.ao()
                binding.materialCount.requestFocus()
                showPopWindow(binding.materialCount, "必须是大于0的数")
                return@setOnClickListener
            }
            val allItem = listOf(
                listOf(
                    binding.config1.content,
                    binding.config1.pos1,
                    binding.config1.pos2,
                ),
                listOf(
                    binding.config2.content,
                    binding.config2.pos1,
                    binding.config2.pos2,
                ),
                listOf(
                    binding.config3.content,
                    binding.config3.pos1,
                    binding.config3.pos2,
                ),
                listOf(
                    binding.config4.content,
                    binding.config4.pos1,
                    binding.config4.pos2,
                )
            )
            if (!checkFinish(allItem)
            ) {
                SoundUtil.ao()
                ToastUtil.show("配置能容不能为空")
                return@setOnClickListener
            }
            if (!checkConfig(allItem)
            ) {
                SoundUtil.ao()
                ToastUtil.show("配置能容不能为空")
                return@setOnClickListener
            }
            SPUtil.put("materialCount", count.toInt())
            SPUtil.put("config1content", binding.config1.content.text.toString())
            SPUtil.put("config1spinner", binding.config1.spinner.selectedItemPosition)
            SPUtil.put("config1pos1", binding.config1.pos1.text.toString().toInt())
            SPUtil.put("config1pos2", binding.config1.pos2.text.toString().toInt())

            SPUtil.put("config2content", binding.config2.content.text.toString())
            SPUtil.put("config2spinner", binding.config2.spinner.selectedItemPosition)
            SPUtil.put("config2pos1", binding.config2.pos1.text.toString().toInt())
            SPUtil.put("config2pos2", binding.config2.pos2.text.toString().toInt())

            SPUtil.put("config3content", binding.config3.content.text.toString())
            SPUtil.put("config3spinner", binding.config3.spinner.selectedItemPosition)
            SPUtil.put("config3pos1", binding.config3.pos1.text.toString().toInt())
            SPUtil.put("config3pos2", binding.config3.pos2.text.toString().toInt())

            SPUtil.put("config4content", binding.config4.content.text.toString())
            SPUtil.put("config4spinner", binding.config4.spinner.selectedItemPosition)
            SPUtil.put("config4pos1", binding.config4.pos1.text.toString().toInt())
            SPUtil.put("config4pos2", binding.config4.pos2.text.toString().toInt())
            finish()
        }

        binding.highSwitch.apply {
            isChecked = SPUtil.getBoolean("highSwitch", false)
            setOnCheckedChangeListener { _, isChecked ->
                SPUtil.put("highSwitch", isChecked)
            }
        }
        binding.baseSwitch.apply {
            isChecked = SPUtil.getBoolean("baseSwitch", false)
            setOnCheckedChangeListener { _, isChecked ->
                SPUtil.put("baseSwitch", isChecked)
            }
        }
    }

    private fun checkConfig(groups: List<List<EditText>>): Boolean {
        groups.forEach { group ->
            if (group[1].text.toString().toInt() <= 0) {
                showPopWindow(group[1], "必须是大于0的数")
                return false
            }
            if (group[1].text.toString().toInt() > group[2].text.toString().toInt()) {
                showPopWindow(group[1], "起始位置不能大于结束位置")
                return false
            }
            val length = group[2].text.toString().toInt() - group[1].text.toString().toInt()
            if (group[0].text.length != length + 1) {
                showPopWindow(group[0], "截取内容与截取位数不匹配")
                showPopWindow(group[1], "截取内容与截取位数不匹配")
                return false
            }
        }
        return true
    }

    private fun checkFinish(groups: List<List<EditText>>): Boolean {
        groups.forEach { group ->
            group.forEach {
                if (it.text.isEmpty()) {
                    it.requestFocus()
                    showPopWindow(it, "配置能容不能为空")
                    return false
                }
            }
        }
        return true
    }

    override fun onViewModelInit(viewModel: ConfigViewModel) {

    }

    private fun handleInt(value: Int): String {
        return if (value == -1)
            ""
        else
            value.toString()
    }

    private fun showPopWindow(view: View, message: String) {
        val window = PopupWindow(this)
        window.contentView = layoutInflater.inflate(R.layout.layout_popwindow, null, false).apply {
            findViewById<TextView>(R.id.message).apply {
                text = message
            }
        }
        window.showAsDropDown(view)
        Handler(Looper.getMainLooper()).postDelayed({ window.dismiss() }, 5000)
    }
}