package com.example.intheclouds.ui.captionedclouds

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.intheclouds.R
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudStateEvent
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import kotlinx.android.synthetic.main.captioned_clouds_fragment.*

class CaptionedCloudsFragment : Fragment(), CaptionedCloudsRecyclerAdapter.Interaction {

    override fun onItemSelected() {

    }

    private lateinit var viewModel: CaptionedCloudsViewModel

    private lateinit var captionedCloudsRecyclerAdapter: CaptionedCloudsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.captioned_clouds_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this).get(CaptionedCloudsViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        captionedCloudsRecyclerView.layoutManager = LinearLayoutManager(context)

        subscribeObservers()
        initRecyclerView()
        triggerLoadCloudsEvent()
    }

    private fun subscribeObservers() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: $dataState")

            // Handle Data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { captionedCloudViewState ->
                    captionedCloudViewState.clouds?.let { clouds ->
                        viewModel.setCloudImagesListData(clouds)
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer {viewState ->
            viewState.clouds?.let {
                println("DEBUG: Setting cloud images to RecyclerView: $it")
                captionedCloudsRecyclerAdapter.submitList(it)
            }

        })

       /* viewModel.allCaptionedClouds.observe(viewLifecycleOwner, Observer { clouds ->
            clouds?.let {
                captionedCloudsRecyclerAdapter.submitList(clouds)
            }
        })*/
    }

    private fun initRecyclerView() {
        captionedCloudsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            captionedCloudsRecyclerAdapter = CaptionedCloudsRecyclerAdapter(this@CaptionedCloudsFragment)
            adapter = captionedCloudsRecyclerAdapter
        }
    }

    fun triggerLoadCloudsEvent() {
        viewModel.setStateEvent(CaptionedCloudStateEvent.loadCloudImages())
    }

    companion object {
        fun newInstance() = CaptionedCloudsFragment()
    }
}
