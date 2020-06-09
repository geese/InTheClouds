package com.example.intheclouds.ui.choosecloud.state

import com.example.intheclouds.room.CaptionedCloud

sealed class ChooseCloudStateEvent {

    object LoadCloudImages : ChooseCloudStateEvent()

    data class ClickCloudImage(val cloud: CaptionedCloud) : ChooseCloudStateEvent()

    object None : ChooseCloudStateEvent()

}