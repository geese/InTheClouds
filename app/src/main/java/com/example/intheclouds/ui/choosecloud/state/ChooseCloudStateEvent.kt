package com.example.intheclouds.ui.choosecloud.state

import com.example.intheclouds.room.CaptionedCloud

sealed class ChooseCloudStateEvent {

    class loadCloudImages: ChooseCloudStateEvent()

    data class clickCloudImage(
        val cloud: CaptionedCloud
    ): ChooseCloudStateEvent()

    class None: ChooseCloudStateEvent()
}