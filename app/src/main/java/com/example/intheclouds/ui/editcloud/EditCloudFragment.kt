package com.example.intheclouds.ui.editcloud

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.intheclouds.R
import kotlinx.android.synthetic.main.edit_cloud_fragment.*

private const val ARG_ID = "cloud_id"
private const val ARG_URL = "cloud_url"

class EditCloudFragment : Fragment() {

    companion object {
        fun newInstance(id: Long, url: String) = EditCloudFragment().apply {
            arguments = Bundle().apply {
                putLong(ARG_ID, id)
                putString(ARG_URL, url)
            }
        }
    }

    private lateinit var viewModel: EditCloudViewModel

    private var cloudId: Long = -1
    private lateinit var cloudUrl: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getLong(ARG_ID)?.let { cloudId = it }
        arguments?.getString(ARG_URL)?.let { cloudUrl = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_cloud_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EditCloudViewModel::class.java)
        // TODO: Use the ViewModel

        textView.text = cloudUrl
    }

}
