package com.example.intheclouds.ui.addeditcloud

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Observer
import com.example.intheclouds.R
import com.example.intheclouds.api.RetrofitBuilder
import com.example.intheclouds.model.CumulusResponse
import com.example.intheclouds.repository.Repository
import com.example.intheclouds.util.Constants
import kotlinx.android.synthetic.main.activity_add_edit_cloud.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddEditCloudActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_cloud)

        button.setOnClickListener {
            var response = RetrofitBuilder.apiService.getCumulusPhotos()
            print("yo yo: $response")

        }
    }


/*response.enqueue(object: Callback<CumulusResponse.Response>{
                override fun onFailure(call: Call<CumulusResponse.Response>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<CumulusResponse.Response>,
                    response: Response<CumulusResponse.Response>
                ) {
                    TODO("Not yet implemented")
                }
            })*/

}
