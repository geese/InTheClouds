package com.example.intheclouds.ui.editcloud

import android.app.Application
import androidx.lifecycle.*
import com.example.intheclouds.repository.CaptionedCloudRepository
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.ui.captionedclouds.CaptionedCloudsFragment
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import com.example.intheclouds.ui.editcloud.state.EditCloudStateEvent
import com.example.intheclouds.ui.editcloud.state.EditCloudViewState
import com.example.intheclouds.util.AbsentLiveData
import com.example.intheclouds.util.DataState
import com.example.intheclouds.util.NavigationExtra

class EditCloudViewModel(application: Application) : AndroidViewModel(application) {

    private val cloudsRepository = CaptionedCloudRepository(
        CloudsDatabase.getDatabase(application, viewModelScope)
            .cloudDao()
    )

   /* fun insert(cloud: CaptionedCloud) = viewModelScope.launch(Dispatchers.IO) {
        captionedCloudRepository.insert(cloud)
    }*/

    // triggers the different actions to take
    private val _stateEvent: MutableLiveData<EditCloudStateEvent> = MutableLiveData()

    // observing the different data models that are visible in the view
    private val _viewState: MutableLiveData<EditCloudViewState> = MutableLiveData()

    val viewState: LiveData<EditCloudViewState>
        get() = _viewState

    // listen to state events - when one is detected, handle it and return LiveData accordingly
    val dataState: LiveData<DataState<EditCloudViewState>> = Transformations
        .switchMap(_stateEvent){ stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun handleStateEvent(stateEvent: EditCloudStateEvent): LiveData<DataState<EditCloudViewState>>{
        println("DEBUG: New StateEvent detected: $stateEvent")
        when(stateEvent){

            is EditCloudStateEvent.SaveCaptionedCloud -> {
                println("DEBUG: saving captioned cloud")
                cloudsRepository.insertOrUpdate(stateEvent.cloud)
                return MediatorLiveData<DataState<EditCloudViewState>>().apply {
                    value = DataState.data(
                        message = "Cloud Saved!",
                        navigationExtra = NavigationExtra(CaptionedCloudsFragment::class.java.simpleName)
                    )
                }
            }

            is EditCloudStateEvent.DeleteCaptionedCloud -> {
                cloudsRepository.delete(stateEvent.cloud)
                return MediatorLiveData<DataState<EditCloudViewState>>().apply {
                    value = DataState.data(
                        message = "Cloud Deleted!",
                        navigationExtra = NavigationExtra(CaptionedCloudsFragment::class.java.simpleName)
                    )
                }
            }

            is EditCloudStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setStateEvent(event: EditCloudStateEvent) {
        _stateEvent.value = event
    }
}
