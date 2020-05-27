package com.example.intheclouds.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudDao
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudViewState
import com.example.intheclouds.ui.main.MainActivity
import com.example.intheclouds.util.DataState
import com.example.intheclouds.util.toByteArray
import kotlinx.coroutines.*

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CaptionedCloudRepository(private val cloudDao: CloudDao) {

    fun allClouds(): LiveData<DataState<CaptionedCloudViewState>> {

        val allClouds = runBlocking(Dispatchers.IO) {
            cloudDao.getAll()
        }
        return MediatorLiveData<DataState<CaptionedCloudViewState>>().apply {
            value = DataState.data(
                data = CaptionedCloudViewState(
                    clouds = allClouds
                )
            )
        }
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

}