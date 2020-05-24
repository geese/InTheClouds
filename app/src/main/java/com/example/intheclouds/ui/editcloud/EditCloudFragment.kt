package com.example.intheclouds.ui.editcloud

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.example.intheclouds.R
import kotlinx.android.synthetic.main.edit_cloud_fragment.*

private const val ARG_BITMAP = "cloud_bitmap"
private const val ARG_URL = "cloud_url"

class EditCloudFragment : Fragment() {

    companion object {
        fun newInstance(bitmap: Bitmap, url: String) = EditCloudFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_BITMAP, bitmap)
                putString(ARG_URL, url)
            }
        }
    }

    private lateinit var viewModel: EditCloudViewModel

    private lateinit var cloudBitmap: Bitmap
    private lateinit var cloudUrl: String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getParcelable<Bitmap>(ARG_BITMAP)?.let { cloudBitmap = it }
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
        image_view.setImageBitmap(cloudBitmap)
    }

}
