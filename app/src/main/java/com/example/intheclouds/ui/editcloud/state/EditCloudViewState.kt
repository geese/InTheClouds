package com.example.intheclouds.ui.editcloud.state

import com.example.intheclouds.room.CaptionedCloud

/**
 * The ViewState is a wrapper class around
 * all the  data objects in the View.
 */
data class EditCloudViewState (
    val savedCloudId: Int? = null,
    val deletedCloudId: Long? = null
)