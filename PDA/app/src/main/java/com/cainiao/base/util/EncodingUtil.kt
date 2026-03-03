package com.cainiao.base.util

import java.io.*
import java.util.*

object EncodingUtil {
    private const val BYTE_SIZE = 8
    var CODE_UTF8 = "UTF-8"
    var CODE_UTF8_BOM = "UTF-8_BOM"
    var CODE_GBK = "GBK"

    /**
     * 通过文件全名称获取编码集名称
     *
     * @param fullFileName
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFileEncoding(fullFileName: String): String {
        return getFileEncoding(fullFileName, true)
    }

    @Throws(Exception::class)
    fun getFileEncoding(fullFile: File): String {
        return getFileEncoding(fullFile, true)
    }

    @Throws(Exception::class)
    fun getFileEncoding(fullFileName: String, ignoreBom: Boolean): String {
        return getEncoding(fullFileName, ignoreBom)
    }

    @Throws(Exception::class)
    fun getFileEncoding(fullFile: File, ignoreBom: Boolean): String {
        return getEncoding(fullFile, ignoreBom)
    }

    /**
     * 通过文件缓存流获取编码集名称，文件流必须为未曾
     *
     * @param fullFileName
     * @param ignoreBom    是否忽略utf-8 bom
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun getEncoding(fullFileName: String, ignoreBom: Boolean): String {
        return getEncoding(File(fullFileName), ignoreBom)
    }

    @Throws(Exception::class)
    private fun getEncoding(fullFileName: File, ignoreBom: Boolean): String {
        val bis = BufferedInputStream(FileInputStream(fullFileName))
        bis.mark(10)
        var encodeType = "未识别"
        val head = ByteArray(3)
        bis.read(head)
        L.e(head[0])
        L.e(head[1])
        L.e(head[2])
        encodeType = if (head[0].toInt() == -1 && head[1].toInt() == -2) {
            "UTF-16LE"
        } else if (head[0].toInt() == -2 && head[1].toInt() == -1) {
            "UTF-16BE"
        } else if (head[0].toInt() == -17 && head[1].toInt() == -69 && head[2].toInt() == -65) { //带BOM
            if (ignoreBom) {
                CODE_UTF8
            } else {
                CODE_UTF8_BOM
            }
        } else if (isUTF8(bis)) {
            CODE_UTF8
        } else {
            CODE_GBK
        }
        return encodeType
    }

    /**
     * 是否是无BOM的UTF8格式，不判断常规场景，只区分无BOM UTF8和GBK
     *
     * @param bis
     * @return
     */
    @Throws(Exception::class)
    private fun isUTF8(bis: BufferedInputStream): Boolean {
        bis.reset()
        //读取第一个字节
        var code = bis.read()
        do {
            val bitSet = convert2BitSet(code)
            //判断是否为单字节
            if (bitSet[0]) { //多字节时，再读取N个字节
                if (!checkMultiByte(bis, bitSet)) { //未检测通过,直接返回
                    return false
                }
            } else {
                //单字节时什么都不用做，再次读取字节
            }
            code = bis.read()
        } while (code != -1)
        return true
    }

    /**
     * 检测多字节，判断是否为utf8，已经读取了一个字节
     *
     * @param bis
     * @param bitSet
     * @return
     */
    @Throws(Exception::class)
    private fun checkMultiByte(bis: BufferedInputStream, bitSet: BitSet): Boolean {
        val count = getCountOfSequential(bitSet)
        val bytes = ByteArray(count - 1) //已经读取了一个字节，不能再读取
        bis.read(bytes)
        for (b in bytes) {
            if (!checkUtf8Byte(b)) {
                return false
            }
        }
        return true
    }

    /**
     * 检测单字节，判断是否为utf8
     *
     * @param b
     * @return
     */
    @Throws(Exception::class)
    private fun checkUtf8Byte(b: Byte): Boolean {
        val bitSet = convert2BitSet(b.toInt())
        return bitSet[0] && !bitSet[1]
    }

    /**
     * 检测bitSet中从开始有多少个连续的1
     *
     * @param bitSet
     * @return
     */
    private fun getCountOfSequential(bitSet: BitSet): Int {
        var count = 0
        for (i in 0 until BYTE_SIZE) {
            if (bitSet[i]) {
                count++
            } else {
                break
            }
        }
        return count
    }


    /**
     * 将整形转为BitSet
     *
     * @param code
     * @return
     */
    private fun convert2BitSet(code: Int): BitSet {
        val bitSet = BitSet(BYTE_SIZE)
        for (i in 0 until BYTE_SIZE) {
            val tmp3 = code shr BYTE_SIZE - i - 1
            val tmp2 = 0x1 and tmp3
            if (tmp2 == 1) {
                bitSet.set(i)
            }
        }
        return bitSet
    }

    /**
     * 将一指定编码的文件转换为另一编码的文件
     *
     * @param oldFullFileName
     * @param oldCharsetName
     * @param newFullFileName
     * @param newCharsetName
     */
    @Throws(Exception::class)
    fun convert(
        oldFullFileName: String?,
        oldCharsetName: String?,
        newFullFileName: String,
        newCharsetName: String?
    ) {
        var newFullFileName = newFullFileName
        val content = StringBuffer()
        val bin =
            BufferedReader(InputStreamReader(FileInputStream(oldFullFileName), oldCharsetName))
        var line: String?
        while (bin.readLine().also { line = it } != null) {
            content.append(line)
            content.append(System.getProperty("line.separator"))
        }
        newFullFileName = newFullFileName.replace("\\", "/")
        val dir = File(newFullFileName.substring(0, newFullFileName.lastIndexOf("/")))
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val out: Writer = OutputStreamWriter(FileOutputStream(newFullFileName), newCharsetName)
        out.write(content.toString())
    }
}