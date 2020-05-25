package com.example.intheclouds.repository

import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudDao
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudViewState
import com.example.intheclouds.util.DataState
import kotlinx.coroutines.*

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CaptionedCloudRepository(private val cloudDao: CloudDao) {

    fun allClouds(): LiveData<DataState<CaptionedCloudViewState>> {

        val clouds = runBlocking(Dispatchers.IO) {
            cloudDao.getAll()
        }
        var dataState = DataState.data(
            data = CaptionedCloudViewState(
                clouds = clouds
            )
        )
        var liveData = MutableLiveData<DataState<CaptionedCloudViewState>>()
        liveData.postValue(dataState)
        return liveData
    }

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