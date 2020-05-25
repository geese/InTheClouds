package com.example.intheclouds.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CloudDao {

    @Query("SELECT * FROM captioned_cloud")
    fun getAll(): LiveData<List<CaptionedCloud>>

    @Query("SELECT * FROM captioned_cloud WHERE id = :cloudId")
    fun getCaptionedCloud(cloudId: Long): LiveData<CaptionedCloud>

    @Insert
    fun insertAll(vararg captionedClouds: CaptionedCloud)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cloud: CaptionedCloud)

    @Update
    fun update(cloud: CaptionedCloud)

    @Delete
    fun delete(cloud: CaptionedCloud)

    @Query("DELETE FROM captioned_cloud WHERE id = :cloudId")
    fun deleteById(cloudId: Long)

    @Query("DELETE FROM captioned_cloud")
    suspend fun deleteAll()
}