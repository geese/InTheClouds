package com.example.intheclouds.ui.choosecloud.state

import com.example.intheclouds.model.Pixabay
import com.example.intheclouds.room.CaptionedCloud

/**
 * The ViewState is a wrapper class around
 * all the  data objects in the View.
 */
data class ChooseCloudViewState(
    var cloudImages: ArrayList<Pixabay.CloudImage>? = null,
    val cloudToEdit: CaptionedCloud? = null
)