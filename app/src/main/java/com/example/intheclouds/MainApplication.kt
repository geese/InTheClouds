package com.example.intheclouds

import android.app.Application
import timber.log.Timber

class MyDebugTree : Timber.DebugTree() {
    override fun createStackElementTag(element: StackTraceElement): String? {
        return String.format(
            "[C:%s] [M:%s] [L:%s]",
            super.createStackElementTag(element),
            element.methodName,
            element.lineNumber
        )
    }
}

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // "Timber" Logging Library
        if (BuildConfig.DEBUG) {
            Timber.plant(MyDebugTree())
        }
    }

}