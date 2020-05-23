package com.example.intheclouds.ui.choosecloud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.intheclouds.R
import com.example.intheclouds.api.RetrofitBuilder
import com.example.intheclouds.model.Cumulus
import kotlinx.android.synthetic.main.activity_edit_cloud.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "ChooseCloud"

class ChooseCloudActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cloud)

        var layoutManager = LinearLayoutManager(this)
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
