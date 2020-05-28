package com.example.intheclouds.ui.captionedclouds

import android.app.Application
import androidx.lifecycle.*
import com.example.intheclouds.repository.CaptionedCloudRepository
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudStateEvent
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudViewState
import com.example.intheclouds.util.AbsentLiveData
import com.example.intheclouds.util.DataState

class CaptionedCloudsViewModel(application: Application): AndroidViewModel(application) {

    private val cloudsRepository = CaptionedCloudRepository(
        CloudsDatabase.getDatabase(application, viewModelScope)
            .cloudDao()
    )

    // triggers the different actions to take
    private val _stateEvent: MutableLiveData<CaptionedCloudStateEvent> = MutableLiveData()

    // observing the different data models that are visible in the view
    private val _viewState: MutableLiveData<CaptionedCloudViewState> = MutableLiveData()

    val viewState: LiveData<CaptionedCloudViewState>
        get() = _viewState

    // listen to state events - when one is detected, handle it and return LiveData accordingly
    val dataState: LiveData<DataState<CaptionedCloudViewState>> = Transformations
        .switchMap(_stateEvent){ stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun handleStateEvent(stateEvent: CaptionedCloudStateEvent): LiveData<DataState<CaptionedCloudViewState>>{
        println("DEBUG: New StateEvent detected: $stateEvent")
        when(stateEvent){

            is CaptionedCloudStateEvent.LoadCloudImages -> {
                return cloudsRepository.allClouds()
            }

            is CaptionedCloudStateEvent.ClickCloudImage -> {
                return MutableLiveData<DataState<CaptionedCloudViewState>>().apply {
                    value = DataState.data(
                        data = CaptionedCloudViewState(cloudToEdit = stateEvent.cloud)
                    )
                }
            }

            is CaptionedCloudStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setCloudImagesListData(clouds: List<CaptionedCloud>) {
        val update = getCurrentViewStateOrNew()
        update.clouds = clouds
        _viewState.value = update
    }

    fun getCurrentViewStateOrNew(): CaptionedCloudViewState {
        return viewState.value?.run {
            this
        } ?: CaptionedCloudViewState()
    }

    fun setStateEvent(event: CaptionedCloudStateEvent) {
        _stateEvent.value = event
    }
}
