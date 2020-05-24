package com.example.intheclouds.ui.choosecloud.state

sealed class ChooseCloudStateEvent {

    class getCloudImages: ChooseCloudStateEvent()
    data class clickCloudImage(val id: Long? = null, val url: String? = null): ChooseCloudStateEvent()

    class None: ChooseCloudStateEvent()
}