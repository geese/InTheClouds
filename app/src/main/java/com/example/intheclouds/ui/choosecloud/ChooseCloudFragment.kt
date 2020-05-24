package com.example.intheclouds.ui.choosecloud

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.intheclouds.R
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import kotlinx.android.synthetic.main.choose_cloud_fragment.*
import java.lang.ClassCastException

class ChooseCloudFragment : Fragment(), CloudsRecyclerAdapter.Interaction {

    override fun onItemSelected(position: Int, item: Cumulus.CloudImage) {
        println("DEBUG: CLICKED :: position: $position, item: $item")
    }

    lateinit var viewModel: ChooseCloudViewModel

    lateinit var dataStateHandler: DataStateListener

    lateinit var cloudsRecyclerAdapter: CloudsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_cloud_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this).get(ChooseCloudViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        cloudsRecyclerView.layoutManager = LinearLayoutManager(context)

        subscribeObservers()
        initRecyclerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun initRecyclerView() {
        cloudsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            cloudsRecyclerAdapter = CloudsRecyclerAdapter(this@ChooseCloudFragment)
            adapter = cloudsRecyclerAdapter
        }
    }

    fun subscribeObservers() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            println("DEBUG: DataState: $dataState")

            // dataStateHandler is the MainActivity implementing DataStateListener
            // to handle loading (progress bar) or error (showing message)
            dataStateHandler.onDataStateChange(dataState)

            // Handle Data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { chooseCloudViewState ->
                    chooseCloudViewState.cloudImages?.let { clouds ->
                        // set CloudImages data
                        viewModel.setCloudImagesListData(clouds)
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.cloudImages?.let {
                println("DEBUG: Setting cloud images to RecyclerView: $it")
                cloudsRecyclerAdapter.submitList(it)
            }
        })
    }

    fun triggerLoadCloudsEvent() {
        viewModel.setStateEvent(ChooseCloudStateEvent.getCloudImages())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.choose_cloud_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_get_clouds -> triggerLoadCloudsEvent()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG: $context must implement DataStateListener")
        }
    }

    companion object {
        fun newInstance() = ChooseCloudFragment()
    }
}
