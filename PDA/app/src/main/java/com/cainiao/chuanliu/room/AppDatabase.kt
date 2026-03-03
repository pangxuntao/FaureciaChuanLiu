package com.cainiao.chuanliu.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScanBean::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun scanDao(): ScanBeanDao
}