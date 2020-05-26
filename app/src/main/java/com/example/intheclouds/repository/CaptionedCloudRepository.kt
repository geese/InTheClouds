package com.example.intheclouds.repository

import android.provider.Settings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudDao
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudViewState
import com.example.intheclouds.ui.editcloud.state.EditCloudViewState
import com.example.intheclouds.util.DataState
import com.example.intheclouds.util.Event
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

    fun insertOrUpdate(cloud: CaptionedCloud) : LiveData<DataState<EditCloudViewState>> {
        var resultId = update(cloud)
        if (resultId == 0) {
            resultId = insert(cloud).toInt()
        }
        return MediatorLiveData<DataState<EditCloudViewState>>().apply{
            value = DataState(
                message = Event("Cloud Saved!"),
                data = Event.dataEvent(EditCloudViewState(savedCloudId = resultId))
            )
        }
    }

    fun delete(cloud: CaptionedCloud) : LiveData<DataState<EditCloudViewState>> {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                cloudDao.delete(cloud)
            }
        }
        return MediatorLiveData<DataState<EditCloudViewState>>().apply {
            value = DataState(
                message = Event("Cloud Deleted!"),
                data = Event.dataEvent(EditCloudViewState(deletedCloudId = cloud.id))
            )
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



    fun getCaptionedCloud(id: Long): LiveData<CaptionedCloud> {  //todo DataState
        return cloudDao.getCaptionedCloud(id)
    }

}