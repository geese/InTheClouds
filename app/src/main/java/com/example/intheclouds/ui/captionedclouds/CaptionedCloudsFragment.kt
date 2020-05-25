package com.example.intheclouds.ui.captionedclouds

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.intheclouds.R

class CaptionedCloudsFragment : Fragment() {

    companion object {
        fun newInstance() = CaptionedCloudsFragment()
    }

    private lateinit var viewModel: CaptionedCloudsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.captioned_clouds_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CaptionedCloudsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
