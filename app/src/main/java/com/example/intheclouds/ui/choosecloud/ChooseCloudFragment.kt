package com.example.intheclouds.ui.choosecloud

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intheclouds.R
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.ui.DataStateListener
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import com.example.intheclouds.ui.editcloud.EditCloudViewModel
import kotlinx.android.synthetic.main.choose_cloud_fragment.*

class ChooseCloudFragment : Fragment(), ChooseCloudsRecyclerAdapter.Interaction {

    override fun onItemSelected(cloud: CaptionedCloud) {
        triggerCloudClickedEvent(cloud)
    }

    lateinit var chooseCloudsRecyclerAdapter: ChooseCloudsRecyclerAdapter

    lateinit var viewModel: ChooseCloudViewModel
    lateinit var dataStateHandler: DataStateListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataStateHandler = context as DataStateListener
        } catch (e: ClassCastException) {
            println("DEBUG: ${e.message}")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_cloud_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        requireActivity().title = "Choose A Cloud"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = activity?.run {
            ViewModelProvider(this).get(ChooseCloudViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        subscribeObservers()
        initRecyclerView()

        if (viewModel.viewState.value?.cloudImages == null) {
            triggerLoadCloudsEvent()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.choose_cloud_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_get_clouds -> triggerLoadCloudsEvent()
            android.R.id.home -> requireActivity().supportFragmentManager.popBackStackImmediate()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        cloudsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            chooseCloudsRecyclerAdapter = ChooseCloudsRecyclerAdapter(this@ChooseCloudFragment)
            adapter = chooseCloudsRecyclerAdapter
        }
    }

    fun subscribeObservers() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->

            println("DEBUG: DataState: $dataState")

            // dataStateHandler is the MainActivity implementing DataStateListener
            // to handle loading (progress bar) or error (showing message) or navigation (handling bundle)
            dataStateHandler.onDataStateChange(dataState)

            // Handle Data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { chooseCloudViewState ->
                    with(chooseCloudViewState) {
                        cloudImages?.let { clouds ->
                            // set CloudImages data
                            viewModel.setCloudImagesListData(clouds)
                        }
                        cloudToEdit?.let {
                            findNavController().navigate(
                                R.id.actionEditCloud,
                                EditCloudViewModel.createArguments(it)
                            )
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.cloudImages?.let {
                println("DEBUG: Setting cloud images to RecyclerView: $it")
                chooseCloudsRecyclerAdapter.submitList(it)
            }
        })
    }

    fun triggerLoadCloudsEvent() {
        viewModel.setStateEvent(ChooseCloudStateEvent.LoadCloudImages)
    }

    fun triggerCloudClickedEvent(cloud: CaptionedCloud) {
        viewModel.setStateEvent(ChooseCloudStateEvent.ClickCloudImage(cloud))
    }
}
