package com.example.intheclouds.ui.choosecloud

import android.os.Bundle
import androidx.lifecycle.*
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.repository.ChooseCloudRepository
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudViewState
import com.example.intheclouds.ui.editcloud.EditCloudFragment
import com.example.intheclouds.util.AbsentLiveData
import com.example.intheclouds.util.Constants
import com.example.intheclouds.util.DataState
import com.example.intheclouds.util.NavigationExtra

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

            is ChooseCloudStateEvent.loadCloudImages -> {
                println("DEBUG: getting cloud images")
                return ChooseCloudRepository.getCloudImages()
            }

            is ChooseCloudStateEvent.clickCloudImage -> {
                println("DEBUG: cloud clicked")
                var extras = Bundle()
                extras.putParcelable(Constants.ARG_CAPTIONED_CLOUD, stateEvent.cloud)
                return MediatorLiveData<DataState<ChooseCloudViewState>>().apply {
                    value = DataState.data(
                        navigationExtra =  NavigationExtra(EditCloudFragment::class.java.simpleName, extras)
                    )
                }
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
