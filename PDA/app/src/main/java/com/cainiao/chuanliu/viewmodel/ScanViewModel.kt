package com.cainiao.chuanliu.viewmodel

import android.annotation.SuppressLint
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.cainiao.base.App
import com.cainiao.base.BaseViewModel
import com.cainiao.base.SingleTimeLiveData
import com.cainiao.base.dialog.DialogUtil
import com.cainiao.base.ext.isNullOrEmpty
import com.cainiao.base.util.L
import com.cainiao.base.util.SPUtil
import com.cainiao.base.util.ToastUtil
import com.cainiao.chuanliu.room.ScanBean
import com.cainiao.mycommon.utils.SoundUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanViewModel : BaseViewModel() {
    @SuppressLint("SimpleDateFormat")
    val passData = MutableLiveData<Boolean>()
    val currentBox = MutableLiveData<String>()
    val currentMaterial = MutableLiveData<String>()
    val materialList = SingleTimeLiveData<MutableList<Pair<String, Long>>>().apply {
        value = mutableListOf()
    }
    var currentModeBig = MutableLiveData<Boolean>()
    var dialog: DialogFragment? = null
    var materialCount = SPUtil.getI("materialCount", 12)
    private val userDao by lazy {
        App.database.scanDao()
    }

    fun handleInput(code: String) {
        if (dialog?.dialog?.isShowing == true) return
        L.e(code)
        if (!isBox(code) && !isMaterial(code)) {
            SoundUtil.ao()
            ToastUtil.show("规则匹配错误")
            passData.value = false
            return
        }
        //扫描的箱号
        if (isBox(code)) {
            if (currentBox.value == code) {
                SoundUtil.jiu()
                passData.value = true
                return
            }
            if (currentBox.value.isNullOrEmpty()) {
                currentBox.value = code
                currentModeBig.value = isHighBox(code)
                SoundUtil.jiu()
            } else {
                SoundUtil.ao()
                DialogUtil.showDialog("提示", "是否切换箱号", {
                    if (it) {
                        currentBox.value = code
                        currentModeBig.value = isHighBox(code)
                        currentMaterial.value = ""
                        materialList.value?.clear()
                        materialList.notifyListChange()
                    }
                })
            }
            return
        }
        //扫描的是物料
        if (currentBox.value.isNullOrEmpty()) {
            SoundUtil.ao()
            ToastUtil.show("请先扫描箱号")
            return
        }
        if (isHighBox(currentBox.value!!) && isBaseMaterial(code)
            || isBaseBox(currentBox.value!!) && isHighMaterial(code)
        ) {
            SoundUtil.ao()
            ToastUtil.show("型号匹配错误")
            passData.value = false
            return
        }
        //必须扫描同一个零件号；；暂时没有这种规则
//        if (!currentMaterial.value.isNullOrEmpty() && currentMaterial.value != code) {
//            SoundUtil.error()
//            ToastUtil.show("物料错误, 同一箱物料号必须一致")
//            return
//        }

        //开关开启后，12个零件不能有重复的
        if (isHighMaterial(code) && SPUtil.getBoolean("highSwitch", false)
            || isBaseMaterial(code) && SPUtil.getBoolean("baseSwitch", false)
        ) {
            //去重
            if (materialList.value?.find { it.first == code } != null) {
                SoundUtil.ao()
                ToastUtil.show("已经存在相同零件号的扫描记录")
                return
            }
        }
        passData.value = true
        SoundUtil.jiu()
        //保存
        materialList.value?.add(Pair(code, System.currentTimeMillis()))
        materialList.notifyListChange()
        currentMaterial.value = code

        if (materialList.value?.size == materialCount) {
            SoundUtil.success()
            viewModelScope.launch(Dispatchers.IO) {
                val insertAll = userDao.insertAll(materialList.value!!.map {
                    ScanBean(id = 0, code = it.first, time = it.second)
                })
                L.e("------------$insertAll")
                withContext(Dispatchers.Main) {
                    dialog = DialogUtil.showOneButtonDialog("提示", "校验完成", cancelable = false) {
                        currentBox.value = ""
                        currentMaterial.value = ""
                        materialList.value?.clear()
                        materialList.notifyListChange()
                        currentModeBig.value = isHighBox(code)
                        passData.value = false
                    }
                }
            }

        }
    }

    private fun isBox(code: String): Boolean {
        return isHighBox(code) || isBaseBox(code)
    }

    private fun isHighBox(code: String): Boolean {
        val highBoxCut = SPUtil.getNullSafe("config1content", "")
        val highBoxDire = SPUtil.getNullSafe("config1spinner", 0)
        val highBoxPos1 = SPUtil.getNullSafe("config1pos1", -1)
        val highBoxPos2 = SPUtil.getNullSafe("config1pos2", -1)
        val codeLength = code.length
        if (highBoxCut.isNotEmpty() && codeLength >= highBoxPos2) {
            return if (highBoxDire == 0) {//正向截取
                code.substring(highBoxPos1 - 1, highBoxPos2) == highBoxCut
            } else {//反向截取
                code.substring(codeLength - highBoxPos2, codeLength - highBoxPos1 + 1) == highBoxCut
            }
        }
        return false
    }

    private fun isBaseBox(code: String): Boolean {
        val codeLength = code.length
        val baseBoxCut = SPUtil.getNullSafe("config3content", "")
        val baseBoxDire = SPUtil.getNullSafe("config3spinner", 0)
        val baseBoxPos1 = SPUtil.getNullSafe("config3pos1", -1)
        val baseBoxPos2 = SPUtil.getNullSafe("config3pos2", -1)
        if (baseBoxCut.isNotEmpty() && codeLength >= baseBoxPos2) {
            return if (baseBoxDire == 0) {//正向截取
                code.substring(baseBoxPos1 - 1, baseBoxPos2) == baseBoxCut
            } else {//反向截取
                code.substring(codeLength - baseBoxPos2, codeLength - baseBoxPos1 + 1) == baseBoxCut
            }
        }
        return false
    }

    private fun isMaterial(code: String): Boolean {
        return isHighMaterial(code) || isBaseMaterial(code)
    }

    private fun isHighMaterial(code: String): Boolean {
        val codeLength = code.length
        val highMaterialCut = SPUtil.getNullSafe("config2content", "")
        val highMaterialDire = SPUtil.getNullSafe("config2spinner", 0)
        val highMaterialPos1 = SPUtil.getNullSafe("config2pos1", -1)
        val highMaterialPos2 = SPUtil.getNullSafe("config2pos2", -1)
        if (highMaterialCut.isNotEmpty() && codeLength >= highMaterialPos2) {
            return if (highMaterialDire == 0) {//正向截取
                code.substring(highMaterialPos1 - 1, highMaterialPos2) == highMaterialCut
            } else {//反向截取
                code.substring(codeLength - highMaterialPos2, codeLength - highMaterialPos1 + 1) == highMaterialCut
            }
        }
        return false
    }

    private fun isBaseMaterial(code: String): Boolean {
        val codeLength = code.length
        val baseMaterialCut = SPUtil.getNullSafe("config4content", "")
        val baseMaterialDire = SPUtil.getNullSafe("config4spinner", 0)
        val baseMaterialPos1 = SPUtil.getNullSafe("config4pos1", -1)
        val baseMaterialPos2 = SPUtil.getNullSafe("config4pos2", -1)
        if (baseMaterialCut.isNotEmpty() && codeLength >= baseMaterialPos2) {
            return if (baseMaterialDire == 0) {//正向截取
                code.substring(baseMaterialPos1 - 1, baseMaterialPos2) == baseMaterialCut
            } else {//反向截取
                code.substring(codeLength - baseMaterialPos2, codeLength - baseMaterialPos1 + 1) == baseMaterialCut
            }
        }
        return false
    }
}