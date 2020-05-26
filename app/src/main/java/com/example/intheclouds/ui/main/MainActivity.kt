package com.example.intheclouds.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.intheclouds.R
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.ui.captionedclouds.CaptionedCloudsFragment
import com.example.intheclouds.ui.captionedclouds.CaptionedCloudsFragment.CaptionedCloudsFragmentListener
import com.example.intheclouds.ui.choosecloud.*
import com.example.intheclouds.ui.choosecloud.ChooseCloudFragment.ChooseCloudFragmentListener
import com.example.intheclouds.ui.editcloud.EditCloudFragment
import com.example.intheclouds.ui.editcloud.EditCloudFragment.EditCloudFragmentListener
import com.example.intheclouds.util.DataState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
    AppCompatActivity(),
    DataStateListener,
    CaptionedCloudsFragmentListener,
    ChooseCloudFragmentListener,
    EditCloudFragmentListener {

    lateinit var chooseCloudViewModel: ChooseCloudViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooseCloudViewModel = ViewModelProvider(this).get(ChooseCloudViewModel::class.java)

        // showChooseCloudFragment()
        showCaptionedCloudsFragment()
    }

    override fun onAddCloudClicked() {
        showChooseCloudFragment()
    }

    override fun onCloudClicked(cloud: CaptionedCloud?, newCloud: Boolean) {
        cloud?.let {
            showEditCloudFragment(cloud, newCloud)
        }
    }

    override fun onCloudSaved() {
        showCaptionedCloudsFragment()
    }

    override fun onCloudDeleted() {
        showCaptionedCloudsFragment()
    }

    override fun goHome() {
        showCaptionedCloudsFragment()
    }

    override fun onDataStateChange(dataState: DataState<*>?) {
        handleDataStateChange(dataState)
    }

    private fun handleDataStateChange(dataState: DataState<*>?) {
        dataState?.let {

            // handle loading
            showProgressBar(dataState.loading)

            // handle message
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
            .addToBackStack("choose_cloud")
            .commit()
    }

    private fun showEditCloudFragment(cloud: CaptionedCloud, newCloud: Boolean) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, EditCloudFragment.newInstance(cloud, newCloud))
            .addToBackStack("edit_cloud")
            .commit()
    }
}

