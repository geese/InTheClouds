package com.example.intheclouds.repository

import androidx.lifecycle.LiveData
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudDao

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CaptionedCloudRepository(private val cloudDao: CloudDao) {

    val allClouds: LiveData<List<CaptionedCloud>> = cloudDao.getAll()

    suspend fun insert(cloud: CaptionedCloud) {
        cloudDao.insert(cloud)
    }

    fun update(cloud: CaptionedCloud) {
        cloudDao.update(cloud)
    }

    fun delete(cloud: CaptionedCloud) {
        cloudDao.delete(cloud)
    }

    fun getCaptionedCloud(id: Long): LiveData<CaptionedCloud> {  //todo DataState
        return cloudDao.getCaptionedCloud(id)
    }

}