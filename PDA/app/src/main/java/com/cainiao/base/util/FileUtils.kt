package com.cainiao.base.util

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import java.util.zip.CRC32
import java.util.zip.Checksum

object FileUtils {
    /**
     * 获取sdcard的文件路径，不带/
     *
     * @return
     */
    fun getSDCardPath(): String? {
        val sdDir: File? = null
        val sdCardExist = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        return if (sdCardExist) Environment.getExternalStorageDirectory().path else "sdcard"
    }

    fun fileExist(path: String?): Boolean {
        val file = File(path)
        return file.exists() && file.isFile
    }

    fun fileNotExist(path: String?): Boolean {
        return !fileExist(path)
    }

    fun fileWithSuffix(path: String, suffix: String?): Boolean {
        if (TextUtils.isEmpty(suffix)) {
            throw NullPointerException("文件后缀suffix不能为空")
        }
        return path.endsWith(suffix!!)
    }

    fun fileNotWithSuffix(path: String, suffix: String?): Boolean {
        return !fileWithSuffix(path, suffix)
    }

    /**
     * 获取某个path下的，带suffixes后缀的文件
     *
     * @param path
     * @param suffixes
     * @return
     */
    fun getFilesWithSuffix(path: String?, vararg suffixes: String): List<File>? {
        val pathFile = File(path)
        val fileList: MutableList<File> = ArrayList()
        pathFile.listFiles { file: File ->
            val fileName = file.name
            for (suffix in suffixes) {
                if (fileName.lowercase(Locale.getDefault())
                        .endsWith(suffix.lowercase(Locale.getDefault()))
                ) {
                    fileList.add(file)
                    break
                }
            }
            true
        }
        return fileList
    }

    /**
     * 创建文件夹路径
     *
     * @param path
     * @return
     */
    fun createPath(path: String?): Boolean {
        return createDirectory(path)
    }

    /**
     * 创建文件夹路径
     *
     * @param path
     * @return
     */
    fun createDirectory(path: String?): Boolean {
        val pathFile = File(path)
        return if (pathFile.exists()) {
            pathFile.isDirectory
        } else pathFile.mkdirs()
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     * @return
     */
    fun deleteFileOrDirectory(file: File): Boolean {
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (file1 in files) {
                    deleteFileOrDirectory(file1)
                }
            }
        }
        return file.delete()
    }

    /**
     * 创建文件后刷新文件系统
     *
     * @param c
     * @param f
     */
    fun notifyFileCreate(c: Context, f: File) {
        FileScanner.instance(c)!!.scanFile(f)
    }

    private class FileScanner private constructor(private val context: Context) {
        private var mConn: MediaScannerConnection? = null
        private var mClient: ScannerClient? = null
        private var mFiles: MutableList<File>? = null

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mFiles = ArrayList()
                mClient = ScannerClient()
                mConn = MediaScannerConnection(context, mClient)
            }
        }

        fun scanFile(file: File) {
            if (file.isDirectory) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    mediaScanIntent.data =
                        Uri.parse("file://" + Environment.getExternalStorageDirectory())
                    context.sendBroadcast(mediaScanIntent)
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return
                }
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                val intent = Intent()
                intent.action = Intent.ACTION_MEDIA_MOUNTED
                intent.data = Uri.fromFile(Environment.getExternalStorageDirectory())
                context.sendBroadcast(intent)
            } else {
                mFiles!!.add(file)
                mConn!!.connect()
            }
        }

        private inner class ScannerClient : MediaScannerConnectionClient {
            override fun onMediaScannerConnected() {
                if (mFiles!!.size > 0) {
                    scan(mFiles)
                }
            }

            override fun onScanCompleted(path: String, uri: Uri) {
                L.v()
                if (mFiles!!.size == 0) {
                    mConn!!.disconnect()
                } else {
                    scan(mFiles)
                }
            }

            private fun scan(mFiles: MutableList<File>?) {
                val file = mFiles!![0]
                mFiles.removeAt(0)
                L.v("scan " + file.absolutePath)
                if (file.isFile) {
                    mConn!!.scanFile(file.absolutePath, null)
                } else {
                    val files = file.listFiles()
                    if (files != null) {
                        for (f in file.listFiles()) {
                            mFiles.add(f)
                        }
                        scan(mFiles)
                    }
                }
            }
        }

        companion object {
            private var instance: FileScanner? = null
            fun instance(context: Context): FileScanner? {
                synchronized(FileScanner::class.java) {
                    if (instance == null) {
                        instance = FileScanner(context.applicationContext)
                    }
                }
                return instance
            }
        }
    }

    @Throws(Exception::class)
    fun getFileEncoding(fullFileName: String): String {
        return EncodingUtil.getFileEncoding(fullFileName)
    }

    @Throws(Exception::class)
    fun getFileEncoding(fullFile: File): String {
        return EncodingUtil.getFileEncoding(fullFile)
    }

    fun checkSumFile(file: String?): Long {
        return checkSumFile(File(file))
    }

    fun checkSumFile(file: File): Long {
        if (file.exists()) {
            val checksum: Checksum = CRC32()
            try {
                FileInputStream(file).use { fis ->
                    val buffer = ByteArray(1024 * 1024)
                    var length: Int
                    while (fis.read(buffer).also { length = it } > 0) {
                        checksum.update(buffer, 0, length)
                    }
                    return checksum.value
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return -1
    }
}