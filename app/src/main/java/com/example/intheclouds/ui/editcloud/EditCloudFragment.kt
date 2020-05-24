package com.example.intheclouds.ui.editcloud

import android.content.Context
import android.graphics.Bitmap
import android.os.BaseBundle
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Base64.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.example.intheclouds.R
import com.example.intheclouds.util.getBase64String
import kotlinx.android.synthetic.main.edit_cloud_fragment.*
import java.io.ByteArrayOutputStream
import java.util.*

private const val ARG_ENCODED_BITMAP = "cloud_encoded_bitmap"
private const val ARG_URL = "cloud_url"

class EditCloudFragment : Fragment() {

    companion object {
        fun newInstance(encodedBitmap: String, url: String) = EditCloudFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_ENCODED_BITMAP, encodedBitmap)
                putString(ARG_URL, url)
            }
        }
    }

    private lateinit var viewModel: EditCloudViewModel

    private lateinit var cloudEncodedBitmap: String
    private lateinit var cloudUrl: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(ARG_ENCODED_BITMAP)?.let { cloudEncodedBitmap = it }
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

        (activity as AppCompatActivity).supportActionBar?.title = "Caption A Cloud"
        editText.setText(cloudUrl)

        var decodedBytes = decode(cloudEncodedBitmap, DEFAULT)

        Glide.with(this)
            .asBitmap()
            .load(decodedBytes)
            .into(image_view)
    }
}
