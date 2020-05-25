package com.example.intheclouds.ui.editcloud

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.intheclouds.repository.CaptionedCloudRepository
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.room.CaptionedCloud
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditCloudViewModel(application: Application) : AndroidViewModel(application) {

    private val captionedCloudRepository: CaptionedCloudRepository

    init {
        val cloudDao = CloudsDatabase.getDatabase(application, viewModelScope).cloudDao()
        captionedCloudRepository =
            CaptionedCloudRepository(cloudDao)
    }

    fun insert(cloud: CaptionedCloud) = viewModelScope.launch(Dispatchers.IO) {
        captionedCloudRepository.insert(cloud)
    }
}
