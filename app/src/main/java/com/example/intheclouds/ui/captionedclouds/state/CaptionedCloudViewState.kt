package com.example.intheclouds.ui.captionedclouds.state

import com.example.intheclouds.room.CaptionedCloud

/**
 * The ViewState is a wrapper class around
 * all the  data objects in the View.
 */
data class CaptionedCloudViewState(
    var clouds: List<CaptionedCloud>? = null,
    var cloudToEdit: CaptionedCloud? = null
)