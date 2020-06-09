package com.example.intheclouds.ui.captionedclouds.state

import com.example.intheclouds.room.CaptionedCloud

sealed class CaptionedCloudStateEvent {

    object LoadCloudImages : CaptionedCloudStateEvent()

    data class ClickCloudImage(val cloud: CaptionedCloud) : CaptionedCloudStateEvent()

    object None : CaptionedCloudStateEvent()
}