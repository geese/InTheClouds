package com.example.intheclouds.ui.choosecloud

import android.widget.Toast
import androidx.lifecycle.*
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.repository.choosecloud.ChooseCloudRepository
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.util.AbsentLiveData
import com.example.intheclouds.util.DataState

class ChooseCloudViewModel : ViewModel() {

    // triggers the different actions to take
    private val _stateEvent: MutableLiveData<ChooseCloudStateEvent> = MutableLiveData()

    // observing the different data models that are visible in the view
    private val _viewState: MutableLiveData<ChooseCloudViewState> = MutableLiveData()

    val viewState: LiveData<ChooseCloudViewState>
        get() = _viewState

    // listen to state events - when one is detected, handle it and return LiveData accordingly
    val dataState: LiveData<DataState<ChooseCloudViewState>> = Transformations
        .switchMap(_stateEvent){ stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun handleStateEvent(stateEvent: ChooseCloudStateEvent): LiveData<DataState<ChooseCloudViewState>>{
        println("DEBUG: New StateEvent detected: $stateEvent")
        when(stateEvent){

            is ChooseCloudStateEvent.getCloudImages -> {

                println("DEBUG: getting cloud images")
                return ChooseCloudRepository.getCloudImages()
            }

            is ChooseCloudStateEvent.clickCloudImage -> {
                println("DEBUG: cloud clicked")
                var viewState = ChooseCloudViewState(
                    editCloud = Pair(stateEvent.id, stateEvent.url)
                )
                var dataState = DataState.data(
                    data = viewState
                )
                var result = MediatorLiveData<DataState<ChooseCloudViewState>>()
                result.postValue(dataState)
                return result
            }

            is ChooseCloudStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setCloudImagesListData(clouds: ArrayList<Cumulus.CloudImage>) {
        val update = getCurrentViewStateOrNew()
        update.cloudImages = clouds
        _viewState.value = update
    }

    fun getCurrentViewStateOrNew(): ChooseCloudViewState {
        return viewState.value?.run {
            this
        } ?: ChooseCloudViewState()
    }

    fun setStateEvent(event: ChooseCloudStateEvent) {
        _stateEvent.value = event
    }

}
