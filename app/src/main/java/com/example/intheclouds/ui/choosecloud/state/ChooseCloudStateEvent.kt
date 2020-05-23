package com.example.intheclouds.ui.choosecloud.state

sealed class ChooseCloudStateEvent {

    class clickCloudImage: ChooseCloudStateEvent()

    class None: ChooseCloudStateEvent()
}