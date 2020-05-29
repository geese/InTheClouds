package com.example.intheclouds.ui.captionedclouds

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
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudStateEvent
import com.example.intheclouds.ui.editcloud.EditCloudViewModel
import com.example.intheclouds.ui.main.MainActivity
import kotlinx.android.synthetic.main.captioned_clouds_fragment.*

class CaptionedCloudsFragment : Fragment(), CaptionedCloudsRecyclerAdapter.Interaction {

    override fun onItemSelected(cloud: CaptionedCloud) {
        triggerCloudClickedEvent(cloud)
    }

    private lateinit var captionedCloudsRecyclerAdapter: CaptionedCloudsRecyclerAdapter

    private lateinit var viewModel: CaptionedCloudsViewModel
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
        return inflater.inflate(R.layout.captioned_clouds_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(true)
        requireActivity().title = "In The Clouds"

        viewModel = activity?.run {
            ViewModelProvider(this).get(CaptionedCloudsViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        subscribeObservers()
        initRecyclerView()

        triggerLoadCloudsEvent()  // TODO:  not sure I like initiating the view state this way - is it not very MVI-ish?
                                    // todo (cont.) should the viewmodel drive the initial state?
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.captioned_cloud_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_cloud -> findNavController().navigate(R.id.actionChooseCloud)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRecyclerView() {
        captionedCloudsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            captionedCloudsRecyclerAdapter = CaptionedCloudsRecyclerAdapter(this@CaptionedCloudsFragment)
            adapter = captionedCloudsRecyclerAdapter
        }
    }

    private fun subscribeObservers() {

        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: $dataState")

            // dataStateHandler is the MainActivity implementing DataStateListener
            // to handle loading (progress bar) or error (showing message) or navigation (handling bundle)
            dataStateHandler.onDataStateChange(dataState)

            // Handle Data<T>
            dataState.data?.let { event ->
                event.getContentIfNotHandled()?.let { captionedCloudViewState ->
                    captionedCloudViewState.clouds?.let { clouds ->
                        viewModel.setCloudImagesListData(clouds)
                    }
                    captionedCloudViewState.cloudToEdit?.let {
                        findNavController().navigate(
                            R.id.actionEditCloud,
                            EditCloudViewModel.createArguments(it)
                        )
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

        // should only happen once, on database creation
        MainActivity.isSampleCloudInserted.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> {
                    println("DEBUG:: SAMPLE CLOUD INSERTED")
                    triggerLoadCloudsEvent()
                    MainActivity.setIsSampleCloudInserted(false)
                }
                false -> {}
            }
        })
    }

    fun triggerLoadCloudsEvent() {
        viewModel.setStateEvent(CaptionedCloudStateEvent.LoadCloudImages)
    }

    fun triggerCloudClickedEvent(cloud: CaptionedCloud) {
        viewModel.setStateEvent(CaptionedCloudStateEvent.ClickCloudImage(cloud))
    }
}
