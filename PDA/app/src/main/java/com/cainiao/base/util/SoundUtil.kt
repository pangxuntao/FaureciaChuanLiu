//package com.cainiao.base.util
//
//import android.media.AudioManager
//import android.media.SoundPool
//import com.cainiao.base.App
//import com.cainiao.chuanliu.R
//
//
///**
// * @Auther: pxt
// * @Date: 2021/5/31 14:04
// * @Description: com.faurecia.base
// * @Version: 1.0
// */
//object SoundUtil {
//    private val soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
//    private val soundPoolMap = HashMap<Int, Int>()
//    private const val KEY_SOUND_SCAN: Int = 1
//    private const val KEY_SOUND_ERROR: Int = 2
//    private const val KEY_SOUND_JIU: Int = 3
//    private const val KEY_SOUND_DCCG: Int = 4
//    private const val KEY_SOUND_CUOWU: Int = 5
//    private const val KEY_SOUND_CHENGG: Int = 6
//
//    init {
//        soundPoolMap.put(KEY_SOUND_SCAN, soundPool.load(App.app, R.raw.scan_new, 1))
//        soundPoolMap.put(KEY_SOUND_ERROR, soundPool.load(App.app, R.raw.scan_wrong, 1))
//        soundPoolMap.put(KEY_SOUND_JIU, soundPool.load(App.app, R.raw.jiu, 1))
//        soundPoolMap.put(KEY_SOUND_DCCG, soundPool.load(App.app, R.raw.dccg, 1))
//        soundPoolMap.put(KEY_SOUND_CUOWU, soundPool.load(App.app, R.raw.cuowu, 1))
//        soundPoolMap.put(KEY_SOUND_CHENGG, soundPool.load(App.app, R.raw.chenggong, 1))
//    }
//
//    fun scan() {
//        playSound(KEY_SOUND_SCAN)
//    }
//
//    fun error() {
//        playSound(KEY_SOUND_ERROR)
//    }
//    fun jiu() {
//        playSound(KEY_SOUND_JIU)
//    }
//    fun dccg() {
//        playSound(KEY_SOUND_DCCG)
//    }
//    fun cuowu() {
//        playSound(KEY_SOUND_CUOWU)
//    }
//    fun chenggong() {
//        playSound(KEY_SOUND_CHENGG)
//    }
//
//    private fun playSound(index: Int) {
//        soundPool.play(soundPoolMap.get(index)!!, 1f, 1f, 0, 0, 1f)
//    }
//}