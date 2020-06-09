package com.example.intheclouds.room

import androidx.room.*

@Dao
interface CloudDao {

    @Query("SELECT * FROM captioned_cloud")
    suspend fun getAll(): List<CaptionedCloud>

    @Query("SELECT * FROM captioned_cloud WHERE id = :cloudId")
    fun getCloud(cloudId: Long): CaptionedCloud

    @Insert
    fun insertAll(vararg captionedClouds: CaptionedCloud)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cloud: CaptionedCloud): Long

    @Update
    suspend fun update(cloud: CaptionedCloud): Int

    @Delete
    suspend fun delete(cloud: CaptionedCloud)

    @Query("DELETE FROM captioned_cloud WHERE id = :cloudId")
    fun deleteById(cloudId: Long)

    @Query("DELETE FROM captioned_cloud")
    suspend fun deleteAll()
}