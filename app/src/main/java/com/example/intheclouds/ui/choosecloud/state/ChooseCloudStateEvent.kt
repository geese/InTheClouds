package com.example.intheclouds.ui.choosecloud.state

import android.graphics.Bitmap

sealed class ChooseCloudStateEvent {

    class getCloudImages: ChooseCloudStateEvent()
    data class clickCloudImage(
        val id: Long? = null,
        val url: String? = null,
        val encodedBitmap: String? = null
    ): ChooseCloudStateEvent()

    class None: ChooseCloudStateEvent()
}