package com.example.intheclouds.ui.choosecloud

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.intheclouds.R
import com.example.intheclouds.api.RetrofitBuilder
import com.example.intheclouds.model.Cumulus
import kotlinx.android.synthetic.main.choose_cloud_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseCloudFragment : Fragment() {

    companion object {
        fun newInstance() = ChooseCloudFragment()
    }

    private lateinit var viewModel: ChooseCloudViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.choose_cloud_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChooseCloudViewModel::class.java)
        // TODO: Use the ViewModel

        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = RecyclerView.VERTICAL
        cloudsRecyclerView.layoutManager = layoutManager

        loadCloudImages()
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
}
