package com.example.intheclouds.ui.choosecloud

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.intheclouds.R
import com.example.intheclouds.api.RetrofitBuilder
import com.example.intheclouds.model.Cumulus
import com.example.intheclouds.ui.choosecloud.state.ChooseCloudStateEvent
import kotlinx.android.synthetic.main.choose_cloud_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseCloudFragment : Fragment() {

    private lateinit var viewModel: ChooseCloudViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_cloud_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChooseCloudViewModel::class.java)
        // TODO: Use the ViewModel

        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        cloudsRecyclerView.layoutManager = layoutManager

        subscribeObservers()

        //loadCloudImages()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            println("DEBUG: DataState: $dataState")
            dataState.cloudImages?.let { clouds ->
                // set CloudImages data
                viewModel.setCloudImagesListData(clouds)
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.cloudImages?.let {
                println("DEBUG: Setting cloud images to RecyclerView: $it")
            }
        })
    }

    fun triggerLoadCloudsEvent() {
        viewModel.setStateEvent(ChooseCloudStateEvent.getCloudImages())
    }

    fun loadCloudImages() {

        RetrofitBuilder.getCumulusPhotos().enqueue(object: Callback<Cumulus.Response> {

            override fun onFailure(call: Call<Cumulus.Response>, t: Throwable) {
                //TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<Cumulus.Response>,
                response: Response<Cumulus.Response>?
            ) {
                response?.let { cloudsResponse ->
                    if (cloudsResponse.isSuccessful) {
                        val body = cloudsResponse.body()
                        body?.let {
                            if (cloudsRecyclerView.adapter == null) {
                                cloudsRecyclerView.adapter = CloudsAdapter(body.cloudImages as ArrayList<Cumulus.CloudImage>)
                            }
                        }
                    }
                }
            }
        })
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

    companion object {
        fun newInstance() = ChooseCloudFragment()
    }
}
