package com.example.intheclouds.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.intheclouds.R
import com.example.intheclouds.ui.choosecloud.ChooseCloudFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChooseCloudFragment.newInstance())
            .commit()
    }
}

