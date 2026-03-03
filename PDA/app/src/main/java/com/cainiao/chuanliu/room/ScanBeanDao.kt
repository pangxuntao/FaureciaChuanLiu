package com.cainiao.chuanliu.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScanBeanDao {
    @Insert
    fun insert(user: ScanBean): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(data: List<ScanBean>): List<Long>

    @Query("SELECT * FROM scanData where time>:start and time<:end")
    fun getAllScanBean(start:Long,end:Long): List<ScanBean>

    @Query("DELETE FROM scanData")
    fun clear()
}