package com.example.intheclouds

import android.app.Application
import androidx.room.Room
import com.example.intheclouds.room.CloudsDatabase

class InTheCloudsApplication: Application() {

    companion object {
        var database: CloudsDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, CloudsDatabase::class.java,
        "in-the-clouds-db").build()
    }
}