package com.example.intheclouds.ui.choosecloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.intheclouds.model.Pixabay
import com.example.intheclouds.repository.ChooseCloudRepository
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

            is ChooseCloudStateEvent.LoadCloudImages -> {
                return ChooseCloudRepository.getCloudImages()
            }

            is ChooseCloudStateEvent.ClickCloudImage -> {
                return MutableLiveData<DataState<ChooseCloudViewState>>().apply {
                    value = DataState.data(
                        data = ChooseCloudViewState(cloudToEdit = stateEvent.cloud)
                    )
                }
            }

            is ChooseCloudStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setCloudImagesListData(clouds: ArrayList<Pixabay.CloudImage>) {
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
