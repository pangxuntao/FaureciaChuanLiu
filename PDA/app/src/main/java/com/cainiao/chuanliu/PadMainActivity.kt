package com.cainiao.chuanliu

import android.graphics.Color
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.cainiao.base.App
import com.cainiao.base.BaseActivity
import com.cainiao.base.dialog.ProgressDialog
import com.cainiao.base.util.SPUtil
import com.cainiao.chuanliu.databinding.ActivityMainPadBinding
import com.cainiao.chuanliu.entities.Data
import com.cainiao.chuanliu.entities.ScanHistory
import com.cainiao.chuanliu.viewmodel.PadMainViewModel
import com.cainiao.mycommon.utils.FormatUtil
import com.cainiao.mycommon.utils.SoundUtil
import com.cainiao.mycommon.utils.ToastUtil
import com.cainiao.mywidget.MyPopupWindow
import com.cainiao.mywidget.recyclerview.adapter.BaseAdapterSingleType
import com.cainiao.mywidget.recyclerview.holder.BaseHolderSingleType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class PadMainActivity : BaseActivity<PadMainViewModel, ActivityMainPadBinding>(R.layout.activity_main_pad) {
    val gson = Gson()
    var dataList = mutableListOf<Data>()
    var history = mutableListOf<ScanHistory>()
    var data: MutableLiveData<Data?> = MutableLiveData(null)
    var adapter = Adapter(history)
    private val scanDao by lazy {
        App.database.scanDao()
    }

    override fun onViewInit(binding: ActivityMainPadBinding) {
        supportActionBar?.hide()
        var datas: List<Data> = gson.fromJson(SPUtil.getS("dataList", "[]"), object : TypeToken<List<Data>>() {}.type)
        dataList.addAll(datas)
        SPUtil.getS("select", null)?.let {
            data.postValue(gson.fromJson(it, Data::class.java))
            data.value?.let {
                binding.dropdown.text = "${it.name1} [${it.code}]"
            }
        }

        binding.recycler.adapter = adapter
        binding.recycler.setEmptyView(binding.emptyView)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.drop.setOnClickListener {
            binding.dropdown.performClick()
        }
        binding.dropdown.setOnClickListener {
            if (dataList.isEmpty()) {
                ToastUtil.toast("请先导入基础数据")
                return@setOnClickListener
            }
            MyPopupWindow.create(binding.dropdown, "", dataList.toList()) { index, _ ->
                data.postValue(dataList[index])
                SPUtil.put("select", gson.toJson(dataList[index]))
            }
        }
        binding.btn1.setOnClickListener {
            history.clear()
            adapter.notifyDataSetChanged()
        }
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
                if (dataList.isEmpty()) {
                    ToastUtil.toast("请先导入基础数据")
                    SoundUtil.ao()
                    binding.text.setText("")
                } else if (data.value == null) {
                    ToastUtil.toast("请先选择零件")
                    SoundUtil.ao()
                    binding.text.setText("")
                } else {
                    val text = binding.text.text.toString()
                    //P4727505:1TF7026059
                    var split = text.split(":")
                    if (!split[0].startsWith("P") || split.size != 2) {
                        ToastUtil.toast("条码格式错误")
                        SoundUtil.ao()
                        binding.text.setText("")
                        binding.result.text = "异常：格式错误"
                        binding.result.setTextColor(Color.parseColor("#ff0000"))
                    } else if (split[0].substring(1) != data.value?.code) {
                        binding.text.setText("")
                        binding.result.text = "异常：${split[0].substring(1)}"
                        binding.result.setTextColor(Color.parseColor("#ff0000"))
                    } else {
                        history.add(
                            0, ScanHistory(
                                code = text,
                                name = text,
                                time = System.currentTimeMillis(),
                                success = history.size % 2 == 0,
                                error = "dsds"
                            )
                        )
                        adapter.notifyDataSetChanged()
                    }
                }
//                lifecycleScope.launch(Dispatchers.IO) {
//                    val insertAll = scanDao.insertAll(materialList.value!!.map {
//                        ScanBean(id = 0, code = it.first, time = it.second)
//                    })
//                    L.e("------------$insertAll")
//                    withContext(Dispatchers.Main) {
//                        dialog = DialogUtil.showOneButtonDialog("提示", "校验完成", cancelable = false) {
//                            currentBox.value = ""
//                            currentMaterial.value = ""
//                            materialList.value?.clear()
//                            materialList.notifyListChange()
//                            currentModeBig.value = isHighBox(code)
//                            passData.value = false
//                        }
//                    }
//                }
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

class Adapter(data: List<ScanHistory>) : BaseAdapterSingleType<ScanHistory, Holder>(data) {
    override fun getLayoutId(): Int {
        return R.layout.item_history
    }
}

class Holder(var view: View) : BaseHolderSingleType<ScanHistory>(view) {
    override fun initView(p0: View?) {}

    override fun bindView(p0: ScanHistory?) {
        view.findViewById<TextView>(R.id.tv1).apply {
            p0?.time?.let {
                text = FormatUtil.formatDate(Date(it), "HH:mm:ss")
            } ?: kotlin.run {
                text = ""
            }
        }
        view.findViewById<TextView>(R.id.tv2).apply {
            text = "${p0?.name}"
        }
        view.findViewById<TextView>(R.id.tv3).apply {
            text = "${p0?.code}"
        }
        view.findViewById<TextView>(R.id.tv4).apply {
            text = if (p0?.success == true) "合格" else "${p0?.error}"
        }
    }
}