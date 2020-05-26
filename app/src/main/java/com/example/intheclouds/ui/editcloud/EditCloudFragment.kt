package com.example.intheclouds.ui.editcloud

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.example.intheclouds.R
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.ui.editcloud.state.EditCloudStateEvent
import com.example.intheclouds.ui.editcloud.state.EditCloudStateEvent.SaveCaptionedCloud
import kotlinx.android.synthetic.main.captioned_cloud_row_item.*
import kotlinx.android.synthetic.main.edit_cloud_fragment.*
import java.lang.ClassCastException

private const val ARG_CAPTIONED_CLOUD = "captioned_cloud"
private const val ARG_NEW_CLOUD = "new_cloud"

class EditCloudFragment : Fragment() {

    private lateinit var viewModel: EditCloudViewModel

    lateinit var dataStateHandler: DataStateListener
    private lateinit var editCloudFragmentListener: EditCloudFragmentListener

    private lateinit var captionedCloud: CaptionedCloud
    private var newCloud: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
            editCloudFragmentListener = context as EditCloudFragmentListener
        } catch (e: ClassCastException) {
            println("DEBUG: ${e.message}")
        }

        arguments?.getParcelable<CaptionedCloud>(ARG_CAPTIONED_CLOUD)?.let { captionedCloud = it }
        arguments?.getBoolean(ARG_NEW_CLOUD)?.let { newCloud = it }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_cloud_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (!newCloud) {
            inflater.inflate(R.menu.edit_cloud_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_cloud -> {
                triggerDeleteCaptionedCloudEvent()
                requireActivity().supportFragmentManager.popBackStackImmediate()
            }
            android.R.id.home -> editCloudFragmentListener.goHome()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().title = "Edit Cloud Caption"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(EditCloudViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        Glide.with(this)
            .asBitmap()
            .load(captionedCloud.byteArray)
            .into(edit_cloud_image_view)

        captionedCloud.caption?.let { editText.setText(it) }

        subscribeObservers()

        save_button.setOnClickListener {
            triggerSaveCaptionedCloudEvent()
        }
    }

    fun subscribeObservers() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: $dataState")

            // dataStateHandler is the MainActivity implementing DataStateListener
            // to handle loading (progress bar) or error (showing message)
            dataStateHandler.onDataStateChange(dataState)

            // Handle Data<T>
            dataState.data?.let { event->
                event.getContentIfNotHandled()?.let { editCloudViewState ->
                    editCloudViewState.savedCloudId?.let { editCloudFragmentListener.onCloudSaved() }
                    editCloudViewState.deletedCloudId?.let { editCloudFragmentListener.onCloudDeleted() }
                }
            }
        })
    }

    private fun triggerSaveCaptionedCloudEvent() {
        captionedCloud.caption = editText.text.toString()
        viewModel.setStateEvent(SaveCaptionedCloud(captionedCloud))
    }

    private fun triggerDeleteCaptionedCloudEvent() {
        viewModel.setStateEvent(EditCloudStateEvent.DeleteCaptionedCloud(captionedCloud))
    }

    interface EditCloudFragmentListener {
        fun onCloudSaved()
        fun onCloudDeleted()
        fun goHome()
    }
    companion object {
        fun newInstance(cloud: CaptionedCloud, newCloud: Boolean) = EditCloudFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CAPTIONED_CLOUD, cloud)
                putBoolean(ARG_NEW_CLOUD, newCloud)
            }
        }
    }
}
