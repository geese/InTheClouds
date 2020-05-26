package com.example.intheclouds.repository

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

    fun insertOrUpdate(cloud: CaptionedCloud){
        var resultId = update(cloud)
        if (resultId == 0) {
            resultId = insert(cloud).toInt()
        }
        println("DEBUG: resultId: $resultId")
    }

    fun delete(cloud: CaptionedCloud) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                cloudDao.delete(cloud)
            }
        }
    }

    fun insert(cloud: CaptionedCloud) : Long {
        return runBlocking(Dispatchers.IO) {
            cloudDao.insert(cloud)
        }
    }

    fun update(cloud: CaptionedCloud) : Int {
        return runBlocking(Dispatchers.IO) {
            cloudDao.update(cloud)
        }
    }

    fun getCloud(id: Long): CaptionedCloud {  //todo DataState
        return cloudDao.getCloud(id)
    }

}