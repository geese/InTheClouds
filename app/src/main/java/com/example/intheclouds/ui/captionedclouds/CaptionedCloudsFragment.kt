package com.example.intheclouds.ui.captionedclouds

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.intheclouds.R
import com.example.intheclouds.room.CaptionedCloud
import com.example.intheclouds.ui.captionedclouds.state.CaptionedCloudStateEvent
import com.example.intheclouds.ui.choosecloud.ChooseCloudFragment
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import kotlinx.android.synthetic.main.captioned_clouds_fragment.*
import java.lang.ClassCastException

class CaptionedCloudsFragment : Fragment(), CaptionedCloudsRecyclerAdapter.Interaction {

    override fun onItemSelected(cloud: CaptionedCloud) {
        triggerCloudClickedEvent(cloud)
    }

    private lateinit var viewModel: CaptionedCloudsViewModel

    lateinit var captionedCloudsFragmentListener: CaptionedCloudsFragmentListener

    private lateinit var captionedCloudsRecyclerAdapter: CaptionedCloudsRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.captioned_clouds_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        requireActivity().title = "In The Clouds"
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewModel = activity?.run {
            ViewModelProvider(this).get(CaptionedCloudsViewModel::class.java)
        }?: throw Exception("Invalid Activity")

        captionedCloudsRecyclerView.layoutManager = LinearLayoutManager(context)

        subscribeObservers()
        initRecyclerView()
        triggerLoadCloudsEvent()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            captionedCloudsFragmentListener = context as CaptionedCloudsFragmentListener
        } catch (e: ClassCastException) {
            println("DEBUG: ${e.message}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.captioned_cloud_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_add_cloud -> captionedCloudsFragmentListener.onAddCloudClicked()
        }
        return super.onOptionsItemSelected(item)
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
                    captionedCloudViewState.cloudToEdit?.let {
                        captionedCloudsFragmentListener.onCloudClicked(it, newCloud = false)
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

    fun triggerCloudClickedEvent(cloud: CaptionedCloud) {
        viewModel.setStateEvent(CaptionedCloudStateEvent.clickCloudImage(cloud))
    }

    interface CaptionedCloudsFragmentListener {
        fun onAddCloudClicked()
        fun onCloudClicked(cloud: CaptionedCloud?, newCloud: Boolean)
    }

    companion object {
        fun newInstance() = CaptionedCloudsFragment()
    }
}
