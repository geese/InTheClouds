package com.example.intheclouds.ui.editcloud.state

import com.example.intheclouds.room.CaptionedCloud

sealed class EditCloudStateEvent {

    data class SaveCaptionedCloud(val cloud: CaptionedCloud) : EditCloudStateEvent()
    data class DeleteCaptionedCloud(val cloud: CaptionedCloud) : EditCloudStateEvent()
    object None : EditCloudStateEvent()
}