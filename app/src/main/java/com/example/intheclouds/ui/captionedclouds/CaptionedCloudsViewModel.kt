package com.example.intheclouds.ui.captionedclouds

import android.app.Application
import androidx.lifecycle.*
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.repository.CaptionedCloudRepository
import com.example.intheclouds.repository.ChooseCloudRepository
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudStateEvent
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudViewState
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.util.AbsentLiveData
import com.example.intheclouds.util.DataState

class CaptionedCloudsViewModel(application: Application): AndroidViewModel(application) {

    private val cloudsRepository = CaptionedCloudRepository(
        CloudsDatabase.getDatabase(application, viewModelScope)
            .cloudDao()
    )

    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    // - https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#9
    //val allCaptionedClouds: LiveData<List<CaptionedCloud>>

    /*init{
        val cloudDao = CloudsDatabase.getDatabase(application, viewModelScope).cloudDao()
        cloudsRepository = CaptionedCloudRepository(cloudDao)
        //allCaptionedClouds = cloudsRepository.allClouds
    }*/

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

            is CaptionedCloudStateEvent.loadCloudImages -> {

                println("DEBUG: getting cloud images")
                return cloudsRepository.allClouds()
            }

            is CaptionedCloudStateEvent.clickCloudImage -> {
                println("DEBUG: cloud clicked")
                return AbsentLiveData.create()

                /*var viewState = ChooseCloudViewState(
                    cloudToEdit = stateEvent.cloud
                )
                var dataState = DataState.data(
                    data = viewState
                )
                var result = MediatorLiveData<DataState<ChooseCloudViewState>>()
                result.postValue(dataState)
                return result*/
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
