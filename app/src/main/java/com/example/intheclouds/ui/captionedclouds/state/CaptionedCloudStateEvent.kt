package com.example.intheclouds.ui.captionedclouds.state

import com.example.intheclouds.room.CaptionedCloud

sealed class CaptionedCloudStateEvent {

    class loadCloudImages: CaptionedCloudStateEvent()

    data class clickCloudImage(
        val cloud: CaptionedCloud
    ): CaptionedCloudStateEvent()

    class None: CaptionedCloudStateEvent()
}