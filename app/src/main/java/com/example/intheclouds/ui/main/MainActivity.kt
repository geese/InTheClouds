package com.example.intheclouds.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.intheclouds.R
import com.example.intheclouds.model.CaptionedCloudModel
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.ui.captionedclouds.CaptionedCloudsFragment
import com.example.intheclouds.ui.choosecloud.*
import com.example.intheclouds.ui.choosecloud.ChooseCloudFragment.ChooseCloudFragmentListener
import com.example.intheclouds.ui.editcloud.EditCloudFragment
import com.example.intheclouds.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    AppCompatActivity(),
    DataStateListener,
    ChooseCloudFragmentListener,
    EditCloudFragment.EditCloudFragmentListener {

    lateinit var chooseCloudViewModel: ChooseCloudViewModel
    lateinit var database: CloudsDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooseCloudViewModel = ViewModelProvider(this).get(ChooseCloudViewModel::class.java)

        showChooseCloudFragment()
        //showCaptionedCloudsFragment()
    }

    override fun onCloudClicked(cloud: CaptionedCloud?) {
        cloud?.let {
            showEditCloudFragment(cloud)
        }
    }

    override fun onCloudSaved() {
        showToast("Cloud Saved!")
        showCaptionedCloudsFragment()
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

    private fun showCaptionedCloudsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CaptionedCloudsFragment.newInstance())
            .commit()
    }

    private fun showChooseCloudFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChooseCloudFragment.newInstance())
            .commit()
    }

    private fun showEditCloudFragment(cloud: CaptionedCloud) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EditCloudFragment.newInstance(cloud))
            .addToBackStack("edit_cloud")
            .commit()
    }
}

