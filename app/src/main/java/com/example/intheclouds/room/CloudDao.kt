package com.example.intheclouds.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CloudDao {

    @Query("SELECT id FROM captioned_cloud WHERE id = :id")
    suspend fun getCloudId(id: Long) : Int

    @Query("SELECT * FROM captioned_cloud")
    suspend fun getAll(): List<CaptionedCloud>

    @Query("SELECT * FROM captioned_cloud WHERE id = :cloudId")
    fun getCaptionedCloud(cloudId: Long): LiveData<CaptionedCloud>

    @Insert
    fun insertAll(vararg captionedClouds: CaptionedCloud)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cloud: CaptionedCloud) : Long

    @Update
    suspend fun update(cloud: CaptionedCloud) : Int

    @Delete
    suspend fun delete(cloud: CaptionedCloud)

    @Query("DELETE FROM captioned_cloud WHERE id = :cloudId")
    fun deleteById(cloudId: Long)

    @Query("DELETE FROM captioned_cloud")
    suspend fun deleteAll()
}