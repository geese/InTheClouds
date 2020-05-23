package com.example.intheclouds.ui.choosecloud

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.intheclouds.model.Cumulus
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
    val dataState: LiveData<ChooseCloudViewState> = Transformations
        .switchMap(_stateEvent){ stateEvent ->
            stateEvent?.let {
                handleStateEvent(stateEvent)
            }
        }

    fun handleStateEvent(stateEvent: ChooseCloudStateEvent): LiveData<ChooseCloudViewState>{
        println("DEBUG: New StateEvent detected: $stateEvent")
        when(stateEvent){

            is ChooseCloudStateEvent.getCloudImages -> {

                // TEMPORARY!!  just to build this whole thing incrementally and see progress

                println("DEBUG: getting cloud images")
                return object: LiveData<ChooseCloudViewState>() {
                    override fun onActive() {
                        super.onActive()
                        val clouds: ArrayList<Cumulus.CloudImage> = ArrayList()
                        clouds.add(
                            Cumulus.CloudImage(
                                url = "https://pixabay.com/get/55e3d6464f5aa914f1dc8460962931781c3edeec504c704c7c2f7ad39049c459_640.jpg"
                            )
                        )
                        value = ChooseCloudViewState(cloudImages = clouds)
                    }
                }
            }

            is ChooseCloudStateEvent.clickCloudImage -> {
                return AbsentLiveData.create()
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
