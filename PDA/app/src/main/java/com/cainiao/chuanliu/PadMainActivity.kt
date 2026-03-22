package com.cainiao.chuanliu

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cainiao.base.App
import com.cainiao.base.BaseActivity
import com.cainiao.base.dialog.DialogUtil
import com.cainiao.base.dialog.ErrorDialog
import com.cainiao.base.dialog.ProgressDialog
import com.cainiao.base.util.L
import com.cainiao.base.util.SPUtil
import com.cainiao.chuanliu.databinding.ActivityMainPadBinding
import com.cainiao.chuanliu.entities.Data
import com.cainiao.chuanliu.entities.ScanHistory
import com.cainiao.chuanliu.room.ScanBean
import com.cainiao.chuanliu.viewmodel.PadMainViewModel
import com.cainiao.mycommon.utils.FormatUtil
import com.cainiao.mycommon.utils.SoundUtil
import com.cainiao.mycommon.utils.ToastUtil
import com.cainiao.mywidget.MyPopupWindow
import com.cainiao.mywidget.recyclerview.adapter.BaseAdapterSingleType
import com.cainiao.mywidget.recyclerview.holder.BaseHolderSingleType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    var dialog: DialogFragment? = null
    override fun onViewInit(binding: ActivityMainPadBinding) {
        supportActionBar?.hide()
        var his: List<ScanHistory> =
            gson.fromJson(SPUtil.getS("history", "[]"), object : TypeToken<List<ScanHistory>>() {}.type)
        history.addAll(his)
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
        binding.confirm.setOnClickListener {
            onConfirm()
        }
        binding.btn1.setOnClickListener {
            DialogUtil.showDialog("提示", "确定要清空今日扫描记录吗？", {
                if (it) {
                    history.clear()
                    adapter.notifyDataSetChanged()
                    updateLv()
                }
            })
        }
        binding.btn2.setOnClickListener {
            startActivity(Intent(this, ExportActivity::class.java))
        }
        binding.btn3.setOnClickListener {
            checkAndRequestExternalStoragePermission()
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
            if (dialog?.dialog?.isShowing == true) return@setOnKeyListener true
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.confirm.performClick()
            }
            return@setOnKeyListener false
        }
    }

    fun onConfirm() {
        lifecycleScope.launch(Dispatchers.Main) {
            if (dataList.isEmpty()) {
                ToastUtil.toast("请先导入基础数据")
                SoundUtil.ao()
                binding.text.setText("")
            } else if (data.value == null) {
                ToastUtil.toast("请先选择零件")
                SoundUtil.ao()
                binding.text.setText("")
            } else {
                val text = binding.text.text.toString().trim()
                if (text.isEmpty()) return@launch
                //P4727505:1TF7026059
                var split = text.split(":")
                if (!split[0].startsWith("P") || split.size != 2) {
                    addHistory(false, text, "格式错误", "格式错误")
                } else if (split[0].substring(1) != data.value?.code) {
                    addHistory(false, text, split[0].substring(1), "匹配失败")
                } else if (data.value?.check == "1" && checkRepeat(text)) {
                    addHistory(false, text, split[0].substring(1), "重复扫描")
                } else {
                    addHistory(true, text, split[0].substring(1), "")
                }
            }
        }
    }

    fun addHistory(success: Boolean, fullCode: String, code: String, error: String = "") {
        binding.text.setText("")
        if (success) {
            SoundUtil.success()
            binding.result.text = "已通过：$code"
            binding.result.setTextColor(Color.parseColor("#00ff00"))
            binding.error2.text = fullCode
            binding.error2.setTextColor(Color.parseColor("#00ff00"))
            lifecycleScope.launch(Dispatchers.IO) {
                val insert = scanDao.insert(
                    ScanBean(
                        id = 0,
                        code = fullCode,
                        name = data.value?.name1 ?: "",
                        time = System.currentTimeMillis()
                    )
                )
                L.e("insert------------$insert")
            }
        } else {
//            ToastUtil.toast(error)
            dialog = ErrorDialog("匹配错误!", error, data.value?.code ?: "", code, fullCode, {
                dialog = null
            })
            dialog?.show(supportFragmentManager, "ErrorDialog")
            SoundUtil.ao()
            binding.result.text = "异常：$error"
            binding.error2.text = fullCode
            binding.result.setTextColor(Color.parseColor("#ff0000"))
            binding.error2.setTextColor(Color.parseColor("#ff0000"))
        }
        history.add(
            0, ScanHistory(
                code = fullCode,
                name = data.value?.name1 ?: "",
                time = System.currentTimeMillis(),
                success = success,
                error = error
            )
        )
        adapter.notifyDataSetChanged()
        updateLv()
    }

    suspend fun checkRepeat(code: String): Boolean = withContext(Dispatchers.IO) {
        val result = scanDao.find(code)
        println("pxt checkRepeat: $result")
        result != null
    }

    @SuppressLint("SetTextI18n")
    fun updateLv() {
        val errorCount = history.count { !it.success }
        binding.today.text = "${history.size}"
        if (history.isEmpty()) {
            binding.lv.text = "100%"
        } else {
            binding.lv.text = "%.2f".format(100.0f * (history.size - errorCount) / history.size) + "%"
        }
        binding.error.text = "$errorCount"
        SPUtil.put("history", gson.toJson(history))
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

    private val REQUEST_MANAGE_EXTERNAL_STORAGE = 1001
    private fun checkAndRequestExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                // 请求管理所有文件权限
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE)
            } else {
                // 已有权限，可以创建文件夹
                createExternalFolder()
            }
        } else {
            // Android 10 及以下版本，使用传统权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                // 请求传统存储权限
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1002)
            } else {
                createExternalFolder()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1002) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createExternalFolder()
            } else {
                Toast.makeText(this, "需要存储权限才能正常使用", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createExternalFolder() {
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
            setTextColor(
                if (p0?.success == true) {
                    Color.parseColor("#00ff00")
                } else {
                    Color.parseColor("#ff0000")
                }
            )
        }
    }
}