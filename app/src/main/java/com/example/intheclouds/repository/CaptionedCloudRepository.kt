package com.example.intheclouds.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudDao
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudViewState
import com.example.intheclouds.util.DataState
import kotlinx.coroutines.*
import timber.log.Timber.d

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CaptionedCloudRepository(private val cloudDao: CloudDao) {

    fun allClouds(): LiveData<DataState<CaptionedCloudViewState>> {

        // todo: I am new to coroutines and don't know if "runBlocking" is the best thing to do here
        val allClouds = runBlocking(Dispatchers.IO) {
            cloudDao.getAll()
        }
        return MutableLiveData<DataState<CaptionedCloudViewState>>().apply {
            value = DataState.data(
                data = CaptionedCloudViewState(
                    clouds = allClouds
                )
            )
        }
    }

    suspend fun insertOrUpdate(cloud: CaptionedCloud) {
        var resultId = update(cloud)
        if (resultId == 0) {
            resultId = insert(cloud).toInt()
        }
        d("DEBUG: resultId: $resultId")
    }

    fun delete(cloud: CaptionedCloud) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                cloudDao.delete(cloud)
            }
        }
    }

    private suspend fun insert(cloud: CaptionedCloud): Long {
        return cloudDao.insert(cloud)
    }

    private suspend fun update(cloud: CaptionedCloud): Int {
        return cloudDao.update(cloud)
    }
}