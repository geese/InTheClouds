package com.example.intheclouds.ui.captionedclouds

import android.app.Application
import android.graphics.Paint
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.intheclouds.repository.CaptionedCloudRepository
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.room.CaptionedCloud

class CaptionedCloudsViewModel(application: Application): AndroidViewModel(application) {


    private val cloudsRepository: CaptionedCloudRepository

    val allCaptionedClouds: LiveData<List<CaptionedCloud>>

    init{
        val cloudDao = CloudsDatabase.getInstance(application, viewModelScope).cloudDao()
        cloudsRepository = CaptionedCloudRepository(cloudDao)
        allCaptionedClouds = cloudsRepository.allClouds
    }

}
