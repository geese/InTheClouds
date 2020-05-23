package com.example.intheclouds.ui.addeditcloud

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.intheclouds.ui.addeditcloud.state.AddEditStateEvent
import com.example.intheclouds.ui.addeditcloud.state.AddEditViewState
import com.example.intheclouds.util.DataState

class AddEditViewModel : ViewModel() {

    private val _stateEvent: MutableLiveData<AddEditStateEvent> = MutableLiveData()
    private val _viewState: MutableLiveData<AddEditViewState> = MutableLiveData()

    val viewState: LiveData<AddEditViewState>
        get() = _viewState


}