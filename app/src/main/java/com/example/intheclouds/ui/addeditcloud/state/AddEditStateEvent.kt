package com.example.intheclouds.ui.addeditcloud.state

sealed class AddEditStateEvent {

    class GetCumulusImagesEvent: AddEditStateEvent()

    class None: AddEditStateEvent()

}