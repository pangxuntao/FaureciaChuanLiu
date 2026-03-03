package com.cainiao.excel

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import java.util.zip.ZipEntry

import java.util.zip.ZipOutputStream
object FileUtil {
    @Throws(IOException::class)
    fun zipFolder(path: String):Path {
        val sourceFolderPath = getSubPath(path)
        val zipPath = getSubPath("$path.zip")
        ZipOutputStream(Files.newOutputStream(zipPath)).use { zipOutputStream ->
            Files.walk(sourceFolderPath).use { stream ->
                stream.filter { p: Path ->
                    !Files.isDirectory(p)
                }.forEach { p: Path ->
                    val zipEntry = ZipEntry(sourceFolderPath.relativize(p).toString())
                    try {
                        zipOutputStream.putNextEntry(zipEntry)
                        Files.copy(p, zipOutputStream)
                        zipOutputStream.closeEntry()
                    } catch (e: IOException) {
                        System.err.println(e)
                    }
                }
            }
            println("writeFile11")
        }
        println("writeFile2")
        return zipPath
    }
    fun getCurrentPath(): Path {
        // 获取当前工作目录
        val currentDir = System.getProperty("user.dir")
        // 创建 Path 对象
        return Paths.get(currentDir)
    }

    fun getSubPath(subfolder: String): Path {
        // 获取当前路径并创建子路径
        val currentPath: Path = getCurrentPath()
        return currentPath.resolve(subfolder)
    }
}
