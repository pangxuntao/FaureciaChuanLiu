package com.cainiao.chuanliu

import android.Manifest
import android.view.KeyEvent
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.cainiao.base.BaseActivity
import com.cainiao.base.dialog.ProgressDialog
import com.cainiao.base.util.SPUtil
import com.cainiao.chuanliu.databinding.ActivityMainPadBinding
import com.cainiao.chuanliu.entities.Data
import com.cainiao.chuanliu.viewmodel.PadMainViewModel
import com.cainiao.mycommon.utils.SoundUtil
import com.cainiao.mycommon.utils.ToastUtil
import com.cainiao.mywidget.MyPopupWindow
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PadMainActivity : BaseActivity<PadMainViewModel, ActivityMainPadBinding>(R.layout.activity_main_pad) {
    val gson = Gson()
    var dataList = mutableListOf<Data>()
    var data: MutableLiveData<Data?> = MutableLiveData(null)
    override fun onViewInit(binding: ActivityMainPadBinding) {
        supportActionBar?.hide()
        var datas: List<Data> = gson.fromJson(SPUtil.getS("dataList", "[]"), object : TypeToken<List<Data>>() {}.type)
        dataList.addAll(datas)
        data.postValue(gson.fromJson(SPUtil.getS("select", "{}"), Data::class.java))
        data.value?.let {
            binding.dropdown.text = "${it.name1} [${it.code}]"
        }
        binding.drop.setOnClickListener {
            binding.dropdown.performClick()
        }
        binding.dropdown.setOnClickListener {
            MyPopupWindow.create(binding.dropdown, "", dataList.toList()) { index, _ ->
                data.postValue(dataList[index])
                SPUtil.put("select", gson.toJson(dataList[index]))
            }
        }
        binding.btn1.setOnClickListener { }
        binding.btn2.setOnClickListener { }
        binding.btn3.setOnClickListener {
            ProgressDialog.show("")
            FileUtil.readDataFromFile { it ->
                ProgressDialog.dismiss()
                if (it?.isNotEmpty() == true) {
                    SoundUtil.success()
                    dataList.addAll(it.subList(1, it.size))
                    data.postValue(null)

                    SPUtil.put("dataList", gson.toJson(it))
                    SPUtil.put("select", "")
                    binding.dropdown.performClick()
                } else {
                    ToastUtil.toast("导入失败")
                    SoundUtil.ao()
                }
                return@readDataFromFile true
            }
        }
        binding.clear.setOnClickListener {
            binding.text.setText("")
        }
        binding.text.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.text.post {
                    binding.text.requestFocus()
                }
            }
        }
        binding.text.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val text = binding.text.text.toString()
            }
            return@setOnKeyListener false
        }
    }

    override fun onViewModelInit(viewModel: PadMainViewModel) {
        data.observe(this) {
            if (it == null) {
                binding.dropdown.text = "下拉选择"
            } else {
                binding.dropdown.text = "${it.name1} [${it.code}]"
            }
        }
    }
}