package com.example.intheclouds.ui.choosecloud.state

sealed class ChooseCloudStateEvent {

    class getCloudImages: ChooseCloudStateEvent()
    class clickCloudImage: ChooseCloudStateEvent()

    class None: ChooseCloudStateEvent()
}