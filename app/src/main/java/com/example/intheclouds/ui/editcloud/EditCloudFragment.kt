package com.example.intheclouds.ui.editcloud

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.intheclouds.R
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.ui.editcloud.state.EditCloudStateEvent
import com.example.intheclouds.ui.editcloud.state.EditCloudStateEvent.SaveCaptionedCloud
import com.example.intheclouds.util.Constants
import kotlinx.android.synthetic.main.edit_cloud_fragment.*

class EditCloudFragment : Fragment() {

    private lateinit var viewModel: EditCloudViewModel
    lateinit var dataStateHandler: DataStateListener

    private lateinit var captionedCloud: CaptionedCloud
    private var isNewCloud: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG: ${e.message}")
        }

        arguments?.getParcelable<CaptionedCloud>(Constants.ARG_CAPTIONED_CLOUD)?.let { captionedCloud = it }
        isNewCloud = captionedCloud.id == 0L
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_cloud_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        requireActivity().title = "Edit Cloud Caption"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(EditCloudViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        Glide.with(this)
            .asBitmap()
            .load(captionedCloud.byteArray)
            .into(edit_cloud_image_view)

        captionedCloud.caption.let { editText.setText(it) }

        subscribeObservers()

        save_button.setOnClickListener {
            triggerSaveCaptionedCloudEvent()
        }

        editText.setOnEditorActionListener(object: TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                return when {
                    actionId == EditorInfo.IME_ACTION_DONE -> {
                        triggerSaveCaptionedCloudEvent()
                        true
                    }
                    else -> false
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (!isNewCloud) {
            inflater.inflate(R.menu.edit_cloud_menu, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_cloud -> {
                triggerDeleteCaptionedCloudEvent()
            }
        }
        return super.onOptionsItemSelected(item)
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
                    with(editCloudViewState) {
                        when {
                            isSaved -> findNavController().navigate(R.id.actionCaptionedCloud)
                            isDeleted -> findNavController().navigate(R.id.actionCaptionedCloud)
                        }
                    }
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
}
