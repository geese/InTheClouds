package com.example.intheclouds.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import com.example.intheclouds.R
import com.example.intheclouds.room.CloudsDatabase
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.util.DataState
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope

class MainActivity :
    AppCompatActivity(),
    DataStateListener {

    override fun onSupportNavigateUp() =
        findNavController(this, R.id.navHostFragment).navigateUp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainContext = this
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

    companion object {
        var mainContext: Context? = null

        private val _sampleCloudInserted = MutableLiveData<Boolean>().apply { value = false }
        val isSampleCloudInserted: LiveData<Boolean> = _sampleCloudInserted
        fun setIsSampleCloudInserted(isInserted: Boolean) = _sampleCloudInserted.postValue(isInserted)
    }
}

