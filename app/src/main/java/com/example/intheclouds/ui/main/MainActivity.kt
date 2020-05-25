package com.example.intheclouds.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.intheclouds.R
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.ui.choosecloud.*
import com.example.intheclouds.ui.choosecloud.ChooseCloudFragment.ChooseCloudFragmentListener
import com.example.intheclouds.ui.editcloud.EditCloudFragment
import com.example.intheclouds.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    DataStateListener, ChooseCloudFragmentListener {

    lateinit var chooseCloudViewModel: ChooseCloudViewModel
    lateinit var database: CloudsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        chooseCloudViewModel = ViewModelProvider(this).get(ChooseCloudViewModel::class.java)

        showChooseCloudFragment()
    }

    override fun onCloudClicked(encodedBitmap: String?, url: String?) {
        if (encodedBitmap != null && url != null) {
            showEditCloudFragment(encodedBitmap, url)
        }
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        handleDataStateChange(dataState)
    }

    private fun handleDataStateChange(dataState: DataState<*>?) {
        dataState?.let {

            // handle loading
            showProgressBar(dataState.loading)

            // handle message (error)
            dataState.message?.let {  event ->
                event.getContentIfNotHandled()?.let { message ->
                    showToast(message)
                }
            }
        }
    }

    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar(isVisible: Boolean){
        if(isVisible){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.INVISIBLE
        }
    }

    private fun showChooseCloudFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChooseCloudFragment.newInstance())
            .commit()
    }

    private fun showEditCloudFragment(encodedBitmap: String, url: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EditCloudFragment.newInstance(encodedBitmap, url))
            .addToBackStack("edit_cloud")
            .commit()
    }
}

