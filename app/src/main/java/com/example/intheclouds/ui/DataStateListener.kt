package com.example.intheclouds.ui

import com.example.intheclouds.util.DataState

interface DataStateListener {
    fun onDataStateChange(dataState: DataState<*>?)
}