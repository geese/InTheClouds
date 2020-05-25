package com.example.intheclouds.ui.editcloud

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.intheclouds.R
import com.example.intheclouds.model.CaptionedCloudModel
import com.example.intheclouds.room.CaptionedCloud
import kotlinx.android.synthetic.main.edit_cloud_fragment.*

private const val ARG_CAPTIONED_CLOUD = "captioned_cloud"

class EditCloudFragment : Fragment() {

    private lateinit var viewModel: EditCloudViewModel
    private lateinit var editCloudFragmentListener: EditCloudFragmentListener

    private lateinit var captionedCloud: CaptionedCloud

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //arguments?.getByteArray(ARG_ENCODED_BITMAP)?.let { cloudEncodedBitmap = it }
        //arguments?.getString(ARG_URL)?.let { cloudUrl = it }
        arguments?.getParcelable<CaptionedCloud>(ARG_CAPTIONED_CLOUD)?.let { captionedCloud = it }

        editCloudFragmentListener = context as EditCloudFragmentListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_cloud_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this).get(EditCloudViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        (activity as AppCompatActivity).supportActionBar?.title = "Caption A Cloud"
        editText.setText(captionedCloud.url)

        Glide.with(this)
            .asBitmap()
            .load(captionedCloud.byteArray)
            .into(edit_cloud_image_view)

        save_button.setOnClickListener {

            // trigger a save intent

            captionedCloud.caption = editText.text.toString()
            viewModel.insert(captionedCloud)
            editCloudFragmentListener.onCloudSaved()
        }
    }

    interface EditCloudFragmentListener {
        fun onCloudSaved()
    }

    companion object {
        fun newInstance(cloud: CaptionedCloud) = EditCloudFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CAPTIONED_CLOUD, cloud)
            }
        }
    }
}
