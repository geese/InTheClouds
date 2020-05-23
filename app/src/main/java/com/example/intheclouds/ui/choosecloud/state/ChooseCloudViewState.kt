package com.example.intheclouds.ui.choosecloud.state

import com.example.intheclouds.model.Cumulus

/**
 * The ViewState is a wrapper class around
 * all the  data objects in the View.
 */
data class ChooseCloudViewState (
    var cloudImages: ArrayList<Cumulus.CloudImage>? = null
)